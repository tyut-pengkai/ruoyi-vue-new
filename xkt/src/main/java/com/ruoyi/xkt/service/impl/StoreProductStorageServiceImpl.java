package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.xkt.domain.StoreProductDemandDetail;
import com.ruoyi.xkt.domain.StoreProductStorage;
import com.ruoyi.xkt.domain.StoreProductStorageDemandDeductNew;
import com.ruoyi.xkt.domain.StoreProductStorageDetail;
import com.ruoyi.xkt.dto.storeProdStorage.StoreProdStorageDTO;
import com.ruoyi.xkt.dto.storeProdStorage.StoreProdStoragePageDTO;
import com.ruoyi.xkt.dto.storeProdStorage.StoreProdStoragePageResDTO;
import com.ruoyi.xkt.dto.storeProdStorage.StoreProdStorageResDTO;
import com.ruoyi.xkt.dto.storeProductDemand.StoreProdDemandSimpleDTO;
import com.ruoyi.xkt.dto.storeProductStock.StoreProdStockDTO;
import com.ruoyi.xkt.enums.EVoucherSequenceType;
import com.ruoyi.xkt.mapper.*;
import com.ruoyi.xkt.service.IStoreProductStockService;
import com.ruoyi.xkt.service.IStoreProductStorageService;
import com.ruoyi.xkt.service.IVoucherSequenceService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 档口商品入库Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
@RequiredArgsConstructor
public class StoreProductStorageServiceImpl implements IStoreProductStorageService {

    final StoreProductStorageMapper storageMapper;
    final StoreProductStorageDetailMapper storageDetailMapper;
    final IVoucherSequenceService sequenceService;
    final IStoreProductStockService stockService;
    final StoreProductDemandDetailMapper demandDetailMapper;
    final StoreProductDemandMapper demandMapper;
    final StoreProductStorageDemandDeductMapper deductMapper;
    final StoreProductStorageDemandDeductNewMapper deductNewMapper;


    /**
     * 分页查询
     *
     * @param pageDTO 查询入参
     * @return Page
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StoreProdStoragePageResDTO> page(StoreProdStoragePageDTO pageDTO) {
        PageHelper.startPage(pageDTO.getPageNum(), pageDTO.getPageSize());
        List<StoreProdStoragePageResDTO> list = this.storageMapper.selectStoragePage(pageDTO);
        return Page.convert(new PageInfo<>(list));
    }

    /**
     * 新增档口商品入库
     *
     * @param storeProdStorageDTO 档口商品入库
     * @return 结果
     */
    @Override
    @Transactional
    public int create(StoreProdStorageDTO storeProdStorageDTO) {
        // 生成code
        String code = this.sequenceService.generateCode(storeProdStorageDTO.getStoreId(), EVoucherSequenceType.STORAGE.getValue(), DateUtils.parseDateToStr(DateUtils.YYYYMMDD, new Date()));
        // 总的数量
        Integer totalNum = storeProdStorageDTO.getDetailList().stream().map(x -> ObjectUtils.defaultIfNull(x.getQuantity(), 0)).reduce(0, Integer::sum);
        // 总的金额
        BigDecimal produceAmount = storeProdStorageDTO.getDetailList().stream().map(x -> ObjectUtils.defaultIfNull(x.getProduceAmount(), BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add);
        // 当前登录用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        // 新增档口商品入库
        StoreProductStorage storeProdStorage = BeanUtil.toBean(storeProdStorageDTO, StoreProductStorage.class)
                .setCode(code).setQuantity(totalNum).setProduceAmount(produceAmount).setOperatorId(loginUser.getUserId()).setOperatorName(loginUser.getUsername());
        int count = this.storageMapper.insert(storeProdStorage);
        // 新增档口商品入库明细
        List<StoreProductStorageDetail> detailList = storeProdStorageDTO.getDetailList().stream().map(x -> BeanUtil.toBean(x, StoreProductStorageDetail.class)
                .setStoreProdStorId(storeProdStorage.getId())).collect(Collectors.toList());
        this.storageDetailMapper.insert(detailList);
        // 构造增加库存的入参DTO
        List<StoreProdStockDTO> increaseStockList = BeanUtil.copyToList(detailList, StoreProdStockDTO.class);
        // 增加档口商品的库存
        this.stockService.increaseStock(storeProdStorageDTO.getStoreId(), increaseStockList);

        // 根据明细列表找到所有提交的需求
        List<StoreProductDemandDetail> demandDetailList = this.demandDetailMapper.selectList(new LambdaQueryWrapper<StoreProductDemandDetail>()
                .eq(StoreProductDemandDetail::getStoreId, storeProdStorageDTO.getStoreId()).eq(StoreProductDemandDetail::getDelFlag, Constants.UNDELETED)
                .in(StoreProductDemandDetail::getDetailStatus, Arrays.asList(1, 2))
                .in(StoreProductDemandDetail::getStoreProdColorId, detailList.stream().map(StoreProductStorageDetail::getStoreProdColorId).collect(Collectors.toList())));
        // 若没有任何需求则不抵扣，直接结束流程
        if (CollectionUtils.isEmpty(demandDetailList)) {
            return count;
        }
        // 所有的需求单ID列表
        final List<Long> demandIdList = demandDetailList.stream().map(StoreProductDemandDetail::getStoreProdDemandId).distinct().collect(Collectors.toList());
        List<StoreProductStorageDemandDeductNew> deductedNewList = this.deductNewMapper.selectList(new LambdaQueryWrapper<StoreProductStorageDemandDeductNew>()
                .in(StoreProductStorageDemandDeductNew::getStoreProdColorId, detailList.stream().map(StoreProductStorageDetail::getStoreProdColorId).collect(Collectors.toList()))
                .eq(StoreProductStorageDemandDeductNew::getDelFlag, Constants.UNDELETED));
        // 已存在的需求抵扣明细列表
        Map<Long, Map<Integer, Integer>> deductedExistsMap = deductedNewList.stream().collect(Collectors.groupingBy(StoreProductStorageDemandDeductNew::getStoreProdColorId,
                Collectors.groupingBy(StoreProductStorageDemandDeductNew::getSize, Collectors.summingInt(x -> ObjectUtils.defaultIfNull(x.getQuantity(), 0)))));

        // 按照需求的storeProdColorId进行分组，再按照：1.生产中（2）、待生产（1） 排序  2. 再按照需求创建时间升序排列
        Map<Long, List<StoreProductDemandDetail>> tempDemandDetailMap = demandDetailList.stream().collect(Collectors
                .groupingBy(StoreProductDemandDetail::getStoreProdColorId, Collectors
                        .collectingAndThen(Collectors.toList(), tempList -> tempList.stream()
                                .sorted(Comparator.comparing(StoreProductDemandDetail::getDetailStatus).reversed()
                                        .thenComparing(StoreProductDemandDetail::getCreateTime)).collect(Collectors.toList()))));
        // 所有颜色尺码需要抵扣的需求数量
        Map<Long, Map<Integer, Map<Long, Integer>>> unDeductMap = new LinkedHashMap<>();
        tempDemandDetailMap.forEach((storeProdColorId, tempDemandDetailList) -> {
            // 已存在抵扣的数量
            Map<Integer, Integer> sizeDeductedMap = ObjectUtils.defaultIfNull(deductedExistsMap.get(storeProdColorId), new LinkedHashMap<>());
            // 尺码为30的已抵扣需求数量
            Integer size30Deduct = ObjectUtils.defaultIfNull(sizeDeductedMap.get(30), 0);
            Integer size31Deduct = ObjectUtils.defaultIfNull(sizeDeductedMap.get(31), 0);
            Integer size32Deduct = ObjectUtils.defaultIfNull(sizeDeductedMap.get(32), 0);
            Integer size33Deduct = ObjectUtils.defaultIfNull(sizeDeductedMap.get(33), 0);
            Integer size34Deduct = ObjectUtils.defaultIfNull(sizeDeductedMap.get(34), 0);
            Integer size35Deduct = ObjectUtils.defaultIfNull(sizeDeductedMap.get(35), 0);
            Integer size36Deduct = ObjectUtils.defaultIfNull(sizeDeductedMap.get(36), 0);
            Integer size37Deduct = ObjectUtils.defaultIfNull(sizeDeductedMap.get(37), 0);
            Integer size38Deduct = ObjectUtils.defaultIfNull(sizeDeductedMap.get(38), 0);
            Integer size39Deduct = ObjectUtils.defaultIfNull(sizeDeductedMap.get(39), 0);
            Integer size40Deduct = ObjectUtils.defaultIfNull(sizeDeductedMap.get(40), 0);
            Integer size41Deduct = ObjectUtils.defaultIfNull(sizeDeductedMap.get(41), 0);
            Integer size42Deduct = ObjectUtils.defaultIfNull(sizeDeductedMap.get(42), 0);
            Integer size43Deduct = ObjectUtils.defaultIfNull(sizeDeductedMap.get(43), 0);
            Map<Long, Integer> size30DemandMap = new LinkedHashMap<>();
            Map<Long, Integer> size31DemandMap = new LinkedHashMap<>();
            Map<Long, Integer> size32DemandMap = new LinkedHashMap<>();
            Map<Long, Integer> size33DemandMap = new LinkedHashMap<>();
            Map<Long, Integer> size34DemandMap = new LinkedHashMap<>();
            Map<Long, Integer> size35DemandMap = new LinkedHashMap<>();
            Map<Long, Integer> size36DemandMap = new LinkedHashMap<>();
            Map<Long, Integer> size37DemandMap = new LinkedHashMap<>();
            Map<Long, Integer> size38DemandMap = new LinkedHashMap<>();
            Map<Long, Integer> size39DemandMap = new LinkedHashMap<>();
            Map<Long, Integer> size40DemandMap = new LinkedHashMap<>();
            Map<Long, Integer> size41DemandMap = new LinkedHashMap<>();
            Map<Long, Integer> size42DemandMap = new LinkedHashMap<>();
            Map<Long, Integer> size43DemandMap = new LinkedHashMap<>();
            // 不同size仍需抵扣的数量
            Map<Integer, Map<Long, Integer>> sizeRequireMap = new LinkedHashMap<>();
            // 依次计算各尺码的需求数量
            for (StoreProductDemandDetail demandDetail : tempDemandDetailList) {
                size30Deduct = this.handleSize30RequireQuantity(size30Deduct, size30DemandMap, demandDetail);
                size31Deduct = this.handleSize31RequireQuantity(size31Deduct, size31DemandMap, demandDetail);
                size32Deduct = this.handleSize32RequireQuantity(size32Deduct, size32DemandMap, demandDetail);
                size33Deduct = this.handleSize33RequireQuantity(size33Deduct, size33DemandMap, demandDetail);
                size34Deduct = this.handleSize34RequireQuantity(size34Deduct, size34DemandMap, demandDetail);
                size35Deduct = this.handleSize35RequireQuantity(size35Deduct, size35DemandMap, demandDetail);
                size36Deduct = this.handleSize36RequireQuantity(size36Deduct, size36DemandMap, demandDetail);
                size37Deduct = this.handleSize37RequireQuantity(size37Deduct, size37DemandMap, demandDetail);
                size38Deduct = this.handleSize38RequireQuantity(size38Deduct, size38DemandMap, demandDetail);
                size39Deduct = this.handleSize39RequireQuantity(size39Deduct, size39DemandMap, demandDetail);
                size40Deduct = this.handleSize40RequireQuantity(size40Deduct, size40DemandMap, demandDetail);
                size41Deduct = this.handleSize41RequireQuantity(size41Deduct, size41DemandMap, demandDetail);
                size42Deduct = this.handleSize42RequireQuantity(size42Deduct, size42DemandMap, demandDetail);
                size43Deduct = this.handleSize43RequireQuantity(size43Deduct, size43DemandMap, demandDetail);
            }
            if (MapUtils.isNotEmpty(size30DemandMap)) {
                sizeRequireMap.put(30, size30DemandMap);
            }
            if (MapUtils.isNotEmpty(size31DemandMap)) {
                sizeRequireMap.put(31, size31DemandMap);
            }
            if (MapUtils.isNotEmpty(size32DemandMap)) {
                sizeRequireMap.put(32, size32DemandMap);
            }
            if (MapUtils.isNotEmpty(size33DemandMap)) {
                sizeRequireMap.put(33, size33DemandMap);
            }
            if (MapUtils.isNotEmpty(size34DemandMap)) {
                sizeRequireMap.put(34, size34DemandMap);
            }
            if (MapUtils.isNotEmpty(size35DemandMap)) {
                sizeRequireMap.put(35, size35DemandMap);
            }
            if (MapUtils.isNotEmpty(size36DemandMap)) {
                sizeRequireMap.put(36, size36DemandMap);
            }
            if (MapUtils.isNotEmpty(size37DemandMap)) {
                sizeRequireMap.put(37, size37DemandMap);
            }
            if (MapUtils.isNotEmpty(size38DemandMap)) {
                sizeRequireMap.put(38, size38DemandMap);
            }
            if (MapUtils.isNotEmpty(size39DemandMap)) {
                sizeRequireMap.put(39, size39DemandMap);
            }
            if (MapUtils.isNotEmpty(size40DemandMap)) {
                sizeRequireMap.put(40, size40DemandMap);
            }
            if (MapUtils.isNotEmpty(size41DemandMap)) {
                sizeRequireMap.put(41, size41DemandMap);
            }
            if (MapUtils.isNotEmpty(size42DemandMap)) {
                sizeRequireMap.put(42, size42DemandMap);
            }
            if (MapUtils.isNotEmpty(size43DemandMap)) {
                sizeRequireMap.put(43, size43DemandMap);
            }
            if (MapUtils.isNotEmpty(sizeRequireMap)) {
                unDeductMap.put(storeProdColorId, sizeRequireMap);
            }
        });

        // 按照入库明细列表依次进行需求数量扣减
        Map<Long, Map<Long, Map<Integer, Integer>>> storageQuantityMap = new LinkedHashMap<>();
        for (StoreProductStorageDetail storageDetail : detailList) {
            Map<Integer, Integer> sizeStorageMap = new LinkedHashMap<>();
            if (ObjectUtils.isNotEmpty(storageDetail.getSize30())) {
                sizeStorageMap.put(30, storageDetail.getSize30());
            }
            if (ObjectUtils.isNotEmpty(storageDetail.getSize31())) {
                sizeStorageMap.put(31, storageDetail.getSize31());
            }
            if (ObjectUtils.isNotEmpty(storageDetail.getSize32())) {
                sizeStorageMap.put(32, storageDetail.getSize32());
            }
            if (ObjectUtils.isNotEmpty(storageDetail.getSize33())) {
                sizeStorageMap.put(33, storageDetail.getSize33());
            }
            if (ObjectUtils.isNotEmpty(storageDetail.getSize34())) {
                sizeStorageMap.put(34, storageDetail.getSize34());
            }
            if (ObjectUtils.isNotEmpty(storageDetail.getSize35())) {
                sizeStorageMap.put(35, storageDetail.getSize35());
            }
            if (ObjectUtils.isNotEmpty(storageDetail.getSize36())) {
                sizeStorageMap.put(36, storageDetail.getSize36());
            }
            if (ObjectUtils.isNotEmpty(storageDetail.getSize37())) {
                sizeStorageMap.put(37, storageDetail.getSize37());
            }
            if (ObjectUtils.isNotEmpty(storageDetail.getSize38())) {
                sizeStorageMap.put(38, storageDetail.getSize38());
            }
            if (ObjectUtils.isNotEmpty(storageDetail.getSize39())) {
                sizeStorageMap.put(39, storageDetail.getSize39());
            }
            if (ObjectUtils.isNotEmpty(storageDetail.getSize40())) {
                sizeStorageMap.put(40, storageDetail.getSize40());
            }
            if (ObjectUtils.isNotEmpty(storageDetail.getSize41())) {
                sizeStorageMap.put(41, storageDetail.getSize41());
            }
            if (ObjectUtils.isNotEmpty(storageDetail.getSize42())) {
                sizeStorageMap.put(42, storageDetail.getSize42());
            }
            if (ObjectUtils.isNotEmpty(storageDetail.getSize43())) {
                sizeStorageMap.put(43, storageDetail.getSize43());
            }
            if (MapUtils.isNotEmpty(sizeStorageMap)) {
                storageQuantityMap.put(storageDetail.getStoreProdColorId(), new LinkedHashMap<Long, Map<Integer, Integer>>() {{
                    put(storageDetail.getId(), sizeStorageMap);
                }});
            }
        }

        // 按照入库的数量明细依次判断哪些需求订单明细还未抵扣完毕
        Map<Long, Map<Long, Map<Integer, Map<Long, Integer>>>> totalMatchMap = new LinkedHashMap<>();
        storageQuantityMap.forEach((storeProdColorId, detailSizeStorageMap) -> {
            Map<Integer, Map<Long, Integer>> sizeRequireMap = unDeductMap.get(storeProdColorId);
            if (MapUtils.isEmpty(sizeRequireMap) || MapUtils.isEmpty(detailSizeStorageMap)) {
                return;
            }
            Map<Long, Map<Integer, Map<Long, Integer>>> storageDetailIdSizeMatchMap = new LinkedHashMap<>();
            // 匹配的尺码集合
            detailSizeStorageMap.forEach((storageDetailId, sizeStorageMap) -> {
                if (MapUtils.isEmpty(sizeStorageMap)) {
                    return;
                }
                Map<Integer, Map<Long, Integer>> sizeMatchMap = new LinkedHashMap<>();
                // 遍历每个入库单明细尺码
                sizeStorageMap.forEach((size, storageQuantity) -> {
                    Map<Long, Integer> requireMap = sizeRequireMap.get(size);
                    if (MapUtils.isEmpty(requireMap)) {
                        return;
                    }
                    // key storeProductDemandDetailId； value matchQuantity
                    Map<Long, Integer> itemMatchMap = new LinkedHashMap<>();
                    // 遍历每个需求订单
                    for (Map.Entry<Long, Integer> entry : requireMap.entrySet()) {
                        // 尺码的入库数量 + 当前尺码的需求明细数量
                        final int tempCompareQuantity = storageQuantity + entry.getValue();
                        if (tempCompareQuantity == 0) {
                            continue;
                        }
                        if (tempCompareQuantity > 0) {
                            // value 取绝对值
                            itemMatchMap.put(entry.getKey(), Math.abs(entry.getValue()));
                            storageQuantity = tempCompareQuantity;
                        } else {
                            // value 取绝对值
                            itemMatchMap.put(entry.getKey(), Math.abs(storageQuantity));
                            break;
                        }
                    }
                    // 本次入库单明细可抵扣的需求单明细ID及数量
                    if (MapUtils.isNotEmpty(itemMatchMap)) {
                        sizeMatchMap.put(size, itemMatchMap);
                    }
                });
                if (MapUtils.isNotEmpty(sizeMatchMap)) {
                    storageDetailIdSizeMatchMap.put(storageDetailId, sizeMatchMap);
                }
            });
            if (MapUtils.isNotEmpty(storageDetailIdSizeMatchMap)) {
                totalMatchMap.put(storeProdColorId, storageDetailIdSizeMatchMap);
            }
        });

        System.err.println(totalMatchMap);
        if (MapUtils.isEmpty(totalMatchMap)) {
            return count;
        }

        List<StoreProdDemandSimpleDTO> demandSimpleList = Optional.ofNullable(this.demandDetailMapper.selectDemandCodeList(demandDetailList.stream()
                        .map(StoreProductDemandDetail::getId).distinct().collect(Collectors.toList())))
                .orElseThrow(() -> new ServiceException("获取需求单号失败", HttpStatus.ERROR));
        // demandDetailId 与 demandCode 的映射关系
        Map<Long, String> demandCodeMap = demandSimpleList.stream().collect(Collectors.toMap(StoreProdDemandSimpleDTO::getStoreProdDemandDetailId, StoreProdDemandSimpleDTO::getCode));
        List<StoreProductStorageDemandDeductNew> deductList = new ArrayList<>();
        // 待更新的需求单明细ID列表
        List<Long> updateDetailIdList = new ArrayList<>();
        totalMatchMap.forEach((storeProdColorId, detailSizeStorageMap) -> {
            detailSizeStorageMap.forEach((storageDetailId, sizeMatchMap) -> {
                sizeMatchMap.forEach((size, itemMatchMap) -> {
                    itemMatchMap.forEach((storeProductDemandDetailId, matchQuantity) -> {
                        StoreProductStorageDemandDeductNew deductNew = new StoreProductStorageDemandDeductNew();
                        deductNew.setStoreProdColorId(storeProdColorId)
                                .setStoreProdDemandDetailId(storeProductDemandDetailId)
                                .setStoreProdStorageDetailId(storageDetailId)
                                .setStorageCode(storeProdStorage.getCode())
                                .setDemandCode(demandCodeMap.get(storeProductDemandDetailId))
                                .setSize(size)
                                .setQuantity(matchQuantity)
                                .setDelFlag(Constants.UNDELETED);
                        updateDetailIdList.add(storeProductDemandDetailId);
                        deductList.add(deductNew);
                    });
                });
            });
        });
        this.deductNewMapper.insert(deductList);

        List<StoreProductStorageDemandDeductNew> listAfterInsert = this.deductNewMapper.selectList(new LambdaQueryWrapper<StoreProductStorageDemandDeductNew>()
                .eq(StoreProductStorageDemandDeductNew::getDelFlag, Constants.UNDELETED).in(StoreProductStorageDemandDeductNew::getStoreProdDemandDetailId, updateDetailIdList));
        if (CollectionUtils.isEmpty(listAfterInsert)) {
            return count;
        }
        Map<Long, List<StoreProductStorageDemandDeductNew>> latestDeductMap = listAfterInsert.stream().collect(Collectors.groupingBy(StoreProductStorageDemandDeductNew::getStoreProdDemandDetailId));
        Map<Long, Map<Integer, Integer>> latestDeductSizeQuantityMap = new LinkedHashMap<>();
        latestDeductMap.forEach((storeProdDemandDetailId, latestDeductList) -> {
            Map<Integer, Integer> sizeQuantityMap = new LinkedHashMap<>();
            Integer size30Quantity = latestDeductList.stream().filter(x -> x.getSize() == 30).map(x -> ObjectUtils.defaultIfNull(x.getQuantity(), 0)).reduce(0, Integer::sum);
            sizeQuantityMap.put(30, size30Quantity);
            Integer size31Quantity = latestDeductList.stream().filter(x -> x.getSize() == 31).map(x -> ObjectUtils.defaultIfNull(x.getQuantity(), 0)).reduce(0, Integer::sum);
            sizeQuantityMap.put(31, size31Quantity);
            Integer size32Quantity = latestDeductList.stream().filter(x -> x.getSize() == 32).map(x -> ObjectUtils.defaultIfNull(x.getQuantity(), 0)).reduce(0, Integer::sum);
            sizeQuantityMap.put(32, size32Quantity);
            Integer size33Quantity = latestDeductList.stream().filter(x -> x.getSize() == 33).map(x -> ObjectUtils.defaultIfNull(x.getQuantity(), 0)).reduce(0, Integer::sum);
            sizeQuantityMap.put(33, size33Quantity);
            Integer size34Quantity = latestDeductList.stream().filter(x -> x.getSize() == 34).map(x -> ObjectUtils.defaultIfNull(x.getQuantity(), 0)).reduce(0, Integer::sum);
            sizeQuantityMap.put(34, size34Quantity);
            Integer size35Quantity = latestDeductList.stream().filter(x -> x.getSize() == 35).map(x -> ObjectUtils.defaultIfNull(x.getQuantity(), 0)).reduce(0, Integer::sum);
            sizeQuantityMap.put(35, size35Quantity);
            Integer size36Quantity = latestDeductList.stream().filter(x -> x.getSize() == 36).map(x -> ObjectUtils.defaultIfNull(x.getQuantity(), 0)).reduce(0, Integer::sum);
            sizeQuantityMap.put(36, size36Quantity);
            Integer size37Quantity = latestDeductList.stream().filter(x -> x.getSize() == 37).map(x -> ObjectUtils.defaultIfNull(x.getQuantity(), 0)).reduce(0, Integer::sum);
            sizeQuantityMap.put(37, size37Quantity);
            Integer size38Quantity = latestDeductList.stream().filter(x -> x.getSize() == 38).map(x -> ObjectUtils.defaultIfNull(x.getQuantity(), 0)).reduce(0, Integer::sum);
            sizeQuantityMap.put(38, size38Quantity);
            Integer size39Quantity = latestDeductList.stream().filter(x -> x.getSize() == 39).map(x -> ObjectUtils.defaultIfNull(x.getQuantity(), 0)).reduce(0, Integer::sum);
            sizeQuantityMap.put(39, size39Quantity);
            Integer size40Quantity = latestDeductList.stream().filter(x -> x.getSize() == 40).map(x -> ObjectUtils.defaultIfNull(x.getQuantity(), 0)).reduce(0, Integer::sum);
            sizeQuantityMap.put(40, size40Quantity);
            Integer size41Quantity = latestDeductList.stream().filter(x -> x.getSize() == 41).map(x -> ObjectUtils.defaultIfNull(x.getQuantity(), 0)).reduce(0, Integer::sum);
            sizeQuantityMap.put(41, size41Quantity);
            Integer size42Quantity = latestDeductList.stream().filter(x -> x.getSize() == 42).map(x -> ObjectUtils.defaultIfNull(x.getQuantity(), 0)).reduce(0, Integer::sum);
            sizeQuantityMap.put(42, size42Quantity);
            Integer size43Quantity = latestDeductList.stream().filter(x -> x.getSize() == 43).map(x -> ObjectUtils.defaultIfNull(x.getQuantity(), 0)).reduce(0, Integer::sum);
            sizeQuantityMap.put(43, size43Quantity);
            latestDeductSizeQuantityMap.put(storeProdDemandDetailId, sizeQuantityMap);
        });
        // 筛选出所有和本次需求单抵扣有关的需求单列表
        List<StoreProductDemandDetail> updateDemandDetailList = Optional.ofNullable(this.demandDetailMapper.selectList(new LambdaQueryWrapper<StoreProductDemandDetail>()
                        .in(StoreProductDemandDetail::getStoreProdDemandId, demandIdList).eq(StoreProductDemandDetail::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("获取需求单明细失败", HttpStatus.ERROR));
        // 依次对比需求单明细不同size的完成数量，并更新需求单明细的detailStatus
        List<StoreProductDemandDetail> demandDetailListAfterUpdate = updateDemandDetailList.stream().map(updateDetail -> {
            Map<Integer, Integer> latestSizeQuantityMap = latestDeductSizeQuantityMap.get(updateDetail.getId());
            if (MapUtils.isEmpty(latestSizeQuantityMap)) {
                return updateDetail;
            }
            Boolean size30Finish = ObjectUtils.defaultIfNull(latestSizeQuantityMap.get(30), 0) >= ObjectUtils.defaultIfNull(updateDetail.getSize30(), 0);
            Boolean size31Finish = ObjectUtils.defaultIfNull(latestSizeQuantityMap.get(31), 0) >= ObjectUtils.defaultIfNull(updateDetail.getSize31(), 0);
            Boolean size32Finish = ObjectUtils.defaultIfNull(latestSizeQuantityMap.get(32), 0) >= ObjectUtils.defaultIfNull(updateDetail.getSize32(), 0);
            Boolean size33Finish = ObjectUtils.defaultIfNull(latestSizeQuantityMap.get(33), 0) >= ObjectUtils.defaultIfNull(updateDetail.getSize33(), 0);
            Boolean size34Finish = ObjectUtils.defaultIfNull(latestSizeQuantityMap.get(34), 0) >= ObjectUtils.defaultIfNull(updateDetail.getSize34(), 0);
            Boolean size35Finish = ObjectUtils.defaultIfNull(latestSizeQuantityMap.get(35), 0) >= ObjectUtils.defaultIfNull(updateDetail.getSize35(), 0);
            Boolean size36Finish = ObjectUtils.defaultIfNull(latestSizeQuantityMap.get(36), 0) >= ObjectUtils.defaultIfNull(updateDetail.getSize36(), 0);
            Boolean size37Finish = ObjectUtils.defaultIfNull(latestSizeQuantityMap.get(37), 0) >= ObjectUtils.defaultIfNull(updateDetail.getSize37(), 0);
            Boolean size38Finish = ObjectUtils.defaultIfNull(latestSizeQuantityMap.get(38), 0) >= ObjectUtils.defaultIfNull(updateDetail.getSize38(), 0);
            Boolean size39Finish = ObjectUtils.defaultIfNull(latestSizeQuantityMap.get(39), 0) >= ObjectUtils.defaultIfNull(updateDetail.getSize39(), 0);
            Boolean size40Finish = ObjectUtils.defaultIfNull(latestSizeQuantityMap.get(40), 0) >= ObjectUtils.defaultIfNull(updateDetail.getSize40(), 0);
            Boolean size41Finish = ObjectUtils.defaultIfNull(latestSizeQuantityMap.get(41), 0) >= ObjectUtils.defaultIfNull(updateDetail.getSize41(), 0);
            Boolean size42Finish = ObjectUtils.defaultIfNull(latestSizeQuantityMap.get(42), 0) >= ObjectUtils.defaultIfNull(updateDetail.getSize42(), 0);
            Boolean size43Finish = ObjectUtils.defaultIfNull(latestSizeQuantityMap.get(43), 0) >= ObjectUtils.defaultIfNull(updateDetail.getSize43(), 0);
            if (size30Finish && size31Finish && size32Finish && size33Finish && size34Finish && size35Finish && size36Finish && size37Finish && size38Finish
                    && size39Finish && size40Finish && size41Finish && size42Finish && size43Finish) {
                updateDetail.setDetailStatus(3);
            }
            return updateDetail;
        }).collect(Collectors.toList());
        this.demandDetailMapper.updateById(demandDetailListAfterUpdate);
        Map<Long, List<StoreProductDemandDetail>> demandGroupMap = updateDemandDetailList.stream().collect(Collectors.groupingBy(StoreProductDemandDetail::getStoreProdDemandId));
        List<Long> demandIdListAfterUpdate = new ArrayList<>();
        demandGroupMap.forEach((demandId, tempDetailList) -> {
            if (tempDetailList.stream().allMatch(x -> x.getDetailStatus() == 3)) {
                demandIdListAfterUpdate.add(demandId);
            }
        });
        if (CollectionUtils.isNotEmpty(demandIdListAfterUpdate)) {
            this.demandMapper.updateStatusByIds(demandIdListAfterUpdate);
        }
        return count;
    }


    /**
     * 查询档口商品入库
     *
     * @param storeProdStorId 档口商品入库主键
     * @return 档口商品入库
     */
    @Override
    @Transactional(readOnly = true)
    public StoreProdStorageResDTO selectByStoreProdStorId(Long storeProdStorId) {
        // 档口商品入库
        StoreProductStorage storage = Optional.ofNullable(this.storageMapper.selectOne(new LambdaQueryWrapper<StoreProductStorage>()
                        .eq(StoreProductStorage::getId, storeProdStorId).eq(StoreProductStorage::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("档口商品入库不存在!", HttpStatus.ERROR));
        // 档口商品入库明细
        List<StoreProductStorageDetail> storageDetailList = storageDetailMapper.selectList(new LambdaQueryWrapper<StoreProductStorageDetail>()
                .eq(StoreProductStorageDetail::getStoreProdStorId, storeProdStorId).eq(StoreProductStorageDetail::getDelFlag, Constants.UNDELETED));
        return BeanUtil.toBean(storage, StoreProdStorageResDTO.class)
                .setDetailList(storageDetailList.stream().map(x -> BeanUtil.toBean(x, StoreProdStorageResDTO.StorageDetailDTO.class)).collect(Collectors.toList()));
    }


    /**
     * 撤销档口商品入库
     *
     * @param storeProdStorageId 需要撤销档口商品入库主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteByStoreProdStorId(Long storeProdStorageId) {
        // 档口商品入库
        StoreProductStorage storage = Optional.ofNullable(this.storageMapper.selectOne(new LambdaQueryWrapper<StoreProductStorage>()
                        .eq(StoreProductStorage::getId, storeProdStorageId).eq(StoreProductStorage::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("档口商品入库不存在!", HttpStatus.ERROR));
        storage.setDelFlag(Constants.DELETED);
        int count = this.storageMapper.updateById(storage);
        // 档口商品入库明细
        List<StoreProductStorageDetail> storageDetailList = storageDetailMapper.selectList(new LambdaQueryWrapper<StoreProductStorageDetail>()
                .eq(StoreProductStorageDetail::getStoreProdStorId, storeProdStorageId).eq(StoreProductStorageDetail::getDelFlag, Constants.UNDELETED));
        storageDetailList.forEach(x -> x.setDelFlag(Constants.DELETED));
        this.storageDetailMapper.updateById(storageDetailList);
        // 减少档口商品库存
        this.stockService.decreaseStock(storage.getStoreId(), storageDetailList.stream()
                .map(x -> BeanUtil.toBean(x, StoreProdStockDTO.class)).collect(Collectors.toList()));
        return count;
    }


    private Integer handleSize43RequireQuantity(Integer size43Deduct, Map<Long, Integer> size43DemandMap, StoreProductDemandDetail demandDetail) {
        final Integer size43Demand = ObjectUtils.defaultIfNull(demandDetail.getSize43(), 0);
        if (size43Deduct < 0) {
            if (size43Demand != 0) {
                size43DemandMap.put(demandDetail.getId(), size43Demand * -1);
            }
        } else {
            // 临时结果
            final int tempSize43Deduct = size43Deduct - size43Demand;
            if (tempSize43Deduct < 0) {
                size43DemandMap.put(demandDetail.getId(), tempSize43Deduct);
            }
            size43Deduct = tempSize43Deduct;
        }
        return size43Deduct;
    }

    private Integer handleSize42RequireQuantity(Integer size42Deduct, Map<Long, Integer> size42DemandMap, StoreProductDemandDetail demandDetail) {
        final Integer size42Demand = ObjectUtils.defaultIfNull(demandDetail.getSize42(), 0);
        if (size42Deduct < 0) {
            if (size42Demand != 0) {
                size42DemandMap.put(demandDetail.getId(), size42Demand * -1);
            }
        } else {
            // 临时结果
            final int tempSize42Deduct = size42Deduct - size42Demand;
            if (tempSize42Deduct < 0) {
                size42DemandMap.put(demandDetail.getId(), tempSize42Deduct);
            }
            size42Deduct = tempSize42Deduct;
        }
        return size42Deduct;
    }

    private Integer handleSize41RequireQuantity(Integer size41Deduct, Map<Long, Integer> size41DemandMap, StoreProductDemandDetail demandDetail) {
        final Integer size41Demand = ObjectUtils.defaultIfNull(demandDetail.getSize41(), 0);
        if (size41Deduct < 0) {
            if (size41Demand != 0) {
                size41DemandMap.put(demandDetail.getId(), size41Demand * -1);
            }
        } else {
            // 临时结果
            final int tempSize41Deduct = size41Deduct - size41Demand;
            if (tempSize41Deduct < 0) {
                size41DemandMap.put(demandDetail.getId(), tempSize41Deduct);
            }
            size41Deduct = tempSize41Deduct;
        }
        return size41Deduct;
    }

    private Integer handleSize40RequireQuantity(Integer size40Deduct, Map<Long, Integer> size40DemandMap, StoreProductDemandDetail demandDetail) {
        final Integer size40Demand = ObjectUtils.defaultIfNull(demandDetail.getSize40(), 0);
        if (size40Deduct < 0) {
            if (size40Demand != 0) {
                size40DemandMap.put(demandDetail.getId(), size40Demand * -1);
            }
        } else {
            // 临时结果
            final int tempSize40Deduct = size40Deduct - size40Demand;
            if (tempSize40Deduct < 0) {
                size40DemandMap.put(demandDetail.getId(), tempSize40Deduct);
            }
            size40Deduct = tempSize40Deduct;
        }
        return size40Deduct;
    }

    private Integer handleSize39RequireQuantity(Integer size39Deduct, Map<Long, Integer> size39DemandMap, StoreProductDemandDetail demandDetail) {
        final Integer size39Demand = ObjectUtils.defaultIfNull(demandDetail.getSize39(), 0);
        if (size39Deduct < 0) {
            if (size39Demand != 0) {
                size39DemandMap.put(demandDetail.getId(), size39Demand * -1);
            }
        } else {
            // 临时结果
            final int tempSize39Deduct = size39Deduct - size39Demand;
            if (tempSize39Deduct < 0) {
                size39DemandMap.put(demandDetail.getId(), tempSize39Deduct);
            }
            size39Deduct = tempSize39Deduct;
        }
        return size39Deduct;
    }

    private Integer handleSize38RequireQuantity(Integer size38Deduct, Map<Long, Integer> size38DemandMap, StoreProductDemandDetail demandDetail) {
        final Integer size38Demand = ObjectUtils.defaultIfNull(demandDetail.getSize38(), 0);
        if (size38Deduct < 0) {
            if (size38Demand != 0) {
                size38DemandMap.put(demandDetail.getId(), size38Demand * -1);
            }
        } else {
            // 临时结果
            final int tempSize38Deduct = size38Deduct - size38Demand;
            if (tempSize38Deduct < 0) {
                size38DemandMap.put(demandDetail.getId(), tempSize38Deduct);
            }
            size38Deduct = tempSize38Deduct;
        }
        return size38Deduct;
    }

    private Integer handleSize37RequireQuantity(Integer size37Deduct, Map<Long, Integer> size37DemandMap, StoreProductDemandDetail demandDetail) {
        final Integer size37Demand = ObjectUtils.defaultIfNull(demandDetail.getSize37(), 0);
        if (size37Deduct < 0) {
            if (size37Demand != 0) {
                size37DemandMap.put(demandDetail.getId(), size37Demand * -1);
            }
        } else {
            // 临时结果
            final int tempSize37Deduct = size37Deduct - size37Demand;
            if (tempSize37Deduct < 0) {
                size37DemandMap.put(demandDetail.getId(), tempSize37Deduct);
            }
            size37Deduct = tempSize37Deduct;
        }
        return size37Deduct;
    }

    private Integer handleSize36RequireQuantity(Integer size36Deduct, Map<Long, Integer> size36DemandMap, StoreProductDemandDetail demandDetail) {
        final Integer size36Demand = ObjectUtils.defaultIfNull(demandDetail.getSize36(), 0);
        if (size36Deduct < 0) {
            if (size36Demand != 0) {
                size36DemandMap.put(demandDetail.getId(), size36Demand * -1);
            }
        } else {
            // 临时结果
            final int tempSize36Deduct = size36Deduct - size36Demand;
            if (tempSize36Deduct < 0) {
                size36DemandMap.put(demandDetail.getId(), tempSize36Deduct);
            }
            size36Deduct = tempSize36Deduct;
        }
        return size36Deduct;
    }

    private Integer handleSize35RequireQuantity(Integer size35Deduct, Map<Long, Integer> size35DemandMap, StoreProductDemandDetail demandDetail) {
        final Integer size35Demand = ObjectUtils.defaultIfNull(demandDetail.getSize35(), 0);
        if (size35Deduct < 0) {
            if (size35Demand != 0) {
                size35DemandMap.put(demandDetail.getId(), size35Demand * -1);
            }
        } else {
            // 临时结果
            final int tempSize35Deduct = size35Deduct - size35Demand;
            if (tempSize35Deduct < 0) {
                size35DemandMap.put(demandDetail.getId(), tempSize35Deduct);
            }
            size35Deduct = tempSize35Deduct;
        }
        return size35Deduct;
    }

    private Integer handleSize34RequireQuantity(Integer size34Deduct, Map<Long, Integer> size34DemandMap, StoreProductDemandDetail demandDetail) {
        final Integer size34Demand = ObjectUtils.defaultIfNull(demandDetail.getSize34(), 0);
        if (size34Deduct < 0) {
            if (size34Demand != 0) {
                size34DemandMap.put(demandDetail.getId(), size34Demand * -1);
            }
        } else {
            // 临时结果
            final int tempSize34Deduct = size34Deduct - size34Demand;
            if (tempSize34Deduct < 0) {
                size34DemandMap.put(demandDetail.getId(), tempSize34Deduct);
            }
            size34Deduct = tempSize34Deduct;
        }
        return size34Deduct;
    }

    private Integer handleSize33RequireQuantity(Integer size33Deduct, Map<Long, Integer> size33DemandMap, StoreProductDemandDetail demandDetail) {
        final Integer size33Demand = ObjectUtils.defaultIfNull(demandDetail.getSize33(), 0);
        if (size33Deduct < 0) {
            if (size33Demand != 0) {
                size33DemandMap.put(demandDetail.getId(), size33Demand * -1);
            }
        } else {
            // 临时结果
            final int tempSize33Deduct = size33Deduct - size33Demand;
            if (tempSize33Deduct < 0) {
                size33DemandMap.put(demandDetail.getId(), tempSize33Deduct);
            }
            size33Deduct = tempSize33Deduct;
        }
        return size33Deduct;
    }

    private Integer handleSize32RequireQuantity(Integer size32Deduct, Map<Long, Integer> size32DemandMap, StoreProductDemandDetail demandDetail) {
        final Integer size32Demand = ObjectUtils.defaultIfNull(demandDetail.getSize32(), 0);
        if (size32Deduct < 0) {
            if (size32Demand != 0) {
                size32DemandMap.put(demandDetail.getId(), size32Demand * -1);
            }
        } else {
            // 临时结果
            final int tempSize32Deduct = size32Deduct - size32Demand;
            if (tempSize32Deduct < 0) {
                size32DemandMap.put(demandDetail.getId(), tempSize32Deduct);
            }
            size32Deduct = tempSize32Deduct;
        }
        return size32Deduct;
    }

    private Integer handleSize31RequireQuantity(Integer size31Deduct, Map<Long, Integer> size31DemandMap, StoreProductDemandDetail demandDetail) {
        final Integer size31Demand = ObjectUtils.defaultIfNull(demandDetail.getSize31(), 0);
        if (size31Deduct < 0) {
            if (size31Demand != 0) {
                size31DemandMap.put(demandDetail.getId(), size31Demand * -1);
            }
        } else {
            // 临时结果
            final int tempSize31Deduct = size31Deduct - size31Demand;
            if (tempSize31Deduct < 0) {
                size31DemandMap.put(demandDetail.getId(), tempSize31Deduct);
            }
            size31Deduct = tempSize31Deduct;
        }
        return size31Deduct;
    }

    private Integer handleSize30RequireQuantity(Integer size30Deduct, Map<Long, Integer> size30DemandMap, StoreProductDemandDetail demandDetail) {
        final Integer size30Demand = ObjectUtils.defaultIfNull(demandDetail.getSize30(), 0);
        if (size30Deduct < 0) {
            if (size30Demand != 0) {
                size30DemandMap.put(demandDetail.getId(), size30Demand * -1);
            }
        } else {
            // 临时结果
            final int tempSize30Deduct = size30Deduct - size30Demand;
            if (tempSize30Deduct < 0) {
                size30DemandMap.put(demandDetail.getId(), tempSize30Deduct);
            }
            size30Deduct = tempSize30Deduct;
        }
        return size30Deduct;
    }


}
