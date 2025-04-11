package com.ruoyi.xkt.service;

import com.ruoyi.common.core.page.Page;
import com.ruoyi.xkt.dto.userFavorite.*;

/**
 * 用户收藏商品Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IUserFavoritesService {
    /**
     * 新增用户收藏商品
     *
     * @param favoriteDTO 新增收藏入参
     * @return Integer
     */
    Integer create(UserFavoriteDTO favoriteDTO);

    /**
     * 获取用户收藏列表
     *
     * @param pageDTO 查询入参
     * @return Page<UserFavoritePageResDTO>
     */
    Page<UserFavoritePageResDTO> page(UserFavoritePageDTO pageDTO);

    /**
     * 批量加入进货车
     *
     * @param batchDTO 批量操作入参
     * @return Integer
     */
    Integer batchAddToShoppingCart(UserFavBatchAddToShopCartDTO batchDTO);

    /**
     * 批量取消收藏
     *
     * @param batchDTO 批量操作入参
     * @return Integer
     */
    Integer batchDelete(UserFavBatchDeleteDTO batchDTO);

}
