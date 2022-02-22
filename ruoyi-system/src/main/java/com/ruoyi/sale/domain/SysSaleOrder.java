package com.ruoyi.sale.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 销售订单对象 sys_sale_order
 *
 * @author zwgu
 * @date 2022-02-21
 */
public class SysSaleOrder extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单编号
     */
    @Excel(name = "订单编号")
    private String orderNo;

    /**
     * 用户ID
     */
    @Excel(name = "用户ID")
    private Long userId;

    /**
     * 应付金额
     */
    @Excel(name = "应付金额")
    private BigDecimal actualFee;

    /**
     * 总价格（折扣前）
     */
    @Excel(name = "总价格", readConverterExp = "折=扣前")
    private BigDecimal totalFee;

    /**
     * 折扣规则
     */
    @Excel(name = "折扣规则")
    private String discountRule;

    /**
     * 折扣金额
     */
    @Excel(name = "折扣金额")
    private BigDecimal discountFee;

    /**
     * 支付方式
     */
    @Excel(name = "支付方式")
    private String payMode;

    /**
     * 1未付款2已付款3未发货4已发货5交易成功6交易关闭
     */
    @Excel(name = "1未付款2已付款3未发货4已发货5交易成功6交易关闭")
    private String status;

    /**
     * 联系方式
     */
    @Excel(name = "联系方式")
    private String contact;

    /**
     * 查询密码
     */
    @Excel(name = "查询密码")
    private String queryPass;

    /**
     * 支付时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "支付时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date paymentTime;

    /**
     * 发货时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "发货时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date deliveryTime;

    /**
     * 订单完成时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "订单完成时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date finishTime;

    /**
     * 订单关闭时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "订单关闭时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date closeTime;

    /**
     * 销售订单详情信息
     */
    private List<SysSaleOrderItem> sysSaleOrderItemList;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getActualFee() {
        return actualFee;
    }

    public void setActualFee(BigDecimal actualFee) {
        this.actualFee = actualFee;
    }

    public BigDecimal getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(BigDecimal totalFee) {
        this.totalFee = totalFee;
    }

    public String getDiscountRule() {
        return discountRule;
    }

    public void setDiscountRule(String discountRule) {
        this.discountRule = discountRule;
    }

    public BigDecimal getDiscountFee() {
        return discountFee;
    }

    public void setDiscountFee(BigDecimal discountFee) {
        this.discountFee = discountFee;
    }

    public String getPayMode() {
        return payMode;
    }

    public void setPayMode(String payMode) {
        this.payMode = payMode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getQueryPass() {
        return queryPass;
    }

    public void setQueryPass(String queryPass) {
        this.queryPass = queryPass;
    }

    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }

    public Date getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public Date getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Date closeTime) {
        this.closeTime = closeTime;
    }

    public List<SysSaleOrderItem> getSysSaleOrderItemList() {
        return sysSaleOrderItemList;
    }

    public void setSysSaleOrderItemList(List<SysSaleOrderItem> sysSaleOrderItemList) {
        this.sysSaleOrderItemList = sysSaleOrderItemList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("orderId", getOrderId())
                .append("orderNo", getOrderNo())
                .append("userId", getUserId())
                .append("actualFee", getActualFee())
                .append("totalFee", getTotalFee())
                .append("discountRule", getDiscountRule())
                .append("discountFee", getDiscountFee())
                .append("payMode", getPayMode())
                .append("status", getStatus())
                .append("contact", getContact())
                .append("queryPass", getQueryPass())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("paymentTime", getPaymentTime())
                .append("deliveryTime", getDeliveryTime())
                .append("finishTime", getFinishTime())
                .append("closeTime", getCloseTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .append("sysSaleOrderItemList", getSysSaleOrderItemList())
                .toString();
    }
}
