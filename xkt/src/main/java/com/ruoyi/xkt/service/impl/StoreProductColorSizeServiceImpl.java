package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.xkt.domain.Store;
import com.ruoyi.xkt.domain.StoreProductColorSize;
import com.ruoyi.xkt.domain.StoreProductStock;
import com.ruoyi.xkt.dto.storeProdColorSize.*;
import com.ruoyi.xkt.enums.StockSysType;
import com.ruoyi.xkt.mapper.StoreMapper;
import com.ruoyi.xkt.mapper.StoreProductColorSizeMapper;
import com.ruoyi.xkt.mapper.StoreProductStockMapper;
import com.ruoyi.xkt.mapper.StoreSaleDetailMapper;
import com.ruoyi.xkt.service.IStoreProductColorSizeService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
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
    final StoreProductStockMapper prodStockMapper;
    final StoreMapper storeMapper;
    final RedisCache redisCache;

    // 纯数字
    private static final Pattern POSITIVE_PATTERN = Pattern.compile("^\\d+$");
/*    // 步橘系统条码截止索引
    private static final Integer buJuEndIndex = 13;
    // 其它系统的条码截止索引
    private static final Integer otherSysEndIndex = 10;*/

    // 当前库存系统的条码前缀长度
    private static final Map<Integer, Integer> STOCK_PREFIX_LENGTH_MAP = new HashMap<>();
    // 当前库存系统的条码长度
    private static final Map<Integer, Set<Integer>> STOCK_FIXED_LENGTHS_MAP = new HashMap<>();

    static {
        STOCK_PREFIX_LENGTH_MAP.put(StockSysType.BU_JU.getValue(), Constants.BU_JU_SN_PREFIX_LENGTH);
        STOCK_PREFIX_LENGTH_MAP.put(StockSysType.TIAN_YOU.getValue(), Constants.TIAN_YOU_SN_PREFIX_LENGTH);
        STOCK_PREFIX_LENGTH_MAP.put(StockSysType.FA_HUO_BAO.getValue(), Constants.FA_HUO_BAO_SN_PREFIX_LENGTH);
    }

    static {
        STOCK_FIXED_LENGTHS_MAP.put(StockSysType.BU_JU.getValue(), Collections
                .unmodifiableSet(new HashSet<>(Collections.singletonList(Constants.BU_JU_SN_LENGTH))));
        STOCK_FIXED_LENGTHS_MAP.put(StockSysType.TIAN_YOU.getValue(), Collections
                .unmodifiableSet(new HashSet<>(Arrays.asList(Constants.TIAN_YOU_SN_LENGTH, Constants.BU_JU_SN_LENGTH))));
        STOCK_FIXED_LENGTHS_MAP.put(StockSysType.FA_HUO_BAO.getValue(), Collections
                .unmodifiableSet(new HashSet<>(Arrays.asList(Constants.FA_HUO_BAO_SN_LENGTH, Constants.BU_JU_SN_LENGTH))));
    }


    /**
     * 查询条码 对应的商品信息
     *
     * @param snDTO 查询入参
     * @return StoreProdColorSizeBarcodeResDTO
     */
    @Override
    @Transactional(readOnly = true)
    public StoreSaleSnResDTO storeSaleSn(StoreSaleSnDTO snDTO) {
        // 用户是否为档口管理者或子账户
        if (!SecurityUtils.isAdmin() && !SecurityUtils.isStoreManagerOrSub(snDTO.getStoreId())) {
            throw new ServiceException("当前用户非档口管理者或子账号，无权限操作!", HttpStatus.ERROR);
        }
        final Integer stockSys = this.stockSys(snDTO.getStoreId());
        // 非纯数字 且 条码长度不合法
        if (!POSITIVE_PATTERN.matcher(snDTO.getSn()).matches()
                && !STOCK_FIXED_LENGTHS_MAP.get(stockSys).contains(snDTO.getSn().length())) {
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
                return this.getSnInfo(snDTO, stockSys);
            }
            // 销售出库[销售] 正常条码查询流程
        } else {
            return this.getSnInfo(snDTO, stockSys);
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
        // 用户是否为档口管理者或子账户
        if (!SecurityUtils.isAdmin() && !SecurityUtils.isStoreManagerOrSub(snDTO.getStoreId())) {
            throw new ServiceException("当前用户非档口管理者或子账号，无权限操作!", HttpStatus.ERROR);
        }
        // 校验条码长度是否合法
        final Integer stockSys = this.stockSys(snDTO.getStoreId());
        // 符合规则的条码列表
        List<String> validSnList = snDTO.getSnList().stream()
                // 为纯数字 且 条码长度合法
                .filter(s -> POSITIVE_PATTERN.matcher(s).matches() && STOCK_FIXED_LENGTHS_MAP.get(stockSys).contains(s.length()))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(validSnList)) {
            // 全部都是错误条码
            return new StoreStorageSnResDTO().setFailList(snDTO.getSnList());
        }
        // 非法的条码
        List<String> failList = snDTO.getSnList().stream().filter(s -> !validSnList.contains(s)).collect(Collectors.toList());
        // 步橘网条码
        Set<String> buJuPrefixSnSet = validSnList.stream().filter(x -> x.startsWith(snDTO.getStoreId().toString()))
                .map(x -> x.substring(0, STOCK_PREFIX_LENGTH_MAP.get(StockSysType.BU_JU.getValue()))).collect(Collectors.toSet());
        // 其它系统条码
        Set<String> otherPrefixSnSet = Objects.equals(StockSysType.BU_JU.getValue(), stockSys) ? new HashSet<>()
                : validSnList.stream().filter(x -> !x.startsWith(snDTO.getStoreId().toString()))
                .map(x -> x.substring(0, STOCK_PREFIX_LENGTH_MAP.get(stockSys))).collect(Collectors.toSet());
        List<StoreStorageSnDTO.SSSDetailDTO> existList = new ArrayList<>();
        // 根据条码查询数据库数据
        if (CollectionUtils.isNotEmpty(buJuPrefixSnSet)) {
            List<StoreStorageSnDTO.SSSDetailDTO> buJuExistList = this.prodColorSizeMapper.selectStorageBuJuSnList(snDTO.getStoreId(), new ArrayList<>(buJuPrefixSnSet));
            CollectionUtils.addAll(existList, buJuExistList);
        }
        if (CollectionUtils.isNotEmpty(otherPrefixSnSet)) {
            List<StoreStorageSnDTO.SSSDetailDTO> otherExistList = this.prodColorSizeMapper.selectStorageOtherSnList(snDTO.getStoreId(), new ArrayList<>(otherPrefixSnSet));
            CollectionUtils.addAll(existList, otherExistList);
        }
        if (CollectionUtils.isEmpty(existList)) {
            // 全部都是错误条码
            return new StoreStorageSnResDTO().setFailList(snDTO.getSnList());
        }
        // 数据库前缀对应的商品数量
        Map<String, StoreStorageSnDTO.SSSDetailDTO> existMap = existList.stream().collect(Collectors.toMap(StoreStorageSnDTO.SSSDetailDTO::getPrefixPart, x -> x));
        Set<StoreStorageSnResDTO.SSSDetailDTO> successSet = new HashSet<>();
        // 临时用来计数的list
        List<StoreStorageSnDTO.SSSDetailDTO> tempList = new ArrayList<>();
        validSnList.forEach(sn -> {
            String prefixPart = sn.startsWith(snDTO.getStoreId().toString())
                    ? sn.substring(0, STOCK_PREFIX_LENGTH_MAP.get(StockSysType.BU_JU.getValue()))
                    : sn.substring(0, STOCK_PREFIX_LENGTH_MAP.get(stockSys));
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
        // 用户是否为档口管理者或子账户
        if (!SecurityUtils.isAdmin() && !SecurityUtils.isStoreManagerOrSub(snDTO.getStoreId())) {
            throw new ServiceException("当前用户非档口管理者或子账号，无权限操作!", HttpStatus.ERROR);
        }
        // 校验条码长度是否合法
        final Integer stockSys = this.stockSys(snDTO.getStoreId());
        // 符合规则的条码列表
        List<String> validSnList = snDTO.getSnList().stream()
                // 为纯数字 且 条码长度合法
                .filter(s -> POSITIVE_PATTERN.matcher(s).matches() && STOCK_FIXED_LENGTHS_MAP.get(stockSys).contains(s.length()))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(validSnList)) {
            // 全部都是错误条码
            return new StoreStockTakingSnResDTO().setFailList(snDTO.getSnList());
        }
        // 非法的条码
        List<String> failList = snDTO.getSnList().stream().filter(s -> !validSnList.contains(s)).collect(Collectors.toList());
        // 步橘网条码
        Set<String> buJuPrefixSnSet = validSnList.stream().filter(x -> x.startsWith(snDTO.getStoreId().toString()))
                .map(x -> x.substring(0, STOCK_PREFIX_LENGTH_MAP.get(StockSysType.BU_JU.getValue()))).collect(Collectors.toSet());
        // 其它系统条码
        Set<String> otherPrefixSnSet = Objects.equals(StockSysType.BU_JU.getValue(), stockSys) ? new HashSet<>()
                : validSnList.stream().filter(x -> !x.startsWith(snDTO.getStoreId().toString()))
                .map(x -> x.substring(0, STOCK_PREFIX_LENGTH_MAP.get(stockSys))).collect(Collectors.toSet());
        List<StoreStockTakingSnTempDTO.SSTSTDetailDTO> existList = new ArrayList<>();
        // 查询出的所有条码
        if (CollectionUtils.isNotEmpty(buJuPrefixSnSet)) {
            List<StoreStockTakingSnTempDTO.SSTSTDetailDTO> buJuExistList = this.prodColorSizeMapper.selectStockBuJuSnList(snDTO.getStoreId(), new ArrayList<>(buJuPrefixSnSet));
            CollectionUtils.addAll(existList, buJuExistList);
        }
        if (CollectionUtils.isNotEmpty(otherPrefixSnSet)) {
            List<StoreStockTakingSnTempDTO.SSTSTDetailDTO> otherExistList = this.prodColorSizeMapper.selectStockOtherSnList(snDTO.getStoreId(), new ArrayList<>(otherPrefixSnSet));
            CollectionUtils.addAll(existList, otherExistList);
        }
        if (CollectionUtils.isEmpty(existList)) {
            // 全部都是错误条码
            return new StoreStockTakingSnResDTO().setFailList(snDTO.getSnList());
        }
        Map<String, StoreStockTakingSnTempDTO.SSTSTDetailDTO> existMap = existList.stream().collect(Collectors.toMap(StoreStockTakingSnTempDTO.SSTSTDetailDTO::getPrefixPart, x -> x));
        // 唯一的 storeProdColorId + size
        Set<StoreStockTakingSnResDTO.SSTSDetailDTO> stockSet = new HashSet<>();
        // 获取数量的列表
        List<StoreStockTakingSnTempDTO.SSTSTDetailDTO> tempList = new ArrayList<>();
        validSnList.forEach(sn -> {
            String prefixPart = sn.startsWith(snDTO.getStoreId().toString())
                    ? sn.substring(0, STOCK_PREFIX_LENGTH_MAP.get(StockSysType.BU_JU.getValue()))
                    : sn.substring(0, STOCK_PREFIX_LENGTH_MAP.get(stockSys));
            StoreStockTakingSnTempDTO.SSTSTDetailDTO exist = existMap.get(prefixPart);
            if (ObjectUtils.isNotEmpty(exist)) {
                tempList.add(exist);
                stockSet.add(BeanUtil.toBean(exist, StoreStockTakingSnResDTO.SSTSDetailDTO.class));
            } else {
                failList.add(sn);
            }
        });
        // 获取数据库该货号 颜色 已存在的库存数量
        List<StoreProductStock> existsStockList = this.prodStockMapper.selectList(new LambdaQueryWrapper<StoreProductStock>()
                .eq(StoreProductStock::getDelFlag, Constants.UNDELETED).in(StoreProductStock::getStoreProdColorId, tempList.stream()
                        .map(StoreStockTakingSnTempDTO.SSTSTDetailDTO::getStoreProdColorId).distinct().collect(Collectors.toList())));
        Map<Long, StoreProductStock> existsStockMap = CollectionUtils.isEmpty(existsStockList) ? new HashMap<>()
                : existsStockList.stream().collect(Collectors.toMap(StoreProductStock::getStoreProdColorId, x -> x));
        // 每个颜色对应的尺码数量map
        Map<Long, Map<Integer, Long>> prodSizeQuantityMap = tempList.stream().collect(Collectors.groupingBy(StoreStockTakingSnTempDTO.SSTSTDetailDTO::getStoreProdColorId,
                Collectors.groupingBy(StoreStockTakingSnTempDTO.SSTSTDetailDTO::getSize, Collectors.counting())));
        List<StoreStockTakingSnResDTO.SSTSDetailDTO> successList = stockSet.stream().map(stock -> {
            StoreProductStock existStock = existsStockMap.get(stock.getStoreProdColorId());
            if (ObjectUtils.isEmpty(existStock)) {
                // 初始化为0
                stock.setExistStock(0);
            } else {
                if (Objects.equals(stock.getSize(), Constants.SIZE_30)) {
                    stock.setExistStock(ObjectUtils.defaultIfNull(existStock.getSize30(), 0));
                } else if (Objects.equals(stock.getSize(), Constants.SIZE_31)) {
                    stock.setExistStock(ObjectUtils.defaultIfNull(existStock.getSize31(), 0));
                } else if (Objects.equals(stock.getSize(), Constants.SIZE_32)) {
                    stock.setExistStock(ObjectUtils.defaultIfNull(existStock.getSize32(), 0));
                } else if (Objects.equals(stock.getSize(), Constants.SIZE_33)) {
                    stock.setExistStock(ObjectUtils.defaultIfNull(existStock.getSize33(), 0));
                } else if (Objects.equals(stock.getSize(), Constants.SIZE_34)) {
                    stock.setExistStock(ObjectUtils.defaultIfNull(existStock.getSize34(), 0));
                } else if (Objects.equals(stock.getSize(), Constants.SIZE_35)) {
                    stock.setExistStock(ObjectUtils.defaultIfNull(existStock.getSize35(), 0));
                } else if (Objects.equals(stock.getSize(), Constants.SIZE_36)) {
                    stock.setExistStock(ObjectUtils.defaultIfNull(existStock.getSize36(), 0));
                } else if (Objects.equals(stock.getSize(), Constants.SIZE_37)) {
                    stock.setExistStock(ObjectUtils.defaultIfNull(existStock.getSize37(), 0));
                } else if (Objects.equals(stock.getSize(), Constants.SIZE_38)) {
                    stock.setExistStock(ObjectUtils.defaultIfNull(existStock.getSize38(), 0));
                } else if (Objects.equals(stock.getSize(), Constants.SIZE_39)) {
                    stock.setExistStock(ObjectUtils.defaultIfNull(existStock.getSize39(), 0));
                } else if (Objects.equals(stock.getSize(), Constants.SIZE_40)) {
                    stock.setExistStock(ObjectUtils.defaultIfNull(existStock.getSize40(), 0));
                } else if (Objects.equals(stock.getSize(), Constants.SIZE_41)) {
                    stock.setExistStock(ObjectUtils.defaultIfNull(existStock.getSize41(), 0));
                } else if (Objects.equals(stock.getSize(), Constants.SIZE_42)) {
                    stock.setExistStock(ObjectUtils.defaultIfNull(existStock.getSize42(), 0));
                } else if (Objects.equals(stock.getSize(), Constants.SIZE_43)) {
                    stock.setExistStock(ObjectUtils.defaultIfNull(existStock.getSize43(), 0));
                }
            }
            // 商品颜色对应的尺码数量
            Map<Integer, Long> sizeQuantityMap = prodSizeQuantityMap.get(stock.getStoreProdColorId());
            return MapUtils.isEmpty(sizeQuantityMap) ? stock : stock.setStock(sizeQuantityMap.get(stock.getSize()));
        }).collect(Collectors.toList());
        return new StoreStockTakingSnResDTO().setFailList(failList).setSuccessList(successList);
    }

    /**
     * 打印条码时获取条码
     *
     * @param snDTO 条码入参
     * @return StorePrintSnResDTO
     */
    @Override
    @Transactional
    public List<StorePrintSnResDTO> getPrintSnList(StorePrintSnDTO snDTO) {
        // 用户是否为档口管理者或子账户
        if (!SecurityUtils.isAdmin() && !SecurityUtils.isStoreManagerOrSub(snDTO.getStoreId())) {
            throw new ServiceException("当前用户非档口管理者或子账号，无权限操作!", HttpStatus.ERROR);
        }
        // 获取商品颜色尺码基础数据
        List<Long> storeProdIdList = snDTO.getColorSizeList().stream().map(StorePrintSnDTO.SPColorSizeVO::getStoreProdId).distinct().collect(Collectors.toList());
        List<Long> storeColorIdList = snDTO.getColorSizeList().stream().map(StorePrintSnDTO.SPColorSizeVO::getStoreColorId).distinct().collect(Collectors.toList());
        List<StoreProductColorSize> colorSizeList = this.prodColorSizeMapper.selectList(new LambdaQueryWrapper<StoreProductColorSize>()
                .eq(StoreProductColorSize::getDelFlag, Constants.UNDELETED).in(StoreProductColorSize::getStoreProdId, storeProdIdList)
                .in(StoreProductColorSize::getStoreColorId, storeColorIdList));
        if (CollectionUtils.isEmpty(colorSizeList)) {
            return new ArrayList<>();
        }
        // key storeProdId + storeColorId + size, value storeProductColorSize
        Map<String, StoreProductColorSize> colorSizeMap = colorSizeList.stream().collect(Collectors
                .toMap(x -> x.getStoreProdId() + ":" + x.getStoreColorId() + ":" + x.getSize(), Function.identity()));
        // 待更新列表
        List<StoreProductColorSize> updateList = new ArrayList<>();
        // 返回给前端的待打印条码列表
        List<StorePrintSnResDTO> printSnList = new ArrayList<>();
        for (int i = 0; i < snDTO.getColorSizeList().size(); i++) {
            final StorePrintSnDTO.SPColorSizeVO colorSizeVO = snDTO.getColorSizeList().get(i);
            List<StorePrintSnDTO.SPSizeVO> sizeQuantityList = colorSizeVO.getSizeQuantityList();
            // storeProductColorId 下 所有的条码
            List<StorePrintSnResDTO.SPSizeSnDTO> sizeSnList = new ArrayList<>();
            for (final StorePrintSnDTO.SPSizeVO quantityVO : sizeQuantityList) {
                // 获取系统条码对应信息
                final String key = colorSizeVO.getStoreProdId() + ":" + colorSizeVO.getStoreColorId() + ":" + quantityVO.getSize();
                StoreProductColorSize colorSize = colorSizeMap.get(key);
                if (ObjectUtils.isEmpty(colorSize)) {
                    continue;
                }
                // 待打印的下一个条码
                Integer nextSn = colorSize.getNextSn();
                // 当前尺码对应的条码列表
                List<String> snList = new ArrayList<>();
                for (int k = 0; k < quantityVO.getQuantity(); k++) {
                    nextSn += 1;
                    // 更新下一次打印的条码编号
                    // 取 colorSize 的 nextSn 字段，不足8位填充为8位
                    snList.add(colorSize.getSnPrefix() + String.format("%08d", nextSn));
                }
                // 更新下一个待打印条码开始值
                updateList.add(colorSize.setNextSn(nextSn));
                sizeSnList.add(new StorePrintSnResDTO.SPSizeSnDTO().setSize(quantityVO.getSize()).setSnList(snList));
            }
            printSnList.add(new StorePrintSnResDTO().setStoreProdColorId(colorSizeVO.getStoreProdColorId()).setSizeSnList(sizeSnList));
        }
        if (CollectionUtils.isNotEmpty(updateList)) {
            this.prodColorSizeMapper.updateById(updateList);
        }
        return printSnList;
    }

    /**
     * 普通销售流程获取条码对应的商品信息
     *
     * @param snDTO    条码入参
     * @param stockSys 使用的库存系统
     * @return StoreSaleBarcodeResDTO
     */
    private StoreSaleSnResDTO getSnInfo(StoreSaleSnDTO snDTO, Integer stockSys) {
        StoreSaleSnResDTO barcodeResDTO;
        // 步橘网生成的条码
        if (snDTO.getSn().startsWith(snDTO.getStoreId().toString())) {
            final String prefixPart = snDTO.getSn().substring(0, STOCK_PREFIX_LENGTH_MAP.get(StockSysType.BU_JU.getValue()));
            // 查询数据库 获取条码对应的商品信息
            barcodeResDTO = prodColorSizeMapper.selectSn(prefixPart, snDTO.getStoreId(), snDTO.getStoreCusId());
        } else {
            // 从系统设置中获取，根据系统迁移时的配置
            final String prefixPart = snDTO.getSn().substring(0, STOCK_PREFIX_LENGTH_MAP.get(stockSys));
            // 查询数据库 获取条码对应的商品信息
            barcodeResDTO = prodColorSizeMapper.selectOtherSn(prefixPart, snDTO.getStoreId(), snDTO.getStoreCusId());
        }
        return ObjectUtils.isEmpty(barcodeResDTO) ? new StoreSaleSnResDTO().setSuccess(Boolean.FALSE).setSn(snDTO.getSn())
                : barcodeResDTO.setSuccess(Boolean.TRUE).setSn(snDTO.getSn());
    }


    /**
     * 校验条码长度是否合法
     *
     * @param storeId 档口ID
     * @return Set
     */
    private Integer stockSys(Long storeId) {
        // 获取当前档口的库存系统是：步橘网、天友、发货宝 的哪一个
        Store store = this.redisCache.getCacheObject(CacheConstants.STORE_KEY + storeId);
        if (ObjectUtils.isEmpty(store)) {
            store = Optional.ofNullable(this.storeMapper.selectById(storeId)).orElseThrow(() -> new ServiceException("档口不存在!", HttpStatus.ERROR));
            this.redisCache.setCacheObject(CacheConstants.STORE_KEY + storeId, store);
        }
        return store.getStockSys();
    }


}
