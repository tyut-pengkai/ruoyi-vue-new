package com.ruoyi.web.controller.xkt;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.xkt.domain.StoreRoleAccount;
import com.ruoyi.xkt.service.IStoreRoleAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 档口子角色账号Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@RestController
@RequestMapping("/rest/v1/store-role-accs")
public class StoreRoleAccountController extends XktBaseController {
    @Autowired
    private IStoreRoleAccountService storeRoleAccountService;

    /**
     * 查询档口子角色账号列表
     */
    @PreAuthorize("@ss.hasPermi('system:account:list')")
    @GetMapping("/list")
    public TableDataInfo list(StoreRoleAccount storeRoleAccount) {
        startPage();
        List<StoreRoleAccount> list = storeRoleAccountService.selectStoreRoleAccountList(storeRoleAccount);
        return getDataTable(list);
    }

    /**
     * 导出档口子角色账号列表
     */
    @PreAuthorize("@ss.hasPermi('system:account:export')")
    @Log(title = "档口子角色账号", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StoreRoleAccount storeRoleAccount) {
        List<StoreRoleAccount> list = storeRoleAccountService.selectStoreRoleAccountList(storeRoleAccount);
        ExcelUtil<StoreRoleAccount> util = new ExcelUtil<StoreRoleAccount>(StoreRoleAccount.class);
        util.exportExcel(response, list, "档口子角色账号数据");
    }

    /**
     * 获取档口子角色账号详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:account:query')")
    @GetMapping(value = "/{storeRoleAccId}")
    public R getInfo(@PathVariable("storeRoleAccId") Long storeRoleAccId) {
        return success(storeRoleAccountService.selectStoreRoleAccountByStoreRoleAccId(storeRoleAccId));
    }

    /**
     * 新增档口子角色账号
     */
    @PreAuthorize("@ss.hasPermi('system:account:add')")
    @Log(title = "档口子角色账号", businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody StoreRoleAccount storeRoleAccount) {
        return success(storeRoleAccountService.insertStoreRoleAccount(storeRoleAccount));
    }

    /**
     * 修改档口子角色账号
     */
    @PreAuthorize("@ss.hasPermi('system:account:edit')")
    @Log(title = "档口子角色账号", businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody StoreRoleAccount storeRoleAccount) {
        return success(storeRoleAccountService.updateStoreRoleAccount(storeRoleAccount));
    }

    /**
     * 删除档口子角色账号
     */
    @PreAuthorize("@ss.hasPermi('system:account:remove')")
    @Log(title = "档口子角色账号", businessType = BusinessType.DELETE)
    @DeleteMapping("/{storeRoleAccIds}")
    public R remove(@PathVariable Long[] storeRoleAccIds) {
        return success(storeRoleAccountService.deleteStoreRoleAccountByStoreRoleAccIds(storeRoleAccIds));
    }
}
