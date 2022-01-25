package com.ruoyi.common.license.bo;

import com.ruoyi.common.utils.os.AbstractServerInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.List;

/**
 * @author huangchen@deepglint.com
 * @desc 自定义需要校验的License参数
 * @date 2019/3/14 10:00
 */
@Setter
@Getter
@ToString
@Slf4j
public class LicenseCheckModel implements Serializable {

    /**
     * 可被允许的IP地址
     */
    private List<String> ipAddress;

    /**
     * 可被允许的MAC地址
     */
    private List<String> macAddress;

    /**
     * 可被允许的CPU序列号
     */
    private String cpuSerial;

    /**
     * 可被允许的主板序列号
     */
    private String mainBoardSerial;

    public static LicenseCheckModel installServerInfo(AbstractServerInfo serverInfo) {
        LicenseCheckModel result = new LicenseCheckModel();
        try {
            result.setIpAddress(serverInfo.getIpAddress());
            result.setMacAddress(serverInfo.getMacAddress());
            result.setCpuSerial(serverInfo.getCPUSerial());
            result.setMainBoardSerial(serverInfo.getMainBoardSerial());
        } catch (Exception e) {
            log.error("获取服务器硬件信息失败", e);
        }
        return result;
    }
}
