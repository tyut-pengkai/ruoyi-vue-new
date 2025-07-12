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
import com.ruoyi.device.mapper.DeviceHourMapper;
import com.ruoyi.system.service.ISysConfigService;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.device.domain.DeviceHour;
import com.ruoyi.device.domain.DeviceInfo;

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
    private DeviceHourMapper deviceHourMapper;

    @Autowired
    private ISysConfigService configService;

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
    @Transactional
    public int bindDeviceToUser(Long userId, DeviceInfo device) {
        Long deviceId = device.getDeviceId();
        // 检查设备是否已被任何用户绑定
        DeviceUser existingBinding = deviceUserMapper.selectDeviceUserByDeviceId(deviceId);
        if (existingBinding != null) {
            // 如果已被当前用户绑定，则返回成功（幂等性）
            if (existingBinding.getUserId().equals(userId)) {
                return 1;
            }
            // 如果已被其他用户绑定，则返回错误标识
            return -1;
        }

        // 绑定设备
        DeviceUser newDeviceUser = new DeviceUser();
        newDeviceUser.setUserId(userId);
        newDeviceUser.setDeviceId(deviceId);
        int result = deviceUserMapper.insertDeviceUser(newDeviceUser);

        if(result > 0) {
            // 绑定默认的智能体
            AgentDevice agentDevice = new AgentDevice();
            agentDevice.setAgentId(1001L);
            agentDevice.setDeviceId(deviceId);
            agentDeviceService.insertAgentDevice(agentDevice);
            
            // 初始化赠送设备时长,时长从配置参数device.hour.init_free_hours获取. 先判断初始绑定(设备时长表是否有记录),非初始绑定初始化,不赠送
            // 检查设备时长记录是否已存在
            DeviceHour deviceHour = deviceHourMapper.selectDeviceHourByDeviceId(deviceId);
            if (deviceHour == null) {
                // 如果不存在，则进行初始化
                String freeHoursStr = configService.selectConfigByKey("device.hour.init_free_hours");
                int freeHours = 0;
                try {
                    if (freeHoursStr != null && !freeHoursStr.isEmpty()){
                        freeHours = Integer.parseInt(freeHoursStr);
                    }
                } catch (NumberFormatException e) {
                   // log error or handle, here we default to 0
                }

                DeviceHour newDeviceHour = new DeviceHour();
                newDeviceHour.setDeviceId(deviceId);
                newDeviceHour.setDeviceName(device.getDeviceName());
                newDeviceHour.setAvailFreeHours((long) freeHours);
                newDeviceHour.setAvailProHours(0L);
                newDeviceHour.setUsedFreeHours(0L);
                newDeviceHour.setUsedProHours(0L);
                deviceHourMapper.insertDeviceHour(newDeviceHour);
            }
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
