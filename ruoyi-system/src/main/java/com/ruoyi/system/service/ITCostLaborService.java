package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.TCostLabor;

/**
 * 人工报价成本Service接口
 * 
 * @author ruoyi
 * @date 2024-10-12
 */
public interface ITCostLaborService 
{
    /**
     * 查询人工报价成本
     * 
     * @param id 人工报价成本主键
     * @return 人工报价成本
     */
    public TCostLabor selectTCostLaborById(Long id);

    /**
     * 查询人工报价成本列表
     * 
     * @param tCostLabor 人工报价成本
     * @return 人工报价成本集合
     */
    public List<TCostLabor> selectTCostLaborList(TCostLabor tCostLabor);

    /**
     * 新增人工报价成本
     * 
     * @param tCostLabor 人工报价成本
     * @return 结果
     */
    public int insertTCostLabor(TCostLabor tCostLabor);

    /**
     * 修改人工报价成本
     * 
     * @param tCostLabor 人工报价成本
     * @return 结果
     */
    public int updateTCostLabor(TCostLabor tCostLabor);

    /**
     * 批量删除人工报价成本
     * 
     * @param ids 需要删除的人工报价成本主键集合
     * @return 结果
     */
    public int deleteTCostLaborByIds(Long[] ids);

    /**
     * 删除人工报价成本信息
     * 
     * @param id 人工报价成本主键
     * @return 结果
     */
    public int deleteTCostLaborById(Long id);
    
    public List<TCostLabor> historyTCostLaborList(TCostLabor tCostLabor);
    
    public int deleteTCostLaborByCustomer(TCostLabor tCostLabor);
}
