package com.ruoyi.xkt.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.xkt.domain.StoreSaleRefundRecord;
import com.ruoyi.xkt.mapper.StoreSaleRefundRecordMapper;
import com.ruoyi.xkt.service.IStoreSaleRefundRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 档口销售返单Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
public class StoreSaleRefundRecordServiceImpl implements IStoreSaleRefundRecordService {
    @Autowired
    private StoreSaleRefundRecordMapper storeSaleRefundRecordMapper;

    /**
     * 查询档口销售返单
     *
     * @param storeSaleRefundRecordId 档口销售返单主键
     * @return 档口销售返单
     */
    @Override
    public StoreSaleRefundRecord selectStoreSaleRefundRecordByStoreSaleRefundRecordId(Long storeSaleRefundRecordId) {
        return storeSaleRefundRecordMapper.selectStoreSaleRefundRecordByStoreSaleRefundRecordId(storeSaleRefundRecordId);
    }

    /**
     * 查询档口销售返单列表
     *
     * @param storeSaleRefundRecord 档口销售返单
     * @return 档口销售返单
     */
    @Override
    public List<StoreSaleRefundRecord> selectStoreSaleRefundRecordList(StoreSaleRefundRecord storeSaleRefundRecord) {
        return storeSaleRefundRecordMapper.selectStoreSaleRefundRecordList(storeSaleRefundRecord);
    }

    /**
     * 新增档口销售返单
     *
     * @param storeSaleRefundRecord 档口销售返单
     * @return 结果
     */
    @Override
    @Transactional
    public int insertStoreSaleRefundRecord(StoreSaleRefundRecord storeSaleRefundRecord) {
        storeSaleRefundRecord.setCreateTime(DateUtils.getNowDate());
        return storeSaleRefundRecordMapper.insertStoreSaleRefundRecord(storeSaleRefundRecord);
    }

    /**
     * 修改档口销售返单
     *
     * @param storeSaleRefundRecord 档口销售返单
     * @return 结果
     */
    @Override
    @Transactional
    public int updateStoreSaleRefundRecord(StoreSaleRefundRecord storeSaleRefundRecord) {
        storeSaleRefundRecord.setUpdateTime(DateUtils.getNowDate());
        return storeSaleRefundRecordMapper.updateStoreSaleRefundRecord(storeSaleRefundRecord);
    }

    /**
     * 批量删除档口销售返单
     *
     * @param storeSaleRefundRecordIds 需要删除的档口销售返单主键
     * @return 结果
     */
    @Override
    public int deleteStoreSaleRefundRecordByStoreSaleRefundRecordIds(Long[] storeSaleRefundRecordIds) {
        return storeSaleRefundRecordMapper.deleteStoreSaleRefundRecordByStoreSaleRefundRecordIds(storeSaleRefundRecordIds);
    }

    /**
     * 删除档口销售返单信息
     *
     * @param storeSaleRefundRecordId 档口销售返单主键
     * @return 结果
     */
    @Override
    public int deleteStoreSaleRefundRecordByStoreSaleRefundRecordId(Long storeSaleRefundRecordId) {
        return storeSaleRefundRecordMapper.deleteStoreSaleRefundRecordByStoreSaleRefundRecordId(storeSaleRefundRecordId);
    }
}
