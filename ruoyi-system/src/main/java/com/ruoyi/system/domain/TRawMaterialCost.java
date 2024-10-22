package com.ruoyi.system.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 原材料费用对象 t_raw_material_cost
 * 
 * @author ruoyi
 * @date 2024-10-10
 */
public class TRawMaterialCost extends BaseEntity
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
    /** 材料规格 */
    @Excel(name = "材料规格")
    private String materialSpec;
    /** 单件重量 */
    @Excel(name = "单件重量")
    private BigDecimal perWight;
    /** 净重(数量*单件重量) */
    @Excel(name = "净重(数量*单件重量)")
    private BigDecimal netWight;
    /** 长 */
    @Excel(name = "长")
    private BigDecimal steelLen;
    /** 宽 */
    @Excel(name = "宽")
    private BigDecimal steelWid;
    /** 高 */
    @Excel(name = "高")
    private BigDecimal steelHei;
    /** 毛坯重量 */
    @Excel(name = "毛坯重量")
    private BigDecimal steelWight;
    /** 材料单价 */
    @Excel(name = "材料单价")
    private BigDecimal steelPerPrice;
    /** 毛坯费 */
    @Excel(name = "毛坯费")
    private BigDecimal steelPrice;
    /** 废料重 */
    @Excel(name = "废料重")
    private BigDecimal steelScrapWgt;
    /** 废料单价 */
    @Excel(name = "废料单价")
    private BigDecimal steelScrapPer;
    /** 废料费 */
    @Excel(name = "废料费")
    private BigDecimal steelScrapPrice;
    /** 原料材料费用小计 */
    @Excel(name = "原料材料费用小计")
    private BigDecimal totalSteel;

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
    public void setMaterialSpec(String materialSpec) 
    {
        this.materialSpec = materialSpec;
    }

    public String getMaterialSpec() 
    {
        return materialSpec;
    }
    public void setPerWight(BigDecimal perWight) 
    {
        this.perWight = perWight;
    }

    public BigDecimal getPerWight() 
    {
        return perWight;
    }
    public void setNetWight(BigDecimal netWight) 
    {
        this.netWight = netWight;
    }

    public BigDecimal getNetWight() 
    {
        return netWight;
    }
    public void setSteelLen(BigDecimal steelLen) 
    {
        this.steelLen = steelLen;
    }

    public BigDecimal getSteelLen() 
    {
        return steelLen;
    }
    public void setSteelWid(BigDecimal steelWid) 
    {
        this.steelWid = steelWid;
    }

    public BigDecimal getSteelWid() 
    {
        return steelWid;
    }
    public void setSteelHei(BigDecimal steelHei) 
    {
        this.steelHei = steelHei;
    }

    public BigDecimal getSteelHei() 
    {
        return steelHei;
    }
    public void setSteelWight(BigDecimal steelWight) 
    {
        this.steelWight = steelWight;
    }

    public BigDecimal getSteelWight() 
    {
        return steelWight;
    }
    public void setSteelPerPrice(BigDecimal steelPerPrice) 
    {
        this.steelPerPrice = steelPerPrice;
    }

    public BigDecimal getSteelPerPrice() 
    {
        return steelPerPrice;
    }
    public void setSteelPrice(BigDecimal steelPrice) 
    {
        this.steelPrice = steelPrice;
    }

    public BigDecimal getSteelPrice() 
    {
        return steelPrice;
    }
    public void setSteelScrapWgt(BigDecimal steelScrapWgt) 
    {
        this.steelScrapWgt = steelScrapWgt;
    }

    public BigDecimal getSteelScrapWgt() 
    {
        return steelScrapWgt;
    }
    public void setSteelScrapPer(BigDecimal steelScrapPer) 
    {
        this.steelScrapPer = steelScrapPer;
    }

    public BigDecimal getSteelScrapPer() 
    {
        return steelScrapPer;
    }
    public void setSteelScrapPrice(BigDecimal steelScrapPrice) 
    {
        this.steelScrapPrice = steelScrapPrice;
    }

    public BigDecimal getSteelScrapPrice() 
    {
        return steelScrapPrice;
    }
    public void setTotalSteel(BigDecimal totalSteel) 
    {
        this.totalSteel = totalSteel;
    }

    public BigDecimal getTotalSteel() 
    {
        return totalSteel;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("quoteNo", getQuoteNo())
            .append("customerId", getCustomerId())
            .append("materialSpec", getMaterialSpec())
            .append("perWight", getPerWight())
            .append("netWight", getNetWight())
            .append("steelLen", getSteelLen())
            .append("steelWid", getSteelWid())
            .append("steelHei", getSteelHei())
            .append("steelWight", getSteelWight())
            .append("steelPerPrice", getSteelPerPrice())
            .append("steelPrice", getSteelPrice())
            .append("steelScrapWgt", getSteelScrapWgt())
            .append("steelScrapPer", getSteelScrapPer())
            .append("steelScrapPrice", getSteelScrapPrice())
            .append("totalSteel", getTotalSteel())
            .toString();
    }
}
