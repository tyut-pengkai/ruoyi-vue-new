package com.ruoyi.xkt.dto.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel
@Data
@Accessors(chain = true)
public class StoreAppViewRankResDTO {

    @ApiModelProperty(value = "档口浏览量列表")
    private List<SAVRViewCountDTO> viewCountList;

    @Data
    public static class SAVRViewCountDTO {
        @ApiModelProperty(value = "档口ID")
        private Long storeId;
        @ApiModelProperty(value = "档口名称")
        private String storeName;
        @ApiModelProperty(value = "会员等级")
        private Integer memberLevel;
        @ApiModelProperty(value = "logo")
        private String logo;
        @ApiModelProperty(value = "在售商品数量")
        private Integer prodCount;
        @ApiModelProperty(value = "商品浏览量")
        private Long viewCount;
    }

}
