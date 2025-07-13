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
@Data
@ApiModel
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShopCartEditVO {

    @ApiModelProperty(value = "进货车ID, 新增不传，编辑必传", required = true)
    @NotNull(message = "进货车ID不能为空!")
    private Long shoppingCartId;
    @NotNull(message = "档口ID不能为空!")
    @ApiModelProperty(value = "档口ID", required = true)
    private Long storeId;
    @NotNull(message = "档口商品ID不能为空!")
    @ApiModelProperty(value = "档口商品ID", required = true)
    private Long storeProdId;
    @NotBlank(message = "商品货号不能为空!")
    @ApiModelProperty(value = "商品货号", required = true)
    private String prodArtNum;
    @Valid
    @NotNull(message = "进货车明细列表不能为空!")
    @ApiModelProperty(value = "进货车明细列表", required = true)
    List<SCDetailVO> detailList;

    @Data
    @ApiModel
    public static class SCDetailVO {
        @NotNull(message = "档口商品颜色ID不能为空!")
        @ApiModelProperty(value = "档口商品颜色ID", required = true)
        private Long storeProdColorId;
        @NotNull(message = "尺码不能为空!")
        @ApiModelProperty(value = "尺码", required = true)
        private Integer size;
        @NotNull(message = "档口商品颜色ID不能为空!")
        @ApiModelProperty(value = "档口商品颜色ID", required = true)
        private Long storeColorId;
        @NotBlank(message = "颜色名称不能为空!")
        @ApiModelProperty(value = "颜色名称", required = true)
        private String colorName;
        @NotNull(message = "商品数量不能为空!")
        @ApiModelProperty(value = "商品数量", required = true)
        private Integer quantity;
    }


}
