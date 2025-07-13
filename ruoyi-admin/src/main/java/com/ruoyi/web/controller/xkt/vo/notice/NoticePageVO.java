package com.ruoyi.web.controller.xkt.vo.notice;

import com.ruoyi.web.controller.xkt.vo.BasePageVO;
import io.swagger.annotations.ApiModel;
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
@ApiModel
public class NoticePageVO extends BasePageVO {

    @ApiModelProperty(value = "公告标题")
    private String noticeTitle;
    @NotNull(message = "ownerType不能为空")
    @ApiModelProperty(value = "谁发的公告 1 档口  2 系统", required = true)
    private Integer ownerType;

}
