package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.xkt.domain.UserAuthentication;
import com.ruoyi.xkt.dto.userAuthentication.*;
import com.ruoyi.xkt.enums.UserAuthStatus;
import com.ruoyi.xkt.mapper.UserAuthenticationMapper;
import com.ruoyi.xkt.service.IUserAuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 用户代发认证Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
@RequiredArgsConstructor
public class UserAuthenticationServiceImpl implements IUserAuthenticationService {

    final UserAuthenticationMapper userAuthMapper;

    /**
     * 新增代发
     *
     * @param createDTO 新增代发入参
     * @return Integer
     */
    @Override
    @Transactional
    public Integer create(UserAuthCreateDTO createDTO) {
        UserAuthentication userAuth = BeanUtil.toBean(createDTO, UserAuthentication.class);
        userAuth.setAuthStatus(UserAuthStatus.UN_AUDITED.getValue());
        return userAuthMapper.insert(userAuth);
    }

    /**
     * 代发分页
     *
     * @param pageDTO 分页查询入参
     * @return Page<UserAuthPageResDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserAuthPageResDTO> page(UserAuthPageDTO pageDTO) {
        List<UserAuthPageResDTO> list = this.userAuthMapper.selectUserAuthPage(pageDTO);
        return Page.convert(new PageInfo<>(list));
    }

    /**
     * 代发详情
     *
     * @param userAuthId 代发ID
     * @return UserAuthResDTO
     */
    @Override
    @Transactional(readOnly = true)
    public UserAuthResDTO getInfo(Long userAuthId) {
        UserAuthentication userAuth = Optional.ofNullable(this.userAuthMapper.selectOne(new LambdaQueryWrapper<UserAuthentication>()
                        .eq(UserAuthentication::getId, userAuthId)))
                .orElseThrow(() -> new ServiceException("代发不存在!", HttpStatus.ERROR));
        return BeanUtil.toBean(userAuth, UserAuthResDTO.class);
    }

    /**
     * 代发 停用 启用
     *
     * @param updateDelFlagDTO 停用启用入参
     * @return Integer
     */
    @Override
    @Transactional
    public Integer updateDelFlag(UserAuthUpdateDelFlagDTO updateDelFlagDTO) {
        UserAuthentication userAuth = Optional.ofNullable(this.userAuthMapper.selectOne(new LambdaQueryWrapper<UserAuthentication>()
                        .eq(UserAuthentication::getId, updateDelFlagDTO.getUserAuthId())))
                .orElseThrow(() -> new ServiceException("代发不存在!", HttpStatus.ERROR));
        userAuth.setDelFlag(updateDelFlagDTO.getDelFlag() ? Constants.DELETED : Constants.UNDELETED);
        return this.userAuthMapper.updateById(userAuth);
    }

    /**
     * 代发审核
     *
     * @param auditDTO 审核入参
     * @return Integer
     */
    @Override
    @Transactional
    public Integer approve(UserAuthAuditDTO auditDTO) {
        UserAuthentication userAuth = Optional.ofNullable(this.userAuthMapper.selectOne(new LambdaQueryWrapper<UserAuthentication>()
                        .eq(UserAuthentication::getId, auditDTO.getUserAuthId())))
                .orElseThrow(() -> new ServiceException("代发不存在!", HttpStatus.ERROR));
        if (auditDTO.getApprove()) {
            // 如果代发状态不为 待审核 或 审核驳回 则报错
            if (!Objects.equals(userAuth.getAuthStatus(), UserAuthStatus.UN_AUDITED.getValue()) ||
                    !Objects.equals(userAuth.getAuthStatus(), UserAuthStatus.AUDIT_REJECTED.getValue())) {
                throw new ServiceException("当前状态不为待审核 或 审核驳回，不可审核!", HttpStatus.ERROR);
            }
            userAuth.setAuthStatus(UserAuthStatus.FORMAL_USE.getValue());
        } else {
            userAuth.setAuthStatus(UserAuthStatus.AUDIT_REJECTED.getValue());
            userAuth.setRejectReason(auditDTO.getRejectReason());
        }
        return this.userAuthMapper.updateById(userAuth);
    }


}
