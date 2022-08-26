package com.ruoyi.common.core.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

public class SysAppUserDeviceCode extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * APPUSER ID
     */
    @Excel(name = "APPUSER ID")
    private Long appUserId;

    /**
     * 设备码
     */
    @Excel(name = "设备码")
    private Long deviceCodeId;

    /**
     * 最后登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "最后登录时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date lastLoginTime;

    /**
     * 登录次数
     */
    @Excel(name = "登录次数")
    private Long loginTimes;

    /**
     * 状态（0正常 1停用）
     */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    private SysAppUser appUser;

    private SysDeviceCode deviceCode;

    public SysAppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(SysAppUser appUser) {
        this.appUser = appUser;
    }

    public SysDeviceCode getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(SysDeviceCode deviceCode) {
        this.deviceCode = deviceCode;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
    public void setAppUserId(Long appUserId)
    {
        this.appUserId = appUserId;
    }

    public Long getAppUserId()
    {
        return appUserId;
    }
    public void setDeviceCodeId(Long deviceCodeId)
    {
        this.deviceCodeId = deviceCodeId;
    }

    public Long getDeviceCodeId()
    {
        return deviceCodeId;
    }
    public void setLastLoginTime(Date lastLoginTime)
    {
        this.lastLoginTime = lastLoginTime;
    }

    public Date getLastLoginTime()
    {
        return lastLoginTime;
    }
    public void setLoginTimes(Long loginTimes)
    {
        this.loginTimes = loginTimes;
    }

    public Long getLoginTimes()
    {
        return loginTimes;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("appUserId", getAppUserId())
                .append("deviceCodeId", getDeviceCodeId())
                .append("lastLoginTime", getLastLoginTime())
                .append("loginTimes", getLoginTimes())
                .append("status", getStatus())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}