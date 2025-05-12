package com.easycode.cloud.domain;

import com.weifu.cloud.common.core.annotation.Excel;
import com.weifu.cloud.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;


/**
 * 生产订单发货退货单明细对象 wms_prd_return_order_detail
 *
 * @author bcp
 * @date 2023-03-14
 */
@Alias("PrdReturnOrderDetail")
public class PrdReturnOrderDetail extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 关联退货单号
     */
    @Excel(name = "关联退货单号")
    private String returnOrderNo;

    /**
     * 生产发货单行号
     */
    @Excel(name = "行号")
    private String lineNo;

    /**
     * 物料代码
     */
    @Excel(name = "物料代码")
    private String materialNo;

    /**
     * 物料描述
     */
    @Excel(name = "物料描述")
    private String materialName;

    /**
     * 旧物料号
     */
    @Excel(name = "旧物料号")
    private String oldMaterialNo;

    /**
     * 工厂代码
     */
    @Excel(name = "工厂代码")
    private String factoryCode;

    /**
     * 原库存地点
     */
    private String storageLocation;

    /**
     * 退货数量
     */
    @Excel(name = "退货数量")
    private BigDecimal returnQty;

    /**
     * 单位
     */
    @Excel(name = "单位")
    private String unit;

    /**
     * 完成数量
     */
    @Excel(name = "完成数量")
    private BigDecimal completeQty;

    /**
     * 租户号
     */
    @Excel(name = "租户号")
    private Long tenantId;

    /**
     * 批次号
     */
    @Excel(name = "批次号")
    private String lot;

    /**
     * 生产订单号
     */
    @Excel(name = "生产订单号")
    private String prdOrderNo;

    /**
     * 生产订单单行号
     */
    private String prdOrderLineNo;

    /**
     * 操作单位
     */
    private String operationUnit;

    /**
     * 退货操作数量
     */
    private BigDecimal operationReturnQty;

    /**
     * 完成操作数量
     */
    private BigDecimal operationCompleteQty;

    /**
     * 默认单位换算
     */
    private BigDecimal conversDefault;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setReturnOrderNo(String returnOrderNo) {
        this.returnOrderNo = returnOrderNo;
    }

    public String getReturnOrderNo() {
        return returnOrderNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
    }

    public String getLineNo() {
        return lineNo;
    }

    public void setMaterialNo(String materialNo) {
        this.materialNo = materialNo;
    }

    public String getMaterialNo() {
        return materialNo;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setOldMaterialNo(String oldMaterialNo) {
        this.oldMaterialNo = oldMaterialNo;
    }

    public String getOldMaterialNo() {
        return oldMaterialNo;
    }

    public void setFactoryCode(String factoryCode) {
        this.factoryCode = factoryCode;
    }

    public String getFactoryCode() {
        return factoryCode;
    }

    public String getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }

    public void setReturnQty(BigDecimal returnQty) {
        this.returnQty = returnQty;
    }

    public BigDecimal getReturnQty() {
        return returnQty;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnit() {
        return unit;
    }

    public void setCompleteQty(BigDecimal completeQty) {
        this.completeQty = completeQty;
    }

    public BigDecimal getCompleteQty() {
        return completeQty;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public String getLot() {
        return lot;
    }

    public void setPrdOrderNo(String prdOrderNo) {
        this.prdOrderNo = prdOrderNo;
    }

    public String getPrdOrderNo() {
        return prdOrderNo;
    }

    public String getPrdOrderLineNo() {
        return prdOrderLineNo;
    }

    public void setPrdOrderLineNo(String prdOrderLineNo) {
        this.prdOrderLineNo = prdOrderLineNo;
    }

    public String getOperationUnit() {
        return operationUnit;
    }

    public void setOperationUnit(String operationUnit) {
        this.operationUnit = operationUnit;
    }

    public BigDecimal getOperationReturnQty() {
        return operationReturnQty;
    }

    public void setOperationReturnQty(BigDecimal operationReturnQty) {
        this.operationReturnQty = operationReturnQty;
    }

    public BigDecimal getOperationCompleteQty() {
        return operationCompleteQty;
    }

    public void setOperationCompleteQty(BigDecimal operationCompleteQty) {
        this.operationCompleteQty = operationCompleteQty;
    }

    public BigDecimal getConversDefault() {
        return conversDefault;
    }

    public void setConversDefault(BigDecimal conversDefault) {
        this.conversDefault = conversDefault;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("returnOrderNo", getReturnOrderNo())
                .append("lineNo", getLineNo())
                .append("materialNo", getMaterialNo())
                .append("materialName", getMaterialName())
                .append("oldMaterialNo", getOldMaterialNo())
                .append("factoryCode", getFactoryCode())
                .append("storageLocation", getStorageLocation())
                .append("returnQty", getReturnQty())
                .append("remark", getRemark())
                .append("unit", getUnit())
                .append("completeQty", getCompleteQty())
                .append("tenantId", getTenantId())
                .append("lot", getLot())
                .append("prdOrderNo", getPrdOrderNo())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
