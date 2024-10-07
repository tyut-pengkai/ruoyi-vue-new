package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RateLimiter;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysAppVersion;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.LimitType;
import com.ruoyi.system.domain.vo.ActivityMethodVo;
import com.ruoyi.system.service.ISysAppService;
import com.ruoyi.system.service.ISysAppVersionService;
import com.ruoyi.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * 软件版本信息Controller
 *
 * @author zwgu
 * @date 2021-12-19
 */
@RestController
@RequestMapping("/system/appVersion")
public class SysAppVersionController extends BaseController {
    @Autowired
    private ISysAppVersionService sysAppVersionService;
    @Autowired
    private ISysAppService sysAppService;
    @Resource
    private RedisCache redisCache;

    /**
     * 查询软件版本信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:appVersion:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysAppVersion sysAppVersion) {
        startPage();
        List<SysAppVersion> list = sysAppVersionService.selectSysAppVersionList(sysAppVersion);
        for (SysAppVersion item : list) {
            item.setApp(sysAppService.selectSysAppByAppId(item.getAppId()));
        }
        return getDataTable(list);
    }

    /**
     * 导出软件版本信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:appVersion:export')")
    @Log(title = "软件版本信息", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SysAppVersion sysAppVersion) {
        List<SysAppVersion> list = sysAppVersionService.selectSysAppVersionList(sysAppVersion);
        ExcelUtil<SysAppVersion> util = new ExcelUtil<SysAppVersion>(SysAppVersion.class);
        return util.exportExcel(list, "软件版本信息数据");
    }

    /**
     * 获取软件版本信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:appVersion:query')")
    @GetMapping(value = "/{appVersionId}")
    public AjaxResult getInfo(@PathVariable("appVersionId") Long appVersionId) {
        SysAppVersion version = sysAppVersionService.selectSysAppVersionByAppVersionId(appVersionId);
        version.setApp(sysAppService.selectSysAppByAppId(version.getAppId()));
        return AjaxResult.success(version);
    }

    /**
     * 新增软件版本信息
     */
    @PreAuthorize("@ss.hasPermi('system:appVersion:add')")
    @Log(title = "软件版本信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysAppVersion sysAppVersion) {
        if (!sysAppVersionService.checkVersionNoUnique(sysAppVersion.getVersionNo(), sysAppVersion.getAppId(), null)) {
            return AjaxResult.error("新增软件版本'" + sysAppVersion.getVersionName() + "'失败，版本号'" + sysAppVersion.getVersionNo() + "'已存在");
        }
        sysAppVersion.setCreateBy(getUsername());
        return toAjax(sysAppVersionService.insertSysAppVersion(sysAppVersion));
    }

    /**
     * 修改软件版本信息
     */
    @PreAuthorize("@ss.hasPermi('system:appVersion:edit')")
    @Log(title = "软件版本信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysAppVersion sysAppVersion) {
        if (!sysAppVersionService.checkVersionNoUnique(sysAppVersion.getVersionNo(), sysAppVersion.getAppId(), sysAppVersion.getAppVersionId())) {
            return AjaxResult.error("新增软件版本'" + sysAppVersion.getVersionName() + "'失败，版本号'" + sysAppVersion.getVersionNo() + "'已存在");
        }
        sysAppVersion.setUpdateBy(getUsername());
        return toAjax(sysAppVersionService.updateSysAppVersion(sysAppVersion));
    }

    /**
     * 删除软件版本信息
     */
    @PreAuthorize("@ss.hasPermi('system:appVersion:remove')")
    @Log(title = "软件版本信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{appVersionIds}")
    public AjaxResult remove(@PathVariable Long[] appVersionIds) {
        return toAjax(sysAppVersionService.deleteSysAppVersionByAppVersionIds(appVersionIds));
    }

    @Log(title = "快速接入", businessType = BusinessType.QUICK_ACCESS)
    // @PreAuthorize("@ss.hasPermi('system:user:import')")
    @PostMapping("/quickAccess")
    public AjaxResult quickAccess(String accessType, MultipartFile file, Long versionId, boolean updateMd5, String apkOper,
                                  String template, String skin, String oriName, String oriPath, String activity, String method, boolean fullScreen, boolean enableOffline, boolean hideAutoLogin, boolean enhancedMode) {
        return AjaxResult.success(sysAppVersionService.quickAccess(accessType, file, versionId, updateMd5, apkOper, template, skin, new ActivityMethodVo(oriName, oriPath, activity, method), fullScreen, enableOffline, hideAutoLogin, enhancedMode));
    }

    @Log(title = "快速接入", businessType = BusinessType.QUICK_ACCESS)
    // @PreAuthorize("@ss.hasPermi('system:user:import')")
    @PostMapping("/downloadDexFile")
    public AjaxResult downloadDexFile(Long versionId, String template, String skin, boolean fullScreen, boolean enableOffline, boolean hideAutoLogin) {
        return AjaxResult.success(sysAppVersionService.downloadDexFile(versionId, template, skin, fullScreen, enableOffline, hideAutoLogin));
    }

    /**
     * 获取快速接入参数信息
     */
    @Log(title = "快速接入", businessType = BusinessType.QUICK_ACCESS)
    @GetMapping("/quickAccessParams/{appVersionId}")
    public AjaxResult getQuickAccessParams(@PathVariable Long appVersionId) {
        return sysAppVersionService.getQuickAccessParams(appVersionId);
    }

    /**
     * 获取快速接入参数信息
     */
    @Log(title = "快速接入", businessType = BusinessType.QUICK_ACCESS)
    @GetMapping("/quickAccessTemplate/list")
    public AjaxResult getQuickAccessTemplateList() {
        return sysAppVersionService.getQuickAccessTemplateList();
    }

    /**
     * 获取快速接入参数信息
     */
    @Log(title = "快速接入(扫码)", businessType = BusinessType.QUICK_ACCESS)
    @RequestMapping("/scan/{appVersionId}/{uuid}")
    @RateLimiter(limitType = LimitType.IP)
    public AjaxResult getQuickAccessParams(@PathVariable Long appVersionId, @PathVariable String uuid) {
        String verifyKey = CacheConstants.CAPTCHA_UUID_KEY + uuid;
        if(redisCache.hasKey(verifyKey)) {
            redisCache.deleteObject(verifyKey);
            return sysAppVersionService.getQuickAccessParams(appVersionId);
        }
        return AjaxResult.error("二维码已过期，请刷新后重新扫描");
    }

    /**
     * 状态修改
     */
    @PreAuthorize("@ss.hasPermi('system:appVersion:edit')")
    @Log(title = "软件版本信息", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysAppVersion version) {
        return toAjax(sysAppVersionService.updateSysVersionStatus(version));
    }

    /**
     * 强制更新状态修改
     */
    @PreAuthorize("@ss.hasPermi('system:appVersion:edit')")
    @Log(title = "软件版本信息", businessType = BusinessType.UPDATE)
    @PutMapping("/changeForceUpdateStatus")
    public AjaxResult changeForceUpdateStatus(@RequestBody SysAppVersion version) {
        return toAjax(sysAppVersionService.updateForceUpdateStatus(version));
    }

}
