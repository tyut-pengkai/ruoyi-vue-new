package com.ruoyi.framework.api.v1.service;

import com.ruoyi.api.v1.utils.MyUtils;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.*;
import com.ruoyi.common.exception.ApiException;
import com.ruoyi.common.utils.*;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.framework.api.v1.utils.ValidUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.system.domain.SysAppUserExpireLog;
import com.ruoyi.system.domain.SysLoginCode;
import com.ruoyi.system.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class SysAppLoginService {

    @Resource
    private TokenService tokenService;
    @Resource
    private RedisCache redisCache;
    @Resource
    private ISysUserService userService;
    @Resource
    private ISysLoginCodeService loginCodeService;
    @Resource
    private ISysAppUserService appUserService;
    @Resource
    private ISysAppService appService;
    @Resource
    private ISysDeviceCodeService deviceCodeService;
    @Resource
    private ISysAppUserDeviceCodeService appUserDeviceCodeService;
    @Resource
    private ValidUtils validUtils;
    @Resource
    private ISysAppTrialUserService appTrialService;
    @Resource
    private ISysAppVersionService versionService;

    /**
     * App登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @return 结果
     */
    public String appLogin(String username, String password, SysApp app, SysAppVersion appVersion, String deviceCodeStr, boolean autoReducePoint) {
        // 检查在线人数限制
        validUtils.checkLicenseMaxOnline();
        // 检查IP黑名单
        validUtils.checkIpBlackList(username);
        // 用户验证
        SysAppUser appUser = null;
        SysDeviceCode deviceCode = null;
        SysAppUserDeviceCode appUserDeviceCode = null;
        String appName = app.getAppName();
        String appVersionStr = appVersion.getVersionShow();
        SysUser user = userService.selectUserByUserName(username);
        try {
            validUtils.checkUser(username, password, user);
            synchronized (username.intern()) {
                appUser = appUserService.selectSysAppUserByAppIdAndUserId(app.getAppId(), user.getUserId());
                if (appUser == null) { // 首次登录
                    appUser = new SysAppUser();
                    appUser.setAppId(app.getAppId());
                    appUser.setUserId(user.getUserId());
                    appUser.setLastLoginTime(null);
                    appUser.setLoginCode(null);
                    appUser.setLoginLimitM(-2);
                    appUser.setLoginLimitU(-2);
                    appUser.setLoginTimes(0L);
                    appUser.setPwdErrorTimes(0);
                    appUser.setStatus(UserConstants.NORMAL);
                    appUser.setFreeBalance(BigDecimal.ZERO);
                    appUser.setPayBalance(BigDecimal.ZERO);
                    appUser.setFreePayment(BigDecimal.ZERO);
                    appUser.setPayPayment(BigDecimal.ZERO);
                    appUser.setUnbindTimes(app.getUnbindTimes());
                    appUser.setApp(app);
                    SysAppUserExpireLog expireLog = new SysAppUserExpireLog();
                    if (app.getBillType() == BillType.TIME) {
                        Date newExpiredTime = MyUtils.getNewExpiredTimeAdd(null, app.getFreeQuotaReg());
                        appUser.setExpireTime(newExpiredTime);
                        appUser.setPoint(null);
                        expireLog.setExpireTimeBefore(null);
                        expireLog.setExpireTimeAfter(newExpiredTime);
                    } else if (app.getBillType() == BillType.POINT) {
                        appUser.setExpireTime(null);
                        BigDecimal newPoint = MyUtils.getNewPointAdd(null, app.getFreeQuotaReg());
                        appUser.setPoint(newPoint);
                        expireLog.setPointBefore(null);
                        expireLog.setPointAfter(newPoint);
                    } else {
                        throw new ApiException("软件计费方式有误");
                    }
                    appUserService.insertSysAppUser(appUser);
                    if (app.getFreeQuotaReg() > 0) {
                        // 记录用户时长变更日志
                        expireLog.setAppUserId(appUser.getAppUserId());
                        expireLog.setTemplateId(null);
                        expireLog.setCardId(null);
                        expireLog.setChangeDesc("用户首次登录赠送");
                        expireLog.setChangeType(AppUserExpireChangeType.APP_LOGIN);
                        expireLog.setChangeAmount(app.getFreeQuotaReg());
                        expireLog.setCardNo(null);
                        expireLog.setAppId(app.getAppId());
                        AsyncManager.me().execute(AsyncFactory.recordAppUserExpire(expireLog));
                    }
                } else {
                    if (UserStatus.DISABLE.getCode().equals(appUser.getStatus())) {
                        log.info("登录用户：{} 已被停用.", username);
                        throw new ApiException(ErrorCode.ERROR_APP_USER_LOCKED, "用户：" + username + " 已停用");
                    }
                }
                appUser.setUserName(user.getUserName());
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
                            log.info("用户设备：{} 已被停用所有软件.", deviceCodeStr);
                            throw new ApiException(ErrorCode.ERROR_DEVICE_CODE_LOCKED, "用户设备：" + deviceCodeStr + " 已被停用所有软件");
                        }
                    }
                    appUserDeviceCode = appUserDeviceCodeService.selectSysAppUserDeviceCodeByAppUserIdAndDeviceCodeId(appUser.getAppUserId(), deviceCode.getDeviceCodeId());
                    if (appUserDeviceCode == null) {
                        // 检查绑定限制
                        if (app.getBindType() == BindType.ONE_TO_ONE) { // 账号与设备一对一
                            validUtils.checkMoreMachine(appUser.getAppUserId(), deviceCode.getDeviceCodeId());
                            validUtils.checkMoreUser(appUser.getAppId(), appUser.getAppUserId(), deviceCode.getDeviceCodeId());
                        } else if (app.getBindType() == BindType.MANY_TO_ONE) { // 账号与设备多对一
                            validUtils.checkMoreMachine(appUser.getAppUserId(), deviceCode.getDeviceCodeId());
                        } else if (app.getBindType() == BindType.ONE_TO_MANY) { // 账号与设备一对多
                            validUtils.checkMoreUser(appUser.getAppId(), appUser.getAppUserId(), deviceCode.getDeviceCodeId());
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
                            log.info("用户设备：{} 已被停用当前软件.", deviceCodeStr);
                            throw new ApiException(ErrorCode.ERROR_DEVICE_CODE_LOCKED, "用户设备：" + deviceCodeStr + " 已被停用当前软件");
                        }
                    }
                }
                // 检测账号是否过期或点数不足
                validUtils.checkAppUserIsExpired(app, appUser, true);
                // 检查用户数、设备数限制
                validUtils.checkLoginLimit(app, appUser, deviceCode);
                try {
                    AsyncManager.me().execute(AsyncFactory.recordAppLogininfor(appUser.getAppUserId(), username, appName, appVersionStr, deviceCodeStr, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                LoginUser loginUser = new LoginUser();
                loginUser.setIfApp(true);
                loginUser.setIfTrial(false);
                loginUser.setUserId(user.getUserId());
                loginUser.setAppKey(app.getAppKey());
                loginUser.setAppId(app.getAppId());
                loginUser.setUser(null);
                loginUser.setAppUserId(appUser.getAppUserId());
                loginUser.setUserName(appUser.getUserName());
                loginUser.setAppVersionId(appVersion.getAppVersionId());
                if (deviceCode != null) {
                    loginUser.setDeviceCodeId(deviceCode.getDeviceCodeId());
                }
//                loginUser.setAppUserDeviceCode(appUserDeviceCode);

                recordLoginInfo(loginUser);

                // 自动扣除点数
                if (app.getBillType() == BillType.POINT &&
                        ((autoReducePoint && app.getLoginReducePointStrategy() == LoginReducePointStrategy.PARAMS) ||
                                app.getLoginReducePointStrategy() == LoginReducePointStrategy.ALWAYS)
                ) {
                    SysAppUserExpireLog expireLog = new SysAppUserExpireLog();
                    appUser = appUserService.selectSysAppUserByAppUserId(appUser.getAppUserId());
                    expireLog.setPointBefore(appUser.getPoint());
                    BigDecimal newPoint = appUser.getPoint().subtract(BigDecimal.valueOf(1));
                    appUser.setPoint(newPoint);
                    appUserService.updateSysAppUser(appUser);
                    expireLog.setPointAfter(newPoint);
                    // 记录用户时长变更日志
                    expireLog.setAppUserId(appUser.getAppUserId());
                    expireLog.setTemplateId(null);
                    expireLog.setCardId(null);
                    expireLog.setChangeDesc("软件登录自动扣点");
                    expireLog.setChangeType(AppUserExpireChangeType.APP_LOGIN);
                    expireLog.setChangeAmount(-1L);
                    expireLog.setCardNo(null);
                    expireLog.setAppId(appUser.getAppId());
                    AsyncManager.me().execute(AsyncFactory.recordAppUserExpire(expireLog));
                }

                // 生成token
                return tokenService.createToken(loginUser);
            }
        } catch (ApiException e) {
            try {
                AsyncManager.me().execute(AsyncFactory.recordAppLogininfor(appUser != null ? appUser.getAppUserId() : null, username, appName, appVersionStr, deviceCodeStr, Constants.LOGIN_FAIL, e.getMessage() + (e.getDetailMessage() != null ? "：" + e.getDetailMessage() : "")));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            throw e;
        } catch (Exception e) {
            try {
                AsyncManager.me().execute(AsyncFactory.recordAppLogininfor(appUser != null ? appUser.getAppUserId() : null, username, appName, appVersionStr, deviceCodeStr, Constants.LOGIN_FAIL, e.getMessage()));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            throw e;
        }
    }

    /**
     * App登录验证
     *
     * @param loginCodeStr 单码
     * @return 结果
     */
    public String appLogin(String loginCodeStr, SysApp app, SysAppVersion appVersion, String deviceCodeStr, boolean autoReducePoint) {
        // 检查在线人数限制
        validUtils.checkLicenseMaxOnline();
        // 检查IP黑名单
        validUtils.checkIpBlackList(loginCodeStr);
        // 用户验证
        SysAppUser appUser = null;
        SysDeviceCode deviceCode = null;
        SysAppUserDeviceCode appUserDeviceCode = null;
        String appName = app.getAppName();
        String appVersionStr = appVersion.getVersionShow();
        SysLoginCode loginCode = loginCodeService.selectSysLoginCodeByCardNo(loginCodeStr);
        String loginCodeShow = "[单码]" + loginCodeStr;
        try {
            validUtils.checkLoginCode(app, loginCodeStr, loginCode);
            synchronized (loginCodeStr.intern()) {
                appUser = appUserService.selectSysAppUserByAppIdAndLoginCode(app.getAppId(), loginCodeStr);
                if (appUser == null) { // 首次登录
                    validUtils.checkLoginCodeNewUser(app, loginCodeStr, loginCode);
                    appUser = new SysAppUser();
                    appUser.setAppId(app.getAppId());
                    appUser.setUserId(null);
                    appUser.setLastLoginTime(null);
                    appUser.setLoginCode(loginCodeStr);
                    appUser.setLoginLimitM(-2);
                    appUser.setLoginLimitU(-2);
                    appUser.setCardLoginLimitM(loginCode.getCardLoginLimitM());
                    appUser.setCardLoginLimitU(loginCode.getCardLoginLimitU());
                    appUser.setCardCustomParams(loginCode.getCardCustomParams());
                    appUser.setLoginTimes(0L);
                    appUser.setPwdErrorTimes(0);
                    appUser.setStatus(UserConstants.NORMAL);
                    appUser.setFreeBalance(BigDecimal.ZERO);
                    appUser.setPayBalance(BigDecimal.ZERO);
                    appUser.setFreePayment(BigDecimal.ZERO);
                    appUser.setPayPayment(BigDecimal.ZERO);
                    appUser.setUnbindTimes(app.getUnbindTimes());
                    appUser.setApp(app);
                    appUser.setLastChargeCardId(loginCode.getCardId());
                    appUser.setLastChargeTemplateId(loginCode.getTemplateId());
                    appUser.setAgentId(loginCode.getAgentId());
                    SysAppUserExpireLog expireLog1 = new SysAppUserExpireLog();
                    SysAppUserExpireLog expireLog2 = new SysAppUserExpireLog();
                    if (app.getBillType() == BillType.TIME) {
                        Date newExpiredTime = MyUtils.getNewExpiredTimeAdd(null, app.getFreeQuotaReg());
                        appUser.setExpireTime(newExpiredTime);
                        Date newExpiredTime2 = MyUtils.getNewExpiredTimeAdd(appUser.getExpireTime(), loginCode.getQuota());
                        appUser.setExpireTime(newExpiredTime2);
                        appUser.setPoint(null);
                        expireLog1.setExpireTimeBefore(null);
                        expireLog1.setExpireTimeAfter(newExpiredTime);
                        expireLog2.setExpireTimeBefore(newExpiredTime);
                        expireLog2.setExpireTimeAfter(newExpiredTime2);
                    } else if (app.getBillType() == BillType.POINT) {
                        appUser.setExpireTime(null);
                        BigDecimal newPoint = MyUtils.getNewPointAdd(null, app.getFreeQuotaReg());
                        appUser.setPoint(newPoint);
                        BigDecimal newPoint2 = MyUtils.getNewPointAdd(appUser.getPoint(), loginCode.getQuota());
                        appUser.setPoint(newPoint2);
                        expireLog1.setPointBefore(null);
                        expireLog1.setPointAfter(newPoint);
                        expireLog2.setPointBefore(newPoint);
                        expireLog2.setPointAfter(newPoint2);
                    } else {
                        throw new ApiException("软件计费方式有误");
                    }
                    appUserService.insertSysAppUser(appUser);
                    loginCode.setIsCharged(UserConstants.YES);
                    loginCode.setChargeTime(DateUtils.getNowDate());
                    loginCode.setOnSale(UserConstants.NO);
                    loginCode.setChargeType(ChargeType.LOGIN);
                    loginCode.setChargeTo(appUser.getAppUserId());
                    loginCodeService.updateSysLoginCode(loginCode);
                    if (app.getFreeQuotaReg() > 0) {
                        // 记录用户时长变更日志
                        expireLog1.setAppUserId(appUser.getAppUserId());
                        expireLog1.setTemplateId(null);
                        expireLog1.setCardId(null);
                        expireLog1.setChangeDesc("用户首次登录赠送");
                        expireLog1.setChangeType(AppUserExpireChangeType.APP_LOGIN);
                        expireLog1.setChangeAmount(app.getFreeQuotaReg());
                        expireLog1.setCardNo(null);
                        expireLog1.setAppId(app.getAppId());
                        AsyncManager.me().execute(AsyncFactory.recordAppUserExpire(expireLog1));
                    }
                    // 记录用户时长变更日志
                    expireLog2.setAppUserId(appUser.getAppUserId());
                    expireLog2.setTemplateId(loginCode.getTemplateId());
                    expireLog2.setCardId(loginCode.getCardId());
                    expireLog2.setChangeDesc("用户首次登录充值卡自动充值");
                    expireLog2.setChangeType(AppUserExpireChangeType.RECHARGE);
                    expireLog2.setChangeAmount(loginCode.getQuota());
                    expireLog2.setCardNo(loginCode.getCardNo());
                    expireLog2.setAppId(app.getAppId());
                    AsyncManager.me().execute(AsyncFactory.recordAppUserExpire(expireLog2));
                } else {
                    if (UserStatus.DISABLE.getCode().equals(appUser.getStatus())) {
                        log.info("登录用户：{} 已被停用.", loginCodeStr);
                        throw new ApiException(ErrorCode.ERROR_APP_USER_LOCKED, "用户：" + loginCodeStr + " 已停用");
                    }
                }
                appUser.setUserName(loginCodeShow);
                // 自动绑定设备码
                if (StringUtils.isNotBlank(deviceCodeStr)) {
                    deviceCode = deviceCodeService.selectSysDeviceCodeByDeviceCode(deviceCodeStr);
                    if (deviceCode == null) {
                        deviceCode = new SysDeviceCode();
                        deviceCode.setDeviceCode(deviceCodeStr);
                        deviceCode.setLoginTimes(0L);
                        deviceCode.setStatus(UserConstants.NORMAL);
                        deviceCode.setCreateBy(null);
                        deviceCodeService.insertSysDeviceCode(deviceCode);
                    } else {
                        if (UserStatus.DISABLE.getCode().equals(deviceCode.getStatus())) {
                            log.info("用户设备：{} 已被停用所有软件.", deviceCodeStr);
                            throw new ApiException(ErrorCode.ERROR_DEVICE_CODE_LOCKED, "用户设备：" + deviceCodeStr + " 已被停用所有软件");
                        }
                    }
                    appUserDeviceCode = appUserDeviceCodeService.selectSysAppUserDeviceCodeByAppUserIdAndDeviceCodeId(appUser.getAppUserId(), deviceCode.getDeviceCodeId());
                    if (appUserDeviceCode == null) {
                        // 检查绑定限制
                        if (app.getBindType() == BindType.ONE_TO_ONE) { // 账号与设备一对一
                            validUtils.checkMoreMachine(appUser.getAppUserId(), deviceCode.getDeviceCodeId());
                            validUtils.checkMoreUser(appUser.getAppId(), appUser.getAppUserId(), deviceCode.getDeviceCodeId());
                        } else if (app.getBindType() == BindType.MANY_TO_ONE) { // 账号与设备多对一
                            validUtils.checkMoreMachine(appUser.getAppUserId(), deviceCode.getDeviceCodeId());
                        } else if (app.getBindType() == BindType.ONE_TO_MANY) { // 账号与设备一对多
                            validUtils.checkMoreUser(appUser.getAppId(), appUser.getAppUserId(), deviceCode.getDeviceCodeId());
                        }
                        appUserDeviceCode = new SysAppUserDeviceCode();
                        appUserDeviceCode.setAppUserId(appUser.getAppUserId());
                        appUserDeviceCode.setCreateBy(null);
                        appUserDeviceCode.setDeviceCodeId(deviceCode.getDeviceCodeId());
                        appUserDeviceCode.setLoginTimes(0L);
                        appUserDeviceCode.setStatus(UserConstants.NORMAL);
                        appUserDeviceCodeService.insertSysAppUserDeviceCode(appUserDeviceCode);
                    } else {
                        if (UserStatus.DISABLE.getCode().equals(appUserDeviceCode.getStatus())) {
                            log.info("用户设备：{} 已被停用当前软件.", deviceCodeStr);
                            throw new ApiException(ErrorCode.ERROR_DEVICE_CODE_LOCKED, "用户设备：" + deviceCodeStr + " 已被停用当前软件");
                        }
                    }
                }
                // 检测账号是否过期或点数不足
                validUtils.checkAppUserIsExpired(app, appUser, true);
                // 检查用户数、设备数限制
                validUtils.checkLoginLimit(app, appUser, deviceCode);
                try {
                    AsyncManager.me().execute(AsyncFactory.recordAppLogininfor(appUser.getAppUserId(), loginCodeShow, appName, appVersionStr, deviceCodeStr, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                LoginUser loginUser = new LoginUser();
                loginUser.setIfApp(true);
                loginUser.setIfTrial(false);
                loginUser.setUserId(null);
                loginUser.setAppKey(app.getAppKey());
                loginUser.setAppId(app.getAppId());
                loginUser.setUser(null);
                loginUser.setAppUserId(appUser.getAppUserId());
                loginUser.setUserName(appUser.getUserName());
                loginUser.setAppVersionId(appVersion.getAppVersionId());
                if (deviceCode != null) {
                    loginUser.setDeviceCodeId(deviceCode.getDeviceCodeId());
                }
//                loginUser.setAppUserDeviceCode(appUserDeviceCode);

                recordLoginInfo(loginUser);

                // 自动扣除点数
                if (app.getBillType() == BillType.POINT &&
                        ((autoReducePoint && app.getLoginReducePointStrategy() == LoginReducePointStrategy.PARAMS) ||
                                app.getLoginReducePointStrategy() == LoginReducePointStrategy.ALWAYS)
                ) {
                    SysAppUserExpireLog expireLog = new SysAppUserExpireLog();
                    appUser = appUserService.selectSysAppUserByAppUserId(appUser.getAppUserId());
                    expireLog.setPointBefore(appUser.getPoint());
                    BigDecimal newPoint = appUser.getPoint().subtract(BigDecimal.valueOf(1));
                    appUser.setPoint(newPoint);
                    appUserService.updateSysAppUser(appUser);
                    expireLog.setPointAfter(newPoint);
                    // 记录用户时长变更日志
                    expireLog.setAppUserId(appUser.getAppUserId());
                    expireLog.setTemplateId(null);
                    expireLog.setCardId(null);
                    expireLog.setChangeDesc("软件登录自动扣点");
                    expireLog.setChangeType(AppUserExpireChangeType.APP_LOGIN);
                    expireLog.setChangeAmount(-1L);
                    expireLog.setCardNo(null);
                    expireLog.setAppId(appUser.getAppId());
                    AsyncManager.me().execute(AsyncFactory.recordAppUserExpire(expireLog));
                }

                // 生成token
                return tokenService.createToken(loginUser);
            }
        } catch (ApiException e) {
            try {
                AsyncManager.me().execute(AsyncFactory.recordAppLogininfor(appUser != null ? appUser.getAppUserId() : null, loginCodeShow, appName, appVersionStr, deviceCodeStr, Constants.LOGIN_FAIL, e.getMessage() + (e.getDetailMessage() != null ? "：" + e.getDetailMessage() : "")));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            throw e;
        } catch (Exception e) {
            try {
                AsyncManager.me().execute(AsyncFactory.recordAppLogininfor(appUser != null ? appUser.getAppUserId() : null, loginCodeShow, appName, appVersionStr, deviceCodeStr, Constants.LOGIN_FAIL, e.getMessage()));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            throw e;
        }
    }

    /**
     * App试用登录验证
     *
     * @param loginIp 登录的IP地址
     * @return 结果
     */
    public String appTrialLogin(String loginIp, SysApp app, SysAppVersion appVersion, String deviceCodeStr) {
        // 检查是否开启试用
        if (!UserConstants.YES.equals(app.getEnableTrial())
                || (!UserConstants.YES.equals(app.getEnableTrialByTimeQuantum()) && !UserConstants.YES.equals(app.getEnableTrialByTimes()))) {
            throw new ApiException(ErrorCode.ERROR_APP_TRIAL_NOT_ENABLE);
        }
        String loginCodeShow = "[试用]" + loginIp + "|" + deviceCodeStr;
        // 检查IP黑名单
        validUtils.checkIpBlackList(loginCodeShow);
        // 检查在线人数限制
        validUtils.checkLicenseMaxOnline();
        // 用户验证
        SysAppTrialUser appTrialUser = null;
        SysDeviceCode deviceCode = null;
        boolean flagNewUser = false;
        String appName = app.getAppName();
        String appVersionStr = appVersion.getVersionShow();
        try {
            appTrialUser = appTrialService.selectSysAppTrialUserByAppIdAndLoginIpAndDeviceCode(app.getAppId(), loginIp, deviceCodeStr);
            synchronized ((app.getAppId() + "|"+ loginIp + "|" + deviceCodeStr).intern()) {
                if (appTrialUser == null) { // 首次登录
                    // 检查同IP下试用设备数限制
                    List<SysAppTrialUser> trialUserList = appTrialService.selectSysAppTrialUserByLoginIp(loginIp);
                    if (app.getTrialTimesPerIp() != -1 && trialUserList.size() >= app.getTrialTimesPerIp()) {
                        throw new ApiException(ErrorCode.ERROR_TRIAL_OVER_LIMIT_TIMES_PER_IP);
                    }
                    flagNewUser = true;
                    appTrialUser = new SysAppTrialUser();
                    appTrialUser.setAppId(app.getAppId());
                    appTrialUser.setLastLoginTime(null);
                    appTrialUser.setStatus(UserConstants.NORMAL);
                    appTrialUser.setLoginTimes(0L);
                    appTrialUser.setLoginTimesAll(0L);
                    appTrialUser.setNextEnableTime(DateUtils.parseDate(UserConstants.MAX_DATE));
                    appTrialUser.setLoginIp(loginIp);
                    appTrialUser.setDeviceCode(deviceCodeStr);
                    appTrialUser.setExpireTime(MyUtils.getNewExpiredTimeAdd(null, app.getTrialTime()));
                    appTrialUser.setApp(app);
                    appTrialService.insertSysAppTrialUser(appTrialUser);
                } else {
                    if (UserStatus.DISABLE.getCode().equals(appTrialUser.getStatus())) {
                        log.info("试用用户：{} 已被停用.", loginCodeShow);
                        throw new ApiException(ErrorCode.ERROR_APP_TRIAL_USER_LOCKED, "您的试用已被停用");
                    }
                }
                appTrialUser.setUserName(loginCodeShow);
                // 自动绑定设备码
                if (StringUtils.isNotBlank(deviceCodeStr)) {
                    deviceCode = deviceCodeService.selectSysDeviceCodeByDeviceCode(deviceCodeStr);
                    if (deviceCode == null) {
                        deviceCode = new SysDeviceCode();
                        deviceCode.setDeviceCode(deviceCodeStr);
                        deviceCode.setLoginTimes(0L);
                        deviceCode.setStatus(UserConstants.NORMAL);
                        deviceCode.setCreateBy(loginCodeShow);
                        deviceCodeService.insertSysDeviceCode(deviceCode);
                    } else {
                        if (UserStatus.DISABLE.getCode().equals(deviceCode.getStatus())) {
                            log.info("用户设备：{} 已被停用所有软件.", deviceCodeStr);
                            throw new ApiException(ErrorCode.ERROR_DEVICE_CODE_LOCKED, "用户设备：" + deviceCodeStr + " 已被停用所有软件");
                        }
                    }
                }
                boolean flag = false;
                if (validUtils.checkInTrialQuantum(app)) {
                    flag = true;
                    appTrialUser.setLoginTimesAll(appTrialUser.getLoginTimesAll() + 1);
                } else {
                    if (!UserConstants.YES.equals(app.getEnableTrialByTimes())) {
                        throw new ApiException(ErrorCode.ERROR_NOT_IN_APP_TRIAL_TIME_QUANTUM);
                    }
                }
                if (!flag && UserConstants.YES.equals(app.getEnableTrialByTimes())) {
                    // 试用时间重置周期
                    if (app.getTrialCycle() != null && app.getTrialCycle() != 0) {
                        Date now = DateUtils.getNowDate();
                        if (appTrialUser.getNextEnableTime().before(now) || appTrialUser.getNextEnableTime().equals(DateUtils.parseDate(UserConstants.MAX_DATE))) {
                            appTrialUser.setLoginTimes(0L);
                            appTrialUser.setNextEnableTime(MyUtils.getNewExpiredTimeAdd(null, app.getTrialCycle()));
                            // appTrialUser.setLoginTimes(appTrialUser.getLoginTimes() + 1);
                            // appTrialUser.setExpireTime(MyUtils.getNewExpiredTimeAdd(null, app.getTrialTime()));
                        }
                    } else {
                        appTrialUser.setNextEnableTime(DateUtils.parseDate(UserConstants.MAX_DATE));
                    }
                    // 未开启试用期间不增加试用次数或不在有效试用时间内
                    if (!UserConstants.YES.equals(app.getNotAddTrialTimesInTrialTime()) || appTrialUser.getExpireTime().before(DateUtils.getNowDate()) || flagNewUser) {
                        // 检查试用次数
                        if (appTrialUser.getLoginTimes() >= app.getTrialTimes()) {
                            throw new ApiException(ErrorCode.ERROR_TRIAL_OVER_LIMIT_TIMES,
                                    "您已试用本软件" + appTrialUser.getLoginTimes() + "次，已到达试用次数上限"
                                            + (app.getTrialCycle() != null && app.getTrialCycle() != 0
                                            ? "，" + DateUtils.parseDateToStr(appTrialUser.getNextEnableTime()) + "后可再次试用" : ""));
                        }
                        appTrialUser.setLoginTimes(appTrialUser.getLoginTimes() + 1);
                        appTrialUser.setExpireTime(MyUtils.getNewExpiredTimeAdd(null, app.getTrialTime()));
                        appTrialUser.setLoginTimesAll(appTrialUser.getLoginTimesAll() + 1);
                    }
                    // 检测账号是否过期
                    validUtils.checkAppTrialUserIsExpired(appTrialUser);
                }
                appTrialService.updateSysAppTrialUser(appTrialUser);
                // 检查用户数、设备数限制
                validUtils.checkTrialLoginLimit(app, appTrialUser);
                try {
                    AsyncManager.me().execute(AsyncFactory.recordAppTrialLogininfor(appTrialUser.getAppTrialUserId(), loginCodeShow, appName, appVersionStr, deviceCodeStr, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                LoginUser loginUser = new LoginUser();
                loginUser.setIfApp(true);
                loginUser.setIfTrial(true);
                loginUser.setUserId(null);
                loginUser.setAppKey(app.getAppKey());
                loginUser.setAppId(app.getAppId());
                loginUser.setUser(null);
                loginUser.setAppTrialUserId(appTrialUser.getAppTrialUserId());
                loginUser.setUserName(appTrialUser.getUserName());
                loginUser.setAppVersionId(appVersion.getAppVersionId());
                loginUser.setDeviceCodeId(deviceCode.getDeviceCodeId());
//                loginUser.setAppUserDeviceCode(null);
                recordLoginInfo(loginUser);
                // 生成token
                return tokenService.createToken(loginUser);
            }
        } catch (ApiException e) {
            try {
                AsyncManager.me().execute(AsyncFactory.recordAppLogininfor(appTrialUser != null ? appTrialUser.getAppTrialUserId() : null, loginCodeShow, appName, appVersionStr, deviceCodeStr, Constants.LOGIN_FAIL, e.getMessage() + (e.getDetailMessage() != null ? "：" + e.getDetailMessage() : "")));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            throw e;
        } catch (Exception e) {
            try {
                AsyncManager.me().execute(AsyncFactory.recordAppLogininfor(appTrialUser != null ? appTrialUser.getAppTrialUserId() : null, loginCodeShow, appName, appVersionStr, deviceCodeStr, Constants.LOGIN_FAIL, e.getMessage()));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            throw e;
        }
    }

    /**
     * 记录登录信息
     */
    public void recordLoginInfo(LoginUser loginUser) {
        Date nowDate = DateUtils.getNowDate();
        // 账号信息
        if (loginUser.getUserId() != null) {
            SysUser sysUser = new SysUser();
            sysUser.setUserId(loginUser.getUserId());
            sysUser.setLoginIp(IpUtils.getIpAddr(ServletUtils.getRequest()));
            sysUser.setLoginDate(nowDate);
            userService.updateUserProfile(sysUser);
        }
        // 用户信息
        if (loginUser.getAppUserId() != null) {
            SysAppUser user = appUserService.selectSysAppUserByAppUserId(loginUser.getAppUserId());
            SysAppUser appUser = new SysAppUser();
            appUser.setAppUserId(user.getAppUserId());
            appUser.setLoginIp(IpUtils.getIpAddr(ServletUtils.getRequest()));
            appUser.setLoginTimes(user.getLoginTimes() + 1);
            appUser.setLastLoginTime(nowDate);
            appUserService.updateSysAppUser(appUser);
        }
        // 试用用户信息
        if (loginUser.getAppTrialUserId() != null) {
            SysAppTrialUser user = appTrialService.selectSysAppTrialUserByAppTrialUserId(loginUser.getAppTrialUserId());
            SysAppTrialUser trialUser = new SysAppTrialUser();
            trialUser.setAppTrialUserId(user.getAppTrialUserId());
            trialUser.setLoginTimes(user.getLoginTimes());
            trialUser.setLoginTimesAll(user.getLoginTimesAll());
            trialUser.setLastLoginTime(nowDate);
            appTrialService.updateSysAppTrialUser(trialUser);
        }
        // 设备码信息
        if (loginUser.getDeviceCodeId() != null) {
            SysDeviceCode code = deviceCodeService.selectSysDeviceCodeByDeviceCodeId(loginUser.getDeviceCodeId());
            SysDeviceCode deviceCode = new SysDeviceCode();
            deviceCode.setDeviceCodeId(code.getDeviceCodeId());
            deviceCode.setLoginTimes(code.getLoginTimes() + 1);
            deviceCode.setLastLoginTime(nowDate);
            deviceCodeService.updateSysDeviceCode(deviceCode);
            // 用户设备码信息
            if(loginUser.getAppUserId() != null) {
                SysAppUserDeviceCode appUserDeviceCode = appUserDeviceCodeService.selectSysAppUserDeviceCodeByAppUserIdAndDeviceCodeId(loginUser.getAppUserId(), loginUser.getDeviceCodeId());
                SysAppUserDeviceCode uAppUserDeviceCode = new SysAppUserDeviceCode();
                uAppUserDeviceCode.setId(appUserDeviceCode.getId());
                uAppUserDeviceCode.setLoginTimes(appUserDeviceCode.getLoginTimes() + 1);
                uAppUserDeviceCode.setLastLoginTime(nowDate);
                appUserDeviceCodeService.updateSysAppUserDeviceCode(uAppUserDeviceCode);
            }
        }
    }

    public String appLogout(LoginUser loginUser) {
        SysAppVersion version = versionService.selectSysAppVersionByAppVersionId(loginUser.getAppVersionId());
        SysDeviceCode deviceCode = deviceCodeService.selectSysDeviceCodeByDeviceCodeId(loginUser.getDeviceCodeId());
        String _deviceCodeStr = null;
        if (deviceCode != null) {
            _deviceCodeStr = deviceCode.getDeviceCode();
        }
        String userKey = CacheConstants.LOGIN_TOKEN_KEY + loginUser.getToken();
        if (loginUser.getIfApp()) {
            if (loginUser.getIfTrial()) {
                userKey = userKey + "|" + loginUser.getAppTrialUserId();
            } else {
                userKey = userKey + "|" + loginUser.getAppUserId();
            }
        }
        redisCache.deleteObject(userKey);
        SysCache.delete(userKey);
        try {
            SysApp app = appService.selectSysAppByAppKey(loginUser.getAppKey());
            AsyncManager.me().execute(AsyncFactory.recordAppLogininfor(loginUser.getAppUserId(), loginUser.getUserName(),
                    app.getAppName(), version.getVersionShow(), _deviceCodeStr, Constants.LOGOUT, "用户注销登录"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "注销成功";
    }
}
