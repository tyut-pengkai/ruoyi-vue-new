package com.ruoyi.web.controller.xkt.shipMaster;

import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.web.controller.xkt.shipMaster.vo.shipMaster.ShipMasterCusDiscountVO;
import com.ruoyi.web.controller.xkt.shipMaster.vo.shipMaster.ShipMasterCusVO;
import com.ruoyi.web.controller.xkt.shipMaster.vo.shipMaster.ShipMasterProdStockVO;
import com.ruoyi.web.controller.xkt.shipMaster.vo.shipMaster.ShipMasterProdVO;
import com.ruoyi.xkt.service.shipMaster.IShipMasterService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * ShipMaster 相关
 *
 * @author ruoyi
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/v1/ship-master")
public class ShipMasterController extends BaseController {

    final IShipMasterService shipMasterService;
    final RedisCache redisCache;

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @PostMapping("/prod/cache")
    public R<Integer> createProdCache(@Validated @RequestBody ShipMasterProdVO prodVO) {
        // 供应商ID
        final Integer supplierId = prodVO.getData().getRecords().get(0).getSupplierId();
        if (ObjectUtils.isEmpty(prodVO.getData()) || CollectionUtils.isEmpty(prodVO.getData().getRecords())) {
            return R.ok();
        }
        // 先从redis中获取列表数据
        List<ShipMasterProdVO.SMIVO> cacheList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_SUPPLIER_PROD_KEY + supplierId), new ArrayList<>());
        CollectionUtils.addAll(cacheList, prodVO.getData().getRecords());
        // 存到redis中
        redisCache.setCacheObject(CacheConstants.MIGRATION_SUPPLIER_PROD_KEY + supplierId, cacheList, 5, TimeUnit.DAYS);
        return R.ok();
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @PostMapping("/cus/cache")
    public R<Integer> createCusCache(@Validated @RequestBody ShipMasterCusVO cusVO) {
        final Integer supplierId = cusVO.getData().getRecords().get(0).getSupplierId();
        if (ObjectUtils.isEmpty(cusVO.getData()) || CollectionUtils.isEmpty(cusVO.getData().getRecords())) {
            return R.ok();
        }
        // 先从redis中获取列表数据
        List<ShipMasterCusVO.SMCVO> cacheList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_SUPPLIER_CUS_KEY + supplierId), new ArrayList<>());
        CollectionUtils.addAll(cacheList, cusVO.getData().getRecords());
        // 存到redis中
        redisCache.setCacheObject(CacheConstants.MIGRATION_SUPPLIER_CUS_KEY + supplierId, cacheList, 5, TimeUnit.DAYS);
        return R.ok();
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @PostMapping("/prod/stock/cache/{supplierId}")
    public R<Integer> createProdStockCache(@PathVariable Integer supplierId, @Validated @RequestBody ShipMasterProdStockVO stockVO) {
        // 供应商ID
        if (ObjectUtils.isEmpty(stockVO.getData()) || ObjectUtils.isEmpty(stockVO.getData().getList())) {
            return R.ok();
        }
        // 先从redis中获取列表数据
        List<ShipMasterProdStockVO.SMPSRecordVO> cacheList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_SUPPLIER_PROD_STOCK_KEY + supplierId), new ArrayList<>());
        CollectionUtils.addAll(cacheList, stockVO.getData().getList().getRecords());
        // 存到redis中
        redisCache.setCacheObject(CacheConstants.MIGRATION_SUPPLIER_PROD_STOCK_KEY + supplierId, cacheList, 5, TimeUnit.DAYS);
        return R.ok();
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @PostMapping("/cus/discount/cache")
    public R<Integer> createCusDiscountCache(@Validated @RequestBody ShipMasterCusDiscountVO cusDiscountVO) {
        final Integer supplierId = cusDiscountVO.getData().getRecords().get(0).getSupplierId();
        // 供应商ID
        if (ObjectUtils.isEmpty(cusDiscountVO.getData()) || ObjectUtils.isEmpty(cusDiscountVO.getData().getRecords())) {
            return R.ok();
        }
        // 设置优惠价格
        cusDiscountVO.getData().getRecords().forEach(record -> record.setDiscount(record.getSupplyPrice() - record.getCustomerPrice()));
        // 先从redis中获取列表数据
        List<ShipMasterCusDiscountVO.SMCDRecordVO> cacheList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_SUPPLIER_CUS_DISCOUNT_KEY + supplierId), new ArrayList<>());
        CollectionUtils.addAll(cacheList, cusDiscountVO.getData().getRecords());
        // 存到redis中
        redisCache.setCacheObject(CacheConstants.MIGRATION_SUPPLIER_CUS_DISCOUNT_KEY + supplierId, cacheList, 5, TimeUnit.DAYS);
        return R.ok();
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @GetMapping("/prod/cache/{supplierId}")
    public R<Integer> getProdCache(@PathVariable Integer supplierId) {
        // 从redis中获取数据
        List<ShipMasterProdVO.SMIVO> cacheList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_SUPPLIER_PROD_KEY + supplierId), new ArrayList<>());
        if (CollectionUtils.isEmpty(cacheList)) {
            return R.fail();
        }
        // 按照artNo分组
        Map<String, List<ShipMasterProdVO.SMIVO>> artNoGroup = cacheList.stream()
                .sorted(Comparator.comparing(ShipMasterProdVO.SMIVO::getArtNo)
                        .thenComparing(ShipMasterProdVO.SMIVO::getColor))
                .collect(Collectors
                        .groupingBy(ShipMasterProdVO.SMIVO::getArtNo, LinkedHashMap::new, Collectors.toList()));
        // 货号 颜色 对应的条码前缀
        Map<String, Map<String, String>> artSnPrefixMap = new LinkedHashMap<>();
        // 货号 颜色 map
        Map<String, List<String>> artNoColorMap = new LinkedHashMap<>();
        artNoGroup.forEach((artNo, colorList) -> {
            artNoColorMap.put(artNo, colorList.stream().map(ShipMasterProdVO.SMIVO::getColor).collect(Collectors.toList()));
            Map<String, String> snPrefixMap = new LinkedHashMap<>();
            // 按照颜色设置条码前缀
            colorList.forEach(color -> snPrefixMap
                    .put(color.getColor(), color.getSupplierId() + String.format("%05d", color.getSupplierSkuId())));
            artSnPrefixMap.put(artNo, snPrefixMap);
        });
        artSnPrefixMap.forEach((k, v) -> {
            System.err.println(k + ":" + v);
        });
        artNoColorMap.forEach((k, v) -> {
            System.err.println(k + ":" + v);
        });
        return R.ok();
    }


    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @GetMapping("/cus/cache/{supplierId}")
    public R<Integer> getCusCache(@PathVariable Integer supplierId) {
        // 从redis中获取数据
        List<ShipMasterCusVO.SMCVO> cacheList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_SUPPLIER_CUS_KEY + supplierId), new ArrayList<>());
        if (CollectionUtils.isEmpty(cacheList)) {
            return R.fail();
        }
        Map<Integer, List<ShipMasterCusVO.SMCVO>> cusGroupMap = cacheList.stream().collect(Collectors
                .groupingBy(ShipMasterCusVO.SMCVO::getSupplierId, LinkedHashMap::new, Collectors.toList()));
        System.err.println(cusGroupMap);
        return R.ok();
    }


}
