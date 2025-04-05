package com.ruoyi.web.controller.xkt.vo.storeProdStock;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口商品库存详情数据")
@Data
@Accessors(chain = true)
public class StoreProdStockResVO {

    @ApiModelProperty(name = "档口商品库存ID")
    private Long storeProdStockId;
    @ApiModelProperty(name = "档口商品ID")
    private Long storeProdId;
    @ApiModelProperty(name = "档口商品主图url")
    private String mainPicUrl;
    @ApiModelProperty(name = "商品货号")
    private String prodArtNum;
    @ApiModelProperty(name = "颜色名称")
    private String colorName;
    @ApiModelProperty(name = "分类类目")
    private String prodCateName;
    @ApiModelProperty(name = "尺码30")
    private Integer size30;
    @ApiModelProperty(name = "尺码31")
    private Integer size31;
    @ApiModelProperty(name = "尺码32")
    private Integer size32;
    @ApiModelProperty(name = "尺码33")
    private Integer size33;
    @ApiModelProperty(name = "尺码34")
    private Integer size34;
    @ApiModelProperty(name = "尺码35")
    private Integer size35;
    @ApiModelProperty(name = "尺码36")
    private Integer size36;
    @ApiModelProperty(name = "尺码37")
    private Integer size37;
    @ApiModelProperty(name = "尺码38")
    private Integer size38;
    @ApiModelProperty(name = "尺码39")
    private Integer size39;
    @ApiModelProperty(name = "尺码40")
    private Integer size40;
    @ApiModelProperty(name = "尺码41")
    private Integer size41;
    @ApiModelProperty(name = "尺码42")
    private Integer size42;
    @ApiModelProperty(name = "尺码43")
    private Integer size43;
    @ApiModelProperty(name = "总数量")
    private Integer totalStock;

}
