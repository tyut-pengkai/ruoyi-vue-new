package com.ruoyi.bookSys.mapper;

import java.util.List;
import com.ruoyi.bookSys.domain.Visitors;
import com.ruoyi.bookSys.domain.VisitRecords;

/**
 * 访客信息Mapper接口
 * 
 * @author ruoyi
 * @date 2025-12-31
 */
public interface VisitorsMapper 
{
    /**
     * 查询访客信息
     * 
     * @param visitorId 访客信息主键
     * @return 访客信息
     */
    public Visitors selectVisitorsByVisitorId(Long visitorId);

    /**
     * 查询访客信息列表
     * 
     * @param visitors 访客信息
     * @return 访客信息集合
     */
    public List<Visitors> selectVisitorsList(Visitors visitors);

    /**
     * 新增访客信息
     * 
     * @param visitors 访客信息
     * @return 结果
     */
    public int insertVisitors(Visitors visitors);

    /**
     * 修改访客信息
     * 
     * @param visitors 访客信息
     * @return 结果
     */
    public int updateVisitors(Visitors visitors);

    /**
     * 删除访客信息
     * 
     * @param visitorId 访客信息主键
     * @return 结果
     */
    public int deleteVisitorsByVisitorId(Long visitorId);

    /**
     * 批量删除访客信息
     * 
     * @param visitorIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteVisitorsByVisitorIds(Long[] visitorIds);

    /**
     * 批量删除访客记录
     * 
     * @param visitorIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteVisitRecordsByVisitorIds(Long[] visitorIds);
    
    /**
     * 批量新增访客记录
     * 
     * @param visitRecordsList 访客记录列表
     * @return 结果
     */
    public int batchVisitRecords(List<VisitRecords> visitRecordsList);
    

    /**
     * 通过访客信息主键删除访客记录信息
     * 
     * @param visitorId 访客信息ID
     * @return 结果
     */
    public int deleteVisitRecordsByVisitorId(Long visitorId);
}
