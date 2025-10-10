package com.common.query;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@Setter
public class HybaseSort {
    /**
     * 按文档的入库顺序排序
     */
    private static final String DOCID = "DOCID";
    /**
     * 按相关度排序
     */
    private static final String RELEVANCE = "RELEVANCE";

    /**
     * 排序字段
     */
    private String field;
    private String hybaseField;
    /**
     * 是否升序
     */
    private Boolean asc;

    public static HybaseSort of(String column) {
        if (column.startsWith("+")) {
            return build(StrUtil.removePrefix(column, "+"), true);
        } else if (column.startsWith("-")) {
            return build(StrUtil.removePrefix(column, "-"), false);
        } else {
            return build(StrUtil.removePrefix(column, "-"), false);
        }
    }

    public static HybaseSort asc(String column) {
        return build(column, true);
    }

    public static HybaseSort desc(String column) {
        return build(column, false);
    }

    public static List<HybaseSort> ascs(String... columns) {
        return Arrays.stream(columns).map(HybaseSort::asc).collect(Collectors.toList());
    }

    public static List<HybaseSort> descs(String... columns) {
        return Arrays.stream(columns).map(HybaseSort::desc).collect(Collectors.toList());
    }

    private static HybaseSort build(String column, boolean asc) {
        HybaseSort hybaseSort = new HybaseSort();
        hybaseSort.setField(column);
        hybaseSort.setAsc(asc);
        return hybaseSort;
    }

    /**
     * hybase 排序表达式 +号是升序 -号是降序 多个字段用分号
     */
    public String getExpression() {
        // true(升序) = +,false(降序) = -
        return (asc ? '+' : '-') + StrUtil.nullToDefault(hybaseField, field);
    }

}
