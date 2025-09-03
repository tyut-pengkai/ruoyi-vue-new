package com.ruoyi.web.controller.xkt.vo.storeHomepage;

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
@ApiModel("档口推荐商品列表")
@Data

public class StoreRecommendResVO {

    @ApiModelProperty(value = "档口ID")
    private String storeId;
    @ApiModelProperty(value = "档口名称")
    private String storeName;
    @ApiModelProperty(value = "档口商品ID")
    private String storeProdId;
    @ApiModelProperty(value = "货号")
    private String prodArtNum;
    @ApiModelProperty(value = "档口商品定价")
    private BigDecimal minPrice;
    @ApiModelProperty(value = "档口商品主图")
    private String mainPicUrl;
    @ApiModelProperty(value = "标题")
    private String prodTitle;
    @ApiModelProperty(value = "是否广告")
    private Boolean advert;
    @ApiModelProperty(value = "是否有视频")
    private Boolean hasVideo;
    @ApiModelProperty(value = "标签")
    private List<String> tags;

}
