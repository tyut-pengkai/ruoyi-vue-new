package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.BjQuoteMapper;
import com.ruoyi.system.domain.BjQuote;
import com.ruoyi.system.service.IBjQuoteService;

/**
 * 对外报价Service业务层处理
 * 
 * @author ssq
 * @date 2024-10-05
 */
@Service
public class BjQuoteServiceImpl implements IBjQuoteService 
{
    @Autowired
    private BjQuoteMapper bjQuoteMapper;

    /**
     * 查询对外报价
     * 
     * @param id 对外报价主键
     * @return 对外报价
     */
    @Override
    public BjQuote selectBjQuoteById(Long id)
    {
        return bjQuoteMapper.selectBjQuoteById(id);
    }

    /**
     * 查询对外报价列表
     * 
     * @param bjQuote 对外报价
     * @return 对外报价
     */
    @Override
    public List<BjQuote> selectBjQuoteList(BjQuote bjQuote)
    {
        return bjQuoteMapper.selectBjQuoteList(bjQuote);
    }

    /**
     * 新增对外报价
     * 
     * @param bjQuote 对外报价
     * @return 结果
     */
    @Override
    public int insertBjQuote(BjQuote bjQuote)
    {
        return bjQuoteMapper.insertBjQuote(bjQuote);
    }

    /**
     * 修改对外报价
     * 
     * @param bjQuote 对外报价
     * @return 结果
     */
    @Override
    public int updateBjQuote(BjQuote bjQuote)
    {
        return bjQuoteMapper.updateBjQuote(bjQuote);
    }

    /**
     * 批量删除对外报价
     * 
     * @param ids 需要删除的对外报价主键
     * @return 结果
     */
    @Override
    public int deleteBjQuoteByIds(Long[] ids)
    {
        return bjQuoteMapper.deleteBjQuoteByIds(ids);
    }

    /**
     * 删除对外报价信息
     * 
     * @param id 对外报价主键
     * @return 结果
     */
    @Override
    public int deleteBjQuoteById(Long id)
    {
        return bjQuoteMapper.deleteBjQuoteById(id);
    }
}
