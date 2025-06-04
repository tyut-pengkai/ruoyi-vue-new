package com.ruoyi.web.controller.xkt.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author liangyq
 * @date 2025-05-11 23:46
 */
@ApiModel
@Data
public class IdsVO {

    @NotEmpty(message = "ID不能为空")
    @ApiModelProperty("ID集合")
    private List<Long> ids;
}
