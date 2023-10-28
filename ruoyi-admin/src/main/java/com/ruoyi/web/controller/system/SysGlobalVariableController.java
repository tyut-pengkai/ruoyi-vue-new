package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.SysGlobalVariable;
import com.ruoyi.system.service.ISysGlobalVariableService;
import com.ruoyi.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

/**
 * 全局变量Controller
 *
 * @author zwgu
 * @date 2022-07-02
 */
@RestController
@RequestMapping("/system/globalVariable")
public class SysGlobalVariableController extends BaseController {
    @Autowired
    private ISysGlobalVariableService sysGlobalVariableService;

    /**
     * 查询全局变量列表
     */
    @PreAuthorize("@ss.hasPermi('system:globalVariable:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysGlobalVariable sysGlobalVariable) {
        startPage();
        List<SysGlobalVariable> list = sysGlobalVariableService.selectSysGlobalVariableList(sysGlobalVariable);
        return getDataTable(list);
    }

    /**
     * 导出全局变量列表
     */
    @PreAuthorize("@ss.hasPermi('system:globalVariable:export')")
    @Log(title = "全局变量", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysGlobalVariable sysGlobalVariable) {
        List<SysGlobalVariable> list = sysGlobalVariableService.selectSysGlobalVariableList(sysGlobalVariable);
        ExcelUtil<SysGlobalVariable> util = new ExcelUtil<SysGlobalVariable>(SysGlobalVariable.class);
        util.exportExcel(response, list, "全局变量数据");
    }

    /**
     * 获取全局变量详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:globalVariable:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(sysGlobalVariableService.selectSysGlobalVariableById(id));
    }

    /**
     * 新增全局变量
     */
    @PreAuthorize("@ss.hasPermi('system:globalVariable:add')")
    @Log(title = "全局变量", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysGlobalVariable sysGlobalVariable) {
        SysGlobalVariable variable = sysGlobalVariableService.selectSysGlobalVariableByName(sysGlobalVariable.getName());
        if (variable != null) {
            throw new ServiceException("已存在同名变量，请更换变量名称后重试");
        }
        return toAjax(sysGlobalVariableService.insertSysGlobalVariable(sysGlobalVariable));
    }

    /**
     * 修改全局变量
     */
    @PreAuthorize("@ss.hasPermi('system:globalVariable:edit')")
    @Log(title = "全局变量", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysGlobalVariable sysGlobalVariable) {
        SysGlobalVariable variable = sysGlobalVariableService.selectSysGlobalVariableByName(sysGlobalVariable.getName());
        if (variable != null && !Objects.equals(variable.getId(), sysGlobalVariable.getId())) {
            throw new ServiceException("已存在同名变量，请更换变量名称后重试");
        }
        return toAjax(sysGlobalVariableService.updateSysGlobalVariable(sysGlobalVariable));
    }

    /**
     * 删除全局变量
     */
    @PreAuthorize("@ss.hasPermi('system:globalVariable:remove')")
    @Log(title = "全局变量", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(sysGlobalVariableService.deleteSysGlobalVariableByIds(ids));
    }
}
