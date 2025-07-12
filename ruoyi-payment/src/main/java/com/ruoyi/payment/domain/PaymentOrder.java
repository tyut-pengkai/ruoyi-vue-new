package com.ruoyi.payment.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 支付订单对象 payment_order
 * 
 * @author auto
 * @date 2025-06-24
 */
public class PaymentOrder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 订单ID */
    private Long orderId;

    /** 订单号 */
    @Excel(name = "订单号")
    private String orderNo;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 用户名 */
    @Excel(name = "用户名")
    private String userName;

    /** 套餐ID */
    @Excel(name = "套餐ID")
    private Long packageId;

    /** 套餐名称 */
    @Excel(name = "套餐名称")
    private String packageName;

    /** 套餐时长（小时） */
    @Excel(name = "套餐时长", readConverterExp = "小=时")
    private Integer packageHours;

    /** 套餐赠送时长 */
    @Excel(name = "套餐赠送时长")
    private Integer packageFreeHours;

    /** 支付金额 */
    @Excel(name = "支付金额")
    private BigDecimal amount;

    /** 货币类型 */
    @Excel(name = "货币类型")
    private String currency;

    /** 订单状态（0=待支付, 1=已完成, 2=已取消, 3=支付失败） */
    @Excel(name = "订单状态", readConverterExp = "0=待支付,1=已完成,2=已取消,3=支付失败")
    private String status;

    /** 支付渠道（paypal/wechat/alipay等） */
    @Excel(name = "支付渠道")
    private String paymentChannel;

    /** 支付平台的支付/交易ID */
    @Excel(name = "支付/交易ID")
    private String paymentId;

    /** 支付令牌 */
    @Excel(name = "支付令牌")
    private String paymentToken;
    
    /** 支付平台用户标识 */
    @Excel(name = "支付平台用户标识")
    private String payerId;
    
    /** 支付方式 */
    @Excel(name = "支付方式")
    private String paymentMethod;
    
    /** 描述 */
    @Excel(name = "描述")
    private String description;

    /** 支付成功时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "支付成功时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date payTime;

    /** 设备id */
    @Excel(name = "设备id")
    private Long deviceId;

    /** 设备名称 */
    @Excel(name = "设备名称")
    private String deviceName;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 备注 */
    @Excel(name = "备注")
    private String remark;

    public void setOrderId(Long orderId) 
    {
        this.orderId = orderId;
    }

    public Long getOrderId() 
    {
        return orderId;
    }

    public void setOrderNo(String orderNo) 
    {
        this.orderNo = orderNo;
    }

    public String getOrderNo() 
    {
        return orderNo;
    }

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    public void setUserName(String userName) 
    {
        this.userName = userName;
    }

    public String getUserName() 
    {
        return userName;
    }

    public void setPackageId(Long packageId) 
    {
        this.packageId = packageId;
    }

    public Long getPackageId() 
    {
        return packageId;
    }

    public void setPackageName(String packageName) 
    {
        this.packageName = packageName;
    }

    public String getPackageName() 
    {
        return packageName;
    }

    public void setPackageHours(Integer packageHours) 
    {
        this.packageHours = packageHours;
    }

    public Integer getPackageHours() 
    {
        return packageHours;
    }

    public void setPackageFreeHours(Integer packageFreeHours)
    {
        this.packageFreeHours = packageFreeHours;
    }

    public Integer getPackageFreeHours()
    {
        return packageFreeHours;
    }

    public void setAmount(BigDecimal amount) 
    {
        this.amount = amount;
    }

    public BigDecimal getAmount() 
    {
        return amount;
    }

    public String getCurrency()
    {
        return currency;
    }

    public void setCurrency(String currency)
    {
        this.currency = currency;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public void setPaymentChannel(String paymentChannel) 
    {
        this.paymentChannel = paymentChannel;
    }

    public String getPaymentChannel() 
    {
        return paymentChannel;
    }

    public void setPaymentId(String paymentId) 
    {
        this.paymentId = paymentId;
    }

    public String getPaymentId() 
    {
        return paymentId;
    }

    public void setPaymentToken(String paymentToken) 
    {
        this.paymentToken = paymentToken;
    }

    public String getPaymentToken() 
    {
        return paymentToken;
    }

    public void setPayerId(String payerId) 
    {
        this.payerId = payerId;
    }

    public String getPayerId() 
    {
        return payerId;
    }

    public void setPaymentMethod(String paymentMethod) 
    {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentMethod() 
    {
        return paymentMethod;
    }

    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }

    public void setPayTime(Date payTime) 
    {
        this.payTime = payTime;
    }

    public Date getPayTime() 
    {
        return payTime;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    public void setRemark(String remark) 
    {
        this.remark = remark;
    }

    public String getRemark() 
    {
        return remark;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("orderId", getOrderId())
            .append("orderNo", getOrderNo())
            .append("userId", getUserId())
            .append("userName", getUserName())
            .append("packageId", getPackageId())
            .append("packageName", getPackageName())
            .append("packageHours", getPackageHours())
            .append("packageFreeHours", getPackageFreeHours())
            .append("amount", getAmount())
            .append("currency", getCurrency())
            .append("status", getStatus())
            .append("paymentChannel", getPaymentChannel())
            .append("paymentId", getPaymentId())
            .append("payTime", getPayTime())
            .append("deviceId", getDeviceId())
            .append("deviceName", getDeviceName())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
