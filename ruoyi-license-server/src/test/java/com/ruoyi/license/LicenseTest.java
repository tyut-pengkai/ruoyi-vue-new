package com.ruoyi.license;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.license.ServerInfo;
import com.ruoyi.common.license.bo.LicenseCheckModel;
import com.ruoyi.license.domain.LicenseCreatorParam;
import de.schlichtherle.util.ObfuscatedString;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author zwgu
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
        //根据不同操作系统类型选择不同的数据获取方法
        ServerInfo.getServerInfo();
        log.info("客户机信息：{}", JSONObject.toJSONString(Constants.LICENSE_CONTENT));
    }

    /**
     * test 生成授权
     */
    @Test
    public void testGenerateLicense() throws Exception {
        LicenseCreatorParam param = new LicenseCreatorParam();
        param.setSubject("license_sub");
        param.setConsumerAmount(1);
        param.setConsumerType("1");
//        param.setPrivateAlias("privateKey");
//        param.setKeyPass("deepglint_key_pwd123");
//        param.setStorePass("deepglint_store_pwd123");
//        param.setLicensePath("D:/dev/code-bak/license/license.lic");
//        param.setPrivateKeysStorePath("D:/dev/jdk1.8_64/bin/privateKeys.keystore");
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
        licenseCheckModel.setAppLimit(100);
        licenseCheckModel.setDomainName(Arrays.asList("www.baidu.com", "abc.com"));
        licenseCheckModel.setSn("1234");
        param.setLicenseCheckModel(licenseCheckModel);

        LicenseCreator licenseCreator = new LicenseCreator(param);
        licenseCreator.generateLicense();
    }

    @Test
    public void obfuscatedString() {
        String str = "license.lic";
        String result = ObfuscatedString.obfuscate(str);
        System.out.format("KEYSTORE-FILENAME:  %s\n", result);
    }

}
