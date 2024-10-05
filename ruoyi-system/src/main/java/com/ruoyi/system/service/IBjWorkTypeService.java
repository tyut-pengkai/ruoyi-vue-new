package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.BjWorkType;

/**
 * 工种管理Service接口
 * 
 * @author ssq
 * @date 2024-10-05
 */
public interface IBjWorkTypeService 
{
    /**
     * 查询工种管理
     * 
     * @param id 工种管理主键
     * @return 工种管理
     */
    public BjWorkType selectBjWorkTypeById(Long id);

    /**
     * 查询工种管理列表
     * 
     * @param bjWorkType 工种管理
     * @return 工种管理集合
     */
    public List<BjWorkType> selectBjWorkTypeList(BjWorkType bjWorkType);

    /**
     * 新增工种管理
     * 
     * @param bjWorkType 工种管理
     * @return 结果
     */
    public int insertBjWorkType(BjWorkType bjWorkType);

    /**
     * 修改工种管理
     * 
     * @param bjWorkType 工种管理
     * @return 结果
     */
    public int updateBjWorkType(BjWorkType bjWorkType);

    /**
     * 批量删除工种管理
     * 
     * @param ids 需要删除的工种管理主键集合
     * @return 结果
     */
    public int deleteBjWorkTypeByIds(Long[] ids);

    /**
     * 删除工种管理信息
     * 
     * @param id 工种管理主键
     * @return 结果
     */
    public int deleteBjWorkTypeById(Long id);
}
