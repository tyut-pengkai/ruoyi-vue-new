package com.common.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class TbZxBean extends HybaseBean implements Serializable {


    private String uuid;
    private String rid;

    private String urlTitle;

    private String urlContent;

    private String source;

    @DateTimeFormat(fallbackPatterns = "yyyy-MM-dd HH:mm:ss")
    private Date pubTime;

    private String urlName;

    private Date loadTime;
}
