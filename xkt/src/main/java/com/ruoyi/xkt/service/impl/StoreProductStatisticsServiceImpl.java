package com.ruoyi.xkt.service.impl;

import com.ruoyi.xkt.dto.StoreProductStatistics.StoreProdAppViewRankResDTO;
import com.ruoyi.xkt.mapper.StoreProductStatisticsMapper;
import com.ruoyi.xkt.service.IStoreProductStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;

/**
 * 档口商品统计 服务层实现
 *
 * @author ruoyi
 */
@Service
@RequiredArgsConstructor
public class StoreProductStatisticsServiceImpl implements IStoreProductStatisticsService {

    final StoreProductStatisticsMapper prodStatisticsMapper;

    /**
     * 档口商品访问榜
     *
     * @return StoreProdAppViewRankResDTO
     */
    @Override
    @Transactional(readOnly = true)
    public StoreProdAppViewRankResDTO getAppViewRank() {
        final Date yesterday = java.sql.Date.valueOf(LocalDate.now().minusDays(1));
        final Date threeMonthAgo = java.sql.Date.valueOf(LocalDate.now().minusDays(1).minusMonths(3));
        return new StoreProdAppViewRankResDTO().setViewCountList(prodStatisticsMapper.selectTop10ProdViewCount(threeMonthAgo, yesterday));
    }

}
