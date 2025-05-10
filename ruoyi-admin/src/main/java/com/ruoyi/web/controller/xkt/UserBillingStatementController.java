package com.ruoyi.web.controller.xkt;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.xkt.domain.UserBillingStatement;
import com.ruoyi.xkt.service.IUserBillingStatementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 用户对账明细Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@RestController
@RequestMapping("/rest/v1/user-bill-stats")
public class UserBillingStatementController extends XktBaseController {
    @Autowired
    private IUserBillingStatementService userBillingStatementService;

    /**
     * 查询用户对账明细列表
     */
    // @PreAuthorize("@ss.hasPermi('system:statement:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserBillingStatement userBillingStatement) {
        startPage();
        List<UserBillingStatement> list = userBillingStatementService.selectUserBillingStatementList(userBillingStatement);
        return getDataTable(list);
    }

    /**
     * 导出用户对账明细列表
     */
    // @PreAuthorize("@ss.hasPermi('system:statement:export')")
    @Log(title = "用户对账明细", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserBillingStatement userBillingStatement) {
        List<UserBillingStatement> list = userBillingStatementService.selectUserBillingStatementList(userBillingStatement);
        ExcelUtil<UserBillingStatement> util = new ExcelUtil<UserBillingStatement>(UserBillingStatement.class);
        util.exportExcel(response, list, "用户对账明细数据");
    }

    /**
     * 获取用户对账明细详细信息
     */
    // @PreAuthorize("@ss.hasPermi('system:statement:query')")
    @GetMapping(value = "/{userBillStatId}")
    public R getInfo(@PathVariable("userBillStatId") Long userBillStatId) {
        return success(userBillingStatementService.selectUserBillingStatementByUserBillStatId(userBillStatId));
    }

    /**
     * 新增用户对账明细
     */
    // @PreAuthorize("@ss.hasPermi('system:statement:add')")
    @Log(title = "用户对账明细", businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody UserBillingStatement userBillingStatement) {
        return success(userBillingStatementService.insertUserBillingStatement(userBillingStatement));
    }

    /**
     * 修改用户对账明细
     */
    // @PreAuthorize("@ss.hasPermi('system:statement:edit')")
    @Log(title = "用户对账明细", businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody UserBillingStatement userBillingStatement) {
        return success(userBillingStatementService.updateUserBillingStatement(userBillingStatement));
    }

    /**
     * 删除用户对账明细
     */
    // @PreAuthorize("@ss.hasPermi('system:statement:remove')")
    @Log(title = "用户对账明细", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userBillStatIds}")
    public R remove(@PathVariable Long[] userBillStatIds) {
        return success(userBillingStatementService.deleteUserBillingStatementByUserBillStatIds(userBillStatIds));
    }
}
