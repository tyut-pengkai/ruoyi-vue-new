package com.ruoyi.framework.hy;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.license.ServerInfo;
import com.ruoyi.common.utils.PathUtils;
import com.ruoyi.framework.license.bo.LicenseVerifyParam;
import de.schlichtherle.util.ObfuscatedString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

/**
 * 在项目启动时安装证书
 *
 * @author zwgu
 * @date 2019/3/14 13:18
 */
@Component
@Slf4j
public class HyL {

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


    public void start() throws Exception {
        //root application context 没有parent
        ServerInfo.getServerInfo();

//        log.info("开始载入License111");
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
//            e.printStackTrace();
            Constants.IS_CRCD = true;
            throw e;
        }
        HyV hyV = new HyV();
        //安装证书
        try {
            hyV.install(param);
//            log.info("License载入成功111");
        } catch (Exception e) {
//            log.info("License载入失败111：{}", e.getMessage());
//            e.printStackTrace();
            Constants.IS_CRCD = true;
            throw e;
        }

    }

}
