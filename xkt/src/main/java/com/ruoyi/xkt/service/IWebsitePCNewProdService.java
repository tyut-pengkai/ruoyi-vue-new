package com.ruoyi.xkt.service;

import com.ruoyi.xkt.dto.advertRound.pc.newProd.*;

import java.util.List;

/**
 * 系统广告位置及搜索页
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IWebsitePCNewProdService {

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

}
