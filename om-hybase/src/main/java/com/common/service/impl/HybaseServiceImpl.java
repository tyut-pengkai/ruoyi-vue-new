package com.common.service.impl;

import com.common.domain.dto.ServerDTO;
import com.common.core.HybaseTemplate;
import com.common.domain.OmDataBean;
import com.common.query.*;
import com.common.service.HybaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author TRS
 * @date 2023-11-16
 */
@RequiredArgsConstructor
@Service
public class HybaseServiceImpl implements HybaseService {


    private final HybaseTemplate<OmDataBean> template;

    /**
     * 设置热度值
     *
     * @param category 分类统计结果
     * @param dto      海贝链接配置对象
     * @param query    海贝基础查询表达式
     * @return
     */
    @Override
    public List<OmDataBean> setHotValue(Map<String, Long> category, ServerDTO dto, String query) {
        List<OmDataBean> resultList = new ArrayList<>();
        HybaseQueryWrapper wrapper = new HybaseQueryWrapper();
        wrapper.setMappingId("OmDataBean.search");
        wrapper.setHybaseSorts(HybaseSort.descs("SY_MEDIA_RANK", "IR_URLTIME"));
        wrapper.setOffsetLimit(OffsetLimit.of(1, 10));
        Set<String> keySet = category.keySet();
        keySet.forEach(key -> {
            String syMd5TAGTitle = HybaseQuery.eq("SY_MD5TAG_TITLE", key);
            String cond = HybaseQuery.andMerge(query, syMd5TAGTitle);
            wrapper.setQuery(cond);
            HybasePage<OmDataBean> page = template.select(dto, wrapper, OmDataBean.class);
            OmDataBean omDataBean = page.getRecords().get(0);
            omDataBean.setHotValue(category.get(key));
            resultList.add(omDataBean);
        });
        return resultList;
    }
}

