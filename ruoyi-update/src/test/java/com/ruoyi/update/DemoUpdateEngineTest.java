package com.ruoyi.update;

import com.alibaba.fastjson.JSON;
import com.ruoyi.update.download.Callback;
import com.ruoyi.update.download.Downloader;
import com.ruoyi.update.download.impl.HttpDownloader;
import com.ruoyi.update.support.*;
import com.ruoyi.update.utils.Utils;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DemoUpdateEngineTest {

    @Test
    public void testGetDiffFromDir() {
        UpdateEngine ue = new UpdateEngine();
        ue.setDefaultDeleteStrategy(EnumValue.DeleteStrategy.IGNORE);
        ue.addDirFilter(new FileFilter("web", EnumValue.HandleStrategy.IGNORE));
        FileFilter filter = new FileFilter("template\\qat.exe.tpl", EnumValue.HandleStrategy.HANDLE);
        filter.setDeleteStrategy(EnumValue.DeleteStrategy.HANDLE);
        ue.addFileFilter(filter);
        String localDirPath = "D:\\网络验证\\红叶\\release\\红叶网络验证系统_v1.0.0_20221018\\upload";
        String remoteDirPath = "D:\\网络验证\\红叶\\release\\红叶网络验证系统_v1.1.0_20221119\\upload";
        ArrayList<FileInfo> diff = ue.getDiffFromDir(localDirPath, remoteDirPath);
//        JSON.config(JSONWriter.Feature.WriteEnumsUsingName);
        System.out.println(JSON.toJSONString(diff));
    }

    @Test
    public void testGenUpdateInfoJson() {
        try {
            String versionName = "v1.9.0";
            Long versionNo = 20241002014300L;
            String fullVersion = versionName + "_" + versionNo;
            UpdateEngine ue = new UpdateEngine();
            ArrayList<FileInfo> updateList = ue.getFileInfoList("D:\\网络验证\\红叶\\release\\红叶网络验证系统_" + fullVersion + "\\upload");
            UpdateInfo updateInfo = new UpdateInfo();
            updateInfo.setFileInfoList(updateList);
            updateInfo.setUpdateDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            updateInfo.setVersionName(versionName);
            updateInfo.setVersionNo(versionNo);
            updateInfo.setUpdateLog(FileUtils.readFileToString(new File("src\\test\\java\\com\\ruoyi\\update\\updateLog.txt"), StandardCharsets.UTF_8));
            List<FileFilter> fileFilterList = new ArrayList<>();
            fileFilterList.add(new FileFilter(EnumValue.FilterType.DIR, "logs", EnumValue.HandleStrategy.IGNORE));
            fileFilterList.add(new FileFilter(EnumValue.FilterType.DIR, "uploadPath", EnumValue.HandleStrategy.IGNORE));
            fileFilterList.add(new FileFilter(EnumValue.FilterType.DIR, "tmp", EnumValue.HandleStrategy.IGNORE));
            fileFilterList.add(new FileFilter(EnumValue.FilterType.DIR, "bin", EnumValue.HandleStrategy.HANDLE, EnumValue.DeleteStrategy.IGNORE));
            fileFilterList.add(new FileFilter(EnumValue.FilterType.FILE, "license.lic", EnumValue.HandleStrategy.IGNORE));
            fileFilterList.add(new FileFilter(EnumValue.FilterType.FILE, "application-config.yml", EnumValue.HandleStrategy.IGNORE));
            fileFilterList.add(new FileFilter(EnumValue.FilterType.FILE, "nohup.out", EnumValue.HandleStrategy.IGNORE));
            updateInfo.setFileFilterList(fileFilterList);
            String updateInfoJson = updateInfo.toJsonString();
            FileUtils.writeStringToFile(new File("src\\test\\java\\com\\ruoyi\\update\\" + fullVersion + ".json"), updateInfoJson, StandardCharsets.UTF_8);
            System.out.println(updateInfoJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDownload() {
        String baseUrl = "http://hy-update.coordsoft.com/";
        String latestVersionJson = Utils.readFromUrl(baseUrl + "latestVersion.json");
        LatestVersion latestVersion = JSON.parseObject(latestVersionJson, LatestVersion.class);
        String remoteJsonUrl = baseUrl + latestVersion.getRelease().getFullVersion() + ".json";
        UpdateEngine ue = new UpdateEngine();
        ue.setBaseDownloadUrl(baseUrl + latestVersion.getRelease().getFullVersion() + "/");
//        String localDirPath = "D:\\网络验证\\红叶\\release\\红叶网络验证系统_v1.0.0_20221018\\upload";
        String localDirPath = "D:\\网络验证\\红叶\\release\\红叶网络验证系统_v1.1.0_20221119\\upload";
        UpdateInfo updateInfo = ue.getUpdateInfoFromUrl(localDirPath, remoteJsonUrl);
        System.out.println(JSON.toJSONString(updateInfo));
        Callback callback = new Callback() {
            @Override
            public void onProgress(long progress) {
                System.out.println("进度：" + progress);
            }

            @Override
            public void onFinish() {
                System.out.println("下载完成");
            }

            @Override
            public void onError(IOException ex) {
                System.out.println("下载出错");
            }

            @Override
            public void onSpeed(double speed) {
                System.out.println("速度：" + speed);
            }
        };
        String saveDirPath = "C:\\Users\\GuZhiwei\\Desktop\\tmp";
        List<FileInfo> fileInfoList = updateInfo.getFileInfoList();
        if (fileInfoList != null && fileInfoList.size() > 0) {
            for (int i = 0; i < fileInfoList.size(); i++) {
                FileInfo fileInfo = fileInfoList.get(i);
                System.out.println("正在下载(" + (i + 1) + "/" + fileInfoList.size() + ")：" + fileInfo.getShortPath());
                if (fileInfo.getDiffType() == EnumValue.DiffType.ADD || fileInfo.getDiffType() == EnumValue.DiffType.UPDATE) {
                    String url = ue.getBaseDownloadUrl() + Utils.makeUrl(fileInfo.getShortPath());
                    String savePath = Utils.makeDirPath(saveDirPath) + fileInfo.getShortPath();
                    Downloader downloader = new HttpDownloader(url, savePath);
                    downloader.download(callback);
                } else if (fileInfo.getDiffType() == EnumValue.DiffType.DEL) {

                }
            }
        } else {
            System.out.println("无更新内容");
        }
    }

}
