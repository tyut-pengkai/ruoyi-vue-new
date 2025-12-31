package com.ruoyi.bookSys.mapper;

import java.util.List;
import com.ruoyi.bookSys.domain.AccessLogs;

/**
 * 通行记录Mapper接口
 * 
 * @author ruoyi
 * @date 2025-12-31
 */
public interface AccessLogsMapper 
{
    /**
     * 查询通行记录
     * 
     * @param logId 通行记录主键
     * @return 通行记录
     */
    public AccessLogs selectAccessLogsByLogId(Long logId);

    /**
     * 查询通行记录列表
     * 
     * @param accessLogs 通行记录
     * @return 通行记录集合
     */
    public List<AccessLogs> selectAccessLogsList(AccessLogs accessLogs);

    /**
     * 新增通行记录
     * 
     * @param accessLogs 通行记录
     * @return 结果
     */
    public int insertAccessLogs(AccessLogs accessLogs);

    /**
     * 修改通行记录
     * 
     * @param accessLogs 通行记录
     * @return 结果
     */
    public int updateAccessLogs(AccessLogs accessLogs);

    /**
     * 删除通行记录
     * 
     * @param logId 通行记录主键
     * @return 结果
     */
    public int deleteAccessLogsByLogId(Long logId);

    /**
     * 批量删除通行记录
     * 
     * @param logIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAccessLogsByLogIds(Long[] logIds);
}
