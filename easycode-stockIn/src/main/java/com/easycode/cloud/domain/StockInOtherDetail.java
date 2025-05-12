package com.easycode.cloud.domain;

import com.weifu.cloud.common.core.annotation.Excel;
import com.weifu.cloud.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;

/**
 * 其它入库明细对象 wms_stockin_other_detail
 *
 * @author bcp
 * @date 2023-03-27
 */
@Alias("StockInOtherDetail")
public class StockInOtherDetail extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 关联其它入库单号 */
    @Excel(name = "关联其它入库单号")
    private String orderNo;

    /** 行号 */
    @Excel(name = "行号")
    private String lineNo;

    /** 物料代码 */
    @Excel(name = "物料代码")
    private String materialNo;

    /** 物料描述 */
    @Excel(name = "物料描述")
    private String materialName;

    /** 旧物料号 */
    @Excel(name = "旧物料号")
    private String oldMaterialNo;

    /** 入库数量 */
    @Excel(name = "入库数量")
    private BigDecimal qty;

    /** 单位 */
    @Excel(name = "单位")
    private String unit;

    /** 操作单位 */
    @Excel(name = "操作单位")
    private String operationUnit;

    /** 操作单位对应数量 */
    @Excel(name = "操作单位对应数量")
    private BigDecimal operationQty;

    /** 批次 */
    @Excel(name = "批次")
    private String lot;

    /** 是否寄售 */
    @Excel(name = "是否寄售")
    private String isConsigned;

    /** 是否项目 */
    @Excel(name = "是否项目")
    private String isProject;

    /** 是否质检 */
    @Excel(name = "是否质检")
    private String isQc;

    /** 工厂 */
    @Excel(name = "工厂")
    private String factoryCode;

    /** 供应商代码 */
    @Excel(name = "供应商代码")
    private String supplierCode;

    /** 供应商 */
    @Excel(name = "供应商")
    private Long supplierId;

    /** 租户号 */
    @Excel(name = "租户号")
    private Long tenantId;

    @Excel(name = "成本中心")
    private String costCenterCode;

    /**
     * 容器号
     */
    private String containerNo;

    /**
     * 容器类型
     */
    private String containerType;

    /** WBS元素 */
    private String wbsElement;
    //用户名
private String uname;
//存储地点
    private String stgeLoc;
    //移动类型
    private String moveType;
//hu号
    private String huNo;
    //mes容器号
    private String mesContainerNo;

    public String getIsProject() {
        return isProject;
    }

    public void setIsProject(String isProject) {
        this.isProject = isProject;
    }

    public String getCostCenterCode() {
        return costCenterCode;
    }

    public void setCostCenterCode(String costCenterCode) {
        this.costCenterCode = costCenterCode;
    }

    public String getHuNo() {
        return huNo;
    }

    public void setHuNo(String huNo) {
        this.huNo = huNo;
    }

    public String getMesContainerNo() {
        return mesContainerNo;
    }

    public void setMesContainerNo(String mesContainerNo) {
        this.mesContainerNo = mesContainerNo;
    }

    public String getSpecialType() {
        return specialType;
    }

    public void setSpecialType(String specialType) {
        this.specialType = specialType;
    }

    //特殊库存类型
    private String specialType;
    public String getMoveType() {
        return moveType;
    }

    public void setMoveType(String moveType) {
        this.moveType = moveType;
    }

    public String getStgeLoc() {
        return stgeLoc;
    }

    public void setStgeLoc(String stgeLoc) {
        this.stgeLoc = stgeLoc;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getWbsElement() {
        return wbsElement;
    }

    public void setWbsElement(String wbsElement) {
        this.wbsElement = wbsElement;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setOrderNo(String orderNo)
    {
        this.orderNo = orderNo;
    }

    public String getOrderNo()
    {
        return orderNo;
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
    public void setQty(BigDecimal qty)
    {
        this.qty = qty;
    }

    public BigDecimal getQty()
    {
        return qty;
    }
    public void setUnit(String unit)
    {
        this.unit = unit;
    }

    public String getUnit()
    {
        return unit;
    }
    public void setLot(String lot)
    {
        this.lot = lot;
    }

    public String getLot()
    {
        return lot;
    }
    public void setIsConsigned(String isConsigned)
    {
        this.isConsigned = isConsigned;
    }

    public String getIsConsigned()
    {
        return isConsigned;
    }
    public void setIsQc(String isQc)
    {
        this.isQc = isQc;
    }

    public String getIsQc()
    {
        return isQc;
    }
    public void setFactoryCode(String factoryCode)
    {
        this.factoryCode = factoryCode;
    }

    public String getFactoryCode()
    {
        return factoryCode;
    }
    public void setSupplierCode(String supplierCode)
    {
        this.supplierCode = supplierCode;
    }

    public String getSupplierCode()
    {
        return supplierCode;
    }
    public void setSupplierId(Long supplierId)
    {
        this.supplierId = supplierId;
    }

    public Long getSupplierId()
    {
        return supplierId;
    }
    public void setTenantId(Long tenantId)
    {
        this.tenantId = tenantId;
    }

    public Long getTenantId()
    {
        return tenantId;
    }

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
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

    public String getContainerType() {
        return containerType;
    }

    public void setContainerType(String containerType) {
        this.containerType = containerType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("orderNo", getOrderNo())
                .append("lineNo", getLineNo())
                .append("materialNo", getMaterialNo())
                .append("materialName", getMaterialName())
                .append("oldMaterialNo", getOldMaterialNo())
                .append("qty", getQty())
                .append("unit", getUnit())
                .append("lot", getLot())
                .append("isConsigned", getIsConsigned())
                .append("isQc", getIsQc())
                .append("factoryCode", getFactoryCode())
                .append("remark", getRemark())
                .append("supplierCode", getSupplierCode())
                .append("supplierId", getSupplierId())
                .append("tenantId", getTenantId())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("wbsElement", getWbsElement())
                .toString();
    }
}
