package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.StoreProductDetail;

/**
 * 档口商品详情内容Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface StoreProductDetailMapper extends BaseMapper<StoreProductDetail> {

    void updateDelFlagByStoreProdId(Long storeProdId);

    StoreProductDetail selectByStoreProdId(Long storeProdId);

}
