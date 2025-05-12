package com.easycode.cloud.domain.vo;

import com.weifu.cloud.common.core.annotation.Excel;
import com.weifu.cloud.common.core.web.domain.BaseEntity;
import com.easycode.cloud.domain.RwmOutSourceReturnOrderDetail;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.ibatis.type.Alias;

import java.util.List;

/**
 * 原材料委外发料退退货单对象 wms_rwm_outsource_return_order
 *
 * @author fsc
 * @date 2023-03-11
 */
@Alias("RwmOutSourceReturnOrderVo")
public class RwmOutSourceReturnOrderVo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 原材料委外发料退退货单号 */
    @Excel(name = "原材料委外发料退退货单号")
    private String orderNo;

    /** 货主 */
    @Excel(name = "货主")
    private String factoryCode;

    /** 货主名称 */
    @Excel(name = "货主名称")
    private String factoryName;

    /** 原材料委外发料退代码 */
    @Excel(name = "原材料委外发料退代码")
    private String rwmOutSourceCode;

    /** 原材料委外发料退描述 */
    @Excel(name = "原材料委外发料退描述")
    private String rwmOutSourceDesc;

    /** 移动类型 */
    @Excel(name = "移动类型")
    private String moveType;

    /** 原材料委外发料退管理类型 */
    @Excel(name = "原材料委外发料退管理类型")
    private String rwmMgType;

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

    private List<RwmOutSourceReturnOrderDetail> detailList;

    private String startTime;

    private String materialNo;

    private String endTime;

    /**
     * 容器号
     */
    private String containerNo;

    /**
     * 容器类型
     */
    private String containerType;

    /**
    * 供应商编码*/
    private String supplierCode;

    private String outsourcedStockoutOrderNo;

    private String purchaseVoucherNo;

    /**
     * 兼容前端*/
    private String purchaseOrderNo;

    public String getPurchaseOrderNo() {
        return purchaseOrderNo;
    }

    public void setPurchaseOrderNo(String purchaseOrderNo) {
        this.purchaseOrderNo = purchaseOrderNo;
    }

    public String getOutsourcedStockoutOrderNo() {
        return outsourcedStockoutOrderNo;
    }

    public void setOutsourcedStockoutOrderNo(String outsourcedStockoutOrderNo) {
        this.outsourcedStockoutOrderNo = outsourcedStockoutOrderNo;
    }

    public String getPurchaseVoucherNo() {
        return purchaseVoucherNo;
    }

    public void setPurchaseVoucherNo(String purchaseVoucherNo) {
        this.purchaseVoucherNo = purchaseVoucherNo;
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
    public void setRwmOutSourceCode(String rwmOutSourceCode)
    {
        this.rwmOutSourceCode = rwmOutSourceCode;
    }

    public String getRwmOutSourceCode()
    {
        return rwmOutSourceCode;
    }
    public void setRwmOutSourceDesc(String rwmOutSourceDesc)
    {
        this.rwmOutSourceDesc = rwmOutSourceDesc;
    }

    public String getRwmOutSourceDesc()
    {
        return rwmOutSourceDesc;
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
    @Override
    public void setTenantId(Long tenantId)
    {
        this.tenantId = tenantId;
    }

    @Override
    public Long getTenantId()
    {
        return tenantId;
    }

    public List<RwmOutSourceReturnOrderDetail> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<RwmOutSourceReturnOrderDetail> detailList) {
        this.detailList = detailList;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getRwmMgType() {
        return rwmMgType;
    }

    public void setRwmMgType(String rwmMgType) {
        this.rwmMgType = rwmMgType;
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

    public String getMaterialNo() {
        return materialNo;
    }

    public void setMaterialNo(String materialNo) {
        this.materialNo = materialNo;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("orderNo", getOrderNo())
                .append("factoryCode", getFactoryCode())
                .append("factoryName", getFactoryName())
                .append("rwmOutSourceCode", getRwmOutSourceCode())
                .append("rwmOutSourceDesc", getRwmOutSourceDesc())
                .append("moveType", getMoveType())
                .append("customerCode", getCustomerCode())
                .append("innerOrderNo", getInnerOrderNo())
                .append("orderStatus", getOrderStatus())
                .append("remark", getRemark())
                .append("tenantId", getTenantId())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
