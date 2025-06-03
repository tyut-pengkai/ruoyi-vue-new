package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.OaProject;

/**
 * 项目管理主Service接口
 * 
 * @author ruoyi
 * @date 2025-06-03
 */
public interface IOaProjectService 
{
    /**
     * 查询项目管理主
     * 
     * @param id 项目管理主主键
     * @return 项目管理主
     */
    public OaProject selectOaProjectById(Long id);

    /**
     * 查询项目管理主列表
     * 
     * @param oaProject 项目管理主
     * @return 项目管理主集合
     */
    public List<OaProject> selectOaProjectList(OaProject oaProject);

    /**
     * 新增项目管理主
     * 
     * @param oaProject 项目管理主
     * @return 结果
     */
    public int insertOaProject(OaProject oaProject);

    /**
     * 修改项目管理主
     * 
     * @param oaProject 项目管理主
     * @return 结果
     */
    public int updateOaProject(OaProject oaProject);

    /**
     * 批量删除项目管理主
     * 
     * @param ids 需要删除的项目管理主主键集合
     * @return 结果
     */
    public int deleteOaProjectByIds(Long[] ids);

    /**
     * 删除项目管理主信息
     * 
     * @param id 项目管理主主键
     * @return 结果
     */
    public int deleteOaProjectById(Long id);
}
