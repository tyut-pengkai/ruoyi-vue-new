package com.ruoyi.web.controller.system;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.SysPaper;
import com.ruoyi.system.service.ISysPaperService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 论文信息Controller
 * 
 * @author ruoyi
 * @date 2023-10-01
 */
@RestController
@RequestMapping("/system/paper")
public class SysPaperController extends BaseController
{
    @Autowired
    private ISysPaperService sysPaperService;

    /**
     * 查询论文信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:paper:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysPaper sysPaper)
    {
        startPage();
        List<SysPaper> list = sysPaperService.selectSysPaperList(sysPaper);
        return getDataTable(list);
    }

    /**
     * 导出论文信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:paper:export')")
    @Log(title = "论文信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysPaper sysPaper)
    {
        List<SysPaper> list = sysPaperService.selectSysPaperList(sysPaper);
        ExcelUtil<SysPaper> util = new ExcelUtil<SysPaper>(SysPaper.class);
        util.exportExcel(response, list, "论文信息数据");
    }

    /**
     * 获取论文信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:paper:query')")
    @GetMapping(value = "/{paperId}")
    public AjaxResult getInfo(@PathVariable("paperId") Long paperId)
    {
        return AjaxResult.success(sysPaperService.selectSysPaperByPaperId(paperId));
    }

    /**
     * 新增论文信息
     */
    @PreAuthorize("@ss.hasPermi('system:paper:add')")
    @Log(title = "论文信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysPaper sysPaper)
    {
        return toAjax(sysPaperService.insertSysPaper(sysPaper));
    }

    /**
     * 修改论文信息
     */
    @PreAuthorize("@ss.hasPermi('system:paper:edit')")
    @Log(title = "论文信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysPaper sysPaper)
    {
        return toAjax(sysPaperService.updateSysPaper(sysPaper));
    }

    /**
     * 删除论文信息
     */
    @PreAuthorize("@ss.hasPermi('system:paper:remove')")
    @Log(title = "论文信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{paperIds}")
    public AjaxResult remove(@PathVariable Long[] paperIds)
    {
        return toAjax(sysPaperService.deleteSysPaperByPaperIds(paperIds));
    }
}