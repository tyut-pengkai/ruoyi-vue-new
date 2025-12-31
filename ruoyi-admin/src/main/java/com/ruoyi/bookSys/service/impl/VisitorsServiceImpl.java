package com.ruoyi.bookSys.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.bookSys.domain.VisitRecords;
import com.ruoyi.bookSys.mapper.VisitorsMapper;
import com.ruoyi.bookSys.domain.Visitors;
import com.ruoyi.bookSys.service.IVisitorsService;

/**
 * 访客信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-12-31
 */
@Service
public class VisitorsServiceImpl implements IVisitorsService 
{
    @Autowired
    private VisitorsMapper visitorsMapper;

    /**
     * 查询访客信息
     * 
     * @param visitorId 访客信息主键
     * @return 访客信息
     */
    @Override
    public Visitors selectVisitorsByVisitorId(Long visitorId)
    {
        return visitorsMapper.selectVisitorsByVisitorId(visitorId);
    }

    /**
     * 查询访客信息列表
     * 
     * @param visitors 访客信息
     * @return 访客信息
     */
    @Override
    public List<Visitors> selectVisitorsList(Visitors visitors)
    {
        return visitorsMapper.selectVisitorsList(visitors);
    }

    /**
     * 新增访客信息
     * 
     * @param visitors 访客信息
     * @return 结果
     */
    @Transactional
    @Override
    public int insertVisitors(Visitors visitors)
    {
        visitors.setCreateTime(DateUtils.getNowDate());
        int rows = visitorsMapper.insertVisitors(visitors);
        insertVisitRecords(visitors);
        return rows;
    }

    /**
     * 修改访客信息
     * 
     * @param visitors 访客信息
     * @return 结果
     */
    @Transactional
    @Override
    public int updateVisitors(Visitors visitors)
    {
        visitors.setUpdateTime(DateUtils.getNowDate());
        visitorsMapper.deleteVisitRecordsByVisitorId(visitors.getVisitorId());
        insertVisitRecords(visitors);
        return visitorsMapper.updateVisitors(visitors);
    }

    /**
     * 批量删除访客信息
     * 
     * @param visitorIds 需要删除的访客信息主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteVisitorsByVisitorIds(Long[] visitorIds)
    {
        visitorsMapper.deleteVisitRecordsByVisitorIds(visitorIds);
        return visitorsMapper.deleteVisitorsByVisitorIds(visitorIds);
    }

    /**
     * 删除访客信息信息
     * 
     * @param visitorId 访客信息主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteVisitorsByVisitorId(Long visitorId)
    {
        visitorsMapper.deleteVisitRecordsByVisitorId(visitorId);
        return visitorsMapper.deleteVisitorsByVisitorId(visitorId);
    }

    /**
     * 新增访客记录信息
     * 
     * @param visitors 访客信息对象
     */
    public void insertVisitRecords(Visitors visitors)
    {
        List<VisitRecords> visitRecordsList = visitors.getVisitRecordsList();
        Long visitorId = visitors.getVisitorId();
        if (StringUtils.isNotNull(visitRecordsList))
        {
            List<VisitRecords> list = new ArrayList<VisitRecords>();
            for (VisitRecords visitRecords : visitRecordsList)
            {
                visitRecords.setVisitorId(visitorId);
                list.add(visitRecords);
            }
            if (list.size() > 0)
            {
                visitorsMapper.batchVisitRecords(list);
            }
        }
    }
}
