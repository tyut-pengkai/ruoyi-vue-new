package com.ruoyi.license.domain;

import com.ruoyi.license.domain.vo.WebsiteLicenseData;

public class WebsiteLicenseInfo {

    private String msg;
    private int code;
    private WebsiteLicenseData data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public WebsiteLicenseData getData() {
        return data;
    }

    public void setData(WebsiteLicenseData data) {
        this.data = data;
    }
}
