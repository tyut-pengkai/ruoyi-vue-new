package com.easycode.cloud.controller;

import com.alibaba.fastjson.JSON;
import com.easycode.cloud.domain.RetTask;
import com.easycode.cloud.domain.dto.RetTaskDto;
import com.easycode.cloud.service.IRetTaskService;
import com.easycode.cloud.service.factory.CommonFactory;
import com.easycode.common.core.domain.AjaxResult;
import com.weifu.cloud.common.core.utils.poi.ExcelUtil;
import com.weifu.cloud.common.core.web.controller.BaseController;
import com.weifu.cloud.common.core.web.domain.AjaxResult;
import com.weifu.cloud.common.core.web.page.TableDataInfo;
import com.weifu.cloud.common.log.annotation.Log;
import com.weifu.cloud.common.log.enums.BusinessType;
import com.weifu.cloud.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 退货任务Controller
 *
 * @author zhanglei
 * @date 2023-03-13
 */
@RestController
@RequestMapping("/retTask")
public class RetTaskController extends BaseController
{
    @Autowired
    private IRetTaskService retTaskService;

    @Autowired
    CommonFactory<IRetTaskService> commonFactory;

    @Autowired
    private Map<String, IRetTaskService> serviceMap;

    /**
     * 查询退货任务列表
     */
    @GetMapping("/list")
    public TableDataInfo list(RetTask retTask)
    {
        startPage();
        List<RetTask> list = retTaskService.selectRetTaskList(retTask);
        return getDataTable(list);
    }

    /**
     * 导出退货任务列表
     */
    @Log(title = "退货任务", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RetTask retTask)
    {
        List<RetTask> list = retTaskService.selectRetTaskList(retTask);
        ExcelUtil<RetTask> util = new ExcelUtil<RetTask>(RetTask.class);
        util.exportExcel(response, list, "退货任务数据");
    }

    /**
     * 获取退货任务详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(retTaskService.selectRetTaskById(id));
    }

    /**
     * 新增退货任务
     */
    @Log(title = "退货任务", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RetTask retTask)
    {
        return toAjax(retTaskService.insertRetTask(retTask));
    }

    /**
     * 修改退货任务
     */
    @Log(title = "退货任务", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RetTask retTask)
    {
        return toAjax(retTaskService.updateRetTask(retTask));
    }

    /**
     * 删除退货任务
     */
    @Log(title = "退货任务", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(retTaskService.deleteRetTaskByIds(ids));
    }

    /**
     * 查询pda退货任务列表
     */
    @PostMapping("/pdaList")
    public AjaxResult pdaList(@RequestBody RetTaskDto retTaskDto)
    {
        IRetTaskService service = commonFactory.getService("VH", serviceMap);
        List<RetTask> list = service.selectPdaRetTaskList(retTaskDto);
        return AjaxResult.success("查询成功", JSON.toJSONString(list));
    }

    @GetMapping("/pdaListNew")
    public AjaxResult pdaListNew(RetTaskDto retTaskDto)
    {
        IRetTaskService service = commonFactory.getService("VH", serviceMap);
        List<RetTask> list = service.selectPdaRetTaskList(retTaskDto);
        return success(list);
    }

    /**
     * pda退货任务提交
     */
    @Log(title = "退货任务", businessType = BusinessType.INSERT)
    @PostMapping("/submit")
    public AjaxResult submit(@RequestBody RetTaskDto retTaskDto) throws Exception
    {
        String plantCodePreFix = SecurityUtils.getComCode().substring(0, 2);
        IRetTaskService service = commonFactory.getService(plantCodePreFix, serviceMap);
        return toAjax(service.submit(retTaskDto));
    }

    /**
     * 退货任务查询
     */
    @GetMapping(value = "/getRetTaskByMaterialNoType/{id}")
    public AjaxResult getRetTaskByMaterialNoType(@PathVariable("id") Long id)
    {
        return success(retTaskService.getRetTaskByMaterialNoType(id));
    }

    /**
     * 根据任务表中的单据明细id获取对应操作单位及操作单位数量
     */
    @Log(title = "退货任务", businessType = BusinessType.INSERT)
    @GetMapping("/getOperationInfo")
    public AjaxResult getOperationInfo(RetTaskDto retTaskDto) throws Exception
    {
        return AjaxResult.success(retTaskService.getOperationInfo(retTaskDto));
    }
}
