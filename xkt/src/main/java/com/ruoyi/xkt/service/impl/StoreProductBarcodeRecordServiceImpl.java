package com.ruoyi.xkt.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.xkt.domain.StoreProductBarcodeRecord;
import com.ruoyi.xkt.mapper.StoreProductBarcodeRecordMapper;
import com.ruoyi.xkt.service.IStoreProductBarcodeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 档口打印条形码记录Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
public class StoreProductBarcodeRecordServiceImpl implements IStoreProductBarcodeRecordService {
    @Autowired
    private StoreProductBarcodeRecordMapper storeProductBarcodeRecordMapper;

    /**
     * 查询档口打印条形码记录
     *
     * @param storeProdBarcodeRecordId 档口打印条形码记录主键
     * @return 档口打印条形码记录
     */
    @Override
    public StoreProductBarcodeRecord selectStoreProductBarcodeRecordByStoreProdBarcodeRecordId(Long storeProdBarcodeRecordId) {
        return storeProductBarcodeRecordMapper.selectStoreProductBarcodeRecordByStoreProdBarcodeRecordId(storeProdBarcodeRecordId);
    }

    /**
     * 查询档口打印条形码记录列表
     *
     * @param storeProductBarcodeRecord 档口打印条形码记录
     * @return 档口打印条形码记录
     */
    @Override
    public List<StoreProductBarcodeRecord> selectStoreProductBarcodeRecordList(StoreProductBarcodeRecord storeProductBarcodeRecord) {
        return storeProductBarcodeRecordMapper.selectStoreProductBarcodeRecordList(storeProductBarcodeRecord);
    }

    /**
     * 新增档口打印条形码记录
     *
     * @param storeProductBarcodeRecord 档口打印条形码记录
     * @return 结果
     */
    @Override
    @Transactional
    public int insertStoreProductBarcodeRecord(StoreProductBarcodeRecord storeProductBarcodeRecord) {
        storeProductBarcodeRecord.setCreateTime(DateUtils.getNowDate());
        return storeProductBarcodeRecordMapper.insertStoreProductBarcodeRecord(storeProductBarcodeRecord);
    }

    /**
     * 修改档口打印条形码记录
     *
     * @param storeProductBarcodeRecord 档口打印条形码记录
     * @return 结果
     */
    @Override
    public int updateStoreProductBarcodeRecord(StoreProductBarcodeRecord storeProductBarcodeRecord) {
        storeProductBarcodeRecord.setUpdateTime(DateUtils.getNowDate());
        return storeProductBarcodeRecordMapper.updateStoreProductBarcodeRecord(storeProductBarcodeRecord);
    }

    /**
     * 批量删除档口打印条形码记录
     *
     * @param storeProdBarcodeRecordIds 需要删除的档口打印条形码记录主键
     * @return 结果
     */
    @Override
    public int deleteStoreProductBarcodeRecordByStoreProdBarcodeRecordIds(Long[] storeProdBarcodeRecordIds) {
        return storeProductBarcodeRecordMapper.deleteStoreProductBarcodeRecordByStoreProdBarcodeRecordIds(storeProdBarcodeRecordIds);
    }

    /**
     * 删除档口打印条形码记录信息
     *
     * @param storeProdBarcodeRecordId 档口打印条形码记录主键
     * @return 结果
     */
    @Override
    public int deleteStoreProductBarcodeRecordByStoreProdBarcodeRecordId(Long storeProdBarcodeRecordId) {
        return storeProductBarcodeRecordMapper.deleteStoreProductBarcodeRecordByStoreProdBarcodeRecordId(storeProdBarcodeRecordId);
    }
}
