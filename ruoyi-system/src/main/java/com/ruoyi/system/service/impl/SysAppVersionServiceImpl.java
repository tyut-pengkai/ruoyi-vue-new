package com.ruoyi.system.service.impl;

import cn.hutool.core.util.ArrayUtil;
import com.alibaba.fastjson.JSON;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.domain.entity.SysApp;
import com.ruoyi.common.core.domain.entity.SysAppVersion;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.PathUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.mapper.SysAppVersionMapper;
import com.ruoyi.system.service.ISysAppService;
import com.ruoyi.system.service.ISysAppVersionService;
import lombok.Data;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 软件版本信息Service业务层处理
 *
 * @author zwgu
 * @date 2021-12-19
 */
@Service
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
    public String quickAccess(MultipartFile file, Long versionId, boolean updateMd5) {
        try {
            // 封装
            byte[] bytes = file.getBytes();
            SysAppVersion version = sysAppVersionMapper.selectSysAppVersionByAppVersionId(versionId);
            SysApp app = sysAppService.selectSysAppByAppId(version.getAppId());
            bytes = quickAccessHandle(bytes, version);
            // 生成文件
            String filename = rename(app.getAppName(), file.getOriginalFilename(), version.getVersionName());
            String filePath = RuoYiConfig.getDownloadPath() + filename;
            File desc = new File(filePath);
            if (!desc.getParentFile().exists()) {
                desc.getParentFile().mkdirs();
            }
            assert bytes != null;
            FileUtils.writeByteArrayToFile(desc, bytes);
            // 更新MD5
            if (updateMd5) {
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

    private byte[] quickAccessHandle(byte[] bytes, SysAppVersion version) {
        try {
            SysApp app = sysAppService.selectSysAppByAppId(version.getAppId());
            sysAppService.setApiUrl(app);
            byte[] split = "|*@#||*@#|".getBytes();
            AppParamVo apv = new AppParamVo();
            apv.setApiUrl(app.getApiUrl());
            apv.setAppSecret(app.getAppSecret());
            apv.setVersionNo(version.getVersionNo().toString());
            apv.setDataInEnc(app.getDataInEnc().getCode());
            apv.setDataInPwd(app.getDataInPwd());
            apv.setDataOutEnc(app.getDataOutEnc().getCode());
            apv.setDataOutPwd(app.getDataOutPwd());
            apv.setApiPwd(app.getApiPwd());
            byte[] apvBytes = JSON.toJSONString(apv).getBytes();
            byte[] tplBytes = FileUtils.readFileToByteArray(new File(PathUtils.getUserPath() + "\\quickAccessTemplate.exe"));
            return ArrayUtil.addAll(tplBytes, split, apvBytes, split, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String rename(String appName, String filename, String versionName) {
        return "__" + appName + "_" + (versionName.replaceAll("\\.", "_")) + ".exe";
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
