package com.ruoyi.web.controller.xkt;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.xkt.domain.StoreHomepage;
import com.ruoyi.xkt.service.IStoreHomepageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 档口首页Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@RestController
@RequestMapping("/rest/v1/store-homepages")
public class StoreHomepageController extends BaseController {
    @Autowired
    private IStoreHomepageService storeHomepageService;

    /**
     * 查询档口首页列表
     */
    @PreAuthorize("@ss.hasPermi('system:homepage:list')")
    @GetMapping("/list")
    public TableDataInfo list(StoreHomepage storeHomepage) {
        startPage();
        List<StoreHomepage> list = storeHomepageService.selectStoreHomepageList(storeHomepage);
        return getDataTable(list);
    }

    /**
     * 导出档口首页列表
     */
    @PreAuthorize("@ss.hasPermi('system:homepage:export')")
    @Log(title = "档口首页", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StoreHomepage storeHomepage) {
        List<StoreHomepage> list = storeHomepageService.selectStoreHomepageList(storeHomepage);
        ExcelUtil<StoreHomepage> util = new ExcelUtil<StoreHomepage>(StoreHomepage.class);
        util.exportExcel(response, list, "档口首页数据");
    }

    /**
     * 获取档口首页详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:homepage:query')")
    @GetMapping(value = "/{storeHomeId}")
    public AjaxResult getInfo(@PathVariable("storeHomeId") Long storeHomeId) {
        return success(storeHomepageService.selectStoreHomepageByStoreHomeId(storeHomeId));
    }

    /**
     * 新增档口首页
     */
    @PreAuthorize("@ss.hasPermi('system:homepage:add')")
    @Log(title = "档口首页", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StoreHomepage storeHomepage) {
        return toAjax(storeHomepageService.insertStoreHomepage(storeHomepage));
    }

    /**
     * 修改档口首页
     */
    @PreAuthorize("@ss.hasPermi('system:homepage:edit')")
    @Log(title = "档口首页", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StoreHomepage storeHomepage) {
        return toAjax(storeHomepageService.updateStoreHomepage(storeHomepage));
    }

    /**
     * 删除档口首页
     */
    @PreAuthorize("@ss.hasPermi('system:homepage:remove')")
    @Log(title = "档口首页", businessType = BusinessType.DELETE)
    @DeleteMapping("/{storeHomeIds}")
    public AjaxResult remove(@PathVariable Long[] storeHomeIds) {
        return toAjax(storeHomepageService.deleteStoreHomepageByStoreHomeIds(storeHomeIds));
    }
}
