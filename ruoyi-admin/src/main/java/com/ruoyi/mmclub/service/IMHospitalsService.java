package com.ruoyi.mmclub.service;

import java.util.List;
import com.ruoyi.mmclub.domain.MHospitals;

/**
 * 医院管理Service接口
 * 
 * @author Jerry.Majesty
 * @date 2025-07-07
 */
public interface IMHospitalsService 
{
    /**
     * 查询医院管理
     * 
     * @param id 医院管理主键
     * @return 医院管理
     */
    public MHospitals selectMHospitalsById(Long id);

    /**
     * 查询医院管理列表
     * 
     * @param mHospitals 医院管理
     * @return 医院管理集合
     */
    public List<MHospitals> selectMHospitalsList(MHospitals mHospitals);

    /**
     * 新增医院管理
     * 
     * @param mHospitals 医院管理
     * @return 结果
     */
    public int insertMHospitals(MHospitals mHospitals);

    /**
     * 修改医院管理
     * 
     * @param mHospitals 医院管理
     * @return 结果
     */
    public int updateMHospitals(MHospitals mHospitals);

    /**
     * 批量删除医院管理
     * 
     * @param ids 需要删除的医院管理主键集合
     * @return 结果
     */
    public int deleteMHospitalsByIds(Long[] ids);

    /**
     * 删除医院管理信息
     * 
     * @param id 医院管理主键
     * @return 结果
     */
    public int deleteMHospitalsById(Long id);
}
