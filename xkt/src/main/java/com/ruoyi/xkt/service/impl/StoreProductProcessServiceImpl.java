package com.ruoyi.xkt.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.xkt.domain.StoreProductProcess;
import com.ruoyi.xkt.mapper.StoreProductProcessMapper;
import com.ruoyi.xkt.service.IStoreProductProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 档口商品工艺信息Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
public class StoreProductProcessServiceImpl implements IStoreProductProcessService {
    @Autowired
    private StoreProductProcessMapper storeProductProcessMapper;

    /**
     * 查询档口商品工艺信息
     *
     * @param storeProdProcessId 档口商品工艺信息主键
     * @return 档口商品工艺信息
     */
    @Override
    @Transactional(readOnly = true)
    public StoreProductProcess selectStoreProductProcessByStoreProdProcessId(Long storeProdProcessId) {
        return storeProductProcessMapper.selectStoreProductProcessByStoreProdProcessId(storeProdProcessId);
    }

    /**
     * 查询档口商品工艺信息列表
     *
     * @param storeProductProcess 档口商品工艺信息
     * @return 档口商品工艺信息
     */
    @Override
    @Transactional(readOnly = true)
    public List<StoreProductProcess> selectStoreProductProcessList(StoreProductProcess storeProductProcess) {
        return storeProductProcessMapper.selectStoreProductProcessList(storeProductProcess);
    }

    /**
     * 新增档口商品工艺信息
     *
     * @param storeProductProcess 档口商品工艺信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertStoreProductProcess(StoreProductProcess storeProductProcess) {
        storeProductProcess.setCreateTime(DateUtils.getNowDate());
        return storeProductProcessMapper.insertStoreProductProcess(storeProductProcess);
    }

    /**
     * 修改档口商品工艺信息
     *
     * @param storeProductProcess 档口商品工艺信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateStoreProductProcess(StoreProductProcess storeProductProcess) {
        storeProductProcess.setUpdateTime(DateUtils.getNowDate());
        return storeProductProcessMapper.updateStoreProductProcess(storeProductProcess);
    }

    /**
     * 批量删除档口商品工艺信息
     *
     * @param storeProdProcessIds 需要删除的档口商品工艺信息主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteStoreProductProcessByStoreProdProcessIds(Long[] storeProdProcessIds) {
        return storeProductProcessMapper.deleteStoreProductProcessByStoreProdProcessIds(storeProdProcessIds);
    }

    /**
     * 删除档口商品工艺信息信息
     *
     * @param storeProdProcessId 档口商品工艺信息主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteStoreProductProcessByStoreProdProcessId(Long storeProdProcessId) {
        return storeProductProcessMapper.deleteStoreProductProcessByStoreProdProcessId(storeProdProcessId);
    }
}
