package com.ruoyi.web.controller.xkt.vo.storeProd;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
public class StoreProdViewVO {

    @ApiModelProperty(value = "会员等级")
    private Integer memberLevel;
    @ApiModelProperty(value = "搜索次数")
    private Long imgSearchCount;
    @ApiModelProperty(value = "同款商品数量")
    private Integer sameProdCount;
    @ApiModelProperty(value = "商品ID")
    private Long storeProdId;
    @ApiModelProperty(value = "商品货号")
    private String prodArtNum;
    @ApiModelProperty(value = "商品第一张主图路径")
    private String mainPicUrl;
    @ApiModelProperty(name = "商品标题")
    private String prodTitle;
    @ApiModelProperty(value = "售价")
    private BigDecimal price;
    @ApiModelProperty(name = "商品标签列表")
    private List<String> tags;
    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "档口名称")
    private String storeName;
//    @ApiModelProperty(value = "档口标签列表")
//    private List<String> storeTagList;

}
