package com.ruoyi.quartz.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
public class JobUpdateStatusDTO {

    @ApiModelProperty(value = "任务ID")
    private Long jobId;
    @ApiModelProperty(value = "任务状态（0正常 1暂停）")
    private String status;

}


