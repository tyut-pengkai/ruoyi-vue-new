package com.ruoyi.framework.sms.ali.entity;

import lombok.Data;

import java.util.Date;

/**
 * 短信模板信息
 *
 * @author liangyq
 */
@Data
public class TemplateInfo {
    private String templateCode;
    private String templateContent;
    private String templateName;
    private Integer templateType;
    private Date createDate;
    private String reason;
    /**
     * 审核状态[0:审核中，1:通过，2:审核失败]
     */
    private Integer templateStatus;
}
