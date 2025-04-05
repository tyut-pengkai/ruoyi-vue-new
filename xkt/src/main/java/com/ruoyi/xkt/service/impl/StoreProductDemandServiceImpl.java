package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.xkt.domain.*;
import com.ruoyi.xkt.dto.storeProductDemand.StoreProdDemandDTO;
import com.ruoyi.xkt.dto.storeProductDemand.StoreProdDemandPageDTO;
import com.ruoyi.xkt.dto.storeProductDemand.StoreProdDemandPageResDTO;
import com.ruoyi.xkt.dto.storeProductDemand.StoreProdDemandQuantityDTO;
import com.ruoyi.xkt.dto.storeProductStock.StoreProdStockDTO;
import com.ruoyi.xkt.enums.EVoucherSequenceType;
import com.ruoyi.xkt.mapper.*;
import com.ruoyi.xkt.service.IStoreProductDemandService;
import com.ruoyi.xkt.service.IVoucherSequenceService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 档口商品需求单Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
@RequiredArgsConstructor
public class StoreProductDemandServiceImpl implements IStoreProductDemandService {

    final StoreProductDemandMapper storeProdDemandMapper;
    final StoreProductDemandDetailMapper storeProdDemandDetailMapper;
    final StoreProductColorMapper storeProdColorMapper;
    final StoreProductStockMapper storeProdStockMapper;
    final StoreProductMapper storeProdMapper;
    final IVoucherSequenceService sequenceService;
    final StoreProductStorageDemandDeductMapper storageDemandDeductMapper;


    /**
     * 获取指定门店及商品的库存和生产数量
     * 此方法用于查询特定门店中特定商品的库存和生产数量信息，帮助进行库存管理和生产计划制定
     *
     * @param storeId     门店ID，用于指定查询的门店
     * @param storeProdId 门店商品ID，用于指定查询的商品
     * @return 返回一个列表，包含门店商品的库存和生产数量信息的DTO对象
     */
    @Override
    @Transactional(readOnly = true)
    public  List<StoreProdDemandQuantityDTO> getStockAndProduceQuantity(Long storeId, Long storeProdId) {
        List<StoreProductColor> prodColorList = Optional.ofNullable(this.storeProdColorMapper.selectList(new LambdaQueryWrapper<StoreProductColor>()
                        .eq(StoreProductColor::getStoreId, storeId).eq(StoreProductColor::getDelFlag, Constants.UNDELETED).eq(StoreProductColor::getStoreProdId, storeProdId)))
                .orElseThrow(() -> new RuntimeException("该档口下没有商品及颜色"));
        // 找到档口下该商品
        StoreProduct storeProd = Optional.ofNullable(this.storeProdMapper.selectOne(new LambdaQueryWrapper<StoreProduct>()
                        .eq(StoreProduct::getId, storeProdId).eq(StoreProduct::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new RuntimeException("该档口下没有商品"));
        List<Long> storeProdColorIdList = prodColorList.stream().map(StoreProductColor::getId).collect(Collectors.toList());
        // 根据各个颜色查询库存信息
        List<StoreProductStock> prodStockList = this.storeProdStockMapper.selectList(new LambdaQueryWrapper<StoreProductStock>()
                .in(StoreProductStock::getStoreProdColorId, storeProdColorIdList).eq(StoreProductStock::getDelFlag, Constants.UNDELETED).eq(StoreProductStock::getStoreId, storeId));
        // 将库存信息封装成Map
        Map<Long, StoreProdStockDTO> stockMap = prodStockList.stream().collect(Collectors
                .toMap(StoreProductStock::getStoreProdColorId, x -> BeanUtil.toBean(x, StoreProdStockDTO.class)));
        // 查询待产及生产中的库存
        List<StoreProductDemandDetail> prodDemandList = this.storeProdDemandDetailMapper.selectList(new LambdaQueryWrapper<StoreProductDemandDetail>()
                .in(StoreProductDemandDetail::getStoreProdColorId, storeProdColorIdList).eq(StoreProductDemandDetail::getDelFlag, Constants.UNDELETED)
                .in(StoreProductDemandDetail::getDetailStatus, Arrays.asList("待生产", "生产中")));
        // 将生产需求信息封转在map中
        Map<Long, List<StoreProductDemandDetail>> demandMap = prodDemandList.stream().collect(Collectors.groupingBy(StoreProductDemandDetail::getStoreProdColorId));
        // 组装返回的对比数量
        List<String> compareStrList = Arrays.asList("库存数量", "在产数量");
        return prodColorList.stream().map(prodColor -> {
            StoreProdStockDTO stock = stockMap.get(prodColor.getId());
            List<StoreProductDemandDetail> demandDetailList = demandMap.get(prodColor.getId());
            Integer size30Demand = Optional.ofNullable(demandDetailList).map(x -> x.stream().map(StoreProductDemandDetail::getSize30).reduce(0, Integer::sum)).orElse(0);
            Integer size31Demand = Optional.ofNullable(demandDetailList).map(x -> x.stream().map(StoreProductDemandDetail::getSize31).reduce(0, Integer::sum)).orElse(0);
            Integer size32Demand = Optional.ofNullable(demandDetailList).map(x -> x.stream().map(StoreProductDemandDetail::getSize32).reduce(0, Integer::sum)).orElse(0);
            Integer size33Demand = Optional.ofNullable(demandDetailList).map(x -> x.stream().map(StoreProductDemandDetail::getSize33).reduce(0, Integer::sum)).orElse(0);
            Integer size34Demand = Optional.ofNullable(demandDetailList).map(x -> x.stream().map(StoreProductDemandDetail::getSize34).reduce(0, Integer::sum)).orElse(0);
            Integer size35Demand = Optional.ofNullable(demandDetailList).map(x -> x.stream().map(StoreProductDemandDetail::getSize35).reduce(0, Integer::sum)).orElse(0);
            Integer size36Demand = Optional.ofNullable(demandDetailList).map(x -> x.stream().map(StoreProductDemandDetail::getSize36).reduce(0, Integer::sum)).orElse(0);
            Integer size37Demand = Optional.ofNullable(demandDetailList).map(x -> x.stream().map(StoreProductDemandDetail::getSize37).reduce(0, Integer::sum)).orElse(0);
            Integer size38Demand = Optional.ofNullable(demandDetailList).map(x -> x.stream().map(StoreProductDemandDetail::getSize38).reduce(0, Integer::sum)).orElse(0);
            Integer size39Demand = Optional.ofNullable(demandDetailList).map(x -> x.stream().map(StoreProductDemandDetail::getSize39).reduce(0, Integer::sum)).orElse(0);
            Integer size40Demand = Optional.ofNullable(demandDetailList).map(x -> x.stream().map(StoreProductDemandDetail::getSize40).reduce(0, Integer::sum)).orElse(0);
            Integer size41Demand = Optional.ofNullable(demandDetailList).map(x -> x.stream().map(StoreProductDemandDetail::getSize41).reduce(0, Integer::sum)).orElse(0);
            Integer size42Demand = Optional.ofNullable(demandDetailList).map(x -> x.stream().map(StoreProductDemandDetail::getSize42).reduce(0, Integer::sum)).orElse(0);
            Integer size43Demand = Optional.ofNullable(demandDetailList).map(x -> x.stream().map(StoreProductDemandDetail::getSize43).reduce(0, Integer::sum)).orElse(0);
            // 尺码为30的数组
            List<String> size30List = Arrays.asList(ObjectUtils.isNotEmpty(stock) && ObjectUtils.isNotEmpty(stock.getSize30()) ? stock.getSize30().toString() : Constants.UNDELETED, size30Demand.toString());
            List<String> size31List = Arrays.asList(ObjectUtils.isNotEmpty(stock) && ObjectUtils.isNotEmpty(stock.getSize31()) ? stock.getSize31().toString() : Constants.UNDELETED, size31Demand.toString());
            List<String> size32List = Arrays.asList(ObjectUtils.isNotEmpty(stock) && ObjectUtils.isNotEmpty(stock.getSize32()) ? stock.getSize32().toString() : Constants.UNDELETED, size32Demand.toString());
            List<String> size33List = Arrays.asList(ObjectUtils.isNotEmpty(stock) && ObjectUtils.isNotEmpty(stock.getSize33()) ? stock.getSize33().toString() : Constants.UNDELETED, size33Demand.toString());
            List<String> size34List = Arrays.asList(ObjectUtils.isNotEmpty(stock) && ObjectUtils.isNotEmpty(stock.getSize34()) ? stock.getSize34().toString() : Constants.UNDELETED, size34Demand.toString());
            List<String> size35List = Arrays.asList(ObjectUtils.isNotEmpty(stock) && ObjectUtils.isNotEmpty(stock.getSize35()) ? stock.getSize35().toString() : Constants.UNDELETED, size35Demand.toString());
            List<String> size36List = Arrays.asList(ObjectUtils.isNotEmpty(stock) && ObjectUtils.isNotEmpty(stock.getSize36()) ? stock.getSize36().toString() : Constants.UNDELETED, size36Demand.toString());
            List<String> size37List = Arrays.asList(ObjectUtils.isNotEmpty(stock) && ObjectUtils.isNotEmpty(stock.getSize37()) ? stock.getSize37().toString() : Constants.UNDELETED, size37Demand.toString());
            List<String> size38List = Arrays.asList(ObjectUtils.isNotEmpty(stock) && ObjectUtils.isNotEmpty(stock.getSize38()) ? stock.getSize38().toString() : Constants.UNDELETED, size38Demand.toString());
            List<String> size39List = Arrays.asList(ObjectUtils.isNotEmpty(stock) && ObjectUtils.isNotEmpty(stock.getSize39()) ? stock.getSize39().toString() : Constants.UNDELETED, size39Demand.toString());
            List<String> size40List = Arrays.asList(ObjectUtils.isNotEmpty(stock) && ObjectUtils.isNotEmpty(stock.getSize40()) ? stock.getSize40().toString() : Constants.UNDELETED, size40Demand.toString());
            List<String> size41List = Arrays.asList(ObjectUtils.isNotEmpty(stock) && ObjectUtils.isNotEmpty(stock.getSize41()) ? stock.getSize41().toString() : Constants.UNDELETED, size41Demand.toString());
            List<String> size42List = Arrays.asList(ObjectUtils.isNotEmpty(stock) && ObjectUtils.isNotEmpty(stock.getSize42()) ? stock.getSize42().toString() : Constants.UNDELETED, size42Demand.toString());
            List<String> size43List = Arrays.asList(ObjectUtils.isNotEmpty(stock) && ObjectUtils.isNotEmpty(stock.getSize43()) ? stock.getSize43().toString() : Constants.UNDELETED, size43Demand.toString());
            return StoreProdDemandQuantityDTO.builder().storeId(storeId).storeProdId(storeProd.getId()).storeProdColorId(prodColor.getId())
                    .prodArtNum(storeProd.getProdArtNum()).colorName(prodColor.getColorName()).compareStrList(compareStrList)
                    // 判断 demandDetailList 中是否有 createTime 为当天的对象
                    .todaySubmitted(Optional.ofNullable(demandDetailList).orElse(Collections.emptyList()).stream().anyMatch(detail -> DateUtils.isSameDay(detail.getCreateTime(), new Date())))
                    .size30List(size30List).size31List(size31List).size32List(size32List).size33List(size33List).size34List(size34List).size35List(size35List)
                    .size36List(size36List).size37List(size37List).size38List(size38List).size39List(size39List).size40List(size40List).size41List(size41List)
                    .size42List(size42List).size43List(size43List).build();
        }).collect(Collectors.toList());
    }

    /**
     * 创建需求订单
     *
     * @param demandDTO 商店产品需求信息，包含需求订单的相关数据，如产品ID、需求数量等
     * @return 返回一个字符串，通常包含需求订单的唯一标识或创建状态
     */
    @Override
    @Transactional
    public Integer createDemand(StoreProdDemandDTO demandDTO) {
        StoreProductDemand demand = new StoreProductDemand();
        // 生成code
        demand.setCode(this.sequenceService.generateCode(demandDTO.getStoreId(), EVoucherSequenceType.DEMAND.getValue(), DateUtils.parseDateToStr(DateUtils.YYYYMMDD, new Date())))
                .setDemandStatus(1).setStoreId(demandDTO.getStoreId()).setStoreFactoryId(demandDTO.getStoreFactoryId());
        int count = this.storeProdDemandMapper.insert(demand);
        // 生产需求详情
        List<StoreProductDemandDetail> detailList = demandDTO.getDetailList().stream()
                .map(x -> BeanUtil.toBean(x, StoreProductDemandDetail.class).setStoreId(demandDTO.getStoreId())
                        .setStoreProdDemandId(demand.getId()).setDetailStatus(1)).collect(Collectors.toList());
        this.storeProdDemandDetailMapper.insert(detailList);
        return count;
    }

    /**
     * 根据需求选择页面
     * 此方法用于根据提供的页面查询条件，返回相应的页面数据
     * 主要用于处理分页查询请求，以便在界面上展示特定的需求信息
     *
     * @param pageDTO 包含页面查询条件的DTO对象，如页码、每页条数等
     * @return 返回一个对象，该对象包含了根据查询条件筛选出的页面数据
     */
    @Override
    @Transactional(readOnly = true)
    /**
     * 根据页面查询条件查询门店生产需求信息
     *
     * @param pageDTO 页面查询条件对象，包含页码和页面大小等信息
     * @return 返回一个包含查询结果的分页对象
     */
    public Page<StoreProdDemandPageResDTO> selectPage(StoreProdDemandPageDTO pageDTO) {
        // 启用分页查询
        PageHelper.startPage(pageDTO.getPageNum(), pageDTO.getPageSize());
        // 执行分页查询，获取需求列表
        List<StoreProdDemandPageResDTO> demandList = this.storeProdDemandDetailMapper.selectDemandPage(pageDTO);
        // 如果查询结果为空，返回一个空的分页对象
        if (CollectionUtils.isEmpty(demandList)) {
            return Page.empty(pageDTO.getPageSize(), pageDTO.getPageNum());
        }
        // 提取需求详情ID列表，用于后续查询抵扣信息
        List<Long> demandDetailIdList = demandList.stream().map(StoreProdDemandPageResDTO::getStoreProdDemandDetailId).distinct().collect(Collectors.toList());
        // 找到需求单抵扣的数据
        List<StoreProductStorageDemandDeduct> deductList = this.storageDemandDeductMapper.selectList(new LambdaQueryWrapper<StoreProductStorageDemandDeduct>()
                .in(StoreProductStorageDemandDeduct::getStoreProdStorageDetailId, demandDetailIdList).eq(StoreProductStorageDemandDeduct::getDelFlag, Constants.UNDELETED));
        // 明细抵扣的数量
        Map<Long, Integer> deductQuantityMap = CollectionUtils.isEmpty(deductList) ? new HashMap<>()
                : deductList.stream().collect(Collectors.groupingBy(StoreProductStorageDemandDeduct::getStoreProdStorageDetailId, Collectors.summingInt(StoreProductStorageDemandDeduct::getQuantity)));
        // 更新需求列表中的每个项，设置库存数量和生产中数量
        demandList.forEach(x -> {
            final Integer deductQuantity = deductQuantityMap.getOrDefault(x.getStoreProdDemandDetailId(), 0);
            x.setStorageQuantity(deductQuantity).setInProdQuantity(x.getQuantity() - deductQuantity);
        });
        // 将查询结果转换为分页对象并返回
        return Page.convert(new PageInfo<>(demandList));
    }


    /**
     * 查询档口商品需求单
     *
     * @param storeProdDemandId 档口商品需求单主键
     * @return 档口商品需求单
     */
    @Override
    public StoreProductDemand selectStoreProductDemandByStoreProdDemandId(Long storeProdDemandId) {
        return storeProdDemandMapper.selectStoreProductDemandByStoreProdDemandId(storeProdDemandId);
    }

    /**
     * 查询档口商品需求单列表
     *
     * @param storeProductDemand 档口商品需求单
     * @return 档口商品需求单
     */
    @Override
    public List<StoreProductDemand> selectStoreProductDemandList(StoreProductDemand storeProductDemand) {
        return storeProdDemandMapper.selectStoreProductDemandList(storeProductDemand);
    }

    /**
     * 新增档口商品需求单
     *
     * @param storeProductDemand 档口商品需求单
     * @return 结果
     */
    @Override
    public int insertStoreProductDemand(StoreProductDemand storeProductDemand) {
        storeProductDemand.setCreateTime(DateUtils.getNowDate());
        return storeProdDemandMapper.insertStoreProductDemand(storeProductDemand);
    }

    /**
     * 修改档口商品需求单
     *
     * @param storeProductDemand 档口商品需求单
     * @return 结果
     */
    @Override
    @Transactional
    public int updateStoreProductDemand(StoreProductDemand storeProductDemand) {
        storeProductDemand.setUpdateTime(DateUtils.getNowDate());
        return storeProdDemandMapper.updateStoreProductDemand(storeProductDemand);
    }

    /**
     * 批量删除档口商品需求单
     *
     * @param storeProdDemandIds 需要删除的档口商品需求单主键
     * @return 结果
     */
    @Override
    public int deleteStoreProductDemandByStoreProdDemandIds(Long[] storeProdDemandIds) {
        return storeProdDemandMapper.deleteStoreProductDemandByStoreProdDemandIds(storeProdDemandIds);
    }

    /**
     * 删除档口商品需求单信息
     *
     * @param storeProdDemandId 档口商品需求单主键
     * @return 结果
     */
    @Override
    public int deleteStoreProductDemandByStoreProdDemandId(Long storeProdDemandId) {
        return storeProdDemandMapper.deleteStoreProductDemandByStoreProdDemandId(storeProdDemandId);
    }


}
