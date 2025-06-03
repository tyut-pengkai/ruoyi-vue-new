package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.OaProjectStage;

/**
 * 项目阶段Service接口
 * 
 * @author ruoyi
 * @date 2025-06-03
 */
public interface IOaProjectStageService 
{
    /**
     * 查询项目阶段
     * 
     * @param id 项目阶段主键
     * @return 项目阶段
     */
    public OaProjectStage selectOaProjectStageById(Long id);

    /**
     * 查询项目阶段列表
     * 
     * @param oaProjectStage 项目阶段
     * @return 项目阶段集合
     */
    public List<OaProjectStage> selectOaProjectStageList(OaProjectStage oaProjectStage);

    /**
     * 新增项目阶段
     * 
     * @param oaProjectStage 项目阶段
     * @return 结果
     */
    public int insertOaProjectStage(OaProjectStage oaProjectStage);

    /**
     * 修改项目阶段
     * 
     * @param oaProjectStage 项目阶段
     * @return 结果
     */
    public int updateOaProjectStage(OaProjectStage oaProjectStage);

    /**
     * 批量删除项目阶段
     * 
     * @param ids 需要删除的项目阶段主键集合
     * @return 结果
     */
    public int deleteOaProjectStageByIds(Long[] ids);

    /**
     * 删除项目阶段信息
     * 
     * @param id 项目阶段主键
     * @return 结果
     */
    public int deleteOaProjectStageById(Long id);
}
