package com.ruoyi.web.controller.xkt.migartion;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.web.controller.xkt.migartion.vo.GtAndFHBCompareDownloadVO;
import com.ruoyi.web.controller.xkt.migartion.vo.GtAndFHBInitVO;
import com.ruoyi.web.controller.xkt.migartion.vo.fhb.FhbCusDiscountVO;
import com.ruoyi.web.controller.xkt.migartion.vo.fhb.FhbCusVO;
import com.ruoyi.web.controller.xkt.migartion.vo.fhb.FhbProdVO;
import com.ruoyi.web.controller.xkt.migartion.vo.gt.GtCateVO;
import com.ruoyi.web.controller.xkt.migartion.vo.gt.GtProdSkuVO;
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
@RequestMapping("/rest/v1/gt-fhb")
public class GtAndFhbBizController extends BaseController {

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
    @PutMapping("/compare/{userId}/{supplierId}")
    public void compare(HttpServletResponse response, @PathVariable("userId") Integer userId, @PathVariable("supplierId") Integer supplierId) throws UnsupportedEncodingException {
        Map<String, List<String>> multiSaleSameGoMap = new HashMap<>();
        Map<String, List<String>> multiOffSaleSameGoMap = new HashMap<>();
        Map<String, List<String>> multiSameFhbMap = new HashMap<>();
        List<GtProdSkuVO> doubleRunSaleBasicList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_GT_SALE_BASIC_KEY + userId), new ArrayList<>());
        Map<String, String> articleNoColorMap = doubleRunSaleBasicList.stream().collect(Collectors.groupingBy(GtProdSkuVO::getArticle_number,
                Collectors.collectingAndThen(Collectors.mapping(GtProdSkuVO::getColor, Collectors.toList()),
                        list -> "(" + list.stream().distinct().collect(Collectors.joining(",")) + ")")));
        List<String> doubleRunSaleArtNoList = doubleRunSaleBasicList.stream().map(GtProdSkuVO::getArticle_number)
                .distinct().collect(Collectors.toList());
        // 查看double_run 在售的商品 这边有多少相似的货号
        doubleRunSaleArtNoList.forEach(article_number -> {
            // 只保留核心连续的数字，去除其他所有符号
            String cleanArtNo = this.extractCoreArticleNumber(article_number);
            List<String> existList = multiSaleSameGoMap.containsKey(cleanArtNo) ? multiSaleSameGoMap.get(cleanArtNo) : new ArrayList<>();
            existList.add(article_number + articleNoColorMap.get(article_number));
            multiSaleSameGoMap.put(cleanArtNo, existList);
        });

        // 查看double_run 下架的商品有多少相似的货号
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

        // 查看Fhb 这边有多少相似的货号
        List<FhbProdVO.SMIVO> shipMasterProdList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_SUPPLIER_PROD_KEY + supplierId), new ArrayList<>());
        Map<String, String> shipMasterArticleNoColorMap = shipMasterProdList.stream().collect(Collectors.groupingBy(FhbProdVO.SMIVO::getArtNo,
                Collectors.collectingAndThen(Collectors.mapping(FhbProdVO.SMIVO::getColor, Collectors.toList()),
                        list -> "(" + list.stream().distinct().collect(Collectors.joining(",")) + ")")));
        List<String> shipArtNoList = shipMasterProdList.stream().map(FhbProdVO.SMIVO::getArtNo)
                .distinct().collect(Collectors.toList());
        shipArtNoList.forEach(artNo -> {
            // 只保留核心连续的数字，去除其他所有符号
            String cleanArtNo = this.extractCoreArticleNumber(artNo);
            List<String> existList = multiSameFhbMap.containsKey(cleanArtNo) ? multiSameFhbMap.get(cleanArtNo) : new ArrayList<>();
            existList.add(artNo + shipMasterArticleNoColorMap.get(artNo));
            multiSameFhbMap.put(cleanArtNo, existList);
        });

        // 清洗数据之后，GO平台和FHB平台 货号一致的，按照这种来展示: GT => [Z1110(黑色,黑色绒里,棕色,棕色绒里)] <= 清洗后的货号 => [Z1110(黑色,黑色绒里,棕色,棕色绒里)] <= FHB

        System.err.println("============ 两边系统“一致”的货号 ============");
        // 清洗后，相同货号映射
        List<String> matchArtNoList = new ArrayList<>();
        Set<String> commonArtNos = new HashSet<>(multiSaleSameGoMap.keySet());
        commonArtNos.retainAll(multiSameFhbMap.keySet());
        commonArtNos.forEach(artNo -> {
            final String sameArtNo = "GT => " + multiSaleSameGoMap.get(artNo) + " <= " + artNo + " => " + multiSameFhbMap.get(artNo) + " <= FHB";
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

        matchArtNoList.add("============ FHB独有的货号 ============");
        matchArtNoList.add("============ FHB独有的货号 ============");

        System.err.println("============ ShipMaster 去掉公共的、下架的 独有的key ============");
        // 获取ShipMaster独有的key  去掉公共的、去掉下架的商品
        Set<String> onlyInFMap = new HashSet<>(multiSameFhbMap.keySet());
        onlyInFMap.removeAll(commonArtNos);
        onlyInFMap.removeAll(multiOffSaleSameGoMap.keySet());
        if (CollectionUtils.isNotEmpty(onlyInFMap)) {
            onlyInFMap.forEach(x -> {
                matchArtNoList.addAll(multiSameFhbMap.get(x));
                System.out.println(multiSameFhbMap.get(x));
            });
        }

        List<GtAndFHBCompareDownloadVO> downloadList = new ArrayList<>();
        for (int i = 0; i < matchArtNoList.size(); i++) {
            downloadList.add(new GtAndFHBCompareDownloadVO().setOrderNum(i + 1).setCode(matchArtNoList.get(i)));
        }
        ExcelUtil<GtAndFHBCompareDownloadVO> util = new ExcelUtil<>(GtAndFHBCompareDownloadVO.class);
        String encodedFileName = URLEncoder.encode("GT与FHB差异" + DateUtils.getDate(), "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename=" + encodedFileName + ".xlsx");
        util.exportExcel(response, downloadList, "差异");
    }


    /**
     * 步骤1: 准备数据，新建颜色;
     * 步骤2: GT 和 FHB 货号对应关系，然后直接copy 对应的属性关系
     * // a. 商品与颜色对应关系
     * // b. 商品颜色尺码 + 价格 对应关系
     * // c. 库存初始化
     * // d. 服务承诺初始化
     * // e. 类目属性初始化
     * 步骤3: 准备数据，新建客户
     * 步骤4: 客户与货号的优惠关系
     * 步骤5: 批量创建数据到ES服务器
     */
    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @PutMapping("/init")

    @Transactional

    public void initToDB(@Validated @RequestBody GtAndFHBInitVO initVO) {
        final Store store = Optional.ofNullable(this.storeMapper.selectOne(new LambdaQueryWrapper<Store>()
                        .eq(Store::getId, initVO.getStoreId()).eq(Store::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("档口不存在!", HttpStatus.ERROR));
        // 步骤1: 准备数据，新建颜色
        Map<String, StoreColor> storeColorMap = this.initStoreColorList(initVO.getStoreId(), initVO.getSupplierId());
        // 步骤2: GT 和 FHB 货号对应关系，然后直接copy 对应的属性关系
        // a. 商品与颜色对应关系
        // b. 商品颜色尺码 + 价格 对应关系
        // c. 库存初始化
        // d. 服务承诺初始化
        // e. 类目属性初始化
        this.init(initVO, storeColorMap);


        // 步骤x: 查看有哪些货号价格是有多个的，单独设置差异的价格


        // 步骤5: 批量创建数据到ES服务器


    }


    /**
     * 初始化档口商品列表
     *
     * @param storeColorMap 档口颜色map
     */
    private R<Integer> init(GtAndFHBInitVO initVO, Map<String, StoreColor> storeColorMap) {
        // 步骤2: GT 和 FHB 货号对应关系，然后直接copy 对应的属性关系
        // a. 商品与颜色对应关系
        // b. 商品颜色尺码 + 价格 对应关系
        // c. 库存初始化
        // d. 服务承诺初始化
        // e. 类目属性初始化
        Map<String, List<String>> multiSaleSameGoMap = new HashMap<>();
        Map<String, List<String>> multiOffSaleSameGoMap = new HashMap<>();
        Map<String, List<String>> multiSameFhbMap = new HashMap<>();
        List<GtProdSkuVO> gtSaleBasicList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_GT_SALE_BASIC_KEY + initVO.getUserId()), new ArrayList<>());
        // 查看gt 在售的商品 这边有多少相似的货号
        gtSaleBasicList.stream().map(GtProdSkuVO::getArticle_number).distinct().forEach(article_number -> {
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

        // 查看Fhb 这边有多少相似的货号
        List<FhbProdVO.SMIVO> fhbProdList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_SUPPLIER_PROD_KEY + initVO.getSupplierId()), new ArrayList<>());
        // Fhb按照颜色分类
        Map<String, List<FhbProdVO.SMIVO>> fhbProdGroupMap = fhbProdList.stream().collect(Collectors.groupingBy(FhbProdVO.SMIVO::getArtNo));
        fhbProdList.stream().map(FhbProdVO.SMIVO::getArtNo).distinct().forEach(artNo -> {
            // 只保留核心连续的数字，去除其他所有符号
            String cleanArtNo = this.extractCoreArticleNumber(artNo);
            List<String> existList = multiSameFhbMap.containsKey(cleanArtNo) ? multiSameFhbMap.get(cleanArtNo) : new ArrayList<>();
            existList.add(artNo);
            multiSameFhbMap.put(cleanArtNo, existList);
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
        commonArtNos.retainAll(multiSameFhbMap.keySet());
        // 待导入的商品
        List<StoreProduct> storeProdList = new ArrayList<>();
        // 当天
        final Date voucherDate = java.sql.Date.valueOf(LocalDate.now());
        // 所有商品的类目属性map  key gt的product_id value StoreProductCategoryAttribute
        Map<Integer, StoreProductCategoryAttribute> prodAttrMap = new HashMap<>();
        commonArtNos.stream()
                // 过滤掉需要特殊处理的货号
                .filter(artNo -> !initVO.getExcludeArtNoList().contains(artNo))
                .forEach(cleanArtNo -> {
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
            // FHB匹配的货号
            List<String> fhbMatchArtNoList = multiSameFhbMap.get(storeProd.getProdArtNum());
            // 获取GT匹配的商品sku列表
            List<GtProdSkuVO> gtMatchSkuList = this.getGtFirstSku(multiSaleSameGoMap, gtSaleGroupMap, storeProd.getProdArtNum());
            // 当前货号在GT的所有尺码，作为标准尺码
            List<Integer> gtStandardSizeList = gtMatchSkuList.stream().map(sku -> (int) Math.floor(Double.parseDouble(sku.getSize())))
                    .collect(Collectors.toList());
            // 先获取最低价格，然后给所有颜色和尺码添加初始值，之后再来单独改，这是最方便的方式了
            final BigDecimal minPrice = gtMatchSkuList.stream().map(GtProdSkuVO::getPrice).min(Comparator.comparing(x -> x))
                    .orElseThrow(() -> new ServiceException("没有GT商品价格!", HttpStatus.ERROR));
            fhbMatchArtNoList.forEach(fhbArtNo -> {
                List<FhbProdVO.SMIVO> fhbMatchSkuList = fhbProdGroupMap.get(fhbArtNo);
                for (int i = 0; i < fhbMatchSkuList.size(); i++) {
                    StoreColor storeColor = Optional.ofNullable(storeColorMap.get(fhbMatchSkuList.get(i).getColor()))
                            .orElseThrow(() -> new ServiceException("没有FHB商品颜色!" + fhbArtNo, HttpStatus.ERROR));
                    // 该商品的颜色
                    prodColorList.add(new StoreProductColor().setStoreId(storeProd.getStoreId()).setStoreProdId(storeProd.getId()).setOrderNum(i + 1)
                            .setColorName(storeColor.getColorName()).setStoreColorId(storeColor.getId()).setProdStatus(EProductStatus.ON_SALE.getValue()));
                    // 该颜色所有的尺码
                    for (int j = 0; j < Constants.SIZE_LIST.size(); j++) {
                        // FHB系统条码前缀
                        final String otherSnPrefix = fhbMatchSkuList.get(i).getSupplierId()
                                + String.format("%05d", fhbMatchSkuList.get(i).getSupplierSkuId()) + Constants.SIZE_LIST.get(j);
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
        this.initStoreCusProdDiscList(initVO, storeProdList, storeCusList, prodColorList, multiSameFhbMap, fhbProdGroupMap);

        return R.ok();
    }


    /**
     * 新建档口客户对应产品的优惠
     *
     * @param initVO          入参
     * @param storeProdList   档口商品列表
     * @param storeCusList    档口客户列表
     * @param prodColorList   商品颜色列表
     * @param multiSameFhbMap 步橘货号和FHB货号对应关系
     * @param fhbProdGroupMap FHB货号和颜色对应关系
     */
    private void initStoreCusProdDiscList(GtAndFHBInitVO initVO, List<StoreProduct> storeProdList, List<StoreCustomer> storeCusList,
                                          List<StoreProductColor> prodColorList, Map<String, List<String>> multiSameFhbMap,
                                          Map<String, List<FhbProdVO.SMIVO>> fhbProdGroupMap) {
        // 从redis中获取客户优惠数据
        List<FhbCusDiscountVO.SMCDRecordVO> fhbCusDiscCacheList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_SUPPLIER_CUS_DISCOUNT_KEY + initVO.getSupplierId()), new ArrayList<>());
        if (CollectionUtils.isEmpty(fhbCusDiscCacheList)) {
            throw new ServiceException("fhb供应商客户优惠列表为空!" + initVO.getSupplierId(), HttpStatus.ERROR);
        }
        // FHB 货号颜色优惠对应关系
        Map<String, Map<String, List<FhbCusDiscountVO.SMCDRecordVO>>> fhbCusDiscGroupMap = fhbCusDiscCacheList.stream().collect(Collectors
                .groupingBy(FhbCusDiscountVO.SMCDRecordVO::getArtNo, Collectors.groupingBy(FhbCusDiscountVO.SMCDRecordVO::getColor)));
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
            // 根据步橘货号 找到FHB对应的货号，可能是列表
            List<String> fhbAtrNoList = Optional.ofNullable(multiSameFhbMap.get(storeProd.getProdArtNum())).orElseThrow(() -> new ServiceException("没有FHB货号!" + storeProd.getProdArtNum(), HttpStatus.ERROR));
            fhbAtrNoList.forEach(fhbAtrNo -> {
                // FHB货号下有哪些颜色存在客户优惠
                Map<String, List<FhbCusDiscountVO.SMCDRecordVO>> fhbColorCusDiscMap = fhbCusDiscGroupMap.get(fhbAtrNo);
                if (MapUtils.isEmpty(fhbColorCusDiscMap)) {
                    return;
                }
                // 依次遍历存在优惠的颜色，设置步橘系统客户优惠关系
                fhbColorCusDiscMap.forEach((fhbColor, fhbCusDiscList) -> fhbCusDiscList.forEach(fhbCusDisc -> {
                    StoreProductColor buJuProdColor = Optional.ofNullable(buJuProdColorMap.get(fhbColor)).orElseThrow(() -> new ServiceException("没有步橘系统对应的颜色!" + fhbColor, HttpStatus.ERROR));
                    StoreCustomer storeCus = Optional.ofNullable(buJuStoreCusMap.get(fhbCusDisc.getCustomerName())).orElseThrow(() -> new ServiceException("没有步橘系统对应的客户!" + fhbCusDisc.getCustomerName(), HttpStatus.ERROR));
                    // 将FHB客户优惠 转为步橘系统优惠
                    prodCusDiscList.add(new StoreCustomerProductDiscount().setStoreId(storeProd.getStoreId()).setStoreProdId(storeProd.getId()).setStoreCusId(storeCus.getId())
                            .setStoreCusName(storeCus.getCusName()).setStoreProdColorId(buJuProdColor.getId()).setDiscount(fhbCusDisc.getDiscount()));
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
    private List<StoreCustomer> initStoreCusList(GtAndFHBInitVO initVO) {
        List<FhbCusVO.SMCVO> cacheList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_SUPPLIER_CUS_KEY + initVO.getSupplierId()), new ArrayList<>());
        if (CollectionUtils.isEmpty(cacheList)) {
            throw new ServiceException("供应商客户列表为空!", HttpStatus.ERROR);
        }
        List<StoreCustomer> storeCusList = cacheList.stream()
                // 排除掉现金客户 因为档口入驻时，会自动创建现金客户
                .filter(x -> !Objects.equals(x.getName(), Constants.STORE_CUS_CASH))
                .map(x -> new StoreCustomer()
                        .setStoreId(initVO.getStoreId()).setCusName(x.getName()))
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
        // 逻辑：如果FHB有多个货号，则都要加到系统中来，因为要扫描条码；如果GT有多个，则只取第一个，匹配相关的属性
        List<GtProdSkuVO> gtMatchSkuList = gtSaleGroupMap.get(gtMatchArtNoList.get(0));
        return gtMatchSkuList;
    }


    /**
     * 初始化档口颜色
     *
     * @param storeId    档口ID
     * @param supplierId 供应商ID
     */
    private Map<String, StoreColor> initStoreColorList(Long storeId, Integer supplierId) {
        List<FhbProdVO.SMIVO> cacheList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_SUPPLIER_PROD_KEY + supplierId), new ArrayList<>());
        if (CollectionUtils.isEmpty(cacheList)) {
            throw new ServiceException("FHB商品列表为空", HttpStatus.ERROR);
        }
        List<String> fhbColorList = cacheList.stream().map(FhbProdVO.SMIVO::getColor).distinct().collect(Collectors.toList());
        List<StoreColor> storeColorList = new ArrayList<>();
        for (int i = 0; i < fhbColorList.size(); i++) {
            storeColorList.add(new StoreColor().setStoreId(storeId).setColorName(fhbColorList.get(i)).setOrderNum(i + 1));
        }
        this.storeColorMapper.insert(storeColorList);
        return storeColorList.stream().collect(Collectors.toMap(StoreColor::getColorName, x -> x));
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
