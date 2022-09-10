package com.ruoyi.license.controller;

import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.PathUtils;
import com.ruoyi.license.domain.LicenseCreatorParam;
import com.ruoyi.license.support.LicenseCreator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

/**
 * @author zwgu
 * @desc 用于生成证书文件，不能放在给客户部署的代码里
 * @date 2019/3/14 10:33
 */
@RestController
@RequestMapping("/license")
public class LicenseController {

    /**
     * 证书subject
     */
    @Value("${license.subject}")
    private String subject;

    /**
     * 生成授权文件
     *
     * @param param 生成授权需要的参数
     *              {
     *              "subject": "license_sub",
     *              "privateAlias": "privateKey",
     *              "keyPass": "deepglint_key_pwd123",
     *              "storePass": "deepglint_store_pwd123",
     *              "licensePath": "D:/dev/code-bak/license/license.lic",
     *              "privateKeysStorePath": "D:/dev/jdk1.8_64/bin/privateKeys.keystore",
     *              "issuedTime": "2019-03-13 00:00:01",
     *              "expiryTime": "2019-03-16 15:30:00",
     *              "licenseCheckModel": {
     *              "ipAddress": ["2001:0:2841:aa90:34fb:8e63:c5ce:e345", "192.168.153.155"],
     *              "macAddress": ["00-00-00-00-00-00-00-E0","B0-52-16-27-F5-EF"],
     *              "cpuSerial": "178BFBFF00660F51",
     *              "mainBoardSerial": "L1HF7B400HZ"
     *              }
     *              }
     */
    /*{
        "licensePath": "D:/license.lic",
        "issuedTime": "2022-01-01 00:00:00",
        "expiryTime": "2023-01-01 00:00:00",
        "description": "",
        "licenseCheckModel": {
            "licenseTo": "COORDSOFT.COM",
            "sn": "90EB-6C02-9D14-D763-2AD7-5167-B374-C084",
            "licenseType": "个人版",
            "appLimit": 100,
            "maxOnline": 100,
            "ipAddress": ["*"],
            "domainName": ["*"],
            "moduleName": ["*"]
        }
    }*/
    @RequestMapping(value = "/generateLicense", produces = {MediaType.APPLICATION_JSON_VALUE})
    public AjaxResult generateLicense(@RequestBody LicenseCreatorParam param) {
        try {
            if (StringUtils.isBlank(param.getLicensePath())) {
                String filePath = RuoYiConfig.getDownloadPath() + "license.lic";
                File licenseFile = new File(filePath);
                if (!licenseFile.getParentFile().exists()) {
                    licenseFile.getParentFile().mkdirs();
                }
                param.setLicensePath(licenseFile.getCanonicalPath());
            }
            if (StringUtils.isBlank(param.getSubject())) {
                param.setSubject(subject);
            }
            if (StringUtils.isBlank(param.getPrivateAlias())) {
                param.setPrivateAlias("privateKey");
            }
            param.setKeyPass(Constants.STORE_PASS);
            param.setStorePass(Constants.STORE_PASS);
            param.setPrivateKeysStorePath(PathUtils.getResourceFile("privateKeys.keystore").getCanonicalPath());
            LicenseCreator licenseCreator = new LicenseCreator(param);
            licenseCreator.generateLicense();
            return AjaxResult.success(param.getLicensePath());
        } catch (Exception e) {
            return AjaxResult.error("License生成失败！" + e.getMessage());
        }
    }
}