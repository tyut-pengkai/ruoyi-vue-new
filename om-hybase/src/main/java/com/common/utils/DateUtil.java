package com.common.utils;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateTime;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 时间工具类
 *
 * @author om
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtil extends cn.hutool.core.date.DateUtil {
    public static final Locale SIMPLIFIED_CHINESE_LOCALE = new Locale("zh", "CN");
    public static final TimeZone SIMPLIFIED_CHINESE_TIMEZONE = TimeZone.getTimeZone("GMT+08:00");

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static String datePath() {
        return DateUtil.format(DateUtil.date(), "yyyy/MM/dd");
    }

    /**
     * 根据配置计算日期范围
     * 1d: 1天, 1h: 1小时, 1m: 1个月
     *
     * @param config 配置
     * @return 日期范围
     */
    public static List<DateTime> calculateDateRange(String config) {
        long millis;
        DateTime endDate = DateUtil.date();
        int time = Integer.parseInt(config.substring(0, config.length() - 1));
        if (config.endsWith("d")) {
            millis = DateUtil.beginOfDay(DateUtil.offsetDay(endDate, -time)).getTime();
        } else if (config.endsWith("h")) {
            millis = DateUtil.offsetHour(endDate, -time).getTime();
        } else if (config.endsWith("m")) {
            millis = DateUtil.offsetMonth(endDate, -time).getTime();
        } else {
            throw new IllegalArgumentException("Invalid configuration: " + config);
        }
        return ListUtil.of(DateTime.of(millis), endDate);
    }


}
