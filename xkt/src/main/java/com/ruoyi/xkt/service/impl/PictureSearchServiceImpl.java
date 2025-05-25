package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.xkt.domain.PictureSearch;
import com.ruoyi.xkt.domain.SysFile;
import com.ruoyi.xkt.dto.advertRound.picSearch.PicSearchAdvertDTO;
import com.ruoyi.xkt.dto.picture.ProductImgSearchCountDTO;
import com.ruoyi.xkt.dto.picture.ProductMatchDTO;
import com.ruoyi.xkt.dto.picture.SearchRequestDTO;
import com.ruoyi.xkt.dto.picture.TopProductMatchDTO;
import com.ruoyi.xkt.dto.storeProduct.StoreProdViewDTO;
import com.ruoyi.xkt.mapper.PictureSearchMapper;
import com.ruoyi.xkt.mapper.StoreProductFileMapper;
import com.ruoyi.xkt.mapper.StoreProductMapper;
import com.ruoyi.xkt.mapper.SysFileMapper;
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
    private StoreProductFileMapper storeProductFileMapper;
    @Autowired
    private IPictureService pictureService;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private IWebsitePCService websitePCService;
    @Autowired
    private StoreProductMapper storeProdMapper;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<StoreProdViewDTO> searchProductByPic(SearchRequestDTO requestDTO) {

        // TODO 校验当前登录者角色，若非电商卖家 或 管理员 或 超级管理员，则不可操作
        // TODO 校验当前登录者角色，若非电商卖家 或 管理员 或 超级管理员，则不可操作
        // TODO 校验当前登录者角色，若非电商卖家 或 管理员 或 超级管理员，则不可操作

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
        List<ProductMatchDTO> results = pictureService.searchProductByPicKey(requestDTO.getPicKey(),
                requestDTO.getNum());
        ThreadUtil.execAsync(() -> {
            for (ProductMatchDTO result : results) {
                //图搜次数+1
                redisCache.valueIncr(CacheConstants.PRODUCT_STATISTICS_IMG_SEARCH_COUNT, result.getStoreProductId());
            }
        });
        // 档口商品显示的基本属性
        List<StoreProdViewDTO> storeProdViewAttrList = this.storeProdMapper.getStoreProdViewAttr(results.stream()
                        .map(ProductMatchDTO::getStoreProductId).distinct().collect(Collectors.toList()),
                java.sql.Date.valueOf(LocalDate.now()), java.sql.Date.valueOf(LocalDate.now().minusMonths(2)));
        // 设置商品标签
        storeProdViewAttrList.stream().filter(x -> StringUtils.isNotBlank(x.getTagStr())).forEach(x -> x.setProdTagList(StrUtil.split(x.getTagStr(), ",")));
        // 以图搜款广告
        List<PicSearchAdvertDTO> picSearchAdverts = websitePCService.getPicSearchList();
        // 将广告插入到预定位置
        return insertAdvertsIntoList(storeProdViewAttrList, BeanUtil.copyToList(picSearchAdverts, StoreProdViewDTO.class), Constants.pic_res_insert_positions);
    }

    /**
     * 图搜热款列表
     * @return List<TopProductMatchDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public List<TopProductMatchDTO> listImgSearchTopProduct() {
        List<TopProductMatchDTO> topProductMatchList = redisCache.getCacheObject(CacheConstants.TOP_IMG_SEARCH_PRODUCT);
        if (CollectionUtils.isNotEmpty(topProductMatchList)) {
            return topProductMatchList;
        }
        // 重新缓存数据到redis
        this.cacheImgSearchTopProduct();
        return redisCache.getCacheObject(CacheConstants.TOP_IMG_SEARCH_PRODUCT);
    }


    @Override
    public void cacheImgSearchTopProduct() {
        //热搜规则（暂定）：1个月内搜图次数排序，最多返回前100，查询同款商品时最多检索1000条数据
        List<ProductImgSearchCountDTO> prodImgSearchCounts = pictureSearchMapper
                .listProductImgSearchCount(DateUtil.beginOfDay(java.sql.Date.valueOf(LocalDate.now())), java.sql.Date.valueOf(LocalDate.now().minusMonths(1)));
        if (CollUtil.isEmpty(prodImgSearchCounts)) {
            return;
        }
        Map<Long, Integer> searchCountMap = prodImgSearchCounts.stream().collect(Collectors
                .toMap(ProductImgSearchCountDTO::getStoreProductId, ProductImgSearchCountDTO::getImgSearchCount));
        // 档口商品显示的基本属性
        List<StoreProdViewDTO> storeProdViewAttrList = this.storeProdMapper.getSearchHotViewAttr(prodImgSearchCounts.stream()
                .map(ProductImgSearchCountDTO::getStoreProductId).distinct().collect(Collectors.toList()));
        storeProdViewAttrList = storeProdViewAttrList.stream().map(x -> {
                    //根据主图搜索同类商品
                    List<ProductMatchDTO> pmList = pictureService.searchProductByPicKey(x.getMainPicUrl(), 1000);
                    return x.setSameProdCount(pmList.size()).setImgSearchCount(ObjectUtils.defaultIfNull(searchCountMap.get(x.getStoreProdId()), 0));
                })
                .sorted(Comparator.comparing(StoreProdViewDTO::getImgSearchCount).reversed())
                .limit(100)
                .collect(Collectors.toList());
        redisCache.setCacheObject(CacheConstants.TOP_IMG_SEARCH_PRODUCT, storeProdViewAttrList);
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
        List<T> mergedList = new ArrayList<>();
        int dataIndex = 0;
        int advertIndex = 0;
        int size = dataList.size();
        for (int i = 0; i < size + positions.size(); i++) {
            if (positions.contains(i) && advertIndex < adverts.size()) {
                // 如果当前位置需要插入广告，并且还有广告可插入
                mergedList.add(adverts.get(advertIndex++));
            } else if (dataIndex < size) {
                // 插入原始数据
                mergedList.add(dataList.get(dataIndex++));
            } else {
                // 源数据已经插完，但仍有广告未插入，直接追加到末尾
                mergedList.add(adverts.get(advertIndex++));
            }
        }
        return mergedList;
    }

}
