package com.ruoyi.web.vo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class AddTemplateRapidVo {

    @NotNull(message = "软件ID不能为空")
    private Long appId;
    @NotEmpty(message = "新增类型不能为空")
    private List<AddTemplateRapidInnerVo> templateSelectionList;

}
