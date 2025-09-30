package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.xkt.domain.Store;
import com.ruoyi.xkt.domain.StoreMember;
import com.ruoyi.xkt.dto.advertRound.AdRoundStoreBoughtResDTO;
import com.ruoyi.xkt.dto.storeMember.StoreMemberCreateDTO;
import com.ruoyi.xkt.dto.storeMember.StoreMemberExpireResDTO;
import com.ruoyi.xkt.dto.storeMember.StoreMemberPageDTO;
import com.ruoyi.xkt.dto.storeMember.StoreMemberPageResDTO;
import com.ruoyi.xkt.enums.NoticeOwnerType;
import com.ruoyi.xkt.enums.NoticeType;
import com.ruoyi.xkt.enums.StoreMemberLevel;
import com.ruoyi.xkt.enums.UserNoticeType;
import com.ruoyi.xkt.mapper.StoreMapper;
import com.ruoyi.xkt.mapper.StoreMemberMapper;
import com.ruoyi.xkt.service.IAssetService;
import com.ruoyi.xkt.service.INoticeService;
import com.ruoyi.xkt.service.IStoreMemberService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
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
    final IAssetService assetService;
    final INoticeService noticeService;
    final StoreMapper storeMapper;

    /**
     * 档口购买会员
     *
     * @param createDTO 新增入参
     * @return Integer
     */
    @Override
    @Transactional
    public Integer create(StoreMemberCreateDTO createDTO) {
        // 用户是否为档口管理者或子账户
        if (!SecurityUtils.isAdmin()) {
            throw new ServiceException("当前用户非管理员账号，无权限操作!", HttpStatus.ERROR);
        }
        Optional.ofNullable(createDTO.getStoreId()).orElseThrow(() -> new RuntimeException("档口ID不能为空!"));
        Optional.ofNullable(createDTO.getPayPrice()).orElseThrow(() -> new RuntimeException("购买金额不能为空!"));
        StoreMember storeMember = new StoreMember();
        storeMember.setStoreId(createDTO.getStoreId());
        // 最低等级会员：实力质造
        storeMember.setLevel(StoreMemberLevel.STRENGTH_CONSTRUCT.getValue());
        storeMember.setStartTime(java.sql.Date.valueOf(LocalDate.now()));
        // 过期时间设置为1年后
        storeMember.setEndTime(java.sql.Date.valueOf(LocalDate.now().plusYears(1)));
        storeMember.setVoucherDate(java.sql.Date.valueOf(LocalDate.now()));
        storeMember.setCreateBy(SecurityUtils.getUsername());
        int count = this.storeMemberMapper.insert(storeMember);
        // 将档口权重增加1
        Store store = Optional.ofNullable(this.storeMapper.selectOne(new LambdaQueryWrapper<Store>()
                        .eq(Store::getId, createDTO.getStoreId()).eq(Store::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("档口不存在!", HttpStatus.ERROR));
        store.setStoreWeight(ObjectUtils.defaultIfNull(store.getStoreWeight(), 0) + 1);
        this.storeMapper.updateById(store);
        // 将档口会员信息添加到 redis 中
        redisCache.setCacheObject(CacheConstants.STORE_MEMBER + createDTO.getStoreId(), storeMember);
        // 新增订购成功的消息通知
        this.noticeService.createSingleNotice(SecurityUtils.getUserId(), "购买会员成功!", NoticeType.NOTICE.getValue(), NoticeOwnerType.SYSTEM.getValue(),
                createDTO.getStoreId(), UserNoticeType.SYSTEM_MSG.getValue(), "恭喜您！购买:实力质造 会员成功!");
        // 扣除会员费
        assetService.payVipFee(createDTO.getStoreId(), createDTO.getPayPrice());
        return count;
    }

    /**
     * 获取档口会员列表
     *
     * @param pageDTO 档口会员列表入参
     * @return Page<StoreMemberPageResDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StoreMemberPageResDTO> page(StoreMemberPageDTO pageDTO) {
        // 用户是否为档口管理者或子账户
        if (!SecurityUtils.isAdmin()) {
            throw new ServiceException("当前用户非管理员账号，无权限操作!", HttpStatus.ERROR);
        }
        PageHelper.startPage(pageDTO.getPageNum(), pageDTO.getPageSize());
        List<StoreMemberPageResDTO> list = this.storeMemberMapper.selectStoreMemberPage(pageDTO);
        return CollectionUtils.isEmpty(list) ? Page.empty(pageDTO.getPageSize(), pageDTO.getPageNum())
                : Page.convert(new PageInfo<>(list));
    }

    /**
     * 获取档口会员过期时间
     *
     * @param storeId 档口ID
     * @return StoreMemberExpireResDTO
     */
    @Override
    @Transactional(readOnly = true)
    public StoreMemberExpireResDTO expire(Long storeId) {
        StoreMember storeMember = this.storeMemberMapper.selectOne(new LambdaQueryWrapper<StoreMember>()
                .eq(StoreMember::getStoreId, storeId).eq(StoreMember::getDelFlag, Constants.UNDELETED)
                .gt(StoreMember::getStartTime, new Date()).le(StoreMember::getEndTime, new Date()));
        return BeanUtil.toBean(storeMember, StoreMemberExpireResDTO.class);
    }


}
