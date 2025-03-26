package com.ruoyi.xkt.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.xkt.domain.StoreCustomer;
import com.ruoyi.xkt.mapper.StoreCustomerMapper;
import com.ruoyi.xkt.service.IStoreCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 档口客户Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
public class StoreCustomerServiceImpl implements IStoreCustomerService {
    @Autowired
    private StoreCustomerMapper storeCustomerMapper;

    /**
     * 查询档口客户
     *
     * @param storeCusId 档口客户主键
     * @return 档口客户
     */
    @Override
    public StoreCustomer selectStoreCustomerByStoreCusId(Long storeCusId) {
        return storeCustomerMapper.selectStoreCustomerByStoreCusId(storeCusId);
    }

    /**
     * 查询档口客户列表
     *
     * @param storeCustomer 档口客户
     * @return 档口客户
     */
    @Override
    public List<StoreCustomer> selectStoreCustomerList(StoreCustomer storeCustomer) {
        return storeCustomerMapper.selectStoreCustomerList(storeCustomer);
    }

    /**
     * 新增档口客户
     *
     * @param storeCustomer 档口客户
     * @return 结果
     */
    @Override
    public int insertStoreCustomer(StoreCustomer storeCustomer) {
        storeCustomer.setCreateTime(DateUtils.getNowDate());
        return storeCustomerMapper.insertStoreCustomer(storeCustomer);
    }

    /**
     * 修改档口客户
     *
     * @param storeCustomer 档口客户
     * @return 结果
     */
    @Override
    public int updateStoreCustomer(StoreCustomer storeCustomer) {
        storeCustomer.setUpdateTime(DateUtils.getNowDate());
        return storeCustomerMapper.updateStoreCustomer(storeCustomer);
    }

    /**
     * 批量删除档口客户
     *
     * @param storeCusIds 需要删除的档口客户主键
     * @return 结果
     */
    @Override
    public int deleteStoreCustomerByStoreCusIds(Long[] storeCusIds) {
        return storeCustomerMapper.deleteStoreCustomerByStoreCusIds(storeCusIds);
    }

    /**
     * 删除档口客户信息
     *
     * @param storeCusId 档口客户主键
     * @return 结果
     */
    @Override
    public int deleteStoreCustomerByStoreCusId(Long storeCusId) {
        return storeCustomerMapper.deleteStoreCustomerByStoreCusId(storeCusId);
    }
}
