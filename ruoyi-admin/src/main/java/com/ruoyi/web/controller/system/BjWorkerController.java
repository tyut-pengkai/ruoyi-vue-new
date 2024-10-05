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
import com.ruoyi.system.domain.BjWorker;
import com.ruoyi.system.service.IBjWorkerService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 技术人员管理Controller
 * 
 * @author ssq
 * @date 2024-10-05
 */
@RestController
@RequestMapping("/system/worker")
public class BjWorkerController extends BaseController
{
    @Autowired
    private IBjWorkerService bjWorkerService;

    /**
     * 查询技术人员管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:worker:list')")
    @GetMapping("/list")
    public TableDataInfo list(BjWorker bjWorker)
    {
        startPage();
        List<BjWorker> list = bjWorkerService.selectBjWorkerList(bjWorker);
        return getDataTable(list);
    }

    /**
     * 导出技术人员管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:worker:export')")
    @Log(title = "技术人员管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BjWorker bjWorker)
    {
        List<BjWorker> list = bjWorkerService.selectBjWorkerList(bjWorker);
        ExcelUtil<BjWorker> util = new ExcelUtil<BjWorker>(BjWorker.class);
        util.exportExcel(response, list, "技术人员管理数据");
    }

    /**
     * 获取技术人员管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:worker:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(bjWorkerService.selectBjWorkerById(id));
    }

    /**
     * 新增技术人员管理
     */
    @PreAuthorize("@ss.hasPermi('system:worker:add')")
    @Log(title = "技术人员管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BjWorker bjWorker)
    {
        return toAjax(bjWorkerService.insertBjWorker(bjWorker));
    }

    /**
     * 修改技术人员管理
     */
    @PreAuthorize("@ss.hasPermi('system:worker:edit')")
    @Log(title = "技术人员管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BjWorker bjWorker)
    {
        return toAjax(bjWorkerService.updateBjWorker(bjWorker));
    }

    /**
     * 删除技术人员管理
     */
    @PreAuthorize("@ss.hasPermi('system:worker:remove')")
    @Log(title = "技术人员管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(bjWorkerService.deleteBjWorkerByIds(ids));
    }
}
