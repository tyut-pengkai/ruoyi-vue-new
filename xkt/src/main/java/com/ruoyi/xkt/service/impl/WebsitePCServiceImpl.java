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
import com.ruoyi.xkt.domain.DailyStoreTag;
import com.ruoyi.xkt.domain.Store;
import com.ruoyi.xkt.domain.SysFile;
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
import com.ruoyi.xkt.dto.picture.ProductMatchDTO;
import com.ruoyi.xkt.dto.storeProduct.StoreProdPriceAndMainPicAndTagDTO;
import com.ruoyi.xkt.dto.storeProduct.StoreProdPriceAndMainPicDTO;
import com.ruoyi.xkt.dto.storeProduct.StoreProdViewDTO;
import com.ruoyi.xkt.dto.storeProductFile.StoreProdFileResDTO;
import com.ruoyi.xkt.dto.website.IndexSearchDTO;
import com.ruoyi.xkt.enums.AdBiddingStatus;
import com.ruoyi.xkt.enums.AdDisplayType;
import com.ruoyi.xkt.enums.AdLaunchStatus;
import com.ruoyi.xkt.mapper.*;
import com.ruoyi.xkt.service.IPictureService;
import com.ruoyi.xkt.service.IWebsitePCService;
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
public class WebsitePCServiceImpl implements IWebsitePCService {

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
    final IPictureService pictureService;

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
            redisList = redisList.stream()
                    .filter(x -> CollectionUtils.isEmpty(searchDTO.getParCateIdList()) || searchDTO.getParCateIdList().contains(x.getParCateId().toString()))
                    .filter(x -> CollectionUtils.isEmpty(searchDTO.getProdCateIdList()) || searchDTO.getProdCateIdList().contains(x.getProdCateId().toString()))
                    .collect(Collectors.toList());
            // 推广数据排在最前面，其次才是真实的数据
            CollectionUtils.addAll(redisList, realDataList);
            // 添加广告的数据（PC的规则是将所有的广告数据全部放到最前面展示，不用给广告打标）
            return new Page<>(page.getPageNum(), page.getPageSize(), page.getPages(), page.getTotal(), redisList);
        } else {
            // 从数据库查首页 推荐商品 推广（精准搜索是否存在推广，不存在从已过期的数据中拉数据来凑数）而且必须是 竞价成功的推广（有可能当天有新的竞价推广还未正式审核通过）
            List<AdvertRound> advertRoundList = this.advertRoundMapper.selectList(new LambdaQueryWrapper<AdvertRound>()
                    .isNotNull(AdvertRound::getStoreId).eq(AdvertRound::getDelFlag, Constants.UNDELETED)
                    .eq(AdvertRound::getTypeId, AdType.PC_HOME_PRODUCT_LIST.getValue())
                    .eq(AdvertRound::getLaunchStatus, AdLaunchStatus.LAUNCHING.getValue())
                    .eq(AdvertRound::getBiddingStatus, AdBiddingStatus.BIDDING_SUCCESS.getValue()));
            if (CollectionUtils.isNotEmpty(advertRoundList)) {
                List<StoreProdPriceAndMainPicAndTagDTO> attrList = storeProdMapper.selectPriceAndMainPicAndTagList(advertRoundList.stream().filter(x -> StringUtils.isNotBlank(x.getProdIdStr()))
                        .map(x -> x.getProdIdStr().split(",")).flatMap(Arrays::stream).map(Long::valueOf).distinct().collect(Collectors.toList()));
                attrList = attrList.stream().peek(x -> x.setTags(StringUtils.isNotBlank(x.getTagStr()) ? Arrays.asList(x.getTagStr().split(",")) : null)).collect(Collectors.toList());
                Map<Long, StoreProdPriceAndMainPicAndTagDTO> attrMap = attrList.stream().collect(Collectors.toMap(StoreProdPriceAndMainPicAndTagDTO::getStoreProdId, x -> x));
                List<PCIndexRecommendDTO> indexRecommendList = new ArrayList<>();
                advertRoundList.stream().filter(x -> StringUtils.isNotBlank(x.getProdIdStr())).forEach(x -> {
                    // 这里是一个档口上传多个档口商品，所以需要对prodIdStr的逗号进行分割
                    List<Long> prodIdList = StrUtil.split(x.getProdIdStr(), ",").stream().map(Long::parseLong).collect(Collectors.toList());
                    prodIdList.forEach(storeProdId -> {
                        StoreProdPriceAndMainPicAndTagDTO attrDto = attrMap.get(storeProdId);
                        indexRecommendList.add(new PCIndexRecommendDTO().setAdvert(Boolean.TRUE).setStoreId(x.getStoreId().toString())
                                .setParCateId(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getParCateId() : null)
                                .setProdCateId(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getProdCateId() : null)
                                .setStoreProdId(storeProdId.toString()).setTags(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getTags() : null)
                                .setStoreName(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getStoreName() : "")
                                .setProdPrice(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getMinPrice().toString() : null)
                                .setProdArtNum(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getProdArtNum() : "")
                                .setMainPic(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getMainPicUrl() : "")
                                .setTags(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getTags() : null));
                    });
                });
                // 将indexRecommendList 顺序打乱，不然一个档口的数据在同一地方展示
                Collections.shuffle(indexRecommendList);
                // 放到redis中 有效期1天
                this.redisCache.setCacheObject(CacheConstants.PC_ADVERT + CacheConstants.PC_INDEX_RECOMMEND, indexRecommendList, 1, TimeUnit.DAYS);
                List<PCIndexRecommendDTO> tempList = indexRecommendList.stream()
                        .filter(x -> CollectionUtils.isEmpty(searchDTO.getParCateIdList()) || searchDTO.getParCateIdList().contains(x.getParCateId().toString()))
                        .filter(x -> CollectionUtils.isEmpty(searchDTO.getProdCateIdList()) || searchDTO.getProdCateIdList().contains(x.getProdCateId().toString()))
                        .collect(Collectors.toList());
                CollectionUtils.addAll(tempList, realDataList);
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
            redisList = redisList.stream()
                    .filter(x -> CollectionUtils.isEmpty(searchDTO.getParCateIdList()) || searchDTO.getParCateIdList().contains(x.getParCateId().toString()))
                    .filter(x -> CollectionUtils.isEmpty(searchDTO.getProdCateIdList()) || searchDTO.getProdCateIdList().contains(x.getProdCateId().toString()))
                    .collect(Collectors.toList());
            // 推广数据排在最前面，其次才是真实的数据
            CollectionUtils.addAll(redisList, realDataList);
            // 添加广告的数据（PC的规则是将所有的广告数据全部放到最前面展示，不用给广告打标）
            return new Page<>(page.getPageNum(), page.getPageSize(), page.getPages(), page.getTotal(), redisList);
        } else {
            // 从数据库查新品馆 推荐商品 推广（精准搜索是否存在推广，不存在从已过期的数据中拉数据来凑数）
            List<AdvertRound> advertRoundList = this.advertRoundMapper.selectList(new LambdaQueryWrapper<AdvertRound>()
                    .isNotNull(AdvertRound::getStoreId).eq(AdvertRound::getDelFlag, Constants.UNDELETED)
                    .eq(AdvertRound::getTypeId, AdType.PC_NEW_PROD_PRODUCT_LIST.getValue())
                    .eq(AdvertRound::getLaunchStatus, AdLaunchStatus.LAUNCHING.getValue())
                    .eq(AdvertRound::getBiddingStatus, AdBiddingStatus.BIDDING_SUCCESS.getValue()));
            if (CollectionUtils.isNotEmpty(advertRoundList)) {
                List<StoreProdPriceAndMainPicAndTagDTO> attrList = storeProdMapper.selectPriceAndMainPicAndTagList(advertRoundList.stream().filter(x -> StringUtils.isNotBlank(x.getProdIdStr()))
                        .map(x -> x.getProdIdStr().split(",")).flatMap(Arrays::stream).map(Long::valueOf).distinct().collect(Collectors.toList()));
                attrList = attrList.stream().peek(x -> x.setTags(StringUtils.isNotBlank(x.getTagStr()) ? Arrays.asList(x.getTagStr().split(",")) : null)).collect(Collectors.toList());
                Map<Long, StoreProdPriceAndMainPicAndTagDTO> attrMap = attrList.stream().collect(Collectors.toMap(StoreProdPriceAndMainPicAndTagDTO::getStoreProdId, x -> x));
                List<PCNewRecommendDTO> newRecommendList = new ArrayList<>();
                advertRoundList.stream().filter(x -> StringUtils.isNotBlank(x.getProdIdStr())).forEach(x -> {
                    // 这里是一个档口上传多个档口商品，所以需要对prodIdStr的逗号进行分割
                    List<Long> prodIdList = Arrays.stream(x.getProdIdStr().split(",")).map(Long::parseLong).collect(Collectors.toList());
                    prodIdList.forEach(storeProdId -> {
                        StoreProdPriceAndMainPicAndTagDTO attrDto = attrMap.get(storeProdId);
                        newRecommendList.add(new PCNewRecommendDTO().setAdvert(Boolean.TRUE).setStoreId(x.getStoreId().toString())
                                .setParCateId(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getParCateId() : null)
                                .setProdCateId(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getProdCateId() : null)
                                .setStoreProdId(storeProdId.toString()).setTags(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getTags() : null)
                                .setStoreName(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getStoreName() : "")
                                .setProdPrice(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getMinPrice().toString() : null)
                                .setProdArtNum(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getProdArtNum() : "")
                                .setMainPic(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getMainPicUrl() : "")
                                .setTags(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getTags() : null));
                    });
                });
                // newRecommendList 顺序打乱，不然一个档口的数据在同一地方展示
                Collections.shuffle(newRecommendList);
                // 放到redis中 有效期1天
                this.redisCache.setCacheObject(CacheConstants.PC_ADVERT + CacheConstants.PC_NEW_RECOMMEND, newRecommendList, 1, TimeUnit.DAYS);
                List<PCNewRecommendDTO> tempList = newRecommendList.stream()
                        .filter(x -> CollectionUtils.isEmpty(searchDTO.getParCateIdList()) || searchDTO.getParCateIdList().contains(x.getParCateId().toString()))
                        .filter(x -> CollectionUtils.isEmpty(searchDTO.getProdCateIdList()) || searchDTO.getProdCateIdList().contains(x.getProdCateId().toString()))
                        .collect(Collectors.toList());
                CollectionUtils.addAll(tempList, realDataList);
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
        List<StoreProdFileResDTO> mainPicList = this.prodFileMapper.selectMainPic(oneMonthList.stream().map(AdvertRound::getProdIdStr).distinct().collect(Collectors.toList()));
        Map<Long, String> mainPicMap = mainPicList.stream().collect(Collectors.toMap(StoreProdFileResDTO::getStoreProdId, StoreProdFileResDTO::getFileUrl, (v1, v2) -> v2));
        List<AdvertRound> launchingList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.LAUNCHING.getValue()))
                .filter(x -> Objects.equals(x.getBiddingStatus(), AdBiddingStatus.BIDDING_SUCCESS.getValue())).collect(Collectors.toList());
        List<AdvertRound> expiredList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.EXPIRED.getValue())).collect(Collectors.toList());
        List<PCIndexTopRightBannerDTO> topRightList;
        // 顶部首页纵向轮播图
        if (CollectionUtils.isEmpty(launchingList)) {
            topRightList = this.fillTopRightFromExpired(expiredList, 4, mainPicMap);
            for (int i = 0; i < topRightList.size(); i++) {
                topRightList.get(i).setOrderNum(i + 1);
            }
        } else {
            topRightList = launchingList.stream().filter(x -> StringUtils.isNotBlank(x.getProdIdStr()))
                    .map(x -> new PCIndexTopRightBannerDTO().setDisplayType(x.getDisplayType()).setStoreProdId(Long.valueOf(x.getProdIdStr()))
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
        final List<Long> storeProdIdList = oneMonthList.stream()
                .filter(x -> StringUtils.isNotBlank(x.getProdIdStr())).map(x -> Long.parseLong(x.getProdIdStr())).distinct().collect(Collectors.toList());
        // 档口商品的价格及商品主图map
        Map<Long, StoreProdPriceAndMainPicDTO> prodPriceAndMainPicMap = CollectionUtils.isEmpty(storeProdIdList) ? new ConcurrentHashMap<>()
                : this.storeProdMapper.selectPriceAndMainPicList(storeProdIdList).stream().collect(Collectors
                .toMap(StoreProdPriceAndMainPicDTO::getStoreProdId, Function.identity()));
        // 左侧广告
        List<AdvertRound> leftLaunchingList = oneMonthList.stream().filter(x -> Objects.equals(x.getTypeId(), AdType.PC_HOME_POP_LEFT_BANNER.getValue()))
                .filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.LAUNCHING.getValue()))
                .filter(x -> Objects.equals(x.getBiddingStatus(), AdBiddingStatus.BIDDING_SUCCESS.getValue())).collect(Collectors.toList());
        List<AdvertRound> leftExpiredList = oneMonthList.stream().filter(x -> Objects.equals(x.getTypeId(), AdType.PC_HOME_POP_LEFT_BANNER.getValue()))
                .filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.EXPIRED.getValue())).collect(Collectors.toList());
        // 中部广告
        List<AdvertRound> midLaunchingList = oneMonthList.stream().filter(x -> Objects.equals(x.getTypeId(), AdType.PC_HOME_POP_MID.getValue()))
                .filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.LAUNCHING.getValue()))
                .filter(x -> Objects.equals(x.getBiddingStatus(), AdBiddingStatus.BIDDING_SUCCESS.getValue())).collect(Collectors.toList());
        List<AdvertRound> midExpiredList = oneMonthList.stream().filter(x -> Objects.equals(x.getTypeId(), AdType.PC_HOME_POP_MID.getValue()))
                .filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.EXPIRED.getValue())).collect(Collectors.toList());
        // 右侧广告
        List<AdvertRound> rightLaunchingList = oneMonthList.stream().filter(x -> Objects.equals(x.getTypeId(), AdType.PC_HOME_POP_RIGHT.getValue()))
                .filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.LAUNCHING.getValue()))
                .filter(x -> Objects.equals(x.getBiddingStatus(), AdBiddingStatus.BIDDING_SUCCESS.getValue())).collect(Collectors.toList());
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
            bottomRightList = rightLaunchingList.stream().filter(x -> StringUtils.isNotBlank(x.getProdIdStr())).map(x -> {
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
        Map<Long, SysFile> fileMap = fileMapper.selectList(new LambdaQueryWrapper<SysFile>().eq(SysFile::getDelFlag, Constants.UNDELETED)
                        .in(SysFile::getId, oneMonthList.stream().map(AdvertRound::getPicId).filter(ObjectUtils::isNotEmpty).collect(Collectors.toList())))
                .stream().collect(Collectors.toMap(SysFile::getId, Function.identity()));
        List<AdvertRound> launchingList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.LAUNCHING.getValue()))
                .filter(x -> Objects.equals(x.getBiddingStatus(), AdBiddingStatus.BIDDING_SUCCESS.getValue())).collect(Collectors.toList());
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
        List<AdvertRound> launchingList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.LAUNCHING.getValue()))
                .filter(x -> Objects.equals(x.getBiddingStatus(), AdBiddingStatus.BIDDING_SUCCESS.getValue())).collect(Collectors.toList());
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
        List<PCIndexSearchRecommendProdDTO> redisRecommendList = redisCache.getCacheObject(CacheConstants.PC_ADVERT + CacheConstants.PC_ADVERT_INDEX_SEARCH_RECOMMEND_PROD);
        if (CollectionUtils.isNotEmpty(redisRecommendList)) {
            return redisRecommendList;
        }
        List<AdvertRound> oneMonthList = this.getOneMonthAdvertList(Collections.singletonList(AdType.PC_HOME_SEARCH_PRODUCT.getValue()));
        if (CollectionUtils.isEmpty(oneMonthList)) {
            return new ArrayList<>();
        }
        final List<Long> storeProdIdList = oneMonthList.stream()
                .filter(x -> StringUtils.isNotBlank(x.getProdIdStr())).map(x -> Long.parseLong(x.getProdIdStr())).distinct().collect(Collectors.toList());
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
                return new PCIndexSearchRecommendProdDTO().setDisplayType(AdDisplayType.PRODUCT.getValue()).setStoreProdId(storeProdId).setOrderNum(this.positionToNumber(advertRound.getPosition()))
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
            for (int i = 0; i < newMidBrandList.size(); i++) {
                newMidBrandList.get(i).setOrderNum(i + 1);
            }
        } else {
            newMidBrandList = launchingList.stream().map(x -> new PCNewMidBrandDTO().setDisplayType(AdDisplayType.PICTURE.getValue())
                            .setOrderNum(this.positionToNumber(x.getPosition())).setStoreId(x.getStoreId())
                            .setStoreName(ObjectUtils.isNotEmpty(storeMap.get(x.getStoreId())) ? storeMap.get(x.getStoreId()).getStoreName() : "")
                            .setFileUrl(ObjectUtils.isNotEmpty(fileMap.get(x.getPicId())) ? fileMap.get(x.getPicId()).getFileUrl() : ""))
                    .collect(Collectors.toList());
            if (newMidBrandList.size() < 10) {
                final List<Long> existStoreIdList = newMidBrandList.stream().map(PCNewMidBrandDTO::getStoreId).distinct().collect(Collectors.toList());
                newMidBrandList.addAll(this.fillNewMidBrandList(expiredList, storeMap, fileMap, existStoreIdList, 10 - newMidBrandList.size()));
                for (int i = 0; i < newMidBrandList.size(); i++) {
                    newMidBrandList.get(i).setOrderNum(i + 1);
                }
            }
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
        List<AdvertRound> launchingList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.LAUNCHING.getValue()))
                .filter(x -> Objects.equals(x.getBiddingStatus(), AdBiddingStatus.BIDDING_SUCCESS.getValue())).collect(Collectors.toList());
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
                        .setOrderNum(this.positionToNumber(x.getPosition())).setStoreProdId(storeProdId)
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
        List<Long> storeProdIdList = oneMonthList.stream().map(AdvertRound::getProdIdStr).map(Long::parseLong).distinct().collect(Collectors.toList());
        List<Store> storeList = this.storeMapper.selectByIds(oneMonthList.stream().map(AdvertRound::getStoreId).collect(Collectors.toList()));
        // 获取商品显示的基本属性
        List<StoreProdViewDTO> storeProdViewList = this.storeProdMapper.getStoreProdViewAttr(storeProdIdList,
                java.sql.Date.valueOf(LocalDate.now()), java.sql.Date.valueOf(LocalDate.now().minusMonths(2)));
        Map<Long, StoreProdViewDTO> viewMap = storeProdViewList.stream().collect(Collectors.toMap(StoreProdViewDTO::getStoreProdId, Function.identity()));
        // 档口标签
        List<DailyStoreTag> storeTagList = this.dailyStoreTagMapper.selectList(new LambdaQueryWrapper<DailyStoreTag>()
                .eq(DailyStoreTag::getDelFlag, Constants.UNDELETED).in(DailyStoreTag::getStoreId, storeList.stream().map(Store::getId).collect(Collectors.toList())));
        Map<Long, List<String>> storeTagMap = storeTagList.stream().collect(Collectors
                .groupingBy(DailyStoreTag::getStoreId, Collectors.collectingAndThen(Collectors.toList(), list -> list.stream()
                        .sorted(Comparator.comparing(DailyStoreTag::getType)).map(DailyStoreTag::getTag).collect(Collectors.toList()))));
        List<AdvertRound> launchingList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.LAUNCHING.getValue()))
                .filter(x -> Objects.equals(x.getBiddingStatus(), AdBiddingStatus.BIDDING_SUCCESS.getValue())).collect(Collectors.toList());
        List<AdvertRound> expiredList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.EXPIRED.getValue())).collect(Collectors.toList());
        // 从正在播放的图搜热款广告或者历史广告中筛选10条
        picSearchList = getPicSearchAdvertList(CollectionUtils.isEmpty(launchingList) ? expiredList : launchingList, viewMap, storeTagMap);
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
        final List<Long> storeProdIdList = oneMonthList.stream()
                .map(AdvertRound::getProdIdStr).map(Long::parseLong).distinct().collect(Collectors.toList());
        // 档口商品的价格及商品主图map
        Map<Long, StoreProdPriceAndMainPicDTO> prodPriceAndMainPicMap = CollectionUtils.isEmpty(storeProdIdList) ? new ConcurrentHashMap<>()
                : this.storeProdMapper.selectPriceAndMainPicList(storeProdIdList).stream().collect(Collectors
                .toMap(StoreProdPriceAndMainPicDTO::getStoreProdId, Function.identity()));
        List<AdvertRound> launchingList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.LAUNCHING.getValue()))
                .filter(x -> Objects.equals(x.getBiddingStatus(), AdBiddingStatus.BIDDING_SUCCESS.getValue())).collect(Collectors.toList());
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
        final List<Long> storeProdIdList = oneMonthList.stream()
                .map(AdvertRound::getProdIdStr).map(Long::parseLong).distinct().collect(Collectors.toList());
        // 档口商品的价格及商品主图map
        Map<Long, StoreProdPriceAndMainPicDTO> prodPriceAndMainPicMap = CollectionUtils.isEmpty(storeProdIdList) ? new ConcurrentHashMap<>()
                : this.storeProdMapper.selectPriceAndMainPicList(storeProdIdList).stream().collect(Collectors
                .toMap(StoreProdPriceAndMainPicDTO::getStoreProdId, Function.identity()));
        List<AdvertRound> launchingList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.LAUNCHING.getValue()))
                .filter(x -> Objects.equals(x.getBiddingStatus(), AdBiddingStatus.BIDDING_SUCCESS.getValue())).collect(Collectors.toList());
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
     * @param picSearchList 图搜热款数据库数据
     * @param storeTagMap   档口标签map
     * @return List<PicSearchAdvertDTO>
     */
    private List<PicSearchAdvertDTO> getPicSearchAdvertList(List<AdvertRound> picSearchList, Map<Long, StoreProdViewDTO> viewMap, Map<Long, List<String>> storeTagMap) {
        return picSearchList.stream().limit(10).map(advertRound -> {
            final Long storeProdId = Long.valueOf(advertRound.getProdIdStr());
            StoreProdViewDTO viewDTO = viewMap.get(storeProdId);
            String mainPic = ObjectUtils.isNotEmpty(viewDTO) ? viewDTO.getMainPicUrl() : "";
            List<ProductMatchDTO> results = StringUtils.isNotBlank(mainPic) ? pictureService.searchProductByPicKey(mainPic, 1000) : new ArrayList<>();
            List<String> prodTagList = new ArrayList<String>() {{
                add("同类热卖");
            }};
            CollectionUtils.addAll(prodTagList, ObjectUtils.isNotEmpty(viewDTO) && StringUtils.isNotEmpty(viewDTO.getTagStr())
                    ? StrUtil.split(viewDTO.getTagStr(), ",") : new ArrayList<>());
            return new PicSearchAdvertDTO().setImgSearchCount(ObjectUtils.isNotEmpty(viewDTO) && ObjectUtils.isNotEmpty(viewDTO.getImgSearchCount())
                            ? viewDTO.getImgSearchCount() : (long) (new Random().nextInt(191) + 10))
                    .setSameProdCount(results.size()).setStoreProdId(storeProdId).setStoreId(advertRound.getStoreId()).setTags(prodTagList)
                    .setStoreName(ObjectUtils.isNotEmpty(viewDTO) ? viewDTO.getStoreName() : "")
                    .setPrice(ObjectUtils.isNotEmpty(viewDTO) ? viewDTO.getPrice() : null)
                    .setProdArtNum(ObjectUtils.isNotEmpty(viewDTO) ? viewDTO.getProdArtNum() : "")
                    .setMainPicUrl(ObjectUtils.isNotEmpty(viewDTO) ? viewDTO.getMainPicUrl() : "")
                    .setProdTitle(ObjectUtils.isNotEmpty(viewDTO) ? viewDTO.getProdTitle() : "");
//                    .setStoreTagList(CollectionUtils.isNotEmpty(storeTagMap.get(advertRound.getStoreId())) ? storeTagMap.get(advertRound.getStoreId()) : null);
        }).collect(Collectors.toList());
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
                    List<String> prodIdStrList = StrUtil.split(advertRound.getProdIdStr(), ",");
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
                    tempList.add(new PCNewMidBrandDTO().setDisplayType(AdDisplayType.PICTURE.getValue()).setStoreId(storeId).setStoreName(storeMap.get(storeId).getStoreName())
                            .setFileUrl(ObjectUtils.isNotEmpty(fileMap.get(advertRound.getPicId())) ? fileMap.get(advertRound.getPicId()).getFileUrl() : ""));
                });
        return tempList.stream().limit(limitCount).collect(Collectors.toList());
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
                        tempList.add(new PCNewMidHotRightDTO().setDisplayType(AdDisplayType.PRODUCT.getValue()).setStoreProdId(storeProdId)
                                .setPrice(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getMinPrice() : null)
                                .setProdArtNum(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getProdArtNum() : "")
                                .setMainPicUrl(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getMainPicUrl() : ""));
                    }
                });
        return tempList.stream().limit(limitCount).collect(Collectors.toList());
    }

    private static FieldValue newFieldValue(String value) {
        return FieldValue.of(value);
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
                            .map(WebsitePCServiceImpl::newFieldValue)
                            .collect(Collectors.toList()))
                    .build();
            boolQuery.filter(f -> f.terms(t -> t.field("prodStatus").terms(termsQueryField)));
        }
        // 添加 prodCateId 过滤条件
        if (CollectionUtils.isNotEmpty(searchDTO.getProdCateIdList())) {
            TermsQueryField termsQueryField = new TermsQueryField.Builder()
                    .value(searchDTO.getProdCateIdList().stream()
                            .map(WebsitePCServiceImpl::newFieldValue)
                            .collect(Collectors.toList()))
                    .build();
            boolQuery.filter(f -> f.terms(t -> t.field("prodCateId").terms(termsQueryField)));
        }
        // 添加 parCateId 过滤条件
        if (CollectionUtils.isNotEmpty(searchDTO.getParCateIdList())) {
            TermsQueryField termsQueryField = new TermsQueryField.Builder()
                    .value(searchDTO.getParCateIdList().stream()
                            .map(WebsitePCServiceImpl::newFieldValue)
                            .collect(Collectors.toList()))
                    .build();
            boolQuery.filter(f -> f.terms(t -> t.field("parCateId").terms(termsQueryField)));
        }
        // 添加 style 过滤条件
        if (CollectionUtils.isNotEmpty(searchDTO.getStyleList())) {
            TermsQueryField termsQueryField = new TermsQueryField.Builder()
                    .value(searchDTO.getStyleList().stream()
                            .map(WebsitePCServiceImpl::newFieldValue)
                            .collect(Collectors.toList()))
                    .build();
            boolQuery.filter(f -> f.terms(t -> t.field("style.keyword").terms(termsQueryField)));
        }
        // 添加 season 过滤条件
        if (CollectionUtils.isNotEmpty(searchDTO.getSeasonList())) {
            TermsQueryField termsQueryField = new TermsQueryField.Builder()
                    .value(searchDTO.getSeasonList().stream()
                            .map(WebsitePCServiceImpl::newFieldValue)
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
