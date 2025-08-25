package com.ruoyi.xkt.dto.advertRound.pc.store;

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
@ApiModel("PC 档口馆 顶部横幅")
@Data

@Accessors(chain = true)
public class PCStoreTopBannerDTO {

    @ApiModelProperty(value = "1 推广图")
    private Integer displayType;
    @ApiModelProperty(value = "排序")
    private Integer orderNum;
    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "推广图路径")
    private String fileUrl;
    @ApiModelProperty(value = "档口出的推广价格")
    private BigDecimal payPrice;
    @ApiModelProperty(value = "商品列表")
    List<PCSTBProdDTO> prodList;

    @Data
    @ApiModel(value = "左侧列表")
    public static class PCSTBProdDTO {
        @ApiModelProperty(value = "2 商品")
        private Integer displayType;
        @ApiModelProperty(value = "档口商品ID")
        private Long storeProdId;
        @ApiModelProperty(value = "商品货号")
        private String prodArtNum;
        @ApiModelProperty(value = "售价")
        private BigDecimal price;
        @ApiModelProperty(value = "排序")
        private Integer orderNum;
        @ApiModelProperty(value = "商品第一张主图路径")
        private String mainPicUrl;
    }

}
