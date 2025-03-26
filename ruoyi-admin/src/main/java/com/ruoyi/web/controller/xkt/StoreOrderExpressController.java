package com.ruoyi.web.controller.xkt;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.xkt.domain.StoreOrderExpress;
import com.ruoyi.xkt.service.IStoreOrderExpressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 档口代发订单快递Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@RestController
@RequestMapping("/rest/v1/store-order-expresses")
public class StoreOrderExpressController extends BaseController {
    @Autowired
    private IStoreOrderExpressService storeOrderExpressService;

    /**
     * 查询档口代发订单快递列表
     */
    @PreAuthorize("@ss.hasPermi('system:express:list')")
    @GetMapping("/list")
    public TableDataInfo list(StoreOrderExpress storeOrderExpress) {
        startPage();
        List<StoreOrderExpress> list = storeOrderExpressService.selectStoreOrderExpressList(storeOrderExpress);
        return getDataTable(list);
    }

    /**
     * 导出档口代发订单快递列表
     */
    @PreAuthorize("@ss.hasPermi('system:express:export')")
    @Log(title = "档口代发订单快递", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StoreOrderExpress storeOrderExpress) {
        List<StoreOrderExpress> list = storeOrderExpressService.selectStoreOrderExpressList(storeOrderExpress);
        ExcelUtil<StoreOrderExpress> util = new ExcelUtil<StoreOrderExpress>(StoreOrderExpress.class);
        util.exportExcel(response, list, "档口代发订单快递数据");
    }

    /**
     * 获取档口代发订单快递详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:express:query')")
    @GetMapping(value = "/{storeOrderExprId}")
    public AjaxResult getInfo(@PathVariable("storeOrderExprId") Long storeOrderExprId) {
        return success(storeOrderExpressService.selectStoreOrderExpressByStoreOrderExprId(storeOrderExprId));
    }

    /**
     * 新增档口代发订单快递
     */
    @PreAuthorize("@ss.hasPermi('system:express:add')")
    @Log(title = "档口代发订单快递", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StoreOrderExpress storeOrderExpress) {
        return toAjax(storeOrderExpressService.insertStoreOrderExpress(storeOrderExpress));
    }

    /**
     * 修改档口代发订单快递
     */
    @PreAuthorize("@ss.hasPermi('system:express:edit')")
    @Log(title = "档口代发订单快递", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StoreOrderExpress storeOrderExpress) {
        return toAjax(storeOrderExpressService.updateStoreOrderExpress(storeOrderExpress));
    }

    /**
     * 删除档口代发订单快递
     */
    @PreAuthorize("@ss.hasPermi('system:express:remove')")
    @Log(title = "档口代发订单快递", businessType = BusinessType.DELETE)
    @DeleteMapping("/{storeOrderExprIds}")
    public AjaxResult remove(@PathVariable Long[] storeOrderExprIds) {
        return toAjax(storeOrderExpressService.deleteStoreOrderExpressByStoreOrderExprIds(storeOrderExprIds));
    }
}
