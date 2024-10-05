package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.BjWorkTypeMapper;
import com.ruoyi.system.domain.BjWorkType;
import com.ruoyi.system.service.IBjWorkTypeService;

/**
 * 工种管理Service业务层处理
 * 
 * @author ssq
 * @date 2024-10-05
 */
@Service
public class BjWorkTypeServiceImpl implements IBjWorkTypeService 
{
    @Autowired
    private BjWorkTypeMapper bjWorkTypeMapper;

    /**
     * 查询工种管理
     * 
     * @param id 工种管理主键
     * @return 工种管理
     */
    @Override
    public BjWorkType selectBjWorkTypeById(Long id)
    {
        return bjWorkTypeMapper.selectBjWorkTypeById(id);
    }

    /**
     * 查询工种管理列表
     * 
     * @param bjWorkType 工种管理
     * @return 工种管理
     */
    @Override
    public List<BjWorkType> selectBjWorkTypeList(BjWorkType bjWorkType)
    {
        return bjWorkTypeMapper.selectBjWorkTypeList(bjWorkType);
    }

    /**
     * 新增工种管理
     * 
     * @param bjWorkType 工种管理
     * @return 结果
     */
    @Override
    public int insertBjWorkType(BjWorkType bjWorkType)
    {
        return bjWorkTypeMapper.insertBjWorkType(bjWorkType);
    }

    /**
     * 修改工种管理
     * 
     * @param bjWorkType 工种管理
     * @return 结果
     */
    @Override
    public int updateBjWorkType(BjWorkType bjWorkType)
    {
        return bjWorkTypeMapper.updateBjWorkType(bjWorkType);
    }

    /**
     * 批量删除工种管理
     * 
     * @param ids 需要删除的工种管理主键
     * @return 结果
     */
    @Override
    public int deleteBjWorkTypeByIds(Long[] ids)
    {
        return bjWorkTypeMapper.deleteBjWorkTypeByIds(ids);
    }

    /**
     * 删除工种管理信息
     * 
     * @param id 工种管理主键
     * @return 结果
     */
    @Override
    public int deleteBjWorkTypeById(Long id)
    {
        return bjWorkTypeMapper.deleteBjWorkTypeById(id);
    }
}
