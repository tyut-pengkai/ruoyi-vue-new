package com.ruoyi.web.controller.xkt;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.xkt.domain.StoreProductProcess;
import com.ruoyi.xkt.service.IStoreProductProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 档口商品工艺信息Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@RestController
@RequestMapping("/rest/v1/prod-processes")
public class StoreProductProcessController extends BaseController {
    @Autowired
    private IStoreProductProcessService storeProductProcessService;

    /**
     * 查询档口商品工艺信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:process:list')")
    @GetMapping("/list")
    public TableDataInfo list(StoreProductProcess storeProductProcess) {
        startPage();
        List<StoreProductProcess> list = storeProductProcessService.selectStoreProductProcessList(storeProductProcess);
        return getDataTable(list);
    }

    /**
     * 导出档口商品工艺信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:process:export')")
    @Log(title = "档口商品工艺信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StoreProductProcess storeProductProcess) {
        List<StoreProductProcess> list = storeProductProcessService.selectStoreProductProcessList(storeProductProcess);
        ExcelUtil<StoreProductProcess> util = new ExcelUtil<StoreProductProcess>(StoreProductProcess.class);
        util.exportExcel(response, list, "档口商品工艺信息数据");
    }

    /**
     * 获取档口商品工艺信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:process:query')")
    @GetMapping(value = "/{storeProdProcessId}")
    public AjaxResult getInfo(@PathVariable("storeProdProcessId") Long storeProdProcessId) {
        return success(storeProductProcessService.selectStoreProductProcessByStoreProdProcessId(storeProdProcessId));
    }

    /**
     * 新增档口商品工艺信息
     */
    @PreAuthorize("@ss.hasPermi('system:process:add')")
    @Log(title = "档口商品工艺信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StoreProductProcess storeProductProcess) {
        return toAjax(storeProductProcessService.insertStoreProductProcess(storeProductProcess));
    }

    /**
     * 修改档口商品工艺信息
     */
    @PreAuthorize("@ss.hasPermi('system:process:edit')")
    @Log(title = "档口商品工艺信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StoreProductProcess storeProductProcess) {
        return toAjax(storeProductProcessService.updateStoreProductProcess(storeProductProcess));
    }

    /**
     * 删除档口商品工艺信息
     */
    @PreAuthorize("@ss.hasPermi('system:process:remove')")
    @Log(title = "档口商品工艺信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{storeProdProcessIds}")
    public AjaxResult remove(@PathVariable Long[] storeProdProcessIds) {
        return toAjax(storeProductProcessService.deleteStoreProductProcessByStoreProdProcessIds(storeProdProcessIds));
    }
}
