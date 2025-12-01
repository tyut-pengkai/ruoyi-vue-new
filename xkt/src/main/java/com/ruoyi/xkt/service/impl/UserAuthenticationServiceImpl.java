package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.sms.SmsClientWrapper;
import com.ruoyi.xkt.domain.SysFile;
import com.ruoyi.xkt.domain.UserAuthentication;
import com.ruoyi.xkt.dto.userAuthentication.*;
import com.ruoyi.xkt.enums.UserAuthStatus;
import com.ruoyi.xkt.mapper.SysFileMapper;
import com.ruoyi.xkt.mapper.UserAuthenticationMapper;
import com.ruoyi.xkt.service.IUserAuthenticationService;
import com.ruoyi.xkt.thirdpart.lfv2.Lfv2Client;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
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
    final SysFileMapper fileMapper;
    final SmsClientWrapper smsClient;
    final Lfv2Client lfv2Client;

    /**
     * 新增代发
     *
     * @param createDTO 新增代发入参
     * @return Integer
     */
    @Override
    @Transactional
    public Integer create(UserAuthCreateDTO createDTO) {
        Long userId = SecurityUtils.getUserIdSafe();
        if (ObjectUtils.isEmpty(userId)) {
            throw new ServiceException("用户未登录，请先登录!", HttpStatus.ERROR);
        }
        // 验证码验证
        boolean match = smsClient.matchVerificationCode(CacheConstants.SMS_AGENT_AUTH_CAPTCHA_CODE_KEY, createDTO.getPhonenumber().trim(), createDTO.getCode().trim());
        if (!match) {
            throw new ServiceException("验证码错误或已过期");
        }
        // 2. 校验代发身份信息是否通过 手机号  姓名  身份证号
        boolean phoneCheck = this.lfv2Client.checkPhone(createDTO.getPhonenumber().trim(), createDTO.getRealName().trim(), createDTO.getIdCard().trim());
        if (!phoneCheck) {
            throw new ServiceException("代发人员身份信息验证未通过！可能原因：1. 代发人员姓名或身份证号输入有误 2. 当前手机号非代发人员本人实名办理!", HttpStatus.ERROR);
        }
        // 保存身份证人脸
        SysFile idCardFace = BeanUtil.toBean(createDTO.getFaceFile(), SysFile.class);
        this.fileMapper.insert(idCardFace);
        // 保存身份证国徽
        SysFile idCardEmblem = BeanUtil.toBean(createDTO.getEmblemFile(), SysFile.class);
        this.fileMapper.insert(idCardEmblem);
        UserAuthentication userAuth = new UserAuthentication().setIdCard(createDTO.getIdCard()).setPhonenumber(createDTO.getPhonenumber())
                .setRealName(createDTO.getRealName()).setUserId(userId).setAuthStatus(UserAuthStatus.UN_AUDITED.getValue())
                .setIdCardFaceFileId(idCardFace.getId()).setIdCardEmblemFileId(idCardEmblem.getId());
        return userAuthMapper.insert(userAuth);
    }

    /**
     * 编辑代发
     *
     * @param updateDTO 编辑入参
     * @return Integer
     */
    @Override
    @Transactional
    public Integer update(UserAuthUpdateDTO updateDTO) {
        Long userId = SecurityUtils.getUserIdSafe();
        if (ObjectUtils.isEmpty(userId)) {
            throw new ServiceException("用户未登录，请先登录!", HttpStatus.ERROR);
        }
        UserAuthentication userAuth = Optional.ofNullable(this.userAuthMapper.selectOne(new LambdaQueryWrapper<UserAuthentication>()
                        .eq(UserAuthentication::getId, updateDTO.getUserAuthId()).eq(UserAuthentication::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("代发专员不存在!", HttpStatus.ERROR));
        // 保存身份证人脸
        SysFile idCardFace = BeanUtil.toBean(updateDTO.getFaceFile(), SysFile.class);
        this.fileMapper.insert(idCardFace);
        // 保存身份证国徽
        SysFile idCardEmblem = BeanUtil.toBean(updateDTO.getEmblemFile(), SysFile.class);
        this.fileMapper.insert(idCardEmblem);
        userAuth.setIdCard(updateDTO.getIdCard()).setPhonenumber(updateDTO.getPhonenumber())
                .setRealName(updateDTO.getRealName()).setUserId(userId).setAuthStatus(UserAuthStatus.UN_AUDITED.getValue())
                .setIdCardFaceFileId(idCardFace.getId()).setIdCardEmblemFileId(idCardEmblem.getId());
        return userAuthMapper.updateById(userAuth);
    }

    /**
     * 根据userId查询代发详情
     *
     * @param userId 用户ID
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public UserAuthResDTO getInfoByUserId(Long userId) {
        UserAuthentication userAuth = this.userAuthMapper.selectOne(new LambdaQueryWrapper<UserAuthentication>()
                .eq(UserAuthentication::getUserId, userId).eq(UserAuthentication::getDelFlag, Constants.UNDELETED));
        if (ObjectUtils.isEmpty(userAuth)) {
            return new UserAuthResDTO();
        }
        SysFile faceFile = this.fileMapper.selectById(userAuth.getIdCardFaceFileId());
        SysFile emblemFile = this.fileMapper.selectById(userAuth.getIdCardEmblemFileId());
        return BeanUtil.toBean(userAuth, UserAuthResDTO.class).setUserAuthId(userAuth.getId())
                .setFaceUrl(faceFile.getFileUrl()).setEmblemUrl(emblemFile.getFileUrl());
    }

    /**
     * APP代发分页
     *
     * @param pageDTO 分页入参
     * @return Page<UserAuthAppPageResDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserAuthAppPageResDTO> appPage(UserAuthPageDTO pageDTO) {
        PageHelper.startPage(pageDTO.getPageNum(), pageDTO.getPageSize());
        List<UserAuthAppPageResDTO> list = this.userAuthMapper.selectUserAuthAppPage(pageDTO);
        return Page.convert(new PageInfo<>(list));
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
        PageHelper.startPage(pageDTO.getPageNum(), pageDTO.getPageSize());
        List<UserAuthPageResDTO> list = this.userAuthMapper.selectUserAuthPage(pageDTO);
        list.forEach(x -> x.setAuthStatusName(UserAuthStatus.of(x.getAuthStatus()).getLabel()));
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
        SysFile faceFile = this.fileMapper.selectById(userAuth.getIdCardFaceFileId());
        SysFile emblemFile = this.fileMapper.selectById(userAuth.getIdCardEmblemFileId());
        return BeanUtil.toBean(userAuth, UserAuthResDTO.class).setUserAuthId(userAuthId)
                .setFaceUrl(faceFile.getFileUrl()).setEmblemUrl(emblemFile.getFileUrl());
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
        // 用户是否为超级管理员
        if (!SecurityUtils.isSuperAdmin()) {
            throw new ServiceException("当前用户非超级管理员，无权限操作!", HttpStatus.ERROR);
        }
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
            if (!Objects.equals(userAuth.getAuthStatus(), UserAuthStatus.UN_AUDITED.getValue()) &&
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
