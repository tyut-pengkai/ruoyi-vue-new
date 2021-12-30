package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.entity.SysDeviceCode;

import java.util.List;

/**
 * 机器码管理Service接口
 * 
 * @author zwgu
 * @date 2021-12-06
 */
public interface ISysDeviceCodeService 
{
    /**
     * 查询机器码管理
     * 
     * @param deviceCodeId 机器码管理主键
     * @return 机器码管理
     */
    public SysDeviceCode selectSysDeviceCodeByDeviceCodeId(Long deviceCodeId);

    /**
     * 查询机器码管理列表
     * 
     * @param sysDeviceCode 机器码管理
     * @return 机器码管理集合
     */
    public List<SysDeviceCode> selectSysDeviceCodeList(SysDeviceCode sysDeviceCode);

    /**
     * 新增机器码管理
     * 
     * @param sysDeviceCode 机器码管理
     * @return 结果
     */
    public int insertSysDeviceCode(SysDeviceCode sysDeviceCode);

    /**
     * 修改机器码管理
     * 
     * @param sysDeviceCode 机器码管理
     * @return 结果
     */
    public int updateSysDeviceCode(SysDeviceCode sysDeviceCode);

    /**
     * 批量删除机器码管理
     * 
     * @param deviceCodeIds 需要删除的机器码管理主键集合
     * @return 结果
     */
    public int deleteSysDeviceCodeByDeviceCodeIds(Long[] deviceCodeIds);

    /**
     * 删除机器码管理信息
     * 
     * @param deviceCodeId 机器码管理主键
     * @return 结果
     */
    public int deleteSysDeviceCodeByDeviceCodeId(Long deviceCodeId);

    /**
     * 修改状态
     *
     * @param deviceCode 信息
     * @return 结果
     */
    public int updateSysDeviceCodeStatus(SysDeviceCode deviceCode);

    /**
     * 查询机器码管理
     *
     * @param deviceCode 机器码
     * @return 机器码管理
     */
    public SysDeviceCode selectSysDeviceCodeByDeviceCode(String deviceCode);
}
