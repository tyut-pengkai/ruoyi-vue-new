package com.ruoyi.api.v1.service;

import com.alibaba.fastjson.JSON;
import com.ruoyi.api.v1.domain.AppLoginUser;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.ErrorCode;
import com.ruoyi.common.enums.UserStatus;
import com.ruoyi.common.exception.ApiException;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.system.domain.SysApp;
import com.ruoyi.system.domain.SysAppUser;
import com.ruoyi.system.domain.SysAppVersion;
import com.ruoyi.system.service.ISysAppService;
import com.ruoyi.system.service.ISysAppUserService;
import com.ruoyi.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class SysAppLoginService {
    @Autowired
    private AppTokenService tokenService;

    @Resource
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysAppService appService;

    @Autowired
    private ISysAppUserService appUserService;


    /**
     * App登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @param code     验证码
     * @param uuid     唯一标识
     * @return 结果
     */
    public String appLogin(String username, String password, SysApp app, SysAppVersion appVersion, String deviceCode) {
        // 用户验证
        Authentication authentication = null;
        SysAppUser appUser = null;
        String appName = app.getAppName();
        String appVersionStr = appVersion.getVersionName() + "(" + appVersion.getVersionNo() + ")";
        SysUser user = userService.selectUserByUserName(username);
        try {
            if (StringUtils.isNull(user)) {
                log.info("登录用户：{} 不存在.", username);
                throw new ApiException(ErrorCode.ERROR_ACCOUNT_NOT_EXIST, "账号：" + username + " 不存在");
            } else if (UserStatus.DELETED.getCode().equals(user.getDelFlag())) {
                log.info("登录用户：{} 已被删除.", username);
                throw new ApiException(ErrorCode.ERROR_ACCOUNT_NOT_EXIST, "账号：" + username + " 已被删除");
            } else if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
                log.info("登录用户：{} 已被停用.", username);
                throw new ApiException(ErrorCode.ERROR_ACCOUNT_LOCKED, "账号：" + username + " 已停用");
            } else if (!SecurityUtils.matchesPassword(password, user.getPassword())) {
                log.info("登录用户：{} 账号或密码错误.", username);
                throw new ApiException(ErrorCode.ERROR_USERNAME_OR_PASSWORD_ERROR, "账号或密码错误");
            }
            appUser = appUserService.selectSysAppUserByAppIdAndUserId(app.getAppId(), user.getUserId());
            if (appUser == null) { // 首次登录
                appUser = new SysAppUser();

            }

        } catch (ApiException e) {
            AsyncManager.me().execute(AsyncFactory.recordAppLogininfor(null, username, appName, appVersionStr, deviceCode, Constants.LOGIN_FAIL, e.getMessage() + "：" + e.getDetailMessage()));
            throw e;
        } catch (Exception e) {
            AsyncManager.me().execute(AsyncFactory.recordAppLogininfor(null, username, appName, appVersionStr, deviceCode, Constants.LOGIN_FAIL, e.getMessage()));
            throw e;
        }
        AsyncManager.me().execute(AsyncFactory.recordAppLogininfor(appUser.getAppUserId(), username, appName, appVersionStr, deviceCode, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        AppLoginUser loginUser = new AppLoginUser();
        loginUser.setApp(app);
        loginUser.setUser(user);
        loginUser.setAppUser(appUser);
        loginUser.setAppVersion(appVersion);
        loginUser.setDeviceCode(null);

        recordLoginInfo(loginUser);

        System.out.println(JSON.toJSONString(loginUser));


//
//        // 生成token
//        return tokenService.createToken(loginUser);
        return null;
    }

//    /**
//     * 校验验证码
//     *
//     * @param username 用户名
//     * @param code 验证码
//     * @param uuid 唯一标识
//     * @return 结果
//     */
//    public void validateCaptcha(String username, String code, String uuid)
//    {
//        String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid;
//        String captcha = redisCache.getCacheObject(verifyKey);
//        redisCache.deleteObject(verifyKey);
//        if (captcha == null)
//        {
//            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire")));
//            throw new CaptchaExpireException();
//        }
//        if (!code.equalsIgnoreCase(captcha))
//        {
//            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
//            throw new CaptchaException();
//        }
//    }

    /**
     * 记录登录信息
     */
    public void recordLoginInfo(AppLoginUser loginUser) {
//        SysUser sysUser = new SysUser();
//        sysUser.setUserId(userId);
//        sysUser.setLoginIp(IpUtils.getIpAddr(ServletUtils.getRequest()));
//        sysUser.setLoginDate(DateUtils.getNowDate());
//        userService.updateUserProfile(sysUser);
    }
}
