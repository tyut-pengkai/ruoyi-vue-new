package com.ruoyi.xkt.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.xkt.domain.StoreProductStorageDemandDeducte;
import com.ruoyi.xkt.mapper.StoreProductStorageDemandDeducteMapper;
import com.ruoyi.xkt.service.IStoreProductStorageDemandDeducteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private StoreProductStorageDemandDeducteMapper storeProductStorageDemandDeducteMapper;

    /**
     * 查询档口商品入库抵扣需求
     *
     * @param storeProdStorDemaDeducteId 档口商品入库抵扣需求主键
     * @return 档口商品入库抵扣需求
     */
    @Override
    public StoreProductStorageDemandDeducte selectStoreProductStorageDemandDeducteByStoreProdStorDemaDeducteId(Long storeProdStorDemaDeducteId) {
        return storeProductStorageDemandDeducteMapper.selectStoreProductStorageDemandDeducteByStoreProdStorDemaDeducteId(storeProdStorDemaDeducteId);
    }

    /**
     * 查询档口商品入库抵扣需求列表
     *
     * @param storeProductStorageDemandDeducte 档口商品入库抵扣需求
     * @return 档口商品入库抵扣需求
     */
    @Override
    public List<StoreProductStorageDemandDeducte> selectStoreProductStorageDemandDeducteList(StoreProductStorageDemandDeducte storeProductStorageDemandDeducte) {
        return storeProductStorageDemandDeducteMapper.selectStoreProductStorageDemandDeducteList(storeProductStorageDemandDeducte);
    }

    /**
     * 新增档口商品入库抵扣需求
     *
     * @param storeProductStorageDemandDeducte 档口商品入库抵扣需求
     * @return 结果
     */
    @Override
    public int insertStoreProductStorageDemandDeducte(StoreProductStorageDemandDeducte storeProductStorageDemandDeducte) {
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
    public int updateStoreProductStorageDemandDeducte(StoreProductStorageDemandDeducte storeProductStorageDemandDeducte) {
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
    public int deleteStoreProductStorageDemandDeducteByStoreProdStorDemaDeducteId(Long storeProdStorDemaDeducteId) {
        return storeProductStorageDemandDeducteMapper.deleteStoreProductStorageDemandDeducteByStoreProdStorDemaDeducteId(storeProdStorDemaDeducteId);
    }
}
