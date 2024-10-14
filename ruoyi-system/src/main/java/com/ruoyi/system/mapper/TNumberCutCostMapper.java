package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.TNumberCutCost;

/**
 * 数割费用Mapper接口
 * 
 * @author ruoyi
 * @date 2024-10-10
 */
public interface TNumberCutCostMapper 
{
    /**
     * 查询数割费用
     * 
     * @param id 数割费用主键
     * @return 数割费用
     */
    public TNumberCutCost selectTNumberCutCostById(Long id);

    /**
     * 查询数割费用列表
     * 
     * @param tNumberCutCost 数割费用
     * @return 数割费用集合
     */
    public List<TNumberCutCost> selectTNumberCutCostList(TNumberCutCost tNumberCutCost);

    /**
     * 新增数割费用
     * 
     * @param tNumberCutCost 数割费用
     * @return 结果
     */
    public int insertTNumberCutCost(TNumberCutCost tNumberCutCost);

    /**
     * 修改数割费用
     * 
     * @param tNumberCutCost 数割费用
     * @return 结果
     */
    public int updateTNumberCutCost(TNumberCutCost tNumberCutCost);

    /**
     * 删除数割费用
     * 
     * @param id 数割费用主键
     * @return 结果
     */
    public int deleteTNumberCutCostById(Long id);

    /**
     * 批量删除数割费用
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTNumberCutCostByIds(Long[] ids);
}
