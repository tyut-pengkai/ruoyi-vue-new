package com.ruoyi.xkt.dto.feedback;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@ApiModel

public class FeedbackDTO {

    @ApiModelProperty(value = "用户反馈内容")
    private String content;
    @ApiModelProperty(value = "用户反馈联系方式")
    private String contact;

}
