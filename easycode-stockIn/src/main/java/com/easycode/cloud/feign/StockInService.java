//package com.easycode.cloud.feign;
//
//import com.weifu.cloud.common.core.web.controller.BaseController;
//import com.weifu.cloud.common.core.web.domain.AjaxResult;
//import com.weifu.cloud.common.core.web.page.TableDataInfo;
//import com.weifu.cloud.common.log.annotation.Log;
//import com.weifu.cloud.common.log.enums.BusinessType;
//import com.weifu.cloud.domain.StockInStdOrder;
//import com.easycode.cloud.domain.TaskInfo;
//import com.easycode.cloud.domain.WmsKbAndSwTaskLog;
//import com.weifu.cloud.domain.dto.StockInFinOrderDto;
//import com.weifu.cloud.domain.vo.TaskInfoVo;
//import com.easycode.cloud.service.IRetTaskService;
//import com.easycode.cloud.service.IStockInFinOrderService;
//import com.easycode.cloud.service.IStockInStdOrderService;
//import com.easycode.cloud.service.ITaskInfoService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//
///**
// * 标准入库单 feign接口
// *
// * @author weifu
// * @date 2022-12-07
// */
//@RestController
//public class StockInService extends BaseController {
//    @Autowired
//    private IStockInStdOrderService stockInStdOrderService;
//
//    @Autowired
//    private ITaskInfoService taskInfoService;
//
//    @Autowired
//    private IStockInFinOrderService stockInFinOrderService;
//
//    @Autowired
//    private IRetTaskService retTaskService;
//
//    /**
//     * 新增标准入库单 远程调用接口
//     */
//    @Log(title = "标准入库单", businessType = BusinessType.INSERT)
//    @PostMapping("/stockInStdOrder/add")
//    public AjaxResult addStockInStdOrder(@RequestBody String stockInStdOrderDtoList) {
//        return toAjax(stockInStdOrderService.insertStockInStdOrder(stockInStdOrderDtoList));
//    }
//
//    /**
//     * 修改仓管任务--关闭
//     */
//    @Log(title = "入库任务", businessType = BusinessType.UPDATE)
//    @PutMapping("/taskInfo/taskEdit")
//    public AjaxResult taskEdit(@RequestBody TaskInfo taskInfo) throws Exception {
//        return toAjax(taskInfoService.updateTaskInfo(taskInfo));
//    }
//
//    /**
//     * 新增数位日志
//     * @param wmsKbAndSwTaskLog
//     * @return
//     */
//    @PostMapping("taskInfo/addKbAndSwTaskLog")
//    public AjaxResult addKbAndSwTaskLog(@RequestBody WmsKbAndSwTaskLog wmsKbAndSwTaskLog) {
//        return toAjax(taskInfoService.addKbAndSwTaskLog(wmsKbAndSwTaskLog));
//    }
//
//    /**
//     * 入库任务列表
//     *
//     * @param taskInfo
//     * @return
//     */
//    @GetMapping("/taskInfo/taskList")
//    public TableDataInfo taskLists(TaskInfoVo taskInfo) {
//        startPage();
//        List<TaskInfoVo> list = taskInfoService.selectTaskInfoList(taskInfo);
//        return getDataTable(list);
//    }
//
//    /**
//     * 打印
//     *
//     * @param taskInfo
//     * @return
//     */
//    @GetMapping("/taskInfo/printInfo")
//    public AjaxResult printInfo(TaskInfoVo taskInfo) {
//        return AjaxResult.success(taskInfoService.printInfoAll(taskInfo));
//    }
//
//    /**
//     * 入库任务列表
//     *
//     * @param taskInfo
//     * @return
//     */
//    @GetMapping("/taskInfo/stdTaskList")
//    public TableDataInfo getStdTaskList(TaskInfoVo taskInfo) {
//        startPage();
//        List<TaskInfoVo> list = taskInfoService.selectStdTaskInfoList(taskInfo);
//        return getDataTable(list);
//    }
//
//    @PostMapping("/taskInfo/updatePrinterStatus")
//    public AjaxResult updatePrinterStatus(@RequestBody TaskInfoVo taskInfo) {
//        taskInfoService.updatePrinterStatus(taskInfo);
//        return AjaxResult.success();
//    }
//
//    /**
//     * 修改成品入库单--关闭
//     */
//    @Log(title = "成品入库单", businessType = BusinessType.UPDATE)
//    @PutMapping("/stockInFinOrder/editStatus")
//    public AjaxResult editStatus(@RequestBody StockInFinOrderDto stockInFinOrderDto) {
//        return toAjax(stockInFinOrderService.closeStockInFinOrderOne(stockInFinOrderDto));
//    }
//
//    /**
//     * 修改标准入库单--关闭
//     */
//    @Log(title = "标准入库单", businessType = BusinessType.UPDATE)
//    @PutMapping("/stockInStdOrder/editStdStatus")
//    public AjaxResult editStdStatus(@RequestBody StockInStdOrder stockInStdOrder) {
//        return toAjax(stockInStdOrderService.closeStockInStdOrder(stockInStdOrder));
//    }
//
//    /**
//     * 关闭入库任务(标准收货)
//     * @param ids
//     * @return
//     */
//    @PostMapping("/taskInfo/closeStockInTask")
//    public AjaxResult closeStockInTask(@RequestBody Long[] ids) {
//        return toAjax(stockInStdOrderService.closeStockInTask(ids));
//    }
//
//
//    /**
//     * 关闭入库任务(退货入库)
//     * @param ids
//     * @return
//     */
//    @PostMapping("/closeRetTask")
//    AjaxResult closeRetTask(@RequestBody Long[] ids){
//        return toAjax(retTaskService.closeTask(ids));
//    }
//
//}
