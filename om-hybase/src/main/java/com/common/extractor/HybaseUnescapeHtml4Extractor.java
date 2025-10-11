package com.common.extractor;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HtmlUtil;

/**
 * hybase 高亮字段提取器
 */
public class HybaseUnescapeHtml4Extractor implements HybaseExtractor {

    @Override
    public String extractValue(String fieldName, String value) {
        return StrUtil.replace(StrUtil.trim(HtmlUtil.unescape(value)), "\u00A0", " ");
    }
}
