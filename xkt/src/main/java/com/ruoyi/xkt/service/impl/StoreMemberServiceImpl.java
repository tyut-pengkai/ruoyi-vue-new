package com.ruoyi.xkt.service.impl;

import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.xkt.domain.StoreMember;
import com.ruoyi.xkt.enums.StoreMemberLevel;
import com.ruoyi.xkt.mapper.StoreMemberMapper;
import com.ruoyi.xkt.service.IStoreMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

/**
 * 推广营销Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
@RequiredArgsConstructor
public class StoreMemberServiceImpl implements IStoreMemberService {

    final StoreMemberMapper storeMemberMapper;
    final RedisCache redisCache;

    /**
     * 档口购买会员
     *
     * @param storeId 档口ID
     * @return Integer
     */
    @Override
    @Transactional
    public Integer create(Long storeId) {
        Optional.ofNullable(storeId).orElseThrow(() -> new RuntimeException("档口ID不能为空"));
        StoreMember storeMember = new StoreMember();
        storeMember.setStoreId(storeId);
        // 最低等级会员：实力质造
        storeMember.setLevel(StoreMemberLevel.STRENGTH_CONSTRUCT.getValue());
        storeMember.setStartTime(java.sql.Date.valueOf(LocalDate.now()));
        // 过期时间设置为1年后
        storeMember.setEndTime(java.sql.Date.valueOf(LocalDate.now().plusYears(1)));
        storeMember.setCreateBy(SecurityUtils.getUsername());
        int count = this.storeMemberMapper.insert(storeMember);
        // 将档口会员信息添加到 redis 中
        redisCache.setCacheObject(CacheConstants.STORE_MEMBER + storeId, StoreMemberLevel.STRENGTH_CONSTRUCT.getValue());
        return count;
    }

}
