package com.common.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class KeywordGroupLogic {
    /**
     * 关系
     */
    private String relation;
    /**
     * 关键词
     */
    private List<Keywords> keywords;
}
