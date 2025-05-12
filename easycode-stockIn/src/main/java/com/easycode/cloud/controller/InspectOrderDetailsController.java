package com.easycode.cloud.controller;

import com.alibaba.fastjson.JSON;
import com.easycode.cloud.domain.InspectOrderDetails;
import com.easycode.cloud.domain.dto.InspectOrderDto;
import com.easycode.cloud.domain.vo.InspectOrderDetailsListVo;
import com.easycode.cloud.domain.vo.InspectOrderDetailsVo;
import com.easycode.cloud.service.IInspectOrderDetailsService;
import com.weifu.cloud.common.core.utils.poi.ExcelUtil;
import com.weifu.cloud.common.core.web.controller.BaseController;
import com.weifu.cloud.common.core.web.domain.AjaxResult;
import com.weifu.cloud.common.core.web.page.TableDataInfo;
import com.weifu.cloud.common.log.annotation.Log;
import com.weifu.cloud.common.log.enums.BusinessType;
import com.weifu.cloud.domain.dto.StockInOrderCommonDto;
import com.weifu.cloud.domain.vo.InspectInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 送检单明细Controller
 *
 * @author weifu
 * @date 2023-03-29
 */
@RestController
@RequestMapping("/inspectOrderDetails")
public class InspectOrderDetailsController extends BaseController
{
    @Autowired
    private IInspectOrderDetailsService inspectOrderDetailsService;

    /**
     * 查询送检单明细列表
     */
    @GetMapping("/list")
    public TableDataInfo list(InspectOrderDetails inspectOrderDetails)
    {
//        startPage();
        List<InspectOrderDetailsVo> list = inspectOrderDetailsService.selectInspectOrderDetailsList(inspectOrderDetails);
        return getDataTable(list);
    }

    /**
     * 查询送检单打印信息
     */
    @GetMapping("/getPrintInfoByIds")
    public AjaxResult getPrintInfoByIds(InspectInfoVo inspectInfoVo)
    {
        List<InspectInfoVo> list = inspectOrderDetailsService.getPrintInfoByIds(inspectInfoVo);
        return success(list);
    }

    /**
     * 导出送检单明细列表
     */
    @Log(title = "送检单明细", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, InspectOrderDetails inspectOrderDetails)
    {
        List<InspectOrderDetailsVo> list = inspectOrderDetailsService.selectInspectOrderDetailsList(inspectOrderDetails);
        ExcelUtil<InspectOrderDetailsVo> util = new ExcelUtil<InspectOrderDetailsVo>(InspectOrderDetailsVo.class);
        util.exportExcel(response, list, "送检单明细数据");
    }

    /**
     * 获取送检单明细详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(inspectOrderDetailsService.selectInspectOrderDetailsById(id));
    }

    /**
     * 新增送检单明细
     */

    @Log(title = "送检单明细", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody InspectOrderDetails inspectOrderDetails)
    {
        return toAjax(inspectOrderDetailsService.insertInspectOrderDetails(inspectOrderDetails));
    }

    /**
     * 修改送检单明细
     */

    @Log(title = "送检单明细", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InspectOrderDetails inspectOrderDetails)
    {
        return toAjax(inspectOrderDetailsService.updateInspectOrderDetails(inspectOrderDetails));
    }


    /**
     * 修改送检单明细
     */
    @Log(title = "送检单明细", businessType = BusinessType.UPDATE)
    @PostMapping(value = "/updateByList")
    public AjaxResult updateByList(@RequestBody InspectOrderDetailsListVo inspectOrderDetails) throws Exception
    {
        return toAjax(inspectOrderDetailsService.updateDetailsByList(inspectOrderDetails));
    }

    /**
     * 删除送检单明细
     */
    @Log(title = "送检单明细", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(inspectOrderDetailsService.deleteInspectOrderDetailsByIds(ids));
    }

    /**
     * 批量释放送检单
     * @param inspectOrderDto 送检单dto
     * @return 未成功的送检单号
     */
    @Log(title = "送检单批量释放", businessType = BusinessType.UPDATE)
    @PostMapping(value = "/releaseBatchInspectOrderDetails")
    public AjaxResult releaseBatchInspectOrderDetails(@RequestBody InspectOrderDto inspectOrderDto)
    {
        return success(inspectOrderDetailsService.releaseBatchInspectOrderDetails(inspectOrderDto));
    }

    /**
     * 根据物料list、单据状态list及检验结果查询待检数量
     * @param stock 检验单信息
     * @Date 2024/05/13
     * @Author fsc
     * @return 物料待检数量集合
     */
    @PostMapping(value = "/queryWaitInspectNum")
    public AjaxResult queryWaitInspectNumGroupByMaterialNo(@RequestBody StockInOrderCommonDto stock)
    {
        return AjaxResult.success("操作成功", JSON.toJSONString(inspectOrderDetailsService.queryWaitInspectNumGroupByMaterialNo(stock)));
    }

}
