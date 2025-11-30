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
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.es.EsClientWrapper;
import com.ruoyi.framework.notice.fs.FsNotice;
import com.ruoyi.system.mapper.SysDictDataMapper;
import com.ruoyi.web.controller.xkt.migartion.vo.fhb.FhbCusDiscountVO;
import com.ruoyi.web.controller.xkt.migartion.vo.fhb.FhbCusVO;
import com.ruoyi.web.controller.xkt.migartion.vo.fhb.FhbProdStockVO;
import com.ruoyi.web.controller.xkt.migartion.vo.fhb.FhbProdVO;
import com.ruoyi.web.controller.xkt.migartion.vo.gt.GtCateVO;
import com.ruoyi.web.controller.xkt.migartion.vo.gt.GtProdSkuVO;
import com.ruoyi.web.controller.xkt.migartion.vo.gtAndFhb.GtAndFHBCompareDownloadVO;
import com.ruoyi.web.controller.xkt.migartion.vo.gtAndFhb.GtAndFHBInitVO;
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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.ruoyi.common.constant.Constants.TOPMOST_PRODUCT_CATEGORY_ID;
import static com.ruoyi.common.constant.Constants.WEIGHT_DEFAULT_ZERO;

/**
 * Compare 相关
 *
 * @author ruoyi
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/v1/gt-fhb/t2")
public class GtAndFhbBiz22222222Controller extends BaseController {

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
    final StoreProductFileMapper storeProdFileMapper;
    final EsClientWrapper esClientWrapper;
    final IPictureService pictureService;
    final FsNotice fsNotice;
    final SysDictDataMapper dictDataMapper;
    @Value("${es.indexName}")
    private String ES_INDEX_NAME;

    // 系统枚举 鞋面内里材质
    private static final String DICT_TYPE_SHOE_UPPER_LINING_MATERIAL = "shoe_upper_lining_material";

    /**
     * step1
     */
    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @GetMapping("/compare/{userId}/{supplierId}")
    public void compare(HttpServletResponse response, @PathVariable("userId") Integer userId, @PathVariable("supplierId") Integer supplierId) throws UnsupportedEncodingException {
        // 处理的思路，以GT为主，根据GT的货号 去匹配TY的货号，有些档口写的比较规范，这种就比较好处理
        List<GtProdSkuVO> gtOnSaleList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_GT_SALE_BASIC_KEY + userId), new ArrayList<>());
        // gt所有在售的货号列表
        List<String> gtArtNoList = gtOnSaleList.stream().map(GtProdSkuVO::getArticle_number).map(String::trim).distinct().collect(Collectors.toList());
        Map<String, String> gtOnSaleArtNoColorMap = gtOnSaleList.stream().collect(Collectors.groupingBy(GtProdSkuVO::getArticle_number,
                Collectors.collectingAndThen(Collectors.mapping(GtProdSkuVO::getColor, Collectors.toList()),
                        list -> "(" + list.stream().distinct().collect(Collectors.joining(",")) + ")")));
        // 查看gt 下架的商品有多少相似的货号
        List<GtProdSkuVO> gtOffSaleBasicList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_GT_OFF_SALE_BASIC_KEY + userId), new ArrayList<>());
        List<String> gtOffSaleArtNoList = gtOffSaleBasicList.stream().map(GtProdSkuVO::getArticle_number).distinct().collect(Collectors.toList());
        // 查看Fhb 这边有多少相似的货号
        List<FhbProdVO.SMIVO> fhbProdList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_SUPPLIER_PROD_KEY + supplierId), new ArrayList<>());
        Map<String, String> fhbArticleNoColorMap = fhbProdList.stream().collect(Collectors.groupingBy(FhbProdVO.SMIVO::getArtNo,
                Collectors.collectingAndThen(Collectors.mapping(FhbProdVO.SMIVO::getColor, Collectors.toList()),
                        list -> "(" + list.stream().sorted(Comparator.naturalOrder()).collect(Collectors.joining(",")) + ")")));
        // GT和FHB匹配的货号map
        Map<String, List<String>> gtMatchFhbArtNoMap = new HashMap<>();
        // 以GT为准在，找FHB匹配的货号
        gtArtNoList.forEach(gtOnSaleArtNo -> fhbArticleNoColorMap.forEach((fhbArtNo, fhbArtNoColorStr) -> {
            // 3种情况 1:1 货号 2:货号R 3:货号-R
            if (Objects.equals(fhbArtNo, gtOnSaleArtNo) || Objects.equals(fhbArtNo, gtOnSaleArtNo + "R") || Objects.equals(fhbArtNo, gtOnSaleArtNo + "-R")) {
                List<String> existMatchArtNoList = gtMatchFhbArtNoMap.getOrDefault(gtOnSaleArtNo, new ArrayList<>());
                existMatchArtNoList.add(fhbArtNo);
                gtMatchFhbArtNoMap.put(gtOnSaleArtNo, existMatchArtNoList);
            }
        }));

        // 清洗后，相同货号映射
        List<String> matchArtNoList = new ArrayList<>();
        List<String> multiMatchGtArtNoList = new ArrayList<>();
        // 清洗数据之后，GO平台和FHB平台 货号一致的，按照这种来展示: GT => [Z1110(黑色,黑色绒里,棕色,棕色绒里)] <= 清洗后的货号 => [Z1110(黑色,黑色绒里,棕色,棕色绒里)] <= FHB
        gtMatchFhbArtNoMap.forEach((gtArtNo, fhbArtNoList) -> {
            List<String> fhbArtNoColorList = fhbArtNoList.stream().map(fhbArtNo -> fhbArtNo + fhbArticleNoColorMap.get(fhbArtNo)).collect(Collectors.toList());
            final String sameArtNo = "GT => " + gtArtNo + gtOnSaleArtNoColorMap.get(gtArtNo) + " <= " + gtArtNo + " => " + fhbArtNoColorList + " <= FHB";
            matchArtNoList.add(sameArtNo);
            multiMatchGtArtNoList.add(gtArtNo);
        });

        matchArtNoList.add("============ GT独有的货号 ============");
        matchArtNoList.add("============ GT独有的货号 ============");
        // 只存在于GT的货号列表
        gtOnSaleArtNoColorMap.forEach((gtArtNo, gtArtNoColorStr) -> {
            if (!multiMatchGtArtNoList.contains(gtArtNo)) {
                matchArtNoList.add(gtArtNo + gtArtNoColorStr);
            }
        });
        matchArtNoList.add("============ FHB独有的货号 ============");
        matchArtNoList.add("============ FHB独有的货号 ============");
        List<String> matchFhbArtNoList = new ArrayList<>();
        // 共同存在GT 和 FHB的FHB货号
        gtMatchFhbArtNoMap.forEach((gtArtNo, tyArtNoList) -> matchFhbArtNoList.addAll(tyArtNoList));
        // 已下架的GT货号，如果能在FHB找到匹配的，也要剔除
        gtOffSaleArtNoList.forEach(gtOffSaleArtNo -> fhbArticleNoColorMap.forEach((fhbArtNo, colorNameStr) -> {
            if (Objects.equals(fhbArtNo, gtOffSaleArtNo) || Objects.equals(fhbArtNo, gtOffSaleArtNo + "R") || Objects.equals(fhbArtNo, gtOffSaleArtNo + "-R")) {
                matchFhbArtNoList.add(fhbArtNo);
            }
        }));
        fhbArticleNoColorMap.forEach((fhbArtNo, colorNameStr) -> {
            if (!matchFhbArtNoList.contains(fhbArtNo)) {
                matchArtNoList.add(fhbArtNo + colorNameStr);
            }
        });

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
     * step2
     */
    // 新增颜色、客户、商品基础数据
    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @PostMapping("/init-prod")
    @Transactional
    public R<Integer> initProd(@Validated @RequestBody GtAndFHBInitVO initVO) {
        // 去掉可能存在的空格
        if (CollectionUtils.isNotEmpty(initVO.getExcludeArtNoList())) {
            initVO.setExcludeArtNoList(initVO.getExcludeArtNoList().stream().filter(StringUtils::isNotEmpty).map(String::trim).collect(Collectors.toList()));
        }
        Optional.ofNullable(this.storeMapper.selectOne(new LambdaQueryWrapper<Store>()
                        .eq(Store::getId, initVO.getStoreId()).eq(Store::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("档口不存在!", HttpStatus.ERROR));
        // 步骤1: 新建颜色
        this.initStoreColorList(initVO.getStoreId(), initVO.getSupplierId());
        // 步骤2: 新建客户
        this.initStoreCusList(initVO);

        // 处理的思路，以GT为主，根据GT的货号 去匹配FHB的货号，有些档口写的比较规范，这种就比较好处理
        List<GtProdSkuVO> gtOnSaleList = ObjectUtils.defaultIfNull(redisCache.getCacheObject(CacheConstants.MIGRATION_GT_SALE_BASIC_KEY + initVO.getUserId()), new ArrayList<>());
        // gt所有在售的货号列表
        List<String> gtArtNoList = gtOnSaleList.stream().map(GtProdSkuVO::getArticle_number).map(String::trim).distinct().collect(Collectors.toList());

        // 查看Fhb 这边有多少相似的货号
        List<FhbProdVO.SMIVO> fhbProdList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_SUPPLIER_PROD_KEY + initVO.getSupplierId()), new ArrayList<>());
        Map<String, String> fhbArticleNoColorMap = fhbProdList.stream().collect(Collectors.groupingBy(FhbProdVO.SMIVO::getArtNo,
                Collectors.collectingAndThen(Collectors.mapping(FhbProdVO.SMIVO::getColor, Collectors.toList()),
                        list -> "(" + list.stream().sorted(Comparator.naturalOrder()).collect(Collectors.joining(",")) + ")")));
        // GT和FHB匹配的货号map
        Map<String, List<String>> gtMatchFhbArtNoMap = new HashMap<>();
        // 以GT为准在，找FHB匹配的货号
        gtArtNoList.forEach(gtOnSaleArtNo -> fhbArticleNoColorMap.forEach((fhbArtNo, fhbArtNoColorStr) -> {
            // 3种情况 1:1 货号 2:货号R 3:货号-R
            if (Objects.equals(fhbArtNo, gtOnSaleArtNo) || Objects.equals(fhbArtNo, gtOnSaleArtNo + "R") || Objects.equals(fhbArtNo, gtOnSaleArtNo + "-R")) {
                List<String> existMatchArtNoList = gtMatchFhbArtNoMap.getOrDefault(gtOnSaleArtNo, new ArrayList<>());
                existMatchArtNoList.add(fhbArtNo);
                gtMatchFhbArtNoMap.put(gtOnSaleArtNo, existMatchArtNoList);
            }
        }));

        // gt按照货号分组
        Map<String, List<GtProdSkuVO>> gtSaleGroupMap = gtOnSaleList.stream().collect(Collectors.groupingBy(GtProdSkuVO::getArticle_number));
        // GT分类
        List<GtCateVO.GCIDataVO> cacheList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_GT_SALE_CATE_KEY + initVO.getUserId()), new ArrayList<>());
        List<SysProductCategory> prodCateList = this.prodCateMapper.selectList(new LambdaQueryWrapper<SysProductCategory>()
                .eq(SysProductCategory::getDelFlag, Constants.UNDELETED));
        Map<String, SysProductCategory> dbCateNameMap = prodCateList.stream().collect(Collectors.toMap(SysProductCategory::getName, x -> x));
        // GT商品分类和步橘分类映射
        Map<Integer, SysProductCategory> cateRelationMap = new HashMap<>();
        cacheList.forEach(gtCate -> {
            final SysProductCategory sysCate = Optional.ofNullable(dbCateNameMap.get(gtCate.getName())).orElseThrow(() -> new ServiceException("GT分类不存在!", HttpStatus.ERROR));
            cateRelationMap.put(gtCate.getId(), sysCate);
        });

        // 当天
        final Date voucherDate = java.sql.Date.valueOf(LocalDate.now());
        // 所有商品的类目属性map  key gt的product_id value StoreProductCategoryAttribute
        Map<Integer, StoreProductCategoryAttribute> prodAttrMap = new HashMap<>();
        // 待导入的商品
        List<StoreProduct> storeProdList = new ArrayList<>();
        gtMatchFhbArtNoMap.forEach((gtArtNo, fhbArtNoList) -> {
            // 排除掉需要特殊处理的货号
            if (CollectionUtils.isEmpty(initVO.getExcludeArtNoList()) || !initVO.getExcludeArtNoList().contains(gtArtNo)) {
                // 获取GT匹配的商品中的第一个商品
                List<GtProdSkuVO> gtMatchSkuList = gtSaleGroupMap.get(gtArtNo);
                final SysProductCategory sysCate = Optional.ofNullable(cateRelationMap.get(gtMatchSkuList.get(0).getCategory_nid())).orElseThrow(() -> new ServiceException("GT分类不存在!", HttpStatus.ERROR));
                // 初始化档口商品
                StoreProduct storeProd = new StoreProduct().setStoreId(initVO.getStoreId()).setProdCateId(sysCate.getId()).setProdCateName(sysCate.getName())
                        .setPrivateItem(0).setProdArtNum(gtMatchSkuList.get(0).getArticle_number()).setProdTitle(gtMatchSkuList.get(0).getCharacters()).setListingWay(ListingType.RIGHT_NOW.getValue())
                        .setVoucherDate(voucherDate).setProdStatus(EProductStatus.ON_SALE.getValue()).setRecommendWeight(0L).setSaleWeight(0L).setPopularityWeight(0L);
                // 提前设置档口商品的类目属性
                this.preMatchAttr(gtMatchSkuList.get(0).getProduct_id(), initVO.getUserId(), prodAttrMap);
                storeProdList.add(storeProd);
            }
        });
        this.storeProdMapper.insert(storeProdList);

        // 步骤4: 初始化商品属性
        List<StoreProductService> prodSvcList = new ArrayList<>();
        List<StoreProductCategoryAttribute> prodAttrList = new ArrayList<>();
        storeProdList.forEach(storeProd -> {
            // 获取GT匹配的商品sku列表
            List<GtProdSkuVO> gtMatchSkuList = gtSaleGroupMap.get(storeProd.getProdArtNum());
            // 初始化商品服务承诺
            prodSvcList.add(new StoreProductService().setStoreProdId(storeProd.getId()).setCustomRefund("0")
                    .setThirtyDayRefund("0").setOneBatchSale("1").setRefundWithinThreeDay("0"));
            // 初始化商品的类目属性
            StoreProductCategoryAttribute cateAttr = Optional.ofNullable(prodAttrMap.get(gtMatchSkuList.get(0).getProduct_id())).orElse(new StoreProductCategoryAttribute());
            cateAttr.setStoreId(storeProd.getStoreId()).setStoreProdId(storeProd.getId());
            prodAttrList.add(cateAttr);
        });
        // 插入商品颜色及颜色对应的尺码，档口服务承诺
        this.prodSvcMapper.insert(prodSvcList);
        this.prodCateAttrMapper.insert(prodAttrList);
        return R.ok();
    }


    /**
     * step3
     * 商品颜色及商品颜色尺码
     */
    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @PostMapping("/init-color")
    @Transactional
    public R<Integer> initColor(@Validated @RequestBody GtAndFHBInitVO initVO) {
        // 从数据库查询最新数据
        List<StoreProduct> storeProdList = this.storeProdMapper.selectList(new LambdaQueryWrapper<StoreProduct>()
                .eq(StoreProduct::getStoreId, initVO.getStoreId()).eq(StoreProduct::getDelFlag, Constants.UNDELETED));
        List<StoreColor> storeColorList = this.storeColorMapper.selectList(new LambdaQueryWrapper<StoreColor>()
                .eq(StoreColor::getStoreId, initVO.getStoreId()).eq(StoreColor::getDelFlag, Constants.UNDELETED));
        Map<String, StoreColor> storeColorMap = storeColorList.stream().collect(Collectors.toMap(StoreColor::getColorName, x -> x, (v1, v2) -> v2));

        // 处理的思路，以GT为主，根据GT的货号 去匹配TY的货号，有些档口写的比较规范，这种就比较好处理
        List<GtProdSkuVO> gtOnSaleList = ObjectUtils.defaultIfNull(redisCache.getCacheObject(CacheConstants.MIGRATION_GT_SALE_BASIC_KEY + initVO.getUserId()), new ArrayList<>());
        // gt所有在售的货号列表
        List<String> gtArtNoList = gtOnSaleList.stream().map(GtProdSkuVO::getArticle_number).map(String::trim).distinct().collect(Collectors.toList());
        // gt按照货号分组
        Map<String, List<GtProdSkuVO>> gtSaleGroupMap = gtOnSaleList.stream().collect(Collectors.groupingBy(GtProdSkuVO::getArticle_number));

        // 查看Fhb 这边有多少相似的货号
        List<FhbProdVO.SMIVO> fhbProdList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_SUPPLIER_PROD_KEY + initVO.getSupplierId()), new ArrayList<>());
        // Fhb按照货号分类
        Map<String, List<FhbProdVO.SMIVO>> fhbProdGroupMap = fhbProdList.stream().collect(Collectors.groupingBy(FhbProdVO.SMIVO::getArtNo));
        Map<String, String> fhbArticleNoColorMap = fhbProdList.stream().collect(Collectors.groupingBy(FhbProdVO.SMIVO::getArtNo,
                Collectors.collectingAndThen(Collectors.mapping(FhbProdVO.SMIVO::getColor, Collectors.toList()),
                        list -> "(" + list.stream().sorted(Comparator.naturalOrder()).collect(Collectors.joining(",")) + ")")));
        // GT和FHB匹配的货号map
        Map<String, List<String>> gtMatchFhbArtNoMap = new HashMap<>();
        // 以GT为准在，找FHB匹配的货号
        gtArtNoList.forEach(gtOnSaleArtNo -> fhbArticleNoColorMap.forEach((fhbArtNo, fhbArtNoColorStr) -> {
            // 3种情况 1:1 货号 2:货号R 3:货号-R
            if (Objects.equals(fhbArtNo, gtOnSaleArtNo) || Objects.equals(fhbArtNo, gtOnSaleArtNo + "R") || Objects.equals(fhbArtNo, gtOnSaleArtNo + "-R")) {
                List<String> existMatchArtNoList = gtMatchFhbArtNoMap.getOrDefault(gtOnSaleArtNo, new ArrayList<>());
                existMatchArtNoList.add(fhbArtNo);
                gtMatchFhbArtNoMap.put(gtOnSaleArtNo, existMatchArtNoList);
            }
        }));

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
            // TY匹配的货号
            List<String> fhbMatchArtNoList = gtMatchFhbArtNoMap.get(storeProd.getProdArtNum());
            // 获取GT匹配的商品sku列表
            List<GtProdSkuVO> gtMatchSkuList = gtSaleGroupMap.get(storeProd.getProdArtNum());
            // 当前货号在GT的所有尺码，作为标准尺码
            List<Integer> gtStandardSizeList = gtMatchSkuList.stream().map(sku -> (int) Math.floor(Double.parseDouble(sku.getSize()))).collect(Collectors.toList());
            AtomicInteger orderNum = new AtomicInteger();
            fhbMatchArtNoList.forEach(fhbArtNo -> {
                List<FhbProdVO.SMIVO> fhbMatchSkuList = fhbProdGroupMap.get(fhbArtNo);
                for (FhbProdVO.SMIVO smivo : fhbMatchSkuList) {
                    StoreColor storeColor = Optional.ofNullable(storeColorMap.get(smivo.getColor()))
                            .orElseThrow(() -> new ServiceException("没有FHB商品颜色!" + fhbArtNo, HttpStatus.ERROR));
                    // 处理内里材质，若步橘没有的，则需要新增
                    if (StringUtils.isNotBlank(smivo.getInnerStuff()) && !shoeUpperLiningMaterialMap.containsKey(smivo.getInnerStuff())) {
                        newShoeUpperLiningMaterialMap.put(smivo.getInnerStuff(), smivo.getInnerStuff());
                    }
                    // 该商品的颜色
                    prodColorList.add(new StoreProductColor().setStoreId(storeProd.getStoreId()).setStoreProdId(storeProd.getId())
                            .setOrderNum(orderNum.addAndGet(1)).setColorName(storeColor.getColorName()).setShoeUpperLiningMaterial(smivo.getInnerStuff())
                            .setStoreColorId(storeColor.getId()).setProdStatus(EProductStatus.ON_SALE.getValue()));
                    // 该颜色所有的尺码
                    for (int j = 0; j < Constants.SIZE_LIST.size(); j++) {
                        // FHB系统条码前缀
                        final String otherSnPrefix = smivo.getSupplierId()
                                + String.format("%05d", smivo.getSupplierSkuId()) + Constants.SIZE_LIST.get(j);
                        prodColorSizeList.add(new StoreProductColorSize().setSize(Constants.SIZE_LIST.get(j)).setStoreColorId(storeColor.getId())
                                .setStoreProdId(storeProd.getId()).setStandard(gtStandardSizeList.contains(Constants.SIZE_LIST.get(j)) ? 1 : 0)
                                // 销售价格以FHB价格为准 非标尺码价格需要加价
                                .setPrice(gtStandardSizeList.contains(Constants.SIZE_LIST.get(j)) ? smivo.getSalePrice()
                                        : smivo.getSalePrice().add(ObjectUtils.defaultIfNull(initVO.getAddOverPrice(), BigDecimal.ZERO)))
                                .setOtherSnPrefix(otherSnPrefix).setNextSn(0));
                    }
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
        prodColorSizeList.sort(Comparator.comparing(StoreProductColorSize::getStoreProdId).thenComparing(StoreProductColorSize::getSize));
        this.prodColorSizeMapper.insert(prodColorSizeList);
        // 还要更新步橘系统的条码前缀
        prodColorSizeList.forEach(x -> x.setSnPrefix(initVO.getStoreId() + String.format("%08d", x.getId())));
        this.prodColorSizeMapper.updateById(prodColorSizeList);
        return R.ok();
    }

    /**
     * step4
     */
    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @PostMapping("/init-cus-disc")
    @Transactional
    public R<Integer> initCusDisc(@Validated @RequestBody GtAndFHBInitVO initVO) {

        List<StoreProduct> storeProdList = this.storeProdMapper.selectList(new LambdaQueryWrapper<StoreProduct>()
                .eq(StoreProduct::getStoreId, initVO.getStoreId()).eq(StoreProduct::getDelFlag, Constants.UNDELETED));
        List<StoreProductColor> prodColorList = this.prodColorMapper.selectList(new LambdaQueryWrapper<StoreProductColor>()
                .eq(StoreProductColor::getStoreId, initVO.getStoreId()).eq(StoreProductColor::getDelFlag, Constants.UNDELETED));
        List<StoreCustomer> storeCusList = this.storeCusMapper.selectList(new LambdaQueryWrapper<StoreCustomer>()
                .eq(StoreCustomer::getStoreId, initVO.getStoreId()).eq(StoreCustomer::getDelFlag, Constants.UNDELETED));

        // 处理的思路，以GT为主，根据GT的货号 去匹配TY的货号，有些档口写的比较规范，这种就比较好处理
        List<GtProdSkuVO> gtOnSaleList = ObjectUtils.defaultIfNull(redisCache.getCacheObject(CacheConstants.MIGRATION_GT_SALE_BASIC_KEY + initVO.getUserId()), new ArrayList<>());
        // gt所有在售的货号列表
        List<String> gtArtNoList = gtOnSaleList.stream().map(GtProdSkuVO::getArticle_number).map(String::trim).distinct().collect(Collectors.toList());
        // FHB商品列表
        List<FhbProdVO.SMIVO> fhbProdList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_SUPPLIER_PROD_KEY + initVO.getSupplierId()), new ArrayList<>());
        if (CollectionUtils.isEmpty(fhbProdList)) {
            throw new ServiceException("FHB商品列表为空", HttpStatus.ERROR);
        }
        // fhb货号正在生效的颜色map，因为客户优惠有些是删除的颜色，需要通过这里去过滤
        Map<String, Set<String>> fhbExistArtNoColorMap = fhbProdList.stream().collect(Collectors
                .groupingBy(FhbProdVO.SMIVO::getArtNo, Collectors.mapping(FhbProdVO.SMIVO::getColor, Collectors.toSet())));
        Map<String, String> fhbArticleNoColorMap = fhbProdList.stream().collect(Collectors.groupingBy(FhbProdVO.SMIVO::getArtNo,
                Collectors.collectingAndThen(Collectors.mapping(FhbProdVO.SMIVO::getColor, Collectors.toList()),
                        list -> "(" + list.stream().sorted(Comparator.naturalOrder()).collect(Collectors.joining(",")) + ")")));
        // GT和FHB匹配的货号map
        Map<String, List<String>> gtMatchFhbArtNoMap = new HashMap<>();
        // 以GT为准在，找FHB匹配的货号
        gtArtNoList.forEach(gtOnSaleArtNo -> fhbArticleNoColorMap.forEach((fhbArtNo, fhbArtNoColorStr) -> {
            // 3种情况 1:1 货号 2:货号R 3:货号-R
            if (Objects.equals(fhbArtNo, gtOnSaleArtNo) || Objects.equals(fhbArtNo, gtOnSaleArtNo + "R") || Objects.equals(fhbArtNo, gtOnSaleArtNo + "-R")) {
                List<String> existMatchArtNoList = gtMatchFhbArtNoMap.getOrDefault(gtOnSaleArtNo, new ArrayList<>());
                existMatchArtNoList.add(fhbArtNo);
                gtMatchFhbArtNoMap.put(gtOnSaleArtNo, existMatchArtNoList);
            }
        }));

        // 从redis中获取客户优惠数据
        List<FhbCusDiscountVO.SMCDRecordVO> fhbCusDiscCacheList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_SUPPLIER_CUS_DISCOUNT_KEY + initVO.getSupplierId()), new ArrayList<>());
        if (CollectionUtils.isEmpty(fhbCusDiscCacheList)) {
            throw new ServiceException("fhb供应商客户优惠列表为空!" + initVO.getSupplierId(), HttpStatus.ERROR);
        }
        // 2025.10.16 增加一重保险，客户优惠必须是大于0的；且必须滤重
        fhbCusDiscCacheList = fhbCusDiscCacheList.stream().filter(x -> x.getDiscount() > 0).distinct().collect(Collectors.toList());

        List<FhbProdStockVO.SMPSRecordVO> fhbStockCacheList = redisCache
                .getCacheObject(CacheConstants.MIGRATION_SUPPLIER_PROD_STOCK_KEY + initVO.getSupplierId());
        if (CollectionUtils.isEmpty(fhbStockCacheList)) {
            throw new ServiceException("fhb供应商商品库存列表为空!" + initVO.getSupplierId(), HttpStatus.ERROR);
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
            // 根据步橘货号 找到FHB对应的货号，可能是列表
            List<String> fhbArtNoList = Optional.ofNullable(gtMatchFhbArtNoMap.get(storeProd.getProdArtNum())).orElseThrow(() -> new ServiceException("没有FHB货号!" + storeProd.getProdArtNum(), HttpStatus.ERROR));
            // 处理档口商品库存
            this.handleProdStock(fhbArtNoList, fhbStockGroupMap, buJuProdColorMap, storeProd.getStoreId(), storeProd.getId(), storeProd.getProdArtNum(), prodStockList);

            fhbArtNoList.forEach(fhbArtNo -> {
                // 处理档口客户商品优惠
                this.handleCusDisc(fhbArtNo, fhbExistArtNoColorMap, fhbCusDiscGroupMap, buJuProdColorMap, buJuStoreCusMap, storeProd.getStoreId(),
                        storeProd.getId(), storeProd.getProdArtNum(), prodCusDiscList);
            });

        });
        // 档口客户优惠
        this.storeCusProdDiscMapper.insert(prodCusDiscList);
        // 档口客户库存
        this.prodStockMapper.insert(prodStockList);
        return R.ok();
    }


    /**
     * step5
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
                    .setProdPrice(ObjectUtils.isNotEmpty(prodMinPrice) ? prodMinPrice.toString() : "")
                    .setSeason(ObjectUtils.isNotEmpty(cateAttr) ? cateAttr.getSuitableSeason() : "")
                    .setProdStatus(product.getProdStatus().toString())
                    .setStoreId(product.getStoreId().toString())
                    .setStoreWeight(ObjectUtils.isNotEmpty(store) ? store.getStoreWeight().toString() : WEIGHT_DEFAULT_ZERO.toString())
                    .setStoreName(ObjectUtils.isNotEmpty(store) ? store.getStoreName() : "")
                    .setStyle(ObjectUtils.isNotEmpty(cateAttr) ? cateAttr.getStyle() : "")
                    .setProdTitle(product.getProdTitle());
            if (ObjectUtils.isNotEmpty(parCate)) {
                // 如果父级分类为顶层分类，则prodCateId 和 parCateId 一样即可
                esProductDTO.setParCateId(Objects.equals(parCate.getId(), TOPMOST_PRODUCT_CATEGORY_ID) ? product.getProdCateId().toString() : parCate.getId().toString())
                        .setParCateName(Objects.equals(parCate.getId(), TOPMOST_PRODUCT_CATEGORY_ID) ? cate.getName() : parCate.getName());
            }
            if (ObjectUtils.isNotEmpty(cateAttr) && StringUtils.isNotBlank(cateAttr.getStyle())) {
                esProductDTO.setTags(Collections.singletonList(cateAttr.getStyle()));
            }
            esProductDTOList.add(esProductDTO);
        }
        // 构建批量操作请求
        List<BulkOperation> bulkOperations = new ArrayList<>();
        for (ESProductDTO esProductDTO : esProductDTOList) {
            BulkOperation bulkOperation = new BulkOperation.Builder()
                    .index(i -> i.id(esProductDTO.getStoreProdId()).index(ES_INDEX_NAME).document(esProductDTO))
                    .build();
            bulkOperations.add(bulkOperation);
        }
        // 执行批量插入
        try {
            BulkResponse response = esClientWrapper.getEsClient().bulk(b -> b.index(ES_INDEX_NAME).operations(bulkOperations));
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
     * 处理客户优惠
     */
    private void handleCusDisc(String fhbAtrNo, Map<String, Set<String>> fhbExistArtNoColorMap,
                               Map<String, Map<String, List<FhbCusDiscountVO.SMCDRecordVO>>> fhbCusDiscGroupMap,
                               Map<String, StoreProductColor> buJuProdColorMap, Map<String, StoreCustomer> buJuStoreCusMap,
                               Long storeId, Long storeProdId, String prodArtNum, List<StoreCustomerProductDiscount> prodCusDiscList) {
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
                    prodCusDiscList.add(new StoreCustomerProductDiscount().setStoreId(storeId).setStoreProdId(storeProdId).setStoreCusId(storeCus.getId()).setProdArtNum(prodArtNum)
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
     * 初始化客户列表
     *
     * @param initVO 入参
     */
    private void initStoreCusList(GtAndFHBInitVO initVO) {
        List<FhbCusVO.SMCVO> cacheList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_SUPPLIER_CUS_KEY + initVO.getSupplierId()), new ArrayList<>());
        if (CollectionUtils.isEmpty(cacheList)) {
            throw new ServiceException("供应商客户列表为空!", HttpStatus.ERROR);
        }
        List<StoreCustomer> storeCusList = cacheList.stream()
                // 排除掉现金客户 因为档口入驻时，会自动创建现金客户
                .filter(x -> !Objects.equals(x.getName(), Constants.STORE_CUS_CASH))
                .map(x -> new StoreCustomer().setStoreId(initVO.getStoreId()).setCusName(x.getName()))
                .collect(Collectors.toList());
        this.storeCusMapper.insert(storeCusList);
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
        // 3.  鞋面材质（靴筒面材质）找不到就找帮面材质
        prodAttr.setShaftMaterial(attrMap.containsKey(Constants.SHAFT_MATERIAL_NAME)
                ? attrMap.get(Constants.SHAFT_MATERIAL_NAME) : attrMap.get(Constants.UPPER_MATERIAL_NAME));
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
     * 初始化档口颜色
     *
     * @param storeId    档口ID
     * @param supplierId 供应商ID
     */
    private void initStoreColorList(Long storeId, Integer supplierId) {
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
    }

}
