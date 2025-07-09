package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.xkt.domain.StoreProductColorSize;
import com.ruoyi.xkt.domain.StoreProductFile;
import com.ruoyi.xkt.domain.StoreProductStock;
import com.ruoyi.xkt.domain.SysFile;
import com.ruoyi.xkt.dto.storeProductFile.StoreProdMainPicDTO;
import com.ruoyi.xkt.dto.storeProductStock.*;
import com.ruoyi.xkt.enums.FileType;
import com.ruoyi.xkt.mapper.*;
import com.ruoyi.xkt.service.IStoreProductStockService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.ibatis.executor.BatchResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.ruoyi.common.constant.Constants.*;

/**
 * 档口商品库存Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
@RequiredArgsConstructor
public class StoreProductStockServiceImpl implements IStoreProductStockService {

    final StoreProductStockMapper storeProdStockMapper;
    final StoreProductFileMapper storeProdFileMapper;
    final SysFileMapper fileMapper;
    final StoreProductColorPriceMapper prodColorPriceMapper;
    final StoreCustomerProductDiscountMapper storeCusProdDiscMapper;
    final StoreProductMapper storeProdMapper;
    final StoreProductColorSizeMapper prodColorSizeMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<StoreProdStockPageResDTO> selectPage(StoreProdStockPageDTO pageDTO) {
        // 用户是否为档口管理者或子账户
        if (!SecurityUtils.isAdmin() && !SecurityUtils.isStoreManagerOrSub(pageDTO.getStoreId())) {
            throw new ServiceException("当前用户非档口管理者或子账号，无权限操作!", HttpStatus.ERROR);
        }
        PageHelper.startPage(pageDTO.getPageNum(), pageDTO.getPageSize());
        List<StoreProdStockPageResDTO> stockList = this.storeProdStockMapper.selectStockPage(pageDTO);
        if (CollectionUtils.isEmpty(stockList)) {
            return Page.empty(pageDTO.getPageSize(), pageDTO.getPageNum());
        }
        // 提取查询结果中的商店产品ID列表
        List<Long> storeProdIdList = stockList.stream().map(StoreProdStockPageResDTO::getStoreProdId).collect(Collectors.toList());
        // 查找排名第一个商品主图列表
        List<StoreProdMainPicDTO> mainPicList = this.storeProdFileMapper.selectMainPicByStoreProdIdList(storeProdIdList, FileType.MAIN_PIC.getValue(), 1);
        Map<Long, String> mainPicMap = CollectionUtils.isEmpty(mainPicList) ? new HashMap<>() : mainPicList.stream()
                .collect(Collectors.toMap(StoreProdMainPicDTO::getStoreProdId, StoreProdMainPicDTO::getFileUrl));
        // 为每个产品设置主图URL和标准尺码列表
        stockList.forEach(x -> x.setMainPicUrl(mainPicMap.get(x.getStoreProdId())));
        return Page.convert(new PageInfo<>(stockList));
    }

    /**
     * 根据档口ID和商品货号查询档口商品库存
     *
     * @param storeId    档口ID
     * @param prodArtNum 商品货号
     * @return String
     */
    @Override
    @Transactional(readOnly = true)
    public List<StoreProdStockResDTO> selectByStoreIdAndProdArtNum(Long storeId, String prodArtNum) {
        List<StoreProductStock> stockList = this.storeProdStockMapper.selectList(new LambdaQueryWrapper<StoreProductStock>()
                .eq(StoreProductStock::getStoreId, storeId).eq(StoreProductStock::getProdArtNum, prodArtNum)
                .eq(StoreProductStock::getDelFlag, Constants.UNDELETED));
        return CollectionUtils.isEmpty(stockList) ? new ArrayList<>()
                : stockList.stream().map(x -> BeanUtil.toBean(x, StoreProdStockResDTO.class).setStoreProdStockId(x.getId())).collect(Collectors.toList());
    }

    /**
     * 根据档口ID 和 档口商品颜色ID 查询商品库存
     *
     * @param dto          入参
     * @return StoreProdStockResDTO
     */
    @Override
    @Transactional(readOnly = true)
    public StoreProdStockAndDiscountResDTO getStockAndCusDiscount(StoreProdStockAndDiscountDTO dto) {
        // 用户是否为档口管理者或子账户
        if (!SecurityUtils.isAdmin() && !SecurityUtils.isStoreManagerOrSub(dto.getStoreId())) {
            throw new ServiceException("当前用户非档口管理者或子账号，无权限操作!", HttpStatus.ERROR);
        }
        // 商品颜色等基本信息
        StoreProdStockAndDiscountResDTO basicInfoDTO = ObjectUtils.defaultIfNull(this.prodColorPriceMapper.selectStockAndCusDiscount(dto),
                new StoreProdStockAndDiscountResDTO());
        // 档口商品颜色 的 库存
        StoreProductStock stock = this.storeProdStockMapper.selectOne(new LambdaQueryWrapper<StoreProductStock>()
                .eq(StoreProductStock::getStoreId, dto.getStoreId()).eq(StoreProductStock::getStoreProdColorId, dto.getStoreProdColorId())
                .eq(StoreProductStock::getDelFlag, Constants.UNDELETED));
        // 标准尺码
        List<StoreProductColorSize> prodColorSizeList = this.prodColorSizeMapper.selectList(new LambdaQueryWrapper<StoreProductColorSize>()
                .eq(StoreProductColorSize::getStoreProdId, basicInfoDTO.getStoreProdId()).eq(StoreProductColorSize::getStoreColorId, basicInfoDTO.getStoreColorId())
                .eq(StoreProductColorSize::getDelFlag, Constants.UNDELETED));
        List<StoreProdStockAndDiscountResDTO.SPSADSizeDTO> sizeStockList = prodColorSizeList.stream().map(size -> new StoreProdStockAndDiscountResDTO.SPSADSizeDTO()
                .setSize(size.getSize()).setStock(this.getSizeStock(size.getSize(), stock))).collect(Collectors.toList());
        return basicInfoDTO.setStoreCusId(dto.getStoreCusId()).setStoreId(dto.getStoreId()).setSizeStockList(sizeStockList);
    }


    /**
     * 库存盘点
     *
     * @param storeId       档口ID
     * @param checkStockDTO 盘点入参
     * @return Integer
     */
    @Override
    @Transactional
    public Integer checkAndUpdateStock(Long storeId, StoreProdCheckStockDTO checkStockDTO) {
        // 用户是否为档口管理者或子账户
        if (!SecurityUtils.isAdmin() && !SecurityUtils.isStoreManagerOrSub(storeId)) {
            throw new ServiceException("当前用户非档口管理者或子账号，无权限操作!", HttpStatus.ERROR);
        }
        List<StoreProductStock> stockList = Optional.ofNullable(this.storeProdStockMapper.selectList(new LambdaQueryWrapper<StoreProductStock>()
                        .in(StoreProductStock::getStoreProdColorId, checkStockDTO.getCheckStockList().stream().map(StoreProdCheckStockDTO.SPCSStockDTO::getStoreProdColorId).collect(Collectors.toList()))
                        .eq(StoreProductStock::getDelFlag, Constants.UNDELETED).eq(StoreProductStock::getStoreId, storeId)))
                .orElseThrow(() -> new ServiceException("当前商品库存不存在，请联系客服!", HttpStatus.ERROR));
        // 每一个颜色
        Map<Long, Map<Integer, Integer>> prodColorSizeMap = checkStockDTO.getCheckStockList().stream().collect(Collectors
                .groupingBy(StoreProdCheckStockDTO.SPCSStockDTO::getStoreProdColorId, Collectors
                        .toMap(StoreProdCheckStockDTO.SPCSStockDTO::getSize, StoreProdCheckStockDTO.SPCSStockDTO::getStock)));
        stockList.forEach(dbStock -> {
            Map<Integer, Integer> sizeStockMap = prodColorSizeMap.get(dbStock.getStoreProdColorId());
            if (MapUtils.isEmpty(sizeStockMap)) {
                return;
            }
            if (ObjectUtils.isNotEmpty(sizeStockMap.get(SIZE_30))) {
                dbStock.setSize30(sizeStockMap.get(SIZE_30));
            }
            if (ObjectUtils.isNotEmpty(sizeStockMap.get(SIZE_31))) {
                dbStock.setSize31(sizeStockMap.get(SIZE_31));
            }
            if (ObjectUtils.isNotEmpty(sizeStockMap.get(SIZE_32))) {
                dbStock.setSize32(sizeStockMap.get(SIZE_32));
            }
            if (ObjectUtils.isNotEmpty(sizeStockMap.get(SIZE_33))) {
                dbStock.setSize33(sizeStockMap.get(SIZE_33));
            }
            if (ObjectUtils.isNotEmpty(sizeStockMap.get(SIZE_34))) {
                dbStock.setSize34(sizeStockMap.get(SIZE_34));
            }
            if (ObjectUtils.isNotEmpty(sizeStockMap.get(SIZE_35))) {
                dbStock.setSize35(sizeStockMap.get(SIZE_35));
            }
            if (ObjectUtils.isNotEmpty(sizeStockMap.get(SIZE_36))) {
                dbStock.setSize36(sizeStockMap.get(SIZE_36));
            }
            if (ObjectUtils.isNotEmpty(sizeStockMap.get(SIZE_37))) {
                dbStock.setSize37(sizeStockMap.get(SIZE_37));
            }
            if (ObjectUtils.isNotEmpty(sizeStockMap.get(SIZE_38))) {
                dbStock.setSize38(sizeStockMap.get(SIZE_38));
            }
            if (ObjectUtils.isNotEmpty(sizeStockMap.get(SIZE_39))) {
                dbStock.setSize39(sizeStockMap.get(SIZE_39));
            }
            if (ObjectUtils.isNotEmpty(sizeStockMap.get(SIZE_40))) {
                dbStock.setSize40(sizeStockMap.get(SIZE_40));
            }
            if (ObjectUtils.isNotEmpty(sizeStockMap.get(SIZE_41))) {
                dbStock.setSize41(sizeStockMap.get(SIZE_41));
            }
            if (ObjectUtils.isNotEmpty(sizeStockMap.get(SIZE_42))) {
                dbStock.setSize42(sizeStockMap.get(SIZE_42));
            }
            if (ObjectUtils.isNotEmpty(sizeStockMap.get(SIZE_43))) {
                dbStock.setSize43(sizeStockMap.get(SIZE_43));
            }
        });
        return this.storeProdStockMapper.updateById(stockList).size();
    }

    /**
     * 导出库存明细
     *
     * @param exportDTO 导出入参
     * @return List<StoreProdStockDownloadDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public List<StoreProdStockDownloadDTO> export(StoreProdStockExportDTO exportDTO) {
        // 用户是否为档口管理者或子账户
        if (!SecurityUtils.isAdmin() && !SecurityUtils.isStoreManagerOrSub(exportDTO.getStoreId())) {
            throw new ServiceException("当前用户非档口管理者或子账号，无权限操作!", HttpStatus.ERROR);
        }
        // 导出勾选
        if (CollectionUtils.isNotEmpty(exportDTO.getStoreProdStockIdList())) {
            return this.storeProdStockMapper.selectExportList(exportDTO.getStoreProdStockIdList());
            // 导出所有库存明细
        } else {
            Optional.ofNullable(exportDTO.getFullExport())
                    .orElseThrow(() -> new ServiceException("全量导出，fullExport不能为空!", HttpStatus.ERROR));
            return this.storeProdStockMapper.selectAllStockList();
        }
    }


    /**
     * 增加库存
     *
     * @param increaseStockList 增加库存入参
     * @return int
     */
    @Override
    @Transactional
    public int increaseStock(Long storeId, List<StoreProdStockDTO> increaseStockList) {
        // 根据关键信息找到已存在的库存
        List<StoreProductStock> existStockList = this.storeProdStockMapper.selectList(new LambdaQueryWrapper<StoreProductStock>()
                .eq(StoreProductStock::getStoreId, storeId).eq(StoreProductStock::getDelFlag, Constants.UNDELETED)
                .in(StoreProductStock::getStoreProdId, increaseStockList.stream().map(StoreProdStockDTO::getStoreProdId).collect(Collectors.toList()))
                .in(StoreProductStock::getStoreProdColorId, increaseStockList.stream().map(StoreProdStockDTO::getStoreProdColorId).collect(Collectors.toList())));
        // 已存在的档口商品颜色库存map
        Map<Long, StoreProductStock> existStockMap = existStockList.stream().collect(Collectors.toMap(StoreProductStock::getStoreProdColorId, Function.identity()));
        // 总的待更新的库存列表
        List<StoreProductStock> stockList = increaseStockList.stream().map(increseStock -> {
            StoreProductStock stock = existStockMap.containsKey(increseStock.getStoreProdColorId())
                    ? this.adjustStock(existStockMap.get(increseStock.getStoreProdColorId()), increseStock, Boolean.TRUE)
                    : BeanUtil.toBean(increseStock, StoreProductStock.class);
            return stock.setStoreId(storeId);
        }).collect(Collectors.toList());
        List<BatchResult> list = this.storeProdStockMapper.insertOrUpdate(stockList);
        return list.size();
    }

    /**
     * 减少库存
     *
     * @param storeId           档口ID
     * @param decreaseStockList 减少库存入参
     * @return int
     */
    @Override
    @Transactional
    public int decreaseStock(Long storeId, List<StoreProdStockDTO> decreaseStockList) {
        // 根据关键信息找到已存在的库存
        List<StoreProductStock> existStockList = this.storeProdStockMapper.selectList(new LambdaQueryWrapper<StoreProductStock>()
                .eq(StoreProductStock::getStoreId, storeId).eq(StoreProductStock::getDelFlag, Constants.UNDELETED)
                .in(StoreProductStock::getStoreProdId, decreaseStockList.stream().map(StoreProdStockDTO::getStoreProdId).collect(Collectors.toList()))
                .in(StoreProductStock::getStoreProdColorId, decreaseStockList.stream().map(StoreProdStockDTO::getStoreProdColorId).collect(Collectors.toList())));
        // 待扣减的库存map
        Map<Long, StoreProdStockDTO> decreaseStockMap = decreaseStockList.stream().collect(Collectors.toMap(StoreProdStockDTO::getStoreProdColorId, Function.identity()));
        existStockList.forEach(stock -> this.adjustStock(stock, decreaseStockMap.get(stock.getStoreProdColorId()), Boolean.FALSE));
        List<BatchResult> list = this.storeProdStockMapper.updateById(existStockList);
        return list.size();
    }

    /**
     * 清空库存
     *
     * @param clearZeroDTO 清空库存入参
     * @return int
     */
    @Override
    @Transactional
    public int clearStockToZero(StoreProdStockClearZeroDTO clearZeroDTO) {
        // 用户是否为档口管理者或子账户
        if (!SecurityUtils.isAdmin() && !SecurityUtils.isStoreManagerOrSub(clearZeroDTO.getStoreId())) {
            throw new ServiceException("当前用户非档口管理者或子账号，无权限操作!", HttpStatus.ERROR);
        }
        List<StoreProductStock> stockList = Optional.ofNullable(this.storeProdStockMapper.selectList(new LambdaQueryWrapper<StoreProductStock>()
                        .eq(StoreProductStock::getStoreId, clearZeroDTO.getStoreId()).eq(StoreProductStock::getDelFlag, Constants.UNDELETED)
                        .in(StoreProductStock::getId, clearZeroDTO.getStoreProdStockIdList())))
                .orElseThrow(() -> new ServiceException("档口商品库存不存在!", HttpStatus.ERROR));
        stockList.forEach(stock -> stock.setSize30(0).setSize31(0).setSize32(0).setSize33(0).setSize34(0).setSize35(0).setSize36(0).setSize37(0)
                .setSize38(0).setSize39(0).setSize40(0).setSize41(0).setSize42(0).setSize43(0));
        return this.storeProdStockMapper.updateById(stockList).size();
    }


    /**
     * 调整库存
     *
     * @param storeId          档口ID
     * @param updateStockList  库存更新diff list  要包含正负数
     * @param multiplierFactor 乘积因子 0 直接调整库存，将库存更新为页面输入的数量 1 不变数量
     * @return int
     */
    @Override
    @Transactional
    public int updateStock(Long storeId, List<StoreProdStockDTO> updateStockList, Integer multiplierFactor) {
        // 用户是否为档口管理者或子账户
        if (!SecurityUtils.isAdmin() && !SecurityUtils.isStoreManagerOrSub(storeId)) {
            throw new ServiceException("当前用户非档口管理者或子账号，无权限操作!", HttpStatus.ERROR);
        }
        List<StoreProductStock> stockList = Optional.ofNullable(this.storeProdStockMapper.selectList(new LambdaQueryWrapper<StoreProductStock>()
                        .in(StoreProductStock::getStoreProdId, updateStockList.stream().map(StoreProdStockDTO::getStoreProdId).collect(Collectors.toList()))
                        .eq(StoreProductStock::getStoreId, storeId)
                        .eq(StoreProductStock::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("档口商品库存不存在!", HttpStatus.ERROR));
        // 数据库数据map
        Map<String, StoreProdStockDTO> diffStockMap = updateStockList.stream().collect(Collectors
                .toMap(stock -> stock.getProdArtNum() + stock.getStoreProdId() + stock.getStoreProdColorId(), Function.identity()));
        List<StoreProductStock> updateList = new ArrayList<>();
        stockList.forEach(stock -> {
            StoreProdStockDTO updateStock = diffStockMap.get(stock.getProdArtNum() + stock.getStoreProdId() + stock.getStoreProdColorId());
            if (ObjectUtils.isEmpty(updateStock)) {
                return;
            }
            if (ObjectUtils.isNotEmpty(updateStock.getSize30())) {
                stock.setSize30(ObjectUtils.defaultIfNull(stock.getSize30(), 0) * multiplierFactor + updateStock.getSize30());
            }
            if (ObjectUtils.isNotEmpty(updateStock.getSize31())) {
                stock.setSize31(ObjectUtils.defaultIfNull(stock.getSize31(), 0) * multiplierFactor + updateStock.getSize31());
            }
            if (ObjectUtils.isNotEmpty(updateStock.getSize32())) {
                stock.setSize32(ObjectUtils.defaultIfNull(stock.getSize32(), 0) * multiplierFactor + updateStock.getSize32());
            }
            if (ObjectUtils.isNotEmpty(updateStock.getSize33())) {
                stock.setSize33(ObjectUtils.defaultIfNull(stock.getSize33(), 0) * multiplierFactor + updateStock.getSize33());
            }
            if (ObjectUtils.isNotEmpty(updateStock.getSize34())) {
                stock.setSize34(ObjectUtils.defaultIfNull(stock.getSize34(), 0) * multiplierFactor + updateStock.getSize34());
            }
            if (ObjectUtils.isNotEmpty(updateStock.getSize35())) {
                stock.setSize35(ObjectUtils.defaultIfNull(stock.getSize35(), 0) * multiplierFactor + updateStock.getSize35());
            }
            if (ObjectUtils.isNotEmpty(updateStock.getSize36())) {
                stock.setSize36(ObjectUtils.defaultIfNull(stock.getSize36(), 0) * multiplierFactor + updateStock.getSize36());
            }
            if (ObjectUtils.isNotEmpty(updateStock.getSize37())) {
                stock.setSize37(ObjectUtils.defaultIfNull(stock.getSize37(), 0) * multiplierFactor + updateStock.getSize37());
            }
            if (ObjectUtils.isNotEmpty(updateStock.getSize38())) {
                stock.setSize38(ObjectUtils.defaultIfNull(stock.getSize38(), 0) * multiplierFactor + updateStock.getSize38());
            }
            if (ObjectUtils.isNotEmpty(updateStock.getSize39())) {
                stock.setSize39(ObjectUtils.defaultIfNull(stock.getSize39(), 0) * multiplierFactor + updateStock.getSize39());
            }
            if (ObjectUtils.isNotEmpty(updateStock.getSize40())) {
                stock.setSize40(ObjectUtils.defaultIfNull(stock.getSize40(), 0) * multiplierFactor + updateStock.getSize40());
            }
            if (ObjectUtils.isNotEmpty(updateStock.getSize41())) {
                stock.setSize41(ObjectUtils.defaultIfNull(stock.getSize41(), 0) * multiplierFactor + updateStock.getSize41());
            }
            if (ObjectUtils.isNotEmpty(updateStock.getSize42())) {
                stock.setSize42(ObjectUtils.defaultIfNull(stock.getSize42(), 0) * multiplierFactor + updateStock.getSize42());
            }
            if (ObjectUtils.isNotEmpty(updateStock.getSize43())) {
                stock.setSize43(ObjectUtils.defaultIfNull(stock.getSize43(), 0) * multiplierFactor + updateStock.getSize43());
            }
            updateList.add(stock);
        });
        List<BatchResult> list = this.storeProdStockMapper.updateById(updateList);
        return list.size();
    }


    /**
     * 查询档口商品库存
     *
     * @param storeId          档口ID
     * @param storeProdStockId 档口商品库存主键
     * @return 档口商品库存
     */
    @Override
    @Transactional(readOnly = true)
    public StoreProdStockResDTO selectByStoreProdStockId(Long storeId, Long storeProdStockId) {
        StoreProductStock stock = Optional.ofNullable(this.storeProdStockMapper.selectOne(new LambdaQueryWrapper<StoreProductStock>()
                        .eq(StoreProductStock::getId, storeProdStockId).eq(StoreProductStock::getStoreId, storeId)
                        .eq(StoreProductStock::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("档口商品库存不存在!", HttpStatus.ERROR));
        // 档口商品第一张主图
        StoreProductFile mainPic = Optional.ofNullable(this.storeProdFileMapper.selectOne(new LambdaQueryWrapper<StoreProductFile>()
                        .eq(StoreProductFile::getStoreProdId, stock.getStoreProdId()).eq(StoreProductFile::getStoreId, storeId)
                        .eq(StoreProductFile::getDelFlag, Constants.UNDELETED).eq(StoreProductFile::getFileType, FileType.MAIN_PIC.getValue())
                        .eq(StoreProductFile::getOrderNum, ORDER_NUM_1)))
                .orElseThrow(() -> new ServiceException("商品主图不存在!", HttpStatus.ERROR));
        // 图片
        SysFile file = Optional.ofNullable(this.fileMapper.selectOne(new LambdaQueryWrapper<SysFile>()
                        .eq(SysFile::getId, mainPic.getFileId()).eq(SysFile::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("商品主图不存在!", HttpStatus.ERROR));
        return BeanUtil.toBean(stock, StoreProdStockResDTO.class)
                .setStoreProdStockId(stock.getId()).setMainPicUrl(file.getFileUrl());
    }

    /**
     * 通过数量diff增减或扣减库存
     *
     * @param stock      数据库库存表
     * @param adjustDTO  diff数据
     * @param isInCrease true 增加库存 false 减少库存
     * @return StoreProductStock
     */
    private StoreProductStock adjustStock(StoreProductStock stock, StoreProdStockDTO adjustDTO, Boolean isInCrease) {
        if (ObjectUtils.isEmpty(adjustDTO)) {
            return stock;
        }
        // 如果isInCrease为true，则为1，反之则为-1
        int adjustSign = isInCrease ? 1 : -1;
        if (ObjectUtils.isNotEmpty(adjustDTO.getSize30())) {
            stock.setSize30(adjustDTO.getSize30() * adjustSign + ObjectUtils.defaultIfNull(stock.getSize30(), 0));
        }
        if (ObjectUtils.isNotEmpty(adjustDTO.getSize31())) {
            stock.setSize31(adjustDTO.getSize31() * adjustSign + ObjectUtils.defaultIfNull(stock.getSize31(), 0));
        }
        if (ObjectUtils.isNotEmpty(adjustDTO.getSize32())) {
            stock.setSize32(adjustDTO.getSize32() * adjustSign + ObjectUtils.defaultIfNull(stock.getSize32(), 0));
        }
        if (ObjectUtils.isNotEmpty(adjustDTO.getSize33())) {
            stock.setSize33(adjustDTO.getSize33() * adjustSign + ObjectUtils.defaultIfNull(stock.getSize33(), 0));
        }
        if (ObjectUtils.isNotEmpty(adjustDTO.getSize34())) {
            stock.setSize34(adjustDTO.getSize34() * adjustSign + ObjectUtils.defaultIfNull(stock.getSize34(), 0));
        }
        if (ObjectUtils.isNotEmpty(adjustDTO.getSize35())) {
            stock.setSize35(adjustDTO.getSize35() * adjustSign + ObjectUtils.defaultIfNull(stock.getSize35(), 0));
        }
        if (ObjectUtils.isNotEmpty(adjustDTO.getSize36())) {
            stock.setSize36(adjustDTO.getSize36() * adjustSign + ObjectUtils.defaultIfNull(stock.getSize36(), 0));
        }
        if (ObjectUtils.isNotEmpty(adjustDTO.getSize37())) {
            stock.setSize37(adjustDTO.getSize37() * adjustSign + ObjectUtils.defaultIfNull(stock.getSize37(), 0));
        }
        if (ObjectUtils.isNotEmpty(adjustDTO.getSize38())) {
            stock.setSize38(adjustDTO.getSize38() * adjustSign + ObjectUtils.defaultIfNull(stock.getSize38(), 0));
        }
        if (ObjectUtils.isNotEmpty(adjustDTO.getSize39())) {
            stock.setSize39(adjustDTO.getSize39() * adjustSign + ObjectUtils.defaultIfNull(stock.getSize39(), 0));
        }
        if (ObjectUtils.isNotEmpty(adjustDTO.getSize40())) {
            stock.setSize40(adjustDTO.getSize40() * adjustSign + ObjectUtils.defaultIfNull(stock.getSize40(), 0));
        }
        if (ObjectUtils.isNotEmpty(adjustDTO.getSize41())) {
            stock.setSize41(adjustDTO.getSize41() * adjustSign + ObjectUtils.defaultIfNull(stock.getSize41(), 0));
        }
        if (ObjectUtils.isNotEmpty(adjustDTO.getSize42())) {
            stock.setSize42(adjustDTO.getSize42() * adjustSign + ObjectUtils.defaultIfNull(stock.getSize42(), 0));
        }
        if (ObjectUtils.isNotEmpty(adjustDTO.getSize43())) {
            stock.setSize43(adjustDTO.getSize43() * adjustSign + ObjectUtils.defaultIfNull(stock.getSize43(), 0));
        }
        return stock;
    }

    /**
     * 获取商品某尺寸的库存
     * @param size 尺寸
     * @param stock 库存
     * @return 库存
     */
    private Integer getSizeStock(Integer size, StoreProductStock stock) {
        if (ObjectUtils.isEmpty(stock)) {
            return null;
        }
        if (size == 30) {
            return stock.getSize30();
        } else if (size == 31) {
            return stock.getSize31();
        }
        else if (size == 32) {
            return stock.getSize32();
        }
        else if (size == 33) {
            return stock.getSize33();
        }
        else if (size == 34) {
            return stock.getSize34();
        }
        else if (size == 35) {
            return stock.getSize35();
        }
        else if (size == 36) {
            return stock.getSize36();
        }
        else if (size == 37) {
            return stock.getSize37();
        }
        else if (size == 38) {
            return stock.getSize38();
        }
        else if (size == 39) {
            return stock.getSize39();
        }
        else if (size == 40) {
            return stock.getSize40();
        }
        else if (size == 41) {
            return stock.getSize41();
        }
        else if (size == 42) {
            return stock.getSize42();
        }
        else if (size == 43) {
            return stock.getSize43();
        }
        return null;
    }


}
