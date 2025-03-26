package com.ruoyi.web.controller.xkt;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.xkt.domain.StoreCustomer;
import com.ruoyi.xkt.service.IStoreCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 档口客户Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@RestController
@RequestMapping("/rest/v1/store-customers")
public class StoreCustomerController extends BaseController {
    @Autowired
    private IStoreCustomerService storeCustomerService;

    /**
     * 查询档口客户列表
     */
    @PreAuthorize("@ss.hasPermi('system:customer:list')")
    @GetMapping("/list")
    public TableDataInfo list(StoreCustomer storeCustomer) {
        startPage();
        List<StoreCustomer> list = storeCustomerService.selectStoreCustomerList(storeCustomer);
        return getDataTable(list);
    }

    /**
     * 导出档口客户列表
     */
    @PreAuthorize("@ss.hasPermi('system:customer:export')")
    @Log(title = "档口客户", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StoreCustomer storeCustomer) {
        List<StoreCustomer> list = storeCustomerService.selectStoreCustomerList(storeCustomer);
        ExcelUtil<StoreCustomer> util = new ExcelUtil<StoreCustomer>(StoreCustomer.class);
        util.exportExcel(response, list, "档口客户数据");
    }

    /**
     * 获取档口客户详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:customer:query')")
    @GetMapping(value = "/{storeCusId}")
    public AjaxResult getInfo(@PathVariable("storeCusId") Long storeCusId) {
        return success(storeCustomerService.selectStoreCustomerByStoreCusId(storeCusId));
    }

    /**
     * 新增档口客户
     */
    @PreAuthorize("@ss.hasPermi('system:customer:add')")
    @Log(title = "档口客户", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StoreCustomer storeCustomer) {
        return toAjax(storeCustomerService.insertStoreCustomer(storeCustomer));
    }

    /**
     * 修改档口客户
     */
    @PreAuthorize("@ss.hasPermi('system:customer:edit')")
    @Log(title = "档口客户", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StoreCustomer storeCustomer) {
        return toAjax(storeCustomerService.updateStoreCustomer(storeCustomer));
    }

    /**
     * 删除档口客户
     */
    @PreAuthorize("@ss.hasPermi('system:customer:remove')")
    @Log(title = "档口客户", businessType = BusinessType.DELETE)
    @DeleteMapping("/{storeCusIds}")
    public AjaxResult remove(@PathVariable Long[] storeCusIds) {
        return toAjax(storeCustomerService.deleteStoreCustomerByStoreCusIds(storeCusIds));
    }
}
