package com.ruoyi.bookSys.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.bookSys.domain.VisitRecords;
import com.ruoyi.bookSys.mapper.PropertyStaffMapper;
import com.ruoyi.bookSys.domain.PropertyStaff;
import com.ruoyi.bookSys.service.IPropertyStaffService;

/**
 * 物业人员Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-12-31
 */
@Service
public class PropertyStaffServiceImpl implements IPropertyStaffService 
{
    @Autowired
    private PropertyStaffMapper propertyStaffMapper;

    /**
     * 查询物业人员
     * 
     * @param staffId 物业人员主键
     * @return 物业人员
     */
    @Override
    public PropertyStaff selectPropertyStaffByStaffId(Long staffId)
    {
        return propertyStaffMapper.selectPropertyStaffByStaffId(staffId);
    }

    /**
     * 查询物业人员列表
     * 
     * @param propertyStaff 物业人员
     * @return 物业人员
     */
    @Override
    public List<PropertyStaff> selectPropertyStaffList(PropertyStaff propertyStaff)
    {
        return propertyStaffMapper.selectPropertyStaffList(propertyStaff);
    }

    /**
     * 新增物业人员
     * 
     * @param propertyStaff 物业人员
     * @return 结果
     */
    @Transactional
    @Override
    public int insertPropertyStaff(PropertyStaff propertyStaff)
    {
        propertyStaff.setCreateTime(DateUtils.getNowDate());
        int rows = propertyStaffMapper.insertPropertyStaff(propertyStaff);
        insertVisitRecords(propertyStaff);
        return rows;
    }

    /**
     * 修改物业人员
     * 
     * @param propertyStaff 物业人员
     * @return 结果
     */
    @Transactional
    @Override
    public int updatePropertyStaff(PropertyStaff propertyStaff)
    {
        propertyStaff.setUpdateTime(DateUtils.getNowDate());
        propertyStaffMapper.deleteVisitRecordsByApproverId(propertyStaff.getStaffId());
        insertVisitRecords(propertyStaff);
        return propertyStaffMapper.updatePropertyStaff(propertyStaff);
    }

    /**
     * 批量删除物业人员
     * 
     * @param staffIds 需要删除的物业人员主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deletePropertyStaffByStaffIds(Long[] staffIds)
    {
        propertyStaffMapper.deleteVisitRecordsByApproverIds(staffIds);
        return propertyStaffMapper.deletePropertyStaffByStaffIds(staffIds);
    }

    /**
     * 删除物业人员信息
     * 
     * @param staffId 物业人员主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deletePropertyStaffByStaffId(Long staffId)
    {
        propertyStaffMapper.deleteVisitRecordsByApproverId(staffId);
        return propertyStaffMapper.deletePropertyStaffByStaffId(staffId);
    }

    /**
     * 新增访客记录信息
     * 
     * @param propertyStaff 物业人员对象
     */
    public void insertVisitRecords(PropertyStaff propertyStaff)
    {
        List<VisitRecords> visitRecordsList = propertyStaff.getVisitRecordsList();
        Long staffId = propertyStaff.getStaffId();
        if (StringUtils.isNotNull(visitRecordsList))
        {
            List<VisitRecords> list = new ArrayList<VisitRecords>();
            for (VisitRecords visitRecords : visitRecordsList)
            {
                visitRecords.setApproverId(staffId);
                list.add(visitRecords);
            }
            if (list.size() > 0)
            {
                propertyStaffMapper.batchVisitRecords(list);
            }
        }
    }
}
