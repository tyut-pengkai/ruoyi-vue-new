package com.ruoyi.xkt.service;

import com.ruoyi.xkt.domain.SysFile;

import java.util.List;

/**
 * fileService接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface ISysFileService {
    /**
     * 查询file
     *
     * @param fileId file主键
     * @return file
     */
    public SysFile selectSysFileByFileId(Long fileId);

    /**
     * 查询file列表
     *
     * @param sysFile file
     * @return file集合
     */
    public List<SysFile> selectSysFileList(SysFile sysFile);

    /**
     * 新增file
     *
     * @param sysFile file
     * @return 结果
     */
    public int insertSysFile(SysFile sysFile);

    /**
     * 修改file
     *
     * @param sysFile file
     * @return 结果
     */
    public int updateSysFile(SysFile sysFile);

    /**
     * 批量删除file
     *
     * @param fileIds 需要删除的file主键集合
     * @return 结果
     */
    public int deleteSysFileByFileIds(Long[] fileIds);

    /**
     * 删除file信息
     *
     * @param fileId file主键
     * @return 结果
     */
    public int deleteSysFileByFileId(Long fileId);
}
