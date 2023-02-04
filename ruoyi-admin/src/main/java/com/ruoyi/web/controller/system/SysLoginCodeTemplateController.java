package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.GenRule;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysLoginCodeTemplate;
import com.ruoyi.system.service.ISysLoginCodeTemplateService;
import com.ruoyi.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 单码类别Controller
 *
 * @author zwgu
 * @date 2022-01-06
 */
@RestController
@RequestMapping("/system/loginCodeTemplate")
public class SysLoginCodeTemplateController extends BaseController {
    @Autowired
    private ISysLoginCodeTemplateService sysLoginCodeTemplateService;

    /**
     * 查询单码类别列表
     */
    @PreAuthorize("@ss.hasPermi('system:loginCodeTemplate:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysLoginCodeTemplate sysLoginCodeTemplate) {
        startPage();
        List<SysLoginCodeTemplate> list = sysLoginCodeTemplateService.selectSysLoginCodeTemplateList(sysLoginCodeTemplate);
        return getDataTable(list);
    }

    /**
     * 查询单码类别列表
     */
    @PreAuthorize("@ss.hasPermi('system:loginCodeTemplate:list')")
    @GetMapping("/listAll")
    public TableDataInfo listAll(SysLoginCodeTemplate sysLoginCodeTemplate) {
        List<SysLoginCodeTemplate> list = sysLoginCodeTemplateService.selectSysLoginCodeTemplateList(sysLoginCodeTemplate);
        return getDataTable(list);
    }

    /**
     * 导出单码类别列表
     */
    @PreAuthorize("@ss.hasPermi('system:loginCodeTemplate:export')")
    @Log(title = "单码类别", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysLoginCodeTemplate sysLoginCodeTemplate) {
        List<SysLoginCodeTemplate> list = sysLoginCodeTemplateService.selectSysLoginCodeTemplateList(sysLoginCodeTemplate);
        ExcelUtil<SysLoginCodeTemplate> util = new ExcelUtil<SysLoginCodeTemplate>(SysLoginCodeTemplate.class);
        util.exportExcel(response, list, "单码类别数据");
    }

    /**
     * 获取单码类别详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:loginCodeTemplate:query')")
    @GetMapping(value = "/{templateId}")
    public AjaxResult getInfo(@PathVariable("templateId") Long templateId) {
        return AjaxResult.success(sysLoginCodeTemplateService.selectSysLoginCodeTemplateByTemplateId(templateId));
    }

    /**
     * 新增单码类别
     */
    @PreAuthorize("@ss.hasPermi('system:loginCodeTemplate:add')")
    @Log(title = "单码类别", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysLoginCodeTemplate sysLoginCodeTemplate) {
        sysLoginCodeTemplate.setCreateBy(getUsername());
        if (sysLoginCodeTemplate.getCardNoGenRule() == GenRule.REGEX && StringUtils.isBlank(sysLoginCodeTemplate.getCardNoRegex())) {
            throw new ServiceException("正则表达式不能为空");
        }
        return toAjax(sysLoginCodeTemplateService.insertSysLoginCodeTemplate(sysLoginCodeTemplate));
    }

    /**
     * 修改单码类别
     */
    @PreAuthorize("@ss.hasPermi('system:loginCodeTemplate:edit')")
    @Log(title = "单码类别", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysLoginCodeTemplate sysLoginCodeTemplate) {
        sysLoginCodeTemplate.setUpdateBy(getUsername());
        if (sysLoginCodeTemplate.getCardNoGenRule() == GenRule.REGEX && StringUtils.isBlank(sysLoginCodeTemplate.getCardNoRegex())) {
            throw new ServiceException("正则表达式不能为空");
        }
        return toAjax(sysLoginCodeTemplateService.updateSysLoginCodeTemplate(sysLoginCodeTemplate));
    }

    /**
     * 删除单码类别
     */
    @PreAuthorize("@ss.hasPermi('system:loginCodeTemplate:remove')")
    @Log(title = "单码类别", businessType = BusinessType.DELETE)
    @DeleteMapping("/{templateIds}")
    public AjaxResult remove(@PathVariable Long[] templateIds) {
        return toAjax(sysLoginCodeTemplateService.deleteSysLoginCodeTemplateByTemplateIds(templateIds));
    }
}
