package com.easycode.cloud.domain.dto;

import java.math.BigDecimal;

/**
 * 成品扫描收货明细对象
 *
 * @author Administrator
 */
public class FinProductScanDetailDto {
    /**
     * 批次
     */
    private String lotNo;

    /**
     * 箱号
     */
    private String lotNoNum;

    /**
     * 计划收货数量
     */
    private BigDecimal totalQty;

    /**
     * 最小包装数
     */
    private BigDecimal minPacking;

    /**
     * 物料名称
     */
    private String materialDesc;

    /**
     * 物料号
     */
    private String materialNo;

    /**
     * 物料id
     */
    private Long materialId;

    public String getLotNo() {
        return lotNo;
    }

    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }

    public String getLotNoNum() {
        return lotNoNum;
    }

    public void setLotNoNum(String lotNoNum) {
        this.lotNoNum = lotNoNum;
    }

    public BigDecimal getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(BigDecimal totalQty) {
        this.totalQty = totalQty;
    }

    public BigDecimal getMinPacking() {
        return minPacking;
    }

    public void setMinPacking(BigDecimal minPacking) {
        this.minPacking = minPacking;
    }

    public String getMaterialDesc() {
        return materialDesc;
    }

    public void setMaterialDesc(String materialDesc) {
        this.materialDesc = materialDesc;
    }

    public String getMaterialNo() {
        return materialNo;
    }

    public void setMaterialNo(String materialNo) {
        this.materialNo = materialNo;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }
}
