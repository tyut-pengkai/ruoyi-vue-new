package com.ruoyi.web.controller.xkt.migartion;

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
import com.ruoyi.web.controller.xkt.migartion.vo.ty.TyCusDiscImportVO;
import com.ruoyi.web.controller.xkt.migartion.vo.ty.TyProdImportVO;
import com.ruoyi.web.controller.xkt.migartion.vo.ty.TyProdStockVO;
import com.ruoyi.web.controller.xkt.migartion.vo.tyBizAfter.TyBizAfterImportVO;
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
import java.util.stream.Collectors;

/**
 * 只有TY处理 相关
 *
 * @author ruoyi
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/v1/ty-other-biz/after")
public class TYOtherBizAfterController extends BaseController {

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
    final StoreCustomerMapper storeCusMapper;
    final SysDictDataMapper dictDataMapper;
    final StoreCustomerProductDiscountMapper storeCusProdDiscMapper;

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
    public R<Integer> initProd(@RequestParam(value = "storeId") Long storeId, @RequestParam(value = "userId") Integer userId, MultipartFile file) throws IOException {
        Optional.ofNullable(this.storeMapper.selectOne(new LambdaQueryWrapper<Store>().eq(Store::getId, storeId).eq(Store::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("档口不存在!", HttpStatus.ERROR));
        ExcelUtil<TyBizAfterImportVO> util = new ExcelUtil<>(TyBizAfterImportVO.class);
        List<TyBizAfterImportVO> tyAfterImportVOList = util.importExcel(file.getInputStream());
        // 待处理的TY独有的货号
        List<String> tyAfterArtNumList = tyAfterImportVOList.stream().map(TyBizAfterImportVO::getTyAfterArtNum).map(String::trim)
                .filter(StringUtils::isNotBlank).map(item -> item.contains("(") ? item.substring(0, item.indexOf("(")) : item)
                .collect(Collectors.toList());
        // 绒里货号map 必须是末尾带R的
        Map<String, String> waitMatchMap = tyAfterArtNumList.stream().filter(x -> x.endsWith("R")).collect(Collectors.toMap(x -> x, x -> x));
        // 按照货号 和 单里绒里匹配关系进行分组
        Map<String, List<String>> tyAfterArtNumGroupMap = new HashMap<>();
        tyAfterArtNumList.stream().filter(x -> !x.endsWith("R")).distinct().forEach(x -> {
            List<String> matchList = new ArrayList<>(Collections.singletonList(x));
            if (waitMatchMap.containsKey(x + "R")) {
                matchList.add(waitMatchMap.get(x + "R"));
            } else if (waitMatchMap.containsKey(x + "-R")) {
                matchList.add(waitMatchMap.get(x + "-R"));
            }
            tyAfterArtNumGroupMap.put(x, matchList);
        });
        // 找到TY对应的商品
        List<TyProdImportVO> tyProdList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_TY_PROD_KEY + userId), new ArrayList<>());

        // 步骤1: 颜色初始化
        this.initStoreColorList(storeId, tyProdList, tyAfterArtNumList);

        // 步骤2: 新增商品
        List<StoreProduct> storeProdList = new ArrayList<>();
        // 当天
        final Date voucherDate = java.sql.Date.valueOf(LocalDate.now());
        // 默认为 单鞋下 弹力靴/袜靴 分类
        final Long prodCateId = 13L;
        tyAfterArtNumGroupMap.forEach((tyArtNum, matchList) -> {
            // 初始化档口商品 默认为私款，只能打印条码出库等，不可在平台展示
            StoreProduct storeProd = new StoreProduct().setStoreId(storeId).setProdCateId(prodCateId).setPrivateItem(1)
                    .setProdArtNum(tyArtNum).setProdTitle("上架大卖").setListingWay(ListingType.RIGHT_NOW.getValue())
                    .setVoucherDate(voucherDate).setProdStatus(EProductStatus.ON_SALE.getValue()).setRecommendWeight(0L).setSaleWeight(0L).setPopularityWeight(0L);
            storeProdList.add(storeProd);
        });
        this.storeProdMapper.insert(storeProdList);

        Map<String, TyProdImportVO> tyProdMap = tyProdList.stream().collect(Collectors.toMap(TyProdImportVO::getProdArtNum, x -> x, (v1, v2) -> v2));
        // 找到枚举的 鞋面材质 和 鞋面内里材质
        List<SysDictData> attrList = Optional.ofNullable(this.dictDataMapper.selectList(new LambdaQueryWrapper<SysDictData>()
                        .eq(SysDictData::getDictType, DICT_TYPE_SHAFT_MATERIAL).eq(SysDictData::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("系统枚举 鞋面材质及鞋面内里材质 不存在!", HttpStatus.ERROR));
        Map<String, String> shaftMaterialMap = attrList.stream().filter(x -> Objects.equals(x.getDictType(), DICT_TYPE_SHAFT_MATERIAL))
                .collect(Collectors.toMap(SysDictData::getDictLabel, SysDictData::getDictValue));
        List<StoreProductCategoryAttribute> prodCateAttrList = new ArrayList<>();
        List<StoreProductService> prodSvcList = new ArrayList<>();
        Map<String, String> newShaftMaterialMap = new HashMap<>();
        storeProdList.forEach(storeProd -> {
            TyProdImportVO tyProdVO = Optional.ofNullable(tyProdMap.get(storeProd.getProdArtNum()))
                    .orElseThrow(() -> new ServiceException("TY独有商品，未找到TY对应的商品!", HttpStatus.ERROR));
            // 鞋面材质 为数据库不存在的属性，则新增
            if (StringUtils.isNotBlank(tyProdVO.getShaftMaterial()) && !shaftMaterialMap.containsKey(tyProdVO.getShaftMaterial())) {
                newShaftMaterialMap.put(tyProdVO.getShaftMaterial(), tyProdVO.getShaftMaterial());
            }
            // 只设置鞋面材质
            prodCateAttrList.add(new StoreProductCategoryAttribute().setStoreId(storeProd.getStoreId())
                    .setStoreProdId(storeProd.getId()).setShaftMaterial(tyProdVO.getShaftMaterial()));
            // 初始化商品服务承诺
            prodSvcList.add(new StoreProductService().setStoreProdId(storeProd.getId()).setCustomRefund("0")
                    .setThirtyDayRefund("0").setOneBatchSale("1").setRefundWithinThreeDay("0"));
        });

        if (MapUtils.isNotEmpty(newShaftMaterialMap)) {
            List<SysDictData> newDictDataList = new ArrayList<>();
            newShaftMaterialMap.forEach((k, v) -> newDictDataList.add(new SysDictData().setDictLabel(k).setDictValue(k).setDictType(DICT_TYPE_SHAFT_MATERIAL).setStatus("0").setDictSort(100L)));
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
    public R<Integer> initColor(@RequestParam(value = "storeId") Long storeId, @RequestParam(value = "userId") Integer userId,
                                @RequestParam(value = "addOverPrice") BigDecimal addOverPrice, MultipartFile file) throws IOException {
        ExcelUtil<TyBizAfterImportVO> util = new ExcelUtil<>(TyBizAfterImportVO.class);
        List<TyBizAfterImportVO> tyAfterImportVOList = util.importExcel(file.getInputStream());
        // 待处理的TY独有的货号
        List<String> tyAfterArtNumList = tyAfterImportVOList.stream().map(TyBizAfterImportVO::getTyAfterArtNum).map(String::trim)
                .filter(StringUtils::isNotBlank).map(item -> item.contains("(") ? item.substring(0, item.indexOf("(")) : item)
                .collect(Collectors.toList());
        // 绒里货号map 必须是末尾带R的
        Map<String, String> waitMatchMap = tyAfterArtNumList.stream().filter(x -> x.endsWith("R")).collect(Collectors.toMap(x -> x, x -> x));
        // 按照货号 和 单里绒里匹配关系进行分组
        Map<String, List<String>> tyAfterArtNumGroupMap = new HashMap<>();
        tyAfterArtNumList.stream().filter(x -> !x.endsWith("R")).distinct().forEach(x -> {
            List<String> matchList = new ArrayList<>(Collections.singletonList(x));
            if (waitMatchMap.containsKey(x + "R")) {
                matchList.add(waitMatchMap.get(x + "R"));
            } else if (waitMatchMap.containsKey(x + "-R")) {
                matchList.add(waitMatchMap.get(x + "-R"));
            }
            tyAfterArtNumGroupMap.put(x, matchList);
        });

        // 从数据库查询最新数据
        List<StoreProduct> storeProdList = this.storeProdMapper.selectList(new LambdaQueryWrapper<StoreProduct>()
                .eq(StoreProduct::getStoreId, storeId).eq(StoreProduct::getDelFlag, Constants.UNDELETED)
                // TODO 特殊处理，只保留TY单独处理的部分  important important important
                .in(StoreProduct::getProdArtNum, tyAfterArtNumGroupMap.keySet()));
        List<StoreColor> storeColorList = this.storeColorMapper.selectList(new LambdaQueryWrapper<StoreColor>()
                .eq(StoreColor::getStoreId, storeId).eq(StoreColor::getDelFlag, Constants.UNDELETED));
        Map<String, StoreColor> storeColorMap = storeColorList.stream().collect(Collectors.toMap(StoreColor::getColorName, x -> x, (v1, v2) -> v2));

        // 找到TY对应的商品
        List<TyProdImportVO> tyProdList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_TY_PROD_KEY + userId), new ArrayList<>());
        Map<String, List<TyProdImportVO>> tyProdSkuMap = tyProdList.stream().collect(Collectors.groupingBy(TyProdImportVO::getProdArtNum));

        // 找到枚举的 鞋面内里材质
        List<SysDictData> shoeUpperLiningMaterialList = Optional.ofNullable(this.dictDataMapper.selectList(new LambdaQueryWrapper<SysDictData>()
                        .eq(SysDictData::getDictType, DICT_TYPE_SHOE_UPPER_LINING_MATERIAL).eq(SysDictData::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("系统枚举 鞋面内里材质 不存在!", HttpStatus.ERROR));
        Map<String, String> shoeUpperLiningMaterialMap = shoeUpperLiningMaterialList.stream().collect(Collectors.toMap(SysDictData::getDictLabel, SysDictData::getDictValue));

        // 商品所有颜色 尺码 颜色库存初始化
        List<StoreProductColor> prodColorList = new ArrayList<>();
        List<StoreProductColorSize> prodColorSizeList = new ArrayList<>();
        List<SysDictData> newDictDataList = new ArrayList<>();
        Map<String, String> newShoeUpperLiningMaterialMap = new HashMap<>();
        storeProdList.forEach(storeProd -> {
            Optional.ofNullable(tyAfterArtNumGroupMap.get(storeProd.getProdArtNum()))
                    .orElseThrow(() -> new ServiceException("没有TY[独有]商品货号!" + storeProd.getProdArtNum(), HttpStatus.ERROR));
            // 单里绒里匹配到的所有颜色
            List<TyProdImportVO> tyMatchColorList = tyAfterArtNumGroupMap.get(storeProd.getProdArtNum()).stream()
                    .map(tyProdSkuMap::get).filter(Objects::nonNull).flatMap(List::stream).collect(Collectors.toList());
            AtomicInteger orderNum = new AtomicInteger();
            tyMatchColorList.forEach(tyProdColor -> {
                StoreColor storeColor = Optional.ofNullable(storeColorMap.get(tyProdColor.getColorName()))
                        .orElseThrow(() -> new ServiceException("没有TY[独有]商品颜色!" + storeProd.getProdArtNum(), HttpStatus.ERROR));
                // 处理内里材质，若步橘没有的，则需要新增
                if (StringUtils.isNotBlank(tyProdColor.getShoeUpperLiningMaterial()) && !shoeUpperLiningMaterialMap.containsKey(tyProdColor.getShoeUpperLiningMaterial())) {
                    newShoeUpperLiningMaterialMap.put(tyProdColor.getShoeUpperLiningMaterial(), tyProdColor.getShoeUpperLiningMaterial());
                }
                // 该商品的颜色
                prodColorList.add(new StoreProductColor().setStoreId(storeProd.getStoreId()).setStoreProdId(storeProd.getId()).setOrderNum(orderNum.addAndGet(1))
                        .setColorName(storeColor.getColorName()).setStoreColorId(storeColor.getId()).setProdStatus(EProductStatus.ON_SALE.getValue()));
                // 该颜色最低价格
                BigDecimal minPrice = ObjectUtils.defaultIfNull(tyProdColor.getPrice(), BigDecimal.ZERO);
                // 该颜色所有的尺码  默认34-40为标准尺码
                for (int j = 0; j < Constants.SIZE_LIST.size(); j++) {
                    // TY系统条码前缀
                    final String otherSnPrefix = tyProdColor.getSn() + Constants.SIZE_LIST.get(j);
                    prodColorSizeList.add(new StoreProductColorSize().setSize(Constants.SIZE_LIST.get(j)).setStoreColorId(storeColor.getId())
                            .setStoreProdId(storeProd.getId()).setOtherSnPrefix(otherSnPrefix).setNextSn(0)
                            .setStandard(Constants.STANDARD_SIZE_LIST.contains(Constants.SIZE_LIST.get(j)) ? 1 : 0)
                            // 销售价格以TY价格为准
                            .setPrice(Constants.STANDARD_SIZE_LIST.contains(Constants.SIZE_LIST.get(j)) ? minPrice
                                    : minPrice.add(ObjectUtils.defaultIfNull(addOverPrice, BigDecimal.ZERO))));
                }
            });
        });

        // 处理新增的 内里材质 枚举
        if (MapUtils.isNotEmpty(newShoeUpperLiningMaterialMap)) {
            newShoeUpperLiningMaterialMap.forEach((k, v) -> newDictDataList.add(new SysDictData().setDictLabel(k).setDictValue(k)
                    .setDictType(DICT_TYPE_SHOE_UPPER_LINING_MATERIAL).setStatus("0").setDictSort(100L)));
            this.dictDataMapper.insert(newDictDataList);
        }

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
     * step3
     */
    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @PostMapping("/init-cus-disc")
    @Transactional
    public R<Integer> initCusDisc(@RequestParam(value = "storeId") Long storeId, @RequestParam(value = "userId") Integer userId, MultipartFile file) throws IOException {
        ExcelUtil<TyBizAfterImportVO> util = new ExcelUtil<>(TyBizAfterImportVO.class);
        List<TyBizAfterImportVO> tyAfterImportVOList = util.importExcel(file.getInputStream());
        // 待处理的TY独有的货号
        List<String> tyAfterArtNumList = tyAfterImportVOList.stream().map(TyBizAfterImportVO::getTyAfterArtNum).map(String::trim)
                .filter(StringUtils::isNotBlank).map(item -> item.contains("(") ? item.substring(0, item.indexOf("(")) : item)
                .collect(Collectors.toList());
        // 绒里货号map 必须是末尾带R的
        Map<String, String> waitMatchMap = tyAfterArtNumList.stream().filter(x -> x.endsWith("R")).collect(Collectors.toMap(x -> x, x -> x));
        // 按照货号 和 单里绒里匹配关系进行分组
        Map<String, List<String>> tyAfterArtNumGroupMap = new HashMap<>();
        tyAfterArtNumList.stream().filter(x -> !x.endsWith("R")).distinct().forEach(x -> {
            List<String> matchList = new ArrayList<>(Collections.singletonList(x));
            if (waitMatchMap.containsKey(x + "R")) {
                matchList.add(waitMatchMap.get(x + "R"));
            } else if (waitMatchMap.containsKey(x + "-R")) {
                matchList.add(waitMatchMap.get(x + "-R"));
            }
            tyAfterArtNumGroupMap.put(x, matchList);
        });

        // 之前导入的仅仅存在于TY的商品列表
        List<StoreProduct> storeProdList = this.storeProdMapper.selectList(new LambdaQueryWrapper<StoreProduct>()
                .eq(StoreProduct::getStoreId, storeId).eq(StoreProduct::getDelFlag, Constants.UNDELETED)
                // TODO 特殊处理，只保留GT单独处理的部分  important important important
                .in(StoreProduct::getProdArtNum, tyAfterArtNumGroupMap.keySet()));
        List<StoreProductColor> prodColorList = this.prodColorMapper.selectList(new LambdaQueryWrapper<StoreProductColor>()
                .eq(StoreProductColor::getStoreId, storeId).eq(StoreProductColor::getDelFlag, Constants.UNDELETED));
        List<StoreCustomer> storeCusList = this.storeCusMapper.selectList(new LambdaQueryWrapper<StoreCustomer>()
                .eq(StoreCustomer::getStoreId, storeId).eq(StoreCustomer::getDelFlag, Constants.UNDELETED));

        // 找到TY对应的商品
        List<TyProdImportVO> tyProdList = redisCache.getCacheObject(CacheConstants.MIGRATION_TY_PROD_KEY + userId);
        if (CollectionUtils.isEmpty(tyProdList)) {
            throw new ServiceException("TY商品列表为空", HttpStatus.ERROR);
        }
        // ty货号正在生效的颜色map，因为客户优惠有些是删除的颜色，需要通过这里去过滤
        Map<String, Set<String>> tyExistArtNoColorMap = tyProdList.stream().collect(Collectors
                .groupingBy(TyProdImportVO::getProdArtNum, Collectors.mapping(TyProdImportVO::getColorName, Collectors.toSet())));

        // 从redis中获取已存在的客户优惠数据
        List<TyCusDiscImportVO> tyCusDiscCacheList = redisCache.getCacheObject(CacheConstants.MIGRATION_TY_CUS_DISCOUNT_KEY + userId);
        if (CollectionUtils.isEmpty(tyCusDiscCacheList)) {
            throw new ServiceException("ty供应商客户优惠列表为空!" + userId, HttpStatus.ERROR);
        }
        // 增加一重保险，客户优惠必须大于0；且必须滤重
        tyCusDiscCacheList = tyCusDiscCacheList.stream().filter(x -> x.getDiscount() > 0).distinct().collect(Collectors.toList());

        // 从redis中获取已存在的商品库存数据
        List<TyProdStockVO> tyStockList = redisCache.getCacheObject(CacheConstants.MIGRATION_TY_PROD_STOCK_KEY + userId);
        if (CollectionUtils.isEmpty(tyStockList)) {
            throw new ServiceException("ty供应商商品库存列表为空!" + userId, HttpStatus.ERROR);
        }
        // TY 货号颜色的库存对应关系
        Map<String, Map<String, TyProdStockVO>> tyProdStockMap = tyStockList.stream().collect(Collectors
                .groupingBy(TyProdStockVO::getProdArtNum, Collectors.toMap(TyProdStockVO::getColorName, x -> x)));
        // TY 货号颜色优惠对应关系
        Map<String, Map<String, List<TyCusDiscImportVO>>> tyCusDiscGroupMap = tyCusDiscCacheList.stream().collect(Collectors
                .groupingBy(TyCusDiscImportVO::getProdArtNum, Collectors.groupingBy(TyCusDiscImportVO::getColorName)));
        // 步橘系统商品所有颜色maps
        Map<Long, Map<String, StoreProductColor>> prodColorGroupMap = prodColorList.stream().collect(Collectors
                .groupingBy(StoreProductColor::getStoreProdId, Collectors.toMap(StoreProductColor::getColorName, x -> x)));
        // 步橘系统客户名称map
        Map<String, StoreCustomer> buJuStoreCusMap = storeCusList.stream().collect(Collectors.toMap(StoreCustomer::getCusName, x -> x));
        List<StoreCustomerProductDiscount> prodCusDiscList = new ArrayList<>();
        List<StoreProductStock> prodStockList = new ArrayList<>();
        storeProdList.forEach(storeProd -> {
            // 当前商品颜色列表 key 颜色中文名称
            Map<String, StoreProductColor> buJuProdColorMap = Optional.ofNullable(prodColorGroupMap.get(storeProd.getId())).orElseThrow(() -> new ServiceException("没有商品颜色!" + storeProd.getProdArtNum(), HttpStatus.ERROR));
            // 根据步橘货号 找到TY对应的货号，可能是列表
            List<String> tyAtrNoList = Optional.ofNullable(tyAfterArtNumGroupMap.get(storeProd.getProdArtNum())).orElseThrow(() -> new ServiceException("没有TY货号!" + storeProd.getProdArtNum(), HttpStatus.ERROR));
            // 处理档口商品库存
            this.handleProdStock(tyAtrNoList, tyProdStockMap, buJuProdColorMap, storeProd.getStoreId(), storeProd.getId(), storeProd.getProdArtNum(), prodStockList);


            tyAtrNoList.forEach(tyAtrNo -> {
                // 处理客户优惠
                this.handleCusDisc(tyAtrNo, tyExistArtNoColorMap, tyCusDiscGroupMap, buJuProdColorMap, buJuStoreCusMap, prodCusDiscList,
                        storeProd.getStoreId(), storeProd.getId(), storeProd.getProdArtNum());
            });


        });
        // 档口客户优惠
        this.storeCusProdDiscMapper.insert(prodCusDiscList);
        // 档口客户库存
        this.prodStockMapper.insert(prodStockList);
        return R.ok();
    }


    /**
     * 初始化档口颜色
     *
     * @param tyProdList TY所有商品sku
     * @param tyAfterArtNumList TY独有商品商品列表
     */
    private void initStoreColorList(Long storeId, List<TyProdImportVO> tyProdList, List<String> tyAfterArtNumList) {
        // 先查询当前档口所有的颜色，如果没有剩余的颜色，则新增导入
        List<StoreColor> existColorList = this.storeColorMapper.selectList(new LambdaQueryWrapper<StoreColor>()
                .eq(StoreColor::getDelFlag, Constants.UNDELETED).eq(StoreColor::getStoreId, storeId));
        List<String> existColorNameList = existColorList.stream().map(StoreColor::getColorName).collect(Collectors.toList());
        // 当前最大的排序
        int maxOrderNum = existColorList.stream().mapToInt(StoreColor::getOrderNum).max().orElse(0) + 1;
        // TY单独处理的货品颜色
        List<String> tyAfterColorList = tyProdList.stream().filter(tyProd -> tyAfterArtNumList.contains(tyProd.getProdArtNum()))
                .map(TyProdImportVO::getColorName).filter(color -> !existColorNameList.contains(color)).distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(tyAfterColorList)) {
            return;
        }
        List<StoreColor> storeColorList = new ArrayList<>();
        for (int i = 0; i < tyAfterColorList.size(); i++) {
            storeColorList.add(new StoreColor().setStoreId(storeId).setColorName(tyAfterColorList.get(i)).setOrderNum(maxOrderNum + i + 1));
        }
        this.storeColorMapper.insert(storeColorList);
    }

    /**
     * 处理商品库存
     */
    private void handleProdStock(List<String> tyAtrNoList, Map<String, Map<String, TyProdStockVO>> tyProdStockMap, Map<String, StoreProductColor> buJuProdColorMap,
                                 Long storeId, Long storeProdId, String prodArtNum, List<StoreProductStock> prodStockList) {
        // 获取ty货号所有颜色的库存
        Map<String, TyProdStockVO> tyColorStockMap = new HashMap<>();
        tyAtrNoList.forEach(tyArtNo -> tyColorStockMap.putAll(tyProdStockMap.getOrDefault(tyArtNo, new HashMap<>())));
        buJuProdColorMap.forEach((buJuColor, buJuProdColor) -> {
            StoreProductStock stock = new StoreProductStock().setStoreId(storeId).setStoreProdId(storeProdId).setProdArtNum(prodArtNum)
                    .setColorName(buJuProdColorMap.get(buJuColor).getColorName()).setStoreProdColorId(buJuProdColorMap.get(buJuColor).getId())
                    .setStoreColorId(buJuProdColorMap.get(buJuColor).getStoreColorId());
            if (tyColorStockMap.containsKey(buJuColor)) {
                TyProdStockVO tySizeStock = tyColorStockMap.getOrDefault(buJuColor, null);
                stock.setSize30(ObjectUtils.isNotEmpty(tySizeStock) ? tySizeStock.getSize30() : null)
                        .setSize31(ObjectUtils.isNotEmpty(tySizeStock) ? tySizeStock.getSize31() : null)
                        .setSize32(ObjectUtils.isNotEmpty(tySizeStock) ? tySizeStock.getSize32() : null)
                        .setSize33(ObjectUtils.isNotEmpty(tySizeStock) ? tySizeStock.getSize33() : null)
                        .setSize34(ObjectUtils.isNotEmpty(tySizeStock) ? tySizeStock.getSize34() : null)
                        .setSize35(ObjectUtils.isNotEmpty(tySizeStock) ? tySizeStock.getSize35() : null)
                        .setSize36(ObjectUtils.isNotEmpty(tySizeStock) ? tySizeStock.getSize36() : null)
                        .setSize37(ObjectUtils.isNotEmpty(tySizeStock) ? tySizeStock.getSize37() : null)
                        .setSize38(ObjectUtils.isNotEmpty(tySizeStock) ? tySizeStock.getSize38() : null)
                        .setSize39(ObjectUtils.isNotEmpty(tySizeStock) ? tySizeStock.getSize39() : null)
                        .setSize40(ObjectUtils.isNotEmpty(tySizeStock) ? tySizeStock.getSize40() : null)
                        .setSize41(ObjectUtils.isNotEmpty(tySizeStock) ? tySizeStock.getSize41() : null)
                        .setSize42(ObjectUtils.isNotEmpty(tySizeStock) ? tySizeStock.getSize42() : null)
                        .setSize43(ObjectUtils.isNotEmpty(tySizeStock) ? tySizeStock.getSize43() : null);
            }
            prodStockList.add(stock);
        });
    }

    /**
     * 处理档口客户优惠
     */
    private void handleCusDisc(String tyAtrNo, Map<String, Set<String>> tyExistArtNoColorMap, Map<String, Map<String, List<TyCusDiscImportVO>>> tyCusDiscGroupMap,
                               Map<String, StoreProductColor> buJuProdColorMap, Map<String, StoreCustomer> buJuStoreCusMap,
                               List<StoreCustomerProductDiscount> prodCusDiscList, Long storeId, Long storeProdId, String prodArtNum) {
        // TY货号下有哪些颜色存在客户优惠
        Map<String, List<TyCusDiscImportVO>> tyColorCusDiscMap = tyCusDiscGroupMap.get(tyAtrNo);
        if (MapUtils.isEmpty(tyColorCusDiscMap)) {
            return;
        }
        // ty当前货号正在生效的颜色列表
        Set<String> existColorSet = tyExistArtNoColorMap.getOrDefault(tyAtrNo, Collections.emptySet());
        // 依次遍历存在优惠的颜色，设置步橘系统客户优惠关系
        tyColorCusDiscMap.forEach((tyColor, tyCusDiscList) -> {
            // 必须是现在正在生效的颜色，才会被添加到系统中
            if (!existColorSet.contains(tyColor)) {
                return;
            }
            // TODO 该处TY 与 FHB处理不同
            // TODO 该处TY 与 FHB处理不同
            // TODO 该处TY 与 FHB处理不同
            tyCusDiscList.stream().filter(x -> x.getDiscount() > 0).forEach(tyCusDisc -> {
                StoreProductColor buJuProdColor = Optional.ofNullable(buJuProdColorMap.get(tyColor)).orElseThrow(() -> new ServiceException("没有步橘系统对应的颜色!" + tyColor, HttpStatus.ERROR));
                StoreCustomer storeCus = Optional.ofNullable(buJuStoreCusMap.get(tyCusDisc.getCusName())).orElseThrow(() -> new ServiceException("没有步橘系统对应的客户!" + tyCusDisc.getCusName(), HttpStatus.ERROR));
                // 将FHB客户优惠 转为步橘系统优惠
                prodCusDiscList.add(new StoreCustomerProductDiscount().setStoreId(storeId).setStoreProdId(storeProdId).setStoreCusId(storeCus.getId()).setProdArtNum(prodArtNum)
                        .setStoreCusName(storeCus.getCusName()).setStoreProdColorId(buJuProdColor.getId()).setDiscount(tyCusDisc.getDiscount()));
            });
        });
    }

}
