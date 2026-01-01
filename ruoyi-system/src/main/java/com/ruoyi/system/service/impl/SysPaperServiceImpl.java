package com.ruoyi.system.service.impl;

import java.util.Arrays;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysPaper;
import com.ruoyi.system.mapper.SysPaperMapper;
import com.ruoyi.system.service.ISysPaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 论文信息Service业务层处理
 * 
 * @author ruoyi
 */
@Service
public class SysPaperServiceImpl extends ServiceImpl<SysPaperMapper, SysPaper> implements ISysPaperService
{
    /**
     * 查询论文信息列表
     * 
     * @param sysPaper 论文信息
     * @return 论文信息
     */
    @Override
    public List<SysPaper> selectSysPaperList(SysPaper sysPaper)
    {
        QueryWrapper<SysPaper> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(sysPaper.getStatus()), "status", sysPaper.getStatus());
        queryWrapper.eq(sysPaper.getStudentId() != null, "student_id", sysPaper.getStudentId());
        queryWrapper.like(StringUtils.isNotBlank(sysPaper.getPaperTitle()), "paper_title", sysPaper.getPaperTitle());
        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 通过论文ID查询论文信息
     * 
     * @param paperId 论文ID
     * @return 论文信息
     */
    @Override
    public SysPaper selectSysPaperByPaperId(Long paperId)
    {
        return baseMapper.selectById(paperId);
    }

    /**
     * 新增论文信息
     * 
     * @param sysPaper 论文信息
     * @return 结果
     */
    @Override
    public int insertSysPaper(SysPaper sysPaper)
    {
        return baseMapper.insert(sysPaper);
    }

    /**
     * 修改论文信息
     * 
     * @param sysPaper 论文信息
     * @return 结果
     */
    @Override
    public int updateSysPaper(SysPaper sysPaper)
    {
        return baseMapper.updateById(sysPaper);
    }

    /**
     * 批量删除论文信息
     * 
     * @param paperIds 需要删除的论文ID
     * @return 结果
     */
    @Override
    public int deleteSysPaperByPaperIds(Long[] paperIds)
    {
        return baseMapper.deleteBatchIds(Arrays.asList(paperIds));
    }

    /**
     * 删除论文信息信息
     * 
     * @param paperId 论文ID
     * @return 结果
     */
    @Override
    public int deleteSysPaperByPaperId(Long paperId)
    {
        return baseMapper.deleteById(paperId);
    }
}