package com.easycode.cloud.domain.vo;

import java.math.BigDecimal;

public class StockInDetailPrintVo {

    private String stockinOrderNo;
    private String locationCode;
    private String factoryCode;
    private String lot;
    private String materialNo;
    private String materialName;
    private String positionNo;
    private String supplierCode;
    private String taskNo;
    private String moveType;
    private BigDecimal qyt;
    private String oldMaterialNo;

    public String getOldMaterialNo() {
        return oldMaterialNo;
    }

    public void setOldMaterialNo(String oldMaterialNo) {
        this.oldMaterialNo = oldMaterialNo;
    }

    public BigDecimal getQyt() {
        return qyt;
    }

    public void setQyt(BigDecimal qyt) {
        this.qyt = qyt;
    }

    public String getStockinOrderNo() {
        return stockinOrderNo;
    }

    public void setStockinOrderNo(String stockinOrderNo) {
        this.stockinOrderNo = stockinOrderNo;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getFactoryCode() {
        return factoryCode;
    }

    public void setFactoryCode(String factoryCode) {
        this.factoryCode = factoryCode;
    }

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
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

    public String getPositionNo() {
        return positionNo;
    }

    public void setPositionNo(String positionNo) {
        this.positionNo = positionNo;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    public String getMoveType() {
        return moveType;
    }

    public void setMoveType(String moveType) {
        this.moveType = moveType;
    }

    @Override
    public String toString() {
        return "StockInOtherDetailPrintVo{" +
                "locationCode='" + locationCode + '\'' +
                ", factoryCode='" + factoryCode + '\'' +
                ", lot='" + lot + '\'' +
                ", materialNo='" + materialNo + '\'' +
                ", materialName='" + materialName + '\'' +
                ", positionNo='" + positionNo + '\'' +
                ", supplierCode='" + supplierCode + '\'' +
                ", taskNo='" + taskNo + '\'' +
                ", moveType='" + moveType + '\'' +
                '}';
    }
}
