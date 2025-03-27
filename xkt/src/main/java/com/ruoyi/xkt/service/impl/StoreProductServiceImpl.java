package com.ruoyi.xkt.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.xkt.domain.StoreProduct;
import com.ruoyi.xkt.dto.storeProduct.StoreProdDTO;
import com.ruoyi.xkt.mapper.StoreProductMapper;
import com.ruoyi.xkt.service.IStoreProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 档口商品Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
public class StoreProductServiceImpl implements IStoreProductService {

    @Autowired
    private StoreProductMapper storeProductMapper;

    /**
     * 查询档口商品
     *
     * @param storeProdId 档口商品主键
     * @return 档口商品
     */
    @Override
    public StoreProduct selectStoreProductByStoreProdId(Long storeProdId) {
        return storeProductMapper.selectStoreProductByStoreProdId(storeProdId);
    }

    /**
     * 查询档口商品列表
     *
     * @param storeProduct 档口商品
     * @return 档口商品
     */
    @Override
    public List<StoreProduct> selectStoreProductList(StoreProduct storeProduct) {
        return storeProductMapper.selectStoreProductList(storeProduct);
    }


    @Override
    public int insertStoreProduct(StoreProdDTO storeProdDTO) {
//        storeProduct.setCreateTime(DateUtils.getNowDate());
//        return storeProductMapper.insertStoreProduct(storeProduct);
        return 0;
    }

    /**
     * 修改档口商品
     *
     * @param storeProduct 档口商品
     * @return 结果
     */
    @Override
    public int updateStoreProduct(StoreProduct storeProduct) {
        storeProduct.setUpdateTime(DateUtils.getNowDate());
        return storeProductMapper.updateStoreProduct(storeProduct);
    }

    /**
     * 批量删除档口商品
     *
     * @param storeProdIds 需要删除的档口商品主键
     * @return 结果
     */
    @Override
    public int deleteStoreProductByStoreProdIds(Long[] storeProdIds) {
        return storeProductMapper.deleteStoreProductByStoreProdIds(storeProdIds);
    }

    /**
     * 删除档口商品信息
     *
     * @param storeProdId 档口商品主键
     * @return 结果
     */
    @Override
    public int deleteStoreProductByStoreProdId(Long storeProdId) {
        return storeProductMapper.deleteStoreProductByStoreProdId(storeProdId);
    }
}
