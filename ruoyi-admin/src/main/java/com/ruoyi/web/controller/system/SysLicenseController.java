package com.ruoyi.web.controller.system;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.framework.license.bo.LicenseInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/system/license")
public class SysLicenseController {

    @GetMapping("/info")
    public Map<String, Object> list(HttpServletRequest request) {
        Map<String, String> serverInfo = new HashMap<>();
        serverInfo.put("sn", Constants.SERVER_SN);
        serverInfo.put("hostName", IpUtils.getHostName());
        serverInfo.put("hostDomain", request.getServerName());
        serverInfo.put("hostIp", IpUtils.getHostIp());
        LicenseInfo licenseInfo = LicenseInfo.getLicenseInfo();
        Map<String, Object> result = new HashMap<>();
        result.put("serverInfo", serverInfo);
        result.put("licenseInfo", licenseInfo);
        return AjaxResult.success().put("data", result);
    }

}
