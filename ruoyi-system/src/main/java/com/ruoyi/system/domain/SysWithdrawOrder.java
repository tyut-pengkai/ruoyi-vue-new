package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 提现记录对象 sys_withdraw_order
 * 
 * @author zwgu
 * @date 2024-06-03
 */
public class SysWithdrawOrder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 提现 id */
    private Long id;

    /** 提现用户 id */
    @Excel(name = "提现用户 id")
    private Long userId;

    /** 提现交易编号 */
    @Excel(name = "提现交易编号")
    private Long orderNo;

    /** 提现手续费 */
    @Excel(name = "提现手续费")
    private BigDecimal handlingFee;

    /** 提现申请金额 */
    @Excel(name = "提现申请金额")
    private BigDecimal applyFee;

    /** 实际提现金额 */
    @Excel(name = "实际提现金额")
    private BigDecimal actualFee;

    /** 提现状态：1：待审核，2：审核通过，3：审核不通过，4：待打款， 5，已打款， 6：打款失败 */
    @Excel(name = "提现状态：1：待审核，2：审核通过，3：审核不通过，4：待打款， 5，已打款， 6：打款失败")
    private String withdrawStatus;

    /** 是否人工转账 */
    @Excel(name = "是否人工转账")
    private String manualTransfer;

    /** 交易时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "交易时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date tradeTime;

    /** 交易号 */
    @Excel(name = "交易号")
    private String tradeNo;

    /** 错误代码 */
    @Excel(name = "错误代码")
    private String errorCode;

    /** 交易失败描述 */
    @Excel(name = "交易失败描述")
    private String errorMessage;

    /** 收款平台 */
    @Excel(name = "收款平台")
    private String receiveMethod;

    /** 收款账号 */
    @Excel(name = "收款账号")
    private String receiveAccount;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setOrderNo(Long orderNo) 
    {
        this.orderNo = orderNo;
    }

    public Long getOrderNo() 
    {
        return orderNo;
    }
    public void setHandlingFee(BigDecimal handlingFee) 
    {
        this.handlingFee = handlingFee;
    }

    public BigDecimal getHandlingFee() 
    {
        return handlingFee;
    }
    public void setApplyFee(BigDecimal applyFee) 
    {
        this.applyFee = applyFee;
    }

    public BigDecimal getApplyFee() 
    {
        return applyFee;
    }
    public void setActualFee(BigDecimal actualFee) 
    {
        this.actualFee = actualFee;
    }

    public BigDecimal getActualFee() 
    {
        return actualFee;
    }
    public void setWithdrawStatus(String withdrawStatus) 
    {
        this.withdrawStatus = withdrawStatus;
    }

    public String getWithdrawStatus() 
    {
        return withdrawStatus;
    }
    public void setManualTransfer(String manualTransfer) 
    {
        this.manualTransfer = manualTransfer;
    }

    public String getManualTransfer() 
    {
        return manualTransfer;
    }
    public void setTradeTime(Date tradeTime) 
    {
        this.tradeTime = tradeTime;
    }

    public Date getTradeTime() 
    {
        return tradeTime;
    }
    public void setTradeNo(String tradeNo) 
    {
        this.tradeNo = tradeNo;
    }

    public String getTradeNo() 
    {
        return tradeNo;
    }
    public void setErrorCode(String errorCode) 
    {
        this.errorCode = errorCode;
    }

    public String getErrorCode() 
    {
        return errorCode;
    }
    public void setErrorMessage(String errorMessage) 
    {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() 
    {
        return errorMessage;
    }
    public void setReceiveMethod(String receiveMethod) 
    {
        this.receiveMethod = receiveMethod;
    }

    public String getReceiveMethod() 
    {
        return receiveMethod;
    }
    public void setReceiveAccount(String receiveAccount) 
    {
        this.receiveAccount = receiveAccount;
    }

    public String getReceiveAccount() 
    {
        return receiveAccount;
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
            .append("userId", getUserId())
            .append("orderNo", getOrderNo())
            .append("handlingFee", getHandlingFee())
            .append("applyFee", getApplyFee())
            .append("actualFee", getActualFee())
            .append("withdrawStatus", getWithdrawStatus())
            .append("manualTransfer", getManualTransfer())
            .append("tradeTime", getTradeTime())
            .append("tradeNo", getTradeNo())
            .append("errorCode", getErrorCode())
            .append("errorMessage", getErrorMessage())
            .append("receiveMethod", getReceiveMethod())
            .append("receiveAccount", getReceiveAccount())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
