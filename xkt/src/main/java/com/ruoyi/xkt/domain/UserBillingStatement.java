package com.ruoyi.xkt.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.XktBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 用户对账明细对象 user_billing_statement
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserBillingStatement extends XktBaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 用户账户明细ID
     */
    @TableId
    private Long id;

    /**
     * sys_user.id
     */
    @Excel(name = "sys_user.id")
    private Long userId;

    /**
     * user_account.id
     */
    @Excel(name = "user_account.id")
    private Long userAccId;

    /**
     * 账单code
     */
    @Excel(name = "账单code")
    private String code;

    /**
     * 交易类型
     */
    @Excel(name = "交易类型")
    private Long transType;

    /**
     * 交易备注
     */
    @Excel(name = "交易备注")
    private String transRemark;

    /**
     * 支付方式
     */
    @Excel(name = "支付方式")
    private Long payWay;

    /**
     * 收入
     */
    @Excel(name = "收入")
    private BigDecimal income;

    /**
     * 支出
     */
    @Excel(name = "支出")
    private BigDecimal expenses;

    /**
     * 账户余额
     */
    @Excel(name = "账户余额")
    private BigDecimal accBalance;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("userId", getUserId())
                .append("userAccId", getUserAccId())
                .append("code", getCode())
                .append("transType", getTransType())
                .append("transRemark", getTransRemark())
                .append("payWay", getPayWay())
                .append("income", getIncome())
                .append("expenses", getExpenses())
                .append("accBalance", getAccBalance())
                .append("version", getVersion())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
