package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.DatabaseProduct;

/**
 * 数据库产品Service接口
 * 
 * @author ruoyi
 * @date 2024-01-20
 */
public interface IDatabaseProductService
{
    /**
     * 查询数据库产品
     * 
     * @param productId 数据库产品主键
     * @return 数据库产品
     */
    public DatabaseProduct selectDatabaseProductByProductId(Long productId);

    /**
     * 查询数据库产品列表
     * 
     * @param databaseProduct 数据库产品
     * @return 数据库产品集合
     */
    public List<DatabaseProduct> selectDatabaseProductList(DatabaseProduct databaseProduct);

    /**
     * 新增数据库产品
     * 
     * @param databaseProduct 数据库产品
     * @return 结果
     */
    public int insertDatabaseProduct(DatabaseProduct databaseProduct);

    /**
     * 修改数据库产品
     * 
     * @param databaseProduct 数据库产品
     * @return 结果
     */
    public int updateDatabaseProduct(DatabaseProduct databaseProduct);

    /**
     * 批量删除数据库产品
     * 
     * @param productIds 需要删除的数据库产品主键集合
     * @return 结果
     */
    public int deleteDatabaseProductByProductIds(Long[] productIds);

    /**
     * 删除数据库产品信息
     * 
     * @param productId 数据库产品主键
     * @return 结果
     */
    public int deleteDatabaseProductByProductId(Long productId);
}