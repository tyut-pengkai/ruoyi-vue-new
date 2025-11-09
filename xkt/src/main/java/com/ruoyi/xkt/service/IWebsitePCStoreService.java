package com.ruoyi.xkt.service;

import com.ruoyi.xkt.dto.advertRound.pc.store.PCStoreMidBannerDTO;
import com.ruoyi.xkt.dto.advertRound.pc.store.PCStoreTopBannerDTO;

import java.util.List;

/**
 * 系统广告位置及搜索页
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IWebsitePCStoreService {

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

}
