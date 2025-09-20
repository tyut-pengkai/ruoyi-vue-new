package com.ruoyi.xkt.service;

import com.ruoyi.xkt.dto.StoreProductStatistics.StoreProdAppViewRankResDTO;

/**
 * 档口商品统计 服务层
 *
 * @author ruoyi
 */
public interface IStoreProductStatisticsService {

    /**
     * 档口商品访问榜
     *
     * @return StoreProdAppViewRankResDTO
     */
    StoreProdAppViewRankResDTO getAppViewRank();
}
