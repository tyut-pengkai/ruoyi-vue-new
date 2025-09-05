package com.ruoyi.xkt.dto.advertRound.app.prod;

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

public class APPProdCateTop3DTO {

    @ApiModelProperty(value = "档口商品分类ID")
    private Long prodCateId;
    @ApiModelProperty(value = "档口商品分类名称")
    private String prodCateName;
    @ApiModelProperty(value = "分类商品列表")
    private List<APPPCTProdDTO> prodList;

    @Data
    @Accessors(chain = true)
    public static class APPPCTProdDTO {
        @ApiModelProperty(value = "主图")
        private String mainPicUrl;
        @ApiModelProperty(value = "排序")
        private Integer orderNum;
        @ApiModelProperty(value = "档口ID")
        private Long storeId;
        @ApiModelProperty(value = "档口商品ID")
        private Long storeProdId;
        @ApiModelProperty(value = "商品货号")
        private String prodArtNum;
    }

}
