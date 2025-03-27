package com.ruoyi.web.controller.xkt;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.xkt.domain.UserSubscriptions;
import com.ruoyi.xkt.service.IUserSubscriptionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 用户关注u档口Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@RestController
@RequestMapping("/rest/v1/user-subs")
public class UserSubscriptionsController extends XktBaseController {
    @Autowired
    private IUserSubscriptionsService userSubscriptionsService;

    /**
     * 查询用户关注u档口列表
     */
    @PreAuthorize("@ss.hasPermi('system:subscriptions:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserSubscriptions userSubscriptions) {
        startPage();
        List<UserSubscriptions> list = userSubscriptionsService.selectUserSubscriptionsList(userSubscriptions);
        return getDataTable(list);
    }

    /**
     * 导出用户关注u档口列表
     */
    @PreAuthorize("@ss.hasPermi('system:subscriptions:export')")
    @Log(title = "用户关注u档口", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserSubscriptions userSubscriptions) {
        List<UserSubscriptions> list = userSubscriptionsService.selectUserSubscriptionsList(userSubscriptions);
        ExcelUtil<UserSubscriptions> util = new ExcelUtil<UserSubscriptions>(UserSubscriptions.class);
        util.exportExcel(response, list, "用户关注u档口数据");
    }

    /**
     * 获取用户关注u档口详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:subscriptions:query')")
    @GetMapping(value = "/{userSubsId}")
    public R getInfo(@PathVariable("userSubsId") Long userSubsId) {
        return success(userSubscriptionsService.selectUserSubscriptionsByUserSubsId(userSubsId));
    }

    /**
     * 新增用户关注u档口
     */
    @PreAuthorize("@ss.hasPermi('system:subscriptions:add')")
    @Log(title = "用户关注u档口", businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody UserSubscriptions userSubscriptions) {
        return success(userSubscriptionsService.insertUserSubscriptions(userSubscriptions));
    }

    /**
     * 修改用户关注u档口
     */
    @PreAuthorize("@ss.hasPermi('system:subscriptions:edit')")
    @Log(title = "用户关注u档口", businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody UserSubscriptions userSubscriptions) {
        return success(userSubscriptionsService.updateUserSubscriptions(userSubscriptions));
    }

    /**
     * 删除用户关注u档口
     */
    @PreAuthorize("@ss.hasPermi('system:subscriptions:remove')")
    @Log(title = "用户关注u档口", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userSubsIds}")
    public R remove(@PathVariable Long[] userSubsIds) {
        return success(userSubscriptionsService.deleteUserSubscriptionsByUserSubsIds(userSubsIds));
    }
}
