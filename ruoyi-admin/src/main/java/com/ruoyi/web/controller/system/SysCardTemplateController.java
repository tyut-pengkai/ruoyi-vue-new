package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.GenRule;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysCardTemplate;
import com.ruoyi.system.service.ISysCardTemplateService;
import com.ruoyi.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * 卡密模板Controller
 *
 * @author zwgu
 * @date 2021-11-28
 */
@RestController
@RequestMapping("/system/cardTemplate")
public class SysCardTemplateController extends BaseController
{
    @Autowired
    private ISysCardTemplateService sysCardTemplateService;

    /**
     * 查询卡密模板列表
     */
    @PreAuthorize("@ss.hasPermi('system:cardTemplate:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysCardTemplate sysCardTemplate) {
        startPage();
        List<SysCardTemplate> list = sysCardTemplateService.selectSysCardTemplateList(sysCardTemplate);
        return getDataTable(list);
    }

    /**
     * 查询卡密模板列表
     */
    @PreAuthorize("@ss.hasPermi('system:cardTemplate:list')")
    @GetMapping("/listAll")
    public TableDataInfo listAll(SysCardTemplate sysCardTemplate) {
        List<SysCardTemplate> list = sysCardTemplateService.selectSysCardTemplateList(sysCardTemplate);
        return getDataTable(list);
    }

    /**
     * 导出卡密模板列表
     */
    @PreAuthorize("@ss.hasPermi('system:cardTemplate:export')")
    @Log(title = "卡密模板", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SysCardTemplate sysCardTemplate)
    {
        List<SysCardTemplate> list = sysCardTemplateService.selectSysCardTemplateList(sysCardTemplate);
        ExcelUtil<SysCardTemplate> util = new ExcelUtil<SysCardTemplate>(SysCardTemplate.class);
        return util.exportExcel(list, "卡密模板数据");
    }

    /**
     * 获取卡密模板详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:cardTemplate:query')")
    @GetMapping(value = "/{templateId}")
    public AjaxResult getInfo(@PathVariable("templateId") Long templateId)
    {
        return AjaxResult.success(sysCardTemplateService.selectSysCardTemplateByTemplateId(templateId));
    }

    /**
     * 新增卡密模板
     */
    @PreAuthorize("@ss.hasPermi('system:cardTemplate:add')")
    @Log(title = "卡密模板", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysCardTemplate sysCardTemplate) {
        sysCardTemplate.setCreateBy(getUsername());
        if (sysCardTemplate.getCardNoGenRule() == GenRule.REGEX && StringUtils.isBlank(sysCardTemplate.getCardNoRegex())) {
            throw new ServiceException("卡号正则表达式不能为空");
        }
        if (sysCardTemplate.getCardPassGenRule() == GenRule.REGEX && StringUtils.isBlank(sysCardTemplate.getCardPassRegex())) {
            throw new ServiceException("密码正则表达式不能为空");
        }
        SysCardTemplate template = sysCardTemplateService.selectSysCardTemplateByAppIdAndTemplateName(sysCardTemplate.getAppId(), sysCardTemplate.getCardName());
        if(template != null) {
            throw new ServiceException("卡类不能重名，此软件下已存在名为[" + sysCardTemplate.getCardName() + "]的卡类");
        }
        return toAjax(sysCardTemplateService.insertSysCardTemplate(sysCardTemplate));
    }

    /**
     * 修改卡密模板
     */
    @PreAuthorize("@ss.hasPermi('system:cardTemplate:edit')")
    @Log(title = "卡密模板", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysCardTemplate sysCardTemplate) {
        sysCardTemplate.setUpdateBy(getUsername());
        if (sysCardTemplate.getCardNoGenRule() == GenRule.REGEX && StringUtils.isBlank(sysCardTemplate.getCardNoRegex())) {
            throw new ServiceException("卡号正则表达式不能为空");
        }
        if (sysCardTemplate.getCardPassGenRule() == GenRule.REGEX && StringUtils.isBlank(sysCardTemplate.getCardPassRegex())) {
            throw new ServiceException("密码正则表达式不能为空");
        }
        SysCardTemplate template = sysCardTemplateService.selectSysCardTemplateByAppIdAndTemplateName(sysCardTemplate.getAppId(), sysCardTemplate.getCardName());
        if(template != null && !Objects.equals(template.getTemplateId(), sysCardTemplate.getTemplateId())) {
            throw new ServiceException("卡类不能重名，此软件下已存在名为[" + sysCardTemplate.getCardName() + "]的卡类");
        }
        return toAjax(sysCardTemplateService.updateSysCardTemplate(sysCardTemplate));
    }

    /**
     * 删除卡密模板
     */
    @PreAuthorize("@ss.hasPermi('system:cardTemplate:remove')")
    @Log(title = "卡密模板", businessType = BusinessType.DELETE)
    @DeleteMapping("/{templateIds}")
    public AjaxResult remove(@PathVariable Long[] templateIds)
    {
        return toAjax(sysCardTemplateService.deleteSysCardTemplateByTemplateIds(templateIds));
    }
}