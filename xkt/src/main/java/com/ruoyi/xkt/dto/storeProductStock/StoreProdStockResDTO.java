package com.ruoyi.xkt.dto.storeProductStock;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@Accessors(chain = true)
public class StoreProdStockResDTO {

    @ApiModelProperty(value = "档口商品库存ID")
    private Long storeProdStockId;
    @ApiModelProperty(value = "档口商品ID")
    private Long storeProdId;
    @ApiModelProperty(value = "档口商品主图url")
    private String mainPicUrl;
    @ApiModelProperty(value = "商品货号")
    private String prodArtNum;
    @ApiModelProperty(value = "档口商品颜色ID")
    private Long storeProdColorId;
    @ApiModelProperty(value = "档口颜色ID")
    private Long storeColorId;
    @ApiModelProperty(value = "颜色名称")
    private String colorName;
    @ApiModelProperty(value = "分类类目")
    private String prodCateName;
    @ApiModelProperty(value = "尺码30")
    private Integer size30;
    @ApiModelProperty(value = "尺码31")
    private Integer size31;
    @ApiModelProperty(value = "尺码32")
    private Integer size32;
    @ApiModelProperty(value = "尺码33")
    private Integer size33;
    @ApiModelProperty(value = "尺码34")
    private Integer size34;
    @ApiModelProperty(value = "尺码35")
    private Integer size35;
    @ApiModelProperty(value = "尺码36")
    private Integer size36;
    @ApiModelProperty(value = "尺码37")
    private Integer size37;
    @ApiModelProperty(value = "尺码38")
    private Integer size38;
    @ApiModelProperty(value = "尺码39")
    private Integer size39;
    @ApiModelProperty(value = "尺码40")
    private Integer size40;
    @ApiModelProperty(value = "尺码41")
    private Integer size41;
    @ApiModelProperty(value = "尺码42")
    private Integer size42;
    @ApiModelProperty(value = "尺码43")
    private Integer size43;
    @ApiModelProperty(value = "总数量")
    private Integer totalStock;

}
