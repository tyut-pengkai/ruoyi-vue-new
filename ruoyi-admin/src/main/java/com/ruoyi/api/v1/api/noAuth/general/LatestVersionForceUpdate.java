package com.ruoyi.api.v1.api.noAuth.general;

import com.ruoyi.api.v1.constants.Constants;
import com.ruoyi.api.v1.domain.Api;
import com.ruoyi.api.v1.domain.Function;
import com.ruoyi.api.v1.domain.vo.SysAppVersionVo;
import com.ruoyi.common.core.domain.entity.SysAppVersion;
import com.ruoyi.system.service.ISysAppVersionService;

import javax.annotation.Resource;

public class LatestVersionForceUpdate extends Function {

    @Resource
    private ISysAppVersionService appVersionService;

    @Override
    public void init() {
        this.setApi(new Api("latestVersionForceUpdate.ng", "获取强制更新软件版本", false, Constants.API_TAG_GENERAL,
                "获取需要被强制更新到的最低软件版本，如果当前版本低于此版本，则应该启动强制更新策略", Constants.AUTH_TYPE_ALL, Constants.BILL_TYPE_ALL));
    }

    @Override
    public Object handle() {
        SysAppVersion sysAppVersion = appVersionService.selectLatestVersionForceUpdateByAppId(getApp().getAppId());
        return new SysAppVersionVo(sysAppVersion);
    }
}
