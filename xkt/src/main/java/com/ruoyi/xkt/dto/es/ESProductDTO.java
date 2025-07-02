package com.ruoyi.xkt.dto.es;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@Accessors(chain = true)
public class ESProductDTO {

    @ApiModelProperty(value = "档口商品ID")
    private String storeProdId;
    @ApiModelProperty(value = "货号")
    private String prodArtNum;
    @ApiModelProperty(value = "档口商品分类ID")
    private String prodCateId;
    @ApiModelProperty(value = "档口商品分类名称")
    private String prodCateName;
    @ApiModelProperty(value = "销量权重")
    private String saleWeight;
    @ApiModelProperty(value = "档口权重")
    private String storeWeight;
    @ApiModelProperty(value = "推荐权重")
    private String recommendWeight;
    @ApiModelProperty(value = "人气权重")
    private String popularityWeight;
    @ApiModelProperty(value = "创建时间")
    private String createTime;
    @ApiModelProperty(value = "主图")
    private String mainPicUrl;
    @ApiModelProperty(value = "主图名称")
    private String mainPicName;
    @ApiModelProperty(value = "主图大小")
    private BigDecimal mainPicSize;
    @ApiModelProperty(value = "上级分类名称")
    private String parCateName;
    @ApiModelProperty(value = "上级分类ID")
    private String parCateId;
    @ApiModelProperty(value = "单价")
    private String prodPrice;
    @ApiModelProperty(value = "适合季节")
    private String season;
    @ApiModelProperty(value = "商品状态")
    private String prodStatus;
    @ApiModelProperty(value = "档口ID")
    private String storeId;
    @ApiModelProperty(value = "档口名称")
    private String storeName;
    @ApiModelProperty(value = "风格")
    private String style;
    @ApiModelProperty(value = "是否有视频")
    private Boolean hasVideo;
    @ApiModelProperty(value = "标签")
    private List<String> tags;
    @ApiModelProperty(value = "标题")
    private String prodTitle;

}
