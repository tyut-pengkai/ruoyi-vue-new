package com.easycode.cloud.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.weifu.cloud.common.core.annotation.Excel;
import com.weifu.cloud.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.ibatis.type.Alias;

import java.util.Date;

/**
 * 采购单对象 wms_purchase_order
 * 
 * @author bcp
 * @date 2023-02-23
 */
@Alias("PurchaseOrder")
public class PurchaseOrder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 采购单号 */
    @Excel(name = "采购单号")
    private String orderNo;

    /** 采购单类型-字典 */
    @Excel(name = "采购单类型-字典")
    private String orderType;

    /** 状态-字典提供 */
    @Excel(name = "状态-字典提供")
    private String orderStatus;

    /** 公司id */
    @Excel(name = "公司id")
    private Long companyId;

    /** 公司名称 */
    @Excel(name = "公司名称")
    private String companyName;

    /** 公司代码 */
    @Excel(name = "公司代码")
    private String companyCode;

    /** 供应商id */
    @Excel(name = "供应商id")
    private Long supplierId;

    /** 供应商名称 */
    @Excel(name = "供应商名称")
    private String supplierName;

    /** 供应商代码 */
    @Excel(name = "供应商代码")
    private String supplierCode;

    /** 采购员 */
    @Excel(name = "采购员")
    private String buyer;

    /** 交货日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "交货日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date deliverDate;

    /** 处理日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "处理日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date processDate;

    /** 关闭日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "关闭日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date closeDate;

    /** 采购退货单号 */
    @Excel(name = "采购退货单号")
    private String purchaseRefundOrderNo;

    /** 采购凭证类型 */
    @Excel(name = "采购凭证类型")
    private String billType;

    /** 租户号 */
    @Excel(name = "租户号")
    private Long tenantId;

    /** 采购组描述 */
    @Excel(name = "采购组描述")
    private String groupName;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setOrderNo(String orderNo) 
    {
        this.orderNo = orderNo;
    }

    public String getOrderNo() 
    {
        return orderNo;
    }
    public void setOrderType(String orderType) 
    {
        this.orderType = orderType;
    }

    public String getOrderType() 
    {
        return orderType;
    }
    public void setOrderStatus(String orderStatus) 
    {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() 
    {
        return orderStatus;
    }
    public void setCompanyId(Long companyId) 
    {
        this.companyId = companyId;
    }

    public Long getCompanyId() 
    {
        return companyId;
    }
    public void setCompanyName(String companyName) 
    {
        this.companyName = companyName;
    }

    public String getCompanyName() 
    {
        return companyName;
    }
    public void setCompanyCode(String companyCode) 
    {
        this.companyCode = companyCode;
    }

    public String getCompanyCode() 
    {
        return companyCode;
    }
    public void setSupplierId(Long supplierId) 
    {
        this.supplierId = supplierId;
    }

    public Long getSupplierId() 
    {
        return supplierId;
    }
    public void setSupplierName(String supplierName) 
    {
        this.supplierName = supplierName;
    }

    public String getSupplierName() 
    {
        return supplierName;
    }
    public void setSupplierCode(String supplierCode) 
    {
        this.supplierCode = supplierCode;
    }

    public String getSupplierCode() 
    {
        return supplierCode;
    }
    public void setBuyer(String buyer) 
    {
        this.buyer = buyer;
    }

    public String getBuyer() 
    {
        return buyer;
    }
    public void setDeliverDate(Date deliverDate) 
    {
        this.deliverDate = deliverDate;
    }

    public Date getDeliverDate() 
    {
        return deliverDate;
    }
    public void setProcessDate(Date processDate) 
    {
        this.processDate = processDate;
    }

    public Date getProcessDate() 
    {
        return processDate;
    }
    public void setCloseDate(Date closeDate) 
    {
        this.closeDate = closeDate;
    }

    public Date getCloseDate() 
    {
        return closeDate;
    }
    public void setPurchaseRefundOrderNo(String purchaseRefundOrderNo) 
    {
        this.purchaseRefundOrderNo = purchaseRefundOrderNo;
    }

    public String getPurchaseRefundOrderNo() 
    {
        return purchaseRefundOrderNo;
    }
    public void setBillType(String billType) 
    {
        this.billType = billType;
    }

    public String getBillType() 
    {
        return billType;
    }
    public void setTenantId(Long tenantId) 
    {
        this.tenantId = tenantId;
    }

    public Long getTenantId() 
    {
        return tenantId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("orderNo", getOrderNo())
            .append("orderType", getOrderType())
            .append("orderStatus", getOrderStatus())
            .append("companyId", getCompanyId())
            .append("companyName", getCompanyName())
            .append("companyCode", getCompanyCode())
            .append("supplierId", getSupplierId())
            .append("supplierName", getSupplierName())
            .append("supplierCode", getSupplierCode())
            .append("buyer", getBuyer())
            .append("deliverDate", getDeliverDate())
            .append("processDate", getProcessDate())
            .append("closeDate", getCloseDate())
            .append("purchaseRefundOrderNo", getPurchaseRefundOrderNo())
            .append("billType", getBillType())
            .append("remark", getRemark())
            .append("tenantId", getTenantId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
