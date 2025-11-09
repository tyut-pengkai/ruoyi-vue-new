package com.ruoyi.xkt.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.AdType;
import com.ruoyi.xkt.domain.*;
import com.ruoyi.xkt.dto.advertRound.pc.index.*;
import com.ruoyi.xkt.dto.dailySale.CateSaleRankDTO;
import com.ruoyi.xkt.dto.storeProduct.StoreProdPriceAndMainPicDTO;
import com.ruoyi.xkt.dto.storeProductFile.StoreProdFileResDTO;
import com.ruoyi.xkt.enums.AdBiddingStatus;
import com.ruoyi.xkt.enums.AdDisplayType;
import com.ruoyi.xkt.enums.AdLaunchStatus;
import com.ruoyi.xkt.enums.AdStyleType;
import com.ruoyi.xkt.mapper.*;
import com.ruoyi.xkt.service.IWebsitePCIndexService;
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

import static com.ruoyi.common.constant.Constants.TOPMOST_PRODUCT_CATEGORY_ID;

/**
 * 首页搜索
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
@RequiredArgsConstructor
public class WebsitePCIndexServiceImpl implements IWebsitePCIndexService {

    final RedisCache redisCache;
    final AdvertRoundMapper advertRoundMapper;
    final SysFileMapper fileMapper;
    final StoreProductFileMapper prodFileMapper;
    final DailySaleProductMapper dailySaleProdMapper;
    final StoreProductMapper storeProdMapper;
    final StoreMapper storeMapper;
    final SysProductCategoryMapper prodCateMapper;

    /***
     * PC 首页 顶部通栏
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<PCIndexTopBannerDTO> getPcIndexTop() {
        // 从redis 中获取 PC 首页 顶部通栏
        List<PCIndexTopBannerDTO> topBannerList = redisCache.getCacheObject(CacheConstants.PC_ADVERT + CacheConstants.PC_ADVERT_INDEX_TOP);
        if (ObjectUtils.isNotEmpty(topBannerList)) {
            return topBannerList;
        }
        // 正在播放的首页顶部通栏 必须是档口已经设置了推广图
        List<AdvertRound> indexTopList = this.advertRoundMapper.selectList(new LambdaQueryWrapper<AdvertRound>()
                .isNotNull(AdvertRound::getStoreId).isNotNull(AdvertRound::getPicId).eq(AdvertRound::getDelFlag, Constants.UNDELETED)
                .eq(AdvertRound::getTypeId, AdType.PC_HOME_TOPMOST_BANNER.getValue()).eq(AdvertRound::getLaunchStatus, AdLaunchStatus.LAUNCHING.getValue()));
        if (CollectionUtils.isEmpty(indexTopList)) {
            return Collections.emptyList();
        }
        Map<Long, SysFile> fileMap = fileMapper.selectList(new LambdaQueryWrapper<SysFile>().eq(SysFile::getDelFlag, Constants.UNDELETED)
                        .in(SysFile::getId, indexTopList.stream().map(AdvertRound::getPicId).filter(ObjectUtils::isNotEmpty).collect(Collectors.toList())))
                .stream().collect(Collectors.toMap(SysFile::getId, Function.identity()));
        // 顶部通栏数据
        List<PCIndexTopBannerDTO> topList = indexTopList.stream().map(x -> new PCIndexTopBannerDTO().setDisplayType(x.getDisplayType())
                        .setStoreId(x.getStoreId()).setPayPrice(ObjectUtils.defaultIfNull(x.getPayPrice(), BigDecimal.ZERO))
                        .setFileUrl(ObjectUtils.isNotEmpty(x.getPicId()) ? fileMap.get(x.getPicId()).getFileUrl() : null))
                .sorted(Comparator.comparing(PCIndexTopBannerDTO::getPayPrice).reversed())
                .collect(Collectors.toList());
        // 按照价格由高到低进行排序，价格高的排1，其次按照价格排 2 3 4 5
        for (int i = 0; i < topList.size(); i++) {
            topList.get(i).setOrderNum(i + 1);
        }
        // 存放到redis中，过期时间为1天
        redisCache.setCacheObject(CacheConstants.PC_ADVERT + CacheConstants.PC_ADVERT_INDEX_TOP, topList, 1, TimeUnit.DAYS);
        return topList;
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
            return Collections.emptyList();
        }
        // 必须是已经设置了推广图的数据
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
        List<PCIndexTopLeftBannerDTO> topLeftList;
        // 如果顶部横向轮播图全部为空，则需要找填充的数据
        if (CollectionUtils.isEmpty(launchingList)) {
            List<PCIndexTopLeftBannerDTO> tempLeftList = new ArrayList<>();
            expiredList.stream().collect(Collectors.groupingBy(AdvertRound::getStoreId))
                    .forEach((storeId, list) -> {
                        AdvertRound advertRound = list.get(0);
                        tempLeftList.add(new PCIndexTopLeftBannerDTO().setDisplayType(advertRound.getDisplayType()).setStoreId(advertRound.getStoreId())
                                .setPayPrice(ObjectUtils.defaultIfNull(advertRound.getPayPrice(), BigDecimal.ZERO))
                                .setFileUrl(ObjectUtils.isNotEmpty(advertRound.getPicId()) ? fileMap.get(advertRound.getPicId()).getFileUrl() : null));
                    });
            // 只取5条数据
            topLeftList = tempLeftList.stream().limit(5).collect(Collectors.toList());
        } else {
            // 顶部轮播图只要有一张即可
            topLeftList = launchingList.stream().map(x -> new PCIndexTopLeftBannerDTO().setDisplayType(x.getDisplayType())
                            .setStoreId(x.getStoreId()).setPayPrice(ObjectUtils.defaultIfNull(x.getPayPrice(), BigDecimal.ZERO))
                            .setFileUrl(ObjectUtils.isNotEmpty(x.getPicId()) ? fileMap.get(x.getPicId()).getFileUrl() : null))
                    .collect(Collectors.toList());
        }
        // 按照价格由高到低进行排序，价格高的排1，其次按照价格排 2 3 4 5
        topLeftList.sort(Comparator.comparing(PCIndexTopLeftBannerDTO::getPayPrice).reversed());
        for (int i = 0; i < topLeftList.size(); i++) {
            topLeftList.get(i).setOrderNum(i + 1);
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
            return Collections.emptyList();
        }
        // 必须是已经设置了prodIdStr的数据
        oneMonthList = oneMonthList.stream().filter(x -> StringUtils.isNotBlank(x.getProdIdStr())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(oneMonthList)) {
            return Collections.emptyList();
        }
        List<StoreProdFileResDTO> mainPicList = this.prodFileMapper.selectMainPic(oneMonthList.stream().map(AdvertRound::getProdIdStr).distinct().collect(Collectors.toList()));
        Map<Long, String> mainPicMap = mainPicList.stream().collect(Collectors.toMap(StoreProdFileResDTO::getStoreProdId, StoreProdFileResDTO::getFileUrl, (v1, v2) -> v2));
        List<AdvertRound> launchingList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.LAUNCHING.getValue()))
                .filter(x -> Objects.equals(x.getBiddingStatus(), AdBiddingStatus.BIDDING_SUCCESS.getValue())).collect(Collectors.toList());
        List<AdvertRound> expiredList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.EXPIRED.getValue())).collect(Collectors.toList());
        List<PCIndexTopRightBannerDTO> topRightList;
        // 顶部首页纵向轮播图
        if (CollectionUtils.isEmpty(launchingList)) {
            topRightList = this.fillTopRightFromExpired(expiredList, 4, mainPicMap);
        } else {
            topRightList = launchingList.stream().filter(x -> StringUtils.isNotBlank(x.getProdIdStr()))
                    .map(x -> new PCIndexTopRightBannerDTO().setDisplayType(x.getDisplayType()).setStoreProdId(Long.valueOf(x.getProdIdStr()))
                            .setFileUrl(mainPicMap.get(Long.valueOf(x.getProdIdStr())))
                            .setOrderNum(this.positionToNumber(x.getPosition())).setStoreId(x.getStoreId()))
                    .collect(Collectors.toList());
            // 如果 launchingList 只有一个则还需要补充一个推广填空
            if (topRightList.size() < 2) {
                topRightList.addAll(this.fillTopRightFromExpired(expiredList, 1, mainPicMap));
            }
        }
        for (int i = 0; i < topRightList.size(); i++) {
            topRightList.get(i).setOrderNum(i + 1);
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
        // 全量的销量统计数据
        List<CateSaleRankDTO> cateSaleList = this.dailySaleProdMapper.selectSaleRankList(oneMonthAgo, now);
        if (CollectionUtils.isEmpty(cateSaleList)) {
            return new ArrayList<>();
        }
        // 销售榜榜一到榜四的推广
        List<AdvertRound> saleRankList = this.advertRoundMapper.selectList(new LambdaQueryWrapper<AdvertRound>()
                .isNotNull(AdvertRound::getStoreId).isNotNull(AdvertRound::getProdIdStr)
                .ne(AdvertRound::getProdIdStr, "").eq(AdvertRound::getDelFlag, Constants.UNDELETED)
                .eq(AdvertRound::getLaunchStatus, AdLaunchStatus.LAUNCHING.getValue()).in(AdvertRound::getTypeId, Arrays.asList(AdType.PC_HOME_SALE_RANK_ONE,
                        AdType.PC_HOME_SALE_RANK_TWO.getValue(), AdType.PC_HOME_SALE_RANK_THREE.getValue(), AdType.PC_HOME_SALE_RANK_FOUR.getValue())));
        // 销量榜一到榜四的推广 key 1 2 3 4 便于后面取数据
        Map<Integer, List<AdvertRound>> saleAdvertMap = saleRankList.stream().collect(Collectors.groupingBy(ad -> {
            switch (ad.getTypeId()) {
                case 3:
                    return 1;
                case 4:
                    return 2;
                case 5:
                    return 3;
                case 6:
                    return 4;
                default:
                    return ad.getTypeId();
            }
        }));
        // 推广商品ID列表
        final List<Long> advertProdIdList = CollectionUtils.isEmpty(saleRankList) ? Collections.emptyList()
                : saleRankList.stream().map(AdvertRound::getProdIdStr).map(Long::parseLong).collect(Collectors.toList());
        // 销售榜推广销量等信息
        List<CateSaleRankDTO> advertSaleList = cateSaleList.stream().filter(x -> advertProdIdList.contains(x.getStoreProdId())).collect(Collectors.toList());
        Map<Long, CateSaleRankDTO> advertSaleMap = CollectionUtils.isEmpty(advertSaleList) ? new HashMap<>()
                : advertSaleList.stream().collect(Collectors.toMap(CateSaleRankDTO::getStoreProdId, Function.identity()));
        // 所有二级分类ID map
        Map<Long, Long> parCateIdMap = this.getParCateIdMap();
        final List<Long> storeProdIdList = cateSaleList.stream().map(CateSaleRankDTO::getStoreProdId).collect(Collectors.toList());
        // 档口商品的价格及商品主图map
        Map<Long, StoreProdPriceAndMainPicDTO> prodPriceAndMainPicMap = CollectionUtils.isEmpty(storeProdIdList) ? new ConcurrentHashMap<>()
                : this.storeProdMapper.selectPriceAndMainPicList(storeProdIdList).stream().collect(Collectors
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
            // 当前销售榜推广
            final List<AdvertRound> saleAdvertList = saleAdvertMap.getOrDefault(i + 1, Collections.emptyList());
            final List<Long> tempAdvertProdIdList = saleAdvertList.stream().map(AdvertRound::getProdIdStr).map(Long::parseLong).collect(Collectors.toList());
            Long cateId = top4CateEntries.get(i).getKey();
            List<CateSaleRankDTO> cateDetailList = topSaleMap.getOrDefault(cateId, Collections.emptyList());
            // 过滤掉销量为0；并且根据广告数量决定截取的商品数量
            cateDetailList = cateDetailList.stream().filter(x -> ObjectUtils.defaultIfNull(x.getSaleNum(), 0) > 0)
                    .filter(x -> !tempAdvertProdIdList.contains(x.getStoreProdId())).limit(5 - saleAdvertList.size()).collect(Collectors.toList());
            List<PCIndexMidSalesDTO.PCIMSSaleDTO> saleDTOList = new ArrayList<>();
            for (int j = 0; j < cateDetailList.size(); j++) {
                CateSaleRankDTO dto = cateDetailList.get(j);
                // 绑定档口会员等级
                StoreMember member = this.redisCache.getCacheObject(CacheConstants.STORE_MEMBER + dto.getStoreId());
                PCIndexMidSalesDTO.PCIMSSaleDTO saleDTO = new PCIndexMidSalesDTO.PCIMSSaleDTO().setDisplayType(AdDisplayType.PRODUCT.getValue())
                        .setStoreId(dto.getStoreId()).setStoreName(dto.getStoreName()).setStoreProdId(dto.getStoreProdId())
                        .setProdArtNum(dto.getProdArtNum()).setMemberLevel(ObjectUtils.isNotEmpty(member) ? member.getLevel() : null)
                        .setStoreProdId(dto.getStoreProdId()).setSaleNum(dto.getSaleNum()).setOrderNum(j + 1)
                        .setPrice(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(dto.getStoreProdId()))
                                ? prodPriceAndMainPicMap.get(dto.getStoreProdId()).getMinPrice() : null)
                        .setMainPicUrl(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(dto.getStoreProdId()))
                                ? prodPriceAndMainPicMap.get(dto.getStoreProdId()).getMainPicUrl() : "");
                saleDTOList.add(saleDTO);
            }
            for (int j = 0; j < saleAdvertList.size(); j++) {
                CateSaleRankDTO advertSaleDTO = advertSaleMap.get(Long.parseLong(saleAdvertList.get(j).getProdIdStr()));
                // 绑定档口会员等级
                StoreMember member = this.redisCache.getCacheObject(CacheConstants.STORE_MEMBER + advertSaleDTO.getStoreId());
                PCIndexMidSalesDTO.PCIMSSaleDTO saleDTO = new PCIndexMidSalesDTO.PCIMSSaleDTO().setDisplayType(AdDisplayType.PRODUCT.getValue())
                        .setStoreId(advertSaleDTO.getStoreId()).setStoreName(advertSaleDTO.getStoreName()).setStoreProdId(advertSaleDTO.getStoreProdId())
                        .setProdArtNum(advertSaleDTO.getProdArtNum()).setMemberLevel(ObjectUtils.isNotEmpty(member) ? member.getLevel() : null)
                        .setStoreProdId(advertSaleDTO.getStoreProdId()).setSaleNum(advertSaleDTO.getSaleNum()).setOrderNum(j + 1)
                        .setPrice(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(advertSaleDTO.getStoreProdId()))
                                ? prodPriceAndMainPicMap.get(advertSaleDTO.getStoreProdId()).getMinPrice() : null)
                        .setMainPicUrl(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(advertSaleDTO.getStoreProdId()))
                                ? prodPriceAndMainPicMap.get(advertSaleDTO.getStoreProdId()).getMainPicUrl() : "");
                saleDTOList.add(saleDTO);
            }
            retCateOrderList.add(new PCIndexMidSalesDTO().setProdCateId(cateId).setProdCateName(cateIdMap.get(cateId)).setOrderNum(i + 1)
                    .setParCateId(parCateIdMap.get(cateId)).setSaleList(saleDTOList));
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
            return Collections.emptyList();
        }
        // 必须是已经设置推广图和推广商品的列表
        oneMonthList = oneMonthList.stream().filter(x -> ObjectUtils.isNotEmpty(x.getPicId()) && StringUtils.isNotBlank(x.getProdIdStr())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(oneMonthList)) {
            return Collections.emptyList();
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
        // 档口商品的价格及商品主图map
        Map<Long, StoreProdPriceAndMainPicDTO> prodPriceAndMainPicMap = CollectionUtils.isEmpty(storeProdIdSet) ? new ConcurrentHashMap<>()
                : this.storeProdMapper.selectPriceAndMainPicList(new ArrayList<>(storeProdIdSet)).stream().collect(Collectors
                .toMap(StoreProdPriceAndMainPicDTO::getStoreProdId, Function.identity()));
        List<AdvertRound> launchingList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.LAUNCHING.getValue()))
                .filter(x -> Objects.equals(x.getBiddingStatus(), AdBiddingStatus.BIDDING_SUCCESS.getValue())).collect(Collectors.toList());
        List<AdvertRound> expiredList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.EXPIRED.getValue())).collect(Collectors.toList());
        List<PCIndexMidStyleDTO> midStyleList;
        // 顶部 中部 风格轮播图
        if (CollectionUtils.isEmpty(launchingList)) {
            midStyleList = this.fillMidStyleFromExpired(expiredList, prodPriceAndMainPicMap, fileMap, storeMap, 4);
        } else {
            midStyleList = this.fillMidStyleFromExpired(launchingList, prodPriceAndMainPicMap, fileMap, storeMap, 4);
            // 轮播图不足4个，则从过期的广告轮播图补充
            if (midStyleList.size() < 4) {
                midStyleList.addAll(this.fillMidStyleFromExpired(expiredList, prodPriceAndMainPicMap, fileMap, storeMap, 4 - launchingList.size()));
            }
        }
        // 按照价格由高到低进行排序，价格高的排1，其次按照价格排 2 3 4 5
        midStyleList.sort(Comparator.comparing(PCIndexMidStyleDTO::getPayPrice).reversed());
        for (int i = 0; i < midStyleList.size(); i++) {
            midStyleList.get(i).setOrderNum(i + 1);
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
        final List<Long> storeProdIdList = oneMonthList.stream()
                .filter(x -> StringUtils.isNotBlank(x.getProdIdStr())).map(x -> Long.parseLong(x.getProdIdStr())).distinct().collect(Collectors.toList());
        // 档口商品的价格及商品主图map
        Map<Long, StoreProdPriceAndMainPicDTO> prodPriceAndMainPicMap = CollectionUtils.isEmpty(storeProdIdList) ? new ConcurrentHashMap<>()
                : this.storeProdMapper.selectPriceAndMainPicList(storeProdIdList).stream().collect(Collectors
                .toMap(StoreProdPriceAndMainPicDTO::getStoreProdId, Function.identity()));
        // 左侧广告
        List<AdvertRound> leftLaunchingList = oneMonthList.stream().filter(x -> Objects.equals(x.getTypeId(), AdType.PC_HOME_POP_LEFT_BANNER.getValue()))
                .filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.LAUNCHING.getValue()))
                .filter(x -> Objects.equals(x.getBiddingStatus(), AdBiddingStatus.BIDDING_SUCCESS.getValue()))
                // 必须是picId不为空的，防止有的档口购买了不设置推广图
                .filter(x -> ObjectUtils.isNotEmpty(x.getPicId())).collect(Collectors.toList());
        List<AdvertRound> leftExpiredList = oneMonthList.stream().filter(x -> Objects.equals(x.getTypeId(), AdType.PC_HOME_POP_LEFT_BANNER.getValue()))
                .filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.EXPIRED.getValue()))
                // 必须是picId不为空的，防止有的档口购买了不设置推广图
                .filter(x -> ObjectUtils.isNotEmpty(x.getPicId())).collect(Collectors.toList());
        // 中部广告
        List<AdvertRound> midLaunchingList = oneMonthList.stream().filter(x -> Objects.equals(x.getTypeId(), AdType.PC_HOME_POP_MID.getValue()))
                .filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.LAUNCHING.getValue()))
                .filter(x -> Objects.equals(x.getBiddingStatus(), AdBiddingStatus.BIDDING_SUCCESS.getValue()))
                // 必须是picId不为空的，防止有的档口购买了不设置推广图
                .filter(x -> ObjectUtils.isNotEmpty(x.getPicId())).collect(Collectors.toList());
        List<AdvertRound> midExpiredList = oneMonthList.stream().filter(x -> Objects.equals(x.getTypeId(), AdType.PC_HOME_POP_MID.getValue()))
                .filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.EXPIRED.getValue()))
                // 必须是picId不为空的，防止有的档口购买了不设置推广图
                .filter(x -> ObjectUtils.isNotEmpty(x.getPicId())).collect(Collectors.toList());
        // 右侧广告
        List<AdvertRound> rightLaunchingList = oneMonthList.stream().filter(x -> Objects.equals(x.getTypeId(), AdType.PC_HOME_POP_RIGHT.getValue()))
                .filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.LAUNCHING.getValue()))
                .filter(x -> Objects.equals(x.getBiddingStatus(), AdBiddingStatus.BIDDING_SUCCESS.getValue()))
                // 必须是prodIdStr不为空的，防止有的档口购买了不设置推广商品
                .filter(x -> StringUtils.isNotBlank(x.getProdIdStr())).collect(Collectors.toList());
        List<AdvertRound> rightExpiredList = oneMonthList.stream().filter(x -> Objects.equals(x.getTypeId(), AdType.PC_HOME_POP_RIGHT.getValue()))
                .filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.EXPIRED.getValue()))
                // 必须是prodIdStr不为空的，防止有的档口购买了不设置推广商品
                .filter(x -> StringUtils.isNotBlank(x.getProdIdStr())).collect(Collectors.toList());

        List<PCIndexBottomPopularDTO.PCIBPPopularLeftDTO> bottomLeftList;
        // 处理左侧广告列表
        if (CollectionUtils.isEmpty(leftLaunchingList)) {
            List<PCIndexBottomPopularDTO.PCIBPPopularLeftDTO> tempLeftList = new ArrayList<>();
            leftExpiredList.stream().collect(Collectors.groupingBy(AdvertRound::getStoreId))
                    .forEach((storeId, list) -> {
                        AdvertRound advertRound = list.get(0);
                        tempLeftList.add(new PCIndexBottomPopularDTO.PCIBPPopularLeftDTO().setDisplayType(AdDisplayType.PICTURE.getValue()).setStoreId(storeId)
                                .setPayPrice(ObjectUtils.defaultIfNull(advertRound.getPayPrice(), BigDecimal.ZERO))
                                .setFileUrl(ObjectUtils.isNotEmpty(fileMap.get(advertRound.getPicId())) ? fileMap.get(advertRound.getPicId()).getFileUrl() : ""));
                    });
            bottomLeftList = tempLeftList.stream().limit(5).collect(Collectors.toList());
        } else {
            // 人气榜底部左侧轮播图只要有一张即可
            bottomLeftList = leftLaunchingList.stream().map(x -> new PCIndexBottomPopularDTO.PCIBPPopularLeftDTO().setDisplayType(AdDisplayType.PICTURE.getValue())
                            .setStoreId(x.getStoreId()).setPayPrice(ObjectUtils.defaultIfNull(x.getPayPrice(), BigDecimal.ZERO))
                            .setFileUrl(ObjectUtils.isNotEmpty(x.getPicId()) ? fileMap.get(x.getPicId()).getFileUrl() : null))
                    .collect(Collectors.toList());
        }
        // 按照价格由高到低进行排序，价格高的排1，其次按照价格排 2 3 4 5
        bottomLeftList.sort(Comparator.comparing(PCIndexBottomPopularDTO.PCIBPPopularLeftDTO::getPayPrice).reversed());
        for (int i = 0; i < bottomLeftList.size(); i++) {
            bottomLeftList.get(i).setOrderNum(i + 1);
        }
        popularDTO.setLeftList(bottomLeftList);
        // 处理中间广告列表
        List<PCIndexBottomPopularDTO.PCIBPPopularMidDTO> bottomMidList;
        // 处理中间广告列表
        if (CollectionUtils.isEmpty(midLaunchingList)) {
            bottomMidList = this.fillPopMidFromExpired(midExpiredList, fileMap, 2);
        } else {
            bottomMidList = midLaunchingList.stream().map(x -> new PCIndexBottomPopularDTO.PCIBPPopularMidDTO().setDisplayType(AdDisplayType.PICTURE.getValue())
                            .setStoreId(x.getStoreId()).setPayPrice(ObjectUtils.defaultIfNull(x.getPayPrice(), BigDecimal.ZERO))
                            .setFileUrl(ObjectUtils.isNotEmpty(x.getPicId()) ? fileMap.get(x.getPicId()).getFileUrl() : null))
                    .collect(Collectors.toList());
            if (bottomMidList.size() < 2) {
                bottomMidList.addAll(this.fillPopMidFromExpired(midExpiredList, fileMap, 1));
            }
        }
        // 按照价格由高到低进行排序，价格高的排1，其次按照价格排 2 3 4 5
        bottomMidList.sort(Comparator.comparing(PCIndexBottomPopularDTO.PCIBPPopularMidDTO::getPayPrice).reversed());
        for (int i = 0; i < bottomMidList.size(); i++) {
            bottomMidList.get(i).setOrderNum(i + 1);
        }
        popularDTO.setMidList(bottomMidList);
        // 处理右侧广告商品
        List<PCIndexBottomPopularDTO.PCIBPPopularRightDTO> bottomRightList;
        if (CollectionUtils.isEmpty(rightLaunchingList)) {
            bottomRightList = this.fillBottomRightFromExpired(rightExpiredList, prodPriceAndMainPicMap, 2);
        } else {
            bottomRightList = rightLaunchingList.stream().filter(x -> StringUtils.isNotBlank(x.getProdIdStr())).map(x -> {
                final Long storeProdId = Long.parseLong(x.getProdIdStr());
                return new PCIndexBottomPopularDTO.PCIBPPopularRightDTO().setDisplayType(AdDisplayType.PRODUCT.getValue())
                        .setStoreProdId(storeProdId).setOrderNum(this.positionToNumber(x.getPosition())).setStoreId(x.getStoreId())
                        .setPrice(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getMinPrice() : null)
                        .setProdArtNum(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getProdArtNum() : "")
                        .setMainPicUrl(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getMainPicUrl() : "");
            }).collect(Collectors.toList());
            if (bottomRightList.size() < 2) {
                bottomRightList.addAll(this.fillBottomRightFromExpired(rightExpiredList, prodPriceAndMainPicMap, 1));
            }
        }
        for (int i = 0; i < bottomRightList.size(); i++) {
            bottomRightList.get(i).setOrderNum(i + 1);
        }
        popularDTO.setRightList(bottomRightList);
        // 存放到redis中，过期时间为1天
        redisCache.setCacheObject(CacheConstants.PC_ADVERT + CacheConstants.PC_ADVERT_INDEX_BOTTOM_POPULAR, popularDTO, 1, TimeUnit.DAYS);
        return popularDTO;
    }


    /**
     * PC 首页 固定侧边栏 只有购买了该广告位才会显示
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
        // 两侧固定侧边栏必须展示正在播放的广告位，不从历史中获取 且必须已经设置推广图
        AdvertRound fixedEar = this.advertRoundMapper.selectOne(new LambdaQueryWrapper<AdvertRound>()
                .isNotNull(AdvertRound::getStoreId).isNotNull(AdvertRound::getPicId).eq(AdvertRound::getDelFlag, Constants.UNDELETED)
                .eq(AdvertRound::getTypeId, AdType.PC_HOME_FIXED_EAR.getValue()).eq(AdvertRound::getLaunchStatus, AdLaunchStatus.LAUNCHING.getValue()));
        if (ObjectUtils.isEmpty(fixedEar)) {
            return fixedEarDTO;
        }
        Map<Long, SysFile> fileMap = fileMapper.selectList(new LambdaQueryWrapper<SysFile>().eq(SysFile::getDelFlag, Constants.UNDELETED)
                        .in(SysFile::getId, Collections.singletonList(fixedEar.getPicId())))
                .stream().collect(Collectors.toMap(SysFile::getId, Function.identity()));
        fixedEarDTO.setDisplayType(AdDisplayType.PICTURE.getValue()).setStoreId(fixedEar.getStoreId()).setOrderNum(1)
                .setFileUrl(ObjectUtils.isNotEmpty(fixedEar.getPicId()) ? fileMap.get(fixedEar.getPicId()).getFileUrl() : null);
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
        // 搜索框下的档口名称只有档口购买了才会显示
        List<AdvertRound> searchDownStoreNameList = this.advertRoundMapper.selectList(new LambdaQueryWrapper<AdvertRound>()
                .isNotNull(AdvertRound::getStoreId).eq(AdvertRound::getDelFlag, Constants.UNDELETED)
                .in(AdvertRound::getTypeId, Collections.singletonList(AdType.PC_HOME_SEARCH_DOWN_NAME.getValue()))
                .eq(AdvertRound::getLaunchStatus, AdLaunchStatus.LAUNCHING.getValue()));
        if (CollectionUtils.isEmpty(searchDownStoreNameList)) {
            return Collections.emptyList();
        }
        Map<Long, Store> storeMap = this.storeMapper.selectList(new LambdaQueryWrapper<Store>().eq(Store::getDelFlag, Constants.UNDELETED)
                        .in(Store::getId, searchDownStoreNameList.stream().map(AdvertRound::getStoreId).collect(Collectors.toList())))
                .stream().collect(Collectors.toMap(Store::getId, Function.identity()));
        searchStoreNameList = searchDownStoreNameList.stream().map(advertRound -> new PCIndexSearchUnderlineStoreNameDTO()
                        .setPayPrice(ObjectUtils.defaultIfNull(advertRound.getPayPrice(), BigDecimal.ZERO))
                        .setStoreId(advertRound.getStoreId()).setDisplayType(AdDisplayType.STORE_NAME.getValue())
                        .setStoreName(ObjectUtils.isNotEmpty(storeMap.get(advertRound.getStoreId())) ? storeMap.get(advertRound.getStoreId()).getStoreName() : ""))
                .collect(Collectors.toList());
        // 按照价格由高到低进行排序，价格高的排1，其次按照价格排 2 3 4 5
        searchStoreNameList.sort(Comparator.comparing(PCIndexSearchUnderlineStoreNameDTO::getPayPrice).reversed());
        for (int i = 0; i < searchStoreNameList.size(); i++) {
            searchStoreNameList.get(i).setOrderNum(i + 1);
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
        List<PCIndexSearchRecommendProdDTO> redisRecommendList = redisCache.getCacheObject(CacheConstants.PC_ADVERT + CacheConstants.PC_ADVERT_INDEX_SEARCH_RECOMMEND_PROD);
        if (CollectionUtils.isNotEmpty(redisRecommendList)) {
            return redisRecommendList;
        }
        List<AdvertRound> oneMonthList = this.getOneMonthAdvertList(Collections.singletonList(AdType.PC_HOME_SEARCH_PRODUCT.getValue()));
        if (CollectionUtils.isEmpty(oneMonthList)) {
            return Collections.emptyList();
        }
        // 过滤掉prodIdStr为空的数据
        oneMonthList = oneMonthList.stream().filter(x -> StringUtils.isNotBlank(x.getProdIdStr())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(oneMonthList)) {
            return Collections.emptyList();
        }
        List<Store> storeList = this.storeMapper.selectList(new LambdaQueryWrapper<Store>().eq(Store::getDelFlag, Constants.UNDELETED)
                .in(Store::getId, oneMonthList.stream().map(AdvertRound::getStoreId).collect(Collectors.toList())));
        Map<Long, Store> storeMap = CollectionUtils.isEmpty(storeList) ? new HashMap<>()
                : storeList.stream().collect(Collectors.toMap(Store::getId, Function.identity()));
        final List<Long> storeProdIdList = oneMonthList.stream().map(x -> Long.parseLong(x.getProdIdStr())).distinct().collect(Collectors.toList());
        // 档口商品的价格及商品主图map
        Map<Long, StoreProdPriceAndMainPicDTO> prodPriceAndMainPicMap = CollectionUtils.isEmpty(storeProdIdList) ? new ConcurrentHashMap<>()
                : this.storeProdMapper.selectPriceAndMainPicList(storeProdIdList).stream().collect(Collectors
                .toMap(StoreProdPriceAndMainPicDTO::getStoreProdId, Function.identity()));
        // 正在播放
        List<AdvertRound> launchingList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.LAUNCHING.getValue()))
                .filter(x -> Objects.equals(x.getBiddingStatus(), AdBiddingStatus.BIDDING_SUCCESS.getValue())).collect(Collectors.toList());
        // 已过期
        List<AdvertRound> expiredList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.EXPIRED.getValue())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(launchingList)) {
            List<PCIndexSearchRecommendProdDTO> tempList = new ArrayList<>();
            // 从已过期商品中随机选择5个商品
            expiredList.stream().collect(Collectors.groupingBy(AdvertRound::getStoreId))
                    .forEach((storeId, list) -> {
                        AdvertRound advertRound = list.get(0);
                        final Long storeProdId = Long.parseLong(advertRound.getProdIdStr());
                        tempList.add(new PCIndexSearchRecommendProdDTO().setDisplayType(AdDisplayType.PRODUCT.getValue())
                                .setStoreProdId(storeProdId).setStoreId(advertRound.getStoreId()).setPayPrice(advertRound.getPayPrice())
                                .setStoreName(storeMap.containsKey(advertRound.getStoreId()) ? storeMap.get(advertRound.getStoreId()).getStoreName() : "")
                                .setProdArtNum(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getProdArtNum() : "")
                                .setMainPicUrl(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getMainPicUrl() : ""));
                    });
            recommendList = tempList.stream().limit(5).collect(Collectors.toList());
        } else {
            recommendList = launchingList.stream()
                    // 必须是已经设置过推广商品的广告位，防止有些档口购买了广告位但没设置商品情况
                    .filter(x -> StringUtils.isNotBlank(x.getProdIdStr()))
                    .map(advertRound -> {
                        final Long storeProdId = Long.parseLong(advertRound.getProdIdStr());
                        return new PCIndexSearchRecommendProdDTO().setDisplayType(AdDisplayType.PRODUCT.getValue()).setStoreProdId(storeProdId)
                                .setOrderNum(this.positionToNumber(advertRound.getPosition())).setStoreId(advertRound.getStoreId()).setPayPrice(advertRound.getPayPrice())
                                .setStoreName(storeMap.containsKey(advertRound.getStoreId()) ? storeMap.get(advertRound.getStoreId()).getStoreName() : "")
                                .setProdArtNum(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getProdArtNum() : "")
                                .setMainPicUrl(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getMainPicUrl() : "");
                    }).collect(Collectors.toList());
            // 如果当前推广商品数量不足5个，则从已过期商品中补充
            if (recommendList.size() < 5) {
                List<String> existStoreProdIdList = recommendList.stream().map(PCIndexSearchRecommendProdDTO::getStoreProdId).map(String::valueOf).collect(Collectors.toList());
                List<PCIndexSearchRecommendProdDTO> tempList = expiredList.stream()
                        .filter(x -> !existStoreProdIdList.contains(x.getProdIdStr())).limit(5 - recommendList.size())
                        .map(advertRound -> {
                            final Long storeProdId = Long.parseLong(advertRound.getProdIdStr());
                            return new PCIndexSearchRecommendProdDTO().setDisplayType(AdDisplayType.PRODUCT.getValue()).setStoreProdId(storeProdId)
                                    .setOrderNum(this.positionToNumber(advertRound.getPosition())).setStoreId(advertRound.getStoreId()).setPayPrice(advertRound.getPayPrice())
                                    .setStoreName(storeMap.containsKey(advertRound.getStoreId()) ? storeMap.get(advertRound.getStoreId()).getStoreName() : "")
                                    .setProdArtNum(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getProdArtNum() : "")
                                    .setMainPicUrl(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getMainPicUrl() : "");
                        }).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(tempList)) {
                    recommendList.addAll(tempList);
                }
            }
        }
        // 按照出价由高到低排序 出价最高拍1 其次依次排2 3 4 5
        recommendList.sort(Comparator.comparing(PCIndexSearchRecommendProdDTO::getPayPrice).reversed());
        for (int i = 0; i < recommendList.size(); i++) {
            recommendList.get(i).setOrderNum(i + 1);
        }
        // 放到redis中，过期时间为1天
        redisCache.setCacheObject(CacheConstants.PC_ADVERT + CacheConstants.PC_ADVERT_INDEX_SEARCH_RECOMMEND_PROD, recommendList, 1, TimeUnit.DAYS);
        return recommendList;
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
                            .setFileUrl(mainPicMap.get(Long.valueOf(advertRound.getProdIdStr()))).setStoreId(advertRound.getStoreId()));
                });
        return tempRightList.stream().limit(limitCount).collect(Collectors.toList());
    }


    /**
     * 获取父级分类ID Map，如果只有父级一级 则 parCateId和prodCateId一致
     *
     * @return
     */
    private Map<Long, Long> getParCateIdMap() {
        List<SysProductCategory> prodCateList = this.prodCateMapper.selectList(new LambdaQueryWrapper<SysProductCategory>()
                .eq(SysProductCategory::getDelFlag, Constants.UNDELETED)
                // 排除最顶层的父级分类
                .ne(SysProductCategory::getParentId, 0));
        // 所有字分类
        List<SysProductCategory> subCateList = prodCateList.stream().filter(x -> !Objects.equals(x.getParentId(), TOPMOST_PRODUCT_CATEGORY_ID)).collect(Collectors.toList());
        Map<Long, Long> subCateMap = subCateList.stream().collect(Collectors.toMap(SysProductCategory::getId, SysProductCategory::getParentId));
        // 所有的一级分类
        List<Long> topCateList = prodCateList.stream().filter(x -> Objects.equals(x.getParentId(), TOPMOST_PRODUCT_CATEGORY_ID))
                .map(SysProductCategory::getId).collect(Collectors.toList());
        final List<Long> hasSubCateIdList = new ArrayList<>(subCateMap.keySet());
        // 如果只有父类一级，则父类分类ID和字类分类ID一致
        Map<Long, Long> parCateAloneIdMap = topCateList.stream().filter(x -> !hasSubCateIdList.contains(x)).collect(Collectors.toMap(x -> x, x -> x));
        subCateMap.putAll(parCateAloneIdMap);
        return subCateMap;
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
        dbStyleList.stream().filter(x -> StringUtils.isNotBlank(x.getProdIdStr()))
                .collect(Collectors.groupingBy(AdvertRound::getStoreId))
                .forEach((storeId, list) -> {
                    AdvertRound advertRound = list.get(0);
                    List<String> prodIdStrList = StrUtil.split(advertRound.getProdIdStr(), ",");
                    List<PCIndexMidStyleDTO.PCIMSStyleDTO> styleList = new ArrayList<>();
                    for (int i = 0; i < prodIdStrList.size(); i++) {
                        Long storeProdId = Long.valueOf(prodIdStrList.get(i));
                        styleList.add(new PCIndexMidStyleDTO.PCIMSStyleDTO().setStoreProdId(storeProdId).setOrderNum(i + 1).setDisplayType(AdDisplayType.PRODUCT.getValue())
                                .setPrice(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getMinPrice() : null)
                                .setProdArtNum(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getProdArtNum() : "")
                                .setMainPicUrl(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getMainPicUrl() : ""));
                    }
                    StoreMember storeMember = this.redisCache.getCacheObject(CacheConstants.STORE_MEMBER + storeId);
                    midStyleList.add(new PCIndexMidStyleDTO().setStoreId(storeId).setStoreName(storeMap.get(storeId).getStoreName())
                            .setDisplayType(AdDisplayType.PICTURE.getValue()).setStyleList(styleList).setStyleType(advertRound.getStyleType())
                            .setStyleTypeName(ObjectUtils.isNotEmpty(advertRound.getStyleType()) ? AdStyleType.of(advertRound.getStyleType()).getLabel() : "")
                            .setPayPrice(ObjectUtils.defaultIfNull(advertRound.getPayPrice(), BigDecimal.ZERO))
                            .setMemberLevel(ObjectUtils.isNotEmpty(storeMember) ? storeMember.getLevel() : null)
                            .setPicUrl(ObjectUtils.isNotEmpty(fileMap.get(advertRound.getPicId())) ? fileMap.get(advertRound.getPicId()).getFileUrl() : ""));
                });
        return midStyleList.stream().limit(limitCount).collect(Collectors.toList());
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
                            .setPayPrice(ObjectUtils.defaultIfNull(advertRound.getPayPrice(), BigDecimal.ZERO))
                            .setFileUrl(ObjectUtils.isNotEmpty(fileMap.get(advertRound.getPicId())) ? fileMap.get(advertRound.getPicId()).getFileUrl() : ""));
                });
        return tempMidList.stream().limit(limitCount).collect(Collectors.toList());
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
                            .setStoreProdId(Long.valueOf(advertRound.getProdIdStr())).setStoreId(advertRound.getStoreId())
                            .setPrice(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getMinPrice() : null)
                            .setProdArtNum(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getProdArtNum() : "")
                            .setMainPicUrl(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getMainPicUrl() : ""));
                });
        return tempRightList.stream().limit(limitCount).collect(Collectors.toList());
    }


}
