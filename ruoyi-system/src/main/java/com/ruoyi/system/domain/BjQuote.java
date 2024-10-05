package com.ruoyi.system.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 对外报价对象 bj_quote
 * 
 * @author ssq
 * @date 2024-10-05
 */
public class BjQuote extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 报价单id */
    private Long id;

    /** 物料编码 */
    @Excel(name = "物料编码")
    private Long materialsNo;

    /** 物料名称 */
    @Excel(name = "物料名称")
    private String name;

    /** 净重(数量*单件重量) */
    @Excel(name = "净重(数量*单件重量)")
    private Long netWight;

    /** 未税 */
    @Excel(name = "未税")
    private BigDecimal noTax;

    /** 公斤价 */
    @Excel(name = "公斤价")
    private BigDecimal perPrice;

    /** 备注 */
    @Excel(name = "备注")
    private String commit;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setMaterialsNo(Long materialsNo) 
    {
        this.materialsNo = materialsNo;
    }

    public Long getMaterialsNo() 
    {
        return materialsNo;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setNetWight(Long netWight) 
    {
        this.netWight = netWight;
    }

    public Long getNetWight() 
    {
        return netWight;
    }
    public void setNoTax(BigDecimal noTax) 
    {
        this.noTax = noTax;
    }

    public BigDecimal getNoTax() 
    {
        return noTax;
    }
    public void setPerPrice(BigDecimal perPrice) 
    {
        this.perPrice = perPrice;
    }

    public BigDecimal getPerPrice() 
    {
        return perPrice;
    }
    public void setCommit(String commit) 
    {
        this.commit = commit;
    }

    public String getCommit() 
    {
        return commit;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("materialsNo", getMaterialsNo())
            .append("name", getName())
            .append("netWight", getNetWight())
            .append("noTax", getNoTax())
            .append("perPrice", getPerPrice())
            .append("commit", getCommit())
            .toString();
    }
}
