package com.ruoyi.xkt.service.impl;

import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.xkt.dto.storeProduct.StoreProdViewDTO;
import com.ruoyi.xkt.dto.userIndex.UserOverallResDTO;
import com.ruoyi.xkt.mapper.UserSubscriptionsMapper;
import com.ruoyi.xkt.service.IUserIndexService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户首页Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
@RequiredArgsConstructor
public class UserIndexServiceImpl implements IUserIndexService {

    final UserSubscriptionsMapper userSubsMapper;
    final RedisCache redisCache;

    /**
     * 获取用户首页数据总览
     *
     * @return UserOverallResDTO
     */
    @Override
    @Transactional(readOnly = true)
    public UserOverallResDTO getOverall() {
        UserOverallResDTO overAllDTO = this.userSubsMapper.getOverall(SecurityUtils.getUserId());
        // 图搜热款数量
        List<StoreProdViewDTO> picSearchHotList = redisCache.getCacheObject(CacheConstants.IMG_SEARCH_PRODUCT_HOT);
        return overAllDTO.setSearchHotCount(CollectionUtils.isEmpty(picSearchHotList) ? 0L : picSearchHotList.size());
    }

}
