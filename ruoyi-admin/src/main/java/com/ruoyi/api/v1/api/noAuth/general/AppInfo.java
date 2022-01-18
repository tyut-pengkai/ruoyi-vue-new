package com.ruoyi.api.v1.api.noAuth.general;

import com.ruoyi.api.v1.constants.Constants;
import com.ruoyi.api.v1.domain.Api;
import com.ruoyi.api.v1.domain.Function;
import com.ruoyi.api.v1.domain.vo.SysAppVo;
import com.ruoyi.common.core.domain.entity.SysApp;
import com.ruoyi.system.service.ISysAppService;

import javax.annotation.Resource;

public class AppInfo extends Function {

    @Resource
    private ISysAppService appService;

    @Override
    public void init() {
        this.setApi(new Api("appInfo.ng", "获取软件配置信息", false, Constants.API_TAG_GENERAL,
                "获取软件配置信息", Constants.AUTH_TYPE_ALL, Constants.BILL_TYPE_ALL));
    }

    @Override
    public Object handle() {
        SysApp sysApp = appService.selectSysAppByAppId(getApp().getAppId());
        return new SysAppVo(sysApp);
    }
}
