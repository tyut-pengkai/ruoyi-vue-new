package com.ruoyi.web.controller.xkt.migartion;

import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.web.controller.xkt.migartion.vo.fhb.FhbCusDiscountVO;
import com.ruoyi.web.controller.xkt.migartion.vo.fhb.FhbCusVO;
import com.ruoyi.web.controller.xkt.migartion.vo.fhb.FhbProdStockVO;
import com.ruoyi.web.controller.xkt.migartion.vo.fhb.FhbProdVO;
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
@RequestMapping("/rest/v1/fhb")
public class FhbController extends BaseController {

    final IShipMasterService shipMasterService;
    final RedisCache redisCache;

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @PostMapping("/prod/cache")
    public R<Integer> createProdCache(@Validated @RequestBody FhbProdVO prodVO) {
        // 供应商ID
        final Integer supplierId = prodVO.getData().getRecords().get(0).getSupplierId();
        if (ObjectUtils.isEmpty(prodVO.getData()) || CollectionUtils.isEmpty(prodVO.getData().getRecords())) {
            return R.ok();
        }
        // 先从redis中获取列表数据
        List<FhbProdVO.SMIVO> cacheList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_SUPPLIER_PROD_KEY + supplierId), new ArrayList<>());
        CollectionUtils.addAll(cacheList, prodVO.getData().getRecords());
        // 存到redis中
        redisCache.setCacheObject(CacheConstants.MIGRATION_SUPPLIER_PROD_KEY + supplierId, cacheList, 5, TimeUnit.DAYS);
        return R.ok();
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @PostMapping("/cus/cache")
    public R<Integer> createCusCache(@Validated @RequestBody FhbCusVO cusVO) {
        final Integer supplierId = cusVO.getData().getRecords().get(0).getSupplierId();
        if (ObjectUtils.isEmpty(cusVO.getData()) || CollectionUtils.isEmpty(cusVO.getData().getRecords())) {
            return R.ok();
        }
        // 先从redis中获取列表数据
        List<FhbCusVO.SMCVO> cacheList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_SUPPLIER_CUS_KEY + supplierId), new ArrayList<>());
        CollectionUtils.addAll(cacheList, cusVO.getData().getRecords());
        // 存到redis中
        redisCache.setCacheObject(CacheConstants.MIGRATION_SUPPLIER_CUS_KEY + supplierId, cacheList, 5, TimeUnit.DAYS);
        return R.ok();
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @PostMapping("/prod/stock/cache/{supplierId}")
    public R<Integer> createProdStockCache(@PathVariable Integer supplierId, @Validated @RequestBody FhbProdStockVO stockVO) {
        // 供应商ID
        if (ObjectUtils.isEmpty(stockVO.getData()) || ObjectUtils.isEmpty(stockVO.getData().getList())) {
            return R.ok();
        }
        // 先从redis中获取列表数据
        List<FhbProdStockVO.SMPSRecordVO> cacheList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_SUPPLIER_PROD_STOCK_KEY + supplierId), new ArrayList<>());
        CollectionUtils.addAll(cacheList, stockVO.getData().getList().getRecords());
        // 存到redis中
        redisCache.setCacheObject(CacheConstants.MIGRATION_SUPPLIER_PROD_STOCK_KEY + supplierId, cacheList, 5, TimeUnit.DAYS);
        return R.ok();
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @PostMapping("/cus/discount/cache")
    public R<Integer> createCusDiscountCache(@Validated @RequestBody FhbCusDiscountVO cusDiscountVO) {
        final Integer supplierId = cusDiscountVO.getData().getRecords().get(0).getSupplierId();
        // 供应商ID
        if (ObjectUtils.isEmpty(cusDiscountVO.getData()) || ObjectUtils.isEmpty(cusDiscountVO.getData().getRecords())) {
            return R.ok();
        }
        // 设置优惠价格
        cusDiscountVO.getData().getRecords().forEach(record -> record.setDiscount(record.getSupplyPrice() - record.getCustomerPrice()));
        // 先从redis中获取列表数据
        List<FhbCusDiscountVO.SMCDRecordVO> cacheList = ObjectUtils.defaultIfNull(redisCache
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
        List<FhbProdVO.SMIVO> cacheList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_SUPPLIER_PROD_KEY + supplierId), new ArrayList<>());
        if (CollectionUtils.isEmpty(cacheList)) {
            return R.fail();
        }
        // 按照artNo分组
        Map<String, List<FhbProdVO.SMIVO>> artNoGroup = cacheList.stream()
                .sorted(Comparator.comparing(FhbProdVO.SMIVO::getArtNo)
                        .thenComparing(FhbProdVO.SMIVO::getColor))
                .collect(Collectors
                        .groupingBy(FhbProdVO.SMIVO::getArtNo, LinkedHashMap::new, Collectors.toList()));
        // 货号 颜色 对应的条码前缀
        Map<String, Map<String, String>> artSnPrefixMap = new LinkedHashMap<>();
        // 货号 颜色 map
        Map<String, List<String>> artNoColorMap = new LinkedHashMap<>();
        artNoGroup.forEach((artNo, colorList) -> {
            artNoColorMap.put(artNo, colorList.stream().map(FhbProdVO.SMIVO::getColor).collect(Collectors.toList()));
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
        List<FhbCusVO.SMCVO> cacheList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_SUPPLIER_CUS_KEY + supplierId), new ArrayList<>());
        if (CollectionUtils.isEmpty(cacheList)) {
            return R.fail();
        }
        Map<Integer, List<FhbCusVO.SMCVO>> cusGroupMap = cacheList.stream().collect(Collectors
                .groupingBy(FhbCusVO.SMCVO::getSupplierId, LinkedHashMap::new, Collectors.toList()));
        System.err.println(cusGroupMap);
        return R.ok();
    }


}
