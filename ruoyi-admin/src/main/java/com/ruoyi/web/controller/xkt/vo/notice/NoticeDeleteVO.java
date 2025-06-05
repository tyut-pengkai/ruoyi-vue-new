package com.ruoyi.web.controller.xkt.vo.notice;

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
public class NoticeDeleteVO {

    @NotNull(message = "公告ID不能为空")
    @ApiModelProperty(value = "公告ID列表")
    List<Long> noticeIdList;


}
