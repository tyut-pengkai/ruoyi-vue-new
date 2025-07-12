package com.ruoyi.device.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.device.mapper.DeviceHourMapper;
import com.ruoyi.device.domain.DeviceHour;
import com.ruoyi.device.service.IDeviceHourService;

/**
 * 设备时长Service业务层处理
 * 
 * @author auto
 * @date 2025-06-25
 */
@Service
public class DeviceHourServiceImpl implements IDeviceHourService 
{
    @Autowired
    private DeviceHourMapper deviceHourMapper;

    /**
     * 查询设备时长
     * 
     * @param deviceId 设备时长主键
     * @return 设备时长
     */
    @Override
    public DeviceHour selectDeviceHourByDeviceId(Long deviceId)
    {
        return deviceHourMapper.selectDeviceHourByDeviceId(deviceId);
    }

    /**
     * 查询设备时长列表
     * 
     * @param deviceHour 设备时长
     * @return 设备时长
     */
    @Override
    public List<DeviceHour> selectDeviceHourList(DeviceHour deviceHour)
    {
        return deviceHourMapper.selectDeviceHourList(deviceHour);
    }

    /**
     * 新增设备时长
     * 
     * @param deviceHour 设备时长
     * @return 结果
     */
    @Override
    public int insertDeviceHour(DeviceHour deviceHour)
    {
        deviceHour.setCreateTime(DateUtils.getNowDate());
        return deviceHourMapper.insertDeviceHour(deviceHour);
    }

    /**
     * 修改设备时长
     * 
     * @param deviceHour 设备时长
     * @return 结果
     */
    @Override
    public int updateDeviceHour(DeviceHour deviceHour)
    {
        deviceHour.setUpdateTime(DateUtils.getNowDate());
        return deviceHourMapper.updateDeviceHour(deviceHour);
    }

    /**
     * 批量删除设备时长
     * 
     * @param deviceIds 需要删除的设备时长主键
     * @return 结果
     */
    @Override
    public int deleteDeviceHourByDeviceIds(Long[] deviceIds)
    {
        return deviceHourMapper.deleteDeviceHourByDeviceIds(deviceIds);
    }

    /**
     * 删除设备时长信息
     * 
     * @param deviceId 设备时长主键
     * @return 结果
     */
    @Override
    public int deleteDeviceHourByDeviceId(Long deviceId)
    {
        return deviceHourMapper.deleteDeviceHourByDeviceId(deviceId);
    }
}
