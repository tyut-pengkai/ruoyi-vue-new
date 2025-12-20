package com.ruoyi.framework.web.service;

import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.UserConstants;
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
import com.ruoyi.common.core.context.TenantContext;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.framework.security.context.AuthenticationContextHolder;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.system.service.ISysTenantService;
import com.ruoyi.system.domain.SysTenant;

/**
 * 登录校验方法
 *
 * @author ruoyi
 */
@Component
public class SysLoginService
{
    private static final Logger log = LoggerFactory.getLogger(SysLoginService.class);
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

    @Autowired
    private ISysTenantService tenantService;

    /**
     * 登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @param code 验证码
     * @param uuid 唯一标识
     * @param tenantCode 租户编码
     * @return 结果
     */
    public String login(String username, String password, String code, String uuid, String tenantCode)
    {
        // 【多租户】登录阶段：临时忽略租户过滤
        // Reason: 登录时需要查询用户表获取tenant_id，形成先有鸡还是先有蛋的问题
        //         因此登录阶段临时设置全局模式，允许查询和更新用户表
        try
        {
            TenantContext.setIgnore(true);
            log.debug("【多租户】登录阶段：临时设置全局模式");

            // 验证码校验
            validateCaptcha(username, code, uuid);
            // 登录前置校验
            loginPreCheck(username, password);

            // 【多租户】通过租户编码查询租户ID并设置到上下文
            Long tenantId = null;
            if (StringUtils.isNotEmpty(tenantCode))
            {
                SysTenant tenant = tenantService.selectSysTenantByTenantCode(tenantCode);
                if (tenant == null)
                {
                    AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, "租户不存在或已停用"));
                    throw new ServiceException("租户不存在或已停用");
                }
                tenantId = tenant.getTenantId();
                // 将租户ID设置到上下文，供 UserDetailsServiceImpl 使用
                TenantContext.setTenantId(tenantId);
                log.info("【多租户】登录租户编码: {}, 租户ID: {}", tenantCode, tenantId);
            }

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

            // 【多租户】从SysUser获取tenant_id并设置到LoginUser
            if (loginUser.getUser() != null && loginUser.getUser().getTenantId() != null)
            {
                loginUser.setTenantId(loginUser.getUser().getTenantId());
                log.info("【多租户】用户 {} 登录成功，租户ID: {}", username, loginUser.getTenantId());
            }
            else if (!com.ruoyi.common.config.TenantConfig.isSuperAdmin(loginUser.getUserId()))
            {
                // 非超级管理员必须有租户ID
                log.error("用户 {} 未分配租户且非超级管理员，拒绝登录", username);
                throw new ServiceException("用户未分配租户，请联系管理员");
            }
            else
            {
                log.warn("【多租户】超级管理员 {} 登录，无需租户ID", username);
            }

            // 生成token
            return tokenService.createToken(loginUser);
        }
        finally
        {
            // 【多租户】登录完成，清除全局模式标记
            TenantContext.clear();
            log.debug("【多租户】登录流程结束，清除全局模式");
        }
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
        userService.updateLoginInfo(userId, IpUtils.getIpAddr(), DateUtils.getNowDate());
    }
}
