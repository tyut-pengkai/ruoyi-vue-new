package com.ruoyi.xkt.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.xkt.domain.StoreProductDetail;
import com.ruoyi.xkt.mapper.StoreProductDetailMapper;
import com.ruoyi.xkt.service.IStoreProductDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 档口商品详情内容Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
public class StoreProductDetailServiceImpl implements IStoreProductDetailService {
    @Autowired
    private StoreProductDetailMapper storeProductDetailMapper;

    /**
     * 查询档口商品详情内容
     *
     * @param storeProdDetailId 档口商品详情内容主键
     * @return 档口商品详情内容
     */
    @Override
    public StoreProductDetail selectStoreProductDetailByStoreProdDetailId(Long storeProdDetailId) {
        return storeProductDetailMapper.selectStoreProductDetailByStoreProdDetailId(storeProdDetailId);
    }

    /**
     * 查询档口商品详情内容列表
     *
     * @param storeProductDetail 档口商品详情内容
     * @return 档口商品详情内容
     */
    @Override
    public List<StoreProductDetail> selectStoreProductDetailList(StoreProductDetail storeProductDetail) {
        return storeProductDetailMapper.selectStoreProductDetailList(storeProductDetail);
    }

    /**
     * 新增档口商品详情内容
     *
     * @param storeProductDetail 档口商品详情内容
     * @return 结果
     */
    @Override
    public int insertStoreProductDetail(StoreProductDetail storeProductDetail) {
        storeProductDetail.setCreateTime(DateUtils.getNowDate());
        return storeProductDetailMapper.insertStoreProductDetail(storeProductDetail);
    }

    /**
     * 修改档口商品详情内容
     *
     * @param storeProductDetail 档口商品详情内容
     * @return 结果
     */
    @Override
    public int updateStoreProductDetail(StoreProductDetail storeProductDetail) {
        storeProductDetail.setUpdateTime(DateUtils.getNowDate());
        return storeProductDetailMapper.updateStoreProductDetail(storeProductDetail);
    }

    /**
     * 批量删除档口商品详情内容
     *
     * @param storeProdDetailIds 需要删除的档口商品详情内容主键
     * @return 结果
     */
    @Override
    public int deleteStoreProductDetailByStoreProdDetailIds(Long[] storeProdDetailIds) {
        return storeProductDetailMapper.deleteStoreProductDetailByStoreProdDetailIds(storeProdDetailIds);
    }

    /**
     * 删除档口商品详情内容信息
     *
     * @param storeProdDetailId 档口商品详情内容主键
     * @return 结果
     */
    @Override
    public int deleteStoreProductDetailByStoreProdDetailId(Long storeProdDetailId) {
        return storeProductDetailMapper.deleteStoreProductDetailByStoreProdDetailId(storeProdDetailId);
    }
}
