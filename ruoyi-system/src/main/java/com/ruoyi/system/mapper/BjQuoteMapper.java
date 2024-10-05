package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BjQuote;

/**
 * 对外报价Mapper接口
 * 
 * @author ssq
 * @date 2024-10-05
 */
public interface BjQuoteMapper 
{
    /**
     * 查询对外报价
     * 
     * @param id 对外报价主键
     * @return 对外报价
     */
    public BjQuote selectBjQuoteById(Long id);

    /**
     * 查询对外报价列表
     * 
     * @param bjQuote 对外报价
     * @return 对外报价集合
     */
    public List<BjQuote> selectBjQuoteList(BjQuote bjQuote);

    /**
     * 新增对外报价
     * 
     * @param bjQuote 对外报价
     * @return 结果
     */
    public int insertBjQuote(BjQuote bjQuote);

    /**
     * 修改对外报价
     * 
     * @param bjQuote 对外报价
     * @return 结果
     */
    public int updateBjQuote(BjQuote bjQuote);

    /**
     * 删除对外报价
     * 
     * @param id 对外报价主键
     * @return 结果
     */
    public int deleteBjQuoteById(Long id);

    /**
     * 批量删除对外报价
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBjQuoteByIds(Long[] ids);
}
