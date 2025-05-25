package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.AdType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.framework.es.EsClientWrapper;
import com.ruoyi.xkt.domain.*;
import com.ruoyi.xkt.dto.advertRound.app.category.APPCateDTO;
import com.ruoyi.xkt.dto.advertRound.app.index.*;
import com.ruoyi.xkt.dto.advertRound.app.own.APPOwnGuessLikeDTO;
import com.ruoyi.xkt.dto.advertRound.pc.PCDownloadDTO;
import com.ruoyi.xkt.dto.advertRound.pc.PCSearchDTO;
import com.ruoyi.xkt.dto.advertRound.pc.PCUserCenterDTO;
import com.ruoyi.xkt.dto.advertRound.pc.index.*;
import com.ruoyi.xkt.dto.advertRound.pc.newProd.*;
import com.ruoyi.xkt.dto.advertRound.pc.store.PCStoreMidBannerDTO;
import com.ruoyi.xkt.dto.advertRound.pc.store.PCStoreTopBannerDTO;
import com.ruoyi.xkt.dto.advertRound.picSearch.PicSearchAdvertDTO;
import com.ruoyi.xkt.dto.dailySale.CateSaleRankDTO;
import com.ruoyi.xkt.dto.es.ESProductDTO;
import com.ruoyi.xkt.dto.storeProduct.StoreProdPriceAndMainPicAndTagDTO;
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
    final DailyProdTagMapper dailyProdTagMapper;
    final StoreProductMapper storeProdMapper;
    final DailyStoreTagMapper dailyStoreTagMapper;
    final StoreMapper storeMapper;
    final StoreProductStatisticsMapper prodStatsMapper;

    /**
     * APP 首页热卖精选列表
     *
     * @param searchDTO 搜索入参
     * @return List<APPIndexHotSalePageDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public Page<APPIndexHotSaleDTO> appIndexHotSalePage(IndexSearchDTO searchDTO) throws IOException {
        Page<ESProductDTO> page = this.search(searchDTO);
        // 筛选出真实的数据
        List<APPIndexHotSaleDTO> realDataList = page.getList().stream()
                .map(esProduct -> BeanUtil.toBean(esProduct, APPIndexHotSaleDTO.class).setAdvert(Boolean.FALSE)).collect(Collectors.toList());
        // APP 只有第一页 有数据 其它页暂时没有广告
        if (searchDTO.getPageNum() > 1) {
            return new Page<>(page.getPageNum(), page.getPageSize(), page.getPages(), page.getTotal(), realDataList);
        }
        // 从redis中获取广告
        List<APPIndexHotSaleDTO> redisList = this.redisCache.getCacheObject(CacheConstants.APP_ADVERT + CacheConstants.APP_INDEX_HOT_SALE_ADVERT);
        if (CollectionUtils.isNotEmpty(redisList)) {
            // 添加广告的数据
            return new Page<>(page.getPageNum(), page.getPageSize(), page.getPages(), page.getTotal(), insertAdvertsIntoList(realDataList, redisList, Constants.insertPositions));
        } else {
            // 从数据库查首页精选热卖推广（精准搜索是否存在推广，不存在从已过期的数据中拉数据来凑数）
            List<AdvertRound> advertRoundList = this.advertRoundMapper.selectList(new LambdaQueryWrapper<AdvertRound>()
                    .isNotNull(AdvertRound::getStoreId).eq(AdvertRound::getDelFlag, Constants.UNDELETED)
                    .eq(AdvertRound::getTypeId, AdType.APP_HOME_HOT_RECOMMEND_PROD.getValue())
                    .eq(AdvertRound::getLaunchStatus, AdLaunchStatus.LAUNCHING.getValue()));
            if (CollectionUtils.isNotEmpty(advertRoundList)) {
                List<StoreProdPriceAndMainPicAndTagDTO> attrList = storeProdMapper.selectPriceAndMainPicAndTagList(advertRoundList.stream()
                        .map(x -> x.getProdIdStr()).map(Long::parseLong).collect(Collectors.toList()));
                attrList = attrList.stream().peek(x -> x.setTags(StringUtils.isNotBlank(x.getTagStr()) ? Arrays.asList(x.getTagStr().split(",")) : null)).collect(Collectors.toList());
                Map<Long, StoreProdPriceAndMainPicAndTagDTO> attrMap = attrList.stream().collect(Collectors.toMap(StoreProdPriceAndMainPicAndTagDTO::getStoreProdId, x -> x));
                List<APPIndexHotSaleDTO> hotSaleList = advertRoundList.stream().map(x -> {
                    StoreProdPriceAndMainPicAndTagDTO attrDto = attrMap.get(Long.parseLong(x.getProdIdStr()));
                    return new APPIndexHotSaleDTO().setAdvert(Boolean.TRUE).setStoreId(x.getStoreId().toString())
                            .setStoreProdId(x.getProdIdStr()).setTags(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getTags() : null)
                            .setStoreName(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getStoreName() : "")
                            .setProdPrice(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getMinPrice().toString() : null)
                            .setProdArtNum(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getProdArtNum() : "")
                            .setMainPic(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getMainPicUrl() : "");
                }).collect(Collectors.toList());
                // 放到redis中 有效期1天
                this.redisCache.setCacheObject(CacheConstants.APP_ADVERT + CacheConstants.APP_INDEX_HOT_SALE_ADVERT, hotSaleList, 1, TimeUnit.DAYS);
                // 添加了广告的数据
                return new Page<>(page.getPageNum(), page.getPageSize(), page.getPages(), page.getTotal(), insertAdvertsIntoList(realDataList, hotSaleList, Constants.insertPositions));
            }
        }
        return new Page<>(page.getPageNum(), page.getPageSize(), page.getPages(), page.getTotal(), realDataList);
    }

    /**
     * APP 首页 人气爆品列表
     *
     * @param searchDTO 搜索入参
     * @return Page<APPIndexPopularSaleDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public Page<APPIndexPopularSaleDTO> appIndexPopularSalePage(IndexSearchDTO searchDTO) throws IOException {
        Page<ESProductDTO> page = this.search(searchDTO);
        // 筛选出真实的数据
        List<APPIndexPopularSaleDTO> realDataList = page.getList().stream()
                .map(esProduct -> BeanUtil.toBean(esProduct, APPIndexPopularSaleDTO.class).setAdvert(Boolean.FALSE)).collect(Collectors.toList());
        // APP 只有第一页 有数据 其它页暂时没有广告
        if (searchDTO.getPageNum() > 1) {
            return new Page<>(page.getPageNum(), page.getPageSize(), page.getPages(), page.getTotal(), realDataList);
        }
        // 从redis中获取数据
        List<APPIndexPopularSaleDTO> redisList = this.redisCache.getCacheObject(CacheConstants.APP_ADVERT + CacheConstants.APP_INDEX_POPULAR_SALE_ADVERT);
        if (CollectionUtils.isNotEmpty(redisList)) {
            // 添加广告的数据
            return new Page<>(page.getPageNum(), page.getPageSize(), page.getPages(), page.getTotal(), insertAdvertsIntoList(realDataList, redisList, Constants.insertPositions));
        } else {
            // 从数据库查首页 人气爆品 推广（精准搜索是否存在推广，不存在从已过期的数据中拉数据来凑数）
            List<AdvertRound> advertRoundList = this.advertRoundMapper.selectList(new LambdaQueryWrapper<AdvertRound>()
                    .isNotNull(AdvertRound::getStoreId).eq(AdvertRound::getDelFlag, Constants.UNDELETED)
                    .eq(AdvertRound::getTypeId, AdType.APP_HOME_POP_RECOMMEND_PROD.getValue())
                    .eq(AdvertRound::getLaunchStatus, AdLaunchStatus.LAUNCHING.getValue()));
            if (CollectionUtils.isNotEmpty(advertRoundList)) {
                List<StoreProdPriceAndMainPicAndTagDTO> attrList = storeProdMapper.selectPriceAndMainPicAndTagList(advertRoundList.stream()
                        .map(x -> x.getProdIdStr()).map(Long::parseLong).collect(Collectors.toList()));
                attrList = attrList.stream().peek(x -> x.setTags(StringUtils.isNotBlank(x.getTagStr()) ? Arrays.asList(x.getTagStr().split(",")) : null)).collect(Collectors.toList());
                Map<Long, StoreProdPriceAndMainPicAndTagDTO> attrMap = attrList.stream().collect(Collectors.toMap(StoreProdPriceAndMainPicAndTagDTO::getStoreProdId, x -> x));
                List<APPIndexPopularSaleDTO> popularSaleList = advertRoundList.stream().map(x -> {
                    StoreProdPriceAndMainPicAndTagDTO attrDto = attrMap.get(Long.parseLong(x.getProdIdStr()));
                    return new APPIndexPopularSaleDTO().setAdvert(Boolean.TRUE).setStoreId(x.getStoreId().toString())
                            .setStoreProdId(x.getProdIdStr()).setTags(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getTags() : null)
                            .setStoreName(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getStoreName() : "")
                            .setProdPrice(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getMinPrice().toString() : null)
                            .setProdArtNum(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getProdArtNum() : "")
                            .setMainPic(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getMainPicUrl() : "");
                }).collect(Collectors.toList());
                // 放到redis中 有效期1天
                this.redisCache.setCacheObject(CacheConstants.APP_ADVERT + CacheConstants.APP_INDEX_POPULAR_SALE_ADVERT, popularSaleList, 1, TimeUnit.DAYS);
                // 添加了广告的数据
                return new Page<>(page.getPageNum(), page.getPageSize(), page.getPages(), page.getTotal(), insertAdvertsIntoList(realDataList, popularSaleList, Constants.insertPositions));
            }
        }
        return new Page<>(page.getPageNum(), page.getPageSize(), page.getPages(), page.getTotal(), realDataList);
    }

    /**
     * APP 首页 新品榜
     *
     * @param searchDTO 搜索入参
     * @return Page<APPIndexNewProdDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public Page<APPIndexNewProdDTO> appIndexNewProdPage(IndexSearchDTO searchDTO) throws IOException {
        Page<ESProductDTO> page = this.search(searchDTO);
        // 筛选出真实的数据
        List<APPIndexNewProdDTO> realDataList = page.getList().stream()
                .map(esProduct -> BeanUtil.toBean(esProduct, APPIndexNewProdDTO.class).setAdvert(Boolean.FALSE)).collect(Collectors.toList());
        // APP 只有第一页 有数据 其它页暂时没有广告
        if (searchDTO.getPageNum() > 1) {
            return new Page<>(page.getPageNum(), page.getPageSize(), page.getPages(), page.getTotal(), realDataList);
        }
        // 从redis中获取数据
        List<APPIndexNewProdDTO> redisList = this.redisCache.getCacheObject(CacheConstants.APP_ADVERT + CacheConstants.APP_INDEX_NEW_PROD);
        if (CollectionUtils.isNotEmpty(redisList)) {
            // 添加广告的数据
            return new Page<>(page.getPageNum(), page.getPageSize(), page.getPages(), page.getTotal(), insertAdvertsIntoList(realDataList, redisList, Constants.insertPositions));
        } else {
            // 从数据库查首页 新品榜 推广（精准搜索是否存在推广，不存在从已过期的数据中拉数据来凑数）
            List<AdvertRound> advertRoundList = this.advertRoundMapper.selectList(new LambdaQueryWrapper<AdvertRound>()
                    .isNotNull(AdvertRound::getStoreId).eq(AdvertRound::getDelFlag, Constants.UNDELETED)
                    .eq(AdvertRound::getTypeId, AdType.APP_HOME_NEW_PROD_RECOMMEND_PROD.getValue())
                    .eq(AdvertRound::getLaunchStatus, AdLaunchStatus.LAUNCHING.getValue()));
            if (CollectionUtils.isNotEmpty(advertRoundList)) {
                List<StoreProdPriceAndMainPicAndTagDTO> attrList = storeProdMapper.selectPriceAndMainPicAndTagList(advertRoundList.stream()
                        .map(x -> x.getProdIdStr()).map(Long::parseLong).collect(Collectors.toList()));
                attrList = attrList.stream().peek(x -> x.setTags(StringUtils.isNotBlank(x.getTagStr()) ? Arrays.asList(x.getTagStr().split(",")) : null)).collect(Collectors.toList());
                Map<Long, StoreProdPriceAndMainPicAndTagDTO> attrMap = attrList.stream().collect(Collectors.toMap(StoreProdPriceAndMainPicAndTagDTO::getStoreProdId, x -> x));
                List<APPIndexNewProdDTO> newProdList = advertRoundList.stream().map(x -> {
                    StoreProdPriceAndMainPicAndTagDTO attrDto = attrMap.get(Long.parseLong(x.getProdIdStr()));
                    return new APPIndexNewProdDTO().setAdvert(Boolean.TRUE).setStoreId(x.getStoreId().toString())
                            .setStoreProdId(x.getProdIdStr()).setTags(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getTags() : null)
                            .setStoreName(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getStoreName() : "")
                            .setProdPrice(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getMinPrice().toString() : null)
                            .setProdArtNum(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getProdArtNum() : "")
                            .setMainPic(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getMainPicUrl() : "");
                }).collect(Collectors.toList());
                // 放到redis中 有效期1天
                this.redisCache.setCacheObject(CacheConstants.APP_ADVERT + CacheConstants.APP_INDEX_NEW_PROD, newProdList, 1, TimeUnit.DAYS);
                // 添加了广告的数据
                return new Page<>(page.getPageNum(), page.getPageSize(), page.getPages(), page.getTotal(), insertAdvertsIntoList(realDataList, newProdList, Constants.insertPositions));
            }
        }
        return new Page<>(page.getPageNum(), page.getPageSize(), page.getPages(), page.getTotal(), realDataList);
    }

    /**
     * APP 搜索列表
     *
     * @param searchDTO 搜索入参
     * @return Page<APPSearchDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public Page<APPSearchDTO> appSearchPage(IndexSearchDTO searchDTO) throws IOException {
        Page<ESProductDTO> page = this.search(searchDTO);
        // 筛选出真实的数据
        List<APPSearchDTO> realDataList = page.getList().stream()
                .map(esProduct -> BeanUtil.toBean(esProduct, APPSearchDTO.class).setAdvert(Boolean.FALSE)).collect(Collectors.toList());
        // APP 只有第一页 有数据 其它页暂时没有广告
        if (searchDTO.getPageNum() > 1) {
            return new Page<>(page.getPageNum(), page.getPageSize(), page.getPages(), page.getTotal(), realDataList);
        }
        // 从redis中获取数据
        List<APPSearchDTO> redisList = this.redisCache.getCacheObject(CacheConstants.APP_ADVERT + CacheConstants.APP_SEARCH);
        if (CollectionUtils.isNotEmpty(redisList)) {
            // 添加广告的数据
            return new Page<>(page.getPageNum(), page.getPageSize(), page.getPages(), page.getTotal(), insertAdvertsIntoList(realDataList, redisList, Constants.insertPositions));
        } else {
            // 从数据库查首页 新品榜 推广（精准搜索是否存在推广，不存在从已过期的数据中拉数据来凑数）
            List<AdvertRound> advertRoundList = this.advertRoundMapper.selectList(new LambdaQueryWrapper<AdvertRound>()
                    .isNotNull(AdvertRound::getStoreId).eq(AdvertRound::getDelFlag, Constants.UNDELETED)
                    .eq(AdvertRound::getTypeId, AdType.APP_SEARCH_RESULT.getValue())
                    .eq(AdvertRound::getLaunchStatus, AdLaunchStatus.LAUNCHING.getValue()));
            if (CollectionUtils.isNotEmpty(advertRoundList)) {
                List<StoreProdPriceAndMainPicAndTagDTO> attrList = storeProdMapper.selectPriceAndMainPicAndTagList(advertRoundList.stream()
                        .map(x -> x.getProdIdStr()).map(Long::parseLong).collect(Collectors.toList()));
                attrList = attrList.stream().peek(x -> x.setTags(StringUtils.isNotBlank(x.getTagStr()) ? Arrays.asList(x.getTagStr().split(",")) : null)).collect(Collectors.toList());
                Map<Long, StoreProdPriceAndMainPicAndTagDTO> attrMap = attrList.stream().collect(Collectors.toMap(StoreProdPriceAndMainPicAndTagDTO::getStoreProdId, x -> x));
                List<APPSearchDTO> newProdList = advertRoundList.stream().map(x -> {
                    StoreProdPriceAndMainPicAndTagDTO attrDto = attrMap.get(Long.parseLong(x.getProdIdStr()));
                    return new APPSearchDTO().setAdvert(Boolean.TRUE).setStoreId(x.getStoreId().toString())
                            .setStoreProdId(x.getProdIdStr()).setTags(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getTags() : null)
                            .setStoreName(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getStoreName() : "")
                            .setProdPrice(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getMinPrice().toString() : null)
                            .setProdArtNum(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getProdArtNum() : "")
                            .setMainPic(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getMainPicUrl() : "");
                }).collect(Collectors.toList());
                // 放到redis中 有效期1天
                this.redisCache.setCacheObject(CacheConstants.APP_ADVERT + CacheConstants.APP_SEARCH, newProdList, 1, TimeUnit.DAYS);
                // 添加了广告的数据
                return new Page<>(page.getPageNum(), page.getPageSize(), page.getPages(), page.getTotal(), insertAdvertsIntoList(realDataList, newProdList, Constants.insertPositions));
            }
        }
        return new Page<>(page.getPageNum(), page.getPageSize(), page.getPages(), page.getTotal(), realDataList);
    }

    /**
     * PC 首页 为你推荐
     *
     * @param searchDTO 搜索入参
     * @return List<PCIndexRecommendProdDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PCIndexRecommendDTO> pcIndexRecommendPage(IndexSearchDTO searchDTO) throws IOException {
        Page<ESProductDTO> page = this.search(searchDTO);
        // 筛选出真实的数据
        List<PCIndexRecommendDTO> realDataList = page.getList().stream()
                .map(esProduct -> BeanUtil.toBean(esProduct, PCIndexRecommendDTO.class).setAdvert(Boolean.FALSE)).collect(Collectors.toList());
        // APP 只有第一页 有数据 其它页暂时没有广告
        if (searchDTO.getPageNum() > 1) {
            return new Page<>(page.getPageNum(), page.getPageSize(), page.getPages(), page.getTotal(), realDataList);
        }
        // 从redis中获取数据
        List<PCIndexRecommendDTO> redisList = this.redisCache.getCacheObject(CacheConstants.PC_ADVERT + CacheConstants.PC_INDEX_RECOMMEND);
        if (CollectionUtils.isNotEmpty(redisList)) {
            // 推广数据排在最前面，其次才是真实的数据
            CollectionUtils.addAll(redisList, realDataList);
            // 添加广告的数据（PC的规则是将所有的广告数据全部放到最前面展示，不用给广告打标）
            return new Page<>(page.getPageNum(), page.getPageSize(), page.getPages(), page.getTotal(), redisList);
        } else {
            // 从数据库查首页 推荐商品 推广（精准搜索是否存在推广，不存在从已过期的数据中拉数据来凑数）
            List<AdvertRound> advertRoundList = this.advertRoundMapper.selectList(new LambdaQueryWrapper<AdvertRound>()
                    .isNotNull(AdvertRound::getStoreId).eq(AdvertRound::getDelFlag, Constants.UNDELETED)
                    .eq(AdvertRound::getTypeId, AdType.PC_HOME_PRODUCT_LIST.getValue())
                    .eq(AdvertRound::getLaunchStatus, AdLaunchStatus.LAUNCHING.getValue()));
            if (CollectionUtils.isNotEmpty(advertRoundList)) {
                List<StoreProdPriceAndMainPicAndTagDTO> attrList = storeProdMapper.selectPriceAndMainPicAndTagList(advertRoundList.stream()
                        .map(x -> x.getProdIdStr().split(",")).flatMap(Arrays::stream).map(Long::valueOf).collect(Collectors.toList()));
                attrList = attrList.stream().peek(x -> x.setTags(StringUtils.isNotBlank(x.getTagStr()) ? Arrays.asList(x.getTagStr().split(",")) : null)).collect(Collectors.toList());
                Map<Long, StoreProdPriceAndMainPicAndTagDTO> attrMap = attrList.stream().collect(Collectors.toMap(StoreProdPriceAndMainPicAndTagDTO::getStoreProdId, x -> x));
                List<PCIndexRecommendDTO> indexRecommendList = new ArrayList<>();
                advertRoundList.stream().forEach(x -> {
                    // 这里是一个档口上传多个档口商品，所以需要对prodIdStr的逗号进行分割
                    List<Long> prodIdList = Arrays.asList(x.getProdIdStr().split(",")).stream().map(Long::parseLong).collect(Collectors.toList());
                    prodIdList.forEach(storeProdId -> {
                        StoreProdPriceAndMainPicAndTagDTO attrDto = attrMap.get(storeProdId);
                        indexRecommendList.add(new PCIndexRecommendDTO().setAdvert(Boolean.TRUE).setStoreId(x.getStoreId().toString())
                                .setStoreProdId(storeProdId.toString()).setTags(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getTags() : null)
                                .setStoreName(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getStoreName() : "")
                                .setProdPrice(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getMinPrice().toString() : null)
                                .setProdArtNum(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getProdArtNum() : "")
                                .setMainPic(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getMainPicUrl() : ""));
                    });
                });
                // 将indexRecommendList 顺序打乱，不然一个档口的数据在同一地方展示
                Collections.shuffle(indexRecommendList);
                // 放到redis中 有效期1天
                this.redisCache.setCacheObject(CacheConstants.PC_ADVERT + CacheConstants.PC_INDEX_RECOMMEND, indexRecommendList, 1, TimeUnit.DAYS);
                CollectionUtils.addAll(indexRecommendList, realDataList);
                // 添加了广告的数据
                return new Page<>(page.getPageNum(), page.getPageSize(), page.getPages(), page.getTotal(), indexRecommendList);
            }
        }
        return new Page<>(page.getPageNum(), page.getPageSize(), page.getPages(), page.getTotal(), realDataList);
    }

    /**
     * PC 新品馆 为你推荐
     *
     * @param searchDTO 搜索入参
     * @return Page<PCNewRecommendDTO>
     */
    @Override
    public Page<PCNewRecommendDTO> pcNewProdRecommendPage(IndexSearchDTO searchDTO) throws IOException {
        Page<ESProductDTO> page = this.search(searchDTO);
        // 筛选出真实的数据
        List<PCNewRecommendDTO> realDataList = page.getList().stream()
                .map(esProduct -> BeanUtil.toBean(esProduct, PCNewRecommendDTO.class).setAdvert(Boolean.FALSE)).collect(Collectors.toList());
        // APP 只有第一页 有数据 其它页暂时没有广告
        if (searchDTO.getPageNum() > 1) {
            return new Page<>(page.getPageNum(), page.getPageSize(), page.getPages(), page.getTotal(), realDataList);
        }
        // 从redis中获取数据
        List<PCNewRecommendDTO> redisList = this.redisCache.getCacheObject(CacheConstants.PC_ADVERT + CacheConstants.PC_NEW_RECOMMEND);
        if (CollectionUtils.isNotEmpty(redisList)) {
            // 推广数据排在最前面，其次才是真实的数据
            CollectionUtils.addAll(redisList, realDataList);
            // 添加广告的数据（PC的规则是将所有的广告数据全部放到最前面展示，不用给广告打标）
            return new Page<>(page.getPageNum(), page.getPageSize(), page.getPages(), page.getTotal(), redisList);
        } else {
            // 从数据库查新品馆 推荐商品 推广（精准搜索是否存在推广，不存在从已过期的数据中拉数据来凑数）
            List<AdvertRound> advertRoundList = this.advertRoundMapper.selectList(new LambdaQueryWrapper<AdvertRound>()
                    .isNotNull(AdvertRound::getStoreId).eq(AdvertRound::getDelFlag, Constants.UNDELETED)
                    .eq(AdvertRound::getTypeId, AdType.PC_NEW_PROD_PRODUCT_LIST.getValue())
                    .eq(AdvertRound::getLaunchStatus, AdLaunchStatus.LAUNCHING.getValue()));
            if (CollectionUtils.isNotEmpty(advertRoundList)) {
                List<StoreProdPriceAndMainPicAndTagDTO> attrList = storeProdMapper.selectPriceAndMainPicAndTagList(advertRoundList.stream()
                        .map(x -> x.getProdIdStr().split(",")).flatMap(Arrays::stream).map(Long::valueOf).collect(Collectors.toList()));
                attrList = attrList.stream().peek(x -> x.setTags(StringUtils.isNotBlank(x.getTagStr()) ? Arrays.asList(x.getTagStr().split(",")) : null)).collect(Collectors.toList());
                Map<Long, StoreProdPriceAndMainPicAndTagDTO> attrMap = attrList.stream().collect(Collectors.toMap(StoreProdPriceAndMainPicAndTagDTO::getStoreProdId, x -> x));
                List<PCNewRecommendDTO> newRecommendList = new ArrayList<>();
                advertRoundList.stream().forEach(x -> {
                    // 这里是一个档口上传多个档口商品，所以需要对prodIdStr的逗号进行分割
                    List<Long> prodIdList = Arrays.asList(x.getProdIdStr().split(",")).stream().map(Long::parseLong).collect(Collectors.toList());
                    prodIdList.forEach(storeProdId -> {
                        StoreProdPriceAndMainPicAndTagDTO attrDto = attrMap.get(storeProdId);
                        newRecommendList.add(new PCNewRecommendDTO().setAdvert(Boolean.TRUE).setStoreId(x.getStoreId().toString())
                                .setStoreProdId(storeProdId.toString()).setTags(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getTags() : null)
                                .setStoreName(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getStoreName() : "")
                                .setProdPrice(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getMinPrice().toString() : null)
                                .setProdArtNum(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getProdArtNum() : "")
                                .setMainPic(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getMainPicUrl() : ""));
                    });
                });
                // newRecommendList 顺序打乱，不然一个档口的数据在同一地方展示
                Collections.shuffle(newRecommendList);
                // 放到redis中 有效期1天
                this.redisCache.setCacheObject(CacheConstants.PC_ADVERT + CacheConstants.PC_NEW_RECOMMEND, newRecommendList, 1, TimeUnit.DAYS);
                CollectionUtils.addAll(newRecommendList, realDataList);
                // 添加了广告的数据
                return new Page<>(page.getPageNum(), page.getPageSize(), page.getPages(), page.getTotal(), newRecommendList);
            }
        }
        return new Page<>(page.getPageNum(), page.getPageSize(), page.getPages(), page.getTotal(), realDataList);
    }

    /**
     * PC 搜索结果
     *
     * @param searchDTO 搜索入参
     * @return Page<PCSearchDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PCSearchDTO> psSearchPage(IndexSearchDTO searchDTO) throws IOException {
        Page<ESProductDTO> page = this.search(searchDTO);
        // 筛选出真实的数据
        List<PCSearchDTO> realDataList = page.getList().stream()
                .map(esProduct -> BeanUtil.toBean(esProduct, PCSearchDTO.class).setAdvert(Boolean.FALSE)).collect(Collectors.toList());
        // 暂时没有广告
        return new Page<>(page.getPageNum(), page.getPageSize(), page.getPages(), page.getTotal(), realDataList);
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
                        .query(query).from((searchDTO.getPageNum() - 1) * searchDTO.getPageSize()).size(searchDTO.getPageSize())
                        .sort(sort -> sort.field(f -> f.field(searchDTO.getSort()).order(SortOrder.Desc))),
                ESProductDTO.class);
        final long total = resList.hits().total().value();
        final List<ESProductDTO> esProdList = resList.hits().hits().stream().map(x -> x.source().setStoreProdId(x.id())).collect(Collectors.toList());
        return new Page<>(searchDTO.getPageNum(), searchDTO.getPageSize(), total / searchDTO.getPageSize() + 1, total, esProdList);
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
        List<StoreProdPriceAndMainPicDTO> prodPriceAndMainPicList = this.storeProdMapper.selectPriceAndMainPicList(cateSaleList.stream()
                .map(CateSaleRankDTO::getStoreProdId).collect(Collectors.toList()));
        // 档口商品的价格及商品主图map
        Map<Long, StoreProdPriceAndMainPicDTO> prodPriceAndMainPicMap = prodPriceAndMainPicList.stream().collect(Collectors
                .toMap(StoreProdPriceAndMainPicDTO::getStoreProdId, Function.identity()));
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
                        .setMainPicUrl(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(dto.getStoreProdId()))
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
        Map<Long, StoreProdPriceAndMainPicDTO> prodPriceAndMainPicMap = prodPriceAndMainPicList.stream().collect(Collectors
                .toMap(StoreProdPriceAndMainPicDTO::getStoreProdId, Function.identity()));
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
        Map<Long, StoreProdPriceAndMainPicDTO> prodPriceAndMainPicMap = prodPriceAndMainPicList.stream().collect(Collectors
                .toMap(StoreProdPriceAndMainPicDTO::getStoreProdId, Function.identity()));

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
                        .setMainPicUrl(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getMainPicUrl() : "");
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
     * PC 首页 固定挂耳
     *
     * @return PCIndexFixedEarDTO
     */
    @Override
    @Transactional(readOnly = true)
    public PCIndexFixedEarDTO getPcIndexFixedEar() {
        PCIndexFixedEarDTO fixedEarDTO = new PCIndexFixedEarDTO();
        // 从redis 中获取 PC 首页 两侧固定挂耳
        PCIndexFixedEarDTO redisFixedEar = redisCache.getCacheObject(CacheConstants.PC_ADVERT + CacheConstants.PC_ADVERT_INDEX_FIXED_EAR);
        if (ObjectUtils.isNotEmpty(redisFixedEar)) {
            return redisFixedEar;
        }
        List<AdvertRound> oneMonthList = this.getOneMonthAdvertList(Collections.singletonList(AdType.PC_HOME_FIXED_EAR.getValue()));
        if (CollectionUtils.isEmpty(oneMonthList)) {
            return fixedEarDTO;
        }
        // 文件map
        Map<Long, SysFile> fileMap = fileMapper.selectList(new LambdaQueryWrapper<SysFile>().eq(SysFile::getDelFlag, Constants.UNDELETED)
                        .in(SysFile::getId, oneMonthList.stream().map(AdvertRound::getPicId).filter(ObjectUtils::isNotEmpty).collect(Collectors.toList())))
                .stream().collect(Collectors.toMap(SysFile::getId, Function.identity()));
        // 正在播放
        List<AdvertRound> launchingList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.LAUNCHING.getValue())).collect(Collectors.toList());
        // 已过期
        List<AdvertRound> expiredList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.EXPIRED.getValue())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(launchingList)) {
            // 从 expiredList 中任意选取一条数据
            fixedEarDTO = expiredList.stream().map(x -> new PCIndexFixedEarDTO().setDisplayType(AdDisplayType.PICTURE.getValue())
                            .setStoreId(x.getStoreId()).setOrderNum(1)
                            .setFileUrl(ObjectUtils.isNotEmpty(x.getPicId()) ? fileMap.get(x.getPicId()).getFileUrl() : null))
                    .findAny().orElse(null);
        } else {
            AdvertRound advertRound = launchingList.get(0);
            fixedEarDTO = new PCIndexFixedEarDTO().setDisplayType(AdDisplayType.PICTURE.getValue())
                    .setStoreId(advertRound.getStoreId()).setOrderNum(1)
                    .setFileUrl(ObjectUtils.isNotEmpty(advertRound.getPicId()) ? fileMap.get(advertRound.getPicId()).getFileUrl() : null);
        }
        // 存放到redis中，过期时间为1天
        redisCache.setCacheObject(CacheConstants.PC_ADVERT + CacheConstants.PC_ADVERT_INDEX_FIXED_EAR, fixedEarDTO, 1, TimeUnit.DAYS);
        return fixedEarDTO;
    }

    /**
     * 获取搜索框下档口名称
     *
     * @return List<PCIndexSearchUnderlineStoreNameDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public List<PCIndexSearchUnderlineStoreNameDTO> getPcIndexSearchUnderlineStoreName() {
        List<PCIndexSearchUnderlineStoreNameDTO> searchStoreNameList;
        // 从redis 中获取 PC 首页 搜索框下档口名称
        List<PCIndexSearchUnderlineStoreNameDTO> redisSearchStoreNameList = redisCache.getCacheObject(CacheConstants.PC_ADVERT + CacheConstants.PC_ADVERT_INDEX_SEARCH_UNDERLINE_STORE_NAME);
        if (ObjectUtils.isNotEmpty(redisSearchStoreNameList)) {
            return redisSearchStoreNameList;
        }
        List<AdvertRound> oneMonthList = this.getOneMonthAdvertList(Collections.singletonList(AdType.PC_HOME_SEARCH_DOWN_NAME.getValue()));
        if (CollectionUtils.isEmpty(oneMonthList)) {
            return new ArrayList<>();
        }
        Map<Long, Store> storeMap = this.storeMapper.selectList(new LambdaQueryWrapper<Store>().eq(Store::getDelFlag, Constants.UNDELETED)
                        .eq(Store::getId, oneMonthList.stream().map(AdvertRound::getStoreId).collect(Collectors.toList())))
                .stream().collect(Collectors.toMap(Store::getId, Function.identity()));
        // 正在播放
        List<AdvertRound> launchingList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.LAUNCHING.getValue())).collect(Collectors.toList());
        // 已过期
        List<AdvertRound> expiredList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.EXPIRED.getValue())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(launchingList)) {
            // 随机选择10个档口
            searchStoreNameList = expiredList.stream().map(AdvertRound::getStoreId).distinct().limit(10)
                    .map(x -> new PCIndexSearchUnderlineStoreNameDTO()
                            .setStoreId(x).setDisplayType(AdDisplayType.STORE_NAME.getValue()).setOrderNum(1)
                            .setStoreName(ObjectUtils.isNotEmpty(storeMap.get(x)) ? storeMap.get(x).getStoreName() : ""))
                    .collect(Collectors.toList());
        } else {
            searchStoreNameList = launchingList.stream().map(AdvertRound::getStoreId).map(x -> new PCIndexSearchUnderlineStoreNameDTO()
                            .setStoreId(x).setDisplayType(AdDisplayType.STORE_NAME.getValue()).setOrderNum(1)
                            .setStoreName(ObjectUtils.isNotEmpty(storeMap.get(x)) ? storeMap.get(x).getStoreName() : ""))
                    .collect(Collectors.toList());
        }
        // 放到redis中，过期时间为1天
        redisCache.setCacheObject(CacheConstants.PC_ADVERT + CacheConstants.PC_ADVERT_INDEX_SEARCH_UNDERLINE_STORE_NAME, searchStoreNameList, 1, TimeUnit.DAYS);
        return searchStoreNameList;
    }

    /**
     * 搜索框中推荐商品
     *
     * @return List<PCIndexSearchRecommendProdDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public List<PCIndexSearchRecommendProdDTO> getPcIndexSearchRecommendProd() {
        List<PCIndexSearchRecommendProdDTO> recommendList;
        // 从redis中获取PC 搜索框中推荐商品
        List<PCIndexSearchRecommendProdDTO> redisRecmmendList = redisCache.getCacheObject(CacheConstants.PC_ADVERT + CacheConstants.PC_ADVERT_INDEX_SEARCH_RECOMMEND_PROD);
        if (CollectionUtils.isNotEmpty(redisRecmmendList)) {
            return redisRecmmendList;
        }
        List<AdvertRound> oneMonthList = this.getOneMonthAdvertList(Collections.singletonList(AdType.PC_HOME_SEARCH_PRODUCT.getValue()));
        if (CollectionUtils.isEmpty(oneMonthList)) {
            return new ArrayList<>();
        }
        List<StoreProdPriceAndMainPicDTO> prodPriceAndMainPicList = this.storeProdMapper.selectPriceAndMainPicList(oneMonthList.stream()
                .map(AdvertRound::getProdIdStr).map(Long::parseLong).collect(Collectors.toList()));
        // 档口商品的价格及商品主图map
        Map<Long, StoreProdPriceAndMainPicDTO> prodPriceAndMainPicMap = prodPriceAndMainPicList.stream().collect(Collectors
                .toMap(StoreProdPriceAndMainPicDTO::getStoreProdId, Function.identity()));
        // 正在播放
        List<AdvertRound> launchingList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.LAUNCHING.getValue())).collect(Collectors.toList());
        // 已过期
        List<AdvertRound> expiredList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.EXPIRED.getValue())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(launchingList)) {
            List<PCIndexSearchRecommendProdDTO> tempList = new ArrayList<>();
            // 从已过期商品中随机选择5个商品
            expiredList.stream().collect(Collectors.groupingBy(AdvertRound::getStoreId))
                    .forEach((storeId, list) -> {
                        AdvertRound advertRound = list.get(0);
                        final Long storeProdId = Long.parseLong(advertRound.getProdIdStr());
                        tempList.add(new PCIndexSearchRecommendProdDTO().setDisplayType(AdDisplayType.PRODUCT.getValue()).setStoreProdId(storeProdId)
                                .setProdArtNum(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getProdArtNum() : "")
                                .setMainPicUrl(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getMainPicUrl() : ""));
                    });
            recommendList = tempList.stream().limit(5).collect(Collectors.toList());
            for (int i = 0; i < recommendList.size(); i++) {
                recommendList.get(i).setOrderNum(i + 1);
            }
        } else {
            recommendList = launchingList.stream().map(advertRound -> {
                final Long storeProdId = Long.parseLong(advertRound.getProdIdStr());
                return new PCIndexSearchRecommendProdDTO().setDisplayType(AdDisplayType.PRODUCT.getValue()).setStoreProdId(storeProdId)
                        .setProdArtNum(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getProdArtNum() : "")
                        .setMainPicUrl(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getMainPicUrl() : "");
            }).collect(Collectors.toList());
        }
        // 放到redis中，过期时间为1天
        redisCache.setCacheObject(CacheConstants.PC_ADVERT + CacheConstants.PC_ADVERT_INDEX_SEARCH_RECOMMEND_PROD, recommendList, 1, TimeUnit.DAYS);
        return recommendList;
    }

    /**
     * PC 新品馆 顶部左侧 轮播图
     *
     * @return List<PCNewTopLeftBannerDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public List<PCNewTopLeftBannerDTO> getPcNewTopLeftBanner() {
        List<PCNewTopLeftBannerDTO> newTopLeftList;
        // 从redis 中获取 PC 首页 人气榜数据
        List<PCNewTopLeftBannerDTO> redisList = redisCache.getCacheObject(CacheConstants.PC_ADVERT + CacheConstants.PC_ADVERT_NEW_TOP_LEFT);
        if (ObjectUtils.isNotEmpty(redisList)) {
            return redisList;
        }
        List<AdvertRound> oneMonthList = this.getOneMonthAdvertList(Collections.singletonList(AdType.PC_NEW_PROD_TOP_LEFT_BANNER.getValue()));
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
        if (CollectionUtils.isEmpty(launchingList)) {
            List<PCNewTopLeftBannerDTO> tempList = new ArrayList<>();
            expiredList.stream().collect(Collectors.groupingBy(AdvertRound::getStoreId))
                    .forEach((storeId, list) -> {
                        AdvertRound advertRound = list.get(0);
                        tempList.add(new PCNewTopLeftBannerDTO().setDisplayType(AdDisplayType.PICTURE.getValue()).setStoreId(advertRound.getStoreId())
                                .setFileUrl(ObjectUtils.isNotEmpty(fileMap.get(advertRound.getPicId())) ? fileMap.get(advertRound.getPicId()).getFileUrl() : ""));
                    });
            newTopLeftList = tempList.stream().limit(5).collect(Collectors.toList());
            for (int i = 0; i < newTopLeftList.size(); i++) {
                newTopLeftList.get(i).setOrderNum(i + 1);
            }
        } else {
            newTopLeftList = launchingList.stream().map(advertRound -> new PCNewTopLeftBannerDTO().setDisplayType(AdDisplayType.PICTURE.getValue())
                            .setStoreId(advertRound.getStoreId()).setOrderNum(this.positionToNumber(advertRound.getPosition()))
                            .setFileUrl(ObjectUtils.isNotEmpty(fileMap.get(advertRound.getPicId())) ? fileMap.get(advertRound.getPicId()).getFileUrl() : ""))
                    .collect(Collectors.toList());
        }
        // 放到redis中，过期时间为1天
        redisCache.setCacheObject(CacheConstants.PC_ADVERT + CacheConstants.PC_ADVERT_NEW_TOP_LEFT, newTopLeftList, 1, TimeUnit.DAYS);
        return newTopLeftList;
    }

    /**
     * PC 新品馆 顶部右侧 轮播图
     *
     * @return PCNewTopRightDTO
     */
    @Override
    @Transactional(readOnly = true)
    public PCNewTopRightDTO getPcNewTopRight() {
        // 从redis 中获取 PC 首页 人气榜数据
        PCNewTopRightDTO newTopRight;
        PCNewTopRightDTO redisNewTopRight = redisCache.getCacheObject(CacheConstants.PC_ADVERT + CacheConstants.PC_ADVERT_NEW_TOP_RIGHT);
        if (ObjectUtils.isNotEmpty(redisNewTopRight)) {
            return redisNewTopRight;
        }
        List<AdvertRound> oneMonthList = this.getOneMonthAdvertList(Collections.singletonList(AdType.PC_NEW_PROD_TOP_RIGHT.getValue()));
        if (CollectionUtils.isEmpty(oneMonthList)) {
            return new PCNewTopRightDTO();
        }
        // 文件map
        Map<Long, SysFile> fileMap = fileMapper.selectList(new LambdaQueryWrapper<SysFile>().eq(SysFile::getDelFlag, Constants.UNDELETED)
                        .in(SysFile::getId, oneMonthList.stream().map(AdvertRound::getPicId).filter(ObjectUtils::isNotEmpty).collect(Collectors.toList())))
                .stream().collect(Collectors.toMap(SysFile::getId, Function.identity()));
        // 正在播放
        List<AdvertRound> launchingList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.LAUNCHING.getValue())).collect(Collectors.toList());
        // 已过期
        List<AdvertRound> expiredList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.EXPIRED.getValue())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(launchingList)) {
            // 从expiredList中任意取一条数据
            newTopRight = expiredList.stream().map(x -> new PCNewTopRightDTO().setDisplayType(AdDisplayType.PICTURE.getValue()).setOrderNum(1).setStoreId(x.getStoreId())
                    .setFileUrl(ObjectUtils.isNotEmpty(fileMap.get(x.getPicId())) ? fileMap.get(x.getPicId()).getFileUrl() : "")).findAny().orElse(null);
        } else {
            newTopRight = launchingList.stream().map(x -> new PCNewTopRightDTO().setDisplayType(AdDisplayType.PICTURE.getValue()).setOrderNum(1).setStoreId(x.getStoreId())
                            .setFileUrl(ObjectUtils.isNotEmpty(fileMap.get(x.getPicId())) ? fileMap.get(x.getPicId()).getFileUrl() : ""))
                    .findAny().orElse(null);
        }
        // 放到redis中，过期时间为1天
        redisCache.setCacheObject(CacheConstants.PC_ADVERT + CacheConstants.PC_ADVERT_NEW_TOP_RIGHT, newTopRight, 1, TimeUnit.DAYS);
        return newTopRight;
    }

    /**
     * PC 新品馆 品牌馆
     *
     * @return List<PCNewMidBrandDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public List<PCNewMidBrandDTO> getPcNewMidBrandList() {
        // 从redis中获取 新品馆 品牌榜数据
        List<PCNewMidBrandDTO> newMidBrandList;
        List<PCNewMidBrandDTO> redisList = redisCache.getCacheObject(CacheConstants.PC_ADVERT + CacheConstants.PC_ADVERT_NEW_MID_BRAND);
        if (CollectionUtils.isNotEmpty(redisList)) {
            return redisList;
        }
        List<AdvertRound> oneMonthList = this.getOneMonthAdvertList(Collections.singletonList(AdType.PC_NEW_PROD_BRAND_BANNER.getValue()));
        if (CollectionUtils.isEmpty(oneMonthList)) {
            return new ArrayList<>();
        }
        Map<Long, Store> storeMap = this.storeMapper.selectList(new LambdaQueryWrapper<Store>().eq(Store::getDelFlag, Constants.UNDELETED)
                        .eq(Store::getId, oneMonthList.stream().map(AdvertRound::getStoreId).collect(Collectors.toList())))
                .stream().collect(Collectors.toMap(Store::getId, Function.identity()));
        // 文件map
        Map<Long, SysFile> fileMap = fileMapper.selectList(new LambdaQueryWrapper<SysFile>().eq(SysFile::getDelFlag, Constants.UNDELETED)
                        .in(SysFile::getId, oneMonthList.stream().map(AdvertRound::getPicId).filter(ObjectUtils::isNotEmpty).collect(Collectors.toList())))
                .stream().collect(Collectors.toMap(SysFile::getId, Function.identity()));
        // 正在播放
        List<AdvertRound> launchingList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.LAUNCHING.getValue())).collect(Collectors.toList());
        // 已过期
        List<AdvertRound> expiredList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.EXPIRED.getValue())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(launchingList)) {
            List<PCNewMidBrandDTO> tempList = new ArrayList<>();
            expiredList.stream().collect(Collectors.groupingBy(AdvertRound::getStoreId))
                    .forEach((storeId, list) -> {
                        AdvertRound advertRound = list.get(0);
                        tempList.add(new PCNewMidBrandDTO().setDisplayType(AdDisplayType.PICTURE.getValue()).setStoreId(storeId).setStoreName(storeMap.get(storeId).getStoreName())
                                .setFileUrl(ObjectUtils.isNotEmpty(fileMap.get(advertRound.getPicId())) ? fileMap.get(advertRound.getPicId()).getFileUrl() : ""));
                    });
            newMidBrandList = tempList.stream().limit(10).collect(Collectors.toList());
            for (int i = 0; i < newMidBrandList.size(); i++) {
                newMidBrandList.get(i).setOrderNum(i + 1);
            }
        } else {
            newMidBrandList = launchingList.stream().map(x -> new PCNewMidBrandDTO().setDisplayType(AdDisplayType.PICTURE.getValue())
                            .setOrderNum(this.positionToNumber(x.getPosition())).setStoreId(x.getStoreId()).setStoreName(storeMap.get(x.getStoreId()).getStoreName())
                            .setFileUrl(ObjectUtils.isNotEmpty(fileMap.get(x.getPicId())) ? fileMap.get(x.getPicId()).getFileUrl() : ""))
                    .collect(Collectors.toList());
        }
        // 放到redis中，过期时间为1天
        redisCache.setCacheObject(CacheConstants.PC_ADVERT + CacheConstants.PC_ADVERT_NEW_MID_BRAND, newMidBrandList, 1, TimeUnit.DAYS);
        return newMidBrandList;
    }

    /**
     * PC 新品馆 热卖榜左侧
     *
     * @return List<PCNewMidHotLeftDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public List<PCNewMidHotLeftDTO> getPcNewMidHotLeftList() {
        // 从redis中获取 新品馆 热卖榜左侧数据
        List<PCNewMidHotLeftDTO> newMidHotLeftList;
        List<PCNewMidHotLeftDTO> redisList = redisCache.getCacheObject(CacheConstants.PC_ADVERT + CacheConstants.PC_ADVERT_NEW_MID_HOT_LEFT);
        if (CollectionUtils.isNotEmpty(redisList)) {
            return redisList;
        }
        List<AdvertRound> oneMonthList = this.getOneMonthAdvertList(Collections.singletonList(AdType.PC_NEW_PROD_HOT_LEFT_BANNER.getValue()));
        if (CollectionUtils.isEmpty(oneMonthList)) {
            return new ArrayList<>();
        }
        Map<Long, SysFile> fileMap = fileMapper.selectList(new LambdaQueryWrapper<SysFile>().eq(SysFile::getDelFlag, Constants.UNDELETED)
                        .in(SysFile::getId, oneMonthList.stream().map(AdvertRound::getPicId).filter(ObjectUtils::isNotEmpty).collect(Collectors.toList())))
                .stream().collect(Collectors.toMap(SysFile::getId, Function.identity()));
        // 正在播放
        List<AdvertRound> launchingList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.LAUNCHING.getValue())).collect(Collectors.toList());
        // 已过期
        List<AdvertRound> expiredList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.EXPIRED.getValue())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(launchingList)) {
            List<PCNewMidHotLeftDTO> tempList = new ArrayList<>();
            expiredList.stream().collect(Collectors.groupingBy(AdvertRound::getStoreId))
                    .forEach((storeId, list) -> {
                        AdvertRound advertRound = list.get(0);
                        tempList.add(new PCNewMidHotLeftDTO().setDisplayType(AdDisplayType.PICTURE.getValue()).setStoreId(storeId)
                                .setFileUrl(ObjectUtils.isNotEmpty(fileMap.get(advertRound.getPicId())) ? fileMap.get(advertRound.getPicId()).getFileUrl() : ""));
                    });
            newMidHotLeftList = tempList.stream().limit(5).collect(Collectors.toList());
            for (int i = 0; i < newMidHotLeftList.size(); i++) {
                newMidHotLeftList.get(i).setOrderNum(i + 1);
            }
        } else {
            newMidHotLeftList = launchingList.stream().map(x -> new PCNewMidHotLeftDTO().setDisplayType(AdDisplayType.PICTURE.getValue())
                            .setOrderNum(this.positionToNumber(x.getPosition())).setStoreId(x.getStoreId())
                            .setFileUrl(ObjectUtils.isNotEmpty(fileMap.get(x.getPicId())) ? fileMap.get(x.getPicId()).getFileUrl() : ""))
                    .collect(Collectors.toList());
        }
        // 放到redis中，过期时间为1天
        redisCache.setCacheObject(CacheConstants.PC_ADVERT + CacheConstants.PC_ADVERT_NEW_MID_HOT_LEFT, newMidHotLeftList, 1, TimeUnit.DAYS);
        return newMidHotLeftList;
    }

    /**
     * PC 新品馆 热卖榜右侧商品
     *
     * @return List<PCNewMidHotRightDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public List<PCNewMidHotRightDTO> getPcNewMidHotRightList() {
        // 从redis中获取 新品馆 热卖榜右侧数据
        List<PCNewMidHotRightDTO> newMidHotRightList;
        List<PCNewMidHotRightDTO> redisList = redisCache.getCacheObject(CacheConstants.PC_ADVERT + CacheConstants.PC_ADVERT_NEW_MID_HOT_RIGHT);
        if (CollectionUtils.isNotEmpty(redisList)) {
            return redisList;
        }
        List<AdvertRound> oneMonthList = this.getOneMonthAdvertList(Collections.singletonList(AdType.PC_NEW_PROD_HOT_RIGHT_PRODUCT.getValue()));
        if (CollectionUtils.isEmpty(oneMonthList)) {
            return new ArrayList<>();
        }
        // 正在播放
        List<AdvertRound> launchingList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.LAUNCHING.getValue())).collect(Collectors.toList());
        // 已过期
        List<AdvertRound> expiredList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.EXPIRED.getValue())).collect(Collectors.toList());
        List<StoreProdPriceAndMainPicDTO> prodPriceAndMainPicList = this.storeProdMapper.selectPriceAndMainPicList(oneMonthList.stream()
                .filter(x -> StringUtils.isNotBlank(x.getProdIdStr())).map(x -> Long.parseLong(x.getProdIdStr())).collect(Collectors.toList()));
        // 档口商品的价格及商品主图map
        Map<Long, StoreProdPriceAndMainPicDTO> prodPriceAndMainPicMap = prodPriceAndMainPicList.stream().collect(Collectors
                .toMap(StoreProdPriceAndMainPicDTO::getStoreProdId, Function.identity()));
        if (CollectionUtils.isEmpty(launchingList)) {
            List<PCNewMidHotRightDTO> tempList = new ArrayList<>();
            expiredList.stream().collect(Collectors.groupingBy(AdvertRound::getStoreId))
                    .forEach((storeId, list) -> {
                        AdvertRound advertRound = list.get(0);
                        final Long storeProdId = Long.parseLong(advertRound.getProdIdStr());
                        tempList.add(new PCNewMidHotRightDTO().setDisplayType(AdDisplayType.PRODUCT.getValue()).setStoreProdId(storeProdId)
                                .setPrice(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getMinPrice() : null)
                                .setProdArtNum(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getProdArtNum() : "")
                                .setMainPicUrl(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getMainPicUrl() : ""));
                    });
            newMidHotRightList = tempList.stream().limit(8).collect(Collectors.toList());
            for (int i = 0; i < newMidHotRightList.size(); i++) {
                newMidHotRightList.get(i).setOrderNum(i + 1);
            }
        } else {
            newMidHotRightList = launchingList.stream().map(x -> {
                final Long storeProdId = Long.parseLong(x.getProdIdStr());
                return new PCNewMidHotRightDTO().setDisplayType(AdDisplayType.PRODUCT.getValue())
                        .setOrderNum(this.positionToNumber(x.getPosition())).setStoreProdId(storeProdId)
                        .setPrice(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getMinPrice() : null)
                        .setProdArtNum(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getProdArtNum() : "")
                        .setMainPicUrl(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getMainPicUrl() : "");
            }).collect(Collectors.toList());
        }
        // 放到redis中，过期时间为1天
        redisCache.setCacheObject(CacheConstants.PC_ADVERT + CacheConstants.PC_ADVERT_NEW_MID_HOT_RIGHT, newMidHotRightList, 1, TimeUnit.DAYS);
        return newMidHotRightList;
    }

    /**
     * PC 新品馆 底部横幅
     *
     * @return List<PCNewBottomBannerDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public List<PCNewBottomBannerDTO> getPcNewBottomBannerList() {
        // 从redis中获取 新品馆 底部横幅数据
        List<PCNewBottomBannerDTO> newBottomBannerList;
        List<PCNewBottomBannerDTO> redisList = redisCache.getCacheObject(CacheConstants.PC_ADVERT + CacheConstants.PC_ADVERT_NEW_BOTTOM_BANNER);
        if (CollectionUtils.isNotEmpty(redisList)) {
            return redisList;
        }
        List<AdvertRound> oneMonthList = this.getOneMonthAdvertList(Collections.singletonList(AdType.PC_NEW_PROD_SINGLE_BANNER.getValue()));
        if (CollectionUtils.isEmpty(oneMonthList)) {
            return new ArrayList<>();
        }
        // 正在播放
        List<AdvertRound> launchingList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.LAUNCHING.getValue())).collect(Collectors.toList());
        // 已过期
        List<AdvertRound> expiredList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.EXPIRED.getValue())).collect(Collectors.toList());
        // 文件map
        Map<Long, SysFile> fileMap = fileMapper.selectList(new LambdaQueryWrapper<SysFile>().eq(SysFile::getDelFlag, Constants.UNDELETED)
                        .in(SysFile::getId, oneMonthList.stream().map(AdvertRound::getPicId).filter(ObjectUtils::isNotEmpty).collect(Collectors.toList())))
                .stream().collect(Collectors.toMap(SysFile::getId, Function.identity()));
        if (CollectionUtils.isEmpty(launchingList)) {
            List<PCNewBottomBannerDTO> tempList = new ArrayList<>();
            expiredList.stream().collect(Collectors.groupingBy(AdvertRound::getStoreId))
                    .forEach((storeId, list) -> {
                        AdvertRound advertRound = list.get(0);
                        tempList.add(new PCNewBottomBannerDTO().setDisplayType(AdDisplayType.PICTURE.getValue()).setStoreId(storeId)
                                .setFileUrl(ObjectUtils.isNotEmpty(fileMap.get(advertRound.getPicId())) ? fileMap.get(advertRound.getPicId()).getFileUrl() : ""));
                    });
            newBottomBannerList = tempList.stream().limit(1).collect(Collectors.toList());
            for (int i = 0; i < newBottomBannerList.size(); i++) {
                newBottomBannerList.get(i).setOrderNum(i + 1);
            }
        } else {
            newBottomBannerList = launchingList.stream().map(x -> new PCNewBottomBannerDTO().setDisplayType(AdDisplayType.PICTURE.getValue())
                            .setOrderNum(this.positionToNumber(x.getPosition())).setStoreId(x.getStoreId())
                            .setFileUrl(ObjectUtils.isNotEmpty(fileMap.get(x.getPicId())) ? fileMap.get(x.getPicId()).getFileUrl() : ""))
                    .collect(Collectors.toList());
        }
        // 放到redis中，过期时间为1天
        redisCache.setCacheObject(CacheConstants.PC_ADVERT + CacheConstants.PC_ADVERT_NEW_BOTTOM_BANNER, newBottomBannerList, 1, TimeUnit.DAYS);
        return newBottomBannerList;
    }

    /**
     * PC 档口馆 顶部横幅及商品
     *
     * @return List<PCStoreTopBannerDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public List<PCStoreTopBannerDTO> getPcStoreTopBannerList() {
        // 从redis中获取 档口馆 顶部横幅及商品数据
        List<PCStoreTopBannerDTO> pcStoreTopBannerList;
        List<PCStoreTopBannerDTO> redisList = redisCache.getCacheObject(CacheConstants.PC_ADVERT + CacheConstants.PC_ADVERT_STORE_TOP_BANNER);
        if (CollectionUtils.isNotEmpty(redisList)) {
            return redisList;
        }
        List<AdvertRound> oneMonthList = this.getOneMonthAdvertList(Collections.singletonList(AdType.PC_STORE_TOP_BANNER.getValue()));
        if (CollectionUtils.isEmpty(oneMonthList)) {
            return new ArrayList<>();
        }
        List<AdvertRound> launchingList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.LAUNCHING.getValue())).collect(Collectors.toList());
        List<AdvertRound> expiredList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.EXPIRED.getValue())).collect(Collectors.toList());
        Map<Long, SysFile> fileMap = fileMapper.selectList(new LambdaQueryWrapper<SysFile>().eq(SysFile::getDelFlag, Constants.UNDELETED)
                        .in(SysFile::getId, oneMonthList.stream().map(AdvertRound::getPicId).filter(ObjectUtils::isNotEmpty).collect(Collectors.toList())))
                .stream().collect(Collectors.toMap(SysFile::getId, Function.identity()));
        List<Long> storeProdIdList = oneMonthList.stream().flatMap(x -> Arrays.stream(x.getProdIdStr().split(",")).map(Long::parseLong)).collect(Collectors.toList());
        List<StoreProdPriceAndMainPicDTO> prodPriceAndMainPicList = this.storeProdMapper.selectPriceAndMainPicList(storeProdIdList);
        // 档口商品的价格及商品主图map
        Map<Long, StoreProdPriceAndMainPicDTO> prodPriceAndMainPicMap = prodPriceAndMainPicList.stream().collect(Collectors
                .toMap(StoreProdPriceAndMainPicDTO::getStoreProdId, Function.identity()));
        if (CollectionUtils.isEmpty(launchingList)) {
            List<PCStoreTopBannerDTO> tempList = new ArrayList<>();
            expiredList.stream().collect(Collectors.groupingBy(AdvertRound::getStoreId))
                    .forEach((storeId, list) -> {
                        AdvertRound advertRound = list.get(0);
                        tempList.add(this.getStoreTopBanner(advertRound, fileMap, prodPriceAndMainPicMap));
                    });
            pcStoreTopBannerList = tempList.stream().limit(1).collect(Collectors.toList());
            for (int i = 0; i < pcStoreTopBannerList.size(); i++) {
                pcStoreTopBannerList.get(i).setOrderNum(i + 1);
            }
        } else {
            pcStoreTopBannerList = launchingList.stream().map(advertRound -> this.getStoreTopBanner(advertRound, fileMap, prodPriceAndMainPicMap)
                    .setOrderNum(this.positionToNumber(advertRound.getPosition()))).collect(Collectors.toList());
        }
        // 放到redis中，过期时间为1天
        redisCache.setCacheObject(CacheConstants.PC_ADVERT + CacheConstants.PC_ADVERT_STORE_TOP_BANNER, pcStoreTopBannerList, 1, TimeUnit.DAYS);
        return pcStoreTopBannerList;
    }

    /**
     * PC 档口馆 中间横幅
     *
     * @return List<PCStoreMidBannerDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public List<PCStoreMidBannerDTO> getPcStoreMidBannerList() {
        // 从redis中获取 档口馆 中间横幅数据
        List<PCStoreMidBannerDTO> pcStoreMidBannerList;
        List<PCStoreMidBannerDTO> redisList = redisCache.getCacheObject(CacheConstants.PC_ADVERT + CacheConstants.PC_ADVERT_STORE_MID_BANNER);
        if (CollectionUtils.isNotEmpty(redisList)) {
            return redisList;
        }
        List<AdvertRound> oneMonthList = this.getOneMonthAdvertList(Collections.singletonList(AdType.PC_STORE_MID_BANNER.getValue()));
        if (CollectionUtils.isEmpty(oneMonthList)) {
            return new ArrayList<>();
        }
        List<AdvertRound> launchingList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.LAUNCHING.getValue())).collect(Collectors.toList());
        List<AdvertRound> expiredList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.EXPIRED.getValue())).collect(Collectors.toList());
        Map<Long, SysFile> fileMap = fileMapper.selectList(new LambdaQueryWrapper<SysFile>().eq(SysFile::getDelFlag, Constants.UNDELETED)
                        .in(SysFile::getId, oneMonthList.stream().map(AdvertRound::getPicId).filter(ObjectUtils::isNotEmpty).collect(Collectors.toList())))
                .stream().collect(Collectors.toMap(SysFile::getId, Function.identity()));
        if (CollectionUtils.isEmpty(launchingList)) {
            List<PCStoreMidBannerDTO> tempList = new ArrayList<>();
            expiredList.stream().collect(Collectors.groupingBy(AdvertRound::getStoreId))
                    .forEach((storeId, list) -> {
                        AdvertRound advertRound = list.get(0);
                        tempList.add(new PCStoreMidBannerDTO().setStoreId(storeId).setDisplayType(AdDisplayType.PICTURE.getValue())
                                .setFileUrl(ObjectUtils.isNotEmpty(fileMap.get(advertRound.getPicId())) ? fileMap.get(advertRound.getPicId()).getFileUrl() : ""));
                    });
            pcStoreMidBannerList = tempList.stream().limit(1).collect(Collectors.toList());
            for (int i = 0; i < pcStoreMidBannerList.size(); i++) {
                pcStoreMidBannerList.get(i).setOrderNum(i + 1);
            }
        } else {
            pcStoreMidBannerList = launchingList.stream().map(advertRound -> new PCStoreMidBannerDTO().setStoreId(advertRound.getStoreId())
                            .setDisplayType(AdDisplayType.PICTURE.getValue()).setOrderNum(this.positionToNumber(advertRound.getPosition()))
                            .setFileUrl(ObjectUtils.isNotEmpty(fileMap.get(advertRound.getPicId())) ? fileMap.get(advertRound.getPicId()).getFileUrl() : ""))
                    .collect(Collectors.toList());
        }
        // 放到redis 中 过期时间1天
        redisCache.setCacheObject(CacheConstants.PC_ADVERT + CacheConstants.PC_ADVERT_STORE_MID_BANNER, pcStoreMidBannerList, 1, TimeUnit.DAYS);
        return pcStoreMidBannerList;
    }

    /**
     * 以图搜款 广告
     *
     * @return List<PicSearchAdvertDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public List<PicSearchAdvertDTO> getPicSearchList() {
        // 从redis中获取 以图搜款 广告数据
        List<PicSearchAdvertDTO> picSearchList;
        List<PicSearchAdvertDTO> redisList = redisCache.getCacheObject(CacheConstants.PC_ADVERT + CacheConstants.PIC_SEARCH);
        if (CollectionUtils.isNotEmpty(redisList)) {
            return redisList;
        }
        List<AdvertRound> oneMonthList = this.getOneMonthAdvertList(Collections.singletonList(AdType.PIC_SEARCH_PRODUCT.getValue()));
        if (CollectionUtils.isEmpty(oneMonthList)) {
            return new ArrayList<>();
        }
        // 获取近2个月 该商品的搜索次数
        List<StoreProductStatistics> prodStatsList = this.prodStatsMapper.selectList(new LambdaQueryWrapper<StoreProductStatistics>()
                .eq(StoreProductStatistics::getDelFlag, Constants.UNDELETED)
                .in(StoreProductStatistics::getStoreProdId, oneMonthList.stream().map(AdvertRound::getProdIdStr).map(Long::parseLong).collect(Collectors.toList()))
                .between(StoreProductStatistics::getVoucherDate, java.sql.Date.valueOf(LocalDate.now().minusMonths(2)), java.sql.Date.valueOf(LocalDate.now())));
        // 商品搜索次数map
        Map<Long, Long> prodSearchCountMap = prodStatsList.stream().collect(Collectors.groupingBy(StoreProductStatistics::getStoreProdId, Collectors
                .summingLong(StoreProductStatistics::getImgSearchCount)));
        List<Store> storeList = this.storeMapper.selectByIds(oneMonthList.stream().map(AdvertRound::getStoreId).collect(Collectors.toList()));
        Map<Long, Store> storeMap = storeList.stream().collect(Collectors.toMap(Store::getId, Function.identity()));
        List<Long> storeProdIdList = oneMonthList.stream().map(AdvertRound::getProdIdStr).map(Long::parseLong).collect(Collectors.toList());
        // 获取所有商品的价格及第一张主图
        List<StoreProdPriceAndMainPicDTO> prodPriceAndMainPicList = this.storeProdMapper.selectPriceAndMainPicList(storeProdIdList);
        Map<Long, StoreProdPriceAndMainPicDTO> prodPriceAndMainPicMap = prodPriceAndMainPicList.stream().collect(Collectors
                .toMap(StoreProdPriceAndMainPicDTO::getStoreProdId, Function.identity()));
        // 商品标签
        List<DailyProdTag> prodTagList = this.dailyProdTagMapper.selectList(new LambdaQueryWrapper<DailyProdTag>()
                .eq(DailyProdTag::getDelFlag, Constants.UNDELETED).in(DailyProdTag::getStoreProdId, storeProdIdList));
        Map<Long, List<String>> prodTagMap = prodTagList.stream().collect(Collectors.groupingBy(DailyProdTag::getStoreProdId, Collectors
                .collectingAndThen(Collectors.toList(), list -> list.stream()
                        .sorted(Comparator.comparing(DailyProdTag::getType)).map(DailyProdTag::getTag).collect(Collectors.toList()))));
        // 档口标签
        List<DailyStoreTag> storeTagList = this.dailyStoreTagMapper.selectList(new LambdaQueryWrapper<DailyStoreTag>()
                .eq(DailyStoreTag::getDelFlag, Constants.UNDELETED).in(DailyStoreTag::getStoreId, storeList.stream().map(Store::getId).collect(Collectors.toList())));
        Map<Long, List<String>> storeTagMap = storeTagList.stream().collect(Collectors
                .groupingBy(DailyStoreTag::getStoreId, Collectors.collectingAndThen(Collectors.toList(), list -> list.stream()
                        .sorted(Comparator.comparing(DailyStoreTag::getType)).map(DailyStoreTag::getTag).collect(Collectors.toList()))));
        List<AdvertRound> launchingList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.LAUNCHING.getValue())).collect(Collectors.toList());
        List<AdvertRound> expiredList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.EXPIRED.getValue())).collect(Collectors.toList());
        // 从正在播放的图搜热款广告或者历史广告中筛选10条
        picSearchList = getPicSearchAdvertList(CollectionUtils.isEmpty(launchingList) ? expiredList : launchingList,
                prodSearchCountMap, storeMap, prodPriceAndMainPicMap, prodTagMap, storeTagMap);
        // 放到redis 中 过期时间1天
        redisCache.setCacheObject(CacheConstants.PC_ADVERT + CacheConstants.PIC_SEARCH, picSearchList, 1, TimeUnit.DAYS);
        return picSearchList;
    }

    /**
     * PC 用户中心
     *
     * @return List<PCUserCenterDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public List<PCUserCenterDTO> getPcUserCenterList() {
        // 从redis中获取 用户中心数据
        List<PCUserCenterDTO> pcUserCenterList;
        List<PCUserCenterDTO> redisList = redisCache.getCacheObject(CacheConstants.PC_ADVERT + CacheConstants.PC_USER_CENTER);
        if (CollectionUtils.isNotEmpty(redisList)) {
            return redisList;
        }
        List<AdvertRound> oneMonthList = this.getOneMonthAdvertList(Collections.singletonList(AdType.PC_USER_CENTER.getValue()));
        if (CollectionUtils.isEmpty(oneMonthList)) {
            return new ArrayList<>();
        }
        List<StoreProdPriceAndMainPicDTO> prodPriceAndMainPicList = this.storeProdMapper.selectPriceAndMainPicList(oneMonthList.stream()
                .map(AdvertRound::getProdIdStr).map(Long::parseLong).collect(Collectors.toList()));
        // 档口商品的价格及商品主图map
        Map<Long, StoreProdPriceAndMainPicDTO> prodPriceAndMainPicMap = prodPriceAndMainPicList.stream().collect(Collectors
                .toMap(StoreProdPriceAndMainPicDTO::getStoreProdId, Function.identity()));
        List<AdvertRound> launchingList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.LAUNCHING.getValue())).collect(Collectors.toList());
        List<AdvertRound> expiredList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.EXPIRED.getValue())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(launchingList)) {
            pcUserCenterList = expiredList.stream().map(x -> this.getPcUserCenterDTO(x, prodPriceAndMainPicMap)).limit(18).collect(Collectors.toList());
            for (int i = 0; i < pcUserCenterList.size(); i++) {
                pcUserCenterList.get(i).setOrderNum(i + 1);
            }
        } else {
            pcUserCenterList = launchingList.stream().map(x -> this.getPcUserCenterDTO(x, prodPriceAndMainPicMap)
                    .setOrderNum(this.positionToNumber(x.getPosition()))).limit(18).collect(Collectors.toList());
        }
        // 放到redis 中 过期时间1天
        redisCache.setCacheObject(CacheConstants.PC_ADVERT + CacheConstants.PC_USER_CENTER, pcUserCenterList, 1, TimeUnit.DAYS);
        return pcUserCenterList;
    }

    /**
     * PC 下载页
     *
     * @return List<PCDownloadDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public List<PCDownloadDTO> getPcDownloadList() {
        // 从redis中获取 下载页数据
        List<PCDownloadDTO> pcDownloadList;
        List<PCDownloadDTO> redisList = redisCache.getCacheObject(CacheConstants.PC_ADVERT + CacheConstants.PC_DOWNLOAD);
        if (CollectionUtils.isNotEmpty(redisList)) {
            return redisList;
        }
        List<AdvertRound> oneMonthList = this.getOneMonthAdvertList(Collections.singletonList(AdType.PC_DOWNLOAD.getValue()));
        if (CollectionUtils.isEmpty(oneMonthList)) {
            return new ArrayList<>();
        }
        List<StoreProdPriceAndMainPicDTO> prodPriceAndMainPicList = this.storeProdMapper.selectPriceAndMainPicList(oneMonthList.stream()
                .map(AdvertRound::getProdIdStr).map(Long::parseLong).collect(Collectors.toList()));
        Map<Long, StoreProdPriceAndMainPicDTO> prodPriceAndMainPicMap = prodPriceAndMainPicList.stream().collect(Collectors
                .toMap(StoreProdPriceAndMainPicDTO::getStoreProdId, Function.identity()));
        List<AdvertRound> launchingList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.LAUNCHING.getValue())).collect(Collectors.toList());
        List<AdvertRound> expiredList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.EXPIRED.getValue())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(launchingList)) {
            pcDownloadList = expiredList.stream().map(advertRound -> this.getPcDownload(advertRound, prodPriceAndMainPicMap)).limit(10).collect(Collectors.toList());
            for (int i = 0; i < pcDownloadList.size(); i++) {
                pcDownloadList.get(i).setOrderNum(i + 1);
            }
        } else {
            pcDownloadList = launchingList.stream().map(advertRound -> this.getPcDownload(advertRound, prodPriceAndMainPicMap)
                    .setOrderNum(this.positionToNumber(advertRound.getPosition()))).limit(10).collect(Collectors.toList());
        }
        // 放到redis 中 过期时间1天
        redisCache.setCacheObject(CacheConstants.PC_ADVERT + CacheConstants.PC_DOWNLOAD, pcDownloadList, 1, TimeUnit.DAYS);
        return pcDownloadList;
    }

    /**
     * APP 首页顶部轮播图
     *
     * @return List<APPIndexTopBannerDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public List<APPIndexTopBannerDTO> getAppIndexTopBanner() {
        List<APPIndexTopBannerDTO> appIndexTopBannerList;
        // 从redis中获取数据
        List<APPIndexTopBannerDTO> redisList = redisCache.getCacheObject(CacheConstants.APP_ADVERT + CacheConstants.APP_INDEX_TOP_BANNER);
        if (CollectionUtils.isNotEmpty(redisList)) {
            return redisList;
        }
        List<AdvertRound> oneMonthList = this.getOneMonthAdvertList(Collections.singletonList(AdType.APP_HOME_TOP_BANNER.getValue()));
        if (CollectionUtils.isEmpty(oneMonthList)) {
            return new ArrayList<>();
        }
        Map<Long, SysFile> fileMap = fileMapper.selectList(new LambdaQueryWrapper<SysFile>().eq(SysFile::getDelFlag, Constants.UNDELETED)
                        .in(SysFile::getId, oneMonthList.stream().map(AdvertRound::getPicId).filter(ObjectUtils::isNotEmpty).collect(Collectors.toList())))
                .stream().collect(Collectors.toMap(SysFile::getId, Function.identity()));
        List<AdvertRound> launchingList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.LAUNCHING.getValue())).collect(Collectors.toList());
        List<AdvertRound> expiredList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.EXPIRED.getValue())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(launchingList)) {
            List<APPIndexTopBannerDTO> tempList = new ArrayList<>();
            expiredList.stream().collect(Collectors.groupingBy(AdvertRound::getStoreId))
                    .forEach((storeId, list) -> {
                        AdvertRound advertRound = list.get(0);
                        tempList.add(new APPIndexTopBannerDTO().setDisplayType(AdDisplayType.PICTURE.getValue()).setStoreId(storeId)
                                .setFileUrl(ObjectUtils.isNotEmpty(fileMap.get(advertRound.getPicId())) ? fileMap.get(advertRound.getPicId()).getFileUrl() : ""));
                    });
            appIndexTopBannerList = tempList.stream().limit(5).collect(Collectors.toList());
            for (int i = 0; i < appIndexTopBannerList.size(); i++) {
                appIndexTopBannerList.get(i).setOrderNum(i + 1);
            }
        } else {
            appIndexTopBannerList = launchingList.stream().map(x -> new APPIndexTopBannerDTO().setDisplayType(AdDisplayType.PICTURE.getValue())
                            .setStoreId(x.getStoreId()).setOrderNum(this.positionToNumber(x.getPosition()))
                            .setFileUrl(ObjectUtils.isNotEmpty(fileMap.get(x.getPicId())) ? fileMap.get(x.getPicId()).getFileUrl() : ""))
                    .collect(Collectors.toList());
        }
        // 放到redis 中 过期时间1天
        redisCache.setCacheObject(CacheConstants.APP_ADVERT + CacheConstants.APP_INDEX_TOP_BANNER, appIndexTopBannerList, 1, TimeUnit.DAYS);
        return appIndexTopBannerList;
    }

    /**
     * APP 首页中部品牌好货
     *
     * @return List<APPIndexMidBrandDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public List<APPIndexMidBrandDTO> getAppIndexMidBrand() {
        List<APPIndexMidBrandDTO> appIndexMidBrandList;
        // 从redis 中获取数据
        List<APPIndexMidBrandDTO> redisList = redisCache.getCacheObject(CacheConstants.APP_ADVERT + CacheConstants.APP_INDEX_MID_BRAND);
        if (CollectionUtils.isNotEmpty(redisList)) {
            return redisList;
        }
        List<AdvertRound> oneMonthList = this.getOneMonthAdvertList(Collections.singletonList(AdType.APP_HOME_RECOMMEND_PRODUCT.getValue()));
        if (CollectionUtils.isEmpty(oneMonthList)) {
            return new ArrayList<>();
        }
        List<StoreProdPriceAndMainPicDTO> prodPriceAndMainPicList = this.storeProdMapper.selectPriceAndMainPicList(oneMonthList.stream()
                .map(AdvertRound::getProdIdStr).map(Long::parseLong).collect(Collectors.toList()));
        Map<Long, StoreProdPriceAndMainPicDTO> prodPriceAndMainPicMap = prodPriceAndMainPicList.stream().collect(Collectors
                .toMap(StoreProdPriceAndMainPicDTO::getStoreProdId, Function.identity()));
        List<AdvertRound> launchingList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.LAUNCHING.getValue())).collect(Collectors.toList());
        List<AdvertRound> expiredList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.EXPIRED.getValue())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(launchingList)) {
            appIndexMidBrandList = this.getAppIndexMidBrandList(expiredList, 5, prodPriceAndMainPicMap);
        } else {
            appIndexMidBrandList = launchingList.stream().map(x -> {
                final Long storeProdId = Long.parseLong(x.getProdIdStr());
                return new APPIndexMidBrandDTO().setDisplayType(AdDisplayType.PRODUCT.getValue()).setStoreProdId(storeProdId).setOrderNum(this.positionToNumber(x.getPosition()))
                        .setPrice(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getMinPrice() : null)
                        .setMainPicUrl(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getMainPicUrl() : "");
            }).limit(5).collect(Collectors.toList());
            if (appIndexMidBrandList.size() < 5) {
                appIndexMidBrandList.addAll(this.getAppIndexMidBrandList(expiredList, 5 - appIndexMidBrandList.size(), prodPriceAndMainPicMap));
            }
        }
        for (int i = 0; i < appIndexMidBrandList.size(); i++) {
            appIndexMidBrandList.get(i).setOrderNum(i + 1);
        }
        // 放到redis中，过期时间为1天
        redisCache.setCacheObject(CacheConstants.APP_ADVERT + CacheConstants.APP_INDEX_MID_BRAND, appIndexMidBrandList, 1, TimeUnit.DAYS);
        return appIndexMidBrandList;
    }

    /**
     * APP 首页中部 品牌好货
     *
     * @param advertRoundList        品牌好货列表
     * @param limitCount             筛选的数量
     * @param prodPriceAndMainPicMap 商品价格和主图map
     * @return List<APPIndexMidBrandDTO>
     */
    private List<APPIndexMidBrandDTO> getAppIndexMidBrandList(List<AdvertRound> advertRoundList, int limitCount, Map<Long, StoreProdPriceAndMainPicDTO> prodPriceAndMainPicMap) {
        return advertRoundList.stream().map(x -> {
            final Long storeProdId = Long.parseLong(x.getProdIdStr());
            return new APPIndexMidBrandDTO().setDisplayType(AdDisplayType.PRODUCT.getValue()).setStoreProdId(storeProdId)
                    .setPrice(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getMinPrice() : null)
                    .setMainPicUrl(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getMainPicUrl() : "");
        }).limit(limitCount).collect(Collectors.toList());
    }

    /**
     * APP 首页热卖精选右侧固定位置
     *
     * @return List<APPIndexHotSaleRightFixDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public List<APPIndexHotSaleRightFixDTO> getAppIndexHotSaleRightFix() {
        // 从redis中获取数据
        List<APPIndexHotSaleRightFixDTO> appIndexHotSaleRightFixList;
        // 从redis 中获取数据
        List<APPIndexHotSaleRightFixDTO> redisList = redisCache.getCacheObject(CacheConstants.APP_ADVERT + CacheConstants.APP_INDEX_HOT_SALE_RIGHT_FIX);
        if (CollectionUtils.isNotEmpty(redisList)) {
            return redisList;
        }
        List<AdvertRound> oneMonthList = this.getOneMonthAdvertList(Collections.singletonList(AdType.APP_HOME_HOT_RECOMMEND_FIXED_PROD.getValue()));
        if (CollectionUtils.isEmpty(oneMonthList)) {
            return new ArrayList<>();
        }
        List<StoreProdPriceAndMainPicDTO> prodPriceAndMainPicList = this.storeProdMapper.selectPriceAndMainPicList(oneMonthList.stream()
                .map(AdvertRound::getProdIdStr).map(Long::parseLong).collect(Collectors.toList()));
        Map<Long, StoreProdPriceAndMainPicDTO> prodPriceAndMainPicMap = prodPriceAndMainPicList.stream().collect(Collectors
                .toMap(StoreProdPriceAndMainPicDTO::getStoreProdId, Function.identity()));
        List<AdvertRound> launchingList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.LAUNCHING.getValue())).collect(Collectors.toList());
        List<AdvertRound> expiredList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.EXPIRED.getValue())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(launchingList)) {
            appIndexHotSaleRightFixList = expiredList.stream().map(x -> {
                Long storeProdId = Long.parseLong(x.getProdIdStr());
                return new APPIndexHotSaleRightFixDTO().setDisplayType(AdDisplayType.PRODUCT.getValue()).setStoreProdId(storeProdId)
                        .setMainPicUrl(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getMainPicUrl() : "");
            }).limit(5).collect(Collectors.toList());
            for (int i = 0; i < appIndexHotSaleRightFixList.size(); i++) {
                appIndexHotSaleRightFixList.get(i).setOrderNum(i + 1);
            }
        } else {
            appIndexHotSaleRightFixList = launchingList.stream().map(x -> {
                Long storeProdId = Long.parseLong(x.getProdIdStr());
                return new APPIndexHotSaleRightFixDTO().setDisplayType(AdDisplayType.PRODUCT.getValue()).setStoreProdId(storeProdId).setOrderNum(this.positionToNumber(x.getPosition()))
                        .setMainPicUrl(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getMainPicUrl() : "");
            }).limit(5).collect(Collectors.toList());
        }
        // 放到redis中，有效期1天
        redisCache.setCacheObject(CacheConstants.APP_ADVERT + CacheConstants.APP_INDEX_HOT_SALE_RIGHT_FIX, appIndexHotSaleRightFixList, 1, TimeUnit.DAYS);
        return appIndexHotSaleRightFixList;
    }

    /**
     * APP 分类页
     *
     * @return List<APPCateDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public List<APPCateDTO> getAppCateList() {
        List<APPCateDTO> appCateList;
        // 从redis中获取数据
        List<APPCateDTO> redisList = redisCache.getCacheObject(CacheConstants.APP_ADVERT + CacheConstants.APP_CATE);
        if (CollectionUtils.isNotEmpty(redisList)) {
            return redisList;
        }
        List<AdvertRound> oneMonthList = this.getOneMonthAdvertList(Collections.singletonList(AdType.APP_CATEGORY_TOP_BANNER.getValue()));
        if (CollectionUtils.isEmpty(oneMonthList)) {
            return new ArrayList<>();
        }
        Map<Long, SysFile> fileMap = fileMapper.selectList(new LambdaQueryWrapper<SysFile>().eq(SysFile::getDelFlag, Constants.UNDELETED)
                        .in(SysFile::getId, oneMonthList.stream().map(AdvertRound::getPicId).filter(ObjectUtils::isNotEmpty).collect(Collectors.toList())))
                .stream().collect(Collectors.toMap(SysFile::getId, Function.identity()));
        List<AdvertRound> launchingList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.LAUNCHING.getValue())).collect(Collectors.toList());
        List<AdvertRound> expiredList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.EXPIRED.getValue())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(launchingList)) {
            List<APPCateDTO> tempList = new ArrayList<>();
            expiredList.stream().collect(Collectors.groupingBy(AdvertRound::getStoreId))
                    .forEach((storeId, list) -> {
                        AdvertRound advertRound = list.get(0);
                        tempList.add(new APPCateDTO().setDisplayType(AdDisplayType.PICTURE.getValue()).setStoreId(advertRound.getStoreId())
                                .setFileUrl(ObjectUtils.isNotEmpty(fileMap.get(advertRound.getPicId())) ? fileMap.get(advertRound.getPicId()).getFileUrl() : ""));
                    });
            appCateList = tempList.stream().limit(5).collect(Collectors.toList());
            for (int i = 0; i < appCateList.size(); i++) {
                appCateList.get(i).setOrderNum(i + 1);
            }
        } else {
            appCateList = launchingList.stream().map(x -> new APPCateDTO().setDisplayType(AdDisplayType.PICTURE.getValue())
                            .setStoreId(x.getStoreId()).setOrderNum(this.positionToNumber(x.getPosition()))
                            .setFileUrl(ObjectUtils.isNotEmpty(fileMap.get(x.getPicId())) ? fileMap.get(x.getPicId()).getFileUrl() : ""))
                    .collect(Collectors.toList());
        }
        // 放到redis中，有效期1天
        redisCache.setCacheObject(CacheConstants.APP_ADVERT + CacheConstants.APP_CATE, appCateList, 1, TimeUnit.DAYS);
        return appCateList;
    }

    /**
     * APP 我的猜你喜欢列表
     *
     * @return List<APPOwnGuessLikeDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public List<APPOwnGuessLikeDTO> getAppOwnGuessLikeList() {
        List<APPOwnGuessLikeDTO> appOwnGuessLikeList;
        // 从redis中获取缓存数据
        List<APPOwnGuessLikeDTO> redisList = redisCache.getCacheObject(CacheConstants.APP_ADVERT + CacheConstants.APP_OWN_GUESS_LIKE);
        if (CollectionUtils.isNotEmpty(redisList)) {
            return redisList;
        }
        List<AdvertRound> oneMonthList = this.getOneMonthAdvertList(Collections.singletonList(AdType.APP_USER_CENTER_GUESS_YOU_LIKE.getValue()));
        if (CollectionUtils.isEmpty(oneMonthList)) {
            return new ArrayList<>();
        }
        List<Long> storeProdIdList = oneMonthList.stream().map(AdvertRound::getProdIdStr).map(Long::parseLong).collect(Collectors.toList());
        // 商品标签
        List<DailyProdTag> prodTagList = this.dailyProdTagMapper.selectList(new LambdaQueryWrapper<DailyProdTag>()
                .eq(DailyProdTag::getDelFlag, Constants.UNDELETED).in(DailyProdTag::getStoreProdId, storeProdIdList));
        Map<Long, List<String>> prodTagMap = prodTagList.stream().collect(Collectors.groupingBy(DailyProdTag::getStoreProdId, Collectors
                .collectingAndThen(Collectors.toList(), list -> list.stream()
                        .sorted(Comparator.comparing(DailyProdTag::getType)).map(DailyProdTag::getTag).collect(Collectors.toList()))));

        List<StoreProdPriceAndMainPicDTO> prodPriceAndMainPicList = this.storeProdMapper.selectPriceAndMainPicList(oneMonthList.stream()
                .map(AdvertRound::getProdIdStr).map(Long::parseLong).collect(Collectors.toList()));
        Map<Long, StoreProdPriceAndMainPicDTO> prodPriceAndMainPicMap = prodPriceAndMainPicList.stream().collect(Collectors
                .toMap(StoreProdPriceAndMainPicDTO::getStoreProdId, Function.identity()));
        List<AdvertRound> launchingList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.LAUNCHING.getValue())).collect(Collectors.toList());
        List<AdvertRound> expiredList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.EXPIRED.getValue())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(launchingList)) {
            appOwnGuessLikeList = expiredList.stream().map(x -> {
                final Long storeProdId = Long.parseLong(x.getProdIdStr());
                return new APPOwnGuessLikeDTO().setDisplayType(AdDisplayType.PRODUCT.getValue()).setStoreProdId(storeProdId)
                        .setTagList(ObjectUtils.isNotEmpty(prodTagMap.get(storeProdId)) ? prodTagMap.get(storeProdId) : null)
                        .setPrice(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getMinPrice() : null)
                        .setProdArtNum(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getProdArtNum() : "")
                        .setMainPicUrl(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getMainPicUrl() : "");
            }).limit(20).collect(Collectors.toList());
            for (int i = 0; i < appOwnGuessLikeList.size(); i++) {
                appOwnGuessLikeList.get(i).setOrderNum(i + 1);
            }
        } else {
            appOwnGuessLikeList = launchingList.stream().map(x -> {
                final Long storeProdId = Long.parseLong(x.getProdIdStr());
                return new APPOwnGuessLikeDTO().setDisplayType(AdDisplayType.PRODUCT.getValue()).setStoreProdId(storeProdId).setOrderNum(this.positionToNumber(x.getPosition()))
                        .setTagList(ObjectUtils.isNotEmpty(prodTagMap.get(storeProdId)) ? prodTagMap.get(storeProdId) : null)
                        .setPrice(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getMinPrice() : null)
                        .setProdArtNum(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getProdArtNum() : "")
                        .setMainPicUrl(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getMainPicUrl() : "");
            }).limit(20).collect(Collectors.toList());
        }
        // 放到redis中，有效期1天
        redisCache.setCacheObject(CacheConstants.APP_ADVERT + CacheConstants.APP_OWN_GUESS_LIKE, appOwnGuessLikeList, 1, TimeUnit.DAYS);
        return appOwnGuessLikeList;
    }



    /**
     * 获取PC 下载页数据
     *
     * @param advertRound            下载页广告
     * @param prodPriceAndMainPicMap 商品价格和图片
     * @return PCDownloadDTO
     */
    private PCDownloadDTO getPcDownload(AdvertRound advertRound, Map<Long, StoreProdPriceAndMainPicDTO> prodPriceAndMainPicMap) {
        final Long storeProdId = Long.parseLong(advertRound.getProdIdStr());
        return new PCDownloadDTO().setDisplayType(AdDisplayType.PRODUCT.getValue())
                .setPrice(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getMinPrice() : null)
                .setProdArtNum(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getProdArtNum() : "")
                .setMainPicUrl(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getMainPicUrl() : "");
    }

    /**
     * 获取PC 用户中心 广告列表
     *
     * @param advertRound            用户中心
     * @param prodPriceAndMainPicMap 商品价格及主图等map
     * @return PCUserCenterDTO
     */
    private PCUserCenterDTO getPcUserCenterDTO(AdvertRound advertRound, Map<Long, StoreProdPriceAndMainPicDTO> prodPriceAndMainPicMap) {
        final Long storeProdId = Long.parseLong(advertRound.getProdIdStr());
        return new PCUserCenterDTO().setDisplayType(AdDisplayType.PRODUCT.getValue())
                .setPrice(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getMinPrice() : null)
                .setProdArtNum(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getProdArtNum() : "")
                .setMainPicUrl(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getMainPicUrl() : "")
                .setProdTitle(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getProdTitle() : "");
    }

    /**
     * 获取以图搜款的广告
     *
     * @param picSearchList          图搜热款数据库数据
     * @param prodSearchCountMap     商品搜索次数
     * @param storeMap               档口map
     * @param prodPriceAndMainPicMap 档口商品价格及主图map
     * @param prodTagMap             商品标签map
     * @param storeTagMap            档口标签map
     * @return List<PicSearchAdvertDTO>
     */
    private List<PicSearchAdvertDTO> getPicSearchAdvertList(List<AdvertRound> picSearchList, Map<Long, Long> prodSearchCountMap, Map<Long, Store> storeMap,
                                                            Map<Long, StoreProdPriceAndMainPicDTO> prodPriceAndMainPicMap, Map<Long, List<String>> prodTagMap,
                                                            Map<Long, List<String>> storeTagMap) {
        return picSearchList.stream().map(advertRound -> {

            // TODO 查询同款商品数量
            // TODO 查询同款商品数量
            // TODO 查询同款商品数量

            final Long storeProdId = Long.valueOf(advertRound.getProdIdStr());
            return new PicSearchAdvertDTO().setImgSearchCount(ObjectUtils.defaultIfNull(prodSearchCountMap.get(storeProdId), (long) (new Random().nextInt(191) + 10)))
                    .setSameProdCount(null).setStoreProdId(storeProdId).setStoreId(advertRound.getStoreId())
                    .setStoreName(ObjectUtils.isNotEmpty(storeMap.get(advertRound.getStoreId())) ? storeMap.get(advertRound.getStoreId()).getStoreName() : "")
                    .setPrice(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getMinPrice() : null)
                    .setProdArtNum(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getProdArtNum() : "")
                    .setMainPicUrl(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getMainPicUrl() : "")
                    .setProdTitle(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getProdTitle() : "")
                    .setProdTagList(CollectionUtils.isNotEmpty(prodTagMap.get(storeProdId)) ? prodTagMap.get(storeProdId) : null);
//                    .setStoreTagList(CollectionUtils.isNotEmpty(storeTagMap.get(advertRound.getStoreId())) ? storeTagMap.get(advertRound.getStoreId()) : null);
        }).limit(10).collect(Collectors.toList());
    }

    /**
     * 获取档口馆 顶部横幅及商品
     *
     * @param advertRound            当前推广
     * @param fileMap                推广图map
     * @param prodPriceAndMainPicMap 商品主图及价格map
     * @return PCStoreTopBannerDTO
     */
    private PCStoreTopBannerDTO getStoreTopBanner(AdvertRound advertRound, Map<Long, SysFile> fileMap, Map<Long, StoreProdPriceAndMainPicDTO> prodPriceAndMainPicMap) {
        List<PCStoreTopBannerDTO.PCSTBProdDTO> prodList = new ArrayList<>();
        String[] prodIdStrArray = advertRound.getProdIdStr().split(",");
        for (int i = 0; i < prodIdStrArray.length; i++) {
            Long tempStoreProdId = Long.valueOf(prodIdStrArray[i]);
            prodList.add(new PCStoreTopBannerDTO.PCSTBProdDTO().setDisplayType(AdDisplayType.PRODUCT.getValue()).setStoreProdId(tempStoreProdId).setOrderNum(i + 1)
                    .setPrice(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(tempStoreProdId)) ? prodPriceAndMainPicMap.get(tempStoreProdId).getMinPrice() : null)
                    .setProdArtNum(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(tempStoreProdId)) ? prodPriceAndMainPicMap.get(tempStoreProdId).getProdArtNum() : "")
                    .setMainPicUrl(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(tempStoreProdId)) ? prodPriceAndMainPicMap.get(tempStoreProdId).getMainPicUrl() : ""));
        }
        return new PCStoreTopBannerDTO().setDisplayType(AdDisplayType.PICTURE.getValue()).setStoreId(advertRound.getStoreId()).setProdList(prodList)
                .setFileUrl(ObjectUtils.isNotEmpty(fileMap.get(advertRound.getPicId())) ? fileMap.get(advertRound.getPicId()).getFileUrl() : "");
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
                            .setMainPicUrl(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getMainPicUrl() : ""));
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
                                .setMainPicUrl(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getMainPicUrl() : ""));
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

    /**
     * 在指定位置插入广告数据到列表中
     *
     * @param dataList      原始数据列表
     * @param adverts       广告数据列表
     * @param positions     插入广告的位置集合
     * @param <T>           数据类型
     * @return 合并后的列表
     */
    public static <T> List<T> insertAdvertsIntoList(List<T> dataList, List<T> adverts, Set<Integer> positions) {
        List<T> mergedList = new ArrayList<>();
        int dataIndex = 0;
        int advertIndex = 0;
        for (int i = 0; i < dataList.size() + positions.size(); i++) {
            if (positions.contains(i) && advertIndex < adverts.size()) {
                mergedList.add(adverts.get(advertIndex++));
            } else if (dataIndex < dataList.size()) {
                mergedList.add(dataList.get(dataIndex++));
            }
        }
        return mergedList;
    }

    private static FieldValue newFieldValue(String value) {
        return FieldValue.of(value);
    }


}
