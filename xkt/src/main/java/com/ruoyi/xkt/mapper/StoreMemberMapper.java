package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.StoreMember;
import com.ruoyi.xkt.dto.storeMember.StoreMemberPageDTO;
import com.ruoyi.xkt.dto.storeMember.StoreMemberPageResDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 推广营销Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Repository
public interface StoreMemberMapper extends BaseMapper<StoreMember> {

    /**
     * 档口会员列表
     *
     * @param pageDTO 列表入参
     * @return List<StoreMemberPageResDTO>
     */
    List<StoreMemberPageResDTO> selectStoreMemberPage(StoreMemberPageDTO pageDTO);

}
