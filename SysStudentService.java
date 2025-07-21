package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.entity.SysStudent;
import java.util.List;

public interface SysStudentService {
    List<SysStudent> selectStudentList(SysStudent student);
    SysStudent selectStudentById(Long studentId);
    int insertStudent(SysStudent student);
    int updateStudent(SysStudent student);
    int deleteStudentById(Long studentId);
}
