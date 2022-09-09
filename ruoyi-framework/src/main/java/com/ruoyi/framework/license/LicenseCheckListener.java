package com.ruoyi.framework.license;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.license.ServerInfo;
import com.ruoyi.common.utils.PathUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.framework.license.bo.LicenseInfo;
import com.ruoyi.framework.license.bo.LicenseVerifyParam;
import de.schlichtherle.util.ObfuscatedString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 在项目启动时安装证书
 *
 * @author zwgu
 * @date 2019/3/14 13:18
 */
@Component
@Slf4j
public class LicenseCheckListener implements ApplicationListener<ContextRefreshedEvent> {

    /**
     * 证书subject
     */
    @Value("${license.subject}")
    private String subject;

    /**
     * 公钥别称
     */
    @Value("${license.publicAlias}")
    private String publicAlias;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        //root application context 没有parent
        ServerInfo.getServerInfo();
        ApplicationContext context = event.getApplicationContext().getParent();
        if (context == null) {
            loadLicense();
        }
        showLicenceInfo();
    }

    public AjaxResult loadLicense() {
        log.info("开始载入License");
        LicenseVerifyParam param = new LicenseVerifyParam();
        param.setSubject(subject);
        param.setPublicAlias(publicAlias);
        param.setStorePass(Constants.STORE_PASS);
        param.setLicensePath(PathUtils.getUserPath() + File.separator + new ObfuscatedString(new long[]{0x2067CAF954C03C60L, 0xF43919925C9E809BL, 0xA7747A77AC27A38AL}).toString() /* => "license.lic" */);
        try {
//                param.setPublicKeysStorePath(PathUtils.getResourceFile(
//                                new ObfuscatedString(new long[]{0xD43EA2656C859964L, 0x114DCA318581A2B9L,
//                                        0xEEA6BCEAC5380304L, 0xD3D9A0D6A6B29B00L}).toString() /* => "publicCerts.keystore" */)
//                        .getCanonicalPath());
            param.setPublicKeysStorePath(PathUtils.getResourceFile(
                            new ObfuscatedString(new long[]{0x2798634924122821L, 0xD28F88883B923E57L}).toString() /* => "hy.ini" */)
                    .getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        LicenseVerify licenseVerify = new LicenseVerify();
        //安装证书
        try {
            licenseVerify.install(param);
            log.info("License载入成功");
            return AjaxResult.success("License载入成功");
        } catch (Exception e) {
            log.info("License载入失败：{}", e.getMessage());
//                e.printStackTrace();
            return AjaxResult.error("License载入失败：" + e.getMessage());
        }
    }

    private void showLicenceInfo() {
        try {
            LicenseInfo info = LicenseInfo.getLicenseInfo();
            File tipFile = PathUtils.getResourceFile("licenseTip.txt");
            String tip = FileUtils.readFileToString(tipFile, StandardCharsets.UTF_8);
            System.out.format(tip, Constants.SERVER_SN, IpUtils.getHostName(),
                    ServerInfo.getServerIp(), info.getLicenseType(), info.getLicenseTo(),
                    info.getAppLimit(), info.getMaxOnline(), info.getLicenseDomain(),
                    info.getLicenseIp(), info.getDatetime());
            log.info("\n>>: 系统启动成功\n");
        } catch (Exception e) {
//            e.printStackTrace();
            log.info("\n>>: 系统启动失败：{}\n", e.getMessage());
        }
    }
}
