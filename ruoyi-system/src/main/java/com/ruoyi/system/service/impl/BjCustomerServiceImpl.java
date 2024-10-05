package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.BjCustomerMapper;
import com.ruoyi.system.domain.BjCustomer;
import com.ruoyi.system.service.IBjCustomerService;

/**
 * 客户管理Service业务层处理
 * 
 * @author ssq
 * @date 2024-10-05
 */
@Service
public class BjCustomerServiceImpl implements IBjCustomerService 
{
    @Autowired
    private BjCustomerMapper bjCustomerMapper;

    /**
     * 查询客户管理
     * 
     * @param id 客户管理主键
     * @return 客户管理
     */
    @Override
    public BjCustomer selectBjCustomerById(Long id)
    {
        return bjCustomerMapper.selectBjCustomerById(id);
    }

    /**
     * 查询客户管理列表
     * 
     * @param bjCustomer 客户管理
     * @return 客户管理
     */
    @Override
    public List<BjCustomer> selectBjCustomerList(BjCustomer bjCustomer)
    {
        return bjCustomerMapper.selectBjCustomerList(bjCustomer);
    }

    /**
     * 新增客户管理
     * 
     * @param bjCustomer 客户管理
     * @return 结果
     */
    @Override
    public int insertBjCustomer(BjCustomer bjCustomer)
    {
        return bjCustomerMapper.insertBjCustomer(bjCustomer);
    }

    /**
     * 修改客户管理
     * 
     * @param bjCustomer 客户管理
     * @return 结果
     */
    @Override
    public int updateBjCustomer(BjCustomer bjCustomer)
    {
        return bjCustomerMapper.updateBjCustomer(bjCustomer);
    }

    /**
     * 批量删除客户管理
     * 
     * @param ids 需要删除的客户管理主键
     * @return 结果
     */
    @Override
    public int deleteBjCustomerByIds(Long[] ids)
    {
        return bjCustomerMapper.deleteBjCustomerByIds(ids);
    }

    /**
     * 删除客户管理信息
     * 
     * @param id 客户管理主键
     * @return 结果
     */
    @Override
    public int deleteBjCustomerById(Long id)
    {
        return bjCustomerMapper.deleteBjCustomerById(id);
    }
}
