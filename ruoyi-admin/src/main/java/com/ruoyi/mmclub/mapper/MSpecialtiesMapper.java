package com.ruoyi.mmclub.mapper;

import java.util.List;
import com.ruoyi.mmclub.domain.MSpecialties;

/**
 * 医生专业管理Mapper接口
 * 
 * @author Jerry.Majesty
 * @date 2025-07-07
 */
public interface MSpecialtiesMapper 
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
     * 删除医生专业管理
     * 
     * @param id 医生专业管理主键
     * @return 结果
     */
    public int deleteMSpecialtiesById(Long id);

    /**
     * 批量删除医生专业管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMSpecialtiesByIds(Long[] ids);
}
