package com.ruoyi.system.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 人工报价成本对象 t_cost_labor
 * 
 * @author ruoyi
 * @date 2024-10-12
 */
public class TCostLabor extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 报价编号 */
    @Excel(name = "报价编号")
    private String quoteNo;

    /** 客户id */
    @Excel(name = "客户id")
    private Long customerId;

    /** 加工ID */
    @Excel(name = "加工ID")
    private Long workTypeId;

    /** 加工名称 */
    @Excel(name = "加工名称")
    private String workTypeName;

    /** 人工成本 */
    @Excel(name = "人工成本")
    private BigDecimal costLabor;

    /** 报价成本 */
    @Excel(name = "报价成本")
    private BigDecimal costPrice;

    /** 类型：1、人工成本，2、报价成本 */
    @Excel(name = "类型：1、人工成本，2、报价成本")
    private Integer types;

    /** 金额 */
    @Excel(name = "金额")
    private BigDecimal price;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setQuoteNo(String quoteNo) 
    {
        this.quoteNo = quoteNo;
    }

    public String getQuoteNo() 
    {
        return quoteNo;
    }
    public void setCustomerId(Long customerId) 
    {
        this.customerId = customerId;
    }

    public Long getCustomerId() 
    {
        return customerId;
    }
    public void setWorkTypeId(Long workTypeId) 
    {
        this.workTypeId = workTypeId;
    }

    public Long getWorkTypeId() 
    {
        return workTypeId;
    }
    public void setWorkTypeName(String workTypeName) 
    {
        this.workTypeName = workTypeName;
    }

    public String getWorkTypeName() 
    {
        return workTypeName;
    }
    public void setCostLabor(BigDecimal costLabor) 
    {
        this.costLabor = costLabor;
    }

    public BigDecimal getCostLabor() 
    {
        return costLabor;
    }
    public void setCostPrice(BigDecimal costPrice) 
    {
        this.costPrice = costPrice;
    }

    public BigDecimal getCostPrice() 
    {
        return costPrice;
    }
    public void setTypes(Integer types) 
    {
        this.types = types;
    }

    public Integer getTypes() 
    {
        return types;
    }
    public void setPrice(BigDecimal price) 
    {
        this.price = price;
    }

    public BigDecimal getPrice() 
    {
        return price;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("quoteNo", getQuoteNo())
            .append("customerId", getCustomerId())
            .append("workTypeId", getWorkTypeId())
            .append("workTypeName", getWorkTypeName())
            .append("costLabor", getCostLabor())
            .append("costPrice", getCostPrice())
            .append("types", getTypes())
            .append("price", getPrice())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .toString();
    }
}
