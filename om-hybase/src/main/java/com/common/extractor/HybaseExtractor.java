package com.common.extractor;

/**
 * hybase 字段提取器
 */
public interface HybaseExtractor {

    /**
     * 提取字段值
     *
     * @param fieldName 字段名
     * @param value     字段值
     * @return 提取后的字段值
     */
    String extractValue(String fieldName, String value);
}
