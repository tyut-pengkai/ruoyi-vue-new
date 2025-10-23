package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.StudentInfo;

/**
 * 学生信息Service接口
 * 
 * @author ruoyi
 * @date 2025-10-23
 */
public interface IStudentInfoService 
{
    /**
     * 查询学生信息
     * 
     * @param studentId 学生信息主键
     * @return 学生信息
     */
    public StudentInfo selectStudentInfoByStudentId(Long studentId);

    /**
     * 查询学生信息列表
     * 
     * @param studentInfo 学生信息
     * @return 学生信息集合
     */
    public List<StudentInfo> selectStudentInfoList(StudentInfo studentInfo);

    /**
     * 新增学生信息
     * 
     * @param studentInfo 学生信息
     * @return 结果
     */
    public int insertStudentInfo(StudentInfo studentInfo);

    /**
     * 修改学生信息
     * 
     * @param studentInfo 学生信息
     * @return 结果
     */
    public int updateStudentInfo(StudentInfo studentInfo);

    /**
     * 批量删除学生信息
     * 
     * @param studentIds 需要删除的学生信息主键集合
     * @return 结果
     */
    public int deleteStudentInfoByStudentIds(Long[] studentIds);

    /**
     * 删除学生信息信息
     * 
     * @param studentId 学生信息主键
     * @return 结果
     */
    public int deleteStudentInfoByStudentId(Long studentId);
    
    /**
     * 修改学生状态
     * 
     * @param studentInfo 学生信息
     * @return 结果
     */
    public int changeStudentStatus(StudentInfo studentInfo);
}
