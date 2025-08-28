package com.ruoyi.xkt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.xkt.domain.UserNotice;
import com.ruoyi.xkt.dto.userNotice.*;
import com.ruoyi.xkt.enums.UserNoticeType;
import com.ruoyi.xkt.mapper.UserNoticeMapper;
import com.ruoyi.xkt.service.IUserNoticeService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 用户所有通知Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
@RequiredArgsConstructor
public class UserNoticeServiceImpl implements IUserNoticeService {

    final UserNoticeMapper userNoticeMapper;

    /**
     * 查询用户所有通知列表
     *
     * @param pageDTO 查询入参
     * @return Page<UserNoticeResDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserNoticeResDTO> pcPage(UserNoticePageDTO pageDTO) {
        PageHelper.startPage(pageDTO.getPageNum(), pageDTO.getPageSize());
        List<UserNoticeResDTO> list = this.userNoticeMapper.selectUserNoticeList(SecurityUtils.getUserId(), pageDTO.getNoticeTitle(), pageDTO.getNoticeType());
        list.forEach(x -> x.setTargetNoticeTypeName(UserNoticeType.of(x.getTargetNoticeType()).getLabel()));
        return Page.convert(new PageInfo<>(list));
    }

    /**
     * 获取APP用户消息列表
     *
     * @return List<UserNoticeAppResDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserNoticeAppListResDTO> appList() {
        List<UserNoticeAppListResDTO> appList = this.userNoticeMapper.appList(SecurityUtils.getUserId());
        if (CollectionUtils.isEmpty(appList)) {
            return Collections.emptyList();
        }
        return appList.stream().collect(Collectors.groupingBy(UserNoticeAppListResDTO::getTargetNoticeType,
                        Collectors.maxBy(Comparator.comparing(UserNoticeAppListResDTO::getCreateTime))))
                .values().stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(x -> x.setTargetNoticeTypeName(UserNoticeType.of(x.getTargetNoticeType()).getLabel()))
                .collect(Collectors.toList());
    }

    /**
     * 获取APP某一个类型消息列表
     *
     * @param pageDTO 分页入参
     * @return List<UserNoticeAppResDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserNoticeAppResDTO> appTypePage(UserNoticeAppTypePageDTO pageDTO) {
        PageHelper.startPage(pageDTO.getPageNum(), pageDTO.getPageSize());
        List<UserNoticeAppResDTO> list = this.userNoticeMapper.selectAppTypePage(SecurityUtils.getUserId(), pageDTO.getTargetNoticeType());
        return Page.convert(new PageInfo<>(list));
    }

    /**
     * app 全部已读
     *
     * @return Integer
     */
    @Override
    @Transactional
    public Integer appBatchRead() {
        Long userId = SecurityUtils.getUserIdSafe();
        if (ObjectUtils.isEmpty(userId)) {
            throw new ServiceException("用户未登录，请先登录!", HttpStatus.ERROR);
        }
        List<UserNotice> unReadList = this.userNoticeMapper.selectList(new LambdaQueryWrapper<UserNotice>()
                .eq(UserNotice::getUserId, userId).eq(UserNotice::getReadStatus, 0)
                .eq(UserNotice::getDelFlag, Constants.UNDELETED));
        if (CollectionUtils.isEmpty(unReadList)) {
            return 0;
        }
        unReadList.forEach(x -> x.setReadStatus(1));
        return this.userNoticeMapper.updateById(unReadList).size();
    }

    /**
     * app 某一个具体分类已读
     *
     * @param targetNoticeType type
     * @return
     */
    @Override
    @Transactional
    public Integer appTypeRead(Integer targetNoticeType) {
        Long userId = SecurityUtils.getUserIdSafe();
        if (ObjectUtils.isEmpty(userId)) {
            throw new ServiceException("用户未登录，请先登录!", HttpStatus.ERROR);
        }
        List<UserNotice> unReadList = this.userNoticeMapper.selectList(new LambdaQueryWrapper<UserNotice>()
                .eq(UserNotice::getUserId, userId).eq(UserNotice::getReadStatus, 0)
                .eq(UserNotice::getTargetNoticeType, targetNoticeType)
                .eq(UserNotice::getDelFlag, Constants.UNDELETED));
        if (CollectionUtils.isEmpty(unReadList)) {
            return 0;
        }
        unReadList.forEach(x -> x.setReadStatus(1));
        return this.userNoticeMapper.updateById(unReadList).size();
    }

}
