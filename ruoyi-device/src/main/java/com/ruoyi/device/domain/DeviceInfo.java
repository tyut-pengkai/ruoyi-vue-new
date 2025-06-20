package com.ruoyi.device.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 设备信息对象 device_info
 * 
 * @author ruoyi
 * @date 2025-06-18
 */
public class DeviceInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 设备id */
    private Long deviceId;

    /** 设备编码 */
    @Excel(name = "设备编码")
    private String deviceCode;

    /** 设备名称 */
    @Excel(name = "设备名称")
    private String deviceName;

    /** mxc地址 */
    @Excel(name = "mxc地址")
    private String mxcAddr;

    /** IP地址 */
    @Excel(name = "IP地址")
    private String ipAddr;

    /** 所属区域 */
    @Excel(name = "所属区域")
    private String area;

    /** 在线状态（0线上 1下线） */
    @Excel(name = "在线状态", readConverterExp = "0=线上,1=下线", combo = {"线上", "下线"})
    private String onlineStatus;

    /** 固件版本 */
    @Excel(name = "固件版本")
    private String otaVersion;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用", combo = {"正常", "停用"})
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setDeviceId(Long deviceId) 
    {
        this.deviceId = deviceId;
    }

    public Long getDeviceId() 
    {
        return deviceId;
    }

    public void setDeviceCode(String deviceCode) 
    {
        this.deviceCode = deviceCode;
    }

    public String getDeviceCode() 
    {
        return deviceCode;
    }

    public void setDeviceName(String deviceName) 
    {
        this.deviceName = deviceName;
    }

    public String getDeviceName() 
    {
        return deviceName;
    }

    public void setMxcAddr(String mxcAddr) 
    {
        this.mxcAddr = mxcAddr;
    }

    public String getMxcAddr() 
    {
        return mxcAddr;
    }

    public void setIpAddr(String ipAddr) 
    {
        this.ipAddr = ipAddr;
    }

    public String getIpAddr() 
    {
        return ipAddr;
    }

    public void setArea(String area) 
    {
        this.area = area;
    }

    public String getArea() 
    {
        return area;
    }

    public void setOnlineStatus(String onlineStatus) 
    {
        this.onlineStatus = onlineStatus;
    }

    public String getOnlineStatus() 
    {
        return onlineStatus;
    }

    public void setOtaVersion(String otaVersion) 
    {
        this.otaVersion = otaVersion;
    }

    public String getOtaVersion() 
    {
        return otaVersion;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("deviceId", getDeviceId())
            .append("deviceCode", getDeviceCode())
            .append("deviceName", getDeviceName())
            .append("mxcAddr", getMxcAddr())
            .append("ipAddr", getIpAddr())
            .append("area", getArea())
            .append("onlineStatus", getOnlineStatus())
            .append("otaVersion", getOtaVersion())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
