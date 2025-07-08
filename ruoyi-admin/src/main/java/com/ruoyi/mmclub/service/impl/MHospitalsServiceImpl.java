package com.ruoyi.mmclub.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.mmclub.domain.MHospitalCategory;
import com.ruoyi.mmclub.mapper.MHospitalsMapper;
import com.ruoyi.mmclub.domain.MHospitals;
import com.ruoyi.mmclub.service.IMHospitalsService;

/**
 * 医院管理Service业务层处理
 * 
 * @author Jerry.Majesty
 * @date 2025-07-07
 */
@Service
public class MHospitalsServiceImpl implements IMHospitalsService 
{
    @Autowired
    private MHospitalsMapper mHospitalsMapper;

    /**
     * 查询医院管理
     * 
     * @param id 医院管理主键
     * @return 医院管理
     */
    @Override
    public MHospitals selectMHospitalsById(Long id)
    {
        return mHospitalsMapper.selectMHospitalsById(id);
    }

    /**
     * 查询医院管理列表
     * 
     * @param mHospitals 医院管理
     * @return 医院管理
     */
    @Override
    public List<MHospitals> selectMHospitalsList(MHospitals mHospitals)
    {
        return mHospitalsMapper.selectMHospitalsList(mHospitals);
    }

    /**
     * 新增医院管理
     * 
     * @param mHospitals 医院管理
     * @return 结果
     */
    @Transactional
    @Override
    public int insertMHospitals(MHospitals mHospitals)
    {
        mHospitals.setCreateTime(DateUtils.getNowDate());
        int rows = mHospitalsMapper.insertMHospitals(mHospitals);
        insertMHospitalCategory(mHospitals);
        return rows;
    }

    /**
     * 修改医院管理
     * 
     * @param mHospitals 医院管理
     * @return 结果
     */
    @Transactional
    @Override
    public int updateMHospitals(MHospitals mHospitals)
    {
        mHospitals.setUpdateTime(DateUtils.getNowDate());
        mHospitalsMapper.deleteMHospitalCategoryByHospitalId(mHospitals.getId());
        insertMHospitalCategory(mHospitals);
        return mHospitalsMapper.updateMHospitals(mHospitals);
    }

    /**
     * 批量删除医院管理
     * 
     * @param ids 需要删除的医院管理主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteMHospitalsByIds(Long[] ids)
    {
        mHospitalsMapper.deleteMHospitalCategoryByHospitalIds(ids);
        return mHospitalsMapper.deleteMHospitalsByIds(ids);
    }

    /**
     * 删除医院管理信息
     * 
     * @param id 医院管理主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteMHospitalsById(Long id)
    {
        mHospitalsMapper.deleteMHospitalCategoryByHospitalId(id);
        return mHospitalsMapper.deleteMHospitalsById(id);
    }

    /**
     * 新增医院分类关系映射信息
     * 
     * @param mHospitals 医院管理对象
     */
    public void insertMHospitalCategory(MHospitals mHospitals)
    {
        List<MHospitalCategory> mHospitalCategoryList = mHospitals.getMHospitalCategoryList();
        Long id = mHospitals.getId();
        if (StringUtils.isNotNull(mHospitalCategoryList))
        {
            List<MHospitalCategory> list = new ArrayList<MHospitalCategory>();
            for (MHospitalCategory mHospitalCategory : mHospitalCategoryList)
            {
                mHospitalCategory.setHospitalId(id);
                list.add(mHospitalCategory);
            }
            if (list.size() > 0)
            {
                mHospitalsMapper.batchMHospitalCategory(list);
            }
        }
    }
}
