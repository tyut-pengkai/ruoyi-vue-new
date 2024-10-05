package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BjCustomer;

/**
 * 客户管理Mapper接口
 * 
 * @author ssq
 * @date 2024-10-05
 */
public interface BjCustomerMapper 
{
    /**
     * 查询客户管理
     * 
     * @param id 客户管理主键
     * @return 客户管理
     */
    public BjCustomer selectBjCustomerById(Long id);

    /**
     * 查询客户管理列表
     * 
     * @param bjCustomer 客户管理
     * @return 客户管理集合
     */
    public List<BjCustomer> selectBjCustomerList(BjCustomer bjCustomer);

    /**
     * 新增客户管理
     * 
     * @param bjCustomer 客户管理
     * @return 结果
     */
    public int insertBjCustomer(BjCustomer bjCustomer);

    /**
     * 修改客户管理
     * 
     * @param bjCustomer 客户管理
     * @return 结果
     */
    public int updateBjCustomer(BjCustomer bjCustomer);

    /**
     * 删除客户管理
     * 
     * @param id 客户管理主键
     * @return 结果
     */
    public int deleteBjCustomerById(Long id);

    /**
     * 批量删除客户管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBjCustomerByIds(Long[] ids);
}
