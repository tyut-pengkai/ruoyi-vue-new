package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SysAppUserDeviceCode;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 软件用户与设备码关联Mapper接口
 *
 * @author zwgu
 * @date 2021-12-18
 */
@Repository
public interface SysAppUserDeviceCodeMapper {
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
     * 删除软件用户与设备码关联
     *
     * @param id 软件用户与设备码关联主键
     * @return 结果
     */
    public int deleteSysAppUserDeviceCodeById(Long id);

    /**
     * 批量删除软件用户与设备码关联
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysAppUserDeviceCodeByIds(Long[] ids);
}
