package com.easycode.cloud.domain;

import com.weifu.cloud.common.core.annotation.Excel;
import com.weifu.cloud.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.ibatis.type.Alias;

/**
 * 半成品入库单对象 wms_stockin_semifin_order
 * 
 * @author bcp
 * @date 2023-07-22
 */
@Alias("StockInSemiFinOrder")
public class StockInSemiFinOrder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 半成品入库单号 */
    @Excel(name = "半成品入库单号")
    private String orderNo;

    /** 物料号 */
    @Excel(name = "物料号")
    private String materialNo;

    /** 旧物料号 */
    @Excel(name = "旧物料号")
    private String oldMaterialNo;

    /** 物料名称 */
    @Excel(name = "物料名称")
    private String materialName;

    /** 工厂代码 */
    @Excel(name = "工厂代码")
    private String factoryCode;

    /** 单据状态:字典 */
    @Excel(name = "单据状态:字典")
    private String orderStatus;

    /** 租户号 */
    @Excel(name = "租户号")
    private Long tenantId;

    /** 部门id */
    @Excel(name = "部门id")
    private Long deptId;

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
    public void setFactoryCode(String factoryCode) 
    {
        this.factoryCode = factoryCode;
    }

    public String getFactoryCode() 
    {
        return factoryCode;
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
    public void setDeptId(Long deptId) 
    {
        this.deptId = deptId;
    }

    public Long getDeptId() 
    {
        return deptId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("orderNo", getOrderNo())
            .append("materialNo", getMaterialNo())
            .append("oldMaterialNo", getOldMaterialNo())
            .append("materialName", getMaterialName())
            .append("factoryCode", getFactoryCode())
            .append("orderStatus", getOrderStatus())
            .append("tenantId", getTenantId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("deptId", getDeptId())
            .toString();
    }
}
