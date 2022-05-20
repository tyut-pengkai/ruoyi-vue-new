package com.ruoyi.system.service;

import com.ruoyi.system.domain.SysGlobalScript;

import java.util.List;

/**
 * 全局脚本Service接口
 *
 * @author zwgu
 * @date 2022-05-19
 */
public interface ISysGlobalScriptService {
    /**
     * 查询全局脚本
     *
     * @param scriptId 全局脚本主键
     * @return 全局脚本
     */
    public SysGlobalScript selectSysGlobalScriptByScriptId(Long scriptId);

    /**
     * 查询全局脚本
     *
     * @param scriptName 全局脚本主键
     * @return 全局脚本
     */
    public SysGlobalScript selectSysGlobalScriptByScriptName(String scriptName);

    /**
     * 查询全局脚本
     *
     * @param scriptKey 全局脚本主键
     * @return 全局脚本
     */
    public SysGlobalScript selectSysGlobalScriptByScriptKey(String scriptKey);

    /**
     * 查询全局脚本列表
     *
     * @param sysGlobalScript 全局脚本
     * @return 全局脚本集合
     */
    public List<SysGlobalScript> selectSysGlobalScriptList(SysGlobalScript sysGlobalScript);

    /**
     * 新增全局脚本
     *
     * @param sysGlobalScript 全局脚本
     * @return 结果
     */
    public int insertSysGlobalScript(SysGlobalScript sysGlobalScript);

    /**
     * 修改全局脚本
     *
     * @param sysGlobalScript 全局脚本
     * @return 结果
     */
    public int updateSysGlobalScript(SysGlobalScript sysGlobalScript);

    /**
     * 批量删除全局脚本
     *
     * @param scriptIds 需要删除的全局脚本主键集合
     * @return 结果
     */
    public int deleteSysGlobalScriptByScriptIds(Long[] scriptIds);

    /**
     * 删除全局脚本信息
     *
     * @param scriptId 全局脚本主键
     * @return 结果
     */
    public int deleteSysGlobalScriptByScriptId(Long scriptId);
}
