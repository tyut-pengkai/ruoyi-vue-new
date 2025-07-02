package com.ruoyi.xkt.dto.userFavorite;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserFavoriteDTO {

    @ApiModelProperty("用户新增收藏列表")
    private List<UFBatchVO> batchList;

    @Data
    @Accessors(chain = true)
    public static class UFBatchVO {
        @ApiModelProperty(value = "档口ID")
        private Long storeId;
        @ApiModelProperty(value = "档口商品ID")
        private Long storeProdId;
    }

}
