package com.ruoyi.web.controller.xkt.vo.advertRound.pc.newArrival;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("PC 新品馆 品牌馆")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PCNewMidBrandVO {

    @ApiModelProperty(value = "1推广图")
    private Integer displayType;
    @ApiModelProperty(value = "排序")
    private Integer orderNum;
    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "推广图路径")
    private String fileUrl;

}
