package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.enums.AdType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.xkt.domain.*;
import com.ruoyi.xkt.dto.adminAdvertRound.*;
import com.ruoyi.xkt.dto.advertRound.AdRoundUpdateDTO;
import com.ruoyi.xkt.enums.*;
import com.ruoyi.xkt.mapper.*;
import com.ruoyi.xkt.service.IAdminAdvertRoundService;
import com.ruoyi.xkt.service.IAssetService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 推广营销轮次播放Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
@RequiredArgsConstructor
public class AdminAdvertRoundServiceImpl implements IAdminAdvertRoundService {

    final IAssetService assetService;
    final AdvertRoundMapper advertRoundMapper;
    final SysFileMapper fileMapper;
    final AdvertStoreFileMapper advertStoreFileMapper;
    final AdvertRoundRecordMapper advertRoundRecordMapper;
    final StoreProductMapper storeProdMapper;

    /**
     * 管理员查询推广营销分页
     *
     * @param pageDTO 分页入参
     * @return Page<AdminAdRoundPageResDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AdminAdRoundPageResDTO> page(AdminAdRoundPageDTO pageDTO) {
        // 用户是否为档口管理者或子账户
        if (!SecurityUtils.isAdmin()) {
            throw new ServiceException("当前用户非管理员账号，无权限操作!", HttpStatus.ERROR);
        }
        Optional.ofNullable(pageDTO.getLaunchStatus()).orElseThrow(() -> new ServiceException("投放状态launchStatus必传", HttpStatus.ERROR));
        PageHelper.startPage(pageDTO.getPageNum(), pageDTO.getPageSize());
        List<AdminAdRoundPageResDTO> list = this.advertRoundMapper.selectAdminAdvertPage(pageDTO);
        // 所有的商品id列表
        List<Long> prodIdList = list.stream().filter(x -> StringUtils.isNotBlank(x.getProdIdStr()))
                .flatMap(x -> StrUtil.split(x.getProdIdStr(), ",").stream()).map(Long::parseLong).collect(Collectors.toList());
        Map<Long, String> storeProdMap = CollectionUtils.isEmpty(prodIdList) ? new ConcurrentHashMap<>()
                : this.storeProdMapper.selectByIds(prodIdList).stream().collect(Collectors.toMap(StoreProduct::getId, StoreProduct::getProdArtNum));
        list.forEach(item -> {
            item.setPlatformName(AdPlatformType.of(item.getPlatformId()).getLabel())
                    .setTypeName(AdType.of(item.getTypeId()).getLabel())
                    .setTabName(ObjectUtils.isNotEmpty(item.getTabId()) ? AdTab.of(item.getTabId()).getLabel() : "")
                    .setLaunchStatusName(ObjectUtils.isNotEmpty(item.getLaunchStatus()) ? AdLaunchStatus.of(item.getLaunchStatus()).getLabel() : "")
                    .setPicAuditStatusName(ObjectUtils.isNotEmpty(item.getPicAuditStatus()) ? AdPicAuditStatus.of(item.getPicAuditStatus()).getLabel() : "")
                    .setPicDesignTypeName(ObjectUtils.isNotEmpty(item.getPicDesignType()) ? AdDesignType.of(item.getPicDesignType()).getLabel() : "")
                    .setPicAuditStatusName(ObjectUtils.isNotEmpty(item.getPicAuditStatus()) ? AdPicAuditStatus.of(item.getPicAuditStatus()).getLabel() : "")
                    .setPicSetTypeName(ObjectUtils.isNotEmpty(item.getPicSetType()) ? AdPicSetType.of(item.getPicSetType()).getLabel() : "")
                    .setBiddingStatusName(ObjectUtils.isNotEmpty(item.getBiddingStatus()) ? AdBiddingStatus.of(item.getBiddingStatus()).getLabel() : "");
            if (StringUtils.isNotBlank(item.getProdIdStr())) {
                item.setProdArtNumList(StrUtil.split(item.getProdIdStr(), ",").stream().map(Long::parseLong).map(storeProdMap::get).collect(Collectors.toList()));
            }
        });
        return Page.convert(new PageInfo<>(list));
    }

    /**
     * 管理员审核推广图
     *
     * @param auditDTO 审核推广图入参
     * @return Integer
     */
    @Override
    @Transactional
    public Integer auditPic(AdminAdRoundAuditDTO auditDTO) {
        // 用户是否为档口管理者或子账户
        if (!SecurityUtils.isAdmin()) {
            throw new ServiceException("当前用户非管理员账号，无权限操作!", HttpStatus.ERROR);
        }
        AdvertRound advertRound = Optional.ofNullable(this.advertRoundMapper.selectOne(new LambdaQueryWrapper<AdvertRound>()
                        .eq(AdvertRound::getId, auditDTO.getAdvertRoundId()).eq(AdvertRound::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("推广位不存在!", HttpStatus.ERROR));
        // 有可能管理员一直没刷新页面，该广告位已被其它档口抢走
        if (!Objects.equals(advertRound.getStoreId(), auditDTO.getStoreId())) {
            throw new ServiceException("推广位已被其它档口抢走，审核失败!请刷新页面!");
        }
        // 校验图片审核类型是否存在
        AdPicAuditStatus.of(auditDTO.getPicAuditStatus());
        advertRound.setPicAuditStatus(auditDTO.getPicAuditStatus()).setRejectReason(auditDTO.getRejectReason());
        int count = this.advertRoundMapper.updateById(advertRound);
        // 如果审核通过的话，则将图片保存到系统图片库中
        if (Objects.equals(auditDTO.getPicAuditStatus(), AdPicAuditStatus.AUDIT_PASS.getValue())) {
            AdvertStoreFile advertStoreFile = new AdvertStoreFile().setAdvertRoundId(advertRound.getId()).setVoucherDate(java.sql.Date.valueOf(LocalDate.now()))
                    .setStoreId(advertRound.getStoreId()).setPicId(advertRound.getPicId()).setTypeId(advertRound.getTypeId()).setPicOwnType(AdPicOwnType.STORE.getValue())
                    .setDisplayType(advertRound.getDisplayType()).setPosition(advertRound.getPosition());
            this.advertStoreFileMapper.insert(advertStoreFile);
        }
        return count;
    }

    /**
     * 档口退订推广位
     *
     * @param unsubscribeDTO 退订入参
     * @return Integer
     */
    @Override
    @Transactional
    public Integer unsubscribe(AdminAdRoundUnsubscribeDTO unsubscribeDTO) {
        // 用户是否为档口管理者或子账户
        if (!SecurityUtils.isAdmin()) {
            throw new ServiceException("当前用户非管理员账号，无权限操作!", HttpStatus.ERROR);
        }
        AdvertRound advertRound = Optional.ofNullable(this.advertRoundMapper.selectOne(new LambdaQueryWrapper<AdvertRound>()
                        .eq(AdvertRound::getId, unsubscribeDTO.getAdvertRoundId()).eq(AdvertRound::getDelFlag, Constants.UNDELETED)
                        .eq(AdvertRound::getStoreId, unsubscribeDTO.getStoreId())))
                .orElseThrow(() -> new ServiceException("档口购买的推广位不存在!", HttpStatus.ERROR));
        // 若是推广位已投放，则不可退订
        if (Objects.equals(advertRound.getLaunchStatus(), AdLaunchStatus.LAUNCHING.getValue())) {
            throw new ServiceException("推广位已投放，购买后不可退订!", HttpStatus.ERROR);
        }
        // 判断当前时间距离开播是否小于12h，若是：则不可取消
        Date twelveHoursAfter = DateUtils.toDate(LocalDateTime.now().plusHours(12));
        if (twelveHoursAfter.after(advertRound.getStartTime())) {
            throw new ServiceException("距推广开播小于12小时，不可退订!", HttpStatus.ERROR);
        }
        // 如果扣除了费用，则减去退回金额
        BigDecimal remainPayPrice = advertRound.getPayPrice().subtract(ObjectUtils.defaultIfNull(unsubscribeDTO.getDeductionFee(), BigDecimal.ZERO));
        // 将费用退回到档口余额中
        assetService.refundAdvertFee(advertRound.getStoreId(), remainPayPrice);
        // 将推广位置为空
        return this.advertRoundMapper.updateAttrNull(advertRound.getId());
    }

    /**
     * @param picDTO 档口上传推广图入参
     * @return Integer
     */
    @Override
    @Transactional
    public Integer uploadAdvertPic(AdRoundUpdateDTO picDTO) {
        // 用户是否为档口管理者或子账户
        if (!SecurityUtils.isAdmin()) {
            throw new ServiceException("当前用户非管理员账号，无权限操作!", HttpStatus.ERROR);
        }
        AdvertRound advertRound = Optional.ofNullable(this.advertRoundMapper.selectOne(new LambdaQueryWrapper<AdvertRound>()
                        .eq(AdvertRound::getId, picDTO.getAdvertRoundId()).eq(AdvertRound::getStoreId, picDTO.getStoreId())
                        .eq(AdvertRound::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("推广位不存在!", HttpStatus.ERROR));
        SysFile file = BeanUtil.toBean(picDTO.getFile(), SysFile.class);
        int count = this.fileMapper.insert(file);
        // 更新推广位的图片ID
        advertRound.setPicId(file.getId());
        advertRound.setPicSetType(AdPicSetType.SET.getValue());
        advertRound.setPicAuditStatus(AdPicAuditStatus.AUDIT_PASS.getValue());
        this.advertRoundMapper.updateById(advertRound);
        // 将档口上传图片保存到AdvertStoreFile
        AdvertStoreFile advertStoreFile = new AdvertStoreFile().setAdvertRoundId(advertRound.getId())
                .setVoucherDate(java.sql.Date.valueOf(LocalDate.now())).setPicOwnType(AdPicOwnType.STORE.getValue()).setPosition(advertRound.getPosition())
                .setStoreId(advertRound.getStoreId()).setPicId(file.getId()).setTypeId(advertRound.getTypeId()).setDisplayType(advertRound.getDisplayType());
        this.advertStoreFileMapper.insert(advertStoreFile);
        return count;
    }

    /**
     * 管理员拦截推广营销
     *
     * @param interceptDTO 拦截推广营销入参
     * @return Integer
     */
    @Override
    @Transactional
    public synchronized Integer sysIntercept(AdminAdRoundSysInterceptDTO interceptDTO) {
        // 用户是否为档口管理者或子账户
        if (!SecurityUtils.isAdmin()) {
            throw new ServiceException("当前用户非管理员账号，无权限操作!", HttpStatus.ERROR);
        }
        final LocalDateTime nineThirty = LocalDateTime.of(LocalDate.now(), LocalTime.of(21, 30));
        final LocalDateTime tenFive = LocalDateTime.of(LocalDate.now(), LocalTime.of(22, 5));
        // 判断当前时间是否为晚上9:30 - 10:05区间，若是，则管理员不可操作推广拦截
        if (LocalDateTime.now().isAfter(nineThirty) && LocalDateTime.now().isBefore(tenFive)) {
            throw new ServiceException("21:30 - 22:05之间，为档口购买推广时间，管理员不可操作推广拦截!", HttpStatus.ERROR);
        }
        AdvertRound advertRound = Optional.ofNullable(this.advertRoundMapper.selectOne(new LambdaQueryWrapper<AdvertRound>()
                        .eq(AdvertRound::getId, interceptDTO.getAdvertRoundId()).eq(AdvertRound::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("推广位不存在!", HttpStatus.ERROR));
        // 如果该广告位已被拦截，则不可再次拦截
        if (Objects.equals(advertRound.getSysIntercept(), AdSysInterceptType.INTERCEPT.getValue())) {
            throw new ServiceException("该推广位已被拦截，不可再次拦截!", HttpStatus.ERROR);
        }
        // 判断要给档口购买的推广位，该档口是否自己已购买
        LambdaQueryWrapper<AdvertRound> queryWrapper = new LambdaQueryWrapper<AdvertRound>()
                .eq(AdvertRound::getAdvertId, advertRound.getAdvertId()).eq(AdvertRound::getRoundId, advertRound.getRoundId())
                .eq(AdvertRound::getSymbol, interceptDTO.getSymbol()).eq(AdvertRound::getLaunchStatus, interceptDTO.getLaunchStatus())
                .eq(AdvertRound::getStoreId, interceptDTO.getStoreId()).eq(AdvertRound::getDelFlag, Constants.UNDELETED);
        // 如果是位置枚举类型，则要加上具体的位置
        if (Objects.equals(advertRound.getShowType(), AdShowType.POSITION_ENUM.getValue())) {
            queryWrapper.eq(AdvertRound::getPosition, advertRound.getPosition());
        }
        // 该档口自己是否已购买该推广位
        List<AdvertRound> existList = this.advertRoundMapper.selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(existList)) {
            throw new ServiceException("档口：" + interceptDTO.getStoreName() + "，已出价该推广位，不可拦截!", HttpStatus.ERROR);
        }
        // 若该推广位已投放
        if (Objects.equals(advertRound.getLaunchStatus(), AdLaunchStatus.LAUNCHING.getValue())) {
            if (ObjectUtils.isNotEmpty(advertRound.getStoreId())) {
                throw new ServiceException("该推广位为档口正常购买，已投放，不可拦截!", HttpStatus.ERROR);
            }
            // 若为待投放推广
        } else {
            // 判断拦截的推广位，是否档口已经购买。
            if (ObjectUtils.isNotEmpty(advertRound.getStoreId()) && Objects.equals(advertRound.getSysIntercept(), AdSysInterceptType.UN_INTERCEPT.getValue())) {
                // 判断当前时间是否为投放前一天22:00之后 若是，则不可拦截，因该推广位已售出
                LocalDateTime twoHourBeforeStartTime = advertRound.getStartTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().withHour(22).withMinute(0).withSecond(0);
                if (LocalDateTime.now().isAfter(twoHourBeforeStartTime)) {
                    throw new ServiceException("当前推广位已被档口购买，不可拦截!", HttpStatus.ERROR);
                }
            }
        }
        // 如果该推广位是被档口正常购买，则直接退费
        if (ObjectUtils.isNotEmpty(advertRound.getStoreId()) && Objects.equals(advertRound.getSysIntercept(), AdSysInterceptType.UN_INTERCEPT.getValue())) {
            // 给档口退费
            assetService.refundAdvertFee(advertRound.getStoreId(), ObjectUtils.defaultIfNull(advertRound.getPayPrice(), BigDecimal.ZERO));
            // 档口竞价失败加入AdvertRoundRecord表
            this.record(advertRound);
        }
        // 设置系统拦截的数据 并将系统拦截设置为1
        advertRound.setStoreId(interceptDTO.getStoreId()).setSysIntercept(AdSysInterceptType.INTERCEPT.getValue())
                .setVoucherDate(java.sql.Date.valueOf(LocalDate.now())).setBiddingStatus(AdBiddingStatus.BIDDING_SUCCESS.getValue())
                .setBiddingTempStatus(AdBiddingStatus.BIDDING_SUCCESS.getValue()).setCreateBy(SecurityUtils.getUsernameSafe());
        if (ObjectUtils.isNotEmpty(interceptDTO.getFile())) {
            SysFile file = BeanUtil.toBean(interceptDTO.getFile(), SysFile.class);
            this.fileMapper.insert(file);
            advertRound.setPicId(file.getId()).setPicAuditStatus(AdPicAuditStatus.AUDIT_PASS.getValue())
                    .setPicDesignType(AdDesignType.SYS_DESIGN.getValue()).setPicSetType(AdPicSetType.SET.getValue());
            // 添加系统归属推广图
            AdvertStoreFile advertStoreFile = new AdvertStoreFile().setAdvertRoundId(advertRound.getId()).setVoucherDate(java.sql.Date.valueOf(LocalDate.now()))
                    .setStoreId(advertRound.getStoreId()).setPicId(file.getId()).setTypeId(advertRound.getTypeId()).setPicOwnType(AdPicOwnType.SYSTEM.getValue())
                    .setDisplayType(advertRound.getDisplayType()).setPosition(advertRound.getPosition());
            this.advertStoreFileMapper.insert(advertStoreFile);
        }
        if (ObjectUtils.isNotEmpty(interceptDTO.getStoreProdIdList())) {
            advertRound.setProdIdStr(StringUtils.join(interceptDTO.getStoreProdIdList(), ","));
        }
        // 如果是位置枚举，则设置一个很高的价格（200 - 300）范围，有其它档口愿意出更高价格拿下就随他去
        if (Objects.equals(advertRound.getShowType(), AdShowType.POSITION_ENUM.getValue())) {
            advertRound.setPayPrice(BigDecimal.valueOf(RandomUtils.nextLong(20, 30 + 1) * 10));
        }
        return this.advertRoundMapper.updateById(advertRound);
    }

    /**
     * 取消拦截广告位
     *
     * @param cancelInterceptDTO 取消拦截入参
     * @return Integer
     */
    @Override
    @Transactional
    public Integer cancelIntercept(AdminAdRoundCancelInterceptDTO cancelInterceptDTO) {
        // 用户是否为档口管理者或子账户
        if (!SecurityUtils.isAdmin()) {
            throw new ServiceException("当前用户非管理员账号，无权限操作!", HttpStatus.ERROR);
        }
        // 该推广位是否被拦截
        AdvertRound advertRound = Optional.ofNullable(this.advertRoundMapper.selectOne(new LambdaQueryWrapper<AdvertRound>()
                        .eq(AdvertRound::getId, cancelInterceptDTO.getAdvertRoundId()).eq(AdvertRound::getDelFlag, Constants.UNDELETED)
                        .eq(AdvertRound::getStoreId, cancelInterceptDTO.getStoreId()).eq(AdvertRound::getSysIntercept, AdSysInterceptType.INTERCEPT.getValue())))
                .orElseThrow(() -> new ServiceException("该推广位未被拦截，不可取消!", HttpStatus.ERROR));
        // 被系统拦截的推广位不用退费，直接清空数据即可
        return this.advertRoundMapper.updateAttrNull(advertRound.getId());
    }

    /**
     * 获取管理员推广营销列表 已投放/待投放数量
     *
     * @return AdminAdRoundPageStatusCountResDTO
     */
    @Override
    @Transactional(readOnly = true)
    public AdminAdRoundStatusCountResDTO statusCount() {
        final Date now = java.sql.Date.valueOf(LocalDate.now().plusDays(1));
        final Date sixMonthAgo = java.sql.Date.valueOf(LocalDate.now().minusMonths(6));
        return this.advertRoundMapper.statusCount(sixMonthAgo, now);
    }

    /**
     * 记录竞价失败档口推广营销
     *
     * @param failAdvert 竞价失败的推广营销
     */
    private void record(AdvertRound failAdvert) {
        // 新增推广营销历史记录 将旧档口推广营销 置为竞价失败
        AdvertRoundRecord record = BeanUtil.toBean(failAdvert, AdvertRoundRecord.class);
        record.setId(null).setAdvertRoundId(failAdvert.getId()).setDisplayType(failAdvert.getDisplayType())
                // 置为竞价失败
                .setBiddingStatus(AdBiddingStatus.BIDDING_FAIL.getValue());
        this.advertRoundRecordMapper.insert(record);
    }

}
