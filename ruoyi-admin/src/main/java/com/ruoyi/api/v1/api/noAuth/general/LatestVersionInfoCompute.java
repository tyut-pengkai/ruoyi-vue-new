package com.ruoyi.api.v1.api.noAuth.general;

import com.ruoyi.api.v1.constants.Constants;
import com.ruoyi.api.v1.domain.*;
import com.ruoyi.api.v1.domain.vo.SysAppVersionVo;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.SysAppVersion;
import com.ruoyi.system.service.ISysAppVersionService;

import javax.annotation.Resource;

public class LatestVersionInfoCompute extends Function {

    @Resource
    private ISysAppVersionService appVersionService;

    @Override
    public void init() {
        this.setApi(new Api("latestVersionInfoCompute.ng", "获取最新版本信息(计算)", false, Constants.API_TAG_GENERAL,
                "获取软件最新版本信息，本接口与latestVersionInfo.ng不同的是结果中的forceUpdate参数为计算后的结果，可看作是latestVersionInfo.ng与latestVersionInfoForceUpdate.ng两个接口的融合", Constants.AUTH_TYPE_ALL, Constants.BILL_TYPE_ALL,
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
                        new RespItem(Resp.DataType.string, "forceUpdate", "是否强制更新（计算后的结果）"),
                        new RespItem(Resp.DataType.string, "checkMd5", "是否校验MD5"),
                        new RespItem(Resp.DataType.string, "downloadUrlDirect", "直链地址"),
                        new RespItem(Resp.DataType.string, "remark", "备注信息"),
                })));
    }

    @Override
    public Object handle() {
        long appVer = Long.parseLong(getParams().get("appVer"));
        SysAppVersion sysAppVersion = appVersionService.selectLatestVersionByAppId(getApp().getAppId());
        SysAppVersion sysAppVersionForceUpdate = appVersionService.selectLatestVersionForceUpdateByAppId(getApp().getAppId());
        if(sysAppVersionForceUpdate != null && sysAppVersionForceUpdate.getVersionNo() > appVer) {
            sysAppVersion.setForceUpdate(UserConstants.YES);
        }
        return new SysAppVersionVo(sysAppVersion);
    }
}
