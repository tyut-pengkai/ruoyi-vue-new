package com.ruoyi.xkt.service;

/**
 * 推广营销Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IStoreMemberService {

    /**
     * 档口购买会员
     *
     * @param storeId 档口ID
     * @return Integer
     */
    Integer create(Long storeId);
}
