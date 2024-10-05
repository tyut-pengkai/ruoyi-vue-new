package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.BjWorkerCost;

/**
 * 人工成本Service接口
 * 
 * @author ssq
 * @date 2024-10-05
 */
public interface IBjWorkerCostService 
{
    /**
     * 查询人工成本
     * 
     * @param id 人工成本主键
     * @return 人工成本
     */
    public BjWorkerCost selectBjWorkerCostById(Long id);

    /**
     * 查询人工成本列表
     * 
     * @param bjWorkerCost 人工成本
     * @return 人工成本集合
     */
    public List<BjWorkerCost> selectBjWorkerCostList(BjWorkerCost bjWorkerCost);

    /**
     * 新增人工成本
     * 
     * @param bjWorkerCost 人工成本
     * @return 结果
     */
    public int insertBjWorkerCost(BjWorkerCost bjWorkerCost);

    /**
     * 修改人工成本
     * 
     * @param bjWorkerCost 人工成本
     * @return 结果
     */
    public int updateBjWorkerCost(BjWorkerCost bjWorkerCost);

    /**
     * 批量删除人工成本
     * 
     * @param ids 需要删除的人工成本主键集合
     * @return 结果
     */
    public int deleteBjWorkerCostByIds(Long[] ids);

    /**
     * 删除人工成本信息
     * 
     * @param id 人工成本主键
     * @return 结果
     */
    public int deleteBjWorkerCostById(Long id);
}
