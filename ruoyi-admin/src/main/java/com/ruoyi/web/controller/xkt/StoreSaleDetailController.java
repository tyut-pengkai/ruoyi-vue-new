package com.ruoyi.web.controller.xkt;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.xkt.domain.StoreSaleDetail;
import com.ruoyi.xkt.service.IStoreSaleDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 档口销售明细Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@RestController
@RequestMapping("/rest/v1/store-sale-details")
public class StoreSaleDetailController extends XktBaseController {
    @Autowired
    private IStoreSaleDetailService storeSaleDetailService;

    /**
     * 查询档口销售明细列表
     */
    // @PreAuthorize("@ss.hasPermi('system:detail:list')")
    @GetMapping("/list")
    public TableDataInfo list(StoreSaleDetail storeSaleDetail) {
        startPage();
        List<StoreSaleDetail> list = storeSaleDetailService.selectStoreSaleDetailList(storeSaleDetail);
        return getDataTable(list);
    }

    /**
     * 导出档口销售明细列表
     */
    // @PreAuthorize("@ss.hasPermi('system:detail:export')")
    @Log(title = "档口销售明细", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StoreSaleDetail storeSaleDetail) {
        List<StoreSaleDetail> list = storeSaleDetailService.selectStoreSaleDetailList(storeSaleDetail);
        ExcelUtil<StoreSaleDetail> util = new ExcelUtil<StoreSaleDetail>(StoreSaleDetail.class);
        util.exportExcel(response, list, "档口销售明细数据");
    }

    /**
     * 获取档口销售明细详细信息
     */
    // @PreAuthorize("@ss.hasPermi('system:detail:query')")
    @GetMapping(value = "/{storeSaleDetailId}")
    public R getInfo(@PathVariable("storeSaleDetailId") Long storeSaleDetailId) {
        return success(storeSaleDetailService.selectStoreSaleDetailByStoreSaleDetailId(storeSaleDetailId));
    }

    /**
     * 新增档口销售明细
     */
    // @PreAuthorize("@ss.hasPermi('system:detail:add')")
    @Log(title = "档口销售明细", businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody StoreSaleDetail storeSaleDetail) {
        return success(storeSaleDetailService.insertStoreSaleDetail(storeSaleDetail));
    }

    /**
     * 修改档口销售明细
     */
    // @PreAuthorize("@ss.hasPermi('system:detail:edit')")
    @Log(title = "档口销售明细", businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody StoreSaleDetail storeSaleDetail) {
        return success(storeSaleDetailService.updateStoreSaleDetail(storeSaleDetail));
    }

    /**
     * 删除档口销售明细
     */
    // @PreAuthorize("@ss.hasPermi('system:detail:remove')")
    @Log(title = "档口销售明细", businessType = BusinessType.DELETE)
    @DeleteMapping("/{storeSaleDetailIds}")
    public R remove(@PathVariable Long[] storeSaleDetailIds) {
        return success(storeSaleDetailService.deleteStoreSaleDetailByStoreSaleDetailIds(storeSaleDetailIds));
    }
}
