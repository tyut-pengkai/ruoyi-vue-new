package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class SysAppVersion extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 版本ID
     */
    private Long appVersionId;

    /**
     * 软件ID
     */
    @Excel(name = "软件ID")
    private Long appId;

    /**
     * 版本名称
     */
    @Excel(name = "版本名称")
    private String versionName;

    /**
     * 版本号
     */
    @Excel(name = "版本号")
    private Long versionNo;

    /**
     * 更新日志
     */
    @Excel(name = "更新日志")
    private String updateLog;

    /**
     * 下载地址
     */
    @Excel(name = "下载地址")
    private String downloadUrl;

    /**
     * 版本状态（0正常 1停用）
     */
    @Excel(name = "版本状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    /**
     * 软件MD5
     */
    @Excel(name = "软件MD5")
    private String md5;

    /**
     * 是否强制更新到此版本
     */
    private String forceUpdate;

    private SysApp app;

    /**
     * 直链地址
     */
    @Excel(name = "是否校验md5", readConverterExp = "Y=是,N=否")
    private String checkMd5;

    /**
     * 直链地址
     */
    @Excel(name = "直链地址")
    private String downloadUrlDirect;

    public SysApp getApp() {
        return app;
    }

    public void setApp(SysApp app) {
        this.app = app;
    }

    public Long getAppVersionId() {
        return appVersionId;
    }

    public void setAppVersionId(Long appVersionId) {
        this.appVersionId = appVersionId;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public Long getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(Long versionNo) {
        this.versionNo = versionNo;
    }

    public String getUpdateLog() {
        return updateLog;
    }

    public void setUpdateLog(String updateLog) {
        this.updateLog = updateLog;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getVersionShow() {
        return versionName + "(" + versionNo + ")";
    }

    public String getForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(String forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public String getCheckMd5() {
        return checkMd5;
    }

    public void setCheckMd5(String checkMd5) {
        this.checkMd5 = checkMd5;
    }

    public String getDownloadUrlDirect() {
        return downloadUrlDirect;
    }

    public void setDownloadUrlDirect(String downloadUrlDirect) {
        this.downloadUrlDirect = downloadUrlDirect;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("appVersionId", getAppVersionId())
                .append("appId", getAppId())
                .append("versionName", getVersionName())
                .append("versionNo", getVersionNo())
                .append("updateLog", getUpdateLog())
                .append("downloadUrl", getDownloadUrl())
                .append("downloadUrlDirect", getDownloadUrlDirect())
                .append("status", getStatus())
                .append("delFlag", getDelFlag())
                .append("md5", getMd5())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .append("forceUpdate", getForceUpdate())
                .append("checkMd5", getCheckMd5())
                .toString();
    }
}
