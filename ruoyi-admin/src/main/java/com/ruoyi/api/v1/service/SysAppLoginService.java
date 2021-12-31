package com.ruoyi.api.v1.service;

import com.ruoyi.api.v1.utils.MyUtils;
import com.ruoyi.api.v1.utils.ValidUtils;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.BillType;
import com.ruoyi.common.enums.BindType;
import com.ruoyi.common.enums.ErrorCode;
import com.ruoyi.common.enums.UserStatus;
import com.ruoyi.common.exception.ApiException;
import com.ruoyi.common.utils.*;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.system.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

@Slf4j
@Component
public class SysAppLoginService {

    @Resource
    private TokenService tokenService;
    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private RedisCache redisCache;
    @Resource
    private ISysUserService userService;
    @Resource
    private ISysAppService appService;
    @Resource
    private ISysAppUserService appUserService;
    @Resource
    private ISysDeviceCodeService deviceCodeService;
    @Resource
    private ISysAppUserDeviceCodeService appUserDeviceCodeService;
    @Resource
    private ValidUtils validUtils;


    /**
     * App登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @return 结果
     */
    public String appLogin(String username, String password, SysApp app, SysAppVersion appVersion, String deviceCodeStr) {
        // 用户验证
        SysAppUser appUser = null;
        SysDeviceCode deviceCode = null;
        SysAppUserDeviceCode appUserDeviceCode = null;
        String appName = app.getAppName();
        String appVersionStr = appVersion.getVersionShow();
        SysUser user = userService.selectUserByUserName(username);
        try {
            if (StringUtils.isNull(user)) {
                log.info("登录账号：{} 不存在.", username);
                throw new ApiException(ErrorCode.ERROR_ACCOUNT_NOT_EXIST, "账号：" + username + " 不存在");
            } else if (UserStatus.DELETED.getCode().equals(user.getDelFlag())) {
                log.info("登录账号：{} 已被删除.", username);
                throw new ApiException(ErrorCode.ERROR_ACCOUNT_NOT_EXIST, "账号：" + username + " 已被删除");
            } else if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
                log.info("登录账号：{} 已被停用.", username);
                throw new ApiException(ErrorCode.ERROR_ACCOUNT_LOCKED, "账号：" + username + " 已停用");
            } else if (!SecurityUtils.matchesPassword(password, user.getPassword())) {
                log.info("登录账号：{} 账号或密码错误.", username);
                throw new ApiException(ErrorCode.ERROR_USERNAME_OR_PASSWORD_ERROR, "账号或密码错误");
            }
            appUser = appUserService.selectSysAppUserByAppIdAndUserId(app.getAppId(), user.getUserId());
            if (appUser == null) { // 首次登录
                appUser = new SysAppUser();
                appUser.setAppId(app.getAppId());
                appUser.setUserId(user.getUserId());
                appUser.setFreeBalance(BigDecimal.ZERO);
                appUser.setLastLoginTime(null);
                appUser.setLoginCode(null);
                appUser.setLoginLimitM(-1);
                appUser.setLoginLimitU(-1);
                appUser.setLoginTimes(0L);
                appUser.setPayBalance(BigDecimal.ZERO);
                appUser.setPwdErrorTimes(0);
                appUser.setStatus(UserConstants.NORMAL);
                appUser.setTotalPay(BigDecimal.ZERO);
                appUser.setUserName(user.getUserName());
                if (app.getBillType() == BillType.TIME) {
                    appUser.setExpireTime(MyUtils.getNewExpiredTime(null, app.getFreeQuotaReg()));
                    appUser.setPoint(null);
                } else if (app.getBillType() == BillType.POINT) {
                    appUser.setExpireTime(null);
                    appUser.setPoint(MyUtils.getNewPoint(null, app.getFreeQuotaReg()));
                } else {
                    throw new ApiException("软件计费方式有误");
                }
                appUserService.insertSysAppUser(appUser);
            } else {
                if (UserStatus.DISABLE.getCode().equals(appUser.getStatus())) {
                    log.info("登录用户：{} 已被停用.", username);
                    throw new ApiException(ErrorCode.ERROR_APPUSER_LOCKED, "用户：" + username + " 已停用");
                }
            }
            // 自动绑定设备码
            if (StringUtils.isNotBlank(deviceCodeStr)) {
                deviceCode = deviceCodeService.selectSysDeviceCodeByDeviceCode(deviceCodeStr);
                if (deviceCode == null) {
                    deviceCode = new SysDeviceCode();
                    deviceCode.setDeviceCode(deviceCodeStr);
                    deviceCode.setLoginTimes(0L);
                    deviceCode.setStatus(UserConstants.NORMAL);
                    deviceCode.setCreateBy(user.getUserName());
                    deviceCodeService.insertSysDeviceCode(deviceCode);
                } else {
                    if (UserStatus.DISABLE.getCode().equals(deviceCode.getStatus())) {
                        log.info("账号设备：{} 已被停用.", deviceCodeStr);
                        throw new ApiException(ErrorCode.ERROR_DEVICE_CODE_LOCKED, "账号设备：" + deviceCodeStr + " 已停用");
                    }
                }
                appUserDeviceCode = appUserDeviceCodeService.selectSysAppUserDeviceCodeByAppUserIdAndDeviceCodeId(appUser.getAppUserId(), deviceCode.getDeviceCodeId());
                if (appUserDeviceCode == null) {
                    // 检查绑定限制
                    if (app.getBindType() == BindType.ONE_TO_ONE) { // 账号与设备一对一
                        validUtils.checkMoreMachine(appUser.getAppUserId(), deviceCode.getDeviceCodeId());
                        validUtils.checkMoreUser(appUser.getAppUserId(), deviceCode.getDeviceCodeId());
                    } else if (app.getBindType() == BindType.MANY_TO_ONE) { // 账号与设备多对一
                        validUtils.checkMoreMachine(appUser.getAppUserId(), deviceCode.getDeviceCodeId());
                    } else if (app.getBindType() == BindType.ONE_TO_MANY) { // 账号与设备一对多
                        validUtils.checkMoreUser(appUser.getAppUserId(), deviceCode.getDeviceCodeId());
                    }
                    appUserDeviceCode = new SysAppUserDeviceCode();
                    appUserDeviceCode.setAppUserId(appUser.getAppUserId());
                    appUserDeviceCode.setCreateBy(user.getUserName());
                    appUserDeviceCode.setDeviceCodeId(deviceCode.getDeviceCodeId());
                    appUserDeviceCode.setLoginTimes(0L);
                    appUserDeviceCode.setStatus(UserConstants.NORMAL);
                    appUserDeviceCodeService.insertSysAppUserDeviceCode(appUserDeviceCode);
                } else {
                    if (UserStatus.DISABLE.getCode().equals(appUserDeviceCode.getStatus())) {
                        log.info("用户设备：{} 已被停用.", deviceCodeStr);
                        throw new ApiException(ErrorCode.ERROR_DEVICE_CODE_LOCKED, "用户设备：" + deviceCodeStr + " 已停用");
                    }
                }
            }
            // 检测账号是否过期或点数不足
            validUtils.checkAppUserIsExpired(app, appUser);
            // 检查用户数、设备数限制
            validUtils.checkLoginLimit(app, appUser, deviceCode);
        } catch (ApiException e) {
            AsyncManager.me().execute(AsyncFactory.recordAppLogininfor(appUser != null ? appUser.getAppUserId() : null, username, appName, appVersionStr, deviceCodeStr, Constants.LOGIN_FAIL, e.getMessage() + (e.getDetailMessage() != null ? "：" + e.getDetailMessage() : "")));
            throw e;
        } catch (Exception e) {
            AsyncManager.me().execute(AsyncFactory.recordAppLogininfor(appUser != null ? appUser.getAppUserId() : null, username, appName, appVersionStr, deviceCodeStr, Constants.LOGIN_FAIL, e.getMessage()));
            throw e;
        }
        AsyncManager.me().execute(AsyncFactory.recordAppLogininfor(appUser.getAppUserId(), username, appName, appVersionStr, deviceCodeStr, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        LoginUser loginUser = new LoginUser();
        loginUser.setIfApp(true);
        loginUser.setUserId(user.getUserId());
        loginUser.setApp(app);
        loginUser.setUser(user);
        loginUser.setAppUser(appUser);
        loginUser.setAppVersion(appVersion);
        loginUser.setDeviceCode(deviceCode);
        loginUser.setAppUserDeviceCode(appUserDeviceCode);

        recordLoginInfo(loginUser);
        // 生成token
        return tokenService.createToken(loginUser);
    }

    /**
     * 记录登录信息
     */
    public void recordLoginInfo(LoginUser loginUser) { // TODO
        Date nowDate = DateUtils.getNowDate();
        // 账号信息
        SysUser sysUser = new SysUser();
        sysUser.setUserId(loginUser.getUserId());
        sysUser.setLoginIp(IpUtils.getIpAddr(ServletUtils.getRequest()));
        sysUser.setLoginDate(nowDate);
        userService.updateUserProfile(sysUser);
        // 用户信息
        SysAppUser appUser = new SysAppUser();
        appUser.setAppUserId(loginUser.getAppUser().getAppUserId());
        appUser.setLoginTimes(loginUser.getAppUser().getLoginTimes() + 1);
        appUser.setLastLoginTime(nowDate);
        appUserService.updateSysAppUser(appUser);
        if (loginUser.getDeviceCode() != null) {
            // 设备码信息
            SysDeviceCode deviceCode = new SysDeviceCode();
            deviceCode.setDeviceCodeId(loginUser.getDeviceCode().getDeviceCodeId());
            deviceCode.setLoginTimes(loginUser.getDeviceCode().getLoginTimes() + 1);
            deviceCode.setLastLoginTime(nowDate);
            deviceCodeService.updateSysDeviceCode(deviceCode);
            // 用户设备码信息
            SysAppUserDeviceCode appUserDeviceCode = new SysAppUserDeviceCode();
            appUserDeviceCode.setId(loginUser.getAppUserDeviceCode().getId());
            appUserDeviceCode.setLoginTimes(loginUser.getAppUserDeviceCode().getLoginTimes() + 1);
            appUserDeviceCode.setLastLoginTime(nowDate);
            appUserDeviceCodeService.updateSysAppUserDeviceCode(appUserDeviceCode);
        }
    }
}
