package com.ruoyi.xkt.dto.notice;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("删除公告")
@Data
@Accessors(chain = true)
public class NoticeDeleteDTO {

    @ApiModelProperty(value = "公告ID列表")
    List<Long> noticeIdList;

   
}
