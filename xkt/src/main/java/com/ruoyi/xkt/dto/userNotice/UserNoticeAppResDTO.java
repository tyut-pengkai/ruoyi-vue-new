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
public class UserNoticeAppResDTO {

    @ApiModelProperty(value = "公告标题")
    private String noticeTitle;
    @ApiModelProperty(value = "公告内容")
    private String noticeContent;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

}
