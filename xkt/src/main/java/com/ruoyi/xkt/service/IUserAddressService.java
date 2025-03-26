package com.ruoyi.xkt.service;

import com.ruoyi.xkt.domain.UserAddress;

import java.util.List;

/**
 * 用户收货地址Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IUserAddressService {
    /**
     * 查询用户收货地址
     *
     * @param userAddrId 用户收货地址主键
     * @return 用户收货地址
     */
    public UserAddress selectUserAddressByUserAddrId(Long userAddrId);

    /**
     * 查询用户收货地址列表
     *
     * @param userAddress 用户收货地址
     * @return 用户收货地址集合
     */
    public List<UserAddress> selectUserAddressList(UserAddress userAddress);

    /**
     * 新增用户收货地址
     *
     * @param userAddress 用户收货地址
     * @return 结果
     */
    public int insertUserAddress(UserAddress userAddress);

    /**
     * 修改用户收货地址
     *
     * @param userAddress 用户收货地址
     * @return 结果
     */
    public int updateUserAddress(UserAddress userAddress);

    /**
     * 批量删除用户收货地址
     *
     * @param userAddrIds 需要删除的用户收货地址主键集合
     * @return 结果
     */
    public int deleteUserAddressByUserAddrIds(Long[] userAddrIds);

    /**
     * 删除用户收货地址信息
     *
     * @param userAddrId 用户收货地址主键
     * @return 结果
     */
    public int deleteUserAddressByUserAddrId(Long userAddrId);
}
