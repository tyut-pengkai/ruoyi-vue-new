package com.ruoyi.device.service.impl;

import java.util.List;

import com.ruoyi.agent.domain.AgentDevice;
import com.ruoyi.agent.service.IAgentDeviceService;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.device.mapper.DeviceUserMapper;
import com.ruoyi.device.domain.DeviceUser;
import com.ruoyi.device.service.IDeviceUserService;

/**
 * 用户设备关联Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-06-18
 */
@Service
public class DeviceUserServiceImpl implements IDeviceUserService 
{
    @Autowired
    private DeviceUserMapper deviceUserMapper;

    @Autowired
    private IAgentDeviceService agentDeviceService;

    /**
     * 查询用户设备关联
     * 
     * @param deviceId 用户设备关联主键
     * @return 用户设备关联
     */
    @Override
    public DeviceUser selectDeviceUserByDeviceId(Long deviceId)
    {
        return deviceUserMapper.selectDeviceUserByDeviceId(deviceId);
    }

    /**
     * 查询用户设备关联列表
     * 
     * @param deviceUser 用户设备关联
     * @return 用户设备关联
     */
    @Override
    public List<DeviceUser> selectDeviceUserList(DeviceUser deviceUser)
    {
        return deviceUserMapper.selectDeviceUserList(deviceUser);
    }

    /**
     * 新增用户设备关联
     * 
     * @param deviceUser 用户设备关联
     * @return 结果
     */
    @Override
    public int insertDeviceUser(DeviceUser deviceUser)
    {
        deviceUser.setCreateTime(DateUtils.getNowDate());
        return deviceUserMapper.insertDeviceUser(deviceUser);
    }

    /**
     * 修改用户设备关联
     * 
     * @param deviceUser 用户设备关联
     * @return 结果
     */
    @Override
    public int updateDeviceUser(DeviceUser deviceUser)
    {
        deviceUser.setUpdateTime(DateUtils.getNowDate());
        return deviceUserMapper.updateDeviceUser(deviceUser);
    }

    /**
     * 批量删除用户设备关联
     * 
     * @param deviceIds 需要删除的用户设备关联主键
     * @return 结果
     */
    @Override
    public int deleteDeviceUserByDeviceIds(Long[] deviceIds)
    {
        return deviceUserMapper.deleteDeviceUserByDeviceIds(deviceIds);
    }

    /**
     * 删除用户设备关联信息
     * 
     * @param deviceId 用户设备关联主键
     * @return 结果
     */
    @Override
    public int deleteDeviceUserByDeviceId(Long deviceId)
    {
        return deviceUserMapper.deleteDeviceUserByDeviceId(deviceId);
    }

    @Override
    public int bindDeviceToUser(Long userId, Long deviceId) {
        // 检查该设备是否已被任何用户绑定
        Integer count = deviceUserMapper.countByDeviceId(deviceId);
        if (count != null && count > 0) {
            // 已被绑定
            return -1;
        }

        // 绑定设备到用户
        int result = deviceUserMapper.insertDeviceUser(userId, deviceId);
        if (result > 0) {
            // 绑定默认的智能体
            AgentDevice agentDevice = new AgentDevice();
            agentDevice.setAgentId(1001L);
            agentDevice.setDeviceId(deviceId);
            agentDeviceService.insertAgentDevice(agentDevice);
        }

        return result;
    }

    @Override
    public int unbindDeviceFromUser(Long userId, Long deviceId) {

        // 解绑设备
        int result = deviceUserMapper.deleteByUserIdAndDeviceId(userId, deviceId);

        if (result > 0) {
            // 同时解绑设备上关联的智能体
            agentDeviceService.deleteAgentDeviceByDeviceId(deviceId);
        }
        return result;
    }
}
