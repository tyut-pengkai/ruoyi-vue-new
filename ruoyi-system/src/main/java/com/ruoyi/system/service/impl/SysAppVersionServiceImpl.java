package com.ruoyi.system.service.impl;

import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.ArrayUtil;
import com.alibaba.fastjson.JSON;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.domain.entity.SysApp;
import com.ruoyi.common.core.domain.entity.SysAppVersion;
import com.ruoyi.common.utils.AesCbcPKCS5PaddingUtil;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.PathUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.vo.QuickAccessResultVo;
import com.ruoyi.system.mapper.SysAppVersionMapper;
import com.ruoyi.system.service.ISysAppService;
import com.ruoyi.system.service.ISysAppVersionService;
import com.ruoyi.utils.QuickAccessApkUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
import java.util.List;

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

    /**
     * 查询软件版本信息
     *
     * @param appVersionId 软件版本信息主键
     * @return 软件版本信息
     */
    @Override
    public SysAppVersion selectSysAppVersionByAppVersionId(Long appVersionId) {
        return sysAppVersionMapper.selectSysAppVersionByAppVersionId(appVersionId);
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
        return sysAppVersionMapper.updateSysAppVersion(sysAppVersion);
    }

    /**
     * 批量删除软件版本信息
     *
     * @param appVersionIds 需要删除的软件版本信息主键
     * @return 结果
     */
    @Override
    public int deleteSysAppVersionByAppVersionIds(Long[] appVersionIds) {
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
    public String quickAccess(MultipartFile file, Long versionId, boolean updateMd5, String apkOper) {
        try {
            // 封装
            String originalFilename = file.getOriginalFilename();
            byte[] bytes = file.getBytes();
            SysAppVersion version = sysAppVersionMapper.selectSysAppVersionByAppVersionId(versionId);
            SysApp app = sysAppService.selectSysAppByAppId(version.getAppId());
            QuickAccessResultVo result = quickAccessHandle(bytes, version, originalFilename, apkOper);
            bytes = result.getBytes();
            // 生成文件
            String remark = "";
            if(originalFilename.endsWith(".apk")) {
                if(result.getData().containsKey("signed")) {
                    remark = (boolean)result.getData().get("signed") ? "signed" : "unsigned";
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
            if (updateMd5 && originalFilename.endsWith(".exe")) {
                String md5 = version.getMd5();
                String md5New = DigestUtils.md5Hex(bytes);
                if (StringUtils.isBlank(md5)) {
                    md5 = md5New;
                } else {
                    if (!md5.contains(md5New)) {
                        if (md5.endsWith("|")) {
                            md5 += md5New;
                        } else {
                            md5 += "|" + md5New;
                        }
                    }
                }
                SysAppVersion versionNew = new SysAppVersion();
                versionNew.setAppVersionId(versionId);
                versionNew.setMd5(md5);
                updateSysAppVersion(versionNew);
            }
            return filename;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param bytes
     * @param version
     * @param filename
     * @param apkOper  1注入并加签 2仅注入 3仅加签
     * @return
     */
    private QuickAccessResultVo quickAccessHandle(byte[] bytes, SysAppVersion version, String filename, String apkOper) {
        try {
            log.info("正在快速接入" + filename);
            SysApp app = sysAppService.selectSysAppByAppId(version.getAppId());
            sysAppService.setApiUrl(app);
            byte[] split = "|*@#||*@#|".getBytes();
            log.info("正在对接参数");
            AppParamVo apv = new AppParamVo();
            apv.setApiUrl(app.getApiUrl());
            apv.setAppSecret(app.getAppSecret());
            apv.setVersionNo(version.getVersionNo().toString());
            apv.setDataInEnc(app.getDataInEnc().getCode());
            apv.setDataInPwd(app.getDataInPwd());
            apv.setDataOutEnc(app.getDataOutEnc().getCode());
            apv.setDataOutPwd(app.getDataOutPwd());
            apv.setApiPwd(app.getApiPwd());

            // 加密
            log.info("正在加密参数");
            String apvStr = JSON.toJSONString(apv);
            apvStr = AesCbcPKCS5PaddingUtil.encode(apvStr, "quickAccess");

            if(filename.endsWith(".exe")) {
                log.info("正在整合exe");
                byte[] apvBytes = apvStr.getBytes();
                byte[] tplBytes = FileUtils.readFileToByteArray(new File(PathUtils.getUserPath() + File.separator + "template" + File.separator + "qat.exe.tpl"));
                log.info("快速接入成功");
                byte[] resultBytes = ArrayUtil.addAll(tplBytes, split, apvBytes, split, bytes);
                QuickAccessResultVo result = new QuickAccessResultVo();
                result.setBytes(resultBytes);
                return result;
            } else if(filename.endsWith(".apk")) {
                QuickAccessResultVo result = new QuickAccessResultVo();
                byte[] injectedApk = bytes;
                if ("1".equals(apkOper) || "2".equals(apkOper)) {
                    log.info("正在注入apk");
                    injectedApk = QuickAccessApkUtil.doProcess(bytes, apvStr);
                    log.info("注入apk完毕");
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
                    } catch(Exception e) {
                        e.printStackTrace();
                        log.info("apk签名失败：" + e.getMessage());
                        result.getData().put("signed", false);
                    }
                }
                log.info("快速接入成功");
                result.setBytes(injectedApk);
                return result;
            }
        } catch (IOException | InvalidAlgorithmParameterException | NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String rename(String appName, String filename, String versionName, String remark) {
        return "__" + appName + "_" + (versionName.replaceAll("\\.", "_")) + (StringUtils.isNotBlank(remark) ? "_" + remark : "") + "." + FileNameUtil.extName(filename);
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
    }
}
