package com.ruoyi.web.controller.xkt.vo.userFavorite;

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
@ApiModel("电商卖家新增商品收藏")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserFavBatchAddToShopCartVO {

    @Valid
    @NotNull(message = "批量操作列表不能为空!")
    @ApiModelProperty(value = "批量操作列表")
    List<BatchVO> batchList;

    @Data
    public static class BatchVO {
        @NotBlank(message = "商品货号不能为空!")
        @ApiModelProperty(value = "商品货号")
        private String prodArtNum;
        @NotNull(message = "档口ID不能为空!")
        @ApiModelProperty(value = "档口ID")
        private Long storeId;
        @NotNull(message = "档口商品ID不能为空!")
        @ApiModelProperty(value = "档口商品ID")
        private Long storeProdId;
    }

}
