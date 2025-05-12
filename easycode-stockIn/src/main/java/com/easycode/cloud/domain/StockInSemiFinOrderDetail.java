package com.easycode.cloud.domain;

import com.weifu.cloud.common.core.annotation.Excel;
import com.weifu.cloud.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;

/**
 * 半成品入库单对象 wms_stockin_semifin_order_detail
 * 
 * @author bcp
 * @date 2023-07-22
 */
@Alias("StockInSemiFinOrderDetail")
public class StockInSemiFinOrderDetail extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 成品入库单id */
    @Excel(name = "半成品入库单id")
    private Long semifinOrderId;

    /** 物料号 */
    @Excel(name = "物料号")
    private String materialNo;

    /** 旧物料号 */
    @Excel(name = "旧物料号")
    private String oldMaterialNo;

    /** 物料名称 */
    @Excel(name = "物料名称")
    private String materialName;

    /** 批次号/容器号 */
    @Excel(name = "批次号/容器号")
    private String lot;

    /** 计划收货数量（箱内数量） */
    @Excel(name = "计划收货数量", readConverterExp = "箱=内数量")
    private BigDecimal planRecivevQty;

    /** 操作计划收货数量（箱内数量） */
    @Excel(name = "操作计划收货数量", readConverterExp = "箱=内数量")
    private BigDecimal operationPlanRecivevQty;

    /** 激活数量 */
    @Excel(name = "激活数量")
    private BigDecimal activeQty;

    /** 最小包装数 */
    @Excel(name = "最小包装数")
    private BigDecimal minPacking;

    /** 包装单位 */
    @Excel(name = "包装单位")
    private String unit;

    /** 操作单位 */
    @Excel(name = "操作单位")
    private String operationUnit;

    /** 是否质检 */
    @Excel(name = "是否质检")
    private String isQc;

    /** 生产订单号 */
    @Excel(name = "生产订单号")
    private String prdOrderNo;

    /** 源库存地点 */
    @Excel(name = "源库存地点")
    private String sourceLocationCode;

    /** 收货库存地点code */
    @Excel(name = "收货库存地点code")
    private String locationCode;

    /** 已收数量 */
    @Excel(name = "已收数量")
    private BigDecimal recievedQty;

    /** 租户号 */
    @Excel(name = "租户号")
    private Long tenantId;
    /**
     * 行号
     */
    private String lineNo;
    /**
     * 是否打印
     */
    private String isPrinted;

    /**
     * 半成品明细状态
     */
    private String status;


    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setSemifinOrderId(Long semifinOrderId) 
    {
        this.semifinOrderId = semifinOrderId;
    }

    public Long getSemifinOrderId() 
    {
        return semifinOrderId;
    }
    public void setMaterialNo(String materialNo) 
    {
        this.materialNo = materialNo;
    }

    public String getMaterialNo() 
    {
        return materialNo;
    }
    public void setOldMaterialNo(String oldMaterialNo) 
    {
        this.oldMaterialNo = oldMaterialNo;
    }

    public String getOldMaterialNo() 
    {
        return oldMaterialNo;
    }
    public void setMaterialName(String materialName) 
    {
        this.materialName = materialName;
    }

    public String getMaterialName() 
    {
        return materialName;
    }
    public void setLot(String lot) 
    {
        this.lot = lot;
    }

    public String getLot() 
    {
        return lot;
    }
    public void setPlanRecivevQty(BigDecimal planRecivevQty) 
    {
        this.planRecivevQty = planRecivevQty;
    }

    public BigDecimal getPlanRecivevQty() 
    {
        return planRecivevQty;
    }
    public void setOperationPlanRecivevQty(BigDecimal operationPlanRecivevQty) 
    {
        this.operationPlanRecivevQty = operationPlanRecivevQty;
    }

    public BigDecimal getOperationPlanRecivevQty() 
    {
        return operationPlanRecivevQty;
    }
    public void setActiveQty(BigDecimal activeQty) 
    {
        this.activeQty = activeQty;
    }

    public BigDecimal getActiveQty() 
    {
        return activeQty;
    }
    public void setMinPacking(BigDecimal minPacking) 
    {
        this.minPacking = minPacking;
    }

    public BigDecimal getMinPacking() 
    {
        return minPacking;
    }
    public void setUnit(String unit) 
    {
        this.unit = unit;
    }

    public String getUnit() 
    {
        return unit;
    }
    public void setOperationUnit(String operationUnit) 
    {
        this.operationUnit = operationUnit;
    }

    public String getOperationUnit() 
    {
        return operationUnit;
    }
    public void setIsQc(String isQc) 
    {
        this.isQc = isQc;
    }

    public String getIsQc() 
    {
        return isQc;
    }
    public void setPrdOrderNo(String prdOrderNo) 
    {
        this.prdOrderNo = prdOrderNo;
    }

    public String getPrdOrderNo() 
    {
        return prdOrderNo;
    }
    public void setSourceLocationCode(String sourceLocationCode) 
    {
        this.sourceLocationCode = sourceLocationCode;
    }

    public String getSourceLocationCode() 
    {
        return sourceLocationCode;
    }
    public void setLocationCode(String locationCode) 
    {
        this.locationCode = locationCode;
    }

    public String getLocationCode() 
    {
        return locationCode;
    }
    public void setRecievedQty(BigDecimal recievedQty) 
    {
        this.recievedQty = recievedQty;
    }

    public BigDecimal getRecievedQty() 
    {
        return recievedQty;
    }
    public void setTenantId(Long tenantId) 
    {
        this.tenantId = tenantId;
    }

    public Long getTenantId() 
    {
        return tenantId;
    }

    public String getLineNo() {
        return lineNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
    }

    public String getIsPrinted() {
        return isPrinted;
    }

    public void setIsPrinted(String isPrinted) {
        this.isPrinted = isPrinted;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("semifinOrderId", getSemifinOrderId())
            .append("materialNo", getMaterialNo())
            .append("oldMaterialNo", getOldMaterialNo())
            .append("materialName", getMaterialName())
            .append("lot", getLot())
            .append("planRecivevQty", getPlanRecivevQty())
            .append("operationPlanRecivevQty", getOperationPlanRecivevQty())
            .append("activeQty", getActiveQty())
            .append("minPacking", getMinPacking())
            .append("unit", getUnit())
            .append("operationUnit", getOperationUnit())
            .append("isQc", getIsQc())
            .append("prdOrderNo", getPrdOrderNo())
            .append("sourceLocationCode", getSourceLocationCode())
            .append("locationCode", getLocationCode())
            .append("recievedQty", getRecievedQty())
            .append("tenantId", getTenantId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
