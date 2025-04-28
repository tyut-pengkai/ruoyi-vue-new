package com.ruoyi.web.controller.xkt.vo.account;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 内部账户
 *
 * @author liangyq
 * @date 2025-04-01 11:57:52.493
 **/
@ApiModel
@Data
public class InternalAccountVO {
    /**
     * 内部账户ID
     */
    @ApiModelProperty(value = "内部账户ID")
    private Long id;
    /**
     * 归属[1:平台 2:档口 3:用户]
     */
    @ApiModelProperty(value = "归属[1:平台 2:档口 3:用户]")
    private Integer ownerType;
    /**
     * 归属ID（平台=-1，档口=store_id）
     */
    @ApiModelProperty(value = "归属ID（平台=-1，档口=store_id）")
    private Long ownerId;
    /**
     * 账户状态[1:正常 2:冻结]
     */
    @ApiModelProperty(value = "账户状态[1:正常 2:冻结]")
    private Integer accountStatus;
    /**
     * 电话号码
     */
    @ApiModelProperty(value = "电话号码")
    private String phoneNumber;
    /**
     * 余额
     */
    @ApiModelProperty(value = "余额")
    private BigDecimal balance;
    /**
     * 可用余额
     */
    @ApiModelProperty(value = "可用余额")
    private BigDecimal usableBalance;
    /**
     * 支付中金额
     */
    @ApiModelProperty(value = "支付中金额")
    private BigDecimal paymentAmount;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
}
