package com.ruoyi.xkt.dto.notice;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel
@Data
@Accessors(chain = true)
public class NoticeLatest10ResDTO {

    @ApiModelProperty(value = "公告ID")
    @JsonProperty(value = "noticeId")
    private Long id;
    @ApiModelProperty(value = "公告标题")
    private String noticeTitle;

}
