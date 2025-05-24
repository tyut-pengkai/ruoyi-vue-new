package com.ruoyi.xkt.dto.storeProduct;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口商品的价格及商品主图")
@Data
public class StoreProdPriceAndMainPicAndTagDTO {

    @ApiModelProperty(value = "父级分类ID")
    private Long parCateId;
    @ApiModelProperty(value = "商品分类ID")
    private Long prodCateId;
    @ApiModelProperty(value = "档口商品ID")
    private Long storeProdId;
    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "档口名称")
    private String storeName;
    @ApiModelProperty(value = "商品标签字符串")
    private String tagStr;
    @ApiModelProperty(value = "商品标签")
    private List<String> tags;
    @ApiModelProperty(value = "商品货号")
    private String prodArtNum;
    @ApiModelProperty(value = "档口商品定价")
    private BigDecimal minPrice;
    @ApiModelProperty(value = "档口商品主图")
    private String mainPicUrl;
    @ApiModelProperty(value = "商品标题")
    private String prodTitle;

}
