package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excels;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.enums.BalanceChangeType;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 余额变动对象 sys_balance_log
 *
 * @author zwgu
 * @date 2022-06-16
 */
public class SysBalanceLog extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 钱包明细 id
     */
    private Long id;

    /**
     * 明细所属用户 id
     */
    @Excel(name = "明细所属用户 id")
    private Long userId;

    /**
     * 金额来源用户
     */
    @Excel(name = "金额来源用户")
    private Long sourceUserId;

    /**
     * 变动可用充值金额
     */
    @Excel(name = "变动可用充值金额")
    private BigDecimal changeAvailablePayAmount;

    /**
     * 变动冻结充值金额
     */
    @Excel(name = "变动冻结充值金额")
    private BigDecimal changeFreezePayAmount;

    /**
     * 1：提现冻结，2：提现成功，3：撤销提现解冻； 4：代理分成，5：推广分成，6：转账收入，7：其他收入，8：消费支出，9：转账支出； 10：其他支出
     */
    @Excel(name = "1：提现冻结，2：提现成功，3：撤销提现解冻； 4：代理分成，5：推广分成，6：转账收入，7：其他收入，8：消费支出，9：转账支出； 10：其他支出")
    private BalanceChangeType changeType;

    /**
     * 变动可用赠送金额
     */
    @Excel(name = "变动可用赠送金额")
    private BigDecimal changeAvailableFreeAmount;

    /**
     * 变动冻结赠送金额
     */
    @Excel(name = "变动冻结赠送金额")
    private BigDecimal changeFreezeFreeAmount;

    /**
     * 冻结赠送余额后
     */
    @Excel(name = "冻结赠送余额后")
    private BigDecimal freezeFreeAfter;

    /**
     * 冻结赠送余额前
     */
    @Excel(name = "冻结赠送余额前")
    private BigDecimal freezeFreeBefore;

    /**
     * 可用赠送余额后
     */
    @Excel(name = "可用赠送余额后")
    private BigDecimal availableFreeAfter;

    /**
     * 可用赠送余额前
     */
    @Excel(name = "可用赠送余额前")
    private BigDecimal availableFreeBefore;

    /**
     * 冻结充值余额后
     */
    @Excel(name = "冻结充值余额后")
    private BigDecimal freezePayAfter;

    /**
     * 冻结充值余额前
     */
    @Excel(name = "冻结充值余额前")
    private BigDecimal freezePayBefore;

    /**
     * 可用充值余额后
     */
    @Excel(name = "可用充值余额后")
    private BigDecimal availablePayAfter;

    /**
     * 可用充值余额前
     */
    @Excel(name = "可用充值余额前")
    private BigDecimal availablePayBefore;

    /**
     * 变动描述
     */
    @Excel(name = "变动描述")
    private String changeDesc;

    /**
     * 关联订单记录ID
     */
    @Excel(name = "关联订单记录ID")
    private Long saleOrderId;

    /**
     * 关联提现记录ID
     */
    @Excel(name = "关联提现记录ID")
    private Long withdrawCashId;

    /**
     * 用户信息
     */
    @Excels({
            @Excel(name = "用户账号", targetAttr = "userName", type = Excel.Type.EXPORT),
            @Excel(name = "用户昵称", targetAttr = "nickName", type = Excel.Type.EXPORT)
    })
    private SysUser user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getSourceUserId() {
        return sourceUserId;
    }

    public void setSourceUserId(Long sourceUserId) {
        this.sourceUserId = sourceUserId;
    }

    public BigDecimal getChangeAvailablePayAmount() {
        return changeAvailablePayAmount;
    }

    public void setChangeAvailablePayAmount(BigDecimal changeAvailablePayAmount) {
        this.changeAvailablePayAmount = changeAvailablePayAmount;
    }

    public BigDecimal getChangeFreezePayAmount() {
        return changeFreezePayAmount;
    }

    public void setChangeFreezePayAmount(BigDecimal changeFreezePayAmount) {
        this.changeFreezePayAmount = changeFreezePayAmount;
    }

    public BalanceChangeType getChangeType() {
        return changeType;
    }

    public void setChangeType(BalanceChangeType changeType) {
        this.changeType = changeType;
    }

    public BigDecimal getChangeAvailableFreeAmount() {
        return changeAvailableFreeAmount;
    }

    public void setChangeAvailableFreeAmount(BigDecimal changeAvailableFreeAmount) {
        this.changeAvailableFreeAmount = changeAvailableFreeAmount;
    }

    public BigDecimal getChangeFreezeFreeAmount() {
        return changeFreezeFreeAmount;
    }

    public void setChangeFreezeFreeAmount(BigDecimal changeFreezeFreeAmount) {
        this.changeFreezeFreeAmount = changeFreezeFreeAmount;
    }

    public BigDecimal getFreezeFreeAfter() {
        return freezeFreeAfter;
    }

    public void setFreezeFreeAfter(BigDecimal freezeFreeAfter) {
        this.freezeFreeAfter = freezeFreeAfter;
    }

    public BigDecimal getFreezeFreeBefore() {
        return freezeFreeBefore;
    }

    public void setFreezeFreeBefore(BigDecimal freezeFreeBefore) {
        this.freezeFreeBefore = freezeFreeBefore;
    }

    public BigDecimal getAvailableFreeAfter() {
        return availableFreeAfter;
    }

    public void setAvailableFreeAfter(BigDecimal availableFreeAfter) {
        this.availableFreeAfter = availableFreeAfter;
    }

    public BigDecimal getAvailableFreeBefore() {
        return availableFreeBefore;
    }

    public void setAvailableFreeBefore(BigDecimal availableFreeBefore) {
        this.availableFreeBefore = availableFreeBefore;
    }

    public BigDecimal getFreezePayAfter() {
        return freezePayAfter;
    }

    public void setFreezePayAfter(BigDecimal freezePayAfter) {
        this.freezePayAfter = freezePayAfter;
    }

    public BigDecimal getFreezePayBefore() {
        return freezePayBefore;
    }

    public void setFreezePayBefore(BigDecimal freezePayBefore) {
        this.freezePayBefore = freezePayBefore;
    }

    public BigDecimal getAvailablePayAfter() {
        return availablePayAfter;
    }

    public void setAvailablePayAfter(BigDecimal availablePayAfter) {
        this.availablePayAfter = availablePayAfter;
    }

    public BigDecimal getAvailablePayBefore() {
        return availablePayBefore;
    }

    public void setAvailablePayBefore(BigDecimal availablePayBefore) {
        this.availablePayBefore = availablePayBefore;
    }

    public String getChangeDesc() {
        return changeDesc;
    }

    public void setChangeDesc(String changeDesc) {
        this.changeDesc = changeDesc;
    }

    public Long getSaleOrderId() {
        return saleOrderId;
    }

    public void setSaleOrderId(Long saleOrderId) {
        this.saleOrderId = saleOrderId;
    }

    public Long getWithdrawCashId() {
        return withdrawCashId;
    }

    public void setWithdrawCashId(Long withdrawCashId) {
        this.withdrawCashId = withdrawCashId;
    }

    public SysUser getUser() {
        return user;
    }

    public void setUser(SysUser user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("userId", getUserId())
                .append("sourceUserId", getSourceUserId())
                .append("changeAvailablePayAmount", getChangeAvailablePayAmount())
                .append("changeFreezePayAmount", getChangeFreezePayAmount())
                .append("changeType", getChangeType())
                .append("changeAvailableFreeAmount", getChangeAvailableFreeAmount())
                .append("changeFreezeFreeAmount", getChangeFreezeFreeAmount())
                .append("freezeFreeAfter", getFreezeFreeAfter())
                .append("freezeFreeBefore", getFreezeFreeBefore())
                .append("availableFreeAfter", getAvailableFreeAfter())
                .append("availableFreeBefore", getAvailableFreeBefore())
                .append("freezePayAfter", getFreezePayAfter())
                .append("freezePayBefore", getFreezePayBefore())
                .append("availablePayAfter", getAvailablePayAfter())
                .append("availablePayBefore", getAvailablePayBefore())
                .append("changeDesc", getChangeDesc())
                .append("saleOrderId", getSaleOrderId())
                .append("withdrawCashId", getWithdrawCashId())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
