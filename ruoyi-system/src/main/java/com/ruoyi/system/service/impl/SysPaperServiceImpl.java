package com.ruoyi.system.service.impl;

import java.util.Arrays;
import java.util.List;
import com.ruoyi.common.utils.DateUtils;
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
public class SysPaperServiceImpl implements ISysPaperService
{
    @Autowired
    private SysPaperMapper sysPaperMapper;
    
    /**
     * 查询论文信息列表
     * 
     * @param sysPaper 论文信息
     * @return 论文信息
     */
    @Override
    public List<SysPaper> selectSysPaperList(SysPaper sysPaper)
    {
        return sysPaperMapper.selectSysPaperList(sysPaper);
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
        return sysPaperMapper.selectSysPaperByPaperId(paperId);
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
        sysPaper.setCreateTime(DateUtils.getNowDate());
        return sysPaperMapper.insertSysPaper(sysPaper);
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
        sysPaper.setUpdateTime(DateUtils.getNowDate());
        return sysPaperMapper.updateSysPaper(sysPaper);
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
        return sysPaperMapper.deleteSysPaperByPaperIds(paperIds);
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
        return sysPaperMapper.deleteSysPaperByPaperId(paperId);
    }
}