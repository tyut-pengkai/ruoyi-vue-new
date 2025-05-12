package com.easycode.cloud.domain.dto;


import com.easycode.cloud.domain.TaskInfo;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;


/**
 * 委外订单 BOM dto
 *
 * @author bcp
 */
@Alias("OutSourceOrderBomDto")
public class OutSourceOrderBomDto extends TaskInfo {

    /**
     * 采购单号
     */
    private String purchaseOrderNo;

    /**
     * 采购凭证的项目编号
     */
    private String poItemNo;

    /**
     * 项目编号
     */
    private String itemNo;

    /**
     * 物料编号
     */
    private String materialNo;

    /**
     * 需求数量
     */
    private BigDecimal qty;

    /**
     * 单位
     */
    private String unit;

    /**
     * 采购单行号
     */
    private String plantCode;

    public String getPurchaseOrderNo() {
        return purchaseOrderNo;
    }

    public void setPurchaseOrderNo(String purchaseOrderNo) {
        this.purchaseOrderNo = purchaseOrderNo;
    }

    public String getPoItemNo() {
        return poItemNo;
    }

    public void setPoItemNo(String poItemNo) {
        this.poItemNo = poItemNo;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    @Override
    public String getMaterialNo() {
        return materialNo;
    }

    @Override
    public void setMaterialNo(String materialNo) {
        this.materialNo = materialNo;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPlantCode() {
        return plantCode;
    }

    public void setPlantCode(String plantCode) {
        this.plantCode = plantCode;
    }
}
