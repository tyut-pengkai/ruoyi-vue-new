package com.ruoyi.common.core.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excels;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

public class SysAppUser extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 软件用户ID
     */
    private Long appUserId;

    /**
     * 用户ID
     */
    @Excel(name = "用户ID")
    private Long userId;

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
     * 登录用户数量限制，整数，-1为不限制，默认为-1
     */
    @Excel(name = "登录用户数量限制，整数，-1为不限制，默认为-1")
    private Integer loginLimitU;

    /**
     * 登录机器数量限制，整数，-1为不限制，默认为-1
     */
    @Excel(name = "登录机器数量限制，整数，-1为不限制，默认为-1")
    private Integer loginLimitM;

    /**
     * 免费余额
     */
    @Excel(name = "免费余额")
    private BigDecimal freeBalance;

    /**
     * 支付余额
     */
    @Excel(name = "支付余额")
    private BigDecimal payBalance;

    /**
     * 免费消费
     */
    @Excel(name = "免费消费")
    private BigDecimal freePayment;

    /**
     * 支付消费
     */
    @Excel(name = "支付消费")
    private BigDecimal payPayment;

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
     * 密码连续错误次数
     */
    private Integer pwdErrorTimes;

    /**
     * 过期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "过期时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date expireTime;

    /**
     * 剩余点数
     */
    @Excel(name = "剩余点数")
    private BigDecimal point;

    /**
     * 单码
     */
    @Excel(name = "单码")
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

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    /**
     * 登录用户数量限制，整数，-1为不限制，默认为-1
     */
    @Excel(name = "由卡密继承来的登录用户数量限制，整数，-1为不限制，-2为不生效，默认为-2")
    private Integer cardLoginLimitU;

    /**
     * 登录机器数量限制，整数，-1为不限制，默认为-1
     */
    @Excel(name = "由卡密继承来的登录机器数量限制，整数，-1为不限制，-2为不生效，默认为-2")
    private Integer cardLoginLimitM;

    /**
     * 卡密自定义参数
     */
    @Excel(name = "卡密自定义参数")
    private String cardCustomParams;

    private Integer effectiveLoginLimitU;

    private Integer effectiveLoginLimitM;

    private Integer currentOnlineU;

    private Integer currentOnlineM;

    /**
     * 可解绑次数
     */
    private Integer unbindTimes;

    /**
     * 最后充值的卡类ID，用于获取是否可解绑信息
     */
    private Long lastChargeTemplateId;

    public Long getLastChargeTemplateId() {
        return lastChargeTemplateId;
    }

    public void setLastChargeTemplateId(Long lastChargeTemplateId) {
        this.lastChargeTemplateId = lastChargeTemplateId;
    }

    public Integer getUnbindTimes() {
        return unbindTimes;
    }

    public void setUnbindTimes(Integer unbindTimes) {
        this.unbindTimes = unbindTimes;
    }

    public Integer getCurrentOnlineU() {
        return currentOnlineU;
    }

    public void setCurrentOnlineU(Integer currentOnlineU) {
        this.currentOnlineU = currentOnlineU;
    }

    public Integer getCurrentOnlineM() {
        return currentOnlineM;
    }

    public void setCurrentOnlineM(Integer currentOnlineM) {
        this.currentOnlineM = currentOnlineM;
    }

    public Integer getEffectiveLoginLimitU() {
        return effectiveLoginLimitU;
    }

    public void setEffectiveLoginLimitU(Integer effectiveLoginLimitU) {
        this.effectiveLoginLimitU = effectiveLoginLimitU;
    }

    public Integer getEffectiveLoginLimitM() {
        return effectiveLoginLimitM;
    }

    public void setEffectiveLoginLimitM(Integer effectiveLoginLimitM) {
        this.effectiveLoginLimitM = effectiveLoginLimitM;
    }

    public Integer getCardLoginLimitU() {
        return cardLoginLimitU;
    }

    public void setCardLoginLimitU(Integer cardLoginLimitU) {
        this.cardLoginLimitU = cardLoginLimitU;
    }

    public Integer getCardLoginLimitM() {
        return cardLoginLimitM;
    }

    public void setCardLoginLimitM(Integer cardLoginLimitM) {
        this.cardLoginLimitM = cardLoginLimitM;
    }

    public String getCardCustomParams() {
        return cardCustomParams;
    }

    public void setCardCustomParams(String cardCustomParams) {
        this.cardCustomParams = cardCustomParams;
    }

    public Long getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(Long appUserId) {
        this.appUserId = appUserId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public void setStatus(String status) {
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

    public BigDecimal getPayBalance() {
        return payBalance;
    }

    public void setPayBalance(BigDecimal payBalance) {
        this.payBalance = payBalance;
    }

    public BigDecimal getFreePayment() {
        return freePayment;
    }

    public void setFreePayment(BigDecimal freePayment) {
        this.freePayment = freePayment;
    }

    public BigDecimal getPayPayment() {
        return payPayment;
    }

    public void setPayPayment(BigDecimal payPayment) {
        this.payPayment = payPayment;
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

    public void setPwdErrorTimes(Integer pwdErrorTimes) {
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

    public String getLoginCode() {
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

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("appUserId", getAppUserId())
                .append("userId", getUserId())
                .append("appId", getAppId())
                .append("status", getStatus())
                .append("loginLimitU", getLoginLimitU())
                .append("loginLimitM", getLoginLimitM())
                .append("freeBalance", getFreeBalance())
                .append("payBalance", getPayBalance())
                .append("totalPay", getFreePayment())
                .append("totalPay", getPayBalance())
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
