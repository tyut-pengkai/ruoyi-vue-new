package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.OaSharedFile;

/**
 * 共享文件Service接口
 * 
 * @author ruoyi
 * @date 2025-06-03
 */
public interface IOaSharedFileService 
{
    /**
     * 查询共享文件
     * 
     * @param id 共享文件主键
     * @return 共享文件
     */
    public OaSharedFile selectOaSharedFileById(Long id);

    /**
     * 查询共享文件列表
     * 
     * @param oaSharedFile 共享文件
     * @return 共享文件集合
     */
    public List<OaSharedFile> selectOaSharedFileList(OaSharedFile oaSharedFile);

    /**
     * 新增共享文件
     * 
     * @param oaSharedFile 共享文件
     * @return 结果
     */
    public int insertOaSharedFile(OaSharedFile oaSharedFile);

    /**
     * 修改共享文件
     * 
     * @param oaSharedFile 共享文件
     * @return 结果
     */
    public int updateOaSharedFile(OaSharedFile oaSharedFile);

    /**
     * 批量删除共享文件
     * 
     * @param ids 需要删除的共享文件主键集合
     * @return 结果
     */
    public int deleteOaSharedFileByIds(Long[] ids);

    /**
     * 删除共享文件信息
     * 
     * @param id 共享文件主键
     * @return 结果
     */
    public int deleteOaSharedFileById(Long id);
}
