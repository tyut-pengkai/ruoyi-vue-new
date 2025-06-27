package com.ruoyi.framework.web.service;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.model.LoginCredential;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.domain.model.UserInfo;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.UserStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.exception.user.*;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.framework.sms.SmsClientWrapper;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 登录校验方法
 *
 * @author ruoyi
 */
@Slf4j
@Component
public class SysLoginService {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private SysPasswordService passwordService;

    @Autowired
    private SmsClientWrapper smsClient;

    /**
     * 登录验证
     *
     * @param loginCredential
     * @return
     */
    public String login(LoginCredential loginCredential) {
        LoginUser loginUser;
        switch (loginCredential.getLoginType()) {
            case USERNAME:
                loginUser = loginByUsername(loginCredential.getUsername(), loginCredential.getPassword(),
                        loginCredential.getImgVerificationCode(), loginCredential.getImgUuid());
                break;
            case SMS_VERIFICATION_CODE:
                loginUser = loginBySmsVerificationCode(loginCredential.getPhoneNumber(),
                        loginCredential.getSmsVerificationCode());
                break;
            default:
                throw new ServiceException("未知登录类型");
        }
        return createToken(loginUser);
    }

    /**
     * 登录成功，生成token
     *
     * @param loginUser
     * @return
     */
    public String createToken(LoginUser loginUser) {
        // 登录操作在返回token时即完成，无后续操作，所以不必设置认证上下文，如有必要需排除APP扫码登录的情况
//        UsernamePasswordAuthenticationToken authenticationToken =
//                new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
//        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        AsyncManager.me().execute(AsyncFactory.recordLogininfor(loginUser.getUsername(), Constants.LOGIN_SUCCESS,
                MessageUtils.message("user.login.success")));
        recordLoginInfo(loginUser.getUserId());
        // 生成token
        return tokenService.createToken(loginUser);
    }

    /**
     * 登录验证
     *
     * @param phoneNumber
     * @param smsCode
     * @return
     */
    private LoginUser loginBySmsVerificationCode(String phoneNumber, String smsCode) {
        // 验证码校验
        validateSmsVerificationCode(phoneNumber, smsCode);
        // 用户验证
        UserInfo user = userService.getUserByPhoneNumber(phoneNumber);
        loginUserInfoCheck(phoneNumber, user);
        return new LoginUser(user);
    }

    /**
     * 登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @param code     验证码
     * @param uuid     唯一标识
     * @return 结果
     */
    private LoginUser loginByUsername(String username, String password, String code, String uuid) {

        // 验证码校验
        validateCaptcha(username, code, uuid);

        // 登录前置校验
        loginPreCheck(username, password);

        // 用户验证
        UserInfo user = userService.getUserByUsername(username);
        loginUserInfoCheck(username, user);

        // 密码验证
        try {
            passwordService.validate(user, password);
        } catch (Exception e) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage()));
            throw new ServiceException(e.getMessage());
        }
        return new LoginUser(user);
    }

    private void loginUserInfoCheck(String username, UserInfo user) {
        if (StringUtils.isNull(user)) {
            log.info("登录用户：{} 不存在.", username);
            String msg = MessageUtils.message("user.not.exists");
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, msg));
            throw new ServiceException(msg);
        } else if (UserStatus.DELETED.getCode().equals(user.getDelFlag())) {
            log.info("登录用户：{} 已被删除.", username);
            String msg = MessageUtils.message("user.password.delete");
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, msg));
            throw new ServiceException(msg);
        } else if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            log.info("登录用户：{} 已被停用.", username);
            String msg = MessageUtils.message("user.blocked");
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, msg));
            throw new ServiceException(msg);
        }
    }

    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code     验证码
     * @param uuid     唯一标识
     * @return 结果
     */
    public void validateCaptcha(String username, String code, String uuid) {
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        if (captchaEnabled) {
            String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
            String captcha = redisCache.getCacheObject(verifyKey);
            if (captcha == null) {
                if (username != null) {
                    AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire")));
                }
                throw new CaptchaExpireException();
            }
            redisCache.deleteObject(verifyKey);
            if (!StrUtil.emptyIfNull(code).equalsIgnoreCase(captcha)) {
                if (username != null) {
                    AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
                }
                throw new CaptchaException();
            }
        }
    }

    /**
     * 登录前置校验
     *
     * @param username 用户名
     * @param password 用户密码
     */
    public void loginPreCheck(String username, String password) {
        // 用户名或密码为空 错误
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("not.null")));
            throw new UserNotExistsException();
        }
        // 密码如果不在指定范围内 错误
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
            throw new UserPasswordNotMatchException();
        }
        // 用户名不在指定范围内 错误
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
            throw new UserPasswordNotMatchException();
        }
        // IP黑名单校验
        String blackStr = configService.selectConfigByKey("sys.login.blackIPList");
        if (IpUtils.isMatchedIp(blackStr, IpUtils.getIpAddr())) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("login.blocked")));
            throw new BlackListException();
        }
    }

    /**
     * 记录登录信息
     *
     * @param userId 用户ID
     */
    public void recordLoginInfo(Long userId) {
        userService.updateLoginInfo(userId, IpUtils.getIpAddr(), DateUtils.getNowDate());
    }

    /**
     * 发送登录/注册短信验证码
     *
     * @param phoneNumber  电话号码
     * @param checkPicCode 校验图形验证码
     * @param code         图形验证码code
     * @param uuid         图形验证码uuid
     */
    public void sendSmsVerificationCode(String phoneNumber, boolean checkPicCode, String code, String uuid) {
        String k = CacheConstants.SMS_CAPTCHA_CODE_CD_PHONE_NUM_KEY + phoneNumber;
        String v = redisCache.getCacheObject(k);
        if (StrUtil.isNotEmpty(v)) {
            throw new ServiceException("验证码发送间隔需大于60S");
        }
        if (checkPicCode) {
            validateCaptcha(null, code, uuid);
        }
        sendSmsVerificationCode(phoneNumber);
        redisCache.setCacheObject(k, "1", 60, TimeUnit.SECONDS);
    }

    /**
     * 发送登录/注册短信验证码
     *
     * @param phoneNumber 电话号码
     */
    public void sendSmsVerificationCode(String phoneNumber) {
        String code = RandomUtil.randomNumbers(6);
        boolean success = smsClient.sendVerificationCode(phoneNumber, code);
        if (success) {
            String rk = CacheConstants.SMS_LOGIN_CAPTCHA_CODE_KEY + phoneNumber;
            redisCache.setCacheObject(rk, code, 5, TimeUnit.MINUTES);
        }
    }

    /**
     * 验证登录/注册短信验证码
     *
     * @param phoneNumber 电话号码
     * @param code        验证码
     * @return
     */
    public void validateSmsVerificationCode(String phoneNumber, String code) {
        String rk = CacheConstants.SMS_LOGIN_CAPTCHA_CODE_KEY + phoneNumber;
        String cacheCode = redisCache.getCacheObject(rk);
        if (cacheCode == null) {
            throw new CaptchaExpireException();
        }
        redisCache.deleteObject(rk);
        if (!StrUtil.equals(cacheCode, code)) {
            throw new CaptchaException();
        }
    }
}
