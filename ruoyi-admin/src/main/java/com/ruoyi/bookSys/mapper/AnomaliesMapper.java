package com.ruoyi.bookSys.mapper;

import java.util.List;
import com.ruoyi.bookSys.domain.Anomalies;

/**
 * 异常记录Mapper接口
 * 
 * @author ruoyi
 * @date 2025-12-31
 */
public interface AnomaliesMapper 
{
    /**
     * 查询异常记录
     * 
     * @param anomalyId 异常记录主键
     * @return 异常记录
     */
    public Anomalies selectAnomaliesByAnomalyId(Long anomalyId);

    /**
     * 查询异常记录列表
     * 
     * @param anomalies 异常记录
     * @return 异常记录集合
     */
    public List<Anomalies> selectAnomaliesList(Anomalies anomalies);

    /**
     * 新增异常记录
     * 
     * @param anomalies 异常记录
     * @return 结果
     */
    public int insertAnomalies(Anomalies anomalies);

    /**
     * 修改异常记录
     * 
     * @param anomalies 异常记录
     * @return 结果
     */
    public int updateAnomalies(Anomalies anomalies);

    /**
     * 删除异常记录
     * 
     * @param anomalyId 异常记录主键
     * @return 结果
     */
    public int deleteAnomaliesByAnomalyId(Long anomalyId);

    /**
     * 批量删除异常记录
     * 
     * @param anomalyIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAnomaliesByAnomalyIds(Long[] anomalyIds);
}
