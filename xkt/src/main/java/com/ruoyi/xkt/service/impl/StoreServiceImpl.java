package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.ESystemRole;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bigDecimal.CollectorsUtil;
import com.ruoyi.framework.es.EsClientWrapper;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.xkt.domain.*;
import com.ruoyi.xkt.dto.store.*;
import com.ruoyi.xkt.dto.storeCertificate.StoreCertResDTO;
import com.ruoyi.xkt.enums.StoreStatus;
import com.ruoyi.xkt.mapper.*;
import com.ruoyi.xkt.service.IAssetService;
import com.ruoyi.xkt.service.IStoreCertificateService;
import com.ruoyi.xkt.service.IStoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
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
@Slf4j
@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements IStoreService {

    final StoreMapper storeMapper;
    final SysUserMapper userMapper;
    final DailyStoreTagMapper storeTagMapper;
    final UserSubscriptionsMapper userSubMapper;
    final RedisCache redisCache;
    final IAssetService assetService;
    final DailySaleMapper dailySaleMapper;
    final DailySaleCustomerMapper dailySaleCusMapper;
    final StoreSaleDetailMapper saleDetailMapper;
    final StoreProductMapper storeProdMapper;
    final DailySaleProductMapper dailySaleProdMapper;
    final ISysUserService userService;
    final StoreCertificateMapper storeCertMapper;
    final SysFileMapper fileMapper;
    final IStoreCertificateService storeCertService;
    final EsClientWrapper esClientWrapper;


    /**
     * 注册时新增档口数据
     *
     * @return 结果
     */
    @Override
    @Transactional
    public int create() {
        Store store = new Store();
        // 初始化注册时只需绑定用户ID即可
        store.setUserId(SecurityUtils.getUserId());
        // 默认档口状态为：待审核
        store.setStoreStatus(StoreStatus.UN_AUDITED.getValue());
        // 当前时间往后推1年为试用期时间
        Date oneYearAfter = Date.from(LocalDate.now().plusYears(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        store.setTrialEndTime(oneYearAfter);
        // 设置档口默认权重 0
        store.setStoreWeight(Constants.STORE_WEIGHT_DEFAULT_ZERO);
        int count = this.storeMapper.insert(store);
        // 创建档口账户
        assetService.createInternalAccountIfNotExists(store.getId());
        // 档口用户绑定
        userService.refreshRelStore(store.getUserId(), ESystemRole.SUPPLIER.getId());
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
            if (!Objects.equals(store.getStoreStatus(), StoreStatus.UN_AUDITED.getValue()) &&
                    !Objects.equals(store.getStoreStatus(), StoreStatus.AUDIT_REJECTED.getValue())) {
                throw new ServiceException("当前状态不为待审核 或 审核驳回，不可审核!", HttpStatus.ERROR);
            }
            store.setStoreStatus(StoreStatus.TRIAL_PERIOD.getValue());
            auditDTO.getStoreCert().setStoreId(auditDTO.getStoreId());
            // 更新档口认证信息
            this.storeCertService.update(auditDTO.getStoreCert());
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
        // 获取用户登录账号
        SysUser user = this.userMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUserId, store.getUserId()).eq(SysUser::getDelFlag, Constants.UNDELETED));
        return ObjectUtils.isEmpty(user) ? resDTO : resDTO.setLoginAccount(user.getPhonenumber());
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
        StoreApproveResDTO approveResDTO = new StoreApproveResDTO();
        approveResDTO.setBasic(this.getInfo(storeId));
        approveResDTO.setCertificate(this.getStoreCertificateInfo(storeId));
        return approveResDTO;
    }

    private StoreCertResDTO getStoreCertificateInfo(Long storeId) {
        StoreCertificate storeCert = this.storeCertMapper.selectOne(new LambdaQueryWrapper<StoreCertificate>()
                .eq(StoreCertificate::getStoreId, storeId).eq(StoreCertificate::getDelFlag, Constants.UNDELETED));
        if (ObjectUtils.isEmpty(storeCert)) {
            return new StoreCertResDTO();
        }
        List<SysFile> fileList = Optional.ofNullable(this.fileMapper.selectList(new LambdaQueryWrapper<SysFile>()
                .in(SysFile::getId, Arrays.asList(storeCert.getIdCardFaceFileId(), storeCert.getIdCardEmblemFileId(), storeCert.getLicenseFileId()))
                .eq(SysFile::getDelFlag, Constants.UNDELETED))).orElseThrow(() -> new ServiceException("文件不存在!", HttpStatus.ERROR));
        List<StoreCertResDTO.StoreCertFileDTO> fileDTOList = fileList.stream().map(x -> BeanUtil.toBean(x, StoreCertResDTO.StoreCertFileDTO.class)
                .setFileType(Objects.equals(x.getId(), storeCert.getIdCardFaceFileId()) ? 4 :
                        (Objects.equals(x.getId(), storeCert.getIdCardEmblemFileId()) ? 5 : 6))).collect(Collectors.toList());
        return BeanUtil.toBean(storeCert, StoreCertResDTO.class).setStoreCertId(storeCert.getId()).setFileList(fileDTOList);

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
        final String voucherDateStart = DateUtil.formatDate(overviewDTO.getVoucherDateStart());
        final String voucherDateEnd = DateUtil.formatDate(overviewDTO.getVoucherDateEnd());
        // 档口销售数据
        List<DailySale> saleList = Optional.ofNullable(this.dailySaleMapper.selectList(new LambdaQueryWrapper<DailySale>()
                        .eq(DailySale::getStoreId, overviewDTO.getStoreId()).eq(DailySale::getDelFlag, Constants.UNDELETED)
                        .between(DailySale::getVoucherDate, voucherDateStart, voucherDateEnd)))
                .orElse(Collections.emptyList());
        saleList = CollectionUtils.isNotEmpty(saleList) ? saleList : Collections.emptyList();
        // 最大销售额客户
        StoreIndexOverviewResDTO maxSaleCus = this.dailySaleCusMapper.getMaxSaleCus(overviewDTO.getStoreId(), voucherDateStart, voucherDateEnd);
        // 总的销售出库金额
        BigDecimal saleAmount = saleList.stream().map(x -> ObjectUtils.defaultIfNull(x.getSaleAmount(), BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add);
        // 总的销售退货金额
        BigDecimal refundAmount = saleList.stream().map(x -> ObjectUtils.defaultIfNull(x.getRefundAmount(), BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add);
        // 总的累计销量
        Integer saleNum = saleList.stream().map(x -> ObjectUtils.defaultIfNull(x.getSaleNum(), 0)).reduce(0, Integer::sum);
        // 总的累计退货量
        Integer refundNum = saleList.stream().map(x -> ObjectUtils.defaultIfNull(x.getRefundNum(), 0)).reduce(0, Integer::sum);
        // 总的累计入库数量
        Integer storageNum = saleList.stream().map(x -> ObjectUtils.defaultIfNull(x.getStorageNum(), 0)).reduce(0, Integer::sum);
        // 总的累计客户数
        Integer customerNum = saleList.stream().map(x -> ObjectUtils.defaultIfNull(x.getCustomerNum(), 0)).reduce(0, Integer::sum);
        return new StoreIndexOverviewResDTO().setSaleAmount(saleAmount).setRefundAmount(refundAmount).setSaleNum(saleNum).setRefundNum(refundNum)
                .setStorageNum(storageNum).setCustomerNum(customerNum).setStoreId(overviewDTO.getStoreId())
                .setTopSaleCusName(ObjectUtils.isNotEmpty(maxSaleCus) ? maxSaleCus.getTopSaleCusName() : "")
                .setTopSaleCusAmount(ObjectUtils.isNotEmpty(maxSaleCus) ? maxSaleCus.getTopSaleCusAmount() : null);
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
        final String voucherDateStart = DateUtil.formatDate(revenueDTO.getVoucherDateStart());
        final String voucherDateEnd = DateUtil.formatDate(revenueDTO.getVoucherDateEnd());
        List<DailySale> saleList = Optional.ofNullable(this.dailySaleMapper.selectList(new LambdaQueryWrapper<DailySale>()
                        .eq(DailySale::getStoreId, revenueDTO.getStoreId()).eq(DailySale::getDelFlag, Constants.UNDELETED)
                        .between(DailySale::getVoucherDate, voucherDateStart, voucherDateEnd)))
                .orElse(Collections.emptyList());
        return saleList.stream().map(x -> BeanUtil.toBean(x, StoreIndexSaleRevenueResDTO.class)
                        .setDay(x.getVoucherDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getDayOfMonth()))
                .sorted(Comparator.comparingInt(StoreIndexSaleRevenueResDTO::getDay))
                .collect(Collectors.toList());
    }

    /**
     * 获取今日商品销售额前5
     *
     * @param storeId 档口ID
     * @return StoreIndexTodaySaleResDTO
     */
    @Override
    @Transactional(readOnly = true)
    public StoreIndexTodaySaleTop5ResDTO indexTodayProdSaleRevenueTop5(Long storeId) {
        List<StoreSaleDetail> detailList = this.saleDetailMapper.selectList(new LambdaQueryWrapper<StoreSaleDetail>()
                .eq(StoreSaleDetail::getStoreId, storeId).eq(StoreSaleDetail::getDelFlag, Constants.UNDELETED)
                .eq(StoreSaleDetail::getVoucherDate, java.sql.Date.valueOf(LocalDate.now())));
        if (CollectionUtils.isEmpty(detailList)) {
            return new StoreIndexTodaySaleTop5ResDTO();
        }
        List<StoreProduct> storeProdList = storeProdMapper.selectList(new LambdaQueryWrapper<StoreProduct>()
                .in(StoreProduct::getId, detailList.stream().map(StoreSaleDetail::getStoreProdId).collect(Collectors.toList()))
                .eq(StoreProduct::getDelFlag, Constants.UNDELETED));
        Map<Long, StoreProduct> storeProdMap = storeProdList.stream().collect(Collectors.toMap(StoreProduct::getId, x -> x));
        Map<Long, BigDecimal> saleMap = detailList.stream().collect(Collectors
                .groupingBy(StoreSaleDetail::getStoreProdId, CollectorsUtil.summingBigDecimal(StoreSaleDetail::getAmount)));
        List<StoreIndexTodaySaleTop5ResDTO.SITSProdSaleDTO> saleList = new ArrayList<>();
        saleMap.forEach((storeProdId, amount) -> {
            StoreIndexTodaySaleTop5ResDTO.SITSProdSaleDTO saleDTO = new StoreIndexTodaySaleTop5ResDTO.SITSProdSaleDTO();
            saleDTO.setProdArtNum(ObjectUtils.isNotEmpty(storeProdMap.get(storeProdId)) ? storeProdMap.get(storeProdId).getProdArtNum() : "")
                    .setSaleAmount(amount);
            saleList.add(saleDTO);
        });
        // 总的金额
        final BigDecimal totalAmount = detailList.stream().map(StoreSaleDetail::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        // 销售额排名前5的列表
        List<StoreIndexTodaySaleTop5ResDTO.SITSProdSaleDTO> top5List = saleList.stream().sorted(Comparator.comparing(StoreIndexTodaySaleTop5ResDTO.SITSProdSaleDTO::getSaleAmount).reversed()).limit(5).collect(Collectors.toList());
        // 其它款式的销售额
        final BigDecimal otherAmount = totalAmount.subtract(top5List.stream().map(StoreIndexTodaySaleTop5ResDTO.SITSProdSaleDTO::getSaleAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
        return new StoreIndexTodaySaleTop5ResDTO().setStoreId(storeId).setOtherAmount(otherAmount).setSaleList(top5List);
    }

    @Transactional(readOnly = true)
    @Override
    public Integer getStoreStatus(Long storeId) {
        return Optional.ofNullable(storeMapper.selectById(storeId)).map(Store::getStoreStatus).orElse(null);
    }

    /**
     * 档口首页今日销售额
     *
     * @param storeId 档口ID
     * @return StoreIndexTodaySaleResDTO
     */
    @Override
    @Transactional(readOnly = true)
    public List<StoreIndexTodaySaleResDTO> indexTodayProdSaleRevenue(Long storeId) {
        List<StoreSaleDetail> detailList = this.saleDetailMapper.selectList(new LambdaQueryWrapper<StoreSaleDetail>()
                .eq(StoreSaleDetail::getStoreId, storeId).eq(StoreSaleDetail::getDelFlag, Constants.UNDELETED)
                .eq(StoreSaleDetail::getVoucherDate, java.sql.Date.valueOf(LocalDate.now())));
        if (CollectionUtils.isEmpty(detailList)) {
            return new ArrayList<>();
        }
        Map<String, List<StoreSaleDetail>> todaySaleMap = detailList.stream().collect(Collectors.groupingBy(StoreSaleDetail::getProdArtNum));
        List<StoreIndexTodaySaleResDTO> saleList = new ArrayList<>();
        todaySaleMap.forEach((prodArtNum, list) -> {
            final Integer saleNum = list.stream().map(x -> ObjectUtils.defaultIfNull(x.getQuantity(), 0)).reduce(0, Integer::sum);
            final BigDecimal saleAmount = list.stream().map(x -> ObjectUtils.defaultIfNull(x.getAmount(), BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add);
            saleList.add(new StoreIndexTodaySaleResDTO().setProdArtNum(prodArtNum).setSaleNum(saleNum).setSaleAmount(saleAmount));
        });
        // 按照销售金额倒序排
        saleList.sort(Comparator.comparing(StoreIndexTodaySaleResDTO::getSaleAmount).reversed());
        return saleList;
    }

    /**
     * 获取今日客户销售额
     *
     * @param storeId 档口ID
     * @return StoreIndexTodayCusSaleResDTO
     */
    @Override
    @Transactional(readOnly = true)
    public List<StoreIndexTodayCusSaleResDTO> indexTodayCusSaleRevenue(Long storeId) {
        List<StoreSaleDetail> detailList = this.saleDetailMapper.selectList(new LambdaQueryWrapper<StoreSaleDetail>()
                .eq(StoreSaleDetail::getStoreId, storeId).eq(StoreSaleDetail::getDelFlag, Constants.UNDELETED)
                .eq(StoreSaleDetail::getVoucherDate, java.sql.Date.valueOf(LocalDate.now())));
        if (CollectionUtils.isEmpty(detailList)) {
            return new ArrayList<>();
        }
        Map<Long, String> storeCusMap = detailList.stream().collect(Collectors.toMap(StoreSaleDetail::getStoreCusId, StoreSaleDetail::getStoreCusName, (s1, s2) -> s2));
        Map<Long, List<StoreSaleDetail>> cusSaleMap = detailList.stream().collect(Collectors.groupingBy(StoreSaleDetail::getStoreCusId));
        List<StoreIndexTodayCusSaleResDTO> saleList = new ArrayList<>();
        cusSaleMap.forEach((storeCusId, list) -> {
            final Integer saleNum = list.stream().map(x -> ObjectUtils.defaultIfNull(x.getQuantity(), 0)).reduce(0, Integer::sum);
            final Long saleAmount = list.stream().map(x -> ObjectUtils.defaultIfNull(x.getAmount(), BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add).longValue();
            saleList.add(new StoreIndexTodayCusSaleResDTO().setStoreCusId(storeCusId).setSaleNum(saleNum).setSaleAmount(saleAmount)
                    .setStoreCusName(ObjectUtils.defaultIfNull(storeCusMap.get(storeCusId), "")));
        });
        return saleList;
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
        return this.dailySaleProdMapper.selectTop10SaleList(saleTop10DTO.getStoreId(),
                DateUtil.formatDate(saleTop10DTO.getVoucherDateStart()), DateUtil.formatDate(saleTop10DTO.getVoucherDateEnd()));
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
        return this.dailySaleCusMapper.selectTop10SaleCustomerList(saleCusTop10DTO.getStoreId(),
                DateUtil.formatDate(saleCusTop10DTO.getVoucherDateStart()), DateUtil.formatDate(saleCusTop10DTO.getVoucherDateEnd()));
    }

    /**
     * PC 商城 获取档口首页简易数据
     *
     * @param storeId 档口ID
     * @return StoreBasicResDTO
     */
    @Override
    @Transactional(readOnly = true)
    public StoreSimpleResDTO getSimpleInfo(Long storeId) {
        StoreSimpleResDTO simpleDTO = this.storeMapper.getSimpleInfo(storeId);
        UserSubscriptions userSub = this.userSubMapper.selectOne(new LambdaQueryWrapper<UserSubscriptions>()
                .eq(UserSubscriptions::getUserId, SecurityUtils.getUserId())
                .eq(UserSubscriptions::getStoreId, storeId)
                .eq(UserSubscriptions::getDelFlag, Constants.UNDELETED));
        return simpleDTO.setFocus(ObjectUtils.isNotEmpty(userSub) ? Boolean.TRUE : Boolean.FALSE);
    }

    @Override
    public Map<Long, String> getStoreNameByIds(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return MapUtil.empty();
        }
        return storeMapper.selectByIds(ids).stream().collect(Collectors.toMap(Store::getId, Store::getStoreName));
    }

    /**
     * 模糊查询档口名称
     *
     * @param storeName 档口名称
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<StoreNameResDTO> fuzzyQuery(String storeName) {
        LambdaQueryWrapper<Store> queryWrapper = new LambdaQueryWrapper<Store>().eq(Store::getDelFlag, Constants.UNDELETED);
        if (StringUtils.isNotBlank(storeName)) {
            queryWrapper.like(Store::getStoreName, storeName);
        }
        List<Store> storeList = this.storeMapper.selectList(queryWrapper);
        return storeList.stream().map(x -> new StoreNameResDTO().setStoreId(x.getId()).setStoreName(x.getStoreName()))
                .collect(Collectors.toList());
    }

    /**
     * 更新档口权重
     *
     * @param storeWeightUpdateDTO 更新入参
     * @return Integer
     */
    @Override
    @Transactional
    public Integer updateStoreWeight(StoreWeightUpdateDTO storeWeightUpdateDTO) throws IOException {
        Store store = Optional.ofNullable(this.storeMapper.selectOne(new LambdaQueryWrapper<Store>()
                        .eq(Store::getId, storeWeightUpdateDTO.getStoreId()).eq(Store::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("档口不存在!", HttpStatus.ERROR));
        if (storeWeightUpdateDTO.getStoreWeight() > 100 || store.getStoreWeight() < -100) {
            throw new ServiceException("权重范围在-100-100之间!", HttpStatus.ERROR);
        }
        store.setStoreWeight(storeWeightUpdateDTO.getStoreWeight());
        int count = this.storeMapper.updateById(store);
        // 更新档口权重到ES中
        List<StoreProduct> storeProdList = this.storeProdMapper.selectList(new LambdaQueryWrapper<StoreProduct>()
                .eq(StoreProduct::getStoreId, storeWeightUpdateDTO.getStoreId()).eq(StoreProduct::getDelFlag, Constants.UNDELETED));
        if (CollectionUtils.isEmpty(storeProdList)) {
            log.info("没有需要更新的档口商品数据");
            return count;
        }
        final Integer storeWeight = ObjectUtils.defaultIfNull(storeWeightUpdateDTO.getStoreWeight(), 0);
        // 构建一个批量数据集合
        List<BulkOperation> list = new ArrayList<>();
        storeProdList.forEach(storeProd -> {
            // 构建部分文档更新请求
            list.add(new BulkOperation.Builder().update(u -> u
                            .action(a -> a.doc(new HashMap<String, Object>() {{
                                put("storeWeight", storeWeight);
                            }}))
                            .id(String.valueOf(storeProd.getId()))
                            .index(Constants.ES_IDX_PRODUCT_INFO))
                    .build());
        });
        try {
            // 调用bulk方法执行批量更新操作
            BulkResponse bulkResponse = esClientWrapper.getEsClient().bulk(e -> e.index(Constants.ES_IDX_PRODUCT_INFO).operations(list));
            log.info("bulkResponse.result() = {}", bulkResponse.items());
        } catch (IOException | RuntimeException e) {
            // 记录日志并抛出或处理异常
            log.error("向ES更新档口权重失败，商品ID: {}, 错误信息: {}", storeProdList.stream().map(StoreProduct::getId).collect(Collectors.toList()), e.getMessage());
            throw e; // 或者做其他补偿处理，比如异步重试
        }
        return count;
    }


}
