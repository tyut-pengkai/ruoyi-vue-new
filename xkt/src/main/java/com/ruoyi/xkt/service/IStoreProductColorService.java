package com.ruoyi.xkt.service;

import com.ruoyi.xkt.domain.StoreProductColor;
import com.ruoyi.xkt.dto.storeProdColor.StoreProdColorResDTO;

import java.util.List;

/**
 * 档口当前商品颜色Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IStoreProductColorService {
    /**
     * 根据商店ID和产品款式编号模糊查询颜色列表
     *
     * @param storeId 商店ID，用于限定查询范围
     * @param prodArtNum 产品款式编号，用于模糊匹配产品
     * @return 返回一个列表，包含匹配的产品颜色信息
     */
    List<StoreProdColorResDTO> fuzzyQueryColorList(Long storeId, String prodArtNum);
}
