package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.web.controller.xkt.vo.userShoppingCart.*;
import com.ruoyi.xkt.dto.userShoppingCart.*;
import com.ruoyi.xkt.service.IShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户进货车Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Api(tags = "用户进货车（只有电商卖家可操作）")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/v1/shopping-carts")
public class ShoppingCartController extends XktBaseController {

    final IShoppingCartService shopCartService;

    /**
     * 电商卖家添加商品到进货车
     */
//    // @PreAuthorize("@ss.hasPermi('system:cart:add')")
    @ApiOperation(value = "电商卖家添加商品到进货车", httpMethod = "POST", response = R.class)
    @Log(title = "电商卖家添加商品到进货车", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Integer> create(@Validated @RequestBody ShopCartVO shopCartVO) {
        return R.ok(shopCartService.create(BeanUtil.toBean(shopCartVO, ShoppingCartDTO.class)));
    }

    /**
     * 电商卖家编辑进货车商品
     */
//    // @PreAuthorize("@ss.hasPermi('system:store:edit')")
    @ApiOperation(value = "电商卖家编辑进货车商品", httpMethod = "PUT", response = R.class)
    @Log(title = "电商卖家编辑进货车商品", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Integer> edit(@Validated @RequestBody ShopCartEditVO editVO) {
        return R.ok(shopCartService.update(BeanUtil.toBean(editVO, ShoppingCartEditDTO.class)));
    }

    /**
     * 获取用户进货车列表
     */
//    // @PreAuthorize("@ss.hasPermi('system:cart:list')")
    @ApiOperation(value = "获取用户进货车列表", httpMethod = "POST", response = R.class)
    @PostMapping("/page")
    public R<Page<ShopCartPageResDTO>> page(@Validated @RequestBody ShopCartPageVO pageVO) {
        return R.ok(shopCartService.page(BeanUtil.toBean(pageVO, ShopCartPageDTO.class)));
    }

    /**
     * 用户进货车列表点击编辑获取数据
     */
//    // @PreAuthorize("@ss.hasPermi('system:cart:list')")
    @ApiOperation(value = "用户进货车列表点击编辑获取数据", httpMethod = "GET", response = R.class)
    @GetMapping("/edit/{shoppingCartId}")
    public R<ShopCartEditDetailResVO> getEditInfo(@PathVariable Long shoppingCartId) {
        return R.ok(BeanUtil.toBean(shopCartService.getEditInfo(shoppingCartId), ShopCartEditDetailResVO.class));
    }

    /**
     * 进货车下单时及商品下单时获取商品列表
     */
    @ApiOperation(value = "进货车下单时及商品下单时获取商品列表", httpMethod = "POST", response = R.class)
    @PostMapping("/list")
    public R<List<ShopCartResVO>> getList(@Validated @RequestBody ShopCartListVO listVO) {
        return R.ok(BeanUtil.copyToList(shopCartService.getList(BeanUtil.toBean(listVO, ShopCartListDTO.class)), ShopCartResVO.class));
    }


    /**
     * 用户删除进货车商品
     */
//    // @PreAuthorize("@ss.hasPermi('system:sale:remove')")
    @ApiOperation(value = "用户进货车列表点击编辑", httpMethod = "DELETE", response = R.class)
    @Log(title = "用户删除进货车商品", businessType = BusinessType.DELETE)
    @DeleteMapping("/{shoppingCartId}")
    public R<Integer> remove(@PathVariable Long shoppingCartId) {
        return R.ok(shopCartService.delete(shoppingCartId));
    }



}
