package com.ruoyi.agent.domain.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ruoyi.common.enums.TemplateType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TemplateInfoVo {

    private Long agentItemId;
    private Boolean checked;
    private Long templateId;
    private TemplateType templateType;
    private String templateName;
    private String appName;
    private BigDecimal price;
    private BigDecimal myPrice;
    private BigDecimal agentPrice;
    private Date expireTime;
    private String remark;
    @JSONField(serialize = false)
    @JsonIgnore
    private boolean handled;

    public String getReadableName() {
        return "[" + getAppName() + "]" + getTemplateName();
    }

}
