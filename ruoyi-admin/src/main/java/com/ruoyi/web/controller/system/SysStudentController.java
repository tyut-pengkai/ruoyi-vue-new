package com.ruoyi.web.controller.system;

import java.util.List;

import javax.validation.Valid;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysStudent;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.service.SysStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system/student")
public class SysStudentController {

    @Autowired
    private SysStudentService studentService;

    @GetMapping("/list")
    @PreAuthorize("hasPermi('system:student:list')")
    public AjaxResult listStudents(SysStudent student) {
        List<SysStudent> students = studentService.selectStudentList(student);
        return AjaxResult.success(students);
    }

    @GetMapping("/{studentId}")
    @PreAuthorize("hasPermi('system:student:query')")
    public AjaxResult getStudentById(@PathVariable Long studentId) {
        return AjaxResult.success(studentService.selectStudentById(studentId));
    }

    @PostMapping
    @PreAuthorize("hasPermi('system:student:add')")
    @Log(title = "学生管理", businessType = BusinessType.INSERT)
    public AjaxResult createStudent(@Valid @RequestBody SysStudent student) {
        studentService.insertStudent(student);
        return AjaxResult.success();
    }

    @PutMapping
    @PreAuthorize("hasPermi('system:student:edit')")
    @Log(title = "学生管理", businessType = BusinessType.UPDATE)
    public AjaxResult updateStudent(@Valid @RequestBody SysStudent student) {
        studentService.updateStudent(student);
        return AjaxResult.success();
    }

    @DeleteMapping("/{studentId}")
    @PreAuthorize("hasPermi('system:student:remove')")
    @Log(title = "学生管理", businessType = BusinessType.DELETE)
    public AjaxResult deleteStudent(@PathVariable Long studentId) {
        studentService.deleteStudentById(studentId);
        return AjaxResult.success();
    }
}
