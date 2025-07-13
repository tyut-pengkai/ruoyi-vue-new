package com.ruoyi.web.controller.xkt.vo.userAuthentication;

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
@Data
@ApiModel
@Accessors(chain = true)
public class UserAuthUpdateDelFlagVO {

    @ApiModelProperty(value = "代发ID", required = true)
    @NotNull(message = "代发ID不能为空")
    private Long userAuthId;
    @NotNull(message = "启用状态不能为空")
    @ApiModelProperty(value = "启用状态", required = true)
    private Boolean delFlag;

}
