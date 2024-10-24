package com.ruoyi.system.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 加工费用对象 t_process_cost
 * 
 * @author ruoyi
 * @date 2024-10-10
 */
public class TProcessCost extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 报价单编号 */
    @Excel(name = "报价单编号")
    private String quoteNo;

    /** 客户id */
    @Excel(name = "客户id")
    private Long customerId;

    /** 工种名称 */
    @Excel(name = "工种名称")
    private String workTypeName;

    /** 工时 */
    @Excel(name = "工时")
    private BigDecimal workTypeHours;

    /** 金额 */
    @Excel(name = "金额")
    private BigDecimal workTypePrice;
    
    private Integer types;

    /** 报价 */
    @Excel(name = "报价")
    private BigDecimal quotaPrice;

    /** 费用小计 */
    @Excel(name = "费用小计")
    private BigDecimal totalPrice;

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
    public void setWorkTypeName(String workTypeName) 
    {
        this.workTypeName = workTypeName;
    }

    public String getWorkTypeName() 
    {
        return workTypeName;
    }
    public void setWorkTypeHours(BigDecimal workTypeHours) 
    {
        this.workTypeHours = workTypeHours;
    }

    public BigDecimal getWorkTypeHours() 
    {
        return workTypeHours;
    }
    public void setWorkTypePrice(BigDecimal workTypePrice) 
    {
        this.workTypePrice = workTypePrice;
    }

    public BigDecimal getWorkTypePrice() 
    {
        return workTypePrice;
    }
    public void setQuotaPrice(BigDecimal quotaPrice) 
    {
        this.quotaPrice = quotaPrice;
    }

    public BigDecimal getQuotaPrice() 
    {
        return quotaPrice;
    }
    public void setTotalPrice(BigDecimal totalPrice) 
    {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getTotalPrice() 
    {
        return totalPrice;
    }

    public Integer getTypes() {
		return types;
	}

	public void setTypes(Integer types) {
		this.types = types;
	}

	@Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("quoteNo", getQuoteNo())
            .append("customerId", getCustomerId())
            .append("workTypeName", getWorkTypeName())
            .append("workTypeHours", getWorkTypeHours())
            .append("workTypePrice", getWorkTypePrice())
            .append("quotaPrice", getQuotaPrice())
            .append("totalPrice", getTotalPrice())
            .toString();
    }
}
