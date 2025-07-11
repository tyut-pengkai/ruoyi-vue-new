package com.ruoyi.xkt.service;

import com.ruoyi.common.core.page.Page;
import com.ruoyi.xkt.dto.advertRound.app.category.APPCateDTO;
import com.ruoyi.xkt.dto.advertRound.app.index.*;
import com.ruoyi.xkt.dto.advertRound.app.own.APPOwnGuessLikeDTO;
import com.ruoyi.xkt.dto.advertRound.app.prod.APPProdCateSubDTO;
import com.ruoyi.xkt.dto.advertRound.app.prod.APPProdCateTop3DTO;
import com.ruoyi.xkt.dto.advertRound.app.prod.APPProdSaleDTO;
import com.ruoyi.xkt.dto.advertRound.app.strength.APPStrengthProdDTO;
import com.ruoyi.xkt.dto.advertRound.app.strength.APPStrengthStoreDTO;
import com.ruoyi.xkt.dto.es.ESProductDTO;
import com.ruoyi.xkt.dto.website.AppStrengthSearchDTO;
import com.ruoyi.xkt.dto.website.IndexSearchDTO;

import java.io.IOException;
import java.util.List;

/**
 * 系统广告位置及搜索页
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IWebsiteAPPService {

    /**
     * 网站首页搜索
     *
     * @param searchDTO 查询入参
     */
    Page<ESProductDTO> search(IndexSearchDTO searchDTO) throws IOException;

    /**
     * APP 首页顶部轮播图
     *
     * @return List<APPIndexTopBannerDTO>
     */
    List<APPIndexTopBannerDTO> getAppIndexTopBanner();

    /**
     * APP 首页中部品牌好货
     *
     * @return List<APPIndexMidBrandDTO>
     */
    List<APPIndexMidBrandDTO> getAppIndexMidBrand();

    /**
     * APP 首页热卖精选右侧固定位置
     *
     * @return List<APPIndexHotSaleRightFixDTO>
     */
    List<APPIndexHotSaleRightFixDTO> getAppIndexHotSaleRightFix();

    /**
     * APP 分类页
     *
     * @return List<APPCateDTO>
     */
    List<APPCateDTO> getAppCateList();

    /**
     * APP 我的猜你喜欢列表
     *
     * @return List<APPOwnGuessLikeDTO>
     */
    List<APPOwnGuessLikeDTO> getAppOwnGuessLikeList();

    /**
     * APP 首页热卖精选列表
     *
     * @param searchDTO 搜索入参
     * @return List<APPIndexHotSalePageDTO>
     */
    Page<APPIndexHotSaleDTO> appIndexHotSalePage(IndexSearchDTO searchDTO) throws IOException;

    /**
     * APP 首页 人气爆品列表
     *
     * @param searchDTO 搜索入参
     * @return Page<APPIndexPopularSaleDTO>
     */
    Page<APPIndexPopularSaleDTO> appIndexPopularSalePage(IndexSearchDTO searchDTO) throws IOException;

    /**
     * APP 首页 新品榜
     *
     * @param searchDTO 搜索入参
     * @return Page<APPIndexNewProdDTO>
     */
    Page<APPIndexNewProdDTO> appIndexNewProdPage(IndexSearchDTO searchDTO) throws IOException;

    /**
     * APP 搜索列表
     *
     * @param searchDTO 搜索入参
     * @return Page<APPSearchDTO>
     */
    Page<APPSearchDTO> appSearchPage(IndexSearchDTO searchDTO) throws IOException;

    /**
     * APP 实力质造专题页 列表
     *
     * @param searchDTO 搜索入参
     * @return Page<APPStrengthProdDTO>
     */
    Page<APPStrengthProdDTO> getAppStrengthProdPage(IndexSearchDTO searchDTO) throws IOException;

    /**
     * APP 实力质造专题页 档口列表
     *
     * @param searchDTO 搜索入参
     * @return Page<APPStrengthStoreDTO>
     */
    Page<APPStrengthStoreDTO> getAppStrengthStorePage(AppStrengthSearchDTO searchDTO);

    /**
     * APP 商品榜 销量前100 商品列表
     *
     * @return List<APPStrengthStoreDTO>
     */
    List<APPProdSaleDTO> getAppProdSaleTop100List() throws IOException;

    /**
     * APP 商品榜 分类页 销量前3 的列表
     *
     * @return List<APPProdCateTop3DTO>
     */
    List<APPProdCateTop3DTO> getAppCateProdSaleTop3List() throws IOException;

    /**
     * APP 商品榜 分类页 某个分类销量明细
     *
     * @param prodCateId 分类ID
     * @return List<APPProdCateSubDTO>
     */
    List<APPProdCateSubDTO> getAppCateSubProdSaleList(Long prodCateId) throws IOException;
}
