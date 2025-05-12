package com.easycode.cloud.domain;

import com.weifu.cloud.common.core.annotation.Excel;
import com.weifu.cloud.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;

/**
 * 送检单明细对象 wms_inspect_order_details
 *
 * @author weifu
 * @date 2023-03-29
 */
@Alias("InspectOrderDetails")
public class InspectOrderDetails extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 关联送检单号
     */
    @Excel(name = "关联送检单号")
    private String orderNo;

    /**
     * 物料代码
     */
    @Excel(name = "物料代码")
    private String materialNo;


    //旧物料号
    @Excel(name = "旧物料号")
    private String oldMaterialNo;

    /**
     * 物料名称
     */
    @Excel(name = "物料名称")
    private String materialName;

    /**
     * 行号
     */
    @Excel(name = "行号")
    private String lineNo;

    /**
     * 批次
     */
    @Excel(name = "批次")
    private String lot;

    /**
     * 质检数量
     */
    @Excel(name = "质检数量")
    private BigDecimal qcQty;

    /**
     * 破坏数量
     */
    @Excel(name = "破坏数量")
    private BigDecimal destructQty;

    /**
     * 退货数量
     */
    @Excel(name = "退货数量")
    private BigDecimal returnQty;

    /**
     * 挑选数量
     */
    @Excel(name = "挑选数量")
    private BigDecimal pickQty;

    /**
     * 让步接收数量
     */
    @Excel(name = "让步接收数量")
    private BigDecimal concessionQty;

    /**
     * 释放数量
     */
    @Excel(name = "释放数量")
    private BigDecimal releaseQty;

    /**
     * 默认单位
     */
    @Excel(name = "默认单位")
    private String unit;

    /**
     * 操作单位
     */
    @Excel(name = "操作单位")
    private String operationUnit;

    /**
     * 炉号
     */
    @Excel(name = "炉号")
    private String furnaceNo;

    /**
     * 生产批次
     */
    @Excel(name = "生产批次")
    private String prdLot;

    /**
     * 仓位号
     */
    private String positionNo;

    /**
     * 台账id
     */
    private Long inventoryId;
    /**
     * 租户号
     */
    @Excel(name = "租户号")
    private Long tenantId;

    public String getOldMaterialNo() {
        return oldMaterialNo;
    }

    public void setOldMaterialNo(String oldMaterialNo) {
        this.oldMaterialNo = oldMaterialNo;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
    }

    public String getLineNo() {
        return lineNo;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public String getLot() {
        return lot;
    }

    public void setQcQty(BigDecimal qcQty) {
        this.qcQty = qcQty;
    }

    public BigDecimal getQcQty() {
        return qcQty;
    }

    public void setDestructQty(BigDecimal destructQty) {
        this.destructQty = destructQty;
    }

    public BigDecimal getDestructQty() {
        return destructQty;
    }

    public void setReleaseQty(BigDecimal releaseQty) {
        this.releaseQty = releaseQty;
    }

    public BigDecimal getReleaseQty() {
        return releaseQty;
    }

    public void setFurnaceNo(String furnaceNo) {
        this.furnaceNo = furnaceNo;
    }

    public String getFurnaceNo() {
        return furnaceNo;
    }

    public void setPrdLot(String prdLot) {
        this.prdLot = prdLot;
    }

    public String getPrdLot() {
        return prdLot;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public String getMaterialNo() {
        return materialNo;
    }

    public void setMaterialNo(String materialNo) {
        this.materialNo = materialNo;
    }

    public BigDecimal getReturnQty() {
        return returnQty;
    }

    public void setReturnQty(BigDecimal returnQty) {
        this.returnQty = returnQty;
    }

    public BigDecimal getConcessionQty() {
        return concessionQty;
    }

    public void setConcessionQty(BigDecimal concessionQty) {
        this.concessionQty = concessionQty;
    }

    public String getPositionNo() {
        return positionNo;
    }

    public void setPositionNo(String positionNo) {
        this.positionNo = positionNo;
    }

    public Long getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Long inventoryId) {
        this.inventoryId = inventoryId;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getOperationUnit() {
        return operationUnit;
    }

    public void setOperationUnit(String operationUnit) {
        this.operationUnit = operationUnit;
    }


    public BigDecimal getPickQty() {
        return pickQty;
    }

    public void setPickQty(BigDecimal pickQty) {
        this.pickQty = pickQty;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("orderNo", getOrderNo())
                .append("lineNo", getLineNo())
                .append("lot", getLot())
                .append("qcQty", getQcQty())
                .append("destructQty", getDestructQty())
                .append("releaseQty", getReleaseQty())
                .append("unit", getUnit())
                .append("operationUnit", getOperationUnit())
                .append("furnaceNo", getFurnaceNo())
                .append("prdLot", getPrdLot())
                .append("positionNo", getPositionNo())
                .append("remark", getRemark())
                .append("tenantId", getTenantId())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
