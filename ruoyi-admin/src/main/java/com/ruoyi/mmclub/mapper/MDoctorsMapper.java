package com.ruoyi.mmclub.mapper;

import java.util.List;
import com.ruoyi.mmclub.domain.MDoctors;
import com.ruoyi.mmclub.domain.MDoctorSpecialty;

/**
 * 医生管理Mapper接口
 * 
 * @author Jerry.Majesty
 * @date 2025-07-07
 */
public interface MDoctorsMapper 
{
    /**
     * 查询医生管理
     * 
     * @param id 医生管理主键
     * @return 医生管理
     */
    public MDoctors selectMDoctorsById(Long id);

    /**
     * 查询医生管理列表
     * 
     * @param mDoctors 医生管理
     * @return 医生管理集合
     */
    public List<MDoctors> selectMDoctorsList(MDoctors mDoctors);

    /**
     * 新增医生管理
     * 
     * @param mDoctors 医生管理
     * @return 结果
     */
    public int insertMDoctors(MDoctors mDoctors);

    /**
     * 修改医生管理
     * 
     * @param mDoctors 医生管理
     * @return 结果
     */
    public int updateMDoctors(MDoctors mDoctors);

    /**
     * 删除医生管理
     * 
     * @param id 医生管理主键
     * @return 结果
     */
    public int deleteMDoctorsById(Long id);

    /**
     * 批量删除医生管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMDoctorsByIds(Long[] ids);

    /**
     * 批量删除医生专业领域关系映射
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMDoctorSpecialtyByDoctorIds(Long[] ids);
    
    /**
     * 批量新增医生专业领域关系映射
     * 
     * @param mDoctorSpecialtyList 医生专业领域关系映射列表
     * @return 结果
     */
    public int batchMDoctorSpecialty(List<MDoctorSpecialty> mDoctorSpecialtyList);
    

    /**
     * 通过医生管理主键删除医生专业领域关系映射信息
     * 
     * @param id 医生管理ID
     * @return 结果
     */
    public int deleteMDoctorSpecialtyByDoctorId(Long id);
}
