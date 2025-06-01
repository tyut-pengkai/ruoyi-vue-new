package com.ruoyi.xkt.dto.storeHomepage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口顶部轮播图")
@Data
@Accessors(chain = true)
public class StoreHomeTopBannerResDTO {

    @ApiModelProperty(value = "1.不跳转 为null 2.跳转店铺 为storeId 3.跳转商品 为storeProdId")
    private Long bizId;
    @ApiModelProperty(value = "1.不跳转 2.跳转店铺 3.跳转商品")
    private Integer jumpType;
    @ApiModelProperty(value = "跳转链接")
    private String fileUrl;
    @ApiModelProperty(value = "排序")
    private Integer orderNum;

}
