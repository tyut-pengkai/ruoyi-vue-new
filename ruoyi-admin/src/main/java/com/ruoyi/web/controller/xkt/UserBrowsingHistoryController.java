package com.ruoyi.web.controller.xkt;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.xkt.domain.UserBrowsingHistory;
import com.ruoyi.xkt.service.IUserBrowsingHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 用户浏览历史Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@RestController
@RequestMapping("/rest/v1/user-brow-hises")
public class UserBrowsingHistoryController extends BaseController {
    @Autowired
    private IUserBrowsingHistoryService userBrowsingHistoryService;

    /**
     * 查询用户浏览历史列表
     */
    @PreAuthorize("@ss.hasPermi('system:history:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserBrowsingHistory userBrowsingHistory) {
        startPage();
        List<UserBrowsingHistory> list = userBrowsingHistoryService.selectUserBrowsingHistoryList(userBrowsingHistory);
        return getDataTable(list);
    }

    /**
     * 导出用户浏览历史列表
     */
    @PreAuthorize("@ss.hasPermi('system:history:export')")
    @Log(title = "用户浏览历史", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserBrowsingHistory userBrowsingHistory) {
        List<UserBrowsingHistory> list = userBrowsingHistoryService.selectUserBrowsingHistoryList(userBrowsingHistory);
        ExcelUtil<UserBrowsingHistory> util = new ExcelUtil<UserBrowsingHistory>(UserBrowsingHistory.class);
        util.exportExcel(response, list, "用户浏览历史数据");
    }

    /**
     * 获取用户浏览历史详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:history:query')")
    @GetMapping(value = "/{userBrowHisId}")
    public AjaxResult getInfo(@PathVariable("userBrowHisId") Long userBrowHisId) {
        return success(userBrowsingHistoryService.selectUserBrowsingHistoryByUserBrowHisId(userBrowHisId));
    }

    /**
     * 新增用户浏览历史
     */
    @PreAuthorize("@ss.hasPermi('system:history:add')")
    @Log(title = "用户浏览历史", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UserBrowsingHistory userBrowsingHistory) {
        return toAjax(userBrowsingHistoryService.insertUserBrowsingHistory(userBrowsingHistory));
    }

    /**
     * 修改用户浏览历史
     */
    @PreAuthorize("@ss.hasPermi('system:history:edit')")
    @Log(title = "用户浏览历史", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UserBrowsingHistory userBrowsingHistory) {
        return toAjax(userBrowsingHistoryService.updateUserBrowsingHistory(userBrowsingHistory));
    }

    /**
     * 删除用户浏览历史
     */
    @PreAuthorize("@ss.hasPermi('system:history:remove')")
    @Log(title = "用户浏览历史", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userBrowHisIds}")
    public AjaxResult remove(@PathVariable Long[] userBrowHisIds) {
        return toAjax(userBrowsingHistoryService.deleteUserBrowsingHistoryByUserBrowHisIds(userBrowHisIds));
    }
}
