package com.ruoyi.xkt.dto.advertRound.pc.store;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("PC 档口 底部横幅")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Accessors(chain = true)
public class PCStoreMidBannerDTO {

    @ApiModelProperty(value = "1推广图")
    private Integer displayType;
    @ApiModelProperty(value = "排序")
    private Integer orderNum;
    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "推广图路径")
    private String fileUrl;

}
