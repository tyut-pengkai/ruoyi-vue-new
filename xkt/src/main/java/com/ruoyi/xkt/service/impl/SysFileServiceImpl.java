package com.ruoyi.xkt.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.xkt.domain.SysFile;
import com.ruoyi.xkt.mapper.SysFileMapper;
import com.ruoyi.xkt.service.ISysFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * fileService业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
public class SysFileServiceImpl implements ISysFileService {
    @Autowired
    private SysFileMapper sysFileMapper;

    /**
     * 查询file
     *
     * @param fileId file主键
     * @return file
     */
    @Override
    public SysFile selectSysFileByFileId(Long fileId) {
        return sysFileMapper.selectSysFileByFileId(fileId);
    }

    /**
     * 查询file列表
     *
     * @param sysFile file
     * @return file
     */
    @Override
    public List<SysFile> selectSysFileList(SysFile sysFile) {
        return sysFileMapper.selectSysFileList(sysFile);
    }

    /**
     * 新增file
     *
     * @param sysFile file
     * @return 结果
     */
    @Override
    public int insertSysFile(SysFile sysFile) {
        sysFile.setCreateTime(DateUtils.getNowDate());
        return sysFileMapper.insertSysFile(sysFile);
    }

    /**
     * 修改file
     *
     * @param sysFile file
     * @return 结果
     */
    @Override
    public int updateSysFile(SysFile sysFile) {
        sysFile.setUpdateTime(DateUtils.getNowDate());
        return sysFileMapper.updateSysFile(sysFile);
    }

    /**
     * 批量删除file
     *
     * @param fileIds 需要删除的file主键
     * @return 结果
     */
    @Override
    public int deleteSysFileByFileIds(Long[] fileIds) {
        return sysFileMapper.deleteSysFileByFileIds(fileIds);
    }

    /**
     * 删除file信息
     *
     * @param fileId file主键
     * @return 结果
     */
    @Override
    public int deleteSysFileByFileId(Long fileId) {
        return sysFileMapper.deleteSysFileByFileId(fileId);
    }
}
