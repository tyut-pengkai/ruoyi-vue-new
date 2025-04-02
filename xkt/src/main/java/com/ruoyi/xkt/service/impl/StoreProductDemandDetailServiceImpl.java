package com.ruoyi.xkt.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.xkt.domain.StoreProductDemandDetail;
import com.ruoyi.xkt.mapper.StoreProductDemandDetailMapper;
import com.ruoyi.xkt.service.IStoreProductDemandDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 档口商品需求单明细Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
public class StoreProductDemandDetailServiceImpl implements IStoreProductDemandDetailService {
    @Autowired
    private StoreProductDemandDetailMapper storeProductDemandDetailMapper;

    /**
     * 查询档口商品需求单明细
     *
     * @param storeProdDemaDetailId 档口商品需求单明细主键
     * @return 档口商品需求单明细
     */
    @Override
    @Transactional(readOnly = true)
    public StoreProductDemandDetail selectStoreProductDemandDetailByStoreProdDemaDetailId(Long storeProdDemaDetailId) {
        return storeProductDemandDetailMapper.selectStoreProductDemandDetailByStoreProdDemaDetailId(storeProdDemaDetailId);
    }

    /**
     * 查询档口商品需求单明细列表
     *
     * @param storeProductDemandDetail 档口商品需求单明细
     * @return 档口商品需求单明细
     */
    @Override
    @Transactional(readOnly = true)
    public List<StoreProductDemandDetail> selectStoreProductDemandDetailList(StoreProductDemandDetail storeProductDemandDetail) {
        return storeProductDemandDetailMapper.selectStoreProductDemandDetailList(storeProductDemandDetail);
    }

    /**
     * 新增档口商品需求单明细
     *
     * @param storeProductDemandDetail 档口商品需求单明细
     * @return 结果
     */
    @Override
    @Transactional
    public int insertStoreProductDemandDetail(StoreProductDemandDetail storeProductDemandDetail) {
        storeProductDemandDetail.setCreateTime(DateUtils.getNowDate());
        return storeProductDemandDetailMapper.insertStoreProductDemandDetail(storeProductDemandDetail);
    }

    /**
     * 修改档口商品需求单明细
     *
     * @param storeProductDemandDetail 档口商品需求单明细
     * @return 结果
     */
    @Override
    public int updateStoreProductDemandDetail(StoreProductDemandDetail storeProductDemandDetail) {
        storeProductDemandDetail.setUpdateTime(DateUtils.getNowDate());
        return storeProductDemandDetailMapper.updateStoreProductDemandDetail(storeProductDemandDetail);
    }

    /**
     * 批量删除档口商品需求单明细
     *
     * @param storeProdDemaDetailIds 需要删除的档口商品需求单明细主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteStoreProductDemandDetailByStoreProdDemaDetailIds(Long[] storeProdDemaDetailIds) {
        return storeProductDemandDetailMapper.deleteStoreProductDemandDetailByStoreProdDemaDetailIds(storeProdDemaDetailIds);
    }

    /**
     * 删除档口商品需求单明细信息
     *
     * @param storeProdDemaDetailId 档口商品需求单明细主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteStoreProductDemandDetailByStoreProdDemaDetailId(Long storeProdDemaDetailId) {
        return storeProductDemandDetailMapper.deleteStoreProductDemandDetailByStoreProdDemaDetailId(storeProdDemaDetailId);
    }
}
