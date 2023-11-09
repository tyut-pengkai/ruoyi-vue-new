package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SysAppUserExpireLog;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 时长或点数变动Mapper接口
 *
 * @author zwgu
 * @date 2023-01-05
 */
@Repository
public interface SysAppUserExpireLogMapper {
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
     * 删除时长或点数变动
     *
     * @param id 时长或点数变动主键
     * @return 结果
     */
    public int deleteSysAppUserExpireLogById(Long id);

    /**
     * 批量删除时长或点数变动
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysAppUserExpireLogByIds(Long[] ids);

    /**
     * 清空日志
     */
    public void cleanAppUserExpireLog();
}
