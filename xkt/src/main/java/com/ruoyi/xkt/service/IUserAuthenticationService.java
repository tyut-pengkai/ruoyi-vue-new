package com.ruoyi.xkt.service;

import com.ruoyi.common.core.page.Page;
import com.ruoyi.xkt.dto.userAuthentication.*;

/**
 * 用户代发认证Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IUserAuthenticationService {

    /**
     * 新增代发
     *
     * @param createDTO 新增代发入参
     * @return Integer
     */
    Integer create(UserAuthCreateDTO createDTO);

    /**
     * 代发分页
     *
     * @param pageDTO 分页查询入参
     * @return Page<UserAuthPageResDTO>
     */
    Page<UserAuthPageResDTO> page(UserAuthPageDTO pageDTO);

    /**
     * 代发详情
     *
     * @param userAuthId 代发ID
     * @return UserAuthResDTO
     */
    UserAuthResDTO getInfo(Long userAuthId);

    /**
     * 代发 停用 启用
     *
     * @param updateDelFlagDTO 停用启用入参
     * @return Integer
     */
    Integer updateDelFlag(UserAuthUpdateDelFlagDTO updateDelFlagDTO);

    /**
     * 代发审核
     *
     * @param auditDTO 审核入参
     * @return Integer
     */
    Integer approve(UserAuthAuditDTO auditDTO);

    /**
     * APP代发分页
     *
     * @param pageDTO 分页入参
     * @return Page<UserAuthAppPageResDTO>
     */
    Page<UserAuthAppPageResDTO> appPage(UserAuthPageDTO pageDTO);

    /**
     * 编辑代发
     *
     * @param updateDTO 编辑入参
     * @return Integer
     */
    Integer update(UserAuthUpdateDTO updateDTO);
}
