package com.ruoyi.common.enums;

/**
 * 数据源
 * 
 * @author ruoyi
 */
public enum DataSourceType
{
    /**
     * 主库
     */
    MASTER,

    /**
     * 从库
     */
    SLAVE,

    // 202.10.28 新增多个从库，避免后期修改代码
    SLAVE2,

    SLAVE3,

    SLAVE4,
    // 202.10.28 新增多个从库，避免后期修改代码（end）
}
