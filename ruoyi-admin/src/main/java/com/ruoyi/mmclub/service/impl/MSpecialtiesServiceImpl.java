package com.ruoyi.mmclub.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.mmclub.mapper.MSpecialtiesMapper;
import com.ruoyi.mmclub.domain.MSpecialties;
import com.ruoyi.mmclub.service.IMSpecialtiesService;

/**
 * 医生专业管理Service业务层处理
 * 
 * @author Jerry.Majesty
 * @date 2025-07-07
 */
@Service
public class MSpecialtiesServiceImpl implements IMSpecialtiesService 
{
    @Autowired
    private MSpecialtiesMapper mSpecialtiesMapper;

    /**
     * 查询医生专业管理
     * 
     * @param id 医生专业管理主键
     * @return 医生专业管理
     */
    @Override
    public MSpecialties selectMSpecialtiesById(Long id)
    {
        return mSpecialtiesMapper.selectMSpecialtiesById(id);
    }

    /**
     * 查询医生专业管理列表
     * 
     * @param mSpecialties 医生专业管理
     * @return 医生专业管理
     */
    @Override
    public List<MSpecialties> selectMSpecialtiesList(MSpecialties mSpecialties)
    {
        return mSpecialtiesMapper.selectMSpecialtiesList(mSpecialties);
    }

    /**
     * 新增医生专业管理
     * 
     * @param mSpecialties 医生专业管理
     * @return 结果
     */
    @Override
    public int insertMSpecialties(MSpecialties mSpecialties)
    {
        mSpecialties.setCreateTime(DateUtils.getNowDate());
        return mSpecialtiesMapper.insertMSpecialties(mSpecialties);
    }

    /**
     * 修改医生专业管理
     * 
     * @param mSpecialties 医生专业管理
     * @return 结果
     */
    @Override
    public int updateMSpecialties(MSpecialties mSpecialties)
    {
        mSpecialties.setUpdateTime(DateUtils.getNowDate());
        return mSpecialtiesMapper.updateMSpecialties(mSpecialties);
    }

    /**
     * 批量删除医生专业管理
     * 
     * @param ids 需要删除的医生专业管理主键
     * @return 结果
     */
    @Override
    public int deleteMSpecialtiesByIds(Long[] ids)
    {
        return mSpecialtiesMapper.deleteMSpecialtiesByIds(ids);
    }

    /**
     * 删除医生专业管理信息
     * 
     * @param id 医生专业管理主键
     * @return 结果
     */
    @Override
    public int deleteMSpecialtiesById(Long id)
    {
        return mSpecialtiesMapper.deleteMSpecialtiesById(id);
    }
}
