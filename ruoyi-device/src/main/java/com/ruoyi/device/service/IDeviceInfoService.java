package com.ruoyi.device.service;

import java.util.List;
import com.ruoyi.device.domain.DeviceInfo;

/**
 * 设备信息Service接口
 * 
 * @author ruoyi
 * @date 2025-06-18
 */
public interface IDeviceInfoService 
{
    /**
     * 查询设备信息
     * 
     * @param deviceId 设备信息主键
     * @return 设备信息
     */
    public DeviceInfo selectDeviceInfoByDeviceId(Long deviceId);

    /**
     * 查询设备信息列表
     * 
     * @param deviceInfo 设备信息
     * @return 设备信息集合
     */
    public List<DeviceInfo> selectDeviceInfoList(DeviceInfo deviceInfo);

    /**
     * 新增设备信息
     * 
     * @param deviceInfo 设备信息
     * @return 结果
     */
    public int insertDeviceInfo(DeviceInfo deviceInfo);

    /**
     * 修改设备信息
     * 
     * @param deviceInfo 设备信息
     * @return 结果
     */
    public int updateDeviceInfo(DeviceInfo deviceInfo);

    /**
     * 批量删除设备信息
     * 
     * @param deviceIds 需要删除的设备信息主键集合
     * @return 结果
     */
    public int deleteDeviceInfoByDeviceIds(Long[] deviceIds);

    /**
     * 删除设备信息信息
     * 
     * @param deviceId 设备信息主键
     * @return 结果
     */
    public int deleteDeviceInfoByDeviceId(Long deviceId);

    /**
     * 根据用户查询设备信息列表
     * @param userId 用户ID
     * @param isAdmin 是否管理员
     * @param deviceInfo 查询条件
     * @return 设备信息集合
     */
    public List<DeviceInfo> selectDeviceInfoListByUser(Long userId, boolean isAdmin, DeviceInfo deviceInfo);

    /**
     * 通过设备编码或mac地址查找设备
     */
    DeviceInfo selectByCodeOrMac(String deviceCode, String macAddr);

    /**
     * 导入设备信息
     */
    String importDevice(List<DeviceInfo> deviceList, boolean updateSupport, String operName);
}
