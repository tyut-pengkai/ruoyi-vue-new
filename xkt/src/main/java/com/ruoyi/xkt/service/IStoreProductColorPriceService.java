package com.ruoyi.xkt.service;

import com.ruoyi.common.core.page.Page;
import com.ruoyi.xkt.dto.storeProdColorPrice.StoreProdColorPricePageDTO;
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

    /**
     * 查询档口商品颜色价格分页
     *
     * @param pageDTO 入参
     * @return Page<StoreProdColorPriceResDTO>
     */
    Page<StoreProdColorPriceResDTO> page(StoreProdColorPricePageDTO pageDTO);

}
