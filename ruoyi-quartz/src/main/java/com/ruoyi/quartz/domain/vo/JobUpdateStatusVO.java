package com.ruoyi.quartz.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
public class JobUpdateStatusVO {

    @NotNull(message = "任务ID不能为空")
    @ApiModelProperty(value = "任务ID")
    private Long jobId;
    @ApiModelProperty(value = "任务状态（0正常 1暂停）")
    @NotBlank(message = "任务状态不能为空")
    private String status;

}


