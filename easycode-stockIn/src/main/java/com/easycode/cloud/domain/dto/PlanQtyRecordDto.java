package com.easycode.cloud.domain.dto;

import com.weifu.cloud.common.core.annotation.Excel;
import com.weifu.cloud.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * 采购单据需求数量变更记录对象 wms_purchase_order_planqty_record
 *
 * @author weifu
 * @date 2024-06-13
 */
public class PlanQtyRecordDto extends BaseEntity {

    /**
     * 采购单号
     */
    @Excel(name = "采购单号")
    @ApiModelProperty(value = "采购单号")
    private String purchaseOrderNo;

    /**
     * 采购单行号
     */
    @Excel(name = "采购单行号")
    @ApiModelProperty(value = "采购单行号")
    private String purchaseLineNo;

    /**
     * sap订单id
     */
    @Excel(name = "sap订单id")
    @ApiModelProperty(value = "sap订单id")
    private Long sapOrderId;

    /**
     * 收货id
     */
    @Excel(name = "收货id")
    @ApiModelProperty(value = "收货id")
    private Long deliveryId;

    /**
     * 物料代码
     */
    @Excel(name = "物料代码")
    @ApiModelProperty(value = "物料代码")
    private String materialNo;

    /**
     * 需求数量
     */
    @Excel(name = "需求数量")
    @ApiModelProperty(value = "需求数量")
    private BigDecimal planQty;

    /**
     * 需求数量
     */
    @Excel(name = "收货数量")
    @ApiModelProperty(value = "收货数量")
    private BigDecimal deliverQty;

    /**
     * add 送货单新增； edit 送货单修改； delete 送货单删除
     */
    private String changeType;

    private Long deliveryOrderDetailId;

    private Long deliveryOrderlId;


    public String getPurchaseOrderNo() {
        return purchaseOrderNo;
    }

    public void setPurchaseOrderNo(String purchaseOrderNo) {
        this.purchaseOrderNo = purchaseOrderNo;
    }

    public String getPurchaseLineNo() {
        return purchaseLineNo;
    }

    public void setPurchaseLineNo(String purchaseLineNo) {
        this.purchaseLineNo = purchaseLineNo;
    }

    public Long getSapOrderId() {
        return sapOrderId;
    }

    public void setSapOrderId(Long sapOrderId) {
        this.sapOrderId = sapOrderId;
    }

    public Long getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(Long deliveryId) {
        this.deliveryId = deliveryId;
    }

    public String getMaterialNo() {
        return materialNo;
    }

    public void setMaterialNo(String materialNo) {
        this.materialNo = materialNo;
    }

    public BigDecimal getPlanQty() {
        return planQty;
    }

    public void setPlanQty(BigDecimal planQty) {
        this.planQty = planQty;
    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public BigDecimal getDeliverQty() {
        return deliverQty;
    }

    public void setDeliverQty(BigDecimal deliverQty) {
        this.deliverQty = deliverQty;
    }

    public Long getDeliveryOrderDetailId() {
        return deliveryOrderDetailId;
    }

    public void setDeliveryOrderDetailId(Long deliveryOrderDetailId) {
        this.deliveryOrderDetailId = deliveryOrderDetailId;
    }

    public Long getDeliveryOrderlId() {
        return deliveryOrderlId;
    }

    public void setDeliveryOrderlId(Long deliveryOrderlId) {
        this.deliveryOrderlId = deliveryOrderlId;
    }
}
