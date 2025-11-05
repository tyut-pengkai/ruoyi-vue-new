package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.xkt.domain.*;
import com.ruoyi.xkt.dto.storeProdColorSize.*;
import com.ruoyi.xkt.enums.StockSysType;
import com.ruoyi.xkt.mapper.*;
import com.ruoyi.xkt.service.IStoreProductColorSizeService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    final StoreProductMapper storeProdMapper;
    final StoreColorMapper storeColorMapper;
    final StoreMapper storeMapper;
    final RedisCache redisCache;

    // 纯数字
    private static final Pattern POSITIVE_PATTERN = Pattern.compile("^\\d+$");

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
        // 非纯数字 或 条码长度不合法
        if (!POSITIVE_PATTERN.matcher(snDTO.getSn()).matches()
                || !STOCK_FIXED_LENGTHS_MAP.get(stockSys).contains(snDTO.getSn().length())) {
            return new StoreSaleSnResDTO().setSuccess(Boolean.FALSE).setSn(snDTO.getSn());
        }
        // 销售出库[退货]
        if (snDTO.getRefund()) {
            // 先查storeSaleDetail中的sns条码是否存在[可能同一个条码，被一个客户多次销售、退货，则取最近销售的那条]
            final boolean isBuJu = snDTO.getSn().startsWith(snDTO.getStoreId().toString());
            // 获取条码前缀
            final String snPrefix = isBuJu ? snDTO.getSn().substring(0, STOCK_PREFIX_LENGTH_MAP.get(StockSysType.BU_JU.getValue()))
                    // 从系统设置中获取，根据系统迁移时的配置
                    : snDTO.getSn().substring(0, STOCK_PREFIX_LENGTH_MAP.get(stockSys));
            // 设置查询的条码前缀
            snDTO.setSnPrefix(snPrefix);
            StoreSaleSnResDTO barcodeResDTO = isBuJu ? this.saleDetailMapper.selectRefundByBuJuSnSale(snDTO)
                    : this.saleDetailMapper.selectRefundByOtherSnSale(snDTO);
            return ObjectUtils.isNotEmpty(barcodeResDTO) ? barcodeResDTO.setSuccess(Boolean.TRUE).setSn(snDTO.getSn())
                    // 若是没查询到数据，则走正常条码查询流程
                    : this.getSnInfo(snDTO, stockSys);
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
     * 一键更新条码
     *
     * @param otherSnDTO 条码入参
     * @return Integer
     */
    @Override
    @Transactional
    public Integer updateOtherSn(StoreUpdateOtherSnDTO otherSnDTO) {
        // 更新条码前的校验
        Integer stockSys = this.otherSnCheck(otherSnDTO);
        // 截取的条码长度
        final Integer interceptLength = Objects.equals(stockSys, StockSysType.TIAN_YOU.getValue())
                ? Constants.TIAN_YOU_SN_COMMON_PREFIX_LENGTH : Constants.FA_HUO_BAO_SN_COMMON_PREFIX_LENGTH;
        // 找到所有已录入的条码
        List<StoreProductColorSize> existList = this.prodColorSizeMapper.selectList(new LambdaQueryWrapper<StoreProductColorSize>()
                .eq(StoreProductColorSize::getDelFlag, Constants.UNDELETED)
                .in(StoreProductColorSize::getStoreColorId, otherSnDTO.getSnList().stream().map(StoreUpdateOtherSnDTO.SUOSnDTO::getStoreColorId).collect(Collectors.toList()))
                .in(StoreProductColorSize::getStoreProdId, otherSnDTO.getSnList().stream().map(StoreUpdateOtherSnDTO.SUOSnDTO::getStoreProdId).collect(Collectors.toList())));
        if (CollectionUtils.isEmpty(existList)) {
            throw new ServiceException("商品尺码不存在，请联系管理员", HttpStatus.ERROR);
        }
        // key storeProdId + storeColorId, value SUOSnDTO
        Map<String, StoreUpdateOtherSnDTO.SUOSnDTO> updateMap = otherSnDTO.getSnList().stream().collect(Collectors
                .toMap(x -> x.getStoreProdId() + ":" + x.getStoreColorId(), Function.identity()));
        // key storeProdId + storeColorId, value storeProductColorSize
        Map<String, StoreProductColorSize> existMap = existList.stream().collect(Collectors
                .toMap(x -> x.getStoreProdId() + ":" + x.getStoreColorId(), Function.identity(), (s1, s2) -> s2));
        // key storeProdId + storeColorId, value List<storeProductColorSize>
        Map<String, List<StoreProductColorSize>> existGroupMap = existList.stream().collect(Collectors.groupingBy(x -> x.getStoreProdId() + ":" + x.getStoreColorId()));
        List<StoreProductColorSize> updateSnList = new ArrayList<>();
        existMap.forEach((key, exist) -> {
            StoreUpdateOtherSnDTO.SUOSnDTO updateSn = updateMap.get(key);
            if (ObjectUtils.isNotEmpty(updateSn)) {
                final String updateCommonPrefix = updateSn.getSn().substring(0, interceptLength);
                // 未设置过条码，则直接新增
                if (StringUtils.isEmpty(exist.getOtherSnPrefix())) {
                    this.updateOtherSn(existGroupMap, key, updateCommonPrefix, updateSnList);
                } else {
                    final String existCommonPrefix = exist.getOtherSnPrefix().substring(0, interceptLength);
                    // 更新的条码和存储的条码不一致，则更新
                    if (!Objects.equals(existCommonPrefix, updateCommonPrefix)) {
                        this.updateOtherSn(existGroupMap, key, updateCommonPrefix, updateSnList);
                    }
                }
            }
        });
        return CollectionUtils.isEmpty(updateSnList) ? 0 : this.prodColorSizeMapper.updateById(updateSnList).size();
    }

    /**
     * 获取未设置条码的商品列表
     *
     * @param storeId 档口ID
     * @return StoreUnsetSnDTO
     */
    @Override
    @Transactional(readOnly = true)
    public StoreUnsetSnDTO getUnSetSnProdList(Long storeId) {
        List<StoreUnsetSnTempDTO> unsetList = this.prodColorSizeMapper.selectUnsetProdList(storeId);
        if (CollectionUtils.isEmpty(unsetList)) {
            return new StoreUnsetSnDTO();
        }
        final List<Long> storeProdIdList = unsetList.stream().map(StoreUnsetSnTempDTO::getStoreProdId).collect(Collectors.toList());
        List<StoreProduct> storeProdList = this.storeProdMapper.selectByIds(storeProdIdList);
        Map<Long, StoreProduct> storeProdMap = CollectionUtils.isEmpty(storeProdList) ? new HashMap<>()
                : storeProdList.stream().collect(Collectors.toMap(StoreProduct::getId, Function.identity()));
        final List<Long> storeColorIdList = unsetList.stream().map(StoreUnsetSnTempDTO::getStoreColorId).collect(Collectors.toList());
        List<StoreColor> storeColorList = this.storeColorMapper.selectByIds(storeColorIdList);
        Map<Long, StoreColor> storeColorMap = CollectionUtils.isEmpty(storeColorList) ? new HashMap<>()
                : storeColorList.stream().collect(Collectors.toMap(StoreColor::getId, x -> x));
        List<StoreUnsetSnDTO.SNSProdDTO> unsetResList = new ArrayList<>();
        unsetList.stream().collect(Collectors.groupingBy(StoreUnsetSnTempDTO::getStoreProdId))
                .forEach((storeProdId, colorIdList) -> unsetResList.add(new StoreUnsetSnDTO.SNSProdDTO()
                        .setStoreProdId(storeProdId).setProdArtNum(storeProdMap.get(storeProdId).getProdArtNum())
                        .setColorList(colorIdList.stream().map(x -> storeColorMap.get(x.getStoreColorId()))
                                .map(x -> new StoreUnsetSnDTO.SNSProdColorDTO().setStoreColorId(x.getId()).setColorName(x.getColorName()))
                                .collect(Collectors.toList()))));
        return new StoreUnsetSnDTO().setUnsetList(unsetResList);
    }

    /**
     * 更新数据库其它系统的条码
     *
     * @param existGroupMap      已存在的条码map
     * @param key                storeProdId:storeColorId
     * @param updateCommonPrefix 更新条码前缀
     * @param updateSnList       待更新条码列表
     */
    private void updateOtherSn(Map<String, List<StoreProductColorSize>> existGroupMap, String key,
                               String updateCommonPrefix, List<StoreProductColorSize> updateSnList) {
        List<StoreProductColorSize> groupList = existGroupMap.get(key);
        if (CollectionUtils.isNotEmpty(groupList)) {
            groupList.forEach(x -> x.setOtherSnPrefix(updateCommonPrefix + x.getSize()));
            updateSnList.addAll(groupList);
        }
    }

    /**
     * 更新条码前的校验
     *
     * @param otherSnDTO 更新入参
     * @return stockSys
     */
    private Integer otherSnCheck(StoreUpdateOtherSnDTO otherSnDTO) {
        // 判断当前库存系统 是步橘还是发货宝 或 天友
        Store store = redisCache.getCacheObject(CacheConstants.STORE_KEY + otherSnDTO.getStoreId());
        if (ObjectUtils.isEmpty(store)) {
            store = Optional.ofNullable(this.storeMapper.selectById(otherSnDTO.getStoreId()))
                    .orElseThrow(() -> new ServiceException("档口不存在!", HttpStatus.ERROR));
        }
        // 如果是步橘系统，则不能录入条码
        if (Objects.equals(store.getStockSys(), StockSysType.BU_JU.getValue())) {
            throw new ServiceException("当前库存系统为步橘网自带系统，不可录入条码!", HttpStatus.ERROR);
        }
        // 其它系统条码长度
        final Integer otherSnLength = Objects.equals(store.getStockSys(), StockSysType.TIAN_YOU.getValue())
                ? Constants.TIAN_YOU_SN_LENGTH : Constants.FA_HUO_BAO_SN_LENGTH;
        // 校验条码长度是否合法
        final Integer stockSys = this.stockSys(otherSnDTO.getStoreId());
        // 不符合规则的条码列表
        List<StoreUpdateOtherSnDTO.SUOSnDTO> illegalSnList = otherSnDTO.getSnList().stream()
                // 非纯数字 或 长度不合法
                .filter(s -> !POSITIVE_PATTERN.matcher(s.getSn()).matches() || !Objects.equals(s.getSn().length(), otherSnLength))
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(illegalSnList)) {
            String errorMsg = illegalSnList.stream().map(x -> x.getProdArtNum() + " " + x.getColorName() + ":" + x.getSn())
                    .collect(Collectors.joining(", "));
            throw new ServiceException("以下条码不符合规则: " + errorMsg, HttpStatus.ERROR);
        }
        return stockSys;
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
        if (ObjectUtils.isEmpty(barcodeResDTO)) {
            return new StoreSaleSnResDTO().setSuccess(Boolean.FALSE).setSn(snDTO.getSn());
        }
        // 设置档口客户优惠金额
        final BigDecimal discountedPrice = barcodeResDTO.getPrice()
                .subtract(ObjectUtils.defaultIfNull(barcodeResDTO.getDiscount(), BigDecimal.ZERO));
        // 销售则数量为1 退货则数量为-1
        final BigDecimal quantity = snDTO.getRefund() ? BigDecimal.ONE.negate() : BigDecimal.ONE;
        return barcodeResDTO.setSuccess(Boolean.TRUE).setSn(snDTO.getSn()).setStoreCusName(snDTO.getStoreCusName()).setDiscountedPrice(discountedPrice)
                .setQuantity(quantity).setAmount(discountedPrice.multiply(barcodeResDTO.getQuantity()));
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
