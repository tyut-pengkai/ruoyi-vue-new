package com.ruoyi.xkt.dto.feedback;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("消息返回数据")
@Data
@Accessors(chain = true)
public class FeedbackResDTO {

    @ApiModelProperty(value = "ID")
    private Long id;
    @ApiModelProperty(value = "内容")
    private String content;
    @ApiModelProperty(value = "联系方式")
    private String contact;

}
