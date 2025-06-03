package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 项目款项对象 oa_project_payment
 * 
 * @author ruoyi
 * @date 2025-06-03
 */
public class OaProjectPayment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 回款ID */
    private Long id;

    /** 项目ID */
    @Excel(name = "项目ID")
    private Long projectId;

    /** 款项类型(0:税金/1:回款/2:出款) */
    @Excel(name = "款项类型(0:税金/1:回款/2:出款)")
    private Integer paymentType;

    /** 金额 */
    @Excel(name = "金额")
    private BigDecimal amount;

    /** 支付日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "支付日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date paymentDate;

    /** 税点 */
    @Excel(name = "税点")
    private BigDecimal taxPoint;

    /** 银行名称 */
    @Excel(name = "银行名称")
    private String bankName;

    /** 银行账号 */
    @Excel(name = "银行账号")
    private String bankAccount;

    /** 发票类型 */
    @Excel(name = "发票类型")
    private String invoiceType;

    /** 开票日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "开票日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date invoiceDate;

    /** 创建人 */
    @Excel(name = "创建人")
    private Long createdBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdTime;

    /** 更新人 */
    @Excel(name = "更新人")
    private Long updatedBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date updatedTime;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setProjectId(Long projectId) 
    {
        this.projectId = projectId;
    }

    public Long getProjectId() 
    {
        return projectId;
    }

    public void setPaymentType(Integer paymentType) 
    {
        this.paymentType = paymentType;
    }

    public Integer getPaymentType() 
    {
        return paymentType;
    }

    public void setAmount(BigDecimal amount) 
    {
        this.amount = amount;
    }

    public BigDecimal getAmount() 
    {
        return amount;
    }

    public void setPaymentDate(Date paymentDate) 
    {
        this.paymentDate = paymentDate;
    }

    public Date getPaymentDate() 
    {
        return paymentDate;
    }

    public void setTaxPoint(BigDecimal taxPoint) 
    {
        this.taxPoint = taxPoint;
    }

    public BigDecimal getTaxPoint() 
    {
        return taxPoint;
    }

    public void setBankName(String bankName) 
    {
        this.bankName = bankName;
    }

    public String getBankName() 
    {
        return bankName;
    }

    public void setBankAccount(String bankAccount) 
    {
        this.bankAccount = bankAccount;
    }

    public String getBankAccount() 
    {
        return bankAccount;
    }

    public void setInvoiceType(String invoiceType) 
    {
        this.invoiceType = invoiceType;
    }

    public String getInvoiceType() 
    {
        return invoiceType;
    }

    public void setInvoiceDate(Date invoiceDate) 
    {
        this.invoiceDate = invoiceDate;
    }

    public Date getInvoiceDate() 
    {
        return invoiceDate;
    }

    public void setCreatedBy(Long createdBy) 
    {
        this.createdBy = createdBy;
    }

    public Long getCreatedBy() 
    {
        return createdBy;
    }

    public void setCreatedTime(Date createdTime) 
    {
        this.createdTime = createdTime;
    }

    public Date getCreatedTime() 
    {
        return createdTime;
    }

    public void setUpdatedBy(Long updatedBy) 
    {
        this.updatedBy = updatedBy;
    }

    public Long getUpdatedBy() 
    {
        return updatedBy;
    }

    public void setUpdatedTime(Date updatedTime) 
    {
        this.updatedTime = updatedTime;
    }

    public Date getUpdatedTime() 
    {
        return updatedTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("projectId", getProjectId())
            .append("paymentType", getPaymentType())
            .append("amount", getAmount())
            .append("paymentDate", getPaymentDate())
            .append("taxPoint", getTaxPoint())
            .append("bankName", getBankName())
            .append("bankAccount", getBankAccount())
            .append("invoiceType", getInvoiceType())
            .append("invoiceDate", getInvoiceDate())
            .append("createdBy", getCreatedBy())
            .append("createdTime", getCreatedTime())
            .append("updatedBy", getUpdatedBy())
            .append("updatedTime", getUpdatedTime())
            .toString();
    }
}
