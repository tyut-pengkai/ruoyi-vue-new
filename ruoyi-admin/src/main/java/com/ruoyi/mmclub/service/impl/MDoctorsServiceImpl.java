package com.ruoyi.mmclub.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.mmclub.domain.MDoctorSpecialty;
import com.ruoyi.mmclub.mapper.MDoctorsMapper;
import com.ruoyi.mmclub.domain.MDoctors;
import com.ruoyi.mmclub.service.IMDoctorsService;

/**
 * 医生管理Service业务层处理
 * 
 * @author Jerry.Majesty
 * @date 2025-07-07
 */
@Service
public class MDoctorsServiceImpl implements IMDoctorsService 
{
    @Autowired
    private MDoctorsMapper mDoctorsMapper;

    /**
     * 查询医生管理
     * 
     * @param id 医生管理主键
     * @return 医生管理
     */
    @Override
    public MDoctors selectMDoctorsById(Long id)
    {
        return mDoctorsMapper.selectMDoctorsById(id);
    }

    /**
     * 查询医生管理列表
     * 
     * @param mDoctors 医生管理
     * @return 医生管理
     */
    @Override
    public List<MDoctors> selectMDoctorsList(MDoctors mDoctors)
    {
        return mDoctorsMapper.selectMDoctorsList(mDoctors);
    }

    /**
     * 新增医生管理
     * 
     * @param mDoctors 医生管理
     * @return 结果
     */
    @Transactional
    @Override
    public int insertMDoctors(MDoctors mDoctors)
    {
        mDoctors.setCreateTime(DateUtils.getNowDate());
        int rows = mDoctorsMapper.insertMDoctors(mDoctors);
        insertMDoctorSpecialty(mDoctors);
        return rows;
    }

    /**
     * 修改医生管理
     * 
     * @param mDoctors 医生管理
     * @return 结果
     */
    @Transactional
    @Override
    public int updateMDoctors(MDoctors mDoctors)
    {
        mDoctors.setUpdateTime(DateUtils.getNowDate());
        mDoctorsMapper.deleteMDoctorSpecialtyByDoctorId(mDoctors.getId());
        insertMDoctorSpecialty(mDoctors);
        return mDoctorsMapper.updateMDoctors(mDoctors);
    }

    /**
     * 批量删除医生管理
     * 
     * @param ids 需要删除的医生管理主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteMDoctorsByIds(Long[] ids)
    {
        mDoctorsMapper.deleteMDoctorSpecialtyByDoctorIds(ids);
        return mDoctorsMapper.deleteMDoctorsByIds(ids);
    }

    /**
     * 删除医生管理信息
     * 
     * @param id 医生管理主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteMDoctorsById(Long id)
    {
        mDoctorsMapper.deleteMDoctorSpecialtyByDoctorId(id);
        return mDoctorsMapper.deleteMDoctorsById(id);
    }

    /**
     * 新增医生专业领域关系映射信息
     * 
     * @param mDoctors 医生管理对象
     */
    public void insertMDoctorSpecialty(MDoctors mDoctors)
    {
        List<MDoctorSpecialty> mDoctorSpecialtyList = mDoctors.getMDoctorSpecialtyList();
        Long id = mDoctors.getId();
        if (StringUtils.isNotNull(mDoctorSpecialtyList))
        {
            List<MDoctorSpecialty> list = new ArrayList<MDoctorSpecialty>();
            for (MDoctorSpecialty mDoctorSpecialty : mDoctorSpecialtyList)
            {
                mDoctorSpecialty.setDoctorId(id);
                list.add(mDoctorSpecialty);
            }
            if (list.size() > 0)
            {
                mDoctorsMapper.batchMDoctorSpecialty(list);
            }
        }
    }
}
