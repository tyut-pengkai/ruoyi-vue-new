package com.ruoyi.xkt.service.impl;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.framework.es.EsClientWrapper;
import com.ruoyi.xkt.dto.es.ESProductDTO;
import com.ruoyi.xkt.dto.website.IndexSearchDTO;
import com.ruoyi.xkt.service.IIndexSearchService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 首页搜索
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
@RequiredArgsConstructor
public class IndexSearchServiceImpl implements IIndexSearchService {

    final EsClientWrapper esClientWrapper;


    /**
     * 网站首页搜索
     *
     * @param searchDTO 搜索入参
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ESProductDTO> search(IndexSearchDTO searchDTO) throws IOException {

        /*// 查询索引
        GetIndexResponse res = esClientWrapper.getEsClient().indices().get(request -> request.index(indexName));
        System.err.println(res);
        System.err.println(res.result());

        Set<String> all = esClientWrapper.getEsClient().indices().get(req -> req.index("*")).result().keySet();
        System.out.println("all = " + all);

        GetResponse<ESProductInfo> response = esClientWrapper.getEsClient().get(g -> g.index(indexName).id("1"), ESProductInfo.class);
        System.err.println(response);*/
        // 分页查询

        // 构建 bool 查询
        BoolQuery.Builder boolQuery = new BoolQuery.Builder();
        // 添加 price 范围查询
        if (ObjectUtils.isNotEmpty(searchDTO.getMinPrice()) && ObjectUtils.isNotEmpty(searchDTO.getMaxPrice())) {
            RangeQuery.Builder builder = new RangeQuery.Builder();
            builder.number(NumberRangeQuery.of(n -> n.field("prodPrice").gte(Double.valueOf(searchDTO.getMinPrice()))
                    .lte(Double.valueOf(searchDTO.getMaxPrice()))));
            boolQuery.filter(builder.build()._toQuery());
        }
        // 添加 multiMatch 查询
        if (StringUtils.isNotBlank(searchDTO.getSearch())) {
            MultiMatchQuery multiMatchQuery = MultiMatchQuery.of(m -> m
                    .query(searchDTO.getSearch())
                    .fields("prodTitle", "prodArtNum", "storeName", "prodCateName", "parCateName")
            );
            boolQuery.must(multiMatchQuery._toQuery());
        }
        // 添加prodStatus 过滤条件
        if (CollectionUtils.isNotEmpty(searchDTO.getProdStatusList())) {
            TermsQueryField termsQueryField = new TermsQueryField.Builder()
                    .value(searchDTO.getProdStatusList().stream()
                            .map(IndexSearchServiceImpl::newFieldValue)
                            .collect(Collectors.toList()))
                    .build();
            boolQuery.filter(f -> f.terms(t -> t.field("prodStatus").terms(termsQueryField)));
        }
        // 添加 prodCateId 过滤条件
        if (CollectionUtils.isNotEmpty(searchDTO.getProdCateIdList())) {
            TermsQueryField termsQueryField = new TermsQueryField.Builder()
                    .value(searchDTO.getProdCateIdList().stream()
                            .map(IndexSearchServiceImpl::newFieldValue)
                            .collect(Collectors.toList()))
                    .build();
            boolQuery.filter(f -> f.terms(t -> t.field("prodCateId").terms(termsQueryField)));
        }
        // 添加 parCateId 过滤条件
        if (CollectionUtils.isNotEmpty(searchDTO.getParCateIdList())) {
            TermsQueryField termsQueryField = new TermsQueryField.Builder()
                    .value(searchDTO.getParCateIdList().stream()
                            .map(IndexSearchServiceImpl::newFieldValue)
                            .collect(Collectors.toList()))
                    .build();
            boolQuery.filter(f -> f.terms(t -> t.field("parCateId").terms(termsQueryField)));
        }
        // 添加 style 过滤条件
        if (CollectionUtils.isNotEmpty(searchDTO.getStyleList())) {
            TermsQueryField termsQueryField = new TermsQueryField.Builder()
                    .value(searchDTO.getStyleList().stream()
                            .map(IndexSearchServiceImpl::newFieldValue)
                            .collect(Collectors.toList()))
                    .build();
            boolQuery.filter(f -> f.terms(t -> t.field("style.keyword").terms(termsQueryField)));
        }
        // 添加 season 过滤条件
        if (CollectionUtils.isNotEmpty(searchDTO.getSeasonList())) {
            TermsQueryField termsQueryField = new TermsQueryField.Builder()
                    .value(searchDTO.getSeasonList().stream()
                            .map(IndexSearchServiceImpl::newFieldValue)
                            .collect(Collectors.toList()))
                    .build();
            boolQuery.filter(f -> f.terms(t -> t.field("season.keyword").terms(termsQueryField)));
        }

        // 如果是按照时间过滤，则表明是“新品”，则限制 时间范围 20天前到现在
        if (Objects.equals(searchDTO.getSort(), "createTime")) {
            // 当前时间
            final String nowStr = DateUtils.getTime();
            // 当前时间往前推20天，获取当天的0点0分0秒
            LocalDateTime ago = LocalDateTime.now().minusDays(20).withHour(0).withMinute(0).withSecond(0);
            // ago 转化为 yyyy-MM-dd HH:mm:ss
            String agoStr = ago.format(DateTimeFormatter.ofPattern(DateUtils.YYYY_MM_DD_HH_MM_SS));
            RangeQuery.Builder builder = new RangeQuery.Builder();
            builder.date(DateRangeQuery.of(d -> d.field("createTime").gte(agoStr).lte(nowStr)));
            boolQuery.filter(builder.build()._toQuery());
        }

        // 构建最终的查询
        Query query = new Query.Builder().bool(boolQuery.build()).build();
        // 执行搜索
        SearchResponse<ESProductDTO> resList = esClientWrapper.getEsClient().search(s -> s.index(Constants.ES_IDX_PRODUCT_INFO)
                        .query(query).from(searchDTO.getPageNum() - 1).size(searchDTO.getPageSize())
                        .sort(sort -> sort.field(f -> f.field(searchDTO.getSort()).order(SortOrder.Desc))),
                ESProductDTO.class);

        System.err.println(resList);

        return CollectionUtils.isEmpty(resList.hits().hits()) ? Page.empty(searchDTO.getPageSize(), searchDTO.getPageNum())
                : Page.convert(new PageInfo<>(resList.hits().hits().stream().map(x -> x.source().setStoreProdId(x.id())).collect(Collectors.toList())));
//                : Page.convert(new PageInfo<>(resList.hits().hits().stream().map(Hit::source).collect(Collectors.toList())));
    }

    private static FieldValue newFieldValue(String value) {
        return FieldValue.of(value);
    }


}
