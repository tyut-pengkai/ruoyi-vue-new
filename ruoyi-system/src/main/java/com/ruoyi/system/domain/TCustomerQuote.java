package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 客户报价单对象 t_customer_quote
 * 
 * @author ruoyi
 * @date 2024-10-10
 */
public class TCustomerQuote extends BaseEntity
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
    
    private Long materialsId;
    
    private Long parentMaterialsId;
    /** 物料编码 */
    @Excel(name = "物料编码")
    private String materialsNo;
    /** 物料名称 */
    @Excel(name = "物料名称")
    private String name;
    /** 数量 */
    @Excel(name = "数量")
    private Long num;
    /** 是否外采：1、否；2、外采；3、协作 */
    @Excel(name = "是否外采：1、否；2、外采；3、协作")
    private Integer isExternal;
    @Excel(name = "单价")
    private BigDecimal price;
    /** 材料规格 */
    @Excel(name = "材料规格")
    private String materialSpec;
    /** 单件重量 */
    @Excel(name = "单件重量")
    private BigDecimal perWight;
    /** 净重(数量*单件重量) */
    @Excel(name = "净重(数量*单件重量)")
    private BigDecimal netWight;
    /** 裸价(元) */
    @Excel(name = "裸价(元)")
    private BigDecimal nakedPrice;
    /** 利润 */
    @Excel(name = "利润")
    private BigDecimal profit;
    /** 包装运输 */
    @Excel(name = "包装运输")
    private BigDecimal transCost;
    /** 产品合计报价 */
    @Excel(name = "产品合计报价")
    private BigDecimal totalPrice;
    /** 未税 */
    @Excel(name = "未税")
    private BigDecimal noTax;
    /** 公斤价 */
    @Excel(name = "公斤价")
    private BigDecimal perPrice;
    
    private String customerName;
    
    private List<TRawMaterialCost> mcList;
    
    private List<TNumberCutCost> ccList;
    
    private List<TProcessCost> pcList;
    
    private BigDecimal firstPrice;
	private String firstBeginTime;
	private String firstEndTime;
	private BigDecimal secondPrice;
	private String secondBeginTime;
	private String secondEndTime;

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
    public void setMaterialsNo(String materialsNo) 
    {
        this.materialsNo = materialsNo;
    }

    public String getMaterialsNo() 
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
    public void setNum(Long num) 
    {
        this.num = num;
    }

    public Long getNum() 
    {
        return num;
    }
    public void setIsExternal(Integer isExternal) 
    {
        this.isExternal = isExternal;
    }

    public Integer getIsExternal() 
    {
        return isExternal;
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
    public void setNakedPrice(BigDecimal nakedPrice) 
    {
        this.nakedPrice = nakedPrice;
    }

    public BigDecimal getNakedPrice() 
    {
        return nakedPrice;
    }
    public void setProfit(BigDecimal profit) 
    {
        this.profit = profit;
    }

    public BigDecimal getProfit() 
    {
        return profit;
    }
    public void setTransCost(BigDecimal transCost) 
    {
        this.transCost = transCost;
    }

    public BigDecimal getTransCost() 
    {
        return transCost;
    }
    public void setTotalPrice(BigDecimal totalPrice) 
    {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getTotalPrice() 
    {
        return totalPrice;
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

    public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public List<TRawMaterialCost> getMcList() {
		return mcList;
	}

	public void setMcList(List<TRawMaterialCost> mcList) {
		this.mcList = mcList;
	}

	public List<TNumberCutCost> getCcList() {
		return ccList;
	}

	public void setCcList(List<TNumberCutCost> ccList) {
		this.ccList = ccList;
	}

	public List<TProcessCost> getPcList() {
		return pcList;
	}

	public void setPcList(List<TProcessCost> pcList) {
		this.pcList = pcList;
	}

	public Long getMaterialsId() {
		return materialsId;
	}

	public void setMaterialsId(Long materialsId) {
		this.materialsId = materialsId;
	}

	public Long getParentMaterialsId() {
		return parentMaterialsId;
	}

	public void setParentMaterialsId(Long parentMaterialsId) {
		this.parentMaterialsId = parentMaterialsId;
	}

	public BigDecimal getFirstPrice() {
		return firstPrice;
	}

	public void setFirstPrice(BigDecimal firstPrice) {
		this.firstPrice = firstPrice;
	}

	public String getFirstBeginTime() {
		return firstBeginTime;
	}

	public void setFirstBeginTime(String firstBeginTime) {
		this.firstBeginTime = firstBeginTime;
	}

	public String getFirstEndTime() {
		return firstEndTime;
	}

	public void setFirstEndTime(String firstEndTime) {
		this.firstEndTime = firstEndTime;
	}

	public BigDecimal getSecondPrice() {
		return secondPrice;
	}

	public void setSecondPrice(BigDecimal secondPrice) {
		this.secondPrice = secondPrice;
	}

	public String getSecondBeginTime() {
		return secondBeginTime;
	}

	public void setSecondBeginTime(String secondBeginTime) {
		this.secondBeginTime = secondBeginTime;
	}

	public String getSecondEndTime() {
		return secondEndTime;
	}

	public void setSecondEndTime(String secondEndTime) {
		this.secondEndTime = secondEndTime;
	}

	@Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("quoteNo", getQuoteNo())
            .append("customerId", getCustomerId())
            .append("materialsNo", getMaterialsNo())
            .append("name", getName())
            .append("num", getNum())
            .append("isExternal", getIsExternal())
            .append("materialSpec", getMaterialSpec())
            .append("perWight", getPerWight())
            .append("netWight", getNetWight())
            .append("nakedPrice", getNakedPrice())
            .append("profit", getProfit())
            .append("transCost", getTransCost())
            .append("totalPrice", getTotalPrice())
            .append("noTax", getNoTax())
            .append("perPrice", getPerPrice())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .toString();
    }
}
