package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.core.domain.entity.SysApp;
import com.ruoyi.common.core.domain.entity.SysAppUser;
import com.ruoyi.common.enums.AppUserExpireChangeType;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 时长或点数变动对象 sys_app_user_expire_log
 *
 * @author zwgu
 * @date 2023-01-05
 */
public class SysAppUserExpireLog extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 软件用户剩余时间或点数id
     */
    private Long id;

    /**
     * 软件id
     */
    @Excel(name = "软件id")
    private Long appId;

    /**
     * 软件用户id
     */
    @Excel(name = "软件用户id")
    private Long appUserId;

    /**
     * 变动面值
     */
    @Excel(name = "变动面值")
    private Long changeAmount;

    /**
     * 1：充值，2：其他收入，3：其他支出
     */
    @Excel(name = "1：充值，2：其他收入，3：其他支出")
    private AppUserExpireChangeType changeType;

    /**
     * 用户过期时间后
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "用户过期时间后", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date expireTimeAfter;

    /**
     * 用户过期时间前
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "用户过期时间前", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date expireTimeBefore;

    /**
     * 用户剩余点数后
     */
    @Excel(name = "用户剩余点数后")
    private BigDecimal pointAfter;

    /**
     * 用户剩余点数前
     */
    @Excel(name = "用户剩余点数前")
    private BigDecimal pointBefore;

    /**
     * 变动描述
     */
    @Excel(name = "变动描述")
    private String changeDesc;

    /**
     * 关联卡类ID
     */
    @Excel(name = "关联卡类ID")
    private Long templateId;

    /**
     * 关联卡ID
     */
    @Excel(name = "关联卡ID")
    private Long cardId;

    /**
     * 关联卡号
     */
    @Excel(name = "关联卡号")
    private String cardNo;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
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

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(Long appUserId) {
        this.appUserId = appUserId;
    }

    public Long getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(Long changeAmount) {
        this.changeAmount = changeAmount;
    }

    public AppUserExpireChangeType getChangeType() {
        return changeType;
    }

    public void setChangeType(AppUserExpireChangeType changeType) {
        this.changeType = changeType;
    }

    public Date getExpireTimeAfter() {
        return expireTimeAfter;
    }

    public void setExpireTimeAfter(Date expireTimeAfter) {
        this.expireTimeAfter = expireTimeAfter;
    }

    public Date getExpireTimeBefore() {
        return expireTimeBefore;
    }

    public void setExpireTimeBefore(Date expireTimeBefore) {
        this.expireTimeBefore = expireTimeBefore;
    }

    public BigDecimal getPointAfter() {
        return pointAfter;
    }

    public void setPointAfter(BigDecimal pointAfter) {
        this.pointAfter = pointAfter;
    }

    public BigDecimal getPointBefore() {
        return pointBefore;
    }

    public void setPointBefore(BigDecimal pointBefore) {
        this.pointBefore = pointBefore;
    }

    public String getChangeDesc() {
        return changeDesc;
    }

    public void setChangeDesc(String changeDesc) {
        this.changeDesc = changeDesc;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
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
                .append("id", getId())
                .append("appUserId", getAppUserId())
                .append("changeAmount", getChangeAmount())
                .append("changeType", getChangeType())
                .append("expireTimeAfter", getExpireTimeAfter())
                .append("expireTimeBefore", getExpireTimeBefore())
                .append("pointAfter", getPointAfter())
                .append("pointBefore", getPointBefore())
                .append("changeDesc", getChangeDesc())
                .append("templateId", getTemplateId())
                .append("cardId", getCardId())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .append("delFlag", getDelFlag())
                .toString();
    }
}
