package com.ruoyi.system.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 数割费用对象 t_number_cut_cost
 * 
 * @author ruoyi
 * @date 2024-10-10
 */
public class TNumberCutCost extends BaseEntity
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

    /** 数割 */
    @Excel(name = "数割")
    private BigDecimal cutNum;

    /** 数割下料费 */
    @Excel(name = "数割下料费")
    private BigDecimal cutMaterial;

    /** 数割费用小计 */
    @Excel(name = "数割费用小计")
    private BigDecimal totalCut;

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
    public void setCutNum(BigDecimal cutNum) 
    {
        this.cutNum = cutNum;
    }

    public BigDecimal getCutNum() 
    {
        return cutNum;
    }
    public void setCutMaterial(BigDecimal cutMaterial) 
    {
        this.cutMaterial = cutMaterial;
    }

    public BigDecimal getCutMaterial() 
    {
        return cutMaterial;
    }
    public void setTotalCut(BigDecimal totalCut) 
    {
        this.totalCut = totalCut;
    }

    public BigDecimal getTotalCut() 
    {
        return totalCut;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("quoteNo", getQuoteNo())
            .append("customerId", getCustomerId())
            .append("cutNum", getCutNum())
            .append("cutMaterial", getCutMaterial())
            .append("totalCut", getTotalCut())
            .toString();
    }
}
