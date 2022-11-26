package com.ruoyi.update.download.impl;

import com.ruoyi.update.download.Callback;
import com.ruoyi.update.download.Downloader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class HttpDownloader implements Downloader {

    private final String url;
    private final String destFileName;
    private Object data;

    public HttpDownloader(String url, String destFileName) {
        this.url = url;
        this.destFileName = destFileName;
    }

    @Override
    public void download(Callback callback) {
        File destFile = new File(destFileName);
        destFile.getParentFile().mkdirs();
        try (FileOutputStream fos = new FileOutputStream(destFile)) {
            URLConnection connection = new URL(url).openConnection();
            long fileSize = connection.getContentLengthLong();
            InputStream inputStream = connection.getInputStream();
            byte[] buffer = new byte[10 * 1024 * 1024];
            int numberOfBytesRead;
            long totalNumberOfBytesRead = 0;
            long start = System.currentTimeMillis();
            while ((numberOfBytesRead = inputStream.read(buffer)) != -1) {
                fos.write(buffer, 0, numberOfBytesRead);
                totalNumberOfBytesRead += numberOfBytesRead;
                callback.onProgress(totalNumberOfBytesRead * 100 / fileSize);
                long costTime = (System.currentTimeMillis() - start) / 1000;
                if (costTime > 0) {
                    callback.onSpeed(totalNumberOfBytesRead / costTime);
                }
            }
            callback.onFinish();
        } catch (IOException ex) {
            callback.onError(ex);
        }

    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getDestPath() {
        return destFileName;
    }

    @Override
    public Object getData() {
        return data;
    }

    @Override
    public void setData(Object data) {
        this.data = data;
    }

}
