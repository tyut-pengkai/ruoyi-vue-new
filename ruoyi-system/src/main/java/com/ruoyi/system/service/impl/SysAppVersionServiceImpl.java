package com.ruoyi.system.service.impl;

import cn.hutool.core.io.file.FileNameUtil;
import com.alibaba.fastjson.JSON;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysApp;
import com.ruoyi.common.core.domain.entity.SysAppVersion;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.*;
import com.ruoyi.system.domain.vo.ActivityMethodVo;
import com.ruoyi.system.domain.vo.QuickAccessResultVo;
import com.ruoyi.system.mapper.SysAppVersionMapper;
import com.ruoyi.system.service.ISysAppService;
import com.ruoyi.system.service.ISysAppVersionService;
import com.ruoyi.utils.QuickAccessApkUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.io.FileUtils;
import org.ini4j.Ini;
import org.ini4j.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 软件版本信息Service业务层处理
 *
 * @author zwgu
 * @date 2021-12-19
 */
@Service
@Slf4j
public class SysAppVersionServiceImpl implements ISysAppVersionService {

    @Resource
    private SysAppVersionMapper sysAppVersionMapper;
    @Resource
    private ISysAppService sysAppService;

    @PostConstruct
    public void init() {
        List<SysAppVersion> appVersionList = sysAppVersionMapper.selectSysAppVersionList(new SysAppVersion());
        for (SysAppVersion appVersion : appVersionList) {
                SysCache.set(CacheConstants.SYS_APP_VERSION_KEY + appVersion.getAppVersionId(), appVersion, 86400000);
        }
    }

    /**
     * 查询软件版本信息
     *
     * @param appVersionId 软件版本信息主键
     * @return 软件版本信息
     */
    @Override
    public SysAppVersion selectSysAppVersionByAppVersionId(Long appVersionId) {
        SysAppVersion appVersion = (SysAppVersion) SysCache.get(CacheConstants.SYS_APP_VERSION_KEY + appVersionId);
        if (appVersion == null) {
            appVersion = sysAppVersionMapper.selectSysAppVersionByAppVersionId(appVersionId);
            SysCache.set(CacheConstants.SYS_APP_VERSION_KEY + appVersionId, appVersion, 86400000);
        }
        return appVersion;
    }

    /**
     * 查询软件版本信息
     *
     * @param appId
     * @param appVersion
     * @return 软件版本信息
     */
    @Override
    public SysAppVersion selectSysAppVersionByAppIdAndVersion(Long appId, Long appVersion) {
        return sysAppVersionMapper.selectSysAppVersionByAppIdAndVersion(appId, appVersion);
    }

    /**
     * 查询软件版本信息列表
     *
     * @param sysAppVersion 软件版本信息
     * @return 软件版本信息
     */
    @Override
    public List<SysAppVersion> selectSysAppVersionList(SysAppVersion sysAppVersion) {
        return sysAppVersionMapper.selectSysAppVersionList(sysAppVersion);
    }

    /**
     * 新增软件版本信息
     *
     * @param sysAppVersion 软件版本信息
     * @return 结果
     */
    @Override
    public int insertSysAppVersion(SysAppVersion sysAppVersion) {
        sysAppVersion.setCreateTime(DateUtils.getNowDate());
        sysAppVersion.setCreateBy(SecurityUtils.getUsernameNoException());
        return sysAppVersionMapper.insertSysAppVersion(sysAppVersion);
    }

    /**
     * 修改软件版本信息
     *
     * @param sysAppVersion 软件版本信息
     * @return 结果
     */
    @Override
    public int updateSysAppVersion(SysAppVersion sysAppVersion) {
        sysAppVersion.setUpdateTime(DateUtils.getNowDate());
        sysAppVersion.setUpdateBy(SecurityUtils.getUsernameNoException());
        int i = sysAppVersionMapper.updateSysAppVersion(sysAppVersion);
        if (i > 0) {
            SysAppVersion appVersion = sysAppVersionMapper.selectSysAppVersionByAppVersionId(sysAppVersion.getAppVersionId());
            SysCache.set(CacheConstants.SYS_APP_VERSION_KEY + sysAppVersion.getAppVersionId(), appVersion, 86400000);
        }
        return i;
    }

    /**
     * 批量删除软件版本信息
     *
     * @param appVersionIds 需要删除的软件版本信息主键
     * @return 结果
     */
    @Override
    public int deleteSysAppVersionByAppVersionIds(Long[] appVersionIds) {
        for (Long appVersionId : appVersionIds) {
            SysCache.delete(CacheConstants.SYS_APP_VERSION_KEY + appVersionId);
        }
        return sysAppVersionMapper.deleteSysAppVersionByAppVersionIds(appVersionIds);
    }

    /**
     * 删除软件版本信息信息
     *
     * @param appVersionId 软件版本信息主键
     * @return 结果
     */
    @Override
    public int deleteSysAppVersionByAppVersionId(Long appVersionId) {
        SysCache.delete(CacheConstants.SYS_APP_VERSION_KEY + appVersionId);
        return sysAppVersionMapper.deleteSysAppVersionByAppVersionId(appVersionId);
    }

    /**
     * @param appId APP ID
     * @return 最新版本信息
     */
    @Override
    public SysAppVersion selectLatestVersionByAppId(Long appId) {
        return sysAppVersionMapper.selectLatestVersionByAppId(appId);
    }

    /**
     * @param appId APP ID
     * @return 强制更新到的最新版本信息
     */
    @Override
    public SysAppVersion selectLatestVersionForceUpdateByAppId(Long appId) {
        return sysAppVersionMapper.selectLatestVersionForceUpdateByAppId(appId);
    }

    /**
     * 快速接入系统
     *
     * @param file
     * @param versionId
     * @return
     */
    @Override
    public Map<String, Object> quickAccess(String accessType, MultipartFile file, Long versionId, boolean updateMd5,
                                           String apkOper, String template, String skin, ActivityMethodVo vo,
                                           boolean fullScreen, boolean enableOffline, boolean hideAutoLogin,
                                           boolean enhancedMode, boolean ignoreSplashActivity, String ignoreActivityKeywords) {
        Map<String, Object> map = new HashMap<>();
        try {
            // 封装
            String originalFilename = null;
            byte[] bytes = null;
            if (file == null) {
                if (StringUtils.isNotBlank(vo.getOriName())) {
                    originalFilename = vo.getOriName();
                }
            } else {
                originalFilename = file.getOriginalFilename();
                bytes = file.getBytes();
            }
            SysAppVersion version = sysAppVersionMapper.selectSysAppVersionByAppVersionId(versionId);
            SysApp app = sysAppService.selectSysAppByAppId(version.getAppId());
            if(StringUtils.isBlank(originalFilename)) {
                throw new ServiceException("文件不能为空");
            }
            QuickAccessResultVo result = quickAccessHandle(accessType, bytes, version, originalFilename, apkOper, template, skin, vo,
                    fullScreen, enableOffline, hideAutoLogin, enhancedMode, ignoreSplashActivity, ignoreActivityKeywords);

            // 1全局 2单例
            if ("1".equals(accessType) || ("2".equals(accessType) && result.getBytes() != null && result.getBytes().length > 0)) {
                bytes = result.getBytes();
                // 生成文件
                String remark = "";
                if (originalFilename.endsWith(".apk")) {
                    if (result.getData().containsKey("signed")) {
                        remark = (boolean) result.getData().get("signed") ? "signed" : "unsigned";
                    } else {
                        remark = "unsigned";
                    }
                }
                String filename = rename(app.getAppName(), originalFilename, version.getVersionName(), remark);
                String filePath = RuoYiConfig.getDownloadPath() + filename;
                File desc = new File(filePath);
                if (!desc.getParentFile().exists()) {
                    desc.getParentFile().mkdirs();
                }
                assert bytes != null;
                FileUtils.writeByteArrayToFile(desc, bytes);
                // 更新MD5
//            if (updateMd5 && originalFilename.endsWith(".exe")) {
//                String md5 = version.getMd5();
//                String md5New = DigestUtils.md5Hex(bytes);
//                if (StringUtils.isBlank(md5)) {
//                    md5 = md5New;
//                } else {
//                    if (!md5.contains(md5New)) {
//                        if (md5.endsWith("|")) {
//                            md5 += md5New;
//                        } else {
//                            md5 += "|" + md5New;
//                        }
//                    }
//                }
//                SysAppVersion versionNew = new SysAppVersion();
//                versionNew.setAppVersionId(versionId);
//                versionNew.setMd5(md5);
//                updateSysAppVersion(versionNew);
//            }
                map.put("step", "file");
                map.put("data", filename);
            } else if ("2".equals(accessType)) {
                if (StringUtils.isBlank(vo.getActivity())) {
                    map.put("step", "activityList");
                } else if (StringUtils.isBlank(vo.getMethod())) {
                    map.put("step", "methodList");
                } else {
                    map.put("step", "file");
                }
                map.putAll(result.getData());
                return map;
            } else {
                throw new ServiceException("接入方式参数有误");
            }
            return map;
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServiceException("接入失败：" + e.getMessage());
        }
    }

    /**
     * @param bytes
     * @param version
     * @param filename
     * @param apkOper  1注入并加签 2仅注入 3仅加签
     * @return
     */
    private QuickAccessResultVo quickAccessHandle(String accessType, byte[] bytes, SysAppVersion version, String filename,
                                                  String apkOper, String template, String skin, ActivityMethodVo vo,
                                                  boolean fullScreen, boolean enableOffline, boolean hideAutoLogin,
                                                  boolean enhancedMode, boolean ignoreSplashActivity, String ignoreActivityKeywords) {
        QuickAccessResultVo result = new QuickAccessResultVo();
        try {
            String apvStr = getApvString(version, template, skin, fullScreen, enableOffline, hideAutoLogin, enhancedMode, ignoreSplashActivity, ignoreActivityKeywords);
            if (filename.endsWith(".exe")) {
//                log.info("正在整合exe");
//                byte[] apvBytes = apvStr.getBytes(StandardCharsets.UTF_8);
//                byte[] tplBytes = FileUtils.readFileToByteArray(new File(PathUtils.getUserPath() + File.separator + "template" + File.separator + "qat.exe.tpl"));
//                log.info("快速接入成功");
//                byte[] resultBytes = ArrayUtil.addAll(tplBytes, split, apvBytes, split, bytes);
//                QuickAccessResultVo result = new QuickAccessResultVo();
//                result.setBytes(resultBytes);
//                return result;
                result.getData().put("apvStr", apvStr);
            } else if(filename.endsWith(".apk")) {
                log.info("正在快速接入" + filename);
                byte[] injectedApk = bytes;
                if ("1".equals(apkOper) || "2".equals(apkOper)) {
                    try {
                        File tempFile;
                        if ("1".equals(accessType) || ("2".equals(accessType) && bytes != null && bytes.length > 0)) {
                            // 写到临时目录
                            tempFile = File.createTempFile("hyQuickAccessApkTempFile" + System.currentTimeMillis(), null);
                            FileUtils.writeByteArrayToFile(tempFile, bytes);
                        } else {
                            tempFile = new File(File.createTempFile(String.valueOf(System.currentTimeMillis()), "").getParent() + File.separator + vo.getOriPath());
                        }
                        // 读入原文件
                        // String oriPath = PathUtils.getUserPath() + File.separator + "src/test/resources" + File.separator + "热血合击西瓜辅助（无验证）.apk";
                        String oriPath = tempFile.getCanonicalPath();
                        if ("1".equals(accessType)) {
                            log.info("正在注入apk");
                            injectedApk = QuickAccessApkUtil.doProcess(oriPath, apvStr, template, enhancedMode);
                            log.info("注入apk完毕");
                        } else if ("2".equals(accessType)) {
                            if (StringUtils.isBlank(vo.getActivity())) {
                                List<String> activityList = QuickAccessApkUtil.parseManifestActivity(oriPath);
                                result.getData().put("list", activityList);
                                result.getData().put("oriPath", tempFile.getName());
                                result.getData().put("oriName", filename);
                                return result;
                            } else if (StringUtils.isBlank(vo.getMethod())) {
                                List<String> methodList = QuickAccessApkUtil.parserMethod(oriPath, vo.getActivity());
                                result.getData().put("list", methodList);
                                return result;
                            } else {
                                log.info("正在注入apk");
                                injectedApk = QuickAccessApkUtil.doProcess2(oriPath, apvStr, template, vo, enhancedMode);
                                log.info("注入apk完毕");
                            }
                        } else {
                            throw new ServiceException("接入方式参数有误");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new ServiceException(e.getMessage());
                    }
                }
                if ("1".equals(apkOper) || "3".equals(apkOper)) { // 签名
                    log.info("正在apk签名");
                    try {
                        File tempFileUnsigned = File.createTempFile("hyQuickAccessApkTempFileUnsigned" + System.currentTimeMillis(), null);
                        File tempFileSigned = File.createTempFile("hyQuickAccessApkTempFileSigned" + System.currentTimeMillis(), null);
                        FileUtils.writeByteArrayToFile(tempFileUnsigned, injectedApk);
                        File apksigner = PathUtils.getResourceFile("apksigner.jar");
                        File keystore = PathUtils.getResourceFile("signApk.keystore");
                        String command = "java -jar \"" + apksigner.getCanonicalPath()
                                + "\" sign --ks \"" + keystore.getCanonicalPath()
                                + "\" --ks-key-alias signApk --ks-pass pass:hy@2022wlyz --out \""
                                + tempFileSigned.getCanonicalPath() + "\" \""
                                + tempFileUnsigned.getCanonicalPath() + "\"";
//                        String command = "java";
                        log.debug(command.replaceAll("hy@2022wlyz", "pass"));
                        //接收正常结果流
                        ByteArrayOutputStream susStream = new ByteArrayOutputStream();
                        //接收异常结果流
                        ByteArrayOutputStream errStream = new ByteArrayOutputStream();
                        CommandLine commandLine = CommandLine.parse(command);
//                                .addArgument("-jar").addArgument(apksigner.getCanonicalPath(), true)
//                                .addArgument("sign").addArgument("--ks")
//                                .addArgument(keystore.getCanonicalPath(), true)
//                                .addArgument("--ks-key-alias").addArgument("signApk").addArgument("--ks-pass")
//                                .addArgument("pass:hy@2022wlyz").addArgument("--out")
//                                .addArgument(tempFileSigned.getCanonicalPath(), true)
//                                .addArgument(tempFileUnsigned.getCanonicalPath(), true);
//                        log.debug(commandLine.toString());
                        DefaultExecutor exec = new DefaultExecutor();
                        PumpStreamHandler streamHandler = new PumpStreamHandler(susStream, errStream);
                        exec.setStreamHandler(streamHandler);
                        int code = exec.execute(commandLine);
                        // 不同操作系统注意编码，否则结果乱码
                        String suc = susStream.toString("UTF-8");
                        String err = errStream.toString("UTF-8");
                        if (code == 0) {
                            log.info("apk签名成功");
                            injectedApk = FileUtils.readFileToByteArray(tempFileSigned);
                            result.getData().put("signed", true);
                        } else {
                            log.info("apk签名失败：" + err);
                            result.getData().put("signed", false);
                        }
//                        ApkSignerTool.main(new String[]{
//                                "sign", "--ks",
//                                keystore.getCanonicalPath(),
//                                "--ks-key-alias",
//                                "signApk",
//                                "--ks-pass",
//                                "pass:hy@2022wlyz",
//                                "--out",
//                                tempFileSigned.getCanonicalPath(),
//                                tempFileUnsigned.getCanonicalPath()
//                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.info("apk签名失败：" + e.getMessage());
                        result.getData().put("signed", false);
                    }
                }
                log.info("快速接入成功");
                result.setBytes(injectedApk);
            }
        } catch (InvalidAlgorithmParameterException | NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
        return result;
    }

    private String getApvString(SysAppVersion version, String template, String skin,
                                boolean fullScreen, boolean enableOffline, boolean hideAutoLogin,
                                boolean enhancedMode, boolean ignoreSplashActivity, String ignoreActivityKeywords
    ) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        AppParamVo apv = new AppParamVo();
        SysApp app = version.getApp();
        if(version.getApp() == null) {
            app = sysAppService.selectSysAppByAppId(version.getAppId());
        }
        sysAppService.setApiUrl(app);
        apv.setApiUrl(app.getApiUrl());
        apv.setAppSecret(app.getAppSecret());
        apv.setVersionNo(version.getVersionNo().toString());
        apv.setDataInEnc(app.getDataInEnc().getCode());
        apv.setDataInPwd(app.getDataInPwd());
        apv.setDataOutEnc(app.getDataOutEnc().getCode());
        apv.setDataOutPwd(app.getDataOutPwd());
        apv.setApiPwd(app.getApiPwd());
        apv.setAuthType(app.getAuthType().getCode());
        apv.setBillType(app.getBillType().getCode());
        apv.setTemplate(template);
        apv.setSkin(skin);
        apv.setFullScreen(fullScreen);
        apv.setEnableOffline(enableOffline);
        apv.setHideAutoLogin(hideAutoLogin);
        apv.setEnhancedMode(enhancedMode);
        apv.setIgnoreSplashActivity(ignoreSplashActivity);
        apv.setIgnoreActivityKeywords(ignoreActivityKeywords);
        // 加密
        String apvStr = JSON.toJSONString(apv);
        apvStr = AesCbcPKCS5PaddingUtil.encode(apvStr, "quickAccess");
        return apvStr;
    }

    private String rename(String appName, String filename, String versionName, String remark) {
        return "__" + appName + "_" + (versionName.replaceAll("\\.", "_")) + (StringUtils.isNotBlank(remark) ? "_" + remark : "") + "." + FileNameUtil.extName(filename);
    }

    /**
     * 检查软件名称唯一性
     *
     * @return
     */
    @Override
    public boolean checkVersionNoUnique(Long versionNo, Long appId, Long appVersionId) {
        int count = sysAppVersionMapper.checkAppVersionNoUnique(versionNo, appId, appVersionId);
        if (count > 0) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 获取快速接入参数信息
     *
     * @param appVersionId
     */
    @Override
    public AjaxResult getQuickAccessParams(Long appVersionId) {
        try {
            SysAppVersion version = sysAppVersionMapper.selectSysAppVersionByAppVersionId(appVersionId);
            String apvStr =  getApvString(version, null, null, false, false, false, true, true, null);
            return AjaxResult.success().put("apvStr", apvStr);
        } catch (InvalidAlgorithmParameterException | NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取快速接入模板列表
     *
     * @return
     */
    @Override
    public AjaxResult getQuickAccessTemplateList() {

//        String templateDirPath = PathUtils.getUserPath() + File.separator + ".." + File.separator + "template";
//        if ("jar".equals(SysAppVersionServiceImpl.class.getResource("").getProtocol())) {
//            // 以 jar 的方式运行
//            templateDirPath = PathUtils.getUserPath() + File.separator + "template";
//        }
        String templateDirPath = PathUtils.getUserPath() + File.separator + "template";
        Collection<File> templateList = FileUtils.listFiles(new File(templateDirPath), new String[]{"apk.tpl"}, false);
        String regex = "^(.*)\\.apk\\.tpl$";
        Pattern pattern = Pattern.compile(regex);
        List<TemplateInfo> result = new ArrayList<>();
        for (File template : templateList) {
            try {
                Matcher matcher = pattern.matcher(template.getName());
                if (matcher.matches()) {
                    String fileName = matcher.group(1);
                    String configFilePath = template.getParentFile().getCanonicalPath() + File.separator + fileName + ".apk.ini";
                    File configFile = new File(configFilePath);
                    if (configFile.exists() && configFile.isFile()) {
                        Ini ini = new Ini();
                        ini.load(configFile);
                        Profile.Section section = ini.get("模板信息");
                        String orderString = section.get("显示排序");
                        int order = 998;
                        try {
                            order = Integer.parseInt(orderString);
                        } catch(Exception ignored) { }
                        result.add(new TemplateInfo(
                                fileName,
                                section.get("模板名称"),
                                section.get("模板描述"),
                                section.get("模板版本"),
                                section.get("模板作者"),
                                section.get("联系方式"),
                                section.get("附加信息"),
                                order,
                                Arrays.asList(section.get("皮肤列表").split(","))));
                    } else {
                        result.add(new TemplateInfo(fileName, fileName, "", "", "", "", "", 999999, new ArrayList<>()));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        result.sort(Comparator.comparing(TemplateInfo::getIndex));
        return AjaxResult.success(result);
    }

    @Override
    public Map<String, Object> downloadDexFile(Long versionId, String template, String skin,
                                               boolean fullScreen, boolean enableOffline, boolean hideAutoLogin) {
        Map<String, Object> map = new HashMap<>();
        try {
            SysAppVersion version = sysAppVersionMapper.selectSysAppVersionByAppVersionId(versionId);
            SysApp app = sysAppService.selectSysAppByAppId(version.getAppId());
            String apvStr = getApvString(version, template, skin, fullScreen, enableOffline, hideAutoLogin, false, false, null);
            byte[] dex = QuickAccessApkUtil.doProcess3(apvStr, template);
            String filename = rename(app.getAppName(), "name.dex", version.getVersionName(), "");
            String filePath = RuoYiConfig.getDownloadPath() + filename;
            File desc = new File(filePath);
            if (!desc.getParentFile().exists()) {
                desc.getParentFile().mkdirs();
            }
            FileUtils.writeByteArrayToFile(desc, dex);
            map.put("step", "file");
            map.put("data", filename);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("生成失败：" + e.getMessage());
        }
    }

    /**
     * 状态修改
     *
     * @param version
     */
    @Override
    public int updateSysVersionStatus(SysAppVersion version) {
        return updateSysAppVersion(version);
    }

    /**
     * 强制更新状态修改
     *
     * @param version
     */
    @Override
    public int updateForceUpdateStatus(SysAppVersion version) {
        return updateSysAppVersion(version);
    }

    @Data
    class AppParamVo {
        private String apiUrl;
        private String appSecret;
        private String versionNo;
        private String dataInEnc;
        private String dataInPwd;
        private String dataOutEnc;
        private String dataOutPwd;
        private String apiPwd;
        private String authType;
        private String billType;
        private String template;
        private String skin;
        private boolean fullScreen;
        private boolean enableOffline;
        private boolean hideAutoLogin;
        private boolean enhancedMode;
        private boolean ignoreSplashActivity;
        private String ignoreActivityKeywords;
    }

    @Data
    @AllArgsConstructor
    class TemplateInfo {
        private String fileName;
        private String name;
        private String description;
        private String version;
        private String author;
        private String contact;
        private String remark;
        private Integer index;
        private List<String> skinList;
    }
}
