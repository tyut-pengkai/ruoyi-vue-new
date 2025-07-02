package com.ruoyi.web.controller.xkt.vo.userNotice;

import com.ruoyi.web.controller.xkt.vo.BasePageVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserNoticeAppTypePageVO extends BasePageVO {

    @NotNull(message = "消息接收类型不能为空")
    @ApiModelProperty(value = "消息接收类型")
    private Integer targetNoticeType;

}
