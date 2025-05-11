package com.ruoyi.xkt.dto.userFavorite;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

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
public class UserFavoriteDTO {

    @ApiModelProperty("用户新增收藏列表")
    private List<UFBatchVO> batchList;

    @Data
    @ApiModel(value = "用户新增收藏")
    @Accessors(chain = true)
    public static class UFBatchVO {
        @ApiModelProperty(value = "档口ID")
        private Long storeId;
        @ApiModelProperty(value = "档口商品ID")
        private Long storeProdId;
    }

}
