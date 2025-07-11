package com.ruoyi.xkt.service;

import com.ruoyi.xkt.dto.storeProdColorSize.*;

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
     * @param snDTO 查询入参
     * @return StoreProdColorSizeBarcodeResDTO
     */
    StoreSaleSnResDTO storeSaleSn(StoreSaleSnDTO snDTO);

    /**
     * 商品入库查询库存
     *
     * @param snDTO 条码入参
     * @return StoreProdSnsResDTO
     */
    StoreStorageSnResDTO storageSnList(StoreProdSnDTO snDTO);

    /**
     * 库存盘点查询库存
     *
     * @param snDTO 条码入参
     * @return StoreStorageSnResDTO
     */
    StoreStockTakingSnResDTO stockTakingSnList(StoreStockTakingSnDTO snDTO);
}
