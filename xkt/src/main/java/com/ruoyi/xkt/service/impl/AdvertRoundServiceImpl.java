package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.AdType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.xkt.domain.*;
import com.ruoyi.xkt.dto.advertRound.*;
import com.ruoyi.xkt.enums.*;
import com.ruoyi.xkt.mapper.*;
import com.ruoyi.xkt.service.IAdvertRoundService;
import com.ruoyi.xkt.service.IAssetService;
import com.ruoyi.xkt.service.INoticeService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.ruoyi.common.constant.CacheConstants.ADVERT_DEADLINE_KEY;
import static com.ruoyi.common.constant.CacheConstants.ADVERT_UPLOAD_FILTER_TIME_KEY;
import static com.ruoyi.common.constant.Constants.ADVERT_POPULAR;

/**
 * 推广营销轮次播放Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
@RequiredArgsConstructor
public class AdvertRoundServiceImpl implements IAdvertRoundService {

    final AdvertRoundMapper advertRoundMapper;
    final AdvertRoundRecordMapper advertRoundRecordMapper;
    final RedisCache redisCache;
    final StoreMapper storeMapper;
    final StoreProductMapper storeProdMapper;
    final SysFileMapper fileMapper;
    final IAssetService assetService;
    final INoticeService noticeService;
    final AdvertMapper advertMapper;


    // 推广营销位锁 key：symbol + roundId 或者 symbol + roundId + position 。value都是new Object()
    public static Map<String, Object> advertLockMap = new ConcurrentHashMap<>();
    // 推广轮次待播放 及 正在播放
    private static final List<Integer> LAUNCHING_STATUS_LIST =
            Arrays.asList(AdLaunchStatus.LAUNCHING.getValue(), AdLaunchStatus.UN_LAUNCH.getValue());
    // 推广轮次 未出价 及 已出价
    private static final List<Integer> BIDDING_STATUS_LIST =
            Arrays.asList(AdBiddingStatus.BIDDING.getValue(), AdBiddingStatus.UN_BIDDING.getValue());


    /**
     * 项目启动后执行一次，做初始化操作
     */
    @PostConstruct
    public void initAdvertLockMap() {
        // 清空advertLockMap，便于多次幂等操作
        advertLockMap.clear();
        // 初始化 advertRound的 symbol 锁资源对象
        List<AdvertRound> advertRoundList = this.advertRoundMapper.selectList(new LambdaQueryWrapper<AdvertRound>()
                .eq(AdvertRound::getDelFlag, Constants.UNDELETED)
                .in(AdvertRound::getLaunchStatus, Arrays.asList(AdLaunchStatus.LAUNCHING.getValue(), AdLaunchStatus.UN_LAUNCH.getValue())));
        if (CollectionUtils.isEmpty(advertRoundList)) {
            return;
        }
        // 依次初始化所资源对象  如果是时间范围的推广位，则锁的是 那一个播放轮；如果是位置枚举则 锁的是某一个具体资源位
        advertRoundList.stream().collect(Collectors.groupingBy(AdvertRound::getTypeId))
                .forEach((typeId, roundList) -> roundList
                        // 初始化  锁资源对象
                        .forEach(round -> advertLockMap.putIfAbsent(round.getSymbol(), new Object())));
    }

    /**
     * 更新广告位轮次的竞价状态
     * 此方法主要用于根据当前日期更新广告位轮次的竞价状态，以确保广告投放的正确性
     * 它首先查询符合条件的广告位轮次，然后根据当前时间与第一轮结束时间的比较，更新相应的竞价状态
     * 如果当前时间小于第一轮最后一天，则更新第一轮的竞价状态；如果等于第一轮最后一天，则更新第二轮的竞价状态
     *
     * @throws ParseException 如果日期解析失败
     */
    @Override
    @Transactional
    public void updateBiddingStatus() throws ParseException {
        List<AdvertRound> advertRoundList = this.advertRoundMapper.selectList(new LambdaQueryWrapper<AdvertRound>()
                .eq(AdvertRound::getDelFlag, Constants.UNDELETED)
                .in(AdvertRound::getRoundId, Arrays.asList(AdRoundType.PLAY_ROUND.getValue(), AdRoundType.SECOND_ROUND.getValue()))
                .in(AdvertRound::getLaunchStatus, Arrays.asList(AdLaunchStatus.LAUNCHING.getValue(), AdLaunchStatus.UN_LAUNCH.getValue())));
        if (CollectionUtils.isEmpty(advertRoundList)) {
            return;
        }
        // 待更新的推广位轮次
        List<AdvertRound> updateList = new ArrayList<>();
        // 如果当前时间小于第一轮最后一天则修改第一轮的竞价状态，若等于第一轮最后一天，则更新第二轮的竞价状态
        final Date now = DateUtils.parseDate(DateUtils.getDate(), DateUtils.YYYY_MM_DD);
        advertRoundList.stream().collect(Collectors.groupingBy(AdvertRound::getAdvertId))
                .forEach((advertId, roundList) -> {
                    // 判断当前时间所处的阶段 小于第一轮播放时间（有可能新广告还未开播）、处于第一轮中间、处于第一轮最后一天
                    final Date firstRoundEndTime = roundList.stream().filter(x -> x.getRoundId().equals(AdRoundType.PLAY_ROUND.getValue()))
                            .max(Comparator.comparing(AdvertRound::getEndTime))
                            .orElseThrow(() -> new ServiceException("获取推广结束时间失败，请联系客服!", HttpStatus.ERROR)).getEndTime();
                    // 判断当前时间是否为第一轮最后一天
                    if (now.before(firstRoundEndTime)) {
                        // 将广告位第 一 轮，出价状态从已出价 改为 竞价成功
                        roundList.stream().filter(x -> x.getRoundId().equals(AdRoundType.PLAY_ROUND.getValue()))
                                // 将出价状态和竞价状态不一致的播放轮次位置 筛选出来
                                .filter(x -> ObjectUtils.isNotEmpty(x.getBiddingStatus()) && ObjectUtils.isNotEmpty(x.getBiddingTempStatus())
                                        && !Objects.equals(x.getBiddingStatus(), x.getBiddingTempStatus()))
                                .forEach(x -> updateList.add(x.setBiddingStatus(x.getBiddingTempStatus())));
                    } else if (now.equals(firstRoundEndTime)) {
                        // 广告第二轮
                        List<AdvertRound> secondRoundList = roundList.stream().filter(x -> x.getRoundId().equals(AdRoundType.SECOND_ROUND.getValue())).collect(Collectors.toList());
                        // 有可能广告下线，没有第二轮广告了
                        if (CollectionUtils.isNotEmpty(secondRoundList)) {
                            // 将广告位第 二 轮，出价状态从已出价 改为 竞价成功
                            secondRoundList.stream()
                                    // 将出价状态和竞价状态不一致的播放轮次位置 筛选出来
                                    .filter(x -> ObjectUtils.isNotEmpty(x.getBiddingStatus()) && ObjectUtils.isNotEmpty(x.getBiddingTempStatus())
                                            && !Objects.equals(x.getBiddingStatus(), x.getBiddingTempStatus()))
                                    .forEach(x -> updateList.add(x.setBiddingStatus(x.getBiddingTempStatus())));
                        }
                    }
                });
        if (CollectionUtils.isEmpty(updateList)) {
            return;
        }
        this.advertRoundMapper.updateById(updateList);
    }

    /**
     * 根据advertRoundId获取当前位置枚举设置的商品
     *
     * @param advertRoundId advertRoundId
     * @return AdRoundSetProdResDTO
     */
    @Override
    @Transactional(readOnly = true)
    public List<AdRoundSetProdResDTO> getSetProdInfo(Long advertRoundId) {
        AdvertRound advertRound = Optional.ofNullable(this.advertRoundMapper.selectOne(new LambdaQueryWrapper<AdvertRound>()
                        .eq(AdvertRound::getId, advertRoundId).eq(AdvertRound::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("推广轮次不存在!"));
        if (!Objects.equals(advertRound.getShowType(), AdShowType.POSITION_ENUM.getValue())) {
            throw new ServiceException("当前推广位非位置枚举，不可调用该接口!", HttpStatus.ERROR);
        }
        // 用户是否为档口管理者或子账户
        if (!SecurityUtils.isAdmin() && !SecurityUtils.isStoreManagerOrSub(advertRound.getStoreId())) {
            throw new ServiceException("当前用户非档口管理者或子账号，无权限操作!", HttpStatus.ERROR);
        }
        if (StringUtils.isEmpty(advertRound.getProdIdStr())) {
            return Collections.emptyList();
        }
        List<StoreProduct> storeProdList = this.storeProdMapper.selectList(new LambdaQueryWrapper<StoreProduct>()
                .eq(StoreProduct::getDelFlag, Constants.UNDELETED).eq(StoreProduct::getStoreId, advertRound.getStoreId())
                .in(StoreProduct::getId, Arrays.stream(advertRound.getProdIdStr().split(",")).map(Long::parseLong).collect(Collectors.toList())));
        return storeProdList.stream().map(x -> new AdRoundSetProdResDTO().setStoreProdId(x.getId()).setProdArtNum(x.getProdArtNum())).collect(Collectors.toList());
    }

    /**
     * 获取推广类型所有轮次
     *
     * @param typeId  推广类型ID
     * @param storeId 档口ID
     * @return List<AdTypeRoundResDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public List<AdRoundTypeRoundResDTO> getTypeRoundList(Long storeId, Long typeId) {
        final LocalTime now = LocalTime.now();
        // 当前时间 是否在  晚上22:00:00  到 晚上23:59:59之间 决定 biddingStatus 和  biddingTempStatus 用那一个字段
        boolean tenClockAfter = now.isAfter(LocalTime.of(22, 0, 0)) && now.isBefore(LocalTime.of(23, 59, 59));
        // 获取当前 typeId 所有 正在投放 和 待投放的推广轮次
        List<AdvertRound> advertRoundList = this.advertRoundMapper.selectList(new LambdaQueryWrapper<AdvertRound>()
                .eq(AdvertRound::getDelFlag, Constants.UNDELETED).eq(AdvertRound::getTypeId, typeId)
                .in(AdvertRound::getLaunchStatus, Arrays.asList(AdLaunchStatus.LAUNCHING.getValue(), AdLaunchStatus.UN_LAUNCH.getValue())));
        if (CollectionUtils.isEmpty(advertRoundList)) {
            return new ArrayList<>();
        }
        // 当天
        final Date voucherDate = java.sql.Date.valueOf(LocalDate.now());
        // 第一轮结束投放时间
        final Date firstRoundEndTime = advertRoundList.stream().filter(x -> x.getRoundId().equals(AdRoundType.PLAY_ROUND.getValue()))
                .max(Comparator.comparing(AdvertRound::getEndTime))
                .orElseThrow(() -> new ServiceException("获取推广结束时间失败，请联系客服!", HttpStatus.ERROR)).getEndTime();
        // 如果当前非第一轮最后一天，则展示前3轮；如果当前是第一轮最后一天，则展示第2到第4轮。
        advertRoundList = voucherDate.before(firstRoundEndTime)
                ? advertRoundList.stream().filter(x -> !Objects.equals(x.getRoundId(), AdRoundType.FOURTH_ROUND.getValue())).collect(Collectors.toList())
                : advertRoundList.stream().filter(x -> !Objects.equals(x.getRoundId(), AdRoundType.PLAY_ROUND.getValue())).collect(Collectors.toList());
        Integer minRoundId = advertRoundList.stream().min(Comparator.comparing(AdvertRound::getRoundId))
                .orElseThrow(() -> new ServiceException("获取最小推广轮次失败，请联系客服!", HttpStatus.ERROR)).getRoundId();
        if (CollectionUtils.isEmpty(advertRoundList)) {
            return new ArrayList<>();
        }
        // 该档口在这些轮次竞价失败的记录，查询非系统拦截的竞价失败记录
        List<AdvertRoundRecord> recordList = this.advertRoundRecordMapper.selectList(new LambdaQueryWrapper<AdvertRoundRecord>()
                .eq(AdvertRoundRecord::getDelFlag, Constants.UNDELETED).eq(AdvertRoundRecord::getTypeId, typeId)
                .eq(AdvertRoundRecord::getStoreId, storeId).eq(AdvertRoundRecord::getVoucherDate, voucherDate)
                .eq(AdvertRoundRecord::getSysIntercept, AdSysInterceptType.UN_INTERCEPT.getValue())
                .in(AdvertRoundRecord::getAdvertRoundId, advertRoundList.stream().map(AdvertRound::getId).collect(Collectors.toList())));
        Map<Integer, List<AdvertRoundRecord>> roundRecordMap = recordList.stream().collect(Collectors.groupingBy(AdvertRoundRecord::getRoundId));
        List<AdRoundTypeRoundResDTO> resList = new ArrayList<>();
        final Date date = new Date();
        advertRoundList.stream().collect(Collectors.groupingBy(AdvertRound::getRoundId))
                .forEach((roundId, currentRoundList) -> {
                    // 如果有当前当前档口购买的推广轮次，则取该条记录，因为setCanPurchased会用到。反之则取第一个
                    AdvertRound advertRound = currentRoundList.stream().filter(x -> Objects.equals(x.getStoreId(), storeId))
                            .findFirst().orElse(currentRoundList.get(0));
                    // 获取档口能够购买的推广数量
                    Advert advert = redisCache.getCacheObject(CacheConstants.ADVERT_KEY + advertRound.getAdvertId());
                    if (ObjectUtils.isEmpty(advert)) {
                        advert = Optional.ofNullable(this.advertMapper.selectById(advertRound.getAdvertId()))
                                .orElseThrow(() -> new ServiceException("推广营销不存在!", HttpStatus.ERROR));
                        redisCache.setCacheObject(CacheConstants.ADVERT_KEY + advertRound.getAdvertId(), advert);
                    }
                    Integer durationDay = calculateDurationDay(advertRound.getStartTime(), advertRound.getEndTime(), Boolean.TRUE);
                    AdRoundTypeRoundResDTO typeRoundResDTO = new AdRoundTypeRoundResDTO().setAdvertId(advertRound.getAdvertId()).setRoundId(advertRound.getRoundId())
                            .setSymbol(advertRound.getSymbol()).setLaunchStatus(advertRound.getLaunchStatus()).setStartTime(advertRound.getStartTime())
                            .setEndTime(advertRound.getEndTime()).setStartWeekDay(getDayOfWeek(advertRound.getStartTime())).setDurationDay(durationDay)
                            // 设置是否可以购买当前推广位
                            .setCanPurchased(this.setRoundCanPurchased(advertRound, storeId, currentRoundList))
                            .setEndWeekDay(getDayOfWeek(advertRound.getEndTime())).setStoreBuyLimit(advert.getStoreBuyLimit())
                            .setShowType(advertRound.getShowType()).setPosition(advertRound.getPosition())
                            .setUploadDeadline(redisCache.getCacheObject(ADVERT_UPLOAD_FILTER_TIME_KEY + advertRound.getSymbol()));
                    // 如果是播放轮，则播放开始时间展示为当天，因为有可能是播放的中间某一天
                    if (Objects.equals(advertRound.getRoundId(), AdRoundType.PLAY_ROUND.getValue())) {
                        Date tomorrow = Date.from(LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
                        typeRoundResDTO.setStartTime(tomorrow).setStartWeekDay(getDayOfWeek(tomorrow))
                                // 计算最新的间隔时间（如果为最新播放轮，则展示第一轮正在播放时间与最后一天的差距）
                                .setDurationDay(calculateDurationDay(tomorrow, advertRound.getEndTime(), Boolean.TRUE));
                    }
                    // 展示类型 为时间范围 则，修改价格并显示每一轮竞价状态（位置枚举的价格是从另一个接口取的）
                    if (Objects.equals(advertRound.getShowType(), AdShowType.TIME_RANGE.getValue())) {
                        // 只有时间范围类型才显示起始价格
                        typeRoundResDTO.setStartPrice(advertRound.getStartPrice());
                        // 只有播放轮才按照时间计算折扣价
                        if (Objects.equals(advertRound.getRoundId(), AdRoundType.PLAY_ROUND.getValue())) {
                            // 根据当前日期与截止日期的占比修改推广价格
                            BigDecimal numerator = BigDecimal.valueOf(calculateDurationDay(date, advertRound.getEndTime(), Boolean.FALSE));
                            BigDecimal curStartPrice = numerator.multiply(advertRound.getStartPrice())
                                    .divide(BigDecimal.valueOf(durationDay), 0, RoundingMode.HALF_UP);
                            typeRoundResDTO.setStartPrice(curStartPrice);
                        }
                        // 当前档口购买的轮次，查询非系统拦截的购买成功的轮次
                        AdvertRound boughtRound = currentRoundList.stream().filter(x -> Objects.equals(x.getStoreId(), storeId))
                                .filter(x -> Objects.equals(x.getSysIntercept(), AdSysInterceptType.UN_INTERCEPT.getValue()))
                                .findFirst().orElse(null);
                        if (ObjectUtils.isNotEmpty(boughtRound)) {
                            // 如果是最近的播放轮次，且当前时间在 晚上10:00:01 之后到 当天23:59:59 都显示 biddingTempStatus 字段
                            Integer biddingStatus = tenClockAfter && Objects.equals(typeRoundResDTO.getRoundId(), minRoundId)
                                    ? boughtRound.getBiddingTempStatus() : boughtRound.getBiddingStatus();
                            typeRoundResDTO.setBiddingStatus(biddingStatus);
                            typeRoundResDTO.setBiddingStatusName(AdBiddingStatus.of(biddingStatus).getLabel());
                        } else {
                            // 当前档口在今天购买失败的轮次
                            List<AdvertRoundRecord> boughtFailList = roundRecordMap.get(roundId);
                            if (CollectionUtils.isNotEmpty(boughtFailList)) {
                                typeRoundResDTO.setBiddingStatus(boughtFailList.get(0).getBiddingStatus());
                                typeRoundResDTO.setBiddingStatusName(AdBiddingStatus.of(boughtFailList.get(0).getBiddingStatus()).getLabel());
                            }
                        }
                    }
                    resList.add(typeRoundResDTO);
                });
        // 按照roundId升序排
        return resList.stream().sorted(Comparator.comparing(AdRoundTypeRoundResDTO::getRoundId)).collect(Collectors.toList());
    }


    /**
     * 位置枚举的推广位档口购买情况
     *
     * @param storeId 档口ID
     * @param typeId  类型ID
     * @param roundId 轮次ID
     * @return List<AdTypeRoundBoughtResDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public List<AdRoundTypeRoundBoughtResDTO> getTypeRoundBoughtInfo(Long storeId, Long typeId, Long roundId) {
        // 档口在当前类型，当前轮次 购买的位置枚举推广位列表
        List<AdvertRound> advertRoundList = this.advertRoundMapper.selectList(new LambdaQueryWrapper<AdvertRound>()
                .eq(AdvertRound::getDelFlag, Constants.UNDELETED).eq(AdvertRound::getTypeId, typeId).eq(AdvertRound::getRoundId, roundId)
                .in(AdvertRound::getLaunchStatus, Arrays.asList(AdLaunchStatus.LAUNCHING.getValue(), AdLaunchStatus.UN_LAUNCH.getValue())));
        if (CollectionUtils.isEmpty(advertRoundList)) {
            return new ArrayList<>();
        }
        final LocalTime now = LocalTime.now();
        // 当前时间 是否在  晚上22:00:00  到 晚上23:59:59之间 决定 biddingStatus 和  biddingTempStatus 用那一个字段
        boolean tenClockAfter = now.isAfter(LocalTime.of(22, 0, 0)) && now.isBefore(LocalTime.of(23, 59, 59));
        final Date date = new Date();
        List<AdRoundTypeRoundBoughtResDTO> resList = advertRoundList.stream().map(advertRound -> {
            AdRoundTypeRoundBoughtResDTO boughtResDTO = new AdRoundTypeRoundBoughtResDTO().setTypeId(advertRound.getTypeId()).setAdvertRoundId(advertRound.getId())
                    .setAdvertId(advertRound.getAdvertId()).setRoundId(advertRound.getRoundId()).setPosition(advertRound.getPosition())
                    // 设置是否可以购买当前推广位
                    .setCanPurchased(this.setRoundCanPurchased(advertRound, storeId, Collections.singletonList(advertRound)))
                    .setStartPrice(advertRound.getStartPrice()).setPayPrice(advertRound.getPayPrice()).setStoreId(storeId).setLaunchStatus(advertRound.getLaunchStatus())
                    .setStartTime(advertRound.getStartTime()).setEndTime(advertRound.getEndTime()).setSymbol(advertRound.getSymbol())
                    .setUploadDeadline(redisCache.getCacheObject(ADVERT_UPLOAD_FILTER_TIME_KEY + advertRound.getSymbol()));
            // 如果当前位置没有档口购买，且为第一轮 则需按照剩余时间比例进行减价
            if (ObjectUtils.isEmpty(advertRound.getStoreId()) && Objects.equals(advertRound.getRoundId(), AdRoundType.PLAY_ROUND.getValue())) {
                Integer durationDay = calculateDurationDay(advertRound.getStartTime(), advertRound.getEndTime(), Boolean.TRUE);
                // 根据当前日期与截止日期的占比修改推广价格
                BigDecimal numerator = BigDecimal.valueOf(calculateDurationDay(date, advertRound.getEndTime(), Boolean.FALSE));
                BigDecimal curStartPrice = numerator.multiply(advertRound.getStartPrice())
                        .divide(BigDecimal.valueOf(durationDay), 0, RoundingMode.HALF_UP);
                // 播放轮开始时间需要调整
                Date tomorrow = Date.from(LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
                boughtResDTO.setStartPrice(curStartPrice).setStartTime(tomorrow);
            }
            // 当前档口购买的推广位置 必须为非系统拦截的广告位
            if (Objects.equals(advertRound.getStoreId(), storeId) && Objects.equals(advertRound.getSysIntercept(), AdSysInterceptType.UN_INTERCEPT.getValue())) {
                // 为22:00之后，且当前播放轮或明天播放轮次才显示 biddingTempStatus 字段
                LocalDate advertRoundYesterday = advertRound.getStartTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().minusDays(1);
                boolean isTLaunch = advertRound.getRoundId() == 1 || (LocalDate.now().equals(advertRoundYesterday));
                Integer biddingStatus = tenClockAfter && isTLaunch ? advertRound.getBiddingTempStatus() : advertRound.getBiddingStatus();
                boughtResDTO.setBiddingStatus(biddingStatus);
                boughtResDTO.setBiddingStatusName(AdBiddingStatus.of(biddingStatus).getLabel());
            }
            return boughtResDTO;
        }).collect(Collectors.toList());
        // 档口未竞价成功的位置
        List<String> unBoughtPositionList = advertRoundList.stream().filter(x -> ObjectUtils.isNotEmpty(x.getStoreId()) && !Objects.equals(x.getStoreId(), storeId))
                .map(AdvertRound::getPosition).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(unBoughtPositionList)) {
            return resList;
        }
        // 当天竞价失败的位置，必须是非系统拦截的广告位
        List<AdvertRoundRecord> recordList = this.advertRoundRecordMapper.selectList(new LambdaQueryWrapper<AdvertRoundRecord>()
                .eq(AdvertRoundRecord::getDelFlag, Constants.UNDELETED).eq(AdvertRoundRecord::getTypeId, typeId)
                .eq(AdvertRoundRecord::getRoundId, roundId).eq(AdvertRoundRecord::getStoreId, storeId)
                .eq(AdvertRoundRecord::getVoucherDate, java.sql.Date.valueOf(LocalDate.now()))
                .eq(AdvertRoundRecord::getSysIntercept, AdSysInterceptType.UN_INTERCEPT.getValue())
                .in(AdvertRoundRecord::getPosition, unBoughtPositionList));
        if (CollectionUtils.isEmpty(recordList)) {
            return resList;
        }
        // 竞价失败的位置
        Map<Long, Integer> bindingFailMap = recordList.stream().collect(Collectors
                .toMap(AdvertRoundRecord::getAdvertRoundId, AdvertRoundRecord::getBiddingStatus, (s1, s2) -> s2));
        resList.forEach(x -> {
            Integer biddingStatus = bindingFailMap.get(x.getAdvertRoundId());
            if (ObjectUtils.isNotEmpty(biddingStatus)) {
                x.setBiddingStatus(biddingStatus);
                x.setBiddingStatusName(AdBiddingStatus.of(biddingStatus).getLabel());
            }
        });
        return resList;
    }

    /**
     * 档口购买推广轮次列表
     *
     * @param storeId 档口ID
     * @return List<AdRoundStoreBoughtResDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public List<AdRoundStoreBoughtResDTO> getStoreBoughtRecord(Long storeId) {
        final LocalTime now = LocalTime.now();
        // 当前时间 是否在  晚上22:00:00  到 晚上23:59:59之间 决定 biddingStatus 和  biddingTempStatus 用那一个字段
        boolean tenClockAfter = now.isAfter(LocalTime.of(22, 0, 0)) && now.isBefore(LocalTime.of(23, 59, 59));
        // 当天
        final Date voucherDate = java.sql.Date.valueOf(LocalDate.now());
        // 已出价 和 竞价成功
        final List<Integer> canPurchasedStatusList = Arrays.asList(AdBiddingStatus.BIDDING_SUCCESS.getValue(), AdBiddingStatus.BIDDING.getValue());
        final ZoneId zoneId = ZoneId.systemDefault();
        // 获取当前所有 正在投放 和 待投放的推广轮次
        List<AdvertRound> allRoundList = this.advertRoundMapper.selectList(new LambdaQueryWrapper<AdvertRound>().eq(AdvertRound::getDelFlag, Constants.UNDELETED)
                .in(AdvertRound::getLaunchStatus, Arrays.asList(AdLaunchStatus.LAUNCHING.getValue(), AdLaunchStatus.UN_LAUNCH.getValue())));
        if (CollectionUtils.isEmpty(allRoundList)) {
            return new ArrayList<>();
        }
        // 当前 档口 在所有 待投放  及 投放中 的推广轮次竞价失败记录. 查询非系统拦截的推广位
        List<AdvertRoundRecord> allRecordList = this.advertRoundRecordMapper.selectList(new LambdaQueryWrapper<AdvertRoundRecord>()
                .eq(AdvertRoundRecord::getDelFlag, Constants.UNDELETED).eq(AdvertRoundRecord::getStoreId, storeId)
                .eq(AdvertRoundRecord::getVoucherDate, voucherDate).eq(AdvertRoundRecord::getSysIntercept, AdSysInterceptType.UN_INTERCEPT.getValue()));
        // 按照advertId进行分组，取最小的roundId列表
        Map<Long, Optional<Integer>> minRoundIdMap = allRoundList.stream().collect(Collectors.groupingBy(AdvertRound::getAdvertId,
                Collectors.mapping(AdvertRound::getRoundId, Collectors.minBy(Comparator.comparing(Integer::intValue)))));
        // 最小的roundId列表
        List<Integer> roundIdList = minRoundIdMap.values().stream().filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
        // 筛选档口 所有的 已购买 推广位数据
        List<AdRoundStoreBoughtResDTO> boughtRoundList = allRoundList.stream().filter(x -> Objects.equals(x.getStoreId(), storeId))
                // 查询非系统拦截的推广位
                .filter(x -> Objects.equals(x.getSysIntercept(), AdSysInterceptType.UN_INTERCEPT.getValue()))
                .map(x -> {
                    // 如果是最近的播放轮次，且当前时间在 晚上10:00:01 之后到 当天23:59:59 都显示 biddingTempStatus 字段
                    final Integer biddingStatus = tenClockAfter && roundIdList.contains(x.getRoundId()) ? x.getBiddingTempStatus() : x.getBiddingStatus();
                    AdRoundStoreBoughtResDTO boughtResDTO = BeanUtil.toBean(x, AdRoundStoreBoughtResDTO.class).setAdvertRoundId(x.getId())
                            .setBiddingStatus(biddingStatus).setLaunchStatus(x.getLaunchStatus()).setActivePlay(Boolean.FALSE)
                            // 设置当前档口是否可以购买
                            .setCanPurchased(this.setBoughtRecordCanPurchased(x, canPurchasedStatusList))
                            // 如果是已出价，则显示 "已出价:50"
                            .setBiddingStatusName(AdBiddingStatus.of(biddingStatus).getLabel() +
                                    (Objects.equals(biddingStatus, AdBiddingStatus.BIDDING.getValue()) ? ":" + x.getPayPrice() : ""))
                            .setTypeName(AdType.of(x.getTypeId()).getLabel())
                            // 如果是时间范围则不返回position
                            .setPosition(Objects.equals(x.getShowType(), AdShowType.TIME_RANGE.getValue()) ? null : x.getPosition());
                    // 当前为播放轮 或 当前为第二轮且开始时间为明天
                    if (Objects.equals(x.getRoundId(), AdRoundType.PLAY_ROUND.getValue())) {
                        boughtResDTO.setActivePlay(Boolean.TRUE);
                        // 如果是第二轮且开始时间为明天
                    } else if (Objects.equals(x.getRoundId(), AdRoundType.SECOND_ROUND.getValue())) {
                        LocalDate startTimeDate = x.getStartTime().toInstant().atZone(zoneId).toLocalDate();
                        if (LocalDate.now().plusDays(1).equals(startTimeDate)) {
                            boughtResDTO.setActivePlay(Boolean.TRUE);
                        }
                    }
                    return boughtResDTO;
                })
                .collect(Collectors.toList());
        // showType 为 时间范围的 每一轮最高的出价map
        Map<Integer, BigDecimal> timeRangeRoundMaxPriceMap = allRoundList.stream()
                .filter(x -> Objects.equals(x.getShowType(), AdShowType.TIME_RANGE.getValue()))
                .filter(x -> ObjectUtils.isNotEmpty(x.getPayPrice()))
                .collect(Collectors.groupingBy(AdvertRound::getRoundId, Collectors
                        .mapping(AdvertRound::getPayPrice, Collectors.reducing(BigDecimal.ZERO, BigDecimal::max))));
        // showType 为 位置枚举的 每一个位置最高出价的 map
        Map<Long, BigDecimal> positionEnumMaxPriceMap = allRoundList.stream()
                .filter(x -> Objects.equals(x.getShowType(), AdShowType.POSITION_ENUM.getValue()))
                .filter(x -> ObjectUtils.isNotEmpty(x.getPayPrice()))
                .collect(Collectors.toMap(AdvertRound::getId, AdvertRound::getPayPrice));
        // 已购买的 时间范围播放轮次 的 typeId 和 roundId 列表
        final List<String> boughtTimeRangeMultiKeyList = boughtRoundList.stream().filter(x -> Objects.equals(x.getShowType(), AdShowType.TIME_RANGE.getValue()))
                .map(x -> x.getTypeId().toString() + ":" + x.getRoundId().toString()).collect(Collectors.toList());
        // 已购买的  位置枚举 的 advertRoundId 列表
        final List<Long> boughtPositionAdvertRoundIdList = boughtRoundList.stream().filter(x -> Objects.equals(x.getShowType(), AdShowType.POSITION_ENUM.getValue()))
                .map(AdRoundStoreBoughtResDTO::getAdvertRoundId).collect(Collectors.toList());
        // 购买失败的 时间范围播放轮次的  列表
        Map<Integer, AdvertRoundRecord> boughtFailTimeRangeMap = allRecordList.stream()
                .filter(x -> Objects.equals(x.getShowType(), AdShowType.TIME_RANGE.getValue()))
                .filter(x -> !boughtTimeRangeMultiKeyList.contains(x.getTypeId().toString() + ":" + x.getRoundId().toString()))
                .collect(Collectors.toMap(AdvertRoundRecord::getRoundId, Function.identity(),
                        BinaryOperator.maxBy(Comparator.comparingLong(AdvertRoundRecord::getId))));
        // 购买失败的 位置枚举播放轮次的 列表
        Map<Long, AdvertRoundRecord> boughtFailPositionMap = allRecordList.stream()
                .filter(x -> Objects.equals(x.getShowType(), AdShowType.POSITION_ENUM.getValue()))
                .filter(x -> !boughtPositionAdvertRoundIdList.contains(x.getAdvertRoundId()))
                .collect(Collectors.toMap(AdvertRoundRecord::getAdvertRoundId, Function.identity(),
                        BinaryOperator.maxBy(Comparator.comparingLong(AdvertRoundRecord::getId))));
        if (MapUtils.isNotEmpty(boughtFailTimeRangeMap)) {
            boughtFailTimeRangeMap.forEach((roundId, record) -> {
                boughtRoundList.add(BeanUtil.toBean(record, AdRoundStoreBoughtResDTO.class).setPosition(null)
                        .setTypeName(AdType.of(record.getTypeId()).getLabel())
                        // 设置是否能购买推广位
                        .setCanPurchased(DateUtils.getTime().compareTo(this.getDeadline(record.getSymbol())) > 0 ? Boolean.FALSE : Boolean.TRUE)
                        .setBiddingStatusName(AdBiddingStatus.of(record.getBiddingStatus()).getLabel()
                                + "，最新出价:" + timeRangeRoundMaxPriceMap.get(record.getRoundId())));
            });
        }
        if (MapUtils.isNotEmpty(boughtFailPositionMap)) {
            boughtFailPositionMap.forEach((advertRoundId, record) -> {
                boughtRoundList.add(BeanUtil.toBean(record, AdRoundStoreBoughtResDTO.class)
                        .setTypeName(AdType.of(record.getTypeId()).getLabel())
                        // 设置是否能购买推广位
                        .setCanPurchased(DateUtils.getTime().compareTo(this.getDeadline(record.getSymbol())) > 0 ? Boolean.FALSE : Boolean.TRUE)
                        .setBiddingStatusName(AdBiddingStatus.of(record.getBiddingStatus()).getLabel()
                                + "，最新出价:" + positionEnumMaxPriceMap.get(record.getAdvertRoundId())));
            });
        }
        // 按照播放时间升序排列
        boughtRoundList.sort(Comparator.comparing(AdRoundStoreBoughtResDTO::getStartTime));
        return boughtRoundList;
    }

    /**
     * 根据推广位置ID及档口ID 获取 设置的推广商品或图片
     *
     * @param advertRoundId 推广位置ID
     * @return AdRoundLatestResDTO
     */
    @Override
    @Transactional(readOnly = true)
    public AdRoundLatestResDTO getSetInfo(Long advertRoundId) {
        AdvertRound advertRound = this.advertRoundMapper.selectOne(new LambdaQueryWrapper<AdvertRound>()
                .eq(AdvertRound::getId, advertRoundId).eq(AdvertRound::getDelFlag, Constants.UNDELETED));
        AdRoundLatestResDTO roundSetInfoDTO = new AdRoundLatestResDTO();
        if (ObjectUtils.isEmpty(advertRound) || ObjectUtils.isEmpty(advertRound.getStoreId())) {
            return roundSetInfoDTO;
        }
        if (StringUtils.isNotBlank(advertRound.getProdIdStr())) {
            List<StoreProduct> storeProdList = Optional.ofNullable(this.storeProdMapper.selectList(new LambdaQueryWrapper<StoreProduct>()
                            .in(StoreProduct::getId, StrUtil.split(advertRound.getProdIdStr(), ","))
                            .eq(StoreProduct::getDelFlag, Constants.UNDELETED).eq(StoreProduct::getStoreId, advertRound.getStoreId())))
                    .orElseThrow(() -> new ServiceException("档口商品不存在!", HttpStatus.ERROR));
            return roundSetInfoDTO.setProdList(storeProdList.stream().map(x -> new AdRoundLatestResDTO.ARLProdDTO()
                    .setStoreProdId(x.getId()).setProdArtNum(x.getProdArtNum())).collect(Collectors.toList()));
        }
        return roundSetInfoDTO;
    }


    /**
     * 档口购买推广营销
     * 主要是两个场景：1. 某个广告位（advert_id）某个轮次（round_id）按照出价（pay_price）决定能否购买。[eg: A B C D E]
     * 2. 某个广告位（advert_id）某个轮次（round_id）某个具体位置（position）按照出价（pay_price）决定能否购买。
     * 思路：每次筛选出某个类型，价格最低的推广位，然后只操作这行数据
     * （创建索引：CREATE INDEX idx_advert_round_pay_pos ON advert_round (advert_id, round_id, pay_price, position)）。
     * 若：该行数据已经有其它档口先竞价了。先进行比价。如果出价低于最低价格，则抛出异常：“已经有档口出价更高了噢，请重新出价!”
     * 若：新出价比原数据价格高，则：a. 给原数据档口创建转移支付单   b.新档口占据该行位置，更新数据。
     * <p>
     * 等到晚上10:00，所有档口购买完成。再通过定时任务（11:30），给每个类型，按照价格从高到低，进行排序。并将首页各个位置的广告数据推送到ES中。为空的广告位如何填充？？
     * <p>
     *
     * @param createDTO 购买入参
     * @return Integer
     */
    @Override
    @Transactional
    public Integer create(AdRoundStoreCreateDTO createDTO) {
        Store store = redisCache.getCacheObject(CacheConstants.STORE_KEY + createDTO.getStoreId());
        if (ObjectUtils.isEmpty(store)) {
            store = Optional.ofNullable(this.storeMapper.selectById(createDTO.getStoreId())).orElseThrow(() -> new ServiceException("档口不存在!", HttpStatus.ERROR));
            redisCache.setCacheObject(CacheConstants.STORE_KEY + createDTO.getStoreId(), store);
        }
        Advert advert = redisCache.getCacheObject(CacheConstants.ADVERT_KEY + createDTO.getAdvertId());
        if (ObjectUtils.isEmpty(advert)) {
            advert = Optional.ofNullable(this.advertMapper.selectById(createDTO.getAdvertId()))
                    .orElseThrow(() -> new ServiceException("推广营销不存在!", HttpStatus.ERROR));
            redisCache.setCacheObject(CacheConstants.ADVERT_KEY + createDTO.getAdvertId(), advert);
        }
        // 购买推广前置校验
        this.advertPreBuyCheck(createDTO);
        // 当前营销推广位的锁
        Object lockObj = Optional.ofNullable(advertLockMap.get(createDTO.getSymbol())).orElseThrow(() -> new ServiceException("symbol不存在!", HttpStatus.ERROR));
        synchronized (lockObj) {
            LambdaQueryWrapper<AdvertRound> queryWrapper = new LambdaQueryWrapper<AdvertRound>()
                    .eq(AdvertRound::getAdvertId, createDTO.getAdvertId()).eq(AdvertRound::getRoundId, createDTO.getRoundId())
                    .in(AdvertRound::getLaunchStatus, LAUNCHING_STATUS_LIST).in(AdvertRound::getBiddingStatus, BIDDING_STATUS_LIST)
                    .eq(AdvertRound::getDelFlag, Constants.UNDELETED).orderByAsc(AdvertRound::getPayPrice).last("LIMIT 1");
            // 如果是 时间范围 的广告位，则不查 系统拦截的数据；其实位置枚举的广告位也可不用查系统拦截的数据，主要是为了真实，给档口留个缝
            if (Objects.equals(createDTO.getShowType(), AdShowType.TIME_RANGE.getValue())) {
                queryWrapper.eq(AdvertRound::getSysIntercept, AdSysInterceptType.UN_INTERCEPT.getValue());
            }
            // 位置枚举类型的广告位，则 需要 查询具体的位置
            if (StringUtils.isNotBlank(createDTO.getPosition())) {
                queryWrapper.eq(AdvertRound::getPosition, createDTO.getPosition());
            }
            // 是否有可供购买的广告位
            AdvertRound minPriceAdvert = this.advertRoundMapper.selectOne(queryWrapper);
            if (ObjectUtils.isEmpty(minPriceAdvert)) {
                throw new ServiceException("当前推广位已售罄，请选购其它推广位噢!", HttpStatus.ERROR);
            }
            // 判断当前档口出价是否低于该推广位最低价格，若是，则提示:"已经有档口出价更高了噢，请重新出价!".
            if (createDTO.getPayPrice().compareTo(ObjectUtils.defaultIfNull(minPriceAdvert.getPayPrice(), BigDecimal.ZERO)) <= 0) {
                throw new ServiceException("已经有档口出价更高了噢，请重新出价!", HttpStatus.ERROR);
            }
            final String position = Objects.equals(minPriceAdvert.getShowType(), AdShowType.TIME_RANGE.getValue()) ? "" : minPriceAdvert.getPosition();
            // storeId不为空，表明之前有其他的档口竞价
            if (ObjectUtils.isNotEmpty(minPriceAdvert.getStoreId())) {
                // 将推广费退至原档口余额
                assetService.refundAdvertFee(minPriceAdvert.getStoreId(), minPriceAdvert.getPayPrice());
                // 记录竞价失败的档口推广营销
                this.record(minPriceAdvert);
                final String failTitle = ObjectUtils.isNotEmpty(advert) ? AdType.of(advert.getTypeId()).getLabel() : "" + position + "被抢占!";
                final String failContent = "您抢购的 " + (ObjectUtils.isNotEmpty(advert) ? AdType.of(advert.getTypeId()).getLabel() : "") + position +
                        " 被其它档口抢拍，订购金额已退回余额!您可在抢购结束前加价抢回噢!";
                // 新增竞价失败档口消息通知
                this.noticeService.createSingleNotice(SecurityUtils.getUserId(), failTitle, NoticeType.NOTICE.getValue(), NoticeOwnerType.SYSTEM.getValue(),
                        minPriceAdvert.getStoreId(), UserNoticeType.ADVERT.getValue(), failContent);
            }
            // 更新广告位数据
            minPriceAdvert
                    // 档口购买的推广位一律设置为 非拦截
                    .setSysIntercept(AdSysInterceptType.UN_INTERCEPT.getValue()).setStyleType(createDTO.getStyleType())
                    .setStoreId(createDTO.getStoreId()).setPayPrice(createDTO.getPayPrice()).setVoucherDate(java.sql.Date.valueOf(LocalDate.now()))
                    .setBiddingStatus(AdBiddingStatus.BIDDING.getValue()).setBiddingTempStatus(AdBiddingStatus.BIDDING_SUCCESS.getValue())
                    .setPicSetType(this.hasPic(minPriceAdvert.getDisplayType()) ? AdPicSetType.UN_SET.getValue() : null)
                    .setPicAuditStatus(this.hasPic(minPriceAdvert.getDisplayType()) ? AdPicAuditStatus.UN_AUDIT.getValue() : null)
                    .setPicDesignType(this.hasPic(minPriceAdvert.getDisplayType()) ? createDTO.getPicDesignType() : null)
                    .setProdIdStr(createDTO.getProdIdStr()).setCreateBy(SecurityUtils.getUsernameSafe());
            this.advertRoundMapper.updateById(minPriceAdvert);
            final String successTitle = ObjectUtils.isNotEmpty(advert) ? AdType.of(advert.getTypeId()).getLabel() : "" + position + "订购成功!";
            final String successContent = ObjectUtils.isNotEmpty(store) ? store.getStoreName() : "" + " ，恭喜您!您成功订购 "
                    + (ObjectUtils.isNotEmpty(advert) ? AdType.of(advert.getTypeId()).getLabel() : "") + position +
                    "播放时间段为" + minPriceAdvert.getStartTime() + "至" + minPriceAdvert.getEndTime() + "，请确保推广设置正确噢!";
            // 新增订购成功的消息通知
            this.noticeService.createSingleNotice(SecurityUtils.getUserId(), successTitle, NoticeType.NOTICE.getValue(), NoticeOwnerType.SYSTEM.getValue(),
                    minPriceAdvert.getStoreId(), UserNoticeType.ADVERT.getValue(), successContent);
            // 扣除推广费
            assetService.payAdvertFee(createDTO.getStoreId(), createDTO.getPayPrice());
        }
        return 1;
    }


    /**
     * 档口已订购推广列表
     *
     * @param pageDTO 分页入参
     * @return Page<AdvertRoundStorePageResDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AdvertRoundStorePageResDTO> page(AdvertRoundStorePageDTO pageDTO) {
        // 用户是否为档口管理者或子账户
        if (!SecurityUtils.isAdmin() && !SecurityUtils.isStoreManagerOrSub(pageDTO.getStoreId())) {
            throw new ServiceException("当前用户非档口管理者或子账号，无权限操作!", HttpStatus.ERROR);
        }
        PageHelper.startPage(pageDTO.getPageNum(), pageDTO.getPageSize());
        // 过滤掉系统管理员拦截的推广
        List<AdvertRoundStorePageResDTO> list = this.advertRoundMapper.selectStoreAdvertPage(pageDTO);
        List<String> idList = list.stream().map(AdvertRoundStorePageResDTO::getProdIdStr)
                .filter(StringUtils::isNotBlank).flatMap(str -> StrUtil.split(str, ",").stream())
                .distinct().collect(Collectors.toList());
        Map<String, StoreProduct> storeProdMap = CollectionUtils.isEmpty(idList) ? new HashMap<>()
                : this.storeProdMapper.selectByIds(idList).stream().collect(Collectors.toMap(x -> x.getId().toString(), x -> x));
        list.forEach(item -> {
            final List<String> prodArtNumList = StringUtils.isEmpty(item.getProdIdStr()) ? new ArrayList<>() : StrUtil.split(item.getProdIdStr(), ",").stream()
                    .map(storeProdMap::get).filter(Objects::nonNull).map(StoreProduct::getProdArtNum).collect(Collectors.toList());
            item.setPlatformName(AdPlatformType.of(item.getPlatformId()).getLabel())
                    .setLaunchStatusName(AdLaunchStatus.of(item.getLaunchStatus()).getLabel())
                    .setPicAuditStatusName(ObjectUtils.isNotEmpty(item.getPicAuditStatus()) ? AdPicAuditStatus.of(item.getPicAuditStatus()).getLabel() : "")
                    .setPicDesignTypeName(ObjectUtils.isNotEmpty(item.getPicDesignType()) ? AdDesignType.of(item.getPicDesignType()).getLabel() : "")
                    .setPicAuditStatusName(ObjectUtils.isNotEmpty(item.getPicAuditStatus()) ? AdPicAuditStatus.of(item.getPicAuditStatus()).getLabel() : "")
                    .setPicSetTypeName(ObjectUtils.isNotEmpty(item.getPicSetType()) ? AdPicSetType.of(item.getPicSetType()).getLabel() : "")
                    .setTypeName(AdType.of(item.getTypeId()).getLabel())
                    .setBiddingStatusName(AdBiddingStatus.of(item.getBiddingStatus()).getLabel())
                    .setProdArtNumList(prodArtNumList);
        });
        return Page.convert(new PageInfo<>(list));
    }

    /**
     * 获取当前档口设置的推广位信息
     *
     * @param storeId       档口ID
     * @param advertRoundId 推广位ID
     * @return AdRoundSetPicResDTO
     */
    @Override
    @Transactional(readOnly = true)
    public AdRoundStoreSetResDTO getAdvertStoreSetInfo(Long storeId, Long advertRoundId) {
        AdvertRound advertRound = Optional.ofNullable(this.advertRoundMapper.selectOne(new LambdaQueryWrapper<AdvertRound>()
                        .eq(AdvertRound::getId, advertRoundId).eq(AdvertRound::getDelFlag, Constants.UNDELETED)
                        .eq(AdvertRound::getStoreId, storeId)))
                .orElseThrow(() -> new ServiceException("档口购买的推广位不存在!", HttpStatus.ERROR));
        AdRoundStoreSetResDTO storeSet = new AdRoundStoreSetResDTO().setAdvertRoundId(advertRoundId);
        // 获取档口设置的推广图
        if (ObjectUtils.isNotEmpty(advertRound.getPicId())) {
            SysFile file = Optional.ofNullable(this.fileMapper.selectOne(new LambdaQueryWrapper<SysFile>()
                            .eq(SysFile::getId, advertRound.getPicId()).eq(SysFile::getDelFlag, Constants.UNDELETED)))
                    .orElseThrow(() -> new ServiceException("推广图不存在", HttpStatus.ERROR));
            storeSet.setFileUrl(file.getFileUrl());
        }
        // 获取档口设置的推广商品
        if (StringUtils.isNotBlank(advertRound.getProdIdStr())) {
            List<StoreProduct> storeProdList = Optional.ofNullable(this.storeProdMapper.selectList(new LambdaQueryWrapper<StoreProduct>()
                            .eq(StoreProduct::getStoreId, storeId).eq(StoreProduct::getDelFlag, Constants.UNDELETED)
                            .in(StoreProduct::getId, Arrays.stream(advertRound.getProdIdStr().split(",")).map(Long::parseLong).collect(Collectors.toList()))))
                    .orElseThrow(() -> new ServiceException("档口设置的推广商品不存在", HttpStatus.ERROR));
            storeSet.setProdList(storeProdList.stream().map(x -> new AdRoundStoreSetResDTO.ARSSProdDTO()
                    .setStoreProdId(x.getId()).setProdArtNum(x.getProdArtNum())).collect(Collectors.toList()));
        }
        return storeSet;
    }

    /**
     * 档口退订推广位
     *
     * @param storeId       档口ID
     * @param advertRoundId 推广位ID
     * @return
     */
    @Override
    @Transactional
    public Integer unsubscribe(Long storeId, Long advertRoundId) {
        // 用户是否为档口管理者或子账户
        if (!SecurityUtils.isAdmin() && !SecurityUtils.isStoreManagerOrSub(storeId)) {
            throw new ServiceException("当前用户非档口管理者或子账号，无权限操作!", HttpStatus.ERROR);
        }
        AdvertRound advertRound = Optional.ofNullable(this.advertRoundMapper.selectOne(new LambdaQueryWrapper<AdvertRound>()
                        .eq(AdvertRound::getId, advertRoundId).eq(AdvertRound::getDelFlag, Constants.UNDELETED)
                        .eq(AdvertRound::getStoreId, storeId)))
                .orElseThrow(() -> new ServiceException("档口购买的推广位不存在!", HttpStatus.ERROR));
        // 判断当前时间距离开播是否小于72h，若是：则不可取消
        Date threeDaysAfter = DateUtils.toDate(LocalDateTime.now().plusDays(3));
        if (threeDaysAfter.after(advertRound.getStartTime())) {
            throw new ServiceException("距推广开播小于72小时，不可退订噢!");
        }
        // 将费用退回到档口余额中
        assetService.refundAdvertFee(advertRound.getStoreId(), advertRound.getPayPrice());
        // 将推广位置为空
        return this.advertRoundMapper.updateAttrNull(advertRound.getId());
    }

    /**
     * 获取最受欢迎8个推广位
     *
     * @return List<AdRoundPopularResDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public List<AdRoundPopularResDTO> getMostPopulars() {
        // 从redis中获取最受欢迎的8个推广位
        List<AdRoundPopularResDTO> redisList = redisCache.getCacheObject(ADVERT_POPULAR);
        if (CollectionUtils.isNotEmpty(redisList)) {
            return redisList;
        }
        // 找到有人购买的最受欢迎的advertId列表
        List<Long> advertIdList = this.advertRoundMapper.selectMostPopulars();
        if (CollectionUtils.isEmpty(advertIdList)) {
            return new ArrayList<>();
        }
        // 获取下一轮待投放的推广位
        List<AdvertRound> advertRoundList = this.advertRoundMapper.selectList(new LambdaQueryWrapper<AdvertRound>()
                .in(AdvertRound::getAdvertId, advertIdList).eq(AdvertRound::getDelFlag, Constants.UNDELETED)
                .eq(AdvertRound::getLaunchStatus, AdLaunchStatus.UN_LAUNCH.getValue()));
        // 找到播放轮次最近的数据
        Map<Long, Optional<AdvertRound>> minRoundIdMap = advertRoundList.stream().collect(Collectors
                .groupingBy(AdvertRound::getAdvertId, Collectors.minBy(Comparator.comparing(AdvertRound::getRoundId))));
        // 将minRoundIdMap中的值转换为List<AdRoundPopularResDTO>
        List<AdRoundPopularResDTO> list = minRoundIdMap.values().stream().map(Optional::get).map(x -> new AdRoundPopularResDTO().setAdvertId(x.getAdvertId())
                        .setTypeId(x.getTypeId()).setTypeName(AdType.of(x.getTypeId()).getLabel()).setShowType(x.getShowType())
                        .setStartTime(DateUtils.timeMMDD(x.getStartTime())).setEndTime(DateUtils.timeMMDD(x.getEndTime()))
                        .setStartPrice(this.getAvgStartPrice(x.getStartTime(), x.getEndTime(), x.getStartPrice())))
                .collect(Collectors.toList());
        // 存到redis中
        redisCache.setCacheObject(ADVERT_POPULAR, list, 1, TimeUnit.DAYS);
        return list;
    }


    /**
     * 获取当前推广位最高的价格及档口设置的商品
     *
     * @param latestDTO 查询入参
     * @return AdRoundLatestResDTO
     */
    @Override
    @Transactional(readOnly = true)
    public AdRoundLatestResDTO getLatestInfo(AdRoundLatestDTO latestDTO) {
        AdvertRound advertRound;
        AdRoundLatestResDTO latestInfo = new AdRoundLatestResDTO();
        // 时间范围类型
        if (Objects.equals(latestDTO.getShowType(), AdShowType.TIME_RANGE.getValue())) {
            advertRound = this.advertRoundMapper.selectOne(new LambdaQueryWrapper<AdvertRound>()
                    .eq(AdvertRound::getAdvertId, latestDTO.getAdvertId()).eq(AdvertRound::getRoundId, latestDTO.getRoundId())
                    .eq(AdvertRound::getLaunchStatus, latestDTO.getLaunchStatus()).eq(AdvertRound::getSymbol, latestDTO.getSymbol())
                    .eq(AdvertRound::getDelFlag, Constants.UNDELETED).orderByDesc(AdvertRound::getPayPrice, AdvertRound::getCreateTime)
                    .last("LIMIT 1"));
        } else {
            Optional.ofNullable(latestDTO.getPosition()).orElseThrow(() -> new ServiceException("位置枚举类型：position必传", HttpStatus.ERROR));
            advertRound = Optional.ofNullable(this.advertRoundMapper.selectOne(new LambdaQueryWrapper<AdvertRound>()
                            .eq(AdvertRound::getAdvertId, latestDTO.getAdvertId()).eq(AdvertRound::getRoundId, latestDTO.getRoundId())
                            .eq(AdvertRound::getLaunchStatus, latestDTO.getLaunchStatus()).eq(AdvertRound::getSymbol, latestDTO.getSymbol())
                            .eq(AdvertRound::getPosition, latestDTO.getPosition()).eq(AdvertRound::getDelFlag, Constants.UNDELETED)))
                    .orElseThrow(() -> new ServiceException("推广位不存在!", HttpStatus.ERROR));
        }
        // 档口购买时间
        latestInfo.setUpdateTime(advertRound.getUpdateTime());
        // 有人竞拍情况，才获取该位置的档口负责人名称
        if (ObjectUtils.isNotEmpty(advertRound.getStoreId())) {
            Store store = Optional.ofNullable(this.storeMapper.selectOne(new LambdaQueryWrapper<Store>()
                            .eq(Store::getId, advertRound.getStoreId()).eq(Store::getDelFlag, Constants.UNDELETED)))
                    .orElseThrow(() -> new ServiceException("档口不存在!", HttpStatus.ERROR));
            latestInfo.setStoreId(advertRound.getStoreId()).setPayPrice(advertRound.getPayPrice())
                    // 对手机号中间位数进行*号替换处理
                    .setContactPhone(this.maskPhoneNum(store.getContactPhone()));
        }
        // 如果当前档口购买该推广位，且设置商品不为空，则返回设置的商品信息
        if (Objects.equals(advertRound.getStoreId(), latestDTO.getStoreId()) && StringUtils.isNotBlank(advertRound.getProdIdStr())) {
            List<StoreProduct> storeProdList = Optional.ofNullable(this.storeProdMapper.selectList(new LambdaQueryWrapper<StoreProduct>()
                            .in(StoreProduct::getId, StrUtil.split(advertRound.getProdIdStr(), ","))
                            .eq(StoreProduct::getDelFlag, Constants.UNDELETED).eq(StoreProduct::getStoreId, advertRound.getStoreId())))
                    .orElseThrow(() -> new ServiceException("档口商品不存在!", HttpStatus.ERROR));
            latestInfo.setProdList(storeProdList.stream().map(x -> new AdRoundLatestResDTO.ARLProdDTO()
                    .setStoreProdId(x.getId()).setProdArtNum(x.getProdArtNum())).collect(Collectors.toList()));
        }
        return latestInfo;
    }


    /**
     * @param picDTO 档口上传推广图 或 修改商品
     * @return Integer
     */
    @Override
    @Transactional
    public Integer updateAdvert(AdRoundUpdateDTO picDTO) {
        // 用户是否为档口管理者或子账户
        if (!SecurityUtils.isAdmin() && !SecurityUtils.isStoreManagerOrSub(picDTO.getStoreId())) {
            throw new ServiceException("当前用户非档口管理者或子账号，无权限操作!", HttpStatus.ERROR);
        }
        AdvertRound advertRound = Optional.ofNullable(this.advertRoundMapper.selectOne(new LambdaQueryWrapper<AdvertRound>()
                        .eq(AdvertRound::getId, picDTO.getAdvertRoundId()).eq(AdvertRound::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("推广位不存在!", HttpStatus.ERROR));
        // 已过期、已退订，则不可修改
        if (Objects.equals(advertRound.getLaunchStatus(), AdLaunchStatus.EXPIRED.getValue())
                || Objects.equals(advertRound.getLaunchStatus(), AdLaunchStatus.CANCEL.getValue())) {
            throw new ServiceException(AdLaunchStatus.of(advertRound.getLaunchStatus()).getLabel() + "推广不可调整!", HttpStatus.ERROR);
        }
        // 获取当前推广上传推广图或上传商品 的 截止时间
        String uploadDeadline = redisCache.getCacheObject(ADVERT_UPLOAD_FILTER_TIME_KEY + advertRound.getSymbol());
        if (StringUtils.isNotBlank(uploadDeadline)) {
            if (uploadDeadline.compareTo(DateUtils.getTime()) < 0) {
                throw new ServiceException("已超过推广修改截止时间，修改失败!", HttpStatus.ERROR);
            }
            // 保底的判断，其实上面的判断已经够了
        } else {
            // 若为 待投放，判断上传时间是否为 推广开始前一晚 22:20 分
            if (Objects.equals(advertRound.getLaunchStatus(), AdLaunchStatus.UN_LAUNCH.getValue())) {
                // 将advertRound.getStartTime()转为LocalDateTime
                LocalDateTime filterTime = advertRound.getStartTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().minusDays(1).withHour(22).withMinute(20);
                if (LocalDateTime.now().isAfter(filterTime)) {
                    throw new ServiceException("已超过推广修改截止时间，修改失败!", HttpStatus.ERROR);
                }
            }
            // 投放中，则判断 voucherDate 是否等于当天，若是，则再判断 是否晚于22:20分，若是，则不可编辑
            if (Objects.equals(advertRound.getLaunchStatus(), AdLaunchStatus.LAUNCHING.getValue())) {
                if (!Objects.equals(DateUtils.dateTime(advertRound.getVoucherDate()), DateUtils.dateTime(new Date()))) {
                    throw new ServiceException("已超过推广修改截止时间，修改失败!", HttpStatus.ERROR);
                }
                if (LocalDateTime.now().isAfter(LocalDateTime.now().withHour(22).withMinute(20))) {
                    throw new ServiceException("已超过推广修改截止时间，修改失败!", HttpStatus.ERROR);
                }
            }
        }
        // 设置推广商品
        advertRound.setProdIdStr(picDTO.getProdIdStr());
        // 设置风格类型
        if (ObjectUtils.isNotEmpty(picDTO.getStyleType())) {
            advertRound.setStyleType(picDTO.getStyleType());
        }
        // 修改推广图
        if (ObjectUtils.isNotEmpty(picDTO.getFile())) {
            SysFile file = BeanUtil.toBean(picDTO.getFile(), SysFile.class);
            this.fileMapper.insert(file);
            advertRound.setPicId(file.getId())
                    // 设置推广图审核状态 为待审核 因为有可能是 审核驳回后再次提交图片
                    .setPicSetType(AdPicSetType.SET.getValue())
                    // 设置推广图状态为 待审核，有可能是审核驳回后再次提交图片
                    .setPicAuditStatus(AdPicAuditStatus.UN_AUDIT.getValue())
                    .setUpdateTime(new Date());
        }
        return this.advertRoundMapper.updateById(advertRound);
    }

    /**
     * 获取审核失败的拒绝理由
     *
     * @param advertRoundId 档口轮次ID
     * @return String
     */
    @Override
    @Transactional(readOnly = true)
    public String getRejectReason(Long advertRoundId) {
        AdvertRound advertRound = Optional.ofNullable(this.advertRoundMapper.selectOne(new LambdaQueryWrapper<AdvertRound>()
                        .eq(AdvertRound::getId, advertRoundId).eq(AdvertRound::getDelFlag, Constants.UNDELETED)
                        .eq(AdvertRound::getPicAuditStatus, AdPicAuditStatus.AUDIT_REJECTED.getValue())))
                .orElseThrow(() -> new ServiceException("推广位不存在!", HttpStatus.ERROR));
        // 用户是否为档口管理者或子账户
        if (!SecurityUtils.isAdmin() && !SecurityUtils.isStoreManagerOrSub(advertRound.getStoreId())) {
            throw new ServiceException("当前用户非档口管理者或子账号，无权限操作!", HttpStatus.ERROR);
        }
        return advertRound.getRejectReason();
    }


    /**
     * 通过定时任务往redis中放当前推广位 当前播放轮 或 即将播放轮 的截止时间；每晚12:05:00执行
     * 比如：5.1 - 5.3
     * a. 现在是4.30 则截止时间是 4.30 22:00
     * b. 现在是5.2，则截止时间是 5.2 22:00:00 。
     * c. 现在是5.3，则第一轮还有请求，肯定是人为的不用管。每一轮过期时间都要添加到redis中
     */
    public void saveAdvertDeadlineToRedis() {
        List<AdvertRound> advertRoundList = this.advertRoundMapper.selectList(new LambdaQueryWrapper<AdvertRound>()
                .eq(AdvertRound::getDelFlag, Constants.UNDELETED)
                .in(AdvertRound::getLaunchStatus, Arrays.asList(AdLaunchStatus.LAUNCHING.getValue(), AdLaunchStatus.UN_LAUNCH.getValue())));
        if (CollectionUtils.isEmpty(advertRoundList)) {
            return;
        }
        final ZoneId zoneId = ZoneId.systemDefault();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateUtils.YYYY_MM_DD_HH_MM_SS);
        advertRoundList.stream().collect(Collectors.groupingBy(AdvertRound::getAdvertId))
                .forEach((advertId, roundList) -> {
                    // 判断当前推广类型是否为 时间范围
                    final boolean isTimeRange = roundList.stream().anyMatch(x -> Objects.equals(x.getShowType(), AdShowType.TIME_RANGE.getValue()));
                    // 时间范围处理逻辑
                    if (isTimeRange) {
                        this.setTimeRangePatternDeadline(formatter, roundList, zoneId);
                    } else {
                        // 开始时间和结束时间相同，则代表是 只有一天 的枚举类型
                        List<AdvertRound> onceRoundList = roundList.stream().filter(x -> Objects.equals(x.getStartTime(), x.getEndTime())).collect(Collectors.toList());
                        // 开始时间和结束时间不一致，则代表是 时间范围 的枚举类型
                        List<AdvertRound> timePositionList = roundList.stream().filter(x -> !Objects.equals(x.getStartTime(), x.getEndTime())).collect(Collectors.toList());
                        // 处理只有一天的枚举类型
                        if (CollectionUtils.isNotEmpty(onceRoundList)) {
                            // 将位置枚举的每一个symbol都存放到redis中
                            onceRoundList.stream().filter(x -> Objects.equals(x.getRoundId(), AdRoundType.PLAY_ROUND.getValue()))
                                    .forEach(x -> {
                                        redisCache.setCacheObject(ADVERT_DEADLINE_KEY + x.getSymbol(),
                                                formatter.format(LocalDateTime.now().with(LocalTime.parse(x.getDeadline()))), 1, TimeUnit.DAYS);
                                        // 档口上传推广图 或 上传推广商品 截止时间
                                        redisCache.setCacheObject(ADVERT_UPLOAD_FILTER_TIME_KEY + x.getSymbol(),
                                                formatter.format(LocalDateTime.now().with(LocalTime.parse(Constants.ADVERT_STORE_UPLOAD_DEADLINE))), 1, TimeUnit.DAYS);
                                    });
                            onceRoundList.stream().filter(x -> !Objects.equals(x.getRoundId(), AdRoundType.PLAY_ROUND.getValue()))
                                    .forEach(x -> {
                                        // 推广开始时间的前一天的 截止时间
                                        final String deadline = formatter.format(x.getStartTime().toInstant().atZone(zoneId)
                                                .toLocalDateTime().minusDays(1).with(LocalTime.parse(x.getDeadline())));
                                        redisCache.setCacheObject(ADVERT_DEADLINE_KEY + x.getSymbol(), deadline, 1, TimeUnit.DAYS);
                                        // 档口上传推广图 或 上传推广商品 截止时间
                                        String uploadDeadline = formatter.format(x.getStartTime().toInstant().atZone(zoneId)
                                                .toLocalDateTime().minusDays(1).with(LocalTime.parse(Constants.ADVERT_STORE_UPLOAD_DEADLINE)));
                                        redisCache.setCacheObject(ADVERT_UPLOAD_FILTER_TIME_KEY + x.getSymbol(), uploadDeadline, 1, TimeUnit.DAYS);

                                    });
                        }
                        // 时间范围 + 位置枚举
                        if (CollectionUtils.isNotEmpty(timePositionList)) {
                            this.setTimeRangePatternDeadline(formatter, timePositionList, zoneId);
                        }
                    }
                });
    }

    /**
     * 处理 "时间范围" 类型 或者 "时间范围 + 位置枚举" 类型 的截止时间
     *
     * @param formatter formatter
     * @param roundList 播放轮次
     * @param zoneId    zoneId
     */
    private void setTimeRangePatternDeadline(DateTimeFormatter formatter, List<AdvertRound> roundList, ZoneId zoneId) {
        Map<String, String> symbolDeadlineMap = new HashMap<>();
        Map<String, Date> roundSymbolMap = new HashMap<>();
        for (AdvertRound x : roundList) {
            symbolDeadlineMap.put(x.getSymbol(), x.getDeadline());
            if (!Objects.equals(x.getRoundId(), AdRoundType.PLAY_ROUND.getValue())) {
                roundSymbolMap.put(x.getSymbol(), x.getStartTime());
            } else {
                // 第一轮（有可能为 位置枚举[多个] 或 时间范围[单个]）
                // 档口购买推广的截止时间
                redisCache.setCacheObject(ADVERT_DEADLINE_KEY + x.getSymbol(),
                        formatter.format(LocalDateTime.now().with(LocalTime.parse(x.getDeadline()))), 1, TimeUnit.DAYS);
                // 档口上传推广图 或 上传推广商品 截止时间
                redisCache.setCacheObject(ADVERT_UPLOAD_FILTER_TIME_KEY + x.getSymbol(),
                        formatter.format(LocalDateTime.now().with(LocalTime.parse(Constants.ADVERT_STORE_UPLOAD_DEADLINE))), 1, TimeUnit.DAYS);
            }
        }
        // 统一处理非第一轮
        roundSymbolMap.forEach((symbol, startTime) -> {
            String defaultDeadline = symbolDeadlineMap.getOrDefault(symbol, "22:00:00");
            // 第二轮之后的轮次过期时间都为开始时间前一天
            String deadline = formatter.format(startTime.toInstant().atZone(zoneId)
                    .toLocalDateTime().minusDays(1).with(LocalTime.parse(defaultDeadline)));
            redisCache.setCacheObject(ADVERT_DEADLINE_KEY + symbol, deadline, 1, TimeUnit.DAYS);
            // 档口上传推广图 或 上传推广商品 截止时间
            String uploadDeadline = formatter.format(startTime.toInstant().atZone(zoneId)
                    .toLocalDateTime().minusDays(1).with(LocalTime.parse(Constants.ADVERT_STORE_UPLOAD_DEADLINE)));
            redisCache.setCacheObject(ADVERT_UPLOAD_FILTER_TIME_KEY + symbol, uploadDeadline, 1, TimeUnit.DAYS);
        });
    }


    /**
     * 获取当前推广轮次的过期时间 每一个轮次都有过期时间，
     *
     * @param symbol 符号
     * @return 过期时间
     */
    private String getDeadline(String symbol) {
        String deadline = redisCache.getCacheObject(ADVERT_DEADLINE_KEY + symbol);
        if (StringUtils.isNotBlank(deadline)) {
            return deadline;
        }
        // 重新存到redis中
        this.saveAdvertDeadlineToRedis();
        deadline = redisCache.getCacheObject(ADVERT_DEADLINE_KEY + symbol);
        if (StringUtils.isEmpty(deadline)) {
            throw new ServiceException("获取推广轮次过期时间失败!请联系客服", HttpStatus.ERROR);
        }
        return deadline;
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

    /**
     * 根据Date获取当前星期几
     *
     * @param date 时间yyyy-MM-dd
     * @return 星期几
     */
    public static String getDayOfWeek(Date date) {
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        DayOfWeek dayOfWeek = localDateTime.getDayOfWeek();
        // 强制中文，如 "星期一"
        return dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA);
    }

    /**
     * 计算两天之间相差多少天
     *
     * @param startDate      开始日期
     * @param endDate        截止日期
     * @param isContainToday true 包含今天的时间间隔 false 不包含
     * @return diffDay
     */
    public static int calculateDurationDay(Date startDate, Date endDate, Boolean isContainToday) {
        LocalDate start = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate end = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return (int) start.until(end, ChronoUnit.DAYS) + (isContainToday ? 1 : 0);
    }

    /**
     * 购买人手机号中国部分用*号代替
     *
     * @param phoneNumber 电话号码
     * @return String
     */
    private String maskPhoneNum(String phoneNumber) {
        if (StringUtils.isEmpty(phoneNumber) || phoneNumber.length() < 4) {
            return "";
        }
        int length = phoneNumber.length();
        String prefix = phoneNumber.substring(0, 2); // 前两位
        String suffix = phoneNumber.substring(length - 2); // 后两位
        StringBuilder middle = new StringBuilder();
        for (int i = 0; i < length - 4; i++) {
            middle.append('*'); // 中间替换为*
        }
        return prefix + middle + suffix;
    }

    /**
     * 判断当前推广类型是否为 推广图 或者 图及商品类型
     *
     * @param displayType 推广类型
     * @return true 是  false 不是
     */
    private boolean hasPic(Integer displayType) {
        return Objects.equals(displayType, AdDisplayType.PICTURE.getValue()) || Objects.equals(displayType, AdDisplayType.PIC_AND_PROD.getValue());
    }

    /**
     * 获取受欢迎推广图的平均价格
     *
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @param startPrice 开始价格
     * @return 平均每一天价格
     */
    private BigDecimal getAvgStartPrice(Date startTime, Date endTime, BigDecimal startPrice) {
        // 根据当前日期与截止日期的占比修改推广价格
        return startPrice.divide(BigDecimal.valueOf(calculateDurationDay(startTime, endTime, Boolean.TRUE)),
                0, RoundingMode.HALF_UP);
    }

    /**
     * 设置当前档口是否能够购买推广
     *
     * @param advertRound      当前推广
     * @param storeId          档口ID
     * @param currentRoundList 当前档口已购买的推广
     * @return true 可以购买  false 不可购买
     */
    private Boolean setRoundCanPurchased(AdvertRound advertRound, Long storeId, List<AdvertRound> currentRoundList) {
        // 初始化为true
        Boolean canPurchased = Boolean.TRUE;
        // 如果为当前档口购买，有可能biddingStatus = 1 或 biddingStatus = 2，不用管 只要storeId 为当前档口即可
        if (Objects.equals(advertRound.getStoreId(), storeId)) {
            canPurchased = Boolean.FALSE;
            // 如果是播放轮，且 所有 biddingStatus = 2，则不能购买
        } else if (Objects.equals(advertRound.getRoundId(), AdRoundType.PLAY_ROUND.getValue())
                && currentRoundList.stream().allMatch(x -> Objects.equals(x.getBiddingStatus(), AdBiddingStatus.BIDDING_SUCCESS.getValue()))) {
            canPurchased = Boolean.FALSE;
            // 判断截止时间是否超时，并且只会处理马上播放的这一轮。比如 5.1-5.3，当前为4.30，处理这一轮；当前为5.2，处理这一轮；当前为5.3（最后一天），处理下一轮。
        } else if (DateUtils.getTime().compareTo(this.getDeadline(advertRound.getSymbol())) > 0) {
            canPurchased = Boolean.FALSE;
        }
        return canPurchased;
    }

    /**
     * 已购推广列表设置 是否能购买广告
     *
     * @param advertRound            当前推广
     * @param canPurchasedStatusList 已出价 和 竞价成功
     * @return true 可以购买  false 不可购买
     */
    private Boolean setBoughtRecordCanPurchased(AdvertRound advertRound, List<Integer> canPurchasedStatusList) {
        // 初始化为true
        Boolean canPurchased = Boolean.TRUE;
        // 当前档口已出价 或 竞价成功，则不可再出价
        if (canPurchasedStatusList.contains(advertRound.getBiddingStatus())) {
            canPurchased = Boolean.FALSE;
            // 判断截止时间是否超时，并且只会处理马上播放的这一轮。比如 5.1-5.3，当前为4.30，处理这一轮；当前为5.2，处理这一轮；当前为5.3（最后一天），处理下一轮。
        } else if (DateUtils.getTime().compareTo(this.getDeadline(advertRound.getSymbol())) > 0) {
            canPurchased = Boolean.FALSE;
        }
        return canPurchased;
    }

    /**
     * 购买推广前置校验
     *
     * @param createDTO 购买入参
     */
    private void advertPreBuyCheck(AdRoundStoreCreateDTO createDTO) {
        // 判断截止时间是否超时，并且只会处理马上播放的这一轮。比如 5.1-5.3，当前为4.30，处理这一轮；当前为5.2，处理这一轮；当前为5.3（最后一天），处理下一轮。
        if (DateUtils.getTime().compareTo(this.getDeadline(createDTO.getSymbol())) > 0) {
            throw new ServiceException("竞价失败，已过系统截止时间!", HttpStatus.ERROR);
        }
        // 如果是位置枚举的推广位，则需要传position
        if (Objects.equals(createDTO.getShowType(), AdShowType.POSITION_ENUM.getValue()) && StringUtils.isEmpty(createDTO.getPosition())) {
            throw new ServiceException("当前推广类型position：必传!", HttpStatus.ERROR);
        }
        // 如果是时间范围的推广位，则不需要传position
        if (Objects.equals(createDTO.getShowType(), AdShowType.TIME_RANGE.getValue()) && StringUtils.isNotBlank(createDTO.getPosition())) {
            throw new ServiceException("当前推广类型position：不传!", HttpStatus.ERROR);
        }
        // 用户是否为档口管理者或子账户
        if (!SecurityUtils.isAdmin() && !SecurityUtils.isStoreManagerOrSub(createDTO.getStoreId())) {
            throw new ServiceException("当前用户非档口管理者或子账号，无权限操作!", HttpStatus.ERROR);
        }
        //校验推广支付方式是否存在
        AdPayWay.of(createDTO.getPayWay());
        // 校验使用余额情况下，密码是否正确
        if (Objects.equals(createDTO.getPayWay(), AdPayWay.BALANCE.getValue())
                && !assetService.checkTransactionPassword(createDTO.getStoreId(), createDTO.getTransactionPassword())) {
            throw new ServiceException("支付密码错误!请重新输入", HttpStatus.ERROR);
        }
    }


}
