package com.ruoyi.web.controller.sale;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.sale.domain.SysSaleOrderItemCard;
import com.ruoyi.sale.service.ISysSaleOrderItemCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 订单卡密Controller
 *
 * @author zwgu
 * @date 2022-02-26
 */
@RestController
@RequestMapping("/sale/saleCard")
public class SysSaleOrderItemCardController extends BaseController {
    @Autowired
    private ISysSaleOrderItemCardService sysSaleOrderItemCardService;

    /**
     * 查询订单卡密列表
     */
    @PreAuthorize("@ss.hasPermi('sale:saleCard:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysSaleOrderItemCard sysSaleOrderItemCard) {
        startPage();
        List<SysSaleOrderItemCard> list = sysSaleOrderItemCardService.selectSysSaleOrderItemCardList(sysSaleOrderItemCard);
        return getDataTable(list);
    }

    /**
     * 导出订单卡密列表
     */
    @PreAuthorize("@ss.hasPermi('sale:saleCard:export')")
    @Log(title = "订单卡密", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysSaleOrderItemCard sysSaleOrderItemCard) {
        List<SysSaleOrderItemCard> list = sysSaleOrderItemCardService.selectSysSaleOrderItemCardList(sysSaleOrderItemCard);
        ExcelUtil<SysSaleOrderItemCard> util = new ExcelUtil<SysSaleOrderItemCard>(SysSaleOrderItemCard.class);
        util.exportExcel(response, list, "订单卡密数据");
    }

    /**
     * 获取订单卡密详细信息
     */
    @PreAuthorize("@ss.hasPermi('sale:saleCard:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(sysSaleOrderItemCardService.selectSysSaleOrderItemCardById(id));
    }

    /**
     * 新增订单卡密
     */
    @PreAuthorize("@ss.hasPermi('sale:saleCard:add')")
    @Log(title = "订单卡密", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysSaleOrderItemCard sysSaleOrderItemCard) {
        return toAjax(sysSaleOrderItemCardService.insertSysSaleOrderItemCard(sysSaleOrderItemCard));
    }

    /**
     * 修改订单卡密
     */
    @PreAuthorize("@ss.hasPermi('sale:saleCard:edit')")
    @Log(title = "订单卡密", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysSaleOrderItemCard sysSaleOrderItemCard) {
        return toAjax(sysSaleOrderItemCardService.updateSysSaleOrderItemCard(sysSaleOrderItemCard));
    }

    /**
     * 删除订单卡密
     */
    @PreAuthorize("@ss.hasPermi('sale:saleCard:remove')")
    @Log(title = "订单卡密", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(sysSaleOrderItemCardService.deleteSysSaleOrderItemCardByIds(ids));
    }
}
