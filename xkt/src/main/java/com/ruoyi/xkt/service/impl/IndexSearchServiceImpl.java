package com.ruoyi.xkt.service.impl;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.framework.es.EsClientWrapper;
import com.ruoyi.xkt.dto.es.ESProductDTO;
import com.ruoyi.xkt.dto.website.IndexSearchDTO;
import com.ruoyi.xkt.service.IIndexSearchService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
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

        String indexName = "product_info";
        /*// 查询索引
        GetIndexResponse res = esClientWrapper.getEsClient().indices().get(request -> request.index(indexName));
        System.err.println(res);
        System.err.println(res.result());

        Set<String> all = esClientWrapper.getEsClient().indices().get(req -> req.index("*")).result().keySet();
        System.out.println("all = " + all);

        GetResponse<ESProductInfo> response = esClientWrapper.getEsClient().get(g -> g.index(indexName).id("1"), ESProductInfo.class);
        System.err.println(response);*/
        // 分页查询

        // 如果有搜索词，则要分词
        if (StringUtils.isNotBlank(searchDTO.getSearch())) {
        }


        // 多个query 根据入参是否为空 分别赋值，有multi_match , terms , range


        /*SearchResponse<ESProductInfo> list = esClientWrapper.getEsClient().search(s -> s
                        .index(indexName)
                        .query(q -> q
                                .bool(b -> b
                                        .must(m -> m
                                                .multiMatch(mm -> mm
                                                .query(searchDTO.getSearch())
                                                .fields("prodTitle", "prodArtNum", "storeName"))
                                        )
                                        .filter(f -> f
                                                .terms(t -> t
                                                        .field("prodCateId")
                                                        .terms(new TermsQueryField.Builder()
                                                                .value(searchDTO.getProdCateIdList().stream()
                                                                        .map(IndexSearchServiceImpl::newFieldValue)
                                                                        .collect(Collectors.toList()))
                                                                .build()
                                                        ))
                                        )
                                       .filter(f -> f
                                                .terms(t -> t
                                                        .field("parCateId")
                                                        .terms(new TermsQueryField.Builder()
                                                                .value(searchDTO.getParCateIdList().stream()
                                                                        .map(IndexSearchServiceImpl::newFieldValue)
                                                                        .collect(Collectors.toList()))
                                                                .build())))
                                        .filter(f -> f
                                                .terms(t -> t
                                                        .field("style.keyword")
                                                        .terms(new TermsQueryField.Builder()
                                                                .value(searchDTO.getStyleList().stream()
                                                                        .map(IndexSearchServiceImpl::newFieldValue)
                                                                        .collect(Collectors.toList()))
                                                                .build())))
                                        .filter(f -> f
                                                .terms(t -> t
                                                        .field("season.keyword")
                                                        .terms(new TermsQueryField.Builder()
                                                                .value(searchDTO.getSeasonList().stream()
                                                                        .map(IndexSearchServiceImpl::newFieldValue)
                                                                        .collect(Collectors.toList()))
                                                                .build())))
                                )
                        )
                        .from(0)
                        .size(10)
                        .sort(sort -> sort.field(f -> f.field("recommendWeight").order(SortOrder.Desc)))
                ,
                ESProductInfo.class
        );*/


        // 构建 bool 查询
        BoolQuery.Builder boolQuery = new BoolQuery.Builder();

        // 添加 price 范围查询
        if (searchDTO.getMinPrice() != null && searchDTO.getMaxPrice() != null) {
            RangeQuery.Builder builder = new RangeQuery.Builder();
            builder.number(NumberRangeQuery.of(n -> n.field("prodPrice").gte(Double.valueOf(searchDTO.getMinPrice()))
                    .lte(Double.valueOf(searchDTO.getMaxPrice()))));
            boolQuery.filter(builder.build()._toQuery());
        }
        // 添加 multiMatch 查询
        if (StringUtils.isNotBlank(searchDTO.getSearch())) {
            MultiMatchQuery multiMatchQuery = MultiMatchQuery.of(m -> m
                    .query(searchDTO.getSearch())
                    .fields("prodTitle", "prodArtNum", "storeName")
            );
            boolQuery.must(multiMatchQuery._toQuery());
        }
        // 添加 prodCateId 过滤条件
        if (searchDTO.getProdCateIdList() != null && !searchDTO.getProdCateIdList().isEmpty()) {
            TermsQueryField termsQueryField = new TermsQueryField.Builder()
                    .value(searchDTO.getProdCateIdList().stream()
                            .map(IndexSearchServiceImpl::newFieldValue)
                            .collect(Collectors.toList()))
                    .build();
            boolQuery.filter(f -> f.terms(t -> t.field("prodCateId").terms(termsQueryField)));
        }
        // 添加 parCateId 过滤条件
        if (searchDTO.getParCateIdList() != null && !searchDTO.getParCateIdList().isEmpty()) {
            TermsQueryField termsQueryField = new TermsQueryField.Builder()
                    .value(searchDTO.getParCateIdList().stream()
                            .map(IndexSearchServiceImpl::newFieldValue)
                            .collect(Collectors.toList()))
                    .build();
            boolQuery.filter(f -> f.terms(t -> t.field("parCateId").terms(termsQueryField)));
        }
        // 添加 style 过滤条件
        if (searchDTO.getStyleList() != null && !searchDTO.getStyleList().isEmpty()) {
            TermsQueryField termsQueryField = new TermsQueryField.Builder()
                    .value(searchDTO.getStyleList().stream()
                            .map(IndexSearchServiceImpl::newFieldValue)
                            .collect(Collectors.toList()))
                    .build();
            boolQuery.filter(f -> f.terms(t -> t.field("style.keyword").terms(termsQueryField)));
        }
        // 添加 season 过滤条件
        if (searchDTO.getSeasonList() != null && !searchDTO.getSeasonList().isEmpty()) {
            TermsQueryField termsQueryField = new TermsQueryField.Builder()
                    .value(searchDTO.getSeasonList().stream()
                            .map(IndexSearchServiceImpl::newFieldValue)
                            .collect(Collectors.toList()))
                    .build();
            boolQuery.filter(f -> f.terms(t -> t.field("season.keyword").terms(termsQueryField)));
        }
        // 构建最终的查询
        Query query = new Query.Builder().bool(boolQuery.build()).build();
        // 执行搜索
        SearchResponse<ESProductDTO> resList = esClientWrapper.getEsClient().search(s -> s.index(indexName)
                        .query(query).from(searchDTO.getPageNum() - 1).size(searchDTO.getPageSize())
                        .sort(sort -> sort.field(f -> f.field(searchDTO.getSort()).order(SortOrder.Desc))),
                ESProductDTO.class);
        return CollectionUtils.isEmpty(resList.hits().hits()) ? Page.empty(searchDTO.getPageSize(), searchDTO.getPageNum())
                : Page.convert(new PageInfo<>(resList.hits().hits().stream().map(Hit::source).collect(Collectors.toList())));
    }

    private static FieldValue newFieldValue(String value) {
        return FieldValue.of(value);
    }


}
