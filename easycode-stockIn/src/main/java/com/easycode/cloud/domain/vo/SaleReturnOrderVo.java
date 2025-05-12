package com.easycode.cloud.domain.vo;

import com.weifu.cloud.common.core.annotation.Excel;
import com.weifu.cloud.common.core.web.domain.BaseEntity;
import com.easycode.cloud.domain.SaleReturnOrderDetail;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.ibatis.type.Alias;

import java.util.List;

/**
 * 销售发货退货单对象 wms_sale_center_return_order
 *
 * @author fsc
 * @date 2023-03-11
 */
@Alias("SaleReturnOrderVo")
public class SaleReturnOrderVo extends BaseEntity
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

    private List<SaleReturnOrderDetail> detailList;

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

    public List<SaleReturnOrderDetail> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<SaleReturnOrderDetail> detailList) {
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
                .append("saleCode", getSaleCode())
                .append("saleDesc", getSaleDesc())
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
