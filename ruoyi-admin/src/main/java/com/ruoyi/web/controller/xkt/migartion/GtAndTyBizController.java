package com.ruoyi.web.controller.xkt.migartion;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.web.controller.xkt.migartion.vo.GtAndTYCompareDownloadVO;
import com.ruoyi.web.controller.xkt.migartion.vo.GtAndTYInitVO;
import com.ruoyi.web.controller.xkt.migartion.vo.gt.GtCateVO;
import com.ruoyi.web.controller.xkt.migartion.vo.gt.GtProdSkuVO;
import com.ruoyi.web.controller.xkt.migartion.vo.ty.TyCusDiscImportVO;
import com.ruoyi.web.controller.xkt.migartion.vo.ty.TyCusImportVO;
import com.ruoyi.web.controller.xkt.migartion.vo.ty.TyProdImportVO;
import com.ruoyi.xkt.domain.*;
import com.ruoyi.xkt.enums.EProductStatus;
import com.ruoyi.xkt.enums.ListingType;
import com.ruoyi.xkt.mapper.*;
import com.ruoyi.xkt.service.shipMaster.IShipMasterService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Compare 相关
 *
 * @author ruoyi
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/v1/gt-ty")
public class GtAndTyBizController extends BaseController {

    final IShipMasterService shipMasterService;
    final RedisCache redisCache;
    final StoreProductColorMapper prodColorMapper;
    final StoreProductColorSizeMapper prodColorSizeMapper;
    final StoreProductMapper storeProdMapper;
    final StoreProductStockMapper prodStockMapper;
    final StoreCustomerMapper storeCusMapper;
    final StoreCustomerProductDiscountMapper storeCusProdDiscMapper;
    final StoreColorMapper storeColorMapper;
    final StoreMapper storeMapper;
    final StoreProductServiceMapper prodSvcMapper;
    final StoreProductCategoryAttributeMapper prodCateAttrMapper;
    final SysProductCategoryMapper prodCateMapper;

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @PutMapping("/sync-es/{storeId}")
    public void syncToEs(@PathVariable("storeId") Long storeId) {
        // 同步主图 到 图搜 服务器

        // TODO 上传到ES之后还需要确认
        // TODO 上传到ES之后还需要确认
        // TODO 上传到ES之后还需要确认

    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @PutMapping("/sync-pic/{storeId}")
    public void syncToPicSearch(@PathVariable("storeId") Long storeId) {
        // 同步主图 到 图搜 服务器

        // TODO 上传到图搜服务器之后还要确认
        // TODO 上传到图搜服务器之后还要确认
        // TODO 上传到图搜服务器之后还要确认

    }


    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @GetMapping("/-R/compare/{userId}")
    public void compare(HttpServletResponse response, @PathVariable("userId") Integer userId) throws UnsupportedEncodingException {
        Map<String, List<String>> multiSaleSameGoMap = new HashMap<>();
        Map<String, List<String>> multiOffSaleSameGoMap = new HashMap<>();
        Map<String, List<String>> multiSameTyMap = new HashMap<>();

        List<GtProdSkuVO> gtSaleBasicList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_GT_SALE_BASIC_KEY + userId), new ArrayList<>());
        Map<String, String> articleNoColorMap = gtSaleBasicList.stream().collect(Collectors.groupingBy(GtProdSkuVO::getArticle_number,
                Collectors.collectingAndThen(Collectors.mapping(GtProdSkuVO::getColor, Collectors.toList()),
                        list -> "(" + list.stream().distinct().collect(Collectors.joining(",")) + ")")));
        List<String> doubleRunSaleArtNoList = gtSaleBasicList.stream().map(GtProdSkuVO::getArticle_number)
                .distinct().collect(Collectors.toList());
        // 查看double_run 在售的商品 这边有多少相似的货号
        doubleRunSaleArtNoList.forEach(article_number -> {
            // 只保留核心连续的数字，去除其他所有符号
            String cleanArtNo = this.extractCoreArticleNumber(article_number);
            List<String> existList = multiSaleSameGoMap.containsKey(cleanArtNo) ? multiSaleSameGoMap.get(cleanArtNo) : new ArrayList<>();
            existList.add(article_number + articleNoColorMap.get(article_number));
            multiSaleSameGoMap.put(cleanArtNo, existList);
        });

        // 查看gt 下架的商品有多少相似的货号
        List<GtProdSkuVO> doubleRunOffSaleBasicList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_GT_OFF_SALE_BASIC_KEY + userId), new ArrayList<>());
        List<String> doubleRunOffSaleArtNoList = doubleRunOffSaleBasicList.stream().map(GtProdSkuVO::getArticle_number)
                .distinct().collect(Collectors.toList());
        doubleRunOffSaleArtNoList.forEach(article_number -> {
            // 只保留核心连续的数字，去除其他所有符号
            String cleanArtNo = this.extractCoreArticleNumber(article_number);
            List<String> existList = multiOffSaleSameGoMap.containsKey(cleanArtNo) ? multiOffSaleSameGoMap.get(cleanArtNo) : new ArrayList<>();
            existList.add(article_number);
            multiOffSaleSameGoMap.put(cleanArtNo, existList);
        });

        // 查看ty 这边有多少相似的货号
        List<TyProdImportVO> tyProdList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_TY_PROD_KEY + userId), new ArrayList<>());
        Map<String, String> tyProdArtNumColorMap = tyProdList.stream().collect(Collectors.groupingBy(TyProdImportVO::getProdArtNum,
                Collectors.collectingAndThen(Collectors.mapping(TyProdImportVO::getColorName, Collectors.toList()),
                        list -> "(" + list.stream().distinct().collect(Collectors.joining(",")) + ")")));
        List<String> tyArtNoList = tyProdList.stream().map(TyProdImportVO::getProdArtNum).distinct().collect(Collectors.toList());
        tyArtNoList.forEach(prodArtNum -> {
            // 只保留核心连续的数字，去除其他所有符号
            String cleanArtNo = this.extractCoreArticleNumber(prodArtNum);
            List<String> existList = multiSameTyMap.containsKey(cleanArtNo) ? multiSameTyMap.get(cleanArtNo) : new ArrayList<>();
            existList.add(prodArtNum + tyProdArtNumColorMap.get(prodArtNum));
            multiSameTyMap.put(cleanArtNo, existList);
        });
        // 清洗数据之后，GO平台和TY平台 货号一致的，按照这种来展示: GT => [Z1110(黑色,黑色绒里,棕色,棕色绒里)] <= 清洗后的货号 => [Z1110(黑色,黑色绒里,棕色,棕色绒里)] <= TY

        System.err.println("============ 两边系统“一致”的货号 ============");
        // 清洗后，相同货号映射
        List<String> matchArtNoList = new ArrayList<>();
        Set<String> commonArtNos = new HashSet<>(multiSaleSameGoMap.keySet());
        commonArtNos.retainAll(multiSameTyMap.keySet());
        commonArtNos.forEach(artNo -> {
            final String sameArtNo = "GT => " + multiSaleSameGoMap.get(artNo) + " <= " + artNo + " => " + multiSameTyMap.get(artNo) + " <= TY";
            matchArtNoList.add(sameArtNo);
        });
        // 输出货号清洗后相同的货号
        matchArtNoList.forEach(System.out::println);

        matchArtNoList.add("============ GT独有的货号 ============");
        matchArtNoList.add("============ GT独有的货号 ============");

        System.err.println("============ GT独有的key ============");
        // 获取GO2独有的key
        Set<String> onlyInGoMap = new HashSet<>(multiSaleSameGoMap.keySet());
        onlyInGoMap.removeAll(commonArtNos);
        if (CollectionUtils.isNotEmpty(onlyInGoMap)) {
            onlyInGoMap.forEach(x -> {
                matchArtNoList.addAll(multiSaleSameGoMap.get(x));
                System.out.println(multiSaleSameGoMap.get(x));
            });
        }

        matchArtNoList.add("============ TY独有的货号 ============");
        matchArtNoList.add("============ TY独有的货号 ============");

        System.err.println("============ TY 去掉公共的、下架的 独有的key ============");
        // 获取TY独有的key  去掉公共的、去掉下架的商品
        Set<String> onlyInTYMap = new HashSet<>(multiSameTyMap.keySet());
        onlyInTYMap.removeAll(commonArtNos);
        onlyInTYMap.removeAll(multiOffSaleSameGoMap.keySet());
        if (CollectionUtils.isNotEmpty(onlyInTYMap)) {
            onlyInTYMap.forEach(x -> {
                matchArtNoList.addAll(multiSameTyMap.get(x));
                System.out.println(multiSameTyMap.get(x));
            });
        }

        List<GtAndTYCompareDownloadVO> downloadList = new ArrayList<>();
        for (int i = 0; i < matchArtNoList.size(); i++) {
            downloadList.add(new GtAndTYCompareDownloadVO().setOrderNum(i + 1).setCode(matchArtNoList.get(i)));
        }
        ExcelUtil<GtAndTYCompareDownloadVO> util = new ExcelUtil<>(GtAndTYCompareDownloadVO.class);
        String encodedFileName = URLEncoder.encode("GT与TY差异" + DateUtils.getDate(), "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename=" + encodedFileName + ".xlsx");
        util.exportExcel(response, downloadList, "差异");
    }


    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @PutMapping("/init")

    @Transactional

    public void initToDB(@Validated @RequestBody GtAndTYInitVO initVO) {
        // 去掉可能的空格
        initVO.setExcludeArtNoList(initVO.getExcludeArtNoList().stream().map(String::trim).collect(Collectors.toList()));
        Optional.ofNullable(this.storeMapper.selectOne(new LambdaQueryWrapper<Store>()
                        .eq(Store::getId, initVO.getStoreId()).eq(Store::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("档口不存在!", HttpStatus.ERROR));
        // 步骤1: 准备数据，新建颜色
        Map<String, StoreColor> storeColorMap = this.initStoreColorList(initVO.getStoreId(), initVO.getUserId());
        // 步骤2: GT 和 TY 货号对应关系，然后直接copy 对应的属性关系
        // a. 商品与颜色对应关系
        // b. 商品颜色尺码 + 价格 对应关系
        // c. 库存初始化
        // d. 服务承诺初始化
        // e. 类目属性初始化
        this.init(initVO, storeColorMap);

    }

    private void init(GtAndTYInitVO initVO, Map<String, StoreColor> storeColorMap) {
        Map<String, List<String>> multiSaleSameGoMap = new HashMap<>();
        Map<String, List<String>> multiOffSaleSameGoMap = new HashMap<>();
        Map<String, List<String>> multiSameTyMap = new HashMap<>();
        List<GtProdSkuVO> gtSaleBasicList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_GT_SALE_BASIC_KEY + initVO.getUserId()), new ArrayList<>());
        // 查看gt 在售的商品 这边有多少相似的货号
        gtSaleBasicList.stream().map(GtProdSkuVO::getArticle_number).distinct()
                // 过滤掉需要特殊处理的货号
                .filter(article_number -> !initVO.getExcludeArtNoList().contains(article_number))
                .forEach(article_number -> {
                    // 只保留核心连续的数字，去除其他所有符号
                    String cleanArtNo = this.extractCoreArticleNumber(article_number);
                    List<String> existList = multiSaleSameGoMap.containsKey(cleanArtNo) ? multiSaleSameGoMap.get(cleanArtNo) : new ArrayList<>();
                    existList.add(article_number);
                    multiSaleSameGoMap.put(cleanArtNo, existList);
                });

        // 查看gt 下架的商品有多少相似的货号
        List<GtProdSkuVO> gtOffSaleBasicList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_GT_OFF_SALE_BASIC_KEY + initVO.getUserId()), new ArrayList<>());
        gtOffSaleBasicList.stream().map(GtProdSkuVO::getArticle_number).distinct().forEach(article_number -> {
            // 只保留核心连续的数字，去除其他所有符号
            String cleanArtNo = this.extractCoreArticleNumber(article_number);
            List<String> existList = multiOffSaleSameGoMap.containsKey(cleanArtNo) ? multiOffSaleSameGoMap.get(cleanArtNo) : new ArrayList<>();
            existList.add(article_number);
            multiOffSaleSameGoMap.put(cleanArtNo, existList);
        });

        // 查看TY 这边有多少相似的货号
        List<TyProdImportVO> tyProdList = redisCache.getCacheObject(CacheConstants.MIGRATION_TY_PROD_KEY + initVO.getUserId());
        // TY按照颜色分类
        Map<String, List<TyProdImportVO>> tyProdGroupMap = tyProdList.stream().collect(Collectors.groupingBy(TyProdImportVO::getProdArtNum));
        tyProdList.stream().map(TyProdImportVO::getProdArtNum).distinct()
                // 过滤掉需要特殊处理的货号
                .filter(artNo -> !initVO.getExcludeArtNoList().contains(artNo))
                .forEach(artNo -> {
                    // 只保留核心连续的数字，去除其他所有符号
                    String cleanArtNo = this.extractCoreArticleNumber(artNo);
                    List<String> existList = multiSameTyMap.containsKey(cleanArtNo) ? multiSameTyMap.get(cleanArtNo) : new ArrayList<>();
                    existList.add(artNo);
                    multiSameTyMap.put(cleanArtNo, existList);
                });

        // gt按照货号分组
        Map<String, List<GtProdSkuVO>> gtSaleGroupMap = gtSaleBasicList.stream().collect(Collectors.groupingBy(GtProdSkuVO::getArticle_number));

        // GT分类
        List<GtCateVO.GCIDataVO> cacheList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_GT_SALE_CATE_KEY + initVO.getUserId()), new ArrayList<>());
        List<SysProductCategory> prodCateList = this.prodCateMapper.selectList(new LambdaQueryWrapper<SysProductCategory>()
                .eq(SysProductCategory::getDelFlag, Constants.UNDELETED));
        Map<String, Long> dbCateNameMap = prodCateList.stream().collect(Collectors.toMap(SysProductCategory::getName, SysProductCategory::getId));
        // GT商品分类和步橘分类映射
        Map<Integer, Long> cateRelationMap = new HashMap<>();
        cacheList.forEach(gtCate -> {
            final Long cateId = Optional.ofNullable(dbCateNameMap.get(gtCate.getName())).orElseThrow(() -> new ServiceException("GT分类不存在!", HttpStatus.ERROR));
            cateRelationMap.put(gtCate.getId(), cateId);
        });

        System.err.println("============ 两边系统“一致”的货号 ============");
        // 清洗后，相同货号映射
        Set<String> commonArtNos = new HashSet<>(multiSaleSameGoMap.keySet());
        commonArtNos.retainAll(multiSameTyMap.keySet());
        // 待导入的商品
        List<StoreProduct> storeProdList = new ArrayList<>();
        // 当天
        final Date voucherDate = java.sql.Date.valueOf(LocalDate.now());
        // 所有商品的类目属性map  key gt的product_id value StoreProductCategoryAttribute
        Map<Integer, StoreProductCategoryAttribute> prodAttrMap = new HashMap<>();
        commonArtNos.forEach(cleanArtNo -> {
            // 获取GT匹配的商品中的第一个商品
            List<GtProdSkuVO> gtMatchSkuList = this.getGtFirstSku(multiSaleSameGoMap, gtSaleGroupMap, cleanArtNo);
            // 初始化档口商品
            StoreProduct storeProd = new StoreProduct().setStoreId(initVO.getStoreId()).setProdCateId(cateRelationMap.get(gtMatchSkuList.get(0).getCategory_nid()))
                    .setProdArtNum(cleanArtNo).setProdTitle(gtMatchSkuList.get(0).getCharacters()).setListingWay(ListingType.RIGHT_NOW.getValue())
                    .setVoucherDate(voucherDate).setProdStatus(EProductStatus.ON_SALE.getValue()).setRecommendWeight(0L).setSaleWeight(0L).setPopularityWeight(0L);
            // 提前设置档口商品的类目属性
            this.preMatchAttr(gtMatchSkuList.get(0).getProduct_id(), initVO.getUserId(), prodAttrMap);
            storeProdList.add(storeProd);
        });
        this.storeProdMapper.insert(storeProdList);
        // 货号map
        Map<Long, String> prodArtNumMap = storeProdList.stream().collect(Collectors.toMap(StoreProduct::getId, StoreProduct::getProdArtNum));

        // 商品所有颜色 尺码 颜色库存初始化
        List<StoreProductColor> prodColorList = new ArrayList<>();
        List<StoreProductColorSize> prodColorSizeList = new ArrayList<>();
        List<StoreProductService> prodSvcList = new ArrayList<>();
        List<StoreProductCategoryAttribute> prodAttrList = new ArrayList<>();
        storeProdList.forEach(storeProd -> {
            // TY匹配的货号
            List<String> tyMatchArtNoList = multiSameTyMap.get(storeProd.getProdArtNum());
            // 获取GT匹配的商品sku列表
            List<GtProdSkuVO> gtMatchSkuList = this.getGtFirstSku(multiSaleSameGoMap, gtSaleGroupMap, storeProd.getProdArtNum());
            // 当前货号在GT的所有尺码，作为标准尺码
            List<Integer> gtStandardSizeList = gtMatchSkuList.stream().map(sku -> (int) Math.floor(Double.parseDouble(sku.getSize())))
                    .collect(Collectors.toList());
            // 先获取最低价格，然后给所有颜色和尺码添加初始值，之后再来单独改，这是最方便的方式了
            final BigDecimal minPrice = gtMatchSkuList.stream().map(GtProdSkuVO::getPrice).min(Comparator.comparing(x -> x))
                    .orElseThrow(() -> new ServiceException("没有GT商品价格!", HttpStatus.ERROR));
            tyMatchArtNoList.forEach(tyArtNo -> {
                List<TyProdImportVO> tyMatchSkuList = tyProdGroupMap.get(tyArtNo);
                for (int i = 0; i < tyMatchSkuList.size(); i++) {
                    StoreColor storeColor = Optional.ofNullable(storeColorMap.get(tyMatchSkuList.get(i).getColorName()))
                            .orElseThrow(() -> new ServiceException("没有TY商品颜色!" + tyArtNo, HttpStatus.ERROR));
                    // 该商品的颜色
                    prodColorList.add(new StoreProductColor().setStoreId(storeProd.getStoreId()).setStoreProdId(storeProd.getId()).setOrderNum(i + 1)
                            .setColorName(storeColor.getColorName()).setStoreColorId(storeColor.getId()).setProdStatus(EProductStatus.ON_SALE.getValue()));
                    // 该颜色所有的尺码
                    for (int j = 0; j < Constants.SIZE_LIST.size(); j++) {
                        // TY系统条码前缀
                        final String otherSnPrefix = tyMatchSkuList.get(i).getTySnPrefix() + Constants.SIZE_LIST.get(j);
                        prodColorSizeList.add(new StoreProductColorSize().setSize(Constants.SIZE_LIST.get(j)).setStoreColorId(storeColor.getId())
                                .setStoreProdId(storeProd.getId()).setPrice(minPrice).setOtherSnPrefix(otherSnPrefix).setNextSn(0)
                                .setStandard(gtStandardSizeList.contains(Constants.SIZE_LIST.get(j)) ? 1 : 0));
                    }
                }
            });

            // 初始化商品服务承诺
            prodSvcList.add(new StoreProductService().setStoreProdId(storeProd.getId()).setCustomRefund("0")
                    .setThirtyDayRefund("0").setOneBatchSale("1").setRefundWithinThreeDay("0"));

            // 初始化商品的类目属性
            StoreProductCategoryAttribute cateAttr = Optional.ofNullable(prodAttrMap.get(gtMatchSkuList.get(0).getProduct_id()))
                    .orElseThrow(() -> new ServiceException("没有GT商品类目属性!" + storeProd.getProdArtNum(), HttpStatus.ERROR));
            cateAttr.setStoreId(storeProd.getStoreId()).setStoreProdId(storeProd.getId());
            prodAttrList.add(cateAttr);
        });
        // 插入商品颜色及颜色对应的尺码，档口服务承诺
        this.prodColorMapper.insert(prodColorList);
        this.prodColorSizeMapper.insert(prodColorSizeList);
        this.prodSvcMapper.insert(prodSvcList);
        this.prodCateAttrMapper.insert(prodAttrList);

        // 还要更新步橘系统的条码前缀
        prodColorSizeList.forEach(x -> x.setSnPrefix(initVO.getStoreId() + String.format("%08d", x.getId())));
        this.prodColorSizeMapper.updateById(prodColorSizeList);

        // 商品颜色对应的库存初始化
        List<StoreProductStock> prodStockList = prodColorList.stream().map(color -> new StoreProductStock().setStoreId(color.getStoreId())
                        .setStoreProdId(color.getStoreProdId()).setProdArtNum(prodArtNumMap.get(color.getStoreProdId()))
                        .setStoreProdColorId(color.getId()).setStoreColorId(color.getStoreColorId()).setColorName(color.getColorName()))
                .collect(Collectors.toList());
        this.prodStockMapper.insert(prodStockList);

        // 步骤3: 准备数据，新建客户
        List<StoreCustomer> storeCusList = this.initStoreCusList(initVO);

        // 步骤4: 客户与货号的优惠关系
        this.initStoreCusProdDiscList(initVO, storeProdList, storeCusList, prodColorList, multiSameTyMap);

    }


    /**
     * 初始化系统颜色
     *
     * @param storeId 档口ID
     * @param userId  GT用户ID
     * @return
     */
    private Map<String, StoreColor> initStoreColorList(Long storeId, Integer userId) {
        // TY商品缓存
        List<TyProdImportVO> cacheList = redisCache.getCacheObject(CacheConstants.MIGRATION_TY_PROD_KEY + userId);
        if (CollectionUtils.isEmpty(cacheList)) {
            throw new ServiceException("TY商品列表为空", HttpStatus.ERROR);
        }
        List<String> tyColorList = cacheList.stream().map(TyProdImportVO::getColorName).distinct().collect(Collectors.toList());
        List<StoreColor> storeColorList = new ArrayList<>();
        for (int i = 0; i < tyColorList.size(); i++) {
            storeColorList.add(new StoreColor().setStoreId(storeId).setColorName(tyColorList.get(i)).setOrderNum(i + 1));
        }
        this.storeColorMapper.insert(storeColorList);
        return storeColorList.stream().collect(Collectors.toMap(StoreColor::getColorName, x -> x));
    }

    /**
     * 新建档口客户对应产品的优惠
     *
     * @param initVO
     * @param storeProdList
     * @param storeCusList
     * @param prodColorList
     * @param multiSameTyMap
     */
    private void initStoreCusProdDiscList(GtAndTYInitVO initVO, List<StoreProduct> storeProdList, List<StoreCustomer> storeCusList,
                                          List<StoreProductColor> prodColorList, Map<String, List<String>> multiSameTyMap) {
        // 从redis中获取已存在的客户优惠数据
        List<TyCusDiscImportVO> tyCusDiscCacheList = redisCache.getCacheObject(CacheConstants.MIGRATION_TY_CUS_DISCOUNT_KEY + initVO.getUserId());
        if (CollectionUtils.isEmpty(tyCusDiscCacheList)) {
            throw new ServiceException("ty供应商客户优惠列表为空!" + initVO.getUserId(), HttpStatus.ERROR);
        }
        // TY 货号颜色优惠对应关系
        Map<String, Map<String, List<TyCusDiscImportVO>>> tyCusDiscGroupMap = tyCusDiscCacheList.stream().collect(Collectors
                .groupingBy(TyCusDiscImportVO::getProdArtNum, Collectors.groupingBy(TyCusDiscImportVO::getColorName)));
        // 步橘系统商品所有颜色maps
        Map<Long, Map<String, StoreProductColor>> prodColorGroupMap = prodColorList.stream().collect(Collectors
                .groupingBy(StoreProductColor::getStoreProdId, Collectors.toMap(StoreProductColor::getColorName, x -> x)));
        // 步橘系统客户名称map
        Map<String, StoreCustomer> buJuStoreCusMap = storeCusList.stream().collect(Collectors.toMap(StoreCustomer::getCusName, x -> x));
        List<StoreCustomerProductDiscount> prodCusDiscList = new ArrayList<>();
        // 依次遍历商品列表，找到货号和FHB货号对应关系，然后用颜色进行匹配，建立客户优惠关系
        storeProdList.forEach(storeProd -> {
            // 当前商品颜色列表 key 颜色中文名称
            Map<String, StoreProductColor> buJuProdColorMap = Optional.ofNullable(prodColorGroupMap.get(storeProd.getId())).orElseThrow(() -> new ServiceException("没有商品颜色!" + storeProd.getProdArtNum(), HttpStatus.ERROR));
            // 根据步橘货号 找到TY对应的货号，可能是列表
            List<String> tyAtrNoList = Optional.ofNullable(multiSameTyMap.get(storeProd.getProdArtNum())).orElseThrow(() -> new ServiceException("没有TY货号!" + storeProd.getProdArtNum(), HttpStatus.ERROR));
            tyAtrNoList.forEach(tyAtrNo -> {
                // TY货号下有哪些颜色存在客户优惠
                Map<String, List<TyCusDiscImportVO>> tyColorCusDiscMap = tyCusDiscGroupMap.get(tyAtrNo);
                if (MapUtils.isEmpty(tyColorCusDiscMap)) {
                    return;
                }
                // 依次遍历存在优惠的颜色，设置步橘系统客户优惠关系
                tyColorCusDiscMap.forEach((tyColor, tyCusDiscList) -> tyCusDiscList.forEach(tyCusDisc -> {
                    StoreProductColor buJuProdColor = Optional.ofNullable(buJuProdColorMap.get(tyColor)).orElseThrow(() -> new ServiceException("没有步橘系统对应的颜色!" + tyColor, HttpStatus.ERROR));
                    StoreCustomer storeCus = Optional.ofNullable(buJuStoreCusMap.get(tyCusDisc.getCusName())).orElseThrow(() -> new ServiceException("没有步橘系统对应的客户!" + tyCusDisc.getCusName(), HttpStatus.ERROR));
                    // 将FHB客户优惠 转为步橘系统优惠
                    prodCusDiscList.add(new StoreCustomerProductDiscount().setStoreId(storeProd.getStoreId()).setStoreProdId(storeProd.getId()).setStoreCusId(storeCus.getId())
                            .setStoreCusName(storeCus.getCusName()).setStoreProdColorId(buJuProdColor.getId()).setDiscount(tyCusDisc.getDiscount()));
                }));
            });
        });
        this.storeCusProdDiscMapper.insert(prodCusDiscList);
    }

    /**
     * 初始化客户列表
     *
     * @param initVO 入参
     * @return List<StoreCustomer>
     */
    private List<StoreCustomer> initStoreCusList(GtAndTYInitVO initVO) {
        List<TyCusImportVO> cacheList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_TY_CUS_KEY + initVO.getUserId()), new ArrayList<>());
        if (CollectionUtils.isEmpty(cacheList)) {
            throw new ServiceException("供应商客户列表为空!", HttpStatus.ERROR);
        }
        List<StoreCustomer> storeCusList = cacheList.stream()
                // 排除掉现金客户 因为档口入驻时，会自动创建现金客户
                .filter(x -> !Objects.equals(x.getCusName(), Constants.STORE_CUS_CASH))
                .map(x -> new StoreCustomer().setStoreId(initVO.getStoreId()).setCusName(x.getCusName()))
                .collect(Collectors.toList());
        this.storeCusMapper.insert(storeCusList);
        return storeCusList;
    }

    /**
     * 提前匹配类目属性
     *
     * @param product_id  GT商品ID
     * @param userId      GT用户ID
     * @param prodAttrMap 类目属性
     */
    private void preMatchAttr(Integer product_id, Integer userId, Map<Integer, StoreProductCategoryAttribute> prodAttrMap) {
        // 类目属性
        Map<String, String> attrMap = redisCache.getCacheMap(CacheConstants.MIGRATION_GT_SALE_ATTR_KEY + userId + "_" + product_id);
        if (MapUtils.isEmpty(attrMap)) {
            return;
        }
        // 将attrMap 的 key 映射到系统中
        StoreProductCategoryAttribute prodAttr = new StoreProductCategoryAttribute();
        // 1. 帮面材质
        if (attrMap.containsKey(Constants.UPPER_MATERIAL_NAME)) {
            prodAttr.setUpperMaterial(attrMap.get(Constants.UPPER_MATERIAL_NAME));
        }
        // 2. 靴筒内里材质
        if (attrMap.containsKey(Constants.SHAFT_LINING_MATERIAL_NAME)) {
            prodAttr.setShaftLiningMaterial(attrMap.get(Constants.SHAFT_LINING_MATERIAL_NAME));
        }
        // 3. 靴筒面材质
        if (attrMap.containsKey(Constants.SHAFT_MATERIAL_NAME)) {
            prodAttr.setShaftMaterial(attrMap.get(Constants.SHAFT_MATERIAL_NAME));
        }
        // 4. 鞋面内里材质
        if (attrMap.containsKey(Constants.SHOE_UPPER_LINING_MATERIAL_NAME)) {
            prodAttr.setShoeUpperLiningMaterial(attrMap.get(Constants.SHOE_UPPER_LINING_MATERIAL_NAME));
        }
        // 5. 靴款品名
        if (attrMap.containsKey(Constants.SHOE_STYLE_NAME_NAME)) {
            prodAttr.setShoeStyleName(attrMap.get(Constants.SHOE_STYLE_NAME_NAME));
        }
        // 6. 筒高
        if (attrMap.containsKey(Constants.SHAFT_HEIGHT_NAME)) {
            prodAttr.setShaftHeight(attrMap.get(Constants.SHAFT_HEIGHT_NAME));
        }
        // 7. 鞋垫材质
        if (attrMap.containsKey(Constants.INSOLE_MATERIAL_NAME)) {
            prodAttr.setInsoleMaterial(attrMap.get(Constants.INSOLE_MATERIAL_NAME));
        }
        // 8. 上市年份季节
        if (attrMap.containsKey(Constants.RELEASE_YEAR_SEASON_NAME)) {
            prodAttr.setReleaseYearSeason(attrMap.get(Constants.RELEASE_YEAR_SEASON_NAME));
        }
        // 9. 后跟高
        if (attrMap.containsKey(Constants.HEEL_HEIGHT_NAME)) {
            prodAttr.setHeelHeight(attrMap.get(Constants.HEEL_HEIGHT_NAME));
        }
        // 10. 跟底款式
        if (attrMap.containsKey(Constants.HEEL_TYPE_NAME)) {
            prodAttr.setHeelType(attrMap.get(Constants.HEEL_TYPE_NAME));
        }
        // 11. 鞋头款式
        if (attrMap.containsKey(Constants.TOE_STYLE_NAME)) {
            prodAttr.setToeStyle(attrMap.get(Constants.TOE_STYLE_NAME));
        }
        // 12. 适合季节
        if (attrMap.containsKey(Constants.SUITABLE_SEASON_NAME)) {
            prodAttr.setSuitableSeason(attrMap.get(Constants.SUITABLE_SEASON_NAME));
        }
        // 13. 开口深度
        if (attrMap.containsKey(Constants.COLLAR_DEPTH_NAME)) {
            prodAttr.setCollarDepth(attrMap.get(Constants.COLLAR_DEPTH_NAME));
        }
        // 14. 鞋底材质
        if (attrMap.containsKey(Constants.OUTSOLE_MATERIAL_NAME)) {
            prodAttr.setOutsoleMaterial(attrMap.get(Constants.OUTSOLE_MATERIAL_NAME));
        }
        // 15. 风格
        if (attrMap.containsKey(Constants.STYLE_NAME)) {
            prodAttr.setStyle(attrMap.get(Constants.STYLE_NAME));
        }
        // 16. 款式
        if (attrMap.containsKey(Constants.DESIGN_NAME)) {
            prodAttr.setDesign(attrMap.get(Constants.DESIGN_NAME));
        }
        // 17. 皮质特征
        if (attrMap.containsKey(Constants.LEATHER_FEATURES_NAME)) {
            prodAttr.setLeatherFeatures(attrMap.get(Constants.LEATHER_FEATURES_NAME));
        }
        // 18. 制作工艺
        if (attrMap.containsKey(Constants.MANUFACTURING_PROCESS_NAME)) {
            prodAttr.setManufacturingProcess(attrMap.get(Constants.MANUFACTURING_PROCESS_NAME));
        }
        // 19. 图案
        if (attrMap.containsKey(Constants.PATTERN_NAME)) {
            prodAttr.setPattern(attrMap.get(Constants.PATTERN_NAME));
        }
        // 20. 闭合方式
        if (attrMap.containsKey(Constants.CLOSURE_TYPE_NAME)) {
            prodAttr.setClosureType(attrMap.get(Constants.CLOSURE_TYPE_NAME));
        }
        // 21. 适用场景
        if (attrMap.containsKey(Constants.OCCASION_NAME)) {
            prodAttr.setOccasion(attrMap.get(Constants.OCCASION_NAME));
        }
        // 22. 厚薄
        if (attrMap.containsKey(Constants.THICKNESS_NAME)) {
            prodAttr.setThickness(attrMap.get(Constants.THICKNESS_NAME));
        }
        // 23. 流行元素
        if (attrMap.containsKey(Constants.FASHION_ELEMENTS_NAME)) {
            prodAttr.setFashionElements(attrMap.get(Constants.FASHION_ELEMENTS_NAME));
        }
        // 24. 适用对象
        if (attrMap.containsKey(Constants.SUITABLE_PERSON_NAME)) {
            prodAttr.setSuitablePerson(attrMap.get(Constants.SUITABLE_PERSON_NAME));
        }
        prodAttrMap.put(product_id, prodAttr);
    }

    /**
     * 取GT匹配的多个货号中的第一个商品
     *
     * @param multiSaleSameGoMap
     * @param gtSaleGroupMap
     * @param cleanArtNo
     * @return
     */
    private List<GtProdSkuVO> getGtFirstSku(Map<String, List<String>> multiSaleSameGoMap, Map<String, List<GtProdSkuVO>> gtSaleGroupMap, String cleanArtNo) {
        // GT匹配的货号
        List<String> gtMatchArtNoList = multiSaleSameGoMap.get(cleanArtNo);
        // 逻辑：如果TY有多个货号，则都要加到系统中来，因为要扫描条码；如果GT有多个，则只取第一个，匹配相关的属性
        List<GtProdSkuVO> gtMatchSkuList = gtSaleGroupMap.get(gtMatchArtNoList.get(0));
        return gtMatchSkuList;
    }


    /**
     * 提取货号中的核心数字部分
     * 例如: z1104 -> 1104, z1087高 -> 1087, z1003-1 -> 1003, 922- -> 922, -8072 -> 8072
     *
     * @param articleNumber 货号
     * @return 核心数字部分
     */
    private String extractCoreArticleNumber(String articleNumber) {
        if (articleNumber == null || articleNumber.isEmpty()) {
            return "";
        }
        // 使用正则表达式匹配第一组连续的数字
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\d+");
        java.util.regex.Matcher matcher = pattern.matcher(articleNumber);
        // 返回第一组匹配到的数字
        if (matcher.find()) {
            return matcher.group();
        }
        // 如果没有找到数字，返回空字符串
        throw new ServiceException("货号格式错误", HttpStatus.ERROR);
    }


}
