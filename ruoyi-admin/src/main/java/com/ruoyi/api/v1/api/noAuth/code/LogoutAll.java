package com.ruoyi.api.v1.api.noAuth.code;

import com.ruoyi.api.v1.constants.Constants;
import com.ruoyi.api.v1.domain.Api;
import com.ruoyi.api.v1.domain.Function;
import com.ruoyi.api.v1.domain.Param;
import com.ruoyi.api.v1.domain.Resp;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.core.domain.entity.SysApp;
import com.ruoyi.common.core.domain.entity.SysAppUser;
import com.ruoyi.common.core.domain.entity.SysAppVersion;
import com.ruoyi.common.core.domain.entity.SysDeviceCode;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.AuthType;
import com.ruoyi.common.enums.ErrorCode;
import com.ruoyi.common.exception.ApiException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.SysCache;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.system.service.ISysAppUserService;
import com.ruoyi.system.service.ISysAppVersionService;
import com.ruoyi.system.service.ISysDeviceCodeService;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Objects;

public class LogoutAll extends Function {

    @Resource
    ISysAppUserService appUserService;
    @Resource
    private ISysAppVersionService versionService;
    @Resource
    private RedisCache redisCache;
    @Resource
    private ISysDeviceCodeService deviceCodeService;

    @Override
    public void init() {
        this.setApi(new Api("logoutAll.nc", "注销所有登录", false, Constants.API_TAG_CODE,
                "注销指定单码在本软件的所有登录", new AuthType[]{AuthType.LOGIN_CODE}, Constants.BILL_TYPE_ALL,
                new Param[]{
                        new Param("deviceCode", false, "设备码，可选，传了只注销指定设备码的登录，不传则注销所有设备码的登录"),
                        new Param("loginCode", true, "单码"),
                }, new Resp(Resp.DataType.string, "成功返回0")));
    }

    @Override
    public Object handle() {
        String deviceCodeStr = this.getParams().get("deviceCode");
        String loginCodeStr = this.getParams().get("loginCode");
        SysApp app = this.getApp();
        if (app == null) {
            throw new ApiException(ErrorCode.ERROR_APP_NOT_EXIST, "应用不存在");
        }

        SysAppUser appUser = appUserService.selectSysAppUserByAppIdAndLoginCode(app.getAppId(), loginCodeStr);
        if (appUser == null) {
            return "0";
        }

        Collection<String> keys = redisCache.scan(CacheConstants.LOGIN_TOKEN_KEY + "*|" + appUser.getAppUserId());
        for (String key : keys) {
            LoginUser loginUser = null;
            try {
                loginUser = (LoginUser) SysCache.get(key);
            } catch (Exception ignored) {
            }
            if (loginUser == null) {
                loginUser = redisCache.getCacheObject(key);
                SysCache.set(key, loginUser, redisCache.getExpire(key));
            }
            if (loginUser != null && loginUser.getIfApp() && Objects.equals(loginUser.getAppUserId(), appUser.getAppUserId())) {
                String _deviceCodeStr = null;
                if (loginUser.getDeviceCodeId() != null) {
                    SysDeviceCode deviceCode = deviceCodeService.selectSysDeviceCodeByDeviceCodeId(loginUser.getDeviceCodeId());
                    _deviceCodeStr = deviceCode.getDeviceCode();
                }
                if (StringUtils.isNotBlank(deviceCodeStr) && !_deviceCodeStr.equals(deviceCodeStr)) {
                    continue; // 如果指定了设备码，只注销指定设备码的登录
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
                    SysAppVersion appVersion = versionService.selectSysAppVersionByAppVersionId(loginUser.getAppVersionId());
                    AsyncManager.me().execute(
                            AsyncFactory.recordAppLogininfor(
                                    appUser.getAppUserId(),
                                    appUser.getUserName(),
                                    getApp().getAppName(),
                                    appVersion.getVersionShow(),
                                    _deviceCodeStr,
                                    com.ruoyi.common.constant.Constants.LOGOUT,
                                    "用户注销所有登录"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return "0";
    }
}
