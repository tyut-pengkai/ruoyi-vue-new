package com.easycode.cloud.domain;

import com.weifu.cloud.common.core.annotation.Excel;
import com.weifu.cloud.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.ibatis.type.Alias;

/**
 * 成本中心退货单对象 wms_cost_center_return_order
 *
 * @author fsc
 * @date 2023-03-11
 */
@Alias("CostCenterReturnOrder")
public class CostCenterReturnOrder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 成本中心退货单号 */
    @Excel(name = "成本中心退货单号")
    private String orderNo;

    /** 货主 */
    @Excel(name = "货主")
    private String factoryCode;

    /** 货主名称 */
    @Excel(name = "货主名称")
    private String factoryName;

    /** 成本中心代码 */
    @Excel(name = "成本中心代码")
    private String costCenterCode;

    /** 成本中心描述 */
    @Excel(name = "成本中心描述")
    private String costCenterDesc;

    /** 移动类型 */
    @Excel(name = "移动类型")
    private String moveType;

    /** 成本中心管理类型 */
    @Excel(name = "成本中心管理类型")
    private String costMgType;

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
    public void setCostCenterCode(String costCenterCode)
    {
        this.costCenterCode = costCenterCode;
    }

    public String getCostCenterCode()
    {
        return costCenterCode;
    }
    public void setCostCenterDesc(String costCenterDesc)
    {
        this.costCenterDesc = costCenterDesc;
    }

    public String getCostCenterDesc()
    {
        return costCenterDesc;
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

    public String getCostMgType() {
        return costMgType;
    }

    public void setCostMgType(String costMgType) {
        this.costMgType = costMgType;
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
                .append("costCenterCode", getCostCenterCode())
                .append("costCenterDesc", getCostCenterDesc())
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
