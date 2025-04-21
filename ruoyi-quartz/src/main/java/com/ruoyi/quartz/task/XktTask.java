package com.ruoyi.quartz.task;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.entity.SysProductCategory;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.quartz.domain.*;
import com.ruoyi.quartz.dto.DailySaleCusDTO;
import com.ruoyi.quartz.dto.DailySaleDTO;
import com.ruoyi.quartz.dto.DailySaleProdDTO;
import com.ruoyi.quartz.dto.WeekCateSaleDTO;
import com.ruoyi.quartz.mapper.*;
import com.ruoyi.system.mapper.SysProductCategoryMapper;
import com.ruoyi.xkt.domain.*;
import com.ruoyi.xkt.dto.dailyStoreTag.DailyStoreTagDTO;
import com.ruoyi.xkt.enums.*;
import com.ruoyi.xkt.mapper.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 鞋库通定时任务
 *
 * @author ruoyi
 */
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
    final StoreProductStatisticsMapper prodStatMapper;

    /**
     * 每晚1点同步档口销售数据
     */
    @Transactional
    public void dailySale() {
        // 使用LocalDate获取当前日期前一天，并转为 Date 格式
        final Date yesterday = Date.from(LocalDate.now().minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
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
     * 每晚1点30分同步档口客户销售数据
     */
    @Transactional
    public void dailySaleCustomer() {
        // 使用LocalDate获取当前日期前一天，并转为 Date 格式
        final Date yesterday = Date.from(LocalDate.now().minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
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
     * 每晚2点同步档口商品销售数据
     */
    @Transactional
    public void dailySaleProduct() {
        // 使用LocalDate获取当前日期前一天，并转为 Date 格式
        final Date yesterday = Date.from(LocalDate.now().minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
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

    @Transactional
    public void categorySort() {
        // 系统所有的商品分类
        List<SysProductCategory> cateList = this.prodCateMapper.selectList(new LambdaQueryWrapper<SysProductCategory>()
                .eq(SysProductCategory::getDelFlag, Constants.UNDELETED).eq(SysProductCategory::getStatus, Constants.UNDELETED));
        if (CollectionUtils.isEmpty(cateList)) {
            throw new ServiceException("商品分类不存在!", HttpStatus.ERROR);
        }
        // 根据LocalDate 获取当前日期前一天
        final Date yesterday = Date.from(LocalDate.now().minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        // 及当前日期前一天的前一周，并转为 Date 格式
        final Date pastDate = Date.from(LocalDate.now().minusDays(8).atStartOfDay(ZoneId.systemDefault()).toInstant());
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
     * 每天更新档口的标签
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
        // 根据LocalDate 获取当前日期前一天
        final Date yesterday = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        // 使用LocalDate 获取当前日期前一天的前一周，并转为 Date 格式
        final Date oneWeekAgo = Date.from(LocalDate.now().minusWeeks(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        // 使用LocalDate 获取当前日期前一天的前一个月
        final Date oneMonthAgo = Date.from(LocalDate.now().minusMonths(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        // 1. 打 销量榜 标签，这个是最重要标签
        this.tagSaleRank(yesterday, oneMonthAgo, tagList);
        // 2. 打 爆款频出 标签，根据销量前50的商品中 档口 先后顺序排列
        this.tagHotRank(yesterday, oneMonthAgo, tagList);
        // 3. 打 新款频出 标签，根据最近一周档口商品上新速度，先后排序
        this.tagNewProd(yesterday, oneWeekAgo, tagList);
        // 4. 打 关注榜 标签，根据关注量，进行排序
        this.tagAttentionRank(yesterday, tagList);
        // 5. 打 收藏榜 标签，根据收藏量，进行排序
        this.tagCollectionRank(yesterday, tagList);
        // 6. 打 库存榜 标签，根据库存量，进行排序
        this.tagStockTag(yesterday, oneMonthAgo, tagList);
        // 打基础标签
        this.tagBasicTag(yesterday, oneWeekAgo, tagList);
        if (CollectionUtils.isEmpty(tagList)) {
            return;
        }
        this.dailyStoreTagMapper.insert(tagList);
    }

    /**
     * 给商品打标
     */
    @Transactional
    public void dailyProdTag() {
        // 先删除所有的商品标签，保证数据唯一性
        List<DailyProdTag> existList = this.dailyProdTagMapper.selectList(new LambdaQueryWrapper<DailyProdTag>()
                .eq(DailyProdTag::getDelFlag, Constants.UNDELETED));
        if (CollectionUtils.isNotEmpty(existList)) {
            this.dailyProdTagMapper.deleteByIds(existList.stream().map(DailyProdTag::getId).collect(Collectors.toList()));
        }
        // 根据LocalDate 获取当前日期前一天
        final Date yesterday = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        // 使用LocalDate 获取当前日期4天前，并转为 Date 格式
        final Date fourDaysAgo = Date.from(LocalDate.now().minusDays(3).atStartOfDay(ZoneId.systemDefault()).toInstant());
        // 使用LocalDate 获取当前日期前一天的前一周，并转为 Date 格式
        final Date oneWeekAgo = Date.from(LocalDate.now().minusWeeks(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        // 使用LocalDate 获取当前日期前一天的前一个月
        final Date oneMonthAgo = Date.from(LocalDate.now().minusMonths(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        List<DailyProdTag> tagList = new ArrayList<>();
        // 1. 当月（近一月）爆款
        this.tagMonthHot(yesterday, oneMonthAgo, tagList);
        // 2. 档口热卖
        this.tagStoreHot(yesterday, oneMonthAgo, tagList);
        // 3. 三日上新
        this.tagThreeDayNew(yesterday, fourDaysAgo, tagList);
        // 4. 七日上新
        this.tagSevenDayNew(yesterday, fourDaysAgo, oneWeekAgo, tagList);
        // 5. 风格
        this.tagStyle(yesterday, tagList);
        if (CollectionUtils.isEmpty(tagList)) {
            return;
        }
        this.dailyProdTagMapper.insert(tagList);
    }


    /**
     * 每日更新档口商品的各项权重数据
     */
    public void dailyProdWeight() {
        List<StoreProduct> storeProdList = this.storeProdMapper.selectList(new LambdaQueryWrapper<StoreProduct>()
                .eq(StoreProduct::getDelFlag, Constants.UNDELETED));
        if (CollectionUtils.isEmpty(storeProdList)) {
            return;
        }
        // 获取 商品销售、商品浏览量、商品收藏量、商品下载量
        List<StoreProductStatistics> statisticsList = this.prodStatMapper.selectList(new LambdaQueryWrapper<StoreProductStatistics>()
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
     * 给商品打风格标签
     *
     * @param yesterday 昨天
     * @param tagList   标签列表
     */
    private void tagStyle(Date yesterday, List<DailyProdTag> tagList) {
        List<StoreProductCategoryAttribute> cateAttrList = this.cateAttrMapper.selectList(new LambdaQueryWrapper<StoreProductCategoryAttribute>()
                .eq(StoreProductCategoryAttribute::getDelFlag, Constants.UNDELETED).eq(StoreProductCategoryAttribute::getDictType, "style"));
        // 查询条件加上
        if (CollectionUtils.isEmpty(cateAttrList)) {
            return;
        }
        // 根据storeProdId找到storeId
        List<StoreProduct> storeProdList = this.storeProdMapper.selectList(new LambdaQueryWrapper<StoreProduct>()
                .eq(StoreProduct::getDelFlag, Constants.UNDELETED).in(StoreProduct::getId, cateAttrList.stream()
                        .map(StoreProductCategoryAttribute::getStoreProdId).collect(Collectors.toList())));
        Map<Long, Long> prodStoreIdMap = storeProdList.stream().collect(Collectors.toMap(StoreProduct::getId, StoreProduct::getStoreId));
        tagList.addAll(cateAttrList.stream().map(x -> DailyProdTag.builder().storeId(prodStoreIdMap.get(x.getStoreProdId())).storeProdId(x.getStoreProdId())
                .tag(x.getDictValue()).type(ProdTagType.STYLE.getValue()).voucherDate(yesterday).build()).collect(Collectors.toList()));
    }

    /**
     * 给商品打标 七日上新
     *
     * @param yesterday 昨天
     * @param fourDaysAgo  4天前
     * @param oneWeekAgo 一周前
     * @param tagList    标签列表
     */
    private void tagSevenDayNew(Date yesterday, Date fourDaysAgo, Date oneWeekAgo, List<DailyProdTag> tagList) {
        List<StoreProduct> storeProdList = this.storeProdMapper.selectList(new LambdaQueryWrapper<StoreProduct>()
                .eq(StoreProduct::getDelFlag, Constants.UNDELETED).between(StoreProduct::getCreateTime, oneWeekAgo, fourDaysAgo));
        if (CollectionUtils.isEmpty(storeProdList)) {
            return;
        }
        tagList.addAll(storeProdList.stream().map(x -> DailyProdTag.builder().storeId(x.getStoreId()).storeProdId(x.getId())
                .type(ProdTagType.SEVEN_DAY_NEW.getValue()).tag("七日上新").voucherDate(yesterday).build()).collect(Collectors.toList()));
    }

    /**
     * 三日上新
     *
     * @param yesterday   昨天
     * @param fourDaysAgo 3天前
     * @param tagList     标签列表
     */
    private void tagThreeDayNew(Date yesterday, Date fourDaysAgo, List<DailyProdTag> tagList) {
        List<StoreProduct> storeProdList = this.storeProdMapper.selectList(new LambdaQueryWrapper<StoreProduct>()
                .eq(StoreProduct::getDelFlag, Constants.UNDELETED).between(StoreProduct::getCreateTime, fourDaysAgo, yesterday));
        if (CollectionUtils.isEmpty(storeProdList)) {
            return;
        }
        tagList.addAll(storeProdList.stream().map(x -> DailyProdTag.builder().storeId(x.getStoreId()).storeProdId(x.getId())
                .type(ProdTagType.THREE_DAY_NEW.getValue()).tag("三日上新").voucherDate(yesterday).build()).collect(Collectors.toList()));
    }

    /**
     * 筛选档口热卖商品
     *
     * @param yesterday   昨天
     * @param oneMonthAgo 一月前
     * @param tagList     标签集合
     */
    private void tagStoreHot(Date yesterday, Date oneMonthAgo, List<DailyProdTag> tagList) {
        List<StoreSaleDetail> detailList = this.saleDetailMapper.selectList(new LambdaQueryWrapper<StoreSaleDetail>()
                .eq(StoreSaleDetail::getDelFlag, Constants.UNDELETED).eq(StoreSaleDetail::getSaleType, SaleType.GENERAL_SALE.getValue())
                .between(StoreSaleDetail::getCreateTime, oneMonthAgo, yesterday));
        if (CollectionUtils.isEmpty(detailList)) {
            return;
        }
        // 按照档口下商品的销量排序
        Map<Long, Map<Long, Integer>> storeSaleMap = detailList.stream().collect(Collectors.groupingBy(StoreSaleDetail::getStoreId, Collectors
                .groupingBy(StoreSaleDetail::getStoreProdId, Collectors.summingInt(StoreSaleDetail::getQuantity))));
        storeSaleMap.forEach((storeId, prodSaleMap) -> {
            // 按照销量倒序排， 如果超过20个商品，则取前20，没有20个就有多少个货品取多少个
            List<Map.Entry<Long, Integer>> prodSaleList = prodSaleMap.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).limit(20).collect(Collectors.toList());
            // 将prodSaleList按照tagList返回
            prodSaleList.forEach(x -> tagList.add(DailyProdTag.builder().storeId(storeId).storeProdId(x.getKey()).tag("档口热卖")
                    .type(ProdTagType.STORE_HOT.getValue()).voucherDate(yesterday).build()));
        });
    }

    /**
     * 给商品打标 本月爆款
     *
     * @param yesterday   昨天
     * @param oneMonthAgo 一月前
     * @param tagList     标签列表
     */
    private void tagMonthHot(Date yesterday, Date oneMonthAgo, List<DailyProdTag> tagList) {
        List<DailyStoreTagDTO> top50List = this.saleDetailMapper.selectTop50List(yesterday, oneMonthAgo);
        if (CollectionUtils.isEmpty(top50List)) {
            return;
        }
        tagList.addAll(top50List.stream().map(x -> DailyProdTag.builder().storeId(x.getStoreId()).storeProdId(x.getStoreProdId())
                .tag("本月爆款").type(ProdTagType.MONTH_HOT.getValue()).voucherDate(yesterday).build()).collect(Collectors.toList()));
    }


    /**
     * 档口基础标签
     *
     * @param yesterday  昨日
     * @param oneWeekAgo 一周前
     * @param tagList    标签列表
     */
    private void tagBasicTag(Date yesterday, Date oneWeekAgo, List<DailyStoreTag> tagList) {
        // 7. 打 经营年限 标签
        List<Store> storeList = this.storeMapper.selectList(new LambdaQueryWrapper<Store>()
                .eq(Store::getDelFlag, Constants.UNDELETED)
                .in(Store::getStoreStatus, Arrays.asList(StoreStatus.TRIAL_PERIOD.getValue(), StoreStatus.FORMAL_USE.getValue())));
        if (CollectionUtils.isNotEmpty(storeList)) {
            storeList.forEach(x -> {
                final Integer operateYears = ObjectUtils.defaultIfNull(x.getOperateYears(), 0);
                tagList.add(DailyStoreTag.builder().storeId(x.getId()).type(StoreTagType.OPERATE_YEARS_RANK.getValue())
                        .tag(operateYears < 3 ? operateYears + "年新店" : operateYears + "年老店").voucherDate(yesterday).build());
            });
        }
        // 8. 打 七日上新 标签
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
     * 给档口打收藏标签
     *
     * @param yesterday 昨天
     * @param tagList   标签列表
     */
    private void tagCollectionRank(Date yesterday, List<DailyStoreTag> tagList) {
        List<DailyStoreTagDTO> top10List = this.userFavMapper.selectTop10List();
        if (CollectionUtils.isEmpty(top10List)) {
            return;
        }
        // 提前计算公共值，减少重复计算
        int collectionRankValue = StoreTagType.COLLECTION_RANK.getValue();
        // 定义标签映射规则
        Map<Integer, String> rankTags = new HashMap<>();
        rankTags.put(0, "收藏榜第一");
        rankTags.put(1, "收藏榜第二");
        rankTags.put(2, "收藏榜前三");
        rankTags.put(3, "收藏榜前五");
        rankTags.put(4, "收藏榜前五");
        // 遍历 top10List 并生成标签
        for (int i = 0; i < Math.min(10, top10List.size()); i++) { // 确保不会超出 top10List 的大小
            DailyStoreTagDTO storeTagDTO = top10List.get(i);
            if (ObjectUtils.isNotEmpty(storeTagDTO)) {
                String tag = rankTags.getOrDefault(i, "收藏榜前十");
                tagList.add(DailyStoreTag.builder()
                        .storeId(storeTagDTO.getStoreId())
                        .type(collectionRankValue)
                        .tag(tag)
                        .voucherDate(yesterday)
                        .build());
            }
        }
    }

    private void tagAttentionRank(Date yesterday, List<DailyStoreTag> tagList) {
        List<DailyStoreTagDTO> top10List = this.userSubsMapper.selectTop10List();
        if (CollectionUtils.isEmpty(top10List)) {
            return;
        }
        // 提前计算公共值，减少重复计算
        int attentionRankValue = StoreTagType.ATTENTION_RANK.getValue();
        // 定义标签映射规则
        Map<Integer, String> rankTags = new HashMap<>();
        rankTags.put(0, "关注榜第一");
        rankTags.put(1, "关注榜第二");
        rankTags.put(2, "关注榜前三");
        rankTags.put(3, "关注榜前五");
        rankTags.put(4, "关注榜前五");
        // 遍历 top10List 并生成标签
        for (int i = 0; i < Math.min(top10List.size(), 10); i++) { // 确保不会超出 top10List 的大小
            // 构建 DailyStoreTag 对象并添加到 tagList
            tagList.add(DailyStoreTag.builder()
                    .storeId(top10List.get(i).getStoreId())
                    .type(attentionRankValue)
                    .tag(rankTags.getOrDefault(i, "关注榜前十"))
                    .voucherDate(yesterday)
                    .build());
        }
    }

    private void tagNewProd(Date yesterday, Date oneWeekAgo, List<DailyStoreTag> tagList) {
        List<DailyStoreTagDTO> top20List = this.saleDetailMapper.selectTop20List(yesterday, oneWeekAgo);
        if (CollectionUtils.isEmpty(top20List)) {
            return;
        }
        tagList.addAll(top20List.stream().map(DailyStoreTagDTO::getStoreId).distinct().map(storeId -> DailyStoreTag.builder()
                        .storeId(storeId).type(StoreTagType.NEW_PRODUCT.getValue()).tag("新款频出").voucherDate(yesterday).build())
                .collect(Collectors.toList()));
    }

    private void tagHotRank(Date yesterday, Date oneMonthAgo, List<DailyStoreTag> tagList) {
        List<DailyStoreTagDTO> top50List = this.saleDetailMapper.selectTop50List(yesterday, oneMonthAgo);
        if (CollectionUtils.isEmpty(top50List)) {
            return;
        }
        tagList.addAll(top50List.stream().map(DailyStoreTagDTO::getStoreId).distinct().map(storeId -> DailyStoreTag.builder()
                        .storeId(storeId).type(StoreTagType.HOT_RANK.getValue()).tag("爆款频出").voucherDate(yesterday).build())
                .collect(Collectors.toList()));
    }

    private void tagSaleRank(Date yesterday, Date oneMonthAgo, List<DailyStoreTag> tagList) {
        // 统计最近一月数据。排名最优先 1. 销量榜 规则：销量排名第1，打标：销量第一  销量第2、第3，打标：销量前三 ；销量前5，打标：销量前五；销量第6-10，打标：销量前十
        List<DailyStoreTagDTO> saleTop10List = this.saleDetailMapper.selectTop10List(yesterday, oneMonthAgo);
        if (CollectionUtils.isEmpty(saleTop10List)) {
            return;
        }
        // 提前计算公共值，减少重复计算
        int salesRankValue = StoreTagType.SALES_RANK.getValue();
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
                tagList.add(DailyStoreTag.builder()
                        .storeId(storeTagDTO.getStoreId())
                        .type(salesRankValue)
                        .tag(tag)
                        .voucherDate(yesterday)
                        .build());
            }
        }
    }

}



