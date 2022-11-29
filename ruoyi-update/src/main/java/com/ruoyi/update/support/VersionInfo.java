package com.ruoyi.update.support;

import lombok.Data;

@Data
public class VersionInfo {

    private String versionName;
    private Long versionNo;

    public String getFullVersion() {
        return versionName + "_" + versionNo;
    }

}
