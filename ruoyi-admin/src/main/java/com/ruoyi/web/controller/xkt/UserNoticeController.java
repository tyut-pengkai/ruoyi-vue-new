package com.ruoyi.web.controller.xkt;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.xkt.domain.UserNotice;
import com.ruoyi.xkt.service.IUserNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 用户所有通知Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@RestController
@RequestMapping("/rest/v1/user-notices")
public class UserNoticeController extends XktBaseController {
    @Autowired
    private IUserNoticeService userNoticeService;

    /**
     * 查询用户所有通知列表
     */
    @PreAuthorize("@ss.hasPermi('system:notice:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserNotice userNotice) {
        startPage();
        List<UserNotice> list = userNoticeService.selectUserNoticeList(userNotice);
        return getDataTable(list);
    }

    /**
     * 导出用户所有通知列表
     */
    @PreAuthorize("@ss.hasPermi('system:notice:export')")
    @Log(title = "用户所有通知", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserNotice userNotice) {
        List<UserNotice> list = userNoticeService.selectUserNoticeList(userNotice);
        ExcelUtil<UserNotice> util = new ExcelUtil<UserNotice>(UserNotice.class);
        util.exportExcel(response, list, "用户所有通知数据");
    }

    /**
     * 获取用户所有通知详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:notice:query')")
    @GetMapping(value = "/{userNoticeId}")
    public R getInfo(@PathVariable("userNoticeId") Long userNoticeId) {
        return success(userNoticeService.selectUserNoticeByUserNoticeId(userNoticeId));
    }

    /**
     * 新增用户所有通知
     */
    @PreAuthorize("@ss.hasPermi('system:notice:add')")
    @Log(title = "用户所有通知", businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody UserNotice userNotice) {
        return success(userNoticeService.insertUserNotice(userNotice));
    }

    /**
     * 修改用户所有通知
     */
    @PreAuthorize("@ss.hasPermi('system:notice:edit')")
    @Log(title = "用户所有通知", businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody UserNotice userNotice) {
        return success(userNoticeService.updateUserNotice(userNotice));
    }

    /**
     * 删除用户所有通知
     */
    @PreAuthorize("@ss.hasPermi('system:notice:remove')")
    @Log(title = "用户所有通知", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userNoticeIds}")
    public R remove(@PathVariable Long[] userNoticeIds) {
        return success(userNoticeService.deleteUserNoticeByUserNoticeIds(userNoticeIds));
    }
}
