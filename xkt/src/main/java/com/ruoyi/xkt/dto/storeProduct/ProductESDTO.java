package com.ruoyi.xkt.dto.storeProduct;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@Accessors(chain = true)
public class ProductESDTO {

    @ApiModelProperty(value = "档口商品ID")
    private Long id;
    @ApiModelProperty(value = "货号")
    private String prodArtNum;
    @ApiModelProperty(value = "档口商品分类ID")
    private String prodCateId;
    @ApiModelProperty(value = "档口商品分类名称")
    private String prodCateName;
    @ApiModelProperty(value = "销量权重")
    private String saleWeight;
    @ApiModelProperty(value = "推荐权重")
    private String recommendWeight;
    @ApiModelProperty(value = "人气权重")
    private String popularityWeight;
    @ApiModelProperty(value = "创建时间")
    private String createTime;
    @ApiModelProperty(value = "主图")
    private String mainPic;
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
    @ApiModelProperty(value = "标签")
    private List<String> tags;
    @ApiModelProperty(value = "标题")
    private String prodTitle;

}
