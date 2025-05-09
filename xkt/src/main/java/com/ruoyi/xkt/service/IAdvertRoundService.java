package com.ruoyi.xkt.service;

import com.ruoyi.xkt.dto.advertRound.AdRoundStoreCreateDTO;
import com.ruoyi.xkt.dto.advertRound.AdRoundStoreResDTO;

import java.text.ParseException;

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
     * @param storeId 档口ID
     * @param advertId  广告ID
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
    Integer create(AdRoundStoreCreateDTO createDTO);

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

    void test();

    void updateBiddingStatus() throws ParseException;
}
