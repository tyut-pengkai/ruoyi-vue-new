package com.ruoyi.system.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.SysAppUserExpireLog;
import com.ruoyi.system.mapper.SysAppUserExpireLogMapper;
import com.ruoyi.system.service.ISysAppUserExpireLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 时长或点数变动Service业务层处理
 *
 * @author zwgu
 * @date 2023-01-05
 */
@Service
public class SysAppUserExpireLogServiceImpl implements ISysAppUserExpireLogService {
    @Autowired
    private SysAppUserExpireLogMapper sysAppUserExpireLogMapper;

    /**
     * 查询时长或点数变动
     *
     * @param id 时长或点数变动主键
     * @return 时长或点数变动
     */
    @Override
    public SysAppUserExpireLog selectSysAppUserExpireLogById(Long id) {
        return sysAppUserExpireLogMapper.selectSysAppUserExpireLogById(id);
    }

    /**
     * 查询时长或点数变动列表
     *
     * @param sysAppUserExpireLog 时长或点数变动
     * @return 时长或点数变动
     */
    @Override
    public List<SysAppUserExpireLog> selectSysAppUserExpireLogList(SysAppUserExpireLog sysAppUserExpireLog) {
        return sysAppUserExpireLogMapper.selectSysAppUserExpireLogList(sysAppUserExpireLog);
    }

    /**
     * 新增时长或点数变动
     *
     * @param sysAppUserExpireLog 时长或点数变动
     * @return 结果
     */
    @Override
    public int insertSysAppUserExpireLog(SysAppUserExpireLog sysAppUserExpireLog) {
        sysAppUserExpireLog.setCreateTime(DateUtils.getNowDate());
        return sysAppUserExpireLogMapper.insertSysAppUserExpireLog(sysAppUserExpireLog);
    }

    /**
     * 修改时长或点数变动
     *
     * @param sysAppUserExpireLog 时长或点数变动
     * @return 结果
     */
    @Override
    public int updateSysAppUserExpireLog(SysAppUserExpireLog sysAppUserExpireLog) {
        sysAppUserExpireLog.setUpdateTime(DateUtils.getNowDate());
        return sysAppUserExpireLogMapper.updateSysAppUserExpireLog(sysAppUserExpireLog);
    }

    /**
     * 批量删除时长或点数变动
     *
     * @param ids 需要删除的时长或点数变动主键
     * @return 结果
     */
    @Override
    public int deleteSysAppUserExpireLogByIds(Long[] ids) {
        return sysAppUserExpireLogMapper.deleteSysAppUserExpireLogByIds(ids);
    }

    /**
     * 删除时长或点数变动信息
     *
     * @param id 时长或点数变动主键
     * @return 结果
     */
    @Override
    public int deleteSysAppUserExpireLogById(Long id) {
        return sysAppUserExpireLogMapper.deleteSysAppUserExpireLogById(id);
    }
}
