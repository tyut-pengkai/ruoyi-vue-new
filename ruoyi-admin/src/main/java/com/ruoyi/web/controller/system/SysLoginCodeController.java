package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.SysLoginCode;
import com.ruoyi.system.domain.SysLoginCodeTemplate;
import com.ruoyi.system.service.ISysLoginCodeService;
import com.ruoyi.system.service.ISysLoginCodeTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 单码Controller
 *
 * @author zwgu
 * @date 2021-12-03
 */
@RestController
@RequestMapping("/system/loginCode")
public class SysLoginCodeController extends BaseController {
    @Autowired
    private ISysLoginCodeService sysLoginCodeService;
    @Resource
    private ISysLoginCodeTemplateService sysLoginCodeTemplateService;

    /**
     * 查询单码列表
     */
    @PreAuthorize("@ss.hasPermi('system:loginCode:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysLoginCode sysLoginCode) {
        startPage();
        List<SysLoginCode> list = sysLoginCodeService.selectSysLoginCodeList(sysLoginCode);
        return getDataTable(list);
    }

    /**
     * 导出单码列表
     */
    @PreAuthorize("@ss.hasPermi('system:loginCode:export')")
    @Log(title = "单码", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SysLoginCode sysLoginCode) {
        List<SysLoginCode> list = sysLoginCodeService.selectSysLoginCodeList(sysLoginCode);
        ExcelUtil<SysLoginCode> util = new ExcelUtil<SysLoginCode>(SysLoginCode.class);
        return util.exportExcel(list, "单码数据");
    }

    /**
     * 获取单码详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:loginCode:query')")
    @GetMapping(value = "/{cardId}")
    public AjaxResult getInfo(@PathVariable("cardId") Long cardId) {
        return AjaxResult.success(sysLoginCodeService.selectSysLoginCodeByCardId(cardId));
    }

    /**
     * 新增单码
     */
    @PreAuthorize("@ss.hasPermi('system:loginCode:add')")
    @Log(title = "单码", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysLoginCode sysLoginCode) {
        if (sysLoginCode.getTemplateId() == null) {
            sysLoginCode.setCreateBy(getUsername());
            return toAjax(sysLoginCodeService.insertSysLoginCode(sysLoginCode));
        } else {
            if (sysLoginCode.getGenQuantity() == null || sysLoginCode.getGenQuantity() < 0) {
                return AjaxResult.error("制卡数量有误，批量制卡失败");
            }
            SysLoginCodeTemplate sysLoginCodeTemplate = sysLoginCodeTemplateService.selectSysLoginCodeTemplateByTemplateId(sysLoginCode.getTemplateId());
            if (sysLoginCodeTemplate == null) {
                return AjaxResult.error("卡类不存在，批量制卡失败");
            }
            return toAjax(sysLoginCodeTemplateService.genSysLoginCodeBatch(sysLoginCodeTemplate, sysLoginCode.getGenQuantity(), sysLoginCode.getOnSale(), UserConstants.NO, sysLoginCode.getRemark()).size());
        }

    }

    /**
     * 修改单码
     */
    @PreAuthorize("@ss.hasPermi('system:loginCode:edit')")
    @Log(title = "单码", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysLoginCode sysLoginCode) {
        sysLoginCode.setUpdateBy(getUsername());
        return toAjax(sysLoginCodeService.updateSysLoginCode(sysLoginCode));
    }

    /**
     * 删除单码
     */
    @PreAuthorize("@ss.hasPermi('system:loginCode:remove')")
    @Log(title = "单码", businessType = BusinessType.DELETE)
    @DeleteMapping("/{cardIds}")
    public AjaxResult remove(@PathVariable Long[] cardIds) {
        return toAjax(sysLoginCodeService.deleteSysLoginCodeByCardIds(cardIds));
    }
}
