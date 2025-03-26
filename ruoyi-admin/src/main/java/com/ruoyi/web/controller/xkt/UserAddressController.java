package com.ruoyi.web.controller.xkt;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.xkt.domain.UserAddress;
import com.ruoyi.xkt.service.IUserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 用户收货地址Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@RestController
@RequestMapping("/rest/v1/user-addrs")
public class UserAddressController extends BaseController {
    @Autowired
    private IUserAddressService userAddressService;

    /**
     * 查询用户收货地址列表
     */
    @PreAuthorize("@ss.hasPermi('system:address:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserAddress userAddress) {
        startPage();
        List<UserAddress> list = userAddressService.selectUserAddressList(userAddress);
        return getDataTable(list);
    }

    /**
     * 导出用户收货地址列表
     */
    @PreAuthorize("@ss.hasPermi('system:address:export')")
    @Log(title = "用户收货地址", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserAddress userAddress) {
        List<UserAddress> list = userAddressService.selectUserAddressList(userAddress);
        ExcelUtil<UserAddress> util = new ExcelUtil<UserAddress>(UserAddress.class);
        util.exportExcel(response, list, "用户收货地址数据");
    }

    /**
     * 获取用户收货地址详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:address:query')")
    @GetMapping(value = "/{userAddrId}")
    public AjaxResult getInfo(@PathVariable("userAddrId") Long userAddrId) {
        return success(userAddressService.selectUserAddressByUserAddrId(userAddrId));
    }

    /**
     * 新增用户收货地址
     */
    @PreAuthorize("@ss.hasPermi('system:address:add')")
    @Log(title = "用户收货地址", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UserAddress userAddress) {
        return toAjax(userAddressService.insertUserAddress(userAddress));
    }

    /**
     * 修改用户收货地址
     */
    @PreAuthorize("@ss.hasPermi('system:address:edit')")
    @Log(title = "用户收货地址", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UserAddress userAddress) {
        return toAjax(userAddressService.updateUserAddress(userAddress));
    }

    /**
     * 删除用户收货地址
     */
    @PreAuthorize("@ss.hasPermi('system:address:remove')")
    @Log(title = "用户收货地址", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userAddrIds}")
    public AjaxResult remove(@PathVariable Long[] userAddrIds) {
        return toAjax(userAddressService.deleteUserAddressByUserAddrIds(userAddrIds));
    }
}
