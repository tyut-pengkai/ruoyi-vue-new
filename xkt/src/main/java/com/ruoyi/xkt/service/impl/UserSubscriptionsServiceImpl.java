package com.ruoyi.xkt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.xkt.domain.Store;
import com.ruoyi.xkt.domain.StoreProduct;
import com.ruoyi.xkt.domain.StoreSale;
import com.ruoyi.xkt.domain.UserSubscriptions;
import com.ruoyi.xkt.dto.userSubscriptions.UserSubscDTO;
import com.ruoyi.xkt.dto.userSubscriptions.UserSubscDeleteDTO;
import com.ruoyi.xkt.dto.userSubscriptions.UserSubscPageDTO;
import com.ruoyi.xkt.dto.userSubscriptions.UserSubscPageResDTO;
import com.ruoyi.xkt.enums.NoticeOwnerType;
import com.ruoyi.xkt.enums.NoticeType;
import com.ruoyi.xkt.enums.UserNoticeType;
import com.ruoyi.xkt.mapper.StoreMapper;
import com.ruoyi.xkt.mapper.StoreProductMapper;
import com.ruoyi.xkt.mapper.StoreSaleMapper;
import com.ruoyi.xkt.mapper.UserSubscriptionsMapper;
import com.ruoyi.xkt.service.INoticeService;
import com.ruoyi.xkt.service.IUserSubscriptionsService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 用户关注档口Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@RequiredArgsConstructor
@Service
public class UserSubscriptionsServiceImpl implements IUserSubscriptionsService {

    final UserSubscriptionsMapper userSubMapper;
    final StoreSaleMapper saleMapper;
    final StoreProductMapper storeProdMapper;
    final INoticeService noticeService;
    final StoreMapper storeMapper;


    /**
     * 新增用户关注档口
     *
     * @param subscDTO 新增用户关注档口入参
     * @return Integer
     */
    @Override
    @Transactional
    public Integer create(UserSubscDTO subscDTO) {
        // 获取当前登录用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        // 判断是否已关注过
        UserSubscriptions exist = this.userSubMapper.selectOne(new LambdaQueryWrapper<UserSubscriptions>()
                .eq(UserSubscriptions::getUserId, loginUser.getUserId()).eq(UserSubscriptions::getStoreId, subscDTO.getStoreId())
                .eq(UserSubscriptions::getDelFlag, Constants.UNDELETED));
        if (ObjectUtils.isNotEmpty(exist)) {
            throw new ServiceException("已关注过当前档口!", HttpStatus.ERROR);
        }
        UserSubscriptions userSubscriptions = new UserSubscriptions();
        userSubscriptions.setUserId(loginUser.getUserId());
        userSubscriptions.setStoreId(subscDTO.getStoreId());
        int count = this.userSubMapper.insert(userSubscriptions);
        Store store = Optional.ofNullable(this.storeMapper.selectOne(new LambdaQueryWrapper<Store>()
                        .eq(Store::getId, subscDTO.getStoreId()).eq(Store::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("档口不存在!", HttpStatus.ERROR));
        // 新增用户关注档口消息通知
        this.noticeService.createSingleNotice(SecurityUtils.getUserId(), "关注档口成功!", NoticeType.NOTICE.getValue(), NoticeOwnerType.SYSTEM.getValue(),
                subscDTO.getStoreId(), UserNoticeType.FOCUS_STORE.getValue(), "恭喜您，关注档口: " + store.getStoreName() + "成功!");
        return count;
    }

    /**
     * 用户批量取消关注档口
     *
     * @param deleteDTO 取消关注档口入参
     * @return Integer
     */
    @Override
    @Transactional
    public Integer delete(UserSubscDeleteDTO deleteDTO) {
        // 获取当前登录用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        List<UserSubscriptions> list = Optional.ofNullable(this.userSubMapper.selectList(new LambdaQueryWrapper<UserSubscriptions>()
                        .eq(UserSubscriptions::getUserId, loginUser.getUserId())
                        .in(UserSubscriptions::getStoreId, deleteDTO.getStoreIdList())
                        .eq(UserSubscriptions::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("用户关注档口不存在!", HttpStatus.ERROR));
        list.forEach(x -> x.setDelFlag(Constants.DELETED));
        int count = this.userSubMapper.updateById(list).size();
        Map<Long, Store> storeMap = this.storeMapper.selectList(new LambdaQueryWrapper<Store>()
                        .in(Store::getId, deleteDTO.getStoreIdList()).eq(Store::getDelFlag, Constants.UNDELETED)).stream()
                .collect(Collectors.toMap(Store::getId, Function.identity()));
        list.forEach(x -> {
            Store store = storeMap.get(x.getStoreId());
            // 用户取消关注档口消息通知
            this.noticeService.createSingleNotice(SecurityUtils.getUserId(), "取消关注档口!", NoticeType.NOTICE.getValue(), NoticeOwnerType.SYSTEM.getValue(),
                    store.getId(), UserNoticeType.FOCUS_STORE.getValue(), "您已取消关注档口: " + store.getStoreName() + " !");
        });
        return count;
    }

    /**
     * 用户关注档口列表
     *
     * @param pageDTO 查询入参
     * @return Page<UserSubscPageResDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserSubscPageResDTO> page(UserSubscPageDTO pageDTO) {
        // 获取当前登录用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        PageHelper.startPage(pageDTO.getPageNum(), pageDTO.getPageSize());
        List<UserSubscPageResDTO> list = this.userSubMapper.selectUserSubscPage(loginUser.getUserId(), pageDTO.getStoreName());
        if (CollectionUtils.isEmpty(list)) {
            return Page.empty(pageDTO.getPageNum(), pageDTO.getPageSize());
        }
        // 今天
        final Date now = java.sql.Date.valueOf(LocalDate.now());
        // 30天前
        final Date before30Days = java.sql.Date.valueOf(LocalDate.now().minusDays(30));
        // 7天前
        final Date before7Days = java.sql.Date.valueOf(LocalDate.now().minusDays(7));
        final List<Long> storeIdList = list.stream().map(UserSubscPageResDTO::getStoreId).collect(Collectors.toList());
        List<StoreSale> saleList = this.saleMapper.selectList(new LambdaQueryWrapper<StoreSale>()
                .in(StoreSale::getStoreId, storeIdList).eq(StoreSale::getDelFlag, Constants.UNDELETED)
                .between(StoreSale::getVoucherDate, before30Days, now));
        // 最近30天销售数据
        Map<Long, Long> saleQuantityMap = saleList.stream().collect(Collectors.groupingBy(StoreSale::getStoreId, Collectors.summingLong(StoreSale::getSaleQuantity)));
        List<StoreProduct> storeProdList = this.storeProdMapper.selectList(new LambdaQueryWrapper<StoreProduct>()
                .eq(StoreProduct::getDelFlag, Constants.UNDELETED).in(StoreProduct::getStoreId, storeIdList)
                .between(StoreProduct::getCreateTime, before7Days, now));
        // 最近7天上新数据
        Map<Long, Long> newProdQuantityMap = storeProdList.stream().collect(Collectors.groupingBy(StoreProduct::getStoreId, Collectors.summingLong(StoreProduct::getId)));
        list.forEach(x -> x.setLast30DaysSaleQuantity(saleQuantityMap.getOrDefault(x.getStoreId(), 0L))
                .setLast7DaysNewProdQuantity(newProdQuantityMap.getOrDefault(x.getStoreId(), 0L)));
        return Page.convert(new PageInfo<>(list));
    }

}
