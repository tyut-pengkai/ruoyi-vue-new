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
     * @param homepageDTO 新增档口首页各部分图
     * @return Integer
     */
    Integer insert(StoreHomeDecorationDTO homepageDTO);

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
     * @param homeDTO 更新的dto
     * @return Integer
     */
    Integer updateStoreHomepage(StoreHomeDecorationDTO homeDTO);

    /**
     * 获取档口推荐商品列表
     *
     * @param storeId 档口ID
     * @return List<StoreRecommendResDTO>
     */
    List<StoreRecommendResDTO> getStoreRecommendList(Long storeId);

    /**
     * 档口首页模板一返回数据
     *
     * @param storeId 档口ID
     * @return StoreHomeTemplateOneResDTO
     */
    StoreHomeTemplateOneResDTO getTemplateOne(Long storeId);

    /**
     * 档口首页模板二返回数据
     *
     * @param storeId 档口ID
     * @return StoreHomeTemplateTwoResDTO
     */
    StoreHomeTemplateTwoResDTO getTemplateTwo(Long storeId);

    /**
     * 档口首页模板三返回数据
     *
     * @param storeId 档口ID
     * @return StoreHomeTemplateThirdResDTO
     */
    StoreHomeTemplateThirdResDTO getTemplateThird(Long storeId);

    /**
     * 档口首页模板四返回数据
     *
     * @param storeId 档口ID
     * @return StoreHomeTemplateFourResDTO
     */
    StoreHomeTemplateFourResDTO getTemplateFour(Long storeId);

    /**
     * 档口首页模板五返回数据
     *
     * @param storeId 档口ID
     * @return StoreHomeTemplateFiveResDTO
     */
    StoreHomeTemplateFiveResDTO getTemplateFive(Long storeId);
}
