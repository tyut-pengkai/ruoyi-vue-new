package com.ruoyi.bookSys.mapper;

import java.util.List;
import com.ruoyi.bookSys.domain.Residents;
import com.ruoyi.bookSys.domain.VisitRecords;

/**
 * 住户信息Mapper接口
 * 
 * @author ruoyi
 * @date 2025-12-31
 */
public interface ResidentsMapper 
{
    /**
     * 查询住户信息
     * 
     * @param residentId 住户信息主键
     * @return 住户信息
     */
    public Residents selectResidentsByResidentId(Long residentId);

    /**
     * 查询住户信息列表
     * 
     * @param residents 住户信息
     * @return 住户信息集合
     */
    public List<Residents> selectResidentsList(Residents residents);

    /**
     * 新增住户信息
     * 
     * @param residents 住户信息
     * @return 结果
     */
    public int insertResidents(Residents residents);

    /**
     * 修改住户信息
     * 
     * @param residents 住户信息
     * @return 结果
     */
    public int updateResidents(Residents residents);

    /**
     * 删除住户信息
     * 
     * @param residentId 住户信息主键
     * @return 结果
     */
    public int deleteResidentsByResidentId(Long residentId);

    /**
     * 批量删除住户信息
     * 
     * @param residentIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteResidentsByResidentIds(Long[] residentIds);

    /**
     * 批量删除访客记录
     * 
     * @param residentIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteVisitRecordsByRecordIds(Long[] residentIds);
    
    /**
     * 批量新增访客记录
     * 
     * @param visitRecordsList 访客记录列表
     * @return 结果
     */
    public int batchVisitRecords(List<VisitRecords> visitRecordsList);
    

    /**
     * 通过住户信息主键删除访客记录信息
     * 
     * @param residentId 住户信息ID
     * @return 结果
     */
    public int deleteVisitRecordsByRecordId(Long residentId);
}
