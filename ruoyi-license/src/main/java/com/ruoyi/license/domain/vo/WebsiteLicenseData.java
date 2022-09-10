package com.ruoyi.license.domain.vo;

import com.ruoyi.framework.license.bo.LicenseInfo;

public class WebsiteLicenseData {

    private ServerInfo serverInfo;
    private LicenseInfo licenseInfo;

    public ServerInfo getServerInfo() {
        return serverInfo;
    }

    public void setServerInfo(ServerInfo serverInfo) {
        this.serverInfo = serverInfo;
    }

    public LicenseInfo getLicenseInfo() {
        return licenseInfo;
    }

    public void setLicenseInfo(LicenseInfo licenseInfo) {
        this.licenseInfo = licenseInfo;
    }
}
