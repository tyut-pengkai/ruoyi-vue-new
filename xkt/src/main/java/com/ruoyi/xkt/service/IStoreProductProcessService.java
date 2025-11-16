package com.ruoyi.xkt.service;

import com.ruoyi.xkt.dto.storeProdProcess.StoreProdProcessDTO;
import com.ruoyi.xkt.dto.storeProdProcess.StoreProdProcessUpdateDTO;

/**
 * 档口商品工艺信息Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IStoreProductProcessService {

    /**
     * 获取档口商品工艺信息
     *
     * @param storeProdId 档口商品ID
     * @return 档口商品工艺信息
     */
    StoreProdProcessDTO getProcess(Long storeProdId);

    /**
     * 更新档口工艺 信息
     *
     * @param storeProdId 档口商品ID
     * @param updateDTO   更新入参
     * @return Integer
     */
    Integer update(Long storeProdId, StoreProdProcessUpdateDTO updateDTO);

}
