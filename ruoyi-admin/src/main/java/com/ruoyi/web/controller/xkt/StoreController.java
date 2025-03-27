package com.ruoyi.web.controller.xkt;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.xkt.domain.Store;
import com.ruoyi.xkt.service.IStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 档口Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@RestController
@RequestMapping("/rest/v1/stores")
public class StoreController extends XktBaseController {
    @Autowired
    private IStoreService storeService;

    /**
     * 查询档口列表
     */
    @PreAuthorize("@ss.hasPermi('system:store:list')")
    @GetMapping("/list")
    public TableDataInfo list(Store store) {
        startPage();
        List<Store> list = storeService.selectStoreList(store);
        return getDataTable(list);
    }

    /**
     * 导出档口列表
     */
    @PreAuthorize("@ss.hasPermi('system:store:export')")
    @Log(title = "档口", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Store store) {
        List<Store> list = storeService.selectStoreList(store);
        ExcelUtil<Store> util = new ExcelUtil<Store>(Store.class);
        util.exportExcel(response, list, "档口数据");
    }

    /**
     * 获取档口详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:store:query')")
    @GetMapping(value = "/{storeId}")
    public R getInfo(@PathVariable("storeId") Long storeId) {
        return success(storeService.selectStoreByStoreId(storeId));
    }

    /**
     * 新增档口
     */
    @PreAuthorize("@ss.hasPermi('system:store:add')")
    @Log(title = "档口", businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody Store store) {
        return success(storeService.insertStore(store));
    }

    /**
     * 修改档口
     */
    @PreAuthorize("@ss.hasPermi('system:store:edit')")
    @Log(title = "档口", businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody Store store) {
        return success(storeService.updateStore(store));
    }

    /**
     * 删除档口
     */
    @PreAuthorize("@ss.hasPermi('system:store:remove')")
    @Log(title = "档口", businessType = BusinessType.DELETE)
    @DeleteMapping("/{storeIds}")
    public R remove(@PathVariable Long[] storeIds) {
        return success(storeService.deleteStoreByStoreIds(storeIds));
    }
}
