package com.ruoyi.update.support;

import com.alibaba.fastjson2.JSON;
import lombok.Data;

import java.util.List;

@Data
public class UpdateInfo {
    private String versionName;
    private Long versionNo;
    private String updateDate;
    private String updateLog;
    private List<FileInfo> fileInfoList;
    private List<FileFilter> fileFilterList;

    public String toJsonString() {
        return JSON.toJSONString(this);
    }

}
