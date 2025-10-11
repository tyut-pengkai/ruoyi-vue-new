package com.common.core;

import com.common.domain.dto.ServerDTO;
import com.common.domain.OmDataBean;
import com.common.query.HybaseQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * hybase 公共检索
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class HybaseCommonSearch {

    private final HybaseTemplate<OmDataBean> hybaseTemplate;

    /**
     * hybase 信息类型统计检索
     *
     * @param query           hyabse条件
     * @param serverDTO hybase服务信息
     * @return
     */
    public Map<String, Long> queryHybaseCategoryInfotypeList(String query, ServerDTO serverDTO) {
        HybaseQueryWrapper hybaseQueryWrapper = new HybaseQueryWrapper();
        hybaseQueryWrapper.setQuery(query);
        hybaseQueryWrapper.setMappingId("OmDataBean.search");
        hybaseQueryWrapper.setCategoryColumn("SY_INFOTYPE");
        hybaseQueryWrapper.setCategoryNum(20L);
        Map<String, Long> selectCategory = hybaseTemplate.selectCategory(serverDTO, hybaseQueryWrapper);
        selectCategory.put("0", selectCategory.values().stream().mapToLong(Long::longValue).sum());
        return selectCategory;
    }
}
