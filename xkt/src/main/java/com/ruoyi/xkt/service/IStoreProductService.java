package com.ruoyi.xkt.service;

import com.ruoyi.common.core.page.Page;
import com.ruoyi.xkt.dto.storeProduct.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * 档口商品Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IStoreProductService {
    /**
     * 查询档口商品
     *
     * @param storeProdId 档口商品主键
     * @return 档口商品
     */
    StoreProdResDTO selectStoreProductByStoreProdId(Long storeProdId);

    /**
     * 查询档口商品列表
     *
     * @param pageDTO 查询入参
     * @return Page<StoreProdPageResDTO>
     */
    Page<StoreProdPageResDTO> page(StoreProdPageDTO pageDTO);

    /**
     * 新增档口商品
     *
     * @param storeProdDTO 档口商品
     * @return 结果
     */
    int create(StoreProdDTO storeProdDTO) throws IOException;

    /**
     * 修改档口商品
     *
     * @param storeProdDTO 档口商品
     * @return 结果
     */
    int update(Long storeProdId, StoreProdDTO storeProdDTO) throws IOException;

    /**
     * 更新档口商品状态 更新档口商品颜色的状态 2在售 3尾货 4下架
     *
     * @param prodStatusDTO 更新状态入参
     */
    Integer updateStoreProductStatus(StoreProdStatusDTO prodStatusDTO) throws IOException;

    /**
     * 根据档口ID和商品货号模糊查询货号列表
     *
     * @param storeId    档口ID
     * @param prodArtNum 商品货号
     * @return List<StoreProdFuzzyResDTO>
     */
    List<StoreProdFuzzyColorResDTO> fuzzyQueryColorList(Long storeId, String prodArtNum);

    /**
     * 根据档口ID和商品货号模糊查询货号列表，返回数据带上商品主图
     *
     * @param storeId    档口ID
     * @param prodArtNum 商品货号
     * @return List<StoreProdFuzzyResPicDTO>
     */
    List<StoreProdFuzzyResPicDTO> fuzzyQueryResPicList(Long storeId, String prodArtNum);

    /**
     * 获取商品所有的风格
     *
     * @return
     */
    List<String> getStyleList();

    /**
     * APP获取档口商品详情
     *
     * @param storeProdId 档口商品ID
     * @return StoreProdAppResDTO
     */
    StoreProdAppResDTO getAppInfo(Long storeProdId);

    /**
     * 获取档口商品颜色及sku等
     *
     * @param storeProdId 档口商品ID
     * @return StoreProdSkuResDTO
     */
    StoreProdSkuResDTO getSkuList(Long storeProdId);

    /**
     * 添加商品统计信息
     *
     * @param storeProdId
     * @param incrViewCount
     * @param incrDownloadCount
     * @param incrImgSearchCount
     * @param date
     */
    void insertOrUpdateProductStatistics(Long storeProdId, Integer incrViewCount, Integer incrDownloadCount,
                                         Integer incrImgSearchCount, Date date);

    /**
     * PC获取档口商品详情
     *
     * @param storeProdId 档口商品ID
     * @return StoreProdPCResDTO
     */
    StoreProdPCResDTO getPCInfo(Long storeProdId);

    /**
     * 获取档口商品各个状态数量
     *
     * @param storeId 档口ID
     * @return StoreProdStatusCountResDTO
     */
    StoreProdStatusCountResDTO getStatusNum(Long storeId);

    /**
     * 获取商品状态下分类数量
     *
     * @param dto 查询入参
     * @return StoreProdStatusCateCountResDTO
     */
    List<StoreProdStatusCateCountResDTO> getStatusCateNum(StoreProdStatusCateNumDTO dto);

    /**
     * 推广营销查询最近30天上新商品
     *
     * @param storeId    档口ID
     * @param prodArtNum 商品货号
     * @return List<StoreProdFuzzyLatest20ResDTO>
     */
    List<StoreProdFuzzyLatest30ResDTO> fuzzyQueryLatest30List(Long storeId, String prodArtNum);

    /**
     * 准备获取图包下载链接
     *
     * @param storeProductId
     * @return
     */
    List<PicPackSimpleDTO> prepareGetPicPackDownloadUrl(Long storeProductId);

    /**
     * 获取图包下载链接（提供给前端请求使用）
     *
     * @param picPackReqDTO
     * @return
     */
    PicPackInfoDTO getPicPackDownloadUrl(PicPackReqDTO picPackReqDTO);

    /**
     * 模糊查询系统所有商品
     *
     * @param prodArtNum 货号
     * @return StoreProdFuzzyResDTO
     */
    List<StoreProdFuzzyResDTO> fuzzyQuery(String prodArtNum);
}
