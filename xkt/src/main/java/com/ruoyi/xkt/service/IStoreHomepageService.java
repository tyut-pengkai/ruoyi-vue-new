package com.ruoyi.xkt.service;

import com.ruoyi.xkt.dto.storeHomepage.StoreHomeDTO;

/**
 * 档口首页Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IStoreHomepageService {

    /**
     * 新增档口首页各部分图
     *
     * @param storeId     档口ID
     * @param templateNum 选择的模板No
     * @param homepageDTO 新增档口首页各部分图
     * @return Integer
     */
    Integer insert(Long storeId, Integer templateNum, StoreHomeDTO homepageDTO);

    /**
     * 获取档口首页各个部分的图信息
     *
     * @param storeId 档口ID
     * @return StoreHomeDTO
     */
    StoreHomeDTO selectByStoreId(Long storeId);

    /**
     * 更新档口首页各部分图信息
     *
     * @param storeId     档口ID
     * @param templateNum 选择的模板Num
     * @param homeDTO     更新的dto
     * @return Integer
     */
    Integer updateStoreHomepage(Long storeId, Integer templateNum, StoreHomeDTO homeDTO);

}
