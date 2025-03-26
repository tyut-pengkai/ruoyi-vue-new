package com.ruoyi.web.controller.xkt;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.xkt.domain.StoreFactory;
import com.ruoyi.xkt.service.IStoreFactoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 档口合作工厂Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@RestController
@RequestMapping("/rest/v1/store-factories")
public class StoreFactoryController extends BaseController {
    @Autowired
    private IStoreFactoryService storeFactoryService;

    /**
     * 查询档口合作工厂列表
     */
    @PreAuthorize("@ss.hasPermi('system:factory:list')")
    @GetMapping("/list")
    public TableDataInfo list(StoreFactory storeFactory) {
        startPage();
        List<StoreFactory> list = storeFactoryService.selectStoreFactoryList(storeFactory);
        return getDataTable(list);
    }

    /**
     * 导出档口合作工厂列表
     */
    @PreAuthorize("@ss.hasPermi('system:factory:export')")
    @Log(title = "档口合作工厂", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StoreFactory storeFactory) {
        List<StoreFactory> list = storeFactoryService.selectStoreFactoryList(storeFactory);
        ExcelUtil<StoreFactory> util = new ExcelUtil<StoreFactory>(StoreFactory.class);
        util.exportExcel(response, list, "档口合作工厂数据");
    }

    /**
     * 获取档口合作工厂详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:factory:query')")
    @GetMapping(value = "/{storeFacId}")
    public AjaxResult getInfo(@PathVariable("storeFacId") Long storeFacId) {
        return success(storeFactoryService.selectStoreFactoryByStoreFacId(storeFacId));
    }

    /**
     * 新增档口合作工厂
     */
    @PreAuthorize("@ss.hasPermi('system:factory:add')")
    @Log(title = "档口合作工厂", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StoreFactory storeFactory) {
        return toAjax(storeFactoryService.insertStoreFactory(storeFactory));
    }

    /**
     * 修改档口合作工厂
     */
    @PreAuthorize("@ss.hasPermi('system:factory:edit')")
    @Log(title = "档口合作工厂", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StoreFactory storeFactory) {
        return toAjax(storeFactoryService.updateStoreFactory(storeFactory));
    }

    /**
     * 删除档口合作工厂
     */
    @PreAuthorize("@ss.hasPermi('system:factory:remove')")
    @Log(title = "档口合作工厂", businessType = BusinessType.DELETE)
    @DeleteMapping("/{storeFacIds}")
    public AjaxResult remove(@PathVariable Long[] storeFacIds) {
        return toAjax(storeFactoryService.deleteStoreFactoryByStoreFacIds(storeFacIds));
    }
}
