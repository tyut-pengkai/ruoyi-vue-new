package com.easycode.cloud.mapper;

import com.easycode.cloud.domain.WmsKbAndSwTaskLog;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 * 
 * @author ruoyi
 * @date 2024-11-19
 */
@Repository
public interface WmsKbAndSwTaskLogMapper 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public WmsKbAndSwTaskLog selectWmsKbAndSwTaskLogById(Long id);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param wmsKbAndSwTaskLog 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<WmsKbAndSwTaskLog> selectWmsKbAndSwTaskLogList(WmsKbAndSwTaskLog wmsKbAndSwTaskLog);

    /**
     * 新增【请填写功能名称】
     * 
     * @param wmsKbAndSwTaskLog 【请填写功能名称】
     * @return 结果
     */
    public int insertWmsKbAndSwTaskLog(WmsKbAndSwTaskLog wmsKbAndSwTaskLog);

    /**
     * 修改【请填写功能名称】
     * 
     * @param wmsKbAndSwTaskLog 【请填写功能名称】
     * @return 结果
     */
    public int updateWmsKbAndSwTaskLog(WmsKbAndSwTaskLog wmsKbAndSwTaskLog);

    /**
     * 删除【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteWmsKbAndSwTaskLogById(Long id);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWmsKbAndSwTaskLogByIds(Long[] ids);

    public int batchInsertWmsKbAndSwTaskLog(List<WmsKbAndSwTaskLog> wmsKbAndSwTaskLogs);
}
