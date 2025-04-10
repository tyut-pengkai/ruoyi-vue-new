package com.ruoyi.xkt.service;

import com.ruoyi.common.core.page.Page;
import com.ruoyi.xkt.domain.StoreCustomer;
import com.ruoyi.xkt.dto.storeCustomer.StoreCusDTO;
import com.ruoyi.xkt.dto.storeCustomer.StoreCusFuzzyResDTO;
import com.ruoyi.xkt.dto.storeCustomer.StoreCusPageDTO;
import com.ruoyi.xkt.dto.storeCustomer.StoreCusPageResDTO;

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
    public StoreCusDTO selectByStoreCusId(Long storeCusId);

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
     * @param storeCusDTO 档口客户
     * @return 结果
     */
    public int updateStoreCus(StoreCusDTO storeCusDTO);

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

    int create(StoreCusDTO storeCusDTO);

    void deleteStoreCus(Long storeCusId);

    Page<StoreCusPageResDTO> selectPage(StoreCusPageDTO storeCusPageDTO);

    /**
     * 模糊查询客户名称列表
     *
     * @param storeId 档口ID
     * @param cusName 客户名称
     * @return List<StoreCusFuzzyResDTO>
     */
    List<StoreCusFuzzyResDTO> fuzzyQueryList(Long storeId, String cusName);

}
