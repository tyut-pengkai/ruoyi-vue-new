package com.ruoyi.web.controller.xkt;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.xkt.domain.StoreQuickFunction;
import com.ruoyi.xkt.service.IStoreQuickFunctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 档口快捷功能Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@RestController
@RequestMapping("/rest/v1/quick-funcs")
public class StoreQuickFunctionController extends XktBaseController {
    @Autowired
    private IStoreQuickFunctionService storeQuickFunctionService;

    /**
     * 查询档口快捷功能列表
     */
    @PreAuthorize("@ss.hasPermi('system:function:list')")
    @GetMapping("/list")
    public TableDataInfo list(StoreQuickFunction storeQuickFunction) {
        startPage();
        List<StoreQuickFunction> list = storeQuickFunctionService.selectStoreQuickFunctionList(storeQuickFunction);
        return getDataTable(list);
    }

    /**
     * 导出档口快捷功能列表
     */
    @PreAuthorize("@ss.hasPermi('system:function:export')")
    @Log(title = "档口快捷功能", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StoreQuickFunction storeQuickFunction) {
        List<StoreQuickFunction> list = storeQuickFunctionService.selectStoreQuickFunctionList(storeQuickFunction);
        ExcelUtil<StoreQuickFunction> util = new ExcelUtil<StoreQuickFunction>(StoreQuickFunction.class);
        util.exportExcel(response, list, "档口快捷功能数据");
    }

    /**
     * 获取档口快捷功能详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:function:query')")
    @GetMapping(value = "/{storeQuickFuncId}")
    public R getInfo(@PathVariable("storeQuickFuncId") Long storeQuickFuncId) {
        return success(storeQuickFunctionService.selectStoreQuickFunctionByStoreQuickFuncId(storeQuickFuncId));
    }

    /**
     * 新增档口快捷功能
     */
    @PreAuthorize("@ss.hasPermi('system:function:add')")
    @Log(title = "档口快捷功能", businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody StoreQuickFunction storeQuickFunction) {
        return success(storeQuickFunctionService.insertStoreQuickFunction(storeQuickFunction));
    }

    /**
     * 修改档口快捷功能
     */
    @PreAuthorize("@ss.hasPermi('system:function:edit')")
    @Log(title = "档口快捷功能", businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody StoreQuickFunction storeQuickFunction) {
        return success(storeQuickFunctionService.updateStoreQuickFunction(storeQuickFunction));
    }

    /**
     * 删除档口快捷功能
     */
    @PreAuthorize("@ss.hasPermi('system:function:remove')")
    @Log(title = "档口快捷功能", businessType = BusinessType.DELETE)
    @DeleteMapping("/{storeQuickFuncIds}")
    public R remove(@PathVariable Long[] storeQuickFuncIds) {
        return success(storeQuickFunctionService.deleteStoreQuickFunctionByStoreQuickFuncIds(storeQuickFuncIds));
    }
}
