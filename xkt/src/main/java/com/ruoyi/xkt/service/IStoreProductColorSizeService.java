package com.ruoyi.xkt.service;

import com.ruoyi.xkt.dto.storeProdColorSize.StoreProdSnDTO;
import com.ruoyi.xkt.dto.storeProdColorSize.StoreProdSnResDTO;
import com.ruoyi.xkt.dto.storeProdColorSize.StoreSaleSnDTO;
import com.ruoyi.xkt.dto.storeProdColorSize.StoreSaleSnResDTO;

/**
 * 档口商品颜色的尺码Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IStoreProductColorSizeService {

    /**
     * 查询条码 对应的商品信息
     *
     * @param barcodeDTO 查询入参
     * @return StoreProdColorSizeBarcodeResDTO
     */
    StoreSaleSnResDTO storeSaleSn(StoreSaleSnDTO barcodeDTO);

    /**
     * 商品入库、库存盘点查询库存
     *
     * @param snsDTO 条码入参
     * @return StoreProdSnsResDTO
     */
    StoreProdSnResDTO sn(StoreProdSnDTO snsDTO);
}
