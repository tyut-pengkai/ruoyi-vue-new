package com.ruoyi.common.license;

import cn.hutool.system.SystemUtil;
import cn.hutool.system.oshi.OshiUtil;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.common.utils.sign.Md5Utils;
import org.apache.commons.lang3.StringUtils;
import oshi.hardware.NetworkIF;

import java.util.Arrays;
import java.util.List;

public class ServerInfo {

    public static void getServerInfo() {
        if (StringUtils.isBlank(Constants.SERVER_SN)) {
            String hostIp = IpUtils.getHostIp();
            String macAddress = null;
            List<NetworkIF> networkIFs = OshiUtil.getNetworkIFs();
            for (NetworkIF n : networkIFs) {
                if (Arrays.asList(n.getIPv4addr()).contains(hostIp)) {
                    macAddress = n.getMacaddr();
                    break;
                }
            }
            //序列号
            String serialNumber = OshiUtil.getSystem().getSerialNumber();
            //处理器ID
            String processorID = OshiUtil.getProcessor().getProcessorIdentifier().getProcessorID();
            //组装 系统机器码 mac串+序列号+处理器ID+程序系统路径+系统名称+主机名+系统架构+环境版本号  -->机器码  可以自行增加硬件信息确保唯一性
            String temp = macAddress + serialNumber + processorID
                    + SystemUtil.getUserInfo().getCurrentDir() + SystemUtil.getOsInfo().getName() + SystemUtil.getHostInfo().getName() +
                    SystemUtil.getOsInfo().getArch() + SystemUtil.getJavaInfo().getVersion();
            //再将机器码加密成16位字符串
            Constants.SERVER_SN = com.ruoyi.common.utils.StringUtils.insertPer4Char(Md5Utils.hash(temp).toUpperCase());
            Constants.SERVER_IP = hostIp;
        }
    }

}
