package com.ruoyi.system.service;

import com.ruoyi.system.domain.SysBalanceLog;

import java.util.List;

/**
 * 余额变动Service接口
 *
 * @author zwgu
 * @date 2022-06-16
 */
public interface ISysBalanceLogService {
    /**
     * 查询余额变动
     *
     * @param id 余额变动主键
     * @return 余额变动
     */
    public SysBalanceLog selectSysBalanceLogById(Long id);

    /**
     * 查询余额变动列表
     *
     * @param sysBalanceLog 余额变动
     * @return 余额变动集合
     */
    public List<SysBalanceLog> selectSysBalanceLogList(SysBalanceLog sysBalanceLog);

    /**
     * 新增余额变动
     *
     * @param sysBalanceLog 余额变动
     * @return 结果
     */
    public int insertSysBalanceLog(SysBalanceLog sysBalanceLog);

    /**
     * 修改余额变动
     *
     * @param sysBalanceLog 余额变动
     * @return 结果
     */
    public int updateSysBalanceLog(SysBalanceLog sysBalanceLog);

    /**
     * 批量删除余额变动
     *
     * @param ids 需要删除的余额变动主键集合
     * @return 结果
     */
    public int deleteSysBalanceLogByIds(Long[] ids);

    /**
     * 删除余额变动信息
     *
     * @param id 余额变动主键
     * @return 结果
     */
    public int deleteSysBalanceLogById(Long id);
}
