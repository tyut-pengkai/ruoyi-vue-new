package com.ruoyi.bookSys.service;

import java.util.List;
import com.ruoyi.bookSys.domain.Visitors;

/**
 * 访客信息Service接口
 * 
 * @author ruoyi
 * @date 2025-12-31
 */
public interface IVisitorsService 
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
     * 批量删除访客信息
     * 
     * @param visitorIds 需要删除的访客信息主键集合
     * @return 结果
     */
    public int deleteVisitorsByVisitorIds(Long[] visitorIds);

    /**
     * 删除访客信息信息
     * 
     * @param visitorId 访客信息主键
     * @return 结果
     */
    public int deleteVisitorsByVisitorId(Long visitorId);
}
