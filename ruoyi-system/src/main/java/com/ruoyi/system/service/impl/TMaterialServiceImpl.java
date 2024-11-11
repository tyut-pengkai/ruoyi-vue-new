package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TMaterialMapper;
import com.ruoyi.system.domain.TMaterial;
import com.ruoyi.system.service.ITMaterialService;

/**
 * 物料Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-10-31
 */
@Service
public class TMaterialServiceImpl implements ITMaterialService 
{
    @Autowired
    private TMaterialMapper tMaterialMapper;

    /**
     * 查询物料
     * 
     * @param id 物料主键
     * @return 物料
     */
    @Override
    public TMaterial selectTMaterialById(Long id)
    {
    	TMaterial material = tMaterialMapper.selectTMaterialById(id);
//    	if(material != null && material.getParentId() != -1) {
//    		material.setParentMaterial(selectTMaterialById(material.getParentId()));
//    	}
    	return material;
    }

    /**
     * 查询物料列表
     * 
     * @param tMaterial 物料
     * @return 物料
     */
    @Override
    public List<TMaterial> selectTMaterialList(TMaterial tMaterial)
    {
    	List<TMaterial> materialList = tMaterialMapper.selectTMaterialList(tMaterial);
//    	if(materialList != null && materialList.size() > 0) {
//    		materialList.forEach(material -> {
//    			if(material.getParentId() != -1) {
//    				material.setParentMaterial(selectTMaterialById(material.getParentId()));
//    			}
//    		});
//    	}
    	return materialList;
    }

    /**
     * 新增物料
     * 
     * @param tMaterial 物料
     * @return 结果
     */
    @Override
    public int insertTMaterial(TMaterial tMaterial)
    {
        tMaterial.setCreateTime(DateUtils.getNowDate());
        return tMaterialMapper.insertTMaterial(tMaterial);
    }

    /**
     * 修改物料
     * 
     * @param tMaterial 物料
     * @return 结果
     */
    @Override
    public int updateTMaterial(TMaterial tMaterial)
    {
        return tMaterialMapper.updateTMaterial(tMaterial);
    }

    /**
     * 批量删除物料
     * 
     * @param ids 需要删除的物料主键
     * @return 结果
     */
    @Override
    public int deleteTMaterialByIds(Long[] ids)
    {
        return tMaterialMapper.deleteTMaterialByIds(ids);
    }

    /**
     * 删除物料信息
     * 
     * @param id 物料主键
     * @return 结果
     */
    @Override
    public int deleteTMaterialById(Long id)
    {
        return tMaterialMapper.deleteTMaterialById(id);
    }
}
