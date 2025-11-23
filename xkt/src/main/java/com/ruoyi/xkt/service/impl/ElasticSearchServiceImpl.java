package com.ruoyi.xkt.service.impl;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.framework.es.EsClientWrapper;
import com.ruoyi.framework.notice.fs.FsNotice;
import com.ruoyi.xkt.domain.Store;
import com.ruoyi.xkt.domain.StoreProduct;
import com.ruoyi.xkt.domain.StoreProductCategoryAttribute;
import com.ruoyi.xkt.domain.SysProductCategory;
import com.ruoyi.xkt.dto.elasticSearch.EsProdBatchCreateDTO;
import com.ruoyi.xkt.dto.elasticSearch.EsProdBatchDeleteDTO;
import com.ruoyi.xkt.dto.es.ESProductDTO;
import com.ruoyi.xkt.dto.storeProdColorPrice.StoreProdMinPriceDTO;
import com.ruoyi.xkt.dto.storeProductFile.StoreProdMainPicDTO;
import com.ruoyi.xkt.dto.website.IndexSearchDTO;
import com.ruoyi.xkt.enums.FileType;
import com.ruoyi.xkt.mapper.*;
import com.ruoyi.xkt.service.IElasticSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.ruoyi.common.constant.Constants.WEIGHT_DEFAULT_ZERO;

/**
 * 公告 服务层实现
 *
 * @author ruoyi
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ElasticSearchServiceImpl implements IElasticSearchService {

    final StoreProductColorSizeMapper prodColorSizeMapper;
    final StoreProductMapper storeProdMapper;
    final StoreMapper storeMapper;
    final StoreProductCategoryAttributeMapper prodCateAttrMapper;
    final SysProductCategoryMapper prodCateMapper;
    final EsClientWrapper esClientWrapper;
    final StoreProductFileMapper prodFileMapper;
    final FsNotice fsNotice;
    @Value("${es.indexName}")
    private String ES_INDEX_NAME;

    /**
     * 批量往ES新增商品数据
     *
     * @param storeId 档口ID
     * @return Integer
     */
    @Override
    @Transactional
    public void batchCreate(Long storeId) {
        LambdaQueryWrapper<StoreProduct> wrapper = new LambdaQueryWrapper<StoreProduct>()
                .eq(StoreProduct::getDelFlag, Constants.UNDELETED);
        if (ObjectUtils.isNotEmpty(storeId)) {
            wrapper.eq(StoreProduct::getStoreId, storeId);
        }
        List<StoreProduct> storeProdList = this.storeProdMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(storeProdList)) {
            throw new ServiceException("商品不存在", HttpStatus.ERROR);
        }
        // 创建商品到ES中
        this.createProdToEs(storeProdList);
    }

    /**
     * 批量新增商品数据
     *
     * @param createProdDTO 新增商品入参
     * @return Integer
     */
    @Override
    @Transactional
    public void batchCreateProd(EsProdBatchCreateDTO createProdDTO) {
        LambdaQueryWrapper<StoreProduct> wrapper = new LambdaQueryWrapper<StoreProduct>()
                .eq(StoreProduct::getDelFlag, Constants.UNDELETED).eq(StoreProduct::getStoreId, createProdDTO.getStoreId())
                .in(StoreProduct::getId, createProdDTO.getStoreProdIdList());
        List<StoreProduct> storeProdList = this.storeProdMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(storeProdList)) {
            throw new ServiceException("商品不存在", HttpStatus.ERROR);
        }
        // 创建商品到ES中
        this.createProdToEs(storeProdList);
    }

    /**
     * 批量删除商品数据
     *
     * @param storeId 档口ID
     * @return Integer
     */
    @Override
    @Transactional
    public void batchDelete(Long storeId) {
        LambdaQueryWrapper<StoreProduct> queryWrapper = new LambdaQueryWrapper<StoreProduct>()
                .eq(StoreProduct::getDelFlag, Constants.UNDELETED);
        if (ObjectUtils.isNotEmpty(storeId)) {
            queryWrapper.eq(StoreProduct::getStoreId, storeId);
        }
        List<StoreProduct> storeProdList = this.storeProdMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(storeProdList)) {
            throw new ServiceException("商品不存在", HttpStatus.ERROR);
        }
        // 批量删除商品
        this.deleteEsProd(storeProdList);
    }


    /**
     * 批量删除商品数据
     *
     * @param deleteDTO 删除商品入参
     */
    @Override
    @Transactional
    public void batchDeleteProd(EsProdBatchDeleteDTO deleteDTO) {
        LambdaQueryWrapper<StoreProduct> queryWrapper = new LambdaQueryWrapper<StoreProduct>()
                .eq(StoreProduct::getDelFlag, Constants.UNDELETED).eq(StoreProduct::getStoreId, deleteDTO.getStoreId())
                .in(StoreProduct::getId, deleteDTO.getStoreProdIdList());
        List<StoreProduct> storeProdList = this.storeProdMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(storeProdList)) {
            throw new ServiceException("商品不存在", HttpStatus.ERROR);
        }
        // 批量删除商品
        this.deleteEsProd(storeProdList);
    }


    /**
     * 网站首页搜索
     *
     * @param searchDTO 搜索入参
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ESProductDTO> search(IndexSearchDTO searchDTO) throws IOException {
        // 构建 bool 查询
        BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();
        // 添加 price 范围查询
        if (ObjectUtils.isNotEmpty(searchDTO.getMinPrice()) && ObjectUtils.isNotEmpty(searchDTO.getMaxPrice())) {
            RangeQuery.Builder rangeBuilder = new RangeQuery.Builder();
            rangeBuilder.number(NumberRangeQuery.of(n -> n.field("prodPrice").gte(Double.valueOf(searchDTO.getMinPrice()))
                    .lte(Double.valueOf(searchDTO.getMaxPrice()))));
            boolQueryBuilder.filter(rangeBuilder.build()._toQuery());
        }
        if (StringUtils.isNotBlank(searchDTO.getSearch())) {
            String searchTerm = searchDTO.getSearch().trim();
            // 创建专门的搜索 bool query
            BoolQuery.Builder searchBoolQuery = new BoolQuery.Builder();
            // 判断搜索词类型
            boolean hasChinese = containsChinese(searchTerm);
            boolean hasEnglishOrNumber = containsEnglishOrNumber(searchTerm);
            boolean isShortText = searchTerm.length() <= 3;
            // 核心策略：根据搜索词类型选择最优查询方式
            if (hasChinese && hasEnglishOrNumber) {
                // 混合文本（如"娇点高筒MaMaT1"）- 智能拆分搜索
                handleMixedTextSearch(searchTerm, searchBoolQuery);
            } else if (isShortText && hasEnglishOrNumber) {
                // 短英文/数字 - 使用通配符和前缀匹配
                handleShortEnglishNumberSearch(searchTerm, searchBoolQuery);
            } else if (isShortText && hasChinese) {
                // 短中文 - 使用分词匹配
                handleShortChineseSearch(searchTerm, searchBoolQuery);
            } else {
                // 长文本 - 使用标准分词搜索
                handleStandardTextSearch(searchTerm, searchBoolQuery);
            }
            // 设置最小匹配条件
            searchBoolQuery.minimumShouldMatch("1");
            // 将搜索条件添加到主查询
            boolQueryBuilder.must(searchBoolQuery.build()._toQuery());
        }
        // 档口ID 过滤条件
        if (CollectionUtils.isNotEmpty(searchDTO.getStoreIdList())) {
            TermsQueryField termsQueryField = new TermsQueryField.Builder()
                    .value(searchDTO.getStoreIdList().stream()
                            .map(FieldValue::of)
                            .collect(Collectors.toList()))
                    .build();
            boolQueryBuilder.filter(f -> f.terms(t -> t.field("storeId").terms(termsQueryField)));
        }
        // 添加prodStatus 过滤条件
        if (CollectionUtils.isNotEmpty(searchDTO.getProdStatusList())) {
            TermsQueryField termsQueryField = new TermsQueryField.Builder()
                    .value(searchDTO.getProdStatusList().stream()
                            .map(FieldValue::of)
                            .collect(Collectors.toList()))
                    .build();
            boolQueryBuilder.filter(f -> f.terms(t -> t.field("prodStatus").terms(termsQueryField)));
        }
        // 添加 prodCateId 过滤条件
        if (CollectionUtils.isNotEmpty(searchDTO.getProdCateIdList())) {
            TermsQueryField termsQueryField = new TermsQueryField.Builder()
                    .value(searchDTO.getProdCateIdList().stream()
                            .map(FieldValue::of)
                            .collect(Collectors.toList()))
                    .build();
            boolQueryBuilder.filter(f -> f.terms(t -> t.field("prodCateId").terms(termsQueryField)));
        }
        // 添加 parCateId 过滤条件
        if (CollectionUtils.isNotEmpty(searchDTO.getParCateIdList())) {
            TermsQueryField termsQueryField = new TermsQueryField.Builder()
                    .value(searchDTO.getParCateIdList().stream()
                            .map(FieldValue::of)
                            .collect(Collectors.toList()))
                    .build();
            boolQueryBuilder.filter(f -> f.terms(t -> t.field("parCateId").terms(termsQueryField)));
        }
        // 添加 style 过滤条件
        if (CollectionUtils.isNotEmpty(searchDTO.getStyleList())) {
            TermsQueryField termsQueryField = new TermsQueryField.Builder()
                    .value(searchDTO.getStyleList().stream()
                            .map(FieldValue::of)
                            .collect(Collectors.toList()))
                    .build();
            boolQueryBuilder.filter(f -> f.terms(t -> t.field("style.keyword").terms(termsQueryField)));
        }
        // 添加 season 过滤条件
        if (CollectionUtils.isNotEmpty(searchDTO.getSeasonList())) {
            TermsQueryField termsQueryField = new TermsQueryField.Builder()
                    .value(searchDTO.getSeasonList().stream()
                            .map(FieldValue::of)
                            .collect(Collectors.toList()))
                    .build();
            boolQueryBuilder.filter(f -> f.terms(t -> t.field("season.keyword").terms(termsQueryField)));
        }
        // 如果是按照时间过滤，则表明是"新品"，则限制 时间范围 30天前到现在
        if (Objects.equals(searchDTO.getSort(), "createTime")) {
            // 当前时间
            final String nowStr = DateUtils.getTime();
            // 当前时间往前推30天，获取当天的0点0分0秒
            LocalDateTime ago = LocalDateTime.now().minusDays(30).withHour(0).withMinute(0).withSecond(0);
            // ago 转化为 yyyy-MM-dd HH:mm:ss
            String agoStr = ago.format(DateTimeFormatter.ofPattern(DateUtils.YYYY_MM_DD_HH_MM_SS));
            RangeQuery.Builder rangeBuilder = new RangeQuery.Builder();
            rangeBuilder.date(DateRangeQuery.of(d -> d.field("createTime").gte(agoStr).lte(nowStr)));
            boolQueryBuilder.filter(rangeBuilder.build()._toQuery());
        }
        // 构建最终的 bool query
        BoolQuery boolQuery = boolQueryBuilder.build();
        // 构建最终的查询
        Query query = new Query.Builder().bool(boolQuery).build();
        long start = System.currentTimeMillis();
        // 执行搜索
        SearchResponse<ESProductDTO> resList = esClientWrapper.getEsClient()
                .search(s -> s.index(ES_INDEX_NAME)
                                .query(query)
                                .from((searchDTO.getPageNum() - 1) * searchDTO.getPageSize())
                                .size(searchDTO.getPageSize())
                                .sort(Arrays.asList(
                                        SortOptions.of(so -> so.field(f -> f.field(searchDTO.getSort()).order(searchDTO.getOrder()))),
                                        SortOptions.of(so -> so.field(f -> f.field("storeWeight").order(SortOrder.Desc)))
                                )),
                        ESProductDTO.class);
        long second = System.currentTimeMillis();
        System.err.println("查询耗时：" + (second - start));
        final long total = resList.hits().total().value();
        final List<ESProductDTO> esProdList = resList.hits().hits().stream().map(x -> x.source().setStoreProdId(x.id())).collect(Collectors.toList());
        return new Page<>(searchDTO.getPageNum(), searchDTO.getPageSize(), total / searchDTO.getPageSize() + 1, total, esProdList);
    }


    /**
     * 处理混合文本搜索（中英文数字混合）
     *
     * @param searchTerm
     * @param searchBoolQuery
     */
    private void handleMixedTextSearch(String searchTerm, BoolQuery.Builder searchBoolQuery) {
        // 1. 整体多字段匹配（主要方式）
        MultiMatchQuery multiMatchQuery = MultiMatchQuery.of(m -> m
                .query(searchTerm)
                .fields("storeName^3", "prodTitle^2", "prodCateName", "parCateName", "prodArtNum^2")
                .type(TextQueryType.BestFields)
        );
        searchBoolQuery.should(multiMatchQuery._toQuery());
        // 2. 提取英文数字部分进行通配符匹配
        String englishNumberPart = extractEnglishNumberPart(searchTerm);
        if (StringUtils.isNotBlank(englishNumberPart)) {
            String wildcardPattern = "*" + englishNumberPart + "*";
            WildcardQuery wildcardQuery = WildcardQuery.of(w -> w
                    .field("storeName.keyword")
                    .value(wildcardPattern)
                    .boost(2.0f) // 提高权重，因为这是用户明确输入的英文部分
            );
            searchBoolQuery.should(wildcardQuery._toQuery());
            // 同时在商品编号字段搜索
            WildcardQuery artNumQuery = WildcardQuery.of(w -> w
                    .field("prodArtNum.keyword")
                    .value(wildcardPattern)
                    .boost(1.5f)
            );
            searchBoolQuery.should(artNumQuery._toQuery());
        }
        // 3. 中文部分单独匹配
        String chinesePart = extractChinesePart(searchTerm);
        if (StringUtils.isNotBlank(chinesePart)) {
            MatchQuery chineseQuery = MatchQuery.of(m -> m
                    .field("storeName")
                    .query(chinesePart)
                    .boost(1.0f)
            );
            searchBoolQuery.should(chineseQuery._toQuery());
        }
    }

    /**
     * 处理短英文数字搜索
     *
     * @param searchTerm
     * @param searchBoolQuery
     */
    private void handleShortEnglishNumberSearch(String searchTerm, BoolQuery.Builder searchBoolQuery) {
        // 1. 前缀匹配（最高优先级）
        String prefixPattern = searchTerm + "*";
        WildcardQuery prefixQuery = WildcardQuery.of(w -> w
                .field("storeName.keyword")
                .value(prefixPattern)
                .boost(3.0f)
        );
        searchBoolQuery.should(prefixQuery._toQuery());
        // 2. 通配符匹配
        String wildcardPattern = "*" + searchTerm + "*";
        WildcardQuery wildcardQuery = WildcardQuery.of(w -> w
                .field("storeName.keyword")
                .value(wildcardPattern)
                .boost(2.0f)
        );
        searchBoolQuery.should(wildcardQuery._toQuery());
        // 3. 在商品编号字段搜索
        WildcardQuery artNumQuery = WildcardQuery.of(w -> w
                .field("prodArtNum.keyword")
                .value(wildcardPattern)
                .boost(2.5f)
        );
        searchBoolQuery.should(artNumQuery._toQuery());
    }

    /**
     * 处理短中文搜索
     *
     * @param searchTerm
     * @param searchBoolQuery
     */
    private void handleShortChineseSearch(String searchTerm, BoolQuery.Builder searchBoolQuery) {
        // 使用 MultiMatchQuery 进行分词匹配
        MultiMatchQuery multiMatchQuery = MultiMatchQuery.of(m -> m
                .query(searchTerm)
                .fields("storeName^3", "prodTitle^2", "prodCateName", "parCateName")
                .type(TextQueryType.MostFields) // 使用最多字段匹配
        );
        searchBoolQuery.should(multiMatchQuery._toQuery());
    }

    /**
     * 处理标准长文本搜索
     *
     * @param searchTerm
     * @param searchBoolQuery
     */
    private void handleStandardTextSearch(String searchTerm, BoolQuery.Builder searchBoolQuery) {
        // 使用标准的 MultiMatchQuery
        MultiMatchQuery multiMatchQuery = MultiMatchQuery.of(m -> m
                .query(searchTerm)
                .fields("storeName^3", "prodTitle", "prodCateName", "parCateName", "prodArtNum^2")
                .type(TextQueryType.BestFields)
        );
        searchBoolQuery.should(multiMatchQuery._toQuery());
    }


    /**
     * 判断是否包含中文
     *
     * @param str
     * @return
     */
    private boolean containsChinese(String str) {
        if (str == null) return false;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c >= '\u4e00' && c <= '\u9fa5') {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否包含英文或数字
     *
     * @param str
     * @return
     */
    private boolean containsEnglishOrNumber(String str) {
        if (str == null) return false;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9')) {
                return true;
            }
        }
        return false;
    }

    /**
     * 提取英文数字部分
     *
     * @param str
     * @return
     */
    private String extractEnglishNumberPart(String str) {
        if (str == null) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9')) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 提取中文部分
     *
     * @param str
     * @return
     */
    private String extractChinesePart(String str) {
        if (str == null) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c >= '\u4e00' && c <= '\u9fa5') {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 创建商品数据到ES中
     *
     * @param storeProdList
     */
    private void createProdToEs(List<StoreProduct> storeProdList) {
        final List<String> storeProdIdList = storeProdList.stream().map(StoreProduct::getId).map(String::valueOf).collect(Collectors.toList());
        // 所有的分类
        List<SysProductCategory> prodCateList = this.prodCateMapper.selectList(new LambdaQueryWrapper<SysProductCategory>()
                .eq(SysProductCategory::getDelFlag, Constants.UNDELETED));
        Map<Long, SysProductCategory> prodCateMap = prodCateList.stream().collect(Collectors.toMap(SysProductCategory::getId, x -> x));
        List<StoreProdMainPicDTO> mainPicDTOList = this.prodFileMapper.selectMainPicByStoreProdIdList(storeProdIdList.stream()
                .map(Long::valueOf).collect(Collectors.toList()), FileType.MAIN_PIC.getValue(), Constants.ORDER_NUM_1);
        Map<Long, String> mainPicMap = mainPicDTOList.stream().collect(Collectors.toMap(StoreProdMainPicDTO::getStoreProdId, StoreProdMainPicDTO::getFileUrl));
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
                    .setMainPicUrl(mainPicMap.get(product.getId())).setMainPicName("").setMainPicSize(BigDecimal.ZERO)
                    .setParCateId(ObjectUtils.isNotEmpty(parCate) ? parCate.getId().toString() : "")
                    .setParCateName(ObjectUtils.isNotEmpty(parCate) ? parCate.getName() : "")
                    .setProdPrice(ObjectUtils.isNotEmpty(prodMinPrice) ? prodMinPrice.toString() : "")
                    .setSeason(ObjectUtils.isNotEmpty(cateAttr) ? cateAttr.getSuitableSeason() : "")
                    .setProdStatus(product.getProdStatus().toString()).setStoreId(product.getStoreId().toString())
                    .setStoreWeight(ObjectUtils.isNotEmpty(store) ? store.getStoreWeight().toString() : WEIGHT_DEFAULT_ZERO.toString())
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
            log.info("全量新增到 ES 成功的 id列表: {}", response.items().stream().map(BulkResponseItem::id).collect(Collectors.toList()));
            // 有哪些没执行成功的，需要发飞书通知
            List<String> successIdList = response.items().stream().map(BulkResponseItem::id).collect(Collectors.toList());
            List<String> unExeIdList = storeProdIdList.stream().map(String::valueOf).filter(x -> !successIdList.contains(x)).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(unExeIdList)) {
                fsNotice.sendMsg2DefaultChat("全量新增商品到 ES 失败", "以下storeProdId未执行成功: " + unExeIdList);
            } else {
                fsNotice.sendMsg2DefaultChat("全量新增商品到 ES 成功", "共处理 " + response.items().size() + " 条记录");
            }
        } catch (Exception e) {
            log.error("批量新增到 ES 失败", e);
            fsNotice.sendMsg2DefaultChat("全量新增商品到 ES 失败", e.getMessage());
        }
    }

    /**
     * 批量删除商品数据
     *
     * @param storeProdList 批量删除商品
     */
    private void deleteEsProd(List<StoreProduct> storeProdList) {
        final List<String> storeProdIdList = storeProdList.stream().map(StoreProduct::getId).map(String::valueOf).collect(Collectors.toList());
        // 构建批量删除操作请求
        List<BulkOperation> bulkOperations = new ArrayList<>();
        for (String storeProdId : storeProdIdList) {
            BulkOperation bulkOperation = new BulkOperation.Builder()
                    .delete(d -> d.id(storeProdId).index(ES_INDEX_NAME))
                    .build();
            bulkOperations.add(bulkOperation);
        }
        // 执行批量删除
        try {
            BulkResponse response = esClientWrapper.getEsClient().bulk(b -> b.index(ES_INDEX_NAME).operations(bulkOperations));
            log.info("全量删除 ES 成功的 id列表: {}", response.items().stream().map(BulkResponseItem::id).collect(Collectors.toList()));
            // 有哪些没执行成功的，需要发飞书通知
            List<String> successIdList = response.items().stream().map(BulkResponseItem::id).collect(Collectors.toList());
            List<String> unExeIdList = storeProdIdList.stream().map(String::valueOf).filter(x -> !successIdList.contains(x)).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(unExeIdList)) {
                fsNotice.sendMsg2DefaultChat("全量删除商品到 ES 失败", "以下storeProdId未执行成功: " + unExeIdList);
            } else {
                fsNotice.sendMsg2DefaultChat("全量删除商品到 ES 成功", "共处理 " + response.items().size() + " 条记录");
            }
        } catch (Exception e) {
            log.error("批量删除 ES 失败", e);
            fsNotice.sendMsg2DefaultChat("全量删除商品到 ES 失败", e.getMessage());
        }
    }

}
