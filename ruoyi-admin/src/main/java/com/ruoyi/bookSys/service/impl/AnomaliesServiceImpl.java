package com.ruoyi.bookSys.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.bookSys.mapper.AnomaliesMapper;
import com.ruoyi.bookSys.domain.Anomalies;
import com.ruoyi.bookSys.service.IAnomaliesService;

/**
 * 异常记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-12-31
 */
@Service
public class AnomaliesServiceImpl implements IAnomaliesService 
{
    @Autowired
    private AnomaliesMapper anomaliesMapper;

    /**
     * 查询异常记录
     * 
     * @param anomalyId 异常记录主键
     * @return 异常记录
     */
    @Override
    public Anomalies selectAnomaliesByAnomalyId(Long anomalyId)
    {
        return anomaliesMapper.selectAnomaliesByAnomalyId(anomalyId);
    }

    /**
     * 查询异常记录列表
     * 
     * @param anomalies 异常记录
     * @return 异常记录
     */
    @Override
    public List<Anomalies> selectAnomaliesList(Anomalies anomalies)
    {
        return anomaliesMapper.selectAnomaliesList(anomalies);
    }

    /**
     * 新增异常记录
     * 
     * @param anomalies 异常记录
     * @return 结果
     */
    @Override
    public int insertAnomalies(Anomalies anomalies)
    {
        anomalies.setCreateTime(DateUtils.getNowDate());
        return anomaliesMapper.insertAnomalies(anomalies);
    }

    /**
     * 修改异常记录
     * 
     * @param anomalies 异常记录
     * @return 结果
     */
    @Override
    public int updateAnomalies(Anomalies anomalies)
    {
        anomalies.setUpdateTime(DateUtils.getNowDate());
        return anomaliesMapper.updateAnomalies(anomalies);
    }

    /**
     * 批量删除异常记录
     * 
     * @param anomalyIds 需要删除的异常记录主键
     * @return 结果
     */
    @Override
    public int deleteAnomaliesByAnomalyIds(Long[] anomalyIds)
    {
        return anomaliesMapper.deleteAnomaliesByAnomalyIds(anomalyIds);
    }

    /**
     * 删除异常记录信息
     * 
     * @param anomalyId 异常记录主键
     * @return 结果
     */
    @Override
    public int deleteAnomaliesByAnomalyId(Long anomalyId)
    {
        return anomaliesMapper.deleteAnomaliesByAnomalyId(anomalyId);
    }
}
