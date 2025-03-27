package com.ruoyi.web.controller.xkt;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.xkt.domain.StoreOrderReceive;
import com.ruoyi.xkt.service.IStoreOrderReceiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 档口代发订单收件人Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@RestController
@RequestMapping("/rest/v1/store-order-receives")
public class StoreOrderReceiveController extends XktBaseController {
    @Autowired
    private IStoreOrderReceiveService storeOrderReceiveService;

    /**
     * 查询档口代发订单收件人列表
     */
    @PreAuthorize("@ss.hasPermi('system:receive:list')")
    @GetMapping("/list")
    public TableDataInfo list(StoreOrderReceive storeOrderReceive) {
        startPage();
        List<StoreOrderReceive> list = storeOrderReceiveService.selectStoreOrderReceiveList(storeOrderReceive);
        return getDataTable(list);
    }

    /**
     * 导出档口代发订单收件人列表
     */
    @PreAuthorize("@ss.hasPermi('system:receive:export')")
    @Log(title = "档口代发订单收件人", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StoreOrderReceive storeOrderReceive) {
        List<StoreOrderReceive> list = storeOrderReceiveService.selectStoreOrderReceiveList(storeOrderReceive);
        ExcelUtil<StoreOrderReceive> util = new ExcelUtil<StoreOrderReceive>(StoreOrderReceive.class);
        util.exportExcel(response, list, "档口代发订单收件人数据");
    }

    /**
     * 获取档口代发订单收件人详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:receive:query')")
    @GetMapping(value = "/{storeOrderRcvId}")
    public R getInfo(@PathVariable("storeOrderRcvId") Long storeOrderRcvId) {
        return success(storeOrderReceiveService.selectStoreOrderReceiveByStoreOrderRcvId(storeOrderRcvId));
    }

    /**
     * 新增档口代发订单收件人
     */
    @PreAuthorize("@ss.hasPermi('system:receive:add')")
    @Log(title = "档口代发订单收件人", businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody StoreOrderReceive storeOrderReceive) {
        return success(storeOrderReceiveService.insertStoreOrderReceive(storeOrderReceive));
    }

    /**
     * 修改档口代发订单收件人
     */
    @PreAuthorize("@ss.hasPermi('system:receive:edit')")
    @Log(title = "档口代发订单收件人", businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody StoreOrderReceive storeOrderReceive) {
        return success(storeOrderReceiveService.updateStoreOrderReceive(storeOrderReceive));
    }

    /**
     * 删除档口代发订单收件人
     */
    @PreAuthorize("@ss.hasPermi('system:receive:remove')")
    @Log(title = "档口代发订单收件人", businessType = BusinessType.DELETE)
    @DeleteMapping("/{storeOrderRcvIds}")
    public R remove(@PathVariable Long[] storeOrderRcvIds) {
        return success(storeOrderReceiveService.deleteStoreOrderReceiveByStoreOrderRcvIds(storeOrderRcvIds));
    }
}
