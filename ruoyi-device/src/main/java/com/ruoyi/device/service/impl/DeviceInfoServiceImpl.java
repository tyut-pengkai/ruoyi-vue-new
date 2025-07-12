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
    public DeviceInfo selectByCodeOrMac(String deviceCode, String macAddr) {
        return deviceInfoMapper.selectByCodeOrMac(deviceCode, macAddr);
    }

    @Override
    public String importDevice(List<DeviceInfo> deviceList, boolean updateSupport, String operName) {
        if (deviceList == null || deviceList.isEmpty()) {
            throw new RuntimeException("导入设备数据不能为空！");
        }
        int successNum = 0, failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (DeviceInfo device : deviceList) {
            try {
                // 仅用mac_addr唯一性校验
                DeviceInfo exist = deviceInfoMapper.selectByCodeOrMac(null, device.getMacAddr());
                if (exist == null) {
                    device.setCreateBy(operName);
                    deviceInfoMapper.insertDeviceInfo(device);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、设备 " + device.getDeviceCode() + " 导入成功");
                } else if (updateSupport) {
                    device.setDeviceId(exist.getDeviceId());
                    device.setUpdateBy(operName);
                    deviceInfoMapper.updateDeviceInfo(device);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、设备 " + device.getDeviceCode() + " 更新成功");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、设备 " + device.getDeviceCode() + " 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、设备 " + device.getDeviceCode() + " 导入失败：" + e.getMessage();
                failureMsg.append(msg);
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new RuntimeException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

    @Override
    public List<DeviceInfo> selectAllDeviceInfoByUserId(Long userId) {
        return deviceInfoMapper.selectAllDeviceInfoByUserId(userId);
    }

    @Override
    public List<DeviceInfo> selectDeviceInfoListByUserId(Long userId, DeviceInfo deviceInfo) {
        return deviceInfoMapper.selectDeviceInfoListByUserId(userId, deviceInfo);
    }
}
