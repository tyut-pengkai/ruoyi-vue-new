package com.ruoyi.license.controller;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.PathUtils;
import com.ruoyi.license.LicenseCreator;
import com.ruoyi.license.domain.LicenseCreatorParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author huangchen@deepglint.com
 * @desc 用于生成证书文件，不能放在给客户部署的代码里
 * @date 2019/3/14 10:33
 */
@RestController
@RequestMapping("/license")
public class LicenseCreatorController {

    /**
     * 证书生成路径
     */
    @Value("${license.licensePath}")
    private String licensePath;
    /**
     * 证书subject
     */
    @Value("${license.subject}")
    private String subject;

    /**
     * 私钥别称
     */
    @Value("${license.privateAlias}")
    private String privateAlias;

    /**
     * 生成授权文件
     *
     * @param param 生成授权需要的参数
     *          {
     *              "subject": "license_sub",
     *              "privateAlias": "privateKey",
     *              "keyPass": "deepglint_key_pwd123",
     *              "storePass": "deepglint_store_pwd123",
     *              "licensePath": "D:/dev/code-bak/license/license.lic",
     *              "privateKeysStorePath": "D:/dev/jdk1.8_64/bin/privateKeys.keystore",
     *              "issuedTime": "2019-03-13 00:00:01",
     *              "expiryTime": "2019-03-16 15:30:00",
     *              "licenseCheckModel": {
     *                  "ipAddress": ["2001:0:2841:aa90:34fb:8e63:c5ce:e345", "192.168.153.155"],
     *                  "macAddress": ["00-00-00-00-00-00-00-E0","B0-52-16-27-F5-EF"],
     *                  "cpuSerial": "178BFBFF00660F51",
     *                  "mainBoardSerial": "L1HF7B400HZ"
     *              }
     *          }
     */
    /*{
        "licensePath": "D:/license.lic",
            "issuedTime": "2022-01-01 00:00:00",
            "expiryTime": "2023-01-01 00:00:00",
            "description": "",
            "licenseCheckModel": {
        "licenseTo": "COORDSOFT.COM",
                "sn": "90EB-6C02-9D14-D763-2AD7-5167-B374-C084",
                "licenseTypeCode": 2,
                "appLimit": 100,
                "maxOnline": 100,
                "ipAddress": [
        "*"
    ],
        "domainName": ["*"],
        "moduleName": []
    }
    }*/
    @RequestMapping(value = "/generateLicense", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public AjaxResult generateLicense(@RequestBody LicenseCreatorParam param) {
        if (StringUtils.isBlank(param.getLicensePath())) {
            param.setLicensePath(licensePath);
        }
        if (StringUtils.isBlank(param.getSubject())) {
            param.setSubject(subject);
        }
        if (StringUtils.isBlank(param.getPrivateAlias())) {
            param.setPrivateAlias(privateAlias);
        }
        param.setKeyPass(Constants.STORE_PASS);
        param.setStorePass(Constants.STORE_PASS);
        try {
            param.setPrivateKeysStorePath(PathUtils.getResourceFile("privateKeys.keystore").getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        LicenseCreator licenseCreator = new LicenseCreator(param);
        boolean result = licenseCreator.generateLicense();
        if (result) {
            return AjaxResult.success("License生成成功！");
        } else {
            return AjaxResult.error("License生成失败！");
        }
    }
}