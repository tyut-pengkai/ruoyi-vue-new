package com.ruoyi.web.controller.xkt.vo.notice;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.xss.Xss;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("新增公告")
@Data
@Accessors(chain = true)
public class NoticeCreateVO {

    @Xss(message = "公告标题不能包含脚本字符")
    @NotBlank(message = "公告标题不能为空")
    @Size(min = 0, max = 50, message = "公告标题不能超过50个字符")
    @ApiModelProperty(value = "公告标题")
    private String noticeTitle;
    @NotNull(message = "公告类型不能为空")
    @ApiModelProperty(value = "公告类型")
    private Integer noticeType;
    @NotBlank(message = "公告内容不能为空")
    @ApiModelProperty(value = "公告内容")
    private String noticeContent;
    @NotNull(message = "谁发的公告不能为空!")
    @ApiModelProperty(value = "谁发的公告 1 档口  2 系统")
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
