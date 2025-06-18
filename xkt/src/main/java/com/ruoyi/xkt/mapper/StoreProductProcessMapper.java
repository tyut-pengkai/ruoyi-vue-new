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

    void updateDelFlagByStoreProdId(Long storeProdId);

    StoreProductProcess selectByStoreProdId(Long storeProdId);
}
