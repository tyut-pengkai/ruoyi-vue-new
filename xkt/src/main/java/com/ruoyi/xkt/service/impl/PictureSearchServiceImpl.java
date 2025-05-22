package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageHelper;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.xkt.domain.PictureSearch;
import com.ruoyi.xkt.domain.SysFile;
import com.ruoyi.xkt.dto.picture.ProductImgSearchCountDTO;
import com.ruoyi.xkt.dto.picture.ProductMatchDTO;
import com.ruoyi.xkt.dto.picture.SearchRequestDTO;
import com.ruoyi.xkt.dto.picture.TopProductMatchDTO;
import com.ruoyi.xkt.dto.storeProductFile.StoreProdMainPicDTO;
import com.ruoyi.xkt.enums.FileType;
import com.ruoyi.xkt.mapper.PictureSearchMapper;
import com.ruoyi.xkt.mapper.StoreProductFileMapper;
import com.ruoyi.xkt.mapper.SysFileMapper;
import com.ruoyi.xkt.service.IPictureSearchService;
import com.ruoyi.xkt.service.IPictureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.ruoyi.common.constant.Constants.ORDER_NUM_1;

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


    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<ProductMatchDTO> searchProductByPic(SearchRequestDTO requestDTO) {
        Assert.notEmpty(requestDTO.getPicKey());
        SysFile sysFile = new SysFile();
        sysFile.setFileUrl(requestDTO.getPicKey());
        sysFile.setFileSize(requestDTO.getPicSize());
        sysFile.setVersion(0);
        sysFile.setDelFlag(Constants.UNDELETED);
        sysFileMapper.insert(sysFile);
        PictureSearch pictureSearch = new PictureSearch();
        pictureSearch.setSearchFileId(sysFile.getId());
        pictureSearch.setUserId(requestDTO.getUserId());
        pictureSearch.setVoucherDate(new Date());
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
        return results;
    }

    @Override
    public List<TopProductMatchDTO> listImgSearchTopProduct() {
        String cacheStr = redisCache.getCacheObject(CacheConstants.TOP_IMG_SEARCH_PRODUCT);
        return JSONUtil.toList(cacheStr, TopProductMatchDTO.class);
    }

    @Override
    public void cacheImgSearchTopProduct() {
        //热搜规则（暂定）：1个月内搜图匹配次数前30的商品，查询同款商品时最多检索1000条数据
        Date end = DateUtil.date();
        Date begin = DateUtil.offset(end, DateField.MONTH, -1);
        PageHelper.startPage(1, 30, false);
        //统计表数据
        List<ProductImgSearchCountDTO> prodImgSearchCounts = pictureSearchMapper
                .listProductImgSearchCount(DateUtil.beginOfDay(begin), end);
        if (CollUtil.isEmpty(prodImgSearchCounts)) {
            return;
        }
        //产品主图
        List<Long> spIds = prodImgSearchCounts.stream().map(ProductImgSearchCountDTO::getStoreProductId)
                .collect(Collectors.toList());
        Map<Long, String> mainPicMap = storeProductFileMapper.selectMainPicByStoreProdIdList(spIds,
                FileType.MAIN_PIC.getValue(), ORDER_NUM_1).stream()
                .collect(Collectors.toMap(StoreProdMainPicDTO::getStoreProdId, StoreProdMainPicDTO::getFileUrl,
                        (o, n) -> n));
        List<TopProductMatchDTO> topProductMatchList = new ArrayList<>(prodImgSearchCounts.size());
        for (ProductImgSearchCountDTO prodImgSearchCount : prodImgSearchCounts) {
            TopProductMatchDTO topProductMatch = BeanUtil.toBean(prodImgSearchCount, TopProductMatchDTO.class);
            //根据主图搜索同类商品
            List<ProductMatchDTO> pmList = pictureService.searchProductByPicKey(mainPicMap
                    .get(prodImgSearchCount.getStoreProductId()), 1000);
            topProductMatch.setSameProductCount(pmList.size());
            topProductMatchList.add(topProductMatch);
        }
        redisCache.setCacheObject(CacheConstants.TOP_IMG_SEARCH_PRODUCT, JSONUtil.toJsonStr(topProductMatchList));
    }
}
