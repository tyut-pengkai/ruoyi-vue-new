package com.ruoyi.device.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 设备时长对象 device_hour
 * 
 * @author auto
 * @date 2025-06-25
 */
public class DeviceHour extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 设备id */
    private Long deviceId;

    /** 设备名称 */
    @Excel(name = "设备名称")
    private String deviceName;

    /** 可用免费时长 */
    @Excel(name = "可用免费时长")
    private Long availFreeHours;

    /** 可用Pro时长 */
    @Excel(name = "可用Pro时长")
    private Long availProHours;

    /** 已用免费时长 */
    @Excel(name = "已用免费时长")
    private Long usedFreeHours;

    /** 已用Pro时长 */
    @Excel(name = "已用Pro时长")
    private Long usedProHours;

    public void setDeviceId(Long deviceId) 
    {
        this.deviceId = deviceId;
    }

    public Long getDeviceId() 
    {
        return deviceId;
    }

    public void setDeviceName(String deviceName) 
    {
        this.deviceName = deviceName;
    }

    public String getDeviceName() 
    {
        return deviceName;
    }

    public void setAvailFreeHours(Long availFreeHours) 
    {
        this.availFreeHours = availFreeHours;
    }

    public Long getAvailFreeHours() 
    {
        return availFreeHours;
    }

    public void setAvailProHours(Long availProHours) 
    {
        this.availProHours = availProHours;
    }

    public Long getAvailProHours() 
    {
        return availProHours;
    }

    public void setUsedFreeHours(Long usedFreeHours) 
    {
        this.usedFreeHours = usedFreeHours;
    }

    public Long getUsedFreeHours() 
    {
        return usedFreeHours;
    }

    public void setUsedProHours(Long usedProHours) 
    {
        this.usedProHours = usedProHours;
    }

    public Long getUsedProHours() 
    {
        return usedProHours;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("deviceId", getDeviceId())
            .append("deviceName", getDeviceName())
            .append("availFreeHours", getAvailFreeHours())
            .append("availProHours", getAvailProHours())
            .append("usedFreeHours", getUsedFreeHours())
            .append("usedProHours", getUsedProHours())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
