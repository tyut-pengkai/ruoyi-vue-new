package com.ruoyi.api.v1.api.noAuth.user;

import com.ruoyi.api.v1.constants.Constants;
import com.ruoyi.api.v1.domain.Api;
import com.ruoyi.api.v1.domain.Function;
import com.ruoyi.api.v1.domain.Param;
import com.ruoyi.api.v1.domain.Resp;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.core.domain.entity.SysAppUser;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.ErrorCode;
import com.ruoyi.common.enums.UserStatus;
import com.ruoyi.common.exception.ApiException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.SysCache;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.system.service.ISysAppService;
import com.ruoyi.system.service.ISysAppUserService;
import com.ruoyi.system.service.ISysUserService;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Objects;

public class LogoutAll extends Function {

    @Resource
    ISysAppUserService appUserService;
    @Resource
    ISysAppService appService;
    @Resource
    private ISysUserService userService;
    @Resource
    private RedisCache redisCache;

    @Override
    public void init() {
        this.setApi(new Api("logoutAll.nu", "注销所有登录", false, Constants.API_TAG_ACCOUNT,
                "注销指定账号在本软件的所有登录", Constants.AUTH_TYPE_ALL, Constants.BILL_TYPE_ALL,
                new Param[]{
                        new Param("username", true, "要注销的账号"),
                        new Param("password", true, "密码")
                }, new Resp(Resp.DataType.string, "成功返回0")));
    }

    @Override
    public Object handle() {
        String username = this.getParams().get("username");
        String password = this.getParams().get("password");

        SysUser sysUser = userService.selectUserByUserName(username);
        if (sysUser == null) {
            throw new ApiException(ErrorCode.ERROR_ACCOUNT_NOT_EXIST, "账号：" + username + " 不存在");
        } else if (UserStatus.DELETED.getCode().equals(sysUser.getDelFlag())) {
            throw new ApiException(ErrorCode.ERROR_ACCOUNT_NOT_EXIST, "账号：" + username + " 已被删除");
        } else if (!SecurityUtils.matchesPassword(password, sysUser.getPassword())) {
            throw new ApiException(ErrorCode.ERROR_USERNAME_OR_PASSWORD_ERROR, "账号或密码错误");
        }

        SysAppUser appUser = appUserService.selectSysAppUserByAppIdAndUserId(this.getApp().getAppId(), sysUser.getUserId());
        if (appUser == null) {
            return "0";
        }

        Collection<String> keys = redisCache.scan(CacheConstants.LOGIN_TOKEN_KEY + "*|" + appUser.getAppUserId());
        for (String key : keys) {
            LoginUser loginUser = null;
            try {
                loginUser = (LoginUser) SysCache.get(key);
            } catch(Exception ignored) {}
            if(loginUser == null) {
                loginUser = redisCache.getCacheObject(key);
                SysCache.set(key, loginUser, redisCache.getExpire(key));
            }
            if (loginUser != null && loginUser.getIfApp() && Objects.equals(loginUser.getAppUserId(), appUser.getAppUserId())) {
                String _deviceCodeStr = null;
                if (loginUser.getDeviceCode() != null) {
                    _deviceCodeStr = loginUser.getDeviceCode().getDeviceCode();
                }
                redisCache.deleteObject(CacheConstants.LOGIN_TOKEN_KEY + loginUser.getToken());
                SysCache.delete(CacheConstants.LOGIN_TOKEN_KEY + loginUser.getToken());
                try {
                    AsyncManager.me().execute(
                            AsyncFactory.recordAppLogininfor(
                                    appUser.getAppUserId(),
                                    appUser.getUserName(),
                                    getApp().getAppName(),
                                    loginUser.getAppVersion().getVersionShow(),
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
