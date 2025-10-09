package com.ruoyi.web.controller.xkt.vo.feedback;

import com.ruoyi.common.xss.Xss;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@ApiModel

public class FeedbackVO {

    @ApiModelProperty(value = "用户反馈内容", required = true)
    @Size(max = 180, message = "反馈内容不能超过180个字!")
    @NotBlank(message = "反馈内容不能为空!")
    @Xss
    private String content;
    @ApiModelProperty(value = "用户反馈联系方式", required = true)
    @Size(max = 20, message = "联系方式不能超过20个字!")
    @Xss
    private String contact;

}
