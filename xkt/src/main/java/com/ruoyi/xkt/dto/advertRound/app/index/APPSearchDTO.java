package com.ruoyi.xkt.dto.advertRound.app.index;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
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
@ApiModel("APP 搜索 列表")
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class APPSearchDTO {

    @ApiModelProperty(value = "会员等级")
    private Integer memberLevel;
    @ApiModelProperty(value = "档口ID")
    private String storeId;
    @ApiModelProperty(value = "档口名称")
    private String storeName;
    @ApiModelProperty(value = "档口商品ID")
    private String storeProdId;
    @ApiModelProperty(value = "货号")
    private String prodArtNum;
    @ApiModelProperty(value = "主图")
    private String mainPicUrl;
    @ApiModelProperty(value = "主图名称")
    private String mainPicName;
    @ApiModelProperty(value = "主图大小")
    private BigDecimal mainPicSize;
    @ApiModelProperty(value = "单价")
    private String prodPrice;
    @ApiModelProperty(value = "是否广告")
    private Boolean advert;
    @ApiModelProperty(value = "标题")
    private String prodTitle;
    @ApiModelProperty(value = "是否有视频")
    private Boolean hasVideo;
    @ApiModelProperty(value = "标签")
    private List<String> tags;

}
