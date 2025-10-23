package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.StudentInfoMapper;
import com.ruoyi.system.domain.StudentInfo;
import com.ruoyi.system.service.IStudentInfoService;

/**
 * 学生信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-23
 */
@Service
public class StudentInfoServiceImpl implements IStudentInfoService 
{
    @Autowired
    private StudentInfoMapper studentInfoMapper;

    /**
     * 查询学生信息
     * 
     * @param studentId 学生信息主键
     * @return 学生信息
     */
    @Override
    public StudentInfo selectStudentInfoByStudentId(Long studentId)
    {
        return studentInfoMapper.selectStudentInfoByStudentId(studentId);
    }

    /**
     * 查询学生信息列表
     * 
     * @param studentInfo 学生信息
     * @return 学生信息
     */
    @Override
    public List<StudentInfo> selectStudentInfoList(StudentInfo studentInfo)
    {
        return studentInfoMapper.selectStudentInfoList(studentInfo);
    }

    /**
     * 新增学生信息
     * 
     * @param studentInfo 学生信息
     * @return 结果
     */
    @Override
    public int insertStudentInfo(StudentInfo studentInfo)
    {
        studentInfo.setCreateTime(DateUtils.getNowDate());
        return studentInfoMapper.insertStudentInfo(studentInfo);
    }

    /**
     * 修改学生信息
     * 
     * @param studentInfo 学生信息
     * @return 结果
     */
    @Override
    public int updateStudentInfo(StudentInfo studentInfo)
    {
        studentInfo.setUpdateTime(DateUtils.getNowDate());
        return studentInfoMapper.updateStudentInfo(studentInfo);
    }

    /**
     * 批量删除学生信息
     * 
     * @param studentIds 需要删除的学生信息主键
     * @return 结果
     */
    @Override
    public int deleteStudentInfoByStudentIds(Long[] studentIds)
    {
        return studentInfoMapper.deleteStudentInfoByStudentIds(studentIds);
    }

    /**
     * 删除学生信息信息
     * 
     * @param studentId 学生信息主键
     * @return 结果
     */
    @Override
    public int deleteStudentInfoByStudentId(Long studentId)
    {
        return studentInfoMapper.deleteStudentInfoByStudentId(studentId);
    }
    
    /**
     * 修改学生状态
     * 
     * @param studentInfo 学生信息
     * @return 结果
     */
    @Override
    public int changeStudentStatus(StudentInfo studentInfo)
    {
        studentInfo.setUpdateTime(DateUtils.getNowDate());
        return studentInfoMapper.updateStudentInfo(studentInfo);
    }
}