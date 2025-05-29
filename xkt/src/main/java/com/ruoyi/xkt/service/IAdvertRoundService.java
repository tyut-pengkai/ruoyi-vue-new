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
     * 根据广告ID获取推广轮次列表，并返回当前档口在这些推广轮次的数据
     *
     * @param storeId  档口ID
     * @param advertId 广告ID
     * @param showType 时间范围 位置枚举
     * @return AdRoundPlayStoreResDTO
     */
    AdRoundStoreResDTO getStoreAdInfo(Long storeId, Long advertId, Integer showType);

    /**
     * 档口购买推广营销
     *
     * @param createDTO 购买入参
     * @return Integer
     */
    Integer create(AdRoundStoreCreateDTO createDTO) throws ParseException;

    /**
     * 初始化每天购买广告的截止时间
     *
     * @throws ParseException
     */
    void saveAdvertDeadlineToRedis() throws ParseException;

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
     * @param picDTO 档口上传推广图入参
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

}
