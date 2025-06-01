package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.StoreProductService;
import com.ruoyi.xkt.dto.storeProdSvc.StoreProdSvcDTO;
import org.apache.ibatis.annotations.Param;

/**
 * 档口商品服务Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface StoreProductServiceMapper extends BaseMapper<StoreProductService> {

    void updateDelFlagByStoreProdId(Long storeProdId);

    StoreProductService selectByStoreProdId(Long storeProdId);

    /**
     * 根据商品ID查询档口商品服务
     *
     * @param storeProdId 商品ID
     * @return 档口商品服务
     */
    StoreProdSvcDTO selectSvc(@Param("storeProdId") Long storeProdId);

}
