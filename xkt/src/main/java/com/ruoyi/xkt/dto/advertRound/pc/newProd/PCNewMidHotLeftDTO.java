package com.ruoyi.xkt.dto.advertRound.pc.newProd;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("PC 新品馆 热卖榜左侧大图")
@Data
@Accessors(chain = true)

public class PCNewMidHotLeftDTO {

    @ApiModelProperty(value = "1推广图")
    private Integer displayType;
    @ApiModelProperty(value = "排序")
    private Integer orderNum;
    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "推广图路径")
    private String fileUrl;
    @ApiModelProperty(value = "档口出的推广价格")
    private BigDecimal payPrice;

}
