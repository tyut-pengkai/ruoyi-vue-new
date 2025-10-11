package com.common.query;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.common.core.HybasePool;
import lombok.NonNull;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class HybaseQuery implements HybasePool {


    /**
     * 生成查询条件
     *
     * @param fieldName 字段名
     * @param words     关键词
     * @param logic     逻辑
     * @param weighting 字段加权
     * @param fuzzy     模糊查询
     * @param quoted    引号
     * @return 查询条件
     */

    private static String general(@NonNull String fieldName, String logic, List<String> words, Integer weighting, Boolean fuzzy, Boolean quoted) {
        StringBuilder query = new StringBuilder();
        if (!words.isEmpty()) {
            query.append(fieldName).append(COLON);
            query.append(PARENTHESIS_START);
            for (int i = 0; i < words.size(); i++) {
                query.append(SPACE);
                if (quoted) query.append(DOUBLE_QUOTES);
                query.append(words.get(i));
                // 权重
                if (weighting > 0) {
                    query.append(CARET);
                    query.append(weighting);
                }
                if (quoted) query.append(DOUBLE_QUOTES);
                // 模糊检索
                if (fuzzy) query.append(ASTERISK);
                query.append(SPACE);
                if (StrUtil.isNotEmpty(logic) && i < words.size() - 1) {
                    query.append(logic);
                }
            }
            query.append(PARENTHESIS_END);
        }
        return query.toString();
    }

    public static String general(@NonNull String fieldName, String logic, List<String> words, Boolean fuzzy, Boolean quoted) {
        return general(fieldName, logic, words, 0, fuzzy, quoted);
    }

    /**
     * OR 表达式生成（加权，模糊，引号）
     *
     * @param fieldName 字段名
     * @param words     关键词
     * @param weighting 字段加权
     * @param fuzzy     模糊查询
     * @param quoted    引号
     * @return 查询条件
     */
    private static String or(@NonNull String fieldName, List<String> words, Integer weighting, Boolean fuzzy, Boolean quoted) {
        return general(fieldName, OR, words, weighting, fuzzy, quoted);
    }

    /**
     * OR 表达式生成 （模糊，引号）
     *
     * @param fieldName 字段名
     * @param words     关键词
     * @param fuzzy     模糊查询
     * @param quoted    引号
     * @return 查询条件
     */
    private static String or(@NonNull String fieldName, List<String> words, Boolean fuzzy, Boolean quoted) {
        return or(fieldName, words, 0, fuzzy, quoted);
    }

    /**
     * OR 表达式生成 （加权）
     *
     * @param fieldName 字段名
     * @param words     关键词
     * @param weighting 字段加权
     * @return 查询条件
     */
    public static String orWithWeighting(@NonNull String fieldName, Integer weighting, String... words) {
        return orWithWeighting(fieldName, ListUtil.toLinkedList(words), weighting);
    }

    /**
     * OR 表达式生成 （加权）
     *
     * @param fieldName 字段名
     * @param words     关键词
     * @param weighting 字段加权
     * @return 查询条件
     */
    public static String orWithWeighting(@NonNull String fieldName, List<String> words, Integer weighting) {
        return or(fieldName, words, weighting, false, false);
    }

    /**
     * OR 表达式生成 （引号）
     *
     * @param fieldName 字段名
     * @param words     关键词
     * @return 查询条件
     */
    public static String orWithQuoted(@NonNull String fieldName, String... words) {
        return orWithQuoted(fieldName, ListUtil.toLinkedList(words));
    }

    /**
     * OR 表达式生成 （引号）
     *
     * @param fieldName 字段名
     * @param words     关键词
     * @return 查询条件
     */
    public static String orWithQuoted(@NonNull String fieldName, List<String> words) {
        return or(fieldName, words, false, true);
    }

    /**
     * OR 表达式生成 （模糊）
     *
     * @param fieldName 字段名
     * @param words     关键词
     * @return 查询条件
     */
    public static String orWithFuzzy(@NonNull String fieldName, String... words) {
        return orWithFuzzy(fieldName, ListUtil.toLinkedList(words));
    }

    /**
     * OR 表达式生成 （模糊）
     *
     * @param fieldName 字段名
     * @param words     关键词
     * @return 查询条件
     */
    public static String orWithFuzzy(@NonNull String fieldName, List<String> words) {
        return or(fieldName, words, true, false);
    }

    /**
     * OR 表达式生成
     *
     * @param fieldName 字段名
     * @param words     关键词
     * @return 查询条件
     */
    public static String or(@NonNull String fieldName, String... words) {
        return or(fieldName, ListUtil.toLinkedList(words));
    }

    /**
     * OR 表达式生成
     *
     * @param fieldName 字段名
     * @param words     关键词
     * @return 查询条件
     */
    public static String or(@NonNull String fieldName, List<String> words) {
        return or(fieldName, words, false, false);
    }

    /**
     * AND 表达式生成（加权，模糊，引号）
     *
     * @param fieldName 字段名
     * @param words     关键词
     * @param weighting 字段加权
     * @param fuzzy     模糊查询
     * @param quoted    引号
     * @return 查询条件
     */
    private static String and(@NonNull String fieldName, List<String> words, Integer weighting, Boolean fuzzy, Boolean quoted) {
        return general(fieldName, AND, words, weighting, fuzzy, quoted);
    }

    /**
     * AND 表达式生成 （模糊，引号）
     *
     * @param fieldName 字段名
     * @param words     关键词
     * @param fuzzy     模糊查询
     * @param quoted    引号
     * @return 查询条件
     */
    private static String and(@NonNull String fieldName, List<String> words, Boolean fuzzy, Boolean quoted) {
        return and(fieldName, words, 0, fuzzy, quoted);
    }

    /**
     * AND 表达式生成 （加权）
     *
     * @param fieldName 字段名
     * @param words     关键词
     * @param weighting 字段加权
     * @return 查询条件
     */
    public static String andWithWeighting(@NonNull String fieldName, Integer weighting, String... words) {
        return andWithWeighting(fieldName, weighting, ListUtil.toLinkedList(words));
    }

    /**
     * AND 表达式生成 （加权）
     *
     * @param fieldName 字段名
     * @param words     关键词
     * @param weighting 字段加权
     * @return 查询条件
     */
    public static String andWithWeighting(@NonNull String fieldName, Integer weighting, List<String> words) {
        return and(fieldName, words, weighting, false, false);
    }

    /**
     * AND 表达式生成 （引号）
     *
     * @param fieldName 字段名
     * @param words     关键词
     * @return 查询条件
     */
    public static String andWithQuoted(@NonNull String fieldName, String... words) {
        return andWithQuoted(fieldName, ListUtil.toLinkedList(words));
    }

    /**
     * AND 表达式生成 （引号）
     *
     * @param fieldName 字段名
     * @param words     关键词
     * @return 查询条件
     */
    public static String andWithQuoted(@NonNull String fieldName, List<String> words) {
        return and(fieldName, words, false, true);
    }

    /**
     * AND 表达式生成 （模糊）
     *
     * @param fieldName 字段名
     * @param words     关键词
     * @return 查询条件
     */
    public static String andWithFuzzy(@NonNull String fieldName, String... words) {
        return andWithQuoted(fieldName, ListUtil.toLinkedList(words));
    }

    /**
     * AND 表达式生成 （模糊）
     *
     * @param fieldName 字段名
     * @param words     关键词
     * @return 查询条件
     */
    public static String andWithFuzzy(@NonNull String fieldName, List<String> words) {
        return and(fieldName, words, true, false);
    }

    /**
     * AND 表达式生成
     *
     * @param fieldName 字段名
     * @param words     关键词
     * @return 查询条件
     */
    public static String and(@NonNull String fieldName, String... words) {
        return and(fieldName, ListUtil.toLinkedList(words));
    }

    /**
     * AND 表达式生成
     *
     * @param fieldName 字段名
     * @param words     关键词
     * @return 查询条件
     */
    public static String and(@NonNull String fieldName, List<String> words) {
        return and(fieldName, words, false, false);
    }

    /**
     * NOT 表达式生成 （query1 的结果集中排除 query2 的结果集）
     *
     * @param query1 查询条件1
     * @param query2 查询条件2
     * @return 查询条件
     */
    public static String not(@NonNull String query1, @NonNull String query2) {
        return PARENTHESIS_START + SPACE + query1 + SPACE + PARENTHESIS_END + SPACE + NOT + SPACE + PARENTHESIS_START + SPACE + query2 + SPACE + PARENTHESIS_END;
    }

    /**
     * NOT 表达式生成 （全库数据排除）
     *
     * @param query 查询条件
     * @return 查询条件
     */
    public static String not(@NonNull String query) {
        return not("*:*", query);
    }

    /**
     * 等于表达式生成
     *
     * @param fieldName 字段名
     * @param words     关键词
     * @param fuzzy     模糊查询
     * @param quoted    引号
     * @return 查询条件
     */
    private static String eq(@NonNull String fieldName, String words, Boolean fuzzy, Boolean quoted) {
        return general(fieldName, null, ListUtil.toLinkedList(words), 0, fuzzy, quoted);
    }

    /**
     * 等于表达式生成 （引号）
     *
     * @param fieldName 字段名
     * @param words     关键词
     * @return 查询条件
     */
    public static String eqWithQuoted(@NonNull String fieldName, String words) {
        return eq(fieldName, words, false, true);
    }

    /**
     * 等于表达式生成 （模糊）
     *
     * @param fieldName 字段名
     * @param words     关键词
     * @return 查询条件
     */
    public static String eqWithFuzzy(@NonNull String fieldName, String words) {
        return eq(fieldName, words, true, false);
    }

    /**
     * 等于表达式生成
     *
     * @param fieldName 字段名
     * @param words     关键词
     * @return 查询条件
     */
    public static String eq(@NonNull String fieldName, String words) {
        return eq(fieldName, words, false, false);
    }

    /**
     * 范围表达式生成
     *
     * @param fieldName    字段名
     * @param startWord    开始关键词
     * @param endWord      结束关键词
     * @param includeStart 是否包含开始关键词
     * @param includeEnd   是否包含结束关键词
     * @return 查询条件
     */
    private static String scope(@NonNull String fieldName, String startWord, String endWord, Boolean includeStart, Boolean includeEnd) {
        StringBuilder sb = new StringBuilder();
        sb.append(fieldName).append(COLON).append(SPACE);
        if (includeStart) {
            sb.append(BRACKET_START);
        } else {
            sb.append(DELIM_START);
        }
        sb.append(SPACE).append(startWord).append(SPACE).append(TO).append(SPACE).append(endWord).append(SPACE);
        if (includeEnd) {
            sb.append(BRACKET_END);
        } else {
            sb.append(DELIM_END);
        }
        return sb.toString();
    }

    /**
     * 大于表达式生成
     *
     * @param fieldName 字段名
     * @param words     关键词
     * @return 查询条件
     */
    public static String gt(@NonNull String fieldName, String words) {
        return scope(fieldName, words, ASTERISK, false, false);
    }

    /**
     * 大于等于表达式生成
     *
     * @param fieldName 字段名
     * @param words     关键词
     * @return 查询条件
     */
    public static String ge(@NonNull String fieldName, String words) {
        return scope(fieldName, words, ASTERISK, true, false);
    }

    /**
     * 小于表达式生成
     *
     * @param fieldName 字段名
     * @param words     关键词
     * @return 查询条件
     */
    public static String lt(@NonNull String fieldName, String words) {
        return scope(fieldName, ASTERISK, words, false, false);
    }

    /**
     * 小于等于表达式生成
     *
     * @param fieldName 字段名
     * @param words     关键词
     * @return 查询条件
     */
    public static String le(@NonNull String fieldName, String words) {
        return scope(fieldName, ASTERISK, words, false, true);
    }

    /**
     * 范围表达式生成
     *
     * @param fieldName 字段名
     * @param startWord 关键词
     * @param endWord   关键词
     * @return 查询条件
     */
    public static String glt(@NonNull String fieldName, String startWord, String endWord) {
        return scope(fieldName, startWord, endWord, false, false);
    }

    /**
     * 范围表达式生成 （包含）
     *
     * @param fieldName 字段名
     * @param startWord 关键词
     * @param endWord   关键词
     * @return 查询条件
     */
    public static String gle(@NonNull String fieldName, String startWord, String endWord) {
        return scope(fieldName, startWord, endWord, true, true);
    }

    /**
     * 时间范围表达式生成
     *
     * @param fieldName    字段名
     * @param startDate    开始时间
     * @param endDate      结束时间
     * @param includeStart 是否包含开始时间
     * @param includeEnd   是否包含结束时间
     * @return 查询条件
     */
    public static String dateScope(@NonNull String fieldName, Date startDate, Date endDate, Boolean includeStart, Boolean includeEnd) {
        return scope(fieldName, DateUtil.format(startDate, DatePattern.PURE_DATETIME_PATTERN), DateUtil.format(endDate, DatePattern.PURE_DATETIME_PATTERN), includeStart, includeEnd);
    }

    /**
     * 时间范围表达式生成 （包含）
     *
     * @param fieldName 字段名
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 查询条件
     */
    public static String dateScope(@NonNull String fieldName, Date startDate, Date endDate) {
        return dateScope(fieldName, startDate, endDate, true, true);
    }

    /**
     * 指定距离查询
     *
     * @param fieldName 字段名
     * @param distance  距离
     * @param words     关键词
     */
    public static String distance(@NonNull String fieldName, Integer distance, String... words) {
        return distance(fieldName, distance, ListUtil.toLinkedList(words));
    }

    /**
     * 指定距离查询
     *
     * @param fieldName 字段名
     * @param distance  距离
     * @param words     关键词
     */
    public static String distance(@NonNull String fieldName, Integer distance, List<String> words) {
        return fieldName + COLON + SPACE + DOUBLE_QUOTES + CollUtil.join(words, SPACE) + DOUBLE_QUOTES + TILDE + distance;
    }

    /**
     * UUID查询
     *
     * @param uuid uuid
     * @return 查询条件
     */
    public static String uuid(String uuid) {
        return eq(UUID, uuid);
    }

    /**
     * 相似性检索
     *
     * @param fieldName 字段名
     * @param distance  命中分词后总词项数百分比
     * @param words     关键词
     * @return 查询条件
     */
    public static String like(@NonNull String fieldName, Integer distance, String... words) {
        return like(fieldName, distance, ListUtil.toLinkedList(words));
    }

    /**
     * 相似性检索
     *
     * @param fieldName 字段名
     * @param distance  命中分词后总词项数百分比
     * @param words     关键词
     * @return 查询条件
     */
    public static String like(@NonNull String fieldName, Integer distance, List<String> words) {
        return fieldName + LIKE + COLON + SPACE + DOUBLE_QUOTES + CollUtil.join(words, SPACE) + DOUBLE_QUOTES + TILDE + distance;
    }

    /**
     * 相似性检索，和LIKE相似，但是由用户自己切分
     *
     * @param fieldName 字段名
     * @param distance  命中分词后总词项数百分比
     * @param words     关键词
     * @return 查询条件
     */
    public static String include(@NonNull String fieldName, Integer distance, String... words) {
        return include(fieldName, distance, ListUtil.toLinkedList(words));
    }

    /**
     * 相似性检索，和LIKE相似，但是由用户自己切分
     *
     * @param fieldName 字段名
     * @param distance  命中分词后总词项数百分比
     * @param words     关键词
     * @return 查询条件
     */
    public static String include(@NonNull String fieldName, Integer distance, List<String> words) {
        return fieldName + INCLUDE + COLON + SPACE + DOUBLE_QUOTES + CollUtil.join(words, SPACE) + DOUBLE_QUOTES + TILDE + distance;
    }

    /**
     * 合并表达式
     *
     * @param logic  AND OR
     * @param querys 表达式
     * @return 查询条件
     */
    public static String merge(@NonNull String logic, List<String> querys) {
        querys = querys.parallelStream().filter(Objects::nonNull).collect(Collectors.toList());
        if (ObjectUtil.isEmpty(querys)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < querys.size(); i++) {
            if (StrUtil.isNotEmpty(querys.get(i))) {
                sb.append(PARENTHESIS_START).append(SPACE).append(querys.get(i)).append(SPACE).append(PARENTHESIS_END);
                if (i != querys.size() - 1) {
                    sb.append(SPACE).append(logic).append(SPACE);
                }
            }
        }
        return sb.toString();
    }

    /**
     * 表达式合并 AND
     *
     * @param querys 表达式
     * @return 查询条件
     */
    public static String andMerge(String... querys) {
        return merge(AND, ListUtil.toLinkedList(querys));
    }

    /**
     * 表达式合并 AND
     *
     * @param querys 表达式
     * @return 查询条件
     */
    public static String andMerge(List<String> querys) {
        return merge(AND, querys);
    }

    /**
     * 表达式合并 OR
     *
     * @param querys 表达式
     * @return 查询条件
     */
    public static String orMerge(String... querys) {
        return merge(OR, ListUtil.toLinkedList(querys));
    }

    /**
     * 表达式合并 OR
     *
     * @param querys 表达式
     * @return 查询条件
     */
    public static String orMerge(List<String> querys) {
        return merge(OR, querys);
    }
}
