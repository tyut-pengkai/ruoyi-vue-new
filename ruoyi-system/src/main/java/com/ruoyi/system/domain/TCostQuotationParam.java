package com.ruoyi.system.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 成本报价参数对象 t_cost_quotation_param
 * 
 * @author ruoyi
 * @date 2024-10-11
 */
public class TCostQuotationParam extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 加工类型id */
    @Excel(name = "加工类型id")
    private Long workTypeId;

    /** 加工类型名称 */
    @Excel(name = "加工类型名称")
    private String workTypeName;

    /** 成本价 */
    @Excel(name = "成本价")
    private BigDecimal cost;

    /** 报价 */
    @Excel(name = "报价")
    private BigDecimal quotation;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
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
    public void setCost(BigDecimal cost) 
    {
        this.cost = cost;
    }

    public BigDecimal getCost() 
    {
        return cost;
    }
    public void setQuotation(BigDecimal quotation) 
    {
        this.quotation = quotation;
    }

    public BigDecimal getQuotation() 
    {
        return quotation;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("workTypeId", getWorkTypeId())
            .append("workTypeName", getWorkTypeName())
            .append("cost", getCost())
            .append("quotation", getQuotation())
            .toString();
    }
}
