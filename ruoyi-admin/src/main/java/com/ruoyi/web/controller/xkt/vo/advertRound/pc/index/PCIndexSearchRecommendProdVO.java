package com.ruoyi.web.controller.xkt.vo.advertRound.pc.index;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("PC 首页 搜索框中推荐档口")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PCIndexSearchRecommendProdVO {

    @ApiModelProperty(value = "2 商品")
    private Integer displayType;
    @ApiModelProperty(value = "档口商品ID")
    private Long storeProdId;
    @ApiModelProperty(value = "商品货号")
    private String prodArtNum;
    @ApiModelProperty(value = "排序")
    private Integer orderNum;
    @ApiModelProperty(value = "商品第一张主图路径")
    private String mainPicUrl;

}
