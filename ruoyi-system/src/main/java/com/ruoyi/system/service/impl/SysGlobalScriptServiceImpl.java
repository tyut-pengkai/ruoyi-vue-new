package com.ruoyi.system.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.SysGlobalScript;
import com.ruoyi.system.mapper.SysGlobalScriptMapper;
import com.ruoyi.system.service.ISysGlobalScriptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 全局脚本Service业务层处理
 *
 * @author zwgu
 * @date 2022-05-19
 */
@Service
public class SysGlobalScriptServiceImpl implements ISysGlobalScriptService {
    @Autowired
    private SysGlobalScriptMapper sysGlobalScriptMapper;

    /**
     * 查询全局脚本
     *
     * @param scriptId 全局脚本主键
     * @return 全局脚本
     */
    @Override
    public SysGlobalScript selectSysGlobalScriptByScriptId(Long scriptId) {
        return sysGlobalScriptMapper.selectSysGlobalScriptByScriptId(scriptId);
    }

    /**
     * 查询全局脚本
     *
     * @param scriptName 全局脚本主键
     * @return 全局脚本
     */
    @Override
    public SysGlobalScript selectSysGlobalScriptByScriptName(String scriptName) {
        return sysGlobalScriptMapper.selectSysGlobalScriptByScriptName(scriptName);
    }

    /**
     * 查询全局脚本
     *
     * @param scriptKey 全局脚本主键
     * @return 全局脚本
     */
    @Override
    public SysGlobalScript selectSysGlobalScriptByScriptKey(String scriptKey) {
        return sysGlobalScriptMapper.selectSysGlobalScriptByScriptKey(scriptKey);
    }

    /**
     * 查询全局脚本列表
     *
     * @param sysGlobalScript 全局脚本
     * @return 全局脚本
     */
    @Override
    public List<SysGlobalScript> selectSysGlobalScriptList(SysGlobalScript sysGlobalScript) {
        return sysGlobalScriptMapper.selectSysGlobalScriptList(sysGlobalScript);
    }

    /**
     * 新增全局脚本
     *
     * @param sysGlobalScript 全局脚本
     * @return 结果
     */
    @Override
    public int insertSysGlobalScript(SysGlobalScript sysGlobalScript) {
        sysGlobalScript.setCreateTime(DateUtils.getNowDate());
        sysGlobalScript.setCreateBy(SecurityUtils.getUsernameNoException());
        return sysGlobalScriptMapper.insertSysGlobalScript(sysGlobalScript);
    }

    /**
     * 修改全局脚本
     *
     * @param sysGlobalScript 全局脚本
     * @return 结果
     */
    @Override
    public int updateSysGlobalScript(SysGlobalScript sysGlobalScript) {
        sysGlobalScript.setUpdateTime(DateUtils.getNowDate());
        sysGlobalScript.setUpdateBy(SecurityUtils.getUsernameNoException());
        return sysGlobalScriptMapper.updateSysGlobalScript(sysGlobalScript);
    }

    /**
     * 批量删除全局脚本
     *
     * @param scriptIds 需要删除的全局脚本主键
     * @return 结果
     */
    @Override
    public int deleteSysGlobalScriptByScriptIds(Long[] scriptIds) {
        return sysGlobalScriptMapper.deleteSysGlobalScriptByScriptIds(scriptIds);
    }

    /**
     * 删除全局脚本信息
     *
     * @param scriptId 全局脚本主键
     * @return 结果
     */
    @Override
    public int deleteSysGlobalScriptByScriptId(Long scriptId) {
        return sysGlobalScriptMapper.deleteSysGlobalScriptByScriptId(scriptId);
    }
}
