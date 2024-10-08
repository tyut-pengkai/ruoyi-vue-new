package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.BjDetailQuoteMapper;
import com.ruoyi.system.domain.BjDetailQuote;
import com.ruoyi.system.service.IBjDetailQuoteService;

/**
 * 详细报价Service业务层处理
 * 
 * @author ssq
 * @date 2024-10-08
 */
@Service
public class BjDetailQuoteServiceImpl implements IBjDetailQuoteService 
{
    @Autowired
    private BjDetailQuoteMapper bjDetailQuoteMapper;

    /**
     * 查询详细报价
     * 
     * @param id 详细报价主键
     * @return 详细报价
     */
    @Override
    public BjDetailQuote selectBjDetailQuoteById(Long id)
    {
        return bjDetailQuoteMapper.selectBjDetailQuoteById(id);
    }

    /**
     * 查询详细报价列表
     * 
     * @param bjDetailQuote 详细报价
     * @return 详细报价
     */
    @Override
    public List<BjDetailQuote> selectBjDetailQuoteList(BjDetailQuote bjDetailQuote)
    {
        return bjDetailQuoteMapper.selectBjDetailQuoteList(bjDetailQuote);
    }

    /**
     * 新增详细报价
     * 
     * @param bjDetailQuote 详细报价
     * @return 结果
     */
    @Override
    public int insertBjDetailQuote(BjDetailQuote bjDetailQuote)
    {
        bjDetailQuote.setCreateTime(DateUtils.getNowDate());
        return bjDetailQuoteMapper.insertBjDetailQuote(bjDetailQuote);
    }

    /**
     * 修改详细报价
     * 
     * @param bjDetailQuote 详细报价
     * @return 结果
     */
    @Override
    public int updateBjDetailQuote(BjDetailQuote bjDetailQuote)
    {
        bjDetailQuote.setUpdateTime(DateUtils.getNowDate());
        return bjDetailQuoteMapper.updateBjDetailQuote(bjDetailQuote);
    }

    /**
     * 批量删除详细报价
     * 
     * @param ids 需要删除的详细报价主键
     * @return 结果
     */
    @Override
    public int deleteBjDetailQuoteByIds(Long[] ids)
    {
        return bjDetailQuoteMapper.deleteBjDetailQuoteByIds(ids);
    }

    /**
     * 删除详细报价信息
     * 
     * @param id 详细报价主键
     * @return 结果
     */
    @Override
    public int deleteBjDetailQuoteById(Long id)
    {
        return bjDetailQuoteMapper.deleteBjDetailQuoteById(id);
    }
}
