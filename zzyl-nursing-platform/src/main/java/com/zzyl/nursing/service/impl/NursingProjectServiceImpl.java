package com.zzyl.nursing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzyl.common.utils.DateUtils;
import com.zzyl.nursing.domain.NursingProject;
import com.zzyl.nursing.mapper.NursingProjectMapper;
import com.zzyl.nursing.service.INursingProjectService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * 护理项目Service业务层处理
 *
 * @author ruoyi
 * @date 2025-05-14
 */
@Service
public class NursingProjectServiceImpl extends ServiceImpl<NursingProjectMapper, NursingProject> implements INursingProjectService {


    /**
     * 查询护理项目
     *
     * @param id 护理项目主键
     * @return 护理项目
     */
    @Override
    public NursingProject selectNursingProjectById(Long id) {
        return this.getById(id);
    }

    /**
     * 查询护理项目列表
     *
     * @param nursingProject 护理项目
     * @return 护理项目
     */
    @Override
    public List<NursingProject> selectNursingProjectList(NursingProject nursingProject) {
        return list(new LambdaQueryWrapper<NursingProject>()
                .like(StringUtils.isNotBlank(nursingProject.getName()), NursingProject::getName, nursingProject.getName())
                .eq(ObjectUtils.isNotNull(nursingProject.getStatus()), NursingProject::getStatus, nursingProject.getStatus()));
    }

    /**
     * 新增护理项目
     *
     * @param nursingProject 护理项目
     * @return 结果
     */
    @Override
    public int insertNursingProject(NursingProject nursingProject) {
        nursingProject.setCreateTime(DateUtils.getNowDate());
        return this.save(nursingProject) ? 1 : 0;
    }

    /**
     * 修改护理项目
     *
     * @param nursingProject 护理项目
     * @return 结果
     */
    @Override
    public int updateNursingProject(NursingProject nursingProject) {
        nursingProject.setUpdateTime(DateUtils.getNowDate());
        return this.updateById(nursingProject) ? 1 : 0;
    }

    /**
     * 批量删除护理项目
     *
     * @param ids 需要删除的护理项目主键
     * @return 结果
     */
    @Override
    public int deleteNursingProjectByIds(Long[] ids) {
        return this.removeByIds(Arrays.asList(ids)) ? 1 : 0;
    }

    /**
     * 删除护理项目信息
     *
     * @param id 护理项目主键
     * @return 结果
     */
    @Override
    public int deleteNursingProjectById(Long id) {
        return removeById(id) ? 1 : 0;
    }
}
