package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.FileInfoMapper;
import com.ruoyi.system.domain.FileInfo;
import com.ruoyi.system.service.IFileInfoService;

/**
 * 文件Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-06-01
 */
@Service
public class FileInfoServiceImpl implements IFileInfoService 
{
    @Autowired
    private FileInfoMapper fileInfoMapper;

    /**
     * 查询文件
     * 
     * @param id 文件主键
     * @return 文件
     */
    @Override
    public FileInfo selectFileInfoById(Long id)
    {
        return fileInfoMapper.selectFileInfoById(id);
    }

    /**
     * 查询文件列表
     * 
     * @param fileInfo 文件
     * @return 文件
     */
    @Override
    public List<FileInfo> selectFileInfoList(FileInfo fileInfo)
    {
        return fileInfoMapper.selectFileInfoList(fileInfo);
    }

    /**
     * 新增文件
     * 
     * @param fileInfo 文件
     * @return 结果
     */
    @Override
    public int insertFileInfo(FileInfo fileInfo)
    {
        fileInfo.setCreateTime(DateUtils.getNowDate());
        return fileInfoMapper.insertFileInfo(fileInfo);
    }

    /**
     * 修改文件
     * 
     * @param fileInfo 文件
     * @return 结果
     */
    @Override
    public int updateFileInfo(FileInfo fileInfo)
    {
        fileInfo.setUpdateTime(DateUtils.getNowDate());
        return fileInfoMapper.updateFileInfo(fileInfo);
    }

    /**
     * 批量删除文件
     * 
     * @param ids 需要删除的文件主键
     * @return 结果
     */
    @Override
    public int deleteFileInfoByIds(Long[] ids)
    {
        return fileInfoMapper.deleteFileInfoByIds(ids);
    }

    /**
     * 删除文件信息
     * 
     * @param id 文件主键
     * @return 结果
     */
    @Override
    public int deleteFileInfoById(Long id)
    {
        return fileInfoMapper.deleteFileInfoById(id);
    }
}
