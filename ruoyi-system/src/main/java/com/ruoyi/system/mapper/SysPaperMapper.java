package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SysPaper;

/**
 * 论文信息Mapper接口
 * 
 * @author ruoyi
 */
public interface SysPaperMapper
{
    /**
     * 查询论文信息列表
     * 
     * @param sysPaper 论文信息
     * @return 论文信息集合
     */
    public List<SysPaper> selectSysPaperList(SysPaper sysPaper);

    /**
     * 通过论文ID查询论文信息
     * 
     * @param paperId 论文ID
     * @return 论文信息
     */
    public SysPaper selectSysPaperByPaperId(Long paperId);

    /**
     * 新增论文信息
     * 
     * @param sysPaper 论文信息
     * @return 结果
     */
    public int insertSysPaper(SysPaper sysPaper);

    /**
     * 修改论文信息
     * 
     * @param sysPaper 论文信息
     * @return 结果
     */
    public int updateSysPaper(SysPaper sysPaper);

    /**
     * 删除论文信息
     * 
     * @param paperId 论文ID
     * @return 结果
     */
    public int deleteSysPaperByPaperId(Long paperId);

    /**
     * 批量删除论文信息
     * 
     * @param paperIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysPaperByPaperIds(Long[] paperIds);
}

