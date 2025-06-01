package com.ruoyi.xkt.service.impl;

import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.xkt.dto.useSearchHistory.UserSearchHistoryDTO;
import com.ruoyi.xkt.service.IUserSearchHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户浏览历史Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
@RequiredArgsConstructor
public class UserSearchHistoryServiceImpl implements IUserSearchHistoryService {

    final RedisCache redisCache;

    @Override
    @Transactional(readOnly = true)
    public List<String> recordList() {
        List<UserSearchHistoryDTO> redisList = this.redisCache.getCacheObject(CacheConstants.USER_SEARCH_HISTORY + SecurityUtils.getUserId());
        // 按照搜索时间倒序排列，最新的搜索数据展示在最前面
        return redisList.stream().sorted(Comparator.comparing(UserSearchHistoryDTO::getSearchTime).reversed())
                .map(UserSearchHistoryDTO::getSearchContent).collect(Collectors.toList());
    }

}
