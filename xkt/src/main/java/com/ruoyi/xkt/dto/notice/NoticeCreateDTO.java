package com.ruoyi.xkt.dto.notice;

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
@ApiModel("新增公告")
@Data
@Accessors(chain = true)
public class NoticeCreateDTO {

    @ApiModelProperty(value = "公告标题")
    private String noticeTitle;
    @ApiModelProperty(value = "公告类型")
    private Integer noticeType;
    @ApiModelProperty(value = "公告内容")
    private String noticeContent;
    @ApiModelProperty(value = "谁发的公告 1 系统  2 档口")
    private Integer ownerType;
    @ApiModelProperty(value = "档口id")
    private Long storeId;
    @ApiModelProperty(value = "生效开始时间")
    private Date effectStart;
    @ApiModelProperty(value = "生效结束时间")
    private Date effectEnd;
    @ApiModelProperty(value = "是否永久生效 1不是 2是")
    private Integer perpetuity;

}
