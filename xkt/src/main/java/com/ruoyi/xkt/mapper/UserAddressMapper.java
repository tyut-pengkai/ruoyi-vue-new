package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.UserAddress;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户收货地址Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Repository
public interface UserAddressMapper extends BaseMapper<UserAddress> {
    /**
     * 查询用户收货地址
     *
     * @param id 用户收货地址主键
     * @return 用户收货地址
     */
    public UserAddress selectUserAddressByUserAddrId(Long id);

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
     * 删除用户收货地址
     *
     * @param id 用户收货地址主键
     * @return 结果
     */
    public int deleteUserAddressByUserAddrId(Long id);

    /**
     * 批量删除用户收货地址
     *
     * @param userAddrIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserAddressByUserAddrIds(Long[] userAddrIds);
}
