package com.ruoyi.device.mapper;

import java.util.List;
import com.ruoyi.device.domain.DeviceUser;

/**
 * 用户设备关联Mapper接口
 * 
 * @author ruoyi
 * @date 2025-06-18
 */
public interface DeviceUserMapper 
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
     * 删除用户设备关联
     * 
     * @param deviceId 用户设备关联主键
     * @return 结果
     */
    public int deleteDeviceUserByDeviceId(Long deviceId);

    /**
     * 批量删除用户设备关联
     * 
     * @param deviceIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDeviceUserByDeviceIds(Long[] deviceIds);

    /**
     * 查询用户和设备是否已绑定
     */
    Integer countByUserIdAndDeviceId(@org.apache.ibatis.annotations.Param("userId") Long userId, @org.apache.ibatis.annotations.Param("deviceId") Long deviceId);

    /**
     * 插入用户设备绑定关系
     */
    int insertDeviceUser(@org.apache.ibatis.annotations.Param("userId") Long userId, @org.apache.ibatis.annotations.Param("deviceId") Long deviceId);

    /**
     * 按用户和设备解绑
     */
    int deleteByUserIdAndDeviceId(@org.apache.ibatis.annotations.Param("userId") Long userId, @org.apache.ibatis.annotations.Param("deviceId") Long deviceId);

    /**
     * 统计某设备是否已被任何用户绑定
     */
    Integer countByDeviceId(@org.apache.ibatis.annotations.Param("deviceId") Long deviceId);
}
