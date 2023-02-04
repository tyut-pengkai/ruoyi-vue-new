package com.ruoyi.web.controller.system;

import com.ruoyi.api.v1.utils.ScriptUtils;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.framework.api.v1.utils.ValidUtils;
import com.ruoyi.system.domain.SysGlobalScript;
import com.ruoyi.system.domain.vo.ScriptResultVo;
import com.ruoyi.system.service.ISysGlobalScriptService;
import com.ruoyi.utils.poi.ExcelUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 全局脚本Controller
 *
 * @author zwgu
 * @date 2022-05-19
 */
@RestController
@RequestMapping("/system/globalScript")
public class SysGlobalScriptController extends BaseController {
    @Autowired
    private ISysGlobalScriptService sysGlobalScriptService;
    @Resource
    private ValidUtils validUtils;

    /**
     * 查询全局脚本列表
     */
    @PreAuthorize("@ss.hasPermi('system:globalScript:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysGlobalScript sysGlobalScript) {
        startPage();
        List<SysGlobalScript> list = sysGlobalScriptService.selectSysGlobalScriptList(sysGlobalScript);
        return getDataTable(list);
    }

    /**
     * 导出全局脚本列表
     */
    @PreAuthorize("@ss.hasPermi('system:globalScript:export')")
    @Log(title = "全局脚本", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysGlobalScript sysGlobalScript) {
        List<SysGlobalScript> list = sysGlobalScriptService.selectSysGlobalScriptList(sysGlobalScript);
        ExcelUtil<SysGlobalScript> util = new ExcelUtil<SysGlobalScript>(SysGlobalScript.class);
        util.exportExcel(response, list, "全局脚本数据");
    }

    /**
     * 获取全局脚本详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:globalScript:query')")
    @GetMapping(value = "/{scriptId}")
    public AjaxResult getInfo(@PathVariable("scriptId") Long scriptId) {
        return AjaxResult.success(sysGlobalScriptService.selectSysGlobalScriptByScriptId(scriptId));
    }

    /**
     * 新增全局脚本
     */
    @PreAuthorize("@ss.hasPermi('system:globalScript:add')")
    @Log(title = "全局脚本", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysGlobalScript sysGlobalScript) {
        sysGlobalScript.setCreateBy(getUsername());
        String key = RandomStringUtils.randomAlphanumeric(32);
        while (sysGlobalScriptService.selectSysGlobalScriptByScriptKey(key) != null) {
            key = RandomStringUtils.randomAlphanumeric(32);
        }
        sysGlobalScript.setScriptKey(key);
        return toAjax(sysGlobalScriptService.insertSysGlobalScript(sysGlobalScript));
    }

    /**
     * 修改全局脚本
     */
    @PreAuthorize("@ss.hasPermi('system:globalScript:edit')")
    @Log(title = "全局脚本", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysGlobalScript sysGlobalScript) {
        sysGlobalScript.setUpdateBy(getUsername());
        return toAjax(sysGlobalScriptService.updateSysGlobalScript(sysGlobalScript));
    }

    /**
     * 删除全局脚本
     */
    @PreAuthorize("@ss.hasPermi('system:globalScript:remove')")
    @Log(title = "全局脚本", businessType = BusinessType.DELETE)
    @DeleteMapping("/{scriptIds}")
    public AjaxResult remove(@PathVariable Long[] scriptIds) {
        return toAjax(sysGlobalScriptService.deleteSysGlobalScriptByScriptIds(scriptIds));
    }

    @PreAuthorize("@ss.hasPermi('system:globalScript:test')")
    @Log(title = "全局脚本", businessType = BusinessType.TEST)
    @PostMapping("/scriptTest")
    public AjaxResult scriptTest(@RequestBody SysGlobalScript script) {
        // 渲染脚本
        String scriptContent = script.getContent();
        scriptContent = ScriptUtils.renderScriptContent(scriptContent, null);
        ScriptResultVo scriptResultVo = ScriptUtils.exec(scriptContent, script.getLanguage(), script.getScriptParams());
        return AjaxResult.success(scriptResultVo);
    }

}
