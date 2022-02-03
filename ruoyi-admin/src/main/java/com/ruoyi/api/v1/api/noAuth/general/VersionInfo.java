package com.ruoyi.api.v1.api.noAuth.general;

import com.ruoyi.api.v1.constants.Constants;
import com.ruoyi.api.v1.domain.Api;
import com.ruoyi.api.v1.domain.Function;
import com.ruoyi.api.v1.domain.Param;
import com.ruoyi.api.v1.domain.vo.SysAppVersionVo;
import com.ruoyi.common.core.domain.entity.SysAppVersion;
import com.ruoyi.common.enums.ErrorCode;
import com.ruoyi.common.exception.ApiException;
import com.ruoyi.system.service.ISysAppVersionService;

import javax.annotation.Resource;

public class VersionInfo extends Function {

    @Resource
    private ISysAppVersionService appVersionService;

    @Override
    public void init() {
        this.setApi(new Api("versionInfo.ng", "获取软件版本信息", false, Constants.API_TAG_GENERAL,
                "获取软件当前版本信息", Constants.AUTH_TYPE_ALL, Constants.BILL_TYPE_ALL,
                new Param[]{
                        new Param("appVer", true, "软件版本号")
                }));
    }

    @Override
    public Object handle() {
        SysAppVersion sysAppVersion = appVersionService.selectSysAppVersionByAppIdAndVersion(getApp().getAppId(), Long.parseLong(getParams().get("appVer")));
        if (sysAppVersion != null) {
            return new SysAppVersionVo(sysAppVersion);
        }
        throw new ApiException(ErrorCode.ERROR_APP_VERSION_NOT_EXIST);
    }
}
