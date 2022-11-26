package com.ruoyi.update.download;

public interface Downloader {

    public void download(Callback callback);

    public String getUrl();

    public Object getData();

    public void setData(Object data);

    public String getDestPath();

}
