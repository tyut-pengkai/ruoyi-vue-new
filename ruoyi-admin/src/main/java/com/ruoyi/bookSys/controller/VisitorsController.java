package com.ruoyi.bookSys.controller;

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
import com.ruoyi.bookSys.domain.Visitors;
import com.ruoyi.bookSys.service.IVisitorsService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 访客信息Controller
 * 
 * @author ruoyi
 * @date 2025-12-31
 */
@RestController
@RequestMapping("/bookSys/visitors")
public class VisitorsController extends BaseController
{
    @Autowired
    private IVisitorsService visitorsService;

    /**
     * 查询访客信息列表
     */
    @PreAuthorize("@ss.hasPermi('bookSys:visitors:list')")
    @GetMapping("/list")
    public TableDataInfo list(Visitors visitors)
    {
        startPage();
        List<Visitors> list = visitorsService.selectVisitorsList(visitors);
        return getDataTable(list);
    }

    /**
     * 导出访客信息列表
     */
    @PreAuthorize("@ss.hasPermi('bookSys:visitors:export')")
    @Log(title = "访客信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Visitors visitors)
    {
        List<Visitors> list = visitorsService.selectVisitorsList(visitors);
        ExcelUtil<Visitors> util = new ExcelUtil<Visitors>(Visitors.class);
        util.exportExcel(response, list, "访客信息数据");
    }

    /**
     * 获取访客信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('bookSys:visitors:query')")
    @GetMapping(value = "/{visitorId}")
    public AjaxResult getInfo(@PathVariable("visitorId") Long visitorId)
    {
        return success(visitorsService.selectVisitorsByVisitorId(visitorId));
    }

    /**
     * 新增访客信息
     */
    @PreAuthorize("@ss.hasPermi('bookSys:visitors:add')")
    @Log(title = "访客信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Visitors visitors)
    {
        return toAjax(visitorsService.insertVisitors(visitors));
    }

    /**
     * 修改访客信息
     */
    @PreAuthorize("@ss.hasPermi('bookSys:visitors:edit')")
    @Log(title = "访客信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Visitors visitors)
    {
        return toAjax(visitorsService.updateVisitors(visitors));
    }

    /**
     * 删除访客信息
     */
    @PreAuthorize("@ss.hasPermi('bookSys:visitors:remove')")
    @Log(title = "访客信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{visitorIds}")
    public AjaxResult remove(@PathVariable Long[] visitorIds)
    {
        return toAjax(visitorsService.deleteVisitorsByVisitorIds(visitorIds));
    }
}
