package com.ruoyi.bookSys.service;

import java.util.List;
import com.ruoyi.bookSys.domain.PropertyStaff;

/**
 * 物业人员Service接口
 * 
 * @author ruoyi
 * @date 2025-12-31
 */
public interface IPropertyStaffService 
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
     * 批量删除物业人员
     * 
     * @param staffIds 需要删除的物业人员主键集合
     * @return 结果
     */
    public int deletePropertyStaffByStaffIds(Long[] staffIds);

    /**
     * 删除物业人员信息
     * 
     * @param staffId 物业人员主键
     * @return 结果
     */
    public int deletePropertyStaffByStaffId(Long staffId);
}
