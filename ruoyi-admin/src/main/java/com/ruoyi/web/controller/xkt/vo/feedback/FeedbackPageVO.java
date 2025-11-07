package com.ruoyi.web.controller.xkt.vo.feedback;

import com.ruoyi.web.controller.xkt.vo.BasePageVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
public class FeedbackPageVO extends BasePageVO {

    @ApiModelProperty(value = "反馈内容")
    private String content;
    @ApiModelProperty(value = "联系方式")
    private String contact;

}
