package com.ruoyi.web.controller.xkt;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.xkt.domain.StoreSale;
import com.ruoyi.xkt.service.IStoreSaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 档口销售出库Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@RestController
@RequestMapping("/rest/v1/store-sales")
public class StoreSaleController extends XktBaseController {
    @Autowired
    private IStoreSaleService storeSaleService;

    /**
     * 查询档口销售出库列表
     */
    @PreAuthorize("@ss.hasPermi('system:sale:list')")
    @GetMapping("/list")
    public TableDataInfo list(StoreSale storeSale) {
        startPage();
        List<StoreSale> list = storeSaleService.selectStoreSaleList(storeSale);
        return getDataTable(list);
    }

    /**
     * 导出档口销售出库列表
     */
    @PreAuthorize("@ss.hasPermi('system:sale:export')")
    @Log(title = "档口销售出库", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StoreSale storeSale) {
        List<StoreSale> list = storeSaleService.selectStoreSaleList(storeSale);
        ExcelUtil<StoreSale> util = new ExcelUtil<StoreSale>(StoreSale.class);
        util.exportExcel(response, list, "档口销售出库数据");
    }

    /**
     * 获取档口销售出库详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:sale:query')")
    @GetMapping(value = "/{storeSaleId}")
    public R getInfo(@PathVariable("storeSaleId") Long storeSaleId) {
        return success(storeSaleService.selectStoreSaleByStoreSaleId(storeSaleId));
    }

    /**
     * 新增档口销售出库
     */
    @PreAuthorize("@ss.hasPermi('system:sale:add')")
    @Log(title = "档口销售出库", businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody StoreSale storeSale) {
        return success(storeSaleService.insertStoreSale(storeSale));
    }

    /**
     * 修改档口销售出库
     */
    @PreAuthorize("@ss.hasPermi('system:sale:edit')")
    @Log(title = "档口销售出库", businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody StoreSale storeSale) {
        return success(storeSaleService.updateStoreSale(storeSale));
    }

    /**
     * 删除档口销售出库
     */
    @PreAuthorize("@ss.hasPermi('system:sale:remove')")
    @Log(title = "档口销售出库", businessType = BusinessType.DELETE)
    @DeleteMapping("/{storeSaleIds}")
    public R remove(@PathVariable Long[] storeSaleIds) {
        return success(storeSaleService.deleteStoreSaleByStoreSaleIds(storeSaleIds));
    }
}
