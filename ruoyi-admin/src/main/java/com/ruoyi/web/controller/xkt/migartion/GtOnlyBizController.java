package com.ruoyi.web.controller.xkt.migartion;

import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.es.EsClientWrapper;
import com.ruoyi.framework.notice.fs.FsNotice;
import com.ruoyi.web.controller.xkt.migartion.vo.gt.GtCateVO;
import com.ruoyi.web.controller.xkt.migartion.vo.gt.GtProdSkuVO;
import com.ruoyi.web.controller.xkt.migartion.vo.gtOnly.GtOnlyInitVO;
import com.ruoyi.xkt.domain.*;
import com.ruoyi.xkt.dto.es.ESProductDTO;
import com.ruoyi.xkt.dto.storeProdColorPrice.StoreProdMinPriceDTO;
import com.ruoyi.xkt.enums.EProductStatus;
import com.ruoyi.xkt.enums.ListingType;
import com.ruoyi.xkt.mapper.*;
import com.ruoyi.xkt.service.IPictureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.ruoyi.common.constant.Constants.WEIGHT_DEFAULT_ZERO;

/**
 * 只有GT处理 相关
 *
 * @author ruoyi
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/v1/gt-only")
public class GtOnlyBizController extends BaseController {

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


    /**
     * step1
     */
    // 新增颜色、商品基础数据
    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @PostMapping("/init-prod")
    @Transactional
    public R<Integer> initProd(@Validated @RequestBody GtOnlyInitVO initVO) {
        Optional.ofNullable(this.storeMapper.selectOne(new LambdaQueryWrapper<Store>()
                        .eq(Store::getId, initVO.getStoreId()).eq(Store::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("档口不存在!", HttpStatus.ERROR));
        // 获取GT所有的货品
        List<GtProdSkuVO> gtOnSaleCacheList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_GT_SALE_BASIC_KEY + initVO.getUserId()), new ArrayList<>());
        if (CollectionUtils.isEmpty(gtOnSaleCacheList)) {
            throw new ServiceException("GT商品列表为空", HttpStatus.ERROR);
        }
        // 步骤1: 新建颜色
        this.initStoreColorList(initVO, gtOnSaleCacheList);

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
        // 步骤2: 新增商品
        List<StoreProduct> storeProdList = new ArrayList<>();
        // 所有商品的类目属性map  key gt的product_id value StoreProductCategoryAttribute
        Map<Integer, StoreProductCategoryAttribute> prodAttrMap = new HashMap<>();
        // 当天
        final Date voucherDate = java.sql.Date.valueOf(LocalDate.now());
        Map<String, GtProdSkuVO> gtProdMap = gtOnSaleCacheList.stream().collect(Collectors.toMap(GtProdSkuVO::getArticle_number, x -> x, (s1, s2) -> s2));
        gtProdMap.forEach((artNum, gtProdSkuVO) -> {
            // 初始化档口商品
            StoreProduct storeProd = new StoreProduct().setStoreId(initVO.getStoreId()).setProdCateId(cateRelationMap.get(gtProdSkuVO.getCategory_nid())).setPrivateItem(0)
                    .setProdArtNum(gtProdSkuVO.getArticle_number()).setProdTitle(gtProdSkuVO.getCharacters()).setListingWay(ListingType.RIGHT_NOW.getValue())
                    .setVoucherDate(voucherDate).setProdStatus(EProductStatus.ON_SALE.getValue()).setRecommendWeight(0L).setSaleWeight(0L).setPopularityWeight(0L);
            // 提前设置档口商品的类目属性
            this.preMatchAttr(gtProdSkuVO.getProduct_id(), initVO.getUserId(), prodAttrMap);
            storeProdList.add(storeProd);
        });
        this.storeProdMapper.insert(storeProdList);

        // 商品所有颜色 尺码 颜色库存初始化
        List<StoreProductService> prodSvcList = new ArrayList<>();
        List<StoreProductCategoryAttribute> prodAttrList = new ArrayList<>();
        storeProdList.forEach(storeProd -> {
            GtProdSkuVO gtProdSkuVO = gtProdMap.get(storeProd.getProdArtNum());
            // 初始化商品服务承诺
            prodSvcList.add(new StoreProductService().setStoreProdId(storeProd.getId()).setCustomRefund("0")
                    .setThirtyDayRefund("0").setOneBatchSale("1").setRefundWithinThreeDay("0"));
            // 初始化商品的类目属性
            StoreProductCategoryAttribute cateAttr = Optional.ofNullable(prodAttrMap.get(gtProdSkuVO.getProduct_id())).orElse(new StoreProductCategoryAttribute());
            cateAttr.setStoreId(storeProd.getStoreId()).setStoreProdId(storeProd.getId());
            prodAttrList.add(cateAttr);
        });
        // 插入档口服务承诺、商品基本属性
        this.prodSvcMapper.insert(prodSvcList);
        this.prodCateAttrMapper.insert(prodAttrList);
        return R.ok();
    }


    /**
     * step2
     * 商品颜色及商品颜色尺码
     */
    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @PostMapping("/init-color")
    @Transactional
    public R<Integer> initColor(@Validated @RequestBody GtOnlyInitVO initVO) {
        // 从数据库查询最新数据
        List<StoreProduct> storeProdList = this.storeProdMapper.selectList(new LambdaQueryWrapper<StoreProduct>()
                .eq(StoreProduct::getStoreId, initVO.getStoreId()).eq(StoreProduct::getDelFlag, Constants.UNDELETED));
        List<StoreColor> storeColorList = this.storeColorMapper.selectList(new LambdaQueryWrapper<StoreColor>()
                .eq(StoreColor::getStoreId, initVO.getStoreId()).eq(StoreColor::getDelFlag, Constants.UNDELETED));
        Map<String, StoreColor> storeColorMap = storeColorList.stream().collect(Collectors.toMap(StoreColor::getColorName, x -> x, (v1, v2) -> v2));

        // 获取GT所有的货品
        List<GtProdSkuVO> gtOnSaleCacheList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_GT_SALE_BASIC_KEY + initVO.getUserId()), new ArrayList<>());
        Map<String, List<GtProdSkuVO>> gtProdSkuMap = gtOnSaleCacheList.stream().collect(Collectors.groupingBy(GtProdSkuVO::getArticle_number));
        // 商品所有颜色 尺码 颜色库存初始化
        List<StoreProductColor> prodColorList = new ArrayList<>();
        List<StoreProductColorSize> prodColorSizeList = new ArrayList<>();
        storeProdList.forEach(storeProd -> {
            // 获取GT匹配的商品sku列表
            List<GtProdSkuVO> gtMatchSkuList = gtProdSkuMap.get(storeProd.getProdArtNum());
            // 当前货号在GT的所有尺码，作为标准尺码
            List<Integer> gtStandardSizeList = gtMatchSkuList.stream().map(sku -> (int) Math.floor(Double.parseDouble(sku.getSize()))).collect(Collectors.toList());
            Map<String, String> prodColorMap = gtMatchSkuList.stream().collect(Collectors.toMap(GtProdSkuVO::getColor, GtProdSkuVO::getColor, (s1, s2) -> s2));
            // GT颜色下尺码价格map
            Map<String, Map<Integer, BigDecimal>> colorSizePriceMap = gtMatchSkuList.stream().collect(Collectors
                    .groupingBy(GtProdSkuVO::getColor, Collectors.toMap(x -> (int) Math.floor(Double.parseDouble(x.getSize())), GtProdSkuVO::getPrice)));
            AtomicInteger orderNum = new AtomicInteger();
            prodColorMap.forEach((color, gtColor) -> {
                StoreColor storeColor = Optional.ofNullable(storeColorMap.get(gtColor))
                        .orElseThrow(() -> new ServiceException("没有GT商品颜色!" + storeProd.getProdArtNum(), HttpStatus.ERROR));
                // 该商品的颜色
                prodColorList.add(new StoreProductColor().setStoreId(storeProd.getStoreId()).setStoreProdId(storeProd.getId()).setOrderNum(orderNum.addAndGet(1))
                        .setColorName(storeColor.getColorName()).setStoreColorId(storeColor.getId()).setProdStatus(EProductStatus.ON_SALE.getValue()));
                Map<Integer, BigDecimal> sizePriceMap = Optional.ofNullable(colorSizePriceMap.get(gtColor))
                        .orElseThrow(() -> new ServiceException("没有GT商品颜色尺码价格!" + storeProd.getProdArtNum(), HttpStatus.ERROR));
                // 该颜色最低价格
                BigDecimal minPrice = sizePriceMap.values().stream().min(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
                // 该颜色所有的尺码
                for (int j = 0; j < Constants.SIZE_LIST.size(); j++) {
                    final boolean isStandard = gtStandardSizeList.contains(Constants.SIZE_LIST.get(j));
                    prodColorSizeList.add(new StoreProductColorSize().setSize(Constants.SIZE_LIST.get(j)).setStoreColorId(storeColor.getId())
                            .setStoreProdId(storeProd.getId()).setStandard(isStandard ? 1 : 0).setNextSn(0)
                            // 销售价格以FHB价格为准
                            .setPrice(sizePriceMap.getOrDefault(Constants.SIZE_LIST.get(j), minPrice).add(ObjectUtils.defaultIfNull(initVO.getAddOverPrice(), BigDecimal.ZERO))));
                }
            });
        });
        // 插入商品颜色及颜色对应的尺码，档口服务承诺
        this.prodColorMapper.insert(prodColorList);
        prodColorSizeList.sort(Comparator.comparing(StoreProductColorSize::getStoreProdId).thenComparing(StoreProductColorSize::getSize));
        this.prodColorSizeMapper.insert(prodColorSizeList);
        // 还要更新步橘系统的条码前缀
        prodColorSizeList.forEach(x -> x.setSnPrefix(initVO.getStoreId() + String.format("%08d", x.getId())));
        this.prodColorSizeMapper.updateById(prodColorSizeList);
        return R.ok();
    }

    /**
     * step3
     */
    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @PostMapping("/init-stock")
    @Transactional
    public R<Integer> initStock(@Validated @RequestBody GtOnlyInitVO initVO) {
        List<StoreProduct> storeProdList = this.storeProdMapper.selectList(new LambdaQueryWrapper<StoreProduct>()
                .eq(StoreProduct::getStoreId, initVO.getStoreId()).eq(StoreProduct::getDelFlag, Constants.UNDELETED));
        List<StoreProductColor> prodColorList = this.prodColorMapper.selectList(new LambdaQueryWrapper<StoreProductColor>()
                .eq(StoreProductColor::getStoreId, initVO.getStoreId()).eq(StoreProductColor::getDelFlag, Constants.UNDELETED));

        // 只需要初始化档口商品颜色库存即可，没有客户优惠
        Map<Long, StoreProduct> storeProdMap = storeProdList.stream().collect(Collectors.toMap(StoreProduct::getId, x -> x));
        List<StoreProductStock> prodStockList = prodColorList.stream().map(x -> {
            StoreProduct storeProd = Optional.ofNullable(storeProdMap.get(x.getStoreProdId()))
                    .orElseThrow(() -> new ServiceException("没有GT商品!" + x.getStoreProdId(), HttpStatus.ERROR));
            return new StoreProductStock().setStoreId(x.getStoreId()).setStoreProdId(x.getStoreProdId()).setProdArtNum(storeProd.getProdArtNum())
                    .setStoreProdColorId(x.getId()).setColorName(x.getColorName());
        }).collect(Collectors.toList());
        // 档口客户库存
        this.prodStockMapper.insert(prodStockList);
        return R.ok();
    }


    /**
     * step4
     */
    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @PostMapping("/sync-es/{storeId}")
    public R<Integer> syncToEs(@PathVariable("storeId") Long storeId) {
        // 将公共的商品同步到ES
        List<StoreProduct> storeProdList = this.storeProdMapper.selectList(new LambdaQueryWrapper<StoreProduct>()
                .eq(StoreProduct::getDelFlag, Constants.UNDELETED).eq(StoreProduct::getStoreId, storeId));
        if (CollectionUtils.isEmpty(storeProdList)) {
            return R.fail();
        }
        final List<String> storeProdIdList = storeProdList.stream().map(StoreProduct::getId).map(String::valueOf).collect(Collectors.toList());
        // 所有的分类
        List<SysProductCategory> prodCateList = this.prodCateMapper.selectList(new LambdaQueryWrapper<SysProductCategory>()
                .eq(SysProductCategory::getDelFlag, Constants.UNDELETED));
        Map<Long, SysProductCategory> prodCateMap = prodCateList.stream().collect(Collectors.toMap(SysProductCategory::getId, x -> x));
        // 获取当前商品最低价格
        Map<Long, BigDecimal> prodMinPriceMap = this.prodColorSizeMapper.selectStoreProdMinPriceList(storeProdIdList).stream().collect(Collectors
                .toMap(StoreProdMinPriceDTO::getStoreProdId, StoreProdMinPriceDTO::getPrice));
        // 档口商品的属性map
        Map<Long, StoreProductCategoryAttribute> cateAttrMap = this.prodCateAttrMapper.selectList(new LambdaQueryWrapper<StoreProductCategoryAttribute>()
                        .eq(StoreProductCategoryAttribute::getDelFlag, Constants.UNDELETED).in(StoreProductCategoryAttribute::getStoreProdId, storeProdIdList))
                .stream().collect(Collectors.toMap(StoreProductCategoryAttribute::getStoreProdId, x -> x));
        // 档口商品对应的档口
        Map<Long, Store> storeMap = this.storeMapper.selectList(new LambdaQueryWrapper<Store>().eq(Store::getDelFlag, Constants.UNDELETED)
                        .in(Store::getId, storeProdList.stream().map(StoreProduct::getStoreId).collect(Collectors.toList())))
                .stream().collect(Collectors.toMap(Store::getId, x -> x));
        List<ESProductDTO> esProductDTOList = new ArrayList<>();
        for (StoreProduct product : storeProdList) {
            final SysProductCategory cate = prodCateMap.get(product.getProdCateId());
            final SysProductCategory parCate = ObjectUtils.isEmpty(cate) ? null : prodCateMap.get(cate.getParentId());
            final Store store = storeMap.get(product.getStoreId());
            final BigDecimal prodMinPrice = prodMinPriceMap.get(product.getId());
            final StoreProductCategoryAttribute cateAttr = cateAttrMap.get(product.getId());
            ESProductDTO esProductDTO = new ESProductDTO().setStoreProdId(product.getId().toString()).setProdArtNum(product.getProdArtNum())
                    .setHasVideo(Boolean.FALSE).setProdCateId(product.getProdCateId().toString()).setCreateTime(DateUtils.getTime())
                    .setProdCateName(ObjectUtils.isNotEmpty(cate) ? cate.getName() : "")
                    .setSaleWeight(WEIGHT_DEFAULT_ZERO.toString()).setRecommendWeight(WEIGHT_DEFAULT_ZERO.toString())
                    .setPopularityWeight(WEIGHT_DEFAULT_ZERO.toString())
                    .setMainPicUrl("").setMainPicName("").setMainPicSize(BigDecimal.ZERO)
                    .setParCateId(ObjectUtils.isNotEmpty(parCate) ? parCate.getId().toString() : "")
                    .setParCateName(ObjectUtils.isNotEmpty(parCate) ? parCate.getName() : "")
                    .setProdPrice(ObjectUtils.isNotEmpty(prodMinPrice) ? prodMinPrice.toString() : "")
                    .setSeason(ObjectUtils.isNotEmpty(cateAttr) ? cateAttr.getSuitableSeason() : "")
                    .setProdStatus(product.getProdStatus().toString())
                    .setStoreId(product.getStoreId().toString())
                    .setStoreName(ObjectUtils.isNotEmpty(store) ? store.getStoreName() : "")
                    .setStyle(ObjectUtils.isNotEmpty(cateAttr) ? cateAttr.getStyle() : "")
                    .setProdTitle(product.getProdTitle());
            if (ObjectUtils.isNotEmpty(cateAttr) && StringUtils.isNotBlank(cateAttr.getStyle())) {
                esProductDTO.setTags(Collections.singletonList(cateAttr.getStyle()));
            }
            esProductDTOList.add(esProductDTO);
        }
        // 构建批量操作请求
        List<BulkOperation> bulkOperations = new ArrayList<>();
        for (ESProductDTO esProductDTO : esProductDTOList) {
            BulkOperation bulkOperation = new BulkOperation.Builder()
                    .index(i -> i.id(esProductDTO.getStoreProdId()).index(Constants.ES_IDX_PRODUCT_INFO).document(esProductDTO))
                    .build();
            bulkOperations.add(bulkOperation);
        }
        // 执行批量插入
        try {
            BulkResponse response = esClientWrapper.getEsClient().bulk(b -> b.index(Constants.ES_IDX_PRODUCT_INFO).operations(bulkOperations));
            log.info("批量新增到 ES 成功的 id列表: {}", response.items().stream().map(BulkResponseItem::id).collect(Collectors.toList()));
            // 有哪些没执行成功的，需要发飞书通知
            List<String> successIdList = response.items().stream().map(BulkResponseItem::id).collect(Collectors.toList());
            List<String> unExeIdList = storeProdIdList.stream().map(String::valueOf).filter(x -> !successIdList.contains(x)).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(unExeIdList)) {
                fsNotice.sendMsg2DefaultChat(storeId + "，批量新增商品到 ES 失败", "以下storeProdId未执行成功: " + unExeIdList);
            } else {
                fsNotice.sendMsg2DefaultChat(storeId + "，批量新增商品到 ES 成功", "共处理 " + response.items().size() + " 条记录");
            }
        } catch (Exception e) {
            log.error("批量新增到 ES 失败", e);
            fsNotice.sendMsg2DefaultChat(storeId + "，批量新增商品到 ES 失败", e.getMessage());
        }
        return R.ok();
    }

    /**
     * 初始化档口颜色
     *
     * @param gtOnSaleCacheList GT所有商品列表
     */
    private void initStoreColorList(GtOnlyInitVO initVO, List<GtProdSkuVO> gtOnSaleCacheList) {
        List<String> gtColorList = gtOnSaleCacheList.stream().map(GtProdSkuVO::getColor).distinct().sorted(Comparator.naturalOrder()).collect(Collectors.toList());
        List<StoreColor> storeColorList = new ArrayList<>();
        for (int i = 0; i < gtColorList.size(); i++) {
            storeColorList.add(new StoreColor().setStoreId(initVO.getStoreId()).setColorName(gtColorList.get(i)).setOrderNum(i + 1));
        }
        this.storeColorMapper.insert(storeColorList);
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


}
