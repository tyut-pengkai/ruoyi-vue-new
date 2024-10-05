package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.BjParamConfigQuoteMapper;
import com.ruoyi.system.domain.BjParamConfigQuote;
import com.ruoyi.system.service.IBjParamConfigQuoteService;

/**
 * 报价参数Service业务层处理
 * 
 * @author ssq
 * @date 2024-10-05
 */
@Service
public class BjParamConfigQuoteServiceImpl implements IBjParamConfigQuoteService 
{
    @Autowired
    private BjParamConfigQuoteMapper bjParamConfigQuoteMapper;

    /**
     * 查询报价参数
     * 
     * @param id 报价参数主键
     * @return 报价参数
     */
    @Override
    public BjParamConfigQuote selectBjParamConfigQuoteById(Long id)
    {
        return bjParamConfigQuoteMapper.selectBjParamConfigQuoteById(id);
    }

    /**
     * 查询报价参数列表
     * 
     * @param bjParamConfigQuote 报价参数
     * @return 报价参数
     */
    @Override
    public List<BjParamConfigQuote> selectBjParamConfigQuoteList(BjParamConfigQuote bjParamConfigQuote)
    {
        return bjParamConfigQuoteMapper.selectBjParamConfigQuoteList(bjParamConfigQuote);
    }

    /**
     * 新增报价参数
     * 
     * @param bjParamConfigQuote 报价参数
     * @return 结果
     */
    @Override
    public int insertBjParamConfigQuote(BjParamConfigQuote bjParamConfigQuote)
    {
        return bjParamConfigQuoteMapper.insertBjParamConfigQuote(bjParamConfigQuote);
    }

    /**
     * 修改报价参数
     * 
     * @param bjParamConfigQuote 报价参数
     * @return 结果
     */
    @Override
    public int updateBjParamConfigQuote(BjParamConfigQuote bjParamConfigQuote)
    {
        return bjParamConfigQuoteMapper.updateBjParamConfigQuote(bjParamConfigQuote);
    }

    /**
     * 批量删除报价参数
     * 
     * @param ids 需要删除的报价参数主键
     * @return 结果
     */
    @Override
    public int deleteBjParamConfigQuoteByIds(Long[] ids)
    {
        return bjParamConfigQuoteMapper.deleteBjParamConfigQuoteByIds(ids);
    }

    /**
     * 删除报价参数信息
     * 
     * @param id 报价参数主键
     * @return 结果
     */
    @Override
    public int deleteBjParamConfigQuoteById(Long id)
    {
        return bjParamConfigQuoteMapper.deleteBjParamConfigQuoteById(id);
    }
}
