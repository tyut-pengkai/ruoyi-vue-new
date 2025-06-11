package com.ruoyi.web.controller.xkt.vo.express;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 物流费用配置
 *
 * @author liangyq
 * @date 2025-04-02 15:00
 */
@ApiModel
@Data
public class ExpressFeeConfigEditVO {
    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    private Long id;
    /**
     * 物流ID
     */
    @ApiModelProperty(value = "物流ID")
    private Long expressId;
    /**
     * 地区编码，基于行政区划代码做扩展，唯一约束
     */
    @ApiModelProperty(value = "地区编码")
    private String regionCode;
    /**
     * 上级地区编码，没有上级的默认空
     */
    @ApiModelProperty(value = "上级地区编码")
    private String parentRegionCode;
    /**
     * 首件运费
     */
    @ApiModelProperty(value = "首件运费")
    private BigDecimal firstItemAmount;
    /**
     * 续费
     */
    @ApiModelProperty(value = "续费")
    private BigDecimal nextItemAmount;
}
