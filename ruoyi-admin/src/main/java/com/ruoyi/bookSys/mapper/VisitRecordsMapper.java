package com.ruoyi.bookSys.mapper;

import java.util.List;
import com.ruoyi.bookSys.domain.VisitRecords;
import com.ruoyi.bookSys.domain.AccessLogs;

/**
 * 访客记录Mapper接口
 * 
 * @author ruoyi
 * @date 2025-12-31
 */
public interface VisitRecordsMapper 
{
    /**
     * 查询访客记录
     * 
     * @param recordId 访客记录主键
     * @return 访客记录
     */
    public VisitRecords selectVisitRecordsByRecordId(Long recordId);

    /**
     * 查询访客记录列表
     * 
     * @param visitRecords 访客记录
     * @return 访客记录集合
     */
    public List<VisitRecords> selectVisitRecordsList(VisitRecords visitRecords);

    /**
     * 新增访客记录
     * 
     * @param visitRecords 访客记录
     * @return 结果
     */
    public int insertVisitRecords(VisitRecords visitRecords);

    /**
     * 修改访客记录
     * 
     * @param visitRecords 访客记录
     * @return 结果
     */
    public int updateVisitRecords(VisitRecords visitRecords);

    /**
     * 删除访客记录
     * 
     * @param recordId 访客记录主键
     * @return 结果
     */
    public int deleteVisitRecordsByRecordId(Long recordId);

    /**
     * 批量删除访客记录
     * 
     * @param recordIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteVisitRecordsByRecordIds(Long[] recordIds);

    /**
     * 批量删除通行记录
     * 
     * @param recordIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAccessLogsByRecordIds(Long[] recordIds);
    
    /**
     * 批量新增通行记录
     * 
     * @param accessLogsList 通行记录列表
     * @return 结果
     */
    public int batchAccessLogs(List<AccessLogs> accessLogsList);
    

    /**
     * 通过访客记录主键删除通行记录信息
     * 
     * @param recordId 访客记录ID
     * @return 结果
     */
    public int deleteAccessLogsByRecordId(Long recordId);
}
