package com.easycode.cloud.domain.dto;

import com.weifu.cloud.common.core.annotation.Excel;
import com.weifu.cloud.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;


/**
 * 委外出库单明细对象 wms_outsourced_stockout_order_detail
 *
 * @author bcp
 * @date 2023-03-01
 */
@Alias("OutsourcedStockOutOrderDetail")
public class OutsourcedStockOutOrderDetail extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 关联委外出库单号 */
    @Excel(name = "关联委外出库单号")
    private String outsourcedStockoutOrderNo;

    /** 物料号 */
    @Excel(name = "物料号")
    private String materialNo;

    /** 旧物料号 */
    @Excel(name = "旧物料号")
    private String oldMaterialNo;

    /** 物料名称 */
    @Excel(name = "物料名称")
    private String materialName;

    /** 需求数量 */
    @Excel(name = "需求数量")
    private BigDecimal requestQty;

    /** 激活数量 */
    @Excel(name = "激活数量")
    private BigDecimal activeQty;

    /** 完成数量 */
    @Excel(name = "完成数量")
    private BigDecimal finishQty;

    /** 是否分拣 */
    @Excel(name = "是否分拣")
    private String isSort;

    /** 需求操作数量 */
    @Excel(name = "需求操作数量")
    private BigDecimal operationRequestQty;

    /** 激活操作数量 */
    @Excel(name = "激活操作数量")
    private BigDecimal operationActiveQty;

    /** 完成操作数量 */
    @Excel(name = "完成操作数量")
    private BigDecimal operationFinishQty;

    /** 缺货操作数量 */
    @Excel(name = "缺货操作数量")
    private BigDecimal operationShortageQty;

    @Excel(name = "操作单位")
    private String operationUnit;

    /** 换算数值 */
    @Excel(name = "换算数值")
    private BigDecimal converterDefault;

    /** 单位 */
    @Excel(name = "单位")
    private String unit;

    /** 缺料数量 */
    @Excel(name = "缺料数量")
    private BigDecimal shortageQty;

    /** 工厂代码 */
    @Excel(name = "工厂代码")
    private String factoryCode;

    /** 工厂id */
    @Excel(name = "工厂id")
    private Long factoryId;

    /** 租户号 */
    @Excel(name = "租户号")
    private Long tenantId;


    /** 发货数量 */
    @Excel(name = "发货数量")
    private BigDecimal quantityDeliver;

    @Excel(name = "需求数量(适配前端)")
    private BigDecimal qty;


    /** 台账ID */
    @Excel(name = "台账ID")
    private Long inventoryId;

    /** 采购凭证号 */
    @Excel(name = "采购凭证号")
    private String purchaseVoucherNo;

    private String orderNo;

    private String lot ;
    /** 供应商编码 */
    @Excel(name = "供应商编码")
    private String supplierCode;

    /** 库存地点 */
    @Excel(name = "库存地点")
    private String locationCode;

    /** 供应商名字 */
    @Excel(name = "供应商名字")
    private String supplierName;


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

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getPurchaseVoucherNo() {
        return purchaseVoucherNo;
    }

    public void setPurchaseVoucherNo(String purchaseVoucherNo) {
        this.purchaseVoucherNo = purchaseVoucherNo;
    }

    public Long getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Long inventoryId) {
        this.inventoryId = inventoryId;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setOutsourcedStockoutOrderNo(String outsourcedStockoutOrderNo)
    {
        this.outsourcedStockoutOrderNo = outsourcedStockoutOrderNo;
    }

    public String getOutsourcedStockoutOrderNo()
    {
        return outsourcedStockoutOrderNo;
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
    public void setRequestQty(BigDecimal requestQty)
    {
        this.requestQty = requestQty;
    }

    public BigDecimal getRequestQty()
    {
        return requestQty;
    }
    public void setActiveQty(BigDecimal activeQty)
    {
        this.activeQty = activeQty;
    }

    public BigDecimal getActiveQty()
    {
        return activeQty;
    }
    public void setFinishQty(BigDecimal finishQty)
    {
        this.finishQty = finishQty;
    }

    public BigDecimal getFinishQty()
    {
        return finishQty;
    }
    public void setIsSort(String isSort)
    {
        this.isSort = isSort;
    }

    public String getIsSort()
    {
        return isSort;
    }
    public void setShortageQty(BigDecimal shortageQty)
    {
        this.shortageQty = shortageQty;
    }

    public BigDecimal getShortageQty()
    {
        return shortageQty;
    }
    public void setFactoryCode(String factoryCode)
    {
        this.factoryCode = factoryCode;
    }

    public String getFactoryCode()
    {
        return factoryCode;
    }
    public void setFactoryId(Long factoryId)
    {
        this.factoryId = factoryId;
    }

    public Long getFactoryId()
    {
        return factoryId;
    }
    public void setTenantId(Long tenantId)
    {
        this.tenantId = tenantId;
    }

    public Long getTenantId()
    {
        return tenantId;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getOperationRequestQty() {
        return operationRequestQty;
    }

    public void setOperationRequestQty(BigDecimal operationRequestQty) {
        this.operationRequestQty = operationRequestQty;
    }

    public BigDecimal getOperationActiveQty() {
        return operationActiveQty;
    }

    public void setOperationActiveQty(BigDecimal operationActiveQty) {
        this.operationActiveQty = operationActiveQty;
    }

    public BigDecimal getOperationFinishQty() {
        return operationFinishQty;
    }

    public void setOperationFinishQty(BigDecimal operationFinishQty) {
        this.operationFinishQty = operationFinishQty;
    }

    public BigDecimal getOperationShortageQty() {
        return operationShortageQty;
    }

    public void setOperationShortageQty(BigDecimal operationShortageQty) {
        this.operationShortageQty = operationShortageQty;
    }

    public String getOperationUnit() {
        return operationUnit;
    }

    public void setOperationUnit(String operationUnit) {
        this.operationUnit = operationUnit;
    }

    public BigDecimal getConverterDefault() {
        return converterDefault;
    }

    public void setConverterDefault(BigDecimal converterDefault) {
        this.converterDefault = converterDefault;
    }

    public BigDecimal getQuantityDeliver() {
        return quantityDeliver;
    }

    public void setQuantityDeliver(BigDecimal quantityDeliver) {
        this.quantityDeliver = quantityDeliver;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("outsourcedStockoutOrderNo", getOutsourcedStockoutOrderNo())
            .append("materialNo", getMaterialNo())
            .append("oldMaterialNo", getOldMaterialNo())
            .append("materialName", getMaterialName())
            .append("requestQty", getRequestQty())
            .append("activeQty", getActiveQty())
            .append("finishQty", getFinishQty())
            .append("isSort", getIsSort())
            .append("shortageQty", getShortageQty())
            .append("factoryCode", getFactoryCode())
            .append("factoryId", getFactoryId())
            .append("tenantId", getTenantId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("quantityDeliver", getQuantityDeliver())
            .toString();
    }
}
