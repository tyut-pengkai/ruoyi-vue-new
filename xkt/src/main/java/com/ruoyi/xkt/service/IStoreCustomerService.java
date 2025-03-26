package com.ruoyi.xkt.service;

import com.ruoyi.xkt.domain.StoreCustomer;

import java.util.List;

/**
 * 档口客户Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IStoreCustomerService {
    /**
     * 查询档口客户
     *
     * @param storeCusId 档口客户主键
     * @return 档口客户
     */
    public StoreCustomer selectStoreCustomerByStoreCusId(Long storeCusId);

    /**
     * 查询档口客户列表
     *
     * @param storeCustomer 档口客户
     * @return 档口客户集合
     */
    public List<StoreCustomer> selectStoreCustomerList(StoreCustomer storeCustomer);

    /**
     * 新增档口客户
     *
     * @param storeCustomer 档口客户
     * @return 结果
     */
    public int insertStoreCustomer(StoreCustomer storeCustomer);

    /**
     * 修改档口客户
     *
     * @param storeCustomer 档口客户
     * @return 结果
     */
    public int updateStoreCustomer(StoreCustomer storeCustomer);

    /**
     * 批量删除档口客户
     *
     * @param storeCusIds 需要删除的档口客户主键集合
     * @return 结果
     */
    public int deleteStoreCustomerByStoreCusIds(Long[] storeCusIds);

    /**
     * 删除档口客户信息
     *
     * @param storeCusId 档口客户主键
     * @return 结果
     */
    public int deleteStoreCustomerByStoreCusId(Long storeCusId);
}
