package com.ruoyi.xkt.service;

import com.ruoyi.xkt.dto.advertRoundPlay.AdPlayStoreCreateDTO;
import com.ruoyi.xkt.dto.advertRoundPlay.AdPlayStoreResDTO;

/**
 * 推广营销Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IAdvertRoundPlayService {

    /**
     * 获取当前类型下档口的推广营销数据
     *
     * @param storeId 档口ID
     * @param typeId  推广类型ID
     * @return AdRoundPlayStoreResDTO
     */
    AdPlayStoreResDTO getStoreAdInfo(Long storeId, Integer typeId);

    /**
     * 档口购买推广营销
     *
     * @param createDTO 购买入参
     * @return Integer
     */
    Integer create(AdPlayStoreCreateDTO createDTO);

}
