package com.easycode.cloud.controller;

import com.easycode.cloud.domain.InspectOrder;
import com.easycode.cloud.domain.dto.InspectOrderDto;
import com.easycode.cloud.domain.vo.InspectOrderVo;
import com.easycode.cloud.service.IInspectOrderService;
import com.weifu.cloud.common.core.utils.poi.ExcelUtil;
import com.weifu.cloud.common.core.web.controller.BaseController;
import com.weifu.cloud.common.core.web.domain.AjaxResult;
import com.weifu.cloud.common.core.web.page.TableDataInfo;
import com.weifu.cloud.common.log.annotation.Log;
import com.weifu.cloud.common.log.enums.BusinessType;
import com.weifu.cloud.domain.dto.SendInspectResultDto;
import com.weifu.cloud.domain.dto.SendSealedResultIQCDto;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 送检单Controller
 *
 * @author weifu
 * @date 2023-03-29
 */
@RestController
@RequestMapping("/inspectOrder")
public class InspectOrderController extends BaseController
{
    @Autowired
    private IInspectOrderService inspectOrderService;

    /**
     * 查询送检单列表
     */
    @GetMapping("/list")
    public TableDataInfo list(InspectOrderVo inspectOrder)
    {
        startPage();
        List<InspectOrderVo> list = inspectOrderService.selectInspectOrderList(inspectOrder);
        return getDataTable(list);
    }

    /**
     * 导出送检单列表
     */

    @Log(title = "送检单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, InspectOrderVo inspectOrder)
    {
        List<InspectOrderVo> list = inspectOrderService.selectInspectOrderList(inspectOrder);
        ExcelUtil<InspectOrderVo> util = new ExcelUtil<InspectOrderVo>(InspectOrderVo.class);
        util.exportExcel(response, list, "送检单数据");
    }

    /**
     * 获取送检单详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(inspectOrderService.selectInspectOrderById(id));
    }

    /**
     * 新增送检单
     */
    @Log(title = "送检单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody InspectOrder inspectOrder)
    {
        return toAjax(inspectOrderService.insertInspectOrder(inspectOrder));
    }

    /**
     * 修改送检单
     */

    @Log(title = "送检单录入结果", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InspectOrderDto inspectOrderDto)
    {
        return toAjax(inspectOrderService.updateInspectOrder(inspectOrderDto));
    }

    /**
     * 删除送检单
     */
    @Log(title = "送检单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(inspectOrderService.deleteInspectOrderByIds(ids));
    }


    /**
     * 送检单附件上传
     */
    @Log(title = "送检单附件上传", businessType = BusinessType.OTHER)
    @DeleteMapping("/upload")
    public AjaxResult upload(MultipartFile file)
    {
        return toAjax(inspectOrderService.upload(file));
    }

    /**
     * 发送检验结果
     * @param detailDto
     * @return 检验结果
     */
    @PostMapping("/sendInspectResult")
    @GlobalTransactional
    public AjaxResult sendInspectResult(@RequestBody SendInspectResultDto detailDto) throws Exception {
        return toAjax(inspectOrderService.sendInspectResult(detailDto));
    }


    /**
     * 后处理结果
     * @param resultIQCDto
     * @return 处理结果
     */
    @PostMapping("/sendSealedResultIQC")
    public AjaxResult sendSealedResultIQC(@RequestBody SendSealedResultIQCDto resultIQCDto) throws Exception {
        return toAjax(inspectOrderService.sendSealedResultIQC(resultIQCDto));
    }

    /**
     * 生成送检任务
     * @param inspectOrderDto
     * @return 处理结果
     */
    @PostMapping("generateInspectTask")
    public AjaxResult generateInspectTask(@RequestBody InspectOrderDto inspectOrderDto) throws Exception {
        return toAjax(inspectOrderService.generateInspectTask(inspectOrderDto));
    }

    /**
     * 打印送检任务
     * @param inspectOrderDto
     * @return 处理结果
     */
    @PostMapping("printInspectTask")
    public AjaxResult printInspectTask(@RequestBody InspectOrderDto inspectOrderDto) throws Exception {
        return toAjax(inspectOrderService.printInspectTask(inspectOrderDto));
    }

    /**
     * 手动释放

     * @return
     */
    @PostMapping("/releaseManual")
    public  AjaxResult  releaseManual(@RequestBody InspectOrderDto inspectOrderDto){

        return inspectOrderService.releaseManual(inspectOrderDto);
    }
}
