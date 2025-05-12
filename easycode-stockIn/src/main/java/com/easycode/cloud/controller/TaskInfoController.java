package com.easycode.cloud.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.easycode.cloud.domain.TaskInfo;
import com.easycode.cloud.domain.WmsKbAndSwTaskLog;
import com.easycode.cloud.domain.dto.OutSourceOrderBomDto;
import com.easycode.cloud.domain.dto.TaskInfoDto;
import com.easycode.cloud.service.IOutsourceOrderBomSyncService;
import com.easycode.cloud.service.ITaskInfoService;
import com.easycode.cloud.service.factory.CommonFactory;
import com.easycode.common.core.controller.BaseController;
import com.weifu.cloud.common.core.utils.poi.ExcelUtil;
import com.weifu.cloud.common.core.web.controller.BaseController;
import com.weifu.cloud.common.core.web.domain.AjaxResult;
import com.weifu.cloud.common.core.web.page.TableDataInfo;
import com.weifu.cloud.common.log.annotation.Log;
import com.weifu.cloud.common.log.enums.BusinessType;
import com.weifu.cloud.common.security.annotation.RequiresPermissions;
import com.weifu.cloud.common.security.utils.SecurityUtils;
import com.weifu.cloud.domain.StockInStdOrderDetail;
import com.weifu.cloud.domain.dto.StockInStdOrderDetailDto;
import com.weifu.cloud.domain.vo.TaskInfoVo;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 仓管任务Controller
 *
 * @author weifu
 * @date 2022-12-12
 */
@RestController
@RequestMapping("/taskInfo")
public class TaskInfoController extends BaseController {
    @Autowired
    private ITaskInfoService taskInfoService;

    @Autowired
    CommonFactory<ITaskInfoService> commonFactory;

    @Autowired
    private Map<String, ITaskInfoService> serviceMap;

    @Autowired
    private IOutsourceOrderBomSyncService iOutsourceOrderBomSyncService;


    /**
     * 查询仓管任务列表
     */
//    @RequiresPermissions("stockin:info:list")
    @GetMapping("/list")
    public TableDataInfo list(TaskInfoVo taskInfo) {
//        startPage();
        List<TaskInfoVo> list = taskInfoService.listBoardTask(taskInfo);
        return getDataTable(list);
    }

    @PostMapping("/listBoardTask")
    public AjaxResult listBoardTask(@RequestBody TaskInfoVo taskInfo) {
        List<TaskInfoVo> list = taskInfoService.listBoardTask(taskInfo);
        return AjaxResult.success("查询成功", JSON.toJSONString(list));
    }

    /**
     * 导出仓管任务列表
     */
    @Log(title = "仓管任务", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TaskInfoVo taskInfo) {
        List<TaskInfoVo> list = taskInfoService.selectTaskInfoList(taskInfo);
        ExcelUtil<TaskInfoVo> util = new ExcelUtil<TaskInfoVo>(TaskInfoVo.class);
        util.exportExcel(response, list, "仓管任务数据");
    }

    /**
     * 获取仓管任务详细信息
     */

    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(taskInfoService.selectTaskInfoById(id));
    }


    @GetMapping(value = "/getById")
    public AjaxResult getByIdInfo(TaskInfoVo taskInfoVo) {
        return success(taskInfoService.selectTaskInfoVById(taskInfoVo));
    }

    @PostMapping(value = "/reSendInspectOrder")
    public AjaxResult reSendInspectOrder(@RequestBody StockInStdOrderDetailDto stockInStdOrderDetailDto) throws Exception {
        taskInfoService.reSendInspectOrder(stockInStdOrderDetailDto);
        return success();
    }


    /**
     * 新增仓管任务
     */

    @Log(title = "仓管任务", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TaskInfo taskInfo) {
        return toAjax(taskInfoService.insertTaskInfo(taskInfo));
    }

    @Log(title = "仓管任务", businessType = BusinessType.INSERT)
    @PostMapping("add")
    public AjaxResult add2(@RequestBody TaskInfo taskInfo) {
        return toAjax(taskInfoService.insertTaskInfo(taskInfo));
    }

    /**
     * 修改仓管任务
     */
    @Log(title = "仓管任务", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TaskInfo taskInfo) throws Exception {
        return toAjax(taskInfoService.updateTaskInfo(taskInfo));
    }

    /**
     * 删除仓管任务
     */
    @Log(title = "仓管任务", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(taskInfoService.deleteTaskInfoByIds(ids));
    }

    /**
     * 查询标准入库任务列表
     */
    @GetMapping("/stdList")
    @Log(title = "仓管任务", businessType = BusinessType.EXPORT)
    public TableDataInfo stdList(TaskInfoVo taskInfoVo) {
        List<TaskInfoVo> list = taskInfoService.selectTaskInfoListByStd(taskInfoVo);
        return getDataTable(list);
    }

    /**
     * 查询标准入库任务入库明细
     */
    @GetMapping("/getStdOrderTaskDetail")
    public AjaxResult getStdOrderTaskDetailByAsnNo(TaskInfoVo taskInfoVo) {
        String plantCodePreFix = SecurityUtils.getComCode().substring(0, 2);
        ITaskInfoService service = commonFactory.getService(plantCodePreFix, serviceMap);
        return success(service.getStdOrderTaskDetail(taskInfoVo));
    }

    /**
     * 标准入库任务提交(ASN)
     */
    @PutMapping("/submitStdASNOrderTask")
    @Log(title = "ASN入库任务提交", businessType = BusinessType.EXPORT)
    public AjaxResult submitStdASNOrderTask(@RequestBody TaskInfoDto taskInfoDto) throws Exception {
        return toAjax(taskInfoService.submitStdAsnOrderTask(taskInfoDto));
    }

    /**
     * 查询成品收货任务列表(pda)
     */
    @GetMapping("/finList")
    public TableDataInfo finList(TaskInfoDto taskInfoDto) {
        List<TaskInfoVo> list = taskInfoService.selectTaskInfoFinList(taskInfoDto);
        return getDataTable(list);
    }

    /**
     * 获取成品收货任务详细信息
     */
    @GetMapping("/getFinInfo/{id}")
    public AjaxResult getFinInfo(@PathVariable("id") Long id) {
        return success(taskInfoService.selectTaskFinInfoById(id));
    }

    /**
     * 成品收货任务提交
     */
    @PostMapping("/submitFinOrderTask")
    public AjaxResult submitFinOrderTask(@RequestBody TaskInfoDto taskInfoDto) throws Exception {

        String plantCodePreFix = SecurityUtils.getComCode().substring(0, 2);
        ITaskInfoService service = commonFactory.getService(plantCodePreFix, serviceMap);
        return toAjax(service.submitFinOrderTask(taskInfoDto));
    }

    /**
     * 获取成品收货单详细信息
     */
    @GetMapping("/getFinDetail/{id}")
    public AjaxResult getFinDetail(@PathVariable("id") Long id) {
        return success(taskInfoService.selectFinDetailById(id));
    }

    /**
     * 查询成品收货单详情列表
     */
    @GetMapping("/finDetailList")
    public TableDataInfo finDetailList(TaskInfo taskInfo) {
        List<TaskInfoVo> list = taskInfoService.selectFinDetailListList(taskInfo);
        return getDataTable(list);
    }

    /**
     * 获取标准收货明细信息
     */
    @PostMapping("/getTaskInfoListByOrderNo")
    public AjaxResult getTaskInfoListByOrderNo(@RequestBody StockInStdOrderDetail stockInStdOrderDetail)
    {
        List<TaskInfoVo> list = taskInfoService.getTaskInfoListByOrderNo(stockInStdOrderDetail);
        return AjaxResult.success("查询成功", JSONObject.toJSONString(list));
    }

    /**
     * mom-wms容器信息
     */
    @GetMapping("/mesToWmsContainer")
    public AjaxResult mesToWmsContainer(TaskInfoDto taskInfoDto) {
        String plantCodePreFix = SecurityUtils.getComCode().substring(0, 2);
        ITaskInfoService service = commonFactory.getService(plantCodePreFix, serviceMap);
        return success(service.mesToWmsContainer(taskInfoDto.getContainerNo()));
    }

    /**
     * 成品收货任务提交(mom)
     */
    @PostMapping("/submitByMom")
    public AjaxResult submitByMom(@RequestBody TaskInfoDto taskInfoDto) throws Exception {
        return toAjax(taskInfoService.submitByMom(taskInfoDto));
    }

    /**
     * 同步激活成品收货单、明细、任务(pda进入成品收货页面后触发)
     */
    @GetMapping("/activeFin/{id}")
    public AjaxResult activeFin(@PathVariable("id") Long id) {
        return toAjax(taskInfoService.activeFin(id));
    }

    /**
     * 查询半成品收货任务列表(pda)
     */
    @GetMapping("/getSemiFinTaskList")
    public TableDataInfo getSemiFinTaskList(TaskInfoDto taskInfoDto) {
        List<TaskInfoVo> list = taskInfoService.getSemiFinTaskList(taskInfoDto);
        return getDataTable(list);
    }

    /**
     * 获取半成品收货任务(pda)
     */
    @GetMapping("/getSemiFinTaskById/{id}")
    public AjaxResult getSemiFinTaskById(@PathVariable("id") Long id) {
        return success(taskInfoService.getSemiFinTaskById(id));
    }

    /**
     * 同步激活半成品收货单(pda进入半成品收货页面后触发)
     */
    @GetMapping("/activeSemiFinTask/{id}")
    public AjaxResult activeSemiFinTask(@PathVariable("id") Long id) {
        return toAjax(taskInfoService.activeSemiFinTask(id));
    }

    /**
     * 半成品收货任务提交(pda)
     */
    @PostMapping("/submitSemiFinOrderTask")
    public AjaxResult submitSemiFinOrderTask(@RequestBody TaskInfoDto taskInfoDto) throws Exception {

        String plantCodePreFix = SecurityUtils.getComCode().substring(0, 2);
        ITaskInfoService service = commonFactory.getService(plantCodePreFix, serviceMap);
        return toAjax(service.submitSemiFinOrderTask(taskInfoDto));
    }

    /**
     * 获取委外订单bom
     */
    @GetMapping("/getOutsourceBomList/{purchaseOrderNo}")
    public TableDataInfo getOutsourceBomList(@PathVariable("purchaseOrderNo") String purchaseOrderNo) throws Exception {
        List<OutSourceOrderBomDto> list = iOutsourceOrderBomSyncService.getOutsourceBomList(purchaseOrderNo);
        return getDataTable(list);
    }

    /**
     * 根据任务类型获取任务列表
     */
    @GetMapping("/getTaskListByTaskType")
    public AjaxResult getTaskListByTaskType(TaskInfoDto taskInfoDto) {
        List<TaskInfoVo> list = taskInfoService.getTaskListByTaskType(taskInfoDto);
        return AjaxResult.success(list);
    }

    /**
     * 成品/半成品出库任务确认
     */
    @PutMapping("/submitTask")
    @GlobalTransactional
    @Log(title = "成品/半成品出库任务确认", businessType = BusinessType.EXPORT)
    public AjaxResult submitTask(@RequestBody TaskInfoDto taskInfoDto) throws Exception {
        return toAjax(taskInfoService.submitTask(taskInfoDto));
    }

    /**
     * 根据ID获取任务详情
     * @param id
     * @return
     */
    @GetMapping("/getTaskById")
    public AjaxResult getTaskById(Long id) throws Exception{
        return AjaxResult.success(taskInfoService.getTaskById(id));
    }

    /**
     * 看板领用
     */
    @GetMapping("/getTaskInfoList")
    public TableDataInfo getTaskInfoList(TaskInfoVo taskInfo) {
        List<TaskInfoVo> list = taskInfoService.listBoardTask(taskInfo);
        return getDataTable(list);
    }

    /**
     * 根据taskNo获取数据
     */
    @GetMapping("taskInfo/getTaskInfByTaskNo")
    public AjaxResult getTaskInfByTaskNo(@RequestBody String taskNo){
        return AjaxResult.success(taskInfoService.getTaskInfoListByTastNo(taskNo));
    }

    /**
     * 新增数位日志
     * @param wmsKbAndSwTaskLog
     * @return
     */
    @PostMapping("taskInfo/addKbAndSwTaskLog")
    public AjaxResult addKbAndSwTaskLog(@RequestBody WmsKbAndSwTaskLog wmsKbAndSwTaskLog) {
        return toAjax(taskInfoService.addKbAndSwTaskLog(wmsKbAndSwTaskLog));
    }

    /**
     * 交货单出库任务删除
     * @param taskInfo
     * @return
     */
    @PostMapping("taskInfo/deleteByOrderNoAndType")
    public AjaxResult deleteByOrderNoAndType(@RequestBody TaskInfo taskInfo) {
        return toAjax(taskInfoService.deleteByOrderNoAndType(taskInfo));
    }

    @Log(title = "仓管任务", businessType = BusinessType.UPDATE)
    @PutMapping("/updateTaskInfoById")
    public AjaxResult updateTaskInfoById(@RequestBody TaskInfo taskInfo) {
        return toAjax(taskInfoService.updateTaskInfoById(taskInfo));
    }
}
