package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.BjDetailQuote;

/**
 * 报价Service接口
 * 
 * @author ssq
 * @date 2024-10-05
 */
public interface IBjDetailQuoteService 
{
    /**
     * 查询报价
     * 
     * @param id 报价主键
     * @return 报价
     */
    public BjDetailQuote selectBjDetailQuoteById(Long id);

    /**
     * 查询报价列表
     * 
     * @param bjDetailQuote 报价
     * @return 报价集合
     */
    public List<BjDetailQuote> selectBjDetailQuoteList(BjDetailQuote bjDetailQuote);

    /**
     * 新增报价
     * 
     * @param bjDetailQuote 报价
     * @return 结果
     */
    public int insertBjDetailQuote(BjDetailQuote bjDetailQuote);

    /**
     * 修改报价
     * 
     * @param bjDetailQuote 报价
     * @return 结果
     */
    public int updateBjDetailQuote(BjDetailQuote bjDetailQuote);

    /**
     * 批量删除报价
     * 
     * @param ids 需要删除的报价主键集合
     * @return 结果
     */
    public int deleteBjDetailQuoteByIds(Long[] ids);

    /**
     * 删除报价信息
     * 
     * @param id 报价主键
     * @return 结果
     */
    public int deleteBjDetailQuoteById(Long id);
}
