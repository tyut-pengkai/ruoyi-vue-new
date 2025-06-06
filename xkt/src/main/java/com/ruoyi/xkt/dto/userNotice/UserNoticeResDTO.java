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
@ApiModel("用户消息返回数据")
@Data
@Accessors(chain = true)
public class UserNoticeResDTO {

    @ApiModelProperty(value = "用户消息ID")
    private Long userNoticeId;
    @ApiModelProperty(value = "公告ID")
    private Long noticeId;
    @ApiModelProperty(value = "用户通知类型")
    private Integer targetNoticeType;
    @ApiModelProperty(value = "用户通知类型名称")
    private String targetNoticeTypeName;
    @ApiModelProperty(value = "公告标题")
    private String noticeTitle;
    @ApiModelProperty(value = "公告内容")
    private String noticeContent;
    @ApiModelProperty(value = "档口id")
    private Long storeId;
    @ApiModelProperty(value = "生效开始时间")
    private Date effectStart;
    @ApiModelProperty(value = "生效结束时间")
    private Date effectEnd;
    @ApiModelProperty(value = "是否永久生效 1不是 2是")
    private Integer perpetuity;

}
