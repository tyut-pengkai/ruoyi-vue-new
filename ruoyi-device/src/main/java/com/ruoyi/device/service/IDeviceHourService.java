package com.ruoyi.device.service;

import java.util.List;
import com.ruoyi.device.domain.DeviceHour;

/**
 * 设备时长Service接口
 * 
 * @author auto
 * @date 2025-06-25
 */
public interface IDeviceHourService 
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
     * 批量删除设备时长
     * 
     * @param deviceIds 需要删除的设备时长主键集合
     * @return 结果
     */
    public int deleteDeviceHourByDeviceIds(Long[] deviceIds);

    /**
     * 删除设备时长信息
     * 
     * @param deviceId 设备时长主键
     * @return 结果
     */
    public int deleteDeviceHourByDeviceId(Long deviceId);
}
