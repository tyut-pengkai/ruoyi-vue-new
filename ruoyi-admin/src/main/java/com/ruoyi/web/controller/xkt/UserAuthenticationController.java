package com.ruoyi.web.controller.xkt;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.xkt.domain.UserAuthentication;
import com.ruoyi.xkt.service.IUserAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 用户代发认证Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@RestController
@RequestMapping("/rest/v1/user-auths")
public class UserAuthenticationController extends XktBaseController {
    @Autowired
    private IUserAuthenticationService userAuthenticationService;

    /**
     * 查询用户代发认证列表
     */
    @PreAuthorize("@ss.hasPermi('system:authentication:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserAuthentication userAuthentication) {
        startPage();
        List<UserAuthentication> list = userAuthenticationService.selectUserAuthenticationList(userAuthentication);
        return getDataTable(list);
    }

    /**
     * 导出用户代发认证列表
     */
    @PreAuthorize("@ss.hasPermi('system:authentication:export')")
    @Log(title = "用户代发认证", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserAuthentication userAuthentication) {
        List<UserAuthentication> list = userAuthenticationService.selectUserAuthenticationList(userAuthentication);
        ExcelUtil<UserAuthentication> util = new ExcelUtil<UserAuthentication>(UserAuthentication.class);
        util.exportExcel(response, list, "用户代发认证数据");
    }

    /**
     * 获取用户代发认证详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:authentication:query')")
    @GetMapping(value = "/{userAuthId}")
    public R getInfo(@PathVariable("userAuthId") Long userAuthId) {
        return success(userAuthenticationService.selectUserAuthenticationByUserAuthId(userAuthId));
    }

    /**
     * 新增用户代发认证
     */
    @PreAuthorize("@ss.hasPermi('system:authentication:add')")
    @Log(title = "用户代发认证", businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody UserAuthentication userAuthentication) {
        return success(userAuthenticationService.insertUserAuthentication(userAuthentication));
    }

    /**
     * 修改用户代发认证
     */
    @PreAuthorize("@ss.hasPermi('system:authentication:edit')")
    @Log(title = "用户代发认证", businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody UserAuthentication userAuthentication) {
        return success(userAuthenticationService.updateUserAuthentication(userAuthentication));
    }

    /**
     * 删除用户代发认证
     */
    @PreAuthorize("@ss.hasPermi('system:authentication:remove')")
    @Log(title = "用户代发认证", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userAuthIds}")
    public R remove(@PathVariable Long[] userAuthIds) {
        return success(userAuthenticationService.deleteUserAuthenticationByUserAuthIds(userAuthIds));
    }
}
