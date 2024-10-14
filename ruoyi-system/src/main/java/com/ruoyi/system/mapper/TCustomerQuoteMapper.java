package com.ruoyi.system.mapper;

import java.util.List;
import java.util.Map;

import com.ruoyi.system.domain.TCustomerQuote;

/**
 * 客户报价单Mapper接口
 * 
 * @author ruoyi
 * @date 2024-10-10
 */
public interface TCustomerQuoteMapper
{
    /**
     * 查询客户报价单
     * 
     * @param id 客户报价单主键
     * @return 客户报价单
     */
    public TCustomerQuote selectTCustomerQuoteById(Long id);

    /**
     * 查询客户报价单列表
     * 
     * @param tCustomerQuote 客户报价单
     * @return 客户报价单集合
     */
    public List<TCustomerQuote> selectTCustomerQuoteList(TCustomerQuote tCustomerQuote);

    /**
     * 新增客户报价单
     * 
     * @param tCustomerQuote 客户报价单
     * @return 结果
     */
    public int insertTCustomerQuote(TCustomerQuote tCustomerQuote);

    /**
     * 修改客户报价单
     * 
     * @param tCustomerQuote 客户报价单
     * @return 结果
     */
    public int updateTCustomerQuote(TCustomerQuote tCustomerQuote);

    /**
     * 删除客户报价单
     * 
     * @param id 客户报价单主键
     * @return 结果
     */
    public int deleteTCustomerQuoteById(Long id);

    /**
     * 批量删除客户报价单
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTCustomerQuoteByIds(Long[] ids);
    
    public List<Map<String,Object>> listCoustomerQuote(TCustomerQuote tCustomerQuote);
}
