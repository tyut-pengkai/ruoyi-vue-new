package com.ruoyi.web.controller.xkt.migartion;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.es.EsClientWrapper;
import com.ruoyi.framework.notice.fs.FsNotice;
import com.ruoyi.system.mapper.SysDictDataMapper;
import com.ruoyi.web.controller.xkt.migartion.vo.fhb.FhbCusDiscountVO;
import com.ruoyi.web.controller.xkt.migartion.vo.fhb.FhbProdStockVO;
import com.ruoyi.web.controller.xkt.migartion.vo.fhb.FhbProdVO;
import com.ruoyi.web.controller.xkt.migartion.vo.fhbBizAfter.FhbBizAfterImportVO;
import com.ruoyi.xkt.domain.*;
import com.ruoyi.xkt.enums.EProductStatus;
import com.ruoyi.xkt.enums.ListingType;
import com.ruoyi.xkt.mapper.*;
import com.ruoyi.xkt.service.IPictureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 只有GT处理 相关
 *
 * @author ruoyi
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/v1/fhb-other-biz/after")
public class FhbOtherBizAfterController extends BaseController {

    final RedisCache redisCache;
    final StoreProductColorMapper prodColorMapper;
    final StoreProductColorSizeMapper prodColorSizeMapper;
    final StoreProductMapper storeProdMapper;
    final StoreProductStockMapper prodStockMapper;
    final StoreColorMapper storeColorMapper;
    final StoreMapper storeMapper;
    final StoreProductServiceMapper prodSvcMapper;
    final StoreProductCategoryAttributeMapper prodCateAttrMapper;
    final SysProductCategoryMapper prodCateMapper;
    final EsClientWrapper esClientWrapper;
    final IPictureService pictureService;
    final FsNotice fsNotice;
    final SysDictDataMapper dictDataMapper;
    final StoreCustomerProductDiscountMapper storeCusProdDiscMapper;
    final StoreCustomerMapper storeCusMapper;

    // 系统枚举 鞋面材质
    private static final String DICT_TYPE_SHAFT_MATERIAL = "shaft_material";
    // 系统枚举 鞋面内里材质
    private static final String DICT_TYPE_SHOE_UPPER_LINING_MATERIAL = "shoe_upper_lining_material";


    /**
     * step1
     * 颜色、客户、商品初始化（包含类目属性及服务等）
     */
    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @PostMapping("/init-prod")
    @Transactional
    public R<Integer> initProd(@RequestParam(value = "storeId") Long storeId, @RequestParam(value = "supplierId") Integer supplierId,
                               MultipartFile file) throws IOException {
        Optional.ofNullable(this.storeMapper.selectOne(new LambdaQueryWrapper<Store>().eq(Store::getId, storeId).eq(Store::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("档口不存在!", HttpStatus.ERROR));
        ExcelUtil<FhbBizAfterImportVO> util = new ExcelUtil<>(FhbBizAfterImportVO.class);
        List<FhbBizAfterImportVO> fbhAfterImportVOList = util.importExcel(file.getInputStream());
        // 待处理的FHB独有的货号
        List<String> fhbAfterArtNumList = fbhAfterImportVOList.stream().map(FhbBizAfterImportVO::getFhbAfterArtNum).map(String::trim)
                .filter(StringUtils::isNotBlank).map(item -> item.contains("(") ? item.substring(0, item.indexOf("(")) : item)
                .collect(Collectors.toList());
        // 绒里货号map 必须是末尾带R的
        Map<String, String> waitMatchMap = fhbAfterArtNumList.stream().filter(x -> x.endsWith("R")).collect(Collectors.toMap(x -> x, x -> x));
        // 按照货号 和 单里绒里匹配关系进行分组（FHB有可能是一个货号里包含 单里绒里）
        Map<String, List<String>> fhbAfterArtNumGroupMap = new HashMap<>();
        fhbAfterArtNumList.stream().filter(x -> !x.endsWith("R")).distinct().forEach(x -> {
            List<String> matchList = new ArrayList<>(Collections.singletonList(x));
            if (waitMatchMap.containsKey(x + "R")) {
                matchList.add(waitMatchMap.get(x + "R"));
            } else if (waitMatchMap.containsKey(x + "-R")) {
                matchList.add(waitMatchMap.get(x + "-R"));
            }
            fhbAfterArtNumGroupMap.put(x, matchList);
        });
        // 校验同一货号下是否有多个颜色
        fhbAfterArtNumGroupMap.forEach((fhbArtNo, matchList) -> {
            Map<String, List<String>> colorSizeMap = matchList.stream().collect(Collectors.groupingBy(x -> x));
            colorSizeMap.forEach((color, artNoList) -> {
                if (artNoList.size() > 1) {
                    throw new ServiceException("同一货号下有多个颜色!", HttpStatus.ERROR);
                }
            });
        });
        // 先从redis中获取列表数据
        List<FhbProdVO.SMIVO> fhbProdList = redisCache.getCacheObject(CacheConstants.MIGRATION_SUPPLIER_PROD_KEY + supplierId);
        if (CollectionUtils.isEmpty(fhbProdList)) {
            throw new ServiceException("FHB cache 数据为空!", HttpStatus.ERROR);
        }

        // 步骤1: 颜色初始化
        this.initStoreColorList(storeId, fhbProdList, fhbAfterArtNumList);

        // 步骤2: 新增商品
        List<StoreProduct> storeProdList = new ArrayList<>();
        // 当天
        final Date voucherDate = java.sql.Date.valueOf(LocalDate.now());
        // 默认为 单鞋下 弹力靴/袜靴 分类
        final Long prodCateId = 13L;
        fhbAfterArtNumGroupMap.forEach((fhbArtNo, matchList) -> {
            // 初始化档口商品 默认为私款，只能打印条码出库等，不可在平台展示
            StoreProduct storeProd = new StoreProduct().setStoreId(storeId).setProdCateId(prodCateId).setPrivateItem(1)
                    .setProdArtNum(fhbArtNo).setProdTitle("上架大卖").setListingWay(ListingType.RIGHT_NOW.getValue())
                    .setVoucherDate(voucherDate).setProdStatus(EProductStatus.ON_SALE.getValue()).setRecommendWeight(0L).setSaleWeight(0L).setPopularityWeight(0L);
            storeProdList.add(storeProd);
        });
        this.storeProdMapper.insert(storeProdList);

        Map<String, FhbProdVO.SMIVO> fhbProdMap = fhbProdList.stream().collect(Collectors.toMap(FhbProdVO.SMIVO::getArtNo, x -> x, (v1, v2) -> v2));
        // 找到枚举的 鞋面材质 和 鞋面内里材质
        List<SysDictData> attrList = Optional.ofNullable(this.dictDataMapper.selectList(new LambdaQueryWrapper<SysDictData>()
                        .in(SysDictData::getDictType, Arrays.asList(DICT_TYPE_SHAFT_MATERIAL, DICT_TYPE_SHOE_UPPER_LINING_MATERIAL))
                        .eq(SysDictData::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("系统枚举 鞋面材质及鞋面内里材质 不存在!", HttpStatus.ERROR));
        Map<String, String> shaftMaterialMap = attrList.stream().filter(x -> Objects.equals(x.getDictType(), DICT_TYPE_SHAFT_MATERIAL))
                .collect(Collectors.toMap(SysDictData::getDictLabel, SysDictData::getDictValue));
        Map<String, String> shoeUpperLiningMaterialMap = attrList.stream().filter(x -> Objects.equals(x.getDictType(), DICT_TYPE_SHOE_UPPER_LINING_MATERIAL))
                .collect(Collectors.toMap(SysDictData::getDictLabel, SysDictData::getDictValue));
        List<StoreProductCategoryAttribute> prodCateAttrList = new ArrayList<>();
        List<StoreProductService> prodSvcList = new ArrayList<>();
        Map<String, String> newShaftMaterialMap = new HashMap<>();
        Map<String, String> newShoeUpperLiningMaterialMap = new HashMap<>();
        storeProdList.forEach(storeProd -> {
            FhbProdVO.SMIVO fhbProdVO = Optional.ofNullable(fhbProdMap.get(storeProd.getProdArtNum()))
                    .orElseThrow(() -> new ServiceException("FHB独有商品，未找到FHB对应的商品!", HttpStatus.ERROR));
            // 鞋面材质 为数据库不存在的属性，则新增
            if (StringUtils.isNotBlank(fhbProdVO.getOutStuff()) && !shaftMaterialMap.containsKey(fhbProdVO.getOutStuff())) {
                newShaftMaterialMap.put(fhbProdVO.getOutStuff(), fhbProdVO.getOutStuff());
            }
            if (StringUtils.isNotBlank(fhbProdVO.getInnerStuff()) && !shoeUpperLiningMaterialMap.containsKey(fhbProdVO.getInnerStuff())) {
                newShoeUpperLiningMaterialMap.put(fhbProdVO.getInnerStuff(), fhbProdVO.getInnerStuff());
            }
            // 只设置鞋面材质 和 鞋面内里材质
            prodCateAttrList.add(new StoreProductCategoryAttribute().setStoreId(storeProd.getStoreId()).setStoreProdId(storeProd.getId())
                    .setShaftMaterial(fhbProdVO.getOutStuff()).setShoeUpperLiningMaterial(fhbProdVO.getInnerStuff()));
            // 初始化商品服务承诺
            prodSvcList.add(new StoreProductService().setStoreProdId(storeProd.getId()).setCustomRefund("0")
                    .setThirtyDayRefund("0").setOneBatchSale("1").setRefundWithinThreeDay("0"));
        });
        List<SysDictData> newDictDataList = new ArrayList<>();
        if (MapUtils.isNotEmpty(newShaftMaterialMap)) {
            newShaftMaterialMap.forEach((k, v) -> newDictDataList.add(new SysDictData().setDictLabel(k).setDictValue(k).setDictType(DICT_TYPE_SHAFT_MATERIAL).setStatus("0").setDictSort(100L)));
        }
        if (MapUtils.isNotEmpty(newShoeUpperLiningMaterialMap)) {
            newShoeUpperLiningMaterialMap.forEach((k, v) -> newDictDataList.add(new SysDictData().setDictLabel(k).setDictValue(k)
                    .setDictType(DICT_TYPE_SHOE_UPPER_LINING_MATERIAL).setStatus("0").setDictSort(100L)));
        }
        if (CollectionUtils.isNotEmpty(newDictDataList)) {
            this.dictDataMapper.insert(newDictDataList);
        }
        this.prodCateAttrMapper.insert(prodCateAttrList);
        this.prodSvcMapper.insert(prodSvcList);
        return R.ok();
    }

    /**
     * step2
     * 商品颜色及商品颜色尺码
     */
    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @PostMapping("/init-color")
    @Transactional
    public R<Integer> initColor(@RequestParam(value = "storeId") Long storeId, @RequestParam(value = "supplierId") Integer supplierId,
                                @RequestParam(value = "addOverPrice") BigDecimal addOverPrice, MultipartFile file) throws IOException {
        ExcelUtil<FhbBizAfterImportVO> util = new ExcelUtil<>(FhbBizAfterImportVO.class);
        List<FhbBizAfterImportVO> fbhAfterImportVOList = util.importExcel(file.getInputStream());
        // 待处理的FHB独有的货号
        List<String> fhbAfterArtNumList = fbhAfterImportVOList.stream().map(FhbBizAfterImportVO::getFhbAfterArtNum).map(String::trim)
                .filter(StringUtils::isNotBlank).map(item -> item.contains("(") ? item.substring(0, item.indexOf("(")) : item)
                .collect(Collectors.toList());
        // 绒里货号map 必须是末尾带R的
        Map<String, String> waitMatchMap = fhbAfterArtNumList.stream().filter(x -> x.endsWith("R")).collect(Collectors.toMap(x -> x, x -> x));
        // 按照货号 和 单里绒里匹配关系进行分组（FHB有可能是一个货号里包含 单里绒里）
        Map<String, List<String>> fhbAfterArtNumGroupMap = new HashMap<>();
        fhbAfterArtNumList.stream().filter(x -> !x.endsWith("R")).distinct().forEach(x -> {
            List<String> matchList = new ArrayList<>(Collections.singletonList(x));
            if (waitMatchMap.containsKey(x + "R")) {
                matchList.add(waitMatchMap.get(x + "R"));
            } else if (waitMatchMap.containsKey(x + "-R")) {
                matchList.add(waitMatchMap.get(x + "-R"));
            }
            fhbAfterArtNumGroupMap.put(x, matchList);
        });

        // 从数据库查询最新数据
        List<StoreProduct> storeProdList = this.storeProdMapper.selectList(new LambdaQueryWrapper<StoreProduct>()
                .eq(StoreProduct::getStoreId, storeId).eq(StoreProduct::getDelFlag, Constants.UNDELETED)
                // TODO 特殊处理，只保留GT单独处理的部分  important important important
                .in(StoreProduct::getProdArtNum, fhbAfterArtNumGroupMap.keySet()));
        List<StoreColor> storeColorList = this.storeColorMapper.selectList(new LambdaQueryWrapper<StoreColor>()
                .eq(StoreColor::getStoreId, storeId).eq(StoreColor::getDelFlag, Constants.UNDELETED));
        Map<String, StoreColor> storeColorMap = storeColorList.stream().collect(Collectors.toMap(StoreColor::getColorName, x -> x, (v1, v2) -> v2));
        // 先从redis中获取列表数据
        List<FhbProdVO.SMIVO> fhbProdList = redisCache.getCacheObject(CacheConstants.MIGRATION_SUPPLIER_PROD_KEY + supplierId);
        if (CollectionUtils.isEmpty(fhbProdList)) {
            throw new ServiceException("FHB cache 数据为空!", HttpStatus.ERROR);
        }
        Map<String, List<FhbProdVO.SMIVO>> fhbProdSkuMap = fhbProdList.stream().collect(Collectors.groupingBy(FhbProdVO.SMIVO::getArtNo));
        // 商品所有颜色 尺码 颜色库存初始化
        List<StoreProductColor> prodColorList = new ArrayList<>();
        List<StoreProductColorSize> prodColorSizeList = new ArrayList<>();
        storeProdList.forEach(storeProd -> {
            Optional.ofNullable(fhbAfterArtNumGroupMap.get(storeProd.getProdArtNum()))
                    .orElseThrow(() -> new ServiceException("没有FHB[独有]商品货号!" + storeProd.getProdArtNum(), HttpStatus.ERROR));
            // 单里绒里匹配到的所有颜色
            List<FhbProdVO.SMIVO> fhbMatchColorList = fhbAfterArtNumGroupMap.get(storeProd.getProdArtNum()).stream()
                    .map(fhbProdSkuMap::get).filter(Objects::nonNull).flatMap(List::stream).collect(Collectors.toList());
            AtomicInteger orderNum = new AtomicInteger();
            fhbMatchColorList.forEach(fhbProdColor -> {
                StoreColor storeColor = Optional.ofNullable(storeColorMap.get(fhbProdColor.getColor()))
                        .orElseThrow(() -> new ServiceException("没有FHB[独有]商品颜色!" + storeProd.getProdArtNum(), HttpStatus.ERROR));
                // 该商品的颜色
                prodColorList.add(new StoreProductColor().setStoreId(storeProd.getStoreId()).setStoreProdId(storeProd.getId()).setOrderNum(orderNum.addAndGet(1))
                        .setColorName(storeColor.getColorName()).setStoreColorId(storeColor.getId()).setProdStatus(EProductStatus.ON_SALE.getValue()));
                // 该颜色最低价格
                BigDecimal minPrice = ObjectUtils.defaultIfNull(fhbProdColor.getSalePrice(), BigDecimal.ZERO);
                List<Integer> standardSizeList = StrUtil.split(fhbProdColor.getSize(), ",").stream().map(Integer::parseInt).collect(Collectors.toList());
                // 该颜色所有的尺码  默认34-40为标准尺码
                for (int j = 0; j < Constants.SIZE_LIST.size(); j++) {
                    // FHB系统条码前缀
                    final String otherSnPrefix = fhbProdColor.getSupplierId()
                            + String.format("%05d", fhbProdColor.getSupplierSkuId()) + Constants.SIZE_LIST.get(j);
                    prodColorSizeList.add(new StoreProductColorSize().setSize(Constants.SIZE_LIST.get(j)).setStoreColorId(storeColor.getId())
                            .setStoreProdId(storeProd.getId()).setStandard(standardSizeList.contains(Constants.SIZE_LIST.get(j)) ? 1 : 0)
                            // 销售价格以FHB价格为准 非标尺码价格需要加价
                            .setPrice(standardSizeList.contains(Constants.SIZE_LIST.get(j)) ? minPrice
                                    : minPrice.add(ObjectUtils.defaultIfNull(addOverPrice, BigDecimal.ZERO)))
                            .setOtherSnPrefix(otherSnPrefix).setNextSn(0));
                }
            });
        });
        // 插入商品颜色及颜色对应的尺码，档口服务承诺
        this.prodColorMapper.insert(prodColorList);
        // 按照货号颜色尺码升序排列
        prodColorSizeList.sort(Comparator.comparing(StoreProductColorSize::getStoreProdId)
                .thenComparing(StoreProductColorSize::getStoreColorId).thenComparing(StoreProductColorSize::getSize));
        this.prodColorSizeMapper.insert(prodColorSizeList);
        // 还要更新步橘系统的条码前缀
        prodColorSizeList.forEach(x -> x.setSnPrefix(storeId + String.format("%08d", x.getId())));
        this.prodColorSizeMapper.updateById(prodColorSizeList);
        return R.ok();
    }

    /**
     * step4
     */
    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @PostMapping("/init-cus-disc")
    @Transactional
    public R<Integer> initCusDisc(@RequestParam(value = "storeId") Long storeId, @RequestParam(value = "supplierId") Integer supplierId,
                                  MultipartFile file) throws IOException {
        ExcelUtil<FhbBizAfterImportVO> util = new ExcelUtil<>(FhbBizAfterImportVO.class);
        List<FhbBizAfterImportVO> fbhAfterImportVOList = util.importExcel(file.getInputStream());
        // 待处理的FHB独有的货号
        List<String> fhbAfterArtNumList = fbhAfterImportVOList.stream().map(FhbBizAfterImportVO::getFhbAfterArtNum).map(String::trim)
                .filter(StringUtils::isNotBlank).map(item -> item.contains("(") ? item.substring(0, item.indexOf("(")) : item)
                .collect(Collectors.toList());
        // 绒里货号map 必须是末尾带R的
        Map<String, String> waitMatchMap = fhbAfterArtNumList.stream().filter(x -> x.endsWith("R")).collect(Collectors.toMap(x -> x, x -> x));
        // 按照货号 和 单里绒里匹配关系进行分组（FHB有可能是一个货号里包含 单里绒里）
        Map<String, List<String>> fhbAfterArtNumGroupMap = new HashMap<>();
        fhbAfterArtNumList.stream().filter(x -> !x.endsWith("R")).distinct().forEach(x -> {
            List<String> matchList = new ArrayList<>(Collections.singletonList(x));
            if (waitMatchMap.containsKey(x + "R")) {
                matchList.add(waitMatchMap.get(x + "R"));
            } else if (waitMatchMap.containsKey(x + "-R")) {
                matchList.add(waitMatchMap.get(x + "-R"));
            }
            fhbAfterArtNumGroupMap.put(x, matchList);
        });
        // 之前导入的仅仅存在于TY的商品列表
        List<StoreProduct> storeProdList = this.storeProdMapper.selectList(new LambdaQueryWrapper<StoreProduct>()
                .eq(StoreProduct::getStoreId, storeId).eq(StoreProduct::getDelFlag, Constants.UNDELETED)
                // TODO 特殊处理，只保留GT单独处理的部分  important important important
                .in(StoreProduct::getProdArtNum, fhbAfterArtNumGroupMap.keySet()));
        List<StoreProductColor> prodColorList = this.prodColorMapper.selectList(new LambdaQueryWrapper<StoreProductColor>()
                .eq(StoreProductColor::getStoreId, storeId).eq(StoreProductColor::getDelFlag, Constants.UNDELETED));
        List<StoreCustomer> storeCusList = this.storeCusMapper.selectList(new LambdaQueryWrapper<StoreCustomer>()
                .eq(StoreCustomer::getStoreId, storeId).eq(StoreCustomer::getDelFlag, Constants.UNDELETED));

        List<FhbProdVO.SMIVO> cacheList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_SUPPLIER_PROD_KEY + supplierId), new ArrayList<>());
        if (CollectionUtils.isEmpty(cacheList)) {
            throw new ServiceException("FHB商品列表为空", HttpStatus.ERROR);
        }
        // fhb货号正在生效的颜色map，因为客户优惠有些是删除的颜色，需要通过这里去过滤
        Map<String, Set<String>> fhbExistArtNoColorMap = cacheList.stream().collect(Collectors
                .groupingBy(FhbProdVO.SMIVO::getArtNo, Collectors.mapping(FhbProdVO.SMIVO::getColor, Collectors.toSet())));

        // 从redis中获取客户优惠数据
        List<FhbCusDiscountVO.SMCDRecordVO> fhbCusDiscCacheList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_SUPPLIER_CUS_DISCOUNT_KEY + supplierId), new ArrayList<>());
        if (CollectionUtils.isEmpty(fhbCusDiscCacheList)) {
            throw new ServiceException("fhb供应商客户优惠列表为空!" + supplierId, HttpStatus.ERROR);
        }
        // 增加一重保险，客户优惠必须是大于0的
        fhbCusDiscCacheList = fhbCusDiscCacheList.stream().filter(x -> x.getDiscount() > 0).collect(Collectors.toList());
        List<FhbProdStockVO.SMPSRecordVO> fhbStockCacheList = redisCache
                .getCacheObject(CacheConstants.MIGRATION_SUPPLIER_PROD_STOCK_KEY + supplierId);
        if (CollectionUtils.isEmpty(fhbStockCacheList)) {
            throw new ServiceException("fhb供应商商品库存列表为空!" + supplierId, HttpStatus.ERROR);
        }
        // FHB 货号颜色优惠对应关系
        Map<String, Map<String, List<FhbCusDiscountVO.SMCDRecordVO>>> fhbCusDiscGroupMap = fhbCusDiscCacheList.stream().collect(Collectors
                .groupingBy(FhbCusDiscountVO.SMCDRecordVO::getArtNo, Collectors.groupingBy(FhbCusDiscountVO.SMCDRecordVO::getColor)));
        // FHB 货号颜色库存对应关系
        Map<String, Map<String, FhbProdStockVO.SMPSRecordVO>> fhbStockGroupMap = fhbStockCacheList.stream().collect(Collectors
                .groupingBy(FhbProdStockVO.SMPSRecordVO::getArtNo, Collectors.toMap(FhbProdStockVO.SMPSRecordVO::getColor, x -> x)));
        // 步橘系统商品所有颜色maps
        Map<Long, Map<String, StoreProductColor>> prodColorGroupMap = prodColorList.stream().collect(Collectors
                .groupingBy(StoreProductColor::getStoreProdId, Collectors.toMap(StoreProductColor::getColorName, x -> x)));
        // 步橘系统客户名称map
        Map<String, StoreCustomer> buJuStoreCusMap = storeCusList.stream().collect(Collectors.toMap(StoreCustomer::getCusName, x -> x));
        List<StoreCustomerProductDiscount> prodCusDiscList = new ArrayList<>();
        List<StoreProductStock> prodStockList = new ArrayList<>();
        // 依次遍历商品列表，找到货号和FHB货号对应关系，然后用颜色进行匹配，建立客户优惠关系
        storeProdList.forEach(storeProd -> {
            // 当前商品颜色列表 key 颜色中文名称
            Map<String, StoreProductColor> buJuProdColorMap = Optional.ofNullable(prodColorGroupMap.get(storeProd.getId())).orElseThrow(() -> new ServiceException("没有商品颜色!" + storeProd.getProdArtNum(), HttpStatus.ERROR));
            // 根据步橘货号 找到TY对应的货号，可能是列表
            List<String> fhbArtNoList = Optional.ofNullable(fhbAfterArtNumGroupMap.get(storeProd.getProdArtNum())).orElseThrow(() -> new ServiceException("没有FHB货号!" + storeProd.getProdArtNum(), HttpStatus.ERROR));
            // 处理档口商品库存
            this.handleProdStock(fhbArtNoList, fhbStockGroupMap, buJuProdColorMap, storeProd.getStoreId(), storeProd.getId(), storeProd.getProdArtNum(), prodStockList);

            fhbArtNoList.forEach(fhbArtNo -> {
                // 处理档口客户商品优惠
                this.handleCusDisc(fhbArtNo, fhbExistArtNoColorMap, fhbCusDiscGroupMap, buJuProdColorMap, buJuStoreCusMap, storeProd.getStoreId(), storeProd.getId(), prodCusDiscList);
            });

        });

        // 档口客户优惠
        this.storeCusProdDiscMapper.insert(prodCusDiscList);
        // 档口客户库存
        this.prodStockMapper.insert(prodStockList);
        return R.ok();
    }


    /**
     * 处理客户优惠
     */
    private void handleCusDisc(String fhbAtrNo, Map<String, Set<String>> fhbExistArtNoColorMap,
                               Map<String, Map<String, List<FhbCusDiscountVO.SMCDRecordVO>>> fhbCusDiscGroupMap,
                               Map<String, StoreProductColor> buJuProdColorMap, Map<String, StoreCustomer> buJuStoreCusMap, Long storeId, Long storeProdId,
                               List<StoreCustomerProductDiscount> prodCusDiscList) {
        // FHB货号下有哪些颜色存在客户优惠
        Map<String, List<FhbCusDiscountVO.SMCDRecordVO>> fhbColorCusDiscMap = fhbCusDiscGroupMap.get(fhbAtrNo);
        if (MapUtils.isEmpty(fhbColorCusDiscMap)) {
            return;
        }
        // fhb当前货号正在生效的颜色列表
        Set<String> existColorSet = fhbExistArtNoColorMap.getOrDefault(fhbAtrNo, Collections.emptySet());
        // 依次遍历存在优惠的颜色，设置步橘系统客户优惠关系
        fhbColorCusDiscMap.forEach((fhbColor, fhbCusDiscList) -> {
            // 必须是现在正在生效的颜色，才会被添加到系统中
            if (!existColorSet.contains(fhbColor)) {
                return;
            }
            // fhb优惠列表，如果对同一客户有多个优惠，则取修改时间最新的那条优惠
            Map<String, FhbCusDiscountVO.SMCDRecordVO> latestCusDiscMap = fhbCusDiscList.stream().collect(Collectors.toMap(FhbCusDiscountVO.SMCDRecordVO::getCustomerName, Function.identity(),
                    BinaryOperator.maxBy(Comparator.comparing(FhbCusDiscountVO.SMCDRecordVO::getUpdateTime))));
            latestCusDiscMap.forEach((cusName, fhbCusDisc) -> {
                // 获取步橘系统对应的颜色
                StoreProductColor buJuProdColor = buJuProdColorMap.get(fhbColor);
                if (ObjectUtils.isNotEmpty(buJuProdColor)) {
                    StoreCustomer storeCus = Optional.ofNullable(buJuStoreCusMap.get(fhbCusDisc.getCustomerName()))
                            .orElseThrow(() -> new ServiceException("没有步橘系统对应的客户!" + fhbCusDisc.getCustomerName(), HttpStatus.ERROR));
                    // 将FHB客户优惠 转为步橘系统优惠
                    prodCusDiscList.add(new StoreCustomerProductDiscount().setStoreId(storeId).setStoreProdId(storeProdId).setStoreCusId(storeCus.getId())
                            .setStoreCusName(storeCus.getCusName()).setStoreProdColorId(buJuProdColor.getId()).setDiscount(fhbCusDisc.getDiscount()));
                }
            });
        });
    }


    /**
     * 匹配fhb货号颜色的库存
     */
    private void handleProdStock(List<String> fhbArtNoList, Map<String, Map<String, FhbProdStockVO.SMPSRecordVO>> fhbStockGroupMap,
                                 Map<String, StoreProductColor> buJuProdColorMap, Long storeId, Long storeProdId, String prodArtNum, List<StoreProductStock> prodStockList) {
        // 获取fhb货号所有颜色的库存
        Map<String, FhbProdStockVO.SMPSRecordVO> fhbColorStockMap = new HashMap<>();
        fhbArtNoList.forEach(fhbArtNo -> fhbColorStockMap.putAll(fhbStockGroupMap.getOrDefault(fhbArtNo, new HashMap<>())));
        buJuProdColorMap.forEach((buJuColor, buJuProdColor) -> {
            StoreProductStock stock = new StoreProductStock().setStoreId(storeId).setStoreProdId(storeProdId).setProdArtNum(prodArtNum)
                    .setColorName(buJuProdColorMap.get(buJuColor).getColorName()).setStoreProdColorId(buJuProdColorMap.get(buJuColor).getId())
                    .setStoreColorId(buJuProdColorMap.get(buJuColor).getStoreColorId());
            // FHB 有该颜色的库存
            if (fhbColorStockMap.containsKey(buJuColor)) {
                FhbProdStockVO.SMPSRecordVO fhbSizeStock = fhbColorStockMap.getOrDefault(buJuColor, null);
                stock.setSize30(ObjectUtils.isNotEmpty(fhbSizeStock) ? fhbSizeStock.getSize30() : null)
                        .setSize31(ObjectUtils.isNotEmpty(fhbSizeStock) ? fhbSizeStock.getSize31() : null)
                        .setSize32(ObjectUtils.isNotEmpty(fhbSizeStock) ? fhbSizeStock.getSize32() : null)
                        .setSize33(ObjectUtils.isNotEmpty(fhbSizeStock) ? fhbSizeStock.getSize33() : null)
                        .setSize34(ObjectUtils.isNotEmpty(fhbSizeStock) ? fhbSizeStock.getSize34() : null)
                        .setSize35(ObjectUtils.isNotEmpty(fhbSizeStock) ? fhbSizeStock.getSize35() : null)
                        .setSize36(ObjectUtils.isNotEmpty(fhbSizeStock) ? fhbSizeStock.getSize36() : null)
                        .setSize37(ObjectUtils.isNotEmpty(fhbSizeStock) ? fhbSizeStock.getSize37() : null)
                        .setSize38(ObjectUtils.isNotEmpty(fhbSizeStock) ? fhbSizeStock.getSize38() : null)
                        .setSize39(ObjectUtils.isNotEmpty(fhbSizeStock) ? fhbSizeStock.getSize39() : null)
                        .setSize40(ObjectUtils.isNotEmpty(fhbSizeStock) ? fhbSizeStock.getSize40() : null)
                        .setSize41(ObjectUtils.isNotEmpty(fhbSizeStock) ? fhbSizeStock.getSize41() : null)
                        .setSize42(ObjectUtils.isNotEmpty(fhbSizeStock) ? fhbSizeStock.getSize42() : null)
                        .setSize43(ObjectUtils.isNotEmpty(fhbSizeStock) ? fhbSizeStock.getSize43() : null);
            }
            prodStockList.add(stock);
        });
    }


    /**
     * 初始化档口颜色
     *
     * @param fhbProdList        FHB所有商品sku
     * @param fhbAfterArtNumList FHB独有商品商品列表
     */
    private void initStoreColorList(Long storeId, List<FhbProdVO.SMIVO> fhbProdList, List<String> fhbAfterArtNumList) {
        // 先查询当前档口所有的颜色，如果没有剩余的颜色，则新增导入
        List<StoreColor> existColorList = this.storeColorMapper.selectList(new LambdaQueryWrapper<StoreColor>()
                .eq(StoreColor::getDelFlag, Constants.UNDELETED).eq(StoreColor::getStoreId, storeId));
        List<String> existColorNameList = existColorList.stream().map(StoreColor::getColorName).collect(Collectors.toList());
        // 当前最大的排序
        int maxOrderNum = existColorList.stream().mapToInt(StoreColor::getOrderNum).max().orElse(0) + 1;
        // TY单独处理的货品颜色
        List<String> fhbAfterColorList = fhbProdList.stream().filter(fhbProd -> fhbAfterArtNumList.contains(fhbProd.getArtNo()))
                .map(FhbProdVO.SMIVO::getColor).filter(color -> !existColorNameList.contains(color)).distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(fhbAfterColorList)) {
            return;
        }
        List<StoreColor> storeColorList = new ArrayList<>();
        for (int i = 0; i < fhbAfterColorList.size(); i++) {
            storeColorList.add(new StoreColor().setStoreId(storeId).setColorName(fhbAfterColorList.get(i)).setOrderNum(maxOrderNum + i + 1));
        }
        this.storeColorMapper.insert(storeColorList);
    }


}
