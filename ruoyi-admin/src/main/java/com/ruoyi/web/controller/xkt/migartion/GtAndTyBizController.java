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
import com.ruoyi.web.controller.xkt.migartion.vo.gt.GtCateVO;
import com.ruoyi.web.controller.xkt.migartion.vo.gt.GtProdSkuVO;
import com.ruoyi.web.controller.xkt.migartion.vo.gtAndTy.GtAndTYCompareDownloadVO;
import com.ruoyi.web.controller.xkt.migartion.vo.gtAndTy.GtAndTYInitVO;
import com.ruoyi.web.controller.xkt.migartion.vo.ty.TyCusDiscImportVO;
import com.ruoyi.web.controller.xkt.migartion.vo.ty.TyCusImportVO;
import com.ruoyi.web.controller.xkt.migartion.vo.ty.TyProdImportVO;
import com.ruoyi.web.controller.xkt.migartion.vo.ty.TyProdStockVO;
import com.ruoyi.xkt.domain.*;
import com.ruoyi.xkt.dto.es.ESProductDTO;
import com.ruoyi.xkt.dto.storeProdColorPrice.StoreProdMinPriceDTO;
import com.ruoyi.xkt.enums.EProductStatus;
import com.ruoyi.xkt.enums.ListingType;
import com.ruoyi.xkt.mapper.*;
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
import java.util.stream.Collectors;

import static com.ruoyi.common.constant.Constants.WEIGHT_DEFAULT_ZERO;

/**
 * Compare 相关 适用于处理不太的数据
 *
 * @author ruoyi
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/v1/gt-ty")
public class GtAndTyBizController extends BaseController {

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
    final EsClientWrapper esClientWrapper;
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
    @GetMapping("/compare/{userId}")
    public void compare(HttpServletResponse response, @PathVariable("userId") Integer userId) throws UnsupportedEncodingException {
        Map<String, List<String>> multiSaleSameGoMap = new HashMap<>();
        Map<String, List<String>> multiOffSaleSameGoMap = new HashMap<>();
        Map<String, List<String>> multiSameTyMap = new HashMap<>();

        List<GtProdSkuVO> gtSaleBasicList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_GT_SALE_BASIC_KEY + userId), new ArrayList<>());
        Map<String, String> articleNoColorMap = gtSaleBasicList.stream().collect(Collectors.groupingBy(GtProdSkuVO::getArticle_number,
                Collectors.collectingAndThen(Collectors.mapping(GtProdSkuVO::getColor, Collectors.toList()),
                        list -> "(" + list.stream().distinct().collect(Collectors.joining(",")) + ")")));
        List<String> doubleRunSaleArtNoList = gtSaleBasicList.stream().map(GtProdSkuVO::getArticle_number).distinct().collect(Collectors.toList());
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
        List<String> doubleRunOffSaleArtNoList = doubleRunOffSaleBasicList.stream().map(GtProdSkuVO::getArticle_number).distinct().collect(Collectors.toList());
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

    /**
     * step2
     * 颜色、客户、商品初始化（包含类目属性及服务等）
     */
    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @PostMapping("/init-prod")
    @Transactional
    public R<Integer> initProd(@Validated @RequestBody GtAndTYInitVO initVO) {
        if (CollectionUtils.isNotEmpty(initVO.getExcludeArtNoList())) {
            // 去掉可能的空格
            initVO.setExcludeArtNoList(initVO.getExcludeArtNoList().stream().map(String::trim).collect(Collectors.toList()));
        }
        Optional.ofNullable(this.storeMapper.selectOne(new LambdaQueryWrapper<Store>()
                        .eq(Store::getId, initVO.getStoreId()).eq(Store::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("档口不存在!", HttpStatus.ERROR));
        // 步骤1: 颜色初始化
        this.initStoreColorList(initVO.getStoreId(), initVO.getUserId());
        // 步骤2: 客户初始化
        this.initStoreCusList(initVO);
        // 步骤3: 商品初始化
        Map<String, List<String>> multiSaleSameGoMap = new HashMap<>();
        Map<String, List<String>> multiOffSaleSameGoMap = new HashMap<>();
        Map<String, List<String>> multiSameTyMap = new HashMap<>();
        List<GtProdSkuVO> gtSaleBasicList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_GT_SALE_BASIC_KEY + initVO.getUserId()), new ArrayList<>());
        // 查看gt 在售的商品 这边有多少相似的货号
        gtSaleBasicList.stream().map(GtProdSkuVO::getArticle_number).distinct()
                .forEach(article_number -> {
                    // 只保留核心连续的数字，去除其他所有符号
                    String cleanArtNo = this.extractCoreArticleNumber(article_number);
                    if (CollectionUtils.isEmpty(initVO.getExcludeArtNoList()) || !initVO.getExcludeArtNoList().contains(cleanArtNo)) {
                        List<String> existList = multiSaleSameGoMap.containsKey(cleanArtNo) ? multiSaleSameGoMap.get(cleanArtNo) : new ArrayList<>();
                        existList.add(article_number);
                        multiSaleSameGoMap.put(cleanArtNo, existList);
                    }
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
        tyProdList.stream().map(TyProdImportVO::getProdArtNum).distinct()
                .forEach(artNo -> {
                    // 只保留核心连续的数字，去除其他所有符号
                    String cleanArtNo = this.extractCoreArticleNumber(artNo);
                    if (CollectionUtils.isEmpty(initVO.getExcludeArtNoList()) || !initVO.getExcludeArtNoList().contains(cleanArtNo)) {
                        List<String> existList = multiSameTyMap.containsKey(cleanArtNo) ? multiSameTyMap.get(cleanArtNo) : new ArrayList<>();
                        existList.add(artNo);
                        multiSameTyMap.put(cleanArtNo, existList);
                    }
                });
        // gt按照货号分组
        Map<String, List<GtProdSkuVO>> gtSaleGroupMap = gtSaleBasicList.stream().collect(Collectors.groupingBy(GtProdSkuVO::getArticle_number));
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
            final SysProductCategory sysCate = Optional.ofNullable(cateRelationMap.get(gtMatchSkuList.get(0).getCategory_nid())).orElseThrow(() -> new ServiceException("GT分类不存在!", HttpStatus.ERROR));
            // 初始化档口商品
            StoreProduct storeProd = new StoreProduct().setStoreId(initVO.getStoreId())
                    .setProdCateId(sysCate.getId()).setProdCateName(sysCate.getName()).setPrivateItem(0)
                    .setProdArtNum(gtMatchSkuList.get(0).getArticle_number()).setProdTitle(gtMatchSkuList.get(0).getCharacters())
                    .setListingWay(ListingType.RIGHT_NOW.getValue()).setVoucherDate(voucherDate).setProdStatus(EProductStatus.ON_SALE.getValue())
                    .setRecommendWeight(0L).setSaleWeight(0L).setPopularityWeight(0L);
            // 提前设置档口商品的类目属性
            this.preMatchAttr(gtMatchSkuList.get(0).getProduct_id(), initVO.getUserId(), prodAttrMap);
            storeProdList.add(storeProd);
        });
        this.storeProdMapper.insert(storeProdList);

        // 步骤4: 初始化商品属性
        List<StoreProductService> prodSvcList = new ArrayList<>();
        List<StoreProductCategoryAttribute> prodAttrList = new ArrayList<>();
        storeProdList.forEach(storeProd -> {
            // 获取clearArtNo
            String clearArtNo = this.extractCoreArticleNumber(storeProd.getProdArtNum());
            // 获取GT匹配的商品sku列表
            List<GtProdSkuVO> gtMatchSkuList = this.getGtFirstSku(multiSaleSameGoMap, gtSaleGroupMap, clearArtNo);
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
    public R<Integer> initColor(@Validated @RequestBody GtAndTYInitVO initVO) {
        // 从数据库查询最新数据
        List<StoreProduct> storeProdList = this.storeProdMapper.selectList(new LambdaQueryWrapper<StoreProduct>()
                .eq(StoreProduct::getStoreId, initVO.getStoreId()).eq(StoreProduct::getDelFlag, Constants.UNDELETED));
        List<StoreColor> storeColorList = this.storeColorMapper.selectList(new LambdaQueryWrapper<StoreColor>()
                .eq(StoreColor::getStoreId, initVO.getStoreId()).eq(StoreColor::getDelFlag, Constants.UNDELETED));
        Map<String, StoreColor> storeColorMap = storeColorList.stream().collect(Collectors.toMap(StoreColor::getColorName, x -> x));

        Map<String, List<String>> multiSaleSameGoMap = new HashMap<>();
        Map<String, List<String>> multiSameTyMap = new HashMap<>();
        List<GtProdSkuVO> gtSaleBasicList = ObjectUtils.defaultIfNull(redisCache
                .getCacheObject(CacheConstants.MIGRATION_GT_SALE_BASIC_KEY + initVO.getUserId()), new ArrayList<>());
        // 查看gt 在售的商品 这边有多少相似的货号
        gtSaleBasicList.stream().map(GtProdSkuVO::getArticle_number).distinct()
                .forEach(article_number -> {
                    // 只保留核心连续的数字，去除其他所有符号
                    String cleanArtNo = this.extractCoreArticleNumber(article_number);
                    if (CollectionUtils.isEmpty(initVO.getExcludeArtNoList()) || !initVO.getExcludeArtNoList().contains(cleanArtNo)) {
                        List<String> existList = multiSaleSameGoMap.containsKey(cleanArtNo) ? multiSaleSameGoMap.get(cleanArtNo) : new ArrayList<>();
                        existList.add(article_number);
                        multiSaleSameGoMap.put(cleanArtNo, existList);
                    }
                });

        // 查看TY 这边有多少相似的货号
        List<TyProdImportVO> tyProdList = redisCache.getCacheObject(CacheConstants.MIGRATION_TY_PROD_KEY + initVO.getUserId());
        // TY按照颜色分类
        Map<String, List<TyProdImportVO>> tyProdGroupMap = tyProdList.stream().collect(Collectors.groupingBy(TyProdImportVO::getProdArtNum));
        tyProdList.stream().map(TyProdImportVO::getProdArtNum).distinct()
                .forEach(artNo -> {
                    // 只保留核心连续的数字，去除其他所有符号
                    String cleanArtNo = this.extractCoreArticleNumber(artNo);
                    if (CollectionUtils.isEmpty(initVO.getExcludeArtNoList()) || !initVO.getExcludeArtNoList().contains(cleanArtNo)) {
                        List<String> existList = multiSameTyMap.containsKey(cleanArtNo) ? multiSameTyMap.get(cleanArtNo) : new ArrayList<>();
                        existList.add(artNo);
                        multiSameTyMap.put(cleanArtNo, existList);
                    }
                });
        // gt按照货号分组
        Map<String, List<GtProdSkuVO>> gtSaleGroupMap = gtSaleBasicList.stream().collect(Collectors.groupingBy(GtProdSkuVO::getArticle_number));

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
            // 取clearArtNo
            String clearArtNo = this.extractCoreArticleNumber(storeProd.getProdArtNum());
            // TY匹配的货号
            List<String> tyMatchArtNoList = multiSameTyMap.get(clearArtNo);
            // 获取GT匹配的商品sku列表
            List<GtProdSkuVO> gtMatchSkuList = this.getGtFirstSku(multiSaleSameGoMap, gtSaleGroupMap, clearArtNo);
            // 当前货号在GT的所有尺码，作为标准尺码
            List<Integer> gtStandardSizeList = gtMatchSkuList.stream().map(sku -> (int) Math.floor(Double.parseDouble(sku.getSize()))).collect(Collectors.toList());
            AtomicInteger orderNum = new AtomicInteger();
            tyMatchArtNoList.forEach(tyArtNo -> {
                List<TyProdImportVO> tyMatchSkuList = tyProdGroupMap.get(tyArtNo);
                for (TyProdImportVO tyProdImportVO : tyMatchSkuList) {
                    StoreColor storeColor = Optional.ofNullable(storeColorMap.get(tyProdImportVO.getColorName()))
                            .orElseThrow(() -> new ServiceException("没有TY商品颜色!" + tyArtNo, HttpStatus.ERROR));
                    // 处理内里材质，若步橘没有的，则需要新增
                    if (StringUtils.isNotBlank(tyProdImportVO.getShoeUpperLiningMaterial()) && !shoeUpperLiningMaterialMap.containsKey(tyProdImportVO.getShoeUpperLiningMaterial())) {
                        newShoeUpperLiningMaterialMap.put(tyProdImportVO.getShoeUpperLiningMaterial(), tyProdImportVO.getShoeUpperLiningMaterial());
                    }
                    // 该商品的颜色
                    prodColorList.add(new StoreProductColor().setStoreId(storeProd.getStoreId()).setStoreProdId(storeProd.getId()).setOrderNum(orderNum.addAndGet(1))
                            .setShoeUpperLiningMaterial(tyProdImportVO.getShoeUpperLiningMaterial()).setColorName(storeColor.getColorName())
                            .setStoreColorId(storeColor.getId()).setProdStatus(EProductStatus.ON_SALE.getValue()));
                    // 该颜色所有的尺码
                    for (int j = 0; j < Constants.SIZE_LIST.size(); j++) {
                        // TY系统条码前缀
                        final String otherSnPrefix = tyProdImportVO.getSn() + Constants.SIZE_LIST.get(j);
                        prodColorSizeList.add(new StoreProductColorSize().setSize(Constants.SIZE_LIST.get(j)).setStoreColorId(storeColor.getId())
                                .setStoreProdId(storeProd.getId()).setOtherSnPrefix(otherSnPrefix).setNextSn(0)
                                .setPrice(gtStandardSizeList.contains(Constants.SIZE_LIST.get(j)) ? tyProdImportVO.getPrice()
                                        : tyProdImportVO.getPrice().add(ObjectUtils.defaultIfNull(initVO.getAddOverPrice(), BigDecimal.ZERO)))
                                .setStandard(gtStandardSizeList.contains(Constants.SIZE_LIST.get(j)) ? 1 : 0));
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
    public R<Integer> initCusDisc(@Validated @RequestBody GtAndTYInitVO initVO) {

        List<StoreProduct> storeProdList = this.storeProdMapper.selectList(new LambdaQueryWrapper<StoreProduct>()
                .eq(StoreProduct::getStoreId, initVO.getStoreId()).eq(StoreProduct::getDelFlag, Constants.UNDELETED));
        List<StoreProductColor> prodColorList = this.prodColorMapper.selectList(new LambdaQueryWrapper<StoreProductColor>()
                .eq(StoreProductColor::getStoreId, initVO.getStoreId()).eq(StoreProductColor::getDelFlag, Constants.UNDELETED));
        List<StoreCustomer> storeCusList = this.storeCusMapper.selectList(new LambdaQueryWrapper<StoreCustomer>()
                .eq(StoreCustomer::getStoreId, initVO.getStoreId()).eq(StoreCustomer::getDelFlag, Constants.UNDELETED));

        Map<String, List<String>> multiSameTyMap = new HashMap<>();
        // 查看TY 这边有多少相似的货号
        List<TyProdImportVO> tyProdList = redisCache.getCacheObject(CacheConstants.MIGRATION_TY_PROD_KEY + initVO.getUserId());
        tyProdList.stream().map(TyProdImportVO::getProdArtNum).distinct()
                .forEach(artNo -> {
                    // 只保留核心连续的数字，去除其他所有符号
                    String cleanArtNo = this.extractCoreArticleNumber(artNo);
                    if (CollectionUtils.isEmpty(initVO.getExcludeArtNoList()) || !initVO.getExcludeArtNoList().contains(cleanArtNo)) {
                        List<String> existList = multiSameTyMap.containsKey(cleanArtNo) ? multiSameTyMap.get(cleanArtNo) : new ArrayList<>();
                        existList.add(artNo);
                        multiSameTyMap.put(cleanArtNo, existList);
                    }
                });

        // TY商品缓存
        List<TyProdImportVO> cacheList = redisCache.getCacheObject(CacheConstants.MIGRATION_TY_PROD_KEY + initVO.getUserId());
        if (CollectionUtils.isEmpty(cacheList)) {
            throw new ServiceException("TY商品列表为空", HttpStatus.ERROR);
        }
        // ty货号正在生效的颜色map，因为客户优惠有些是删除的颜色，需要通过这里去过滤
        Map<String, Set<String>> tyExistArtNoColorMap = cacheList.stream().collect(Collectors
                .groupingBy(TyProdImportVO::getProdArtNum, Collectors.mapping(TyProdImportVO::getColorName, Collectors.toSet())));
        // 从redis中获取已存在的客户优惠数据
        List<TyCusDiscImportVO> tyCusDiscCacheList = redisCache.getCacheObject(CacheConstants.MIGRATION_TY_CUS_DISCOUNT_KEY + initVO.getUserId());
        if (CollectionUtils.isEmpty(tyCusDiscCacheList)) {
            throw new ServiceException("ty供应商客户优惠列表为空!" + initVO.getUserId(), HttpStatus.ERROR);
        }
        Map<String, List<TyCusDiscImportVO>> distinctDiscMap = tyCusDiscCacheList.stream()
                // 第一重保险：客户优惠必须大于0；且必须滤重
                .filter(x -> x.getDiscount() > 0).distinct().collect(Collectors
                        .groupingBy(x -> x.getCusName() + x.getProdArtNum() + ":" + x.getColorName()));
        List<TyCusDiscImportVO> tyCusDiscDistinctCacheList = new ArrayList<>();
        distinctDiscMap.forEach((k, multiDiscList) -> {
            // 存在这种错误数据，同一客户：相同颜色及货号 优惠相同 但初始价格及优惠价格不一样 比如：228 213；218 203 但优惠都是15。所以统一处理，取最大优惠
            if (multiDiscList.size() > 1) {
                // 获取最大优惠
                TyCusDiscImportVO maxDisc = multiDiscList.stream().max(Comparator.comparingInt(TyCusDiscImportVO::getDiscount)).get();
                tyCusDiscDistinctCacheList.add(maxDisc);
            } else {
                tyCusDiscDistinctCacheList.addAll(multiDiscList);
            }
        });

        // 从redis中获取已存在的商品库存数据
        List<TyProdStockVO> tyStockList = redisCache.getCacheObject(CacheConstants.MIGRATION_TY_PROD_STOCK_KEY + initVO.getUserId());
        if (CollectionUtils.isEmpty(tyStockList)) {
            throw new ServiceException("ty供应商商品库存列表为空!" + initVO.getUserId(), HttpStatus.ERROR);
        }
        // TY 货号颜色的库存对应关系
        Map<String, Map<String, TyProdStockVO>> tyProdStockMap = tyStockList.stream().collect(Collectors
                .groupingBy(TyProdStockVO::getProdArtNum, Collectors.toMap(TyProdStockVO::getColorName, x -> x)));
        // TY 货号颜色优惠对应关系
        Map<String, Map<String, List<TyCusDiscImportVO>>> tyCusDiscGroupMap = tyCusDiscDistinctCacheList.stream().collect(Collectors
                .groupingBy(TyCusDiscImportVO::getProdArtNum, Collectors.groupingBy(TyCusDiscImportVO::getColorName)));
        // 步橘系统商品所有颜色maps
        Map<Long, Map<String, StoreProductColor>> prodColorGroupMap = prodColorList.stream().collect(Collectors
                .groupingBy(StoreProductColor::getStoreProdId, Collectors.toMap(StoreProductColor::getColorName, x -> x)));
        // 步橘系统客户名称map
        Map<String, StoreCustomer> buJuStoreCusMap = storeCusList.stream().collect(Collectors.toMap(StoreCustomer::getCusName, x -> x));
        List<StoreCustomerProductDiscount> prodCusDiscList = new ArrayList<>();
        List<StoreProductStock> prodStockList = new ArrayList<>();
        // 依次遍历商品列表，找到货号和FHB货号对应关系，然后用颜色进行匹配，建立客户优惠关系
        storeProdList.forEach(storeProd -> {
            final String cleanArtNo = this.extractCoreArticleNumber(storeProd.getProdArtNum());
            // 当前商品颜色列表 key 颜色中文名称
            Map<String, StoreProductColor> buJuProdColorMap = Optional.ofNullable(prodColorGroupMap.get(storeProd.getId())).orElseThrow(() -> new ServiceException("没有商品颜色!" + storeProd.getProdArtNum(), HttpStatus.ERROR));
            // 根据步橘货号 找到TY对应的货号，可能是列表
            List<String> tyAtrNoList = Optional.ofNullable(multiSameTyMap.get(cleanArtNo)).orElseThrow(() -> new ServiceException("没有TY货号!" + storeProd.getProdArtNum(), HttpStatus.ERROR));
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
     * 初始化系统颜色
     *
     * @param storeId 档口ID
     * @param userId  GT用户ID
     */
    private void initStoreColorList(Long storeId, Integer userId) {
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
        storeColorList.stream().collect(Collectors.toMap(StoreColor::getColorName, x -> x));
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
    private void handleCusDisc(String tyAtrNo, Map<String, Set<String>> tyExistArtNoColorMap,
                               Map<String, Map<String, List<TyCusDiscImportVO>>> tyCusDiscGroupMap,
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
        // GT 最短的货号
        String shortestArtNo = gtMatchArtNoList.stream().min(Comparator.comparingInt(String::length))
                .orElse(gtMatchArtNoList.get(0));
        return gtSaleGroupMap.get(shortestArtNo);
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
