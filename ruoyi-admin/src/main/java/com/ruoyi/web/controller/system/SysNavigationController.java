package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.SysNavigation;
import com.ruoyi.system.service.ISysNavigationService;
import com.ruoyi.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 首页导航Controller
 *
 * @author zwgu
 * @date 2022-11-12
 */
@RestController
@RequestMapping("/system/navigation")
public class SysNavigationController extends BaseController {
    @Autowired
    private ISysNavigationService sysNavigationService;

    /**
     * 查询首页导航列表
     */
    @PreAuthorize("@ss.hasPermi('system:navigation:list')")
    @GetMapping("/list")
    public AjaxResult list(SysNavigation sysNavigation) {
        List<SysNavigation> list = sysNavigationService.selectSysNavigationList(sysNavigation);
        return AjaxResult.success(list);
    }

    /**
     * 导出首页导航列表
     */
    @PreAuthorize("@ss.hasPermi('system:navigation:export')")
    @Log(title = "首页导航", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysNavigation sysNavigation) {
        List<SysNavigation> list = sysNavigationService.selectSysNavigationList(sysNavigation);
        ExcelUtil<SysNavigation> util = new ExcelUtil<SysNavigation>(SysNavigation.class);
        util.exportExcel(response, list, "首页导航数据");
    }

    /**
     * 获取首页导航详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:navigation:query')")
    @GetMapping(value = "/{navId}")
    public AjaxResult getInfo(@PathVariable("navId") Long navId) {
        return AjaxResult.success(sysNavigationService.selectSysNavigationByNavId(navId));
    }

    /**
     * 新增首页导航
     */
    @PreAuthorize("@ss.hasPermi('system:navigation:add')")
    @Log(title = "首页导航", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysNavigation sysNavigation) {
        return toAjax(sysNavigationService.insertSysNavigation(sysNavigation));
    }

    /**
     * 修改首页导航
     */
    @PreAuthorize("@ss.hasPermi('system:navigation:edit')")
    @Log(title = "首页导航", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysNavigation sysNavigation) {
        return toAjax(sysNavigationService.updateSysNavigation(sysNavigation));
    }

    /**
     * 删除首页导航
     */
    @PreAuthorize("@ss.hasPermi('system:navigation:remove')")
    @Log(title = "首页导航", businessType = BusinessType.DELETE)
    @DeleteMapping("/{navIds}")
    public AjaxResult remove(@PathVariable Long[] navIds) {
        return toAjax(sysNavigationService.deleteSysNavigationByNavIds(navIds));
    }
}
