package com.ruoyi.bookSys.mapper;

import java.util.List;
import com.ruoyi.bookSys.domain.PropertyStaff;
import com.ruoyi.bookSys.domain.VisitRecords;

/**
 * 物业人员Mapper接口
 * 
 * @author ruoyi
 * @date 2025-12-31
 */
public interface PropertyStaffMapper 
{
    /**
     * 查询物业人员
     * 
     * @param staffId 物业人员主键
     * @return 物业人员
     */
    public PropertyStaff selectPropertyStaffByStaffId(Long staffId);

    /**
     * 查询物业人员列表
     * 
     * @param propertyStaff 物业人员
     * @return 物业人员集合
     */
    public List<PropertyStaff> selectPropertyStaffList(PropertyStaff propertyStaff);

    /**
     * 新增物业人员
     * 
     * @param propertyStaff 物业人员
     * @return 结果
     */
    public int insertPropertyStaff(PropertyStaff propertyStaff);

    /**
     * 修改物业人员
     * 
     * @param propertyStaff 物业人员
     * @return 结果
     */
    public int updatePropertyStaff(PropertyStaff propertyStaff);

    /**
     * 删除物业人员
     * 
     * @param staffId 物业人员主键
     * @return 结果
     */
    public int deletePropertyStaffByStaffId(Long staffId);

    /**
     * 批量删除物业人员
     * 
     * @param staffIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePropertyStaffByStaffIds(Long[] staffIds);

    /**
     * 批量删除访客记录
     * 
     * @param staffIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteVisitRecordsByApproverIds(Long[] staffIds);
    
    /**
     * 批量新增访客记录
     * 
     * @param visitRecordsList 访客记录列表
     * @return 结果
     */
    public int batchVisitRecords(List<VisitRecords> visitRecordsList);
    

    /**
     * 通过物业人员主键删除访客记录信息
     * 
     * @param staffId 物业人员ID
     * @return 结果
     */
    public int deleteVisitRecordsByApproverId(Long staffId);
}
