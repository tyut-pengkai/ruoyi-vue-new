package com.ruoyi.web.controller.system;

import com.ruoyi.api.v1.constants.ApiDefine;
import com.ruoyi.api.v1.domain.Api;
import com.ruoyi.api.v1.utils.encrypt.AesCbcZeroPaddingUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysApp;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.EncrypType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.ISysAppService;
import com.ruoyi.utils.poi.ExcelUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 软件Controller
 *
 * @author zwgu
 * @date 2021-11-05
 */
@RestController
@RequestMapping("/system/app")
public class SysAppController extends BaseController {
    @Autowired
    private ISysAppService sysAppService;
//    @Value("${server.port}")
//    private int port;

    /**
     * 查询软件列表
     */
    @PreAuthorize("@ss.hasPermi('system:app:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysApp sysApp) {
        startPage();
        List<SysApp> list = sysAppService.selectSysAppList(sysApp);
        for (SysApp app : list) {
            sysAppService.setApiUrl(app);
        }
        return getDataTable(list);
    }

    /**
     * 查询软件列表
     */
    @PreAuthorize("@ss.hasPermi('system:app:list')")
    @GetMapping("/listAll")
    public TableDataInfo listAll(SysApp sysApp) {
        List<SysApp> list = sysAppService.selectSysAppList(sysApp);
        for (SysApp app : list) {
            sysAppService.setApiUrl(app);
        }
        return getDataTable(list);
    }

    /**
     * 导出软件列表
     */
    @PreAuthorize("@ss.hasPermi('system:app:export')")
    @Log(title = "软件管理", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SysApp sysApp) {
        List<SysApp> list = sysAppService.selectSysAppList(sysApp);
        for (SysApp app : list) {
            sysAppService.setApiUrl(app);
        }
        ExcelUtil<SysApp> util = new ExcelUtil<SysApp>(SysApp.class);
        return util.exportExcel(list, "软件数据");
    }

    /**
     * 获取软件详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:app:query')")
    @GetMapping(value = "/{appId}")
    public AjaxResult getInfo(@PathVariable("appId") Long appId) {
        SysApp sysApp = sysAppService.selectSysAppByAppId(appId);
        sysAppService.setApiUrl(sysApp);
        // 设置API加密
        List<Map<String, String>> enApiList = new ArrayList<>();
        Map<String, Api> apis = ApiDefine.apiMap;
        apis.remove("calcSign");
        List<String> apiList = new ArrayList<>(apis.keySet());
        Collections.sort(apiList);
        for (String api : apiList) {
            try {
                Map<String, String> map = new HashMap<>();
                if (StringUtils.isNotBlank(sysApp.getApiPwd())) {
                    String enApi = AesCbcZeroPaddingUtil.encodeSafe(api, sysApp.getApiPwd());
                    map.put("api", api);
                    map.put("enApi", enApi);
                    enApiList.add(map);
                } else {
                    map.put("api", api);
                    map.put("enApi", "未配置密码");
                    enApiList.add(map);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        sysApp.setEnApi(enApiList);
        return AjaxResult.success(sysApp);
    }

    /**
     * 新增软件
     */
    @PreAuthorize("@ss.hasPermi('system:app:add')")
    @Log(title = "软件管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysApp sysApp) {
        if (!sysAppService.checkAppNameUnique(sysApp.getAppName(), null)) {
            return AjaxResult.error("新增软件'" + sysApp.getAppName() + "'失败，软件名称已存在");
        }
        if (sysApp.getDataInEnc() != null && sysApp.getDataInEnc() != EncrypType.NONE && sysApp.getDataInEnc() != EncrypType.BASE64 && StringUtils.isBlank(sysApp.getDataInPwd())) {
            return AjaxResult.error("新增软件'" + sysApp.getAppName() + "'失败，您设置了数据输入加密，但是未提供加密密码");
        }
        if (sysApp.getDataOutEnc() != null && sysApp.getDataOutEnc() != EncrypType.NONE && sysApp.getDataOutEnc() != EncrypType.BASE64 && StringUtils.isBlank(sysApp.getDataOutPwd())) {
            return AjaxResult.error("新增软件'" + sysApp.getAppName() + "'失败，您设置了数据输出加密，但是未提供加密密码");
        }
        if (sysApp.getHeartBeatTime() > -1 && sysApp.getHeartBeatTime() < 30) {
            return AjaxResult.error("新增软件'" + sysApp.getAppName() + "'失败，心跳间隔最低为30秒");
        }
        sysApp.setCreateBy(getUsername());
        sysApp.setCreateTime(DateUtils.getNowDate());
        sysApp.setAppKey(RandomStringUtils.randomAlphanumeric(32));
        sysApp.setAppSecret(RandomStringUtils.randomAlphanumeric(32));
        sysAppService.setApiUrl(sysApp);
        sysApp.setDelFlag("0");
        return toAjax(sysAppService.insertSysApp(sysApp));
    }

    /**
     * 修改软件
     */
    @PreAuthorize("@ss.hasPermi('system:app:edit')")
    @Log(title = "软件管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysApp sysApp) {
        if (!sysAppService.checkAppNameUnique(sysApp.getAppName(), sysApp.getAppId())) {
            return AjaxResult.error("修改软件'" + sysApp.getAppName() + "'失败，软件名称已存在");
        }
        if (sysApp.getDataInEnc() != null && sysApp.getDataInEnc() != EncrypType.NONE && sysApp.getDataInEnc() != EncrypType.BASE64 && StringUtils.isBlank(sysApp.getDataInPwd())) {
            return AjaxResult.error("修改软件'" + sysApp.getAppName() + "'失败，您设置了数据输入加密，但是未提供加密密码");
        }
        if (sysApp.getDataOutEnc() != null && sysApp.getDataOutEnc() != EncrypType.NONE && sysApp.getDataOutEnc() != EncrypType.BASE64 && StringUtils.isBlank(sysApp.getDataOutPwd())) {
            return AjaxResult.error("修改软件'" + sysApp.getAppName() + "'失败，您设置了数据输出加密，但是未提供加密密码");
        }
        if (sysApp.getHeartBeatTime() > -1 && sysApp.getHeartBeatTime() < 30) {
            return AjaxResult.error("修改软件'" + sysApp.getAppName() + "'失败，心跳间隔最低为30秒");
        }
        sysApp.setUpdateBy(getUsername());
        return toAjax(sysAppService.updateSysApp(sysApp));
    }

    /**
     * 删除软件
     */
    @PreAuthorize("@ss.hasPermi('system:app:remove')")
    @Log(title = "软件管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{appIds}")
    public AjaxResult remove(@PathVariable Long[] appIds)
    {
        return toAjax(sysAppService.deleteSysAppByAppIds(appIds));
    }

    /**
     * 状态修改
     */
    @PreAuthorize("@ss.hasPermi('system:app:edit')")
    @Log(title = "软件管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysApp app)
    {
        app.setUpdateBy(getUsername());
        return toAjax(sysAppService.updateSysAppStatus(app));
    }

    /**
     * 计费状态修改
     */
    @PreAuthorize("@ss.hasPermi('system:app:edit')")
    @Log(title = "软件管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeChargeStatus")
    public AjaxResult changeChargeStatus(@RequestBody SysApp app) {
        app.setUpdateBy(getUsername());
        return toAjax(sysAppService.updateSysAppChargeStatus(app));
    }
}
