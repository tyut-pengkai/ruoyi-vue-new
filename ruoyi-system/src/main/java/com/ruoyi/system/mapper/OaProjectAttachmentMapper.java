package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.OaProjectAttachment;

/**
 * 项目附件Mapper接口
 * 
 * @author ruoyi
 * @date 2025-06-03
 */
public interface OaProjectAttachmentMapper 
{
    /**
     * 查询项目附件
     * 
     * @param id 项目附件主键
     * @return 项目附件
     */
    public OaProjectAttachment selectOaProjectAttachmentById(Long id);

    /**
     * 查询项目附件列表
     * 
     * @param oaProjectAttachment 项目附件
     * @return 项目附件集合
     */
    public List<OaProjectAttachment> selectOaProjectAttachmentList(OaProjectAttachment oaProjectAttachment);

    /**
     * 新增项目附件
     * 
     * @param oaProjectAttachment 项目附件
     * @return 结果
     */
    public int insertOaProjectAttachment(OaProjectAttachment oaProjectAttachment);

    /**
     * 修改项目附件
     * 
     * @param oaProjectAttachment 项目附件
     * @return 结果
     */
    public int updateOaProjectAttachment(OaProjectAttachment oaProjectAttachment);

    /**
     * 删除项目附件
     * 
     * @param id 项目附件主键
     * @return 结果
     */
    public int deleteOaProjectAttachmentById(Long id);

    /**
     * 批量删除项目附件
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteOaProjectAttachmentByIds(Long[] ids);
}
