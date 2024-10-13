package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysAppVersion;
import com.ruoyi.system.domain.vo.ActivityMethodVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 软件版本信息Service接口
 *
 * @author zwgu
 * @date 2021-12-19
 */
public interface ISysAppVersionService {
    /**
     * 查询软件版本信息
     *
     * @param appVersionId 软件版本信息主键
     * @return 软件版本信息
     */
    public SysAppVersion selectSysAppVersionByAppVersionId(Long appVersionId);

    /**
     * 查询软件版本信息
     *
     * @param appVersion 软件版本信息主键
     * @return 软件版本信息
     */
    public SysAppVersion selectSysAppVersionByAppIdAndVersion(Long appId, Long appVersion);

    /**
     * 查询软件版本信息列表
     *
     * @param sysAppVersion 软件版本信息
     * @return 软件版本信息集合
     */
    public List<SysAppVersion> selectSysAppVersionList(SysAppVersion sysAppVersion);

    /**
     * 新增软件版本信息
     *
     * @param sysAppVersion 软件版本信息
     * @return 结果
     */
    public int insertSysAppVersion(SysAppVersion sysAppVersion);

    /**
     * 修改软件版本信息
     *
     * @param sysAppVersion 软件版本信息
     * @return 结果
     */
    public int updateSysAppVersion(SysAppVersion sysAppVersion);

    /**
     * 批量删除软件版本信息
     *
     * @param appVersionIds 需要删除的软件版本信息主键集合
     * @return 结果
     */
    public int deleteSysAppVersionByAppVersionIds(Long[] appVersionIds);

    /**
     * 删除软件版本信息信息
     *
     * @param appVersionId 软件版本信息主键
     * @return 结果
     */
    public int deleteSysAppVersionByAppVersionId(Long appVersionId);

    /**
     * @param appId APP ID
     * @return 最新版本信息
     */
    public SysAppVersion selectLatestVersionByAppId(Long appId);

    /**
     * @param appId APP ID
     * @return 强制更新到的最新版本信息
     */
    public SysAppVersion selectLatestVersionForceUpdateByAppId(Long appId);

    /**
     * 快速接入系统
     *
     * @param file
     * @param versionId
     * @return
     */
    public Map<String, Object> quickAccess(String accessType, MultipartFile file, Long versionId, boolean updateMd5,
                                           String apkOper, String template, String skin, ActivityMethodVo vo,
                                           boolean fullScreen, boolean enableOffline, boolean hideAutoLogin,
                                           boolean enhancedMode, boolean ignoreSplashActivity, String ignoreActivityKeywords);

    /**
     * 检查软件名称唯一性
     *
     * @return
     */
    public boolean checkVersionNoUnique(Long versionNo, Long appId, Long appVersionId);

    /**
     * 获取快速接入参数信息
     */
    public AjaxResult getQuickAccessParams(Long appVersionId);

    /**
     * 获取快速接入模板列表
     *
     * @return
     */
    public AjaxResult getQuickAccessTemplateList();

    public Map<String, Object> downloadDexFile(Long versionId, String template, String skin,
                                               boolean fullScreen, boolean enableOffline, boolean hideAutoLogin);

    /**
     * 状态修改
     */
    int updateSysVersionStatus(SysAppVersion version);

    /**
     * 强制更新状态修改
     */
    int updateForceUpdateStatus(SysAppVersion version);
}
