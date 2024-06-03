package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 提现配置对象 sys_config_withdraw
 *
 * @author zwgu
 * @date 2024-06-03
 */
public class SysConfigWithdraw extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 提现总开关
     */
    @Excel(name = "提现总开关")
    private String enableWithdrawCash;

    /**
     * 最低提现
     */
    @Excel(name = "最低提现")
    private BigDecimal withdrawCashMin;

    /**
     * 最高提现
     */
    @Excel(name = "最高提现")
    private BigDecimal withdrawCashMax;

    /**
     * 手续费百分比
     */
    @Excel(name = "手续费百分比")
    private Integer handlingFeeRate;

    /**
     * 最低手续费
     */
    @Excel(name = "最低手续费")
    private BigDecimal handlingFeeMin;

    /**
     * 最高手续费
     */
    @Excel(name = "最高手续费")
    private BigDecimal handlingFeeMax;

    /**
     * 支付宝结算
     */
    @Excel(name = "支付宝结算")
    private String enableAlipay;

    /**
     * 微信结算
     */
    @Excel(name = "微信结算")
    private String enableWechat;

    /**
     * QQ钱包结算
     */
    @Excel(name = "QQ钱包结算")
    private String enableQq;

    /**
     * 银行卡结算
     */
    @Excel(name = "银行卡结算")
    private String enableBankCard;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setEnableWithdrawCash(String enableWithdrawCash) {
        this.enableWithdrawCash = enableWithdrawCash;
    }

    public String getEnableWithdrawCash() {
        return enableWithdrawCash;
    }

    public void setWithdrawCashMin(BigDecimal withdrawCashMin) {
        this.withdrawCashMin = withdrawCashMin;
    }

    public BigDecimal getWithdrawCashMin() {
        return withdrawCashMin;
    }

    public void setWithdrawCashMax(BigDecimal withdrawCashMax) {
        this.withdrawCashMax = withdrawCashMax;
    }

    public BigDecimal getWithdrawCashMax() {
        return withdrawCashMax;
    }

    public void setHandlingFeeRate(Integer handlingFeeRate) {
        this.handlingFeeRate = handlingFeeRate;
    }

    public Integer getHandlingFeeRate() {
        return handlingFeeRate;
    }

    public void setHandlingFeeMin(BigDecimal handlingFeeMin) {
        this.handlingFeeMin = handlingFeeMin;
    }

    public BigDecimal getHandlingFeeMin() {
        return handlingFeeMin;
    }

    public void setHandlingFeeMax(BigDecimal handlingFeeMax) {
        this.handlingFeeMax = handlingFeeMax;
    }

    public BigDecimal getHandlingFeeMax() {
        return handlingFeeMax;
    }

    public void setEnableAlipay(String enableAlipay) {
        this.enableAlipay = enableAlipay;
    }

    public String getEnableAlipay() {
        return enableAlipay;
    }

    public void setEnableWechat(String enableWechat) {
        this.enableWechat = enableWechat;
    }

    public String getEnableWechat() {
        return enableWechat;
    }

    public void setEnableQq(String enableQq) {
        this.enableQq = enableQq;
    }

    public String getEnableQq() {
        return enableQq;
    }

    public void setEnableBankCard(String enableBankCard) {
        this.enableBankCard = enableBankCard;
    }

    public String getEnableBankCard() {
        return enableBankCard;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("enableWithdrawCash", getEnableWithdrawCash())
                .append("withdrawCashMin", getWithdrawCashMin())
                .append("withdrawCashMax", getWithdrawCashMax())
                .append("handlingFeeRate", getHandlingFeeRate())
                .append("handlingFeeMin", getHandlingFeeMin())
                .append("handlingFeeMax", getHandlingFeeMax())
                .append("enableAlipay", getEnableAlipay())
                .append("enableWechat", getEnableWechat())
                .append("enableQq", getEnableQq())
                .append("enableBankCard", getEnableBankCard())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
