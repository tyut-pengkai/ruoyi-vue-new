package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.SysWithdrawMethod;
import com.ruoyi.system.service.ISysWithdrawMethodService;
import com.ruoyi.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 收款方式Controller
 *
 * @author zwgu
 * @date 2024-06-03
 */
@RestController
@RequestMapping("/system/withdrawMethod")
public class SysWithdrawMethodController extends BaseController {
    @Autowired
    private ISysWithdrawMethodService sysWithdrawMethodService;

    /**
     * 查询收款方式列表
     */
    @PreAuthorize("@ss.hasPermi('system:withdrawMethod:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysWithdrawMethod sysWithdrawMethod) {
        startPage();
        sysWithdrawMethod.setUserId(getUserId());
        List<SysWithdrawMethod> list = sysWithdrawMethodService.selectSysWithdrawMethodList(sysWithdrawMethod);
        return getDataTable(list);
    }

    /**
     * 导出收款方式列表
     */
    @PreAuthorize("@ss.hasPermi('system:withdrawMethod:export')")
    @Log(title = "收款方式", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysWithdrawMethod sysWithdrawMethod) {
        sysWithdrawMethod.setUserId(getUserId());
        List<SysWithdrawMethod> list = sysWithdrawMethodService.selectSysWithdrawMethodList(sysWithdrawMethod);
        ExcelUtil<SysWithdrawMethod> util = new ExcelUtil<SysWithdrawMethod>(SysWithdrawMethod.class);
        util.exportExcel(response, list, "收款方式数据");
    }

    /**
     * 获取收款方式详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:withdrawMethod:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(sysWithdrawMethodService.selectSysWithdrawMethodById(id));
    }

    /**
     * 新增收款方式
     */
    @PreAuthorize("@ss.hasPermi('system:withdrawMethod:add')")
    @Log(title = "收款方式", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysWithdrawMethod sysWithdrawMethod) {
        sysWithdrawMethod.setUserId(getUserId());
        return toAjax(sysWithdrawMethodService.insertSysWithdrawMethod(sysWithdrawMethod));
    }

    /**
     * 修改收款方式
     */
    @PreAuthorize("@ss.hasPermi('system:withdrawMethod:edit')")
    @Log(title = "收款方式", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysWithdrawMethod sysWithdrawMethod) {
        sysWithdrawMethod.setUserId(getUserId());
        return toAjax(sysWithdrawMethodService.updateSysWithdrawMethod(sysWithdrawMethod));
    }

    /**
     * 删除收款方式
     */
    @PreAuthorize("@ss.hasPermi('system:withdrawMethod:remove')")
    @Log(title = "收款方式", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(sysWithdrawMethodService.deleteSysWithdrawMethodByIds(ids));
    }
}
