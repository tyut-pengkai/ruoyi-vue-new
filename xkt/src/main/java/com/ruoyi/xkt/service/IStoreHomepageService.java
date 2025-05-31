package com.ruoyi.xkt.service;

import com.ruoyi.xkt.dto.storeHomepage.*;

import java.util.List;

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
    Integer insert(Long storeId, Integer templateNum, StoreHomeDecorationDTO homepageDTO);

    /**
     * 获取档口首页各个部分的图信息
     *
     * @param storeId 档口ID
     * @return StoreHomeDTO
     */
    StoreHomeDecorationResDTO selectByStoreId(Long storeId);

    /**
     * 更新档口首页各部分图信息
     *
     * @param storeId     档口ID
     * @param templateNum 选择的模板Num
     * @param homeDTO     更新的dto
     * @return Integer
     */
    Integer updateStoreHomepage(Long storeId, Integer templateNum, StoreHomeDecorationDTO homeDTO);

    /**
     * 获取档口首页数据
     *
     * @param storeId 档口ID
     * @return StoreHomeResDTO
     */
    StoreHomeResDTO getHomepageInfo(Long storeId);

    /**
     * 首页查询档口商品详情
     *
     * @param storeId     档口ID
     * @param storeProdId 档口商品详情ID
     * @return StoreHomeProdResDTO
     */
    StoreHomeProdResDTO getStoreProdInfo(Long storeId, Long storeProdId);

    /**
     * 获取档口推荐商品列表
     *
     * @param storeId 档口ID
     * @return  List<StoreRecommendResDTO>
     */
    List<StoreRecommendResDTO> getStoreRecommendList(Long storeId);

}
