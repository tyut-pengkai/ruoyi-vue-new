package com.ruoyi.xkt.dto.notice;

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
@ApiModel("公告分页查询入参")
@Data
public class NoticePageDTO extends BasePageDTO {

    @ApiModelProperty(value = "公告标题")
    private String noticeTitle;

}
