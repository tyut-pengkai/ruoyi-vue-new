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

    @ApiModelProperty(value = "storeProdId")
    private Long storeProdId;
    @ApiModelProperty(value = "档口id")
    private Long storeId;
    @ApiModelProperty(value = "业务名称")
    private String bizName;
    @ApiModelProperty(value = "1.档口（推广图） 2.商品  10.不跳转")
    private Integer displayType;
    @ApiModelProperty(value = "跳转链接")
    private String fileUrl;
    @ApiModelProperty(value = "排序")
    private Integer orderNum;

}
