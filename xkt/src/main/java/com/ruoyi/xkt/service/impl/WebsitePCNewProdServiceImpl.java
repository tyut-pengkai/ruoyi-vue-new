package com.ruoyi.xkt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.AdType;
import com.ruoyi.xkt.domain.AdvertRound;
import com.ruoyi.xkt.domain.Store;
import com.ruoyi.xkt.domain.SysFile;
import com.ruoyi.xkt.dto.advertRound.pc.newProd.*;
import com.ruoyi.xkt.dto.storeProduct.StoreProdPriceAndMainPicDTO;
import com.ruoyi.xkt.enums.AdBiddingStatus;
import com.ruoyi.xkt.enums.AdDisplayType;
import com.ruoyi.xkt.enums.AdLaunchStatus;
import com.ruoyi.xkt.mapper.AdvertRoundMapper;
import com.ruoyi.xkt.mapper.StoreMapper;
import com.ruoyi.xkt.mapper.StoreProductMapper;
import com.ruoyi.xkt.mapper.SysFileMapper;
import com.ruoyi.xkt.service.IWebsitePCNewProdService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
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
public class WebsitePCNewProdServiceImpl implements IWebsitePCNewProdService {

    final RedisCache redisCache;
    final AdvertRoundMapper advertRoundMapper;
    final SysFileMapper fileMapper;
    final StoreProductMapper storeProdMapper;
    final StoreMapper storeMapper;

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
            return Collections.emptyList();
        }
        // 必须是已经设置了推广图的，防止有的档口购买了但没有设置图
        oneMonthList = oneMonthList.stream().filter(x -> ObjectUtils.isNotEmpty(x.getPicId())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(oneMonthList)) {
            return Collections.emptyList();
        }
        Map<Long, SysFile> fileMap = fileMapper.selectList(new LambdaQueryWrapper<SysFile>().eq(SysFile::getDelFlag, Constants.UNDELETED)
                        .in(SysFile::getId, oneMonthList.stream().map(AdvertRound::getPicId).filter(ObjectUtils::isNotEmpty).collect(Collectors.toList())))
                .stream().collect(Collectors.toMap(SysFile::getId, Function.identity()));
        List<AdvertRound> launchingList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.LAUNCHING.getValue()))
                .filter(x -> Objects.equals(x.getBiddingStatus(), AdBiddingStatus.BIDDING_SUCCESS.getValue())).collect(Collectors.toList());
        List<AdvertRound> expiredList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.EXPIRED.getValue())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(launchingList)) {
            List<PCNewTopLeftBannerDTO> tempList = new ArrayList<>();
            expiredList.stream().collect(Collectors.groupingBy(AdvertRound::getStoreId))
                    .forEach((storeId, list) -> {
                        AdvertRound advertRound = list.get(0);
                        tempList.add(new PCNewTopLeftBannerDTO().setDisplayType(AdDisplayType.PICTURE.getValue()).setStoreId(advertRound.getStoreId())
                                .setPayPrice(ObjectUtils.defaultIfNull(advertRound.getPayPrice(), BigDecimal.ZERO))
                                .setFileUrl(ObjectUtils.isNotEmpty(fileMap.get(advertRound.getPicId())) ? fileMap.get(advertRound.getPicId()).getFileUrl() : ""));
                    });
            newTopLeftList = tempList.stream().limit(5).collect(Collectors.toList());
        } else {
            newTopLeftList = launchingList.stream().map(advertRound -> new PCNewTopLeftBannerDTO().setDisplayType(AdDisplayType.PICTURE.getValue())
                            .setStoreId(advertRound.getStoreId()).setPayPrice(ObjectUtils.defaultIfNull(advertRound.getPayPrice(), BigDecimal.ZERO))
                            .setFileUrl(ObjectUtils.isNotEmpty(fileMap.get(advertRound.getPicId())) ? fileMap.get(advertRound.getPicId()).getFileUrl() : ""))
                    .collect(Collectors.toList());
        }
        // 按照价格由高到低进行排序，价格高的排1，其次按照价格排 2 3 4 5
        newTopLeftList.sort(Comparator.comparing(PCNewTopLeftBannerDTO::getPayPrice).reversed());
        for (int i = 0; i < newTopLeftList.size(); i++) {
            newTopLeftList.get(i).setOrderNum(i + 1);
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
        // 必须是已经设置了推广图的，防止有的档口购买了但没有设置图
        oneMonthList = oneMonthList.stream().filter(x -> ObjectUtils.isNotEmpty(x.getPicId())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(oneMonthList)) {
            return new PCNewTopRightDTO();
        }
        Map<Long, SysFile> fileMap = fileMapper.selectList(new LambdaQueryWrapper<SysFile>().eq(SysFile::getDelFlag, Constants.UNDELETED)
                        .in(SysFile::getId, oneMonthList.stream().map(AdvertRound::getPicId).filter(ObjectUtils::isNotEmpty).collect(Collectors.toList())))
                .stream().collect(Collectors.toMap(SysFile::getId, Function.identity()));
        List<AdvertRound> launchingList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.LAUNCHING.getValue()))
                .filter(x -> Objects.equals(x.getBiddingStatus(), AdBiddingStatus.BIDDING_SUCCESS.getValue())).collect(Collectors.toList());
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
        // 必须是已经设置了推广图的，防止有的档口购买了但没有设置图
        oneMonthList = oneMonthList.stream().filter(x -> ObjectUtils.isNotEmpty(x.getPicId())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(oneMonthList)) {
            return new ArrayList<>();
        }
        Map<Long, Store> storeMap = this.storeMapper.selectList(new LambdaQueryWrapper<Store>().eq(Store::getDelFlag, Constants.UNDELETED)
                        .in(Store::getId, oneMonthList.stream().map(AdvertRound::getStoreId).collect(Collectors.toList())))
                .stream().collect(Collectors.toMap(Store::getId, Function.identity()));
        Map<Long, SysFile> fileMap = fileMapper.selectList(new LambdaQueryWrapper<SysFile>().eq(SysFile::getDelFlag, Constants.UNDELETED)
                        .in(SysFile::getId, oneMonthList.stream().map(AdvertRound::getPicId).filter(ObjectUtils::isNotEmpty).collect(Collectors.toList())))
                .stream().collect(Collectors.toMap(SysFile::getId, Function.identity()));
        List<AdvertRound> launchingList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.LAUNCHING.getValue()))
                .filter(x -> Objects.equals(x.getBiddingStatus(), AdBiddingStatus.BIDDING_SUCCESS.getValue())).collect(Collectors.toList());
        List<AdvertRound> expiredList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.EXPIRED.getValue())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(launchingList)) {
            newMidBrandList = this.fillNewMidBrandList(expiredList, storeMap, fileMap, new ArrayList<>(), 10);
        } else {
            newMidBrandList = launchingList.stream().map(x -> new PCNewMidBrandDTO().setDisplayType(AdDisplayType.PICTURE.getValue())
                            .setStoreId(x.getStoreId()).setPayPrice(ObjectUtils.defaultIfNull(x.getPayPrice(), BigDecimal.ZERO))
                            .setStoreName(ObjectUtils.isNotEmpty(storeMap.get(x.getStoreId())) ? storeMap.get(x.getStoreId()).getStoreName() : "")
                            .setFileUrl(ObjectUtils.isNotEmpty(fileMap.get(x.getPicId())) ? fileMap.get(x.getPicId()).getFileUrl() : ""))
                    .collect(Collectors.toList());
            if (newMidBrandList.size() < 10) {
                final List<Long> existStoreIdList = newMidBrandList.stream().map(PCNewMidBrandDTO::getStoreId).distinct().collect(Collectors.toList());
                newMidBrandList.addAll(this.fillNewMidBrandList(expiredList, storeMap, fileMap, existStoreIdList, 10 - newMidBrandList.size()));
            }
        }
        // 按照价格由高到低进行排序，价格高的排1，其次按照价格排 2 3 4 5
        newMidBrandList.sort(Comparator.comparing(PCNewMidBrandDTO::getPayPrice).reversed());
        for (int i = 0; i < newMidBrandList.size(); i++) {
            newMidBrandList.get(i).setOrderNum(i + 1);
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
            return Collections.emptyList();
        }
        // 必须是已经设置了推广图的，防止有的档口购买了但没有设置图
        oneMonthList = oneMonthList.stream().filter(x -> ObjectUtils.isNotEmpty(x.getPicId())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(oneMonthList)) {
            return Collections.emptyList();
        }
        Map<Long, SysFile> fileMap = fileMapper.selectList(new LambdaQueryWrapper<SysFile>().eq(SysFile::getDelFlag, Constants.UNDELETED)
                        .in(SysFile::getId, oneMonthList.stream().map(AdvertRound::getPicId).filter(ObjectUtils::isNotEmpty).collect(Collectors.toList())))
                .stream().collect(Collectors.toMap(SysFile::getId, Function.identity()));
        List<AdvertRound> launchingList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.LAUNCHING.getValue()))
                .filter(x -> Objects.equals(x.getBiddingStatus(), AdBiddingStatus.BIDDING_SUCCESS.getValue())).collect(Collectors.toList());
        List<AdvertRound> expiredList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.EXPIRED.getValue())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(launchingList)) {
            List<PCNewMidHotLeftDTO> tempList = new ArrayList<>();
            expiredList.stream().collect(Collectors.groupingBy(AdvertRound::getStoreId))
                    .forEach((storeId, list) -> {
                        AdvertRound advertRound = list.get(0);
                        tempList.add(new PCNewMidHotLeftDTO().setDisplayType(AdDisplayType.PICTURE.getValue()).setStoreId(storeId)
                                .setPayPrice(ObjectUtils.defaultIfNull(advertRound.getPayPrice(), BigDecimal.ZERO))
                                .setFileUrl(ObjectUtils.isNotEmpty(fileMap.get(advertRound.getPicId())) ? fileMap.get(advertRound.getPicId()).getFileUrl() : ""));
                    });
            newMidHotLeftList = tempList.stream().limit(5).collect(Collectors.toList());
        } else {
            newMidHotLeftList = launchingList.stream().map(x -> new PCNewMidHotLeftDTO().setDisplayType(AdDisplayType.PICTURE.getValue())
                            .setStoreId(x.getStoreId()).setPayPrice(ObjectUtils.defaultIfNull(x.getPayPrice(), BigDecimal.ZERO))
                            .setFileUrl(ObjectUtils.isNotEmpty(fileMap.get(x.getPicId())) ? fileMap.get(x.getPicId()).getFileUrl() : ""))
                    .collect(Collectors.toList());
        }
        // 按照价格由高到低进行排序，价格高的排1，其次按照价格排 2 3 4 5
        newMidHotLeftList.sort(Comparator.comparing(PCNewMidHotLeftDTO::getPayPrice).reversed());
        for (int i = 0; i < newMidHotLeftList.size(); i++) {
            newMidHotLeftList.get(i).setOrderNum(i + 1);
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
        // 必须是已经设置了推广商品的，防止有的档口购买了但没有设置推广商品
        oneMonthList = oneMonthList.stream().filter(x -> StringUtils.isNotBlank(x.getProdIdStr())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(oneMonthList)) {
            return new ArrayList<>();
        }
        List<AdvertRound> launchingList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.LAUNCHING.getValue()))
                .filter(x -> Objects.equals(x.getBiddingStatus(), AdBiddingStatus.BIDDING_SUCCESS.getValue())).collect(Collectors.toList());
        List<AdvertRound> expiredList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.EXPIRED.getValue())).collect(Collectors.toList());
        final List<Long> storeProdIdList = oneMonthList.stream()
                .filter(x -> StringUtils.isNotBlank(x.getProdIdStr())).map(x -> Long.parseLong(x.getProdIdStr())).distinct().collect(Collectors.toList());
        // 档口商品的价格及商品主图map
        Map<Long, StoreProdPriceAndMainPicDTO> prodPriceAndMainPicMap = CollectionUtils.isEmpty(storeProdIdList) ? new ConcurrentHashMap<>()
                : this.storeProdMapper.selectPriceAndMainPicList(storeProdIdList).stream().collect(Collectors
                .toMap(StoreProdPriceAndMainPicDTO::getStoreProdId, Function.identity()));
        if (CollectionUtils.isEmpty(launchingList)) {
            newMidHotRightList = this.fillNewMidHotRightList(expiredList, prodPriceAndMainPicMap, new ArrayList<>(), 8);
            for (int i = 0; i < newMidHotRightList.size(); i++) {
                newMidHotRightList.get(i).setOrderNum(i + 1);
            }
        } else {
            newMidHotRightList = launchingList.stream().filter(x -> StringUtils.isNotBlank(x.getProdIdStr())).map(x -> {
                final Long storeProdId = Long.parseLong(x.getProdIdStr());
                return new PCNewMidHotRightDTO().setDisplayType(AdDisplayType.PRODUCT.getValue())
                        .setOrderNum(this.positionToNumber(x.getPosition())).setStoreProdId(storeProdId).setStoreId(x.getStoreId())
                        .setPrice(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getMinPrice() : null)
                        .setProdArtNum(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getProdArtNum() : "")
                        .setMainPicUrl(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getMainPicUrl() : "");
            }).collect(Collectors.toList());
            if (newMidHotRightList.size() < 8) {
                final List<Long> existProdIdList = newMidHotRightList.stream().map(PCNewMidHotRightDTO::getStoreProdId).collect(Collectors.toList());
                newMidHotRightList.addAll(this.fillNewMidHotRightList(expiredList, prodPriceAndMainPicMap, existProdIdList, 8 - newMidHotRightList.size()));
                for (int i = 0; i < newMidHotRightList.size(); i++) {
                    newMidHotRightList.get(i).setOrderNum(i + 1);
                }
            }
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
            return Collections.emptyList();
        }
        // 必须是已经设置了推广图的，防止有的档口购买了推广但没有设置推广图
        oneMonthList = oneMonthList.stream().filter(x -> ObjectUtils.isNotEmpty(x.getPicId())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(oneMonthList)) {
            return Collections.emptyList();
        }
        List<AdvertRound> launchingList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.LAUNCHING.getValue()))
                .filter(x -> Objects.equals(x.getBiddingStatus(), AdBiddingStatus.BIDDING_SUCCESS.getValue())).collect(Collectors.toList());
        List<AdvertRound> expiredList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.EXPIRED.getValue())).collect(Collectors.toList());
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
     * 填充 PC 首页新品馆 品牌榜
     *
     * @param advertRoundList  广告 列表
     * @param storeMap         档口map
     * @param fileMap          推广图map
     * @param existStoreIdList 已存在的档口ID列表
     * @param limitCount       填充数量
     * @return List<PCNewMidBrandDTO>
     */
    private List<PCNewMidBrandDTO> fillNewMidBrandList(List<AdvertRound> advertRoundList, Map<Long, Store> storeMap,
                                                       Map<Long, SysFile> fileMap, List<Long> existStoreIdList, int limitCount) {
        List<PCNewMidBrandDTO> tempList = new ArrayList<>();
        advertRoundList.stream().collect(Collectors.groupingBy(AdvertRound::getStoreId))
                .forEach((storeId, list) -> {
                    if (existStoreIdList.contains(storeId)) {
                        return;
                    }
                    AdvertRound advertRound = list.get(0);
                    tempList.add(new PCNewMidBrandDTO().setDisplayType(AdDisplayType.PICTURE.getValue()).setStoreId(storeId)
                            .setStoreName(storeMap.get(storeId).getStoreName())
                            .setPayPrice(ObjectUtils.defaultIfNull(advertRound.getPayPrice(), BigDecimal.ZERO))
                            .setFileUrl(ObjectUtils.isNotEmpty(fileMap.get(advertRound.getPicId())) ? fileMap.get(advertRound.getPicId()).getFileUrl() : ""));
                });
        return tempList.stream().limit(limitCount).collect(Collectors.toList());
    }

    /**
     * 填充 PC 新品馆 中间热卖榜右侧商品
     *
     * @param advertRoundList        广告列表
     * @param prodPriceAndMainPicMap 商品价格和主图map
     * @param existProdIdList        已存在的 商品ID列表
     * @param limitCount             返回的 数量
     * @return
     */
    private List<PCNewMidHotRightDTO> fillNewMidHotRightList(List<AdvertRound> advertRoundList, Map<Long, StoreProdPriceAndMainPicDTO> prodPriceAndMainPicMap,
                                                             List<Long> existProdIdList, int limitCount) {
        List<PCNewMidHotRightDTO> tempList = new ArrayList<>();
        advertRoundList.stream().collect(Collectors.groupingBy(AdvertRound::getStoreId))
                .forEach((storeId, list) -> {
                    AdvertRound advertRound = list.stream().filter(x -> StringUtils.isNotBlank(x.getProdIdStr()))
                            .filter(x -> CollectionUtils.isEmpty(existProdIdList) || !existProdIdList.contains(Long.parseLong(x.getProdIdStr()))).findAny().orElse(null);
                    if (ObjectUtils.isNotEmpty(advertRound)) {
                        final Long storeProdId = Long.parseLong(advertRound.getProdIdStr());
                        tempList.add(new PCNewMidHotRightDTO().setDisplayType(AdDisplayType.PRODUCT.getValue()).setStoreProdId(storeProdId).setStoreId(storeId)
                                .setPrice(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getMinPrice() : null)
                                .setProdArtNum(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getProdArtNum() : "")
                                .setMainPicUrl(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getMainPicUrl() : ""));
                    }
                });
        return tempList.stream().limit(limitCount).collect(Collectors.toList());
    }

}
