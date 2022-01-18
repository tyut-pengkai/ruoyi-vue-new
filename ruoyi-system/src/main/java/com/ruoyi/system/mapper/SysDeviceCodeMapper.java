package com.ruoyi.system.mapper;

import com.ruoyi.common.core.domain.entity.SysDeviceCode;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 设备码管理Mapper接口
 *
 * @author zwgu
 * @date 2021-12-06
 */
@Repository
public interface SysDeviceCodeMapper 
{
    /**
     * 查询设备码管理
     *
     * @param deviceCodeId 设备码管理主键
     * @return 设备码管理
     */
    public SysDeviceCode selectSysDeviceCodeByDeviceCodeId(Long deviceCodeId);

    /**
     * 查询设备码管理列表
     *
     * @param sysDeviceCode 设备码管理
     * @return 设备码管理集合
     */
    public List<SysDeviceCode> selectSysDeviceCodeList(SysDeviceCode sysDeviceCode);

    /**
     * 新增设备码管理
     *
     * @param sysDeviceCode 设备码管理
     * @return 结果
     */
    public int insertSysDeviceCode(SysDeviceCode sysDeviceCode);

    /**
     * 修改设备码管理
     *
     * @param sysDeviceCode 设备码管理
     * @return 结果
     */
    public int updateSysDeviceCode(SysDeviceCode sysDeviceCode);

    /**
     * 删除设备码管理
     *
     * @param deviceCodeId 设备码管理主键
     * @return 结果
     */
    public int deleteSysDeviceCodeByDeviceCodeId(Long deviceCodeId);

    /**
     * 批量删除设备码管理
     *
     * @param deviceCodeIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysDeviceCodeByDeviceCodeIds(Long[] deviceCodeIds);

    /**
     * 查询设备码
     *
     * @param deviceCode 设备码
     * @return 设备码
     */
    public SysDeviceCode selectSysDeviceCodeByDeviceCode(String deviceCode);
}
