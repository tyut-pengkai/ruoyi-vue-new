package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.SysFile;

import java.util.List;

/**
 * fileMapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface SysFileMapper extends BaseMapper<SysFile> {
    /**
     * 查询file
     *
     * @param id file主键
     * @return file
     */
    public SysFile selectSysFileByFileId(Long id);

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
     * 删除file
     *
     * @param id file主键
     * @return 结果
     */
    public int deleteSysFileByFileId(Long id);

    /**
     * 批量删除file
     *
     * @param fileIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysFileByFileIds(Long[] fileIds);
}
