package com.ruoyi.xkt.service.impl;

import com.ruoyi.xkt.dto.storeProdColorPrice.StoreProdColorPriceResDTO;
import com.ruoyi.xkt.mapper.StoreProductColorPriceMapper;
import com.ruoyi.xkt.service.IStoreProductColorPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 档口商品颜色定价Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
@RequiredArgsConstructor
public class StoreProductColorPriceServiceImpl implements IStoreProductColorPriceService {

    final StoreProductColorPriceMapper prodColorPriceMapper;

    /**
     * 根据档口ID 和 商品ID 获取所有颜色及价格
     *
     * @param storeId     档口ID
     * @param storeProdId 档口商品ID
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<StoreProdColorPriceResDTO> getColorPriceByStoreProdId(Long storeId, Long storeProdId) {
        return this.prodColorPriceMapper.selectListByStoreProdIdAndStoreId(storeProdId, storeId);
    }

}
