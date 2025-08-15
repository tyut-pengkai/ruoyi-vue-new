package com.ruoyi.web.controller.xkt.vo.advertRound.pc.index;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@ApiModel
public class PCIndexTopRightBannerVO {

    @ApiModelProperty(value = "展示类型 1推广图、2商品、3推广图及商品、4店铺名称")
    private Integer displayType;
    @ApiModelProperty(value = "档口商品ID")
    private Long storeProdId;
    @ApiModelProperty(value = "排序")
    private Integer orderNum;
    @ApiModelProperty(value = "商品第一张主图路径")
    private String fileUrl;
    @ApiModelProperty(value = "档口ID")
    private Long storeId;

}
