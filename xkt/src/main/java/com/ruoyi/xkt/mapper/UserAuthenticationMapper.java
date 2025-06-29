package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.UserAuthentication;
import com.ruoyi.xkt.dto.userAuthentication.UserAuthPageDTO;
import com.ruoyi.xkt.dto.userAuthentication.UserAuthPageResDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户代发认证Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Repository
public interface UserAuthenticationMapper extends BaseMapper<UserAuthentication> {

    /**
     * 筛选代发分页
     *
     * @param pageDTO 分页入参
     * @return List<UserAuthPageResDTO>
     */
    List<UserAuthPageResDTO> selectUserAuthPage(UserAuthPageDTO pageDTO);
}
