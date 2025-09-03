package com.ruoyi.xkt.dto.storeCusProdDiscount;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data

public class StoreCusProdDiscExistDTO {

    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "优惠列表")
    List<DiscountItemDTO> discountList;

    @Data
    public static class DiscountItemDTO {
        @ApiModelProperty(value = "档口商品ID")
        private Long storeProdId;
        @ApiModelProperty(value = "商品货号")
        private String prodArtNum;
        @ApiModelProperty(value = "商品颜色名称")
        private String colorName;
        @ApiModelProperty(value = "档口商品颜色ID")
        private Long storeProdColorId;
        @ApiModelProperty(value = "优惠金额")
        private Integer discount;
        @ApiModelProperty(value = "档口客户ID")
        private Long storeCusId;
        @ApiModelProperty(value = "档口客户名称")
        private String storeCusName;
    }


}
