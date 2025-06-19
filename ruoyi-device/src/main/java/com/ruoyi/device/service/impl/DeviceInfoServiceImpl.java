package com.ruoyi.device.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.device.mapper.DeviceInfoMapper;
import com.ruoyi.device.domain.DeviceInfo;
import com.ruoyi.device.service.IDeviceInfoService;

/**
 * 设备信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-06-18
 */
@Service
public class DeviceInfoServiceImpl implements IDeviceInfoService 
{
    @Autowired
    private DeviceInfoMapper deviceInfoMapper;

    /**
     * 查询设备信息
     * 
     * @param deviceId 设备信息主键
     * @return 设备信息
     */
    @Override
    public DeviceInfo selectDeviceInfoByDeviceId(Long deviceId)
    {
        return deviceInfoMapper.selectDeviceInfoByDeviceId(deviceId);
    }

    /**
     * 查询设备信息列表
     * 
     * @param deviceInfo 设备信息
     * @return 设备信息
     */
    @Override
    public List<DeviceInfo> selectDeviceInfoList(DeviceInfo deviceInfo)
    {
        return deviceInfoMapper.selectDeviceInfoList(deviceInfo);
    }

    /**
     * 新增设备信息
     * 
     * @param deviceInfo 设备信息
     * @return 结果
     */
    @Override
    public int insertDeviceInfo(DeviceInfo deviceInfo)
    {
        deviceInfo.setCreateTime(DateUtils.getNowDate());
        return deviceInfoMapper.insertDeviceInfo(deviceInfo);
    }

    /**
     * 修改设备信息
     * 
     * @param deviceInfo 设备信息
     * @return 结果
     */
    @Override
    public int updateDeviceInfo(DeviceInfo deviceInfo)
    {
        deviceInfo.setUpdateTime(DateUtils.getNowDate());
        return deviceInfoMapper.updateDeviceInfo(deviceInfo);
    }

    /**
     * 批量删除设备信息
     * 
     * @param deviceIds 需要删除的设备信息主键
     * @return 结果
     */
    @Override
    public int deleteDeviceInfoByDeviceIds(Long[] deviceIds)
    {
        return deviceInfoMapper.deleteDeviceInfoByDeviceIds(deviceIds);
    }

    /**
     * 删除设备信息信息
     * 
     * @param deviceId 设备信息主键
     * @return 结果
     */
    @Override
    public int deleteDeviceInfoByDeviceId(Long deviceId)
    {
        return deviceInfoMapper.deleteDeviceInfoByDeviceId(deviceId);
    }

    @Override
    public List<DeviceInfo> selectDeviceInfoListByUser(Long userId, boolean isAdmin, DeviceInfo deviceInfo) {
        if (isAdmin) {
            return deviceInfoMapper.selectDeviceInfoList(deviceInfo);
        } else {
            return deviceInfoMapper.selectDeviceInfoListByUserId(userId, deviceInfo);
        }
    }

    @Override
    public DeviceInfo selectByCodeOrMxc(String deviceCode, String mxcAddr) {
        return deviceInfoMapper.selectByCodeOrMxc(deviceCode, mxcAddr);
    }
}
