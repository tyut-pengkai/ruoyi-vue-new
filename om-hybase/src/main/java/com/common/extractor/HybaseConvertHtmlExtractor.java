package com.common.extractor;

import cn.hutool.core.util.StrUtil;

import java.util.List;

public class HybaseConvertHtmlExtractor implements HybaseExtractor {
    @Override
    public String extractValue(String fieldName, String value) {
        List<String> split = StrUtil.split(value, "\r\n");
        StringBuilder sb = new StringBuilder();
        for (String s : split) {
            sb.append("<p>").append(StrUtil.trim(s)).append("</p>");
        }
        return sb.toString();
    }
}
