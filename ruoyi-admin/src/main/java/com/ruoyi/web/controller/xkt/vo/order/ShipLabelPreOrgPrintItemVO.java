package com.ruoyi.web.controller.xkt.vo.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 面单预备原单号打印项
 *
 * @author liangyq
 * @date 2025-07-19
 **/
@ApiModel
@Data
public class ShipLabelPreOrgPrintItemVO {
    /**
     * 物流运单号（快递单号）
     */
    @ApiModelProperty(value = "物流运单号（快递单号）")
    private String expressWaybillNo;
    /**
     * 物流ID
     */
    @ApiModelProperty(value = "物流ID")
    private Long expressId;
    /**
     * 物流名称
     */
    @ApiModelProperty(value = "物流名称")
    private String expressName;
    /**
     * 商品概要
     */
    @ApiModelProperty(value = "商品概要")
    private String goodsSummary;
    /**
     * 打印时间
     */
    @ApiModelProperty(value = "打印时间")
    private Date lastPrintTime;
}
