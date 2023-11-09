package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.core.domain.entity.SysApp;
import com.ruoyi.common.core.domain.entity.SysAppUser;
import com.ruoyi.common.enums.UnbindType;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 解绑日志对象 sys_unbind_log
 *
 * @author zwgu
 * @date 2023-11-08
 */
public class SysUnbindLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 软件用户id */
    @Excel(name = "软件用户id")
    private Long appUserId;

    /** 软件id */
    @Excel(name = "软件id")
    private Long appId;

    /** 首次登录时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "首次登录时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date firstLoginTime;

    /** 最后登录时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "最后登录时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date lastLoginTime;

    /** 登录次数 */
    @Excel(name = "登录次数")
    private Long loginTimes;

    /** 变动面值 */
    @Excel(name = "变动面值")
    private Long changeAmount;

    /** 1：用户前台解绑，2：用户软件解绑(API)，3：管理员后台解绑 */
    @Excel(name = "解绑类型", dictType = "sys_unbind_type")
    private UnbindType unbindType;

    /** 解绑描述 */
    @Excel(name = "解绑描述")
    private String unbindDesc;

    /** 用户过期时间后 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "用户过期时间后", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date expireTimeAfter;

    /** 用户过期时间前 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "用户过期时间前", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date expireTimeBefore;

    /** 用户剩余点数后 */
    @Excel(name = "用户剩余点数后")
    private BigDecimal pointAfter;

    /** 用户剩余点数前 */
    @Excel(name = "用户剩余点数前")
    private BigDecimal pointBefore;

    /** 设备码 */
    @Excel(name = "设备码")
    private String deviceCode;

    /** 设备码id */
    @Excel(name = "设备码id")
    private Long deviceCodeId;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    private String userName;

    private SysAppUser appUser;

    private SysApp app;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public SysAppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(SysAppUser appUser) {
        this.appUser = appUser;
    }

    public SysApp getApp() {
        return app;
    }

    public void setApp(SysApp app) {
        this.app = app;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
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
    public void setAppId(Long appId)
    {
        this.appId = appId;
    }

    public Long getAppId()
    {
        return appId;
    }
    public void setFirstLoginTime(Date firstLoginTime)
    {
        this.firstLoginTime = firstLoginTime;
    }

    public Date getFirstLoginTime()
    {
        return firstLoginTime;
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
    public void setChangeAmount(Long changeAmount)
    {
        this.changeAmount = changeAmount;
    }

    public Long getChangeAmount()
    {
        return changeAmount;
    }
    public void setUnbindType(UnbindType unbindType)
    {
        this.unbindType = unbindType;
    }

    public UnbindType getUnbindType()
    {
        return unbindType;
    }
    public void setUnbindDesc(String unbindDesc)
    {
        this.unbindDesc = unbindDesc;
    }

    public String getUnbindDesc()
    {
        return unbindDesc;
    }
    public void setExpireTimeAfter(Date expireTimeAfter)
    {
        this.expireTimeAfter = expireTimeAfter;
    }

    public Date getExpireTimeAfter()
    {
        return expireTimeAfter;
    }
    public void setExpireTimeBefore(Date expireTimeBefore)
    {
        this.expireTimeBefore = expireTimeBefore;
    }

    public Date getExpireTimeBefore()
    {
        return expireTimeBefore;
    }
    public void setPointAfter(BigDecimal pointAfter)
    {
        this.pointAfter = pointAfter;
    }

    public BigDecimal getPointAfter()
    {
        return pointAfter;
    }
    public void setPointBefore(BigDecimal pointBefore)
    {
        this.pointBefore = pointBefore;
    }

    public BigDecimal getPointBefore()
    {
        return pointBefore;
    }
    public void setDeviceCode(String deviceCode)
    {
        this.deviceCode = deviceCode;
    }

    public String getDeviceCode()
    {
        return deviceCode;
    }
    public void setDeviceCodeId(Long deviceCodeId)
    {
        this.deviceCodeId = deviceCodeId;
    }

    public Long getDeviceCodeId()
    {
        return deviceCodeId;
    }
    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("appUserId", getAppUserId())
            .append("appId", getAppId())
            .append("firstLoginTime", getFirstLoginTime())
            .append("lastLoginTime", getLastLoginTime())
            .append("loginTimes", getLoginTimes())
            .append("changeAmount", getChangeAmount())
            .append("unbindType", getUnbindType())
            .append("unbindDesc", getUnbindDesc())
            .append("expireTimeAfter", getExpireTimeAfter())
            .append("expireTimeBefore", getExpireTimeBefore())
            .append("pointAfter", getPointAfter())
            .append("pointBefore", getPointBefore())
            .append("deviceCode", getDeviceCode())
            .append("deviceCodeId", getDeviceCodeId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
