package com.ruoyi.system.service;

import com.ruoyi.system.domain.SysAppUserExpireLog;

import java.util.List;

/**
 * 时长或点数变动Service接口
 *
 * @author zwgu
 * @date 2023-01-05
 */
public interface ISysAppUserExpireLogService {
    /**
     * 查询时长或点数变动
     *
     * @param id 时长或点数变动主键
     * @return 时长或点数变动
     */
    public SysAppUserExpireLog selectSysAppUserExpireLogById(Long id);

    /**
     * 查询时长或点数变动列表
     *
     * @param sysAppUserExpireLog 时长或点数变动
     * @return 时长或点数变动集合
     */
    public List<SysAppUserExpireLog> selectSysAppUserExpireLogList(SysAppUserExpireLog sysAppUserExpireLog);

    /**
     * 新增时长或点数变动
     *
     * @param sysAppUserExpireLog 时长或点数变动
     * @return 结果
     */
    public int insertSysAppUserExpireLog(SysAppUserExpireLog sysAppUserExpireLog);

    /**
     * 修改时长或点数变动
     *
     * @param sysAppUserExpireLog 时长或点数变动
     * @return 结果
     */
    public int updateSysAppUserExpireLog(SysAppUserExpireLog sysAppUserExpireLog);

    /**
     * 批量删除时长或点数变动
     *
     * @param ids 需要删除的时长或点数变动主键集合
     * @return 结果
     */
    public int deleteSysAppUserExpireLogByIds(Long[] ids);

    /**
     * 删除时长或点数变动信息
     *
     * @param id 时长或点数变动主键
     * @return 结果
     */
    public int deleteSysAppUserExpireLogById(Long id);
}
