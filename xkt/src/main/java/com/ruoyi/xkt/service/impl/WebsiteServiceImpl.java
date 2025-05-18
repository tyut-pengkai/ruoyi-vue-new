package com.ruoyi.xkt.service.impl;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.AdType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.framework.es.EsClientWrapper;
import com.ruoyi.xkt.domain.AdvertRound;
import com.ruoyi.xkt.domain.Store;
import com.ruoyi.xkt.domain.SysFile;
import com.ruoyi.xkt.dto.advertRound.pc.*;
import com.ruoyi.xkt.dto.dailySale.CateSaleRankDTO;
import com.ruoyi.xkt.dto.es.ESProductDTO;
import com.ruoyi.xkt.dto.storeProduct.StoreProdPriceAndMainPicDTO;
import com.ruoyi.xkt.dto.storeProductFile.StoreProdFileResDTO;
import com.ruoyi.xkt.dto.website.IndexSearchDTO;
import com.ruoyi.xkt.enums.AdDisplayType;
import com.ruoyi.xkt.enums.AdLaunchStatus;
import com.ruoyi.xkt.mapper.*;
import com.ruoyi.xkt.service.IWebsiteService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 首页搜索
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
@RequiredArgsConstructor
public class WebsiteServiceImpl implements IWebsiteService {

    final EsClientWrapper esClientWrapper;
    final RedisCache redisCache;
    final AdvertRoundMapper advertRoundMapper;
    final SysFileMapper fileMapper;
    final AdvertStoreFileMapper advertStoreFileMapper;
    final StoreProductFileMapper prodFileMapper;
    final DailySaleProductMapper dailySaleProdMapper;
    final StoreProductMapper storeProdMapper;
    final StoreMapper storeMapper;

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
        // 档口ID 过滤条件
        if (ObjectUtils.isNotEmpty(searchDTO.getStoreId())) {
            boolQuery.filter(f -> f.term(t -> t.field("storeId").value(searchDTO.getStoreId())));
        }
        // 添加prodStatus 过滤条件
        if (CollectionUtils.isNotEmpty(searchDTO.getProdStatusList())) {
            TermsQueryField termsQueryField = new TermsQueryField.Builder()
                    .value(searchDTO.getProdStatusList().stream()
                            .map(WebsiteServiceImpl::newFieldValue)
                            .collect(Collectors.toList()))
                    .build();
            boolQuery.filter(f -> f.terms(t -> t.field("prodStatus").terms(termsQueryField)));
        }
        // 添加 prodCateId 过滤条件
        if (CollectionUtils.isNotEmpty(searchDTO.getProdCateIdList())) {
            TermsQueryField termsQueryField = new TermsQueryField.Builder()
                    .value(searchDTO.getProdCateIdList().stream()
                            .map(WebsiteServiceImpl::newFieldValue)
                            .collect(Collectors.toList()))
                    .build();
            boolQuery.filter(f -> f.terms(t -> t.field("prodCateId").terms(termsQueryField)));
        }
        // 添加 parCateId 过滤条件
        if (CollectionUtils.isNotEmpty(searchDTO.getParCateIdList())) {
            TermsQueryField termsQueryField = new TermsQueryField.Builder()
                    .value(searchDTO.getParCateIdList().stream()
                            .map(WebsiteServiceImpl::newFieldValue)
                            .collect(Collectors.toList()))
                    .build();
            boolQuery.filter(f -> f.terms(t -> t.field("parCateId").terms(termsQueryField)));
        }
        // 添加 style 过滤条件
        if (CollectionUtils.isNotEmpty(searchDTO.getStyleList())) {
            TermsQueryField termsQueryField = new TermsQueryField.Builder()
                    .value(searchDTO.getStyleList().stream()
                            .map(WebsiteServiceImpl::newFieldValue)
                            .collect(Collectors.toList()))
                    .build();
            boolQuery.filter(f -> f.terms(t -> t.field("style.keyword").terms(termsQueryField)));
        }
        // 添加 season 过滤条件
        if (CollectionUtils.isNotEmpty(searchDTO.getSeasonList())) {
            TermsQueryField termsQueryField = new TermsQueryField.Builder()
                    .value(searchDTO.getSeasonList().stream()
                            .map(WebsiteServiceImpl::newFieldValue)
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

    /**
     * PC 首页 顶部左侧 轮播图
     *
     * @return PCIndexTopLeftBannerDTO
     */
    @Override
    @Transactional(readOnly = true)
    public List<PCIndexTopLeftBannerDTO> getPcIndexTopLeftBanner() {
        // 从redis 中获取 PC 首页顶部左侧 轮播图
        List<PCIndexTopLeftBannerDTO> topLeftBannerList = redisCache.getCacheObject(CacheConstants.PC_ADVERT + CacheConstants.PC_ADVERT_INDEX_TOP_LEFT);
        if (ObjectUtils.isNotEmpty(topLeftBannerList)) {
            return topLeftBannerList;
        }
        // 获取近一月 档口首页PC 顶部左侧轮播图推广数据
        List<AdvertRound> oneMonthList = this.getOneMonthAdvertList(Collections.singletonList(AdType.PC_HOME_TOP_LEFT_BANNER.getValue()));
        if (CollectionUtils.isEmpty(oneMonthList)) {
            return new ArrayList<>();
        }
        // 文件map
        Map<Long, SysFile> fileMap = fileMapper.selectList(new LambdaQueryWrapper<SysFile>().eq(SysFile::getDelFlag, Constants.UNDELETED)
                        .in(SysFile::getId, oneMonthList.stream().map(AdvertRound::getPicId).filter(ObjectUtils::isNotEmpty).collect(Collectors.toList())))
                .stream().collect(Collectors.toMap(SysFile::getId, Function.identity()));
        // 正在播放
        List<AdvertRound> launchingList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.LAUNCHING.getValue())).collect(Collectors.toList());
        // 已过期
        List<AdvertRound> expiredList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.EXPIRED.getValue())).collect(Collectors.toList());
        List<PCIndexTopLeftBannerDTO> topLeftList;
        // 如果顶部横向轮播图全部为空，则需要找填充的数据
        if (CollectionUtils.isEmpty(launchingList)) {
            List<PCIndexTopLeftBannerDTO> tempLeftList = new ArrayList<>();
            expiredList.stream().collect(Collectors.groupingBy(AdvertRound::getStoreId))
                    .forEach((storeId, list) -> {
                        AdvertRound advertRound = list.get(0);
                        tempLeftList.add(new PCIndexTopLeftBannerDTO().setDisplayType(advertRound.getDisplayType()).setStoreId(advertRound.getStoreId())
                                .setFileUrl(ObjectUtils.isNotEmpty(advertRound.getPicId()) ? fileMap.get(advertRound.getPicId()).getFileUrl() : null));
                    });
            // 给topLeftList 中的 orderNum 设置值，1 2 3 4 5，并只取5条数据
            topLeftList = tempLeftList.stream().limit(5).collect(Collectors.toList()); // 先限制为前5条数据.collect(Collectors.toList());
            for (int i = 0; i < topLeftList.size(); i++) {
                topLeftList.get(i).setOrderNum(i + 1);
            }
        } else {
            // 顶部轮播图只要有一张即可
            topLeftList = launchingList.stream().map(x -> new PCIndexTopLeftBannerDTO().setDisplayType(x.getDisplayType())
                            .setStoreId(x.getStoreId()).setOrderNum(this.positionToNumber(x.getPosition()))
                            .setFileUrl(ObjectUtils.isNotEmpty(x.getPicId()) ? fileMap.get(x.getPicId()).getFileUrl() : null))
                    .collect(Collectors.toList());
        }
        // 存放到redis中，过期时间为1天
        redisCache.setCacheObject(CacheConstants.PC_ADVERT + CacheConstants.PC_ADVERT_INDEX_TOP_LEFT, topLeftList, 1, TimeUnit.DAYS);
        return topLeftList;
    }

    /**
     * PC 首页 顶部右侧 纵向轮播图
     *
     * @return PCIndexTopRightBannerDTO
     */
    @Override
    @Transactional(readOnly = true)
    public List<PCIndexTopRightBannerDTO> getPcIndexTopRightBanner() {
        // 从redis 中获取 PC 首页顶部右侧 轮播图
        List<PCIndexTopRightBannerDTO> topRightBannerList = redisCache.getCacheObject(CacheConstants.PC_ADVERT + CacheConstants.PC_ADVERT_INDEX_TOP_RIGHT);
        if (ObjectUtils.isNotEmpty(topRightBannerList)) {
            return topRightBannerList;
        }
        List<AdvertRound> oneMonthList = this.getOneMonthAdvertList(Collections.singletonList(AdType.PC_HOME_TOP_RIGHT_BANNER.getValue()));
        if (CollectionUtils.isEmpty(oneMonthList)) {
            return new ArrayList<>();
        }
        List<StoreProdFileResDTO> mainPicList = this.prodFileMapper.selectMainPic(oneMonthList.stream().map(AdvertRound::getProdIdStr).collect(Collectors.toList()));
        Map<Long, String> mainPicMap = mainPicList.stream().collect(Collectors.toMap(StoreProdFileResDTO::getStoreProdId, StoreProdFileResDTO::getFileUrl));
        // 正在播放
        List<AdvertRound> launchingList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.LAUNCHING.getValue())).collect(Collectors.toList());
        // 已过期
        List<AdvertRound> expiredList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.EXPIRED.getValue())).collect(Collectors.toList());
        List<PCIndexTopRightBannerDTO> topRightList;
        // 顶部首页纵向轮播图
        if (CollectionUtils.isEmpty(launchingList)) {
            topRightList = this.fillTopRightFromExpired(expiredList, 4, mainPicMap);
            for (int i = 0; i < topRightList.size(); i++) {
                topRightList.get(i).setOrderNum(i + 1);
            }
        } else {
            topRightList = launchingList.stream().map(x -> new PCIndexTopRightBannerDTO().setDisplayType(x.getDisplayType()).setStoreProdId(Long.valueOf(x.getProdIdStr()))
                            .setFileUrl(mainPicMap.get(Long.valueOf(x.getProdIdStr()))).setOrderNum(this.positionToNumber(x.getPosition())))
                    .collect(Collectors.toList());
            // 如果 launchingList 只有一个则还需要补充一个推广填空
            if (launchingList.size() < 2) {
                topRightList.addAll(this.fillTopRightFromExpired(expiredList, 1, mainPicMap));
                for (int i = 0; i < topRightList.size(); i++) {
                    topRightList.get(i).setOrderNum(i + 1);
                }
            }
        }
        // 存放到redis中，过期时间为1天
        redisCache.setCacheObject(CacheConstants.PC_ADVERT + CacheConstants.PC_ADVERT_INDEX_TOP_RIGHT, topRightList, 1, TimeUnit.DAYS);
        return topRightList;
    }

    /**
     * PC 首页 销售榜
     *
     * @return PCIndexMidSalesDTO
     */
    @Override
    @Transactional(readOnly = true)
    public List<PCIndexMidSalesDTO> getPcIndexMidSaleList() {
        // 从redis中获取销售榜数据
        List<PCIndexMidSalesDTO> midSaleList = redisCache.getCacheObject(CacheConstants.PC_ADVERT + CacheConstants.PC_ADVERT_INDEX_MID_SALE);
        if (ObjectUtils.isNotEmpty(midSaleList)) {
            return midSaleList;
        }
        final Date now = java.sql.Date.valueOf(LocalDate.now());
        final Date oneMonthAgo = java.sql.Date.valueOf(LocalDate.now().minusMonths(1));
        List<CateSaleRankDTO> cateSaleList = this.dailySaleProdMapper.selectSaleRankList(oneMonthAgo, now);
        if (CollectionUtils.isEmpty(cateSaleList)) {
            return new ArrayList<>();
        }
        List<StoreProdPriceAndMainPicDTO> prodPriceAndMainPicList = this.storeProdMapper
                .selectPriceAndMainPicList(cateSaleList.stream().map(CateSaleRankDTO::getStoreProdId).collect(Collectors.toList()));
        // 档口商品的价格及商品主图map
        Map<Long, StoreProdPriceAndMainPicDTO> prodPriceAndMainPicMap = prodPriceAndMainPicList.stream()
                .collect(Collectors.toMap(StoreProdPriceAndMainPicDTO::getStoreProdId, Function.identity()));
        Map<Long, String> cateIdMap = cateSaleList.stream().collect(Collectors.toMap(CateSaleRankDTO::getProdCateId, CateSaleRankDTO::getProdCateName, (existing, replacement) -> existing));
        // Step 1: 按 prodCateId 分组，并取每组销量前5的商品
        Map<Long, List<CateSaleRankDTO>> topSaleMap = cateSaleList.stream().collect(Collectors.groupingBy(CateSaleRankDTO::getProdCateId, Collectors.collectingAndThen(
                Collectors.toList(), list -> list.stream().sorted(Comparator.comparing(CateSaleRankDTO::getSaleNum).reversed()).limit(5).collect(Collectors.toList()))));
        // Step 2: 统计每个 prodCateId 的总销量
        Map<Long, Integer> totalSalesPerCate = cateSaleList.stream().collect(Collectors.groupingBy(CateSaleRankDTO::getProdCateId, Collectors.summingInt(CateSaleRankDTO::getSaleNum)));
        // Step 3: 取总销量排名前4的 prodCateId
        List<Map.Entry<Long, Integer>> top4CateEntries = totalSalesPerCate.entrySet().stream().sorted(Map.Entry.<Long, Integer>comparingByValue().reversed()).limit(4).collect(Collectors.toList());
        // Step 4: 构建返回结果
        List<PCIndexMidSalesDTO> retCateOrderList = new ArrayList<>();
        for (int i = 0; i < top4CateEntries.size(); i++) {
            Long cateId = top4CateEntries.get(i).getKey();
            List<CateSaleRankDTO> cateDetailList = topSaleMap.getOrDefault(cateId, Collections.emptyList());
            List<PCIndexMidSalesDTO.PCIMSSaleDTO> saleDTOList = new ArrayList<>();
            for (int j = 0; j < cateDetailList.size(); j++) {
                CateSaleRankDTO dto = cateDetailList.get(j);
                PCIndexMidSalesDTO.PCIMSSaleDTO saleDTO = new PCIndexMidSalesDTO.PCIMSSaleDTO().setDisplayType(AdDisplayType.PRODUCT.getValue())
                        .setStoreId(dto.getStoreId()).setStoreName(dto.getStoreName()).setStoreProdId(dto.getStoreProdId()).setProdArtNum(dto.getProdArtNum())
                        .setStoreProdId(dto.getStoreProdId()).setSaleNum(dto.getSaleNum()).setOrderNum(j + 1)
                        .setPrice(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(dto.getStoreProdId()))
                                ? prodPriceAndMainPicMap.get(dto.getStoreProdId()).getMinPrice() : null)
                        .setManPicUrl(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(dto.getStoreProdId()))
                                ? prodPriceAndMainPicMap.get(dto.getStoreProdId()).getMainPicUrl() : "");
                saleDTOList.add(saleDTO);
            }
            retCateOrderList.add(new PCIndexMidSalesDTO().setProdCateId(cateId).setProdCateName(cateIdMap.get(cateId)).setOrderNum(i + 1).setSaleList(saleDTOList));
        }
        // 缓存至 Redis
        redisCache.setCacheObject(CacheConstants.PC_ADVERT + CacheConstants.PC_ADVERT_INDEX_MID_SALE, retCateOrderList, 1, TimeUnit.DAYS);
        return retCateOrderList;
    }

    /**
     * PC 首页 风格榜
     *
     * @return List<PCIndexMidStyleDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public List<PCIndexMidStyleDTO> getPcIndexMidStyleList() {
        // 从redis 中获取 PC 首页风格榜数据
        List<PCIndexMidStyleDTO> redisStyleList = redisCache.getCacheObject(CacheConstants.PC_ADVERT + CacheConstants.PC_ADVERT_INDEX_MID_STYLE);
        if (ObjectUtils.isNotEmpty(redisStyleList)) {
            return redisStyleList;
        }
        List<AdvertRound> oneMonthList = this.getOneMonthAdvertList(Collections.singletonList(AdType.PC_HOME_STYLE_RANK.getValue()));
        if (CollectionUtils.isEmpty(oneMonthList)) {
            return new ArrayList<>();
        }
        // 档口推广主图map
        List<SysFile> fileList = this.fileMapper.selectByIds(oneMonthList.stream().map(AdvertRound::getPicId)
                .filter(ObjectUtils::isNotEmpty).collect(Collectors.toList()));
        Map<Long, SysFile> fileMap = fileList.stream().collect(Collectors.toMap(SysFile::getId, Function.identity()));
        List<Store> storeList = this.storeMapper.selectByIds(oneMonthList.stream().map(AdvertRound::getStoreId).collect(Collectors.toList()));
        Map<Long, Store> storeMap = storeList.stream().collect(Collectors.toMap(Store::getId, Function.identity()));
        // 获取所有的档口商品ID列表
        Set<Long> storeProdIdSet = new HashSet<>();
        oneMonthList.stream().filter(x -> StringUtils.isNotBlank(x.getProdIdStr())).forEach(x -> storeProdIdSet
                .addAll(Arrays.stream(x.getProdIdStr().split(",")).map(Long::parseLong).collect(Collectors.toSet())));
        // 获取所有商品的价格及第一张主图
        List<StoreProdPriceAndMainPicDTO> prodPriceAndMainPicList = this.storeProdMapper.selectPriceAndMainPicList(new ArrayList<>(storeProdIdSet));
        Map<Long, StoreProdPriceAndMainPicDTO> prodPriceAndMainPicMap = prodPriceAndMainPicList.stream()
                .collect(Collectors.toMap(StoreProdPriceAndMainPicDTO::getStoreProdId, Function.identity()));
        // 正在播放
        List<AdvertRound> launchingList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.LAUNCHING.getValue())).collect(Collectors.toList());
        // 已过期
        List<AdvertRound> expiredList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.EXPIRED.getValue())).collect(Collectors.toList());
        List<PCIndexMidStyleDTO> midStyleList;
        // 顶部 中部 风格轮播图
        if (CollectionUtils.isEmpty(launchingList)) {
            midStyleList = this.fillMidStyleFromExpired(expiredList, prodPriceAndMainPicMap, fileMap, storeMap, 4);
            for (int i = 0; i < midStyleList.size(); i++) {
                midStyleList.get(i).setOrderNum(i + 1);
            }
        } else {
            midStyleList = this.fillMidStyleFromExpired(launchingList, prodPriceAndMainPicMap, fileMap, storeMap, 4);
            // 轮播图不足4个，则从过期的广告轮播图补充
            if (launchingList.size() < 4) {
                midStyleList.addAll(this.fillMidStyleFromExpired(expiredList, prodPriceAndMainPicMap, fileMap, storeMap, 4 - launchingList.size()));
                for (int i = 0; i < midStyleList.size(); i++) {
                    midStyleList.get(i).setOrderNum(i + 1);
                }
            }
        }
        // 存放到redis中，过期时间为1天
        redisCache.setCacheObject(CacheConstants.PC_ADVERT + CacheConstants.PC_ADVERT_INDEX_MID_STYLE, midStyleList, 1, TimeUnit.DAYS);
        return midStyleList;
    }

    /**
     * PC 首页 人气榜
     *
     * @return PCIndexBottomPopularDTO
     */
    @Override
    @Transactional(readOnly = true)
    public PCIndexBottomPopularDTO getPcIndexBottomPopularList() {
        PCIndexBottomPopularDTO popularDTO = new PCIndexBottomPopularDTO();
        // 从redis 中获取 PC 首页 人气榜数据
        PCIndexBottomPopularDTO redisPopular = redisCache.getCacheObject(CacheConstants.PC_ADVERT + CacheConstants.PC_ADVERT_INDEX_BOTTOM_POPULAR);
        if (ObjectUtils.isNotEmpty(redisPopular)) {
            return redisPopular;
        }
        List<AdvertRound> oneMonthList = this.getOneMonthAdvertList(Arrays.asList(
                AdType.PC_HOME_POP_LEFT_BANNER.getValue(), AdType.PC_HOME_POP_MID.getValue(), AdType.PC_HOME_POP_RIGHT.getValue()));
        if (CollectionUtils.isEmpty(oneMonthList)) {
            return popularDTO;
        }
        // 文件map
        Map<Long, SysFile> fileMap = fileMapper.selectList(new LambdaQueryWrapper<SysFile>().eq(SysFile::getDelFlag, Constants.UNDELETED)
                        .in(SysFile::getId, oneMonthList.stream().map(AdvertRound::getPicId).filter(ObjectUtils::isNotEmpty).collect(Collectors.toList())))
                .stream().collect(Collectors.toMap(SysFile::getId, Function.identity()));
        List<StoreProdPriceAndMainPicDTO> prodPriceAndMainPicList = this.storeProdMapper.selectPriceAndMainPicList(oneMonthList.stream()
                .filter(x -> StringUtils.isNotBlank(x.getProdIdStr())).map(x -> Long.parseLong(x.getProdIdStr())).collect(Collectors.toList()));
        // 档口商品的价格及商品主图map
        Map<Long, StoreProdPriceAndMainPicDTO> prodPriceAndMainPicMap = prodPriceAndMainPicList.stream()
                .collect(Collectors.toMap(StoreProdPriceAndMainPicDTO::getStoreProdId, Function.identity()));

        // 左侧广告
        List<AdvertRound> leftLaunchingList = oneMonthList.stream().filter(x -> Objects.equals(x.getTypeId(), AdType.PC_HOME_POP_LEFT_BANNER.getValue()))
                .filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.LAUNCHING.getValue())).collect(Collectors.toList());
        List<AdvertRound> leftExpiredList = oneMonthList.stream().filter(x -> Objects.equals(x.getTypeId(), AdType.PC_HOME_POP_LEFT_BANNER.getValue()))
                .filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.EXPIRED.getValue())).collect(Collectors.toList());
        // 中部广告
        List<AdvertRound> midLaunchingList = oneMonthList.stream().filter(x -> Objects.equals(x.getTypeId(), AdType.PC_HOME_POP_MID.getValue()))
                .filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.LAUNCHING.getValue())).collect(Collectors.toList());
        List<AdvertRound> midExpiredList = oneMonthList.stream().filter(x -> Objects.equals(x.getTypeId(), AdType.PC_HOME_POP_MID.getValue()))
                .filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.EXPIRED.getValue())).collect(Collectors.toList());
        // 右侧广告
        List<AdvertRound> rightLaunchingList = oneMonthList.stream().filter(x -> Objects.equals(x.getTypeId(), AdType.PC_HOME_POP_RIGHT.getValue()))
                .filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.LAUNCHING.getValue())).collect(Collectors.toList());
        List<AdvertRound> rightExpiredList = oneMonthList.stream().filter(x -> Objects.equals(x.getTypeId(), AdType.PC_HOME_POP_RIGHT.getValue()))
                .filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.EXPIRED.getValue())).collect(Collectors.toList());

        List<PCIndexBottomPopularDTO.PCIBPPopularLeftDTO> bottomLeftList;
        // 处理左侧广告列表
        if (CollectionUtils.isEmpty(leftLaunchingList)) {
            List<PCIndexBottomPopularDTO.PCIBPPopularLeftDTO> tempLeftList = new ArrayList<>();
            leftExpiredList.stream().collect(Collectors.groupingBy(AdvertRound::getStoreId))
                    .forEach((storeId, list) -> {
                        AdvertRound advertRound = list.get(0);
                        tempLeftList.add(new PCIndexBottomPopularDTO.PCIBPPopularLeftDTO().setDisplayType(AdDisplayType.PICTURE.getValue()).setStoreId(storeId)
                                .setFileUrl(ObjectUtils.isNotEmpty(fileMap.get(advertRound.getPicId())) ? fileMap.get(advertRound.getPicId()).getFileUrl() : ""));
                    });
            bottomLeftList = tempLeftList.stream().limit(5).collect(Collectors.toList());
            for (int i = 0; i < bottomLeftList.size(); i++) {
                bottomLeftList.get(i).setOrderNum(i + 1);
            }
        } else {
            // 人气榜底部左侧轮播图只要有一张即可
            bottomLeftList = leftLaunchingList.stream().map(x -> new PCIndexBottomPopularDTO.PCIBPPopularLeftDTO().setDisplayType(AdDisplayType.PICTURE.getValue())
                            .setStoreId(x.getStoreId()).setOrderNum(this.positionToNumber(x.getPosition()))
                            .setFileUrl(ObjectUtils.isNotEmpty(x.getPicId()) ? fileMap.get(x.getPicId()).getFileUrl() : null))
                    .collect(Collectors.toList());
        }
        popularDTO.setLeftList(bottomLeftList);

        // 处理中间广告列表
        List<PCIndexBottomPopularDTO.PCIBPPopularMidDTO> bottomMidList;
        // 处理中间广告列表
        if (CollectionUtils.isEmpty(midLaunchingList)) {
            bottomMidList = this.fillPopMidFromExpired(midExpiredList, fileMap, 2);
            for (int i = 0; i < bottomMidList.size(); i++) {
                bottomMidList.get(i).setOrderNum(i + 1);
            }
        } else {
            bottomMidList = midLaunchingList.stream().map(x -> new PCIndexBottomPopularDTO.PCIBPPopularMidDTO().setDisplayType(AdDisplayType.PICTURE.getValue())
                            .setStoreId(x.getStoreId()).setOrderNum(this.positionToNumber(x.getPosition()))
                            .setFileUrl(ObjectUtils.isNotEmpty(x.getPicId()) ? fileMap.get(x.getPicId()).getFileUrl() : null))
                    .collect(Collectors.toList());
            if (bottomMidList.size() < 2) {
                bottomMidList.addAll(this.fillPopMidFromExpired(midExpiredList, fileMap, 1));
                for (int i = 0; i < bottomMidList.size(); i++) {
                    bottomMidList.get(i).setOrderNum(i + 1);
                }
            }
        }
        popularDTO.setMidList(bottomMidList);

        // 处理右侧广告商品
        List<PCIndexBottomPopularDTO.PCIBPPopularRightDTO> bottomRightList;
        if (CollectionUtils.isEmpty(rightLaunchingList)) {
            bottomRightList = this.fillBottomRightFromExpired(rightExpiredList, prodPriceAndMainPicMap, 2);
            for (int i = 0; i < bottomRightList.size(); i++) {
                bottomRightList.get(i).setOrderNum(i + 1);
            }
        } else {
            bottomRightList = rightLaunchingList.stream().map(x -> {
                final Long storeProdId = Long.parseLong(x.getProdIdStr());
                return new PCIndexBottomPopularDTO.PCIBPPopularRightDTO().setDisplayType(AdDisplayType.PRODUCT.getValue())
                        .setStoreProdId(storeProdId).setOrderNum(this.positionToNumber(x.getPosition()))
                        .setPrice(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getMinPrice() : null)
                        .setProdArtNum(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getProdArtNum() : "")
                        .setManPicUrl(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getMainPicUrl() : "");
            }).collect(Collectors.toList());
            if (bottomRightList.size() < 2) {
                bottomRightList.addAll(this.fillBottomRightFromExpired(rightExpiredList, prodPriceAndMainPicMap, 1));
                for (int i = 0; i < bottomRightList.size(); i++) {
                    bottomRightList.get(i).setOrderNum(i + 1);
                }
            }
        }
        popularDTO.setRightList(bottomRightList);
        // 存放到redis中，过期时间为1天
        redisCache.setCacheObject(CacheConstants.PC_ADVERT + CacheConstants.PC_ADVERT_INDEX_BOTTOM_POPULAR, popularDTO, 1, TimeUnit.DAYS);
        return popularDTO;
    }

    /**
     * 填充PC 首页底部 人气榜右侧商品
     *
     * @param rightExpiredList       右侧过期广告
     * @param prodPriceAndMainPicMap 商品价格及主图map
     * @param limitCount             筛选的数量
     * @return List<PCIndexBottomPopularDTO.PCIBPPopularRightDTO>
     */
    private List<PCIndexBottomPopularDTO.PCIBPPopularRightDTO> fillBottomRightFromExpired(List<AdvertRound> rightExpiredList,
                                                                                          Map<Long, StoreProdPriceAndMainPicDTO> prodPriceAndMainPicMap, int limitCount) {
        List<PCIndexBottomPopularDTO.PCIBPPopularRightDTO> tempRightList = new ArrayList<>();
        rightExpiredList.stream().collect(Collectors.groupingBy(AdvertRound::getStoreId))
                .forEach((storeId, list) -> {
                    AdvertRound advertRound = list.get(0);
                    final Long storeProdId = Long.parseLong(advertRound.getProdIdStr());
                    tempRightList.add(new PCIndexBottomPopularDTO.PCIBPPopularRightDTO().setDisplayType(AdDisplayType.PRODUCT.getValue())
                            .setStoreProdId(Long.valueOf(advertRound.getProdIdStr()))
                            .setPrice(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getMinPrice() : null)
                            .setProdArtNum(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getProdArtNum() : "")
                            .setManPicUrl(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getMainPicUrl() : ""));
                });
        return tempRightList.stream().limit(limitCount).collect(Collectors.toList());
    }

    /**
     * 填充首页 人气榜 中部广告
     *
     * @param midExpiredList 人气榜中部过期列表
     * @param fileMap        文件map
     * @param limitCount     筛选的数量
     * @return List<PCIndexBottomPopularDTO.PCIBPPopularMidDTO>
     */
    private List<PCIndexBottomPopularDTO.PCIBPPopularMidDTO> fillPopMidFromExpired(List<AdvertRound> midExpiredList, Map<Long, SysFile> fileMap, int limitCount) {
        List<PCIndexBottomPopularDTO.PCIBPPopularMidDTO> tempMidList = new ArrayList<>();
        midExpiredList.stream().collect(Collectors.groupingBy(AdvertRound::getStoreId))
                .forEach((storeId, list) -> {
                    AdvertRound advertRound = list.get(0);
                    tempMidList.add(new PCIndexBottomPopularDTO.PCIBPPopularMidDTO().setDisplayType(AdDisplayType.PICTURE.getValue()).setStoreId(storeId)
                            .setFileUrl(ObjectUtils.isNotEmpty(fileMap.get(advertRound.getPicId())) ? fileMap.get(advertRound.getPicId()).getFileUrl() : ""));
                });
        return tempMidList.stream().limit(limitCount).collect(Collectors.toList());
    }

    /**
     * 填充首页中部风格榜数据
     *
     * @param dbStyleList            风格数据
     * @param prodPriceAndMainPicMap 档口商品价格及主图map
     * @param fileMap                档口推广图map
     * @param storeMap               档口map
     * @param limitCount             返回的数据数量
     * @return List<PCIndexMidStyleDTO>
     */
    private List<PCIndexMidStyleDTO> fillMidStyleFromExpired(List<AdvertRound> dbStyleList, Map<Long, StoreProdPriceAndMainPicDTO> prodPriceAndMainPicMap,
                                                             Map<Long, SysFile> fileMap, Map<Long, Store> storeMap, int limitCount) {
        List<PCIndexMidStyleDTO> midStyleList = new ArrayList<>();
        dbStyleList.stream().collect(Collectors.groupingBy(AdvertRound::getStoreId))
                .forEach((storeId, list) -> {
                    AdvertRound advertRound = list.get(0);
                    List<String> prodIdStrList = Arrays.asList(advertRound.getProdIdStr().split(","));
                    List<PCIndexMidStyleDTO.PCIMSStyleDTO> styleList = new ArrayList<>();
                    for (int i = 0; i < prodIdStrList.size(); i++) {
                        Long storeProdId = Long.valueOf(prodIdStrList.get(i));
                        styleList.add(new PCIndexMidStyleDTO.PCIMSStyleDTO().setStoreProdId(storeProdId).setOrderNum(i + 1).setDisplayType(AdDisplayType.PRODUCT.getValue())
                                .setPrice(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getMinPrice() : null)
                                .setProdArtNum(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getProdArtNum() : "")
                                .setManPicUrl(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getMainPicUrl() : ""));
                    }
                    midStyleList.add(new PCIndexMidStyleDTO().setStoreId(storeId).setStoreName(storeMap.get(storeId).getStoreName()).setDisplayType(AdDisplayType.PICTURE.getValue())
                            .setPicUrl(ObjectUtils.isNotEmpty(fileMap.get(advertRound.getPicId())) ? fileMap.get(advertRound.getPicId()).getFileUrl() : "").setStyleList(styleList));
                });
        return midStyleList.stream().limit(limitCount).collect(Collectors.toList());
    }


    /**
     * 填充首页顶部右侧纵向轮播图
     *
     * @param expiredList 过期列表
     * @param limitCount  返回的数据条数
     * @param mainPicMap  商品主图map
     * @return
     */
    private List<PCIndexTopRightBannerDTO> fillTopRightFromExpired(List<AdvertRound> expiredList, int limitCount, Map<Long, String> mainPicMap) {
        List<PCIndexTopRightBannerDTO> tempRightList = new ArrayList<>();
        expiredList.stream().collect(Collectors.groupingBy(AdvertRound::getStoreId))
                .forEach((storeId, list) -> {
                    AdvertRound advertRound = list.get(0);
                    tempRightList.add(new PCIndexTopRightBannerDTO().setDisplayType(advertRound.getDisplayType()).setStoreProdId(Long.valueOf(advertRound.getProdIdStr()))
                            .setFileUrl(mainPicMap.get(Long.valueOf(advertRound.getProdIdStr()))));
                });
        return tempRightList.stream().limit(limitCount).collect(Collectors.toList());
    }

    /**
     * 获取近一月档口推广数据
     *
     * @return 近一月档口推广数据
     */
    private List<AdvertRound> getOneMonthAdvertList(final List<Integer> typeIdList) {
        // 当前时间
        final Date now = java.sql.Date.valueOf(LocalDate.now());
        // 一月前
        final Date oneMonthAgo = java.sql.Date.valueOf(LocalDate.now().minusMonths(1));
        // 查询最近一个月所有的 正在播放 及 已过期的推广列表
        return this.advertRoundMapper.selectList(new LambdaQueryWrapper<AdvertRound>()
                .isNotNull(AdvertRound::getStoreId).eq(AdvertRound::getDelFlag, Constants.UNDELETED)
                .in(AdvertRound::getTypeId, typeIdList).between(AdvertRound::getStartTime, oneMonthAgo, now)
                .in(AdvertRound::getLaunchStatus, Arrays.asList(AdLaunchStatus.LAUNCHING.getValue(), AdLaunchStatus.EXPIRED.getValue())));
    }


    /**
     * 将position 的 A B C D E 转化为 1 2 3 4 5
     *
     * @param position 位置
     * @return 1 2 3 4 5
     */
    private int positionToNumber(String position) {
        if (position == null || position.isEmpty()) {
            throw new IllegalArgumentException("Position cannot be null or empty.");
        }
        char firstChar = Character.toUpperCase(position.charAt(0));
        if (firstChar < 'A' || firstChar > 'Z') {
            throw new IllegalArgumentException("Position must start with a letter A-Z.");
        }
        return firstChar - 'A' + 1;
    }

    private static FieldValue newFieldValue(String value) {
        return FieldValue.of(value);
    }


}
