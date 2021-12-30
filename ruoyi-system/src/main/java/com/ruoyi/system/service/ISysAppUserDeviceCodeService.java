package com.ruoyi.system.service;

import com.ruoyi.system.domain.SysAppUserDeviceCode;

import java.util.List;

/**
 * 软件用户与设备码关联Service接口
 *
 * @author zwgu
 * @date 2021-12-18
 */
public interface ISysAppUserDeviceCodeService {
    /**
     * 查询软件用户与设备码关联
     *
     * @param id 软件用户与设备码关联主键
     * @return 软件用户与设备码关联
     */
    public SysAppUserDeviceCode selectSysAppUserDeviceCodeById(Long id);

    /**
     * 查询软件用户与设备码关联列表
     *
     * @param sysAppUserDeviceCode 软件用户与设备码关联
     * @return 软件用户与设备码关联集合
     */
    public List<SysAppUserDeviceCode> selectSysAppUserDeviceCodeList(SysAppUserDeviceCode sysAppUserDeviceCode);

    /**
     * 新增软件用户与设备码关联
     *
     * @param sysAppUserDeviceCode 软件用户与设备码关联
     * @return 结果
     */
    public int insertSysAppUserDeviceCode(SysAppUserDeviceCode sysAppUserDeviceCode);

    /**
     * 修改软件用户与设备码关联
     *
     * @param sysAppUserDeviceCode 软件用户与设备码关联
     * @return 结果
     */
    public int updateSysAppUserDeviceCode(SysAppUserDeviceCode sysAppUserDeviceCode);

    /**
     * 批量删除软件用户与设备码关联
     *
     * @param ids 需要删除的软件用户与设备码关联主键集合
     * @return 结果
     */
    public int deleteSysAppUserDeviceCodeByIds(Long[] ids);

    /**
     * 删除软件用户与设备码关联信息
     *
     * @param id 软件用户与设备码关联主键
     * @return 结果
     */
    public int deleteSysAppUserDeviceCodeById(Long id);

    /**
     * 修改状态
     *
     * @param appUserDeviceCode 信息
     * @return 结果
     */
    public int updateSysDeviceCodeStatus(SysAppUserDeviceCode appUserDeviceCode);

    /**
     * 查询软件用户与设备码关联
     *
     * @return 软件用户与设备码关联
     */
    public SysAppUserDeviceCode selectSysAppUserDeviceCodeByAppUserIdAndDeviceCodeId(Long appUserId, Long deviceCodeId);

    /**
     * 查询软件用户与设备码关联
     *
     * @return 软件用户与设备码关联
     */
    List<SysAppUserDeviceCode> selectSysAppUserDeviceCodeByAppUserId(Long appUserId);

    /**
     * 查询软件用户与设备码关联
     *
     * @return 软件用户与设备码关联
     */
    List<SysAppUserDeviceCode> selectSysAppUserDeviceCodeByDeviceCodeId(Long deviceCodeId);
}
