package com.ruoyi.xkt.service;

import com.ruoyi.common.core.page.Page;
import com.ruoyi.xkt.dto.advertRound.*;

import java.text.ParseException;
import java.util.List;

/**
 * 推广营销Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IAdvertRoundService {

    /**
     * 档口购买推广营销
     *
     * @param createDTO 购买入参
     * @return Integer
     */
    Integer create(AdRoundStoreCreateDTO createDTO);

    /**
     * 初始化每天购买广告的截止时间
     */
    void saveAdvertDeadlineToRedis();

    /**
     * 初始化广告竞争的资源锁
     */
    void initAdvertLockMap();

    /**
     * 每晚11:30更新广告位轮次状态 将biddingTempStatus赋值给biddingStatus
     *
     * @throws ParseException
     */
    void updateBiddingStatus() throws ParseException;

    /**
     * 根据advertRoundId获取当前位置枚举设置的商品
     *
     * @param advertRoundId advertRoundId
     * @return AdRoundSetProdResDTO
     */
    List<AdRoundSetProdResDTO> getSetProdInfo(Long advertRoundId);

    /**
     * 档口已订购推广列表
     *
     * @param pageDTO 分页入参
     * @return Page<AdvertRoundStorePageResDTO>
     */
    Page<AdvertRoundStorePageResDTO> page(AdvertRoundStorePageDTO pageDTO);

    /**
     * 获取当前推广位的推广图
     *
     * @param storeId       档口ID
     * @param advertRoundId 推广位ID
     * @return AdRoundSetPicResDTO
     */
    AdRoundStoreSetResDTO getAdvertStoreSetInfo(Long storeId, Long advertRoundId);

    /**
     * 档口退订推广位
     *
     * @param storeId       档口ID
     * @param advertRoundId 推广位ID
     * @return
     */
    Integer unsubscribe(Long storeId, Long advertRoundId);

    /**
     * 获取最受欢迎8个推广位
     *
     * @return List<AdRoundPopularResDTO>
     */
    List<AdRoundPopularResDTO> getMostPopulars();

    /**
     * 获取当前推广位最高的价格及档口设置的商品
     *
     * @param latestDTO 查询入参
     * @return
     */
    AdRoundLatestResDTO getLatestInfo(AdRoundLatestDTO latestDTO);

    /**
     * @param picDTO 档口上传推广图 或 修改商品
     * @return Integer
     */
    Integer updateAdvert(AdRoundUpdateDTO picDTO);

    /**
     * 获取审核失败的拒绝理由
     *
     * @param advertRoundId 档口轮次ID
     * @return String
     */
    String getRejectReason(Long advertRoundId);

    /**
     * 获取推广类型所有轮次
     *
     * @param storeId 档口ID
     * @param typeId  推广类型ID
     * @return List<AdTypeRoundResDTO>
     */
    List<AdRoundTypeRoundResDTO> getTypeRoundList(Long storeId, Long typeId);

    /**
     * 位置枚举的推广位档口购买情况
     *
     * @param storeId 档口ID
     * @param typeId  类型ID
     * @param roundId 轮次ID
     * @return List<AdTypeRoundBoughtResDTO>
     */
    List<AdRoundTypeRoundBoughtResDTO> getTypeRoundBoughtInfo(Long storeId, Long typeId, Long roundId);

    /**
     * 档口购买推广轮次列表
     *
     * @param storeId 档口ID
     * @return List<AdRoundStoreBoughtResDTO>
     */
    List<AdRoundStoreBoughtResDTO> getStoreBoughtRecord(Long storeId);

    /**
     * 根据推广位置ID及档口ID 获取 设置的推广商品或图片
     *
     * @param advertRoundId 推广位置ID
     * @return AdRoundLatestResDTO
     */
    AdRoundLatestResDTO getSetInfo(Long advertRoundId);
}
