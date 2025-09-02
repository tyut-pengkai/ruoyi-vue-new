package com.ruoyi.xkt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.xkt.domain.UserSearchHistory;
import com.ruoyi.xkt.dto.useSearchHistory.UserSearchHistoryDTO;
import com.ruoyi.xkt.mapper.UserSearchHistoryMapper;
import com.ruoyi.xkt.service.IUserSearchHistoryService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
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
    final UserSearchHistoryMapper userSearchHisMapper;

    @Override
    @Transactional(readOnly = true)
    public List<String> recordList() {
        Long userId = SecurityUtils.getUserIdSafe();
        if (ObjectUtils.isEmpty(userId)) {
            return Collections.emptyList();
        }
        List<UserSearchHistoryDTO> redisList = this.redisCache.getCacheObject(CacheConstants.USER_SEARCH_HISTORY + userId);
        if (CollectionUtils.isEmpty(redisList)) {
            return Collections.emptyList();
        }
        // 按照搜索时间倒序排列，最新的搜索数据展示在最前面
        return redisList.stream().sorted(Comparator.comparing(UserSearchHistoryDTO::getSearchTime).reversed())
                .map(UserSearchHistoryDTO::getSearchContent).collect(Collectors.toList());
    }

    /**
     * 清空用户搜索历史
     *
     * @return Integer
     */
    @Override
    @Transactional
    public Integer clearSearchHisRecord() {
        Long userId = SecurityUtils.getUserIdSafe();
        if (ObjectUtils.isEmpty(userId)) {
            return 0;
        }
        this.redisCache.deleteObject(CacheConstants.USER_SEARCH_HISTORY + userId);
        // 获取用户的浏览历史
        List<UserSearchHistory> searchHisList = this.userSearchHisMapper.selectList(new LambdaQueryWrapper<UserSearchHistory>()
                .eq(UserSearchHistory::getUserId, userId).eq(UserSearchHistory::getDelFlag, Constants.UNDELETED));
        if (CollectionUtils.isEmpty(searchHisList)) {
            return 0;
        }
        searchHisList.forEach(x -> x.setDelFlag(Constants.DELETED));
        return this.userSearchHisMapper.updateById(searchHisList).size();
    }

}
