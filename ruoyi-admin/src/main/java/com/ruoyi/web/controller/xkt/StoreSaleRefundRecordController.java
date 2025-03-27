package com.ruoyi.web.controller.xkt;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.xkt.domain.StoreSaleRefundRecord;
import com.ruoyi.xkt.service.IStoreSaleRefundRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 档口销售返单Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@RestController
@RequestMapping("/rest/v1/sale-refund-records")
public class StoreSaleRefundRecordController extends XktBaseController {
    @Autowired
    private IStoreSaleRefundRecordService storeSaleRefundRecordService;

    /**
     * 查询档口销售返单列表
     */
    @PreAuthorize("@ss.hasPermi('system:record:list')")
    @GetMapping("/list")
    public TableDataInfo list(StoreSaleRefundRecord storeSaleRefundRecord) {
        startPage();
        List<StoreSaleRefundRecord> list = storeSaleRefundRecordService.selectStoreSaleRefundRecordList(storeSaleRefundRecord);
        return getDataTable(list);
    }

    /**
     * 导出档口销售返单列表
     */
    @PreAuthorize("@ss.hasPermi('system:record:export')")
    @Log(title = "档口销售返单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StoreSaleRefundRecord storeSaleRefundRecord) {
        List<StoreSaleRefundRecord> list = storeSaleRefundRecordService.selectStoreSaleRefundRecordList(storeSaleRefundRecord);
        ExcelUtil<StoreSaleRefundRecord> util = new ExcelUtil<StoreSaleRefundRecord>(StoreSaleRefundRecord.class);
        util.exportExcel(response, list, "档口销售返单数据");
    }

    /**
     * 获取档口销售返单详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:record:query')")
    @GetMapping(value = "/{storeSaleRefundRecordId}")
    public R getInfo(@PathVariable("storeSaleRefundRecordId") Long storeSaleRefundRecordId) {
        return success(storeSaleRefundRecordService.selectStoreSaleRefundRecordByStoreSaleRefundRecordId(storeSaleRefundRecordId));
    }

    /**
     * 新增档口销售返单
     */
    @PreAuthorize("@ss.hasPermi('system:record:add')")
    @Log(title = "档口销售返单", businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody StoreSaleRefundRecord storeSaleRefundRecord) {
        return success(storeSaleRefundRecordService.insertStoreSaleRefundRecord(storeSaleRefundRecord));
    }

    /**
     * 修改档口销售返单
     */
    @PreAuthorize("@ss.hasPermi('system:record:edit')")
    @Log(title = "档口销售返单", businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody StoreSaleRefundRecord storeSaleRefundRecord) {
        return success(storeSaleRefundRecordService.updateStoreSaleRefundRecord(storeSaleRefundRecord));
    }

    /**
     * 删除档口销售返单
     */
    @PreAuthorize("@ss.hasPermi('system:record:remove')")
    @Log(title = "档口销售返单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{storeSaleRefundRecordIds}")
    public R remove(@PathVariable Long[] storeSaleRefundRecordIds) {
        return success(storeSaleRefundRecordService.deleteStoreSaleRefundRecordByStoreSaleRefundRecordIds(storeSaleRefundRecordIds));
    }
}
