package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.xkt.domain.PictureSearch;
import com.ruoyi.xkt.domain.StoreMember;
import com.ruoyi.xkt.domain.SysFile;
import com.ruoyi.xkt.domain.UserFavorites;
import com.ruoyi.xkt.dto.advertRound.picSearch.PicSearchAdvertDTO;
import com.ruoyi.xkt.dto.picture.ProductImgSearchCountDTO;
import com.ruoyi.xkt.dto.picture.ProductMatchDTO;
import com.ruoyi.xkt.dto.picture.SearchRequestDTO;
import com.ruoyi.xkt.dto.storeProduct.StoreProdViewDTO;
import com.ruoyi.xkt.mapper.*;
import com.ruoyi.xkt.service.IPictureSearchService;
import com.ruoyi.xkt.service.IPictureService;
import com.ruoyi.xkt.service.IWebsitePCService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 以图搜款Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Slf4j
@Service
public class PictureSearchServiceImpl implements IPictureSearchService {

    @Autowired
    private PictureSearchMapper pictureSearchMapper;
    @Autowired
    private SysFileMapper sysFileMapper;
    @Autowired
    private IPictureService pictureService;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private IWebsitePCService websitePCService;
    @Autowired
    private StoreProductMapper storeProdMapper;
    @Autowired
    private StoreProductStatisticsMapper storeProdStatisticsMapper;
    @Autowired
    private UserFavoritesMapper userFavMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<StoreProdViewDTO> searchProductByPic(SearchRequestDTO requestDTO) {
        // 以图搜款广告
        List<PicSearchAdvertDTO> picSearchAdverts = websitePCService.getPicSearchList();
        if (StringUtils.isBlank(requestDTO.getPicKey()) || StringUtils.isBlank(requestDTO.getPicName())
                || requestDTO.getNum() <= 0) {
            return BeanUtil.copyToList(picSearchAdverts, StoreProdViewDTO.class);
        }
        Assert.notEmpty(requestDTO.getPicKey());
        SysFile sysFile = new SysFile().setFileUrl(requestDTO.getPicKey()).setFileName(requestDTO.getPicName()).setFileSize(requestDTO.getPicSize());
        sysFile.setVersion(0);
        sysFile.setDelFlag(Constants.UNDELETED);
        sysFileMapper.insert(sysFile);
        PictureSearch pictureSearch = new PictureSearch().setSearchFileId(sysFile.getId()).setUserId(SecurityUtils.getUserId())
                .setVoucherDate(java.sql.Date.valueOf(LocalDate.now()));
        pictureSearch.setVersion(0);
        pictureSearch.setDelFlag(Constants.UNDELETED);
        pictureSearchMapper.insert(pictureSearch);
        //搜索
        List<ProductMatchDTO> results = pictureService.searchProductByPicKey(requestDTO.getPicKey(), requestDTO.getNum());
        ThreadUtil.execAsync(() -> {
            for (ProductMatchDTO result : results) {
                //图搜次数+1
                redisCache.valueIncr(CacheConstants.PRODUCT_STATISTICS_IMG_SEARCH_COUNT, result.getStoreProductId());
            }
        });
        // 没有搜出结果，则直接返回广告
        if (CollectionUtils.isEmpty(results)) {
            return BeanUtil.copyToList(picSearchAdverts, StoreProdViewDTO.class);
        }
        // 图搜的商品ID列表
        final List<Long> storeProdIdList = picSearchAdverts.stream().map(x -> x.getStoreProdId()).collect(Collectors.toList());
        // 搜索结果过滤掉广告商品
        List<ProductMatchDTO> filterResults = results.stream().filter(x -> !storeProdIdList.contains(x.getStoreProductId())).collect(Collectors.toList());
        // 档口商品显示的基本属性 数据库筛选，必须要带prodStatus，因为图搜搜出来的可能是下架的商品
        List<StoreProdViewDTO> storeProdViewAttrList = this.storeProdMapper.getStoreProdViewAttr(filterResults.stream()
                        .map(ProductMatchDTO::getStoreProductId).distinct().collect(Collectors.toList()),
                java.sql.Date.valueOf(LocalDate.now().minusMonths(2)), java.sql.Date.valueOf(LocalDate.now()));
        // 设置商品标签
        storeProdViewAttrList.stream().filter(x -> StringUtils.isNotBlank(x.getTagStr())).forEach(x -> x.setTags(StrUtil.split(x.getTagStr(), ",")));
        // 将广告插入到预定位置
        return insertAdvertsIntoList(storeProdViewAttrList, BeanUtil.copyToList(picSearchAdverts, StoreProdViewDTO.class), Constants.PIC_SEARCH_INSERT_POSITIONS);
    }

    /**
     * 图搜热款列表
     *
     * @return List<TopProductMatchDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public List<StoreProdViewDTO> listImgSearchTopProduct() {
        List<StoreProdViewDTO> picSearchHotList = redisCache.getCacheObject(CacheConstants.IMG_SEARCH_PRODUCT_HOT);
        if (CollectionUtils.isEmpty(picSearchHotList)) {
            // 重新缓存数据到redis
            picSearchHotList = this.cacheImgSearchTopProduct();
        }
        picSearchHotList.forEach(x -> {
            // 查询档口会员等级
            StoreMember member = this.redisCache.getCacheObject(CacheConstants.STORE_MEMBER + x.getStoreId());
            x.setMemberLevel(ObjectUtils.isNotEmpty(member) ? member.getLevel() : null);
        });
        // 设置用户是否关注
        Long userId = SecurityUtils.getUserIdSafe();
        if (ObjectUtils.isNotEmpty(userId)) {
            // 获取用户关注了哪些商品
            List<UserFavorites> userFavList = this.userFavMapper.selectList(new LambdaQueryWrapper<UserFavorites>()
                    .eq(UserFavorites::getDelFlag, Constants.UNDELETED).eq(UserFavorites::getUserId, userId)
                    .in(UserFavorites::getStoreProdId, picSearchHotList.stream()
                            .map(StoreProdViewDTO::getStoreProdId).collect(Collectors.toList())));
            Map<Long, Long> userFavMap = CollectionUtils.isEmpty(userFavList) ? new HashMap<>()
                    : userFavList.stream().collect(Collectors.toMap(UserFavorites::getStoreProdId, UserFavorites::getStoreProdId));
            // 设置是否已关注
            picSearchHotList.forEach(x -> x.setFocus(userFavMap.containsKey(x.getStoreProdId())));
        }
        return picSearchHotList;
    }


    @Override
    public List<StoreProdViewDTO> cacheImgSearchTopProduct() {
        //热搜规则（暂定）：1个月内搜图次数排序，最多返回前100，查询同款商品时最多检索1000条数据
        List<ProductImgSearchCountDTO> prodImgSearchCounts = storeProdStatisticsMapper
                .listProdImgSearchCount(java.sql.Date.valueOf(LocalDate.now().minusMonths(1)), java.sql.Date.valueOf(LocalDate.now()));
        if (CollUtil.isEmpty(prodImgSearchCounts)) {
            return new ArrayList<>();
        }
        Map<Long, Integer> searchCountMap = prodImgSearchCounts.stream().collect(Collectors
                .toMap(ProductImgSearchCountDTO::getStoreProductId, ProductImgSearchCountDTO::getImgSearchCount));
        // 档口商品显示的基本属性
        List<StoreProdViewDTO> storeProdViewAttrList = this.storeProdMapper.getSearchHotViewAttr(prodImgSearchCounts.stream()
                .map(ProductImgSearchCountDTO::getStoreProductId).distinct().collect(Collectors.toList()));
        storeProdViewAttrList = storeProdViewAttrList.stream().map(x -> {
                    //根据主图搜索同类商品
                    List<ProductMatchDTO> pmList = pictureService.searchProductByPicKey(x.getMainPicUrl(), 50);
                    return x.setTags(StrUtil.split(x.getTagStr(), ",")).setSameProdCount(pmList.size())
                            .setImgSearchCount(ObjectUtils.defaultIfNull(searchCountMap.get(x.getStoreProdId()), 0));
                })
                .sorted(Comparator.comparing(StoreProdViewDTO::getImgSearchCount).reversed())
                .collect(Collectors.toList());
        // 所有的图搜热款
        redisCache.setCacheObject(CacheConstants.IMG_SEARCH_PRODUCT_HOT_TOTAL, storeProdViewAttrList);
        // 图搜热款前100
        redisCache.setCacheObject(CacheConstants.IMG_SEARCH_PRODUCT_HOT, storeProdViewAttrList.stream().limit(100).collect(Collectors.toList()));
        return storeProdViewAttrList;
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

}
