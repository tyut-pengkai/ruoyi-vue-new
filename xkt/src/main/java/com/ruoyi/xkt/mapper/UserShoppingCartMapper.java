package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.UserShoppingCart;

import java.util.List;

/**
 * 用户进货车Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface UserShoppingCartMapper extends BaseMapper<UserShoppingCart> {
    /**
     * 查询用户进货车
     *
     * @param userShopCartId 用户进货车主键
     * @return 用户进货车
     */
    public UserShoppingCart selectUserShoppingCartByUserShopCartId(Long userShopCartId);

    /**
     * 查询用户进货车列表
     *
     * @param userShoppingCart 用户进货车
     * @return 用户进货车集合
     */
    public List<UserShoppingCart> selectUserShoppingCartList(UserShoppingCart userShoppingCart);

    /**
     * 新增用户进货车
     *
     * @param userShoppingCart 用户进货车
     * @return 结果
     */
    public int insertUserShoppingCart(UserShoppingCart userShoppingCart);

    /**
     * 修改用户进货车
     *
     * @param userShoppingCart 用户进货车
     * @return 结果
     */
    public int updateUserShoppingCart(UserShoppingCart userShoppingCart);

    /**
     * 删除用户进货车
     *
     * @param userShopCartId 用户进货车主键
     * @return 结果
     */
    public int deleteUserShoppingCartByUserShopCartId(Long userShopCartId);

    /**
     * 批量删除用户进货车
     *
     * @param userShopCartIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserShoppingCartByUserShopCartIds(Long[] userShopCartIds);
}
