package com.ruoyi.xkt.service;

import com.ruoyi.common.core.page.Page;
import com.ruoyi.xkt.dto.userShoppingCart.*;

/**
 * 用户进货车Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IShoppingCartService {

    /**
     * 用户往进货车新增商品
     *
     * @param shoppingCartDTO
     * @return Integer
     */
    Integer create(ShoppingCartDTO shoppingCartDTO);

    /**
     * 用户进货车列表
     *
     * @param pageDTO 查询列表入参
     * @return Page<ShopCartListResDTO>
     */
    Page<ShopCartPageResDTO> page(ShopCartPageDTO pageDTO);

    /**
     * 根据购物车ID 获取购物车详情
     *
     * @param shoppingCartId 购物车ID
     * @return ShopCartDetailResDTO
     */
    ShopCartDetailResDTO getEditInfo(Long shoppingCartId);

    /**
     * 用户编辑进货车商品
     *
     * @param cartDTO 编辑进货车商品入参
     * @return
     */
    Integer update(ShoppingCartEditDTO cartDTO);

    /**
     * 用户删除进货车商品
     *
     * @param shoppingCartId 进货车ID
     * @return
     */
    Integer delete(Long shoppingCartId);

    /**
     * 根据storeProdid获取进货车详情
     *
     * @param storeProdId 商品ID
     * @return ShoppingCartDTO
     */
    ShoppingCartDTO getByStoreProdId(Long storeProdId);

}
