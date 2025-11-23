package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.AdType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.es.EsClientWrapper;
import com.ruoyi.xkt.domain.*;
import com.ruoyi.xkt.dto.advertRound.pc.PCDownloadDTO;
import com.ruoyi.xkt.dto.advertRound.pc.PCSearchDTO;
import com.ruoyi.xkt.dto.advertRound.pc.PCSearchResultAdvertDTO;
import com.ruoyi.xkt.dto.advertRound.pc.PCUserCenterDTO;
import com.ruoyi.xkt.dto.advertRound.pc.index.PCIndexRecommendDTO;
import com.ruoyi.xkt.dto.advertRound.pc.newProd.PCNewRecommendDTO;
import com.ruoyi.xkt.dto.advertRound.pc.store.PCStorePageDTO;
import com.ruoyi.xkt.dto.advertRound.pc.store.PCStoreRecommendDTO;
import com.ruoyi.xkt.dto.advertRound.picSearch.PicSearchAdvertDTO;
import com.ruoyi.xkt.dto.es.ESProductDTO;
import com.ruoyi.xkt.dto.picture.ProductMatchDTO;
import com.ruoyi.xkt.dto.storeProduct.StoreProdPriceAndMainPicAndTagDTO;
import com.ruoyi.xkt.dto.storeProduct.StoreProdPriceAndMainPicDTO;
import com.ruoyi.xkt.dto.storeProduct.StoreProdViewDTO;
import com.ruoyi.xkt.dto.storeProductFile.StoreProdFileLatestFourProdDTO;
import com.ruoyi.xkt.dto.useSearchHistory.UserSearchHistoryDTO;
import com.ruoyi.xkt.dto.website.IndexSearchDTO;
import com.ruoyi.xkt.dto.website.StoreSearchDTO;
import com.ruoyi.xkt.enums.AdBiddingStatus;
import com.ruoyi.xkt.enums.AdDisplayType;
import com.ruoyi.xkt.enums.AdLaunchStatus;
import com.ruoyi.xkt.enums.SearchPlatformType;
import com.ruoyi.xkt.mapper.*;
import com.ruoyi.xkt.service.IElasticSearchService;
import com.ruoyi.xkt.service.IPictureService;
import com.ruoyi.xkt.service.IWebsitePCService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
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
public class WebsitePCServiceImpl implements IWebsitePCService {

    final EsClientWrapper esClientWrapper;
    final RedisCache redisCache;
    final AdvertRoundMapper advertRoundMapper;
    final SysFileMapper fileMapper;
    final AdvertStoreFileMapper advertStoreFileMapper;
    final StoreProductFileMapper prodFileMapper;
    final DailySaleProductMapper dailySaleProdMapper;
    final StoreProductMapper storeProdMapper;
    final DailyStoreTagMapper dailyStoreTagMapper;
    final StoreMapper storeMapper;
    final StoreProductStatisticsMapper prodStatsMapper;
    final IPictureService pictureService;
    final UserSubscriptionsMapper userSubsMapper;
    final SysProductCategoryMapper prodCateMapper;
    final ObjectMapper jsonMapper;
    final IElasticSearchService esService;
    @Value("${es.indexName}")
    private String ES_INDEX_NAME;


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
        if (CollectionUtils.isEmpty(page.getList())) {
            return new Page<>(page.getPageNum(), page.getPageSize(), page.getPages(), page.getTotal(), Collections.emptyList());
        }
        // 筛选出真实的数据
        List<PCIndexRecommendDTO> realDataList = page.getList().stream()
                .map(esProduct -> BeanUtil.toBean(esProduct, PCIndexRecommendDTO.class).setAdvert(Boolean.FALSE).setMemberLevel(null))
                .collect(Collectors.toList());
        // 绑定档口会员等级
        realDataList.forEach(x -> {
            StoreMember member = this.redisCache.getCacheObject(CacheConstants.STORE_MEMBER + x.getStoreId());
            x.setMemberLevel(ObjectUtils.isNotEmpty(member) ? member.getLevel() : null);
        });
        // 从redis中获取数据
        List<PCIndexRecommendDTO> redisList = this.redisCache.getCacheObject(CacheConstants.PC_INDEX_RECOMMEND);
        // 返回的真实数据列表 过滤掉广告商品
        if (CollectionUtils.isNotEmpty(redisList)) {
            final List<String> advertProdIdList = CollectionUtils.isEmpty(redisList) ? Collections.emptyList()
                    : redisList.stream().map(PCIndexRecommendDTO::getStoreProdId).collect(Collectors.toList());
            realDataList = realDataList.stream()
                    .filter(x -> CollectionUtils.isEmpty(advertProdIdList) || !advertProdIdList.contains(x.getStoreProdId()))
                    .collect(Collectors.toList());
        }
        // APP 只有第一页 有数据 其它页暂时没有广告
        if (searchDTO.getPageNum() > 1) {
            return new Page<>(page.getPageNum(), page.getPageSize(), page.getPages(), page.getTotal(), realDataList);
        }
        if (CollectionUtils.isNotEmpty(redisList)) {
            // 推广数据排在最前面，其次才是真实的数据
            CollectionUtils.addAll(redisList, realDataList);
            // 添加广告的数据（PC的规则是将所有的广告数据全部放到最前面展示，不用给广告打标）
            return new Page<>(page.getPageNum(), page.getPageSize(), page.getPages(), page.getTotal(), redisList);
        } else {
            // 从数据库查首页 推荐商品 推广（精准搜索是否存在推广，不存在从已过期的数据中拉数据来凑数）而且必须是 竞价成功的推广（有可能当天有新的竞价推广还未正式审核通过）
            List<AdvertRound> advertRoundList = this.advertRoundMapper.selectList(new LambdaQueryWrapper<AdvertRound>()
                    .isNotNull(AdvertRound::getStoreId).isNotNull(AdvertRound::getProdIdStr).ne(AdvertRound::getProdIdStr, "").eq(AdvertRound::getDelFlag, Constants.UNDELETED)
                    .eq(AdvertRound::getTypeId, AdType.PC_HOME_PRODUCT_LIST.getValue()).eq(AdvertRound::getLaunchStatus, AdLaunchStatus.LAUNCHING.getValue())
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
                                .setProdTitle(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getProdTitle() : "")
                                .setHasVideo(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getHasVideo() : Boolean.FALSE)
                                .setParCateId(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getParCateId() : null)
                                .setProdCateId(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getProdCateId() : null)
                                .setStoreProdId(storeProdId.toString()).setTags(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getTags() : null)
                                .setStoreName(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getStoreName() : "")
                                .setProdPrice(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getMinPrice().toString() : null)
                                .setProdArtNum(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getProdArtNum() : "")
                                .setMainPicUrl(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getMainPicUrl() : "")
                                .setMainPicName(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getMainPicName() : "")
                                .setMainPicSize(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getMainPicSize() : null)
                                .setTags(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getTags() : null));
                    });
                });
                // 将indexRecommendList 顺序打乱，不然一个档口的数据在同一地方展示
                Collections.shuffle(indexRecommendList);
                // 放到redis中 有效期1天
                this.redisCache.setCacheObject(CacheConstants.PC_INDEX_RECOMMEND, indexRecommendList, 1, TimeUnit.DAYS);
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
        if (CollectionUtils.isEmpty(page.getList())) {
            return new Page<>(page.getPageNum(), page.getPageSize(), page.getPages(), page.getTotal(), Collections.emptyList());
        }
        // 筛选出真实的数据
        List<PCNewRecommendDTO> realDataList = page.getList().stream()
                .map(esProduct -> BeanUtil.toBean(esProduct, PCNewRecommendDTO.class).setAdvert(Boolean.FALSE)).collect(Collectors.toList());
        // 绑定档口会员等级
        realDataList.forEach(x -> {
            StoreMember member = this.redisCache.getCacheObject(CacheConstants.STORE_MEMBER + x.getStoreId());
            x.setMemberLevel(ObjectUtils.isNotEmpty(member) ? member.getLevel() : null);
        });
        // 从redis中获取数据
        List<PCNewRecommendDTO> redisList = this.redisCache.getCacheObject(CacheConstants.PC_NEW_RECOMMEND);
        // 返回的真实数据列表 过滤掉广告商品
        if (CollectionUtils.isNotEmpty(redisList)) {
            final List<String> advertProdIdList = CollectionUtils.isEmpty(redisList) ? Collections.emptyList()
                    : redisList.stream().map(PCNewRecommendDTO::getStoreProdId).collect(Collectors.toList());
            realDataList = realDataList.stream()
                    .filter(x -> CollectionUtils.isEmpty(advertProdIdList) || !advertProdIdList.contains(x.getStoreProdId()))
                    .collect(Collectors.toList());
        }
        // APP 只有第一页 有数据 其它页暂时没有广告
        if (searchDTO.getPageNum() > 1) {
            return new Page<>(page.getPageNum(), page.getPageSize(), page.getPages(), page.getTotal(), realDataList);
        }
        if (CollectionUtils.isNotEmpty(redisList)) {
            // 推广数据排在最前面，其次才是真实的数据
            CollectionUtils.addAll(redisList, realDataList);
            // 添加广告的数据（PC的规则是将所有的广告数据全部放到最前面展示，不用给广告打标）
            return new Page<>(page.getPageNum(), page.getPageSize(), page.getPages(), page.getTotal(), redisList);
        } else {
            // 从数据库查新品馆 推荐商品 推广（精准搜索是否存在推广，不存在从已过期的数据中拉数据来凑数）
            List<AdvertRound> advertRoundList = this.advertRoundMapper.selectList(new LambdaQueryWrapper<AdvertRound>()
                    .isNotNull(AdvertRound::getStoreId).isNotNull(AdvertRound::getProdIdStr).ne(AdvertRound::getProdIdStr, "").eq(AdvertRound::getDelFlag, Constants.UNDELETED)
                    .eq(AdvertRound::getTypeId, AdType.PC_NEW_PROD_PRODUCT_LIST.getValue()).eq(AdvertRound::getLaunchStatus, AdLaunchStatus.LAUNCHING.getValue())
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
                                .setProdTitle(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getProdTitle() : "")
                                .setHasVideo(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getHasVideo() : Boolean.FALSE)
                                .setParCateId(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getParCateId() : null)
                                .setProdCateId(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getProdCateId() : null)
                                .setStoreProdId(storeProdId.toString()).setTags(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getTags() : null)
                                .setStoreName(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getStoreName() : "")
                                .setProdPrice(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getMinPrice().toString() : null)
                                .setProdArtNum(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getProdArtNum() : "")
                                .setMainPicUrl(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getMainPicUrl() : "")
                                .setMainPicName(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getMainPicName() : "")
                                .setMainPicSize(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getMainPicSize() : null)
                                .setTags(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getTags() : null));
                    });
                });
                // newRecommendList 顺序打乱，不然一个档口的数据在同一地方展示
                Collections.shuffle(newRecommendList);
                // 放到redis中 有效期1天
                this.redisCache.setCacheObject(CacheConstants.PC_NEW_RECOMMEND, newRecommendList, 1, TimeUnit.DAYS);
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
        // 更新用户搜索历史
        try {
            updateRedisUserSearchHistory(searchDTO.getSearch());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 获取用户搜索结果列表
        Page<ESProductDTO> page = this.search(searchDTO);
        if (CollectionUtils.isEmpty(page.getList())) {
            return new Page<>(page.getPageNum(), page.getPageSize(), page.getPages(), page.getTotal(), Collections.emptyList());
        }
        // 筛选出真实的数据
        List<PCSearchDTO> realDataList = page.getList().stream()
                .map(esProduct -> BeanUtil.toBean(esProduct, PCSearchDTO.class).setAdvert(Boolean.FALSE)).collect(Collectors.toList());
        // 绑定档口会员等级
        realDataList.forEach(x -> {
            StoreMember member = this.redisCache.getCacheObject(CacheConstants.STORE_MEMBER + x.getStoreId());
            x.setMemberLevel(ObjectUtils.isNotEmpty(member) ? member.getLevel() : null);
        });
        // pc 搜索结果需要过滤掉广告商品
        List<PCSearchDTO> redisList = redisCache.getCacheObject(CacheConstants.PC_ADVERT + CacheConstants.PC_SEARCH_RESULT_LIST_RECOMMEND);
        if (CollectionUtils.isNotEmpty(redisList)) {
            final List<String> advertProdIdList = redisList.stream().map(PCSearchDTO::getStoreProdId).map(String::valueOf).collect(Collectors.toList());
            realDataList = realDataList.stream()
                    .filter(x -> CollectionUtils.isEmpty(advertProdIdList) || !advertProdIdList.contains(x.getStoreProdId()))
                    .collect(Collectors.toList());
        }
        // 只有第一页 有数据 其它页暂时没有广告
        if (searchDTO.getPageNum() > 1) {
            return new Page<>(page.getPageNum(), page.getPageSize(), page.getPages(), page.getTotal(), realDataList);
        }
        if (CollectionUtils.isNotEmpty(redisList)) {
            // 添加广告的数据
            return new Page<>(page.getPageNum(), page.getPageSize(), page.getPages(), page.getTotal(),
                    insertAdvertsIntoList(realDataList, redisList, Constants.PC_SEARCH_RESULT_INSERT_POSITIONS));
        } else {
            // 从数据库查询搜索结果是否有广告（只查询现在正在播放的，不查询历史数据）
            List<AdvertRound> advertRoundList = this.advertRoundMapper.selectList(new LambdaQueryWrapper<AdvertRound>()
                    .isNotNull(AdvertRound::getStoreId).isNotNull(AdvertRound::getProdIdStr).ne(AdvertRound::getProdIdStr, "").eq(AdvertRound::getDelFlag, Constants.UNDELETED)
                    .eq(AdvertRound::getTypeId, AdType.PC_SEARCH_RESULT_PRODUCT.getValue()).eq(AdvertRound::getLaunchStatus, AdLaunchStatus.LAUNCHING.getValue())
                    .eq(AdvertRound::getBiddingStatus, AdBiddingStatus.BIDDING_SUCCESS.getValue()));
            if (CollectionUtils.isNotEmpty(advertRoundList)) {
                final List<Long> storeProdIdList = advertRoundList.stream()
                        .filter(x -> StringUtils.isNotBlank(x.getProdIdStr())).map(x -> Long.parseLong(x.getProdIdStr())).distinct().collect(Collectors.toList());
                // 商品的货号、价格、主图等
                List<StoreProdPriceAndMainPicAndTagDTO> attrList = storeProdMapper.selectPriceAndMainPicAndTagList(storeProdIdList);
                attrList = attrList.stream().peek(x -> x.setTags(StringUtils.isNotBlank(x.getTagStr()) ? Arrays.asList(x.getTagStr().split(",")) : null)).collect(Collectors.toList());
                Map<Long, StoreProdPriceAndMainPicAndTagDTO> attrMap = attrList.stream().collect(Collectors.toMap(StoreProdPriceAndMainPicAndTagDTO::getStoreProdId, x -> x));
                List<PCSearchDTO> searchResAdvertList = advertRoundList.stream().filter(x -> StringUtils.isNotBlank(x.getProdIdStr())).map(x -> {
                    StoreProdPriceAndMainPicAndTagDTO attrDto = attrMap.get(Long.parseLong(x.getProdIdStr()));
                    return new PCSearchDTO().setAdvert(Boolean.TRUE).setStoreId(x.getStoreId().toString()).setStoreProdId(x.getProdIdStr())
                            .setProdTitle(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getProdTitle() : "")
                            .setHasVideo(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getHasVideo() : Boolean.FALSE)
                            .setTags(StringUtils.isNotBlank(attrDto.getTagStr()) ? StrUtil.split(attrDto.getTagStr(), ",") : null)
                            .setStoreName(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getStoreName() : "")
                            .setProdPrice(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getMinPrice().toString() : null)
                            .setProdArtNum(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getProdArtNum() : "")
                            .setMainPicUrl(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getMainPicUrl() : "")
                            .setMainPicName(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getMainPicName() : "")
                            .setMainPicSize(ObjectUtils.isNotEmpty(attrDto) ? attrDto.getMainPicSize() : null);
                }).collect(Collectors.toList());
                // 放到redis中 有效期1天
                this.redisCache.setCacheObject(CacheConstants.APP_INDEX_HOT_SALE_ADVERT, searchResAdvertList, 1, TimeUnit.DAYS);
                // 添加了广告的数据
                return new Page<>(page.getPageNum(), page.getPageSize(), page.getPages(), page.getTotal(),
                        insertAdvertsIntoList(realDataList, searchResAdvertList, Constants.PC_SEARCH_RESULT_INSERT_POSITIONS));
            }
        }
        return new Page<>(page.getPageNum(), page.getPageSize(), page.getPages(), page.getTotal(), realDataList);
    }

    /**
     * PC 档口馆 档口列表
     *
     * @param searchDTO 搜索入参
     * @return Page<PCStoreRecommendDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PCStoreRecommendDTO> pcStoreRecommendPage(StoreSearchDTO searchDTO) {
        // 从redis中获取档口推荐列表
        List<PCStoreRecommendDTO> redisList = this.redisCache.getCacheObject(CacheConstants.PC_STORE_RECOMMEND_LIST);
        if (CollectionUtils.isEmpty(redisList)) {
            return Page.empty(searchDTO.getPageSize(), searchDTO.getPageNum());
        }
        // 正确的分页数据
        List<PCStoreRecommendDTO> realDataList = redisList.stream()
                .filter(x -> StringUtils.isEmpty(searchDTO.getStoreName()) || x.getStoreName().contains(searchDTO.getStoreName()))
                .skip((long) (searchDTO.getPageNum() - 1) * searchDTO.getPageSize())
                .limit(searchDTO.getPageSize()).collect(Collectors.toList());
        // 当前分页无数据，则直接返回
        if (CollectionUtils.isEmpty(realDataList)) {
            return Page.empty(searchDTO.getPageSize(), searchDTO.getPageNum());
        }
        realDataList.forEach(x -> {
            // 查询档口会员等级
            StoreMember member = this.redisCache.getCacheObject(CacheConstants.STORE_MEMBER + x.getStoreId());
            x.setMemberLevel(ObjectUtils.isNotEmpty(member) ? member.getLevel() : null);
        });
        final long pages = (long) Math.ceil((double) redisList.size() / searchDTO.getPageSize());
        // 从redis中获取数据
        List<PCStoreRecommendDTO> redisAdvertList = this.redisCache.getCacheObject(CacheConstants.PC_STORE_RECOMMEND_ADVERT);
        // 返回的真实数据列表 过滤掉广告档口
        if (CollectionUtils.isNotEmpty(redisAdvertList)) {
            // 推广的档口ID列表
            final List<Long> advertStoreIdList = redisAdvertList.stream().map(PCStoreRecommendDTO::getStoreId).collect(Collectors.toList());
            realDataList = realDataList.stream()
                    .filter(x -> CollectionUtils.isEmpty(advertStoreIdList) || !advertStoreIdList.contains(x.getStoreId()))
                    .collect(Collectors.toList());
        }
        // APP 只有第一页 有数据 其它页暂时没有广告
        if (searchDTO.getPageNum() > 1) {
            return new Page<>(searchDTO.getPageNum(), searchDTO.getPageSize(), pages, redisList.size(), realDataList);
        }
        if (CollectionUtils.isNotEmpty(redisAdvertList)) {
            // 添加广告的数据
            return new Page<>(searchDTO.getPageNum(), searchDTO.getPageSize(), pages, redisList.size(),
                    insertAdvertsIntoList(realDataList, redisAdvertList, Constants.STORE_RECOMMEND_INSERT_POSITIONS));
        } else {
            // 从数据库查档口推荐列表 是否存在推广
            List<AdvertRound> advertRoundList = this.advertRoundMapper.selectList(new LambdaQueryWrapper<AdvertRound>()
                    .isNotNull(AdvertRound::getStoreId).eq(AdvertRound::getDelFlag, Constants.UNDELETED)
                    .eq(AdvertRound::getTypeId, AdType.PC_STORE_RECOMMEND.getValue())
                    // 必须是正在推广的档口，不能从已过期的档口推广列表中选数据
                    .eq(AdvertRound::getLaunchStatus, AdLaunchStatus.LAUNCHING.getValue())
                    .eq(AdvertRound::getBiddingStatus, AdBiddingStatus.BIDDING_SUCCESS.getValue()));
            if (CollectionUtils.isNotEmpty(advertRoundList)) {
                // 档口列表
                List<Store> storeList = this.storeMapper.selectList(new LambdaQueryWrapper<Store>()
                        .in(Store::getId, advertRoundList.stream().map(AdvertRound::getStoreId).collect(Collectors.toList())));
                Map<Long, Store> storeMap = storeList.stream().collect(Collectors.toMap(Store::getId, Function.identity()));
                // 档口所有的标签
                List<DailyStoreTag> storeTagList = this.dailyStoreTagMapper.selectList(new LambdaQueryWrapper<DailyStoreTag>()
                        .in(DailyStoreTag::getStoreId, advertRoundList.stream().map(AdvertRound::getStoreId).collect(Collectors.toList())));
                Map<Long, List<String>> storeTagMap = CollectionUtils.isEmpty(storeTagList) ? new ConcurrentHashMap<>()
                        : storeTagList.stream().collect(Collectors.groupingBy(DailyStoreTag::getStoreId, Collectors.mapping(DailyStoreTag::getTag, Collectors.toList())));
                // 筛选这些档口前4张最新的商品主图
                List<StoreProdFileLatestFourProdDTO> latest4ProdList = this.prodFileMapper.selectLatestFourProdList(Collections.emptyList());
                Map<Long, List<StoreProdFileLatestFourProdDTO>> latest4ProdMap = latest4ProdList.stream().collect(Collectors
                        .groupingBy(StoreProdFileLatestFourProdDTO::getStoreId));
                List<PCStoreRecommendDTO> storeRecommendList = new ArrayList<>();
                advertRoundList.forEach(x -> {
                    Store store = storeMap.get(x.getStoreId());
                    PCStoreRecommendDTO storeRecommend = new PCStoreRecommendDTO().setAdvert(Boolean.TRUE).setStoreId(x.getStoreId())
                            .setPayPrice(ObjectUtils.defaultIfNull(x.getPayPrice(), BigDecimal.ZERO))
                            .setStoreName(ObjectUtils.isNotEmpty(store) ? store.getStoreName() : "").setAdvert(Boolean.TRUE)
                            .setTags(storeTagMap.getOrDefault(x.getStoreId(), new ArrayList<>()))
                            .setContactPhone(ObjectUtils.isNotEmpty(store) ? store.getContactPhone() : "")
                            .setContactBackPhone(ObjectUtils.isNotEmpty(store) ? store.getContactBackPhone() : "")
                            .setWechatAccount(ObjectUtils.isNotEmpty(store) ? store.getWechatAccount() : "")
                            .setQqAccount(ObjectUtils.isNotEmpty(store) ? store.getQqAccount() : "")
                            .setStoreAddress(ObjectUtils.isNotEmpty(store) ? store.getStoreAddress() : "");
                    // 档口最新4个商品
                    List<StoreProdFileLatestFourProdDTO> prodMainPicList = latest4ProdMap.get(x.getStoreId());
                    storeRecommend.setProdList(BeanUtil.copyToList(prodMainPicList, PCStoreRecommendDTO.PCSRNewProdDTO.class));
                    storeRecommendList.add(storeRecommend);
                });
                // 按照价格由高到低进行排序，价格高的排1，其次按照价格排 2 3 4 5
                storeRecommendList.sort(Comparator.comparing(PCStoreRecommendDTO::getPayPrice).reversed());
                for (int i = 0; i < storeRecommendList.size(); i++) {
                    storeRecommendList.get(i).setOrderNum(i + 1);
                }
                // 放到redis中，过期时间为1天
                this.redisCache.setCacheObject(CacheConstants.PC_STORE_RECOMMEND_ADVERT, storeRecommendList, 1, TimeUnit.DAYS);
                // 添加广告的数据
                return new Page<>(searchDTO.getPageNum(), searchDTO.getPageSize(), pages, redisList.size(),
                        insertAdvertsIntoList(realDataList, storeRecommendList, Constants.STORE_RECOMMEND_INSERT_POSITIONS));
            }
        }
        return new Page<>(searchDTO.getPageNum(), searchDTO.getPageSize(), pages, redisList.size(), realDataList);
    }

    /**
     * PC 搜索结果右侧广告列表。
     * 即使是会员 也不返回会员标识，这样布局更好看
     *
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<PCSearchResultAdvertDTO> getPcSearchAdvertList() {
        // 从redis 中获取 PC 搜索结果广告列表
        List<PCSearchResultAdvertDTO> searchResultAdvertList = redisCache.getCacheObject(CacheConstants.PC_ADVERT + CacheConstants.PC_SEARCH_RESULT_ADVERT);
        if (ObjectUtils.isNotEmpty(searchResultAdvertList)) {
            return searchResultAdvertList;
        }
        List<AdvertRound> oneMonthList = this.getOneMonthAdvertList(Collections.singletonList(AdType.PC_SEARCH_RIGHT_PRODUCT.getValue()));
        if (CollectionUtils.isEmpty(oneMonthList)) {
            return new ArrayList<>();
        }
        // 必须是已经设置了推广商品，防止有的档口购买了推广位但未设置推广商品
        oneMonthList = oneMonthList.stream().filter(x -> StringUtils.isNotBlank(x.getProdIdStr())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(oneMonthList)) {
            return new ArrayList<>();
        }
        // 获取档口
        final List<Long> storeIdList = oneMonthList.stream().filter(x -> ObjectUtils.isNotEmpty(x.getStoreId())).map(AdvertRound::getStoreId).collect(Collectors.toList());
        List<Store> storeList = storeMapper.selectList(new LambdaQueryWrapper<Store>().eq(Store::getDelFlag, Constants.UNDELETED).in(Store::getId, storeIdList));
        Map<Long, Store> storeMap = storeList.stream().collect(Collectors.toMap(Store::getId, Function.identity()));
        List<AdvertRound> launchingList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.LAUNCHING.getValue()))
                .filter(x -> Objects.equals(x.getBiddingStatus(), AdBiddingStatus.BIDDING_SUCCESS.getValue())).collect(Collectors.toList());
        List<AdvertRound> expiredList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.EXPIRED.getValue())).collect(Collectors.toList());
        final List<Long> storeProdIdList = oneMonthList.stream()
                .filter(x -> StringUtils.isNotBlank(x.getProdIdStr())).map(x -> Long.parseLong(x.getProdIdStr())).distinct().collect(Collectors.toList());
        // 档口商品的价格及商品主图map
        Map<Long, StoreProdPriceAndMainPicDTO> prodPriceAndMainPicMap = CollectionUtils.isEmpty(storeProdIdList) ? new ConcurrentHashMap<>()
                : this.storeProdMapper.selectPriceAndMainPicList(storeProdIdList).stream().collect(Collectors
                .toMap(StoreProdPriceAndMainPicDTO::getStoreProdId, Function.identity()));
        List<PCSearchResultAdvertDTO> pcSearchResAdvertList;
        if (CollectionUtils.isEmpty(launchingList)) {
            pcSearchResAdvertList = this.fillPcSearchResultAdvertList(expiredList, prodPriceAndMainPicMap, storeMap, new ArrayList<>(), 15);
            pcSearchResAdvertList.sort(Comparator.comparing(PCSearchResultAdvertDTO::getPayPrice).reversed());
            // 按照价格由高到低进行排序，价格高的排1，其次按照价格排 2 3 4 5
            for (int i = 0; i < pcSearchResAdvertList.size(); i++) {
                pcSearchResAdvertList.get(i).setOrderNum(i + 1);
            }
        } else {
            pcSearchResAdvertList = launchingList.stream().filter(x -> StringUtils.isNotBlank(x.getProdIdStr())).map(x -> {
                final Long storeProdId = Long.parseLong(x.getProdIdStr());
                return new PCSearchResultAdvertDTO().setDisplayType(AdDisplayType.PRODUCT.getValue()).setStoreId(x.getStoreId())
                        .setStoreName(ObjectUtils.isNotEmpty(storeMap.get(x.getStoreId())) ? storeMap.get(x.getStoreId()).getStoreName() : "")
                        .setOrderNum(this.positionToNumber(x.getPosition())).setStoreProdId(storeProdId).setFocus(Boolean.FALSE)
                        .setPrice(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getMinPrice() : null)
                        .setProdArtNum(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getProdArtNum() : "")
                        .setMainPicUrl(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getMainPicUrl() : "");
            }).collect(Collectors.toList());
        }
        // 获取当前登录用户
        Long userId = SecurityUtils.getUserIdSafe();
        if (ObjectUtils.isNotEmpty(userId)) {
            // 获取当前用户关注档口
            List<UserSubscriptions> subsList = this.userSubsMapper.selectList(new LambdaQueryWrapper<UserSubscriptions>()
                    .eq(UserSubscriptions::getDelFlag, Constants.UNDELETED).eq(UserSubscriptions::getUserId, userId));
            if (CollectionUtils.isNotEmpty(subsList)) {
                final List<Long> focusStoreIdList = subsList.stream().map(UserSubscriptions::getStoreId).collect(Collectors.toList());
                pcSearchResAdvertList.forEach(x -> x.setFocus(focusStoreIdList.contains(x.getStoreId()) ? Boolean.TRUE : Boolean.FALSE));
            }
        }
        // 此广告位不返回会员标识，页面样式更好看

        // 放到redis中，过期时间为1天
        redisCache.setCacheObject(CacheConstants.PC_ADVERT + CacheConstants.PC_SEARCH_RESULT_ADVERT, pcSearchResAdvertList, 1, TimeUnit.DAYS);
        return pcSearchResAdvertList;
    }

    /**
     * PC档口商品列表
     *
     * @param searchDTO 搜索入参
     * @return Page<PCStorePageDTO>
     */
    @Override
    public Page<PCStorePageDTO> psStorePage(IndexSearchDTO searchDTO) throws IOException {
        // 获取用户搜索结果列表
        Page<ESProductDTO> page = this.search(searchDTO);
        if (CollectionUtils.isEmpty(page.getList())) {
            return new Page<>(page.getPageNum(), page.getPageSize(), page.getPages(), page.getTotal(), Collections.emptyList());
        }
        // 筛选出真实的数据
        List<PCStorePageDTO> realDataList = page.getList().stream()
                .map(esProduct -> BeanUtil.toBean(esProduct, PCStorePageDTO.class)).collect(Collectors.toList());
        // 暂时没有广告
        return new Page<>(page.getPageNum(), page.getPageSize(), page.getPages(), page.getTotal(), realDataList);
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
        // 必须是设置了推广商品的，防止有的档口购买了广告但没上传推广商品
        oneMonthList = oneMonthList.stream().filter(x -> StringUtils.isNotBlank(x.getProdIdStr())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(oneMonthList)) {
            return new ArrayList<>();
        }
        List<Long> storeProdIdList = oneMonthList.stream().map(AdvertRound::getProdIdStr).map(Long::parseLong).distinct().collect(Collectors.toList());
        // 获取商品显示的基本属性
        List<StoreProdViewDTO> storeProdViewList = this.storeProdMapper.getStoreProdViewAttr(storeProdIdList,
                java.sql.Date.valueOf(LocalDate.now().minusMonths(2)), java.sql.Date.valueOf(LocalDate.now()));
        Map<Long, StoreProdViewDTO> viewMap = storeProdViewList.stream().collect(Collectors.toMap(StoreProdViewDTO::getStoreProdId, Function.identity()));
        // 商品图搜次数
        List<StoreProductStatistics> prodStatsList = prodStatsMapper.selectList(new LambdaQueryWrapper<StoreProductStatistics>()
                .eq(StoreProductStatistics::getDelFlag, Constants.UNDELETED).in(StoreProductStatistics::getStoreProdId, storeProdIdList));
        Map<Long, Long> prodStatsMap = prodStatsList.stream().collect(Collectors
                .groupingBy(StoreProductStatistics::getStoreProdId, Collectors.summingLong(StoreProductStatistics::getImgSearchCount)));
        List<AdvertRound> launchingList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.LAUNCHING.getValue()))
                .filter(x -> Objects.equals(x.getBiddingStatus(), AdBiddingStatus.BIDDING_SUCCESS.getValue())).collect(Collectors.toList());
        List<AdvertRound> expiredList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.EXPIRED.getValue())).collect(Collectors.toList());
        // 从正在播放的图搜热款广告或者历史广告中筛选10条
        picSearchList = getPicSearchAdvertList(CollectionUtils.isEmpty(launchingList) ? expiredList : launchingList, viewMap, prodStatsMap);
        picSearchList.forEach(x -> {
            // 查询档口会员等级
            StoreMember member = this.redisCache.getCacheObject(CacheConstants.STORE_MEMBER + x.getStoreId());
            x.setMemberLevel(ObjectUtils.isNotEmpty(member) ? member.getLevel() : null);
        });
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
        // 必须是设置了推广商品的，防止有的档口购买了广告位但未上传商品
        oneMonthList = oneMonthList.stream().filter(x -> StringUtils.isNotBlank(x.getProdIdStr())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(oneMonthList)) {
            return new ArrayList<>();
        }
        final List<Long> storeProdIdList = oneMonthList.stream().map(AdvertRound::getProdIdStr).map(Long::parseLong).distinct().collect(Collectors.toList());
        List<StoreProdPriceAndMainPicAndTagDTO> attrList = storeProdMapper.selectPriceAndMainPicAndTagList(storeProdIdList);
        attrList = attrList.stream().peek(x -> x.setTags(StringUtils.isNotBlank(x.getTagStr()) ? Arrays.asList(x.getTagStr().split(",")) : null)).collect(Collectors.toList());
        Map<Long, StoreProdPriceAndMainPicAndTagDTO> attrMap = attrList.stream().collect(Collectors.toMap(StoreProdPriceAndMainPicAndTagDTO::getStoreProdId, x -> x));
        List<AdvertRound> launchingList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.LAUNCHING.getValue()))
                .filter(x -> Objects.equals(x.getBiddingStatus(), AdBiddingStatus.BIDDING_SUCCESS.getValue())).collect(Collectors.toList());
        List<AdvertRound> expiredList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.EXPIRED.getValue())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(launchingList)) {
            pcUserCenterList = expiredList.stream().map(x -> this.getPcUserCenterDTO(x, attrMap)).limit(18).collect(Collectors.toList());
            for (int i = 0; i < pcUserCenterList.size(); i++) {
                pcUserCenterList.get(i).setOrderNum(i + 1);
            }
        } else {
            pcUserCenterList = launchingList.stream().map(x -> this.getPcUserCenterDTO(x, attrMap)
                    .setOrderNum(this.positionToNumber(x.getPosition()))).limit(18).collect(Collectors.toList());
        }
        pcUserCenterList.forEach(x -> {
            // 查询档口会员等级
            StoreMember member = this.redisCache.getCacheObject(CacheConstants.STORE_MEMBER + x.getStoreId());
            x.setMemberLevel(ObjectUtils.isNotEmpty(member) ? member.getLevel() : null);
        });
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
        // 必须是设置了推广商品的，防止有的档口购买了广告位但未上传商品
        oneMonthList = oneMonthList.stream().filter(x -> StringUtils.isNotBlank(x.getProdIdStr())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(oneMonthList)) {
            return new ArrayList<>();
        }
        final List<Long> storeProdIdList = oneMonthList.stream().map(AdvertRound::getProdIdStr).map(Long::parseLong).distinct().collect(Collectors.toList());
        List<StoreProdPriceAndMainPicAndTagDTO> attrList = storeProdMapper.selectPriceAndMainPicAndTagList(storeProdIdList);
        attrList = attrList.stream().peek(x -> x.setTags(StringUtils.isNotBlank(x.getTagStr()) ? Arrays.asList(x.getTagStr().split(",")) : null)).collect(Collectors.toList());
        Map<Long, StoreProdPriceAndMainPicAndTagDTO> attrMap = attrList.stream().collect(Collectors.toMap(StoreProdPriceAndMainPicAndTagDTO::getStoreProdId, x -> x));
        List<AdvertRound> launchingList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.LAUNCHING.getValue()))
                .filter(x -> Objects.equals(x.getBiddingStatus(), AdBiddingStatus.BIDDING_SUCCESS.getValue())).collect(Collectors.toList());
        List<AdvertRound> expiredList = oneMonthList.stream().filter(x -> Objects.equals(x.getLaunchStatus(), AdLaunchStatus.EXPIRED.getValue())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(launchingList)) {
            pcDownloadList = expiredList.stream().map(advertRound -> this.getPcDownload(advertRound, attrMap)).limit(10).collect(Collectors.toList());
            for (int i = 0; i < pcDownloadList.size(); i++) {
                pcDownloadList.get(i).setOrderNum(i + 1);
            }
        } else {
            pcDownloadList = launchingList.stream().map(advertRound -> this.getPcDownload(advertRound, attrMap)
                    .setOrderNum(this.positionToNumber(advertRound.getPosition()))).limit(10).collect(Collectors.toList());
        }
        pcDownloadList.forEach(x -> {
            // 查询档口会员等级
            StoreMember member = this.redisCache.getCacheObject(CacheConstants.STORE_MEMBER + x.getStoreId());
            x.setMemberLevel(ObjectUtils.isNotEmpty(member) ? member.getLevel() : null);
        });
        // 放到redis 中 过期时间1天
        redisCache.setCacheObject(CacheConstants.PC_ADVERT + CacheConstants.PC_DOWNLOAD, pcDownloadList, 1, TimeUnit.DAYS);
        return pcDownloadList;
    }

    /**
     * 获取PC 下载页数据
     *
     * @param advertRound 下载页广告
     * @param attrMap     商品价格和图片等
     * @return PCDownloadDTO
     */
    private PCDownloadDTO getPcDownload(AdvertRound advertRound, Map<Long, StoreProdPriceAndMainPicAndTagDTO> attrMap) {
        final Long storeProdId = Long.parseLong(advertRound.getProdIdStr());
        final StoreProdPriceAndMainPicAndTagDTO dto = attrMap.get(storeProdId);
        return new PCDownloadDTO().setDisplayType(AdDisplayType.PRODUCT.getValue())
                .setStoreProdId(storeProdId).setAdvert(Boolean.TRUE)
                .setStoreId(ObjectUtils.isNotEmpty(dto) ? dto.getStoreId() : null)
                .setStoreName(ObjectUtils.isNotEmpty(dto) ? dto.getStoreName() : "")
                .setProdArtNum(ObjectUtils.isNotEmpty(dto) ? dto.getProdArtNum() : "")
                .setProdArtNum(ObjectUtils.isNotEmpty(dto) ? dto.getProdArtNum() : "")
                .setMainPicUrl(ObjectUtils.isNotEmpty(dto) ? dto.getMainPicUrl() : "")
                .setProdPrice(ObjectUtils.isNotEmpty(dto) ? dto.getMinPrice() : null)
                .setProdTitle(ObjectUtils.isNotEmpty(dto) ? dto.getProdTitle() : "")
                .setHasVideo(ObjectUtils.isNotEmpty(dto) ? dto.getHasVideo() : Boolean.FALSE)
                .setTags(ObjectUtils.isNotEmpty(dto) ? dto.getTags() : null);
    }

    /**
     * 获取PC 用户中心 广告列表
     *
     * @param advertRound 用户中心
     * @param attrMap     商品价格及主图等map
     * @return PCUserCenterDTO
     */
    private PCUserCenterDTO getPcUserCenterDTO(AdvertRound advertRound, Map<Long, StoreProdPriceAndMainPicAndTagDTO> attrMap) {
        final Long storeProdId = Long.parseLong(advertRound.getProdIdStr());
        final StoreProdPriceAndMainPicAndTagDTO dto = attrMap.get(storeProdId);
        return new PCUserCenterDTO().setDisplayType(AdDisplayType.PRODUCT.getValue())
                .setStoreProdId(storeProdId).setAdvert(Boolean.TRUE).setStoreId(advertRound.getStoreId())
                .setProdCateId(ObjectUtils.isNotEmpty(dto) ? dto.getProdCateId() : null)
                .setProdCateName(ObjectUtils.isNotEmpty(dto) ? dto.getProdCateName() : "")
                .setStoreName(ObjectUtils.isNotEmpty(dto) ? dto.getStoreName() : "")
                .setProdArtNum(ObjectUtils.isNotEmpty(dto) ? dto.getProdArtNum() : "")
                .setProdPrice(ObjectUtils.isNotEmpty(dto) ? dto.getMinPrice() : null)
                .setMainPicUrl(ObjectUtils.isNotEmpty(dto) ? dto.getMainPicUrl() : "")
                .setProdTitle(ObjectUtils.isNotEmpty(dto) ? dto.getProdTitle() : "")
                .setHasVideo(ObjectUtils.isNotEmpty(dto) ? dto.getHasVideo() : Boolean.FALSE)
                .setTags(ObjectUtils.isNotEmpty(dto) ? dto.getTags() : null);
    }

    /**
     * 获取以图搜款的广告
     *
     * @param picSearchList 图搜热款数据库数据
     * @param prodStatsMap  图搜次数map
     * @return List<PicSearchAdvertDTO>
     */
    private List<PicSearchAdvertDTO> getPicSearchAdvertList(List<AdvertRound> picSearchList, Map<Long, StoreProdViewDTO> viewMap, Map<Long, Long> prodStatsMap) {
        return picSearchList.stream().limit(10).map(advertRound -> {
            final Long storeProdId = Long.valueOf(advertRound.getProdIdStr());
            StoreProdViewDTO viewDTO = viewMap.get(storeProdId);
            String mainPic = ObjectUtils.isNotEmpty(viewDTO) ? viewDTO.getMainPicUrl() : "";
            List<ProductMatchDTO> results = StringUtils.isNotBlank(mainPic)
                    ? pictureService.searchProductByPicKey(mainPic, Constants.IMG_SEARCH_MAX_PAGE_NUM) : new ArrayList<>();
            List<String> prodTagList = new ArrayList<String>() {{
                add("同类热卖");
            }};
            CollectionUtils.addAll(prodTagList, ObjectUtils.isNotEmpty(viewDTO) && StringUtils.isNotBlank(viewDTO.getTagStr())
                    ? StrUtil.split(viewDTO.getTagStr(), ",") : new ArrayList<>());
            return new PicSearchAdvertDTO()
                    .setImgSearchCount(prodStatsMap.getOrDefault(storeProdId, 1L))
                    .setSameProdCount(results.size()).setStoreProdId(storeProdId).setStoreId(advertRound.getStoreId()).setTags(prodTagList)
                    .setStoreName(ObjectUtils.isNotEmpty(viewDTO) ? viewDTO.getStoreName() : "")
                    .setPrice(ObjectUtils.isNotEmpty(viewDTO) ? viewDTO.getPrice() : null)
                    .setProdArtNum(ObjectUtils.isNotEmpty(viewDTO) ? viewDTO.getProdArtNum() : "")
                    .setMainPicUrl(ObjectUtils.isNotEmpty(viewDTO) ? viewDTO.getMainPicUrl() : "")
                    .setProdTitle(ObjectUtils.isNotEmpty(viewDTO) ? viewDTO.getProdTitle() : "");
        }).collect(Collectors.toList());
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
     * @param advertRoundList        广告列表
     * @param prodPriceAndMainPicMap 商品价格及主图map
     * @param existProdIdList        已存在的商品ID列表
     * @param limitCount             返回的 数量
     * @return
     */
    private List<PCSearchResultAdvertDTO> fillPcSearchResultAdvertList(List<AdvertRound> advertRoundList, Map<Long, StoreProdPriceAndMainPicDTO> prodPriceAndMainPicMap,
                                                                       Map<Long, Store> storeMap, List<Long> existProdIdList, int limitCount) {
        List<PCSearchResultAdvertDTO> tempList = new ArrayList<>();
        advertRoundList.stream().collect(Collectors.groupingBy(AdvertRound::getStoreId))
                .forEach((storeId, list) -> {
                    AdvertRound advertRound = list.stream().filter(x -> StringUtils.isNotBlank(x.getProdIdStr()))
                            .filter(x -> CollectionUtils.isEmpty(existProdIdList) || !existProdIdList.contains(Long.parseLong(x.getProdIdStr()))).findAny().orElse(null);
                    if (ObjectUtils.isNotEmpty(advertRound)) {
                        final Long storeProdId = Long.parseLong(advertRound.getProdIdStr());
                        tempList.add(new PCSearchResultAdvertDTO().setDisplayType(AdDisplayType.PRODUCT.getValue()).setStoreProdId(storeProdId).setStoreId(storeId)
                                .setStoreName(ObjectUtils.isNotEmpty(storeMap.get(storeId)) ? storeMap.get(storeId).getStoreName() : "").setFocus(Boolean.FALSE)
                                .setPayPrice(ObjectUtils.isNotEmpty(advertRound.getPayPrice()) ? advertRound.getPayPrice() : BigDecimal.ZERO)
                                .setPrice(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getMinPrice() : null)
                                .setProdArtNum(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getProdArtNum() : "")
                                .setMainPicUrl(ObjectUtils.isNotEmpty(prodPriceAndMainPicMap.get(storeProdId)) ? prodPriceAndMainPicMap.get(storeProdId).getMainPicUrl() : ""));
                    }
                });
        return tempList.stream().limit(limitCount).collect(Collectors.toList());
    }

    /**
     * 网站首页搜索
     *
     * @param searchDTO 搜索入参
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ESProductDTO> search(IndexSearchDTO searchDTO) throws IOException {
        return this.esService.search(searchDTO);
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
        if (CollectionUtils.isEmpty(adverts)) {
            return dataList;
        }
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

    /**
     * 更新用户搜索结果到redis
     *
     * @param search 用户搜索内容
     */
    private void updateRedisUserSearchHistory(String search) {
        if (StringUtils.isEmpty(search)) {
            return;
        }
        Long userId = SecurityUtils.getUserIdSafe();
        if (ObjectUtils.isEmpty(userId)) {
            return;
        }
        // 将用户搜索的数据存放到redis中，每晚统一存到数据库中
        LoginUser loginUser = SecurityUtils.getLoginUser();
        // 获取用户在redis中的搜索数据
        List<UserSearchHistoryDTO> userSearchList = CollUtil.defaultIfEmpty(redisCache.getCacheObject(CacheConstants.USER_SEARCH_HISTORY + loginUser.getUserId()), new ArrayList<>());
        userSearchList.add(new UserSearchHistoryDTO().setUserId(loginUser.getUserId()).setUserName(loginUser.getUser().getNickName()).setSearchContent(search)
                .setPlatformId(SearchPlatformType.PC.getValue()).setSearchTime(new Date()));
        // 设置用户搜索历史，不过期
        redisCache.setCacheObject(CacheConstants.USER_SEARCH_HISTORY + loginUser.getUserId(), userSearchList);
    }


}
