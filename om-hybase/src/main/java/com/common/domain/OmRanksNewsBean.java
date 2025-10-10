package com.common.domain;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 排名库实体类
 */

@EqualsAndHashCode(callSuper = true)
@ToString
@Getter
@Setter
@Data
public class OmRanksNewsBean extends HybaseBean implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String sid;
    // 标题
    private String title;
    //热度
    private Long irHeat;
    //内容
    private String irContent;
    //阅读量
    private String irIndexLink;
    //搜索指数
    private String irSearchIndex;
    //发布时间
    private Date urlTime;
    // 发布站点
    private String siteName;
    // 频道
    private String channel;
    // 原文链接
    private String urlName;

}
