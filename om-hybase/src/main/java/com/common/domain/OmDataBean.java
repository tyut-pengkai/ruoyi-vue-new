package com.common.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@ToString
@Getter
@Setter
public class OmDataBean extends HybaseBean implements Serializable {

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
     * 原文地址
     */
    private String url;
    /**
     * 作者
     */
    private String author;
    /**
     * 文章来源
     */
    private String source;
    /**
     * 文章来源图标
     */
    private String sourceIco;
    /**
     * 发布时间
     */
    private Date pubTime;
    /**
     * 关键词
     */
    private String keywords;
    /**
     * 摘要
     */
    private String abstracts;
    /**
     * 处理过的标红摘要
     */
    private String advancedAbstracts;
    /**
     * 文章里图片地址
     */
    private String[] imageUrl;
    /**
     * 文章里视频地址
     */
    private String[] videoUrl;
    /**
     * 媒体类型
     */
    private String infotype;
    /**
     * 情感倾向
     */
    private String tendency;
    /**
     * 短视频视频地址
     */
    private String irPextag2;
    /**
     * 短视频封面地址
     */
    private String irPextag3;
    /**
     * 热度值
     */
    private Long hotValue;

    /**
     * 微博文章ID
     */
    private String irAccountUid;
    /**
     * 微博用户粉丝数
     */
    private Long irAccountCount1;
    /**
     * 微博认证类型
     */
    private String irAccountVerified;
    /**
     * 信息层级
     */
    private Long syWbForwardLevel;
    /**
     * 涉及用户字段
     */
    private String syWbExtractName;
    // 阅读数
    private Long readCount;
    // 评论数
    private Long commentsCount;
    // 点赞数
    private Long likeCount;
    // 收藏数
    private Long collectionCount;
    // 转发数
    private Long forwardCount;
    // 分享数
    private Long shareCount;
    // 短视频内容抽帧
    private String syImageContent;


}
