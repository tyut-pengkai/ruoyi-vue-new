package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.xkt.domain.StoreProductStorage;
import com.ruoyi.xkt.domain.StoreProductStorageDemandDeduct;
import com.ruoyi.xkt.domain.StoreProductStorageDetail;
import com.ruoyi.xkt.dto.storeProductStorageDemandDeduct.StoreProdStorageDemandDeductDTO;
import com.ruoyi.xkt.mapper.StoreProductStorageDemandDeductMapper;
import com.ruoyi.xkt.mapper.StoreProductStorageDetailMapper;
import com.ruoyi.xkt.mapper.StoreProductStorageMapper;
import com.ruoyi.xkt.service.IStoreProductStorageDemandDeducteService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.ruoyi.common.constant.Constants.*;

/**
 * 档口商品入库抵扣需求Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
@RequiredArgsConstructor
public class StoreProductStorageDemandDeducteServiceImpl implements IStoreProductStorageDemandDeducteService {

    final StoreProductStorageMapper prodStorageMapper;
    final StoreProductStorageDetailMapper productStorageDetailMapper;
    final StoreProductStorageDemandDeductMapper prodStorageDemandDeductMapper;


    /**
     * 入库单列表获取抵扣需求明细列表
     *
     * @param storeId 档口ID
     * @param storageCode 入库单号
     * @return List<StoreProdStorageDemandDeductDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public StoreProdStorageDemandDeductDTO getStorageDemandDeductList(Long storeId, String storageCode) {
        // 根据storageCode找到入库单
        StoreProductStorage storage = Optional.ofNullable(this.prodStorageMapper.selectOne(new LambdaQueryWrapper<StoreProductStorage>()
                .eq(StoreProductStorage::getCode, storageCode).eq(StoreProductStorage::getStoreId, storeId)
                .eq(StoreProductStorage::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("档口商品入库不存在!", HttpStatus.ERROR));
        List<StoreProductStorageDetail> storageDetailList = this.productStorageDetailMapper.selectList(new LambdaQueryWrapper<StoreProductStorageDetail>()
                .eq(StoreProductStorageDetail::getStoreProdStorId, storage.getId()).eq(StoreProductStorageDetail::getDelFlag, Constants.UNDELETED));
        List<StoreProductStorageDemandDeduct> demandDeductList = this.prodStorageDemandDeductMapper.selectList(new LambdaQueryWrapper<StoreProductStorageDemandDeduct>()
                .eq(StoreProductStorageDemandDeduct::getStorageCode, storageCode).eq(StoreProductStorageDemandDeduct::getDelFlag, Constants.UNDELETED));
        StoreProdStorageDemandDeductDTO dto = BeanUtil.toBean(storage, StoreProdStorageDemandDeductDTO.class).setStorageCode(storageCode);
        if (CollectionUtils.isEmpty(demandDeductList)) {
            return dto;
        }
        Map<Long, List<StoreProductStorageDemandDeduct>> demandDeductMap = demandDeductList.stream().collect(Collectors.groupingBy(StoreProductStorageDemandDeduct::getStoreProdStorageDetailId));
        List<StoreProdStorageDemandDeductDTO.SPSDDDemandDetailDTO> demandDetailDTOList = storageDetailList.stream().map(storageDetail -> {
            List<StoreProductStorageDemandDeduct> demandDetailList = demandDeductMap.get(storageDetail.getId());
            if (CollectionUtils.isEmpty(demandDetailList)) {
                return StoreProdStorageDemandDeductDTO.SPSDDDemandDetailDTO.builder()
                        .prodArtNum(storageDetail.getProdArtNum()).colorName(storageDetail.getColorName())
                        .build();
            }
            // 抵扣的需求明细列表 再按照code进行分类
            Map<String, List<StoreProductStorageDemandDeduct>> demandSizeMap = demandDetailList.stream().collect(Collectors.groupingBy(StoreProductStorageDemandDeduct::getDemandCode));
            // 生产需求单号列
            final List<String> demandCodeList = new ArrayList<String>(){{
                add("");
                // 生产需求单号列表
                demandDetailList.stream().map(StoreProductStorageDemandDeduct::getDemandCode).distinct().sorted(Comparator.comparing(x -> x)).forEach(this::add);
            }};
            // 初始化数量对比 列
            List<String> compareNumStrList = new ArrayList<String>(){{
                add("入库数量");
                // 根据生产需求单号数量 生成 对应的 抵扣数量
                demandDetailList.stream().map(StoreProductStorageDemandDeduct::getDemandCode).distinct().forEach(x -> this.add("抵扣需求数量"));
            }};
            return StoreProdStorageDemandDeductDTO.SPSDDDemandDetailDTO.builder().spsddId(storageDetail.getId())
                    .prodArtNum(storageDetail.getProdArtNum()).colorName(storageDetail.getColorName())
                    .demandCodeList(demandCodeList).compareStrList(compareNumStrList)
                    .size30List(this.getSizeQuantityList(demandCodeList, demandSizeMap, SIZE_30))
                    .size31List(this.getSizeQuantityList(demandCodeList, demandSizeMap, SIZE_31))
                    .size32List(this.getSizeQuantityList(demandCodeList, demandSizeMap, SIZE_32))
                    .size33List(this.getSizeQuantityList(demandCodeList, demandSizeMap, SIZE_33))
                    .size34List(this.getSizeQuantityList(demandCodeList, demandSizeMap, SIZE_34))
                    .size35List(this.getSizeQuantityList(demandCodeList, demandSizeMap, SIZE_35))
                    .size36List(this.getSizeQuantityList(demandCodeList, demandSizeMap, SIZE_36))
                    .size37List(this.getSizeQuantityList(demandCodeList, demandSizeMap, SIZE_37))
                    .size38List(this.getSizeQuantityList(demandCodeList, demandSizeMap, SIZE_38))
                    .size39List(this.getSizeQuantityList(demandCodeList, demandSizeMap, SIZE_39))
                    .size40List(this.getSizeQuantityList(demandCodeList, demandSizeMap, SIZE_40))
                    .size41List(this.getSizeQuantityList(demandCodeList, demandSizeMap, SIZE_41))
                    .size42List(this.getSizeQuantityList(demandCodeList, demandSizeMap, SIZE_42))
                    .size43List(this.getSizeQuantityList(demandCodeList, demandSizeMap, SIZE_43))
                    .build();
        }).collect(Collectors.toList());
        return dto.setDetailList(demandDetailDTOList);
    }



    private List<Integer> getSizeQuantityList(List<String> demandCodeList, Map<String, List<StoreProductStorageDemandDeduct>> demandSizeMap, Integer size) {
        // 每一个尺码的数量对比明细
        return demandCodeList.stream().map(demandCode -> {
            List<StoreProductStorageDemandDeduct> demandSizeList = demandSizeMap.get(demandCode);
            return CollectionUtils.isEmpty(demandSizeList) ? 0
                    : demandSizeList.stream().filter(x -> Objects.equals(x.getSize(), size))
                    .map(x -> ObjectUtils.defaultIfNull(x.getQuantity(), 0)).reduce(0, Integer::sum);
        }).collect(Collectors.toList());
    }



}
