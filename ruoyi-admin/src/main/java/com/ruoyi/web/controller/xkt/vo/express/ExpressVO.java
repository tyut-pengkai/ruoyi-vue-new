package com.ruoyi.web.controller.xkt.vo.express;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 物流信息
 *
 * @author liangyq
 * @date 2025-04-01 11:57:52.434
 **/
@ApiModel
@Data
public class ExpressVO {
    /**
     * 物流ID
     */
    @ApiModelProperty(value = "物流ID")
    private Long id;
    /**
     * 物流编码
     */
    @ApiModelProperty(value = "物流编码")
    private String expressCode;
    /**
     * 物流名称
     */
    @ApiModelProperty(value = "物流名称")
    private String expressName;
    /**
     * 系统发货可选
     */
    @ApiModelProperty(value = "系统发货可选")
    private Boolean systemDeliverAccess;
    /**
     * 档口发货可选
     */
    @ApiModelProperty(value = "档口发货可选")
    private Boolean storeDeliverAccess;
    /**
     * 用户退货可选
     */
    @ApiModelProperty(value = "用户退货可选")
    private Boolean userRefundAccess;
}
