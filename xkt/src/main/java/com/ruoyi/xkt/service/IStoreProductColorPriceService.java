package com.ruoyi.xkt.service;

import com.ruoyi.xkt.dto.storeProdColorPrice.StoreProdColorPriceResDTO;

import java.util.List;

/**
 * 档口商品颜色定价Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IStoreProductColorPriceService {

    List<StoreProdColorPriceResDTO> getColorPriceByStoreProdId(Long storeId, Long storeProdId);
}
