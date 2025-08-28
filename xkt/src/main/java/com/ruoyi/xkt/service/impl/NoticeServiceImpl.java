package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.xkt.domain.Notice;
import com.ruoyi.xkt.domain.UserNotice;
import com.ruoyi.xkt.dto.notice.*;
import com.ruoyi.xkt.enums.*;
import com.ruoyi.xkt.mapper.NoticeMapper;
import com.ruoyi.xkt.mapper.UserFavoritesMapper;
import com.ruoyi.xkt.mapper.UserNoticeMapper;
import com.ruoyi.xkt.mapper.UserSubscriptionsMapper;
import com.ruoyi.xkt.service.INoticeService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 公告 服务层实现
 *
 * @author ruoyi
 */
@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements INoticeService {

    final NoticeMapper noticeMapper;
    final UserSubscriptionsMapper userSubMapper;
    final UserFavoritesMapper userFavMapper;
    final UserNoticeMapper userNoticeMapper;
    final SysUserMapper userMapper;

    /**
     * 新增公告
     *
     * @param createDTO 新增公告参数
     * @return Integer
     */
    @Override
    @Transactional
    public Integer create(NoticeCreateDTO createDTO) {
        nullFilter(createDTO.getOwnerType(), createDTO.getStoreId(), createDTO.getPerpetuity(), createDTO.getEffectStart(), createDTO.getEffectEnd());
        Notice notice = BeanUtil.toBean(createDTO, Notice.class).setNoticeType(NoticeType.ANNOUNCEMENT.getValue()).setUserId(SecurityUtils.getUserId());
        notice.setCreateBy(SecurityUtils.getUsername());
        int count = this.noticeMapper.insert(notice);
        final Date voucherDate = java.sql.Date.valueOf(LocalDate.now());
        final List<Long> userIdList = Objects.equals(createDTO.getOwnerType(), NoticeOwnerType.STORE.getValue())
                // 档口发的公告，则发送给关注档口用户
                ? this.userSubMapper.selectUserFocusList(notice.getStoreId())
                // 系统发的公告，则发送给所有用户
                : this.userMapper.selectList(new LambdaQueryWrapper<SysUser>().eq(SysUser::getDelFlag, Constants.UNDELETED))
                .stream().map(SysUser::getUserId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(userIdList)) {
            return 0;
        }
        // 往user_notice表插入数据
        List<UserNotice> userNoticeList = userIdList.stream().map(userId -> new UserNotice().setNoticeId(notice.getId())
                        .setUserId(userId).setReadStatus(NoticeReadType.UN_READ.getValue()).setVoucherDate(voucherDate)
                        .setTargetNoticeType(Objects.equals(createDTO.getOwnerType(), NoticeOwnerType.STORE.getValue())
                                ? UserNoticeType.FOCUS_STORE.getValue() : UserNoticeType.SYSTEM_MSG.getValue()))
                .collect(Collectors.toList());
        this.userNoticeMapper.insert(userNoticeList);
        return count;
    }


    /**
     * 编辑公告
     *
     * @param editDTO 编辑入参
     * @return Integer
     */
    @Override
    @Transactional
    public Integer edit(NoticeEditDTO editDTO) {
        nullFilter(editDTO.getOwnerType(), editDTO.getStoreId(), editDTO.getPerpetuity(), editDTO.getEffectStart(), editDTO.getEffectEnd());
        Notice notice = Optional.ofNullable(this.noticeMapper.selectOne(new LambdaQueryWrapper<Notice>()
                        .eq(Notice::getId, editDTO.getNoticeId()).eq(Notice::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("公告不存在!", HttpStatus.ERROR));
        BeanUtil.copyProperties(editDTO, notice);
        notice.setNoticeType(NoticeType.ANNOUNCEMENT.getValue());
        notice.setUpdateBy(SecurityUtils.getUsername());
        return this.noticeMapper.updateById(notice);
    }

    /**
     * 删除公告
     *
     * @param deleteDTO 删除入参
     * @return Integer
     */
    @Override
    @Transactional
    public Integer delete(NoticeDeleteDTO deleteDTO) {
        List<Notice> noticeList = this.noticeMapper.selectList(new LambdaQueryWrapper<Notice>()
                .in(Notice::getId, deleteDTO.getNoticeIdList()).eq(Notice::getDelFlag, Constants.UNDELETED));
        if (CollectionUtils.isEmpty(noticeList)) {
            return 0;
        }
        noticeList.forEach(x -> x.setDelFlag(Constants.DELETED));
        int count = this.noticeMapper.updateById(noticeList).size();
        final List<Long> noticeIdList = noticeList.stream().map(Notice::getId).collect(Collectors.toList());
        // 将已分发到用户的公告置为无效
        List<UserNotice> userNoticeList = this.userNoticeMapper.selectList(new LambdaQueryWrapper<UserNotice>()
                .in(UserNotice::getNoticeId, noticeIdList).eq(UserNotice::getDelFlag, Constants.UNDELETED));
        if (CollectionUtils.isEmpty(userNoticeList)) {
            return count;
        }
        userNoticeList.forEach(x -> x.setDelFlag(Constants.DELETED));
        this.userNoticeMapper.updateById(userNoticeList);
        return count;
    }

    /**
     * 获取公告详情
     *
     * @param noticeId 公告ID
     * @return NoticeResDTO
     */
    @Override
    @Transactional(readOnly = true)
    public NoticeResDTO getInfo(Long noticeId) {
        Notice notice = Optional.ofNullable(this.noticeMapper.selectOne(new LambdaQueryWrapper<Notice>()
                        .eq(Notice::getId, noticeId).eq(Notice::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("公告不存在!", HttpStatus.ERROR));
        return BeanUtil.toBean(notice, NoticeResDTO.class).setId(noticeId);
    }

    /**
     * 分页查询公告列表
     *
     * @param pageDTO 分页查询入参
     * @return Page<NoticeResDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public Page<NoticeResDTO> page(NoticePageDTO pageDTO) {
        // 查询公告
        LambdaQueryWrapper<Notice> queryWrapper = new LambdaQueryWrapper<Notice>().eq(Notice::getDelFlag, Constants.UNDELETED)
                .eq(Notice::getNoticeType, NoticeType.ANNOUNCEMENT.getValue())
                .eq(Notice::getOwnerType, pageDTO.getOwnerType()).orderByDesc(Notice::getCreateTime);
        if (StringUtils.isNotBlank(pageDTO.getNoticeTitle())) {
            queryWrapper.like(Notice::getNoticeTitle, pageDTO.getNoticeTitle());
        }
        PageHelper.startPage(pageDTO.getPageNum(), pageDTO.getPageSize());
        List<Notice> noticeList = this.noticeMapper.selectList(queryWrapper);
        return Page.convert(new PageInfo<>(noticeList), BeanUtil.copyToList(noticeList, NoticeResDTO.class));
    }

    /**
     * 空值校验
     *
     * @param ownerType   公告所属类型
     * @param storeId     档口ID
     * @param perpetuity  是否永久生效
     * @param effectStart 生效开始时间
     * @param effectEnd   生效结束时间
     */
    private void nullFilter(Integer ownerType, Long storeId, Integer perpetuity, Date effectStart, Date effectEnd) {
        // 如果是档口发公告，则storeId不可为空
        if (Objects.equals(ownerType, NoticeOwnerType.STORE.getValue()) && ObjectUtils.isEmpty(storeId)) {
            throw new ServiceException("档口发公告，storeId不可为空!", HttpStatus.ERROR);
        }
        if (ObjectUtils.isEmpty(perpetuity) && ObjectUtils.isEmpty(effectStart) && ObjectUtils.isEmpty(effectEnd)) {
            throw new ServiceException("生效时间不能同时为空!", HttpStatus.ERROR);
        }
    }

    /**
     * 创建单个通知
     *
     * @param userId           被通知的userId
     * @param title            标题
     * @param noticeType       NoticeType
     * @param ownerType        NoticeOwnerType
     * @param storeId          档口ID
     * @param targetNoticeType 目标通知类型
     * @param content          通知内容
     * @return 新增成功条数
     */
    public Integer createSingleNotice(Long userId, String title, Integer noticeType, Integer ownerType, Long storeId, Integer targetNoticeType, String content) {
        Notice notice = new Notice().setNoticeTitle(title).setNoticeType(noticeType).setOwnerType(ownerType)
                .setStoreId(storeId).setUserId(userId).setPerpetuity(NoticePerpetuityType.PERMANENT.getValue())
                .setNoticeContent(content);
        this.noticeMapper.insert(notice);
        return this.userNoticeMapper.insert(new UserNotice().setNoticeId(notice.getId()).setUserId(userId).setReadStatus(NoticeReadType.UN_READ.getValue())
                .setVoucherDate(java.sql.Date.valueOf(LocalDate.now())).setTargetNoticeType(targetNoticeType));
    }

    /**
     * 获取最新的10条公告
     *
     * @return List<NoticeLatest10ResDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public List<NoticeLatest10ResDTO> latest10() {
        List<Notice> noticeList = this.noticeMapper.selectList(new LambdaQueryWrapper<Notice>()
                .eq(Notice::getNoticeType, NoticeType.ANNOUNCEMENT.getValue()).eq(Notice::getDelFlag, Constants.UNDELETED)
                .eq(Notice::getOwnerType, NoticeOwnerType.SYSTEM.getValue()).orderByDesc(Notice::getCreateTime));
        if (CollectionUtils.isEmpty(noticeList)) {
            return new ArrayList<>();
        }
        final Date now = new Date();
        return noticeList.stream().filter(x -> {
                    // 存在有效期，则判断是否在有效期内
                    if (Objects.equals(x.getPerpetuity(), NoticePerpetuityType.TEMPORARY.getValue())) {
                        return x.getEffectStart().before(now) && x.getEffectEnd().after(now);
                    }
                    return Boolean.TRUE;
                }).limit(10)
                .map(x -> new NoticeLatest10ResDTO().setId(x.getId()).setNoticeTitle(x.getNoticeTitle()))
                .collect(Collectors.toList());
    }


}
