package com.ruoyi.xkt.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.xkt.domain.StoreProductStorageDemandDeduct;
import com.ruoyi.xkt.mapper.StoreProductStorageDemandDeductMapper;
import com.ruoyi.xkt.service.IStoreProductStorageDemandDeducteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 档口商品入库抵扣需求Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
public class StoreProductStorageDemandDeducteServiceImpl implements IStoreProductStorageDemandDeducteService {
    @Autowired
    private StoreProductStorageDemandDeductMapper storeProductStorageDemandDeducteMapper;

    /**
     * 查询档口商品入库抵扣需求
     *
     * @param storeProdStorDemaDeducteId 档口商品入库抵扣需求主键
     * @return 档口商品入库抵扣需求
     */
    @Override
    @Transactional(readOnly = true)
    public StoreProductStorageDemandDeduct selectStoreProductStorageDemandDeducteByStoreProdStorDemaDeducteId(Long storeProdStorDemaDeducteId) {
        return storeProductStorageDemandDeducteMapper.selectStoreProductStorageDemandDeducteByStoreProdStorDemaDeducteId(storeProdStorDemaDeducteId);
    }

    /**
     * 查询档口商品入库抵扣需求列表
     *
     * @param storeProductStorageDemandDeducte 档口商品入库抵扣需求
     * @return 档口商品入库抵扣需求
     */
    @Override
    @Transactional(readOnly = true)
    public List<StoreProductStorageDemandDeduct> selectStoreProductStorageDemandDeducteList(StoreProductStorageDemandDeduct storeProductStorageDemandDeducte) {
        return storeProductStorageDemandDeducteMapper.selectStoreProductStorageDemandDeducteList(storeProductStorageDemandDeducte);
    }

    /**
     * 新增档口商品入库抵扣需求
     *
     * @param storeProductStorageDemandDeducte 档口商品入库抵扣需求
     * @return 结果
     */
    @Override
    @Transactional
    public int insertStoreProductStorageDemandDeducte(StoreProductStorageDemandDeduct storeProductStorageDemandDeducte) {
        storeProductStorageDemandDeducte.setCreateTime(DateUtils.getNowDate());
        return storeProductStorageDemandDeducteMapper.insertStoreProductStorageDemandDeducte(storeProductStorageDemandDeducte);
    }

    /**
     * 修改档口商品入库抵扣需求
     *
     * @param storeProductStorageDemandDeducte 档口商品入库抵扣需求
     * @return 结果
     */
    @Override
    @Transactional
    public int updateStoreProductStorageDemandDeducte(StoreProductStorageDemandDeduct storeProductStorageDemandDeducte) {
        storeProductStorageDemandDeducte.setUpdateTime(DateUtils.getNowDate());
        return storeProductStorageDemandDeducteMapper.updateStoreProductStorageDemandDeducte(storeProductStorageDemandDeducte);
    }

    /**
     * 批量删除档口商品入库抵扣需求
     *
     * @param storeProdStorDemaDeducteIds 需要删除的档口商品入库抵扣需求主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteStoreProductStorageDemandDeducteByStoreProdStorDemaDeducteIds(Long[] storeProdStorDemaDeducteIds) {
        return storeProductStorageDemandDeducteMapper.deleteStoreProductStorageDemandDeducteByStoreProdStorDemaDeducteIds(storeProdStorDemaDeducteIds);
    }

    /**
     * 删除档口商品入库抵扣需求信息
     *
     * @param storeProdStorDemaDeducteId 档口商品入库抵扣需求主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteStoreProductStorageDemandDeducteByStoreProdStorDemaDeducteId(Long storeProdStorDemaDeducteId) {
        return storeProductStorageDemandDeducteMapper.deleteStoreProductStorageDemandDeducteByStoreProdStorDemaDeducteId(storeProdStorDemaDeducteId);
    }
}
