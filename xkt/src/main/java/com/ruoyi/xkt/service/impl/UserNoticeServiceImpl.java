package com.ruoyi.xkt.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.xkt.dto.userNotice.*;
import com.ruoyi.xkt.enums.UserNoticeType;
import com.ruoyi.xkt.mapper.UserNoticeMapper;
import com.ruoyi.xkt.service.IUserNoticeService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
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
        List<UserNoticeResDTO> list = this.userNoticeMapper.selectUserNoticeList(SecurityUtils.getUserId(), pageDTO.getNoticeTitle());
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

}
