package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.TCostQuotationParam;

/**
 * 成本报价参数Mapper接口
 * 
 * @author ruoyi
 * @date 2024-10-11
 */
public interface TCostQuotationParamMapper 
{
    /**
     * 查询成本报价参数
     * 
     * @param id 成本报价参数主键
     * @return 成本报价参数
     */
    public TCostQuotationParam selectTCostQuotationParamById(Long id);

    /**
     * 查询成本报价参数列表
     * 
     * @param tCostQuotationParam 成本报价参数
     * @return 成本报价参数集合
     */
    public List<TCostQuotationParam> selectTCostQuotationParamList(TCostQuotationParam tCostQuotationParam);

    /**
     * 新增成本报价参数
     * 
     * @param tCostQuotationParam 成本报价参数
     * @return 结果
     */
    public int insertTCostQuotationParam(TCostQuotationParam tCostQuotationParam);

    /**
     * 修改成本报价参数
     * 
     * @param tCostQuotationParam 成本报价参数
     * @return 结果
     */
    public int updateTCostQuotationParam(TCostQuotationParam tCostQuotationParam);

    /**
     * 删除成本报价参数
     * 
     * @param id 成本报价参数主键
     * @return 结果
     */
    public int deleteTCostQuotationParamById(Long id);

    /**
     * 批量删除成本报价参数
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTCostQuotationParamByIds(Long[] ids);
}
