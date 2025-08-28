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
public class UserNoticePageDTO extends BasePageDTO {

    @ApiModelProperty(value = "公告标题")
    private String noticeTitle;
    @ApiModelProperty(value = "公告类型（1通知 2公告）")
    private Integer noticeType;

}
