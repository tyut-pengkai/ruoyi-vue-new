package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
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
import com.ruoyi.xkt.domain.AdvertRound;
import com.ruoyi.xkt.domain.SysFile;
import com.ruoyi.xkt.dto.advertRound.app.category.APPCateDTO;
import com.ruoyi.xkt.dto.advertRound.app.index.*;
import com.ruoyi.xkt.dto.advertRound.app.own.APPOwnGuessLikeDTO;
import com.ruoyi.xkt.dto.es.ESProductDTO;
import com.ruoyi.xkt.dto.storeProduct.StoreProdPriceAndMainPicAndTagDTO;
import com.ruoyi.xkt.dto.storeProduct.StoreProdPriceAndMainPicDTO;
import com.ruoyi.xkt.dto.website.IndexSearchDTO;
import com.ruoyi.xkt.enums.AdBiddingStatus;
import com.ruoyi.xkt.enums.AdDisplayType;
import com.ruoyi.xkt.enums.AdLaunchStatus;
import com.ruoyi.xkt.mapper.*;
import com.ruoyi.xkt.service.IWebsiteAPPService;
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
import java.util.concurrent.ConcurrentHashMap;
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
public class WebsiteAPPServiceImpl implements IWebsiteAPPService {

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
                    .eq(AdvertRound::getLaunchStatus, AdLaunchStatus.LAUNCHING.getValue())
                    .eq(AdvertRound::getBiddingStatus, AdBiddingStatus.BIDDING_SUCCESS.getValue()));
            if (CollectionUtils.isNotEmpty(advertRoundList)) {
                final List<Long> storeProdIdList = advertRoundList.stream()
                        .filter(x -> StringUtils.isNotBlank(x.getProdIdStr())).map(x -> Long.parseLong(x.getProdIdStr())).distinct().collect(Collectors.toList());
                // 商品的货号、价格、主图等
                Map<Long, StoreProdPriceAndMainPicAndTagDTO> attrMap = this.getStoreProdAttrMap(storeProdIdList);
                List<APPIndexHotSaleDTO> hotSaleList = advertRoundList.stream().filter(x -> StringUtils.isNotBlank(x.getProdIdStr())).map(x -> {
                    StoreProdPriceAndMainPicAndTagDTO attrDto = attrMap.get(Long.parseLong(x.getProdIdStr()));
                    return new APPIndexHotSaleDTO().setAdvert(Boolean.TRUE).setStoreId(x.getStoreId().toString()).setStoreProdId(x.getProdIdStr())
                            .setTags(StringUtils.isNotBlank(attrDto.getTagStr()) ? StrUtil.split(attrDto.getTagStr(), ",") : null)
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
                    .eq(AdvertRound::getLaunchStatus, AdLaunchStatus.LAUNCHING.getValue())
                    .eq(AdvertRound::getBiddingStatus, AdBiddingStatus.BIDDING_SUCCESS.getValue()));
            if (CollectionUtils.isNotEmpty(advertRoundList)) {
                final List<Long> storeProdIdList = advertRoundList.stream()
                        .filter(x -> StringUtils.isNotBlank(x.getProdIdStr())).map(x -> Long.parseLong(x.getProdIdStr())).distinct().collect(Collectors.toList());
                // 商品的货号、价格、主图等
                Map<Long, StoreProdPriceAndMainPicAndTagDTO> attrMap = this.getStoreProdAttrMap(storeProdIdList);
                List<APPIndexPopularSaleDTO> popularSaleList = advertRoundList.stream().filter(x -> StringUtils.isNotBlank(x.getProdIdStr())).map(x -> {
                    StoreProdPriceAndMainPicAndTagDTO attrDto = attrMap.get(Long.parseLong(x.getProdIdStr()));
                    return new APPIndexPopularSaleDTO().setAdvert(Boolean.TRUE).setStoreId(x.getStoreId().toString()).setStoreProdId(x.getProdIdStr())
                            .setTags(StringUtils.isNotBlank(attrDto.getTagStr()) ? StrUtil.split(attrDto.getTagStr(), ",") : null)
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
                    .eq(AdvertRound::getLaunchStatus, AdLaunchStatus.LAUNCHING.getValue())
                    .eq(AdvertRound::getBiddingStatus, AdBiddingStatus.BIDDING_SUCCESS.getValue()));
            if (CollectionUtils.isNotEmpty(advertRoundList)) {
                final List<Long> storeProdIdList = advertRoundList.stream()
                        .filter(x -> StringUtils.isNotBlank(x.getProdIdStr())).map(x -> Long.parseLong(x.getProdIdStr())).distinct().collect(Collectors.toList());
                // 商品的货号、价格、主图等
                Map<Long, StoreProdPriceAndMainPicAndTagDTO> attrMap = this.getStoreProdAttrMap(storeProdIdList);
                List<APPIndexNewProdDTO> newProdList = advertRoundList.stream().filter(x -> StringUtils.isNotBlank(x.getProdIdStr())).map(x -> {
                    StoreProdPriceAndMainPicAndTagDTO attrDto = attrMap.get(Long.parseLong(x.getProdIdStr()));
                    return new APPIndexNewProdDTO().setAdvert(Boolean.TRUE).setStoreId(x.getStoreId().toString()).setStoreProdId(x.getProdIdStr())
                            .setTags(StringUtils.isNotBlank(attrDto.getTagStr()) ? StrUtil.split(attrDto.getTagStr(), ",") : null)
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
                    .eq(AdvertRound::getLaunchStatus, AdLaunchStatus.LAUNCHING.getValue())
                    .eq(AdvertRound::getBiddingStatus, AdBiddingStatus.BIDDING_SUCCESS.getValue()));
            if (CollectionUtils.isNotEmpty(advertRoundList)) {
                final List<Long> storeProdIdList = advertRoundList.stream()
                        .filter(x -> StringUtils.isNotBlank(x.getProdIdStr())).map(x -> Long.parseLong(x.getProdIdStr())).distinct().collect(Collectors.toList());
                // 商品的货号、价格、主图等
                Map<Long, StoreProdPriceAndMainPicAndTagDTO> attrMap = this.getStoreProdAttrMap(storeProdIdList);
                List<APPSearchDTO> newProdList = advertRoundList.stream().filter(x -> StringUtils.isNotBlank(x.getProdIdStr())).map(x -> {
                    StoreProdPriceAndMainPicAndTagDTO attrDto = attrMap.get(Long.parseLong(x.getProdIdStr()));
                    return new APPSearchDTO().setAdvert(Boolean.TRUE).setStoreId(x.getStoreId().toString()).setStoreProdId(x.getProdIdStr())
                            .setTags(StringUtils.isNotBlank(attrDto.getTagStr()) ? StrUtil.split(attrDto.getTagStr(), ",") : null)
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
        List<AdvertRound> launchingList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.LAUNCHING.getValue()))
                .filter(x -> Objects.equals(x.getBiddingStatus(), AdBiddingStatus.BIDDING_SUCCESS.getValue())).collect(Collectors.toList());
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
        final List<Long> storeProdIdList = oneMonthList.stream()
                .filter(x -> StringUtils.isNotBlank(x.getProdIdStr())).map(x -> Long.parseLong(x.getProdIdStr())).distinct().collect(Collectors.toList());
        // 档口商品的价格及商品主图map
        Map<Long, StoreProdPriceAndMainPicDTO> prodPriceAndMainPicMap = CollectionUtils.isEmpty(storeProdIdList) ? new ConcurrentHashMap<>()
                : this.storeProdMapper.selectPriceAndMainPicList(storeProdIdList).stream().collect(Collectors
                .toMap(StoreProdPriceAndMainPicDTO::getStoreProdId, Function.identity()));
        List<AdvertRound> launchingList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.LAUNCHING.getValue()))
                .filter(x -> Objects.equals(x.getBiddingStatus(), AdBiddingStatus.BIDDING_SUCCESS.getValue())).collect(Collectors.toList());
        List<AdvertRound> expiredList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.EXPIRED.getValue())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(launchingList)) {
            appIndexMidBrandList = this.getAppIndexMidBrandList(expiredList, 5, prodPriceAndMainPicMap);
        } else {
            appIndexMidBrandList = launchingList.stream().filter(x -> StringUtils.isNotBlank(x.getProdIdStr())).map(x -> {
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
        /*final List<Long> storeProdIdList = oneMonthList.stream()
                .filter(x -> StringUtils.isNotBlank(x.getProdIdStr())).map(x -> Long.parseLong(x.getProdIdStr())).distinct().collect(Collectors.toList());
        // 档口商品的价格及商品主图map
        Map<Long, StoreProdPriceAndMainPicDTO> prodPriceAndMainPicMap = CollectionUtils.isEmpty(storeProdIdList) ? new ConcurrentHashMap<>()
                : this.storeProdMapper.selectPriceAndMainPicList(storeProdIdList).stream().collect(Collectors
                .toMap(StoreProdPriceAndMainPicDTO::getStoreProdId, Function.identity()));*/



        List<AdvertRound> launchingList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.LAUNCHING.getValue()))
                .filter(x -> Objects.equals(x.getBiddingStatus(), AdBiddingStatus.BIDDING_SUCCESS.getValue())).collect(Collectors.toList());
        List<AdvertRound> expiredList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.EXPIRED.getValue())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(launchingList)) {
            appIndexHotSaleRightFixList = expiredList.stream().filter(x -> StringUtils.isNotBlank(x.getProdIdStr())).map(x -> {
                Long storeProdId = Long.parseLong(x.getProdIdStr());
                return new APPIndexHotSaleRightFixDTO().setDisplayType(AdDisplayType.PRODUCT.getValue()).setStoreProdId(storeProdId)
                        .setMainPicUrl(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getMainPicUrl() : "");
            }).limit(5).collect(Collectors.toList());
            for (int i = 0; i < appIndexHotSaleRightFixList.size(); i++) {
                appIndexHotSaleRightFixList.get(i).setOrderNum(i + 1);
            }
        } else {
            appIndexHotSaleRightFixList = launchingList.stream().filter(x -> StringUtils.isNotBlank(x.getProdIdStr())).map(x -> {
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
        List<AdvertRound> launchingList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.LAUNCHING.getValue()))
                .filter(x -> Objects.equals(x.getBiddingStatus(), AdBiddingStatus.BIDDING_SUCCESS.getValue())).collect(Collectors.toList());
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
        final List<Long> storeProdIdList = oneMonthList.stream()
                .filter(x -> StringUtils.isNotBlank(x.getProdIdStr())).map(x -> Long.parseLong(x.getProdIdStr())).distinct().collect(Collectors.toList());
        // 商品的货号、价格、主图等
        Map<Long, StoreProdPriceAndMainPicAndTagDTO> attrMap = this.getStoreProdAttrMap(storeProdIdList);
        List<AdvertRound> launchingList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.LAUNCHING.getValue()))
                .filter(x -> Objects.equals(x.getBiddingStatus(), AdBiddingStatus.BIDDING_SUCCESS.getValue())).collect(Collectors.toList());
        List<AdvertRound> expiredList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.EXPIRED.getValue())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(launchingList)) {
            appOwnGuessLikeList = expiredList.stream().filter(x -> StringUtils.isNotBlank(x.getProdIdStr())).map(x -> {
                final Long storeProdId = Long.parseLong(x.getProdIdStr());
                StoreProdPriceAndMainPicAndTagDTO dto = attrMap.get(storeProdId);
                return new APPOwnGuessLikeDTO().setDisplayType(AdDisplayType.PRODUCT.getValue()).setStoreProdId(storeProdId)
                        .setTags(ObjectUtils.isNotEmpty(dto) ? dto.getTags() : null)
                        .setPrice(ObjectUtils.isNotEmpty(dto) ? dto.getMinPrice() : null)
                        .setProdArtNum(ObjectUtils.isNotEmpty(dto) ? dto.getProdArtNum() : "")
                        .setMainPicUrl(ObjectUtils.isNotEmpty(dto) ? dto.getMainPicUrl() : "");
            }).limit(20).collect(Collectors.toList());
            for (int i = 0; i < appOwnGuessLikeList.size(); i++) {
                appOwnGuessLikeList.get(i).setOrderNum(i + 1);
            }
        } else {
            appOwnGuessLikeList = launchingList.stream().filter(x -> StringUtils.isNotBlank(x.getProdIdStr())).map(x -> {
                final Long storeProdId = Long.parseLong(x.getProdIdStr());
                StoreProdPriceAndMainPicAndTagDTO dto = attrMap.get(storeProdId);
                return new APPOwnGuessLikeDTO().setDisplayType(AdDisplayType.PRODUCT.getValue()).setStoreProdId(storeProdId).setOrderNum(this.positionToNumber(x.getPosition()))
                        .setTags(ObjectUtils.isNotEmpty(dto) ? dto.getTags() : null)
                        .setPrice(ObjectUtils.isNotEmpty(dto) ? dto.getMinPrice() : null)
                        .setProdArtNum(ObjectUtils.isNotEmpty(dto) ? dto.getProdArtNum() : "")
                        .setMainPicUrl(ObjectUtils.isNotEmpty(dto) ? dto.getMainPicUrl() : "");
            }).limit(20).collect(Collectors.toList());
        }
        // 放到redis中，有效期1天
        redisCache.setCacheObject(CacheConstants.APP_ADVERT + CacheConstants.APP_OWN_GUESS_LIKE, appOwnGuessLikeList, 1, TimeUnit.DAYS);
        return appOwnGuessLikeList;
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
        return advertRoundList.stream().filter(x -> StringUtils.isNotBlank(x.getProdIdStr())).map(x -> {
            final Long storeProdId = Long.parseLong(x.getProdIdStr());
            return new APPIndexMidBrandDTO().setDisplayType(AdDisplayType.PRODUCT.getValue()).setStoreProdId(storeProdId)
                    .setPrice(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getMinPrice() : null)
                    .setMainPicUrl(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getMainPicUrl() : "");
        }).limit(limitCount).collect(Collectors.toList());
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
     * 在指定位置插入广告数据到列表中，若位置超出数据长度，则追加到列表末尾
     *
     * @param dataList  原始数据列表
     * @param adverts   广告数据列表
     * @param positions 插入广告的位置集合
     * @param <T>       数据类型
     * @return 合并后的列表
     */
    public static <T> List<T> insertAdvertsIntoList(List<T> dataList, List<T> adverts, Set<Integer> positions) {
        List<T> mergedList = new ArrayList<>(dataList); // 先拷贝原始数据
        int advertIndex = 0;
        // 遍历所有广告插入位置
        for (Integer position : positions) {
            if (advertIndex >= adverts.size()) {
                // 广告已经插完，结束循环
                break;
            }
            if (position >= 0 && position < mergedList.size()) {
                // 插入位置合法，插入广告
                mergedList.add(position, adverts.get(advertIndex++));
            } else {
                // 插入位置非法（大于等于当前列表长度），追加到末尾
                mergedList.add(adverts.get(advertIndex++));
            }
        }
        return mergedList;
    }

    private static FieldValue newFieldValue(String value) {
        return FieldValue.of(value);
    }

    /**
     * 获取档口商品各种属性
     *
     * @param storeProdIdList 档口商品ID列表
     * @return Map<Long, StoreProdPriceAndMainPicAndTagDTO>
     */
    private Map<Long, StoreProdPriceAndMainPicAndTagDTO> getStoreProdAttrMap(List<Long> storeProdIdList) {
        if (CollectionUtils.isEmpty(storeProdIdList)) {
            return new ConcurrentHashMap<>();
        }
        List<StoreProdPriceAndMainPicAndTagDTO> attrList = storeProdMapper.selectPriceAndMainPicAndTagList(storeProdIdList);
        return attrList.stream()
                .peek(x -> x.setTags(StringUtils.isNotBlank(x.getTagStr()) ? Arrays.asList(x.getTagStr().split(",")) : null))
                .collect(Collectors.toMap(StoreProdPriceAndMainPicAndTagDTO::getStoreProdId, x -> x));
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
                            .map(WebsiteAPPServiceImpl::newFieldValue)
                            .collect(Collectors.toList()))
                    .build();
            boolQuery.filter(f -> f.terms(t -> t.field("prodStatus").terms(termsQueryField)));
        }
        // 添加 prodCateId 过滤条件
        if (CollectionUtils.isNotEmpty(searchDTO.getProdCateIdList())) {
            TermsQueryField termsQueryField = new TermsQueryField.Builder()
                    .value(searchDTO.getProdCateIdList().stream()
                            .map(WebsiteAPPServiceImpl::newFieldValue)
                            .collect(Collectors.toList()))
                    .build();
            boolQuery.filter(f -> f.terms(t -> t.field("prodCateId").terms(termsQueryField)));
        }
        // 添加 parCateId 过滤条件
        if (CollectionUtils.isNotEmpty(searchDTO.getParCateIdList())) {
            TermsQueryField termsQueryField = new TermsQueryField.Builder()
                    .value(searchDTO.getParCateIdList().stream()
                            .map(WebsiteAPPServiceImpl::newFieldValue)
                            .collect(Collectors.toList()))
                    .build();
            boolQuery.filter(f -> f.terms(t -> t.field("parCateId").terms(termsQueryField)));
        }
        // 添加 style 过滤条件
        if (CollectionUtils.isNotEmpty(searchDTO.getStyleList())) {
            TermsQueryField termsQueryField = new TermsQueryField.Builder()
                    .value(searchDTO.getStyleList().stream()
                            .map(WebsiteAPPServiceImpl::newFieldValue)
                            .collect(Collectors.toList()))
                    .build();
            boolQuery.filter(f -> f.terms(t -> t.field("style.keyword").terms(termsQueryField)));
        }
        // 添加 season 过滤条件
        if (CollectionUtils.isNotEmpty(searchDTO.getSeasonList())) {
            TermsQueryField termsQueryField = new TermsQueryField.Builder()
                    .value(searchDTO.getSeasonList().stream()
                            .map(WebsiteAPPServiceImpl::newFieldValue)
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


}
