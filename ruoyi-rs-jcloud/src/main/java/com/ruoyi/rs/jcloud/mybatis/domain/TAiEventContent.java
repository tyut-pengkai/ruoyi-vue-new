/**
 * @Copyright: lyj  All rights reserved.
 */
package com.ruoyi.rs.jcloud.mybatis.domain;

import java.util.Date;
import java.io.Serializable;

public class TAiEventContent implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer userId;
    private String status;
    private Date createTime;
    private Date updateTime;
    private String content;
    private String eventType;
    private String figureName;
    private String keyWords;

    public Integer getId() {
        return id;
    }
    public Integer getUserId() {
        return userId;
    }
    public String getStatus() {
        return status;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public Date getUpdateTime() {
        return updateTime;
    }
    public String getContent() {
        return content;
    }
    public String getEventType() {
        return eventType;
    }
    public String getFigureName() {
        return figureName;
    }
    public String getKeyWords() {
        return keyWords;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
    public void setFigureName(String figureName) {
        this.figureName = figureName;
    }
    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }
}