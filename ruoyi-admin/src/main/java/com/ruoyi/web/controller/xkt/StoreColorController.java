package com.ruoyi.web.controller.xkt;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.xkt.domain.StoreColor;
import com.ruoyi.xkt.service.IStoreColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 档口所有颜色Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@RestController
@RequestMapping("/rest/v1/store-colors")
public class StoreColorController extends XktBaseController {
    @Autowired
    private IStoreColorService storeColorService;

    /**
     * 查询档口所有颜色列表
     */
    @PreAuthorize("@ss.hasPermi('system:color:list')")
    @GetMapping("/list")
    public TableDataInfo list(StoreColor storeColor) {
        startPage();
        List<StoreColor> list = storeColorService.selectStoreColorList(storeColor);
        return getDataTable(list);
    }

    /**
     * 导出档口所有颜色列表
     */
    @PreAuthorize("@ss.hasPermi('system:color:export')")
    @Log(title = "档口所有颜色", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StoreColor storeColor) {
        List<StoreColor> list = storeColorService.selectStoreColorList(storeColor);
        ExcelUtil<StoreColor> util = new ExcelUtil<StoreColor>(StoreColor.class);
        util.exportExcel(response, list, "档口所有颜色数据");
    }

    /**
     * 获取档口所有颜色详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:color:query')")
    @GetMapping(value = "/{storeColorId}")
    public R getInfo(@PathVariable("storeColorId") Long storeColorId) {
        return success(storeColorService.selectStoreColorByStoreColorId(storeColorId));
    }

    /**
     * 新增档口所有颜色
     */
    @PreAuthorize("@ss.hasPermi('system:color:add')")
    @Log(title = "档口所有颜色", businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody StoreColor storeColor) {
        return success(storeColorService.insertStoreColor(storeColor));
    }

    /**
     * 修改档口所有颜色
     */
    @PreAuthorize("@ss.hasPermi('system:color:edit')")
    @Log(title = "档口所有颜色", businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody StoreColor storeColor) {
        return success(storeColorService.updateStoreColor(storeColor));
    }

    /**
     * 删除档口所有颜色
     */
    @PreAuthorize("@ss.hasPermi('system:color:remove')")
    @Log(title = "档口所有颜色", businessType = BusinessType.DELETE)
    @DeleteMapping("/{storeColorIds}")
    public R remove(@PathVariable Long[] storeColorIds) {
        return success(storeColorService.deleteStoreColorByStoreColorIds(storeColorIds));
    }
}
