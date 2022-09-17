package com.ruoyi.framework.license;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.exception.LicenseException;
import com.ruoyi.common.license.bo.CustomKeyStoreParam;
import com.ruoyi.framework.license.bo.LicenseVerifyParam;
import de.schlichtherle.license.*;
import de.schlichtherle.util.ObfuscatedString;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.prefs.Preferences;

/**
 * @author zwgu
 * @desc License校验类
 * @date 2019/3/14 13:24
 */
@Slf4j
public class LicenseVerify {

    private static final String EXC_LICENSE_HAS_EXPIRED = (new ObfuscatedString(new long[]{1000558500458715757L, -6998261911041258483L, -5490039629745846648L, 3561172928787106880L})).toString();

    /**
     * 安装License证书
     */
    public synchronized LicenseContent install(LicenseVerifyParam param) throws Exception {
        LicenseContent result = null;
//        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //1. 安装证书
        try {
            LicenseManager licenseManager = LicenseManagerHolder.getInstance(initLicenseParam(param));
            licenseManager.uninstall();
            result = licenseManager.install(new File(param.getLicensePath()));
            Constants.LICENSE_CONTENT = result;
//            log.info(MessageFormat.format("License载入成功，有效期：{0} - {1}", format.format(result.getNotBefore()), format.format(result.getNotAfter())));
        } catch (LicenseContentException lce) {
            if (EXC_LICENSE_HAS_EXPIRED.equals(lce.getMessage())) {
//                log.error("License载入失败，License已过期！");
                throw new LicenseException("License已过期");
            } else {
//                log.error("License载入失败！");
                throw lce;
            }
//            throw lce;
        } catch (Exception e) {
//            log.error("License载入失败！");
            throw new LicenseException(e.getMessage());
        }
        return result;
    }

    /**
     * 校验License证书
     */
    public boolean verify() {
        LicenseManager licenseManager = LicenseManagerHolder.getInstance(null);
//        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //2. 校验证书
        try {
            LicenseContent licenseContent = licenseManager.verify();
//            log.debug(MessageFormat.format("License校验通过，有效期：{0} - {1}", format.format(licenseContent.getNotBefore()), format.format(licenseContent.getNotAfter())));
            return true;
        } catch (Exception e) {
//            log.error("License校验失败：{}", e.getMessage());
//            e.printStackTrace();
            return false;
        }
    }

    /**
     * 初始化证书生成参数
     */
    private LicenseParam initLicenseParam(LicenseVerifyParam param) {
        Preferences preferences = Preferences.userNodeForPackage(LicenseVerify.class);

        CipherParam cipherParam = new DefaultCipherParam(param.getStorePass());

        KeyStoreParam publicStoreParam = new CustomKeyStoreParam(LicenseVerify.class
                , param.getPublicKeysStorePath()
                , param.getPublicAlias()
                , param.getStorePass()
                , null);

        return new DefaultLicenseParam(param.getSubject()
                , preferences
                , publicStoreParam
                , cipherParam);
    }
}
