package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.xkt.dto.storeProdColorSize.*;
import com.ruoyi.xkt.mapper.StoreProductColorSizeMapper;
import com.ruoyi.xkt.mapper.StoreSaleDetailMapper;
import com.ruoyi.xkt.service.IStoreProductColorSizeService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 档口商品颜色的尺码Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
@RequiredArgsConstructor
public class StoreProductColorSizeServiceImpl implements IStoreProductColorSizeService {

    final StoreProductColorSizeMapper prodColorSizeMapper;
    final StoreSaleDetailMapper saleDetailMapper;

    // 纯数字
    private static final Pattern POSITIVE_PATTERN = Pattern.compile("^\\d+$");
    // 步橘系统条码截止索引
    private static final Integer buJuEndIndex = 13;
    // 其它系统的条码截止索引
    private static final Integer otherSysEndIndex = 10;

    /**
     * 查询条码 对应的商品信息
     *
     * @param snDTO 查询入参
     * @return StoreProdColorSizeBarcodeResDTO
     */
    @Override
    @Transactional(readOnly = true)
    public StoreSaleSnResDTO storeSaleSn(StoreSaleSnDTO snDTO) {
        // 非纯数字，则直接返回
        if (!POSITIVE_PATTERN.matcher(snDTO.getSn()).matches()) {
            return new StoreSaleSnResDTO().setSuccess(Boolean.FALSE).setSn(snDTO.getSn());
        }
        // 销售出库[退货]
        if (snDTO.getRefund()) {
            // 先查storeSaleDetail中的sns条码是否存在[可能同一个条码，被一个客户多次销售、退货，则取最近的一条]
            StoreSaleSnResDTO barcodeResDTO = this.saleDetailMapper.selectBySn(snDTO);
            if (ObjectUtils.isNotEmpty(barcodeResDTO)) {
                return barcodeResDTO.setSuccess(Boolean.TRUE);
            } else {
                // 若是没查询到数据，则走正常条码查询流程
                return this.getSnInfo(snDTO);
            }
            // 销售出库[销售] 正常条码查询流程
        } else {
            return this.getSnInfo(snDTO);
        }
    }

    /**
     * 商品入库查询库存
     *
     * @param snDTO 条码入参
     * @return StoreProdSnsResDTO
     */
    @Override
    @Transactional(readOnly = true)
    public StoreStorageSnResDTO storageSnList(StoreProdSnDTO snDTO) {
        List<String> snList = snDTO.getSnList().stream().filter(s -> POSITIVE_PATTERN.matcher(s).matches()).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(snList)) {
            return new StoreStorageSnResDTO().setFailList(snDTO.getSnList());
        }
        // 非纯数字的条码
        List<String> failList = snDTO.getSnList().stream().filter(s -> !POSITIVE_PATTERN.matcher(s).matches()).collect(Collectors.toList());
        // 步橘网条码
        Set<String> buJuPrefixSnSet = snList.stream().filter(x -> x.startsWith(snDTO.getStoreId())).map(x -> x.substring(0, buJuEndIndex)).collect(Collectors.toSet());
        // 其它系统条码
        Set<String> otherPrefixSnSet = snList.stream().filter(x -> !x.startsWith(snDTO.getStoreId())).map(x -> x.substring(0, otherSysEndIndex)).collect(Collectors.toSet());
        // 根据条码查询数据库数据
        List<StoreStorageSnDTO.SSSDetailDTO> existList = this.prodColorSizeMapper
                .selectStorageTotalSnList(snDTO.getStoreId(), new ArrayList<>(buJuPrefixSnSet), new ArrayList<>(otherPrefixSnSet));
        // 数据库前缀对应的商品数量
        Map<String, StoreStorageSnDTO.SSSDetailDTO> existMap = existList.stream().collect(Collectors.toMap(StoreStorageSnDTO.SSSDetailDTO::getPrefixPart, x -> x));
        Set<StoreStorageSnResDTO.SSSDetailDTO> successSet = new HashSet<>();
        // 临时用来计数的list
        List<StoreStorageSnDTO.SSSDetailDTO> tempList = new ArrayList<>();
        snList.forEach(sn -> {
            String prefixPart = sn.startsWith(snDTO.getStoreId()) ? sn.substring(0, buJuEndIndex) : sn.substring(0, otherSysEndIndex);
            StoreStorageSnDTO.SSSDetailDTO exist = existMap.get(prefixPart);
            if (ObjectUtils.isNotEmpty(exist)) {
                tempList.add(exist);
                successSet.add(BeanUtil.toBean(exist, StoreStorageSnResDTO.SSSDetailDTO.class));
            } else {
                failList.add(sn);
            }
        });
        // 货号颜色对应的尺码数量map
        Map<Long, Map<Integer, Long>> prodSizeQuantityMap = tempList.stream().collect(Collectors.groupingBy(StoreStorageSnDTO.SSSDetailDTO::getStoreProdColorId, Collectors
                .groupingBy(StoreStorageSnDTO.SSSDetailDTO::getSize, Collectors.counting())));
        List<StoreStorageSnResDTO.SSSDetailDTO> successList = successSet.stream().map(x -> {
            // 商品颜色对应的尺码数量
            Map<Integer, Long> sizeQuantityMap = prodSizeQuantityMap.get(x.getStoreProdColorId());
            return MapUtils.isEmpty(sizeQuantityMap) ? x : x.setSizeCountList(sizeQuantityMap.entrySet().stream()
                    .map(entry -> new StoreStorageSnResDTO.SSSSizeCountDTO().setSize(entry.getKey()).setCount(entry.getValue())).collect(Collectors.toList()));
        }).collect(Collectors.toList());
        return new StoreStorageSnResDTO().setSuccessList(successList).setFailList(failList);
    }

    /**
     * 库存盘点查询库存
     *
     * @param snDTO 条码入参
     * @return StoreStorageSnResDTO
     */
    @Override
    @Transactional(readOnly = true)
    public StoreStockTakingSnResDTO stockTakingSnList(StoreStockTakingSnDTO snDTO) {
        List<String> snList = snDTO.getSnList().stream().filter(s -> POSITIVE_PATTERN.matcher(s).matches()).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(snList)) {
            return new StoreStockTakingSnResDTO().setFailList(snDTO.getSnList());
        }
        // 非纯数字的条码
        List<String> failList = snDTO.getSnList().stream().filter(s -> !POSITIVE_PATTERN.matcher(s).matches()).collect(Collectors.toList());
        // 步橘网条码
        Set<String> buJuPrefixSnSet = snList.stream().filter(x -> x.startsWith(snDTO.getStoreId())).map(x -> x.substring(0, buJuEndIndex)).collect(Collectors.toSet());
        // 其它系统条码
        Set<String> otherPrefixSnSet = snList.stream().filter(x -> !x.startsWith(snDTO.getStoreId())).map(x -> x.substring(0, otherSysEndIndex)).collect(Collectors.toSet());
        // 查询出的所有条码
        List<StoreStockTakingSnTempDTO.SSTSTDetailDTO> existList = this.prodColorSizeMapper.selectStockSnList(snDTO.getStoreId(),
                new ArrayList<>(buJuPrefixSnSet), new ArrayList<>(otherPrefixSnSet));
        Map<String, StoreStockTakingSnTempDTO.SSTSTDetailDTO> existMap = existList.stream().collect(Collectors.toMap(StoreStockTakingSnTempDTO.SSTSTDetailDTO::getPrefixPart, x -> x));
        // 唯一的 storeProdColorId + size
        Set<StoreStockTakingSnResDTO.SSTSDetailDTO> stockSet = new HashSet<>();
        // 获取数量的列表
        List<StoreStockTakingSnTempDTO.SSTSTDetailDTO> tempList = new ArrayList<>();
        snList.forEach(sn -> {
            String prefixPart = sn.startsWith(snDTO.getStoreId()) ? sn.substring(0, buJuEndIndex) : sn.substring(0, otherSysEndIndex);
            StoreStockTakingSnTempDTO.SSTSTDetailDTO exist = existMap.get(prefixPart);
            if (ObjectUtils.isNotEmpty(exist)) {
                tempList.add(exist);
                stockSet.add(BeanUtil.toBean(exist, StoreStockTakingSnResDTO.SSTSDetailDTO.class));
            } else {
                failList.add(sn);
            }
        });
        // 每个颜色对应的尺码数量map
        Map<Long, Map<Integer, Long>> prodSizeQuantityMap = tempList.stream().collect(Collectors.groupingBy(StoreStockTakingSnTempDTO.SSTSTDetailDTO::getStoreProdColorId,
                Collectors.groupingBy(StoreStockTakingSnTempDTO.SSTSTDetailDTO::getSize, Collectors.counting())));
        List<StoreStockTakingSnResDTO.SSTSDetailDTO> successList = stockSet.stream().map(stock -> {
            // 商品颜色对应的尺码数量
            Map<Integer, Long> sizeQuantityMap = prodSizeQuantityMap.get(stock.getStoreProdColorId());
            return MapUtils.isEmpty(sizeQuantityMap) ? stock : stock.setStock(sizeQuantityMap.get(stock.getSize()));
        }).collect(Collectors.toList());
        return new StoreStockTakingSnResDTO().setFailList(failList).setSuccessList(successList);
    }

    /**
     * 普通销售流程获取条码对应的商品信息
     *
     * @param snDTO 条码入参
     * @return StoreSaleBarcodeResDTO
     */
    private StoreSaleSnResDTO getSnInfo(StoreSaleSnDTO snDTO) {
        StoreSaleSnResDTO barcodeResDTO;
        // 步橘网生成的条码
        if (snDTO.getSn().startsWith(snDTO.getStoreId())) {
            final String prefixPart = snDTO.getSn().substring(0, buJuEndIndex);
            // 查询数据库 获取条码对应的商品信息
            barcodeResDTO = prodColorSizeMapper.selectSn(prefixPart, snDTO.getStoreId(), snDTO.getStoreCusId());
        } else {
            // 从系统设置中获取，根据系统迁移时的配置

            final String prefixPart = snDTO.getSn().substring(0, otherSysEndIndex);
            // 查询数据库 获取条码对应的商品信息
            barcodeResDTO = prodColorSizeMapper.selectOtherSn(prefixPart, snDTO.getStoreId(), snDTO.getStoreCusId());
        }
        return ObjectUtils.isEmpty(barcodeResDTO) ? new StoreSaleSnResDTO().setSuccess(Boolean.FALSE).setSn(snDTO.getSn())
                : barcodeResDTO.setSuccess(Boolean.TRUE).setSn(snDTO.getSn());
    }


}
