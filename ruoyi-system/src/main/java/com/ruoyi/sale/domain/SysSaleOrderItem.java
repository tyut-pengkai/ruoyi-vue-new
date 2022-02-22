package com.ruoyi.sale.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 销售订单详情对象 sys_sale_order_item
 *
 * @author zwgu
 * @date 2022-02-21
 */
public class SysSaleOrderItem extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long itemId;

    /**
     * 订单ID
     */
    @Excel(name = "订单ID")
    private Long orderId;

    /**
     * 1卡类2登录码类
     */
    @Excel(name = "1卡类2登录码类")
    private String templateType;

    /**
     * 模板ID
     */
    @Excel(name = "模板ID")
    private Long templateId;

    /**
     * 购买数量
     */
    @Excel(name = "购买数量")
    private Long num;

    /**
     * 商品标题
     */
    @Excel(name = "商品标题")
    private String title;

    /**
     * 商品单价
     */
    @Excel(name = "商品单价")
    private BigDecimal price;

    /**
     * 总价格（折扣前）
     */
    @Excel(name = "总价格", readConverterExp = "折=扣前")
    private String totalFee;

    /**
     * 折扣规则
     */
    @Excel(name = "折扣规则")
    private String discountRule;

    /**
     * 折扣金额
     */
    @Excel(name = "折扣金额")
    private String discountFee;

    /**
     * 应付金额
     */
    @Excel(name = "应付金额")
    private String actualFee;

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public String getDiscountRule() {
        return discountRule;
    }

    public void setDiscountRule(String discountRule) {
        this.discountRule = discountRule;
    }

    public String getDiscountFee() {
        return discountFee;
    }

    public void setDiscountFee(String discountFee) {
        this.discountFee = discountFee;
    }

    public String getActualFee() {
        return actualFee;
    }

    public void setActualFee(String actualFee) {
        this.actualFee = actualFee;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("itemId", getItemId())
                .append("orderId", getOrderId())
                .append("templateType", getTemplateType())
                .append("templateId", getTemplateId())
                .append("num", getNum())
                .append("title", getTitle())
                .append("price", getPrice())
                .append("totalFee", getTotalFee())
                .append("discountRule", getDiscountRule())
                .append("discountFee", getDiscountFee())
                .append("actualFee", getActualFee())
                .toString();
    }
}
