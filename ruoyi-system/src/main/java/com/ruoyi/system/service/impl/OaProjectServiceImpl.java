package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.OaProjectMapper;
import com.ruoyi.system.domain.OaProject;
import com.ruoyi.system.service.IOaProjectService;

/**
 * 项目管理主Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-06-03
 */
@Service
public class OaProjectServiceImpl implements IOaProjectService 
{
    @Autowired
    private OaProjectMapper oaProjectMapper;

    /**
     * 查询项目管理主
     * 
     * @param id 项目管理主主键
     * @return 项目管理主
     */
    @Override
    public OaProject selectOaProjectById(Long id)
    {
        return oaProjectMapper.selectOaProjectById(id);
    }

    /**
     * 查询项目管理主列表
     * 
     * @param oaProject 项目管理主
     * @return 项目管理主
     */
    @Override
    public List<OaProject> selectOaProjectList(OaProject oaProject)
    {
        return oaProjectMapper.selectOaProjectList(oaProject);
    }

    /**
     * 新增项目管理主
     * 
     * @param oaProject 项目管理主
     * @return 结果
     */
    @Override
    public int insertOaProject(OaProject oaProject)
    {
        return oaProjectMapper.insertOaProject(oaProject);
    }

    /**
     * 修改项目管理主
     * 
     * @param oaProject 项目管理主
     * @return 结果
     */
    @Override
    public int updateOaProject(OaProject oaProject)
    {
        return oaProjectMapper.updateOaProject(oaProject);
    }

    /**
     * 批量删除项目管理主
     * 
     * @param ids 需要删除的项目管理主主键
     * @return 结果
     */
    @Override
    public int deleteOaProjectByIds(Long[] ids)
    {
        return oaProjectMapper.deleteOaProjectByIds(ids);
    }

    /**
     * 删除项目管理主信息
     * 
     * @param id 项目管理主主键
     * @return 结果
     */
    @Override
    public int deleteOaProjectById(Long id)
    {
        return oaProjectMapper.deleteOaProjectById(id);
    }
}
