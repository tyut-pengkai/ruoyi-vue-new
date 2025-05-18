package com.ruoyi.xkt.service;

import com.ruoyi.common.core.page.Page;
import com.ruoyi.xkt.dto.advertRound.pc.*;
import com.ruoyi.xkt.dto.es.ESProductDTO;
import com.ruoyi.xkt.dto.website.IndexSearchDTO;

import java.io.IOException;
import java.util.List;

/**
 * 档口销售出库Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IWebsiteService {

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

}
