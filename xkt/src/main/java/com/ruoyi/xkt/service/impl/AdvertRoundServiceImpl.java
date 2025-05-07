package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.xkt.domain.AdvertRound;
import com.ruoyi.xkt.domain.AdvertRoundRecord;
import com.ruoyi.xkt.dto.advertRound.AdRoundStoreCreateDTO;
import com.ruoyi.xkt.dto.advertRound.AdRoundStoreResDTO;
import com.ruoyi.xkt.enums.AdBiddingStatus;
import com.ruoyi.xkt.enums.AdLaunchStatus;
import com.ruoyi.xkt.enums.AdRoundType;
import com.ruoyi.xkt.mapper.AdvertRoundMapper;
import com.ruoyi.xkt.mapper.AdvertRoundRecordMapper;
import com.ruoyi.xkt.service.IAdvertRoundService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
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
     * 获取当前类型下档口的推广营销数据
     *
     * @param storeId 档口ID
     * @param typeId  推广类型ID
     * @return AdRoundPlayStoreResDTO
     */
    @Override
    @Transactional(readOnly = true)
    public AdRoundStoreResDTO getStoreAdInfo(Long storeId, Integer typeId) {
        // 先获取所有 投放中 待投放的营销推广
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

        // 校验position位置是否必传
        this.isPositionRequired(createDTO);

        // 判断当前推广位的这一轮每个档口可以买几个，不可超买
        boolean isOverBuy = this.advertRoundMapper.isStallOverBuy(createDTO.getAdvertId(), createDTO.getRoundId(), createDTO.getStoreId(), createDTO.getPosition());
        if (isOverBuy) {
            throw new ServiceException("已购买过该推广位，不可超买哦!", HttpStatus.ERROR);
        }


        // 当前营销推广位的锁
        Object lockObj = Optional.ofNullable(advertLockMap.get(createDTO.getSymbol())).orElseThrow(() -> new ServiceException("symbol不存在!", HttpStatus.ERROR));
        // 锁当前推广营销位
        synchronized (lockObj) {
            LambdaQueryWrapper<AdvertRound> queryWrapper = new LambdaQueryWrapper<AdvertRound>()
                    .eq(AdvertRound::getAdvertId, createDTO.getAdvertId()).eq(AdvertRound::getRoundId, createDTO.getRoundId())
                    .eq(AdvertRound::getDelFlag, Constants.UNDELETED).orderByAsc(AdvertRound::getPayPrice).last("LIMIT 1");
            AdvertRound minPriceAdvert = Optional.ofNullable(this.advertRoundMapper.selectOne(queryWrapper)).orElseThrow(() -> new ServiceException("获取推广营销位置失败，请联系客服!", HttpStatus.ERROR));
            // 判断当前档口出价是否低于该推广位最低价格，若是，则提示:"已经有档口出价更高了噢，请重新出价!".
            if (createDTO.getPayPrice().compareTo(ObjectUtils.defaultIfNull(minPriceAdvert.getPayPrice(), BigDecimal.ZERO)) < 0) {
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
                    // 存放到redis中 有效期90分钟
                    redisCache.setCacheObject(symbol, filterTime, 90, TimeUnit.MINUTES);
                });
    }

    /**
     * 判断当前类型position是否必传
     * @param createDTO 购买广告位入参
     */
    private void isPositionRequired(AdRoundStoreCreateDTO createDTO) {
        // 如果是位置枚举的推广位，则需要传position
        if (Constants.posEnumTypeList.contains(createDTO.getTypeId()) && StringUtils.isEmpty(createDTO.getPosition())) {
            throw new ServiceException("当前推广类型position：必传!", HttpStatus.ERROR);
        }
        if (!Constants.posEnumTypeList.contains(createDTO.getTypeId()) && StringUtils.isNotBlank(createDTO.getPosition())) {
            throw new ServiceException("当前推广类型position：不传!", HttpStatus.ERROR);
        }
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

    // TODO 在每晚11:30起定时任务，重新梳理，每个广告位的大小顺序
    // TODO 在每晚11:30起定时任务，重新梳理，每个广告位的大小顺序


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


    // TODO 新增档口广告购买时，需要加锁，一定要锁住
    // TODO 新增档口广告购买时，需要加锁，一定要锁住
    // TODO 新增档口广告购买时，需要加锁，一定要锁住

}
