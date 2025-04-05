package com.ruoyi.xkt.service.impl;

import com.ruoyi.xkt.dto.storeProdColor.StoreProdColorResDTO;
import com.ruoyi.xkt.mapper.StoreProductColorMapper;
import com.ruoyi.xkt.service.IStoreProductColorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 档口当前商品颜色Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
@RequiredArgsConstructor
public class StoreProductColorServiceImpl implements IStoreProductColorService {

    final StoreProductColorMapper storeProdColorMapper;

    /**
     * 根据商店ID和产品款式编号模糊查询颜色列表
     *
     * @param storeId    商店ID，用于限定查询范围
     * @param prodArtNum 产品款式编号，用于模糊匹配产品
     * @return 返回一个列表，包含匹配的产品颜色信息
     */
    @Override
    @Transactional(readOnly = true)
    public List<StoreProdColorResDTO> fuzzyQueryColorList(Long storeId, String prodArtNum) {
        return storeProdColorMapper.fuzzyQueryColorList(storeId, prodArtNum);
    }
}
