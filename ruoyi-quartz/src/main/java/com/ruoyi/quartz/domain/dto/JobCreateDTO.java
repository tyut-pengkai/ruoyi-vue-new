package com.ruoyi.quartz.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("新增定时任务入参")
@Data
public class JobCreateDTO {

    @ApiModelProperty(value = "任务名称")
    private String jobName;
    @ApiModelProperty(value = "任务组名")
    private String jobGroup;
    @ApiModelProperty(value = "调用目标字符串")
    private String invokeTarget;
    @ApiModelProperty(value = "cron执行表达式")
    private String cronExpression;
    @ApiModelProperty(value = "cron计划策略", notes = "0=默认,1=立即触发执行,2=触发一次执行,3=不触发立即执行")
    private String misfirePolicy;
    @ApiModelProperty(value = "是否并发执行（0允许 1禁止）")
    private String concurrent;

}


