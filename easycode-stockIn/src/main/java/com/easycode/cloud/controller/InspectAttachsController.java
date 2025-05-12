package com.easycode.cloud.controller;

import com.easycode.cloud.domain.InspectAttachs;
import com.easycode.cloud.domain.vo.InspectAttachsVo;
import com.easycode.cloud.service.IInspectAttachsService;
import com.weifu.cloud.common.core.utils.poi.ExcelUtil;
import com.weifu.cloud.common.core.web.controller.BaseController;
import com.weifu.cloud.common.core.web.domain.AjaxResult;
import com.weifu.cloud.common.core.web.page.TableDataInfo;
import com.weifu.cloud.common.log.annotation.Log;
import com.weifu.cloud.common.log.enums.BusinessType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 送检单附件Controller
 *
 * @author fangshucheng
 * @date 2023-03-31
 */
@RestController
@RequestMapping("/inspectAttachs")
public class InspectAttachsController extends BaseController
{
    @Autowired
    private IInspectAttachsService inspectAttachsService;

    /**
     * 查询送检单附件列表
     */
    @GetMapping("/list")
    public TableDataInfo list(InspectAttachs inspectAttachs)
    {
        startPage();
        List<InspectAttachs> list = inspectAttachsService.selectInspectAttachsList(inspectAttachs);
        return getDataTable(list);
    }

    /**
     * 导出送检单附件列表
     */
    @Log(title = "送检单附件", businessType = BusinessType.EXPORT)
    public void export(HttpServletResponse response, InspectAttachs inspectAttachs)
    {
        List<InspectAttachs> list = inspectAttachsService.selectInspectAttachsList(inspectAttachs);
        ExcelUtil<InspectAttachs> util = new ExcelUtil<InspectAttachs>(InspectAttachs.class);
        util.exportExcel(response, list, "送检单附件数据");
    }

    /**
     * 获取送检单附件详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(inspectAttachsService.selectInspectAttachsById(id));
    }

    /**
     * 新增送检单附件
     */
    @Log(title = "送检单附件", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody InspectAttachs inspectAttachs)
    {
        return toAjax(inspectAttachsService.insertInspectAttachs(inspectAttachs));
    }

    /**
     * 批量新增送检单附件
     */
    @Log(title = "批量新增送检单附件", businessType = BusinessType.INSERT)
    @PostMapping(value = "/batchAdd")
    public AjaxResult batchAdd(@RequestBody InspectAttachsVo inspectAttachsVo)
    {
        return toAjax(inspectAttachsService.batchInsertInspectAttachs(inspectAttachsVo));
    }

    /**
     * 修改送检单附件
     */

    @Log(title = "送检单附件", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InspectAttachs inspectAttachs)
    {
        return toAjax(inspectAttachsService.updateInspectAttachs(inspectAttachs));
    }

    /**
     * 删除送检单附件
     */
    @Log(title = "送检单附件", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(inspectAttachsService.deleteInspectAttachsByIds(ids));
    }
}
