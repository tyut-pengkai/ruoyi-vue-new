package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.TMaterial;

/**
 * 物料Mapper接口
 * 
 * @author ruoyi
 * @date 2024-10-31
 */
public interface TMaterialMapper 
{
    /**
     * 查询物料
     * 
     * @param id 物料主键
     * @return 物料
     */
    public TMaterial selectTMaterialById(Long id);

    /**
     * 查询物料列表
     * 
     * @param tMaterial 物料
     * @return 物料集合
     */
    public List<TMaterial> selectTMaterialList(TMaterial tMaterial);

    /**
     * 新增物料
     * 
     * @param tMaterial 物料
     * @return 结果
     */
    public int insertTMaterial(TMaterial tMaterial);

    /**
     * 修改物料
     * 
     * @param tMaterial 物料
     * @return 结果
     */
    public int updateTMaterial(TMaterial tMaterial);

    /**
     * 删除物料
     * 
     * @param id 物料主键
     * @return 结果
     */
    public int deleteTMaterialById(Long id);

    /**
     * 批量删除物料
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTMaterialByIds(Long[] ids);
}
