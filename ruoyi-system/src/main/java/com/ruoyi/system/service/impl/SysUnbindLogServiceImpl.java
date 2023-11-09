package com.ruoyi.system.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.SysUnbindLog;
import com.ruoyi.system.mapper.SysUnbindLogMapper;
import com.ruoyi.system.service.ISysUnbindLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 解绑日志Service业务层处理
 *
 * @author zwgu
 * @date 2023-11-08
 */
@Service
public class SysUnbindLogServiceImpl implements ISysUnbindLogService
{
    @Autowired
    private SysUnbindLogMapper sysUnbindLogMapper;

    /**
     * 查询解绑日志
     *
     * @param id 解绑日志主键
     * @return 解绑日志
     */
    @Override
    public SysUnbindLog selectSysUnbindLogById(Long id)
    {
        return sysUnbindLogMapper.selectSysUnbindLogById(id);
    }

    /**
     * 查询解绑日志列表
     *
     * @param sysUnbindLog 解绑日志
     * @return 解绑日志
     */
    @Override
    public List<SysUnbindLog> selectSysUnbindLogList(SysUnbindLog sysUnbindLog)
    {
        return sysUnbindLogMapper.selectSysUnbindLogList(sysUnbindLog);
    }

    /**
     * 新增解绑日志
     *
     * @param sysUnbindLog 解绑日志
     * @return 结果
     */
    @Override
    public int insertSysUnbindLog(SysUnbindLog sysUnbindLog)
    {
        sysUnbindLog.setCreateTime(DateUtils.getNowDate());
        sysUnbindLog.setCreateBy(SecurityUtils.getUsernameNoException());
        return sysUnbindLogMapper.insertSysUnbindLog(sysUnbindLog);
    }

    /**
     * 修改解绑日志
     *
     * @param sysUnbindLog 解绑日志
     * @return 结果
     */
    @Override
    public int updateSysUnbindLog(SysUnbindLog sysUnbindLog)
    {
        sysUnbindLog.setUpdateTime(DateUtils.getNowDate());
        sysUnbindLog.setUpdateBy(SecurityUtils.getUsernameNoException());
        return sysUnbindLogMapper.updateSysUnbindLog(sysUnbindLog);
    }

    /**
     * 批量删除解绑日志
     *
     * @param ids 需要删除的解绑日志主键
     * @return 结果
     */
    @Override
    public int deleteSysUnbindLogByIds(Long[] ids)
    {
        return sysUnbindLogMapper.deleteSysUnbindLogByIds(ids);
    }

    /**
     * 删除解绑日志信息
     *
     * @param id 解绑日志主键
     * @return 结果
     */
    @Override
    public int deleteSysUnbindLogById(Long id)
    {
        return sysUnbindLogMapper.deleteSysUnbindLogById(id);
    }

    /**
     * 清空日志
     */
    @Override
    public void cleanUnbindLog() {
        sysUnbindLogMapper.cleanUnbindLog();
    }
}
