package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.framework.web.service.PermissionService;
import com.ruoyi.system.domain.SysWithdrawOrder;
import com.ruoyi.system.service.ISysWithdrawOrderService;
import com.ruoyi.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

/**
 * 提现记录Controller
 *
 * @author zwgu
 * @date 2024-06-03
 */
@RestController
@RequestMapping("/system/withdrawOrder")
public class SysWithdrawOrderController extends BaseController {
    @Autowired
    private ISysWithdrawOrderService sysWithdrawOrderService;
    @Resource
    private PermissionService permissionService;

    /**
     * 查询提现记录列表
     */
    @GetMapping("/self/list")
    public TableDataInfo listSelf(SysWithdrawOrder sysWithdrawOrder) {
        startPage();
        sysWithdrawOrder.setUserId(getUserId());
        List<SysWithdrawOrder> list = sysWithdrawOrderService.selectSysWithdrawOrderList(sysWithdrawOrder);
        return getDataTable(list);
    }

    /**
     * 获取提现记录详细信息
     */
    @GetMapping(value = "/self/{id}")
    public AjaxResult getInfoSelf(@PathVariable("id") Long id) {
        SysWithdrawOrder order = sysWithdrawOrderService.selectSysWithdrawOrderById(id);
        if (Objects.equals(order.getUserId(), getUserId())) {
            return AjaxResult.success(order);
        } else {
            throw new ServiceException("您没有查看该数据的权限");
        }
    }

    /**
     * 查询提现记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:withdrawOrder:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysWithdrawOrder sysWithdrawOrder) {
        startPage();
        if (!permissionService.hasAnyRoles("sadmin,admin")) {
            sysWithdrawOrder.setUserId(getUserId());
        }
        List<SysWithdrawOrder> list = sysWithdrawOrderService.selectSysWithdrawOrderList(sysWithdrawOrder);
        return getDataTable(list);
    }

    /**
     * 导出提现记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:withdrawOrder:export')")
    @Log(title = "提现记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysWithdrawOrder sysWithdrawOrder) {
        if (!permissionService.hasAnyRoles("sadmin,admin")) {
            sysWithdrawOrder.setUserId(getUserId());
        }
        List<SysWithdrawOrder> list = sysWithdrawOrderService.selectSysWithdrawOrderList(sysWithdrawOrder);
        ExcelUtil<SysWithdrawOrder> util = new ExcelUtil<SysWithdrawOrder>(SysWithdrawOrder.class);
        util.exportExcel(response, list, "提现记录数据");
    }

    /**
     * 获取提现记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:withdrawOrder:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(sysWithdrawOrderService.selectSysWithdrawOrderById(id));
    }

    /**
     * 新增提现记录
     */
    @PreAuthorize("@ss.hasPermi('system:withdrawOrder:add')")
    @Log(title = "提现记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysWithdrawOrder sysWithdrawOrder) {
        return toAjax(sysWithdrawOrderService.insertSysWithdrawOrder(sysWithdrawOrder));
    }

    /**
     * 修改提现记录
     */
    @PreAuthorize("@ss.hasPermi('system:withdrawOrder:edit')")
    @Log(title = "提现记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysWithdrawOrder sysWithdrawOrder) {
        return toAjax(sysWithdrawOrderService.updateSysWithdrawOrder(sysWithdrawOrder));
    }

    /**
     * 删除提现记录
     */
    @PreAuthorize("@ss.hasPermi('system:withdrawOrder:remove')")
    @Log(title = "提现记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(sysWithdrawOrderService.deleteSysWithdrawOrderByIds(ids));
    }

    /**
     * 删除提现记录
     */
    @PreAuthorize("@ss.hasPermi('system:withdrawOrder:cancel')")
    @Log(title = "提现记录", businessType = BusinessType.CANCEL)
    @DeleteMapping("/cancel/{ids}")
    public AjaxResult cancel(@PathVariable Long[] ids) {
        return toAjax(sysWithdrawOrderService.cancelSysWithdrawOrderByIds(ids));
    }

}
