package com.ruoyi.quartz.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("定时任务分页返回数据")
@Data
public class JobPageResDTO {

    @ApiModelProperty(value = "任务ID")
    private Long jobId;
    @ApiModelProperty(value = "任务名称")
    private String jobName;
    @ApiModelProperty(value = "任务组名")
    private String jobGroup;
    @ApiModelProperty(value = "调用目标字符串")
    private String invokeTarget;
    @ApiModelProperty(value = "cron执行表达式")
    private String cronExpression;
    @ApiModelProperty(value = "任务状态（0正常 1暂停）")
    private Integer status;

}
