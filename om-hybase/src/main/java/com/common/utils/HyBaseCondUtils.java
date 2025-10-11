package com.common.utils;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.common.domain.dto.KeywordGroupLogic;
import com.common.domain.dto.Keywords;
import com.common.utils.DateUtil;
import com.common.core.HybasePool;
import com.common.query.HybaseQuery;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class HyBaseCondUtils implements HybasePool {

    public static String separator = ";";

    /**
     * 关键词
     */
    public static String getKeywordGroupLogic(String keywordGroupLogic) {
        if (StrUtil.isNotEmpty(keywordGroupLogic)) {
            KeywordGroupLogic keywordGroup = JSONUtil.toBean(keywordGroupLogic, KeywordGroupLogic.class);
            return getKeywordGroupLogic(keywordGroup);
        }
        return null;
    }

    public static String getKeywordGroupLogic(KeywordGroupLogic keywordGroupLogic) {
        List<String> temp = new LinkedList<>();
        for (Keywords keyword : keywordGroupLogic.getKeywords()) {
            if (StrUtil.isNotEmpty(keyword.getKeyword())) {
                if (keyword.getIsDistance()) {
                    if ("FULL".equals(keyword.getField())) {
                        temp.add(HybaseQuery.orMerge(
                            HybaseQuery.distance("IR_URLTITLE", keyword.getDistance(), StrUtil.split(keyword.getKeyword(), separator)),
                            HybaseQuery.distance("IR_CONTENT", keyword.getDistance(), StrUtil.split(keyword.getKeyword(), separator))
                        ));
                    } else {
                        temp.add(HybaseQuery.distance(keyword.getField(), keyword.getDistance(), StrUtil.split(keyword.getKeyword(), separator)));
                    }
                } else {
                    List<String> keys = StrUtil.split(keyword.getKeyword(), separator);
                    if ("FULL".equals(keyword.getField())) {
                        temp.add(HybaseQuery.orMerge(
                            HybaseQuery.general("IR_URLTITLE", keyword.getRelation(), keys, false, true),
                            HybaseQuery.general("IR_CONTENT", keyword.getRelation(), keys, false, true)
                        ));
                    } else {
                        temp.add(HybaseQuery.general(keyword.getField(), keyword.getRelation(), keys, false, true));
                    }
                }
            }
        }
        if (ObjectUtil.isNotEmpty(temp)) {
            String relation = keywordGroupLogic.getRelation();
            if (OR.equals(relation)) {
                return HybaseQuery.orMerge(temp);
            }
            if (AND.equals(relation)) {
                return HybaseQuery.andMerge(temp);
            }
        }
        return null;
    }

    /**
     * 时间范围
     */
    public static String getTimeRange(Date startDate, Date endDate, Long recentDays) {
        if (ObjectUtil.isAllNotEmpty(startDate, endDate)) {
            return HybaseQuery.dateScope("IR_URLTIME", startDate, endDate, true, true);
        } else if (ObjectUtil.isNotEmpty(recentDays)) {
            Date end = DateUtil.parse(DateUtil.now());
            Date start = DateUtil.offsetDay(end, -recentDays.intValue());
            return HybaseQuery.dateScope("IR_URLTIME", start, end, true, true);
        }
        return null;
    }

    /**
     * 检索中心时间表达式
     */
    public static String getCustomizeDate(Date[] time, String timeType) {
        if (timeType.equals("customize")) {
            return HybaseQuery.dateScope("IR_URLTIME", time[0], time[1]);
        } else {
            List<DateTime> dateTimes = DateUtil.calculateDateRange(timeType);
            return HybaseQuery.dateScope("IR_URLTIME", dateTimes.get(0), dateTimes.get(1));
        }
    }


    /**
     * 媒体类型
     */
    public static String getInfoType(String[] infoType) {
        if (ArrayUtil.isNotEmpty(infoType)) {
            return HybaseQuery.or("SY_INFOTYPE", infoType);
        }
        return null;
    }

    public static String getInfoType(String infoType) {
        if (StrUtil.isNotEmpty(infoType)) {
            return getInfoType(StrUtil.splitToArray(infoType, separator));
        }
        return null;
    }

    /**
     * 数据排重
     */
    public static String getDataWeight(String sim) {
        if (StrUtil.isNotEmpty(sim)) {
            switch (sim) {
                case "content":
                    return HybaseQuery.eq("SY_ISSAME", "0");
                case "title":
                    return HybaseQuery.eq("SY_ISSAME_TITLE", "0");
                case "mixing":
                    return HybaseQuery.orMerge(HybaseQuery.eq("SY_ISSAME_TITLE", "0"), HybaseQuery.eq("SY_ISSAME", "0"));
                default:
                    return null;
            }
        }
        return null;
    }

    /**
     * 媒体性质-->SY_MEDIA_TYPE4
     * 媒体资质-->SY_MEDIA_TYPE3
     * 媒体分级-->SY_MEDIA_TYPE5
     */
    public static String getMedia(String field, String[] mediaNature) {
        if (ArrayUtil.isNotEmpty(mediaNature)) {
            return HybaseQuery.or(field, mediaNature);
        }
        return null;
    }

    public static String getMedia(String field, String mediaNature) {
        if (StrUtil.isNotEmpty(mediaNature)) {
            return getMedia(field, StrUtil.splitToArray(mediaNature, separator));
        }
        return null;
    }

    /**
     * 行业领域
     */
    public static String getIndustrySector(String[] industrySector) {
        if (ArrayUtil.isNotEmpty(industrySector)) {
            String template = "行业领域\\\\{}*";
            List<String> list = ArrayUtil.map(industrySector, s -> s.equals("NULL") ? "NULL" : StrUtil.format(template, s));
            return HybaseQuery.or("SY_PROJECT_TAGS", list);
        }
        return null;
    }

    public static String getIndustrySector(String industrySector) {
        if (StrUtil.isNotEmpty(industrySector)) {
            return getIndustrySector(StrUtil.splitToArray(industrySector, separator));
        }
        return null;
    }

    /**
     * 倾向性
     */
    public static String getTendency(String tendency) {
        if (StrUtil.isNotEmpty(tendency) && !StrUtil.equals(tendency, "no")) {
            return HybaseQuery.or("SY_BB_COMMON", StrUtil.split(tendency, separator));
        }
        return null;
    }

    /**
     * 附加表达式
     */
    public static String getAdditionalExpression(String additionalExpression) {
        if (StrUtil.isNotEmpty(additionalExpression)) {
            return additionalExpression;
        }
        return null;
    }

}
