package com.ruoyi.system.service;

import com.ruoyi.system.domain.SysGlobalVariable;

import java.util.List;

/**
 * 全局变量Service接口
 *
 * @author zwgu
 * @date 2022-07-02
 */
public interface ISysGlobalVariableService {
    /**
     * 查询全局变量
     *
     * @param id 全局变量主键
     * @return 全局变量
     */
    public SysGlobalVariable selectSysGlobalVariableById(Long id);

    /**
     * 查询全局变量
     *
     * @param name 全局变量名称
     * @return 全局变量
     */
    public SysGlobalVariable selectSysGlobalVariableByName(String name);

    /**
     * 查询全局变量列表
     *
     * @param sysGlobalVariable 全局变量
     * @return 全局变量集合
     */
    public List<SysGlobalVariable> selectSysGlobalVariableList(SysGlobalVariable sysGlobalVariable);

    /**
     * 新增全局变量
     *
     * @param sysGlobalVariable 全局变量
     * @return 结果
     */
    public int insertSysGlobalVariable(SysGlobalVariable sysGlobalVariable);

    /**
     * 修改全局变量
     *
     * @param sysGlobalVariable 全局变量
     * @return 结果
     */
    public int updateSysGlobalVariable(SysGlobalVariable sysGlobalVariable);

    /**
     * 批量删除全局变量
     *
     * @param ids 需要删除的全局变量主键集合
     * @return 结果
     */
    public int deleteSysGlobalVariableByIds(Long[] ids);

    /**
     * 删除全局变量信息
     *
     * @param id 全局变量主键
     * @return 结果
     */
    public int deleteSysGlobalVariableById(Long id);
}
