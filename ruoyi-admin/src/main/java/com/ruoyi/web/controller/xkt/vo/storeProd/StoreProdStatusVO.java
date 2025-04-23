package com.ruoyi.web.controller.xkt.vo.storeProd;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口商品状态")
@Data
public class StoreProdStatusVO {

    @NotNull(message = "档口商品ID不能为空!")
    @ApiModelProperty(value = "档口商品ID", required = true)
    private List<Long> storeProdIdList;
    @NotNull(message = "档口商品状态不能为空!")
    @ApiModelProperty(value = "档口商品状态", required = true)
    private Integer prodStatus;

}
