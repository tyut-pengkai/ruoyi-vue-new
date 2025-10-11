package com.common.extractor;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.http.HtmlUtil;

import java.util.List;

/**
 * hybase 高亮字段提取器
 */
public class HybaseImageUrlExtractor implements HybaseExtractor {

    @Override
    public String extractValue(String fieldName, String value) {
        String imageUrlPattern = "(?i)<(?:IMAGE|IMG)(?:\\s|\u00A0)+SRC=\"([^\"]+)\"(\\s|\u00A0)*>";
        String unescape = HtmlUtil.unescape(value);
        List<String> imageUrlList = ReUtil.findAll(imageUrlPattern, unescape, 1);
        return CollUtil.join(imageUrlList, ";");
    }
}
