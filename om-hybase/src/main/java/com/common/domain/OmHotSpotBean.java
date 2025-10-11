package com.common.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 *hybase热点库实体类
 */
@ToString
@Getter
@Setter
public class OmHotSpotBean extends HybaseBean implements Serializable {

    /**
     * Hybase唯一值ID
     */
    private String uuid;
    /**
     * 自增id
     */
    private String rid;
    /**
     * 唯一值id
     */
    private String sid;
    /**
     * 标题
     */
    private String title;
    /**
     * 正文
     */
    private String content;
    /**
     * 发布时间
     */
    private Date pubTime;
    /**
     * 作者
     */
    private String author;
    /**
     * 关键词
     */
    private String keywords;
    /**
     * 原文地址
     */
    private String url;

    private String irCataLog2;

    /**
     * 地域
     */
    private String irCextTag3;
    /**
     * 热度
     */
    private Long syIexTag1;

    private String IrCextTag1;

    /**
     * 阅读数量
     */
    private Long irCount1;
    /**
     * 点赞数量
     */
    private Long irCount2;

}
