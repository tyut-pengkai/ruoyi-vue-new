package com.ruoyi.web.controller.xkt.vo.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口启用停用")
@Data
@Accessors(chain = true)
public class StoreUpdateDelFlagVO {

    @ApiModelProperty(value = "档口ID", required = true)
    @NotNull(message = "档口ID不能为空")
    private Long storeId;
    @NotNull(message = "启用状态不能为空")
    @ApiModelProperty(value = "启用状态", required = true)
    private Boolean delFlag;

}
