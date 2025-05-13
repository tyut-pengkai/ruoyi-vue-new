package com.ruoyi.xkt.dto.advertRound;

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
@ApiModel("获取当前推广位最新的出价及商品信息")
@Data
@Accessors(chain = true)
public class AdRoundLatestResDTO {

    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "档口负责人联系电话")
    private String contactPhone;
    @ApiModelProperty(value = "最高价格")
    private BigDecimal payPrice;
    @ApiModelProperty(value = "档口设置的商品")
    private List<ARLProdDTO> prodList;

    @Data
    @ApiModel(value = "档口设置的商品")
    @Accessors(chain = true)
    public static class ARLProdDTO {
        @ApiModelProperty("档口商品ID")
        private Long storeProdId;
        @ApiModelProperty(value = "商品货号")
        private String prodArtNum;
    }

}
