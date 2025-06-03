package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.OaProjectAttachmentMapper;
import com.ruoyi.system.domain.OaProjectAttachment;
import com.ruoyi.system.service.IOaProjectAttachmentService;

/**
 * 项目附件Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-06-03
 */
@Service
public class OaProjectAttachmentServiceImpl implements IOaProjectAttachmentService 
{
    @Autowired
    private OaProjectAttachmentMapper oaProjectAttachmentMapper;

    /**
     * 查询项目附件
     * 
     * @param id 项目附件主键
     * @return 项目附件
     */
    @Override
    public OaProjectAttachment selectOaProjectAttachmentById(Long id)
    {
        return oaProjectAttachmentMapper.selectOaProjectAttachmentById(id);
    }

    /**
     * 查询项目附件列表
     * 
     * @param oaProjectAttachment 项目附件
     * @return 项目附件
     */
    @Override
    public List<OaProjectAttachment> selectOaProjectAttachmentList(OaProjectAttachment oaProjectAttachment)
    {
        return oaProjectAttachmentMapper.selectOaProjectAttachmentList(oaProjectAttachment);
    }

    /**
     * 新增项目附件
     * 
     * @param oaProjectAttachment 项目附件
     * @return 结果
     */
    @Override
    public int insertOaProjectAttachment(OaProjectAttachment oaProjectAttachment)
    {
        return oaProjectAttachmentMapper.insertOaProjectAttachment(oaProjectAttachment);
    }

    /**
     * 修改项目附件
     * 
     * @param oaProjectAttachment 项目附件
     * @return 结果
     */
    @Override
    public int updateOaProjectAttachment(OaProjectAttachment oaProjectAttachment)
    {
        return oaProjectAttachmentMapper.updateOaProjectAttachment(oaProjectAttachment);
    }

    /**
     * 批量删除项目附件
     * 
     * @param ids 需要删除的项目附件主键
     * @return 结果
     */
    @Override
    public int deleteOaProjectAttachmentByIds(Long[] ids)
    {
        return oaProjectAttachmentMapper.deleteOaProjectAttachmentByIds(ids);
    }

    /**
     * 删除项目附件信息
     * 
     * @param id 项目附件主键
     * @return 结果
     */
    @Override
    public int deleteOaProjectAttachmentById(Long id)
    {
        return oaProjectAttachmentMapper.deleteOaProjectAttachmentById(id);
    }
}
