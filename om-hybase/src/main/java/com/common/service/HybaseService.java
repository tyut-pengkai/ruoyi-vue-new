package com.common.service;

import com.common.domain.dto.ServerDTO;
import com.common.domain.OmDataBean;

import java.util.List;
import java.util.Map;

/**
 * @author TRS
 * @date 2023-11-16
 */
public interface HybaseService {

    /**
     * 设置热度值
     *
     * @param category 分类统计结果
     * @param dto      海贝链接配置对象
     * @param query    海贝基础查询表达式
     * @return
     */
    List<OmDataBean> setHotValue(Map<String, Long> category, ServerDTO dto, String query);
}
