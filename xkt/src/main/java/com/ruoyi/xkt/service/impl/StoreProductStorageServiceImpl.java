package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.xkt.domain.StoreProductStorage;
import com.ruoyi.xkt.domain.StoreProductStorageDetail;
import com.ruoyi.xkt.dto.storeProdStorage.StoreProdStorageDTO;
import com.ruoyi.xkt.dto.storeProdStorage.StoreProdStoragePageDTO;
import com.ruoyi.xkt.dto.storeProdStorage.StoreProdStoragePageResDTO;
import com.ruoyi.xkt.dto.storeProdStorage.StoreProdStorageResDTO;
import com.ruoyi.xkt.dto.storeProductStock.StoreProdStockUpdateDTO;
import com.ruoyi.xkt.mapper.StoreProductStorageDetailMapper;
import com.ruoyi.xkt.mapper.StoreProductStorageMapper;
import com.ruoyi.xkt.service.IStoreProductStockService;
import com.ruoyi.xkt.service.IStoreProductStorageService;
import com.ruoyi.xkt.service.IVoucherSequenceService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 档口商品入库Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
@RequiredArgsConstructor
public class StoreProductStorageServiceImpl implements IStoreProductStorageService {

    final StoreProductStorageMapper storageMapper;
    final StoreProductStorageDetailMapper storageDetailMapper;
    final IVoucherSequenceService sequenceService;
    final IStoreProductStockService stockService;


    /**
     * 分页查询
     *
     * @param pageDTO 查询入参
     * @return Page
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StoreProdStoragePageResDTO> page(StoreProdStoragePageDTO pageDTO) {
        PageHelper.startPage(pageDTO.getPageNum(), pageDTO.getPageSize());
        List<StoreProdStoragePageResDTO> list = this.storageMapper.selectStoragePage(pageDTO);
        return Page.convert(new PageInfo<>(list));
    }

    /**
     * 新增档口商品入库
     *
     * @param storeProdStorageDTO 档口商品入库
     * @return 结果
     */
    @Override
    @Transactional
    public int create(StoreProdStorageDTO storeProdStorageDTO) {
        // 生成code
        String code = this.sequenceService.generateCode(storeProdStorageDTO.getStoreId(), "STORAGE", DateUtils.parseDateToStr(DateUtils.YYYYMMDD, new Date()));
        // 总的数量
        Integer totalNum = storeProdStorageDTO.getDetailList().stream().map(x -> ObjectUtils.defaultIfNull(x.getQuantity(), 0)).reduce(0, Integer::sum);
        // 总的金额
        BigDecimal produceAmount = storeProdStorageDTO.getDetailList().stream().map(x -> ObjectUtils.defaultIfNull(x.getProduceAmount(), BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add);
        // 当前登录用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        // 新增档口商品入库
        StoreProductStorage storeProdStorage = BeanUtil.toBean(storeProdStorageDTO, StoreProductStorage.class)
                .setCode(code).setQuantity(totalNum).setProduceAmount(produceAmount).setOperatorId(loginUser.getUserId()).setOperatorName(loginUser.getUsername());
        int count = this.storageMapper.insert(storeProdStorage);
        // 新增档口商品入库明细
        List<StoreProductStorageDetail> detailList  = storeProdStorageDTO.getDetailList().stream().map(x -> BeanUtil.toBean(x, StoreProductStorageDetail.class)
                .setStoreProdStorId(storeProdStorage.getId())).collect(Collectors.toList());
        this.storageDetailMapper.insert(detailList);
        // 构造增加库存的入参DTO
        List<StoreProdStockUpdateDTO> increaseStockList = BeanUtil.copyToList(detailList, StoreProdStockUpdateDTO.class);
        // 增加档口商品的库存
        this.stockService.increaseStock(storeProdStorageDTO.getStoreId(), increaseStockList);
        return count;
    }



    /**
     * 查询档口商品入库
     *
     * @param storeProdStorId 档口商品入库主键
     * @return 档口商品入库
     */
    @Override
    @Transactional(readOnly = true)
    public StoreProdStorageResDTO selectByStoreProdStorId(Long storeProdStorId) {
        // 档口商品入库
        StoreProductStorage storage = Optional.ofNullable(this.storageMapper.selectOne(new LambdaQueryWrapper<StoreProductStorage>()
                .eq(StoreProductStorage::getId, storeProdStorId).eq(StoreProductStorage::getDelFlag, "0")))
                .orElseThrow(() -> new ServiceException("档口商品入库不存在!", HttpStatus.ERROR));
        // 档口商品入库明细
        List<StoreProductStorageDetail> storageDetailList = storageDetailMapper.selectList(new LambdaQueryWrapper<StoreProductStorageDetail>()
                .eq(StoreProductStorageDetail::getStoreProdStorId, storeProdStorId).eq(StoreProductStorageDetail::getDelFlag, "0"));
        return BeanUtil.toBean(storage, StoreProdStorageResDTO.class)
                .setDetailList(storageDetailList.stream().map(x -> BeanUtil.toBean(x, StoreProdStorageResDTO.StorageDetailDTO.class)).collect(Collectors.toList()));
    }

    /**
     * 查询档口商品入库列表
     *
     * @param storeProductStorage 档口商品入库
     * @return 档口商品入库
     */
    @Override
    @Transactional(readOnly = true)
    public List<StoreProductStorage> selectStoreProductStorageList(StoreProductStorage storeProductStorage) {
        return storageMapper.selectStoreProductStorageList(storeProductStorage);
    }

    /**
     * 撤销档口商品入库
     *
     * @param storeProdStorId 需要撤销档口商品入库主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteByStoreProdStorId(Long storeProdStorId) {
        // 档口商品入库
        StoreProductStorage storage = Optional.ofNullable(this.storageMapper.selectOne(new LambdaQueryWrapper<StoreProductStorage>()
                        .eq(StoreProductStorage::getId, storeProdStorId).eq(StoreProductStorage::getDelFlag, "0")))
                .orElseThrow(() -> new ServiceException("档口商品入库不存在!", HttpStatus.ERROR));
        storage.setDelFlag("2");
       int count = this.storageMapper.updateById(storage);
        // 档口商品入库明细
        List<StoreProductStorageDetail> storageDetailList = storageDetailMapper.selectList(new LambdaQueryWrapper<StoreProductStorageDetail>()
                .eq(StoreProductStorageDetail::getStoreProdStorId, storeProdStorId).eq(StoreProductStorageDetail::getDelFlag, "0"));
        storageDetailList.forEach(x -> x.setDelFlag("2"));
        this.storageDetailMapper.updateById(storageDetailList);
       // 减少档口商品库存
        this.stockService.decreaseStock(storage.getStoreId(), storageDetailList.stream()
                .map(x -> BeanUtil.toBean(x, StoreProdStockUpdateDTO.class)).collect(Collectors.toList()));
        return count;
    }





    /**
     * 修改档口商品入库
     *
     * @param storeProductStorage 档口商品入库
     * @return 结果
     */
    @Override
    @Transactional
    public int updateStoreProductStorage(StoreProductStorage storeProductStorage) {
        storeProductStorage.setUpdateTime(DateUtils.getNowDate());
        return storageMapper.updateStoreProductStorage(storeProductStorage);
    }



    /**
     * 删除档口商品入库信息
     *
     * @param storeProdStorId 档口商品入库主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteStoreProductStorageByStoreProdStorId(Long storeProdStorId) {
        return storageMapper.deleteStoreProductStorageByStoreProdStorId(storeProdStorId);
    }


}
