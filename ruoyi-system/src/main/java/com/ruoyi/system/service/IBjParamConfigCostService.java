package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.BjParamConfigCost;

/**
 * 成本参数Service接口
 * 
 * @author ssq
 * @date 2024-10-05
 */
public interface IBjParamConfigCostService 
{
    /**
     * 查询成本参数
     * 
     * @param id 成本参数主键
     * @return 成本参数
     */
    public BjParamConfigCost selectBjParamConfigCostById(Long id);

    /**
     * 查询成本参数列表
     * 
     * @param bjParamConfigCost 成本参数
     * @return 成本参数集合
     */
    public List<BjParamConfigCost> selectBjParamConfigCostList(BjParamConfigCost bjParamConfigCost);

    /**
     * 新增成本参数
     * 
     * @param bjParamConfigCost 成本参数
     * @return 结果
     */
    public int insertBjParamConfigCost(BjParamConfigCost bjParamConfigCost);

    /**
     * 修改成本参数
     * 
     * @param bjParamConfigCost 成本参数
     * @return 结果
     */
    public int updateBjParamConfigCost(BjParamConfigCost bjParamConfigCost);

    /**
     * 批量删除成本参数
     * 
     * @param ids 需要删除的成本参数主键集合
     * @return 结果
     */
    public int deleteBjParamConfigCostByIds(Long[] ids);

    /**
     * 删除成本参数信息
     * 
     * @param id 成本参数主键
     * @return 结果
     */
    public int deleteBjParamConfigCostById(Long id);
}
