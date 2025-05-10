package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.web.controller.xkt.vo.userFavorite.UserFavBatchAddToShopCartVO;
import com.ruoyi.web.controller.xkt.vo.userFavorite.UserFavBatchDeleteVO;
import com.ruoyi.web.controller.xkt.vo.userFavorite.UserFavoritePageVO;
import com.ruoyi.web.controller.xkt.vo.userFavorite.UserFavoriteVO;
import com.ruoyi.xkt.dto.userFavorite.*;
import com.ruoyi.xkt.service.IUserFavoritesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户收藏商品Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Api(tags = "用户收藏（只有电商卖家可操作）")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/v1/user-favorites")
public class UserFavoritesController extends XktBaseController {

    final IUserFavoritesService userFavService;

    /**
     * 新增用户收藏商品
     */
    // @PreAuthorize("@ss.hasPermi('system:favorites:add')")
    @ApiOperation(value = "用户收藏商品", httpMethod = "POST", response = R.class)
    @Log(title = "用户收藏商品", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Integer> create(@Validated @RequestBody UserFavoriteVO favoriteVO) {
        return success(userFavService.create(BeanUtil.toBean(favoriteVO, UserFavoriteDTO.class)));
    }

    /**
     * 获取用户收藏列表
     */
    // @PreAuthorize("@ss.hasPermi('system:favorites:list')")
    @ApiOperation(value = "获取用户收藏列表", httpMethod = "POST", response = R.class)
    @PostMapping("/page")
    public R<Page<UserFavoritePageResDTO>> page(@Validated @RequestBody UserFavoritePageVO pageVO) {
        return R.ok(userFavService.page(BeanUtil.toBean(pageVO, UserFavoritePageDTO.class)));
    }


    /**
     * 批量加入进货车
     */
    // @PreAuthorize("@ss.hasPermi('system:favorites:add')")
    @ApiOperation(value = "批量加入进货车", httpMethod = "POST", response = R.class)
    @Log(title = "批量加入进货车", businessType = BusinessType.INSERT)
    @PostMapping("/batch/shopping-cart")
    public R<Integer> batchAddToShoppingCart(@Validated @RequestBody UserFavBatchAddToShopCartVO batchVO) {
        return success(userFavService.batchAddToShoppingCart(BeanUtil.toBean(batchVO, UserFavBatchAddToShopCartDTO.class)));
    }


    /**
     * 批量取消收藏
     */
    // @PreAuthorize("@ss.hasPermi('system:favorites:add')")
    @ApiOperation(value = "批量取消收藏", httpMethod = "DELETE", response = R.class)
    @Log(title = "批量取消收藏", businessType = BusinessType.INSERT)
    @DeleteMapping("/batch")
    public R<Integer> batchDelete(@Validated @RequestBody UserFavBatchDeleteVO batchDeleteVO) {
        return success(userFavService.batchDelete(BeanUtil.toBean(batchDeleteVO, UserFavBatchDeleteDTO.class)));
    }


}
