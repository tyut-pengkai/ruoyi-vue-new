package com.ruoyi.xkt.dto.userFavorite;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserFavBatchAddToShopCartDTO {

    @ApiModelProperty(value = "批量操作列表")
    List<BatchDTO> batchList;

    @Data
    public static class BatchDTO {
        @ApiModelProperty(value = "档口ID")
        private Long storeId;
        @ApiModelProperty(value = "档口商品ID")
        private Long storeProdId;
        @ApiModelProperty(value = "档口名称")
        private String storeName;
        @ApiModelProperty(value = "商品货号")
        private String prodArtNum;

    }

}
