package com.ruoyi.web.controller.xkt;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.xkt.domain.StoreProductDemand;
import com.ruoyi.xkt.service.IStoreProductDemandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 档口商品需求单Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@RestController
@RequestMapping("/rest/v1/prod-demands")
public class StoreProductDemandController extends XktBaseController {
    @Autowired
    private IStoreProductDemandService storeProductDemandService;


    // TODO 入库单判断需求单是否存在
    // TODO 入库单判断需求单是否存在
    // TODO 入库单判断需求单是否存在
    // TODO 入库单判断需求单是否存在



    /**
     * 查询档口商品需求单列表
     */
    @PreAuthorize("@ss.hasPermi('system:demand:list')")
    @GetMapping("/list")
    public TableDataInfo list(StoreProductDemand storeProductDemand) {
        startPage();
        List<StoreProductDemand> list = storeProductDemandService.selectStoreProductDemandList(storeProductDemand);
        return getDataTable(list);
    }

    /**
     * 导出档口商品需求单列表
     */
    @PreAuthorize("@ss.hasPermi('system:demand:export')")
    @Log(title = "档口商品需求单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StoreProductDemand storeProductDemand) {
        List<StoreProductDemand> list = storeProductDemandService.selectStoreProductDemandList(storeProductDemand);
        ExcelUtil<StoreProductDemand> util = new ExcelUtil<StoreProductDemand>(StoreProductDemand.class);
        util.exportExcel(response, list, "档口商品需求单数据");
    }

    /**
     * 获取档口商品需求单详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:demand:query')")
    @GetMapping(value = "/{storeProdDemandId}")
    public R getInfo(@PathVariable("storeProdDemandId") Long storeProdDemandId) {
        return success(storeProductDemandService.selectStoreProductDemandByStoreProdDemandId(storeProdDemandId));
    }

    /**
     * 新增档口商品需求单
     */
    @PreAuthorize("@ss.hasPermi('system:demand:add')")
    @Log(title = "档口商品需求单", businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody StoreProductDemand storeProductDemand) {
        return success(storeProductDemandService.insertStoreProductDemand(storeProductDemand));
    }

    /**
     * 修改档口商品需求单
     */
    @PreAuthorize("@ss.hasPermi('system:demand:edit')")
    @Log(title = "档口商品需求单", businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody StoreProductDemand storeProductDemand) {
        return success(storeProductDemandService.updateStoreProductDemand(storeProductDemand));
    }

    /**
     * 删除档口商品需求单
     */
    @PreAuthorize("@ss.hasPermi('system:demand:remove')")
    @Log(title = "档口商品需求单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{storeProdDemandIds}")
    public R remove(@PathVariable Long[] storeProdDemandIds) {
        return success(storeProductDemandService.deleteStoreProductDemandByStoreProdDemandIds(storeProdDemandIds));
    }
}
