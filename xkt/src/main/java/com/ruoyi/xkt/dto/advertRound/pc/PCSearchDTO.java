package com.ruoyi.xkt.dto.advertRound.pc;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("PC 搜索 ")
@Data
@Accessors(chain = true)
public class PCSearchDTO {

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
