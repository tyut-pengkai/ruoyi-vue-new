package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.xkt.dto.userBrowsingHistory.UserBrowHisPageDTO;
import com.ruoyi.xkt.dto.userBrowsingHistory.UserBrowsingHisDTO;
import com.ruoyi.xkt.dto.userBrowsingHistory.UserBrowsingHisPageResDTO;
import com.ruoyi.xkt.service.IUserBrowsingHistoryService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
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
public class UserBrowsingHistoryServiceImpl implements IUserBrowsingHistoryService {

    final RedisCache redisCache;

    /**
     * 查询用户浏览历史分页
     *
     * @param pageDTO 查询入参
     * @return Page<UserBrowsingHisResDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserBrowsingHisPageResDTO> page(UserBrowHisPageDTO pageDTO) {
        // 从redis获取所有的浏览足迹
        List<UserBrowsingHisDTO> redisList = this.redisCache.getCacheObject(CacheConstants.USER_BROWSING_HISTORY + SecurityUtils.getUserId());
        if (CollectionUtils.isEmpty(redisList)) {
            return Page.empty(pageDTO.getPageSize(), pageDTO.getPageNum());
        }
        // 按照浏览时间倒序排
        redisList.sort(Comparator.comparing(UserBrowsingHisDTO::getBrowsingTime).reversed());
        List<UserBrowsingHisPageResDTO> page = redisList.stream().sorted(Comparator.comparing(UserBrowsingHisDTO::getBrowsingTime).reversed())
                .skip((long) (pageDTO.getPageNum() - 1) * pageDTO.getPageSize()).limit(pageDTO.getPageSize())
                .map(x -> BeanUtil.toBean(x, UserBrowsingHisPageResDTO.class))
                .collect(Collectors.toList());
        final long pages = (long) Math.ceil((double) redisList.size() / pageDTO.getPageSize());
        return new Page<>(pageDTO.getPageNum(), pageDTO.getPageSize(), pages, redisList.size(), page);
    }

}
