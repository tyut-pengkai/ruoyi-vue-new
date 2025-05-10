package com.ruoyi.web.controller.xkt;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.xkt.domain.StoreProductDetail;
import com.ruoyi.xkt.service.IStoreProductDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 档口商品详情内容Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@RestController
@RequestMapping("/rest/v1/prod-details")
public class StoreProductDetailController extends XktBaseController {
    @Autowired
    private IStoreProductDetailService storeProductDetailService;

    /**
     * 查询档口商品详情内容列表
     */
    // @PreAuthorize("@ss.hasPermi('system:detail:list')")
    @GetMapping("/list")
    public TableDataInfo list(StoreProductDetail storeProductDetail) {
        startPage();
        List<StoreProductDetail> list = storeProductDetailService.selectStoreProductDetailList(storeProductDetail);
        return getDataTable(list);
    }

    /**
     * 导出档口商品详情内容列表
     */
    // @PreAuthorize("@ss.hasPermi('system:detail:export')")
    @Log(title = "档口商品详情内容", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StoreProductDetail storeProductDetail) {
        List<StoreProductDetail> list = storeProductDetailService.selectStoreProductDetailList(storeProductDetail);
        ExcelUtil<StoreProductDetail> util = new ExcelUtil<StoreProductDetail>(StoreProductDetail.class);
        util.exportExcel(response, list, "档口商品详情内容数据");
    }

    /**
     * 获取档口商品详情内容详细信息
     */
    // @PreAuthorize("@ss.hasPermi('system:detail:query')")
    @GetMapping(value = "/{storeProdDetailId}")
    public R getInfo(@PathVariable("storeProdDetailId") Long storeProdDetailId) {
        return success(storeProductDetailService.selectStoreProductDetailByStoreProdDetailId(storeProdDetailId));
    }

    /**
     * 新增档口商品详情内容
     */
    // @PreAuthorize("@ss.hasPermi('system:detail:add')")
    @Log(title = "档口商品详情内容", businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody StoreProductDetail storeProductDetail) {
        return success(storeProductDetailService.insertStoreProductDetail(storeProductDetail));
    }

    /**
     * 修改档口商品详情内容
     */
    // @PreAuthorize("@ss.hasPermi('system:detail:edit')")
    @Log(title = "档口商品详情内容", businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody StoreProductDetail storeProductDetail) {
        return success(storeProductDetailService.updateStoreProductDetail(storeProductDetail));
    }

    /**
     * 删除档口商品详情内容
     */
    // @PreAuthorize("@ss.hasPermi('system:detail:remove')")
    @Log(title = "档口商品详情内容", businessType = BusinessType.DELETE)
    @DeleteMapping("/{storeProdDetailIds}")
    public R remove(@PathVariable Long[] storeProdDetailIds) {
        return success(storeProductDetailService.deleteStoreProductDetailByStoreProdDetailIds(storeProdDetailIds));
    }
}
