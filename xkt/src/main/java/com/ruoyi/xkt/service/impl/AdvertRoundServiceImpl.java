package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.AdType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.xkt.domain.AdvertRound;
import com.ruoyi.xkt.domain.AdvertRoundRecord;
import com.ruoyi.xkt.dto.advertRound.AdRoundStoreCreateDTO;
import com.ruoyi.xkt.dto.advertRound.AdRoundStoreResDTO;
import com.ruoyi.xkt.enums.AdBiddingStatus;
import com.ruoyi.xkt.enums.AdLaunchStatus;
import com.ruoyi.xkt.enums.AdRoundType;
import com.ruoyi.xkt.enums.AdShowType;
import com.ruoyi.xkt.mapper.AdvertRoundMapper;
import com.ruoyi.xkt.mapper.AdvertRoundRecordMapper;
import com.ruoyi.xkt.mapper.StoreMapper;
import com.ruoyi.xkt.service.IAdvertRoundService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    public void  test() {
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
     * 根据广告ID获取推广轮次列表，并返回当前档口在这些推广轮次的数据
     *
     * @param storeId  档口ID
     * @param advertId 广告ID
     * @param showType 显示类型 时间范围 位置枚举
     * @return AdRoundPlayStoreResDTO
     */
    @Override
    @Transactional(readOnly = true)
    public AdRoundStoreResDTO getStoreAdInfo(final Long storeId,final Long advertId,final Integer showType) {
        // 获取当前 正在播放 和 待播放的推广轮次
        List<AdvertRound> advertRoundList = this.advertRoundMapper.selectList(new LambdaQueryWrapper<AdvertRound>()
                .eq(AdvertRound::getAdvertId, advertId).eq(AdvertRound::getDelFlag, Constants.UNDELETED)
                .in(AdvertRound::getLaunchStatus, Arrays.asList(AdLaunchStatus.LAUNCHING.getValue(), AdLaunchStatus.UN_LAUNCH.getValue())));
        if (CollectionUtils.isEmpty(advertRoundList)) {
            return AdRoundStoreResDTO.builder().build();
        }
        // 当天
        final Date voucherDate = java.sql.Date.valueOf(LocalDate.now());
        // 第一轮结束投放时间
        final Date firstRoundEndTime = advertRoundList.stream().filter(x -> x.getRoundId().equals(AdRoundType.PLAY_ROUND.getValue()))
                .max(Comparator.comparing(AdvertRound::getEndTime))
                .orElseThrow(() -> new ServiceException("获取推广结束时间失败，请联系客服!", HttpStatus.ERROR)).getEndTime();
        // 如果当前非第一轮最后一天，则展示前4轮；如果当前是第一轮最后一天，则展示第2到第5轮。
        advertRoundList = voucherDate.before(firstRoundEndTime)
                ? advertRoundList.stream().filter(x -> !Objects.equals(x.getRoundId(), AdRoundType.FIFTH_ROUND.getValue())).collect(Collectors.toList())
                : advertRoundList.stream().filter(x -> !Objects.equals(x.getRoundId(), AdRoundType.PLAY_ROUND.getValue())).collect(Collectors.toList());
        // 有档口购买的所有轮次
        Set<Integer> roundIdSet = advertRoundList.stream().filter(x -> ObjectUtils.isNotEmpty(x.getPayPrice())).map(AdvertRound::getRoundId).collect(Collectors.toSet());
        // 当前档口购买的轮次
        Set<Integer> boughtIdSet = advertRoundList.stream().filter(x -> Objects.equals(x.getStoreId(), storeId)).map(AdvertRound::getRoundId).collect(Collectors.toSet());
        // 当前档口未购买的轮次
        roundIdSet.removeAll(boughtIdSet);
        List<AdvertRoundRecord> recordList = CollectionUtils.isNotEmpty(roundIdSet)
                ?  this.advertRoundRecordMapper.selectRecordList(advertId, storeId, voucherDate, roundIdSet) : new ArrayList<>();
        // 获取已抢购推广位列表
        AdRoundStoreResDTO roundResDTO =  AdRoundStoreResDTO.builder().boughtRoundList(this.getStoreBoughtList(storeId, advertRoundList, recordList)).build();
        // 如果投放类型是：时间范围，则只需要返回每一轮的开始时间和结束时间；如果投放类型是：位置枚举，则需要返回每一个位置的详细情况
        if (Objects.equals(showType, AdShowType.TIME_RANGE.getValue())) {
            // 构建当前round基础数据
            List<AdRoundStoreResDTO.ADRSRoundTimeRangeDTO> rangeDTOList = advertRoundList.stream().map(x -> new AdRoundStoreResDTO.ADRSRoundTimeRangeDTO()
                            .setAdvertId(x.getAdvertId()).setRoundId(x.getRoundId()).setSymbol(x.getSymbol()).setStartTime(x.getStartTime()).setEndTime(x.getEndTime())
                            .setStartWeekDay(getDayOfWeek(x.getStartTime())).setEndWeekDay(getDayOfWeek(x.getEndTime())).setStartPrice(x.getStartPrice())
                            .setDurationDay(calculateDurationDay(x.getStartTime(), x.getEndTime())))
                    .distinct().collect(Collectors.toList());
            // 当前档口购买的推广轮次
            Map<Integer, AdvertRound> boughtRoundMap = advertRoundList.stream().filter(x -> Objects.equals(x.getStoreId(), storeId))
                    .collect(Collectors.toMap(AdvertRound::getRoundId, Function.identity()));
            Map<Integer, AdvertRoundRecord> unBoughtRoundMap = recordList.stream().collect(Collectors.toConcurrentMap(AdvertRoundRecord::getRoundId, Function.identity()));
            final Date date = new Date();
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
                    x.setBiddingStatus(boughtRound.getBiddingStatus());
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
        } else {
            // 位置枚举
            
        }






        // 已抢购推广列表 显示单据日期大于等于当天的数据  针对某一轮 如果抢购成功了，则这一轮所以失败的记录都不显示。如果未来的某一轮：已出价或者竞价失败有多次，则只显示最新的那一条数据。
        // 如果是已出价，要显示最新出价。如果是竞价失败，要显示最新的价格

        // 针对当前播放轮，如果竞价成功，则显示。就算是当前轮最后一天 也要显示 竞价成功



        // 写 sql 从advertRound中获取 advertId 和 roundId 最高的价格
     /*   Map<Integer, BigDecimal> otherRoundMaxPriceMap = advertRoundList.stream().filter(x -> ObjectUtils.isNotEmpty(x.getPayPrice()))
                .filter(x -> remainRoundIdList.contains(x.getRoundId()))
                .collect(Collectors.groupingBy(
                        AdvertRound::getRoundId,
                        Collectors.reducing(BigDecimal.ZERO, AdvertRound::getPayPrice, BigDecimal::max)
                ));*/



        // 根据广告ID获取所有推广轮次列表，如果有其他档口的数据则 需要过滤

        // 这里要返回两个部分的数据 1. 左边的档口广告列表  2. 右边的已购推广位竞价结果

        // 如果左边某个推广位竞价失败，则失败的当天展示；第二天该推广位就不再展示竞价失败 字样了。但是右边的“已订购推广列表”，一直展示知道该推广位播放完毕

        // 当天不是第一轮最后一天，类型位：时间范围，则显示前4轮推广位；类型为：位置枚举，则显示前1轮。是最后一天，类型为：时间范围，显示第2轮到第4轮；类型为：位置枚举，则显示第二轮






        // 再判断当前当前与每一轮推广营销中的关系，已出价、竞价失败、竞价成功等

        return null;
    }



    /**
     * 档口购买推广营销
     *     主要是两个场景：1. 某个广告位（advert_id）某个轮次（round_id）按照出价（pay_price）决定能否购买。[eg: A B C D E]
     *                 2. 某个广告位（advert_id）某个轮次（round_id）某个具体位置（position）按照出价（pay_price）决定能否购买。
     *     思路：每次筛选出某个类型，价格最低的推广位，然后只操作这行数据
     *                 （创建索引：CREATE INDEX idx_advert_round_pay_pos ON advert_round (advert_id, round_id, pay_price, position)）。
     *     若：该行数据已经有其它档口先竞价了。先进行比价。如果出价低于最低价格，则抛出异常：“已经有档口出价更高了噢，请重新出价!”
     *     若：新出价比原数据价格高，则：a. 给原数据档口创建转移支付单   b.新档口占据该行位置，更新数据。
     * <p>
     *     等到晚上10:00，所有档口购买完成。再通过定时任务（11:30），给每个类型，按照价格从高到低，进行排序。并将首页各个位置的广告数据推送到ES中。为空的广告位如何填充？？
     * <p>
     *
     * @param createDTO 购买入参
     * @return Integer
     */
    @Override
    @Transactional
    public Integer create(AdRoundStoreCreateDTO createDTO) {
        // 截止时间都是当天 22:00:00，并且只会处理马上播放的这一轮。比如 5.1-5.3，当前为4.30，处理这一轮；当前为5.2，处理这一轮；当前为5.3（最后一天），处理下一轮。
        if (DateUtils.getTime().compareTo(this.getDeadline(createDTO.getSymbol())) > 0) {
            throw new ServiceException("竞价结束，已经有档口出价更高了噢!", HttpStatus.ERROR);
        }
        if (ObjectUtils.isEmpty(redisCache.getCacheObject(Constants.STORE_REDIS_PREFIX + createDTO.getStoreId()))) {
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
            boolean isOverBuy = this.advertRoundMapper.isStallOverBuy(createDTO.getAdvertId(), createDTO.getRoundId(), createDTO.getStoreId(), createDTO.getPosition());
            if (isOverBuy) {
                throw new ServiceException("已购买过该推广位，不可超买哦!", HttpStatus.ERROR);
            }
            LambdaQueryWrapper<AdvertRound> queryWrapper = new LambdaQueryWrapper<AdvertRound>()
                    .eq(AdvertRound::getAdvertId, createDTO.getAdvertId()).eq(AdvertRound::getRoundId, createDTO.getRoundId())
                    .eq(AdvertRound::getDelFlag, Constants.UNDELETED).orderByAsc(AdvertRound::getPayPrice).last("LIMIT 1");
            AdvertRound minPriceAdvert = Optional.ofNullable(this.advertRoundMapper.selectOne(queryWrapper)).orElseThrow(() -> new ServiceException("获取推广营销位置失败，请联系客服!", HttpStatus.ERROR));
            // 判断当前档口出价是否低于该推广位最低价格，若是，则提示:"已经有档口出价更高了噢，请重新出价!".
            if (createDTO.getPayPrice().compareTo(ObjectUtils.defaultIfNull(minPriceAdvert.getPayPrice(), BigDecimal.ZERO)) <= 0) {
                throw new ServiceException("已经有档口出价更高了噢，请重新出价!", HttpStatus.ERROR);
            }

            // storeId不为空，表明之前有其他的档口竞价
            if (ObjectUtils.isNotEmpty(minPriceAdvert.getStoreId())) {

                // TODO 将推广费退至原档口余额
                // TODO 将推广费退至原档口余额

                // 记录竞价失败的档口推广营销
                this.record(minPriceAdvert);
            }

            // 更新出价最低的广告位
            minPriceAdvert.setStoreId(createDTO.getStoreId()).setPayPrice(createDTO.getPayPrice())
                    .setVoucherDate(java.sql.Date.valueOf(LocalDate.now()))
                    .setBiddingStatus(AdBiddingStatus.BIDDING.getValue())
                    .setBiddingTempStatus(AdBiddingStatus.BIDDING_SUCCESS.getValue())
                    .setPicDesignType(createDTO.getPicDesignType()).setProdIdStr(createDTO.getProdIdStr());
            this.advertRoundMapper.updateById(minPriceAdvert);

            // TODO 新竞价成功的档口扣减余额
            // TODO 新竞价成功的档口扣减余额

        }
        return 1;
    }

    /**
     * 获取已抢购推广位列表
     *
     * @param storeId         档口ID
     * @param advertRoundList 播放轮次列表
     * @param recordList 未购买的播放轮次列表
     * @return List<AdRoundStoreResDTO.ADRSRoundRecordDTO>
     */
    private List<AdRoundStoreResDTO.ADRSRoundRecordDTO> getStoreBoughtList(Long storeId, List<AdvertRound> advertRoundList, List<AdvertRoundRecord> recordList) {
        // 每一轮最高的出价map
        Map<Integer, BigDecimal> maxBidMap = advertRoundList.stream().filter(x -> ObjectUtils.isNotEmpty(x.getPayPrice())).collect(Collectors
                .groupingBy(AdvertRound::getRoundId, Collectors
                        .mapping(AdvertRound::getPayPrice, Collectors.reducing(BigDecimal.ZERO, BigDecimal::max))));
        // 先处理 已抢购广告位列表，此处不用管 播放的轮次  统一展示前4轮的结果
        List<AdRoundStoreResDTO.ADRSRoundRecordDTO> boughtRoundList = advertRoundList.stream().filter(x -> Objects.equals(x.getStoreId(), storeId))
                .map(x -> BeanUtil.toBean(x, AdRoundStoreResDTO.ADRSRoundRecordDTO.class).setTypeName(Objects.requireNonNull(AdType.of(x.getTypeId())).getLabel())
                        .setBiddingStatusName(Objects.requireNonNull(AdBiddingStatus.of(x.getBiddingStatus())).getLabel()))
                .collect(Collectors.toList());
        // 查询其它轮次是否有购买记录
        if (CollectionUtils.isEmpty(recordList)) {
            return boughtRoundList;
        }
        boughtRoundList.addAll(recordList.stream().sorted(Comparator.comparing(AdvertRoundRecord::getBiddingStatus))
                .map(x -> BeanUtil.toBean(x, AdRoundStoreResDTO.ADRSRoundRecordDTO.class).setTypeName(Objects.requireNonNull(AdType.of(x.getTypeId())).getLabel())
                        .setBiddingStatusName(Objects.requireNonNull(AdBiddingStatus.of(x.getBiddingStatus())).getLabel() + "，最新出价:" + maxBidMap.get(x.getRoundId())))
                .collect(Collectors.toList()));
        return boughtRoundList;
    }


    /**
     * 通过定时任务往redis中放当前推广位 当前播放轮 或 即将播放轮 的截止时间；
     * 比如：5.1 - 5.3
     *      a. 现在是4.30 则截止时间是 4.30 22:00
     *      b. 现在是5.2，则截止时间是 5.2 22:00:00 。
     *      c. 现在是5.3，则第一轮还有请求，肯定是人为的不用管。请求第三轮 或者 第四轮 不报错。只处理第二轮请求
     *
     * @throws ParseException
     */
    public void saveAdvertDeadlineToRedis() throws ParseException {
        List<AdvertRound> advertRoundList = this.advertRoundMapper.selectList(new LambdaQueryWrapper<AdvertRound>()
                .eq(AdvertRound::getDelFlag, Constants.UNDELETED)
                .in(AdvertRound::getLaunchStatus, Arrays.asList(AdLaunchStatus.LAUNCHING.getValue(), AdLaunchStatus.UN_LAUNCH.getValue()))
                .in(AdvertRound::getRoundId, Arrays.asList(AdRoundType.PLAY_ROUND.getValue(), AdRoundType.SECOND_ROUND.getValue())));
        if (CollectionUtils.isEmpty(advertRoundList)) {
            return;
        }
        // 服务器当前时间 yyyy-MM-dd
        final Date now = DateUtils.parseDate(DateUtils.getDate(), DateUtils.YYYY_MM_DD);
        // 当天截止的时间 yyyy-MM-dd 22:00:00
        final String filterTime =  DateTimeFormatter.ofPattern(DateUtils.YYYY_MM_DD_HH_MM_SS)
                .format(LocalDateTime.now().withHour(22).withMinute(0).withSecond(0));
        advertRoundList.stream().collect(Collectors.groupingBy(AdvertRound::getAdvertId))
                .forEach((advertId, roundList) -> {
                    // 判断当前时间所处的阶段 小于第一轮播放时间（有可能新广告还未开播）、处于第一轮中间、处于第一轮最后一天
                    final Date firstRoundEndTime = roundList.stream().filter(x -> x.getRoundId().equals(AdRoundType.PLAY_ROUND.getValue()))
                            .max(Comparator.comparing(AdvertRound::getEndTime))
                            .orElseThrow(() -> new ServiceException("获取推广结束时间失败，请联系客服!", HttpStatus.ERROR)).getEndTime();
                    // 默认第一轮的symbol
                    String symbol = roundList.stream().filter(x -> x.getRoundId().equals(AdRoundType.PLAY_ROUND.getValue()))
                            .map(AdvertRound::getSymbol).findAny().orElseThrow(() -> new ServiceException("获取推广第一轮symbol失败，请联系客服!", HttpStatus.ERROR));
                    // 第一轮最后一天，则直接处理第二轮的symbol
                    if (now.equals(firstRoundEndTime)) {
                        String secondRoundSymbol = roundList.stream().filter(x -> x.getRoundId().equals(AdRoundType.SECOND_ROUND.getValue()))
                                .map(AdvertRound::getSymbol).findAny().orElse(null);
                        // 有可能第二轮不存在，因为该推广已下线，则定时任务没有生成后续轮次
                        if (StringUtils.isEmpty(secondRoundSymbol)) {
                            return;
                        }
                    }
                    // 作用于某一轮推广的symbol（包含了播放轮次的）
                    /*String symbol = now.before(firstRoundEndTime)
                            ? roundList.stream().filter(x -> x.getRoundId().equals(AdRoundType.PLAY_ROUND.getValue()))
                            .map(AdvertRound::getSymbol).findAny().orElseThrow(() -> new ServiceException("获取推广第一轮symbol失败，请联系客服!", HttpStatus.ERROR))
                            : roundList.stream().filter(x -> x.getRoundId().equals(AdRoundType.SECOND_ROUND.getValue()))
                            .map(AdvertRound::getSymbol).findAny().orElseThrow(() -> new ServiceException("获取推广第二轮symbol失败，请联系客服!", HttpStatus.ERROR));*/
                    // 存放到redis中 有效期1天
                    redisCache.setCacheObject(symbol, filterTime, 1, TimeUnit.DAYS);
                });
    }


    /**
     * 获取当前推广轮次的过期时间
     * @param symbol 符号
     * @return
     */
    private String getDeadline(String symbol) {
        final String deadline = redisCache.getCacheObject(symbol);
        return StringUtils.isNotBlank(deadline) ? deadline : "";
    }

    /**
     * 记录竞价失败档口推广营销
     * @param failAdvert 竞价失败的推广营销
     */
    private void record(AdvertRound failAdvert) {
        // 新增推广营销历史记录 将旧档口推广营销 置为竞价失败
        AdvertRoundRecord record = BeanUtil.toBean(failAdvert, AdvertRoundRecord.class);
        record.setId(null);
        record.setAdvertRoundId(failAdvert.getId());
        // 置为竞价失败
        record.setBiddingStatus(AdBiddingStatus.BIDDING_FAIL.getValue());
        this.advertRoundRecordMapper.insert(record);
    }

    /**
     * 根据Date获取当前星期几
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
     * @param startDate 开始日期
     * @param endDate 截止日期
     * @return diffDay
     */
    public static int calculateDurationDay(Date startDate, Date endDate) {
        LocalDate start = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate end = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return (int) start.until(end, ChronoUnit.DAYS) + 1;
    }


}
