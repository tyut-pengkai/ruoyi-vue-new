package com.ruoyi.web.controller.xkt.vo.advertRound.pc.index;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("PC 首页 顶部横向轮播图")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PCIndexTopLeftBannerVO {

    @ApiModelProperty(value = "展示类型 1推广图、2商品、3推广图及商品、4店铺名称")
    private Integer displayType;
    @ApiModelProperty(value = "排序")
    private Integer orderNum;
    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "推广图路径")
    private String fileUrl;

}
