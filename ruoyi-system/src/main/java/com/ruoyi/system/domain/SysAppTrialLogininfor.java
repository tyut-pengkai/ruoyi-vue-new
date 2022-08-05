package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 系统访问记录对象 sys_app_trial_logininfor
 *
 * @author zwgu
 * @date 2021-12-29
 */
public class SysAppTrialLogininfor extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 访问ID
     */
    private Long infoId;

    /**
     * 用户名
     */
    @Excel(name = "用户名")
    private String userName;

    /**
     * 用户ID
     */
    @Excel(name = "用户ID")
    private Long appTrialUserId;

    /**
     * APP名
     */
    @Excel(name = "APP名")
    private String appName;

    /**
     * 登录IP地址
     */
    @Excel(name = "登录IP地址")
    private String ipaddr;

    /**
     * APP版本
     */
    @Excel(name = "APP版本")
    private String appVersion;

    /**
     * 登录地点
     */
    @Excel(name = "登录地点")
    private String loginLocation;

    /**
     * 设备码
     */
    @Excel(name = "设备码")
    private String deviceCode;

    /**
     * 浏览器类型
     */
    @Excel(name = "浏览器类型")
    private String browser;

    /**
     * 操作系统
     */
    @Excel(name = "操作系统")
    private String os;

    /**
     * 登录状态（0成功 1失败）
     */
    @Excel(name = "登录状态", readConverterExp = "0=成功,1=失败")
    private String status;

    /**
     * 提示消息
     */
    @Excel(name = "提示消息")
    private String msg;

    /**
     * 访问时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "访问时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date loginTime;

    public Long getInfoId() {
        return infoId;
    }

    public void setInfoId(Long infoId) {
        this.infoId = infoId;
    }

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

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getIpaddr() {
        return ipaddr;
    }

    public void setIpaddr(String ipaddr) {
        this.ipaddr = ipaddr;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getLoginLocation() {
        return loginLocation;
    }

    public void setLoginLocation(String loginLocation) {
        this.loginLocation = loginLocation;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("infoId", getInfoId())
                .append("userName", getUserName())
                .append("appTrialUserId", getAppTrialUserId())
                .append("appName", getAppName())
                .append("ipaddr", getIpaddr())
                .append("appVersion", getAppVersion())
                .append("loginLocation", getLoginLocation())
                .append("deviceCode", getDeviceCode())
                .append("browser", getBrowser())
                .append("os", getOs())
                .append("status", getStatus())
                .append("msg", getMsg())
                .append("loginTime", getLoginTime())
                .toString();
    }
}
