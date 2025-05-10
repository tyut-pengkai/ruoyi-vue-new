package com.ruoyi.web.controller.xkt;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.xkt.domain.UserNoticeSetting;
import com.ruoyi.xkt.service.IUserNoticeSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 用户通知接收设置Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@RestController
@RequestMapping("/rest/v1/user-notice-settings")
public class UserNoticeSettingController extends XktBaseController {
    @Autowired
    private IUserNoticeSettingService userNoticeSettingService;

    /**
     * 查询用户通知接收设置列表
     */
    // @PreAuthorize("@ss.hasPermi('system:setting:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserNoticeSetting userNoticeSetting) {
        startPage();
        List<UserNoticeSetting> list = userNoticeSettingService.selectUserNoticeSettingList(userNoticeSetting);
        return getDataTable(list);
    }

    /**
     * 导出用户通知接收设置列表
     */
    // @PreAuthorize("@ss.hasPermi('system:setting:export')")
    @Log(title = "用户通知接收设置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserNoticeSetting userNoticeSetting) {
        List<UserNoticeSetting> list = userNoticeSettingService.selectUserNoticeSettingList(userNoticeSetting);
        ExcelUtil<UserNoticeSetting> util = new ExcelUtil<UserNoticeSetting>(UserNoticeSetting.class);
        util.exportExcel(response, list, "用户通知接收设置数据");
    }

    /**
     * 获取用户通知接收设置详细信息
     */
    // @PreAuthorize("@ss.hasPermi('system:setting:query')")
    @GetMapping(value = "/{userNoticeSetId}")
    public R getInfo(@PathVariable("userNoticeSetId") Long userNoticeSetId) {
        return success(userNoticeSettingService.selectUserNoticeSettingByUserNoticeSetId(userNoticeSetId));
    }

    /**
     * 新增用户通知接收设置
     */
    // @PreAuthorize("@ss.hasPermi('system:setting:add')")
    @Log(title = "用户通知接收设置", businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody UserNoticeSetting userNoticeSetting) {
        return success(userNoticeSettingService.insertUserNoticeSetting(userNoticeSetting));
    }

    /**
     * 修改用户通知接收设置
     */
    // @PreAuthorize("@ss.hasPermi('system:setting:edit')")
    @Log(title = "用户通知接收设置", businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody UserNoticeSetting userNoticeSetting) {
        return success(userNoticeSettingService.updateUserNoticeSetting(userNoticeSetting));
    }

    /**
     * 删除用户通知接收设置
     */
    // @PreAuthorize("@ss.hasPermi('system:setting:remove')")
    @Log(title = "用户通知接收设置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userNoticeSetIds}")
    public R remove(@PathVariable Long[] userNoticeSetIds) {
        return success(userNoticeSettingService.deleteUserNoticeSettingByUserNoticeSetIds(userNoticeSetIds));
    }
}
