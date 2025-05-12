package com.easycode.cloud.controller;


import com.alibaba.fastjson.JSON;
import com.easycode.cloud.domain.dto.DeliveryInspectionTaskDto;
import com.easycode.cloud.domain.vo.DeliveryInspectionTaskVo;
import com.easycode.cloud.service.IInspectTaskService;
import com.weifu.cloud.common.core.web.controller.BaseController;
import com.weifu.cloud.common.core.web.domain.AjaxResult;
import com.weifu.cloud.common.core.web.page.TableDataInfo;
import com.weifu.cloud.common.security.annotation.RequiresPermissions;
import com.weifu.cloud.service.IPrintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 检验任务Controller
 *
 * @author zhanglei
 * @date 2023-04-12
 */
@RestController
@RequestMapping("/inspectTask")
public class InspectTaskController extends BaseController
{

    @Autowired
    private IPrintService printService;

    @Autowired
    private IInspectTaskService inspectTaskService;

    /**
     * 查询上架任务列表
     */
    @GetMapping("/list")
    public TableDataInfo list(DeliveryInspectionTaskDto deliveryInspectionTask)
    {
        startPage();
        List<DeliveryInspectionTaskVo> list = inspectTaskService.selectInspectTaskList(deliveryInspectionTask);
        return getDataTable(list);
    }

    /**
     *获取所有的检验任务
     * @param deliveryInspectionTask
     * @return
     */
    @PostMapping("/getAllInspectionTask")
    public AjaxResult getAllInspectionTask(@RequestBody DeliveryInspectionTaskDto deliveryInspectionTask)
    {
        List<DeliveryInspectionTaskVo> list = inspectTaskService.getAllInspectionTask(deliveryInspectionTask);
        return AjaxResult.success("查询成功", JSON.toJSONString(list));
    }

    /**
     * 打印送检任务
     * @param inspectionTask
     * @return 处理结果
     */
    @PostMapping("/printInspectTask")
    public AjaxResult printInspectTask(@RequestBody DeliveryInspectionTaskDto inspectionTask) throws Exception {
        return AjaxResult.success(inspectTaskService.printInspectTask(inspectionTask));
    }

//    /**
//     * 获取上架任务详细信息
//     */
//    @GetMapping(value = "/{id}")
//    public AjaxResult getInfo(@PathVariable("id") Long id)
//    {
//        return success(inspectTaskService.selectInspectTaskById(id));
//    }
//
//    /**
//     * 打印上架任务
//     */
//    @GetMapping("/print/{id}")
//    public AjaxResult print(@PathVariable("id") Long id)
//    {
//        InspectTask inspectTask = inspectTaskService.selectInspectTaskById(id);
//
//        // 打印T1单
//        String templateCode = "T1";
//        HiprintTemplateDTO dto = new HiprintTemplateDTO();
//        dto.setId(inspectTask.getTaskNo());
//        dto.setTemplateNo(templateCode);
//        dto.setPrinterName("预留");
//        dto.setTitle("T1标签打印");
//        JSONArray jsonArray = new JSONArray();
//        com.alibaba.fastjson2.JSONObject jsonObject = new com.alibaba.fastjson2.JSONObject();
//
//        jsonObject.put("taskType", "质检上架任务");
//        // 目标存储类型
//        jsonObject.put("targetSotrageType", "");
//        // 工厂或库存地点
////        jsonObject.put("location", inspectTask.getFactoryCode());
//        // 操作类型
////        jsonObject.put("operateType", stockInStdOrderDetails.get(0).getMoveType());
//        // 存储类型
//        jsonObject.put("storageType", "");
//        // 移动类型
////        jsonObject.put("moveType", stockInStdOrderDetails.get(0).getMoveType());
//        // 创建时间
//        jsonObject.put("createDate", DateUtils.getNowDate());
//        // 供应商名称
////        jsonObject.put("vendorName", stockInStdOrder.getVendorName());
//        // 用户名
//        jsonObject.put("userName", "");
//        // 货架寿命
//        jsonObject.put("age", "");
//        // 源发货库存地点
//        jsonObject.put("oriiginLoction", inspectTask.getLocationCode());
//        // 重量
////        jsonObject.put("weight", stockInStdOrder.getBtgew());
//        // qrCode
//        jsonObject.put("qrCode", inspectTask.getTaskNo());
//        // 供应商批次
//        jsonObject.put("vendorBatchNo", "");
//        // 物料号
//        jsonObject.put("materialNo", inspectTask.getMaterialNo());
//        // 物料名称
//        jsonObject.put("materialName", inspectTask.getMaterialName());
//        jsonArray.add(jsonObject);
//        dto.setDataArray(jsonArray);
//
////        AjaxResult ajaxResult1 = printService.printByTemplate(dto);
////
////        if(ajaxResult1.isError()) {
////            throw new ServiceException(String.format("打印失败,原因：%s",ajaxResult1.get("msg").toString()));
////        }
//        return success();
//    }
//
//
//    /**
//     * 查询pda上架任务列表
//     */
//    @GetMapping("/pdaList")
//    public AjaxResult pdaList(InspectTaskDto inspectTaskDto)
//    {
//        List<InspectTask> list = inspectTaskService.selectPdaInspectTaskList(inspectTaskDto);
//        return success(list);
//    }
//
//    /**
//     * pda上架任务提交
//     */
//    @Log(title = "退货任务", businessType = BusinessType.INSERT)
//    @PostMapping("/submit")
//    public AjaxResult submit(@RequestBody InspectTaskDto inspectTaskDto)
//    {
//        return toAjax(inspectTaskService.submit(inspectTaskDto));
//    }
}
