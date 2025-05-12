package com.easycode.cloud.domain;

import com.weifu.cloud.common.core.annotation.Excel;
import com.weifu.cloud.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.ibatis.type.Alias;

/**
 * 其它入库单对象 wms_stockin_other
 *
 * @author bcp
 * @date 2023-03-24
 */
@Alias("StockInOther")
public class StockInOther extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 其它入库单号 */
    @Excel(name = "其它入库单号")
    private String orderNo;

    /** 货主 */
    @Excel(name = "货主")
    private String factoryCode;

    /** 库存地点 */
    @Excel(name = "库存地点")
    private String locationCode;

    /** 单据状态 */
    @Excel(name = "单据状态")
    private String billStatus;

    /** 入库类型 */
    @Excel(name = "入库类型")
    private String billType;

    /** 供应商名称 */
    @Excel(name = "供应商名称")
    private String supplierName;

    /** 供应商代码 */
    @Excel(name = "供应商代码")
    private String supplierCode;

    /**
     * 生产订单号
     */
    @Excel(name = "生产订单号")
    private String prdOrderNo;

    /** 租户号 */
    @Excel(name = "租户号")
    private Long tenantId;

    /** 部门id */
    private Long deptId;

    /**
     * 容器号
     */
    private String containerNo;

    /** WBS元素 */
    private String wbsElement;

    /**
     * 容器类型
     */
    private String containerType;
    /**
     * 移动类型
     */
    private String moveType;
    /**
     * hu号
     */
    private String huNo;
    /**
     * mes容器号
     */
    private String mesContainerNo;

    public String getMoveType() {
        return moveType;
    }

    public void setMoveType(String moveType) {
        this.moveType = moveType;
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

    /**
     * 特殊库存类型
     */
    private String specialType;
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
    public void setFactoryCode(String factoryCode)
    {
        this.factoryCode = factoryCode;
    }

    public String getFactoryCode()
    {
        return factoryCode;
    }
    public void setLocationCode(String locationCode)
    {
        this.locationCode = locationCode;
    }

    public String getLocationCode()
    {
        return locationCode;
    }
    public void setBillStatus(String billStatus)
    {
        this.billStatus = billStatus;
    }

    public String getBillStatus()
    {
        return billStatus;
    }
    public void setSupplierName(String supplierName)
    {
        this.supplierName = supplierName;
    }

    public String getSupplierName()
    {
        return supplierName;
    }
    public void setSupplierCode(String supplierCode)
    {
        this.supplierCode = supplierCode;
    }

    public String getSupplierCode()
    {
        return supplierCode;
    }
    public void setTenantId(Long tenantId)
    {
        this.tenantId = tenantId;
    }

    public String getPrdOrderNo() {
        return prdOrderNo;
    }

    public void setPrdOrderNo(String prdOrderNo) {
        this.prdOrderNo = prdOrderNo;
    }

    public Long getTenantId()
    {
        return tenantId;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
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

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("orderNo", getOrderNo())
            .append("factoryCode", getFactoryCode())
            .append("locationCode", getLocationCode())
            .append("billStatus", getBillStatus())
            .append("supplierName", getSupplierName())
            .append("supplierCode", getSupplierCode())
            .append("billType", getBillType())
            .append("tenantId", getTenantId())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("wbsElements", getWbsElement())
            .toString();
    }
}
