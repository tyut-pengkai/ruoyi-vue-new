package com.common.query;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@Data
public class HybasePage<T> implements Serializable {
    /**
     * 当前页数据
     */
    private List<T> records;
    /**
     * 总数
     */
    private long total;
    /**
     * 每页显示条数
     */
    private long size;
    /**
     * 当前页
     */
    private long current;
}
