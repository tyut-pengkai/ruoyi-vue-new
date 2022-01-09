package com.ruoyi.api.v1.api;

import com.ruoyi.api.v1.constants.Constants;
import com.ruoyi.api.v1.domain.Api;
import com.ruoyi.api.v1.domain.Function;
import com.ruoyi.common.core.domain.entity.SysApp;
import com.ruoyi.common.core.domain.entity.SysAppVersion;
import com.ruoyi.common.core.domain.entity.SysDeviceCode;
import com.ruoyi.common.utils.DateUtils;

public class ApiTime extends Function {

    public ApiTime(SysApp app, SysAppVersion appVersion, SysDeviceCode deviceCode) {
        super(app, appVersion, deviceCode);
    }

    @Override
    public void init() {
        this.setApi(new Api("timetime.a", "获取服务器时间", false, Constants.API_TAG_COMMON, "获取服务器时间，格式yyyy-MM-dd HH:mm:ss"));
    }

    @Override
    public Object run() {
        return DateUtils.getTime();
    }

}
