package com.ruoyi.license;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.license.bo.LicenseCheckModel;
import com.ruoyi.common.utils.os.AbstractServerInfo;
import com.ruoyi.common.utils.os.LinuxServerInfo;
import com.ruoyi.common.utils.os.WindowsServerInfo;
import com.ruoyi.license.domain.LicenseCreatorParam;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author huangchen@deepglint.com
 * @desc
 * @date 2019/3/14 13:55
 */
@SpringBootTest
@EnableAutoConfiguration
@Slf4j
public class LicenseTest {

    /**
     * test 获取客户机信息
     */
    @Test
    public void testGetServerInfo() {
        String osName = System.getProperty("os.name").toLowerCase();
        AbstractServerInfo abstractServerInfo;
        //根据不同操作系统类型选择不同的数据获取方法
        if (osName.startsWith("windows")) {
            abstractServerInfo = new WindowsServerInfo();
        } else if (osName.startsWith("linux")) {
            abstractServerInfo = new LinuxServerInfo();
        } else {//其他服务器类型
            abstractServerInfo = new LinuxServerInfo();
        }
        log.info("客户机信息：{}", JSONObject.toJSONString(LicenseCheckModel.installServerInfo(abstractServerInfo)));
    }

    /**
     * test 生成授权
     */
    @Test
    public void testGenerateLicense() {
        LicenseCreatorParam param = new LicenseCreatorParam();
        param.setSubject("license_sub");
        param.setPrivateAlias("privateKey");
        param.setKeyPass("deepglint_key_pwd123");
        param.setStorePass("deepglint_store_pwd123");
        param.setLicensePath("D:/dev/code-bak/license/license.lic");
        param.setPrivateKeysStorePath("D:/dev/jdk1.8_64/bin/privateKeys.keystore");
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            param.setIssuedTime(dateFormat.parse("2019-03-13 00:00:01"));
            param.setExpiryTime(dateFormat.parse("2019-03-16 15:30:00"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        LicenseCheckModel licenseCheckModel = new LicenseCheckModel();
        List<String> ipList = new ArrayList<>();
        ipList.add("192.168.153.155");
        licenseCheckModel.setIpAddress(ipList);
        List<String> macList = new ArrayList<>();
        macList.add("B0-52-16-27-F5-EF");
        licenseCheckModel.setMacAddress(macList);
        licenseCheckModel.setCpuSerial("178BFBFF00660F51");
        licenseCheckModel.setMainBoardSerial("L1HF7B400HZ");
        param.setLicenseCheckModel(licenseCheckModel);

        LicenseCreator licenseCreator = new LicenseCreator(param);
        licenseCreator.generateLicense();
    }
}
