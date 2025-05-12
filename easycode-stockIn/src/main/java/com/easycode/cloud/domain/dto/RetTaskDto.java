package com.easycode.cloud.domain.dto;


import com.easycode.cloud.domain.RetTask;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;

/**
 * 退货任务dto
 * @author bcp
 */
@Alias("RetTaskDto")
public class RetTaskDto extends RetTask {

    private String closeTask;

    /**
     * 任务状态
     */
    private String[] taskStatusArr;

    /**
     * 确认仓位代码
     */
    private String confirmPosition;

    /**
     * 供应商代码
     */
    private String supplierCode;

    /**
     * 是否质检
     */
    private String isQc;

    /**
     * 物料类型
     */
    private String type;

    /**
     * 生产批次/sap默认批次
     */
    private String productionLot;

    /**
     * 原库存地点
     */
    private String storageLocation;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 销售发货代码
     */
    private String saleCode;

    /**
     * 是否可以提交过账
     */
    private Boolean isSubmit;

    /**
    * 确认数量 */
    private BigDecimal confirmQty;

    /**
     * 目的库存点 */
    private String targetStorageLocation;

    public BigDecimal getConfirmQty() {
        return confirmQty;
    }

    public void setConfirmQty(BigDecimal confirmQty) {
        this.confirmQty = confirmQty;
    }

    public String getTargetStorageLocation() {
        return targetStorageLocation;
    }

    public void setTargetStorageLocation(String targetStorageLocation) {
        this.targetStorageLocation = targetStorageLocation;
    }

    private String startTime;

    private String endTime;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getCloseTask() {
        return closeTask;
    }

    public void setCloseTask(String closeTask) {
        this.closeTask = closeTask;
    }

    public String[] getTaskStatusArr() {
        return taskStatusArr;
    }

    public void setTaskStatusArr(String[] taskStatusArr) {
        this.taskStatusArr = taskStatusArr;
    }


    public String getConfirmPosition() {
        return confirmPosition;
    }

    public void setConfirmPosition(String confirmPosition) {
        this.confirmPosition = confirmPosition;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getIsQc() {
        return isQc;
    }

    public void setIsQc(String isQc) {
        this.isQc = isQc;
    }

    public String getProductionLot() {
        return productionLot;
    }

    public void setProductionLot(String productionLot) {
        this.productionLot = productionLot;
    }

    public String getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getSaleCode() {
        return saleCode;
    }

    public void setSaleCode(String saleCode) {
        this.saleCode = saleCode;
    }

    public Boolean getSubmit() {
        return isSubmit;
    }

    public void setSubmit(Boolean submit) {
        isSubmit = submit;
    }
}
