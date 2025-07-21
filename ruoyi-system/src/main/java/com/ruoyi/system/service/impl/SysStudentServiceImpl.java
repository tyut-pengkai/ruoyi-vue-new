package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.domain.entity.SysStudent;
import com.ruoyi.system.mapper.SysStudentMapper;
import com.ruoyi.system.service.SysStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysStudentServiceImpl implements SysStudentService {

    @Autowired
    private SysStudentMapper studentMapper;

    @Override
    public List<SysStudent> selectStudentList(SysStudent student) {
        return studentMapper.selectStudentList(student);
    }

    @Override
    public SysStudent selectStudentById(Long studentId) {
        return studentMapper.selectStudentById(studentId);
    }

    @Override
    public int insertStudent(SysStudent student) {
        return studentMapper.insertStudent(student);
    }

    @Override
    public int updateStudent(SysStudent student) {
        return studentMapper.updateStudent(student);
    }

    @Override
    public int deleteStudentById(Long studentId) {
        return studentMapper.deleteStudentById(studentId);
    }
}
