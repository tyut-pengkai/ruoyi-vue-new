package com.ruoyi.xkt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.AdType;
import com.ruoyi.xkt.domain.AdvertRound;
import com.ruoyi.xkt.domain.SysFile;
import com.ruoyi.xkt.dto.advertRound.pc.store.PCStoreMidBannerDTO;
import com.ruoyi.xkt.dto.advertRound.pc.store.PCStoreTopBannerDTO;
import com.ruoyi.xkt.dto.storeProduct.StoreProdPriceAndMainPicDTO;
import com.ruoyi.xkt.enums.AdBiddingStatus;
import com.ruoyi.xkt.enums.AdDisplayType;
import com.ruoyi.xkt.enums.AdLaunchStatus;
import com.ruoyi.xkt.mapper.AdvertRoundMapper;
import com.ruoyi.xkt.mapper.StoreProductMapper;
import com.ruoyi.xkt.mapper.SysFileMapper;
import com.ruoyi.xkt.service.IPictureService;
import com.ruoyi.xkt.service.IWebsitePCStoreService;
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
public class WebsitePCStoreServiceImpl implements IWebsitePCStoreService {

    final RedisCache redisCache;
    final AdvertRoundMapper advertRoundMapper;
    final SysFileMapper fileMapper;
    final StoreProductMapper storeProdMapper;
    final IPictureService pictureService;


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
        // 必须是已经设置了推广图及推广商品的，防止有的档口购买了推广位但没上传推广图及推广商品
        oneMonthList = oneMonthList.stream().filter(x -> ObjectUtils.isNotEmpty(x.getPicId()) && ObjectUtils.isNotEmpty(x.getProdIdStr())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(oneMonthList)) {
            return new ArrayList<>();
        }
        List<AdvertRound> launchingList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.LAUNCHING.getValue()))
                .filter(x -> Objects.equals(x.getBiddingStatus(), AdBiddingStatus.BIDDING_SUCCESS.getValue())).collect(Collectors.toList());
        List<AdvertRound> expiredList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.EXPIRED.getValue())).collect(Collectors.toList());
        Map<Long, SysFile> fileMap = fileMapper.selectList(new LambdaQueryWrapper<SysFile>().eq(SysFile::getDelFlag, Constants.UNDELETED)
                        .in(SysFile::getId, oneMonthList.stream().map(AdvertRound::getPicId).filter(ObjectUtils::isNotEmpty).collect(Collectors.toList())))
                .stream().collect(Collectors.toMap(SysFile::getId, Function.identity()));
        final List<Long> storeProdIdList = oneMonthList.stream().filter(x -> StringUtils.isNotBlank(x.getProdIdStr()))
                .flatMap(x -> Arrays.stream(x.getProdIdStr().split(",")).map(Long::parseLong)).distinct().collect(Collectors.toList());
        // 档口商品的价格及商品主图map
        Map<Long, StoreProdPriceAndMainPicDTO> prodPriceAndMainPicMap = CollectionUtils.isEmpty(storeProdIdList) ? new ConcurrentHashMap<>()
                : this.storeProdMapper.selectPriceAndMainPicList(storeProdIdList).stream().collect(Collectors
                .toMap(StoreProdPriceAndMainPicDTO::getStoreProdId, Function.identity()));
        if (CollectionUtils.isEmpty(launchingList)) {
            List<PCStoreTopBannerDTO> tempList = new ArrayList<>();
            expiredList.stream().collect(Collectors.groupingBy(AdvertRound::getStoreId))
                    .forEach((storeId, list) -> {
                        AdvertRound advertRound = list.get(0);
                        tempList.add(this.getStoreTopBanner(advertRound, fileMap, prodPriceAndMainPicMap));
                    });
            pcStoreTopBannerList = tempList.stream().limit(1).collect(Collectors.toList());
        } else {
            pcStoreTopBannerList = launchingList.stream().map(advertRound -> this
                    .getStoreTopBanner(advertRound, fileMap, prodPriceAndMainPicMap)).collect(Collectors.toList());
        }
        // 按照价格由高到低进行排序，价格高的排1，其次按照价格排 2 3 4 5
        pcStoreTopBannerList.sort(Comparator.comparing(PCStoreTopBannerDTO::getPayPrice).reversed());
        for (int i = 0; i < pcStoreTopBannerList.size(); i++) {
            pcStoreTopBannerList.get(i).setOrderNum(i + 1);
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
        // 必须是经设置了推广图的，防止有的档口购买了广告但没上传推广图
        oneMonthList = oneMonthList.stream().filter(x -> ObjectUtils.isNotEmpty(x.getPicId())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(oneMonthList)) {
            return new ArrayList<>();
        }
        List<AdvertRound> launchingList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.LAUNCHING.getValue()))
                .filter(x -> Objects.equals(x.getBiddingStatus(), AdBiddingStatus.BIDDING_SUCCESS.getValue())).collect(Collectors.toList());
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
                                .setPayPrice(ObjectUtils.defaultIfNull(advertRound.getPayPrice(), BigDecimal.ZERO))
                                .setFileUrl(ObjectUtils.isNotEmpty(fileMap.get(advertRound.getPicId())) ? fileMap.get(advertRound.getPicId()).getFileUrl() : ""));
                    });
            pcStoreMidBannerList = tempList.stream().limit(1).collect(Collectors.toList());
        } else {
            pcStoreMidBannerList = launchingList.stream().map(advertRound -> new PCStoreMidBannerDTO().setStoreId(advertRound.getStoreId())
                            .setDisplayType(AdDisplayType.PICTURE.getValue()).setPayPrice(ObjectUtils.defaultIfNull(advertRound.getPayPrice(), BigDecimal.ZERO))
                            .setFileUrl(ObjectUtils.isNotEmpty(fileMap.get(advertRound.getPicId())) ? fileMap.get(advertRound.getPicId()).getFileUrl() : ""))
                    .collect(Collectors.toList());
        }
        // 按照价格由高到低进行排序，价格高的排1，其次按照价格排 2 3 4 5
        pcStoreMidBannerList.sort(Comparator.comparing(PCStoreMidBannerDTO::getPayPrice).reversed());
        for (int i = 0; i < pcStoreMidBannerList.size(); i++) {
            pcStoreMidBannerList.get(i).setOrderNum(i + 1);
        }
        // 放到redis 中 过期时间1天
        redisCache.setCacheObject(CacheConstants.PC_ADVERT + CacheConstants.PC_ADVERT_STORE_MID_BANNER, pcStoreMidBannerList, 1, TimeUnit.DAYS);
        return pcStoreMidBannerList;
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
     * 获取档口馆 顶部横幅及商品
     *
     * @param advertRound            当前推广
     * @param fileMap                推广图map
     * @param prodPriceAndMainPicMap 商品主图及价格map
     * @return PCStoreTopBannerDTO
     */
    private PCStoreTopBannerDTO getStoreTopBanner(AdvertRound advertRound, Map<Long, SysFile> fileMap, Map<Long, StoreProdPriceAndMainPicDTO> prodPriceAndMainPicMap) {
        List<PCStoreTopBannerDTO.PCSTBProdDTO> prodList = new ArrayList<>();
        if (StringUtils.isNotBlank(advertRound.getProdIdStr())) {
            String[] prodIdStrArray = advertRound.getProdIdStr().split(",");
            for (int i = 0; i < prodIdStrArray.length; i++) {
                Long tempStoreProdId = Long.valueOf(prodIdStrArray[i]);
                prodList.add(new PCStoreTopBannerDTO.PCSTBProdDTO().setDisplayType(AdDisplayType.PRODUCT.getValue()).setStoreProdId(tempStoreProdId).setOrderNum(i + 1)
                        .setPrice(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(tempStoreProdId)) ? prodPriceAndMainPicMap.get(tempStoreProdId).getMinPrice() : null)
                        .setProdArtNum(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(tempStoreProdId)) ? prodPriceAndMainPicMap.get(tempStoreProdId).getProdArtNum() : "")
                        .setMainPicUrl(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(tempStoreProdId)) ? prodPriceAndMainPicMap.get(tempStoreProdId).getMainPicUrl() : ""));
            }
        }
        return new PCStoreTopBannerDTO().setDisplayType(AdDisplayType.PICTURE.getValue()).setStoreId(advertRound.getStoreId())
                .setProdList(prodList).setPayPrice(ObjectUtils.defaultIfNull(advertRound.getPayPrice(), BigDecimal.ZERO))
                .setFileUrl(ObjectUtils.isNotEmpty(fileMap.get(advertRound.getPicId())) ? fileMap.get(advertRound.getPicId()).getFileUrl() : "");
    }


}
