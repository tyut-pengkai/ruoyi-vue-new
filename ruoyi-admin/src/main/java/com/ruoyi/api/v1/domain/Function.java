package com.ruoyi.api.v1.domain;

import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.system.domain.SysLoginCode;
import lombok.Data;

@Data
public abstract class Function {
    //公共
    private Api api;
    private SysApp app;
    private SysAppVersion appVersion;
    private SysDeviceCode deviceCode;
    private SysAppUser appUser;
    //账号登录
    private SysUser user;
    //登录码登录
    private SysLoginCode loginCode;

//    public Function() {
//        init();
//    }

    public Function(SysApp app, SysAppVersion appVersion, SysDeviceCode deviceCode) {
        init();
        this.app = app;
        this.appVersion = appVersion;
        this.deviceCode = deviceCode;
    }

    public Function(SysApp app, SysAppVersion appVersion, SysDeviceCode deviceCode, SysUser user) {
        init();
        this.app = app;
        this.appVersion = appVersion;
        this.deviceCode = deviceCode;
        this.user = user;
    }

    public Function(SysApp app, SysAppVersion appVersion, SysDeviceCode deviceCode, SysLoginCode loginCode) {
        init();
        this.app = app;
        this.appVersion = appVersion;
        this.deviceCode = deviceCode;
        this.loginCode = loginCode;
    }

    public abstract void init();

    public abstract Object run();

}
