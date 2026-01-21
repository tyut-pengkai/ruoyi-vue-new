package com.ruoyi.web.controller.open3rd.domain;

public class Open3rdDownloadInfo
{
    private String url;
    private String digest;
    private String digestAlgorithm;

    public Open3rdDownloadInfo()
    {
    }

    public Open3rdDownloadInfo(String url)
    {
        this.url = url;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getDigest()
    {
        return digest;
    }

    public void setDigest(String digest)
    {
        this.digest = digest;
    }

    public String getDigestAlgorithm()
    {
        return digestAlgorithm;
    }

    public void setDigestAlgorithm(String digestAlgorithm)
    {
        this.digestAlgorithm = digestAlgorithm;
    }
}
