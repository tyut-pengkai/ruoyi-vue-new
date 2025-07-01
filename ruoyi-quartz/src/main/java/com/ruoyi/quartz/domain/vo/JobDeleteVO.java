package com.ruoyi.quartz.domain.vo;

import io.swagger.annotations.ApiModel;
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
public class JobDeleteVO {

    @NotNull(message = "任务ID不能为空")
    @ApiModelProperty(value = "任务ID")
    private Long jobId;

    @NotBlank(message = "任务名称不能为空")
    @ApiModelProperty(value = "任务名称")
    private String jobName;
    @NotBlank(message = "任务组名不能为空")
    @ApiModelProperty(value = "任务组名")
    private String jobGroup;
    @NotBlank(message = "调用目标字符串不能为空")
    @ApiModelProperty(value = "调用目标字符串")
    private String invokeTarget;
    @NotBlank(message = "cron执行表达式不能为空")
    @ApiModelProperty(value = "cron执行表达式")
    private String cronExpression;
    @NotBlank(message = "cron计划策略不能为空")
    @ApiModelProperty(value = "cron计划策略", notes = "0=默认,1=立即触发执行,2=触发一次执行,3=不触发立即执行")
    private String misfirePolicy;
    @NotBlank(message = "是否并发执行不能为空")
    @ApiModelProperty(value = "是否并发执行（0允许 1禁止）")
    private String concurrent;
    @ApiModelProperty(value = "任务状态（0正常 1暂停）")
    @NotBlank(message = "任务状态不能为空")
    private String status;

}


