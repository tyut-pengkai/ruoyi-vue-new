package com.ruoyi.xkt.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.xkt.domain.UserShoppingCart;
import com.ruoyi.xkt.mapper.UserShoppingCartMapper;
import com.ruoyi.xkt.service.IUserShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户进货车Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
public class UserShoppingCartServiceImpl implements IUserShoppingCartService {
    @Autowired
    private UserShoppingCartMapper userShoppingCartMapper;

    /**
     * 查询用户进货车
     *
     * @param userShopCartId 用户进货车主键
     * @return 用户进货车
     */
    @Override
    public UserShoppingCart selectUserShoppingCartByUserShopCartId(Long userShopCartId) {
        return userShoppingCartMapper.selectUserShoppingCartByUserShopCartId(userShopCartId);
    }

    /**
     * 查询用户进货车列表
     *
     * @param userShoppingCart 用户进货车
     * @return 用户进货车
     */
    @Override
    public List<UserShoppingCart> selectUserShoppingCartList(UserShoppingCart userShoppingCart) {
        return userShoppingCartMapper.selectUserShoppingCartList(userShoppingCart);
    }

    /**
     * 新增用户进货车
     *
     * @param userShoppingCart 用户进货车
     * @return 结果
     */
    @Override
    @Transactional
    public int insertUserShoppingCart(UserShoppingCart userShoppingCart) {
        userShoppingCart.setCreateTime(DateUtils.getNowDate());
        return userShoppingCartMapper.insertUserShoppingCart(userShoppingCart);
    }

    /**
     * 修改用户进货车
     *
     * @param userShoppingCart 用户进货车
     * @return 结果
     */
    @Override
    @Transactional
    public int updateUserShoppingCart(UserShoppingCart userShoppingCart) {
        userShoppingCart.setUpdateTime(DateUtils.getNowDate());
        return userShoppingCartMapper.updateUserShoppingCart(userShoppingCart);
    }

    /**
     * 批量删除用户进货车
     *
     * @param userShopCartIds 需要删除的用户进货车主键
     * @return 结果
     */
    @Override
    public int deleteUserShoppingCartByUserShopCartIds(Long[] userShopCartIds) {
        return userShoppingCartMapper.deleteUserShoppingCartByUserShopCartIds(userShopCartIds);
    }

    /**
     * 删除用户进货车信息
     *
     * @param userShopCartId 用户进货车主键
     * @return 结果
     */
    @Override
    public int deleteUserShoppingCartByUserShopCartId(Long userShopCartId) {
        return userShoppingCartMapper.deleteUserShoppingCartByUserShopCartId(userShopCartId);
    }
}
