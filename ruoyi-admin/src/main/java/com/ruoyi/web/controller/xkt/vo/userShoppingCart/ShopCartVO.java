package com.ruoyi.web.controller.xkt.vo.userShoppingCart;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@ApiModel("电商卖家新增进货车")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShopCartVO {

    @NotNull(message = "档口ID不能为空!")
    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @NotNull(message = "档口商品ID不能为空!")
    @ApiModelProperty(value = "档口商品ID")
    private Long storeProdId;
    @NotBlank(message = "商品货号不能为空!")
    @ApiModelProperty(value = "商品货号")
    private String prodArtNum;
    @Valid
    @NotNull(message = "进货车明细列表不能为空!")
    @ApiModelProperty(value = "进货车明细列表")
    List<SCDetailVO> detailList;

    @Data
    public static class SCDetailVO {
        @NotNull(message = "档口商品颜色ID不能为空!")
        @ApiModelProperty(value = "档口商品颜色ID")
        private Long storeProdColorId;
        @NotNull(message = "尺码不能为空!")
        @ApiModelProperty(value = "尺码")
        private Integer size;
        @NotNull(message = "档口商品颜色ID不能为空!")
        @ApiModelProperty(value = "档口商品颜色ID")
        private Long storeColorId;
        @NotBlank(message = "颜色名称不能为空!")
        @ApiModelProperty(value = "颜色名称")
        private String colorName;
        @NotNull(message = "商品数量不能为空!")
        @ApiModelProperty(value = "商品数量")
        private Integer quantity;
    }


}
