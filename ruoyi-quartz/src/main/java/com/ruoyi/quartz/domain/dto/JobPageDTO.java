package com.ruoyi.quartz.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口分页查询入参")
@Data
public class JobPageDTO {

    @ApiModelProperty(value = "pageNum")
    private int pageNum = 1;
    @ApiModelProperty(value = "pageSize")
    private int pageSize = 20;
    @ApiModelProperty(value = "任务名称")
    private String jobName;
    @ApiModelProperty(value = "任务组名")
    private String jobGroup;
    @ApiModelProperty(value = "任务状态（0正常 1暂停）")
    private String status;

}


