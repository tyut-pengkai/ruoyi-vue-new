package com.ruoyi.device.mapper;

import java.util.List;
import com.ruoyi.device.domain.DeviceHour;

/**
 * 设备时长Mapper接口
 * 
 * @author auto
 * @date 2025-06-25
 */
public interface DeviceHourMapper 
{
    /**
     * 查询设备时长
     * 
     * @param deviceId 设备时长主键
     * @return 设备时长
     */
    public DeviceHour selectDeviceHourByDeviceId(Long deviceId);

    /**
     * 查询设备时长列表
     * 
     * @param deviceHour 设备时长
     * @return 设备时长集合
     */
    public List<DeviceHour> selectDeviceHourList(DeviceHour deviceHour);

    /**
     * 新增设备时长
     * 
     * @param deviceHour 设备时长
     * @return 结果
     */
    public int insertDeviceHour(DeviceHour deviceHour);

    /**
     * 修改设备时长
     * 
     * @param deviceHour 设备时长
     * @return 结果
     */
    public int updateDeviceHour(DeviceHour deviceHour);

    /**
     * 删除设备时长
     * 
     * @param deviceId 设备时长主键
     * @return 结果
     */
    public int deleteDeviceHourByDeviceId(Long deviceId);

    /**
     * 批量删除设备时长
     * 
     * @param deviceIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDeviceHourByDeviceIds(Long[] deviceIds);
}
