package com.common.extractor;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.http.HtmlUtil;

import java.util.List;

/**
 * 处理正文里的视频链接
 */
public class HybaseVideoUrlExtractor implements HybaseExtractor {
    @Override
    public String extractValue(String fieldName, String value) {
        String imageUrlPattern = "(?i)<(?:VIDEO)(?:\\s|\u00A0)+SRC=\"([^\"]+)\"(\\s|\u00A0)*>";
        String unescape = HtmlUtil.unescape(value);
        List<String> imageUrlList = ReUtil.findAll(imageUrlPattern, unescape, 1);
        return CollUtil.join(imageUrlList, ";");
    }
}
