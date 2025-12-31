package com.ruoyi.bookSys.service;

import java.util.List;
import com.ruoyi.bookSys.domain.VisitRecords;

/**
 * 访客记录Service接口
 * 
 * @author ruoyi
 * @date 2025-12-31
 */
public interface IVisitRecordsService 
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
     * 批量删除访客记录
     * 
     * @param recordIds 需要删除的访客记录主键集合
     * @return 结果
     */
    public int deleteVisitRecordsByRecordIds(Long[] recordIds);

    /**
     * 删除访客记录信息
     * 
     * @param recordId 访客记录主键
     * @return 结果
     */
    public int deleteVisitRecordsByRecordId(Long recordId);
}
