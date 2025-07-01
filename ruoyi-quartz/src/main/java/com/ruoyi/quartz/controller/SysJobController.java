package com.ruoyi.quartz.controller;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.job.TaskException;
import com.ruoyi.quartz.domain.dto.*;
import com.ruoyi.quartz.domain.vo.*;
import com.ruoyi.quartz.service.ISysJobService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.quartz.SchedulerException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 调度任务信息操作处理
 *
 * @author ruoyi
 */
@Api(tags = "定时任务调度")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/v1/monitor/job")
public class SysJobController extends XktBaseController {

    final ISysJobService jobService;

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @ApiOperation(value = "定时任务列表 ", httpMethod = "POST", response = R.class)
    @PostMapping("/page")
    public R<Page<JobPageResDTO>> page(@Validated @RequestBody JobPageVO pageVO) {
        return R.ok(jobService.page(BeanUtil.toBean(pageVO, JobPageDTO.class)));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @ApiOperation(value = "新增定时任务 ", httpMethod = "POST", response = R.class)
    @Log(title = "新增定时任务", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Integer> create(@Validated @RequestBody JobCreateVO createVO) throws SchedulerException, TaskException {
        return R.ok(this.jobService.create(BeanUtil.toBean(createVO, JobCreateDTO.class)));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @ApiOperation(value = "编辑定时任务 ", httpMethod = "PUT", response = R.class)
    @Log(title = "编辑定时任务", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Integer> update(@Validated @RequestBody JobUpdateVO updateVO) throws SchedulerException, TaskException {
        return R.ok(this.jobService.update(BeanUtil.toBean(updateVO, JobUpdateDTO.class)));
    }

    @SneakyThrows
    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @ApiOperation(value = "删除定时任务 ", httpMethod = "DELETE", response = R.class)
    @Log(title = "删除定时任务", businessType = BusinessType.DELETE)
    @DeleteMapping("/{jobId}")
    public R<Integer> delete(@PathVariable Long jobId) throws SchedulerException {
        return R.ok(this.jobService.delete(jobId));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @ApiOperation(value = "获取定时任务详情 ", httpMethod = "GET", response = R.class)
    @Log(title = "获取定时任务详情", businessType = BusinessType.INSERT)
    @GetMapping("/{jobId}")
    public R<JobResVO> getInfo(@PathVariable Long jobId) {
        return R.ok(BeanUtil.toBean(this.jobService.getInfo(jobId), JobResVO.class));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @ApiOperation(value = "更改定时任务状态 ", httpMethod = "PUT", response = R.class)
    @Log(title = "更改定时任务状态", businessType = BusinessType.UPDATE)
    @PutMapping("/change-status")
    public R<Integer> updateStatus(@Validated @RequestBody JobUpdateStatusVO updateStatusVO) throws SchedulerException {
        return R.ok(this.jobService.updateStatus(BeanUtil.toBean(updateStatusVO, JobUpdateStatusDTO.class)));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @ApiOperation(value = "立即执行定时任务 ", httpMethod = "PUT", response = R.class)
    @Log(title = "立即执行定时任务", businessType = BusinessType.UPDATE)
    @PutMapping("/run/{jobId}")
    public R run(@PathVariable Long jobId) throws SchedulerException {
        this.jobService.run(jobId);
        return R.ok();
    }

}
