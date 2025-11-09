package com.ruoyi.xkt.service;

import com.ruoyi.xkt.dto.advertRound.pc.index.*;

import java.util.List;

/**
 * 系统广告位置及搜索页
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IWebsitePCIndexService {

    /**
     * PC 首页顶部通栏
     *
     * @return PCIndexTopBannerDTO
     */
    List<PCIndexTopBannerDTO> getPcIndexTop();

    /**
     * PC 首页 顶部左侧 轮播图
     *
     * @return PCIndexTopLeftBannerDTO
     */
    List<PCIndexTopLeftBannerDTO> getPcIndexTopLeftBanner();

    /**
     * PC 首页 顶部右侧 纵向轮播图
     *
     * @return PCIndexTopRightBannerDTO
     */
    List<PCIndexTopRightBannerDTO> getPcIndexTopRightBanner();

    /**
     * PC 首页 销售榜
     *
     * @return List<PCIndexMidSalesDTO>
     */
    List<PCIndexMidSalesDTO> getPcIndexMidSaleList();

    /**
     * PC 首页 风格榜
     *
     * @return List<PCIndexMidStyleDTO>
     */
    List<PCIndexMidStyleDTO> getPcIndexMidStyleList();

    /**
     * PC 首页 人气榜
     *
     * @return PCIndexBottomPopularDTO
     */
    PCIndexBottomPopularDTO getPcIndexBottomPopularList();

    /**
     * PC 首页 固定侧边栏
     *
     * @return PCIndexFixedEarDTO
     */
    PCIndexFixedEarDTO getPcIndexFixedEar();

    /**
     * 获取搜索框下档口名称
     *
     * @return List<PCIndexSearchUnderlineStoreNameDTO>
     */
    List<PCIndexSearchUnderlineStoreNameDTO> getPcIndexSearchUnderlineStoreName();

    /**
     * 搜索框中推荐商品
     *
     * @return List<PCIndexSearchRecommendProdDTO>
     */
    List<PCIndexSearchRecommendProdDTO> getPcIndexSearchRecommendProd();

}
