package com.ruoyi.web.controller.xkt.vo.storeCusProdDiscount;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口客户批量减价、批量抹零")
@Data

public class StoreCusProdBatchDiscountVO {

    @NotNull(message = "档口ID不能为空!")
    @ApiModelProperty(value = "档口ID", required = true)
    private Long storeId;
    @NotNull(message = "业务类型不能为空!")
    @ApiModelProperty(value = "业务类型：true:新增客户定价   false:批量减价、批量优惠", required = true)
    private Boolean isInsert;
    @NotNull(message = "优惠列表不能为空!")
    @Valid
    @ApiModelProperty(value = "优惠列表", required = true)
    List<DiscountItemVO> discountList;

    @Data
    public static class DiscountItemVO {
        @NotNull(message = "档口商品ID不能为空!")
        @ApiModelProperty(value = "档口商品ID", required = true)
        private Long storeProdId;
        @NotNull(message = "档口商品颜色ID不能为空!")
        @ApiModelProperty(value = "档口商品颜色ID", required = true)
        private Long storeProdColorId;
        @NotNull(message = "优惠金额不能为空!")
        @ApiModelProperty(value = "优惠金额", required = true)
        private Integer discount;
        @NotNull(message = "档口客户ID不能为空!")
        @ApiModelProperty(value = "档口ID", required = true)
        private Long storeCusId;
        @ApiModelProperty(value = "档口客户名称", required = true)
        @NotBlank(message = "档口客户名称不能为空!")
        private String storeCusName;
    }


}
