package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.BjParamConfigQuote;

/**
 * 报价参数Service接口
 * 
 * @author ssq
 * @date 2024-10-05
 */
public interface IBjParamConfigQuoteService 
{
    /**
     * 查询报价参数
     * 
     * @param id 报价参数主键
     * @return 报价参数
     */
    public BjParamConfigQuote selectBjParamConfigQuoteById(Long id);

    /**
     * 查询报价参数列表
     * 
     * @param bjParamConfigQuote 报价参数
     * @return 报价参数集合
     */
    public List<BjParamConfigQuote> selectBjParamConfigQuoteList(BjParamConfigQuote bjParamConfigQuote);

    /**
     * 新增报价参数
     * 
     * @param bjParamConfigQuote 报价参数
     * @return 结果
     */
    public int insertBjParamConfigQuote(BjParamConfigQuote bjParamConfigQuote);

    /**
     * 修改报价参数
     * 
     * @param bjParamConfigQuote 报价参数
     * @return 结果
     */
    public int updateBjParamConfigQuote(BjParamConfigQuote bjParamConfigQuote);

    /**
     * 批量删除报价参数
     * 
     * @param ids 需要删除的报价参数主键集合
     * @return 结果
     */
    public int deleteBjParamConfigQuoteByIds(Long[] ids);

    /**
     * 删除报价参数信息
     * 
     * @param id 报价参数主键
     * @return 结果
     */
    public int deleteBjParamConfigQuoteById(Long id);
}
