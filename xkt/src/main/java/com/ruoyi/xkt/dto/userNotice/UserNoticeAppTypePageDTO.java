package com.ruoyi.xkt.dto.userNotice;

import com.ruoyi.xkt.dto.BasePageDTO;
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
@ApiModel("用户消息分页查询入参")
@Data
public class UserNoticeAppTypePageDTO extends BasePageDTO {

    @ApiModelProperty(value = "消息接收类型")
    private Integer targetNoticeType;

}
