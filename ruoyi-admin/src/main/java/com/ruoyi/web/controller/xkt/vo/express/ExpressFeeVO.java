package com.ruoyi.web.controller.xkt.vo.express;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 物流信息
 *
 * @author liangyq
 * @date 2025-04-01 11:57:52.434
 **/
@ApiModel
@Data
public class ExpressFeeVO {
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

    @ApiModelProperty(value = "快递费")
    private BigDecimal expressFee;
}
