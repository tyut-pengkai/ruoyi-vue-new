package com.ruoyi.web.controller.open3rd.domain;

public class Open3rdSignature
{
    private String sign;
    private String nonce;
    private long timeStamp;

    public Open3rdSignature()
    {
    }

    public Open3rdSignature(String sign, String nonce, long timeStamp)
    {
        this.sign = sign;
        this.nonce = nonce;
        this.timeStamp = timeStamp;
    }

    public String getSign()
    {
        return sign;
    }

    public void setSign(String sign)
    {
        this.sign = sign;
    }

    public String getNonce()
    {
        return nonce;
    }

    public void setNonce(String nonce)
    {
        this.nonce = nonce;
    }

    public long getTimeStamp()
    {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp)
    {
        this.timeStamp = timeStamp;
    }
}
