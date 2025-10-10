package com.common.mapping;

import lombok.Data;

import java.util.Collection;
import java.util.Map;

@Data
public class HybaseClassMapping {

    /**
     * 从配置文件中读取的类映射
     */
    private String id;

    /**
     * 类名
     */
    private String className;

    /**
     * 表名
     */
    private String table;

    /**
     * uuid属性名
     */
    private String uuidPropertyName;

    /**
     * 列名
     */
    private Collection<String> columnNames;

    /**
     * 属性名
     */
    private Collection<String> propertyNames;

    /**
     * 有标红的属性名
     */
    private Collection<String> highlighterPropertyNames;


    /**
     * 属性名和列名的映射
     */
    private Map<String, String> columnPropertyMap;

    /**
     * 属性名和属性映射的映射
     */
    private Map<String, HybasePropertyMapping> propertyMappings;

    /**
     * 获取属性名
     *
     * @param propertyName 属性名
     * @return 属性名
     */
    public String getColumnName(String propertyName) {
        return columnPropertyMap.get(propertyName);
    }
}
