package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.model.ESystemRole;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.xkt.domain.Store;
import com.ruoyi.xkt.domain.StoreCertificate;
import com.ruoyi.xkt.domain.SysFile;
import com.ruoyi.xkt.domain.VoucherSequence;
import com.ruoyi.xkt.dto.storeCertificate.StoreCertDTO;
import com.ruoyi.xkt.dto.storeCertificate.StoreCertResDTO;
import com.ruoyi.xkt.dto.storeCertificate.StoreCertStepResDTO;
import com.ruoyi.xkt.enums.FileType;
import com.ruoyi.xkt.enums.StockSysType;
import com.ruoyi.xkt.enums.StoreStatus;
import com.ruoyi.xkt.mapper.*;
import com.ruoyi.xkt.service.IAssetService;
import com.ruoyi.xkt.service.IStoreCertificateService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 档口认证Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
@RequiredArgsConstructor
public class StoreCertificateServiceImpl implements IStoreCertificateService {

    final StoreCertificateMapper storeCertMapper;
    final SysFileMapper fileMapper;
    final StoreMapper storeMapper;
    final RedisCache redisCache;
    final IAssetService assetService;
    final DailySaleMapper dailySaleMapper;
    final DailySaleCustomerMapper dailySaleCusMapper;
    final StoreSaleDetailMapper saleDetailMapper;
    final StoreProductMapper storeProdMapper;
    final DailySaleProductMapper dailySaleProdMapper;
    final ISysUserService userService;
    final VoucherSequenceMapper vsMapper;

    /**
     * 新增档口认证
     *
     * @param certDTO 档口认证入参
     * @return int
     */
    @Override
    @Transactional
    public Integer create(StoreCertDTO certDTO) {
        // 新增档口
        Store store = this.createStore(certDTO);
        StoreCertificate storeCert = BeanUtil.toBean(certDTO.getStoreCert(), StoreCertificate.class)
                .setStoreId(store.getId());
        // 新增档口认证的文件列表
        this.handleStoreCertFileList(certDTO, storeCert);
        int count = this.storeCertMapper.insert(storeCert);
        // 新增档口的单据编号初始化 销售出库、采购入库、需求单、订单
        List<VoucherSequence> vsList = new ArrayList<>();
        // 销售出库
        vsList.add(this.initVoucherSequence(store.getId(), Constants.VOUCHER_SEQ_STORE_SALE_TYPE, Constants.VOUCHER_SEQ_STORE_SALE_PREFIX));
        // 采购入库
        vsList.add(this.initVoucherSequence(store.getId(), Constants.VOUCHER_SEQ_STORAGE_TYPE, Constants.VOUCHER_SEQ_STORAGE_PREFIX));
        // 需求单
        vsList.add(this.initVoucherSequence(store.getId(), Constants.VOUCHER_SEQ_DEMAND_TYPE, Constants.VOUCHER_SEQ_DEMAND_PREFIX));
        // 代发订单
        vsList.add(this.initVoucherSequence(store.getId(), Constants.VOUCHER_SEQ_STORE_ORDER_TYPE, Constants.VOUCHER_SEQ_STORE_ORDER_PREFIX));
        this.vsMapper.insert(vsList);
        return count;
    }


    /**
     * 根据档口ID获取档口详情信息
     *
     * @param storeId 档口ID
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public StoreCertResDTO getInfo(Long storeId) {
        StoreCertificate storeCert = this.storeCertMapper.selectOne(new LambdaQueryWrapper<StoreCertificate>()
                .eq(StoreCertificate::getStoreId, storeId).eq(StoreCertificate::getDelFlag, Constants.UNDELETED));
        if (ObjectUtils.isEmpty(storeCert)) {
            return new StoreCertResDTO();
        }
        List<SysFile> fileList = Optional.ofNullable(this.fileMapper.selectList(new LambdaQueryWrapper<SysFile>()
                .in(SysFile::getId, Arrays.asList(storeCert.getIdCardFaceFileId(), storeCert.getIdCardEmblemFileId(), storeCert.getLicenseFileId()))
                .eq(SysFile::getDelFlag, Constants.UNDELETED))).orElseThrow(() -> new ServiceException("文件不存在!", HttpStatus.ERROR));
        List<StoreCertResDTO.StoreCertFileDTO> fileDTOList = fileList.stream().map(x -> BeanUtil.toBean(x, StoreCertResDTO.StoreCertFileDTO.class)
                        .setFileType(Objects.equals(x.getId(), storeCert.getIdCardFaceFileId()) ? FileType.ID_CARD_FACE.getValue() :
                                (Objects.equals(x.getId(), storeCert.getIdCardEmblemFileId()) ? FileType.ID_CARD_EMBLEM.getValue() : FileType.BUSINESS_LICENSE.getValue())))
                .collect(Collectors.toList());
        return BeanUtil.toBean(storeCert, StoreCertResDTO.class).setStoreCertId(storeCert.getId()).setFileList(fileDTOList);
    }

    /**
     * 编辑档口认证信息
     *
     * @param certDTO 档口认证信息
     * @return Integer
     */
    @Override
    @Transactional
    public Integer update(StoreCertDTO certDTO) {
        StoreCertificate storeCert = Optional.ofNullable(this.storeCertMapper.selectOne(new LambdaQueryWrapper<StoreCertificate>()
                        .eq(StoreCertificate::getId, certDTO.getStoreCertId()).eq(StoreCertificate::getDelFlag, Constants.UNDELETED)
                        .eq(StoreCertificate::getStoreId, certDTO.getStoreId())))
                .orElseThrow(() -> new ServiceException("档口认证不存在!", HttpStatus.ERROR));
        // 更新档口认证信息
        List<SysFile> oldFileList = Optional.ofNullable(this.fileMapper.selectList(new LambdaQueryWrapper<SysFile>()
                        .eq(SysFile::getDelFlag, Constants.UNDELETED).in(SysFile::getId,
                                Arrays.asList(storeCert.getIdCardFaceFileId(), storeCert.getIdCardEmblemFileId(), storeCert.getLicenseFileId()))))
                .orElseThrow(() -> new ServiceException("档口认证相关文件不存在!", HttpStatus.ERROR));
        this.fileMapper.updateById(oldFileList.stream().peek(x -> x.setDelFlag(Constants.DELETED)).collect(Collectors.toList()));
        // 更新属性
        BeanUtil.copyProperties(certDTO.getStoreCert(), storeCert);
        // 新增档口认证的文件列表
        this.handleStoreCertFileList(certDTO, storeCert);
        // 更新档口信息
        this.updateStore(certDTO.getStoreId(), certDTO.getStoreBasic());
        return this.storeCertMapper.updateById(storeCert);
    }

    /**
     * 新增认证流程 获取认证信息
     *
     * @param storeId 档口ID
     * @return StoreCertStepResDTO
     */
    @Override
    @Transactional(readOnly = true)
    public StoreCertStepResDTO getStepCertInfo(Long storeId) {
        StoreCertificate storeCert = this.storeCertMapper.selectOne(new LambdaQueryWrapper<StoreCertificate>()
                .eq(StoreCertificate::getStoreId, storeId).eq(StoreCertificate::getDelFlag, Constants.UNDELETED));
        if (ObjectUtils.isEmpty(storeCert)) {
            return new StoreCertStepResDTO();
        }
        Store store = this.storeMapper.selectOne(new LambdaQueryWrapper<Store>()
                .eq(Store::getId, storeId).eq(Store::getDelFlag, Constants.UNDELETED));
        // 获取认证的证照信息
        List<Long> fileIdList = Arrays.asList(storeCert.getIdCardFaceFileId(), storeCert.getIdCardEmblemFileId(), storeCert.getLicenseFileId());
        List<SysFile> fileList = Optional.ofNullable(this.fileMapper.selectList(new LambdaQueryWrapper<SysFile>()
                        .eq(SysFile::getDelFlag, Constants.UNDELETED).in(SysFile::getId, fileIdList)))
                .orElseThrow(() -> new ServiceException("文件不存在!", HttpStatus.ERROR));
        // 档口认证所有的文件列表
        List<StoreCertStepResDTO.SCStoreFileDTO> fileDTOList = fileList.stream().map(x -> BeanUtil.toBean(x, StoreCertStepResDTO.SCStoreFileDTO.class)
                        .setFileType(Objects.equals(x.getId(), storeCert.getIdCardFaceFileId()) ? FileType.ID_CARD_FACE.getValue() :
                                (Objects.equals(x.getId(), storeCert.getIdCardEmblemFileId())
                                        ? FileType.ID_CARD_EMBLEM.getValue() : FileType.BUSINESS_LICENSE.getValue())))
                .collect(Collectors.toList());
        StoreCertStepResDTO certStepDTO = new StoreCertStepResDTO()
                .setStoreCert(BeanUtil.toBean(storeCert, StoreCertStepResDTO.SCSStoreCertDTO.class).setStoreCertId(storeCert.getId()).setFileList(fileDTOList))
                .setStoreBasic(BeanUtil.toBean(store, StoreCertStepResDTO.SCSStoreBasicDTO.class));
        // 设置档口LOGO
        if (ObjectUtils.isNotEmpty(store.getStoreLogoId())) {
            SysFile storeLogo = this.fileMapper.selectById(store.getStoreLogoId());
            certStepDTO.getStoreBasic().setStoreLogo(BeanUtil.toBean(storeLogo, StoreCertStepResDTO.SCStoreFileDTO.class));
        }
        return certStepDTO;
    }


    /**
     * 新增档口认证文件列表
     *
     * @param certDTO   档口认证文件入参
     * @param storeCert 档口认证对象
     */
    private void handleStoreCertFileList(StoreCertDTO certDTO, StoreCertificate storeCert) {
        // 档口认证文件类型与文件名映射
        Map<Integer, String> typeNameTransMap = certDTO.getStoreCert().getFileList().stream().collect(Collectors
                .toMap(StoreCertDTO.SCStoreFileDTO::getFileType, StoreCertDTO.SCStoreFileDTO::getFileName));
        // 上传的文件列表
        final List<StoreCertDTO.SCStoreFileDTO> fileDTOList = certDTO.getStoreCert().getFileList();
        // 将文件插入到SysFile表中
        List<SysFile> fileList = BeanUtil.copyToList(fileDTOList, SysFile.class);
        this.fileMapper.insert(fileList);
        // 文件名称与文件ID映射
        Map<String, Long> fileMap = fileList.stream().collect(Collectors.toMap(SysFile::getFileName, SysFile::getId));
        // 设置身份证人脸文件ID
        storeCert.setIdCardFaceFileId(fileMap.get(typeNameTransMap.get(FileType.ID_CARD_FACE.getValue())));
        // 设置身份证国徽文件ID
        storeCert.setIdCardEmblemFileId(fileMap.get(typeNameTransMap.get(FileType.ID_CARD_EMBLEM.getValue())));
        // 设置营业执照文件ID
        storeCert.setLicenseFileId(fileMap.get(typeNameTransMap.get(FileType.BUSINESS_LICENSE.getValue())));
    }

    /**
     * 新增档口
     *
     * @param certDTO
     */
    private Store createStore(StoreCertDTO certDTO) {
        Store store = BeanUtil.toBean(certDTO.getStoreBasic(), Store.class);
        // 初始化注册时只需绑定用户ID即可
        store.setUserId(SecurityUtils.getUserId());
        // 默认档口状态为：待审核
        store.setStoreStatus(StoreStatus.UN_AUDITED.getValue());
        // 设置已使用空间为0
        store.setStorageUsage(BigDecimal.ZERO);
        // 默认使用第一个模板
        store.setTemplateNum(1);
        // 当前时间往后推1年为试用期时间
        Date oneYearAfter = Date.from(LocalDate.now().plusYears(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        store.setTrialEndTime(oneYearAfter);
        // 设置档口默认权重 0
        store.setStoreWeight(Constants.STORE_WEIGHT_DEFAULT_ZERO);
        // 设置默认的库存系统为 步橘 ，后续可在条码迁移之处修改
        store.setStockSys(StockSysType.BU_JU.getValue());
        // 已使用的档口空间
        BigDecimal storageUsage = certDTO.getStoreCert().getFileList().stream().map(x -> ObjectUtils.defaultIfNull(x.getFileSize(), BigDecimal.ZERO))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        // 新增档口LOGO
        if (ObjectUtils.isNotEmpty(certDTO.getStoreBasic().getStoreLogo())) {
            SysFile file = BeanUtil.toBean(certDTO.getStoreBasic().getStoreLogo(), SysFile.class);
            this.fileMapper.insert(file);
            store.setStoreLogoId(file.getId());
            store.setStorageUsage(storageUsage.add(file.getFileSize()));
        }
        this.storeMapper.insert(store);
        // 创建档口账户
        assetService.createInternalAccountIfNotExists(store.getId());
        // 档口用户绑定
        userService.refreshRelStore(store.getUserId(), ESystemRole.SUPPLIER.getId());
        // 放到redis中
        redisCache.setCacheObject(CacheConstants.STORE_KEY + store.getId(), store);
        return store;
    }

    /**
     * 认证流程更新档口
     *
     * @param storeId    档口ID
     * @param storeBasic 档口基本信息
     */
    private void updateStore(Long storeId, StoreCertDTO.SCStoreBasicDTO storeBasic) {
        Store store = Optional.ofNullable(this.storeMapper.selectOne(new LambdaQueryWrapper<Store>()
                        .eq(Store::getId, storeId).eq(Store::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("档口不存在!", HttpStatus.ERROR));
        BeanUtil.copyProperties(storeBasic, store);
        if (ObjectUtils.isNotEmpty(storeBasic.getStoreLogo())) {
            // 直接新增
            SysFile file = BeanUtil.toBean(storeBasic.getStoreLogo(), SysFile.class);
            this.fileMapper.insert(file);
            store.setStoreLogoId(file.getId());
        }
        // 默认档口状态为：待审核
        store.setStoreStatus(StoreStatus.UN_AUDITED.getValue());
        this.storeMapper.updateById(store);
    }

    /**
     * 初始化各类单据情况
     *
     * @param storeId 档口ID
     * @param type    类型
     * @param prefix  前缀
     * @return
     */
    private VoucherSequence initVoucherSequence(Long storeId, String type, String prefix) {
        return new VoucherSequence().setStoreId(storeId).setType(type).setDateFormat(DateUtils.YYYY_MM_DD)
                .setPrefix(prefix).setNextSequence(1).setSequenceFormat(Constants.VOUCHER_SEQ_FORMAT);
    }


}
