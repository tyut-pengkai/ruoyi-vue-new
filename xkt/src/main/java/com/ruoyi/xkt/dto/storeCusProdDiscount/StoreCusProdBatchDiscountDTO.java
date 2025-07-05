package com.ruoyi.xkt.dto.storeCusProdDiscount;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreCusProdBatchDiscountDTO {

    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "业务类型：true:新增客户定价   false:批量减价、批量优惠")
    private Boolean isInsert;
    @ApiModelProperty(value = "优惠列表")
    private List<DiscountItemDTO> discountList;

    @Data
    @Valid
    public static class DiscountItemDTO {
        @ApiModelProperty(value = "档口商品ID")
        private Long storeProdId;
        @ApiModelProperty(value = "档口商品颜色ID")
        private Long storeProdColorId;
        @ApiModelProperty(value = "优惠金额")
        private Integer discount;
        @ApiModelProperty(value = "档口ID")
        private Long storeCusId;
        @ApiModelProperty(value = "档口客户名称")
        private String storeCusName;
    }


}
