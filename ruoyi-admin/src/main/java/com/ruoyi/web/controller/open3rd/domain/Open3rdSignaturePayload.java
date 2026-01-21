package com.ruoyi.web.controller.open3rd.domain;

public class Open3rdSignaturePayload
{
    private String appId;
    private Open3rdSignature signature;

    public Open3rdSignaturePayload()
    {
    }

    public Open3rdSignaturePayload(String appId, Open3rdSignature signature)
    {
        this.appId = appId;
        this.signature = signature;
    }

    public String getAppId()
    {
        return appId;
    }

    public void setAppId(String appId)
    {
        this.appId = appId;
    }

    public Open3rdSignature getSignature()
    {
        return signature;
    }

    public void setSignature(Open3rdSignature signature)
    {
        this.signature = signature;
    }
}
