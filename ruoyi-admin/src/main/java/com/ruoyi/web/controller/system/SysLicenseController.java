package com.ruoyi.web.controller.system;

import cn.hutool.crypto.digest.MD5;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.license.ServerInfo;
import com.ruoyi.common.utils.AesCbcPKCS5PaddingUtil;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.PathUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.framework.license.LicenseCheckListener;
import com.ruoyi.framework.license.bo.LicenseInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/system/license")
@Slf4j
public class SysLicenseController {

    @Resource
    private LicenseCheckListener licenseCheckListener;

    @GetMapping("/info")
    public AjaxResult info(HttpServletRequest request) {
        Map<String, String> serverInfo = new HashMap<>();
        serverInfo.put("sn", Constants.SERVER_SN);
        serverInfo.put("hostName", IpUtils.getHostName());
        serverInfo.put("hostDomain", request.getServerName());
        serverInfo.put("hostIp", ServerInfo.getServerIp());
        LicenseInfo licenseInfo = LicenseInfo.getLicenseInfo();
        Map<String, Object> result = new HashMap<>();
        result.put("serverInfo", serverInfo);
        result.put("licenseInfo", licenseInfo);
        return AjaxResult.success().put("data", result);
    }

    @GetMapping("/simpleInfo")
    public AjaxResult simpleInfo(HttpServletRequest request) {
        LicenseInfo licenseInfo = LicenseInfo.getLicenseInfo();
        Map<String, Object> result = new HashMap<>();
        result.put("expireTime", DateUtils.parseDateToStr(licenseInfo.getTo()));
        result.put("remainTimeReadable", DateUtils.timeDistance(licenseInfo.getTo(), DateUtils.getNowDate()));
        result.put("remainTimeSeconds", DateUtils.differentSecondsByMillisecond(licenseInfo.getTo(), DateUtils.getNowDate()) / 1000);
        return AjaxResult.success().put("data", result);
    }

    @GetMapping("/load")
    public AjaxResult loadLicense() {
        return licenseCheckListener.loadLicense();
    }

    @PostMapping("/inject")
    public AjaxResult injectLicense(String license) {
        try {
            log.info("receive: " + license);
            byte[] bytes = Base64.decodeBase64(license);
            log.info("receive: " + MD5.create().digestHex(bytes));
            if (bytes == null || bytes.length == 0) {
                return AjaxResult.error("无效授权");
            }
            File file = new File(PathUtils.getUserPath() + File.separator + "license.lic");
            if (file.exists() && file.length() > 0) {
                FileUtils.copyFile(file, new File(PathUtils.getUserPath() + File.separator + "license" + "." + System.currentTimeMillis() + ".lic"));
            }
            FileUtils.writeByteArrayToFile(file, bytes);
            AjaxResult result = loadLicense();
            if ((int) result.get(AjaxResult.CODE_TAG) == 200) {
                return AjaxResult.success("添加授权成功，请重新登录您的验证后台");
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }
    }

    @PostMapping("/remove")
    public AjaxResult removeLicense(String sign) {
        try {
            log.info("remove license：" + sign);
            String decode = AesCbcPKCS5PaddingUtil.decode(sign, Constants.STORE_PASS);
            if (System.currentTimeMillis() - Long.parseLong(decode) < 3000) {
                File file = new File(PathUtils.getUserPath() + File.separator + "license.lic");
                if (file.exists() && file.length() > 0) {
                    FileUtils.writeStringToFile(file, "", StandardCharsets.UTF_8);
                }
                return AjaxResult.success("授权已移除");
            }
            return AjaxResult.success("非法请求");
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }
    }

}
