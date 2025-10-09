package com.ruoyi.xkt.service;

import com.ruoyi.common.core.page.Page;
import com.ruoyi.xkt.dto.userShoppingCart.*;

import java.util.List;

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
     * @param deleteDTO 进货车ID删除列表
     * @return
     */
    Integer delete(ShopCartDeleteDTO deleteDTO);

    /**
     * 根据storeProdId获取进货车详情
     *
     * @param listDTO 档口商品ID列表
     * @return ShoppingCartDTO
     */
    List<ShoppingCartDTO> getList(ShopCartListDTO listDTO);

    /**
     * 获取用户进货车各状态数量
     *
     * @return ShopCartStatusCountResDTO
     */
    ShopCartStatusCountResDTO getStatusNum();

    /**
     * 用户更新进货车明细数量
     *
     * @param updateQuantityDTO 更新入参
     * @return Integer
     */
    Integer updateDetailQuantity(ShopCartDetailQuantityUpdateDTO updateQuantityDTO);

    /**
     * 下单后，删除用户进货车商品
     *
     * @param storeProdId 档口商品ID
     * @param userId      用户ID
     * @return Integer
     */
    Integer removeShoppingCart(Long storeProdId, Long userId);

}
