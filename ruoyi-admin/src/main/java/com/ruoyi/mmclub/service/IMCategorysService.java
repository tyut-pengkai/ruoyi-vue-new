package com.ruoyi.mmclub.service;

import java.util.List;
import com.ruoyi.mmclub.domain.MCategorys;

/**
 * 医院分类管理Service接口
 * 
 * @author Jerry.Majesty
 * @date 2025-07-07
 */
public interface IMCategorysService 
{
    /**
     * 查询医院分类管理
     * 
     * @param id 医院分类管理主键
     * @return 医院分类管理
     */
    public MCategorys selectMCategorysById(Long id);

    /**
     * 查询医院分类管理列表
     * 
     * @param mCategorys 医院分类管理
     * @return 医院分类管理集合
     */
    public List<MCategorys> selectMCategorysList(MCategorys mCategorys);

    /**
     * 新增医院分类管理
     * 
     * @param mCategorys 医院分类管理
     * @return 结果
     */
    public int insertMCategorys(MCategorys mCategorys);

    /**
     * 修改医院分类管理
     * 
     * @param mCategorys 医院分类管理
     * @return 结果
     */
    public int updateMCategorys(MCategorys mCategorys);

    /**
     * 批量删除医院分类管理
     * 
     * @param ids 需要删除的医院分类管理主键集合
     * @return 结果
     */
    public int deleteMCategorysByIds(Long[] ids);

    /**
     * 删除医院分类管理信息
     * 
     * @param id 医院分类管理主键
     * @return 结果
     */
    public int deleteMCategorysById(Long id);
}
