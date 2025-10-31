package com.ruoyi.web.controller.xkt.migartion;

import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.web.controller.xkt.migartion.vo.CusDiscErrorVO;
import com.ruoyi.web.controller.xkt.migartion.vo.fhb.FhbCusDiscountVO;
import com.ruoyi.web.controller.xkt.migartion.vo.fhb.FhbCusVO;
import com.ruoyi.web.controller.xkt.migartion.vo.fhb.FhbProdStockVO;
import com.ruoyi.web.controller.xkt.migartion.vo.fhb.FhbProdVO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * FHB 相关
 *
 * @author ruoyi
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/v1/fhb")
public class FhbController extends BaseController {

    final RedisCache redisCache;

    /**
     * step1
     */
    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @PostMapping("/prod/cache")
    public R<Integer> createProdCache(@Validated @RequestBody FhbProdVO prodVO) {
        // 供应商ID
        final Integer supplierId = prodVO.getData().getRecords().get(0).getSupplierId();
        if (ObjectUtils.isEmpty(prodVO.getData()) || CollectionUtils.isEmpty(prodVO.getData().getRecords())) {
            return R.ok();
        }
        // 前置校验 判断同一货号中是否存在相同的颜色
        Map<String, Map<String, List<FhbProdVO.SMIVO>>> fhbProdColorMap = prodVO.getData().getRecords().stream().collect(Collectors
                .groupingBy(FhbProdVO.SMIVO::getArtNo, Collectors.groupingBy(FhbProdVO.SMIVO::getColor)));
        fhbProdColorMap.forEach((artNo, colorMap) -> colorMap.forEach((color, list) -> {
            if (list.size() > 1) {
                throw new ServiceException("同一货号中存在相同的颜色" + artNo, HttpStatus.ERROR);
            }
        }));
        // 先从redis中获取列表数据
        List<FhbProdVO.SMIVO> cacheList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_SUPPLIER_PROD_KEY + supplierId), new ArrayList<>());
        // 如果货号包括R 则表明是 货号为绒里，手动给颜色添加后缀“绒里”
        List<FhbProdVO.SMIVO> importList = prodVO.getData().getRecords().stream().peek(x -> {
            if (x.getArtNo().endsWith("R")) {
                x.setColor(x.getColor().contains("绒") ? x.getColor() : (x.getColor() + "绒里"));
            }
        }).collect(Collectors.toList());
        CollectionUtils.addAll(cacheList, importList);
        // 存到redis中
        redisCache.setCacheObject(CacheConstants.MIGRATION_SUPPLIER_PROD_KEY + supplierId, cacheList, 5, TimeUnit.DAYS);
        return R.ok();
    }

    /**
     * step2
     */
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

    /**
     * step3
     */
    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @PostMapping("/cus/discount/cache")
    public R<Integer> createCusDiscountCache(@Validated @RequestBody FhbCusDiscountVO cusDiscountVO) {
        final Integer supplierId = cusDiscountVO.getData().getRecords().get(0).getSupplierId();
        // 供应商ID
        if (ObjectUtils.isEmpty(cusDiscountVO.getData()) || ObjectUtils.isEmpty(cusDiscountVO.getData().getRecords())) {
            return R.ok();
        }
        // 设置优惠价格
        cusDiscountVO.getData().getRecords().forEach(record -> {
            final Integer supplyPrice = ObjectUtils.defaultIfNull(record.getSupplyPrice(), 0);
            final Integer customerPrice = ObjectUtils.defaultIfNull(record.getCustomerPrice(), 0);
            record.setDiscount(supplyPrice - customerPrice);
        });
        // 先从redis中获取列表数据
        List<FhbCusDiscountVO.SMCDRecordVO> cacheList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_SUPPLIER_CUS_DISCOUNT_KEY + supplierId), new ArrayList<>());
        CollectionUtils.addAll(cacheList, cusDiscountVO.getData().getRecords());
        // 存到redis中
        redisCache.setCacheObject(CacheConstants.MIGRATION_SUPPLIER_CUS_DISCOUNT_KEY + supplierId, cacheList, 5, TimeUnit.DAYS);
        return R.ok();
    }

    /**
     * step4
     */
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

    /**
     * step5
     *
     * @return
     */
    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @GetMapping("/error/cus/price/{supplierId}")
    public void getErrorCusDisc(HttpServletResponse response,  @PathVariable Integer supplierId) throws UnsupportedEncodingException {
        // 先从redis中获取列表数据
        List<FhbCusDiscountVO.SMCDRecordVO> cacheList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_SUPPLIER_CUS_DISCOUNT_KEY + supplierId), new ArrayList<>());
        List<CusDiscErrorVO> errList = new ArrayList<>();
        // 1. 有哪些是优惠价大于销售价的
        cacheList.forEach(record -> {
            final Integer supplyPrice = ObjectUtils.defaultIfNull(record.getSupplyPrice(), 0);
            final Integer customerPrice = ObjectUtils.defaultIfNull(record.getCustomerPrice(), 0);
            if (supplyPrice - customerPrice < 0) {
                errList.add(new CusDiscErrorVO().setCusName(record.getCustomerName()).setProdArtNum(record.getArtNo())
                        .setColorName(record.getColor()).setErrMsg("优惠价大于原售价").setDetail("销售价:" + supplyPrice + " 优惠价:" + customerPrice));
            }
        });
        /*// 2. 有哪些优惠是同一货号不同颜色优惠金额不一致
        List<String> errCusDiscUnSameList = new ArrayList<>();
        Map<String, Map<String, List<FhbCusDiscountVO.SMCDRecordVO>>> artNoCusDiscMap = cacheList.stream().collect(Collectors
                .groupingBy(FhbCusDiscountVO.SMCDRecordVO::getArtNo, Collectors.groupingBy(FhbCusDiscountVO.SMCDRecordVO::getCustomerName)));
        artNoCusDiscMap.forEach((artNo, cusDiscMap) -> cusDiscMap.forEach((cusName, cusDiscList) -> {
            Map<String, Integer> colorDiscMap = cusDiscList.stream().collect(Collectors.groupingBy(FhbCusDiscountVO.SMCDRecordVO::getColor,
                    Collectors.collectingAndThen(Collectors.maxBy(Comparator.comparing(FhbCusDiscountVO.SMCDRecordVO::getUpdateTime,
                            Comparator.nullsFirst(String::compareTo))), record -> record.get().getDiscount())));
            // 判断所有颜色的优惠金额是否一致
            Set<Integer> discValueSet = new HashSet<>(colorDiscMap.values());
            if (discValueSet.size() > 1) {
                errCusDiscUnSameList.add(artNo + ":" + cusName + ":" + colorDiscMap.keySet() + "，优惠金额不一致");
            }
        }));*/
        for (int i = 0; i < errList.size(); i++) {
            errList.get(i).setOrderNum(i + 1);
        }
        ExcelUtil<CusDiscErrorVO> util = new ExcelUtil<>(CusDiscErrorVO.class);
        String encodedFileName = URLEncoder.encode("FHB客户优惠问题" + DateUtils.getDate(), "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename=" + encodedFileName + ".xlsx");
        util.exportExcel(response, errList, "FHB客户优惠问题");
    }


    // ======================================================================================================================================


    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @PostMapping("/colors/cache/{supplierId}")
    public R<Integer> createColorsCache(@PathVariable Integer supplierId) {

        /*// 先从redis中获取列表数据
        List<FhbProdVO.SMIVO> cacheList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_SUPPLIER_PROD_KEY + supplierId), new ArrayList<>());
        if (CollectionUtils.isEmpty(cacheList)) {
            throw new ServiceException("FHB商品列表为空", HttpStatus.ERROR);
        }*/

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


    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @GetMapping("/cus/discount/cache/{supplierId}")
    public R<Integer> getCusDiscountCache(@PathVariable Integer supplierId) {
        // 从redis中获取数据
        List<FhbCusDiscountVO.SMCDRecordVO> cacheList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_SUPPLIER_CUS_DISCOUNT_KEY + supplierId), new ArrayList<>());
        if (CollectionUtils.isEmpty(cacheList)) {
            return R.fail();
        }
        Map<String, Map<String, List<FhbCusDiscountVO.SMCDRecordVO>>> cusDiscGroupMap = cacheList.stream().collect(Collectors
                .groupingBy(FhbCusDiscountVO.SMCDRecordVO::getArtNo, Collectors.groupingBy(FhbCusDiscountVO.SMCDRecordVO::getColor)));
        cusDiscGroupMap.forEach((k, v) -> {
            System.out.println(k + ":" + v);
        });
        return R.ok();
    }


}
