package com.easycode.cloud.domain;

import com.weifu.cloud.common.core.web.domain.BaseEntity;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 送检任务实体类
 */
@Alias("DeliveryInspectionTask")
public class DeliveryInspectionTask extends BaseEntity {


    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /**
     * 任务号
     */
    private String taskNo;

    /**
     * 任务类型
     */
    private String taskType;

    /**
     * 销售订单号
     */
    private String marketOrderNo;

    /**
     * 销售订单发货单号
     */
    private String marketDeliveryNo;

    /**
     * 任务状态
     */
    private String status;

    /**
     * 物料号
     */
    private String materialNo;

    /**
     *物料名称
     */
    private String materialName;
    /**
     *任务数量
     */
    private BigDecimal qty;
    /**
     *批次号
     */
    private String lot;
    /**
     *运送时间
     */
    private Date deliveryTime;
    /**
     *租户号
     */
    private Long tenantId;

    /**
     *送检单id
     */
    private Long inspectOrderId;

    /**
     * 送检单号
     */
    private String inspectOrderNo;


    /** 库存点 */
    private String locationCode;

    /** 仓位 */
    private String positionNo;

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getPositionNo() {
        return positionNo;
    }

    public void setPositionNo(String positionNo) {
        this.positionNo = positionNo;
    }

    public Long getInspectOrderId() {
        return inspectOrderId;
    }

    public void setInspectOrderId(Long inspectOrderId) {
        this.inspectOrderId = inspectOrderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getMarketOrderNo() {
        return marketOrderNo;
    }

    public void setMarketOrderNo(String marketOrderNo) {
        this.marketOrderNo = marketOrderNo;
    }

    public String getMarketDeliveryNo() {
        return marketDeliveryNo;
    }

    public void setMarketDeliveryNo(String marketDeliveryNo) {
        this.marketDeliveryNo = marketDeliveryNo;
    }

    public String getMaterialNo() {
        return materialNo;
    }

    public void setMaterialNo(String materialNo) {
        this.materialNo = materialNo;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public Date getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getInspectOrderNo() {
        return inspectOrderNo;
    }

    public void setInspectOrderNo(String inspectOrderNo) {
        this.inspectOrderNo = inspectOrderNo;
    }

    @Override
    public Long getTenantId() {
        return tenantId;
    }

    @Override
    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }
}
