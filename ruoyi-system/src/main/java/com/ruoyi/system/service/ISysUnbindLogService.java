package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysUnbindLog;

/**
 * 解绑日志Service接口
 * 
 * @author zwgu
 * @date 2023-11-08
 */
public interface ISysUnbindLogService 
{
    /**
     * 查询解绑日志
     * 
     * @param id 解绑日志主键
     * @return 解绑日志
     */
    public SysUnbindLog selectSysUnbindLogById(Long id);

    /**
     * 查询解绑日志列表
     * 
     * @param sysUnbindLog 解绑日志
     * @return 解绑日志集合
     */
    public List<SysUnbindLog> selectSysUnbindLogList(SysUnbindLog sysUnbindLog);

    /**
     * 新增解绑日志
     * 
     * @param sysUnbindLog 解绑日志
     * @return 结果
     */
    public int insertSysUnbindLog(SysUnbindLog sysUnbindLog);

    /**
     * 修改解绑日志
     * 
     * @param sysUnbindLog 解绑日志
     * @return 结果
     */
    public int updateSysUnbindLog(SysUnbindLog sysUnbindLog);

    /**
     * 批量删除解绑日志
     * 
     * @param ids 需要删除的解绑日志主键集合
     * @return 结果
     */
    public int deleteSysUnbindLogByIds(Long[] ids);

    /**
     * 删除解绑日志信息
     * 
     * @param id 解绑日志主键
     * @return 结果
     */
    public int deleteSysUnbindLogById(Long id);
}
