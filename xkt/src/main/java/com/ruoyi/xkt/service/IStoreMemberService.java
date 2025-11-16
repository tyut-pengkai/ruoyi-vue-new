package com.ruoyi.xkt.service;

import com.ruoyi.common.core.page.Page;
import com.ruoyi.xkt.dto.storeMember.StoreMemberAuditDTO;
import com.ruoyi.xkt.dto.storeMember.StoreMemberCreateDTO;
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
     * 购买档口会员 免费版
     *
     * @param storeId 档口ID
     * @return Integer
     */
    Integer createNoMoney(Long storeId);

    /**
     * 审核档口会员
     *
     * @param auditDTO 档口会员审核入参
     * @return Integer
     */
    Integer audit(StoreMemberAuditDTO auditDTO);
}
