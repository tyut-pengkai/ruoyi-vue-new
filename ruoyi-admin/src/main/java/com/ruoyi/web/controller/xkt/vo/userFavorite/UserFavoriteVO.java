package com.ruoyi.web.controller.xkt.vo.userFavorite;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.Valid;
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
public class UserFavoriteVO {

    @ApiModelProperty("用户新增收藏列表")
    private List<UFBatchVO> batchList;

    @Data
    @Accessors(chain = true)
    @Valid
    public static class UFBatchVO {
        @NotNull(message = "档口ID不能为空!")
        @ApiModelProperty(value = "档口ID")
        private Long storeId;
        @NotNull(message = "档口商品ID不能为空!")
        @ApiModelProperty(value = "档口商品ID")
        private Long storeProdId;
    }

}
