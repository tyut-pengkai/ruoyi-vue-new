package com.ruoyi.bookSys.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.bookSys.domain.VisitRecords;
import com.ruoyi.bookSys.mapper.ResidentsMapper;
import com.ruoyi.bookSys.domain.Residents;
import com.ruoyi.bookSys.service.IResidentsService;

/**
 * 住户信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-12-31
 */
@Service
public class ResidentsServiceImpl implements IResidentsService 
{
    @Autowired
    private ResidentsMapper residentsMapper;

    /**
     * 查询住户信息
     * 
     * @param residentId 住户信息主键
     * @return 住户信息
     */
    @Override
    public Residents selectResidentsByResidentId(Long residentId)
    {
        return residentsMapper.selectResidentsByResidentId(residentId);
    }

    /**
     * 查询住户信息列表
     * 
     * @param residents 住户信息
     * @return 住户信息
     */
    @Override
    public List<Residents> selectResidentsList(Residents residents)
    {
        return residentsMapper.selectResidentsList(residents);
    }

    /**
     * 新增住户信息
     * 
     * @param residents 住户信息
     * @return 结果
     */
    @Transactional
    @Override
    public int insertResidents(Residents residents)
    {
        residents.setCreateTime(DateUtils.getNowDate());
        int rows = residentsMapper.insertResidents(residents);
        insertVisitRecords(residents);
        return rows;
    }

    /**
     * 修改住户信息
     * 
     * @param residents 住户信息
     * @return 结果
     */
    @Transactional
    @Override
    public int updateResidents(Residents residents)
    {
        residents.setUpdateTime(DateUtils.getNowDate());
        residentsMapper.deleteVisitRecordsByRecordId(residents.getResidentId());
        insertVisitRecords(residents);
        return residentsMapper.updateResidents(residents);
    }

    /**
     * 批量删除住户信息
     * 
     * @param residentIds 需要删除的住户信息主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteResidentsByResidentIds(Long[] residentIds)
    {
        residentsMapper.deleteVisitRecordsByRecordIds(residentIds);
        return residentsMapper.deleteResidentsByResidentIds(residentIds);
    }

    /**
     * 删除住户信息信息
     * 
     * @param residentId 住户信息主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteResidentsByResidentId(Long residentId)
    {
        residentsMapper.deleteVisitRecordsByRecordId(residentId);
        return residentsMapper.deleteResidentsByResidentId(residentId);
    }

    /**
     * 新增访客记录信息
     * 
     * @param residents 住户信息对象
     */
    public void insertVisitRecords(Residents residents)
    {
        List<VisitRecords> visitRecordsList = residents.getVisitRecordsList();
        Long residentId = residents.getResidentId();
        if (StringUtils.isNotNull(visitRecordsList))
        {
            List<VisitRecords> list = new ArrayList<VisitRecords>();
            for (VisitRecords visitRecords : visitRecordsList)
            {
                visitRecords.setRecordId(residentId);
                list.add(visitRecords);
            }
            if (list.size() > 0)
            {
                residentsMapper.batchVisitRecords(list);
            }
        }
    }
}
