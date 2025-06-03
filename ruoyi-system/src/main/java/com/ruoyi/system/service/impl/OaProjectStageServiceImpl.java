package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.OaProjectStageMapper;
import com.ruoyi.system.domain.OaProjectStage;
import com.ruoyi.system.service.IOaProjectStageService;

/**
 * 项目阶段Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-06-03
 */
@Service
public class OaProjectStageServiceImpl implements IOaProjectStageService 
{
    @Autowired
    private OaProjectStageMapper oaProjectStageMapper;

    /**
     * 查询项目阶段
     * 
     * @param id 项目阶段主键
     * @return 项目阶段
     */
    @Override
    public OaProjectStage selectOaProjectStageById(Long id)
    {
        return oaProjectStageMapper.selectOaProjectStageById(id);
    }

    /**
     * 查询项目阶段列表
     * 
     * @param oaProjectStage 项目阶段
     * @return 项目阶段
     */
    @Override
    public List<OaProjectStage> selectOaProjectStageList(OaProjectStage oaProjectStage)
    {
        return oaProjectStageMapper.selectOaProjectStageList(oaProjectStage);
    }

    /**
     * 新增项目阶段
     * 
     * @param oaProjectStage 项目阶段
     * @return 结果
     */
    @Override
    public int insertOaProjectStage(OaProjectStage oaProjectStage)
    {
        return oaProjectStageMapper.insertOaProjectStage(oaProjectStage);
    }

    /**
     * 修改项目阶段
     * 
     * @param oaProjectStage 项目阶段
     * @return 结果
     */
    @Override
    public int updateOaProjectStage(OaProjectStage oaProjectStage)
    {
        return oaProjectStageMapper.updateOaProjectStage(oaProjectStage);
    }

    /**
     * 批量删除项目阶段
     * 
     * @param ids 需要删除的项目阶段主键
     * @return 结果
     */
    @Override
    public int deleteOaProjectStageByIds(Long[] ids)
    {
        return oaProjectStageMapper.deleteOaProjectStageByIds(ids);
    }

    /**
     * 删除项目阶段信息
     * 
     * @param id 项目阶段主键
     * @return 结果
     */
    @Override
    public int deleteOaProjectStageById(Long id)
    {
        return oaProjectStageMapper.deleteOaProjectStageById(id);
    }
}
