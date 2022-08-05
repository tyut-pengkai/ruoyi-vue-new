package com.ruoyi.common.core.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 试用信息对象 sys_app_trial
 *
 * @author zwgu
 * @date 2022-08-01
 */
public class SysAppTrialUser extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long appTrialUserId;

    /**
     * 软件ID
     */
    @Excel(name = "软件ID")
    private Long appId;

    /**
     * 状态（0正常 1停用）
     */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /**
     * 最后登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "最后登录时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date lastLoginTime;

    /**
     * 单次已试用次数
     */
    @Excel(name = "单次已试用次数")
    private Long loginTimes;

    /**
     * 总已试用次数
     */
    @Excel(name = "总已试用次数")
    private Long loginTimesAll;

    /**
     * 下次试用时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "下次试用时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date nextEnableTime;

    /**
     * 过期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "过期时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date expireTime;

    /**
     * 登录IP
     */
    @Excel(name = "登录IP")
    private String loginIp;

    /**
     * 设备码
     */
    @Excel(name = "设备码")
    private String deviceCode;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getAppTrialUserId() {
        return appTrialUserId;
    }

    public void setAppTrialUserId(Long appTrialUserId) {
        this.appTrialUserId = appTrialUserId;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Long getLoginTimes() {
        return loginTimes;
    }

    public void setLoginTimes(Long loginTimes) {
        this.loginTimes = loginTimes;
    }

    public Long getLoginTimesAll() {
        return loginTimesAll;
    }

    public void setLoginTimesAll(Long loginTimesAll) {
        this.loginTimesAll = loginTimesAll;
    }

    public Date getNextEnableTime() {
        return nextEnableTime;
    }

    public void setNextEnableTime(Date nextEnableTime) {
        this.nextEnableTime = nextEnableTime;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("appTrialId", getAppTrialUserId())
                .append("appId", getAppId())
                .append("status", getStatus())
                .append("lastLoginTime", getLastLoginTime())
                .append("loginTimes", getLoginTimes())
                .append("loginTimesAll", getLoginTimesAll())
                .append("nextEnableTime", getNextEnableTime())
                .append("expireTime", getExpireTime())
                .append("loginIp", getLoginIp())
                .append("deviceCode", getDeviceCode())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .append("delFlag", getDelFlag())
                .toString();
    }
}
