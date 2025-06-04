package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
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
import com.ruoyi.common.utils.bigDecimal.CollectorsUtil;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.xkt.domain.*;
import com.ruoyi.xkt.dto.store.*;
import com.ruoyi.xkt.enums.StoreStatus;
import com.ruoyi.xkt.mapper.*;
import com.ruoyi.xkt.service.IAssetService;
import com.ruoyi.xkt.service.IStoreCertificateService;
import com.ruoyi.xkt.service.IStoreService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 档口Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements IStoreService {

    final StoreMapper storeMapper;
    final IStoreCertificateService storeCertService;
    final SysUserMapper userMapper;
    final DailyStoreTagMapper storeTagMapper;
    final UserSubscriptionsMapper userSubMapper;
    final RedisCache redisCache;
    final IAssetService assetService;
    final DailySaleMapper dailySaleMapper;
    final DailySaleCustomerMapper  dailySaleCusMapper;
    final StoreSaleDetailMapper saleDetailMapper;
    final StoreProductMapper storeProdMapper;
    final DailySaleProductMapper dailySaleProdMapper;


    /**
     * 注册时新增档口数据
     *
     * @param createDTO 档口基础数据
     * @return 结果
     */
    @Override
    @Transactional
    public int create(StoreCreateDTO createDTO) {
        Store store = new Store();
        // 初始化注册时只需绑定用户ID即可
        store.setUserId(createDTO.getUserId());
        // 默认档口状态为：已注册
        store.setStoreStatus(StoreStatus.REGISTERED.getValue());
        // 当前时间往后推1年为试用期时间
        Date oneYearAfter = Date.from(LocalDate.now().plusYears(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        store.setTrialEndTime(oneYearAfter);
        // 设置档口默认权重100
        store.setStoreWeight(Constants.STORE_WEIGHT_DEFAULT);
        int count = this.storeMapper.insert(store);
        // 创建档口账户
        assetService.createInternalAccountIfNotExists(store.getId());
        // 放到redis中
        redisCache.setCacheObject(CacheConstants.STORE_KEY + store.getId(), store.getId());
        return count;
    }

    /**
     * 档口分页数据
     *
     * @param pageDTO 查询入参
     * @return Page<StorePageResDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StorePageResDTO> page(StorePageDTO pageDTO) {
        PageHelper.startPage(pageDTO.getPageNum(), pageDTO.getPageSize());
        List<StorePageResDTO> storeList = this.storeMapper.selectStorePage(pageDTO);
        return Page.convert(new PageInfo<>(storeList));
    }

    /**
     * 更新档口启用/停用状态
     *
     * @param delFlagDTO 入参
     * @return Integer
     */
    @Override
    @Transactional
    public Integer updateDelFlag(StoreUpdateDelFlagDTO delFlagDTO) {
        Store store = Optional.ofNullable(this.storeMapper.selectOne(new LambdaQueryWrapper<Store>()
                        .eq(Store::getId, delFlagDTO.getStoreId())))
                .orElseThrow(() -> new ServiceException("档口不存在!", HttpStatus.ERROR));
        store.setDelFlag(delFlagDTO.getDelFlag() ? Constants.UNDELETED : Constants.DELETED);
        return storeMapper.updateById(store);
    }

    /**
     * 审核档口
     *
     * @param auditDTO 审核入参
     * @return Integer
     */
    @Override
    @Transactional
    public Integer approve(StoreAuditDTO auditDTO) {
        Store store = Optional.ofNullable(this.storeMapper.selectOne(new LambdaQueryWrapper<Store>()
                        .eq(Store::getId, auditDTO.getStoreId()).eq(Store::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("档口不存在!", HttpStatus.ERROR));
        if (auditDTO.getApprove()) {
            // 如果档口状态不为 待审核 或 审核驳回 则报错
            if (!Objects.equals(store.getStoreStatus(), StoreStatus.UN_AUDITED.getValue()) ||
                !Objects.equals(store.getStoreStatus(), StoreStatus.AUDIT_REJECTED.getValue())) {
                throw new ServiceException("当前状态不为待审核 或 审核驳回，不可审核!", HttpStatus.ERROR);
            }
            store.setStoreStatus(StoreStatus.TRIAL_PERIOD.getValue());
        } else {
            store.setStoreStatus(StoreStatus.AUDIT_REJECTED.getValue());
            store.setRejectReason(auditDTO.getRejectReason());
        }
        return this.storeMapper.updateById(store);
    }

    /**
     * 获取档口详细信息
     *
     * @param storeId 档口ID
     * @return StoreBasicResDTO
     */
    @Override
    @Transactional(readOnly = true)
    public StoreResDTO getInfo(Long storeId) {
        Store store = Optional.ofNullable(this.storeMapper.selectOne(new LambdaQueryWrapper<Store>()
                .eq(Store::getId, storeId))).orElseThrow(() -> new ServiceException("档口不存在!", HttpStatus.ERROR));
        StoreResDTO resDTO = BeanUtil.toBean(store, StoreResDTO.class);
        resDTO.setStoreId(storeId);

        // TODO 用户名称
        // TODO 用户名称
        // TODO 用户名称

        // 获取档口绑定的用户
        /*SysUser user = Optional.ofNullable(this.userMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUserId, store.getUserId()))).orElseThrow(() -> new ServiceException("用户不存在!", HttpStatus.ERROR));
        resDTO.setUserName(user.getUserName());*/
        return resDTO;
    }

    /**
     * 审核时获取档口信息
     *
     * @param storeId 档口ID
     * @return StoreApproveResDTO
     */
    @Override
    @Transactional(readOnly = true)
    public StoreApproveResDTO getApproveInfo(Long storeId) {
        return new StoreApproveResDTO(){{
            setBasic(getInfo(storeId));
            setCertificate(storeCertService.getInfo(storeId));
        }};
    }

    /**
     * 获取APP档口基本信息
     *
     * @param storeId 档口ID
     * @return StoreAppResDTO
     */
    @Override
    @Transactional(readOnly = true)
    public StoreAppResDTO getAppInfo(Long storeId) {
        Store store = Optional.ofNullable(this.storeMapper.selectOne(new LambdaQueryWrapper<Store>()
                        .eq(Store::getId, storeId).eq(Store::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("档口不存在!", HttpStatus.ERROR));
        // 获取档口的标签
        List<DailyStoreTag> storeTagList = this.storeTagMapper.selectList(new LambdaQueryWrapper<DailyStoreTag>()
                .eq(DailyStoreTag::getStoreId, storeId).eq(DailyStoreTag::getDelFlag, Constants.UNDELETED)
                .orderByAsc(DailyStoreTag::getType));
        // 判断当前用户是否已关注档口
        UserSubscriptions userSub = this.userSubMapper.selectOne(new LambdaQueryWrapper<UserSubscriptions>()
                .eq(UserSubscriptions::getUserId, SecurityUtils.getUserId()).eq(UserSubscriptions::getStoreId, storeId)
                .eq(UserSubscriptions::getDelFlag, Constants.UNDELETED));
        return BeanUtil.toBean(store, StoreAppResDTO.class)
                .setAttention(ObjectUtils.isNotEmpty(userSub) ? Boolean.TRUE : Boolean.FALSE)
                .setTagList(storeTagList.stream().map(DailyStoreTag::getTag).collect(Collectors.toList()));
    }

    /**
     * 管理员审核推广获取档口基本信息
     *
     * @param storeId 档口ID
     * @return StoreAdvertResDTO
     */
    @Override
    @Transactional(readOnly = true)
    public StoreAdvertResDTO getAdvertStoreInfo(Long storeId) {
        Store store = Optional.ofNullable(this.storeMapper.selectOne(new LambdaQueryWrapper<Store>()
                .eq(Store::getId, storeId).eq(Store::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("档口不存在!", HttpStatus.ERROR));
        return BeanUtil.toBean(store, StoreAdvertResDTO.class);
    }

    /**
     * 修改档口基本信息
     *
     * @param storeUpdateDTO 档口
     * @return 结果
     */
    @Override
    @Transactional
    public int updateStore(StoreUpdateDTO storeUpdateDTO) {
        Store store = Optional.ofNullable(this.storeMapper.selectOne(new LambdaQueryWrapper<Store>()
                        .eq(Store::getId, storeUpdateDTO.getStoreId()).eq(Store::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("档口不存在!", HttpStatus.ERROR));
        BeanUtil.copyProperties(storeUpdateDTO, store);
        // 如果当前状态为认证营业执照
        if (Objects.equals(store.getStoreStatus(), StoreStatus.AUTH_LICENSE.getValue())) {
            // 将档口状态更改为：认证基本信息
            store.setStoreStatus(StoreStatus.AUTH_BASE_INFO.getValue());
        }
        return storeMapper.updateById(store);
    }

    /**
     * 档口首页数据概览
     *
     * @param overviewDTO 查询入参
     * @return StoreIndexOverviewResDTO
     */
    @Override
    @Transactional(readOnly = true)
    public StoreIndexOverviewResDTO indexOverview(StoreOverviewDTO overviewDTO) {
        List<DailySale> saleList = this.dailySaleMapper.selectList(new LambdaQueryWrapper<DailySale>()
                .eq(DailySale::getStoreId, overviewDTO.getStoreId()).eq(DailySale::getDelFlag, Constants.UNDELETED)
                .between(DailySale::getVoucherDate, overviewDTO.getVoucherDateStart(), overviewDTO.getVoucherDateEnd()));
        if (CollectionUtils.isEmpty(saleList)) {
            return new StoreIndexOverviewResDTO();
        }
        // 总的销售出库金额
        BigDecimal saleAmount = saleList.stream().map(x -> ObjectUtils.defaultIfNull(x.getSaleAmount(), BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add);
        // 总的销售退货金额
        BigDecimal refundAmount = saleList.stream().map(x -> ObjectUtils.defaultIfNull(x.getRefundAmount(), BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add);
        // 总的累计销量
        Integer saleNum = saleList.stream().map(DailySale::getSaleNum).reduce(0, Integer::sum);
        // 总的累计退货量
        Integer refundNum = saleList.stream().map(DailySale::getRefundNum).reduce(0, Integer::sum);
        // 总的累计入库数量
        Integer storageNum = saleList.stream().map(DailySale::getStorageNum).reduce(0, Integer::sum);
        // 总的累计客户数
        Integer customerNum = saleList.stream().map(DailySale::getCustomerNum).reduce(0, Integer::sum);
        return new StoreIndexOverviewResDTO().setSaleAmount(saleAmount).setRefundAmount(refundAmount).setSaleNum(saleNum).setRefundNum(refundNum)
                .setStorageNum(storageNum).setCustomerNum(customerNum);
    }

    /**
     * 档口首页销售额
     *
     * @param revenueDTO 查询入参
     * @return StoreIndexSaleRevenueResDTO
     */
    @Override
    @Transactional(readOnly = true)
    public List<StoreIndexSaleRevenueResDTO> indexSaleRevenue(StoreSaleRevenueDTO revenueDTO) {
        List<DailySale> saleList = this.dailySaleMapper.selectList(new LambdaQueryWrapper<DailySale>()
                .eq(DailySale::getStoreId, revenueDTO.getStoreId()).eq(DailySale::getDelFlag, Constants.UNDELETED)
                .between(DailySale::getVoucherDate, revenueDTO.getVoucherDateStart(), revenueDTO.getVoucherDateEnd()));
        return BeanUtil.copyToList(saleList, StoreIndexSaleRevenueResDTO.class);
    }

    /**
     * 档口首页今日销售额
     *
     * @param storeId 档口ID
     * @return StoreIndexTodaySaleResDTO
     */
    @Override
    @Transactional(readOnly = true)
    public StoreIndexTodaySaleResDTO indexTodaySaleRevenue(Long storeId) {
        List<StoreSaleDetail> detailList = this.saleDetailMapper.selectList(new LambdaQueryWrapper<StoreSaleDetail>()
                .eq(StoreSaleDetail::getStoreId, storeId).eq(StoreSaleDetail::getDelFlag, Constants.UNDELETED)
                .eq(StoreSaleDetail::getVoucherDate, java.sql.Date.valueOf(LocalDate.now())));
        if (CollectionUtils.isEmpty(detailList)) {
            return new StoreIndexTodaySaleResDTO();
        }
        List<StoreProduct> storeProdList = storeProdMapper.selectList(new LambdaQueryWrapper<StoreProduct>()
                .in(StoreProduct::getId, detailList.stream().map(StoreSaleDetail::getStoreProdId).collect(Collectors.toList()))
                .eq(StoreProduct::getDelFlag, Constants.UNDELETED));
        Map<Long, StoreProduct> storeProdMap = storeProdList.stream().collect(Collectors.toMap(StoreProduct::getId, x -> x));
        Map<Long, BigDecimal> saleMap = detailList.stream().collect(Collectors
                .groupingBy(StoreSaleDetail::getStoreProdId, CollectorsUtil.summingBigDecimal(StoreSaleDetail::getAmount)));
        List<StoreIndexTodaySaleResDTO.SITSProdSaleDTO> saleList = new ArrayList<>();
        saleMap.forEach((storeProdId, amount) -> {
            StoreIndexTodaySaleResDTO.SITSProdSaleDTO saleDTO = new StoreIndexTodaySaleResDTO.SITSProdSaleDTO();
            saleDTO.setProdArtNum(ObjectUtils.isNotEmpty(storeProdMap.get(storeProdId)) ? storeProdMap.get(storeProdId).getProdArtNum() : "")
                    .setSaleAmount(amount);
            saleList.add(saleDTO);
        });
        // 总的金额
        final BigDecimal totalAmount = detailList.stream().map(StoreSaleDetail::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        // 销售额排名前5的列表
        List<StoreIndexTodaySaleResDTO.SITSProdSaleDTO> top5List = saleList.stream().sorted(Comparator.comparing(StoreIndexTodaySaleResDTO.SITSProdSaleDTO::getSaleAmount).reversed()).limit(5).collect(Collectors.toList());
        // 其它款式的销售额
        final BigDecimal otherAmount = totalAmount.subtract(top5List.stream().map(StoreIndexTodaySaleResDTO.SITSProdSaleDTO::getSaleAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
        return new StoreIndexTodaySaleResDTO().setStoreId(storeId).setOtherAmount(otherAmount).setSaleList(top5List);
    }

    /**
     * 档口商品销售额前10
     *
     * @param saleTop10DTO 销售额前10入参
     * @return List<StoreIndexSaleTop10ResDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public List<StoreIndexSaleTop10ResDTO> indexTop10Sale(StoreSaleTop10DTO saleTop10DTO) {
        return this.dailySaleProdMapper.selectTop10SaleList(saleTop10DTO);
    }

    /**
     * 档口客户销售额前10
     *
     * @param saleCusTop10DTO 销售额前10入参
     * @return List<StoreIndexCustomerSaleTop10ResDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public List<StoreIndexCusSaleTop10ResDTO> indexTop10SaleCus(StoreSaleCustomerTop10DTO saleCusTop10DTO) {
        return this.dailySaleCusMapper.selectTop10SaleCustomerList(saleCusTop10DTO);
    }

    /**
     * PC 商城 获取档口首页简易数据
     *
     * @param storeId 档口ID
     * @return StoreBasicResDTO
     */
    @Override
    @Transactional (readOnly = true)
    public StoreSimpleResDTO getSimpleInfo(Long storeId) {
        StoreSimpleResDTO simpleDTO = this.storeMapper.getSimpleInfo(storeId);

        return simpleDTO;

        // TODO 获取用户是否关注档口
        // TODO 获取用户是否关注档口
        // TODO 获取用户是否关注档口

        /*final Long userId = SecurityUtils.getUserId();
        UserSubscriptions userSub = ObjectUtils.isEmpty(userId) ? null
                : this.userSubMapper.selectOne(new LambdaQueryWrapper<UserSubscriptions>().eq(UserSubscriptions::getUserId, userId)
                .eq(UserSubscriptions::getStoreId, storeId).eq(UserSubscriptions::getDelFlag, Constants.UNDELETED));
        return simpleDTO.setFocus(ObjectUtils.isNotEmpty(userSub) ? Boolean.TRUE : Boolean.FALSE);*/
    }

    @Override
    public Map<Long, String> getStoreNameByIds(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return MapUtil.empty();
        }
        return storeMapper.selectByIds(ids).stream().collect(Collectors.toMap(Store::getId, Store::getStoreName));
    }

}
