package com.ruoyi.xkt.service;

import com.ruoyi.common.core.page.Page;
import com.ruoyi.xkt.dto.advertRound.pc.PCDownloadDTO;
import com.ruoyi.xkt.dto.advertRound.pc.PCSearchDTO;
import com.ruoyi.xkt.dto.advertRound.pc.PCUserCenterDTO;
import com.ruoyi.xkt.dto.advertRound.pc.index.*;
import com.ruoyi.xkt.dto.advertRound.pc.newProd.*;
import com.ruoyi.xkt.dto.advertRound.pc.store.PCStoreMidBannerDTO;
import com.ruoyi.xkt.dto.advertRound.pc.store.PCStoreRecommendDTO;
import com.ruoyi.xkt.dto.advertRound.pc.store.PCStoreTopBannerDTO;
import com.ruoyi.xkt.dto.advertRound.picSearch.PicSearchAdvertDTO;
import com.ruoyi.xkt.dto.es.ESProductDTO;
import com.ruoyi.xkt.dto.website.IndexSearchDTO;
import com.ruoyi.xkt.dto.website.StoreSearchDTO;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * 系统广告位置及搜索页
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IWebsitePCService {

    /**
     * 网站首页搜索
     *
     * @param searchDTO 查询入参
     */
    Page<ESProductDTO> search(IndexSearchDTO searchDTO) throws IOException;

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
     * PC 首页 固定挂耳
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

    /**
     * PC 新品馆 顶部左侧 轮播图
     *
     * @return List<PCNewTopLeftBannerDTO>
     */
    List<PCNewTopLeftBannerDTO> getPcNewTopLeftBanner();

    /**
     * PC 新品馆 顶部右侧 轮播图
     *
     * @return PCNewTopRightDTO
     */
    PCNewTopRightDTO getPcNewTopRight();

    /**
     * PC 新品馆 品牌馆
     *
     * @return List<PCNewMidBrandDTO>
     */
    List<PCNewMidBrandDTO> getPcNewMidBrandList();

    /**
     * PC 新品馆 热卖榜左侧
     *
     * @return List<PCNewMidHotLeftDTO>
     */
    List<PCNewMidHotLeftDTO> getPcNewMidHotLeftList();

    /**
     * PC 新品馆 热卖榜右侧商品
     *
     * @return List<PCNewMidHotRightDTO>
     */
    List<PCNewMidHotRightDTO> getPcNewMidHotRightList();

    /**
     * PC 新品馆 底部横幅
     *
     * @return List<PCNewBottomBannerDTO>
     */
    List<PCNewBottomBannerDTO> getPcNewBottomBannerList();

    /**
     * PC 档口馆 顶部横幅及商品
     *
     * @return List<PCStoreTopBannerDTO>
     */
    List<PCStoreTopBannerDTO> getPcStoreTopBannerList();

    /**
     * PC 档口馆 中间横幅
     *
     * @return List<PCStoreMidBannerDTO>
     */
    List<PCStoreMidBannerDTO> getPcStoreMidBannerList();

    /**
     * 以图搜款 广告
     *
     * @return List<PicSearchAdvertDTO>
     */
    List<PicSearchAdvertDTO> getPicSearchList();

    /**
     * PC 用户中心
     *
     * @return List<PCUserCenterDTO>
     */
    List<PCUserCenterDTO> getPcUserCenterList();

    /**
     * PC 下载页
     *
     * @return List<PCDownloadDTO>
     */
    List<PCDownloadDTO> getPcDownloadList();

    /**
     * PC 首页 为你推荐
     *
     * @param searchDTO 搜索入参
     * @return List<PCIndexRecommendProdDTO>
     */
    Page<PCIndexRecommendDTO> pcIndexRecommendPage(IndexSearchDTO searchDTO) throws IOException;

    /**
     * PC 新品馆 为你推荐
     *
     * @param searchDTO 搜索入参
     * @return Page<PCNewRecommendDTO>
     */
    Page<PCNewRecommendDTO> pcNewProdRecommendPage(IndexSearchDTO searchDTO) throws IOException;

    /**
     * PC 搜索结果
     *
     * @param searchDTO 搜索入参
     * @return Page<PCSearchDTO>
     */
    Page<PCSearchDTO> psSearchPage(IndexSearchDTO searchDTO) throws IOException;

    /**
     * PC 档口馆 档口列表
     *
     * @param searchDTO 搜索入参
     * @return Page<PCStoreRecommendDTO>
     */
    Page<PCStoreRecommendDTO> pcStoreRecommendPage(StoreSearchDTO searchDTO);

}
