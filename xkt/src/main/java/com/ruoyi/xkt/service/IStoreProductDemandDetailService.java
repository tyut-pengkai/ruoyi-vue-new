package com.ruoyi.xkt.service;

import com.ruoyi.xkt.dto.storeProductDemandDetail.StoreProdDemandDetailUpdateDTO;

/**
 * 档口商品需求单明细Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IStoreProductDemandDetailService {

    /**
     * 更新商品需求单明细数量
     *
     * @param updateDTO 更新入参
     * @return 更新数量
     */
    Integer updateDetailQuantity(StoreProdDemandDetailUpdateDTO updateDTO);

}
