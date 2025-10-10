package com.common.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.common.query.HybaseQuery;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HybaseUtils {

    /**
     * 分库字段
     */
    public static final String range = "IR_URLTIME";


    /**
     * 获取分库查询条件
     *
     * @param query 查询条件
     * @return 分库查询条件
     */
    public static String getSearchRangeFilter(String query) {
        String regex = "(\\d{4}[.-/]?\\d{2}[.-/]?\\d{2}( ?\\d{2}:?\\d{2}:?\\d{2})?) TO (\\d{4}[.-/]?\\d{2}[.-/]?\\d{2}( ?\\d{2}:?\\d{2}:?\\d{2})?)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(query);
        StringBuilder startDate = new StringBuilder();
        StringBuilder endDate = new StringBuilder();

        while (matcher.find()) {
            if (!matcher.group(1).isEmpty()) {
                startDate.append(DateUtil.format(DateUtil.parse(matcher.group(1)), "yyyyMM"));
            }
            if (!matcher.group(3).isEmpty()) {
                endDate.append(DateUtil.format(DateUtil.parse(matcher.group(3)), "yyyyMM"));
            }
        }
        if (!startDate.isEmpty() && !endDate.isEmpty()) {
            return StrUtil.format("{}:[{} {} {}]", range, startDate, HybaseQuery.TO, endDate);
        } else if (!startDate.isEmpty() && endDate.isEmpty()) {
            return StrUtil.format("{}:[{} {} *]", range, startDate, HybaseQuery.TO, endDate);
        } else if (startDate.isEmpty() && !endDate.isEmpty()) {
            return StrUtil.format("{}:[* {} {}]", range, startDate, HybaseQuery.TO, endDate);
        } else {
            return "";
        }
    }
}
