package com.ruoyi.framework.web.service;

import javax.annotation.Resource;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.pojo.dos.WxMiniAppLoginResponseDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.exception.user.BlackListException;
import com.ruoyi.common.exception.user.CaptchaException;
import com.ruoyi.common.exception.user.CaptchaExpireException;
import com.ruoyi.common.exception.user.UserNotExistsException;
import com.ruoyi.common.exception.user.UserPasswordNotMatchException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.framework.security.context.AuthenticationContextHolder;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysUserService;
import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.ruoyi.common.core.domain.model.WxLoginBody;
import com.ruoyi.common.core.domain.model.WxLoginResponse;

/**
 * 登录校验方法
 * 
 * @author ruoyi
 */
@Component
public class SysLoginService
{

    private static final Logger log = LoggerFactory.getLogger(SysLoginService.class);
//    //微信小程序appId
//    @Value("${wx.minApp.appId}")
//    private String appId;
//
//    //微信小程序密钥
//    @Value("${wx.minApp.appSecret}")
//    private String appSecret;

    @Autowired
    private TokenService tokenService;

    @Resource
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;
    
    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysConfigService configService;

    @Resource
    private WxMaService wxMaService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    /**
     * 登录验证
     * 
     * @param username 用户名
     * @param password 密码
     * @param code 验证码
     * @param uuid 唯一标识
     * @return 结果
     */
    public String login(String username, String password, String code, String uuid)
    {
        // 验证码校验
        validateCaptcha(username, code, uuid);
        // 登录前置校验
        loginPreCheck(username, password);
        // 用户验证
        Authentication authentication = null;
        try
        {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            AuthenticationContextHolder.setContext(authenticationToken);
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager.authenticate(authenticationToken);
        }
        catch (Exception e)
        {
            if (e instanceof BadCredentialsException)
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                throw new UserPasswordNotMatchException();
            }
            else
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage()));
                throw new ServiceException(e.getMessage());
            }
        }
        finally
        {
            AuthenticationContextHolder.clearContext();
        }
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        recordLoginInfo(loginUser.getUserId());
        // 生成token
        return tokenService.createToken(loginUser);
    }

    /**
     * 校验验证码
     * 
     * @param username 用户名
     * @param code 验证码
     * @param uuid 唯一标识
     * @return 结果
     */
    public void validateCaptcha(String username, String code, String uuid)
    {
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        if (captchaEnabled)
        {
            String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
            String captcha = redisCache.getCacheObject(verifyKey);
            if (captcha == null)
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire")));
                throw new CaptchaExpireException();
            }
            redisCache.deleteObject(verifyKey);
            if (!code.equalsIgnoreCase(captcha))
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
                throw new CaptchaException();
            }
        }
    }

    /**
     * 登录前置校验
     * @param username 用户名
     * @param password 用户密码
     */
    public void loginPreCheck(String username, String password)
    {
        // 用户名或密码为空 错误
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password))
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("not.null")));
            throw new UserNotExistsException();
        }
        // 密码如果不在指定范围内 错误
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH)
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
            throw new UserPasswordNotMatchException();
        }
        // 用户名不在指定范围内 错误
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH)
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
            throw new UserPasswordNotMatchException();
        }
        // IP黑名单校验
        String blackStr = configService.selectConfigByKey("sys.login.blackIPList");
        if (IpUtils.isMatchedIp(blackStr, IpUtils.getIpAddr()))
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("login.blocked")));
            throw new BlackListException();
        }
    }

    /**
     * 记录登录信息
     *
     * @param userId 用户ID
     */
    public void recordLoginInfo(Long userId)
    {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setLoginIp(IpUtils.getIpAddr());
        sysUser.setLoginDate(DateUtils.getNowDate());
        userService.updateUserProfile(sysUser);
    }

    /**
     * 小程序一键登录（含微信昵称头像和手机号）
     * @param wxLoginBody 微信小程序登录参数对象
     * @return token
     */
    public String miniProgramLogin(WxLoginBody wxLoginBody) {
        try {
            // 1. code换session
            WxMaJscode2SessionResult session = wxMaService.getUserService().getSessionInfo(wxLoginBody.getCode());
            String openId = session.getOpenid();
            String unionId = session.getUnionid();
            String sessionKey = session.getSessionKey();

            // 2. 解密用户信息（昵称、头像、性别等）
            String nickName = "微信用户";
            String avatarUrl = "";
            String gender = "0";
            
            // 如果有用户信息，则解密并获取
            if (wxLoginBody.getEncryptedData() != null && wxLoginBody.getIv() != null) {
                try {
                    // 校验签名
                    boolean check = wxMaService.getUserService().checkUserInfo(sessionKey, wxLoginBody.getRawData(), wxLoginBody.getSignature());
                    if (check) {
                        WxMaUserInfo wxUserInfo = wxMaService.getUserService().getUserInfo(sessionKey, wxLoginBody.getEncryptedData(), wxLoginBody.getIv());
                        nickName = processNickName(wxUserInfo.getNickName());
                        avatarUrl = wxUserInfo.getAvatarUrl();
                        gender = wxUserInfo.getGender(); // 0未知 1男 2女
                        log.info("成功获取微信用户信息，昵称: {}, 头像: {}, 性别: {}", nickName, avatarUrl, gender);
                    } else {
                        log.warn("微信用户信息签名校验失败，使用默认值");
                    }
                } catch (Exception e) {
                    log.warn("微信用户信息解密失败: {}, 使用默认值", e.getMessage());
                }
            } else {
                log.info("未提供微信用户信息，使用默认值");
            }

            // 3. 解密手机号
            String phoneNumber = null;
            if (wxLoginBody.getPhoneEncryptedData() != null && wxLoginBody.getPhoneIv() != null) {
                try {
                    WxMaPhoneNumberInfo phoneInfo = wxMaService.getUserService().getPhoneNoInfo(sessionKey, wxLoginBody.getPhoneEncryptedData(), wxLoginBody.getPhoneIv());
                    if (phoneInfo != null) {
                        phoneNumber = phoneInfo.getPhoneNumber();
                        log.info("成功获取手机号: {}", phoneNumber);
                    }
                } catch (Exception e) {
                    log.error("手机号解密失败: {}", e.getMessage());
                    throw new ServiceException("手机号解密失败: " + e.getMessage());
                }
            }

            // 4. 查询用户是否存在，如果不存在则创建，如果存在则更新
            SysUser user = userService.getUserByOpenId(openId);
            if (user == null) {
                // 创建新用户
                user = new SysUser();
                user.setOpenId(openId);
                user.setUnionId(unionId);
                user.setNickName(nickName);
                user.setAvatar(avatarUrl);
                user.setSex(gender);
                user.setPhonenumber(phoneNumber);
                // 生成短用户名，确保不超过30个字符
                String shortUuid = java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 8);
                user.setUserName("wx_" + shortUuid);
                // 设置默认部门ID，避免null值
                user.setDeptId(103L);
                user.setPassword(SecurityUtils.encryptPassword(configService.selectConfigByKey("sys.user.initPassword")));
                userService.registerUser(user);
                log.info("创建新用户，openId: {}, 昵称: {}, 头像: {}, 手机号: {}", openId, nickName, avatarUrl, phoneNumber);
            } else {
                // 更新现有用户信息
                user.setUnionId(unionId);
                
                // 更新昵称（如果微信昵称不为空）
                if (StringUtils.isNotEmpty(nickName)) {
                    user.setNickName(nickName);
                    log.info("更新用户昵称: {}", nickName);
                }
                
                // 更新头像（如果微信头像不为空）
                if (StringUtils.isNotEmpty(avatarUrl)) {
                    user.setAvatar(avatarUrl);
                    log.info("更新用户头像: {}", avatarUrl);
                }
                
                // 更新性别（如果微信性别不为空）
                if (StringUtils.isNotEmpty(gender)) {
                    user.setSex(gender);
                    log.info("更新用户性别: {}", gender);
                }
                
                // 更新手机号（如果微信手机号不为空）
                if (phoneNumber != null) {
                    user.setPhonenumber(phoneNumber);
                }
                
                userService.updateUser(user);
                log.info("更新用户信息，openId: {}, 昵称: {}, 头像: {}, 手机号: {}", openId, user.getNickName(), user.getAvatar(), phoneNumber);
            }

            // 5. 登录并生成token
            UserDetails userDetail = userDetailsService.createLoginUser(user);
            LoginUser loginUser = cn.hutool.core.bean.BeanUtil.copyProperties(userDetail, LoginUser.class);
            recordLoginInfo(loginUser.getUserId());
            return tokenService.createToken(loginUser);
        } catch (Exception e) {
            log.error("小程序登录失败: {}", e.getMessage(), e);
            throw new ServiceException("小程序登录失败: " + e.getMessage());
        }
    }

    /**
     * 小程序分步骤登录
     * @param wxLoginBody 微信小程序登录参数对象
     * @return 登录响应对象
     */
    public WxLoginResponse wxStepLogin(WxLoginBody wxLoginBody) {
        try {
            // 1. code换session
            WxMaJscode2SessionResult session = wxMaService.getUserService().getSessionInfo(wxLoginBody.getCode());
            String openId = session.getOpenid();
            String unionId = session.getUnionid();
            String sessionKey = session.getSessionKey();

            // 2. 查询用户是否存在
            SysUser user = userService.getUserByOpenId(openId);
            
            // 如果用户不存在，直接返回需要获取手机号的状态
            if (user == null) {
                log.info("新用户首次登录，需要获取手机号，openId: {}", openId);
                WxLoginResponse response = new WxLoginResponse(0, "新用户需要获取手机号");
                response.setOpenId(openId);
                response.setHasPhone(false);
                response.setUserInfo(null);
                response.setToken(null);
                return response;
            }
            
            // 3. 检查用户是否已绑定手机号
            boolean hasPhone = StringUtils.isNotEmpty(user.getPhonenumber());
            
            // 如果用户没有手机号，返回需要获取手机号的状态
            if (!hasPhone) {
                log.info("现有用户未绑定手机号，需要获取手机号，openId: {}", openId);
                WxLoginResponse response = new WxLoginResponse(0, "需要获取手机号");
                response.setOpenId(openId);
                response.setHasPhone(false);
                response.setUserInfo(user);
                response.setToken(null);
                return response;
            }
            
            // 4. 如果用户有手机号，检查昵称和头像是否为空，如果为空则尝试获取微信信息并更新
            if (StringUtils.isEmpty(user.getNickName()) || StringUtils.isEmpty(user.getAvatar())) {
                log.info("用户昵称或头像为空，检查是否有微信用户信息，openId: {}", openId);
                
                // 如果有微信用户信息，尝试补充
                if (wxLoginBody.getEncryptedData() != null && wxLoginBody.getIv() != null) {
                    supplementUserInfo(user, sessionKey, wxLoginBody, unionId);
                } else {
                    // 没有微信用户信息，返回需要完善用户信息的状态
                    log.info("用户信息不完整且未提供微信用户信息，需要完善用户信息，openId: {}", openId);
                    WxLoginResponse response = new WxLoginResponse(2, "需要完善用户信息");
                    response.setOpenId(openId);
                    response.setHasPhone(true);
                    response.setUserInfo(user);
                    response.setToken(null);
                    return response;
                }
            }
            
            // 5. 登录并生成token
            log.info("用户已绑定手机号，直接登录，openId: {}", openId);
            UserDetails userDetail = userDetailsService.createLoginUser(user);
            LoginUser loginUser = cn.hutool.core.bean.BeanUtil.copyProperties(userDetail, LoginUser.class);
            recordLoginInfo(loginUser.getUserId());
            String token = tokenService.createToken(loginUser);
            
            WxLoginResponse response = new WxLoginResponse(1, "登录成功");
            response.setToken(token);
            response.setOpenId(openId);
            response.setHasPhone(true);
            response.setUserInfo(user);
            return response;
            
        } catch (Exception e) {
            log.error("小程序分步骤登录失败: {}", e.getMessage(), e);
            throw new ServiceException("小程序登录失败: " + e.getMessage());
        }
    }

    /**
     * 小程序完善用户信息并登录
     * @param wxLoginBody 微信小程序登录参数对象
     * @return token
     */
    public String wxCompleteUserInfo(WxLoginBody wxLoginBody) {
        try {
            // 1. code换session
            WxMaJscode2SessionResult session = wxMaService.getUserService().getSessionInfo(wxLoginBody.getCode());
            String openId = session.getOpenid();
            String unionId = session.getUnionid();
            String sessionKey = session.getSessionKey();

            // 2. 解密用户信息（昵称、头像、性别等）
            String nickName = "微信用户";
            String avatarUrl = "";
            String gender = "0";
            
            // 如果有用户信息，则解密并获取
            if (wxLoginBody.getEncryptedData() != null && wxLoginBody.getIv() != null) {
                try {
                    // 校验签名
                    boolean check = wxMaService.getUserService().checkUserInfo(sessionKey, wxLoginBody.getRawData(), wxLoginBody.getSignature());
                    if (check) {
                        WxMaUserInfo wxUserInfo = wxMaService.getUserService().getUserInfo(sessionKey, wxLoginBody.getEncryptedData(), wxLoginBody.getIv());
                        nickName = processNickName(wxUserInfo.getNickName());
                        avatarUrl = wxUserInfo.getAvatarUrl();
                        gender = wxUserInfo.getGender(); // 0未知 1男 2女
                        log.info("成功获取微信用户信息，昵称: {}, 头像: {}, 性别: {}", nickName, avatarUrl, gender);
                    } else {
                        log.warn("微信用户信息签名校验失败，使用默认值");
                    }
                } catch (Exception e) {
                    log.warn("微信用户信息解密失败: {}, 使用默认值", e.getMessage());
                }
            } else {
                log.info("未提供微信用户信息，使用默认值");
            }

            // 3. 解密手机号（必需）
            String phoneNumber = null;
            if (wxLoginBody.getPhoneEncryptedData() != null && wxLoginBody.getPhoneIv() != null) {
                try {
                    WxMaPhoneNumberInfo phoneInfo = wxMaService.getUserService().getPhoneNoInfo(sessionKey, wxLoginBody.getPhoneEncryptedData(), wxLoginBody.getPhoneIv());
                    if (phoneInfo != null) {
                        phoneNumber = phoneInfo.getPhoneNumber();
                        log.info("成功获取手机号: {}", phoneNumber);
                    }
                } catch (Exception e) {
                    log.error("手机号解密失败: {}", e.getMessage());
                    throw new ServiceException("手机号解密失败: " + e.getMessage());
                }
            }
            
            // 验证手机号是否获取成功
            if (StringUtils.isEmpty(phoneNumber)) {
                throw new ServiceException("手机号获取失败，请重新授权");
            }

            // 4. 查询用户是否存在，如果不存在则创建，如果存在则更新
            SysUser user = userService.getUserByOpenId(openId);
            if (user == null) {
                // 创建新用户
                user = new SysUser();
                user.setOpenId(openId);
                user.setUnionId(unionId);
                user.setNickName(nickName);
                user.setAvatar(avatarUrl);
                user.setSex(gender);
                user.setPhonenumber(phoneNumber);
                // 生成短用户名，确保不超过30个字符
                String shortUuid = java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 8);
                user.setUserName("wx_" + shortUuid);
                // 设置默认部门ID，避免null值
                user.setDeptId(103L);
                user.setPassword(SecurityUtils.encryptPassword(configService.selectConfigByKey("sys.user.initPassword")));
                userService.registerUser(user);
                log.info("创建新用户，openId: {}, 昵称: {}, 头像: {}, 手机号: {}", openId, nickName, avatarUrl, phoneNumber);
            } else {
                // 更新现有用户信息
                user.setUnionId(unionId);
                
                // 更新昵称（如果微信昵称不为空）
                if (StringUtils.isNotEmpty(nickName)) {
                    user.setNickName(nickName);
                    log.info("更新用户昵称: {}", nickName);
                }
                
                // 更新头像（如果微信头像不为空）
                if (StringUtils.isNotEmpty(avatarUrl)) {
                    user.setAvatar(avatarUrl);
                    log.info("更新用户头像: {}", avatarUrl);
                }
                
                // 更新性别（如果微信性别不为空）
                if (StringUtils.isNotEmpty(gender)) {
                    user.setSex(gender);
                    log.info("更新用户性别: {}", gender);
                }
                
                // 更新手机号（如果微信手机号不为空）
                if (phoneNumber != null) {
                    user.setPhonenumber(phoneNumber);
                }
                
                userService.updateUser(user);
                log.info("更新用户信息，openId: {}, 昵称: {}, 头像: {}, 手机号: {}", openId, user.getNickName(), user.getAvatar(), phoneNumber);
            }

            // 5. 登录并生成token
            UserDetails userDetail = userDetailsService.createLoginUser(user);
            LoginUser loginUser = cn.hutool.core.bean.BeanUtil.copyProperties(userDetail, LoginUser.class);
            recordLoginInfo(loginUser.getUserId());
            return tokenService.createToken(loginUser);
        } catch (Exception e) {
            log.error("小程序完善用户信息失败: {}", e.getMessage(), e);
            throw new ServiceException("小程序完善用户信息失败: " + e.getMessage());
        }
    }

    /**
     * 生成随机微信昵称
     * @return 随机昵称
     */
    private String generateRandomNickName() {
        // 生成4位随机字符串，使用数字和字母组合，更简洁友好
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        java.util.Random random = new java.util.Random();
        for (int i = 0; i < 4; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return "微信用户" + sb.toString();
    }

    /**
     * 处理微信昵称，如果为空则生成随机昵称
     * @param originalNickName 原始微信昵称
     * @return 处理后的昵称
     */
    private String processNickName(String originalNickName) {
        if (StringUtils.isEmpty(originalNickName)) {
            String randomNickName = generateRandomNickName();
            log.info("微信昵称为空，生成随机昵称: {}", randomNickName);
            return randomNickName;
        } else {
            log.info("获取到微信昵称: {}", originalNickName);
            return originalNickName;
        }
    }

    /**
     * 补充更新用户微信信息（昵称、头像、性别）
     * @param user 用户对象
     * @param sessionKey 微信sessionKey
     * @param wxLoginBody 微信登录参数
     * @param unionId 微信unionId
     * @return 是否有信息更新
     */
    private boolean supplementUserInfo(SysUser user, String sessionKey, WxLoginBody wxLoginBody, String unionId) {
        boolean updated = false;
        
        // 检查是否需要补充用户信息
        if (StringUtils.isNotEmpty(user.getNickName()) && StringUtils.isNotEmpty(user.getAvatar()) && StringUtils.isNotEmpty(user.getSex())) {
            log.info("用户信息已完整，无需补充，openId: {}", user.getOpenId());
            return false;
        }
        
        // 如果有用户信息，则解密并获取
        if (wxLoginBody.getEncryptedData() != null && wxLoginBody.getIv() != null) {
            try {
                // 校验签名
                boolean check = wxMaService.getUserService().checkUserInfo(sessionKey, wxLoginBody.getRawData(), wxLoginBody.getSignature());
                if (check) {
                    WxMaUserInfo wxUserInfo = wxMaService.getUserService().getUserInfo(sessionKey, wxLoginBody.getEncryptedData(), wxLoginBody.getIv());
                    String nickName = processNickName(wxUserInfo.getNickName());
                    String avatarUrl = wxUserInfo.getAvatarUrl();
                    String gender = wxUserInfo.getGender();
                    
                    // 补充昵称
                    if (StringUtils.isEmpty(user.getNickName()) && StringUtils.isNotEmpty(nickName)) {
                        user.setNickName(nickName);
                        updated = true;
                        log.info("补充用户昵称: {}", nickName);
                    }
                    
                    // 补充头像
                    if (StringUtils.isEmpty(user.getAvatar()) && StringUtils.isNotEmpty(avatarUrl)) {
                        user.setAvatar(avatarUrl);
                        updated = true;
                        log.info("补充用户头像: {}", avatarUrl);
                    }
                    
                    // 补充性别
                    if (StringUtils.isEmpty(user.getSex()) && StringUtils.isNotEmpty(gender)) {
                        user.setSex(gender);
                        updated = true;
                        log.info("补充用户性别: {}", gender);
                    }
                    
                    // 更新unionId
                    if (StringUtils.isNotEmpty(unionId)) {
                        user.setUnionId(unionId);
                    }
                    
                    if (updated) {
                        userService.updateUser(user);
                        log.info("成功补充用户微信信息，openId: {}", user.getOpenId());
                    }
                } else {
                    log.warn("微信用户信息签名校验失败，无法补充用户信息");
                }
            } catch (Exception e) {
                log.warn("微信用户信息解密失败，无法补充用户信息: {}", e.getMessage());
            }
        } else {
            log.info("未提供微信用户信息，无法补充用户昵称和头像");
        }
        
        return updated;
    }
}
