package com.common.query;

import cn.hutool.core.util.ObjectUtil;
import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * hybase查询参数封装类
 */
@Data
public class HybaseQueryWrapper {

    /**
     * hybase实体类映射id
     */
    private String mappingId;
    /**
     * hybase查询参数
     */
    private String query;
    /**
     * hybase排序参数
     */
    private List<HybaseSort> hybaseSorts;
    /**
     * hybase排序参数
     */
    private HybaseSort hybaseSort;
    /**
     * 分页参数
     */
    private OffsetLimit offsetLimit;
    /**
     * 分类字段
     */
    private String categoryColumn;
    /**
     * 返回类别统计前多少个类别统计数
     */
    private Long categoryNum;

    public List<HybaseSort> getHybaseSorts() {
        if (ObjectUtil.isNull(hybaseSorts) && ObjectUtil.isNotNull(hybaseSort)) {
            hybaseSorts = Collections.singletonList(hybaseSort);
        }
        return hybaseSorts;
    }
}
