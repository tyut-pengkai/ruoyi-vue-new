package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.StoreProduct;
import com.ruoyi.xkt.dto.dailyStoreTag.DailyStoreTagDTO;
import com.ruoyi.xkt.dto.picture.ProductMatchDTO;
import com.ruoyi.xkt.dto.storeHomepage.StoreRecommendResDTO;
import com.ruoyi.xkt.dto.storeProduct.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 档口商品Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Repository
public interface StoreProductMapper extends BaseMapper<StoreProduct> {

    /**
     * 根据商品货号模糊查询档口商品并返回商品主图
     *
     * @param storeId    档口ID
     * @param prodArtNum 货号
     * @return List<StoreProdFuzzyResPicDTO>
     */
    List<StoreProdFuzzyResPicDTO> fuzzyQueryResPicList(@Param("storeId") Long storeId, @Param("prodArtNum") String prodArtNum);

    /**
     * 档口商品ID列表
     * 过滤掉 私款商品
     *
     * @param idList id列表
     * @return List<ProductESDTO>
     */
    List<ProductESDTO> selectESDTOList(@Param("idList") List<Long> idList);

    /**
     * 获取档口商品的sku列表
     *
     * @param storeProdId 档口商品ID
     * @return List<StoreProdSkuDTO>
     */
    List<StoreProdSkuDTO> selectSkuList(Long storeProdId);

    /**
     * 档口商品的价格及商品主图
     *
     * @param storeProdIdList 档口商品ID列表
     * @return List<StoreProdPriceAndMainPicDTO>
     */
    List<StoreProdPriceAndMainPicDTO> selectPriceAndMainPicList(@Param("storeProdIdList") List<Long> storeProdIdList);

    /**
     * 获取商品的价格主图及标签列表等
     *
     * @param storeProdIdList 档口商品ID列表
     * @return List<StoreProdPriceAndMainPicAndTagDTO>
     */
    List<StoreProdPriceAndMainPicAndTagDTO> selectPriceAndMainPicAndTagList(@Param("storeProdIdList") List<Long> storeProdIdList);

    /**
     * 获取商品展示的各种基本属性
     *
     * @param storeProdIdList 档口商品ID列表
     * @return List<PicSearchAdvertDTO>
     */
    List<StoreProdViewDTO> getStoreProdViewAttr(@Param("storeProdIdList") List<Long> storeProdIdList,
                                                @Param("voucherDateStart") Date voucherDateStart,
                                                @Param("voucherDateEnd") Date voucherDateEnd);

    /**
     * 获取图搜热款时，商品的各种基本属性
     *
     * @param storeProdIdList 档口商品ID列表
     * @return List<PicSearchAdvertDTO>
     */
    List<StoreProdViewDTO> getSearchHotViewAttr(@Param("storeProdIdList") List<Long> storeProdIdList);

    /**
     * 获取新款频出的前20名档口
     *
     * @param yesterday  昨天
     * @param oneWeekAgo 一周前
     * @return List<DailyStoreTagDTO>
     */
    List<DailyStoreTagDTO> selectTop20List(@Param("yesterday") Date yesterday, @Param("oneWeekAgo") Date oneWeekAgo);

    /**
     * 获取PC商品详情页信息
     *
     * @param storeProdId 当前商品ID
     * @param userId      当前登录用户ID
     * @return StoreProdPCResDTO
     */
    StoreProdPCResDTO selectPCProdInfo(@Param("storeProdId") Long storeProdId, @Param("userId") Long userId);

    /**
     * 获取档口各个状态的分类数量
     *
     * @param cateNumDTO 查询入参
     * @return StoreProdStatusCateCountResDTO
     */
    List<StoreProdStatusCateCountDTO> getStatusCateNum(StoreProdStatusCateNumDTO cateNumDTO);

    /**
     * 获取APP 档口商品详情
     *
     * @param storeProdId 商品ID
     * @param userId
     * @return StoreProdAppResDTO
     */
    StoreProdAppResDTO getAppInfo(@Param("storeProdId") Long storeProdId, @Param("userId") Long userId);

    /**
     * 获取档口各个状态的数量
     *
     * @param storeId 档口ID
     * @return StoreProdStatusCountResDTO
     */
    StoreProdStatusCountResDTO getStatusNum(@Param("storeId") Long storeId);

    /**
     * 获取档口最新的10个商品
     *
     * @param storeId 档口ID
     * @return List<StoreRecommendResDTO>
     */
    List<StoreRecommendResDTO> selectLatest10List(@Param("storeId") Long storeId);

    /**
     * 获取商品下载信息
     *
     * @param storeProdId 档口商品ID
     * @return StoreProdPcDownloadInfoResDTO
     */
    StoreProdPcDownloadInfoResDTO selectDownloadProdInfo(@Param("storeProdId") Long storeProdId);

    /**
     * 过滤掉无效的档口商品
     *
     * @param storeProdIdList 档口商品ID列表
     * @return List<ProductMatchDTO>
     */
    List<ProductMatchDTO> filterInvalidStoreProd(@Param("storeProdIdList") List<Long> storeProdIdList);

}

