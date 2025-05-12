package com.easycode.cloud.domain;

import com.weifu.cloud.common.core.annotation.Excel;
import com.weifu.cloud.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.ibatis.type.Alias;

/**
 * 销售发货退货单对象 wms_sale_return_order
 *
 * @author fsc
 * @date 2023-03-11
 */
@Alias("SaleReturnOrder")
public class SaleReturnOrder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 销售发货退货单号 */
    @Excel(name = "销售发货退货单号")
    private String orderNo;

    /** 货主 */
    @Excel(name = "货主")
    private String factoryCode;

    /** 货主名称 */
    @Excel(name = "货主名称")
    private String factoryName;

    /** 销售发货代码 */
    @Excel(name = "销售发货代码")
    private String saleCode;

    /** 销售发货描述 */
    @Excel(name = "销售发货描述")
    private String saleDesc;

    /** 移动类型 */
    @Excel(name = "移动类型")
    private String moveType;

    /** 销售发货管理类型 */
    @Excel(name = "销售发货管理类型")
    private String saleMgType;

    /** 客户编号 */
    @Excel(name = "客户编号")
    private String customerCode;

    /** 内部订单号 */
    @Excel(name = "内部订单号")
    private String innerOrderNo;

    /** 单据状态 */
    @Excel(name = "单据状态")
    private String orderStatus;

    /** 租户号 */
    @Excel(name = "租户号")
    private Long tenantId;

    /** 部门id */
    private Long deptId;

    /** HU号 */
    private String huNo;
    /**
     * 容器号
     */
    private String containerNo;

    /**
     * 容器类型
     */
    private String containerType;

    /**
     *记录创建日期
     */
    private String erdat;

    /**
     *交货类型
     */
    private String lfart;

    /**
     *名称
     */
    private String name1;

    /**
     *装运类型
     */
    private String vsart;

    /**
     *计划货物移动日期
     */
    private String wadat;

    /**
     *交货日期
     */
    private String lfdat;

    public String getErdat() {
        return erdat;
    }

    public void setErdat(String erdat) {
        this.erdat = erdat;
    }

    public String getLfart() {
        return lfart;
    }

    public void setLfart(String lfart) {
        this.lfart = lfart;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getVsart() {
        return vsart;
    }

    public void setVsart(String vsart) {
        this.vsart = vsart;
    }

    public String getWadat() {
        return wadat;
    }

    public void setWadat(String wadat) {
        this.wadat = wadat;
    }

    public String getLfdat() {
        return lfdat;
    }

    public void setLfdat(String lfdat) {
        this.lfdat = lfdat;
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
    public void setFactoryName(String factoryName)
    {
        this.factoryName = factoryName;
    }

    public String getFactoryName()
    {
        return factoryName;
    }
    public void setSaleCode(String saleCode)
    {
        this.saleCode = saleCode;
    }

    public String getSaleCode()
    {
        return saleCode;
    }
    public void setSaleDesc(String saleDesc)
    {
        this.saleDesc = saleDesc;
    }

    public String getSaleDesc()
    {
        return saleDesc;
    }
    public void setMoveType(String moveType)
    {
        this.moveType = moveType;
    }

    public String getMoveType()
    {
        return moveType;
    }
    public void setCustomerCode(String customerCode)
    {
        this.customerCode = customerCode;
    }

    public String getCustomerCode()
    {
        return customerCode;
    }
    public void setInnerOrderNo(String innerOrderNo)
    {
        this.innerOrderNo = innerOrderNo;
    }

    public String getInnerOrderNo()
    {
        return innerOrderNo;
    }
    public void setOrderStatus(String orderStatus)
    {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus()
    {
        return orderStatus;
    }
    public void setTenantId(Long tenantId)
    {
        this.tenantId = tenantId;
    }

    public Long getTenantId()
    {
        return tenantId;
    }

    public String getSaleMgType() {
        return saleMgType;
    }

    public void setSaleMgType(String saleMgType) {
        this.saleMgType = saleMgType;
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

    public String getHuNo() {
        return huNo;
    }

    public void setHuNo(String huNo) {
        this.huNo = huNo;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("orderNo", getOrderNo())
                .append("factoryCode", getFactoryCode())
                .append("factoryName", getFactoryName())
                .append("saleCode", getSaleCode())
                .append("saleDesc", getSaleDesc())
                .append("moveType", getMoveType())
                .append("customerCode", getCustomerCode())
                .append("innerOrderNo", getInnerOrderNo())
                .append("orderStatus", getOrderStatus())
                .append("remark", getRemark())
                .append("huNo", getHuNo())
                .append("tenantId", getTenantId())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
