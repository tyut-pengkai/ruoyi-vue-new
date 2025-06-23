package com.ruoyi.xkt.service;

import com.ruoyi.xkt.dto.express.UserAddressInfoDTO;

import java.util.List;

/**
 * 用户收货地址Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IUserAddressService {
    /**
     * 新增
     *
     * @param userAddressInfoDTO
     * @return
     */
    UserAddressInfoDTO createUserAddress(UserAddressInfoDTO userAddressInfoDTO);

    /**
     * 复制
     *
     * @param id
     * @return
     */
    UserAddressInfoDTO copyUserAddress(Long id);

    /**
     * 获取详情
     *
     * @param id
     * @return
     */
    UserAddressInfoDTO getUserAddress(Long id);

    /**
     * 列表
     *
     * @param userId
     * @return
     */
    List<UserAddressInfoDTO> listByUser(Long userId);

    /**
     * 修改
     *
     * @param userAddressInfoDTO
     * @return
     */
    UserAddressInfoDTO modifyUserAddress(UserAddressInfoDTO userAddressInfoDTO);

    /**
     * 删除
     *
     * @param id
     */
    void deleteUserAddress(Long id);

    /**
     * 校验地址归属
     *
     * @param id
     * @param userId
     */
    void checkOwner(Long id, Long userId);
}
