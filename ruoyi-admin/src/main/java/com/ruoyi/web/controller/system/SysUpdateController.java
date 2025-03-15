package com.ruoyi.web.controller.system;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.PathUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysConfigWebsite;
import com.ruoyi.system.mapper.PublicSqlMapper;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysConfigWebsiteService;
import com.ruoyi.update.UpdateEngine;
import com.ruoyi.update.download.Callback;
import com.ruoyi.update.download.Downloader;
import com.ruoyi.update.download.impl.HttpDownloader;
import com.ruoyi.update.support.*;
import com.ruoyi.update.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.io.FileUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/system/update")
@Slf4j
public class SysUpdateController extends BaseController {

    private static final int MAX_THREAD_NUM = 20;
    @Resource
    private ISysConfigWebsiteService sysConfigWebsiteService;
    @Resource
    private RuoYiConfig config;
    @Resource
    private ISysConfigService sysConfigService;
    @Resource
    private PublicSqlMapper publicSqlMapper;

    private boolean success = true;
    private boolean inProgress = false;
    private AjaxResult result = AjaxResult.success();

    public void fillConfigVersion() {
        if (StringUtils.isBlank(config.getDbVersion())) {
            SysConfigWebsite website = sysConfigWebsiteService.getById(1);
            config.setDbVersion(website.getDbVersion());
            config.setDbVersionNo(website.getDbVersionNo());
        }
    }

    private String getBaseUrl() {
        String baseUrl = sysConfigService.selectConfigByKey("sys.update.url");
        if (StringUtils.isBlank(baseUrl)) {
            baseUrl = "http://hy-update.coordsoft.com/";
        }
        baseUrl = Utils.fillUrl(baseUrl);
        return baseUrl;
    }

    private String getLocalDir() throws IOException {
        File localDir = new File("D:\\网络验证\\红叶\\release\\红叶网络验证系统_v1.0.0_20221018203100_test_update\\upload");
        if ("jar".equals(SysUpdateController.class.getResource("").getProtocol())) {
            // 以 jar 的方式运行
            localDir = new File(PathUtils.getUserPath());
        }
        return localDir.getCanonicalPath();
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,sadmin')")
    @RequestMapping("/checkUpdate")
    public AjaxResult checkUpdate() {
        log.info("开始检查更新");
        try {
            fillConfigVersion();
            String baseUrl = getBaseUrl();
            String latestVersionJson = Utils.readFromUrl(baseUrl + "latestVersion.json");
            LatestVersion latestVersion = JSON.parseObject(latestVersionJson, LatestVersion.class);
            Map<String, Object> map = new HashMap<>();
            Map<String, Object> currentVersion = new HashMap<>();
            currentVersion.put("version", config.getVersion());
            currentVersion.put("versionNo", config.getVersionNo());
            currentVersion.put("dbVersion", config.getDbVersion());
            currentVersion.put("dbVersionNo", config.getDbVersionNo());
            map.put("currentVersion", currentVersion);

            VersionInfo versionInfo = latestVersion.getRelease();
            if (versionInfo.getVersionNo() > config.getVersionNo()) {
                map.put("update", true);
            } else {
                map.put("update", false);
            }
            if (versionInfo.getVersionNo() > config.getDbVersionNo()) {
                map.put("updateDb", true);
            } else {
                map.put("updateDb", false);
            }

            // 正式版无更新，检查是否接收内测更新
            if (!(versionInfo.getVersionNo() > config.getVersionNo() || versionInfo.getVersionNo() > config.getDbVersionNo())) {
                String updateDev = sysConfigService.selectConfigByKey("sys.update.develop");
                if (StringUtils.isNotBlank(updateDev) && Convert.toBool(updateDev)) {
                    versionInfo = latestVersion.getDevelop();
                    if (versionInfo.getVersionNo() > config.getVersionNo()) {
                        map.put("update", true);
                    } else {
                        map.put("update", false);
                    }
                    if (versionInfo.getVersionNo() > config.getDbVersionNo()) {
                        map.put("updateDb", true);
                    } else {
                        map.put("updateDb", false);
                    }
                }
            }

            UpdateEngine ue = new UpdateEngine();
            String remoteJsonUrl = baseUrl + versionInfo.getFullVersion() + ".json";
            UpdateInfo updateInfo = ue.getUpdateInfoFromUrl(getLocalDir(), remoteJsonUrl);
            updateInfo.setFileFilterList(null);
            updateInfo.setFileInfoList(null);
            if (StringUtils.isNotBlank(updateInfo.getUpdateLog())) {
                updateInfo.setUpdateLog(updateInfo.getUpdateLog().replaceAll("\r\n", "<br>").replaceAll("\r", "<br>").replaceAll("\n", "<br>"));
            }
            map.put("updateInfo", updateInfo);
            log.info("获取到信息：\n" + JSON.toJSONString(map));
            log.info("信息获取完毕");
            return AjaxResult.success(map);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("获取信息出错：" + e.getMessage());
            return AjaxResult.error(e.getMessage());
        }
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,sadmin')")
    @RequestMapping("/doUpdate")
    public AjaxResult doUpdate() {
        if (inProgress) {
            return AjaxResult.error("后台正在执行更新，请勿重复执行");
        }
        new Thread(() -> {
            inProgress = true;
            success = true;
            result = AjaxResult.success();
            log.info("开始更新");
            try {
                fillConfigVersion();
                String baseUrl = getBaseUrl();
                String localDir = Utils.fillPath(getLocalDir());
                String latestVersionJson = Utils.readFromUrl(baseUrl + "latestVersion.json");
                LatestVersion latestVersion = JSON.parseObject(latestVersionJson, LatestVersion.class);
                VersionInfo versionInfo = latestVersion.getRelease();

                // 正式版无更新，检查是否接收内测更新
                if (!(versionInfo.getVersionNo() > config.getVersionNo() || versionInfo.getVersionNo() > config.getDbVersionNo())) {
                    String updateDev = sysConfigService.selectConfigByKey("sys.update.develop");
                    if (StringUtils.isNotBlank(updateDev) && Convert.toBool(updateDev)) {
                        versionInfo = latestVersion.getDevelop();
                    }
                }

                // 主程序需更新
                log.info("检查主程序");
                boolean update = false; // 主程序是否更新，如果主程序更新，则需要重启后台服务
                if (versionInfo.getVersionNo() > config.getVersionNo()) {
                    log.info("主程序需更新：{}({})->{}({})", config.getVersion(), config.getVersionNo(), versionInfo.getVersionName(), versionInfo.getVersionNo());
                    String remoteJsonUrl = baseUrl + versionInfo.getFullVersion() + ".json";
                    UpdateEngine ue = new UpdateEngine();
                    ue.setBaseDownloadUrl(baseUrl + versionInfo.getFullVersion() + "/");
                    UpdateInfo updateInfo = ue.getUpdateInfoFromUrl(localDir, remoteJsonUrl);
                    log.info("主程序更新：\n" + JSON.toJSONString(updateInfo));
                    log.info("信息获取完毕");
                    //log.info("旧数据备份");
                    log.info("开始更新数据");
                    ExecutorService fixedThreadPool = Executors.newFixedThreadPool(MAX_THREAD_NUM);
                    String saveDirPath = localDir + "tmp";
                    File saveDir = new File(saveDirPath);
                    if (saveDir.exists()) {
                        Utils.forceDelete(saveDir);
                    } else {
                        saveDir.mkdirs();
                    }
                    List<FileInfo> fileInfoList = updateInfo.getFileInfoList();
                    List<FileInfo> deleteFileInfoList = new ArrayList<>();
                    if (fileInfoList != null && fileInfoList.size() > 0) {
                        for (int i = 0; i < fileInfoList.size(); i++) {
                            FileInfo fileInfo = fileInfoList.get(i);
                            if (fileInfo.getDiffType() == EnumValue.DiffType.ADD || fileInfo.getDiffType() == EnumValue.DiffType.UPDATE) {
                                log.info("正在下载(" + (i + 1) + "/" + fileInfoList.size() + ")：" + fileInfo.getShortPath());
                                String url = ue.getBaseDownloadUrl() + Utils.makeUrl(fileInfo.getShortPath());
                                String savePath = Utils.makeDirPath(saveDirPath) + fileInfo.getShortPath();
                                Downloader downloader = new HttpDownloader(url, savePath);
                                DownloadThread dt = new DownloadThread(downloader);
                                fixedThreadPool.execute(dt);
                            } else if (fileInfo.getDiffType() == EnumValue.DiffType.DEL) {
                                deleteFileInfoList.add(fileInfo);
                            }
                        }
                        fixedThreadPool.shutdown();
                        while (!fixedThreadPool.isTerminated()) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        if (success) {
                            log.info("更新数据下载完毕");
                            log.info("开始替换数据");
                            List<Path> collect = Files.list(Paths.get(saveDirPath)).collect(Collectors.toList());
                            List<File> fileList = collect.stream().map(Path::toFile).collect(Collectors.toList());
                            for (File file : fileList) {
                                log.info("正在替换：" + file.getCanonicalPath());
                                if (file.isFile()) {
                                    FileUtils.copyFileToDirectory(file, new File(localDir));
                                } else if (file.isDirectory()) {
                                    FileUtils.copyDirectoryToDirectory(file, new File(localDir));
                                }
                            }
                            log.info("替换数据成功");
                            log.info("开始删除数据");
                            ExecutorService fixedThreadPool2 = Executors.newFixedThreadPool(MAX_THREAD_NUM);
                            for (int i = 0; i < deleteFileInfoList.size(); i++) {
                                FileInfo fileInfo = deleteFileInfoList.get(i);
                                log.info("正在删除(" + (i + 1) + "/" + fileInfoList.size() + ")：" + fileInfo.getShortPath());
                                DeleteThread dt = new DeleteThread(localDir + fileInfo.getShortPath(), fileInfo.getShortPath());
                                fixedThreadPool2.execute(dt);
                            }
                            DeleteThread dt = new DeleteThread(localDir + "tmp", null);
                            fixedThreadPool2.execute(dt);
                            fixedThreadPool2.shutdown();
                            while (!fixedThreadPool2.isTerminated()) {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            log.info("删除数据成功");
                            update = true;
                            log.info("主程序升级完毕");
                        } else {
                            log.error("更新数据下载失败");
                            throw new ServiceException("更新数据下载失败");
                        }
                    } else {
                        log.info("无更新内容");
                    }
                } else {
                    log.info("主程序无需更新");
                }
                // 数据库需更新
                log.info("检查数据库");
                if (versionInfo.getVersionNo() > config.getDbVersionNo()) {
                    log.info("数据库需更新：{}({})->{}({})", config.getDbVersion(), config.getDbVersionNo(), versionInfo.getVersionName(), versionInfo.getVersionNo());
                    String remoteJson = Utils.readFromUrl(baseUrl + "upgradeSql.json");
                    List<String> sqlList = JSON.parseArray(remoteJson, String.class);
                    List<String> readySqlList = new ArrayList<>();
                    recursion(config.getDbVersionNo(), versionInfo.getVersionNo(), sqlList, readySqlList);
                    log.info("数据库更新：\n" + JSON.toJSONString(readySqlList));
                    log.info("数据库更新信息获取完毕");
                    // log.info("旧数据备份");
                    log.info("开始更新数据");
                    Map<String, String> sqlMap = new HashMap<>();
                    if (readySqlList.size() > 0) {
                        for (int i = 0; i < readySqlList.size(); i++) {
                            String sqlName = readySqlList.get(i);
                            log.info("正在下载(" + (i + 1) + "/" + readySqlList.size() + ")：" + sqlName);
                            String url = baseUrl + "upgradeSql/upgrade." + sqlName + ".sql";
                            String sql = Utils.readFromUrl(url);
                            if (StringUtils.isNotBlank(sql)) {
                                sqlMap.put(sqlName, sql);
                            } else {
                                log.error("sql下载失败：" + url);
                                throw new ServiceException("sql下载失败：" + url);
                            }
                        }
                        for (int i = 0; i < readySqlList.size(); i++) {
                            String sqlName = readySqlList.get(i);
                            log.info("正在执行(" + (i + 1) + "/" + readySqlList.size() + ")：" + sqlName);
                            String sqlStr = sqlMap.get(sqlName);
                            sqlStr = sqlStr.replaceAll("\r\n", "\n");
                            String[] split = sqlStr.split(";\n");
                            for (int j = 0; j < split.length; j++) {
                                String sql = split[j];
                                if (!sql.endsWith(";")) {
                                    sql += ";";
                                }
                                log.info("正在执行(" + (j + 1) + "/" + split.length + " | " + (i + 1) + "/" + readySqlList.size() + ")：" + sqlName);
                                if (StringUtils.isNotBlank(sql)) {
                                    try {
                                        publicSqlMapper.nativeSql(sql.trim());
                                    } catch (Exception e) {
                                        log.warn("数据库执行异常警告：{}，sql：{}", e.getMessage(), sql);
                                    }
                                }
                            }
                        }
                        log.info("数据库升级完毕");
                    } else {
                        log.info("无更新内容");
                    }
                } else {
                    log.info("数据库无需更新");
                }
                // 重启服务
                if (update) {
                    new Thread(() -> {
                        try {
                            Thread.sleep(5000);
                            String command = "";
                            String osName = System.getProperty("os.name");
                            command = "cmd /c start \"D:\\网络验证\\红叶\\release\\红叶网络验证系统_v1.0.0_20221018203100_test_update\\upload\\hy.bat\"";
                            if ("jar".equals(SysUpdateController.class.getResource("").getProtocol())) {
                                // 以 jar 的方式运行
                                if (osName.startsWith("Windows")) {
                                    command = "cmd /c start \"hy.bat\"";
                                } else if (osName.startsWith("Linux")) {
                                    command = "sh hy.sh restart";
                                }
                            }
                            log.info("执行重启指令：" + command);
                            CommandLine commandLine = CommandLine.parse(command);
                            DefaultExecutor exec = new DefaultExecutor();
                            exec.execute(commandLine, new DefaultExecuteResultHandler());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }).start();
                    result = AjaxResult.success("更新完毕，即将重启，服务将中断2~3分钟");
                } else {
                    result = AjaxResult.success("更新完毕，本次更新无需重启");
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.info("更新出错：" + e.getMessage());
                result = AjaxResult.success("更新出错，请稍后重试：" + e.getMessage());
            } finally {
                inProgress = false;
            }
        }).start();
        return AjaxResult.success();
    }

    private void recursion(Long fromVersion, Long toVersion, List<String> sqlList, List<String> readySqlList) {
        for (String sql : sqlList) {
            String[] split = sql.split("-");
            if (split.length == 2) {
                long oldVer = Long.parseLong(split[0]);
                long newVer = Long.parseLong(split[1]);
                if(Objects.equals(fromVersion, toVersion)) { // 避免数据库版本大于程序版本时，多升级数据库版本，造成程序与数据版本不一致的问题
                    break;
                }
                if (oldVer == fromVersion) {
                    readySqlList.add(sql);
                    recursion(newVer, toVersion, sqlList, readySqlList);
                    break;
                }
            }
        }
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,sadmin')")
    @RequestMapping("/status")
    public AjaxResult status() {
        if (inProgress) {
            return AjaxResult.success("后台更新中").put("finish", 0);
        } else {
            return result.put("finish", 1);
        }
    }

    class DownloadThread implements Runnable {
        private Downloader downloader;

        public DownloadThread(Downloader downloader) {
            this.downloader = downloader;
        }

        @Override
        public void run() {
            downloader.download(new CallbackImpl());
        }

        class CallbackImpl implements Callback {
            @Override
            public void onProgress(long progress) {
//                log.info(downloader.getDestPath() + "已下载：" + progress + " %");
            }

            @Override
            public void onFinish() {
                log.info(downloader.getDestPath() + "已下载完毕");
            }

            @Override
            public void onError(IOException ex) {
                log.info(downloader.getDestPath() + "下载出现异常：" + ex.getMessage());
                success = false;
                ex.printStackTrace();
            }

            @Override
            public void onSpeed(double speed) {
//                log.info("当前下载速度：" + speed/1000 + "k/s");
            }
        }
    }

    class DeleteThread implements Runnable {
        private String filePath;
        private String shortPath;

        public DeleteThread(String filePath, String shortPath) {
            this.filePath = filePath;
            this.shortPath = shortPath;
        }

        @Override
        public void run() {
            try {
                File file = new File(filePath);
                if (file.exists()) {
                    try {
                        Utils.forceDelete(file);
                        log.info(filePath + "已删除");
                    } catch (IOException e) {
                        log.info(filePath + "删除失败");
                    }
                } else {
                    log.info(filePath + "不存在，跳过删除");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
