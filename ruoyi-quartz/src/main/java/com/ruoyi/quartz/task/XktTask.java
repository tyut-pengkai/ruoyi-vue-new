package com.ruoyi.quartz.task;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.SimpleEntity;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.es.EsClientWrapper;
import com.ruoyi.framework.notice.fs.FsNotice;
import com.ruoyi.system.mapper.SysDictDataMapper;
import com.ruoyi.xkt.domain.*;
import com.ruoyi.xkt.dto.account.WithdrawPrepareResult;
import com.ruoyi.xkt.dto.advertRound.pc.store.PCStoreRecommendDTO;
import com.ruoyi.xkt.dto.advertRound.pc.store.PCStoreRecommendTempDTO;
import com.ruoyi.xkt.dto.dailySale.DailySaleCusDTO;
import com.ruoyi.xkt.dto.dailySale.DailySaleDTO;
import com.ruoyi.xkt.dto.dailySale.DailySaleProdDTO;
import com.ruoyi.xkt.dto.dailySale.WeekCateSaleDTO;
import com.ruoyi.xkt.dto.dailyStoreProd.DailyStoreProdSaleDTO;
import com.ruoyi.xkt.dto.dailyStoreTag.DailyStoreTagDTO;
import com.ruoyi.xkt.dto.es.ESProductDTO;
import com.ruoyi.xkt.dto.order.StoreOrderCancelDTO;
import com.ruoyi.xkt.dto.order.StoreOrderRefund;
import com.ruoyi.xkt.dto.picture.ProductPicSyncDTO;
import com.ruoyi.xkt.dto.picture.ProductPicSyncResultDTO;
import com.ruoyi.xkt.dto.storeProdColorPrice.StoreProdMinPriceDTO;
import com.ruoyi.xkt.dto.storeProductFile.StoreProdFileLatestFourProdDTO;
import com.ruoyi.xkt.dto.storeProductFile.StoreProdFileResDTO;
import com.ruoyi.xkt.dto.useSearchHistory.UserSearchHistoryDTO;
import com.ruoyi.xkt.dto.userBrowsingHistory.UserBrowsingHisDTO;
import com.ruoyi.xkt.enums.*;
import com.ruoyi.xkt.manager.PaymentManager;
import com.ruoyi.xkt.manager.impl.ZtoExpressManagerImpl;
import com.ruoyi.xkt.mapper.*;
import com.ruoyi.xkt.service.*;
import com.ruoyi.xkt.thirdpart.zto.ZtoRegion;
import io.jsonwebtoken.lang.Assert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 鞋库通定时任务
 *
 * @author ruoyi
 */
@Slf4j
@Component("xktTask")
@RequiredArgsConstructor
public class XktTask {

    final DailySaleMapper dailySaleMapper;
    final StoreSaleMapper saleMapper;
    final StoreSaleDetailMapper saleDetailMapper;
    final StoreProductStorageMapper storageMapper;
    final DailySaleCustomerMapper dailySaleCusMapper;
    final DailySaleProductMapper dailySaleProdMapper;
    final SysProductCategoryMapper prodCateMapper;
    final WeekCateSaleMapper weekCateSaleMapper;
    final DailyStoreTagMapper dailyStoreTagMapper;
    final UserSubscriptionsMapper userSubsMapper;
    final UserFavoritesMapper userFavMapper;
    final StoreProductStockMapper stockMapper;
    final StoreMapper storeMapper;
    final StoreProductMapper storeProdMapper;
    final DailyProdTagMapper dailyProdTagMapper;
    final StoreProductCategoryAttributeMapper cateAttrMapper;
    final EsClientWrapper esClientWrapper;
    final AdvertMapper advertMapper;
    final AdvertRoundMapper advertRoundMapper;
    final RedisCache redisCache;
    final IAdvertRoundService advertRoundService;
    final IStoreOrderService storeOrderService;
    final IAssetService assetService;
    final IAlipayCallbackService alipayCallbackService;
    final FsNotice fsNotice;
    final List<PaymentManager> paymentManagers;
    final IStoreProductService storeProductService;
    final IPictureSearchService pictureSearchService;
    final PictureSearchMapper pictureSearchMapper;
    final StoreProductStatisticsMapper storeProdStatMapper;
    final StoreProductFileMapper storeProdFileMapper;
    final UserSearchHistoryMapper userSearchHisMapper;
    final UserBrowsingHistoryMapper userBrowHisMapper;
    final StoreProductColorPriceMapper prodColorPriceMapper;
    final IPictureService pictureService;
    final NoticeMapper noticeMapper;
    final UserNoticeMapper userNoticeMapper;
    final UserSubscriptionsMapper userSubMapper;
    final StoreMemberMapper storeMemberMapper;
    final IExpressService expressService;
    final ZtoExpressManagerImpl ztoExpressManager;
    final SysDictDataMapper dictDataMapper;

    public void test() throws IOException {
        System.err.println("aaa");
    }

    /**
     * 每天执行定时任务
     * 每年3月1日、6月1日、9月1日、12月1日执行 生成春夏秋冬标签
     */
    @Transactional
    public void seasonTag() {
        LocalDate today = LocalDate.now();
        int month = today.getMonthValue();
        int day = today.getDayOfMonth();
        String seasonLabel = "";
        if (month == 3 && day == 1) {
            seasonLabel = today.getYear() + "年春季";
        } else if (month == 6 && day == 1) {
            seasonLabel = today.getYear() + "年夏季";
        } else if (month == 9 && day == 1) {
            seasonLabel = today.getYear() + "年秋季";
        } else if (month == 12 && day == 1) {
            seasonLabel = today.getYear() + "年冬季";
        }
        if (StringUtils.isEmpty(seasonLabel)) {
            return;
        }
        log.info("生成季节标签：{}", seasonLabel);
        List<SysDictData> dictDataList = this.dictDataMapper.selectList(new LambdaQueryWrapper<SysDictData>()
                .eq(SysDictData::getDictType, Constants.RELEASE_YEAR_SEASON_DICT).eq(SysDictData::getDelFlag, Constants.UNDELETED)
                .eq(SysDictData::getStatus, "0"));
        // 当前最大排序
        final Long maxSort = dictDataList.stream().max(Comparator.comparingLong(SysDictData::getDictSort))
                .map(SysDictData::getDictSort).orElse(100L);
        // 往sys_dict_data表插入一条数据
        SysDictData dictData = new SysDictData();
        dictData.setDictLabel(seasonLabel);
        dictData.setDictValue(seasonLabel);
        dictData.setDictType(Constants.RELEASE_YEAR_SEASON_DICT);
        dictData.setDictSort(maxSort + 1);
        dictData.setStatus("0");
        dictData.setCreateBy("admin");
        this.dictDataMapper.insert(dictData);
    }

    /**
     * 每天凌晨12:00:01秒 更新store到redis中
     */
    public void saveStoreToRedis() {
        List<Store> storeList = this.storeMapper.selectList(new LambdaQueryWrapper<Store>()
                .eq(Store::getDelFlag, Constants.UNDELETED));
        if (CollectionUtils.isEmpty(storeList)) {
            return;
        }
        storeList.forEach(store -> {
            redisCache.setCacheObject(CacheConstants.STORE_KEY + store.getId(), store);
        });
    }

    /**
     * 凌晨12:01 更新推广轮次
     */
    @Transactional
    public void dailyAdvertRound() {
        List<Advert> advertList = this.advertMapper.selectList(new LambdaQueryWrapper<Advert>()
                .eq(Advert::getDelFlag, Constants.UNDELETED).eq(Advert::getOnlineStatus, AdOnlineStatus.ONLINE.getValue()));
        if (CollectionUtils.isEmpty(advertList)) {
            return;
        }
        // 正在投放 或 待投放列表
        List<AdvertRound> advertRoundList = this.advertRoundMapper.selectList(new LambdaQueryWrapper<AdvertRound>()
                .eq(AdvertRound::getDelFlag, Constants.UNDELETED)
                .in(AdvertRound::getLaunchStatus,
                        Arrays.asList(AdLaunchStatus.UN_LAUNCH.getValue(), AdLaunchStatus.LAUNCHING.getValue())));
        // 投放轮次按照advertId进行分组
        Map<Long, List<AdvertRound>> advertRoundMap = advertRoundList.stream().collect(Collectors.groupingBy(AdvertRound::getAdvertId));
        // 待更新或待新增的推广轮次列表
        List<AdvertRound> updateList = new ArrayList<>();
        advertList.forEach(advert -> {
            List<AdvertRound> roundList = advertRoundMap.get(advert.getId());
            // 如果没有 投放中 或 待投放的推广轮次，则 一次性创建所有的轮次
            if (CollectionUtils.isEmpty(roundList)) {
                // 播放的轮次
                for (int playRound = 0; playRound < advert.getPlayRound(); playRound++) {
                    // 如果i = 0 则表明从未创建过推广位，直接新建所有
                    final Integer launchStatus = playRound == 0 ? AdLaunchStatus.LAUNCHING.getValue() : AdLaunchStatus.UN_LAUNCH.getValue();
                    final LocalDate now = playRound == 0 ? LocalDate.now() : LocalDate.now().plusDays((long) advert.getPlayInterval() * playRound);
                    // 间隔时间
                    final LocalDate endDate = now.plusDays(advert.getPlayInterval() - 1);
                    // 按照播放数量依次生成下一轮播放的推广位
                    for (int playNum = 0; playNum < advert.getPlayNum(); playNum++) {
                        // 依次按照26个字母顺序 如果i == 0 则A i == 1 则B i==2则C
                        final String position = String.valueOf((char) ('A' + playNum));
                        // 当前播放轮次id
                        final int roundId = playRound + 1;
                        updateList.add(new AdvertRound().setAdvertId(advert.getId()).setTypeId(advert.getTypeId()).setRoundId(roundId).setLaunchStatus(launchStatus)
                                .setStartTime(java.sql.Date.valueOf(now)).setEndTime(java.sql.Date.valueOf(endDate)).setPosition(position).setStartPrice(advert.getStartPrice())
                                .setSysIntercept(AdSysInterceptType.UN_INTERCEPT.getValue()).setShowType(advert.getShowType())
                                .setDisplayType(advert.getDisplayType()).setDeadline(advert.getDeadline()).setBiddingStatus(AdBiddingStatus.UN_BIDDING.getValue())
                                .setSymbol(Objects.equals(advert.getShowType(), AdShowType.POSITION_ENUM.getValue())
                                        // 如果是位置枚举的推广位，则需要精确到某一个position的推广位，反之，若是时间范围，则直接精确到播放轮次即可
                                        ? advert.getBasicSymbol() + roundId + position : advert.getBasicSymbol() + roundId));
                    }
                }
            } else {
                // 判断当天是否为播放轮次最小结束时间的下一天 最小结束时间为：yyyy-MM-dd格式
                final String compareDateStr = DateTimeFormatter.ofPattern(DateUtils.YYYY_MM_DD).format(LocalDate.now().minusDays(1));
                final Date minEndTime = roundList.stream().min(Comparator.comparing(AdvertRound::getEndTime)).map(AdvertRound::getEndTime).orElse(null);
                final String minEndTimeStr = ObjectUtils.isNotEmpty(minEndTime) ? DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, minEndTime) : null;
                if (Objects.equals(compareDateStr, minEndTimeStr)) {
                    // 当前所有播放轮次，用于判断是否修改了配置。必须在这里直接获取才行
                    final Integer currentRoundNum = roundList.stream().map(AdvertRound::getRoundId).max(Comparator.comparingInt(x -> x)).orElse(0);
                    // 将播放轮次为1的推广轮置为：已过期
                    roundList.stream().filter(x -> Objects.equals(x.getRoundId(), AdRoundType.PLAY_ROUND.getValue())).forEach(x -> x.setLaunchStatus(AdLaunchStatus.EXPIRED.getValue()));
                    // 将播放轮次 大于 1 的推广轮 依次减1
                    roundList.stream().filter(x -> x.getRoundId() > AdRoundType.PLAY_ROUND.getValue()).forEach(x -> x.setRoundId(x.getRoundId() - 1));
                    // 将播放轮次为1 且 投放状态为：待投放的 置为投放中
                    roundList.stream().filter(x -> Objects.equals(x.getRoundId(), AdRoundType.PLAY_ROUND.getValue())
                            && Objects.equals(x.getLaunchStatus(), AdLaunchStatus.UN_LAUNCH.getValue())).forEach(x -> x.setLaunchStatus(AdLaunchStatus.LAUNCHING.getValue()));
                    // 非 “已过期”的播放轮次 重新生成每一轮的symbol
                    roundList.stream()
                            .filter(x -> !Objects.equals(x.getLaunchStatus(), AdLaunchStatus.EXPIRED.getValue()))
                            .forEach(x -> x.setSymbol(Objects.equals(advert.getShowType(), AdShowType.POSITION_ENUM.getValue())
                                    // 如果是位置枚举的推广位，则需要精确到某一个position的推广位，反之，若是时间范围，则直接精确到播放轮次即可
                                    ? advert.getBasicSymbol() + x.getRoundId() + x.getPosition() : advert.getBasicSymbol() + x.getRoundId()));
                    updateList.addAll(roundList);
                    // 需要新增最后一个待播放轮次的推广（固定增加最后一个播放轮次）
                    this.createAdvertRound(roundList, 1, advert, updateList);
                    // 如果播放轮次有更新，则需重新判断
                    final int diffRound = advert.getPlayRound() - currentRoundNum;
                    // diff < 0 代表轮次有减少，则不新增播放轮， diff == 0 则代表播放轮次不增不减，不做调整
                    if (diffRound > 0) {
                        this.createAdvertRound(roundList, diffRound, advert, updateList);
                    }
                }
            }
        });
        if (CollectionUtils.isNotEmpty(updateList)) {
            this.advertRoundMapper.insertOrUpdate(updateList);
        }
    }


    /**
     * 凌晨00:04更新symbol对应的锁资源到redis中
     */
    public void saveSymbolToRedis() {
        advertRoundService.initAdvertLockMap();
    }

    /**
     * 凌晨00:08更新各推广轮次结束时间
     */
    public void saveAdvertDeadlineToRedis() {
        // 直接调service方法，若当时redis出了问题，也方便第一时间 通过业务流程弥补 两边都有一个补偿机制
        this.advertRoundService.saveAdvertDeadlineToRedis();
    }

    /**
     * 凌晨00:10 同步档口销售数据
     */
    @Transactional
    public void dailySale() {
        // 使用LocalDate获取当前日期前一天，并转为 Date 格式
        final Date yesterday = java.sql.Date.valueOf(LocalDate.now().minusDays(1));
        // 先检查是否有该天的销售数据，若有则先删除，保证数据唯一性
        List<DailySale> existList = this.dailySaleMapper.selectList(new LambdaQueryWrapper<DailySale>()
                .eq(DailySale::getVoucherDate, yesterday).eq(DailySale::getDelFlag, Constants.UNDELETED));
        if (CollectionUtils.isNotEmpty(existList)) {
            this.dailySaleMapper.deleteByIds(existList.stream().map(DailySale::getId).collect(Collectors.toList()));
        }
        // 查询当前的销售数据
        List<DailySaleDTO> saleList = this.dailySaleMapper.selectDailySale(yesterday);
        if (CollectionUtils.isEmpty(saleList)) {
            return;
        }
        this.dailySaleMapper.insert(saleList.stream().map(x -> BeanUtil.toBean(x, DailySale.class)
                .setVoucherDate(yesterday)).collect(Collectors.toList()));
    }

    /**
     * 凌晨00:15 同步档口客户销售数据
     */
    @Transactional
    public void dailySaleCustomer() {
        // 使用LocalDate获取当前日期前一天，并转为 Date 格式
        final Date yesterday = java.sql.Date.valueOf(LocalDate.now().minusDays(1));
        // 先检查是否有该天的销售数据，若有则先删除，保证数据唯一性
        List<DailySaleCustomer> existList = this.dailySaleCusMapper.selectList(new LambdaQueryWrapper<DailySaleCustomer>()
                .eq(DailySaleCustomer::getVoucherDate, yesterday).eq(DailySaleCustomer::getDelFlag, Constants.UNDELETED));
        if (CollectionUtils.isNotEmpty(existList)) {
            this.dailySaleCusMapper.deleteByIds(existList.stream().map(DailySaleCustomer::getId).collect(Collectors.toList()));
        }
        // 查询当前的客户销售数据
        List<DailySaleCusDTO> cusSaleList = this.dailySaleCusMapper.selectDailySale(yesterday);
        if (CollectionUtils.isEmpty(cusSaleList)) {
            return;
        }
        this.dailySaleCusMapper.insert(cusSaleList.stream().map(x -> BeanUtil.toBean(x, DailySaleCustomer.class)
                .setVoucherDate(yesterday)).collect(Collectors.toList()));
    }

    /**
     * 凌晨00:20 同步档口商品销售数据
     */
    @Transactional
    public void dailySaleProduct() {
        // 使用LocalDate获取当前日期前一天，并转为 Date 格式
        final Date yesterday = java.sql.Date.valueOf(LocalDate.now().minusDays(1));
        // 先检查是否有该天的销售数据，若有则先删除，保证数据唯一性
        List<DailySaleProduct> existList = this.dailySaleProdMapper.selectList(new LambdaQueryWrapper<DailySaleProduct>()
                .eq(DailySaleProduct::getVoucherDate, yesterday).eq(DailySaleProduct::getDelFlag, Constants.UNDELETED));
        if (CollectionUtils.isNotEmpty(existList)) {
            this.dailySaleProdMapper.deleteByIds(existList.stream().map(DailySaleProduct::getId).collect(Collectors.toList()));
        }
        // 查询档口商品的销售数据
        List<DailySaleProdDTO> saleList = this.dailySaleProdMapper.selectDailySale(yesterday);
        if (CollectionUtils.isEmpty(saleList)) {
            return;
        }
        this.dailySaleProdMapper.insert(saleList.stream().map(x -> BeanUtil.toBean(x, DailySaleProduct.class)
                .setVoucherDate(yesterday)).collect(Collectors.toList()));
    }

    /**
     * 凌晨00:25 同步商品最新分类排序
     */
    @Transactional
    public void dailyCategorySort() {
        // 系统所有的商品分类
        List<SysProductCategory> cateList = this.prodCateMapper.selectList(new LambdaQueryWrapper<SysProductCategory>()
                .eq(SysProductCategory::getDelFlag, Constants.UNDELETED).eq(SysProductCategory::getStatus, Constants.UNDELETED));
        if (CollectionUtils.isEmpty(cateList)) {
            throw new ServiceException("商品分类不存在!", HttpStatus.ERROR);
        }
        // 根据LocalDate 获取当前日期前一天
        final Date yesterday = java.sql.Date.valueOf(LocalDate.now().minusDays(1));
        // 及当前日期前一天的前一周，并转为 Date 格式
        final Date pastDate = java.sql.Date.valueOf(LocalDate.now().minusDays(8));
        // 获取各项子分类最近一周的销售数量
        List<WeekCateSaleDTO> weekCateSaleList = this.weekCateSaleMapper.selectWeekCateSale(yesterday, pastDate);
        if (CollectionUtils.isEmpty(weekCateSaleList)) {
            return;
        }
        // 将各个小项销售数量转化为map
        Map<Long, Integer> itemCateCountMap = weekCateSaleList.stream().collect(Collectors.toMap(WeekCateSaleDTO::getProdCateId, WeekCateSaleDTO::getCount));
        // 按照大类对应的各小类以此进行数量统计及排序
        List<WeekCateSaleDTO> sortList = new ArrayList<>();
        cateList.stream()
                // 过滤掉父级为0的分类，以及父级为1的分类（父级为1的为子分类）
                .filter(x -> !Objects.equals(x.getParentId(), 0L) && !Objects.equals(x.getParentId(), 1L))
                .collect(Collectors.groupingBy(SysProductCategory::getParentId))
                .forEach((parentId, itemList) -> sortList.add(new WeekCateSaleDTO() {{
                    setCount(itemList.stream().mapToInt(x -> itemCateCountMap.getOrDefault(x.getId(), 0)).sum());
                    setProdCateId(parentId);
                }}));
        // 按照大类的数量倒序排列
        sortList.sort(Comparator.comparing(WeekCateSaleDTO::getCount).reversed());
        Map<Long, Integer> topCateSortMap = new LinkedHashMap<>();
        // 按照sortList的顺序，结合 topCateMap 依次更新SysProductCategory 的 sortNum的排序
        for (int i = 0; i < sortList.size(); i++) {
            topCateSortMap.put(sortList.get(i).getProdCateId(), i + 1);
        }
        // 顶级分类的数量
        Integer topCateSize = Math.toIntExact(cateList.stream().filter(x -> Objects.equals(x.getParentId(), 1L)).count());
        // 次级分类列表
        List<SysProductCategory> updateList = cateList.stream().filter(x -> Objects.equals(x.getParentId(), 1L))
                // 如果存在具体销售数量，则按照具体销售数量排序，否则将排序置为最大值
                .peek(x -> x.setOrderNum(topCateSortMap.getOrDefault(x.getId(), topCateSize)))
                .collect(Collectors.toList());
        this.prodCateMapper.updateById(updateList);
    }

    /**
     * 凌晨00:30 更新档口的标签
     */
    @Transactional
    public void dailyStoreTag() {
        // 先删除所有的档口标签，保证数据唯一性
        List<DailyStoreTag> existList = this.dailyStoreTagMapper.selectList(new LambdaQueryWrapper<DailyStoreTag>()
                .eq(DailyStoreTag::getDelFlag, Constants.UNDELETED));
        if (CollectionUtils.isNotEmpty(existList)) {
            this.dailyStoreTagMapper.deleteByIds(existList.stream().map(DailyStoreTag::getId).collect(Collectors.toList()));
        }
        List<DailyStoreTag> tagList = new ArrayList<>();
        // 单据日期为当然
        final Date now = java.sql.Date.valueOf(LocalDate.now());
        // 根据LocalDate 获取当前日期前一天
        final Date yesterday = java.sql.Date.valueOf(LocalDate.now().minusDays(1));
        // 使用LocalDate 获取当前日期前一天的前一周，并转为 Date 格式
        final Date oneWeekAgo = java.sql.Date.valueOf(LocalDate.now().minusWeeks(1));
        // 使用LocalDate 获取当前日期前一天的前一个月
        final Date oneMonthAgo = java.sql.Date.valueOf(LocalDate.now().minusMonths(1));
        // 1. 打 月销千件 标签
        this.tagSaleThousand(now, yesterday, oneMonthAgo, tagList);
        // 2. 打 销量榜 标签
        this.tagStoreSaleRank(now, yesterday, oneMonthAgo, tagList);
        // 3. 打 爆款频出 标签，根据销量前50的商品中 档口 先后顺序排列
        this.tagHotRank(now, yesterday, oneMonthAgo, tagList);
        // 4. 打 新款频出 标签，根据最近一周档口商品上新速度，先后排序
        this.tagNewProd(now, yesterday, oneWeekAgo, tagList);
        // 5. 打 关注榜 标签，根据关注量，进行排序
        this.tagAttentionRank(now, yesterday, tagList);
        // 6. 打 库存榜 标签，根据库存量，进行排序
        this.tagStockTag(yesterday, oneMonthAgo, tagList);
        // 7. 打 七日上新 标签
        this.tag7DaysNewTag(now, yesterday, oneWeekAgo, tagList);
        if (CollectionUtils.isEmpty(tagList)) {
            return;
        }
        this.dailyStoreTagMapper.insert(tagList);
    }

    /**
     * 凌晨00:40 更新商品标签
     */
    @Transactional
    public void dailyProdTag() throws IOException {
        // 先删除所有的商品标签，保证数据唯一性
        List<DailyProdTag> existList = this.dailyProdTagMapper.selectList(new LambdaQueryWrapper<DailyProdTag>()
                .eq(DailyProdTag::getDelFlag, Constants.UNDELETED));
        if (CollectionUtils.isNotEmpty(existList)) {
            this.dailyProdTagMapper.deleteByIds(existList.stream().map(DailyProdTag::getId).collect(Collectors.toList()));
        }
        final Date now = java.sql.Date.valueOf(LocalDate.now());
        // 根据LocalDate 获取当前日期前一天
        final Date yesterday = java.sql.Date.valueOf(LocalDate.now().minusDays(1));
        // 使用LocalDate 获取当前日期4天前，并转为 Date 格式
        final Date fourDaysAgo = java.sql.Date.valueOf(LocalDate.now().minusDays(4));
        // 使用LocalDate 获取当前日期前一天的前一周，并转为 Date 格式
        final Date oneWeekAgo = java.sql.Date.valueOf(LocalDate.now().minusWeeks(1));
        // 使用LocalDate 获取当前日期前一天的前一个月
        final Date oneMonthAgo = java.sql.Date.valueOf(LocalDate.now().minusMonths(1));
        List<DailyProdTag> tagList = new ArrayList<>();
        // 1. 打 月销千件 标签
        this.tagMonthSaleThousand(now, yesterday, oneMonthAgo, tagList);
        // 2. 打 销量榜 标签
        this.tagProdSaleTop10(now, yesterday, oneMonthAgo, tagList);
        // 3. 当月（近一月）爆款
        this.tagMonthHot(now, yesterday, oneMonthAgo, tagList);
        // 4. 档口热卖
        this.tagStoreHotTop20(now, yesterday, oneMonthAgo, tagList);
        // 5. 图搜榜
        this.tagImgSearchTop10(now, yesterday, oneMonthAgo, tagList);
        // 6. 收藏榜
        this.tagCollectionTop10(now, yesterday, oneMonthAgo, tagList);
        // 7. 下载榜 铺货榜
        this.tagDownloadTop10(now, yesterday, oneMonthAgo, tagList);
        // 8. 三日上新
        this.tagThreeDayNew(now, yesterday, fourDaysAgo, tagList);
        // 9. 七日上新
        this.tagSevenDayNew(now, yesterday, fourDaysAgo, oneWeekAgo, tagList);
        // 10. 风格
        this.tagStyle(now, yesterday, tagList);
        if (CollectionUtils.isEmpty(tagList)) {
            return;
        }
        this.dailyProdTagMapper.insert(tagList);
        // 更新商品的标签到ES
        this.updateESProdTags(tagList);
    }

    /**
     * 凌晨12:45 将advert的数据暂存到redis中
     */
    public void saveAdvertToRedis() {
        List<Advert> advertList = this.advertMapper.selectList(new LambdaQueryWrapper<Advert>()
                .eq(Advert::getDelFlag, Constants.UNDELETED));
        if (CollectionUtils.isEmpty(advertList)) {
            return;
        }
        advertList.forEach(advert ->
                redisCache.setCacheObject(CacheConstants.ADVERT_KEY + advert.getId(), advert, 1, TimeUnit.DAYS));
    }

    /**
     * 凌晨1:00 更新档口商品的各项权重数据
     */
    @Transactional
    public void dailyProdWeight() {
        List<StoreProduct> storeProdList = this.storeProdMapper.selectList(new LambdaQueryWrapper<StoreProduct>()
                .eq(StoreProduct::getDelFlag, Constants.UNDELETED));
        if (CollectionUtils.isEmpty(storeProdList)) {
            return;
        }
        // 获取 商品销售、商品浏览量、商品收藏量、商品下载量
        List<StoreProductStatistics> statisticsList = this.storeProdStatMapper.selectList(new LambdaQueryWrapper<StoreProductStatistics>()
                .eq(StoreProductStatistics::getDelFlag, Constants.UNDELETED));
        // 商品浏览量、下载量
        Map<Long, StoreProductStatistics> prodStatMap = statisticsList.stream().collect(Collectors.toMap(StoreProductStatistics::getStoreProdId, Function.identity()));
        // 商品收藏量
        List<UserFavorites> userFavList = this.userFavMapper.selectList(new LambdaQueryWrapper<UserFavorites>()
                .eq(UserFavorites::getDelFlag, Constants.UNDELETED));
        Map<Long, Long> userFavMap = userFavList.stream().collect(Collectors.groupingBy(UserFavorites::getStoreProdId, Collectors.summingLong(UserFavorites::getId)));
        // 商品销售量
        List<StoreSaleDetail> saleDetailList = this.saleDetailMapper.selectList(new LambdaQueryWrapper<StoreSaleDetail>()
                .eq(StoreSaleDetail::getDelFlag, Constants.UNDELETED).eq(StoreSaleDetail::getSaleType, SaleType.GENERAL_SALE.getValue()));
        Map<Long, Long> saleMap = saleDetailList.stream().collect(Collectors.groupingBy(StoreSaleDetail::getStoreProdId,
                Collectors.summingLong(StoreSaleDetail::getQuantity)));
        storeProdList.forEach(x -> {
            final long viewCount = ObjectUtils.isEmpty(prodStatMap.get(x.getId())) ? 0L : prodStatMap.get(x.getId()).getViewCount();
            final long downloadCount = ObjectUtils.isEmpty(prodStatMap.get(x.getId())) ? 0L : prodStatMap.get(x.getId()).getDownloadCount();
            final long favCount = ObjectUtils.isEmpty(userFavMap.get(x.getId())) ? 0L : userFavMap.get(x.getId());
            final long saleCount = ObjectUtils.isEmpty(saleMap.get(x.getId())) ? 0L : saleMap.get(x.getId());
            // 计算推荐权重，权重计算公式为：(浏览次数 * 0.3 + 下载次数 + 收藏次数 + 销售量 * 0.3)
            final long recommendWeight = (long) (viewCount * 0.3 + downloadCount + favCount + saleCount * 0.3);
            // 根据销售数量计算销售权重，权重占销售数量的30%
            final long saleWeight = (long) (saleCount * 0.3);
            // 计算人气权重，权重计算公式为：(浏览次数 * 0.1 和 100 取最小值) + 下载次数 + 收藏次数
            final long popularityWeight = (long) (Math.min(viewCount * 0.1, 100) + downloadCount + favCount);
            x.setRecommendWeight(recommendWeight).setSaleWeight(saleWeight).setPopularityWeight(popularityWeight);
        });
        this.storeProdMapper.updateById(storeProdList);
    }

    /**
     * 凌晨1:10 更新PC档口馆推荐列表权重数据
     */
    @Transactional
    public void dailyStoreWeight() {
        List<StoreProduct> storeProdList = this.storeProdMapper.selectList(new LambdaQueryWrapper<StoreProduct>().eq(StoreProduct::getDelFlag, Constants.UNDELETED));
        if (CollectionUtils.isEmpty(storeProdList)) {
            return;
        }
        // 按照storeId 的维度，对档口权重 按照 recommendWeight 进行汇总，按照降序排列
        Map<Long, Long> storeWeightMap = storeProdList.stream().collect(Collectors.groupingBy(StoreProduct::getStoreId,
                Collectors.summingLong(x -> ObjectUtils.defaultIfNull(x.getRecommendWeight(), 0L))));
        // 筛选每个档口最新的4个商品及主图
        List<Store> storeList = this.storeMapper.selectList(new LambdaQueryWrapper<Store>().eq(Store::getDelFlag, Constants.UNDELETED));
        Map<Long, Store> storeMap = storeList.stream().collect(Collectors.toMap(Store::getId, Function.identity()));
        List<DailyStoreTag> storeTagList = this.dailyStoreTagMapper.selectList(new LambdaQueryWrapper<DailyStoreTag>()
                .eq(DailyStoreTag::getDelFlag, Constants.UNDELETED));
        // 档口标签map
        Map<Long, List<String>> storeTagMap = storeTagList.stream().collect(Collectors
                .groupingBy(DailyStoreTag::getStoreId, Collectors.collectingAndThen(Collectors.toList(), list -> list.stream()
                        .sorted(Comparator.comparing(DailyStoreTag::getType)).map(DailyStoreTag::getTag).collect(Collectors.toList()))));
        List<StoreProdFileLatestFourProdDTO> latest4ProdList = this.storeProdFileMapper.selectLatestFourProdList();
        Map<Long, List<StoreProdFileLatestFourProdDTO>> latestProdMap = latest4ProdList.stream().collect(Collectors
                .groupingBy(StoreProdFileLatestFourProdDTO::getStoreId));
        List<PCStoreRecommendTempDTO> storeRecommendList = new ArrayList<>();
        storeWeightMap.forEach((storeId, recommendWeight) -> {
            final Store store = storeMap.get(storeId);
            storeRecommendList.add(new PCStoreRecommendTempDTO().setStoreId(storeId).setTags(storeTagMap.get(storeId))
                    .setAdvert(Boolean.FALSE).setRecommendWeight(recommendWeight)
                    .setStoreWeight(ObjectUtils.isNotEmpty(store) ? store.getStoreWeight() : null)
                    .setStoreName(ObjectUtils.isNotEmpty(store) ? store.getStoreName() : "")
                    .setStoreAddress(ObjectUtils.isNotEmpty(store) ? store.getStoreAddress() : "")
                    .setContactPhone(ObjectUtils.isNotEmpty(store) ? store.getContactPhone() : "")
                    .setQqAccount(ObjectUtils.isNotEmpty(store) ? store.getQqAccount() : "")
                    .setWechatAccount(ObjectUtils.isNotEmpty(store) ? store.getWechatAccount() : "")
                    .setProdList(BeanUtil.copyToList(latestProdMap.get(storeId), PCStoreRecommendTempDTO.PCSRNewProdDTO.class)));
        });
        if (CollectionUtils.isEmpty(storeRecommendList)) {
            return;
        }
        // 先按照 档口权重 倒序排，再按照 推荐权重 倒序排
        storeRecommendList.sort(Comparator.comparing(PCStoreRecommendTempDTO::getStoreWeight, Comparator.nullsLast(Comparator.reverseOrder()))
                .thenComparing(PCStoreRecommendTempDTO::getRecommendWeight, Comparator.nullsLast(Comparator.reverseOrder())));
        // 返回给前端的数据 不包含 storeWeight  和 storeRecommnedWeight
        List<PCStoreRecommendDTO> recommendList = BeanUtil.copyToList(storeRecommendList, PCStoreRecommendDTO.class);
        // 放到redis中
        redisCache.setCacheObject(CacheConstants.PC_STORE_RECOMMEND_LIST, recommendList);
    }

    /**
     * 凌晨01:15 更新用户搜索历史入库
     */
    @Transactional
    public void dailyUpdateUserSearchHistory() {
        Collection<String> keyList = this.redisCache.scanKeys(CacheConstants.USER_SEARCH_HISTORY + "*");
        if (CollectionUtils.isEmpty(keyList)) {
            return;
        }
        List<UserSearchHistoryDTO> redisSearchList = new ArrayList<>();
        keyList.forEach(key -> {
            List<UserSearchHistoryDTO> tempList = this.redisCache.getCacheObject(key);
            CollectionUtils.addAll(redisSearchList, tempList);
        });
        // 用户最新搜索列表
        List<UserSearchHistoryDTO> insertSearchList = redisSearchList.stream().filter(x -> ObjectUtils.isEmpty(x.getId())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(insertSearchList)) {
            this.userSearchHisMapper.insert(BeanUtil.copyToList(insertSearchList, UserSearchHistory.class));
        }
        final Date sixMonthAgo = java.sql.Date.valueOf(LocalDate.now().minusMonths(6));
        final Date now = java.sql.Date.valueOf(LocalDate.now());
        // 将最新的数据更新到redis中
        List<UserSearchHistory> latestSearchList = this.userSearchHisMapper.selectList(new LambdaQueryWrapper<UserSearchHistory>()
                .eq(UserSearchHistory::getDelFlag, Constants.UNDELETED).between(UserSearchHistory::getSearchTime, sixMonthAgo, now));
        if (CollectionUtils.isEmpty(latestSearchList)) {
            return;
        }
        latestSearchList.stream().collect(Collectors.groupingBy(UserSearchHistory::getUserId))
                .forEach((userId, list) -> {
                    // 获取用户最新搜索的20条数据
                    list = list.stream().sorted(Comparator.comparing(UserSearchHistory::getSearchTime).reversed()).limit(20).collect(Collectors.toList());
                    // 反转列表
                    Collections.reverse(list);
                    redisCache.setCacheObject(CacheConstants.USER_SEARCH_HISTORY + userId, BeanUtil.copyToList(list, UserSearchHistoryDTO.class));
                });
    }

    /**
     * 凌晨01:20 更新用户浏览记录入库
     */
    @Transactional
    public void dailyUpdateUserBrowsingHistory() {
        Collection<String> keyList = this.redisCache.scanKeys(CacheConstants.USER_BROWSING_HISTORY + "*");
        if (CollectionUtils.isEmpty(keyList)) {
            return;
        }
        List<UserBrowsingHisDTO> redisBrowsingList = new ArrayList<>();
        keyList.forEach(key -> {
            List<UserBrowsingHisDTO> tempList = this.redisCache.getCacheObject(key);
            CollectionUtils.addAll(redisBrowsingList, tempList);
        });
        // 用户最新浏览列表
        List<UserBrowsingHisDTO> insertBrowsingList = redisBrowsingList.stream().filter(x -> ObjectUtils.isEmpty(x.getId())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(insertBrowsingList)) {
            this.userBrowHisMapper.insert(BeanUtil.copyToList(insertBrowsingList, UserBrowsingHistory.class));
        }
        final Date sixMonthAgo = java.sql.Date.valueOf(LocalDate.now().minusMonths(6));
        final Date now = java.sql.Date.valueOf(LocalDate.now());
        // 将最新的数据更新到redis中
        List<UserBrowsingHistory> latestBrowsingList = this.userBrowHisMapper.selectList(new LambdaQueryWrapper<UserBrowsingHistory>()
                .eq(UserBrowsingHistory::getDelFlag, Constants.UNDELETED).between(UserBrowsingHistory::getBrowsingTime, sixMonthAgo, now));
        if (CollectionUtils.isEmpty(latestBrowsingList)) {
            return;
        }
        latestBrowsingList.stream().collect(Collectors.groupingBy(UserBrowsingHistory::getUserId))
                .forEach((userId, list) -> {
                    // 按照浏览时间升序排
                    list.sort(Comparator.comparing(UserBrowsingHistory::getBrowsingTime));
                    redisCache.setCacheObject(CacheConstants.USER_BROWSING_HISTORY + userId, BeanUtil.copyToList(list, UserBrowsingHisDTO.class));
                });
    }

    /**
     * 凌晨01:25 更新系统热搜到redis中
     */
    @Transactional
    public void dailyUpdateSearchHotToRedis() {
        Collection<String> keyList = this.redisCache.scanKeys(CacheConstants.USER_SEARCH_HISTORY + "*");
        if (CollectionUtils.isEmpty(keyList)) {
            return;
        }
        List<UserSearchHistoryDTO> redisSearchList = new ArrayList<>();
        keyList.forEach(key -> {
            List<UserSearchHistoryDTO> tempList = this.redisCache.getCacheObject(key);
            CollectionUtils.addAll(redisSearchList, tempList);
        });
        Map<String, Long> searchCountMap = redisSearchList.stream().collect(Collectors.groupingBy(UserSearchHistoryDTO::getSearchContent,
                Collectors.summingLong(UserSearchHistoryDTO::getUserId)));
        // searchCountMap 按照 value 大小倒序排，取前20条热搜
        List<Map.Entry<String, Long>> top20List = searchCountMap.entrySet().stream().sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(20).collect(Collectors.toList());
        List<String> top20Keys = top20List.stream().map(Map.Entry::getKey).collect(Collectors.toList());
        redisCache.setCacheObject(CacheConstants.SEARCH_HOT_KEY, top20Keys);
    }

    /**
     * 凌晨01:30 更新统计图搜热款
     */
    public void imgSearchTopProductStatistics() {
        log.info("-------------统计图搜热款开始-------------");
        pictureSearchService.cacheImgSearchTopProduct();
        log.info("-------------统计图搜热款结束-------------");
    }

    /**
     * 凌晨1:35 更新试用期过期档口
     */
    public void autoCloseTrialStore() {

    }

    /**
     * 凌晨1:40 更新年费过期档口
     */
    public void autoCloseTimeoutStore() {


        // TODO 更新未交年费档口
        // TODO 更新未交年费档口

    }

    /**
     * 凌晨1:45 更新档口会员过期
     */
    public void autoCloseExpireStoreMember() {
        final Date yesterday = java.sql.Date.valueOf(LocalDate.now().minusDays(1));
        // 截止昨天，会员过期的档口
        List<StoreMember> expireList = this.storeMemberMapper.selectList(new LambdaQueryWrapper<StoreMember>()
                .eq(StoreMember::getDelFlag, Constants.UNDELETED).eq(StoreMember::getEndTime, yesterday));
        if (CollectionUtils.isEmpty(expireList)) {
            return;
        }
        expireList.forEach(x -> x.setDelFlag(Constants.DELETED));
        this.storeMemberMapper.updateById(expireList);
        // 删除redis中过期会员
        expireList.forEach(x -> redisCache.deleteObject(CacheConstants.STORE_MEMBER + x.getId()));
    }

    /**
     * 凌晨1:50 将档口会员存到redis中
     */
    public void saveStoreMemberToRedis() {
        List<StoreMember> memberList = this.storeMemberMapper.selectList(new LambdaQueryWrapper<StoreMember>()
                .eq(StoreMember::getDelFlag, Constants.UNDELETED));
        if (CollectionUtils.isEmpty(memberList)) {
            return;
        }
        memberList.forEach(x -> redisCache.setCacheObject(CacheConstants.STORE_MEMBER + x.getStoreId(), x));
    }

    /**
     * 凌晨1:55 更新APP商品销量榜、分类商品销量榜
     */
    public void dailyProdTopSale() {
        final Date oneMonthAgo = java.sql.Date.valueOf(LocalDate.now().minusMonths(1));
        final Date now = java.sql.Date.valueOf(LocalDate.now());
        // 销量前100的ID列表，直接放到redis中
        List<DailyStoreProdSaleDTO> top50ProdList = this.dailySaleProdMapper.prodSaleTop50List(oneMonthAgo, now);
        if (CollectionUtils.isNotEmpty(top50ProdList)) {
            redisCache.setCacheObject(CacheConstants.TOP_50_SALE_PROD, top50ProdList);
        }
        // 按照商品分类来进行销量排序
        List<DailyStoreProdSaleDTO> cateSaleTop50ProdList = this.dailySaleProdMapper.prodCateSaleTop50List(oneMonthAgo, now);
        if (CollectionUtils.isEmpty(cateSaleTop50ProdList)) {
            return;
        }
        // 将各个分类销量数据存到redis中
        redisCache.setCacheObject(CacheConstants.CATE_TOP_50_SALE_PROD, cateSaleTop50ProdList);
    }

    /**
     * 凌晨2:00更新档口权重到redis
     */
    public void updateStoreWeightToES() throws IOException {
        // 找到昨天开通会员的所有档口
        List<StoreMember> memberList = this.storeMemberMapper.selectList(new LambdaQueryWrapper<StoreMember>()
                .eq(StoreMember::getDelFlag, Constants.UNDELETED)
                .eq(StoreMember::getLevel, StoreMemberLevel.STRENGTH_CONSTRUCT.getValue())
                .eq(StoreMember::getVoucherDate, java.sql.Date.valueOf(LocalDate.now().minusDays(1))));
        if (CollectionUtils.isEmpty(memberList)) {
            return;
        }
        final List<Long> storeIdList = memberList.stream().map(StoreMember::getStoreId).collect(Collectors.toList());
        List<Store> storeList = this.storeMapper.selectList(new LambdaQueryWrapper<Store>()
                .eq(Store::getDelFlag, Constants.UNDELETED).in(Store::getId, storeIdList));
        List<StoreProduct> storeProdList = this.storeProdMapper.selectList(new LambdaQueryWrapper<StoreProduct>()
                .eq(StoreProduct::getDelFlag, Constants.UNDELETED).in(StoreProduct::getStoreId, storeIdList));
        if (CollectionUtils.isEmpty(storeProdList)) {
            return;
        }
        // 档口权重map
        Map<Long, Integer> storeWeightMap = storeList.stream().collect(Collectors
                .toMap(Store::getId, x -> ObjectUtils.defaultIfNull(x.getStoreWeight(), 0)));
        // 构建一个批量数据集合
        List<BulkOperation> list = new ArrayList<>();
        storeProdList.forEach(storeProd -> {
            // 构建部分文档更新请求
            list.add(new BulkOperation.Builder().update(u -> u
                            .action(a -> a.doc(new HashMap<String, Object>() {{
                                put("storeWeight", ObjectUtils.defaultIfNull(storeWeightMap.get(storeProd.getStoreId()), Constants.STORE_WEIGHT_DEFAULT_ZERO));
                            }}))
                            .id(String.valueOf(storeProd.getId()))
                            .index(Constants.ES_IDX_PRODUCT_INFO))
                    .build());
        });
        try {
            // 调用bulk方法执行批量更新操作
            BulkResponse bulkResponse = esClientWrapper.getEsClient().bulk(e -> e.index(Constants.ES_IDX_PRODUCT_INFO).operations(list));
            log.info("bulkResponse.result() = {}", bulkResponse.items());
        } catch (IOException | RuntimeException e) {
            // 记录日志并抛出或处理异常
            log.error("向ES更新档口权重失败，商品ID: {}, 错误信息: {}", storeProdList.stream().map(StoreProduct::getId).collect(Collectors.toList()), e.getMessage());
            throw e; // 或者做其他补偿处理，比如异步重试
        }
    }


    /**
     * 每晚22:00:10 更新广告位竞价状态 将biddingTempStatus赋值给biddingStatus
     */
    @Transactional
    public void updateAdvertRoundBiddingStatus() throws ParseException {
        this.advertRoundService.updateBiddingStatus();
    }

    /**
     * 每个小时执行一次，发布商品
     */
    @Transactional
    public void hourPublicStoreProduct() {
        // 获取当前时间 格式化为 yyyy-MM-dd HH:00:00
        String hourTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:00:00").format(LocalDateTime.now());
        // 当前整点待发布的商品
        List<StoreProduct> unpublicList = this.storeProdMapper.selectList(new LambdaQueryWrapper<StoreProduct>()
                .eq(StoreProduct::getDelFlag, Constants.UNDELETED).eq(StoreProduct::getListingWaySchedule, hourTime)
                .eq(StoreProduct::getProdStatus, EProductStatus.UN_PUBLISHED.getValue()));
        if (CollectionUtils.isEmpty(unpublicList)) {
            return;
        }
        System.err.println(unpublicList);
        final List<String> storeProdIdList = unpublicList.stream().map(StoreProduct::getId).map(String::valueOf).collect(Collectors.toList());
        // 获取所有的商品的第一张主图
        List<StoreProdFileResDTO> mainPicList = this.storeProdFileMapper.selectMainPic(storeProdIdList);
        // 所有的商品主图map
        Map<Long, List<String>> mainPicMap = mainPicList.stream().filter(x -> Objects.equals(x.getFileType(), FileType.MAIN_PIC.getValue()))
                .collect(Collectors.groupingBy(StoreProdFileResDTO::getStoreProdId, Collectors.mapping(StoreProdFileResDTO::getFileUrl, Collectors.toList())));
        // 第一张商品主图map
        Map<Long, StoreProdFileResDTO> firstMainPicMap = mainPicList.stream().filter(x -> Objects.equals(x.getFileType(), FileType.MAIN_PIC.getValue()))
                .filter(x -> Objects.equals(x.getOrderNum(), Constants.ORDER_NUM_1)).collect(Collectors
                        .toMap(StoreProdFileResDTO::getStoreProdId, x -> x));
        // 主图视频map
        Map<Long, Boolean> mainVideoMap = mainPicList.stream().filter(x -> Objects.equals(x.getFileType(), FileType.MAIN_PIC_VIDEO.getValue()))
                .collect(Collectors.toMap(StoreProdFileResDTO::getStoreProdId, x -> true));
        // 所有的分类
        List<SysProductCategory> prodCateList = this.prodCateMapper.selectList(new LambdaQueryWrapper<SysProductCategory>()
                .eq(SysProductCategory::getDelFlag, Constants.UNDELETED));
        Map<Long, SysProductCategory> prodCateMap = prodCateList.stream().collect(Collectors.toMap(SysProductCategory::getId, x -> x));
        // 父级分类
        Map<Long, Long> parProdCateMap = prodCateList.stream().collect(Collectors.toMap(SysProductCategory::getParentId, SysProductCategory::getParentId, (s1, s2) -> s2));
        // 子分类
        Map<Long, Long> childProdCateMap = prodCateList.stream().collect(Collectors.toMap(SysProductCategory::getId, SysProductCategory::getId));
        // 获取当前商品最低价格
        Map<Long, BigDecimal> prodMinPriceMap = this.prodColorPriceMapper.selectStoreProdMinPriceList(storeProdIdList).stream().collect(Collectors
                .toMap(StoreProdMinPriceDTO::getStoreProdId, StoreProdMinPriceDTO::getPrice));
        // 档口商品的属性map
        Map<Long, StoreProductCategoryAttribute> cateAttrMap = this.cateAttrMapper.selectList(new LambdaQueryWrapper<StoreProductCategoryAttribute>()
                        .eq(StoreProductCategoryAttribute::getDelFlag, Constants.UNDELETED).in(StoreProductCategoryAttribute::getStoreProdId, storeProdIdList))
                .stream().collect(Collectors.toMap(StoreProductCategoryAttribute::getStoreProdId, x -> x));
        // 档口商品对应的档口
        Map<Long, Store> storeMap = this.storeMapper.selectList(new LambdaQueryWrapper<Store>().eq(Store::getDelFlag, Constants.UNDELETED)
                        .in(Store::getId, unpublicList.stream().map(StoreProduct::getStoreId).collect(Collectors.toList())))
                .stream().collect(Collectors.toMap(Store::getId, x -> x));
        // 构建批量操作请求
        List<BulkOperation> bulkOperations = new ArrayList<>();
        for (StoreProduct product : unpublicList) {
            final SysProductCategory cate = prodCateMap.get(product.getProdCateId());
            final SysProductCategory parCate = ObjectUtils.isEmpty(cate) ? null : prodCateMap.get(cate.getParentId());
            final Store store = storeMap.get(product.getStoreId());
            final StoreProdFileResDTO mainPic = firstMainPicMap.get(product.getId());
            final BigDecimal prodMinPrice = prodMinPriceMap.get(product.getId());
            final StoreProductCategoryAttribute cateAttr = cateAttrMap.get(product.getId());
            final Boolean hasVideo = mainVideoMap.getOrDefault(product.getId(), Boolean.FALSE);
            ESProductDTO esProductDTO = new ESProductDTO().setStoreProdId(product.getId().toString()).setProdArtNum(product.getProdArtNum())
                    .setHasVideo(hasVideo).setProdCateId(product.getProdCateId().toString()).setCreateTime(DateUtils.getTime())
                    .setProdCateName(ObjectUtils.isNotEmpty(cate) ? cate.getName() : "")
                    .setSaleWeight("0").setRecommendWeight("0").setPopularityWeight("0")
                    .setMainPicUrl(ObjectUtils.isNotEmpty(mainPic) ? mainPic.getFileUrl() : "")
                    .setMainPicName(ObjectUtils.isNotEmpty(mainPic) ? mainPic.getFileName() : "")
                    .setMainPicSize(ObjectUtils.isNotEmpty(mainPic) ? mainPic.getFileSize() : BigDecimal.ZERO)
                    .setParCateId(ObjectUtils.isNotEmpty(parCate) ? parCate.getId().toString() : "")
                    .setParCateName(ObjectUtils.isNotEmpty(parCate) ? parCate.getName() : "")
                    .setProdPrice(ObjectUtils.isNotEmpty(prodMinPrice) ? prodMinPrice.toString() : "")
                    .setSeason(ObjectUtils.isNotEmpty(cateAttr) ? cateAttr.getSuitableSeason() : "")
                    .setProdStatus(product.getProdStatus().toString())
                    .setStoreId(product.getStoreId().toString())
                    .setStoreName(ObjectUtils.isNotEmpty(store) ? store.getStoreName() : "")
                    .setStyle(ObjectUtils.isNotEmpty(cateAttr) ? cateAttr.getStyle() : "")
                    .setTags(ObjectUtils.isNotEmpty(cateAttr) ? Collections.singletonList(cateAttr.getStyle()) : new ArrayList<>())
                    .setProdTitle(product.getProdTitle());
            // 创建更新操作
            BulkOperation bulkOperation = new BulkOperation.Builder()
                    .index(i -> i
                            // 使用商品ID作为文档ID
                            .id(String.valueOf(product.getId()))
                            // 索引名称
                            .index(Constants.ES_IDX_PRODUCT_INFO)
                            // 插入的数据
                            .document(esProductDTO))
                    .build();
            bulkOperations.add(bulkOperation);
            this.createNotice(product, store.getStoreName());
        }
        // 执行批量插入
        try {
            BulkResponse response = esClientWrapper.getEsClient().bulk(b -> b.index(Constants.ES_IDX_PRODUCT_INFO).operations(bulkOperations));
            log.info("批量插入到 ES 成功，共处理 {} 条记录", response.items().size());
        } catch (Exception e) {
            log.error("批量插入到 ES 失败", e);
        }
        for (StoreProduct product : unpublicList) {
            List<String> mainPicUrlList = mainPicMap.get(product.getId());
            if (CollUtil.isEmpty(mainPicUrlList)) {
                return;
            }
            this.sync2ImgSearchServer(product.getId(), mainPicUrlList, true);
        }
    }


    /**
     * 自动关闭超时订单
     */
    public void autoCloseTimeoutStoreOrder() {
        log.info("-------------自动关闭超时订单开始-------------");
        Integer batchCount = 20;
        Date beforeDate = DateUtil.offset(new Date(), DateField.MINUTE, 60);
        List<Long> storeOrderIds = storeOrderService.listNeedAutoCloseOrder(beforeDate, batchCount);
        for (Long storeOrderId : storeOrderIds) {
            log.info("开始处理: {}", storeOrderId);
            try {
                StoreOrderCancelDTO cancelDTO = new StoreOrderCancelDTO();
                cancelDTO.setStoreOrderId(storeOrderId);
                cancelDTO.setRemark("超时订单自动关闭");
                storeOrderService.cancelOrder(cancelDTO);
            } catch (Exception e) {
                log.error("自动关闭超时订单异常", e);
                fsNotice.sendMsg2DefaultChat("自动关闭超时订单异常", "订单ID:" + storeOrderId);
            }
        }
        log.info("-------------自动关闭超时订单结束-------------");
    }

    public void autoCompleteStoreOrder() {
        log.info("-------------自动完成订单开始-------------");
        Integer batchCount = 20;
        Date beforeDate = DateUtil.offset(new Date(), DateField.DAY_OF_YEAR, 14);
        List<Long> storeOrderIds = storeOrderService.listNeedAutoCompleteOrder(beforeDate, batchCount);
        for (Long storeOrderId : storeOrderIds) {
            log.info("开始处理: {}", storeOrderId);
            try {
                storeOrderService.completeOrder(storeOrderId, -1L);
            } catch (Exception e) {
                log.error("自动完成订单异常", e);
                fsNotice.sendMsg2DefaultChat("自动完成订单异常", "订单ID:" + storeOrderId);
            }
        }
        log.info("-------------自动完成订单结束-------------");
    }

    public void autoRefundStoreOrder() {
        log.info("-------------自动订单退款开始-------------");
        Integer batchCount = 20;
        List<Long> storeOrderIds = storeOrderService.listNeedAutoRefundOrder(batchCount);
        for (Long storeOrderId : storeOrderIds) {
            log.info("开始处理: {}", storeOrderId);
            try {
                StoreOrderRefund storeOrderRefund = storeOrderService.prepareRefundByOriginOrder(storeOrderId);
                callRefund(storeOrderRefund);
            } catch (Exception e) {
                log.error("自动完成订单异常", e);
                fsNotice.sendMsg2DefaultChat("自动完成订单异常", "订单ID:" + storeOrderId);
            }
        }
        log.info("-------------自动订单退款结束-------------");
    }

    /**
     * 继续处理退款（异常中断补偿，非正常流程）
     */
    public void continueProcessRefund() {
        log.info("-------------继续处理退款开始-------------");
        Integer batchCount = 20;
        List<StoreOrderRefund> storeOrderRefunds = storeOrderService.listNeedContinueRefundOrder(batchCount);
        for (StoreOrderRefund storeOrderRefund : storeOrderRefunds) {
            log.info("开始处理: {}", storeOrderRefund);
            callRefund(storeOrderRefund);
        }
        log.info("-------------继续处理退款结束-------------");
    }

    private void callRefund(StoreOrderRefund storeOrderRefund) {
        try {
            //支付宝接口要求：同一笔交易的退款至少间隔3s后发起
            String markKey = CacheConstants.STORE_ORDER_REFUND_PROCESSING_MARK +
                    storeOrderRefund.getRefundOrder().getId();
            boolean less3s = redisCache.hasKey(markKey);
            if (less3s) {
                log.warn("订单[{}]退款间隔小于3s跳过执行", storeOrderRefund.getRefundOrder().getId());
                return;
            }
            PaymentManager paymentManager = getPaymentManager(EPayChannel.of(storeOrderRefund.getRefundOrder().getPayChannel()));
            //查询退款结果
            ENetResult queryResult = paymentManager.queryStoreOrderRefundResult(
                    storeOrderRefund.getRefundOrder().getOrderNo(),
                    storeOrderRefund.getOriginOrder().getOrderNo());
            if (ENetResult.SUCCESS == queryResult) {
                //退款成功
                //支付状态->已支付，收款单到账
                storeOrderService.refundSuccess(storeOrderRefund.getRefundOrder().getId(),
                        storeOrderRefund.getRefundOrderDetails().stream().map(SimpleEntity::getId).collect(Collectors.toList()),
                        null);
            } else {
                //可能是退款失败，也可能是退款处理中，重复调用支付宝接口时只要参数正确也不会重复退款
                boolean success = paymentManager.refundStoreOrder(storeOrderRefund);
                //标记
                redisCache.setCacheObject(markKey, 1, 3, TimeUnit.SECONDS);
                if (success) {
                    //支付状态->已支付，收款单到账
                    storeOrderService.refundSuccess(storeOrderRefund.getRefundOrder().getId(),
                            storeOrderRefund.getRefundOrderDetails().stream().map(SimpleEntity::getId).collect(Collectors.toList()),
                            null);
                } else {
                    fsNotice.sendMsg2DefaultChat("退款失败", "参数: " + JSON.toJSONString(storeOrderRefund));
                }
            }
        } catch (Exception e) {
            log.error("继续处理退款异常", e);
            fsNotice.sendMsg2DefaultChat("退款异常", "参数: " + JSON.toJSONString(storeOrderRefund));
        }
    }

    /**
     * 继续处理档口提现（异常中断补偿，非正常流程）
     */
    public void continueProcessWithdraw() {
        log.info("-------------继续处理档口提现开始-------------");
        Integer batchCount = 20;
        Map<EPayChannel, List<WithdrawPrepareResult>> map = assetService.getNeedContinueWithdrawGroupMap(batchCount);
        for (Map.Entry<EPayChannel, List<WithdrawPrepareResult>> entry : map.entrySet()) {
            PaymentManager paymentManager = getPaymentManager(entry.getKey());
            for (WithdrawPrepareResult prepareResult : entry.getValue()) {
                log.info("开始处理: {}", prepareResult);
                try {
                    //查询转账结果
                    ENetResult queryResult = paymentManager.queryTransferResult(prepareResult.getBillNo());
                    if (ENetResult.SUCCESS == queryResult) {
                        //转账成功，直接到账
                        assetService.withdrawSuccess(prepareResult.getFinanceBillId());
                    } else if (ENetResult.FAILURE == queryResult) {
                        //转账失败，尝试重新支付
                        boolean success = paymentManager.transfer(prepareResult.getBillNo(),
                                prepareResult.getAccountOwnerNumber(),
                                prepareResult.getAccountOwnerName(),
                                prepareResult.getAmount());
                        //付款单到账
                        if (success) {
                            assetService.withdrawSuccess(prepareResult.getFinanceBillId());
                        } else {
                            fsNotice.sendMsg2DefaultChat("档口提现失败", "参数: " + JSON.toJSONString(prepareResult));
                        }
                    } else {
                        log.warn("档口提现支付宝处理中: {}", prepareResult);
                    }
                } catch (Exception e) {
                    log.error("继续继续处理档口提现异常", e);
                    fsNotice.sendMsg2DefaultChat("档口提现异常", "参数: " + JSON.toJSONString(prepareResult));
                }
            }
        }
        log.info("-------------继续处理档口提现结束-------------");
    }

    /**
     * 继续处理支付宝支付回调信息（异常中断补偿，非正常流程）
     */
    public void continueProcessAliCallback() {
        log.info("-------------继续处理支付宝支付回调信息开始-------------");
        Integer batchCount = 20;
        List<AlipayCallback> callbacks = alipayCallbackService.listNeedContinueProcessCallback(batchCount);
        for (AlipayCallback callback : callbacks) {
            log.info("开始处理: {}", callback);
            try {
                alipayCallbackService.continueProcess(Collections.singletonList(callback));
            } catch (Exception e) {
                log.error("继续处理支付宝支付回调异常", e);
                fsNotice.sendMsg2DefaultChat("支付回调处理异常", "回调信息: " + JSON.toJSONString(callback));
            }
        }
        log.info("-------------继续处理支付宝支付回调信息结束-------------");
    }

    /**
     * 商品当日浏览量、下载量、图搜次数统计
     */
    public void dailyProductStatistics() {
        log.info("-------------商品信息每日统计开始-------------");
        Map<String, Integer> iscMap = redisCache.getCacheMap(CacheConstants.PRODUCT_STATISTICS_IMG_SEARCH_COUNT);
        Map<String, Integer> vcMap = redisCache.getCacheMap(CacheConstants.PRODUCT_STATISTICS_VIEW_COUNT);
        Map<String, Integer> dcMap = redisCache.getCacheMap(CacheConstants.PRODUCT_STATISTICS_DOWNLOAD_COUNT);
        Set<String> storeProdIds = MapUtil.emptyIfNull(iscMap).keySet();
        storeProdIds.addAll(MapUtil.emptyIfNull(vcMap).keySet());
        storeProdIds.addAll(MapUtil.emptyIfNull(dcMap).keySet());
        Date now = new Date();
        for (String storeProdId : storeProdIds) {
            try {
                // 保存到数据库
                storeProductService.insertOrUpdateProductStatistics(Long.parseLong(storeProdId),
                        vcMap.get(storeProdId), dcMap.get(storeProdId), iscMap.get(storeProdId), now);
                // 清除当日缓存
                redisCache.deleteCacheMapValue(CacheConstants.PRODUCT_STATISTICS_IMG_SEARCH_COUNT, storeProdId);
                redisCache.deleteCacheMapValue(CacheConstants.PRODUCT_STATISTICS_VIEW_COUNT, storeProdId);
                redisCache.deleteCacheMapValue(CacheConstants.PRODUCT_STATISTICS_DOWNLOAD_COUNT, storeProdId);
            } catch (Exception e) {
                log.error("商品信息每日统计异常:" + storeProdId, e);
            }
        }
        // 清除当日缓存
//        redisCache.deleteObject(CacheConstants.PRODUCT_STATISTICS_IMG_SEARCH_COUNT);
//        redisCache.deleteObject(CacheConstants.PRODUCT_STATISTICS_VIEW_COUNT);
//        redisCache.deleteObject(CacheConstants.PRODUCT_STATISTICS_DOWNLOAD_COUNT);
        log.info("-------------商品信息每日统计结束-------------");
    }


    /**
     * 给商品打销量过千标签
     *
     * @param now         今天
     * @param yesterday   昨天
     * @param oneMonthAgo 一月前
     * @param tagList     标签列表
     */
    private void tagMonthSaleThousand(Date now, Date yesterday, Date oneMonthAgo, List<DailyProdTag> tagList) {
        List<DailyStoreTagDTO> prodSale1000List = this.dailySaleProdMapper.prodSale1000List(oneMonthAgo, yesterday);
        if (CollectionUtils.isEmpty(prodSale1000List)) {
            return;
        }
        tagList.addAll(prodSale1000List.stream().map(x -> DailyProdTag.builder().storeId(x.getStoreId()).storeProdId(x.getStoreProdId())
                .tag(ProdTagType.MONTH_SALES_THOUSAND.getLabel()).type(ProdTagType.MONTH_SALES_THOUSAND.getValue())
                .voucherDate(now).build()).collect(Collectors.toList()));
    }

    /**
     * 给商品打销量榜标签
     *
     * @param now         今天
     * @param yesterday   昨天
     * @param oneMonthAgo 一月前
     * @param tagList     标签列表
     */
    private void tagProdSaleTop10(Date now, Date yesterday, Date oneMonthAgo, List<DailyProdTag> tagList) {
        List<DailyStoreTagDTO> prodSaleTop10List = this.dailySaleProdMapper.prodSaleTop10List(oneMonthAgo, yesterday);
        if (CollectionUtils.isEmpty(prodSaleTop10List)) {
            return;
        }
        int saleRankType = ProdTagType.SALES_RANK.getValue();
        // 定义标签映射规则
        Map<Integer, String> rankTags = new HashMap<>();
        rankTags.put(0, "销量第一");
        rankTags.put(1, "销量第二");
        rankTags.put(2, "销量前三");
        rankTags.put(3, "销量前五");
        rankTags.put(4, "销量前五");
        // 遍历 top10List 并生成标签 确保不会超出 top10List 的大小
        for (int i = 0; i < Math.min(prodSaleTop10List.size(), 10); i++) {
            DailyStoreTagDTO tagDTO = prodSaleTop10List.get(i);
            // 构建 DailyStoreTag 对象并添加到 tagList
            tagList.add(DailyProdTag.builder().storeId(tagDTO.getStoreId()).storeProdId(tagDTO.getStoreProdId()).type(saleRankType)
                    .tag(rankTags.getOrDefault(i, "销量前十")).voucherDate(now).build());
        }
    }

    /**
     * 给商品打图搜标签
     *
     * @param now         现在
     * @param yesterday   昨天
     * @param oneMonthAgo 一月前
     * @param tagList     标签列表
     */
    private void tagImgSearchTop10(Date now, Date yesterday, Date oneMonthAgo, List<DailyProdTag> tagList) {
        List<DailyStoreTagDTO> imgSearchTop10List = this.storeProdStatMapper.searchTop10Prod(oneMonthAgo, yesterday);
        if (CollectionUtils.isEmpty(imgSearchTop10List)) {
            return;
        }
        int imgSearchType = ProdTagType.IMG_SEARCH_RANK.getValue();
        // 定义标签映射规则
        Map<Integer, String> rankTags = new HashMap<>();
        rankTags.put(0, "图搜第一");
        rankTags.put(1, "图搜第二");
        rankTags.put(2, "图搜前三");
        rankTags.put(3, "图搜前五");
        rankTags.put(4, "图搜前五");
        // 遍历 imgSearchTop10List 并生成标签 确保不会超出 top10List 的大小
        for (int i = 0; i < Math.min(imgSearchTop10List.size(), 10); i++) {
            DailyStoreTagDTO tagDTO = imgSearchTop10List.get(i);
            // 构建 DailyProdTag 对象并添加到 tagList
            tagList.add(DailyProdTag.builder().storeId(tagDTO.getStoreId()).storeProdId(tagDTO.getStoreProdId()).type(imgSearchType)
                    .tag(rankTags.getOrDefault(i, "图搜前十")).voucherDate(now).build());
        }
    }

    /**
     * 给商品打收藏榜前10标签
     *
     * @param now         现在
     * @param yesterday   昨天
     * @param oneMonthAgo 一月前
     * @param tagList     标签列表
     */
    private void tagCollectionTop10(Date now, Date yesterday, Date oneMonthAgo, List<DailyProdTag> tagList) {
        List<DailyStoreTagDTO> collectTop10List = this.userFavMapper.searchTop10Prod(oneMonthAgo, yesterday);
        if (CollectionUtils.isEmpty(collectTop10List)) {
            return;
        }
        int imgSearchType = ProdTagType.COLLECTION_RANK.getValue();
        // 定义标签映射规则
        Map<Integer, String> rankTags = new HashMap<>();
        rankTags.put(0, "收藏第一");
        rankTags.put(1, "收藏第二");
        rankTags.put(2, "收藏前三");
        rankTags.put(3, "收藏前五");
        rankTags.put(4, "收藏前五");
        // 遍历 collectTop10List 并生成标签 确保不会超出 top10List 的大小
        for (int i = 0; i < Math.min(collectTop10List.size(), 10); i++) {
            DailyStoreTagDTO tagDTO = collectTop10List.get(i);
            // 构建 DailyProdTag 对象并添加到 tagList
            tagList.add(DailyProdTag.builder().storeId(tagDTO.getStoreId()).storeProdId(tagDTO.getStoreProdId()).type(imgSearchType)
                    .tag(rankTags.getOrDefault(i, "收藏前十")).voucherDate(now).build());
        }
    }

    /**
     * 给商品打下载榜前10标签
     *
     * @param now         现在
     * @param yesterday   昨天
     * @param oneMonthAgo 一月前
     * @param tagList     标签列表
     */
    private void tagDownloadTop10(Date now, Date yesterday, Date oneMonthAgo, List<DailyProdTag> tagList) {
        List<DailyStoreTagDTO> downloadTop10List = this.storeProdStatMapper.downloadTop10Prod(oneMonthAgo, yesterday);
        if (CollectionUtils.isEmpty(downloadTop10List)) {
            return;
        }
        int downloadType = ProdTagType.DOWNLOAD_RANK.getValue();
        // 定义标签映射规则
        Map<Integer, String> rankTags = new HashMap<>();
        rankTags.put(0, "铺货第一");
        rankTags.put(1, "铺货第二");
        rankTags.put(2, "铺货前三");
        rankTags.put(3, "铺货前五");
        rankTags.put(4, "铺货前五");
        // 遍历 downloadTop10List 并生成标签 确保不会超出 top10List 的大小
        for (int i = 0; i < Math.min(downloadTop10List.size(), 10); i++) {
            DailyStoreTagDTO tagDTO = downloadTop10List.get(i);
            // 构建 DailyProdTag 对象并添加到 tagList
            tagList.add(DailyProdTag.builder().storeId(tagDTO.getStoreId()).storeProdId(tagDTO.getStoreProdId()).type(downloadType)
                    .tag(rankTags.getOrDefault(i, "铺货前十")).voucherDate(now).build());
        }
    }

    /**
     * 给商品打风格标签
     *
     * @param now       今天
     * @param yesterday 昨天
     * @param tagList   标签列表
     */
    private void tagStyle(Date now, Date yesterday, List<DailyProdTag> tagList) {
        List<StoreProductCategoryAttribute> cateAttrList = this.cateAttrMapper.selectList(new LambdaQueryWrapper<StoreProductCategoryAttribute>()
                .eq(StoreProductCategoryAttribute::getDelFlag, Constants.UNDELETED));
        if (CollectionUtils.isEmpty(cateAttrList)) {
            return;
        }
        tagList.addAll(cateAttrList.stream().filter(x -> StringUtils.isNotBlank(x.getStyle()))
                .map(x -> DailyProdTag.builder().storeId(x.getStoreId()).storeProdId(x.getStoreProdId())
                        .tag(x.getStyle()).type(ProdTagType.STYLE.getValue()).voucherDate(now).build())
                .collect(Collectors.toList()));
    }

    /**
     * 给商品打标 七日上新
     *
     * @param now         现在
     * @param yesterday   昨天
     * @param fourDaysAgo 4天前
     * @param oneWeekAgo  一周前
     * @param tagList     标签列表
     */
    private void tagSevenDayNew(Date now, Date yesterday, Date fourDaysAgo, Date oneWeekAgo, List<DailyProdTag> tagList) {
        List<StoreProduct> storeProdList = this.storeProdMapper.selectList(new LambdaQueryWrapper<StoreProduct>()
                .eq(StoreProduct::getDelFlag, Constants.UNDELETED).between(StoreProduct::getCreateTime, oneWeekAgo, fourDaysAgo));
        if (CollectionUtils.isEmpty(storeProdList)) {
            return;
        }
        tagList.addAll(storeProdList.stream().map(x -> DailyProdTag.builder().storeId(x.getStoreId()).storeProdId(x.getId())
                        .type(ProdTagType.SEVEN_DAY_NEW.getValue()).tag(ProdTagType.SEVEN_DAY_NEW.getLabel()).voucherDate(now).build())
                .collect(Collectors.toList()));
    }

    /**
     * 三日上新
     *
     * @param now         现在
     * @param yesterday   昨天
     * @param fourDaysAgo 3天前
     * @param tagList     标签列表
     */
    private void tagThreeDayNew(Date now, Date yesterday, Date fourDaysAgo, List<DailyProdTag> tagList) {
        List<StoreProduct> storeProdList = this.storeProdMapper.selectList(new LambdaQueryWrapper<StoreProduct>()
                .eq(StoreProduct::getDelFlag, Constants.UNDELETED).between(StoreProduct::getCreateTime, fourDaysAgo, yesterday));
        if (CollectionUtils.isEmpty(storeProdList)) {
            return;
        }
        tagList.addAll(storeProdList.stream().map(x -> DailyProdTag.builder().storeId(x.getStoreId()).storeProdId(x.getId())
                .type(ProdTagType.THREE_DAY_NEW.getValue()).tag(ProdTagType.THREE_DAY_NEW.getLabel())
                .voucherDate(now).build()).collect(Collectors.toList()));
    }

    /**
     * 筛选档口热卖商品
     *
     * @param now
     * @param yesterday   昨天
     * @param oneMonthAgo 一月前
     * @param tagList     标签集合
     */
    private void tagStoreHotTop20(Date now, Date yesterday, Date oneMonthAgo, List<DailyProdTag> tagList) {
        List<DailySaleProduct> saleProdList = this.dailySaleProdMapper.selectList(new LambdaQueryWrapper<DailySaleProduct>()
                .eq(DailySaleProduct::getDelFlag, Constants.UNDELETED).eq(DailySaleProduct::getVoucherDate, now));
        if (CollectionUtils.isEmpty(saleProdList)) {
            return;
        }
        // 筛选每个档口，销量排名前20的商品
        Map<Long, List<DailySaleProduct>> storeHotSaleMap = saleProdList.stream().collect(Collectors
                .groupingBy(DailySaleProduct::getStoreId, Collectors
                        .collectingAndThen(Collectors.toList(), list -> list.stream().limit(20).collect(Collectors.toList()))));
        storeHotSaleMap.forEach((storeId, saleList) -> {
            tagList.addAll(saleList.stream().map(x -> DailyProdTag.builder().storeId(x.getStoreId()).storeProdId(x.getStoreProdId())
                            .type(ProdTagType.STORE_HOT.getValue()).tag(ProdTagType.STORE_HOT.getLabel()).voucherDate(now).build())
                    .collect(Collectors.toList()));
        });
    }

    /**
     * 给商品打标 本月爆款
     *
     * @param now         今天
     * @param yesterday   昨天
     * @param oneMonthAgo 一月前
     * @param tagList     标签列表
     */
    private void tagMonthHot(Date now, Date yesterday, Date oneMonthAgo, List<DailyProdTag> tagList) {
        List<DailyStoreTagDTO> top50List = this.dailySaleProdMapper.selectTop50ProdList(yesterday, oneMonthAgo);
        if (CollectionUtils.isEmpty(top50List)) {
            return;
        }
        tagList.addAll(top50List.stream().map(x -> DailyProdTag.builder().storeId(x.getStoreId()).storeProdId(x.getStoreProdId())
                        .tag(ProdTagType.MONTH_HOT.getLabel()).type(ProdTagType.MONTH_HOT.getValue()).voucherDate(now).build())
                .collect(Collectors.toList()));
    }


    /**
     * 档口基础标签
     *
     * @param now        今天
     * @param yesterday  昨日
     * @param oneWeekAgo 一周前
     * @param tagList    标签列表
     */
    private void tag7DaysNewTag(Date now, Date yesterday, Date oneWeekAgo, List<DailyStoreTag> tagList) {
        List<StoreProduct> newProdList = this.storeProdMapper.selectList(new LambdaQueryWrapper<StoreProduct>()
                .eq(StoreProduct::getDelFlag, Constants.UNDELETED)
                .in(StoreProduct::getProdStatus, Collections.singletonList(EProductStatus.ON_SALE.getValue()))
                .between(StoreProduct::getCreateTime, oneWeekAgo, yesterday));
        if (CollectionUtils.isEmpty(newProdList)) {
            return;
        }
        newProdList.stream().map(StoreProduct::getStoreId).distinct().forEach(x -> {
            tagList.add(DailyStoreTag.builder().storeId(x).type(StoreTagType.SEVEN_DAY_NEW_RANK.getValue())
                    .tag("七日上新").voucherDate(yesterday).build());
        });
    }

    /**
     * 给档口打库存标签
     *
     * @param yesterday   昨天
     * @param oneMonthAgo 一月前
     * @param tagList     标签列表
     */
    private void tagStockTag(Date yesterday, Date oneMonthAgo, List<DailyStoreTag> tagList) {
        List<DailyStoreTagDTO> top10List = this.stockMapper.selectTop10List(yesterday, oneMonthAgo);
        if (CollectionUtils.isEmpty(top10List)) {
            return;
        }
        tagList.addAll(top10List.stream().map(x -> DailyStoreTag.builder().storeId(x.getStoreId()).type(StoreTagType.STOCK_RANK.getValue())
                .tag("库存充足").voucherDate(yesterday).build()).collect(Collectors.toList()));
    }


    /**
     * 给档口打关注标签
     *
     * @param now       今天
     * @param yesterday 昨天
     * @param tagList   档口标签列表
     */
    private void tagAttentionRank(Date now, Date yesterday, List<DailyStoreTag> tagList) {
        List<DailyStoreTagDTO> top10List = this.userSubsMapper.selectTop10List();
        if (CollectionUtils.isEmpty(top10List)) {
            return;
        }
        int attentionRankType = StoreTagType.ATTENTION_RANK.getValue();
        // 定义标签映射规则
        Map<Integer, String> rankTags = new HashMap<>();
        rankTags.put(0, "关注第一");
        rankTags.put(1, "关注第二");
        rankTags.put(2, "关注前三");
        rankTags.put(3, "关注前五");
        rankTags.put(4, "关注前五");
        // 遍历 top10List 并生成标签
        for (int i = 0; i < Math.min(top10List.size(), 10); i++) { // 确保不会超出 top10List 的大小
            // 构建 DailyStoreTag 对象并添加到 tagList
            tagList.add(DailyStoreTag.builder().storeId(top10List.get(i).getStoreId()).type(attentionRankType)
                    .tag(rankTags.getOrDefault(i, "关注前十")).voucherDate(now).build());
        }
    }

    /**
     * 给档口打销量过千标签
     *
     * @param now         今天
     * @param yesterday   昨天
     * @param oneMonthAgo 一月前
     * @param tagList     档口标签列表
     */
    private void tagSaleThousand(Date now, Date yesterday, Date oneMonthAgo, List<DailyStoreTag> tagList) {
        List<DailyStoreTagDTO> thousandSaleList = this.dailySaleMapper.selectSaleThousand(yesterday, oneMonthAgo);
        if (CollectionUtils.isEmpty(thousandSaleList)) {
            return;
        }
        tagList.addAll(thousandSaleList.stream().map(x -> DailyStoreTag.builder().storeId(x.getStoreId()).voucherDate(now)
                        .type(StoreTagType.MONTH_SALES_THOUSAND.getValue()).tag(StoreTagType.MONTH_SALES_THOUSAND.getLabel()).build())
                .collect(Collectors.toList()));
    }

    /**
     * 统计最近一月档口销售数据。 1. 销量榜 规则：销量排名第1，打标：销量第一  销量第2、第3，打标：销量前三 ；
     * 销量前5，打标：销量前五；销量第6-10，打标：销量前十
     *
     * @param now         当天
     * @param yesterday   昨天
     * @param oneMonthAgo 一月前
     * @param tagList     档口标签列表
     */
    private void tagStoreSaleRank(Date now, Date yesterday, Date oneMonthAgo, List<DailyStoreTag> tagList) {
        // 获取最近一月销量前10的档口
        List<DailyStoreTagDTO> saleTop10List = this.dailySaleMapper.selectTop10List(yesterday, oneMonthAgo);
        if (CollectionUtils.isEmpty(saleTop10List)) {
            return;
        }
        int salesRankType = StoreTagType.SALES_RANK.getValue();
        // 定义标签映射规则
        Map<Integer, String> rankTags = new HashMap<>();
        rankTags.put(0, "销量第一");
        rankTags.put(1, "销量第二");
        rankTags.put(2, "销量前三");
        rankTags.put(3, "销量前五");
        rankTags.put(4, "销量前五");
        // 遍历 saleTop10List 并生成标签
        for (int i = 0; i < Math.min(10, saleTop10List.size()); i++) { // 确保不会超出 saleTop10List 的大小
            DailyStoreTagDTO storeTagDTO = saleTop10List.get(i);
            if (ObjectUtils.isNotEmpty(storeTagDTO)) {
                String tag = rankTags.getOrDefault(i, "销量前十");
                tagList.add(DailyStoreTag.builder().storeId(storeTagDTO.getStoreId()).type(salesRankType)
                        .tag(tag).voucherDate(now).build());
            }
        }
    }

    /**
     * 打销量爆款标签
     *
     * @param now         当天
     * @param yesterday   昨天
     * @param oneMonthAgo 一月前
     * @param tagList     档口标签列表
     */
    private void tagHotRank(Date now, Date yesterday, Date oneMonthAgo, List<DailyStoreTag> tagList) {
        List<DailyStoreTagDTO> top50List = this.dailySaleProdMapper.selectTop50ProdList(yesterday, oneMonthAgo);
        if (CollectionUtils.isEmpty(top50List)) {
            return;
        }
        tagList.addAll(top50List.stream().map(DailyStoreTagDTO::getStoreId).distinct().map(storeId -> DailyStoreTag.builder()
                        .storeId(storeId).type(StoreTagType.HOT_RANK.getValue()).tag(StoreTagType.HOT_RANK.getLabel())
                        .voucherDate(now).build())
                .collect(Collectors.toList()));
    }

    /**
     * 给档口打新品频出标签
     *
     * @param now        今天
     * @param yesterday  昨天
     * @param oneWeekAgo 一周前
     * @param tagList    标签列表
     */
    private void tagNewProd(Date now, Date yesterday, Date oneWeekAgo, List<DailyStoreTag> tagList) {
        List<DailyStoreTagDTO> top20List = this.storeProdMapper.selectTop20List(yesterday, oneWeekAgo);
        if (CollectionUtils.isEmpty(top20List)) {
            return;
        }
        tagList.addAll(top20List.stream().map(DailyStoreTagDTO::getStoreId).distinct().map(storeId -> DailyStoreTag.builder()
                        .storeId(storeId).type(StoreTagType.NEW_PRODUCT.getValue()).tag(StoreTagType.NEW_PRODUCT.getLabel())
                        .voucherDate(now).build())
                .collect(Collectors.toList()));
    }


    /**
     * 更新商品的标签到ES
     *
     * @param tagList 标签集合
     * @throws IOException
     */
    private void updateESProdTags(List<DailyProdTag> tagList) throws IOException {
        // 构建一个批量数据集合
        List<BulkOperation> list = new ArrayList<>();
        tagList.stream().collect(Collectors.groupingBy(DailyProdTag::getStoreProdId))
                .forEach((storeProdId, tags) -> {
                    // 构建部分文档更新请求
                    list.add(new BulkOperation.Builder().update(u -> u
                                    .action(a -> a.doc(new HashMap<String, Object>() {{
                                        put("tags", tags.stream().sorted(Comparator.comparing(x -> x.getType())).map(DailyProdTag::getTag).collect(Collectors.toList()));
                                    }}))
                                    .id(String.valueOf(storeProdId))
                                    .index(Constants.ES_IDX_PRODUCT_INFO))
                            .build());
                });
        // 调用bulk方法执行批量更新操作
        BulkResponse bulkResponse = esClientWrapper.getEsClient().bulk(e -> e.index(Constants.ES_IDX_PRODUCT_INFO).operations(list));
        System.out.println("bulkResponse.items() = " + bulkResponse.items());
    }

    /**
     * 根据支付渠道匹配支付类
     *
     * @param payChannel
     * @return
     */
    private PaymentManager getPaymentManager(EPayChannel payChannel) {
        Assert.notNull(payChannel);
        for (PaymentManager paymentManager : paymentManagers) {
            if (paymentManager.channel() == payChannel) {
                return paymentManager;
            }
        }
        throw new ServiceException("未知支付渠道");
    }

    /**
     * 搜图服务同步商品
     *
     * @param storeProductId
     * @param picKeys
     * @param async
     */
    private void sync2ImgSearchServer(Long storeProductId, List<String> picKeys, boolean async) {
        if (async) {
            ThreadUtil.execAsync(() -> {
                        ProductPicSyncResultDTO r =
                                pictureService.sync2ImgSearchServer(new ProductPicSyncDTO(storeProductId, picKeys));
                        log.info("商品图片同步至搜图服务器: id: {}, result: {}", storeProductId, JSONUtil.toJsonStr(r));
                    }
            );
        } else {
            ProductPicSyncResultDTO r =
                    pictureService.sync2ImgSearchServer(new ProductPicSyncDTO(storeProductId, picKeys));
            log.info("商品图片同步至搜图服务器: id: {}, result: {}", storeProductId, JSONUtil.toJsonStr(r));
        }
    }

    /**
     * 新增商品时，新增系统通知
     *
     * @param storeProd 档口商品
     * @param storeName 档口名称
     */
    private void createNotice(StoreProduct storeProd, String storeName) {
        // 新增档口 商品动态 通知公告
        Long userId = SecurityUtils.getUserId();
        // 新增一条档口消息通知
        Notice notice = new Notice().setNoticeTitle(storeName + "商品上新啦!").setNoticeType(NoticeType.NOTICE.getValue())
                .setNoticeContent(storeName + "上新了货号为: " + storeProd.getProdArtNum() + " 的商品!请及时关注!")
                .setOwnerType(NoticeOwnerType.SYSTEM.getValue()).setStoreId(storeProd.getStoreId())
                .setUserId(userId).setPerpetuity(NoticePerpetuityType.PERMANENT.getValue());
        this.noticeMapper.insert(notice);
        final Date voucherDate = java.sql.Date.valueOf(LocalDate.now());
        // 新增消息通知列表
        List<UserNotice> userNoticeList = new ArrayList<>();
        // 新增档口商品动态
        userNoticeList.add(new UserNotice().setNoticeId(notice.getId())
                .setUserId(userId).setReadStatus(NoticeReadType.UN_READ.getValue()).setVoucherDate(voucherDate)
                .setTargetNoticeType(UserNoticeType.PRODUCT_DYNAMIC.getValue()));
        List<UserSubscriptions> userSubList = this.userSubMapper.selectList(new LambdaQueryWrapper<UserSubscriptions>()
                .eq(UserSubscriptions::getStoreId, storeProd.getStoreId()));
        if (CollectionUtils.isNotEmpty(userSubList)) {
            userSubList.forEach(x -> userNoticeList.add(new UserNotice().setNoticeId(notice.getId())
                    .setUserId(x.getUserId()).setReadStatus(NoticeReadType.UN_READ.getValue()).setVoucherDate(voucherDate)
                    .setTargetNoticeType(UserNoticeType.FOCUS_STORE.getValue())));
        }
        if (CollectionUtils.isEmpty(userNoticeList)) {
            return;
        }
        this.userNoticeMapper.insert(userNoticeList);
    }

    /**
     * 从中通同步行政区划
     */
    public void syncRegionFromZto() {
        log.info("-------------同步行政区划开始-------------");
        try {
            Map<Long, ZtoRegion> ztoRegionMap = ztoExpressManager.getAllRegion()
                    .stream()
                    .filter(o -> Integer.valueOf(1).equals(o.getEnabled()))
                    .collect(Collectors.toMap(ZtoRegion::getId, o -> o, (o, n) -> n));
            List<ExpressRegion> expressRegions = new ArrayList<>(ztoRegionMap.size());
            for (ZtoRegion ztoRegion : ztoRegionMap.values()) {
                if (ztoRegion.getLayer() != null && ztoRegion.getLayer() > 4) {
                    continue;
                }
                ExpressRegion expressRegion = new ExpressRegion();
                expressRegion.setRegionCode(ztoRegion.getCode());
                expressRegion.setRegionName(ztoRegion.getFullName());
                if (ztoRegion.getParentId() != null) {
                    ZtoRegion ztoParentRegion = ztoRegionMap.get(ztoRegion.getParentId());
                    if (ztoParentRegion != null) {
                        expressRegion.setParentRegionCode(ztoParentRegion.getCode());
                        expressRegion.setParentRegionName(ztoParentRegion.getFullName());
                    }
                }
                expressRegion.setRegionLevel(Optional.ofNullable(ztoRegion.getLayer()).map(n -> n - 1).orElse(0));
                expressRegion.setVersion(0L);
                expressRegion.setDelFlag(Constants.UNDELETED);
                expressRegions.add(expressRegion);
            }
            expressService.clearAndInsertAllRegion(expressRegions);
        } catch (Exception e) {
            log.error("同步行政区划异常", e);
            fsNotice.sendMsg2DefaultChat("同步行政区划异常", StrUtil.emptyIfNull(e.getMessage()));
        }
        log.info("-------------同步行政区划结束-------------");
    }

    /**
     * 创建推广轮次
     *
     * @param roundList  当前所有的推广轮次
     * @param diffRound  需要创建多少轮
     * @param advert     当前推广数据
     * @param updateList 待更新列表
     * @return
     */
    private void createAdvertRound(List<AdvertRound> roundList, int diffRound, Advert advert, List<AdvertRound> updateList) {
        // 当前最大轮次
        int maxRoundId = roundList.stream().mapToInt(AdvertRound::getRoundId).max().getAsInt();
        // 最大轮次的结束时间
        LocalDate maxEndTime = roundList.stream().max(Comparator.comparing(AdvertRound::getEndTime))
                .map(round -> round.getEndTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .orElseThrow(() -> new ServiceException("获取推广轮次最大结束时间失败", HttpStatus.ERROR));
        LocalDate maxEndTimeNextDay = maxEndTime.plusDays(1);
        // 根据轮次差来判断当前需要补多少播放轮
        for (int diff = 0; diff < diffRound; diff++) {
            // 推广轮次 + 1
            maxRoundId += 1;
            final LocalDate startDate = diff == 0 ? maxEndTimeNextDay : maxEndTimeNextDay.plusDays((long) advert.getPlayInterval() * diff);
            // 间隔时间
            final LocalDate endDate = startDate.plusDays(advert.getPlayInterval() - 1);
            // 每一轮的播放数量
            for (int playNum = 0; playNum < advert.getPlayNum(); playNum++) {
                // 依次按照26个字母顺序 如果i == 0 则A i == 1 则B i==2则C
                final String position = String.valueOf((char) ('A' + playNum));
                AdvertRound advertRound = new AdvertRound().setAdvertId(advert.getId()).setTypeId(advert.getTypeId()).setRoundId(maxRoundId)
                        .setLaunchStatus(AdLaunchStatus.UN_LAUNCH.getValue()).setPosition(position).setStartPrice(advert.getStartPrice())
                        .setSysIntercept(AdSysInterceptType.UN_INTERCEPT.getValue()).setShowType(advert.getShowType()).setDisplayType(advert.getDisplayType())
                        .setStartTime(Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant()))
                        .setEndTime(Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant()))
                        .setDeadline(advert.getDeadline()).setBiddingStatus(AdBiddingStatus.UN_BIDDING.getValue())
                        .setSymbol(Objects.equals(advert.getShowType(), AdShowType.POSITION_ENUM.getValue())
                                // 如果是位置枚举的推广位，则需要精确到某一个position的推广位，反之，若是时间范围，则直接精确到播放轮次即可
                                ? advert.getBasicSymbol() + maxRoundId + position : advert.getBasicSymbol() + maxRoundId);
                // 添加到推广轮次列表，如果playNum有调整，后续会用到roundList
                roundList.add(advertRound);
                // 生成最新的下一轮推广位
                updateList.add(advertRound);
            }
        }
    }

}



