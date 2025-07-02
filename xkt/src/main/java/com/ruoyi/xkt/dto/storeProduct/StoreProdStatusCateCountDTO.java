package com.ruoyi.xkt.dto.storeProduct;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口商品各个状态下分类数量")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreProdStatusCateCountDTO {

    @ApiModelProperty(value = "商品状态")
    private Integer prodStatus;
    @ApiModelProperty(value = "商品分类ID")
    private Long prodCateId;
    @ApiModelProperty(value = "商品分类名称")
    private String prodCateName;
    @ApiModelProperty(value = "商品分类数量")
    private Integer cateCount;

}
