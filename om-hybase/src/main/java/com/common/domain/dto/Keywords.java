package com.common.domain.dto;

import lombok.Data;

@Data
public class Keywords {
    /**
     * 字段
     */
    private String field;
    /**
     * 关系
     */
    private String relation;
    /**
     * 关键词
     */
    private String keyword;
    /**
     * 是否距离检索
     */
    private Boolean isDistance;
    /**
     * 距离
     */
    private Integer distance;
}
