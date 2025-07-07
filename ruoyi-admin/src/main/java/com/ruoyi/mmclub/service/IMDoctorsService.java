package com.ruoyi.mmclub.service;

import java.util.List;
import com.ruoyi.mmclub.domain.MDoctors;

/**
 * 医生管理Service接口
 * 
 * @author Jerry.Majesty
 * @date 2025-07-07
 */
public interface IMDoctorsService 
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
     * 批量删除医生管理
     * 
     * @param ids 需要删除的医生管理主键集合
     * @return 结果
     */
    public int deleteMDoctorsByIds(Long[] ids);

    /**
     * 删除医生管理信息
     * 
     * @param id 医生管理主键
     * @return 结果
     */
    public int deleteMDoctorsById(Long id);
}
