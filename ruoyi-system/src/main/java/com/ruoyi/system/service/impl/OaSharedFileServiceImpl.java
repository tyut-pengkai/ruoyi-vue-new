package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.OaSharedFileMapper;
import com.ruoyi.system.domain.OaSharedFile;
import com.ruoyi.system.service.IOaSharedFileService;

/**
 * 共享文件Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-06-03
 */
@Service
public class OaSharedFileServiceImpl implements IOaSharedFileService 
{
    @Autowired
    private OaSharedFileMapper oaSharedFileMapper;

    /**
     * 查询共享文件
     * 
     * @param id 共享文件主键
     * @return 共享文件
     */
    @Override
    public OaSharedFile selectOaSharedFileById(Long id)
    {
        return oaSharedFileMapper.selectOaSharedFileById(id);
    }

    /**
     * 查询共享文件列表
     * 
     * @param oaSharedFile 共享文件
     * @return 共享文件
     */
    @Override
    public List<OaSharedFile> selectOaSharedFileList(OaSharedFile oaSharedFile)
    {
        return oaSharedFileMapper.selectOaSharedFileList(oaSharedFile);
    }

    /**
     * 新增共享文件
     * 
     * @param oaSharedFile 共享文件
     * @return 结果
     */
    @Override
    public int insertOaSharedFile(OaSharedFile oaSharedFile)
    {
        return oaSharedFileMapper.insertOaSharedFile(oaSharedFile);
    }

    /**
     * 修改共享文件
     * 
     * @param oaSharedFile 共享文件
     * @return 结果
     */
    @Override
    public int updateOaSharedFile(OaSharedFile oaSharedFile)
    {
        return oaSharedFileMapper.updateOaSharedFile(oaSharedFile);
    }

    /**
     * 批量删除共享文件
     * 
     * @param ids 需要删除的共享文件主键
     * @return 结果
     */
    @Override
    public int deleteOaSharedFileByIds(Long[] ids)
    {
        return oaSharedFileMapper.deleteOaSharedFileByIds(ids);
    }

    /**
     * 删除共享文件信息
     * 
     * @param id 共享文件主键
     * @return 结果
     */
    @Override
    public int deleteOaSharedFileById(Long id)
    {
        return oaSharedFileMapper.deleteOaSharedFileById(id);
    }
}
