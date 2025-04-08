package com.ruoyi.common.license;

import cn.hutool.core.io.FileUtil;
import cn.hutool.system.SystemUtil;
import cn.hutool.system.oshi.OshiUtil;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.utils.AesCbcPKCS5PaddingUtil;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.common.utils.sign.Md5Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import oshi.hardware.NetworkIF;

import java.io.File;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
public class ServerInfo {

    public static void getServerInfo() {
        String hostIp = ServerInfo.getServerIp();
        Constants.IP_ADDRESS = Collections.singletonList(hostIp);
        if (StringUtils.isBlank(Constants.SERVER_SN)) {
            // 判断固定机器码文件是否存在
            String storePath = "";
            String fileName = Md5Utils.hash(SystemUtil.getUserInfo().getCurrentDir()).toLowerCase();
            if (SystemUtil.getOsInfo().getName().toLowerCase().contains("windows") || SystemUtil.getOsInfo().isWindows()) {
                storePath = "C:\\ProgramData\\hywlyz\\" + fileName;
            } else if (SystemUtil.getOsInfo().getName().toLowerCase().contains("linux") || SystemUtil.getOsInfo().isLinux()) {
                storePath = "/var/lib/hywlyz/" + fileName;
            } else {
                getServerSn(null);
                log.error("未知操作系统：{}，固定机器码失败", SystemUtil.getOsInfo().getName());
                return;
            }
            File file = new File(storePath);
            if(file.exists() && file.isFile()) {
                String s = FileUtil.readString(storePath, StandardCharsets.UTF_8);
                try {
                    Constants.SERVER_SN = AesCbcPKCS5PaddingUtil.decode(s, Constants.STORE_PASS);
                } catch (Exception e) {
                    log.error("固定机器码获取失败", e);
                    getServerSn(storePath);
                }
            } else {
                getServerSn(storePath);
            }
        }
    }

    private static void getServerSn(String storePath) {
        String hostIp = ServerInfo.getServerIp();
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
        String temp = "CORE|" + macAddress + serialNumber + processorID
                + SystemUtil.getUserInfo().getCurrentDir() + SystemUtil.getOsInfo().getName()
                + SystemUtil.getHostInfo().getName() + SystemUtil.getOsInfo().getArch()
                + SystemUtil.getJavaInfo().getVersion();
        //再将机器码加密成16位字符串
        Constants.SERVER_SN = com.ruoyi.common.utils.StringUtils.insertPer4Char(Md5Utils.hash(temp).toUpperCase());
        try {
            // 固定机器码，避免重启服务器后机器码变化
            if (StringUtils.isNotBlank(storePath)) {
                FileUtil.writeString(AesCbcPKCS5PaddingUtil.encode(Constants.SERVER_SN, Constants.STORE_PASS), storePath, StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            log.error("固定机器码失败", e);
        }
    }

    public static List<Inet4Address> getLocalIp4AddressFromNetworkInterface() throws SocketException {
        List<Inet4Address> addresses = new ArrayList<>(1);
        Enumeration e = NetworkInterface.getNetworkInterfaces();
        if (e == null) {
            return addresses;
        }
        while (e.hasMoreElements()) {
            NetworkInterface n = (NetworkInterface) e.nextElement();
            if (!isValidInterface(n)) {
                continue;
            }
            Enumeration ee = n.getInetAddresses();
            while (ee.hasMoreElements()) {
                InetAddress i = (InetAddress) ee.nextElement();
                if (isValidAddress(i)) {
                    addresses.add((Inet4Address) i);
                }
            }
        }
        return addresses;
    }

    /**
     * 过滤回环网卡、点对点网卡、非活动网卡、虚拟网卡并要求网卡名字是eth或ens开头
     *
     * @param ni 网卡
     * @return 如果满足要求则true，否则false
     */
    private static boolean isValidInterface(NetworkInterface ni) throws SocketException {
        return !ni.isLoopback() && !ni.isPointToPoint() && ni.isUp() && !ni.isVirtual()
                && (ni.getName().startsWith("eth") || ni.getName().startsWith("ens"));
    }

    /**
     * 判断是否是IPv4，并且内网地址并过滤回环地址.
     */
    private static boolean isValidAddress(InetAddress address) {
        return address instanceof Inet4Address && address.isSiteLocalAddress() && !address.isLoopbackAddress();
    }

    private static Optional<Inet4Address> getIpBySocket() throws SocketException {
        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            if (socket.getLocalAddress() instanceof Inet4Address) {
                return Optional.of((Inet4Address) socket.getLocalAddress());
            }
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    public static Optional<Inet4Address> getLocalIp4Address() throws SocketException {
        final List<Inet4Address> ipByNi = getLocalIp4AddressFromNetworkInterface();
        if (ipByNi.isEmpty() || ipByNi.size() > 1) {
            final Optional<Inet4Address> ipBySocketOpt = getIpBySocket();
            if (ipBySocketOpt.isPresent()) {
                return ipBySocketOpt;
            } else {
                return ipByNi.isEmpty() ? Optional.empty() : Optional.of(ipByNi.get(0));
            }
        }
        return Optional.of(ipByNi.get(0));
    }

    public static String getServerIp() {
        try {
            return getLocalIp4Address().map(Inet4Address::getHostAddress).orElse(IpUtils.getHostIp());
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "获取失败";
    }

}
