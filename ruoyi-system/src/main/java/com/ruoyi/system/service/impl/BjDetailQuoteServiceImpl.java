package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.BjDetailQuoteMapper;
import com.ruoyi.system.domain.BjDetailQuote;
import com.ruoyi.system.service.IBjDetailQuoteService;

/**
 * 报价Service业务层处理
 * 
 * @author ssq
 * @date 2024-10-05
 */
@Service
public class BjDetailQuoteServiceImpl implements IBjDetailQuoteService 
{
    @Autowired
    private BjDetailQuoteMapper bjDetailQuoteMapper;

    /**
     * 查询报价
     * 
     * @param id 报价主键
     * @return 报价
     */
    @Override
    public BjDetailQuote selectBjDetailQuoteById(Long id)
    {
        return bjDetailQuoteMapper.selectBjDetailQuoteById(id);
    }

    /**
     * 查询报价列表
     * 
     * @param bjDetailQuote 报价
     * @return 报价
     */
    @Override
    public List<BjDetailQuote> selectBjDetailQuoteList(BjDetailQuote bjDetailQuote)
    {
        return bjDetailQuoteMapper.selectBjDetailQuoteList(bjDetailQuote);
    }

    /**
     * 新增报价
     * 
     * @param bjDetailQuote 报价
     * @return 结果
     */
    @Override
    public int insertBjDetailQuote(BjDetailQuote bjDetailQuote)
    {
        return bjDetailQuoteMapper.insertBjDetailQuote(bjDetailQuote);
    }

    /**
     * 修改报价
     * 
     * @param bjDetailQuote 报价
     * @return 结果
     */
    @Override
    public int updateBjDetailQuote(BjDetailQuote bjDetailQuote)
    {
        return bjDetailQuoteMapper.updateBjDetailQuote(bjDetailQuote);
    }

    /**
     * 批量删除报价
     * 
     * @param ids 需要删除的报价主键
     * @return 结果
     */
    @Override
    public int deleteBjDetailQuoteByIds(Long[] ids)
    {
        return bjDetailQuoteMapper.deleteBjDetailQuoteByIds(ids);
    }

    /**
     * 删除报价信息
     * 
     * @param id 报价主键
     * @return 结果
     */
    @Override
    public int deleteBjDetailQuoteById(Long id)
    {
        return bjDetailQuoteMapper.deleteBjDetailQuoteById(id);
    }
}
