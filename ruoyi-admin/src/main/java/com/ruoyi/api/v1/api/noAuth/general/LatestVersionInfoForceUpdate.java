package com.ruoyi.api.v1.api.noAuth.general;

import com.ruoyi.api.v1.constants.Constants;
import com.ruoyi.api.v1.domain.Api;
import com.ruoyi.api.v1.domain.Function;
import com.ruoyi.api.v1.domain.Resp;
import com.ruoyi.api.v1.domain.RespItem;
import com.ruoyi.api.v1.domain.vo.SysAppVersionVo;
import com.ruoyi.common.core.domain.entity.SysAppVersion;
import com.ruoyi.system.service.ISysAppVersionService;

import javax.annotation.Resource;

public class LatestVersionInfoForceUpdate extends Function {

    @Resource
    private ISysAppVersionService appVersionService;

    @Override
    public void init() {
        this.setApi(new Api("latestVersionInfoForceUpdate.ng", "获取强制更新版本信息", false, Constants.API_TAG_GENERAL,
                "获取需要被强制更新到的最低软件版本，如果当前版本低于此版本，则应该启动强制更新策略", Constants.AUTH_TYPE_ALL,
                Constants.BILL_TYPE_ALL, null,
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
        SysAppVersion sysAppVersion = appVersionService.selectLatestVersionForceUpdateByAppId(getApp().getAppId());
        return sysAppVersion == null ? new SysAppVersionVo(new SysAppVersion()) : new SysAppVersionVo(sysAppVersion);
    }
}
