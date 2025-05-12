package com.easycode.cloud.domain;

import com.weifu.cloud.common.core.annotation.Excel;
import com.weifu.cloud.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.ibatis.type.Alias;


/**
 * 生产订单发货退料单对象 wms_prd_return_order
 *
 * @author bcp
 * @date 2023-03-11
 */
@Alias("PrdReturnOrder")
public class PrdReturnOrder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 生产订单退货单号 */
    @Excel(name = "生产订单退货单号")
    private String returnOrderNo;

    /** 生产订单号 */
    @Excel(name = "生产订单号")
    private String prdOrderNo;

    /** 产品代码 */
    @Excel(name = "产品代码")
    private String productCode;

    /** 产品描述 */
    @Excel(name = "产品描述")
    private String productDes;

    /** 货主代码 */
    @Excel(name = "货主代码")
    private String factoryCode;

    /** 货主名称 */
    @Excel(name = "货主名称")
    private String factoryName;

    /** 入库单状态 */
    @Excel(name = "入库单状态")
    private String prdOrderStatus;

    /** 订单状态 */
    @Excel(name = "订单状态")
    private String orderStatus;

    /** 租户号 */
    @Excel(name = "租户号")
    private Long tenantId;

    /** 部门id */
    private Long deptId;

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
    public void setPrdOrderNo(String prdOrderNo)
    {
        this.prdOrderNo = prdOrderNo;
    }

    public String getPrdOrderNo()
    {
        return prdOrderNo;
    }
    public void setProductCode(String productCode)
    {
        this.productCode = productCode;
    }

    public String getProductCode()
    {
        return productCode;
    }
    public void setProductDes(String productDes)
    {
        this.productDes = productDes;
    }

    public String getProductDes()
    {
        return productDes;
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
    public void setPrdOrderStatus(String prdOrderStatus)
    {
        this.prdOrderStatus = prdOrderStatus;
    }

    public String getPrdOrderStatus()
    {
        return prdOrderStatus;
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
            .append("returnOrderNo", getReturnOrderNo())
            .append("prdOrderNo", getPrdOrderNo())
            .append("productCode", getProductCode())
            .append("productDes", getProductDes())
            .append("factoryCode", getFactoryCode())
            .append("factoryName", getFactoryName())
            .append("prdOrderStatus", getPrdOrderStatus())
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
