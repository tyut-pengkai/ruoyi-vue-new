package com.easycode.cloud.controller;

import com.easycode.cloud.domain.LabelPrintRecord;
import com.easycode.cloud.domain.LabelPrintRecordExcel;
import com.easycode.cloud.domain.vo.LabelPrintRecordVo;
import com.easycode.cloud.service.ILabelPrintRecordService;
import com.weifu.cloud.common.core.utils.poi.ExcelUtil;
import com.weifu.cloud.common.core.web.controller.BaseController;
import com.weifu.cloud.common.core.web.domain.AjaxResult;
import com.weifu.cloud.common.core.web.page.TableDataInfo;
import com.weifu.cloud.common.log.annotation.Log;
import com.weifu.cloud.common.log.enums.BusinessType;
import com.weifu.cloud.common.security.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 标签打印记录Controller
 * 
 * @author weifu
 * @date 2024-05-14
 */
@RestController
@RequestMapping("/labelPrintRecord")
public class LabelPrintRecordController extends BaseController
{
    @Autowired
    private ILabelPrintRecordService labelPrintRecordService;

    /**
     * 查询标签打印记录列表
     */
    @RequiresPermissions("stockin:printRecord:list")
    @GetMapping("/list")
    public TableDataInfo list(LabelPrintRecordVo labelPrintRecordVo)
    {
        startPage();
        List<LabelPrintRecord> list = labelPrintRecordService.selectLabelPrintRecordList(labelPrintRecordVo);
        return getDataTable(list);
    }

    /**
     * 导出标签打印记录列表
     */
    @RequiresPermissions("stockin:printRecord:export")
    @Log(title = "标签打印记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, LabelPrintRecordVo labelPrintRecordVo)
    {
        List<LabelPrintRecord> list = labelPrintRecordService.selectLabelPrintRecordList(labelPrintRecordVo);
        ExcelUtil<LabelPrintRecord> util = new ExcelUtil<LabelPrintRecord>(LabelPrintRecord.class);
        util.exportExcel(response, list, "标签打印记录数据");
    }

    /**
     * 获取标签打印记录详细信息
     */
    @RequiresPermissions("stockin:printRecord:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(labelPrintRecordService.selectLabelPrintRecordById(id));
    }

    /**
     * 新增标签打印记录
     */
    @RequiresPermissions("stockin:printRecord:add")
    @Log(title = "标签打印记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LabelPrintRecord labelPrintRecord)
    {
        return toAjax(labelPrintRecordService.insertLabelPrintRecord(labelPrintRecord));
    }


    @RequiresPermissions("stockin:printRecord:add")
    @Log(title = "标签打印记录", businessType = BusinessType.INSERT)
    @PostMapping("/addBatch")
    public AjaxResult addBatch(@RequestBody List<LabelPrintRecord> list)
    {
        return toAjax(labelPrintRecordService.addBatch(list));
    }

    /**
     * 修改标签打印记录
     */
    @RequiresPermissions("stockin:printRecord:edit")
    @Log(title = "标签打印记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LabelPrintRecord labelPrintRecord)
    {
        return toAjax(labelPrintRecordService.updateLabelPrintRecord(labelPrintRecord));
    }

    /**
     * 删除标签打印记录
     */
    @RequiresPermissions("stockin:printRecord:remove")
    @Log(title = "标签打印记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(labelPrintRecordService.deleteLabelPrintRecordByIds(ids));
    }


    @RequiresPermissions("stockin:printRecord:add")
    @Log(title = "工序委外打印模板下载", businessType = BusinessType.INSERT)
    @PostMapping("/importTemplate")
    public void printDownload(HttpServletResponse response) {
        ExcelUtil util = new ExcelUtil(LabelPrintRecordExcel.class);
        util.importTemplateExcel(response,"工序委外打印模板");
    }


    @RequiresPermissions("stockin:printRecord:add")
    @Log(title = "工序委外打印导入", businessType = BusinessType.INSERT)
    @PostMapping("/importData")
    public TableDataInfo printDownload(MultipartFile file) throws Exception {
        ExcelUtil util = new ExcelUtil(LabelPrintRecordExcel.class);
        List<LabelPrintRecordExcel> list = util.importExcel(file.getInputStream());
        return getDataTable(list);
    }


    /**
     * 查询生成订单号最大流水
     * @param productionOrderNo
     * @return
     */
    @RequiresPermissions("stockin:printRecord:add")
    @GetMapping("/getProductionMax")
    public AjaxResult getProductionMax(@RequestParam("productionOrderNo") String productionOrderNo)
    {
        return success(labelPrintRecordService.getProductionMax(productionOrderNo));
    }


}
