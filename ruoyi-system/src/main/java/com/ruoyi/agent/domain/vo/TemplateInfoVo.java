package com.ruoyi.agent.domain.vo;

import com.ruoyi.common.enums.TemplateType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TemplateInfoVo {

    private Long templateId;
    private TemplateType templateType;
    private String templateName;
    private String appName;
    private BigDecimal price;

}
