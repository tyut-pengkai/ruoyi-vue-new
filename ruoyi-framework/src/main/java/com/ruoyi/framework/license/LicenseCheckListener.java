package com.ruoyi.framework.license;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.enums.LicenseType;
import com.ruoyi.common.license.ServerInfo;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.PathUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.framework.license.bo.LicenseVerifyParam;
import de.schlichtherle.license.LicenseContent;
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
import java.util.Date;

/**
 * 在项目启动时安装证书
 *
 * @author huangchen@deepglint.com
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
            log.info("开始载入License");
            LicenseVerifyParam param = new LicenseVerifyParam();
            param.setSubject(subject);
            param.setPublicAlias(publicAlias);
            param.setStorePass(Constants.STORE_PASS);
            param.setLicensePath(PathUtils.getUserPath() + "\\license.lic");
            try {
                param.setPublicKeysStorePath(PathUtils.getResourceFile("publicCerts.keystore").getCanonicalPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            LicenseVerify licenseVerify = new LicenseVerify();
            //安装证书
            try {
                licenseVerify.install(param);
                log.info("License载入成功");

            } catch (Exception e) {
                log.info("License载入失败：{}", e.getMessage());
            }
        }
        showLicenceInfo();
    }

    private void showLicenceInfo() {
        try {
            LicenseContent license = Constants.LICENSE_CONTENT;
            Date from = null;
            Date to = null;
            String licenceType = "未授权";
            String appLimit = "未授权";
            String maxOnline = "未授权";
            String datetime = "0000-00-00 00:00:00 - 0000-00-00 00:00:00 (0天0小时0分钟)";
            if (license != null) {
                from = license.getNotBefore();
                to = license.getNotAfter();
                licenceType = LicenseType.valueOfCode(license.getConsumerType()).getInfo();
                appLimit = "无限制";
                maxOnline = license.getConsumerAmount() + " 人";
                datetime = DateUtils.parseDateToStr(from) + " - " + DateUtils.parseDateToStr(to) +
                        " (" + DateUtils.getDatePoor(to, from) + ")";
            }
            File tipFile = PathUtils.getResourceFile("licenseTip.txt");
            assert tipFile != null;
            String tip = FileUtils.readFileToString(tipFile, StandardCharsets.UTF_8);
            System.out.format(tip + "\n>>: 系统启动成功\n", Constants.SERVER_SN, IpUtils.getHostName(),
                    IpUtils.getHostIp(), licenceType, appLimit, maxOnline, datetime);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("\n>>: 系统启动失败\n");
        }
    }
}
