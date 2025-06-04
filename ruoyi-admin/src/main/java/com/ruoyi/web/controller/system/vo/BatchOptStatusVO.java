package com.ruoyi.web.controller.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author liangyq
 * @date 2025-06-03 21:12
 */
@ApiModel
@Data
public class BatchOptStatusVO {

    @NotEmpty(message = "ID不能为空")
    @ApiModelProperty("ID集合")
    private List<Long> ids;

    @NotEmpty(message = "状态不能为空")
    @ApiModelProperty("状态（0正常 1停用）")
    private String status;
}
