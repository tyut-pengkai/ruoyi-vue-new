package com.ruoyi.mmclub.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.mmclub.mapper.MCategorysMapper;
import com.ruoyi.mmclub.domain.MCategorys;
import com.ruoyi.mmclub.service.IMCategorysService;

/**
 * 医院分类管理Service业务层处理
 * 
 * @author Jerry.Majesty
 * @date 2025-07-07
 */
@Service
public class MCategorysServiceImpl implements IMCategorysService 
{
    @Autowired
    private MCategorysMapper mCategorysMapper;

    /**
     * 查询医院分类管理
     * 
     * @param id 医院分类管理主键
     * @return 医院分类管理
     */
    @Override
    public MCategorys selectMCategorysById(Long id)
    {
        return mCategorysMapper.selectMCategorysById(id);
    }

    /**
     * 查询医院分类管理列表
     * 
     * @param mCategorys 医院分类管理
     * @return 医院分类管理
     */
    @Override
    public List<MCategorys> selectMCategorysList(MCategorys mCategorys)
    {
        return mCategorysMapper.selectMCategorysList(mCategorys);
    }

    /**
     * 新增医院分类管理
     * 
     * @param mCategorys 医院分类管理
     * @return 结果
     */
    @Override
    public int insertMCategorys(MCategorys mCategorys)
    {
        mCategorys.setCreateTime(DateUtils.getNowDate());
        return mCategorysMapper.insertMCategorys(mCategorys);
    }

    /**
     * 修改医院分类管理
     * 
     * @param mCategorys 医院分类管理
     * @return 结果
     */
    @Override
    public int updateMCategorys(MCategorys mCategorys)
    {
        mCategorys.setUpdateTime(DateUtils.getNowDate());
        return mCategorysMapper.updateMCategorys(mCategorys);
    }

    /**
     * 批量删除医院分类管理
     * 
     * @param ids 需要删除的医院分类管理主键
     * @return 结果
     */
    @Override
    public int deleteMCategorysByIds(Long[] ids)
    {
        return mCategorysMapper.deleteMCategorysByIds(ids);
    }

    /**
     * 删除医院分类管理信息
     * 
     * @param id 医院分类管理主键
     * @return 结果
     */
    @Override
    public int deleteMCategorysById(Long id)
    {
        return mCategorysMapper.deleteMCategorysById(id);
    }
}
