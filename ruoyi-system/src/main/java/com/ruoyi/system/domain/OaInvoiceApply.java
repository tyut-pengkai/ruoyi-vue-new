package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 开票申请对象 oa_invoice_apply
 * 
 * @author ruoyi
 * @date 2025-06-03
 */
public class OaInvoiceApply extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 开票申请ID */
    private Long id;

    /** 关联项目ID */
    @Excel(name = "关联项目ID")
    private Long projectId;

    /** 申请人ID */
    @Excel(name = "申请人ID")
    private Long applyUserId;

    /** 申请日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "申请日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date applyDate;

    /** 建设单位 */
    @Excel(name = "建设单位")
    private String constructionUnit;

    /** 纳税人识别号 */
    @Excel(name = "纳税人识别号")
    private String taxIdentNum;

    /** 地址电话 */
    @Excel(name = "地址电话")
    private String addressPhone;

    /** 开户行及账号 */
    @Excel(name = "开户行及账号")
    private String bankAccount;

    /** 工程名称 */
    @Excel(name = "工程名称")
    private String projectName;

    /** 合同金额 */
    @Excel(name = "合同金额")
    private BigDecimal contractAmount;

    /** 结算金额 */
    @Excel(name = "结算金额")
    private BigDecimal settlementAmount;

    /** 已开票金额 */
    @Excel(name = "已开票金额")
    private BigDecimal invoicedAmount;

    /** 未付款金额 */
    @Excel(name = "未付款金额")
    private BigDecimal unpaidInvoiceAmount;

    /** 申请开票金额 */
    @Excel(name = "申请开票金额")
    private BigDecimal applyAmount;

    /** 发票类型 */
    @Excel(name = "发票类型")
    private String invoiceType;

    /** 税率 */
    @Excel(name = "税率")
    private BigDecimal taxRate;

    /** 状态(0:草稿/1:审批中/2:已开票/3:已驳回) */
    @Excel(name = "状态(0:草稿/1:审批中/2:已开票/3:已驳回)")
    private Integer status;

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

    public void setApplyUserId(Long applyUserId) 
    {
        this.applyUserId = applyUserId;
    }

    public Long getApplyUserId() 
    {
        return applyUserId;
    }

    public void setApplyDate(Date applyDate) 
    {
        this.applyDate = applyDate;
    }

    public Date getApplyDate() 
    {
        return applyDate;
    }

    public void setConstructionUnit(String constructionUnit) 
    {
        this.constructionUnit = constructionUnit;
    }

    public String getConstructionUnit() 
    {
        return constructionUnit;
    }

    public void setTaxIdentNum(String taxIdentNum) 
    {
        this.taxIdentNum = taxIdentNum;
    }

    public String getTaxIdentNum() 
    {
        return taxIdentNum;
    }

    public void setAddressPhone(String addressPhone) 
    {
        this.addressPhone = addressPhone;
    }

    public String getAddressPhone() 
    {
        return addressPhone;
    }

    public void setBankAccount(String bankAccount) 
    {
        this.bankAccount = bankAccount;
    }

    public String getBankAccount() 
    {
        return bankAccount;
    }

    public void setProjectName(String projectName) 
    {
        this.projectName = projectName;
    }

    public String getProjectName() 
    {
        return projectName;
    }

    public void setContractAmount(BigDecimal contractAmount) 
    {
        this.contractAmount = contractAmount;
    }

    public BigDecimal getContractAmount() 
    {
        return contractAmount;
    }

    public void setSettlementAmount(BigDecimal settlementAmount) 
    {
        this.settlementAmount = settlementAmount;
    }

    public BigDecimal getSettlementAmount() 
    {
        return settlementAmount;
    }

    public void setInvoicedAmount(BigDecimal invoicedAmount) 
    {
        this.invoicedAmount = invoicedAmount;
    }

    public BigDecimal getInvoicedAmount() 
    {
        return invoicedAmount;
    }

    public void setUnpaidInvoiceAmount(BigDecimal unpaidInvoiceAmount) 
    {
        this.unpaidInvoiceAmount = unpaidInvoiceAmount;
    }

    public BigDecimal getUnpaidInvoiceAmount() 
    {
        return unpaidInvoiceAmount;
    }

    public void setApplyAmount(BigDecimal applyAmount) 
    {
        this.applyAmount = applyAmount;
    }

    public BigDecimal getApplyAmount() 
    {
        return applyAmount;
    }

    public void setInvoiceType(String invoiceType) 
    {
        this.invoiceType = invoiceType;
    }

    public String getInvoiceType() 
    {
        return invoiceType;
    }

    public void setTaxRate(BigDecimal taxRate) 
    {
        this.taxRate = taxRate;
    }

    public BigDecimal getTaxRate() 
    {
        return taxRate;
    }

    public void setStatus(Integer status) 
    {
        this.status = status;
    }

    public Integer getStatus() 
    {
        return status;
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
            .append("applyUserId", getApplyUserId())
            .append("applyDate", getApplyDate())
            .append("constructionUnit", getConstructionUnit())
            .append("taxIdentNum", getTaxIdentNum())
            .append("addressPhone", getAddressPhone())
            .append("bankAccount", getBankAccount())
            .append("projectName", getProjectName())
            .append("contractAmount", getContractAmount())
            .append("settlementAmount", getSettlementAmount())
            .append("invoicedAmount", getInvoicedAmount())
            .append("unpaidInvoiceAmount", getUnpaidInvoiceAmount())
            .append("applyAmount", getApplyAmount())
            .append("invoiceType", getInvoiceType())
            .append("taxRate", getTaxRate())
            .append("remark", getRemark())
            .append("status", getStatus())
            .append("createdBy", getCreatedBy())
            .append("createdTime", getCreatedTime())
            .append("updatedBy", getUpdatedBy())
            .append("updatedTime", getUpdatedTime())
            .toString();
    }
}
