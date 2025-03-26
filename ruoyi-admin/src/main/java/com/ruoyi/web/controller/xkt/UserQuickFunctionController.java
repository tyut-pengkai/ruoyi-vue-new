package com.ruoyi.web.controller.xkt;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.xkt.domain.UserQuickFunction;
import com.ruoyi.xkt.service.IUserQuickFunctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 用户快捷功能Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@RestController
@RequestMapping("/rest/v1/user-quick-funcs")
public class UserQuickFunctionController extends BaseController {
    @Autowired
    private IUserQuickFunctionService userQuickFunctionService;

    /**
     * 查询用户快捷功能列表
     */
    @PreAuthorize("@ss.hasPermi('system:function:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserQuickFunction userQuickFunction) {
        startPage();
        List<UserQuickFunction> list = userQuickFunctionService.selectUserQuickFunctionList(userQuickFunction);
        return getDataTable(list);
    }

    /**
     * 导出用户快捷功能列表
     */
    @PreAuthorize("@ss.hasPermi('system:function:export')")
    @Log(title = "用户快捷功能", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserQuickFunction userQuickFunction) {
        List<UserQuickFunction> list = userQuickFunctionService.selectUserQuickFunctionList(userQuickFunction);
        ExcelUtil<UserQuickFunction> util = new ExcelUtil<UserQuickFunction>(UserQuickFunction.class);
        util.exportExcel(response, list, "用户快捷功能数据");
    }

    /**
     * 获取用户快捷功能详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:function:query')")
    @GetMapping(value = "/{userQuickFuncId}")
    public AjaxResult getInfo(@PathVariable("userQuickFuncId") Long userQuickFuncId) {
        return success(userQuickFunctionService.selectUserQuickFunctionByUserQuickFuncId(userQuickFuncId));
    }

    /**
     * 新增用户快捷功能
     */
    @PreAuthorize("@ss.hasPermi('system:function:add')")
    @Log(title = "用户快捷功能", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UserQuickFunction userQuickFunction) {
        return toAjax(userQuickFunctionService.insertUserQuickFunction(userQuickFunction));
    }

    /**
     * 修改用户快捷功能
     */
    @PreAuthorize("@ss.hasPermi('system:function:edit')")
    @Log(title = "用户快捷功能", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UserQuickFunction userQuickFunction) {
        return toAjax(userQuickFunctionService.updateUserQuickFunction(userQuickFunction));
    }

    /**
     * 删除用户快捷功能
     */
    @PreAuthorize("@ss.hasPermi('system:function:remove')")
    @Log(title = "用户快捷功能", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userQuickFuncIds}")
    public AjaxResult remove(@PathVariable Long[] userQuickFuncIds) {
        return toAjax(userQuickFunctionService.deleteUserQuickFunctionByUserQuickFuncIds(userQuickFuncIds));
    }
}
