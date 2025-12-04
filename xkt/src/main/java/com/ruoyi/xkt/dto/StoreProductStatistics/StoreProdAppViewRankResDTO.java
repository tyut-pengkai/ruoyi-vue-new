package com.ruoyi.xkt.dto.StoreProductStatistics;

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
@ApiModel
@Data
@Accessors(chain = true)
public class StoreProdAppViewRankResDTO {

    @ApiModelProperty(value = "商品浏览量列表")
    private List<SPAVRViewCountDTO> viewCountList;

    @Data
    @Accessors(chain = true)
    public static class SPAVRViewCountDTO {
        @ApiModelProperty(value = "档口ID")
        private Long storeId;
        @ApiModelProperty(value = "档口名称")
        private String storeName;
        @ApiModelProperty(value = "商品主图")
        private String mainPicUrl;
        @ApiModelProperty(value = "商品ID")
        private Long storeProdId;
        @ApiModelProperty(value = "商品货号")
        private String prodArtNum;
        @ApiModelProperty(value = "商品最低价格")
        private BigDecimal price;
        @ApiModelProperty(value = "商品浏览量")
        private Long viewCount;
    }


}
