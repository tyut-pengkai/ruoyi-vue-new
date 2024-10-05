package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.BjWorker;

/**
 * 技术人员管理Service接口
 * 
 * @author ssq
 * @date 2024-10-05
 */
public interface IBjWorkerService 
{
    /**
     * 查询技术人员管理
     * 
     * @param id 技术人员管理主键
     * @return 技术人员管理
     */
    public BjWorker selectBjWorkerById(Long id);

    /**
     * 查询技术人员管理列表
     * 
     * @param bjWorker 技术人员管理
     * @return 技术人员管理集合
     */
    public List<BjWorker> selectBjWorkerList(BjWorker bjWorker);

    /**
     * 新增技术人员管理
     * 
     * @param bjWorker 技术人员管理
     * @return 结果
     */
    public int insertBjWorker(BjWorker bjWorker);

    /**
     * 修改技术人员管理
     * 
     * @param bjWorker 技术人员管理
     * @return 结果
     */
    public int updateBjWorker(BjWorker bjWorker);

    /**
     * 批量删除技术人员管理
     * 
     * @param ids 需要删除的技术人员管理主键集合
     * @return 结果
     */
    public int deleteBjWorkerByIds(Long[] ids);

    /**
     * 删除技术人员管理信息
     * 
     * @param id 技术人员管理主键
     * @return 结果
     */
    public int deleteBjWorkerById(Long id);
}
