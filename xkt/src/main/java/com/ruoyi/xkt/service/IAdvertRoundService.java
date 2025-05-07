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
     * 获取当前类型下档口的推广营销数据
     *
     * @param storeId 档口ID
     * @param typeId  推广类型ID
     * @return AdRoundPlayStoreResDTO
     */
    AdRoundStoreResDTO getStoreAdInfo(Long storeId, Integer typeId);

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

}
