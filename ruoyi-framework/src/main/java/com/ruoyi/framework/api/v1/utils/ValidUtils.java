package com.ruoyi.framework.api.v1.utils;

import com.ruoyi.api.v1.constants.ApiDefine;
import com.ruoyi.api.v1.domain.Api;
import com.ruoyi.api.v1.domain.Param;
import com.ruoyi.api.v1.utils.MyUtils;
import com.ruoyi.api.v1.utils.SignUtil;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.*;
import com.ruoyi.common.exception.ApiException;
import com.ruoyi.common.license.bo.LicenseCheckModel;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.system.domain.SysLoginCode;
import com.ruoyi.system.service.ISysAppService;
import com.ruoyi.system.service.ISysAppTrialUserService;
import com.ruoyi.system.service.ISysAppUserDeviceCodeService;
import com.ruoyi.system.service.ISysAppVersionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

@Component
@Slf4j
public class ValidUtils {

    @Resource
    private ISysAppUserDeviceCodeService appUserDeviceCodeService;
    @Resource
    private RedisCache redisCache;
    @Resource
    private TokenService tokenService;
    @Resource
    private ISysAppVersionService appVersionService;
    @Resource
    private ISysAppService appService;
    @Resource
    private ISysAppTrialUserService appTrialUserService;


    public void apiCheckApp(String appkey, SysApp app) {
        // 检查软件是否存在
        if (StringUtils.isBlank(appkey) || app == null) {
            log.info("软件：{} 不存在.", appkey);
            throw new ApiException(ErrorCode.ERROR_APP_NOT_EXIST);
        } else if (UserStatus.DELETED.getCode().equals(app.getDelFlag())) {
            log.info("软件：{} 已被删除.", app.getAppName());
            throw new ApiException(ErrorCode.ERROR_ACCOUNT_NOT_EXIST, "软件：" + app.getAppName() + " 已被删除");
        } else if (UserStatus.DISABLE.getCode().equals(app.getStatus())) {
            log.info("软件：{} 已被停用.", app.getAppName());
            throw new ApiException(ErrorCode.ERROR_APP_OFF, "软件：" + app.getAppName() + " 正在维护中" + (StringUtils.isBlank(app.getOffNotice()) ? "" : "，详情：" + app.getOffNotice()));
        }
    }

    public void apiCheckVersion(SysApp app, String appVersionStr, SysAppVersion version) {
        // 检查软件版本是否存在
        if (version == null) {
            log.info("软件 {} 的版本：{} 不存在.", app.getAppName(), appVersionStr);
            throw new ApiException(ErrorCode.ERROR_APP_VERSION_NOT_EXIST);
        } else if (UserStatus.DELETED.getCode().equals(version.getDelFlag())) {
            log.info("软件 {} 的版本：{} 已被删除.", app.getAppName(), appVersionStr);
            throw new ApiException(ErrorCode.ERROR_APP_VERSION_NOT_EXIST, "软件版本：" + appVersionStr + " 已被删除");
        } else if (UserStatus.DISABLE.getCode().equals(version.getStatus())) {
            log.info("软件：{} 的版本：{} 已被停用.", app.getAppName(), appVersionStr);
            throw new ApiException(ErrorCode.ERROR_APP_VERSION_OFF, "软件版本：" + appVersionStr + " 已停用");
        }
    }

    /**
     * 前置校验
     */
    public SysAppVersion apiCheckPreLogin(String appkey, SysApp app, Map<String, String> params) {
        String appVersionStr = params.get("appVer");
        if (StringUtils.isBlank(appVersionStr)) {
            throw new ApiException(ErrorCode.ERROR_PARAMETERS_MISSING, "软件版本号不能为空");
        }
        Long version = Long.parseLong(appVersionStr);
        SysAppVersion appVersion = appVersionService.selectSysAppVersionByAppIdAndVersion(app.getAppId(), version);
        // APP
//        apiCheckApp(appkey, app); 解码时已经检查过了，无需二次检测
        // 版本
        apiCheckVersion(app, appVersionStr, appVersion);
        // MD5
        checkMd5(appVersion, params);
        // 设备码
        if (app.getBindType() != null && app.getBindType() != BindType.NONE) {
            String deviceCode = params.get("deviceCode");
            if (StringUtils.isBlank(deviceCode)) {
                throw new ApiException(ErrorCode.ERROR_PARAMETERS_MISSING, "软件已开启设备验证，设备码不能为空");
            }
        }
        return appVersion;
    }

    /**
     * 不需要验证token的api接口验证
     */
    public void apiCheck(String api, SysApp app, Map<String, String> params, boolean needToken) {
        // 检查该软件是否可调用该api接口
        checkAppMatchApi(api, app, params);
        // 检查公共参数
        checkPublicParams(params, needToken);
        // 检查私有参数
        checkPrivateParams(api, params);
        // 检查sign
        checkSign(app, params);
        if (needToken) {
            // 检查数据是否过期
            checkDataExpired(app, params);
            // 检查软件用户是否可用
//        checkAppUserEnable(app, params);
        }
    }

    /**
     * 检查公共参数
     */
    private void checkPublicParams(Map<String, String> params, boolean needToken) {
        if (needToken) {
            for (Param publicParam : ApiDefine.publicParamsAuth) {
                if (publicParam.isNecessary() && StringUtils.isBlank(params.get(publicParam.getName()))) {
                    throw new ApiException(ErrorCode.ERROR_PARAMETERS_MISSING, "缺少必要公共参数或参数值为空：" + publicParam.getName() + "，参数说明：" + publicParam.getDescription());
                }
            }
        } else {
            for (Param publicParam : ApiDefine.publicParamsNoAuth) {
                if (publicParam.isNecessary() && StringUtils.isBlank(params.get(publicParam.getName()))) {
                    throw new ApiException(ErrorCode.ERROR_PARAMETERS_MISSING, "缺少必要公共参数或参数值为空：" + publicParam.getName() + "，参数说明：" + publicParam.getDescription());
                }
            }
        }
    }

    private void checkPrivateParams(String api, Map<String, String> params) {
        Api apiParams = ApiDefine.apiMap.get(api);
        if (apiParams.getParams() != null) {
            for (Param privateParam : apiParams.getParams()) {
                if (privateParam.isNecessary() && StringUtils.isBlank(params.get(privateParam.getName()))) {
                    throw new ApiException(ErrorCode.ERROR_PARAMETERS_MISSING, "缺少必要API参数或参数值为空：" + privateParam.getName() + "，参数说明：" + privateParam.getDescription());
                }
            }
        }
    }

    /**
     * 检查md5
     */
    private void checkMd5(SysAppVersion appVersion, Map<String, String> params) {
        if (UserConstants.YES.equals(appVersion.getCheckMd5())) {
            boolean flag = false;
            String md5 = appVersion.getMd5();
            if (StringUtils.isNotBlank(md5)) {
                String[] split = md5.split("\\|");
                for (String str : split) {
                    if (str.equalsIgnoreCase(params.get("md5"))) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    throw new ApiException(ErrorCode.ERROR_APP_MD5_MISMATCH);
                }
            }
        }
    }

    private void checkAppMatchApi(String api, SysApp app, Map<String, String> params) {
        Api apiParams = ApiDefine.apiMap.get(api);
        List<AuthType> loginTypeList = Arrays.asList(apiParams.getAuthTypes());
        List<BillType> chargeTypeList = Arrays.asList(apiParams.getBillTypes());

        if (!(loginTypeList.size() == 0 || loginTypeList.contains(app.getAuthType()))) {
            throw new ApiException(ErrorCode.ERROR_API_CALLED_MISMATCH);
        }
        if (!(chargeTypeList.size() == 0 || chargeTypeList.contains(app.getBillType()))) {
            throw new ApiException(ErrorCode.ERROR_API_CALLED_MISMATCH);
        }
    }

    /**
     * 检查数据包是否过期
     */
    private void checkDataExpired(SysApp app, Map<String, String> params) {
        Long dataTransExpiredTime = app.getDataExpireTime();
        if (dataTransExpiredTime > -1 && (System.currentTimeMillis() - Long.parseLong(params.get("timestamp")) > dataTransExpiredTime * 1000)) {
            throw new ApiException(ErrorCode.ERROR_DATA_EXPIRED);
        }
    }

    /**
     * 检查sign
     */
    private void checkSign(SysApp app, Map<String, String> params) {
//        System.out.println(SignUtil.sign(params, app.getAppSecret()));
        if (!SignUtil.verifySign(params, params.get("sign"), app.getAppSecret())) {
            throw new ApiException(ErrorCode.ERROR_SIGN_INVALID);
        }
    }

//    private void checkAppUserEnable(SysApp app, Map<String, String> params) {
//        Integer loginSoftwareId = getLoginSid();
//        Integer loginUserId = getLoginUid();
//        if (loginSoftwareId == null) {
//            throw new ApiException(Code.ERROR_TOKEN_INVALID);
//        }
//        if (loginSoftwareId != null && software.getId() != Integer.valueOf(loginSoftwareId)) {
//            throw new ApiException(Code.ERROR_REQUEST_CROSS_SOFTWARE);
//        }
//        if (loginSoftwareId != null && loginUserId != null) {
//            SoftwareUser softwareUser = softwareUserService.getBySidAndUid(loginSoftwareId,
//                    getLoginUid());
//            if (softwareUser != null) {
//                if (softwareUser.getIsBanned() == Constants.TRUE) {
//                    throw new ApiException(Code.ERROR_SOFTWARE_USER_LOCKED);
//                }
//                checkSoftwareUserIsExpired(software, softwareUser);
//            } else {
//                throw new ApiException("系统错误");
//            }
//        } else {
//            throw new ApiException(Code.ERROR_NO_LOGIN);
//        }
//    }

    public void checkMoreMachine(Long appUserId, Long deviceCodeId) {
        boolean flagFinishBind = false;
        List<SysAppUserDeviceCode> appUserDeviceCodeList = appUserDeviceCodeService.selectSysAppUserDeviceCodeByAppUserId(appUserId);
        if (appUserDeviceCodeList.size() > 0) {
            for (SysAppUserDeviceCode appUserDeviceCode : appUserDeviceCodeList) {
                if (Objects.equals(appUserDeviceCode.getDeviceCodeId(), deviceCodeId)) {
                    flagFinishBind = true;
                    break;
                }
            }
            if (!flagFinishBind) {
                throw new ApiException(ErrorCode.ERROR_BIND_MACHINE_LIMIT);
            }
        }
    }

    public void checkMoreUser(Long appId, Long appUserId, Long deviceCodeId) {
        boolean flagFinishBind = false;
        List<SysAppUserDeviceCode> appUserDeviceCodeList = appUserDeviceCodeService.selectSysAppUserDeviceCodeByAppIdAndDeviceCodeId(appId, deviceCodeId);
        if (appUserDeviceCodeList.size() > 0) {
            for (SysAppUserDeviceCode appUserDeviceCode : appUserDeviceCodeList) {
                if (Objects.equals(appUserDeviceCode.getAppUserId(), appUserId)) {
                    flagFinishBind = true;
                    break;
                }
            }
            if (!flagFinishBind) {
                throw new ApiException(ErrorCode.ERROR_BIND_USER_LIMIT);
            }
        }
    }

    public void checkAppUserIsExpired(SysApp app, SysAppUser appUser) {
        checkAppUserIsExpired(app, appUser, false);
    }

    /**
     * 检查软件用户是否过期
     *
     * @param app
     * @param appUser
     * @param checkEvenFree 是否即使软件关闭计费依然检查，否则将不检查直接通过
     */
    public void checkAppUserIsExpired(SysApp app, SysAppUser appUser, boolean checkEvenFree) {
        if (Objects.equals(app.getIsCharge(), UserConstants.YES) || checkEvenFree) {
            if (app.getBillType() == BillType.TIME) {
                if (appUser.getExpireTime() == null || !appUser.getExpireTime().after(DateUtils.getNowDate())) {
                    throw new ApiException(ErrorCode.ERROR_APP_USER_EXPIRED);
                }
            } else if (app.getBillType() == BillType.POINT) {
                if (appUser.getPoint() == null || appUser.getPoint().compareTo(BigDecimal.ZERO) <= 0) {
                    throw new ApiException(ErrorCode.ERROR_APP_USER_NO_POINT);
                }
            } else {
                throw new ApiException("软件计费方式有误");
            }
        }
    }

    public void checkAppTrialUserIsExpired(SysAppTrialUser appUser) {
        if (appUser.getExpireTime() == null || !appUser.getExpireTime().after(DateUtils.getNowDate())) {
            throw new ApiException(ErrorCode.ERROR_APP_TRIAL_USER_EXPIRED);
        }
    }

//    public void checkLoginLimit(SysApp app, SysAppUser appUser, SysDeviceCode deviceCode) {
//        Collection<String> keys = redisCache.keys(Constants.LOGIN_TOKEN_KEY + "*");
//        List<LoginUser> onlineListU = new ArrayList<>();
//        for (String key : keys) {
//            LoginUser user = redisCache.getCacheObject(key);
//            if (user.getIfApp() && !user.getIfTrial() && Objects.equals(appUser.getAppUserId(), user.getAppUser().getAppUserId())) {
//                onlineListU.add(user);
//            }
//        }
//        // 检查用户数量
//        Integer userLimitApp = app.getLoginLimitU();
//        Integer userLimitAppUser = appUser.getLoginLimitU();
//        Integer mixU;
//        if (userLimitApp == -1) {
//            mixU = userLimitAppUser;
//        } else {
//            if (userLimitAppUser == -1) {
//                mixU = userLimitApp;
//            } else {
//                mixU = userLimitApp < userLimitAppUser ? userLimitApp : userLimitAppUser;
//            }
//        }
//        if (mixU != -1 && mixU <= onlineListU.size()) {
//            if (app.getLimitOper() == LimitOper.TIPS) {
//                throw new ApiException(ErrorCode.ERROR_LOGIN_USER_LIMIT);
//            } else if (app.getLimitOper() == LimitOper.EARLIEST_LOGOUT) {
//                // 登录最早的退出
//                logoutTheEarliest(onlineListU, "用户同时登录数量超出限制");
//            }
//        }
//        // 检查设备数量
//        if (deviceCode != null) {
//            List<Long> onlineListM = new ArrayList<>();
//            for (LoginUser user : onlineListU) {
//                onlineListM.add(user.getDeviceCode().getDeviceCodeId());
//            }
//            Integer machineLimitApp = app.getLoginLimitM();
//            Integer machineLimitAppUser = appUser.getLoginLimitM();
//            Integer mixM;
//            if (machineLimitApp == -1) {
//                mixM = machineLimitAppUser;
//            } else {
//                if (machineLimitAppUser == -1) {
//                    mixM = machineLimitApp;
//                } else {
//                    mixM = machineLimitApp < machineLimitAppUser ? machineLimitApp : machineLimitAppUser;
//                }
//            }
//            if (mixM != -1 && mixM <= onlineListM.size() && !onlineListM.contains(deviceCode.getDeviceCodeId())) {
//                if (app.getLimitOper() == LimitOper.TIPS) {
//                    throw new ApiException(ErrorCode.ERROR_LOGIN_MACHINE_LIMIT);
//                } else if (app.getLimitOper() == LimitOper.EARLIEST_LOGOUT) {
//                    // 登录最早的设备退出
//                    logoutTheEarliest(onlineListU, "同时在线设备数量超出限制");
//                }
//            }
//        }
//    }

    public void checkLoginLimit(SysApp app, SysAppUser appUser, SysDeviceCode deviceCode) {
        Collection<String> keys = redisCache.scan(CacheConstants.LOGIN_TOKEN_KEY + "*|" + appUser.getAppUserId());
        List<LoginUser> onlineListU = new ArrayList<>();
        for (String key : keys) {
            LoginUser user = redisCache.getCacheObject(key);
            if (user != null && user.getIfApp() && !user.getIfTrial() && Objects.equals(appUser.getAppUserId(), user.getAppUserId())) {
                onlineListU.add(user);
            }
        }
        // 检查用户数量
        Integer mixU = MyUtils.getEffectiveLoginLimitU(app, appUser);
        if (mixU != -1 && mixU <= onlineListU.size()) {
            if (app.getLimitOper() == LimitOper.TIPS) {
                throw new ApiException(ErrorCode.ERROR_LOGIN_USER_LIMIT);
            } else if (app.getLimitOper() == LimitOper.EARLIEST_LOGOUT) {
                // 登录最早的退出
                logoutTheEarliest(onlineListU, "用户同时登录数量超出限制");
            }
        }
        // 检查设备数量
        if (deviceCode != null) {
            Set<Long> onlineListM = new HashSet<>();
            for (LoginUser user : onlineListU) {
                if (user.getDeviceCode() != null) {
                    onlineListM.add(user.getDeviceCode().getDeviceCodeId());
                }
            }
            Integer mixM = MyUtils.getEffectiveLoginLimitM(app, appUser);
            if (mixM != -1 && mixM <= onlineListM.size() && !onlineListM.contains(deviceCode.getDeviceCodeId())) {
                if (app.getLimitOper() == LimitOper.TIPS) {
                    throw new ApiException(ErrorCode.ERROR_LOGIN_MACHINE_LIMIT);
                } else if (app.getLimitOper() == LimitOper.EARLIEST_LOGOUT) {
                    // 登录最早的设备退出
                    logoutTheEarliest(onlineListU, "同时在线设备数量超出限制");
                }
            }
        }
    }

    public void checkTrialLoginLimit(SysApp app, SysAppTrialUser appTrialUser) {
        Collection<String> keys = redisCache.scan(CacheConstants.LOGIN_TOKEN_KEY + "*|" + appTrialUser.getAppTrialUserId());
        SysAppTrialUser trailUser = appTrialUserService.selectSysAppTrialUserByAppTrialUserId(appTrialUser.getAppTrialUserId());
        List<LoginUser> onlineListU = new ArrayList<>();
        for (String key : keys) {
            LoginUser user = redisCache.getCacheObject(key);
            if (user != null && trailUser != null && user.getIfTrial()
                    && Objects.equals(appTrialUser.getLoginIp(), trailUser.getLoginIp())
                    && Objects.equals(appTrialUser.getDeviceCode(), trailUser.getDeviceCode())) {
                onlineListU.add(user);
            }
        }
        // 检查用户数量
        Integer userLimitApp = app.getLoginLimitU();
        if (userLimitApp != -1 && userLimitApp <= onlineListU.size()) {
            if (app.getLimitOper() == LimitOper.TIPS) {
                throw new ApiException(ErrorCode.ERROR_LOGIN_USER_LIMIT);
            } else if (app.getLimitOper() == LimitOper.EARLIEST_LOGOUT) {
                // 登录最早的退出
                logoutTheEarliest(onlineListU, "用户同时登录数量超出限制");
            }
        }
    }

    /**
     * 注销最早登录的账号
     *
     * @param onlineList
     */
    private void logoutTheEarliest(List<LoginUser> onlineList, String msg) {
        try {
            // 将当前在线用户按照登录时间排序
            onlineList.sort(Comparator.comparing(LoginUser::getLoginTime));
            if (onlineList.size() > 0) {
                LoginUser loginUser = onlineList.get(0);
                String userName = loginUser.getUsername();
                // 删除用户缓存记录
                tokenService.delLoginUser(loginUser.getToken());
                try {
                    // 记录用户退出日志
                    SysApp app = appService.selectSysAppByAppKey(loginUser.getAppKey());
                    AsyncManager.me().execute(AsyncFactory.recordAppLogininfor(loginUser.getAppUserId(), userName,
                            app.getAppName(), loginUser.getAppVersion().getVersionShow(),
                            loginUser.getDeviceCode() != null ? loginUser.getDeviceCode().getDeviceCode() : null,
                            Constants.LOGOUT, "系统强制退出：" + msg));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Constants.LAST_ERROR_REASON_MAP.put(loginUser.getToken(), "您的账号/登录码在其他设备上登录");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkLicenseMaxOnline() {
        // 检查在线人数限制
        List<String> onlineAppUser = new ArrayList<>();
        Collection<String> keys = redisCache.scan(CacheConstants.LOGIN_TOKEN_KEY + "*|*");
//        for (String key : keys) {
//            LoginUser user = redisCache.getCacheObject(key);
//            if (user != null && user.getIfApp()) {
//                onlineAppUser.add(key);
//            }
//        }
        // -1 表示不限制
        Integer maxOnline = 0;
        if (Constants.LICENSE_CONTENT != null && Constants.LICENSE_CONTENT.getExtra() != null) {
            maxOnline = ((LicenseCheckModel) Constants.LICENSE_CONTENT.getExtra()).getMaxOnline();
        }
//        if (maxOnline != -1 && onlineAppUser.size() >= maxOnline) {
//            throw new ApiException("当前在线人数已超出授权上限，请联系管理员升级授权或稍后再试");
//        }
        if (maxOnline != -1 && keys.size() >= maxOnline) {
            throw new ApiException("当前在线人数已超出授权上限，请联系管理员升级授权或稍后再试");
        }

    }

    public void checkUser(String username, String password, SysUser user) {
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
    }

    public void checkUser(String username, SysUser user) {
        if (StringUtils.isNull(user)) {
            log.info("登录账号：{} 不存在.", username);
            throw new ApiException(ErrorCode.ERROR_ACCOUNT_NOT_EXIST, "账号：" + username + " 不存在");
        } else if (UserStatus.DELETED.getCode().equals(user.getDelFlag())) {
            log.info("登录账号：{} 已被删除.", username);
            throw new ApiException(ErrorCode.ERROR_ACCOUNT_NOT_EXIST, "账号：" + username + " 已被删除");
        } else if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            log.info("登录账号：{} 已被停用.", username);
            throw new ApiException(ErrorCode.ERROR_ACCOUNT_LOCKED, "账号：" + username + " 已停用");
        }
    }

    public void checkLoginCode(SysApp app, String loginCodeStr, SysLoginCode loginCode) {
        if (StringUtils.isNull(loginCode)) {
            log.info("单码：{} 不存在.", loginCodeStr);
            throw new ApiException(ErrorCode.ERROR_LOGIN_CODE_NOT_EXIST, "单码：" + loginCodeStr + " 不存在");
        } else if (UserConstants.YES.equals(loginCode.getIsCharged())) {
            log.info("单码：{} 已被使用.", loginCodeStr);
            throw new ApiException(ErrorCode.ERROR_LOGIN_CODE_NOT_EXIST, "单码：" + loginCodeStr + " 已被使用");
        } else if (UserStatus.DELETED.getCode().equals(loginCode.getDelFlag())) {
            log.info("单码：{} 已被删除.", loginCodeStr);
            throw new ApiException(ErrorCode.ERROR_LOGIN_CODE_NOT_EXIST, "单码：" + loginCodeStr + " 已被删除");
        } else if (UserStatus.DISABLE.getCode().equals(loginCode.getStatus())) {
            log.info("单码：{} 已被停用.", loginCodeStr);
            throw new ApiException(ErrorCode.ERROR_LOGIN_CODE_LOCKED, "单码：" + loginCodeStr + " 已停用");
        } else if (loginCode.getExpireTime().before(DateUtils.getNowDate())) {
            String expiredTime = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, loginCode.getExpireTime());
            log.info("单码：{} 有效期至：{}，现已过期", loginCodeStr, expiredTime);
            throw new ApiException(ErrorCode.ERROR_LOGIN_CODE_EXPIRED, "单码：" + loginCodeStr + " 有效期至：" + expiredTime + "，现已过期");
        } else if (!Objects.equals(loginCode.getAppId(), app.getAppId())) {
            throw new ApiException(ErrorCode.ERROR_LOGIN_CODE_APP_MISMATCH, "单码：" + loginCodeStr + " 与软件：" + app.getAppName() + "不匹配");
        }
    }

    public boolean checkInTrialQuantum(SysApp app) {
        if (UserConstants.YES.equals(app.getEnableTrial()) && UserConstants.YES.equals(app.getEnableTrialByTimeQuantum())) {
            if (app.getTrialTimeQuantum() != null) {
                String[] split = app.getTrialTimeQuantum().split("-");
                if (split.length == 2) {
                    Date now = DateUtils.getNowDate();
                    String date = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, now);
                    Date start = DateUtils.parseDate(date + " " + split[0]);
                    Date end = DateUtils.parseDate(date + " " + split[1]);
                    return !start.after(now) && !end.before(now);
                }
            }
        }
        return false;
    }

    public Date getTrialQuantumStartTime(SysApp app) {
        if (app.getTrialTimeQuantum() != null) {
            String[] split = app.getTrialTimeQuantum().split("-");
            if (split.length == 2) {
                Date now = DateUtils.getNowDate();
                String date = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, now);
                return DateUtils.parseDate(date + " " + split[0]);
            }
        }
        return null;
    }

    public Date getTrialQuantumEndTime(SysApp app) {
        if (app.getTrialTimeQuantum() != null) {
            String[] split = app.getTrialTimeQuantum().split("-");
            if (split.length == 2) {
                Date now = DateUtils.getNowDate();
                String date = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, now);
                return DateUtils.parseDate(date + " " + split[1]);
            }
        }
        return null;
    }

}
