package com.ruoyi.device.service;

import java.util.List;
import com.ruoyi.device.domain.DeviceUser;
import com.ruoyi.device.domain.DeviceInfo;

/**
 * 用户设备关联Service接口
 * 
 * @author ruoyi
 * @date 2025-06-18
 */
public interface IDeviceUserService 
{
    /**
     * 查询用户设备关联
     * 
     * @param deviceId 用户设备关联主键
     * @return 用户设备关联
     */
    public DeviceUser selectDeviceUserByDeviceId(Long deviceId);

    /**
     * 查询用户设备关联列表
     * 
     * @param deviceUser 用户设备关联
     * @return 用户设备关联集合
     */
    public List<DeviceUser> selectDeviceUserList(DeviceUser deviceUser);

    /**
     * 新增用户设备关联
     * 
     * @param deviceUser 用户设备关联
     * @return 结果
     */
    public int insertDeviceUser(DeviceUser deviceUser);

    /**
     * 修改用户设备关联
     * 
     * @param deviceUser 用户设备关联
     * @return 结果
     */
    public int updateDeviceUser(DeviceUser deviceUser);

    /**
     * 批量删除用户设备关联
     * 
     * @param deviceIds 需要删除的用户设备关联主键集合
     * @return 结果
     */
    public int deleteDeviceUserByDeviceIds(Long[] deviceIds);

    /**
     * 删除用户设备关联信息
     * 
     * @param deviceId 用户设备关联主键
     * @return 结果
     */
    public int deleteDeviceUserByDeviceId(Long deviceId);

    /**
     * 绑定设备到用户同时绑定默认音色(智能体)，并初始化设备时长
     *
     * @param userId 用户ID
     * @param device 设备信息
     * @return 结果
     */
    int bindDeviceToUser(Long userId, DeviceInfo device);

    /**
     * 解绑设备同时解绑音色(智能体)
     */
    int unbindDeviceFromUser(Long userId, Long deviceId);
}
