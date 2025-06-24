package com.ruoyi.payment.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 商品套餐对象 payment_package
 * 
 * @author auto
 * @date 2025-06-24
 */
public class PaymentPackage extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 套餐ID */
    private Long packageId;

    /** 套餐名称 */
    @Excel(name = "套餐名称")
    private String name;

    /** 套餐时长（小时） */
    @Excel(name = "套餐时长")
    private Long hours;

    /** 价格 */
    @Excel(name = "价格")
    private BigDecimal price;

    /** 币种(USD=美金, CNY=人民币) */
    @Excel(name = "币种(USD=美金, CNY=人民币)")
    private String currency;

    /** 折扣标签 */
    @Excel(name = "折扣标签")
    private String discountLabel;

    /** 状态（0启用 1停用） */
    @Excel(name = "状态", readConverterExp = "0=启用,1=停用")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setPackageId(Long packageId) 
    {
        this.packageId = packageId;
    }

    public Long getPackageId() 
    {
        return packageId;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }

    public void setHours(Long hours) 
    {
        this.hours = hours;
    }

    public Long getHours() 
    {
        return hours;
    }

    public void setPrice(BigDecimal price) 
    {
        this.price = price;
    }

    public BigDecimal getPrice() 
    {
        return price;
    }

    public void setCurrency(String currency) 
    {
        this.currency = currency;
    }

    public String getCurrency() 
    {
        return currency;
    }

    public void setDiscountLabel(String discountLabel) 
    {
        this.discountLabel = discountLabel;
    }

    public String getDiscountLabel() 
    {
        return discountLabel;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("packageId", getPackageId())
            .append("name", getName())
            .append("hours", getHours())
            .append("price", getPrice())
            .append("currency", getCurrency())
            .append("discountLabel", getDiscountLabel())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
