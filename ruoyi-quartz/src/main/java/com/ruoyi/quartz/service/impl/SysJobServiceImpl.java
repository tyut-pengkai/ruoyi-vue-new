package com.ruoyi.quartz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.constant.ScheduleConstants;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.exception.job.TaskException;
import com.ruoyi.quartz.domain.SysJob;
import com.ruoyi.quartz.domain.dto.*;
import com.ruoyi.quartz.mapper.SysJobMapper;
import com.ruoyi.quartz.service.ISysJobService;
import com.ruoyi.quartz.util.CronUtils;
import com.ruoyi.quartz.util.ScheduleUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

import static com.ruoyi.common.utils.SecurityUtils.getUsername;

/**
 * 定时任务调度信息 服务层
 *
 * @author ruoyi
 */
@Service
@RequiredArgsConstructor
public class SysJobServiceImpl implements ISysJobService {

    final Scheduler scheduler;
    final SysJobMapper jobMapper;

    /**
     * 项目启动时，初始化定时器 主要是防止手动修改数据库导致未同步到定时任务处理（注：不能手动修改数据库ID和任务组名，否则会导致脏数据）
     */
    @PostConstruct
    public void init() throws SchedulerException, TaskException {
        scheduler.clear();
        List<SysJob> jobList = this.jobMapper.selectList(new LambdaQueryWrapper<SysJob>()
                .eq(SysJob::getDelFlag, Constants.UNDELETED));
//        List<SysJob> jobList = jobMapper.selectJobAll();
        for (SysJob job : jobList) {
            ScheduleUtils.createScheduleJob(scheduler, job);
        }
    }

    /**
     * 定时任务表达式分页
     *
     * @param pageDTO 分页参数
     * @return Page<JobPageResDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public Page<JobPageResDTO> page(JobPageDTO pageDTO) {
        LambdaQueryWrapper<SysJob> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(pageDTO.getJobName())) {
            queryWrapper.like(SysJob::getJobName, pageDTO.getJobName());
        }
        if (StringUtils.isNotBlank(pageDTO.getJobGroup())) {
            queryWrapper.like(SysJob::getJobGroup, pageDTO.getJobGroup());
        }
        if (StringUtils.isNotBlank(pageDTO.getStatus())) {
            queryWrapper.eq(SysJob::getStatus, pageDTO.getStatus());
        }
        PageHelper.startPage(pageDTO.getPageNum(), pageDTO.getPageSize());
        List<SysJob> list = this.jobMapper.selectList(queryWrapper);
        return Page.convert(new PageInfo<>(BeanUtil.copyToList(list, JobPageResDTO.class)));
    }

    /**
     * 新增定时任务
     *
     * @param createDTO 创建入参
     * @return Integer
     */
    @Override
    @Transactional
    public Integer create(JobCreateDTO createDTO) throws SchedulerException, TaskException {
        if (!CronUtils.isValid(createDTO.getCronExpression())) {
            throw new ServiceException("新增任务'" + createDTO.getJobName() + "'失败，Cron表达式不正确", HttpStatus.ERROR);
        } else if (StringUtils.containsIgnoreCase(createDTO.getInvokeTarget(), Constants.LOOKUP_RMI)) {
            throw new ServiceException("新增任务'" + createDTO.getJobName() + "'失败，目标字符串不允许'rmi'调用", HttpStatus.ERROR);
        } else if (StringUtils.containsAnyIgnoreCase(createDTO.getInvokeTarget(), new String[]{Constants.LOOKUP_LDAP, Constants.LOOKUP_LDAPS})) {
            throw new ServiceException("新增任务'" + createDTO.getJobName() + "'失败，目标字符串不允许'ldap(s)'调用", HttpStatus.ERROR);
        } else if (StringUtils.containsAnyIgnoreCase(createDTO.getInvokeTarget(), new String[]{Constants.HTTP, Constants.HTTPS})) {
            throw new ServiceException("新增任务'" + createDTO.getJobName() + "'失败，目标字符串不允许'http(s)'调用", HttpStatus.ERROR);
        } else if (StringUtils.containsAnyIgnoreCase(createDTO.getInvokeTarget(), Constants.JOB_ERROR_STR)) {
            throw new ServiceException("新增任务'" + createDTO.getJobName() + "'失败，目标字符串存在违规", HttpStatus.ERROR);
        } else if (!ScheduleUtils.whiteList(createDTO.getInvokeTarget())) {
            throw new ServiceException("新增任务'" + createDTO.getJobName() + "'失败，目标字符串不在白名单内", HttpStatus.ERROR);
        }
        SysJob job = BeanUtil.toBean(createDTO, SysJob.class);
        job.setStatus(ScheduleConstants.Status.PAUSE.getValue());
        job.setCreateBy(getUsername());
        int count = this.jobMapper.insert(job);
        ScheduleUtils.createScheduleJob(scheduler, job);
        return count;
    }

    /**
     * 获取定时任务详情
     *
     * @param jobId 任务ID
     * @return JobResDTO
     */
    @Override
    @Transactional(readOnly = true)
    public JobResDTO getInfo(Long jobId) {
        SysJob job = Optional.ofNullable(this.jobMapper.selectOne(new LambdaQueryWrapper<SysJob>()
                        .eq(SysJob::getJobId, jobId).eq(SysJob::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("任务不存在!"));
        return BeanUtil.toBean(job, JobResDTO.class);
    }

    /**
     * 更新定时任务
     *
     * @param updateDTO 更新入参
     * @return Integer
     */
    @Override
    @Transactional
    public Integer update(JobUpdateDTO updateDTO) throws SchedulerException, TaskException {
        SysJob job = Optional.ofNullable(this.jobMapper.selectOne(new LambdaQueryWrapper<SysJob>()
                        .eq(SysJob::getJobId, updateDTO.getJobId()).eq(SysJob::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("任务不存在!"));
        if (!CronUtils.isValid(updateDTO.getCronExpression())) {
            throw new ServiceException("新增任务'" + updateDTO.getJobName() + "'失败，Cron表达式不正确", HttpStatus.ERROR);
        } else if (StringUtils.containsIgnoreCase(updateDTO.getInvokeTarget(), Constants.LOOKUP_RMI)) {
            throw new ServiceException("新增任务'" + updateDTO.getJobName() + "'失败，目标字符串不允许'rmi'调用", HttpStatus.ERROR);
        } else if (StringUtils.containsAnyIgnoreCase(updateDTO.getInvokeTarget(), new String[]{Constants.LOOKUP_LDAP, Constants.LOOKUP_LDAPS})) {
            throw new ServiceException("新增任务'" + updateDTO.getJobName() + "'失败，目标字符串不允许'ldap(s)'调用", HttpStatus.ERROR);
        } else if (StringUtils.containsAnyIgnoreCase(updateDTO.getInvokeTarget(), new String[]{Constants.HTTP, Constants.HTTPS})) {
            throw new ServiceException("新增任务'" + updateDTO.getJobName() + "'失败，目标字符串不允许'http(s)'调用", HttpStatus.ERROR);
        } else if (StringUtils.containsAnyIgnoreCase(updateDTO.getInvokeTarget(), Constants.JOB_ERROR_STR)) {
            throw new ServiceException("新增任务'" + updateDTO.getJobName() + "'失败，目标字符串存在违规", HttpStatus.ERROR);
        } else if (!ScheduleUtils.whiteList(updateDTO.getInvokeTarget())) {
            throw new ServiceException("新增任务'" + updateDTO.getJobName() + "'失败，目标字符串不在白名单内", HttpStatus.ERROR);
        }
        BeanUtil.copyProperties(updateDTO, job);
        job.setUpdateBy(getUsername());
        int count = this.jobMapper.updateById(job);
        updateSchedulerJob(job, job.getJobGroup());
        return count;
    }

    /**
     * 删除定时任务
     *
     * @param jobId 任务ID
     * @return Integer
     */
    @Override
    @Transactional
    public Integer delete(Long jobId) throws SchedulerException {
        SysJob job = Optional.ofNullable(this.jobMapper.selectOne(new LambdaQueryWrapper<SysJob>()
                        .eq(SysJob::getJobId, jobId).eq(SysJob::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("任务不存在!"));
        job.setDelFlag(Constants.DELETED);
        int count = this.jobMapper.updateById(job);
        scheduler.deleteJob(ScheduleUtils.getJobKey(jobId, job.getJobGroup()));
        return count;
    }

    /**
     * 更改定时任务状态
     *
     * @param updateStatusDTO 更新状态入参
     * @return Integer
     */
    @Override
    @Transactional
    public Integer updateStatus(JobUpdateStatusDTO updateStatusDTO) throws SchedulerException {
        SysJob job = Optional.ofNullable(this.jobMapper.selectOne(new LambdaQueryWrapper<SysJob>()
                        .eq(SysJob::getJobId, updateStatusDTO.getJobId()).eq(SysJob::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("任务不存在!"));
        // 更新状态
        job.setStatus(updateStatusDTO.getStatus());
        int count = this.jobMapper.updateById(job);
        if (ScheduleConstants.Status.NORMAL.getValue().equals(job.getStatus())) {
            scheduler.resumeJob(ScheduleUtils.getJobKey(job.getJobId(), job.getJobGroup()));
        } else if (ScheduleConstants.Status.PAUSE.getValue().equals(job.getStatus())) {
            scheduler.pauseJob(ScheduleUtils.getJobKey(job.getJobId(), job.getJobGroup()));
        }
        return count;
    }

    /**
     * 立即运行定时任务
     *
     * @param jobId jobId
     * @return Integer
     */
    @Override
    @Transactional
    public void run(Long jobId) throws SchedulerException {
        SysJob job = Optional.ofNullable(this.jobMapper.selectOne(new LambdaQueryWrapper<SysJob>()
                        .eq(SysJob::getJobId, jobId).eq(SysJob::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("任务不存在!"));
        JobKey jobKey = ScheduleUtils.getJobKey(jobId, job.getJobGroup());
        if (!scheduler.checkExists(jobKey)) {
            throw new ServiceException("任务不存在或已过期!", HttpStatus.ERROR);
        }
        // 参数
        JobDataMap dataMap = new JobDataMap();
        dataMap.put(ScheduleConstants.TASK_PROPERTIES, job);
        scheduler.triggerJob(jobKey, dataMap);
    }

    /**
     * 更新任务
     *
     * @param job      任务对象
     * @param jobGroup 任务组名
     */
    public void updateSchedulerJob(SysJob job, String jobGroup) throws SchedulerException, TaskException {
        Long jobId = job.getJobId();
        // 判断是否存在
        JobKey jobKey = ScheduleUtils.getJobKey(jobId, jobGroup);
        if (scheduler.checkExists(jobKey)) {
            // 防止创建时存在数据问题 先移除，然后在执行创建操作
            scheduler.deleteJob(jobKey);
        }
        ScheduleUtils.createScheduleJob(scheduler, job);
    }

}
