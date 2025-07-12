package com.ruoyi.device.mapper;

import java.util.List;
import com.ruoyi.device.domain.DeviceInfo;
import org.apache.ibatis.annotations.Param;

/**
 * 设备信息Mapper接口
 * 
 * @author ruoyi
 * @date 2025-06-18
 */
public interface DeviceInfoMapper 
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
     * 删除设备信息
     * 
     * @param deviceId 设备信息主键
     * @return 结果
     */
    public int deleteDeviceInfoByDeviceId(Long deviceId);

    /**
     * 批量删除设备信息
     * 
     * @param deviceIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDeviceInfoByDeviceIds(Long[] deviceIds);

    /**
     * 根据用户ID和设备信息查询设备列表
     * @param userId 用户ID
     * @param deviceInfo 设备信息
     * @return 设备列表
     */
    public List<DeviceInfo> selectDeviceInfoListByUserId(@Param("userId") Long userId, @Param("deviceInfo") DeviceInfo deviceInfo);

    /**
     * 根据用户ID查询所有设备列表
     * @param userId 用户ID
     * @return 设备列表
     */
    public List<DeviceInfo> selectAllDeviceInfoByUserId(Long userId);

    /**
     * 通过设备编码或mac地址查找设备
     */
    DeviceInfo selectByCodeOrMac(@org.apache.ibatis.annotations.Param("deviceCode") String deviceCode, @org.apache.ibatis.annotations.Param("macAddr") String macAddr);

    /**
     * 根据用户ID查询设备列表
     * @param userId 用户ID
     * @return 设备列表
     */
    public List<DeviceInfo> selectDeviceInfoListByUserId(Long userId);
}
