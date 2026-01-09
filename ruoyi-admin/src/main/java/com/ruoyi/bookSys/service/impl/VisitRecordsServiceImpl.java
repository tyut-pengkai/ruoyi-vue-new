package com.ruoyi.bookSys.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.bookSys.domain.AccessLogs;
import com.ruoyi.bookSys.mapper.VisitRecordsMapper;
import com.ruoyi.bookSys.domain.VisitRecords;
import com.ruoyi.bookSys.service.IVisitRecordsService;

/**
 * 访客记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-12-31
 */
@Service
public class VisitRecordsServiceImpl implements IVisitRecordsService 
{
    @Autowired
    private VisitRecordsMapper visitRecordsMapper;

    /**
     * 查询访客记录
     * 
     * @param recordId 访客记录主键
     * @return 访客记录
     */
    @Override
    public VisitRecords selectVisitRecordsByRecordId(Long recordId)
    {
        return visitRecordsMapper.selectVisitRecordsByRecordId(recordId);
    }

    /**
     * 查询访客记录列表
     * 
     * @param visitRecords 访客记录
     * @return 访客记录
     */
    @Override
    public List<VisitRecords> selectVisitRecordsList(VisitRecords visitRecords)
    {
        return visitRecordsMapper.selectVisitRecordsList(visitRecords);
    }

    /**
     * 新增访客记录
     * 
     * @param visitRecords 访客记录
     * @return 结果
     */
    @Transactional
    @Override
    public int insertVisitRecords(VisitRecords visitRecords)
    {
        visitRecords.setCreateTime(DateUtils.getNowDate());
        int rows = visitRecordsMapper.insertVisitRecords(visitRecords);
        insertAccessLogs(visitRecords);
        return rows;
    }

    /**
     * 修改访客记录
     * 
     * @param visitRecords 访客记录
     * @return 结果
     */
    @Transactional
    @Override
    public int updateVisitRecords(VisitRecords visitRecords)
    {
        visitRecords.setUpdateTime(DateUtils.getNowDate());
        visitRecordsMapper.deleteAccessLogsByRecordId(visitRecords.getRecordId());
        insertAccessLogs(visitRecords);
        return visitRecordsMapper.updateVisitRecords(visitRecords);
    }

    /**
     * 批量删除访客记录
     * 
     * @param recordIds 需要删除的访客记录主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteVisitRecordsByRecordIds(Long[] recordIds)
    {
        visitRecordsMapper.deleteAccessLogsByRecordIds(recordIds);
        return visitRecordsMapper.deleteVisitRecordsByRecordIds(recordIds);
    }

    /**
     * 删除访客记录信息
     * 
     * @param recordId 访客记录主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteVisitRecordsByRecordId(Long recordId)
    {
        visitRecordsMapper.deleteAccessLogsByRecordId(recordId);
        return visitRecordsMapper.deleteVisitRecordsByRecordId(recordId);
    }

    /**
     * 新增通行记录信息
     * 
     * @param visitRecords 访客记录对象
     */
    public void insertAccessLogs(VisitRecords visitRecords)
    {
        List<AccessLogs> accessLogsList = visitRecords.getAccessLogsList();
        Long recordId = visitRecords.getRecordId();
        if (StringUtils.isNotNull(accessLogsList))
        {
            List<AccessLogs> list = new ArrayList<AccessLogs>();
            for (AccessLogs accessLogs : accessLogsList)
            {
                accessLogs.setRecordId(recordId);
                list.add(accessLogs);
            }
            if (list.size() > 0)
            {
                visitRecordsMapper.batchAccessLogs(list);
            }
        }
    }
}
