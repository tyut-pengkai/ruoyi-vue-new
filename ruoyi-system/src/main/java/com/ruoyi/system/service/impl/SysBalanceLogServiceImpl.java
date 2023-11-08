package com.ruoyi.system.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.SysBalanceLog;
import com.ruoyi.system.mapper.SysBalanceLogMapper;
import com.ruoyi.system.service.ISysBalanceLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 余额变动Service业务层处理
 *
 * @author zwgu
 * @date 2022-06-16
 */
@Service
public class SysBalanceLogServiceImpl implements ISysBalanceLogService {
    @Autowired
    private SysBalanceLogMapper sysBalanceLogMapper;

    /**
     * 查询余额变动
     *
     * @param id 余额变动主键
     * @return 余额变动
     */
    @Override
    public SysBalanceLog selectSysBalanceLogById(Long id) {
        return sysBalanceLogMapper.selectSysBalanceLogById(id);
    }

    /**
     * 查询余额变动列表
     *
     * @param sysBalanceLog 余额变动
     * @return 余额变动
     */
    @Override
    public List<SysBalanceLog> selectSysBalanceLogList(SysBalanceLog sysBalanceLog) {
        return sysBalanceLogMapper.selectSysBalanceLogList(sysBalanceLog);
    }

    /**
     * 新增余额变动
     *
     * @param sysBalanceLog 余额变动
     * @return 结果
     */
    @Override
    public int insertSysBalanceLog(SysBalanceLog sysBalanceLog) {
        sysBalanceLog.setCreateTime(DateUtils.getNowDate());
        sysBalanceLog.setCreateBy(SecurityUtils.getUsernameNoException());
        return sysBalanceLogMapper.insertSysBalanceLog(sysBalanceLog);
    }

    /**
     * 修改余额变动
     *
     * @param sysBalanceLog 余额变动
     * @return 结果
     */
    @Override
    public int updateSysBalanceLog(SysBalanceLog sysBalanceLog) {
        sysBalanceLog.setUpdateTime(DateUtils.getNowDate());
        sysBalanceLog.setUpdateBy(SecurityUtils.getUsernameNoException());
        return sysBalanceLogMapper.updateSysBalanceLog(sysBalanceLog);
    }

    /**
     * 批量删除余额变动
     *
     * @param ids 需要删除的余额变动主键
     * @return 结果
     */
    @Override
    public int deleteSysBalanceLogByIds(Long[] ids) {
        return sysBalanceLogMapper.deleteSysBalanceLogByIds(ids);
    }

    /**
     * 删除余额变动信息
     *
     * @param id 余额变动主键
     * @return 结果
     */
    @Override
    public int deleteSysBalanceLogById(Long id) {
        return sysBalanceLogMapper.deleteSysBalanceLogById(id);
    }

    /**
     * 清空日志
     */
    @Override
    public void cleanBalanceLog() {
        sysBalanceLogMapper.cleanBalanceLog();
    }
}
