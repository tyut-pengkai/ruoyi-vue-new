package com.easycode.cloud.service.impl;

import java.util.List;

import com.weifu.cloud.common.core.utils.DateUtils;
import com.easycode.cloud.domain.WmsKbAndSwTaskLog;
import com.easycode.cloud.mapper.WmsKbAndSwTaskLogMapper;
import com.easycode.cloud.service.IWmsKbAndSwTaskLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-11-19
 */
@Service
public class WmsKbAndSwTaskLogServiceImpl implements IWmsKbAndSwTaskLogService
{
    @Autowired
    private WmsKbAndSwTaskLogMapper wmsKbAndSwTaskLogMapper;

    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public WmsKbAndSwTaskLog selectWmsKbAndSwTaskLogById(Long id)
    {
        return wmsKbAndSwTaskLogMapper.selectWmsKbAndSwTaskLogById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param wmsKbAndSwTaskLog 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<WmsKbAndSwTaskLog> selectWmsKbAndSwTaskLogList(WmsKbAndSwTaskLog wmsKbAndSwTaskLog)
    {
        return wmsKbAndSwTaskLogMapper.selectWmsKbAndSwTaskLogList(wmsKbAndSwTaskLog);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param wmsKbAndSwTaskLog 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertWmsKbAndSwTaskLog(WmsKbAndSwTaskLog wmsKbAndSwTaskLog)
    {
        wmsKbAndSwTaskLog.setCreateTime(DateUtils.getNowDate());
        return wmsKbAndSwTaskLogMapper.insertWmsKbAndSwTaskLog(wmsKbAndSwTaskLog);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param wmsKbAndSwTaskLog 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateWmsKbAndSwTaskLog(WmsKbAndSwTaskLog wmsKbAndSwTaskLog)
    {
        return wmsKbAndSwTaskLogMapper.updateWmsKbAndSwTaskLog(wmsKbAndSwTaskLog);
    }

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteWmsKbAndSwTaskLogByIds(Long[] ids)
    {
        return wmsKbAndSwTaskLogMapper.deleteWmsKbAndSwTaskLogByIds(ids);
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteWmsKbAndSwTaskLogById(Long id)
    {
        return wmsKbAndSwTaskLogMapper.deleteWmsKbAndSwTaskLogById(id);
    }
}
