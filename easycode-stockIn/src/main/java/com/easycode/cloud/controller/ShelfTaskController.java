package com.easycode.cloud.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.easycode.cloud.domain.ShelfTask;
import com.easycode.cloud.domain.dto.ShelfTaskDto;
import com.easycode.cloud.service.IShelfTaskService;
import com.weifu.cloud.common.core.exception.ServiceException;
import com.weifu.cloud.common.core.utils.DateUtils;
import com.weifu.cloud.common.core.utils.poi.ExcelUtil;
import com.weifu.cloud.common.core.web.controller.BaseController;
import com.weifu.cloud.common.core.web.domain.AjaxResult;
import com.weifu.cloud.common.core.web.page.TableDataInfo;
import com.weifu.cloud.common.log.annotation.Log;
import com.weifu.cloud.common.log.enums.BusinessType;
import com.weifu.cloud.common.security.annotation.RequiresPermissions;
import com.weifu.cloud.domian.WmsPrinterLocation;
import com.weifu.cloud.domian.dto.HiprintTemplateDTO;
import com.weifu.cloud.service.IPrintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 上架任务Controller
 * 
 * @author zhanglei
 * @date 2023-04-12
 */
@RestController
@RequestMapping("/shelfTask")
public class ShelfTaskController extends BaseController
{

    @Autowired
    private IPrintService printService;

    @Autowired
    private IShelfTaskService shelfTaskService;

    /**
     * 查询上架任务列表
     */
    @RequiresPermissions("stockin:shelfTask:list")
    @GetMapping("/list")
    public TableDataInfo list(ShelfTask shelfTask)
    {
        startPage();
        List<ShelfTask> list = shelfTaskService.selectShelfTaskList(shelfTask);
        return getDataTable(list);
    }

    /**
     * 导出上架任务列表
     */
    @RequiresPermissions("stockin:shelfTask:export")
    @Log(title = "上架任务", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ShelfTask shelfTask)
    {
        List<ShelfTask> list = shelfTaskService.selectShelfTaskList(shelfTask);
        ExcelUtil<ShelfTask> util = new ExcelUtil<ShelfTask>(ShelfTask.class);
        util.exportExcel(response, list, "上架任务数据");
    }

    /**
     * 获取上架任务详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(shelfTaskService.selectShelfTaskById(id));
    }

    /**
     * 获取上架任务详细信息
     */
    @GetMapping(value = "getShelfTaskByNo/{taskNo}")
    public AjaxResult getShelfTaskByNo(@PathVariable("taskNo") String taskNo)
    {
        return success(shelfTaskService.selectShelfTaskByTaskNo(taskNo));
    }

    /**
     * 新增上架任务
     */
    @RequiresPermissions("stockin:shelfTask:add")
    @Log(title = "上架任务", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ShelfTask shelfTask)
    {
        return success(shelfTaskService.insertShelfTask(shelfTask));
    }

    /**
     * 修改上架任务
     */
    @RequiresPermissions("stockin:shelfTask:edit")
    @Log(title = "上架任务", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ShelfTask shelfTask)
    {
        return toAjax(shelfTaskService.updateShelfTask(shelfTask));
    }

    /**
     * 删除上架任务
     */
    @RequiresPermissions("stockin:shelfTask:remove")
    @Log(title = "上架任务", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(shelfTaskService.deleteShelfTaskByIds(ids));
    }

    /**
     * 关闭上架任务
     */
    //@RequiresPermissions("stockin:shelfTask:close")
    @GetMapping("/close/{ids}")
    public AjaxResult close(@PathVariable("ids") Long[] ids)
    {
        shelfTaskService.closeShelfTask(ids);
        return success();
    }

    /**
     * 打印上架任务
     */
    @GetMapping("/print/{id}")
    public AjaxResult print(@PathVariable("id") Long id)
    {
        ShelfTask shelfTask = shelfTaskService.selectShelfTaskById(id);
        return success(shelfTaskService.print(shelfTask.getLocationCode(), id));
    }

    /**
     * 打印上架任务
     */
    @GetMapping("/printByLocationCode/{locationCode}/{id}")
    public AjaxResult print(@PathVariable("locationCode") String locationCode, @PathVariable("id") Long id)
    {
        shelfTaskService.print(locationCode, id);
        return success();
    }

    /**
     * 打印上架任务列表
     */
    @GetMapping("/printListByLocationCode/{locationCode}/{ids}")
    public AjaxResult printList(@PathVariable("locationCode") String locationCode, @PathVariable("ids") List<Long> ids)
    {
        shelfTaskService.printList(locationCode, ids);
        return success();
    }

    /**
     * 查询pda上架任务列表
     */
    @GetMapping("/pdaList")
    public AjaxResult pdaList(ShelfTaskDto shelfTaskDto)
    {
        List<ShelfTask> list = shelfTaskService.selectPdaShelfTaskList(shelfTaskDto);
        return success(list);
    }

    /**
     * pda上架任务提交
     */
    @Log(title = "上架任务", businessType = BusinessType.INSERT)
    @PostMapping("/submit")
    public AjaxResult submit(@RequestBody ShelfTaskDto shelfTaskDto)
    {
        return toAjax(shelfTaskService.submit(shelfTaskDto));
    }
}
