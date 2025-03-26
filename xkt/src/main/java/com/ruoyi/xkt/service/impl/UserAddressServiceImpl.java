package com.ruoyi.xkt.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.xkt.domain.UserAddress;
import com.ruoyi.xkt.mapper.UserAddressMapper;
import com.ruoyi.xkt.service.IUserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户收货地址Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
public class UserAddressServiceImpl implements IUserAddressService {
    @Autowired
    private UserAddressMapper userAddressMapper;

    /**
     * 查询用户收货地址
     *
     * @param userAddrId 用户收货地址主键
     * @return 用户收货地址
     */
    @Override
    public UserAddress selectUserAddressByUserAddrId(Long userAddrId) {
        return userAddressMapper.selectUserAddressByUserAddrId(userAddrId);
    }

    /**
     * 查询用户收货地址列表
     *
     * @param userAddress 用户收货地址
     * @return 用户收货地址
     */
    @Override
    public List<UserAddress> selectUserAddressList(UserAddress userAddress) {
        return userAddressMapper.selectUserAddressList(userAddress);
    }

    /**
     * 新增用户收货地址
     *
     * @param userAddress 用户收货地址
     * @return 结果
     */
    @Override
    public int insertUserAddress(UserAddress userAddress) {
        userAddress.setCreateTime(DateUtils.getNowDate());
        return userAddressMapper.insertUserAddress(userAddress);
    }

    /**
     * 修改用户收货地址
     *
     * @param userAddress 用户收货地址
     * @return 结果
     */
    @Override
    public int updateUserAddress(UserAddress userAddress) {
        userAddress.setUpdateTime(DateUtils.getNowDate());
        return userAddressMapper.updateUserAddress(userAddress);
    }

    /**
     * 批量删除用户收货地址
     *
     * @param userAddrIds 需要删除的用户收货地址主键
     * @return 结果
     */
    @Override
    public int deleteUserAddressByUserAddrIds(Long[] userAddrIds) {
        return userAddressMapper.deleteUserAddressByUserAddrIds(userAddrIds);
    }

    /**
     * 删除用户收货地址信息
     *
     * @param userAddrId 用户收货地址主键
     * @return 结果
     */
    @Override
    public int deleteUserAddressByUserAddrId(Long userAddrId) {
        return userAddressMapper.deleteUserAddressByUserAddrId(userAddrId);
    }
}
