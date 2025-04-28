package com.ruoyi.web.controller.xkt.vo.account;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author liangyq
 * @date 2025-04-28 19:01
 */
@ApiModel
@Data
public class TransDetailUserPageItemVO {
    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    private String orderNo;
    /**
     * 交易时间
     */
    @ApiModelProperty(value = "交易时间")
    private Date transTime;
    /**
     * 收入
     */
    @ApiModelProperty(value = "收入")
    private BigDecimal inputAmount;
    /**
     * 支出
     */
    @ApiModelProperty(value = "支出")
    private BigDecimal outputAmount;
    /**
     * 交易说明
     */
    @ApiModelProperty(value = "交易说明")
    private String remark;
    /**
     * 交易类型
     */
    @ApiModelProperty(value = "交易类型")
    private String transType;
    /**
     * 支付方式
     */
    @ApiModelProperty(value = "支付方式")
    private String payType;
    /**
     * 交易对象
     */
    @ApiModelProperty(value = "交易对象")
    private String transTarget;

}
