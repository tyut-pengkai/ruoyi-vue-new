package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.StoreProductProcess;

/**
 * 档口商品工艺信息Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface StoreProductProcessMapper extends BaseMapper<StoreProductProcess> {

    /**
     * 将商品工艺信息置为无效
     *
     * @param storeProdId 档口商品ID
     */
    void updateDelFlagByStoreProdId(Long storeProdId);

    /**
     * 查询档口商品工艺信息
     *
     * @param storeProdId 档口商品ID
     * @return StoreProductProcess
     */
    StoreProductProcess selectByStoreProdId(Long storeProdId);

}
