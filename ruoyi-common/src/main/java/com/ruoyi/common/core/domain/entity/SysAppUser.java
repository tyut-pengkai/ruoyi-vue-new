package com.ruoyi.common.core.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excels;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

public class SysAppUser extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 软件用户ID */
    private Long appUserId;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 软件ID */
    @Excel(name = "软件ID")
    private Long appId;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /**
     * 登录用户数量限制，整数，-1为不限制，默认为-1
     */
    @Excel(name = "登录用户数量限制，整数，-1为不限制，默认为-1")
    private Integer loginLimitU;

    /**
     * 登录机器数量限制，整数，-1为不限制，默认为-1
     */
    @Excel(name = "登录机器数量限制，整数，-1为不限制，默认为-1")
    private Integer loginLimitM;

    /** 免费余额 */
    @Excel(name = "免费余额")
    private BigDecimal freeBalance;

    /** 支付余额 */
    @Excel(name = "支付余额")
    private BigDecimal payBalance;

    /** 总消费 */
    @Excel(name = "总消费")
    private BigDecimal totalPay;

    /** 最后登录时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "最后登录时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date lastLoginTime;

    /** 登录次数 */
    @Excel(name = "登录次数")
    private Long loginTimes;

    /** 密码连续错误次数 */
    private Integer pwdErrorTimes;

    /** 过期时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "过期时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date expireTime;

    /**
     * 剩余点数
     */
    @Excel(name = "剩余点数")
    private BigDecimal point;

    /** 登录码 */
    @Excel(name = "登录码")
    private String loginCode;

    /**
     * 所属账号信息
     */
    @Excels({
            @Excel(name = "用户账号", targetAttr = "userName", type = Excel.Type.EXPORT),
            @Excel(name = "用户昵称", targetAttr = "nickName", type = Excel.Type.EXPORT)
    })
    private SysUser user;

    /**
     * 所属软件信息
     */
    @Excel(name = "软件名称", targetAttr = "appName", type = Excel.Type.EXPORT)
    private SysApp app;

    /**
     * 所属用户用户名称，用户承载搜索参数
     */
    private String userName;

    public void setAppUserId(Long appUserId) 
    {
        this.appUserId = appUserId;
    }

    public Long getAppUserId() 
    {
        return appUserId;
    }
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setAppId(Long appId) 
    {
        this.appId = appId;
    }

    public Long getAppId() 
    {
        return appId;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public Integer getLoginLimitU() {
        return loginLimitU;
    }

    public void setLoginLimitU(Integer loginLimitU) {
        this.loginLimitU = loginLimitU;
    }

    public Integer getLoginLimitM() {
        return loginLimitM;
    }

    public void setLoginLimitM(Integer loginLimitM) {
        this.loginLimitM = loginLimitM;
    }

    public void setFreeBalance(BigDecimal freeBalance) {
        this.freeBalance = freeBalance;
    }

    public BigDecimal getFreeBalance() {
        return freeBalance;
    }

    public void setPayBalance(BigDecimal payBalance)
    {
        this.payBalance = payBalance;
    }

    public BigDecimal getPayBalance() 
    {
        return payBalance;
    }
    public void setTotalPay(BigDecimal totalPay) 
    {
        this.totalPay = totalPay;
    }

    public BigDecimal getTotalPay() 
    {
        return totalPay;
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
    public void setPwdErrorTimes(Integer pwdErrorTimes)
    {
        this.pwdErrorTimes = pwdErrorTimes;
    }

    public Integer getPwdErrorTimes() {
        return pwdErrorTimes;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public BigDecimal getPoint() {
        return point;
    }

    public void setPoint(BigDecimal point) {
        this.point = point;
    }

    public void setLoginCode(String loginCode) {
        this.loginCode = loginCode;
    }

    public String getLoginCode() 
    {
        return loginCode;
    }

    public SysUser getUser() {
        return user;
    }

    public void setUser(SysUser user) {
        this.user = user;
    }

    public SysApp getApp() {
        return app;
    }

    public void setApp(SysApp app) {
        this.app = app;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("appUserId", getAppUserId())
            .append("userId", getUserId())
            .append("appId", getAppId())
            .append("status", getStatus())
            .append("loginLimitU", getLoginLimitU())
            .append("loginLimitM", getLoginLimitM())
            .append("freeBalance", getFreeBalance())
            .append("payBalance", getPayBalance())
            .append("totalPay", getTotalPay())
            .append("lastLoginTime", getLastLoginTime())
            .append("loginTimes", getLoginTimes())
            .append("pwdErrorTimes", getPwdErrorTimes())
            .append("expireTime", getExpireTime())
            .append("point", getPoint())
            .append("loginCode", getLoginCode())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
