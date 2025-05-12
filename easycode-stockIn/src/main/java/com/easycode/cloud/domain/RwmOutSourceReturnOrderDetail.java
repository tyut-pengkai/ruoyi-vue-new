package com.easycode.cloud.domain;

import com.weifu.cloud.common.core.annotation.Excel;
import com.weifu.cloud.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;

/**
 * 原材料委外发料退退货单明细对象 wms_rwm_outsource_return_order_detail
 *
 * @author fsc
 * @date 2023-03-11
 */
@Alias("RwmOutSourceReturnOrderDetail")
public class RwmOutSourceReturnOrderDetail extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 关联退货单号 */
    private String returnOrderNo;

    /** 行号 */
    private String lineNo;

    /** 物料代码 */
    @Excel(name = "物料代码")
    private String materialNo;

    /** 物料描述 */
    private String materialName;

    /** 旧物料号 */
    private String oldMaterialNo;

    /** 工厂代码 */
    private String factoryCode;

    /** 退货数量 */
    private BigDecimal returnQty;

    /** 单位 */
    private String unit;


    /** 操作单位 */
    private String operationUnit;

    /** 操作单位对应数量 */
    @Excel(name = "数量")
    private BigDecimal operationQty;

    /** 批次号 */
    private String lot;

    /**
     * 任务书号
     */
    @Excel(name = "任务书号")
    private String taskBookNo;

    /** 租户号 */
    private Long tenantId;

    /**
     * 容器号
     */
    private String containerNo;

    /**
     * 容器类型
     */
    private String containerType;

    /**
     * 仓位id */
    private Long positionId;

    /**
     * 仓位id */
    private String positionNo;


    /** 采购凭证号 */
    @Excel(name = "采购凭证号")
    private String purchaseVoucherNo;


    /** 供应商编码 */
    @Excel(name = "供应商编码")
    private String supplierCode;

    private String supplierName;

    /**
     * 库位地点*/
    private String localtionCode;


    @Excel(name = "出库单货单明细id")
    private Long outsourcedStockoutOrderDetailId;

    @Excel(name = "出库单货单明细id(兼容前端)")
    private Long detailId;
    /** 库存地code */
    @Excel(name = "库存地code")
    private String locationCode;

    /** 台账ld */
    @Excel(name = "台账ld")
    private String inventoryDetailId;


    /** 状态 */
    @Excel(name = "状态")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public Long getOutsourcedStockoutOrderDetailId() {
        return outsourcedStockoutOrderDetailId;
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public String getInventoryDetailId() {
        return inventoryDetailId;
    }

    public void setOutsourcedStockoutOrderDetailId(Long outsourcedStockoutOrderDetailId) {
        this.outsourcedStockoutOrderDetailId = outsourcedStockoutOrderDetailId;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public void setInventoryDetailId(String inventoryDetailId) {
        this.inventoryDetailId = inventoryDetailId;
    }

    public String getLocaltionCode() {
        return localtionCode;
    }

    public void setLocaltionCode(String localtionCode) {
        this.localtionCode = localtionCode;
    }

    public String getPositionNo() {
        return positionNo;
    }

    public void setPositionNo(String positionNo) {
        this.positionNo = positionNo;
    }

    public String getPurchaseVoucherNo() {
        return purchaseVoucherNo;
    }

    public void setPurchaseVoucherNo(String purchaseVoucherNo) {
        this.purchaseVoucherNo = purchaseVoucherNo;
    }

    public Long getPositionId() {
        return positionId;
    }

    public void setPositionId(Long positionId) {
        this.positionId = positionId;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setReturnOrderNo(String returnOrderNo)
    {
        this.returnOrderNo = returnOrderNo;
    }

    public String getReturnOrderNo()
    {
        return returnOrderNo;
    }
    public void setLineNo(String lineNo)
    {
        this.lineNo = lineNo;
    }

    public String getLineNo()
    {
        return lineNo;
    }
    public void setMaterialNo(String materialNo)
    {
        this.materialNo = materialNo;
    }

    public String getMaterialNo()
    {
        return materialNo;
    }
    public void setMaterialName(String materialName)
    {
        this.materialName = materialName;
    }

    public String getMaterialName()
    {
        return materialName;
    }
    public void setOldMaterialNo(String oldMaterialNo)
    {
        this.oldMaterialNo = oldMaterialNo;
    }

    public String getOldMaterialNo()
    {
        return oldMaterialNo;
    }
    public void setFactoryCode(String factoryCode)
    {
        this.factoryCode = factoryCode;
    }

    public String getFactoryCode()
    {
        return factoryCode;
    }
    public void setReturnQty(BigDecimal returnQty)
    {
        this.returnQty = returnQty;
    }

    public BigDecimal getReturnQty()
    {
        return returnQty;
    }
    public void setUnit(String unit)
    {
        this.unit = unit;
    }

    public String getUnit()
    {
        return unit;
    }
    public void setTenantId(Long tenantId)
    {
        this.tenantId = tenantId;
    }

    public Long getTenantId()
    {
        return tenantId;
    }

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public String getOperationUnit() {
        return operationUnit;
    }

    public void setOperationUnit(String operationUnit) {
        this.operationUnit = operationUnit;
    }

    public BigDecimal getOperationQty() {
        return operationQty;
    }

    public void setOperationQty(BigDecimal operationQty) {
        this.operationQty = operationQty;
    }

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    public String getContainerType() {
        return containerType;
    }

    public void setContainerType(String containerType) {
        this.containerType = containerType;
    }

    public String getTaskBookNo() {
        return taskBookNo;
    }

    public void setTaskBookNo(String taskBookNo) {
        this.taskBookNo = taskBookNo;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("returnOrderNo", getReturnOrderNo())
                .append("lineNo", getLineNo())
                .append("materialNo", getMaterialNo())
                .append("materialName", getMaterialName())
                .append("oldMaterialNo", getOldMaterialNo())
                .append("factoryCode", getFactoryCode())
                .append("returnQty", getReturnQty())
                .append("remark", getRemark())
                .append("unit", getUnit())
                .append("operationUnit", getOperationUnit())
                .append("operationQty", getOperationQty())
                .append("tenantId", getTenantId())
                .append("lot", getLot())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
