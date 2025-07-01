package com.ruoyi.quartz.service;

import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.exception.job.TaskException;
import com.ruoyi.quartz.domain.SysJob;
import com.ruoyi.quartz.domain.dto.*;
import org.quartz.SchedulerException;

import java.util.List;

/**
 * 定时任务调度信息信息 服务层
 *
 * @author ruoyi
 */
public interface ISysJobService {
    /**
     * 定时任务表达式分页
     *
     * @param pageDTO 分页参数
     * @return Page<JobPageResDTO>
     */
    Page<JobPageResDTO> page(JobPageDTO pageDTO);

    /**
     * 新增定时任务
     *
     * @param createDTO 创建入参
     * @return Integer
     */
    Integer create(JobCreateDTO createDTO) throws SchedulerException, TaskException;

    /**
     * 获取定时任务详情
     *
     * @param jobId 任务ID
     * @return JobResDTO
     */
    JobResDTO getInfo(Long jobId);

    /**
     * 更新定时任务
     *
     * @param updateDTO 更新入参
     * @return Integer
     */
    Integer update(JobUpdateDTO updateDTO) throws SchedulerException, TaskException;

    /**
     * 删除定时任务
     *
     * @param jobId 任务ID
     * @return Integer
     */
    Integer delete(Long jobId) throws SchedulerException;

    /**
     * 更改定时任务状态
     * @param updateStatusDTO 更新状态入参
     * @return Integer
     */
    Integer updateStatus(JobUpdateStatusDTO updateStatusDTO) throws SchedulerException;

    /**
     * 立即运行定时任务
     * @param jobId jobId
     * @return Integer
     */
    void run(Long jobId) throws SchedulerException;
}
