package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
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
import com.ruoyi.xkt.domain.*;
import com.ruoyi.xkt.dto.advertRound.*;
import com.ruoyi.xkt.enums.*;
import com.ruoyi.xkt.mapper.*;
import com.ruoyi.xkt.service.IAdvertRoundService;
import com.ruoyi.xkt.service.IAssetService;
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
    final AdvertStoreFileMapper advertStoreFileMapper;

    // 推广营销位锁 key：symbol + roundId 或者 symbol + roundId + position 。value都是new Object()
    public static Map<String, Object> advertLockMap = new ConcurrentHashMap<>();

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
        System.err.println(advertLockMap);
    }

    public void test() {
        System.err.println(advertLockMap);
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
        if (StringUtils.isEmpty(advertRound.getProdIdStr())) {
            return Collections.emptyList();
        }
        List<StoreProduct> storeProdList = this.storeProdMapper.selectList(new LambdaQueryWrapper<StoreProduct>()
                .eq(StoreProduct::getDelFlag, Constants.UNDELETED).eq(StoreProduct::getStoreId, advertRound.getStoreId())
                .in(StoreProduct::getId, Arrays.stream(advertRound.getProdIdStr().split(",")).map(Long::parseLong).collect(Collectors.toList())));
        return storeProdList.stream().map(x -> new AdRoundSetProdResDTO().setStoreProdId(x.getId()).setProdArtNum(x.getProdArtNum())).collect(Collectors.toList());
    }


    /**
     * 根据广告ID获取推广轮次列表，并返回当前档口在这些推广轮次的数据
     *
     * @param storeId  档口ID
     * @param advertId 广告ID
     * @param showType 显示类型 时间范围 位置枚举
     * @return AdRoundPlayStoreResDTO
     */
    @Override
    @Transactional(readOnly = true)
    public AdRoundStoreResDTO getStoreAdInfo(final Long storeId, final Long advertId, final Integer showType) {
        final LocalTime now = LocalTime.now();
        // 当前时间 是否在  晚上22:00:01  到 晚上23:59:59之间 决定 biddingStatus 和  biddingTempStatus 用那一个字段
        boolean tenClockAfter = now.isAfter(LocalTime.of(22, 0, 1)) && now.isBefore(LocalTime.of(23, 59, 59));
        // 当天
        final Date voucherDate = java.sql.Date.valueOf(LocalDate.now());
        // 获取当前所有 正在投放 和 待投放的推广轮次
        List<AdvertRound> allRoundList = this.advertRoundMapper.selectList(new LambdaQueryWrapper<AdvertRound>()
                .eq(AdvertRound::getDelFlag, Constants.UNDELETED)
                .in(AdvertRound::getLaunchStatus, Arrays.asList(AdLaunchStatus.LAUNCHING.getValue(), AdLaunchStatus.UN_LAUNCH.getValue())));
        if (CollectionUtils.isEmpty(allRoundList)) {
            return AdRoundStoreResDTO.builder().build();
        }
        // 当前 档口 在所有 待投放  及 投放中 的推广轮次竞价失败记录
        List<AdvertRoundRecord> allRecordList = this.advertRoundRecordMapper.selectList(new LambdaQueryWrapper<AdvertRoundRecord>()
                .eq(AdvertRoundRecord::getDelFlag, Constants.UNDELETED).eq(AdvertRoundRecord::getStoreId, storeId)
                .in(AdvertRoundRecord::getAdvertRoundId, allRoundList.stream().map(AdvertRound::getId).collect(Collectors.toList())));
        AdRoundStoreResDTO roundResDTO = AdRoundStoreResDTO.builder()
                // 获取档口 已抢购推广位
                .boughtRoundList(this.getStoreBoughtRecordList(allRoundList, allRecordList, storeId, voucherDate, tenClockAfter))
                .build();
        // 筛选当前 推广位 正在投放 及 待投放的推广轮次
        List<AdvertRound> advertRoundList = allRoundList.stream().filter(x -> Objects.equals(x.getAdvertId(), advertId)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(advertRoundList)) {
            return roundResDTO;
        }
        // 第一轮结束投放时间
        final Date firstRoundEndTime = advertRoundList.stream().filter(x -> x.getRoundId().equals(AdRoundType.PLAY_ROUND.getValue()))
                .max(Comparator.comparing(AdvertRound::getEndTime))
                .orElseThrow(() -> new ServiceException("获取推广结束时间失败，请联系客服!", HttpStatus.ERROR)).getEndTime();
        // 如果当前非第一轮最后一天，则展示前4轮；如果当前是第一轮最后一天，则展示第2到第5轮。
        advertRoundList = voucherDate.before(firstRoundEndTime)
                ? advertRoundList.stream().filter(x -> !Objects.equals(x.getRoundId(), AdRoundType.FIFTH_ROUND.getValue())).collect(Collectors.toList())
                : advertRoundList.stream().filter(x -> !Objects.equals(x.getRoundId(), AdRoundType.PLAY_ROUND.getValue())).collect(Collectors.toList());
        // 如果投放类型是：时间范围，则只需要返回每一轮的开始时间和结束时间；如果投放类型是：位置枚举，则需要返回每一个位置的详细情况
        if (Objects.equals(showType, AdShowType.TIME_RANGE.getValue())) {
            // 有档口购买的所有轮次
            Set<Integer> roundIdSet = advertRoundList.stream().filter(x -> ObjectUtils.isNotEmpty(x.getPayPrice())).map(AdvertRound::getRoundId).collect(Collectors.toSet());
            // 当前档口购买的轮次
            Set<Integer> boughtIdSet = advertRoundList.stream().filter(x -> Objects.equals(x.getStoreId(), storeId)).map(AdvertRound::getRoundId).collect(Collectors.toSet());
            // 当前档口未购买的轮次
            roundIdSet.removeAll(boughtIdSet);
            // 构建当前round基础数据
            List<AdRoundStoreResDTO.ADRSRoundTimeRangeDTO> rangeDTOList = advertRoundList.stream().map(x -> new AdRoundStoreResDTO.ADRSRoundTimeRangeDTO()
                            .setAdvertId(x.getAdvertId()).setRoundId(x.getRoundId()).setSymbol(x.getSymbol()).setStartTime(x.getStartTime()).setEndTime(x.getEndTime())
                            .setStartWeekDay(getDayOfWeek(x.getStartTime())).setEndWeekDay(getDayOfWeek(x.getEndTime())).setStartPrice(x.getStartPrice())
                            .setDurationDay(calculateDurationDay(x.getStartTime(), x.getEndTime())))
                    .distinct().collect(Collectors.toList());
            // 当前档口购买的推广轮次
            Map<Integer, AdvertRound> boughtRoundMap = advertRoundList.stream().filter(x -> Objects.equals(x.getStoreId(), storeId))
                    .collect(Collectors.toMap(AdvertRound::getRoundId, Function.identity()));
            // 未购买的推广轮次记录
            Map<Integer, AdvertRoundRecord> unBoughtRoundMap = CollectionUtils.isEmpty(roundIdSet) ? new HashMap<>()
                    : allRecordList.stream().filter(x -> Objects.equals(x.getAdvertId(), advertId)
                            && Objects.equals(x.getVoucherDate(), voucherDate) && roundIdSet.contains(x.getRoundId()))
                    .collect(Collectors.toMap(AdvertRoundRecord::getRoundId, Function.identity(),
                            BinaryOperator.maxBy(Comparator.comparing(AdvertRoundRecord::getId))));

            final Date date = new Date();
            // 当前最近的播放轮次
            final Integer minRoundId = advertRoundList.stream().min(Comparator.comparing(AdvertRound::getRoundId))
                    .orElseThrow(() -> new ServiceException("当前播放轮次不存在!请联系客服", HttpStatus.ERROR)).getRoundId();
            // 设置当前档口在推广轮次中的数据详情
            rangeDTOList.forEach(x -> {
                // 只有播放轮才按照时间计算折扣价
                if (Objects.equals(x.getRoundId(), AdRoundType.PLAY_ROUND.getValue())) {
                    // 根据当前日期与截止日期的占比修改推广价格
                    final BigDecimal curStartPrice = BigDecimal.valueOf(calculateDurationDay(date, x.getEndTime()))
                            .divide(BigDecimal.valueOf(x.getDurationDay()), 10, RoundingMode.DOWN).multiply(x.getStartPrice())
                            .setScale(0, RoundingMode.DOWN);
                    x.setStartPrice(curStartPrice);
                }
                // 已购买推广位轮次
                final AdvertRound boughtRound = boughtRoundMap.get(x.getRoundId());
                if (ObjectUtils.isNotEmpty(boughtRound) && ObjectUtils.isNotEmpty(boughtRound.getBiddingStatus())) {
                    // 如果是最近的播放轮次，且当前时间在 晚上10:00:01 之后到 当天23:59:59 都显示 biddingTempStatus 字段
                    x.setBiddingStatus(tenClockAfter && Objects.equals(x.getRoundId(), minRoundId) ? boughtRound.getBiddingTempStatus() : boughtRound.getBiddingStatus());
                    x.setBiddingStatusName(Objects.requireNonNull(AdBiddingStatus.of(boughtRound.getBiddingStatus())).getLabel());
                }
                // 未购买推广位轮次
                final AdvertRoundRecord unBought = unBoughtRoundMap.get(x.getRoundId());
                if (ObjectUtils.isNotEmpty(unBought)) {
                    x.setBiddingStatus(unBought.getBiddingStatus());
                    x.setBiddingStatusName(Objects.requireNonNull(AdBiddingStatus.of(unBought.getBiddingStatus())).getLabel());
                }
            });
            return roundResDTO.setTimeRangeList(rangeDTOList);
            // 位置枚举
        } else {
            // 单纯的位置枚举 只展示一轮，主要用于：首页商品推广、下载竞价推广
            Integer minRoundId = advertRoundList.stream().min(Comparator.comparing(AdvertRound::getRoundId)).orElseThrow(() -> new ServiceException("当前播放轮次不存在!请联系客服", HttpStatus.ERROR)).getRoundId();
            // 找到roundId最小的播放轮列表
            List<AdvertRound> minRoundList = advertRoundList.stream().filter(x -> Objects.equals(x.getRoundId(), minRoundId)).collect(Collectors.toList());
            // 当前轮次所有的位置
            List<String> positionList = minRoundList.stream().map(AdvertRound::getPosition).collect(Collectors.toList());
            // 当前轮次已购买的位置
            final List<String> boughtPositionList = minRoundList.stream().filter(x -> Objects.equals(x.getStoreId(), storeId)).map(AdvertRound::getPosition).collect(Collectors.toList());
            // 该轮次 剩下的未购买的位置
            positionList.removeAll(boughtPositionList);

            Map<String, AdvertRoundRecord> unBoughtRecordMap = CollectionUtils.isEmpty(positionList) ? new HashMap<>()
                    : allRecordList.stream().filter(x -> Objects.equals(x.getAdvertId(), advertId) && Objects.equals(x.getVoucherDate(), voucherDate)
                    && positionList.contains(x.getPosition()) && Objects.equals(x.getRoundId(), minRoundId)).collect(Collectors.toMap(AdvertRoundRecord::getPosition, Function.identity(),
                    // 从unBoughtRecordList中取出每个位置最大createTime的数据，仅取一条
                    BinaryOperator.maxBy(Comparator.comparing(AdvertRoundRecord::getCreateTime))));

            List<AdRoundStoreResDTO.ADRSRoundPositionDTO> positionDTOList = minRoundList.stream().map(x -> {
                // 当前轮次有购买记录
                if (Objects.equals(x.getStoreId(), storeId)) {
                    // 晚上10:00:01 之后到 当天23:59:59 都显示 biddingTempStatus 字段
                    final Integer biddingStatus = tenClockAfter ? x.getBiddingTempStatus() : x.getBiddingStatus();
                    return BeanUtil.toBean(x, AdRoundStoreResDTO.ADRSRoundPositionDTO.class).setBiddingStatus(biddingStatus)
                            .setBiddingStatusName(Objects.requireNonNull(AdBiddingStatus.of(biddingStatus)).getLabel());
                }
                // 其它轮次有购买记录
                if (ObjectUtils.isNotEmpty(unBoughtRecordMap.get(x.getPosition()))) {
                    return BeanUtil.toBean(unBoughtRecordMap.get(x.getPosition()), AdRoundStoreResDTO.ADRSRoundPositionDTO.class)
                            .setBiddingStatusName(Objects.requireNonNull(AdBiddingStatus.of(unBoughtRecordMap.get(x.getPosition()).getBiddingStatus())).getLabel())
                            // 需要展示当前推广位置 最高的价格
                            .setPayPrice(x.getPayPrice());
                }
                // 其它轮次没有购买轮次
                return BeanUtil.toBean(x, AdRoundStoreResDTO.ADRSRoundPositionDTO.class).setBiddingStatus(null);
            }).filter(ObjectUtils::isNotEmpty).collect(Collectors.toList());
            return roundResDTO.setPositionList(positionDTOList);
        }
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
    public Integer create(AdRoundStoreCreateDTO createDTO) throws ParseException {
        // 截止时间都是当天 22:00:00，并且只会处理马上播放的这一轮。比如 5.1-5.3，当前为4.30，处理这一轮；当前为5.2，处理这一轮；当前为5.3（最后一天），处理下一轮。
        if (DateUtils.getTime().compareTo(this.getDeadline(createDTO.getSymbol())) > 0) {
            throw new ServiceException("竞价失败，已经有档口出价更高了噢!", HttpStatus.ERROR);
        }
        if (ObjectUtils.isEmpty(redisCache.getCacheObject(CacheConstants.STORE_KEY + createDTO.getStoreId()))) {
            throw new ServiceException("档口不存在!", HttpStatus.ERROR);
        }
        // 如果是位置枚举的推广位，则需要传position
        if (Objects.equals(createDTO.getShowType(), AdShowType.POSITION_ENUM.getValue()) && StringUtils.isEmpty(createDTO.getPosition())) {
            throw new ServiceException("当前推广类型position：必传!", HttpStatus.ERROR);
        }
        // 如果是时间范围的推广位，则不需要传position
        if (Objects.equals(createDTO.getShowType(), AdShowType.TIME_RANGE.getValue()) && StringUtils.isNotBlank(createDTO.getPosition())) {
            throw new ServiceException("当前推广类型position：不传!", HttpStatus.ERROR);
        }
        // 当前营销推广位的锁
        Object lockObj = Optional.ofNullable(advertLockMap.get(createDTO.getSymbol())).orElseThrow(() -> new ServiceException("symbol不存在!", HttpStatus.ERROR));
        synchronized (lockObj) {
            // 判断当前推广位的这一轮每个档口可以买几个，不可超买
            boolean isOverBuy = this.advertRoundMapper.isStallOverBuy(createDTO.getAdvertId(), createDTO.getRoundId(), createDTO.getStoreId(),
                    Arrays.asList(AdLaunchStatus.LAUNCHING.getValue(), AdLaunchStatus.UN_LAUNCH.getValue()));
            if (isOverBuy) {
                throw new ServiceException("已购买过该推广位，不可超买哦!", HttpStatus.ERROR);
            }
            LambdaQueryWrapper<AdvertRound> queryWrapper = new LambdaQueryWrapper<AdvertRound>()
                    .eq(AdvertRound::getAdvertId, createDTO.getAdvertId()).eq(AdvertRound::getRoundId, createDTO.getRoundId())
                    .eq(AdvertRound::getDelFlag, Constants.UNDELETED).orderByAsc(AdvertRound::getPayPrice).last("LIMIT 1");
            // 如果是 时间范围 的广告位，则不查 系统拦截的数据；其实位置枚举的广告位也可不用查系统拦截的数据，主要是为了真实，给档口留个缝
            if (Objects.equals(createDTO.getShowType(), AdShowType.TIME_RANGE.getValue())) {
                queryWrapper.eq(AdvertRound::getSysIntercept, AdSysInterceptType.UN_INTERCEPT.getValue());
            }
            // 位置枚举类型的广告位，则 需要 查询具体的位置
            if (StringUtils.isNotBlank(createDTO.getPosition())) {
                queryWrapper.eq(AdvertRound::getPosition, createDTO.getPosition());
            }
            AdvertRound minPriceAdvert = Optional.ofNullable(this.advertRoundMapper.selectOne(queryWrapper)).orElseThrow(() -> new ServiceException("获取推广营销位置失败，请联系客服!", HttpStatus.ERROR));
            // 判断当前档口出价是否低于该推广位最低价格，若是，则提示:"已经有档口出价更高了噢，请重新出价!".
            if (createDTO.getPayPrice().compareTo(ObjectUtils.defaultIfNull(minPriceAdvert.getPayPrice(), BigDecimal.ZERO)) <= 0) {
                throw new ServiceException("已经有档口出价更高了噢，请重新出价!", HttpStatus.ERROR);
            }
            // storeId不为空，表明之前有其他的档口竞价
            if (ObjectUtils.isNotEmpty(minPriceAdvert.getStoreId())) {
                // 将推广费退至原档口余额
                assetService.refundAdvertFee(minPriceAdvert.getStoreId(), minPriceAdvert.getPayPrice());
                // 记录竞价失败的档口推广营销
                this.record(minPriceAdvert);
            }
            // 更新广告位数据
            minPriceAdvert
                    // 档口购买的推广位一律设置为 非拦截
                    .setSysIntercept(AdSysInterceptType.UN_INTERCEPT.getValue())
                    .setStoreId(createDTO.getStoreId()).setPayPrice(createDTO.getPayPrice()).setVoucherDate(java.sql.Date.valueOf(LocalDate.now()))
                    .setBiddingStatus(AdBiddingStatus.BIDDING.getValue()).setBiddingTempStatus(AdBiddingStatus.BIDDING_SUCCESS.getValue())
                    .setPicSetType(!Objects.equals(minPriceAdvert.getDisplayType(), AdDisplayType.PRODUCT.getValue()) ? AdPicSetType.UN_SET.getValue() : null)
                    .setPicAuditStatus(!Objects.equals(minPriceAdvert.getDisplayType(), AdDisplayType.PRODUCT.getValue()) ? AdPicAuditStatus.UN_AUDIT.getValue() : null)
                    .setPicDesignType(!Objects.equals(minPriceAdvert.getDisplayType(), AdDisplayType.PRODUCT.getValue()) ? createDTO.getPicDesignType() : null)
                    .setPicAuditStatus(!Objects.equals(minPriceAdvert.getDisplayType(), AdDisplayType.PRODUCT.getValue()) ? AdPicAuditStatus.UN_AUDIT.getValue() : null)
                    .setProdIdStr(createDTO.getProdIdStr());
            this.advertRoundMapper.updateById(minPriceAdvert);
            // 扣除推广费
            assetService.payAdvertFee(createDTO.getStoreId(), createDTO.getPayPrice(), createDTO.getTransactionPassword());
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
        PageHelper.startPage(pageDTO.getPageNum(), pageDTO.getPageSize());
        List<AdvertRoundStorePageResDTO> list = this.advertRoundMapper.selectStoreAdvertPage(pageDTO);
        list.forEach(item -> item.setPlatformName(AdPlatformType.of(item.getPlatformId()).getLabel())
                .setLaunchStatusName(AdLaunchStatus.of(item.getLaunchStatus()).getLabel())
                .setPicAuditStatusName(ObjectUtils.isNotEmpty(item.getPicAuditStatus()) ? AdPicAuditStatus.of(item.getPicAuditStatus()).getLabel() : "")
                .setPicDesignTypeName(ObjectUtils.isNotEmpty(item.getPicDesignType()) ? AdDesignType.of(item.getPicDesignType()).getLabel() : "")
                .setPicAuditStatusName(ObjectUtils.isNotEmpty(item.getPicAuditStatus()) ? AdPicAuditStatus.of(item.getPicAuditStatus()).getLabel() : "")
                .setPicSetTypeName(ObjectUtils.isNotEmpty(item.getPicSetType()) ? AdPicSetType.of(item.getPicSetType()).getLabel() : "")
                .setTypeName(AdType.of(item.getTypeId()).getLabel())
                .setBiddingStatusName(AdBiddingStatus.of(item.getBiddingStatus()).getLabel()));
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
                        .setStartTime(DateUtils.timeMMDD(x.getStartTime())).setEndTime(DateUtils.timeMMDD(x.getEndTime())).setStartPrice(x.getStartPrice()))
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
                    .eq(AdvertRound::getDelFlag, Constants.UNDELETED).orderByDesc(AdvertRound::getPayPrice, AdvertRound::getCreateTime)
                    .last("LIMIT 1"));
        } else {
            Optional.ofNullable(latestDTO.getPosition()).orElseThrow(() -> new ServiceException("位置枚举类型：position必传", HttpStatus.ERROR));
            advertRound = Optional.ofNullable(this.advertRoundMapper.selectOne(new LambdaQueryWrapper<AdvertRound>()
                            .eq(AdvertRound::getAdvertId, latestDTO.getAdvertId()).eq(AdvertRound::getRoundId, latestDTO.getRoundId())
                            .eq(AdvertRound::getPosition, latestDTO.getPosition()).eq(AdvertRound::getDelFlag, Constants.UNDELETED)))
                    .orElseThrow(() -> new ServiceException("推广位不存在!", HttpStatus.ERROR));
        }
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
                            .eq(StoreProduct::getId, Arrays.stream(advertRound.getProdIdStr().split(",")))
                            .eq(StoreProduct::getDelFlag, Constants.UNDELETED).eq(StoreProduct::getStoreId, advertRound.getStoreId())))
                    .orElseThrow(() -> new ServiceException("档口商品不存在!", HttpStatus.ERROR));
            latestInfo.setProdList(storeProdList.stream().map(x -> new AdRoundLatestResDTO.ARLProdDTO()
                    .setStoreProdId(x.getId()).setProdArtNum(x.getProdArtNum())).collect(Collectors.toList()));
        }
        return latestInfo;
    }



    /**
     * @param picDTO 档口上传推广图入参
     * @return Integer
     */
    @Override
    @Transactional
    public Integer uploadAdvertPic(AdRoundUploadPicDTO picDTO) {
        AdvertRound advertRound = Optional.ofNullable(this.advertRoundMapper.selectOne(new LambdaQueryWrapper<AdvertRound>()
                        .eq(AdvertRound::getId, picDTO.getAdvertRoundId()).eq(AdvertRound::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("推广位不存在!", HttpStatus.ERROR));
        SysFile file = BeanUtil.toBean(picDTO, SysFile.class);
        int count = this.fileMapper.insert(file);
        // 更新推广位的图片ID
        advertRound.setPicId(file.getId());
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
        return advertRound.getRejectReason();
    }

    /**
     * 通过定时任务往redis中放当前推广位 当前播放轮 或 即将播放轮 的截止时间；每晚12:05:00执行
     * 比如：5.1 - 5.3
     * a. 现在是4.30 则截止时间是 4.30 22:00
     * b. 现在是5.2，则截止时间是 5.2 22:00:00 。
     * c. 现在是5.3，则第一轮还有请求，肯定是人为的不用管。每一轮过期时间都要添加到redis中
     *
     * @throws ParseException
     */
    public void saveAdvertDeadlineToRedis() throws ParseException {
        List<AdvertRound> advertRoundList = this.advertRoundMapper.selectList(new LambdaQueryWrapper<AdvertRound>()
                .eq(AdvertRound::getDelFlag, Constants.UNDELETED)
                .in(AdvertRound::getLaunchStatus, Arrays.asList(AdLaunchStatus.LAUNCHING.getValue(), AdLaunchStatus.UN_LAUNCH.getValue())));
        if (CollectionUtils.isEmpty(advertRoundList)) {
            return;
        }
        // 服务器当前时间 yyyy-MM-dd
        final Date now = DateUtils.parseDate(DateUtils.getDate(), DateUtils.YYYY_MM_DD);
        // 当天截止的时间 yyyy-MM-dd 22:00:00
        final String filterTime = DateTimeFormatter.ofPattern(DateUtils.YYYY_MM_DD_HH_MM_SS).format(LocalDateTime.now().withHour(22).withMinute(0).withSecond(0));
        advertRoundList.stream().collect(Collectors.groupingBy(AdvertRound::getAdvertId))
                .forEach((advertId, roundList) -> {
                    // 判断当前推广类型是否为 时间范围
                    final boolean isTimeRange = roundList.stream().anyMatch(x -> Objects.equals(x.getShowType(), AdShowType.TIME_RANGE.getValue()));
                    // 判断当前时间所处的阶段 小于第一轮播放时间（有可能新广告还未开播）、处于第一轮中间、处于第一轮最后一天
                    final Date firstRoundEndTime = roundList.stream().filter(x -> x.getRoundId().equals(AdRoundType.PLAY_ROUND.getValue()))
                            .max(Comparator.comparing(AdvertRound::getEndTime))
                            .orElseThrow(() -> new ServiceException("获取推广结束时间失败，请联系客服!", HttpStatus.ERROR)).getEndTime();
                    // 时间范围处理逻辑
                    if (isTimeRange) {
                        if (now.compareTo(firstRoundEndTime) < 0) {
                            // 第一轮过期时间为当天
                            AdvertRound firstRound = roundList.stream().filter(x -> Objects.equals(x.getRoundId(), AdRoundType.PLAY_ROUND.getValue()))
                                    .max(Comparator.comparing(AdvertRound::getEndTime))
                                    .orElseThrow(() -> new ServiceException("获取推广结束时间失败，请联系客服!", HttpStatus.ERROR));
                            redisCache.setCacheObject(ADVERT_DEADLINE_KEY + firstRound.getSymbol(), filterTime, 1, TimeUnit.DAYS);
                            // 第二轮之后的轮次过期时间都为开始时间前一天
                            Map<String, Date> roundSymbolMap = roundList.stream().filter(x -> !Objects.equals(x.getRoundId(), AdRoundType.PLAY_ROUND.getValue()))
                                    .collect(Collectors.toMap(AdvertRound::getSymbol, AdvertRound::getStartTime, (s1, s2) -> s2));
                            if (MapUtils.isNotEmpty(roundSymbolMap)) {
                                roundSymbolMap.forEach((symbol, startTime) -> {
                                    // 推广开始时间的前一天的 22:00:00
                                    LocalDateTime futureTimeFilter = startTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().minusDays(1).withHour(22).withMinute(0).withSecond(0);
                                    redisCache.setCacheObject(ADVERT_DEADLINE_KEY + symbol, futureTimeFilter, 1, TimeUnit.DAYS);
                                });
                            }
                        }
                        if (now.compareTo(firstRoundEndTime) == 0) {
                            // 第一轮不用管，设置第二轮  或者 第一轮 第二轮过期时间都可以为 当天
                            List<AdvertRound> firstOrSecondRoundList = roundList.stream().filter(x -> Objects.equals(x.getRoundId(), AdRoundType.PLAY_ROUND.getValue())
                                    || Objects.equals(x.getRoundId(), AdRoundType.SECOND_ROUND.getValue())).collect(Collectors.toList());
                            Map<String, Date> roundSymbolMap = firstOrSecondRoundList.stream().collect(Collectors.toMap(AdvertRound::getSymbol, AdvertRound::getStartTime, (s1, s2) -> s2));
                            if (MapUtils.isNotEmpty(roundSymbolMap)) {
                                roundSymbolMap.forEach((symbol, startTime) -> {
                                    // 推广开始时间的前一天的 22:00:00
                                    LocalDateTime futureTimeFilter = startTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().minusDays(1).withHour(22).withMinute(0).withSecond(0);
                                    redisCache.setCacheObject(ADVERT_DEADLINE_KEY + symbol, futureTimeFilter, 1, TimeUnit.DAYS);
                                });
                            }
                            List<AdvertRound> otherRoundList = roundList.stream().filter(x -> !Objects.equals(x.getRoundId(), AdRoundType.PLAY_ROUND.getValue())
                                    && !Objects.equals(x.getRoundId(), AdRoundType.SECOND_ROUND.getValue())).collect(Collectors.toList());
                            Map<String, Date> otherRoundSymbolMap = otherRoundList.stream().collect(Collectors.toMap(AdvertRound::getSymbol, AdvertRound::getStartTime, (s1, s2) -> s2));
                            if (MapUtils.isNotEmpty(otherRoundSymbolMap)) {
                                otherRoundSymbolMap.forEach((symbol, startTime) -> {
                                    // 推广开始时间的前一天的 22:00:00
                                    LocalDateTime futureTimeFilter = startTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().minusDays(1).withHour(22).withMinute(0).withSecond(0);
                                    redisCache.setCacheObject(ADVERT_DEADLINE_KEY + symbol, futureTimeFilter, 1, TimeUnit.DAYS);
                                });
                            }
                        }
                    } else {
                        // 第一轮最后一天，则直接处理第二轮的symbol
                        if (now.equals(firstRoundEndTime)) {
                            // 将位置枚举的每一个symbol都存放到redis中
                            roundList.stream().filter(x -> !Objects.equals(x.getRoundId(), AdRoundType.PLAY_ROUND.getValue()))
                                    .forEach(x -> redisCache.setCacheObject(ADVERT_DEADLINE_KEY + x.getSymbol(), filterTime, 1, TimeUnit.DAYS));
                        } else {
                            // 将位置枚举的每一个symbol都存放到redis中
                            roundList.stream().filter(x -> Objects.equals(x.getRoundId(), AdRoundType.PLAY_ROUND.getValue()))
                                    .forEach(x -> redisCache.setCacheObject(ADVERT_DEADLINE_KEY + x.getSymbol(), filterTime, 1, TimeUnit.DAYS));
                        }
                    }
                });
    }


    /**
     * 获取当前推广轮次的过期时间 每一个轮次都有过期时间，
     *
     * @param symbol 符号
     * @return
     */
    private String getDeadline(String symbol) throws ParseException {
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
        return dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault());
    }

    /**
     * 计算两天之间相差多少天
     *
     * @param startDate 开始日期
     * @param endDate   截止日期
     * @return diffDay
     */
    public static int calculateDurationDay(Date startDate, Date endDate) {
        LocalDate start = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate end = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return (int) start.until(end, ChronoUnit.DAYS) + 1;
    }

    /**
     * 获取当前档口 已抢购推广位
     *
     * @param allRoundList  所有的推广
     * @param allRecordList 所有竞价失败的推广
     * @param storeId       档口ID
     * @param voucherDate   单据日期
     * @param tenClockAfter 是否是10点后
     * @return
     */
    private List<AdRoundStoreResDTO.ADRSRoundRecordDTO> getStoreBoughtRecordList(List<AdvertRound> allRoundList, List<AdvertRoundRecord> allRecordList,
                                                                                 Long storeId, Date voucherDate, boolean tenClockAfter) {
        // 按照advertId进行分组，取最小的roundId列表
        Map<Long, Optional<Integer>> minRoundIdMap = allRoundList.stream().collect(Collectors.groupingBy(AdvertRound::getAdvertId,
                Collectors.mapping(AdvertRound::getRoundId, Collectors.minBy(Comparator.comparing(Integer::intValue)))));
        // 最小的roundId列表
        List<Integer> roundIdList = minRoundIdMap.values().stream().filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
        // 筛选档口 所有的 已购买 推广位数据
        List<AdRoundStoreResDTO.ADRSRoundRecordDTO> boughtRoundList = allRoundList.stream().filter(x -> Objects.equals(x.getStoreId(), storeId))
                .map(x -> {
                    // 如果是最近的播放轮次，且当前时间在 晚上10:00:01 之后到 当天23:59:59 都显示 biddingTempStatus 字段
                    final Integer biddingStatus = tenClockAfter && roundIdList.contains(x.getRoundId()) ? x.getBiddingTempStatus() : x.getBiddingStatus();
                    return BeanUtil.toBean(x, AdRoundStoreResDTO.ADRSRoundRecordDTO.class).setBiddingStatus(biddingStatus)
                            .setBiddingStatusName(Objects.requireNonNull(AdBiddingStatus.of(biddingStatus)).getLabel())
                            .setTypeName(AdType.of(x.getTypeId()).getLabel())
                            // 如果是时间范围则不返回position
                            .setPosition(Objects.equals(x.getShowType(), AdShowType.TIME_RANGE.getValue()) ? null : x.getPosition());
                })
                .collect(Collectors.toList());
        // showType 为 时间范围的 每一轮最高的出价map
        Map<Integer, BigDecimal> timeRangeRoundMaxPriceMap = allRoundList.stream()
                .filter(x -> Objects.equals(x.getShowType(), AdShowType.TIME_RANGE.getValue()))
                .filter(x -> ObjectUtils.isNotEmpty(x.getPayPrice()))
                .collect(Collectors
                        .groupingBy(AdvertRound::getRoundId, Collectors
                                .mapping(AdvertRound::getPayPrice, Collectors.reducing(BigDecimal.ZERO, BigDecimal::max))));
        // showType 为 位置枚举的 每一个位置最高出价的 map
        Map<Long, BigDecimal> positionEnumMaxPriceMap = allRoundList.stream()
                .filter(x -> Objects.equals(x.getShowType(), AdShowType.POSITION_ENUM.getValue()))
                .filter(x -> ObjectUtils.isNotEmpty(x.getPayPrice()))
                .collect(Collectors.toMap(AdvertRound::getId, AdvertRound::getPayPrice));
        // 已购买的 时间范围播放轮次 的roundId列表
        final List<Integer> boughtTimeRangeRoundIdList = boughtRoundList.stream().filter(x -> Objects.equals(x.getShowType(), AdShowType.TIME_RANGE.getValue()))
                .map(AdRoundStoreResDTO.ADRSRoundRecordDTO::getRoundId).collect(Collectors.toList());
        // 已购买的  位置枚举 的 advertRoundId 列表
        final List<Long> boughtPositionAdvertRoundIdList = boughtRoundList.stream().filter(x -> Objects.equals(x.getShowType(), AdShowType.POSITION_ENUM.getValue()))
                .map(AdRoundStoreResDTO.ADRSRoundRecordDTO::getAdvertRoundId).collect(Collectors.toList());
        // 购买失败的 时间范围播放轮次的  列表
        Map<Integer, AdvertRoundRecord> unBoughtTimeRangeMap = allRecordList.stream()
                .filter(x -> Objects.equals(x.getShowType(), AdShowType.TIME_RANGE.getValue()))
                .filter(x -> !boughtTimeRangeRoundIdList.contains(x.getRoundId()))
                .filter(x -> Objects.equals(x.getVoucherDate(), voucherDate))
                .collect(Collectors.toMap(AdvertRoundRecord::getRoundId, Function.identity(),
                        BinaryOperator.maxBy(Comparator.comparingLong(AdvertRoundRecord::getId))));
        // 购买失败的 位置枚举播放轮次的 列表
        Map<Long, AdvertRoundRecord> unBoughtPositionMap = allRecordList.stream()
                .filter(x -> Objects.equals(x.getShowType(), AdShowType.POSITION_ENUM.getValue()))
                .filter(x -> !boughtPositionAdvertRoundIdList.contains(x.getAdvertRoundId()))
                .filter(x -> Objects.equals(x.getVoucherDate(), voucherDate))
                .collect(Collectors.toMap(AdvertRoundRecord::getAdvertRoundId, Function.identity(),
                        BinaryOperator.maxBy(Comparator.comparingLong(AdvertRoundRecord::getId))));
        if (MapUtils.isNotEmpty(unBoughtTimeRangeMap)) {
            unBoughtTimeRangeMap.forEach((roundId, record) -> {
                boughtRoundList.add(BeanUtil.toBean(record, AdRoundStoreResDTO.ADRSRoundRecordDTO.class).setPosition(null)
                        .setTypeName(AdType.of(record.getTypeId()).getLabel())
                        .setBiddingStatusName(Objects.requireNonNull(AdBiddingStatus.of(record.getBiddingStatus())).getLabel()
                                + "，最新出价:" + timeRangeRoundMaxPriceMap.get(record.getRoundId())));
            });
        }
        if (MapUtils.isNotEmpty(unBoughtPositionMap)) {
            unBoughtPositionMap.forEach((advertRoundId, record) -> {
                boughtRoundList.add(BeanUtil.toBean(record, AdRoundStoreResDTO.ADRSRoundRecordDTO.class)
                        .setTypeName(AdType.of(record.getTypeId()).getLabel())
                        .setBiddingStatusName(Objects.requireNonNull(AdBiddingStatus.of(record.getBiddingStatus())).getLabel()
                                + "，最新出价:" + positionEnumMaxPriceMap.get(record.getAdvertRoundId()))
                );
            });
        }
        return boughtRoundList;
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

}
