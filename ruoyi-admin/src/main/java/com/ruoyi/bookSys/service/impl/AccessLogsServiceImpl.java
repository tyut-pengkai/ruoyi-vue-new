package com.ruoyi.bookSys.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.bookSys.mapper.AccessLogsMapper;
import com.ruoyi.bookSys.domain.AccessLogs;
import com.ruoyi.bookSys.service.IAccessLogsService;

/**
 * 通行记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-12-31
 */
@Service
public class AccessLogsServiceImpl implements IAccessLogsService 
{
    @Autowired
    private AccessLogsMapper accessLogsMapper;

    /**
     * 查询通行记录
     * 
     * @param logId 通行记录主键
     * @return 通行记录
     */
    @Override
    public AccessLogs selectAccessLogsByLogId(Long logId)
    {
        return accessLogsMapper.selectAccessLogsByLogId(logId);
    }

    /**
     * 查询通行记录列表
     * 
     * @param accessLogs 通行记录
     * @return 通行记录
     */
    @Override
    public List<AccessLogs> selectAccessLogsList(AccessLogs accessLogs)
    {
        return accessLogsMapper.selectAccessLogsList(accessLogs);
    }

    /**
     * 新增通行记录
     * 
     * @param accessLogs 通行记录
     * @return 结果
     */
    @Override
    public int insertAccessLogs(AccessLogs accessLogs)
    {
        return accessLogsMapper.insertAccessLogs(accessLogs);
    }

    /**
     * 修改通行记录
     * 
     * @param accessLogs 通行记录
     * @return 结果
     */
    @Override
    public int updateAccessLogs(AccessLogs accessLogs)
    {
        return accessLogsMapper.updateAccessLogs(accessLogs);
    }

    /**
     * 批量删除通行记录
     * 
     * @param logIds 需要删除的通行记录主键
     * @return 结果
     */
    @Override
    public int deleteAccessLogsByLogIds(Long[] logIds)
    {
        return accessLogsMapper.deleteAccessLogsByLogIds(logIds);
    }

    /**
     * 删除通行记录信息
     * 
     * @param logId 通行记录主键
     * @return 结果
     */
    @Override
    public int deleteAccessLogsByLogId(Long logId)
    {
        return accessLogsMapper.deleteAccessLogsByLogId(logId);
    }
}
