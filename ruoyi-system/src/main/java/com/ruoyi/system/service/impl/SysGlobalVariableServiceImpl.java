package com.ruoyi.system.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.SysGlobalVariable;
import com.ruoyi.system.mapper.SysGlobalVariableMapper;
import com.ruoyi.system.service.ISysGlobalVariableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 全局变量Service业务层处理
 *
 * @author zwgu
 * @date 2022-07-02
 */
@Service
public class SysGlobalVariableServiceImpl implements ISysGlobalVariableService {
    @Autowired
    private SysGlobalVariableMapper sysGlobalVariableMapper;

    /**
     * 查询全局变量
     *
     * @param id 全局变量主键
     * @return 全局变量
     */
    @Override
    public SysGlobalVariable selectSysGlobalVariableById(Long id) {
        return sysGlobalVariableMapper.selectSysGlobalVariableById(id);
    }

    /**
     * 查询全局变量
     *
     * @param name 全局变量名称
     * @return 全局变量
     */
    @Override
    public SysGlobalVariable selectSysGlobalVariableByName(String name) {
        return sysGlobalVariableMapper.selectSysGlobalVariableByName(name);
    }

    /**
     * 查询全局变量列表
     *
     * @param sysGlobalVariable 全局变量
     * @return 全局变量
     */
    @Override
    public List<SysGlobalVariable> selectSysGlobalVariableList(SysGlobalVariable sysGlobalVariable) {
        return sysGlobalVariableMapper.selectSysGlobalVariableList(sysGlobalVariable);
    }

    /**
     * 新增全局变量
     *
     * @param sysGlobalVariable 全局变量
     * @return 结果
     */
    @Override
    public int insertSysGlobalVariable(SysGlobalVariable sysGlobalVariable) {
        sysGlobalVariable.setCreateTime(DateUtils.getNowDate());
        sysGlobalVariable.setCreateBy(SecurityUtils.getUsernameNoException());
        return sysGlobalVariableMapper.insertSysGlobalVariable(sysGlobalVariable);
    }

    /**
     * 修改全局变量
     *
     * @param sysGlobalVariable 全局变量
     * @return 结果
     */
    @Override
    public int updateSysGlobalVariable(SysGlobalVariable sysGlobalVariable) {
        sysGlobalVariable.setUpdateTime(DateUtils.getNowDate());
        sysGlobalVariable.setUpdateBy(SecurityUtils.getUsernameNoException());
        return sysGlobalVariableMapper.updateSysGlobalVariable(sysGlobalVariable);
    }

    /**
     * 批量删除全局变量
     *
     * @param ids 需要删除的全局变量主键
     * @return 结果
     */
    @Override
    public int deleteSysGlobalVariableByIds(Long[] ids) {
        return sysGlobalVariableMapper.deleteSysGlobalVariableByIds(ids);
    }

    /**
     * 删除全局变量信息
     *
     * @param id 全局变量主键
     * @return 结果
     */
    @Override
    public int deleteSysGlobalVariableById(Long id) {
        return sysGlobalVariableMapper.deleteSysGlobalVariableById(id);
    }
}
