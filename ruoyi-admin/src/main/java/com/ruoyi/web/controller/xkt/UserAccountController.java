package com.ruoyi.web.controller.xkt;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.xkt.domain.UserAccount;
import com.ruoyi.xkt.service.IUserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 用户账户（支付宝、微信等）Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@RestController
@RequestMapping("/rest/v1/user-accs")
public class UserAccountController extends BaseController {
    @Autowired
    private IUserAccountService userAccountService;

    /**
     * 查询用户账户（支付宝、微信等）列表
     */
    @PreAuthorize("@ss.hasPermi('system:account:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserAccount userAccount) {
        startPage();
        List<UserAccount> list = userAccountService.selectUserAccountList(userAccount);
        return getDataTable(list);
    }

    /**
     * 导出用户账户（支付宝、微信等）列表
     */
    @PreAuthorize("@ss.hasPermi('system:account:export')")
    @Log(title = "用户账户（支付宝、微信等）", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserAccount userAccount) {
        List<UserAccount> list = userAccountService.selectUserAccountList(userAccount);
        ExcelUtil<UserAccount> util = new ExcelUtil<UserAccount>(UserAccount.class);
        util.exportExcel(response, list, "用户账户（支付宝、微信等）数据");
    }

    /**
     * 获取用户账户（支付宝、微信等）详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:account:query')")
    @GetMapping(value = "/{userAccId}")
    public AjaxResult getInfo(@PathVariable("userAccId") Long userAccId) {
        return success(userAccountService.selectUserAccountByUserAccId(userAccId));
    }

    /**
     * 新增用户账户（支付宝、微信等）
     */
    @PreAuthorize("@ss.hasPermi('system:account:add')")
    @Log(title = "用户账户（支付宝、微信等）", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UserAccount userAccount) {
        return toAjax(userAccountService.insertUserAccount(userAccount));
    }

    /**
     * 修改用户账户（支付宝、微信等）
     */
    @PreAuthorize("@ss.hasPermi('system:account:edit')")
    @Log(title = "用户账户（支付宝、微信等）", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UserAccount userAccount) {
        return toAjax(userAccountService.updateUserAccount(userAccount));
    }

    /**
     * 删除用户账户（支付宝、微信等）
     */
    @PreAuthorize("@ss.hasPermi('system:account:remove')")
    @Log(title = "用户账户（支付宝、微信等）", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userAccIds}")
    public AjaxResult remove(@PathVariable Long[] userAccIds) {
        return toAjax(userAccountService.deleteUserAccountByUserAccIds(userAccIds));
    }
}
