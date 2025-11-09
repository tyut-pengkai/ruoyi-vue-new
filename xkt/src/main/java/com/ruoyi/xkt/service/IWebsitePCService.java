package com.ruoyi.xkt.service;

import com.ruoyi.common.core.page.Page;
import com.ruoyi.xkt.dto.advertRound.pc.PCDownloadDTO;
import com.ruoyi.xkt.dto.advertRound.pc.PCSearchDTO;
import com.ruoyi.xkt.dto.advertRound.pc.PCSearchResultAdvertDTO;
import com.ruoyi.xkt.dto.advertRound.pc.PCUserCenterDTO;
import com.ruoyi.xkt.dto.advertRound.pc.index.PCIndexRecommendDTO;
import com.ruoyi.xkt.dto.advertRound.pc.newProd.PCNewRecommendDTO;
import com.ruoyi.xkt.dto.advertRound.pc.store.PCStorePageDTO;
import com.ruoyi.xkt.dto.advertRound.pc.store.PCStoreRecommendDTO;
import com.ruoyi.xkt.dto.advertRound.picSearch.PicSearchAdvertDTO;
import com.ruoyi.xkt.dto.es.ESProductDTO;
import com.ruoyi.xkt.dto.website.IndexSearchDTO;
import com.ruoyi.xkt.dto.website.StoreSearchDTO;

import java.io.IOException;
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

    /**
     * PC 搜索结果广告列表
     *
     * @return List<PCSearchResultAdvertDTO>
     */
    List<PCSearchResultAdvertDTO> getPcSearchAdvertList();

    /**
     * PC档口首页商品列表
     *
     * @param searchDTO 搜索入参
     * @return Page<PCStorePageDTO>
     */
    Page<PCStorePageDTO> psStorePage(IndexSearchDTO searchDTO) throws IOException;

}
