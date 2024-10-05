package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BjDetailQuote;

/**
 * 报价Mapper接口
 * 
 * @author ssq
 * @date 2024-10-05
 */
public interface BjDetailQuoteMapper 
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
     * 删除报价
     * 
     * @param id 报价主键
     * @return 结果
     */
    public int deleteBjDetailQuoteById(Long id);

    /**
     * 批量删除报价
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBjDetailQuoteByIds(Long[] ids);
}
