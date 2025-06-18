package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.xkt.domain.*;
import com.ruoyi.xkt.dto.storeProductDemand.*;
import com.ruoyi.xkt.dto.storeProductStock.StoreProdStockDTO;
import com.ruoyi.xkt.enums.DemandStatus;
import com.ruoyi.xkt.enums.EVoucherSequenceType;
import com.ruoyi.xkt.mapper.*;
import com.ruoyi.xkt.service.IStoreProductDemandService;
import com.ruoyi.xkt.service.IVoucherSequenceService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.ibatis.executor.BatchResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.ruoyi.common.constant.Constants.*;

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
    public List<StoreProdDemandQuantityDTO> getStockAndProduceQuantity(Long storeId, Long storeProdId) {
        // 用户是否为档口管理者或子账户
        if (!SecurityUtils.isStoreManagerOrSub(storeId)) {
            throw new ServiceException("当前用户非档口管理者或子账号，无权限操作!", HttpStatus.ERROR);
        }
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
                .in(StoreProductDemandDetail::getDetailStatus, Arrays.asList(DemandStatus.PENDING_PRODUCTION.getValue(), DemandStatus.IN_PRODUCTION.getValue())));
        // 将生产需求信息封转在map中
        Map<Long, List<StoreProductDemandDetail>> demandMap = prodDemandList.stream().collect(Collectors.groupingBy(StoreProductDemandDetail::getStoreProdColorId));
        // 组装返回的对比数量
        List<String> compareStrList = Arrays.asList("库存数量", "在产数量");
        return prodColorList.stream().map(prodColor -> {
            StoreProdStockDTO stock = stockMap.get(prodColor.getId());
            List<StoreProductDemandDetail> demandDetailList = ObjectUtils.defaultIfNull(demandMap.get(prodColor.getId()), Collections.emptyList());
            Integer size30Demand = demandDetailList.stream().map(x -> ObjectUtils.defaultIfNull(x.getSize30(), 0)).reduce(0, Integer::sum);
            Integer size31Demand = demandDetailList.stream().map(x -> ObjectUtils.defaultIfNull(x.getSize31(), 0)).reduce(0, Integer::sum);
            Integer size32Demand = demandDetailList.stream().map(x -> ObjectUtils.defaultIfNull(x.getSize32(), 0)).reduce(0, Integer::sum);
            Integer size33Demand = demandDetailList.stream().map(x -> ObjectUtils.defaultIfNull(x.getSize33(), 0)).reduce(0, Integer::sum);
            Integer size34Demand = demandDetailList.stream().map(x -> ObjectUtils.defaultIfNull(x.getSize34(), 0)).reduce(0, Integer::sum);
            Integer size35Demand = demandDetailList.stream().map(x -> ObjectUtils.defaultIfNull(x.getSize35(), 0)).reduce(0, Integer::sum);
            Integer size36Demand = demandDetailList.stream().map(x -> ObjectUtils.defaultIfNull(x.getSize36(), 0)).reduce(0, Integer::sum);
            Integer size37Demand = demandDetailList.stream().map(x -> ObjectUtils.defaultIfNull(x.getSize37(), 0)).reduce(0, Integer::sum);
            Integer size38Demand = demandDetailList.stream().map(x -> ObjectUtils.defaultIfNull(x.getSize38(), 0)).reduce(0, Integer::sum);
            Integer size39Demand = demandDetailList.stream().map(x -> ObjectUtils.defaultIfNull(x.getSize39(), 0)).reduce(0, Integer::sum);
            Integer size40Demand = demandDetailList.stream().map(x -> ObjectUtils.defaultIfNull(x.getSize40(), 0)).reduce(0, Integer::sum);
            Integer size41Demand = demandDetailList.stream().map(x -> ObjectUtils.defaultIfNull(x.getSize41(), 0)).reduce(0, Integer::sum);
            Integer size42Demand = demandDetailList.stream().map(x -> ObjectUtils.defaultIfNull(x.getSize42(), 0)).reduce(0, Integer::sum);
            Integer size43Demand = demandDetailList.stream().map(x -> ObjectUtils.defaultIfNull(x.getSize43(), 0)).reduce(0, Integer::sum);
            List<Integer> size30List = Arrays.asList(ObjectUtils.isNotEmpty(stock) && ObjectUtils.isNotEmpty(stock.getSize30()) ? stock.getSize30() : 0, size30Demand);
            List<Integer> size31List = Arrays.asList(ObjectUtils.isNotEmpty(stock) && ObjectUtils.isNotEmpty(stock.getSize31()) ? stock.getSize31() : 0, size31Demand);
            List<Integer> size32List = Arrays.asList(ObjectUtils.isNotEmpty(stock) && ObjectUtils.isNotEmpty(stock.getSize32()) ? stock.getSize32() : 0, size32Demand);
            List<Integer> size33List = Arrays.asList(ObjectUtils.isNotEmpty(stock) && ObjectUtils.isNotEmpty(stock.getSize33()) ? stock.getSize33() : 0, size33Demand);
            List<Integer> size34List = Arrays.asList(ObjectUtils.isNotEmpty(stock) && ObjectUtils.isNotEmpty(stock.getSize34()) ? stock.getSize34() : 0, size34Demand);
            List<Integer> size35List = Arrays.asList(ObjectUtils.isNotEmpty(stock) && ObjectUtils.isNotEmpty(stock.getSize35()) ? stock.getSize35() : 0, size35Demand);
            List<Integer> size36List = Arrays.asList(ObjectUtils.isNotEmpty(stock) && ObjectUtils.isNotEmpty(stock.getSize36()) ? stock.getSize36() : 0, size36Demand);
            List<Integer> size37List = Arrays.asList(ObjectUtils.isNotEmpty(stock) && ObjectUtils.isNotEmpty(stock.getSize37()) ? stock.getSize37() : 0, size37Demand);
            List<Integer> size38List = Arrays.asList(ObjectUtils.isNotEmpty(stock) && ObjectUtils.isNotEmpty(stock.getSize38()) ? stock.getSize38() : 0, size38Demand);
            List<Integer> size39List = Arrays.asList(ObjectUtils.isNotEmpty(stock) && ObjectUtils.isNotEmpty(stock.getSize39()) ? stock.getSize39() : 0, size39Demand);
            List<Integer> size40List = Arrays.asList(ObjectUtils.isNotEmpty(stock) && ObjectUtils.isNotEmpty(stock.getSize40()) ? stock.getSize40() : 0, size40Demand);
            List<Integer> size41List = Arrays.asList(ObjectUtils.isNotEmpty(stock) && ObjectUtils.isNotEmpty(stock.getSize41()) ? stock.getSize41() : 0, size41Demand);
            List<Integer> size42List = Arrays.asList(ObjectUtils.isNotEmpty(stock) && ObjectUtils.isNotEmpty(stock.getSize42()) ? stock.getSize42() : 0, size42Demand);
            List<Integer> size43List = Arrays.asList(ObjectUtils.isNotEmpty(stock) && ObjectUtils.isNotEmpty(stock.getSize43()) ? stock.getSize43() : 0, size43Demand);
            return StoreProdDemandQuantityDTO.builder().storeId(storeId).storeProdId(storeProd.getId()).storeProdColorId(prodColor.getId())
                    .prodArtNum(storeProd.getProdArtNum()).colorName(prodColor.getColorName()).compareStrList(compareStrList)
                    // 判断 demandDetailList 中是否有 createTime 为当天的对象
                    .todaySubmitted(demandDetailList.stream().anyMatch(detail -> DateUtils.isSameDay(detail.getCreateTime(), new Date())))
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
        // 用户是否为档口管理者或子账户
        if (!SecurityUtils.isStoreManagerOrSub(demandDTO.getStoreId())) {
            throw new ServiceException("当前用户非档口管理者或子账号，无权限操作!", HttpStatus.ERROR);
        }
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
        // 用户是否为档口管理者或子账户
        if (!SecurityUtils.isStoreManagerOrSub(pageDTO.getStoreId())) {
            throw new ServiceException("当前用户非档口管理者或子账号，无权限操作!", HttpStatus.ERROR);
        }
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
     * 更新产品的生产状态
     * 此方法通过接收一个包含产品生产信息的DTO对象来更新数据库中对应产品的生产状态
     * 主要用于在生产流程中更新产品当前的加工状态或者生产阶段
     *
     * @param workingDTO 包含产品生产信息的数据传输对象用于更新产品生产状态
     * @return
     */
    @Override
    @Transactional
    public Integer updateWorkingStatus(StoreProdDemandWorkingDTO workingDTO) {
        // 用户是否为档口管理者或子账户
        if (!SecurityUtils.isStoreManagerOrSub(workingDTO.getStoreId())) {
            throw new ServiceException("当前用户非档口管理者或子账号，无权限操作!", HttpStatus.ERROR);
        }
        List<StoreProductDemandDetail> demandDetailList = Optional.ofNullable(this.storeProdDemandDetailMapper.selectList(new LambdaQueryWrapper<StoreProductDemandDetail>()
                        .eq(StoreProductDemandDetail::getStoreId, workingDTO.getStoreId()).eq(StoreProductDemandDetail::getDelFlag, Constants.UNDELETED)
                        .in(StoreProductDemandDetail::getId, workingDTO.getStoreProdDemandDetailIdList())))
                .orElseThrow(() -> new ServiceException("需求单明细不存在!", HttpStatus.ERROR));
        demandDetailList.forEach(x -> x.setDetailStatus(DemandStatus.IN_PRODUCTION.getValue()));
        List<BatchResult> list = this.storeProdDemandDetailMapper.updateById(demandDetailList);
        return list.size();
    }

    /**
     * 校验产品需求是否存在
     * 该方法用于验证给定的产品需求信息，在库存中是否存在有效的匹配
     * 主要用于在进行产品需求处理前，确保需求是可满足的或者已存在的
     *
     * @param demandVerifyDTO 包含需求验证信息的DTO对象，用于传递需求校验所需的参数
     * @return 返回一个包含校验结果的DTO对象，包括是否存在以及相关的验证信息
     */
    @Override
    @Transactional(readOnly = true)
    public StoreProdDemandVerifyResDTO verifyDemandExist(StoreProdDemandVerifyDTO demandVerifyDTO) {
        // 用户是否为档口管理者或子账户
        if (!SecurityUtils.isStoreManagerOrSub(demandVerifyDTO.getStoreId())) {
            throw new ServiceException("当前用户非档口管理者或子账号，无权限操作!", HttpStatus.ERROR);
        }
        // 所有的档口颜色ID列表
        List<Long> prodColorIdList = demandVerifyDTO.getDetailList().stream().map(StoreProdDemandVerifyDTO.DetailDTO::getStoreProdColorId).distinct().collect(Collectors.toList());
        List<StoreProductDemandDetail> demandDetailList = this.storeProdDemandDetailMapper.selectList(new LambdaQueryWrapper<StoreProductDemandDetail>()
                .eq(StoreProductDemandDetail::getStoreId, demandVerifyDTO.getStoreId()).eq(StoreProductDemandDetail::getDelFlag, Constants.UNDELETED)
                .in(StoreProductDemandDetail::getDetailStatus, Arrays.asList(DemandStatus.PENDING_PRODUCTION.getValue(), DemandStatus.IN_PRODUCTION.getValue()))
                .in(StoreProductDemandDetail::getStoreProdColorId, prodColorIdList));
        List<String> errorsMsgList = new ArrayList<>();
        if (CollectionUtils.isEmpty(demandDetailList)) {
            demandVerifyDTO.getDetailList().forEach(x -> {
                List<String> errors = this.addErrorMsg(x);
                if (CollectionUtils.isNotEmpty(errors)) {
                    errorsMsgList.addAll(errors);
                }
            });
        } else {
            Map<Long, List<StoreProductDemandDetail>> demandDetailMap = demandDetailList.stream().collect(Collectors.groupingBy(StoreProductDemandDetail::getStoreProdColorId));
            Map<Long, Map<Integer, Integer>> colorSizeQuantityMap = new LinkedHashMap<>();
            demandDetailMap.forEach((prodColorId, tempDemandDetailList) -> {
                Map<Integer, Integer> sizeQuantityMap = new LinkedHashMap<>();
                tempDemandDetailList.forEach(x -> {
                    if (ObjectUtils.isNotEmpty(x.getSize30())) {
                        sizeQuantityMap.put(SIZE_30, sizeQuantityMap.getOrDefault(SIZE_30, 0) + x.getSize30());
                    }
                    if (ObjectUtils.isNotEmpty(x.getSize31())) {
                        sizeQuantityMap.put(SIZE_31, sizeQuantityMap.getOrDefault(SIZE_31, 0) + x.getSize31());
                    }
                    if (ObjectUtils.isNotEmpty(x.getSize32())) {
                        sizeQuantityMap.put(SIZE_32, sizeQuantityMap.getOrDefault(SIZE_32, 0) + x.getSize32());
                    }
                    if (ObjectUtils.isNotEmpty(x.getSize33())) {
                        sizeQuantityMap.put(SIZE_33, sizeQuantityMap.getOrDefault(SIZE_33, 0) + x.getSize33());
                    }
                    if (ObjectUtils.isNotEmpty(x.getSize34())) {
                        sizeQuantityMap.put(SIZE_34, sizeQuantityMap.getOrDefault(SIZE_34, 0) + x.getSize34());
                    }
                    if (ObjectUtils.isNotEmpty(x.getSize35())) {
                        sizeQuantityMap.put(SIZE_35, sizeQuantityMap.getOrDefault(SIZE_35, 0) + x.getSize35());
                    }
                    if (ObjectUtils.isNotEmpty(x.getSize36())) {
                        sizeQuantityMap.put(SIZE_36, sizeQuantityMap.getOrDefault(SIZE_36, 0) + x.getSize36());
                    }
                    if (ObjectUtils.isNotEmpty(x.getSize37())) {
                        sizeQuantityMap.put(SIZE_37, sizeQuantityMap.getOrDefault(SIZE_37, 0) + x.getSize37());
                    }
                    if (ObjectUtils.isNotEmpty(x.getSize38())) {
                        sizeQuantityMap.put(SIZE_38, sizeQuantityMap.getOrDefault(SIZE_38, 0) + x.getSize38());
                    }
                    if (ObjectUtils.isNotEmpty(x.getSize39())) {
                        sizeQuantityMap.put(SIZE_39, sizeQuantityMap.getOrDefault(SIZE_39, 0) + x.getSize39());
                    }
                    if (ObjectUtils.isNotEmpty(x.getSize40())) {
                        sizeQuantityMap.put(SIZE_40, sizeQuantityMap.getOrDefault(SIZE_40, 0) + x.getSize40());
                    }
                    if (ObjectUtils.isNotEmpty(x.getSize41())) {
                        sizeQuantityMap.put(SIZE_41, sizeQuantityMap.getOrDefault(SIZE_41, 0) + x.getSize41());
                    }
                    if (ObjectUtils.isNotEmpty(x.getSize42())) {
                        sizeQuantityMap.put(SIZE_42, sizeQuantityMap.getOrDefault(SIZE_42, 0) + x.getSize42());
                    }
                    if (ObjectUtils.isNotEmpty(x.getSize43())) {
                        sizeQuantityMap.put(SIZE_43, sizeQuantityMap.getOrDefault(SIZE_43, 0) + x.getSize43());
                    }
                });
                if (MapUtils.isNotEmpty(sizeQuantityMap)) {
                    colorSizeQuantityMap.put(prodColorId, sizeQuantityMap);
                }
            });
            // 按照入库的数量依次进行对比，若需求单中不存在匹配信息，则直接返回错误信息
            demandVerifyDTO.getDetailList().forEach(x -> {
                Map<Integer, Integer> sizeQuantityMap = colorSizeQuantityMap.get(x.getStoreProdColorId());
                final String prefix = x.getProdArtNum() + x.getColorName();
                final String suffix = "码";
                // 如果没有对应的需求单则直接返回错误信息
                if (MapUtils.isEmpty(sizeQuantityMap)) {
                    List<String> errors = this.addErrorMsg(x);
                    if (CollectionUtils.isNotEmpty(errors)) {
                        errorsMsgList.addAll(errors);
                    }
                } else {
                    if (ObjectUtils.isNotEmpty(x.getSize30()) && ObjectUtils.isEmpty(sizeQuantityMap.get(SIZE_30))) {
                        errorsMsgList.add(prefix + SIZE_30 + suffix);
                    }
                    if (ObjectUtils.isNotEmpty(x.getSize31()) && ObjectUtils.isEmpty(sizeQuantityMap.get(SIZE_31))) {
                        errorsMsgList.add(prefix + SIZE_31 + suffix);
                    }
                    if (ObjectUtils.isNotEmpty(x.getSize32()) && ObjectUtils.isEmpty(sizeQuantityMap.get(SIZE_32))) {
                        errorsMsgList.add(prefix + SIZE_32 + suffix);
                    }
                    if (ObjectUtils.isNotEmpty(x.getSize33()) && ObjectUtils.isEmpty(sizeQuantityMap.get(SIZE_33))) {
                        errorsMsgList.add(prefix + SIZE_33 + suffix);
                    }
                    if (ObjectUtils.isNotEmpty(x.getSize34()) && ObjectUtils.isEmpty(sizeQuantityMap.get(SIZE_34))) {
                        errorsMsgList.add(prefix + SIZE_34 + suffix);
                    }
                    if (ObjectUtils.isNotEmpty(x.getSize35()) && ObjectUtils.isEmpty(sizeQuantityMap.get(SIZE_35))) {
                        errorsMsgList.add(prefix + SIZE_35 + suffix);
                    }
                    if (ObjectUtils.isNotEmpty(x.getSize36()) && ObjectUtils.isEmpty(sizeQuantityMap.get(SIZE_36))) {
                        errorsMsgList.add(prefix + SIZE_36 + suffix);
                    }
                    if (ObjectUtils.isNotEmpty(x.getSize37()) && ObjectUtils.isEmpty(sizeQuantityMap.get(SIZE_37))) {
                        errorsMsgList.add(prefix + SIZE_37 + suffix);
                    }
                    if (ObjectUtils.isNotEmpty(x.getSize38()) && ObjectUtils.isEmpty(sizeQuantityMap.get(SIZE_38))) {
                        errorsMsgList.add(prefix + SIZE_38 + suffix);
                    }
                    if (ObjectUtils.isNotEmpty(x.getSize39()) && ObjectUtils.isEmpty(sizeQuantityMap.get(SIZE_39))) {
                        errorsMsgList.add(prefix + SIZE_39 + suffix);
                    }
                    if (ObjectUtils.isNotEmpty(x.getSize40()) && ObjectUtils.isEmpty(sizeQuantityMap.get(SIZE_40))) {
                        errorsMsgList.add(prefix + SIZE_40 + suffix);
                    }
                    if (ObjectUtils.isNotEmpty(x.getSize41()) && ObjectUtils.isEmpty(sizeQuantityMap.get(SIZE_41))) {
                        errorsMsgList.add(prefix + SIZE_41 + suffix);
                    }
                    if (ObjectUtils.isNotEmpty(x.getSize42()) && ObjectUtils.isEmpty(sizeQuantityMap.get(SIZE_42))) {
                        errorsMsgList.add(prefix + SIZE_42 + suffix);
                    }
                    if (ObjectUtils.isNotEmpty(x.getSize43()) && ObjectUtils.isEmpty(sizeQuantityMap.get(SIZE_43))) {
                        errorsMsgList.add(prefix + SIZE_43 + suffix);
                    }
                }
            });
        }
        return new StoreProdDemandVerifyResDTO() {{
            setErrorMsgList(errorsMsgList);
        }};
    }

    /**
     * 批量删除档口商品需求单
     *
     * @param deleteDTO 需要删除的档口商品需求单主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteByStoreProdDemandIds(StoreProdDemandDeleteDTO deleteDTO) {
        // 用户是否为档口管理者或子账户
        if (!SecurityUtils.isStoreManagerOrSub(deleteDTO.getStoreId())) {
            throw new ServiceException("当前用户非档口管理者或子账号，无权限操作!", HttpStatus.ERROR);
        }
        List<StoreProductDemandDetail> demandDetailList = Optional.ofNullable(this.storeProdDemandDetailMapper.selectList(new LambdaQueryWrapper<StoreProductDemandDetail>()
                        .eq(StoreProductDemandDetail::getStoreId, deleteDTO.getStoreId()).eq(StoreProductDemandDetail::getDelFlag, Constants.UNDELETED)
                        .in(StoreProductDemandDetail::getId, deleteDTO.getStoreProdDemandDetailIdList())))
                .orElseThrow(() -> new ServiceException("需求单明细不存在!", HttpStatus.ERROR));
        // 根据需求明细ID能否找到对应的入库与需求抵扣关系数据
        List<StoreProductStorageDemandDeduct> deductList = this.storageDemandDeductMapper.selectList(new LambdaQueryWrapper<StoreProductStorageDemandDeduct>()
                .in(StoreProductStorageDemandDeduct::getStoreProdDemandDetailId, deleteDTO.getStoreProdDemandDetailIdList())
                .eq(StoreProductStorageDemandDeduct::getDelFlag, Constants.UNDELETED));
        if (CollectionUtils.isEmpty(deductList)) {
            // 标记需求详情列表中的所有记录为已删除
            demandDetailList.forEach(x -> x.setDelFlag(Constants.DELETED));
            // 批量更新需求详情列表中的所有记录
            List<BatchResult> list = this.storeProdDemandDetailMapper.updateById(demandDetailList);
            // 查询所有未删除的需求详情记录
            List<StoreProductDemandDetail> allDemandDetailList = this.storeProdDemandDetailMapper.selectList(new LambdaQueryWrapper<StoreProductDemandDetail>()
                    .eq(StoreProductDemandDetail::getStoreId, deleteDTO.getStoreId())
                    .in(StoreProductDemandDetail::getStoreProdDemandId, demandDetailList.stream()
                            .map(StoreProductDemandDetail::getStoreProdDemandId).distinct().collect(Collectors.toList())));
            // 将查询结果按需求ID分组
            Map<Long, List<StoreProductDemandDetail>> demandGroupMap = allDemandDetailList.stream().collect(Collectors.groupingBy(StoreProductDemandDetail::getStoreProdDemandId));
            // 遍历分组后的结果，找出所有记录都被删除的需求ID
            List<Long> deleteDemandIdList = new ArrayList<>();
            demandGroupMap.forEach((demandId, tempDetailList) -> {
                if (tempDetailList.stream().allMatch(x -> Objects.equals(x.getDelFlag(), Constants.DELETED))) {
                    deleteDemandIdList.add(demandId);
                }
            });
            // 如果存在所有记录都被删除的需求ID，则删除这些需求
            if (CollectionUtils.isNotEmpty(deleteDemandIdList)) {
                this.storeProdDemandMapper.deleteDemandList(deleteDemandIdList);
            }
            // 返回更新的需求详情记录数
            return list.size();
        } else {
            // 按照入库单明细ID进行分组，获取每个入库单明细对应的抵扣数量
            Map<Long, List<StoreProductStorageDemandDeduct>> deductQuantityMap = deductList.stream().collect(Collectors
                    .groupingBy(StoreProductStorageDemandDeduct::getStoreProdDemandDetailId, Collectors.toList()));
            // key 为入库单明细ID，value 为该入库单明细对应的尺码与数量
            Map<Long, Map<Integer, Integer>> deductSizeQuantityMap = new HashMap<>();
            deductQuantityMap.forEach((demandDetailId, sizeDeductList) -> {
                Map<Integer, Integer> sizeQuantityMap = new HashMap<>();
                Integer size30Quantity = sizeDeductList.stream().filter(x -> Objects.equals(x.getSize(), SIZE_30)).map(x -> ObjectUtils.defaultIfNull(x.getQuantity(), 0)).reduce(0, Integer::sum);
                sizeQuantityMap.put(SIZE_30, size30Quantity);
                Integer size31Quantity = sizeDeductList.stream().filter(x -> Objects.equals(x.getSize(), SIZE_31)).map(x -> ObjectUtils.defaultIfNull(x.getQuantity(), 0)).reduce(0, Integer::sum);
                sizeQuantityMap.put(SIZE_31, size31Quantity);
                Integer size32Quantity = sizeDeductList.stream().filter(x -> Objects.equals(x.getSize(), SIZE_32)).map(x -> ObjectUtils.defaultIfNull(x.getQuantity(), 0)).reduce(0, Integer::sum);
                sizeQuantityMap.put(SIZE_32, size32Quantity);
                Integer size33Quantity = sizeDeductList.stream().filter(x -> Objects.equals(x.getSize(), SIZE_33)).map(x -> ObjectUtils.defaultIfNull(x.getQuantity(), 0)).reduce(0, Integer::sum);
                sizeQuantityMap.put(SIZE_33, size33Quantity);
                Integer size34Quantity = sizeDeductList.stream().filter(x -> Objects.equals(x.getSize(), SIZE_34)).map(x -> ObjectUtils.defaultIfNull(x.getQuantity(), 0)).reduce(0, Integer::sum);
                sizeQuantityMap.put(SIZE_34, size34Quantity);
                Integer size35Quantity = sizeDeductList.stream().filter(x -> Objects.equals(x.getSize(), SIZE_35)).map(x -> ObjectUtils.defaultIfNull(x.getQuantity(), 0)).reduce(0, Integer::sum);
                sizeQuantityMap.put(SIZE_35, size35Quantity);
                Integer size36Quantity = sizeDeductList.stream().filter(x -> Objects.equals(x.getSize(), SIZE_36)).map(x -> ObjectUtils.defaultIfNull(x.getQuantity(), 0)).reduce(0, Integer::sum);
                sizeQuantityMap.put(SIZE_36, size36Quantity);
                Integer size37Quantity = sizeDeductList.stream().filter(x -> Objects.equals(x.getSize(), SIZE_37)).map(x -> ObjectUtils.defaultIfNull(x.getQuantity(), 0)).reduce(0, Integer::sum);
                sizeQuantityMap.put(SIZE_37, size37Quantity);
                Integer size38Quantity = sizeDeductList.stream().filter(x -> Objects.equals(x.getSize(), SIZE_38)).map(x -> ObjectUtils.defaultIfNull(x.getQuantity(), 0)).reduce(0, Integer::sum);
                sizeQuantityMap.put(SIZE_38, size38Quantity);
                Integer size39Quantity = sizeDeductList.stream().filter(x -> Objects.equals(x.getSize(), SIZE_39)).map(x -> ObjectUtils.defaultIfNull(x.getQuantity(), 0)).reduce(0, Integer::sum);
                sizeQuantityMap.put(SIZE_39, size39Quantity);
                Integer size40Quantity = sizeDeductList.stream().filter(x -> Objects.equals(x.getSize(), SIZE_40)).map(x -> ObjectUtils.defaultIfNull(x.getQuantity(), 0)).reduce(0, Integer::sum);
                sizeQuantityMap.put(SIZE_40, size40Quantity);
                Integer size41Quantity = sizeDeductList.stream().filter(x -> Objects.equals(x.getSize(), SIZE_41)).map(x -> ObjectUtils.defaultIfNull(x.getQuantity(), 0)).reduce(0, Integer::sum);
                sizeQuantityMap.put(SIZE_41, size41Quantity);
                Integer size42Quantity = sizeDeductList.stream().filter(x -> Objects.equals(x.getSize(), SIZE_42)).map(x -> ObjectUtils.defaultIfNull(x.getQuantity(), 0)).reduce(0, Integer::sum);
                sizeQuantityMap.put(SIZE_42, size42Quantity);
                Integer size43Quantity = sizeDeductList.stream().filter(x -> Objects.equals(x.getSize(), SIZE_43)).map(x -> ObjectUtils.defaultIfNull(x.getQuantity(), 0)).reduce(0, Integer::sum);
                sizeQuantityMap.put(SIZE_43, size43Quantity);
                deductSizeQuantityMap.put(demandDetailId, sizeQuantityMap);
            });
            // 依次更新每个明细、每个尺码的数量
            demandDetailList.forEach(demandDetail -> {
                Integer quantity = 0;
                Map<Integer, Integer> sizeQuantityMap = deductSizeQuantityMap.get(demandDetail.getId());
                if (MapUtils.isEmpty(sizeQuantityMap)) {
                    return;
                }
                if (ObjectUtils.isNotEmpty(demandDetail.getSize30())) {
                    demandDetail.setSize30(ObjectUtils.defaultIfNull(sizeQuantityMap.get(SIZE_30), 0));
                    quantity += demandDetail.getSize30();
                }
                if (ObjectUtils.isNotEmpty(demandDetail.getSize31())) {
                    demandDetail.setSize31(ObjectUtils.defaultIfNull(sizeQuantityMap.get(SIZE_31), 0));
                    quantity += demandDetail.getSize31();
                }
                if (ObjectUtils.isNotEmpty(demandDetail.getSize32())) {
                    demandDetail.setSize32(ObjectUtils.defaultIfNull(sizeQuantityMap.get(SIZE_32), 0));
                    quantity += demandDetail.getSize32();
                }
                if (ObjectUtils.isNotEmpty(demandDetail.getSize33())) {
                    demandDetail.setSize33(ObjectUtils.defaultIfNull(sizeQuantityMap.get(SIZE_33), 0));
                    quantity += demandDetail.getSize33();
                }
                if (ObjectUtils.isNotEmpty(demandDetail.getSize34())) {
                    demandDetail.setSize34(ObjectUtils.defaultIfNull(sizeQuantityMap.get(SIZE_34), 0));
                    quantity += demandDetail.getSize34();
                }
                if (ObjectUtils.isNotEmpty(demandDetail.getSize35())) {
                    demandDetail.setSize35(ObjectUtils.defaultIfNull(sizeQuantityMap.get(SIZE_35), 0));
                    quantity += demandDetail.getSize35();
                }
                if (ObjectUtils.isNotEmpty(demandDetail.getSize36())) {
                    demandDetail.setSize36(ObjectUtils.defaultIfNull(sizeQuantityMap.get(SIZE_36), 0));
                    quantity += demandDetail.getSize36();
                }
                if (ObjectUtils.isNotEmpty(demandDetail.getSize37())) {
                    demandDetail.setSize37(ObjectUtils.defaultIfNull(sizeQuantityMap.get(SIZE_37), 0));
                    quantity += demandDetail.getSize37();
                }
                if (ObjectUtils.isNotEmpty(demandDetail.getSize38())) {
                    demandDetail.setSize38(ObjectUtils.defaultIfNull(sizeQuantityMap.get(SIZE_38), 0));
                    quantity += demandDetail.getSize38();
                }
                if (ObjectUtils.isNotEmpty(demandDetail.getSize39())) {
                    demandDetail.setSize39(ObjectUtils.defaultIfNull(sizeQuantityMap.get(SIZE_39), 0));
                    quantity += demandDetail.getSize39();
                }
                if (ObjectUtils.isNotEmpty(demandDetail.getSize40())) {
                    demandDetail.setSize40(ObjectUtils.defaultIfNull(sizeQuantityMap.get(SIZE_40), 0));
                    quantity += demandDetail.getSize40();
                }
                if (ObjectUtils.isNotEmpty(demandDetail.getSize41())) {
                    demandDetail.setSize41(ObjectUtils.defaultIfNull(sizeQuantityMap.get(SIZE_41), 0));
                    quantity += demandDetail.getSize41();
                }
                if (ObjectUtils.isNotEmpty(demandDetail.getSize42())) {
                    demandDetail.setSize42(ObjectUtils.defaultIfNull(sizeQuantityMap.get(SIZE_42), 0));
                    quantity += demandDetail.getSize42();
                }
                if (ObjectUtils.isNotEmpty(demandDetail.getSize43())) {
                    demandDetail.setSize43(ObjectUtils.defaultIfNull(sizeQuantityMap.get(SIZE_43), 0));
                    quantity += demandDetail.getSize43();
                }
                demandDetail
                        // 更新状态为已完成
                        .setDetailStatus(DemandStatus.PRODUCTION_COMPLETE.getValue())
                        // 更新数量为最新数量
                        .setQuantity(quantity);
            });
            this.storeProdDemandDetailMapper.updateById(demandDetailList);
            // 有需求明细的抵扣关系，则将已完成的入库单数量变更为当前需求单数量 并将需求明细状态置为已完成
            List<StoreProductDemandDetail> listAfterUpdate = this.storeProdDemandDetailMapper.selectList(new LambdaQueryWrapper<StoreProductDemandDetail>()
                    .in(StoreProductDemandDetail::getStoreProdDemandId, demandDetailList.stream().map(StoreProductDemandDetail::getStoreProdDemandId).collect(Collectors.toList()))
                    .eq(StoreProductDemandDetail::getDelFlag, Constants.UNDELETED));
            Map<Long, List<StoreProductDemandDetail>> demandMap = listAfterUpdate.stream().collect(Collectors.groupingBy(StoreProductDemandDetail::getStoreProdDemandId));
            List<Long> completeDemandIdList = new ArrayList<>();
            demandMap.forEach((demandId, detailListAfterUpdate) -> {
                if (detailListAfterUpdate.stream().allMatch(x -> Objects.equals(x.getDetailStatus(), DemandStatus.PRODUCTION_COMPLETE.getValue()))) {
                    completeDemandIdList.add(demandId);
                }
            });
            if (CollectionUtils.isNotEmpty(completeDemandIdList)) {
                this.storeProdDemandMapper.updateStatusByIds(completeDemandIdList);
            }
            return demandDetailList.size();
        }
    }


    /**
     * 根据商品详情信息添加错误消息
     * 此方法遍历商品的不同尺寸，如果该尺寸有错误，则生成相应的错误消息
     * 错误消息格式为：商品的货号和颜色名称 + 尺寸 + "码"
     *
     * @param detailDTO 商品详情对象，包含商品的各个尺寸信息
     * @return 返回一个字符串列表，包含所有错误消息
     */
    private List<String> addErrorMsg(StoreProdDemandVerifyDTO.DetailDTO detailDTO) {
        List<String> errorMsgList = new ArrayList<>();
        final String prefix = detailDTO.getProdArtNum() + detailDTO.getColorName();
        final String suffix = "码";
        if (ObjectUtils.isNotEmpty(detailDTO.getSize30())) {
            errorMsgList.add(prefix + SIZE_30 + suffix);
        }
        if (ObjectUtils.isNotEmpty(detailDTO.getSize31())) {
            errorMsgList.add(prefix + SIZE_31 + suffix);
        }
        if (ObjectUtils.isNotEmpty(detailDTO.getSize32())) {
            errorMsgList.add(prefix + SIZE_32 + suffix);
        }
        if (ObjectUtils.isNotEmpty(detailDTO.getSize33())) {
            errorMsgList.add(prefix + SIZE_33 + suffix);
        }
        if (ObjectUtils.isNotEmpty(detailDTO.getSize34())) {
            errorMsgList.add(prefix + SIZE_34 + suffix);
        }
        if (ObjectUtils.isNotEmpty(detailDTO.getSize35())) {
            errorMsgList.add(prefix + SIZE_35 + suffix);
        }
        if (ObjectUtils.isNotEmpty(detailDTO.getSize36())) {
            errorMsgList.add(prefix + SIZE_36 + suffix);
        }
        if (ObjectUtils.isNotEmpty(detailDTO.getSize37())) {
            errorMsgList.add(prefix + SIZE_37 + suffix);
        }
        if (ObjectUtils.isNotEmpty(detailDTO.getSize38())) {
            errorMsgList.add(prefix + SIZE_38 + suffix);
        }
        if (ObjectUtils.isNotEmpty(detailDTO.getSize39())) {
            errorMsgList.add(prefix + SIZE_39 + suffix);
        }
        if (ObjectUtils.isNotEmpty(detailDTO.getSize40())) {
            errorMsgList.add(prefix + SIZE_40 + suffix);
        }
        if (ObjectUtils.isNotEmpty(detailDTO.getSize41())) {
            errorMsgList.add(prefix + SIZE_41 + suffix);
        }
        if (ObjectUtils.isNotEmpty(detailDTO.getSize42())) {
            errorMsgList.add(prefix + SIZE_42 + suffix);
        }
        if (ObjectUtils.isNotEmpty(detailDTO.getSize43())) {
            errorMsgList.add(prefix + SIZE_43 + suffix);
        }
        return errorMsgList;
    }


}
