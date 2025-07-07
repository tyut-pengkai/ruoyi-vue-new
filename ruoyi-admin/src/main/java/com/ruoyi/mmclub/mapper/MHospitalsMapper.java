package com.ruoyi.mmclub.mapper;

import java.util.List;
import com.ruoyi.mmclub.domain.MHospitals;
import com.ruoyi.mmclub.domain.MHospitalCategory;

/**
 * 医院管理Mapper接口
 * 
 * @author Jerry.Majesty
 * @date 2025-07-07
 */
public interface MHospitalsMapper 
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
     * 删除医院管理
     * 
     * @param id 医院管理主键
     * @return 结果
     */
    public int deleteMHospitalsById(Long id);

    /**
     * 批量删除医院管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMHospitalsByIds(Long[] ids);

    /**
     * 批量删除医院分类关系映射
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMHospitalCategoryByHospitalIds(Long[] ids);
    
    /**
     * 批量新增医院分类关系映射
     * 
     * @param mHospitalCategoryList 医院分类关系映射列表
     * @return 结果
     */
    public int batchMHospitalCategory(List<MHospitalCategory> mHospitalCategoryList);
    

    /**
     * 通过医院管理主键删除医院分类关系映射信息
     * 
     * @param id 医院管理ID
     * @return 结果
     */
    public int deleteMHospitalCategoryByHospitalId(Long id);
}
