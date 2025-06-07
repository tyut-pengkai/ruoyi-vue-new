package com.ruoyi.xkt.dto.storeProduct;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("推广营销查询最近20天上新产品")
@Data
@Accessors(chain = true)
public class StoreProdFuzzyLatest20ResDTO {

    @ApiModelProperty(value = "档口商品ID")
    private Long storeProdId;
    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "商品货号")
    private String prodArtNum;

}
