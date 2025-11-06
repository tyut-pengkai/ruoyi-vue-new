package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.DatabaseProductMapper;
import com.ruoyi.system.domain.DatabaseProduct;
import com.ruoyi.system.service.IDatabaseProductService;
import com.ruoyi.common.core.text.Convert;

/**
 * 数据库产品Service业务层实现
 * 
 * @author ruoyi
 * @date 2024-01-20
 */
@Service
public class DatabaseProductServiceImpl implements IDatabaseProductService
{
    @Autowired
    private DatabaseProductMapper databaseProductMapper;

    /**
     * 查询数据库产品
     * 
     * @param productId 数据库产品主键
     * @return 数据库产品
     */
    @Override
    public DatabaseProduct selectDatabaseProductByProductId(Long productId)
    {
        return databaseProductMapper.selectDatabaseProductByProductId(productId);
    }

    /**
     * 查询数据库产品列表
     * 
     * @param databaseProduct 数据库产品
     * @return 数据库产品集合
     */
    @Override
    public List<DatabaseProduct> selectDatabaseProductList(DatabaseProduct databaseProduct)
    {
        return databaseProductMapper.selectDatabaseProductList(databaseProduct);
    }

    /**
     * 新增数据库产品
     * 
     * @param databaseProduct 数据库产品
     * @return 结果
     */
    @Override
    public int insertDatabaseProduct(DatabaseProduct databaseProduct)
    {
        return databaseProductMapper.insertDatabaseProduct(databaseProduct);
    }

    /**
     * 修改数据库产品
     * 
     * @param databaseProduct 数据库产品
     * @return 结果
     */
    @Override
    public int updateDatabaseProduct(DatabaseProduct databaseProduct)
    {
        return databaseProductMapper.updateDatabaseProduct(databaseProduct);
    }

    /**
     * 批量删除数据库产品
     * 
     * @param productIds 需要删除的数据库产品主键集合
     * @return 结果
     */
    @Override
    public int deleteDatabaseProductByProductIds(Long[] productIds)
    {
        return databaseProductMapper.deleteDatabaseProductByProductIds(productIds);
    }

    /**
     * 删除数据库产品信息
     * 
     * @param productId 数据库产品主键
     * @return 结果
     */
    @Override
    public int deleteDatabaseProductByProductId(Long productId)
    {
        return databaseProductMapper.deleteDatabaseProductByProductId(productId);
    }
}