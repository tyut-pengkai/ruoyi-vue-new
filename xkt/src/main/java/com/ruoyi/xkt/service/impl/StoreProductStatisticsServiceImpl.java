package com.ruoyi.xkt.service.impl;

import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.xkt.dto.StoreProductStatistics.StoreProdAppViewRankResDTO;
import com.ruoyi.xkt.dto.storeProduct.StoreProdPriceAndMainPicAndTagDTO;
import com.ruoyi.xkt.mapper.StoreProductMapper;
import com.ruoyi.xkt.mapper.StoreProductStatisticsMapper;
import com.ruoyi.xkt.service.IStoreProductStatisticsService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 档口商品统计 服务层实现
 *
 * @author ruoyi
 */
@Service
@RequiredArgsConstructor
public class StoreProductStatisticsServiceImpl implements IStoreProductStatisticsService {

    final StoreProductStatisticsMapper prodStatisticsMapper;
    final RedisCache redisCache;
    final StoreProductMapper storeProdMapper;

    /**
     * 档口商品访问榜
     *
     * @return StoreProdAppViewRankResDTO
     */
    @Override
    @Transactional(readOnly = true)
    public StoreProdAppViewRankResDTO getAppViewRank() {
        // 从redis中获取商品访问量
        StoreProdAppViewRankResDTO redisAppViewRank = redisCache.getCacheObject(CacheConstants.STORE_PROD_VIEW_COUNT_CACHE);
        if (ObjectUtils.isNotEmpty(redisAppViewRank)) {
            return redisAppViewRank;
        }
        redisAppViewRank = new StoreProdAppViewRankResDTO();
        final Date yesterday = java.sql.Date.valueOf(LocalDate.now().minusDays(1));
        final Date threeMonthAgo = java.sql.Date.valueOf(LocalDate.now().minusDays(1).minusMonths(3));
        List<StoreProdAppViewRankResDTO.SPAVRViewCountDTO> viewCountList = prodStatisticsMapper.selectTop10ProdViewCount(threeMonthAgo, yesterday);
        if (CollectionUtils.isEmpty(viewCountList)) {
            return redisAppViewRank;
        }
        final List<Long> storeProdIdList = viewCountList.stream().map(StoreProdAppViewRankResDTO.SPAVRViewCountDTO::getStoreProdId).collect(Collectors.toList());
        List<StoreProdPriceAndMainPicAndTagDTO> prodInfoList = this.storeProdMapper.selectPriceAndMainPicAndTagList(storeProdIdList);
        Map<Long, StoreProdPriceAndMainPicAndTagDTO> prodInfoMap = CollectionUtils.isEmpty(prodInfoList) ? new HashMap<>()
                : prodInfoList.stream().collect(Collectors.toMap(StoreProdPriceAndMainPicAndTagDTO::getStoreProdId, v -> v));
        List<StoreProdAppViewRankResDTO.SPAVRViewCountDTO> retViewList = viewCountList.stream()
                .filter(x -> prodInfoMap.containsKey(x.getStoreProdId()))
                .map(x -> {
                    StoreProdPriceAndMainPicAndTagDTO prodInfo = prodInfoMap.get(x.getStoreProdId());
                    return x.setStoreId(prodInfo.getStoreId()).setStoreProdId(x.getStoreProdId()).setStoreName(prodInfo.getStoreName())
                            .setMainPicUrl(prodInfo.getMainPicUrl()).setProdArtNum(prodInfo.getProdArtNum()).setPrice(prodInfo.getMinPrice());
                }).collect(Collectors.toList());
        redisAppViewRank.setViewCountList(retViewList);
        // 放到redis中
        redisCache.setCacheObject(CacheConstants.STORE_PROD_VIEW_COUNT_CACHE, redisAppViewRank, 1, TimeUnit.DAYS);
        return redisAppViewRank;
    }

}
