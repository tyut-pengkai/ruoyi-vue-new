package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.StoreCustomer;

import java.util.List;

/**
 * 档口客户Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface StoreCustomerMapper extends BaseMapper<StoreCustomer> {
    /**
     * 查询档口客户
     *
     * @param id 档口客户主键
     * @return 档口客户
     */
    public StoreCustomer selectStoreCustomerByStoreCusId(Long id);

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
     * 删除档口客户
     *
     * @param id 档口客户主键
     * @return 结果
     */
    public int deleteStoreCustomerByStoreCusId(Long id);

    /**
     * 批量删除档口客户
     *
     * @param storeCusIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStoreCustomerByStoreCusIds(Long[] storeCusIds);
}
