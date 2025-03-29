package com.ruoyi.xkt.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.xkt.domain.StoreProductStorageDetail;
import com.ruoyi.xkt.mapper.StoreProductStorageDetailMapper;
import com.ruoyi.xkt.service.IStoreProductStorageDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 档口商品入库明细Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
public class StoreProductStorageDetailServiceImpl implements IStoreProductStorageDetailService {
    @Autowired
    private StoreProductStorageDetailMapper storeProductStorageDetailMapper;

    /**
     * 查询档口商品入库明细
     *
     * @param storeProdStorDetailId 档口商品入库明细主键
     * @return 档口商品入库明细
     */
    @Override
    @Transactional(readOnly = true)
    public StoreProductStorageDetail selectStoreProductStorageDetailByStoreProdStorDetailId(Long storeProdStorDetailId) {
        return storeProductStorageDetailMapper.selectStoreProductStorageDetailByStoreProdStorDetailId(storeProdStorDetailId);
    }

    /**
     * 查询档口商品入库明细列表
     *
     * @param storeProductStorageDetail 档口商品入库明细
     * @return 档口商品入库明细
     */
    @Override
    @Transactional(readOnly = true)
    public List<StoreProductStorageDetail> selectStoreProductStorageDetailList(StoreProductStorageDetail storeProductStorageDetail) {
        return storeProductStorageDetailMapper.selectStoreProductStorageDetailList(storeProductStorageDetail);
    }

    /**
     * 新增档口商品入库明细
     *
     * @param storeProductStorageDetail 档口商品入库明细
     * @return 结果
     */
    @Override
    @Transactional
    public int insertStoreProductStorageDetail(StoreProductStorageDetail storeProductStorageDetail) {
        storeProductStorageDetail.setCreateTime(DateUtils.getNowDate());
        return storeProductStorageDetailMapper.insertStoreProductStorageDetail(storeProductStorageDetail);
    }

    /**
     * 修改档口商品入库明细
     *
     * @param storeProductStorageDetail 档口商品入库明细
     * @return 结果
     */
    @Override
    @Transactional
    public int updateStoreProductStorageDetail(StoreProductStorageDetail storeProductStorageDetail) {
        storeProductStorageDetail.setUpdateTime(DateUtils.getNowDate());
        return storeProductStorageDetailMapper.updateStoreProductStorageDetail(storeProductStorageDetail);
    }

    /**
     * 批量删除档口商品入库明细
     *
     * @param storeProdStorDetailIds 需要删除的档口商品入库明细主键
     * @return 结果
     */
    @Override
    public int deleteStoreProductStorageDetailByStoreProdStorDetailIds(Long[] storeProdStorDetailIds) {
        return storeProductStorageDetailMapper.deleteStoreProductStorageDetailByStoreProdStorDetailIds(storeProdStorDetailIds);
    }

    /**
     * 删除档口商品入库明细信息
     *
     * @param storeProdStorDetailId 档口商品入库明细主键
     * @return 结果
     */
    @Override
    public int deleteStoreProductStorageDetailByStoreProdStorDetailId(Long storeProdStorDetailId) {
        return storeProductStorageDetailMapper.deleteStoreProductStorageDetailByStoreProdStorDetailId(storeProdStorDetailId);
    }
}
