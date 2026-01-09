package com.ruoyi.bookSys.service;

import java.util.List;
import com.ruoyi.bookSys.domain.Anomalies;

/**
 * 异常记录Service接口
 * 
 * @author ruoyi
 * @date 2025-12-31
 */
public interface IAnomaliesService 
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
     * 批量删除异常记录
     * 
     * @param anomalyIds 需要删除的异常记录主键集合
     * @return 结果
     */
    public int deleteAnomaliesByAnomalyIds(Long[] anomalyIds);

    /**
     * 删除异常记录信息
     * 
     * @param anomalyId 异常记录主键
     * @return 结果
     */
    public int deleteAnomaliesByAnomalyId(Long anomalyId);
}
