package com.ruoyi.bookSys.service;

import java.util.List;
import com.ruoyi.bookSys.domain.Residents;

/**
 * 住户信息Service接口
 * 
 * @author ruoyi
 * @date 2025-12-31
 */
public interface IResidentsService 
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
     * 批量删除住户信息
     * 
     * @param residentIds 需要删除的住户信息主键集合
     * @return 结果
     */
    public int deleteResidentsByResidentIds(Long[] residentIds);

    /**
     * 删除住户信息信息
     * 
     * @param residentId 住户信息主键
     * @return 结果
     */
    public int deleteResidentsByResidentId(Long residentId);
}
