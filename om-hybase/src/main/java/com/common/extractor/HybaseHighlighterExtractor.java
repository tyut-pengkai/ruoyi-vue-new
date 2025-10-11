package com.common.extractor;

/**
 * hybase 高亮字段提取器
 */
public class HybaseHighlighterExtractor implements HybaseExtractor {

    @Override
    public String extractValue(String fieldName, String value) {
        return value.replaceAll("<font color=red>", "<highlighter>").replaceAll("</font>", "</highlighter>");
    }
}
