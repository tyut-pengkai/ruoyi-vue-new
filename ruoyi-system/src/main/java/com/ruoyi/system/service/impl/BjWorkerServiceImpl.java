package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.BjWorkerMapper;
import com.ruoyi.system.domain.BjWorker;
import com.ruoyi.system.service.IBjWorkerService;

/**
 * 技术人员管理Service业务层处理
 * 
 * @author ssq
 * @date 2024-10-05
 */
@Service
public class BjWorkerServiceImpl implements IBjWorkerService 
{
    @Autowired
    private BjWorkerMapper bjWorkerMapper;

    /**
     * 查询技术人员管理
     * 
     * @param id 技术人员管理主键
     * @return 技术人员管理
     */
    @Override
    public BjWorker selectBjWorkerById(Long id)
    {
        return bjWorkerMapper.selectBjWorkerById(id);
    }

    /**
     * 查询技术人员管理列表
     * 
     * @param bjWorker 技术人员管理
     * @return 技术人员管理
     */
    @Override
    public List<BjWorker> selectBjWorkerList(BjWorker bjWorker)
    {
        return bjWorkerMapper.selectBjWorkerList(bjWorker);
    }

    /**
     * 新增技术人员管理
     * 
     * @param bjWorker 技术人员管理
     * @return 结果
     */
    @Override
    public int insertBjWorker(BjWorker bjWorker)
    {
        return bjWorkerMapper.insertBjWorker(bjWorker);
    }

    /**
     * 修改技术人员管理
     * 
     * @param bjWorker 技术人员管理
     * @return 结果
     */
    @Override
    public int updateBjWorker(BjWorker bjWorker)
    {
        return bjWorkerMapper.updateBjWorker(bjWorker);
    }

    /**
     * 批量删除技术人员管理
     * 
     * @param ids 需要删除的技术人员管理主键
     * @return 结果
     */
    @Override
    public int deleteBjWorkerByIds(Long[] ids)
    {
        return bjWorkerMapper.deleteBjWorkerByIds(ids);
    }

    /**
     * 删除技术人员管理信息
     * 
     * @param id 技术人员管理主键
     * @return 结果
     */
    @Override
    public int deleteBjWorkerById(Long id)
    {
        return bjWorkerMapper.deleteBjWorkerById(id);
    }
}
