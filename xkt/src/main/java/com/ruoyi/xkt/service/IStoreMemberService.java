package com.ruoyi.xkt.service;

import com.ruoyi.common.core.page.Page;
import com.ruoyi.xkt.dto.storeMember.StoreMemberCreateDTO;
import com.ruoyi.xkt.dto.storeMember.StoreMemberExpireResDTO;
import com.ruoyi.xkt.dto.storeMember.StoreMemberPageDTO;
import com.ruoyi.xkt.dto.storeMember.StoreMemberPageResDTO;

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
     * @param createDTO 新增入参
     * @return Integer
     */
    Integer create(StoreMemberCreateDTO createDTO);

    /**
     * 档口会员列表
     *
     * @param pageDTO 档口会员列表入参
     * @return Page<StoreMemberPageResDTO>
     */
    Page<StoreMemberPageResDTO> page(StoreMemberPageDTO pageDTO);

    /**
     * 获取档口会员过期时间
     *
     * @param storeId 档口ID
     * @return StoreMemberExpireResDTO
     */
    StoreMemberExpireResDTO expire(Long storeId);
}
