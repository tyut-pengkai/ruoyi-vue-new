package com.ruoyi.license.support;

import com.ruoyi.common.license.CustomLicenseManager;
import com.ruoyi.common.license.bo.CustomKeyStoreParam;
import com.ruoyi.license.domain.LicenseCreatorParam;
import de.schlichtherle.license.*;
import lombok.extern.slf4j.Slf4j;

import javax.security.auth.x500.X500Principal;
import java.io.File;
import java.util.prefs.Preferences;

/**
 * @author zwgu
 * @desc License生成类
 * @date 2019/3/14 10:24
 */
@Slf4j
public class LicenseCreator {

    private final static X500Principal DEFAULT_HOLDER_AND_ISSUER = new X500Principal("CN=localhost, OU=localhost, O=localhost, L=SH, ST=SH, C=CN");
    private LicenseCreatorParam param;

    public LicenseCreator(LicenseCreatorParam param) {
        this.param = param;
    }

    /**
     * 生成License证书
     *
     * @return boolean
     */
    public void generateLicense() throws Exception {
        LicenseManager licenseManager = new CustomLicenseManager(initLicenseParam());
        LicenseContent licenseContent = initLicenseContent();
        licenseManager.store(licenseContent, new File(param.getLicensePath()));
        log.info("License生成成功！license位于{}", param.getLicensePath());
    }

    /**
     * 初始化证书生成参数
     */
    private LicenseParam initLicenseParam() {
        Preferences preferences = Preferences.userNodeForPackage(LicenseCreator.class);

        //设置对证书内容加密的秘钥
        CipherParam cipherParam = new DefaultCipherParam(param.getStorePass());

        KeyStoreParam privateStoreParam = new CustomKeyStoreParam(LicenseCreator.class
                , param.getPrivateKeysStorePath()
                , param.getPrivateAlias()
                , param.getStorePass()
                , param.getKeyPass());

        LicenseParam licenseParam = new DefaultLicenseParam(param.getSubject()
                , preferences
                , privateStoreParam
                , cipherParam);

        return licenseParam;
    }

    /**
     * 设置证书生成正文信息
     */
    private LicenseContent initLicenseContent() {
        LicenseContent licenseContent = new LicenseContent();
        licenseContent.setHolder(DEFAULT_HOLDER_AND_ISSUER);
        licenseContent.setIssuer(DEFAULT_HOLDER_AND_ISSUER);

        licenseContent.setSubject(param.getSubject());
        licenseContent.setIssued(param.getIssuedTime());
        licenseContent.setNotBefore(param.getIssuedTime());
        licenseContent.setNotAfter(param.getExpiryTime());
        licenseContent.setConsumerType(param.getConsumerType());
        licenseContent.setConsumerAmount(param.getConsumerAmount());
        licenseContent.setInfo(param.getDescription());

        //扩展校验服务器硬件信息
        licenseContent.setExtra(param.getLicenseCheckModel());

        return licenseContent;
    }
}
