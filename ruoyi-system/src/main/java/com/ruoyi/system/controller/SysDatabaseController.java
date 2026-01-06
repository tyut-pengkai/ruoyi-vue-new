package com.ruoyi.system.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.SysDatabase;
import com.ruoyi.system.service.ISysDatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 数据库管理Controller
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/database")
public class SysDatabaseController extends BaseController
{
    @Autowired
    private ISysDatabaseService sysDatabaseService;

    /**
     * 查询数据库管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:database:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysDatabase sysDatabase)
    {
        startPage();
        List<SysDatabase> list = sysDatabaseService.selectSysDatabaseList(sysDatabase);
        return getDataTable(list);
    }

    /**
     * 导出数据库管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:database:export')")
    @Log(title = "数据库管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysDatabase sysDatabase)
    {
        List<SysDatabase> list = sysDatabaseService.selectSysDatabaseList(sysDatabase);
        ExcelUtil<SysDatabase> util = new ExcelUtil<SysDatabase>(SysDatabase.class);
        util.exportExcel(response, list, "数据库管理数据");
    }

    /**
     * 获取数据库管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:database:query')")
    @GetMapping(value = "/{databaseId}")
    public AjaxResult getInfo(@PathVariable("databaseId") Long databaseId)
    {
        return AjaxResult.success(sysDatabaseService.selectSysDatabaseById(databaseId));
    }

    /**
     * 新增数据库管理
     */
    @PreAuthorize("@ss.hasPermi('system:database:add')")
    @Log(title = "数据库管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysDatabase sysDatabase)
    {
        return toAjax(sysDatabaseService.insertSysDatabase(sysDatabase));
    }

    /**
     * 修改数据库管理
     */
    @PreAuthorize("@ss.hasPermi('system:database:edit')")
    @Log(title = "数据库管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysDatabase sysDatabase)
    {
        return toAjax(sysDatabaseService.updateSysDatabase(sysDatabase));
    }

    /**
     * 删除数据库管理
     */
    @PreAuthorize("@ss.hasPermi('system:database:remove')")
    @Log(title = "数据库管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{databaseIds}")
    public AjaxResult remove(@PathVariable Long[] databaseIds)
    {
        return toAjax(sysDatabaseService.deleteSysDatabaseByIds(databaseIds));
    }

    /**
     * 测试数据库连接
     */
    @PreAuthorize("@ss.hasPermi('system:database:test')")
    @PostMapping("/testConnection")
    public AjaxResult testConnection(@RequestBody SysDatabase sysDatabase)
    {
        return sysDatabaseService.testConnection(sysDatabase);
    }

    /**
     * 查询数据库表结构列表
     */
    @PreAuthorize("@ss.hasPermi('system:database:tables')")
    @GetMapping("/tables/{databaseId}")
    public TableDataInfo selectDatabaseTables(@PathVariable Long databaseId)
    {
        List<Map<String, Object>> list = sysDatabaseService.selectDatabaseTables(databaseId);
        return getDataTable(list);
    }

    /**
     * 数据库备份
     */
    @PreAuthorize("@ss.hasPermi('system:database:backup')")
    @PostMapping("/backup/{databaseId}")
    public AjaxResult backupDatabase(@PathVariable Long databaseId)
    {
        return sysDatabaseService.backupDatabase(databaseId);
    }

    /**
     * 数据库恢复
     */
    @PreAuthorize("@ss.hasPermi('system:database:restore')")
    @PostMapping("/restore/{databaseId}")
    public AjaxResult restoreDatabase(@PathVariable Long databaseId, @RequestParam String backupFile)
    {
        return sysDatabaseService.restoreDatabase(databaseId, backupFile);
    }

    /**
     * 导入数据
     */
    @PreAuthorize("@ss.hasPermi('system:database:import')")
    @Log(title = "数据库管理", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(@RequestParam("file") MultipartFile file, boolean updateSupport)
    {
        ExcelUtil<SysDatabase> util = new ExcelUtil<SysDatabase>(SysDatabase.class);
        try
        {
            List<SysDatabase> databaseList = util.importExcel(file.getInputStream());
            String operName = getUsername();
            String message = sysDatabaseService.importDatabase(databaseList, updateSupport, operName);
            return AjaxResult.success(message);
        }
        catch (Exception e)
        {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 导出模板
     */
    @PreAuthorize("@ss.hasPermi('system:database:exportTemplate')")
    @GetMapping("/exportTemplate")
    public void exportTemplate(HttpServletResponse response)
    {
        ExcelUtil<SysDatabase> util = new ExcelUtil<SysDatabase>(SysDatabase.class);
        util.exportExcel(response, null, "数据库管理模板");
    }
}