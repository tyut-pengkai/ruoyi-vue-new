package com.ruoyi.system.service;

import com.ruoyi.system.domain.SysGlobalFile;

import java.util.List;

/**
 * 远程文件Service接口
 *
 * @author zwgu
 * @date 2022-09-30
 */
public interface ISysGlobalFileService {
    /**
     * 查询远程文件
     *
     * @param id 远程文件主键
     * @return 远程文件
     */
    public SysGlobalFile selectSysGlobalFileById(Long id);

    /**
     * 查询全局变量
     *
     * @param name 全局变量主键
     * @return 全局变量
     */
    public SysGlobalFile selectSysGlobalFileByName(String name);

    /**
     * 查询远程文件列表
     *
     * @param sysGlobalFile 远程文件
     * @return 远程文件集合
     */
    public List<SysGlobalFile> selectSysGlobalFileList(SysGlobalFile sysGlobalFile);

    /**
     * 新增远程文件
     *
     * @param sysGlobalFile 远程文件
     * @return 结果
     */
    public int insertSysGlobalFile(SysGlobalFile sysGlobalFile);

    /**
     * 修改远程文件
     *
     * @param sysGlobalFile 远程文件
     * @return 结果
     */
    public int updateSysGlobalFile(SysGlobalFile sysGlobalFile);

    /**
     * 批量删除远程文件
     *
     * @param ids 需要删除的远程文件主键集合
     * @return 结果
     */
    public int deleteSysGlobalFileByIds(Long[] ids);

    /**
     * 删除远程文件信息
     *
     * @param id 远程文件主键
     * @return 结果
     */
    public int deleteSysGlobalFileById(Long id);
}
