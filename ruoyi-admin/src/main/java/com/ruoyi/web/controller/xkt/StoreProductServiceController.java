package com.ruoyi.web.controller.xkt;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.xkt.domain.StoreProductService;
import com.ruoyi.xkt.service.IStoreProductServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 档口商品服务Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@RestController
@RequestMapping("/rest/v1/prod-services")
public class StoreProductServiceController extends BaseController {
    @Autowired
    private IStoreProductServiceService storeProductServiceService;

    /**
     * 查询档口商品服务列表
     */
    @PreAuthorize("@ss.hasPermi('system:service:list')")
    @GetMapping("/list")
    public TableDataInfo list(StoreProductService storeProductService) {
        startPage();
        List<StoreProductService> list = storeProductServiceService.selectStoreProductServiceList(storeProductService);
        return getDataTable(list);
    }

    /**
     * 导出档口商品服务列表
     */
    @PreAuthorize("@ss.hasPermi('system:service:export')")
    @Log(title = "档口商品服务", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StoreProductService storeProductService) {
        List<StoreProductService> list = storeProductServiceService.selectStoreProductServiceList(storeProductService);
        ExcelUtil<StoreProductService> util = new ExcelUtil<StoreProductService>(StoreProductService.class);
        util.exportExcel(response, list, "档口商品服务数据");
    }

    /**
     * 获取档口商品服务详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:service:query')")
    @GetMapping(value = "/{storeProdSvcId}")
    public AjaxResult getInfo(@PathVariable("storeProdSvcId") Long storeProdSvcId) {
        return success(storeProductServiceService.selectStoreProductServiceByStoreProdSvcId(storeProdSvcId));
    }

    /**
     * 新增档口商品服务
     */
    @PreAuthorize("@ss.hasPermi('system:service:add')")
    @Log(title = "档口商品服务", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StoreProductService storeProductService) {
        return toAjax(storeProductServiceService.insertStoreProductService(storeProductService));
    }

    /**
     * 修改档口商品服务
     */
    @PreAuthorize("@ss.hasPermi('system:service:edit')")
    @Log(title = "档口商品服务", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StoreProductService storeProductService) {
        return toAjax(storeProductServiceService.updateStoreProductService(storeProductService));
    }

    /**
     * 删除档口商品服务
     */
    @PreAuthorize("@ss.hasPermi('system:service:remove')")
    @Log(title = "档口商品服务", businessType = BusinessType.DELETE)
    @DeleteMapping("/{storeProdSvcIds}")
    public AjaxResult remove(@PathVariable Long[] storeProdSvcIds) {
        return toAjax(storeProductServiceService.deleteStoreProductServiceByStoreProdSvcIds(storeProdSvcIds));
    }
}
