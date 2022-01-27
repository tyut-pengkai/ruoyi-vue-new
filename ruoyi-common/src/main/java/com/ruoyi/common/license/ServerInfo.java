package com.ruoyi.common.license;

import com.ruoyi.common.license.bo.LicenseCheckModel;
import com.ruoyi.common.utils.os.AbstractServerInfo;
import com.ruoyi.common.utils.os.LinuxServerInfo;
import com.ruoyi.common.utils.os.WindowsServerInfo;
import org.apache.commons.lang3.StringUtils;

public class ServerInfo {

    public static LicenseCheckModel getServerInfo() {
        return getServerInfo(null);
    }

    public static LicenseCheckModel getServerInfo(String osName) {
        //操作系统类型
        if (StringUtils.isBlank(osName)) {
            osName = System.getProperty("os.name");
        }
        osName = osName.toLowerCase();
        AbstractServerInfo abstractServerInfo;
        //根据不同操作系统类型选择不同的数据获取方法
        if (osName.startsWith("windows")) {
            abstractServerInfo = new WindowsServerInfo();
        } else if (osName.startsWith("linux")) {
            abstractServerInfo = new LinuxServerInfo();
        } else {//其他服务器类型
            abstractServerInfo = new LinuxServerInfo();
        }
        return LicenseCheckModel.installServerInfo(abstractServerInfo);
    }

}
