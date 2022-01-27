package com.ruoyi.framework.license;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.license.ServerInfo;
import com.ruoyi.common.utils.PathUtils;
import com.ruoyi.framework.license.bo.LicenseVerifyParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.io.IOException;

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
    }
}
