package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.BjDetailQuote;

/**
 * 详细报价Service接口
 * 
 * @author ssq
 * @date 2024-10-08
 */
public interface IBjDetailQuoteService 
{
    /**
     * 查询详细报价
     * 
     * @param id 详细报价主键
     * @return 详细报价
     */
    public BjDetailQuote selectBjDetailQuoteById(Long id);

    /**
     * 查询详细报价列表
     * 
     * @param bjDetailQuote 详细报价
     * @return 详细报价集合
     */
    public List<BjDetailQuote> selectBjDetailQuoteList(BjDetailQuote bjDetailQuote);

    /**
     * 新增详细报价
     * 
     * @param bjDetailQuote 详细报价
     * @return 结果
     */
    public int insertBjDetailQuote(BjDetailQuote bjDetailQuote);

    /**
     * 修改详细报价
     * 
     * @param bjDetailQuote 详细报价
     * @return 结果
     */
    public int updateBjDetailQuote(BjDetailQuote bjDetailQuote);

    /**
     * 批量删除详细报价
     * 
     * @param ids 需要删除的详细报价主键集合
     * @return 结果
     */
    public int deleteBjDetailQuoteByIds(Long[] ids);

    /**
     * 删除详细报价信息
     * 
     * @param id 详细报价主键
     * @return 结果
     */
    public int deleteBjDetailQuoteById(Long id);
}
