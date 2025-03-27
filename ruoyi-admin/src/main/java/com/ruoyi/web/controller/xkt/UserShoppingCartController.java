package com.ruoyi.web.controller.xkt;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.xkt.domain.UserShoppingCart;
import com.ruoyi.xkt.service.IUserShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 用户进货车Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@RestController
@RequestMapping("/rest/v1/user-carts")
public class UserShoppingCartController extends XktBaseController {
    @Autowired
    private IUserShoppingCartService userShoppingCartService;

    /**
     * 查询用户进货车列表
     */
    @PreAuthorize("@ss.hasPermi('system:cart:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserShoppingCart userShoppingCart) {
        startPage();
        List<UserShoppingCart> list = userShoppingCartService.selectUserShoppingCartList(userShoppingCart);
        return getDataTable(list);
    }

    /**
     * 导出用户进货车列表
     */
    @PreAuthorize("@ss.hasPermi('system:cart:export')")
    @Log(title = "用户进货车", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserShoppingCart userShoppingCart) {
        List<UserShoppingCart> list = userShoppingCartService.selectUserShoppingCartList(userShoppingCart);
        ExcelUtil<UserShoppingCart> util = new ExcelUtil<UserShoppingCart>(UserShoppingCart.class);
        util.exportExcel(response, list, "用户进货车数据");
    }

    /**
     * 获取用户进货车详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:cart:query')")
    @GetMapping(value = "/{userShopCartId}")
    public R getInfo(@PathVariable("userShopCartId") Long userShopCartId) {
        return success(userShoppingCartService.selectUserShoppingCartByUserShopCartId(userShopCartId));
    }

    /**
     * 新增用户进货车
     */
    @PreAuthorize("@ss.hasPermi('system:cart:add')")
    @Log(title = "用户进货车", businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody UserShoppingCart userShoppingCart) {
        return success(userShoppingCartService.insertUserShoppingCart(userShoppingCart));
    }

    /**
     * 修改用户进货车
     */
    @PreAuthorize("@ss.hasPermi('system:cart:edit')")
    @Log(title = "用户进货车", businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody UserShoppingCart userShoppingCart) {
        return success(userShoppingCartService.updateUserShoppingCart(userShoppingCart));
    }

    /**
     * 删除用户进货车
     */
    @PreAuthorize("@ss.hasPermi('system:cart:remove')")
    @Log(title = "用户进货车", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userShopCartIds}")
    public R remove(@PathVariable Long[] userShopCartIds) {
        return success(userShoppingCartService.deleteUserShoppingCartByUserShopCartIds(userShopCartIds));
    }
}
