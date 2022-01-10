package com.ruoyi.api.v1.api.noAuth.general;

import com.ruoyi.api.v1.constants.Constants;
import com.ruoyi.api.v1.domain.Api;
import com.ruoyi.api.v1.domain.Function;
import com.ruoyi.api.v1.domain.vo.SysAppVersionVo;
import com.ruoyi.common.core.domain.entity.SysAppVersion;
import com.ruoyi.system.service.ISysAppVersionService;

import javax.annotation.Resource;

public class LatestVersion extends Function {

    @Resource
    private ISysAppVersionService appVersionService;

    @Override
    public void init() {
        this.setApi(new Api("latestVersion.ng", "获取软件最新版本", false, Constants.API_TAG_GENERAL, "获取软件最新版本"));
    }

    @Override
    public Object handle() {
        SysAppVersion sysAppVersion = appVersionService.selectLatestVersionByAppId(getApp().getAppId());
        return new SysAppVersionVo(sysAppVersion);
    }
}
