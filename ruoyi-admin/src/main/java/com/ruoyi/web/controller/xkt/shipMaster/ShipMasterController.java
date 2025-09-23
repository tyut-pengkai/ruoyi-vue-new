package com.ruoyi.web.controller.xkt.shipMaster;

import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.web.controller.xkt.shipMaster.vo.ShipMasterVO;
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
    @PostMapping("/cache")
    public R<Integer> createCache(@Validated @RequestBody ShipMasterVO importVO) {
        // 供应商ID
        final Integer supplierId = importVO.getData().getRecords().get(0).getSupplierId();
        // 先从redis中获取列表数据
        List<ShipMasterVO.SMIVO> cacheList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_SUPPLIER_PROD_KEY + supplierId), new ArrayList<>());
        CollectionUtils.addAll(cacheList, importVO.getData().getRecords());
        // 存到redis中
        redisCache.setCacheObject(CacheConstants.MIGRATION_SUPPLIER_PROD_KEY + supplierId, cacheList, 5, TimeUnit.DAYS);
        return R.ok();
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @GetMapping("/cache/{supplierId}")
    public R<Integer> getCache(@PathVariable Integer supplierId) {
        // 从redis中获取数据
        List<ShipMasterVO.SMIVO> cacheList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_SUPPLIER_PROD_KEY + supplierId), new ArrayList<>());
        if (CollectionUtils.isEmpty(cacheList)) {
            return R.fail();
        }
        // 按照artNo分组
        Map<String, List<ShipMasterVO.SMIVO>> artNoGroup = cacheList.stream()
                .sorted(Comparator.comparing(ShipMasterVO.SMIVO::getArtNo)
                        .thenComparing(ShipMasterVO.SMIVO::getColor))
                .collect(Collectors
                        .groupingBy(ShipMasterVO.SMIVO::getArtNo, LinkedHashMap::new, Collectors.toList()));
        // 货号 颜色 对应的条码前缀
        Map<String, Map<String, String>> artSnPrefixMap = new LinkedHashMap<>();
        // 货号 颜色 map
        Map<String, List<String>> artNoColorMap = new LinkedHashMap<>();
        artNoGroup.forEach((artNo, colorList) -> {
            artNoColorMap.put(artNo, colorList.stream().map(ShipMasterVO.SMIVO::getColor).collect(Collectors.toList()));
            Map<String, String> snPrefixMap = new LinkedHashMap<>();
            // 按照颜色设置条码前缀
            colorList.forEach(color -> snPrefixMap
                    .put(color.getColor(), color.getSupplierId() + String.format("%05d", color.getSupplierSkuId())));
            artSnPrefixMap.put(artNo, snPrefixMap);
        });
        artSnPrefixMap.forEach((k,v) -> {
            System.err.println(k + ":" + v);
        });
        artNoColorMap.forEach((k,v) -> {
            System.err.println(k + ":" + v);
        });
        return R.ok();
    }


}
