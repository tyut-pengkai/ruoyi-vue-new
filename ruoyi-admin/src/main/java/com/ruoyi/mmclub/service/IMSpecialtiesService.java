package com.ruoyi.mmclub.service;

import java.util.List;
import com.ruoyi.mmclub.domain.MSpecialties;

/**
 * 医生专业管理Service接口
 * 
 * @author Jerry.Majesty
 * @date 2025-07-07
 */
public interface IMSpecialtiesService 
{
    /**
     * 查询医生专业管理
     * 
     * @param id 医生专业管理主键
     * @return 医生专业管理
     */
    public MSpecialties selectMSpecialtiesById(Long id);

    /**
     * 查询医生专业管理列表
     * 
     * @param mSpecialties 医生专业管理
     * @return 医生专业管理集合
     */
    public List<MSpecialties> selectMSpecialtiesList(MSpecialties mSpecialties);

    /**
     * 新增医生专业管理
     * 
     * @param mSpecialties 医生专业管理
     * @return 结果
     */
    public int insertMSpecialties(MSpecialties mSpecialties);

    /**
     * 修改医生专业管理
     * 
     * @param mSpecialties 医生专业管理
     * @return 结果
     */
    public int updateMSpecialties(MSpecialties mSpecialties);

    /**
     * 批量删除医生专业管理
     * 
     * @param ids 需要删除的医生专业管理主键集合
     * @return 结果
     */
    public int deleteMSpecialtiesByIds(Long[] ids);

    /**
     * 删除医生专业管理信息
     * 
     * @param id 医生专业管理主键
     * @return 结果
     */
    public int deleteMSpecialtiesById(Long id);
}
