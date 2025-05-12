package com.easycode.cloud.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.weifu.cloud.common.core.annotation.Excel;
import com.weifu.cloud.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 采购单据需求数量变更记录对象 wms_purchase_order_planqty_record
 *
 * @author weifu
 * @date 2024-06-13
 */
public class WmsPurchaseOrderPlanqtyRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 采购单id */
    @Excel(name = "采购单id")
    @ApiModelProperty(value = "采购单id")
    private Long purchaseOrderId;

    /** 采购单明细id */
    @Excel(name = "采购单明细id")
    @ApiModelProperty(value = "采购单明细id")
    private Long purchaseOrderDetailId;

    /** 采购单号 */
    @Excel(name = "采购单号")
    @ApiModelProperty(value = "采购单号")
    private String purchaseOrderNo;

    /** 采购单行号 */
    @Excel(name = "采购单行号")
    @ApiModelProperty(value = "采购单行号")
    private String purchaseLineNo;

    /** sap订单id */
    @Excel(name = "sap订单id")
    @ApiModelProperty(value = "sap订单id")
    private String sapOrderId;

    /** 收货id */
    @Excel(name = "收货id")
    @ApiModelProperty(value = "收货id")
    private Long deliveryId;

    /** 物料代码 */
    @Excel(name = "物料代码")
    @ApiModelProperty(value = "物料代码")
    private String materialNo;

    /** 物料id */
    @Excel(name = "物料id")
    @ApiModelProperty(value = "物料id")
    private Long materialId;

    /** 变更类型（1：sap需求量、11：sap变更时、2：sap收货数量、21：sap收货变更时、3：wms建单数量、31：修改建单数量变化值、32：单据作废或删除时数量变化值、4：wms收货数量、41：wms收货作废或者删除时） */
    @Excel(name = "变更类型", readConverterExp = "1=：sap需求量、11：sap变更时、2：sap收货数量、21：sap收货变更时、3：wms建单数量、31：修改建单数量变化值、32：单据作废或删除时数量变化值、4：wms收货数量、41：wms收货作废或者删除时")
    @ApiModelProperty(value = "变更类型")
    private String changeType;

    /** 总需求数量 */
    @Excel(name = "供应商需求数量")
    @ApiModelProperty(value = "供应商需求数量")
    private Double totalPlanQty;

    /** 供应商收货数量（考虑了供应商时间后的数量） */
    @Excel(name = "供应商收货数量")
    private Double supplierDeliveryQty;
    /** 需求数量 */
    @Excel(name = "需求数量")
    @ApiModelProperty(value = "需求数量")
    private Double planQty;

    /** 操作之前需求量 */
    @Excel(name = "操作之前需求量")
    @ApiModelProperty(value = "操作之前需求量")
    private Double beforePlanQty;

    /** 事件变更数量 */
    @Excel(name = "事件变更数量")
    @ApiModelProperty(value = "事件变更数量")
    private Double changePlanQty;

    /** (sap/wms)收货数量 */
    @Excel(name = "(sap/wms)收货数量")
    @ApiModelProperty(value = "(sap/wms)收货数量")
    private Double deliveryQty;

    /** 租户号 */
    @Excel(name = "租户号")
    @ApiModelProperty(value = "租户号")
    private Long tenantId;

    /** 创建人 */
    @Excel(name = "创建人")
    @ApiModelProperty(value = "创建人")
    private String createdBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createdTime;

    /** 更新人 */
    @Excel(name = "更新人")
    @ApiModelProperty(value = "更新人")
    private String updatedBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private Date updatedTime;

    public Double getTotalPlanQty() {
        return totalPlanQty;
    }

    public void setTotalPlanQty(Double totalPlanQty) {
        this.totalPlanQty = totalPlanQty;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setPurchaseOrderId(Long purchaseOrderId)
    {
        this.purchaseOrderId = purchaseOrderId;
    }

    public Long getPurchaseOrderId()
    {
        return purchaseOrderId;
    }
    public void setPurchaseOrderDetailId(Long purchaseOrderDetailId)
    {
        this.purchaseOrderDetailId = purchaseOrderDetailId;
    }

    public Long getPurchaseOrderDetailId()
    {
        return purchaseOrderDetailId;
    }
    public void setPurchaseOrderNo(String purchaseOrderNo)
    {
        this.purchaseOrderNo = purchaseOrderNo;
    }

    public String getPurchaseOrderNo()
    {
        return purchaseOrderNo;
    }
    public void setPurchaseLineNo(String purchaseLineNo)
    {
        this.purchaseLineNo = purchaseLineNo;
    }

    public String getPurchaseLineNo()
    {
        return purchaseLineNo;
    }
    public void setSapOrderId(String sapOrderId)
    {
        this.sapOrderId = sapOrderId;
    }

    public String getSapOrderId()
    {
        return sapOrderId;
    }
    public void setDeliveryId(Long deliveryId)
    {
        this.deliveryId = deliveryId;
    }

    public Long getDeliveryId()
    {
        return deliveryId;
    }
    public void setMaterialNo(String materialNo)
    {
        this.materialNo = materialNo;
    }

    public String getMaterialNo()
    {
        return materialNo;
    }
    public void setMaterialId(Long materialId)
    {
        this.materialId = materialId;
    }

    public Long getMaterialId()
    {
        return materialId;
    }
    public void setChangeType(String changeType)
    {
        this.changeType = changeType;
    }

    public String getChangeType()
    {
        return changeType;
    }
    public void setPlanQty(Double planQty)
    {
        this.planQty = planQty;
    }

    public Double getPlanQty()
    {
        return planQty;
    }
    public void setBeforePlanQty(Double beforePlanQty)
    {
        this.beforePlanQty = beforePlanQty;
    }

    public Double getBeforePlanQty()
    {
        return beforePlanQty;
    }
    public void setChangePlanQty(Double changePlanQty)
    {
        this.changePlanQty = changePlanQty;
    }

    public Double getChangePlanQty()
    {
        return changePlanQty;
    }
    public void setDeliveryQty(Double deliveryQty)
    {
        this.deliveryQty = deliveryQty;
    }

    public Double getDeliveryQty()
    {
        return deliveryQty;
    }
    public void setTenantId(Long tenantId)
    {
        this.tenantId = tenantId;
    }

    public Long getTenantId()
    {
        return tenantId;
    }
    public void setCreatedBy(String createdBy)
    {
        this.createdBy = createdBy;
    }

    public String getCreatedBy()
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
    public void setUpdatedBy(String updatedBy)
    {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedBy()
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

    public Double getSupplierDeliveryQty() {
        return supplierDeliveryQty;
    }

    public void setSupplierDeliveryQty(Double supplierDeliveryQty) {
        this.supplierDeliveryQty = supplierDeliveryQty;
    }

    @Override
    public String toString() {
        return "WmsPurchaseOrderPlanqtyRecord{" +
                "id=" + id +
                ", purchaseOrderId=" + purchaseOrderId +
                ", purchaseOrderDetailId=" + purchaseOrderDetailId +
                ", purchaseOrderNo='" + purchaseOrderNo + '\'' +
                ", purchaseLineNo=" + purchaseLineNo +
                ", sapOrderId=" + sapOrderId +
                ", deliveryId=" + deliveryId +
                ", materialNo='" + materialNo + '\'' +
                ", materialId=" + materialId +
                ", changeType='" + changeType + '\'' +
                ", totalPlanQty=" + totalPlanQty +
                ", supplierDeliveryQty=" + supplierDeliveryQty +
                ", planQty=" + planQty +
                ", beforePlanQty=" + beforePlanQty +
                ", changePlanQty=" + changePlanQty +
                ", deliveryQty=" + deliveryQty +
                ", tenantId=" + tenantId +
                ", createdBy='" + createdBy + '\'' +
                ", createdTime=" + createdTime +
                ", updatedBy='" + updatedBy + '\'' +
                ", updatedTime=" + updatedTime +
                '}';
    }
}
