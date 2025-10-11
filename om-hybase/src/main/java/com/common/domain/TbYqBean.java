package com.common.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class TbYqBean extends HybaseBean implements Serializable {

    private String uuid;

    private String rid;

    private String urlTitle;

    private String abstracts;

    private String urlContent;

    private String source;

    @DateTimeFormat(fallbackPatterns = "yyyy-MM-dd HH:mm:ss")
    private Date pubTime;

    private String urlName;

    private Date loadTime;

    // 违规事项名称
    private String illegalName;

    // 违规类型
    private String illegalType;

    // 违规对象
    private String illegalObject;

    // 违规日期
    @DateTimeFormat(fallbackPatterns = "yyyy-MM-dd HH:mm:ss")
    private Date illegalDate;

    // 说明
    private String introduction;

    // 通报部门
    private String reportDept;

    // 处罚依据
    private String punishAccording;

    // 处罚结果
    private String punishResult;
}
