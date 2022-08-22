package com.ruoyi.api.v1.api.noAuth.general;

import com.ruoyi.api.v1.constants.Constants;
import com.ruoyi.api.v1.domain.*;
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
                },
                new Resp(Resp.DataType.object, "版本信息", new RespItem[]{
                        new RespItem(Resp.DataType.string, "versionName", "版本名称"),
                        new RespItem(Resp.DataType.integer, "versionNo", "版本号"),
                        new RespItem(Resp.DataType.string, "updateLog", "更新日志"),
                        new RespItem(Resp.DataType.string, "downloadUrl", "下载地址"),
                        new RespItem(Resp.DataType.string, "status", "版本状态（0正常 1停用）"),
                        new RespItem(Resp.DataType.string, "md5", "软件MD5"),
                        new RespItem(Resp.DataType.string, "forceUpdate", "是否强制更新"),
                        new RespItem(Resp.DataType.string, "checkMd5", "是否校验MD5"),
                        new RespItem(Resp.DataType.string, "downloadUrlDirect", "直链地址"),
                        new RespItem(Resp.DataType.string, "remark", "备注信息"),
                })));
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
