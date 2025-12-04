package com.ruoyi.xkt.service.impl;

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
import com.ruoyi.xkt.dto.storeMember.StoreMemberAuditDTO;
import com.ruoyi.xkt.dto.storeMember.StoreMemberCreateDTO;
import com.ruoyi.xkt.dto.storeMember.StoreMemberPageDTO;
import com.ruoyi.xkt.dto.storeMember.StoreMemberPageResDTO;
import com.ruoyi.xkt.enums.*;
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
import java.time.ZoneId;
import java.util.*;

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
        if (!SecurityUtils.isAdmin() && !SecurityUtils.isStoreManagerOrSub(createDTO.getStoreId())) {
            throw new ServiceException("当前用户非管理员账号，无权限操作!", HttpStatus.ERROR);
        }
        //校验推广支付方式是否存在
        AdPayWay.of(createDTO.getPayWay());
        // 校验使用余额情况下，密码是否正确
        if (Objects.equals(createDTO.getPayWay(), AdPayWay.BALANCE.getValue())
                && !assetService.checkTransactionPassword(createDTO.getStoreId(), createDTO.getTransactionPassword())) {
            throw new ServiceException("支付密码错误!请重新输入", HttpStatus.ERROR);
        }
        Optional.ofNullable(createDTO.getStoreId()).orElseThrow(() -> new RuntimeException("档口ID不能为空!"));
        Optional.ofNullable(createDTO.getPayPrice()).orElseThrow(() -> new RuntimeException("购买金额不能为空!"));
        // 看是否已存在会员
        StoreMember storeMember = this.storeMemberMapper.selectOne(new LambdaQueryWrapper<StoreMember>()
                .eq(StoreMember::getStoreId, createDTO.getStoreId()).eq(StoreMember::getMemberStatus, StoreMemberStatus.AUDIT_PASS.getValue())
                .eq(StoreMember::getDelFlag, Constants.UNDELETED));
        if (ObjectUtils.isNotEmpty(storeMember)) {
            // 将状态置为 2 已购买待审核
            storeMember.setMemberStatus(StoreMemberStatus.BOUGHT_WAIT_AUDIT.getValue());
        } else {
            storeMember = new StoreMember();
            storeMember.setStoreId(createDTO.getStoreId());
            // 将状态置为 1 未购买待审核
            storeMember.setMemberStatus(StoreMemberStatus.WAIT_AUDIT.getValue());
            storeMember.setCreateBy(SecurityUtils.getUsername());
        }
        storeMember.setVoucherDate(java.sql.Date.valueOf(LocalDate.now()));
        storeMember.setPayPrice(createDTO.getPayPrice());
        this.storeMemberMapper.insertOrUpdate(storeMember);
        // 购买时就扣除会员费
        assetService.payVipFee(storeMember.getStoreId(), storeMember.getPayPrice());
        return 1;
    }

    /**
     * 购买档口会员 免费版
     *
     * @param storeId 档口ID
     * @return Integer
     */
    @Override
    @Transactional
    public Integer createNoMoney(Long storeId) {
        // 用户是否为超级管理员
        if (!SecurityUtils.isSuperAdmin()) {
            throw new ServiceException("当前用户非超级管理员，无权限操作!", HttpStatus.ERROR);
        }
        // 看是否已存在会员
        StoreMember storeMember = this.storeMemberMapper.selectOne(new LambdaQueryWrapper<StoreMember>()
                .eq(StoreMember::getStoreId, storeId).eq(StoreMember::getMemberStatus, StoreMemberStatus.AUDIT_PASS.getValue())
                .eq(StoreMember::getDelFlag, Constants.UNDELETED));
        int count;
        // 已存在会员，则在之前的基础上续期
        if (ObjectUtils.isNotEmpty(storeMember)) {
            // 续期结束时间在原来基础上再加一年
            Date memberEndTime = storeMember.getEndTime();
            // 直接增加一年
            Date memberEndTimePlus = Date.from(memberEndTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                    .plusYears(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
            storeMember.setEndTime(memberEndTimePlus);
            storeMember.setUpdateBy(SecurityUtils.getUsername());
            count = this.storeMemberMapper.updateById(storeMember);
        } else {
            storeMember = new StoreMember();
            storeMember.setStoreId(storeId);
            // 最低等级会员：实力质造
            storeMember.setLevel(StoreMemberLevel.STRENGTH_CONSTRUCT.getValue());
            storeMember.setStartTime(java.sql.Date.valueOf(LocalDate.now()));
            // 过期时间设置为1年后
            storeMember.setEndTime(java.sql.Date.valueOf(LocalDate.now().plusYears(1)));
            storeMember.setVoucherDate(java.sql.Date.valueOf(LocalDate.now()));
            storeMember.setCreateBy(SecurityUtils.getUsername());
            count = this.storeMemberMapper.insert(storeMember);
            // 将档口权重增加1
            Store store = Optional.ofNullable(this.storeMapper.selectOne(new LambdaQueryWrapper<Store>()
                            .eq(Store::getId, storeId).eq(Store::getDelFlag, Constants.UNDELETED)))
                    .orElseThrow(() -> new ServiceException("档口不存在!", HttpStatus.ERROR));
            store.setStoreWeight(ObjectUtils.defaultIfNull(store.getStoreWeight(), 0) + 1);
            this.storeMapper.updateById(store);
            // 将档口会员信息添加到 redis 中
            redisCache.setCacheObject(CacheConstants.STORE_MEMBER + storeId, storeMember);
        }
        return count;
    }

    /**
     * 审核档口会员
     *
     * @param auditDTO 档口会员审核入参
     * @return Integer
     */
    @Override
    @Transactional
    public Integer audit(StoreMemberAuditDTO auditDTO) {
        // 用户是否为超级管理员
        if (!SecurityUtils.isSuperAdmin()) {
            throw new ServiceException("当前用户非超级管理员，无权限操作!", HttpStatus.ERROR);
        }
        StoreMember storeMember = Optional.ofNullable(this.storeMemberMapper.selectOne(new LambdaQueryWrapper<StoreMember>()
                        .eq(StoreMember::getId, auditDTO.getStoreMemberId()).eq(StoreMember::getDelFlag, Constants.UNDELETED)
                        .in(StoreMember::getMemberStatus, Arrays.asList(StoreMemberStatus.WAIT_AUDIT.getValue(), StoreMemberStatus.BOUGHT_WAIT_AUDIT.getValue()))))
                .orElseThrow(() -> new ServiceException("购买会员记录不存在!", HttpStatus.ERROR));
        if (Objects.equals(auditDTO.getMemberStatus(), StoreMemberStatus.AUDIT_PASS.getValue())) {
            // 未购买待审核
            if (Objects.equals(storeMember.getMemberStatus(), StoreMemberStatus.WAIT_AUDIT.getValue())) {
                // 若结束时间在当前时间之前，则直接设置为当前时间
                Date startTime = ObjectUtils.isEmpty(storeMember.getEndTime()) ? new Date()
                        : (storeMember.getEndTime().after(new Date()) ? storeMember.getEndTime() : new Date());
                // 直接增加一年
                Date endTime = Date.from(startTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                        .plusYears(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
                storeMember.setStartTime(startTime);
                storeMember.setEndTime(endTime);
                // 已购买待审核（续费状态）
            } else if (Objects.equals(storeMember.getMemberStatus(), StoreMemberStatus.BOUGHT_WAIT_AUDIT.getValue())) {
                Date oldEndTime = ObjectUtils.isEmpty(storeMember.getEndTime()) ? new Date() : storeMember.getEndTime();
                // 直接增加一年
                Date endTime = Date.from(oldEndTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                        .plusYears(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
                storeMember.setEndTime(endTime);
            }
            storeMember.setUpdateBy(SecurityUtils.getUsername());
            storeMember.setMemberStatus(StoreMemberStatus.AUDIT_PASS.getValue());
            // 最低等级会员：实力质造
            storeMember.setLevel(StoreMemberLevel.STRENGTH_CONSTRUCT.getValue());
            storeMember.setVoucherDate(java.sql.Date.valueOf(LocalDate.now()));
            // 将档口权重增加1
            Store store = Optional.ofNullable(this.storeMapper.selectOne(new LambdaQueryWrapper<Store>()
                            .eq(Store::getId, storeMember.getStoreId()).eq(Store::getDelFlag, Constants.UNDELETED)))
                    .orElseThrow(() -> new ServiceException("档口不存在!", HttpStatus.ERROR));
            store.setStoreWeight(ObjectUtils.defaultIfNull(store.getStoreWeight(), 0) + 1);
            this.storeMapper.updateById(store);
            // 将档口购买金额置为null
            this.storeMapper.updateMemberAmountNull(store.getId());
            // 将档口会员信息添加到 redis 中
            redisCache.setCacheObject(CacheConstants.STORE_MEMBER + storeMember.getStoreId(), storeMember);
            // 新增订购成功的消息通知
            this.noticeService.createSingleNotice(SecurityUtils.getUserId(), "购买会员成功!", NoticeType.NOTICE.getValue(), NoticeOwnerType.SYSTEM.getValue(),
                    storeMember.getStoreId(), UserNoticeType.SYSTEM_MSG.getValue(), "恭喜您！购买:实力质造 会员成功!");
        } else {
            // 如果审核驳回，则直接将该笔审核置为无效
            storeMember.setDelFlag(Constants.DELETED);
            // 退会员费（购买时就支付了，审核驳回就直接退费）
            this.assetService.refundVipFee(storeMember.getStoreId(), storeMember.getPayPrice());
            // 新增购买驳回的消息通知
            this.noticeService.createSingleNotice(SecurityUtils.getUserId(), "购买会员驳回!", NoticeType.NOTICE.getValue(), NoticeOwnerType.SYSTEM.getValue(),
                    storeMember.getStoreId(), UserNoticeType.SYSTEM_MSG.getValue(), "您购买:实力质造 会员驳回!请联系客服18228090035");
        }
        return this.storeMemberMapper.updateById(storeMember);
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
        // 用户是否为超级管理员
        if (!SecurityUtils.isAdmin()) {
            throw new ServiceException("当前用户非管理员账号，无权限操作!", HttpStatus.ERROR);
        }
        PageHelper.startPage(pageDTO.getPageNum(), pageDTO.getPageSize());
        List<StoreMemberPageResDTO> list = this.storeMemberMapper.selectStoreMemberPage(pageDTO);
        return CollectionUtils.isEmpty(list) ? Page.empty(pageDTO.getPageSize(), pageDTO.getPageNum())
                : Page.convert(new PageInfo<>(list));
    }


}
