package com.ruoyi.xkt.service;

import com.ruoyi.xkt.dto.storeProductDemandTemplate.StoreDemandTemplateResDTO;
import com.ruoyi.xkt.dto.storeProductDemandTemplate.StoreDemandTemplateUpdateDTO;

/**
 * 档口Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IStoreProductDemandTemplateService {

    /**
     * 获取当前档口设置的模板
     *
     * @param storeId 档口ID
     * @return StoreDemandTemplateResDTO
     */
    StoreDemandTemplateResDTO getTemplate(Long storeId);

    /**
     * 初始化模板
     *
     * @param storeId 档口ID
     * @return 影响行数
     */
    Integer initTemplate(Long storeId);

    /**
     * 更新需求下载模板
     *
     * @param updateDTO 更新入参
     * @return
     */
    Integer updateTemplate(StoreDemandTemplateUpdateDTO updateDTO);

}
