package com.ruoyi.xkt.dto.userNotice;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("用户消息APP返回数据")
@Data
@Accessors(chain = true)
public class UserNoticeAppListResDTO {

    @ApiModelProperty(value = "用户通知类型")
    private Integer targetNoticeType;
    @ApiModelProperty(value = "用户通知类型名称")
    private String targetNoticeTypeName;
    @ApiModelProperty(value = "公告内容")
    private String noticeContent;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "是否已读 0未读 1已读")
    private Integer readStatus;

}
