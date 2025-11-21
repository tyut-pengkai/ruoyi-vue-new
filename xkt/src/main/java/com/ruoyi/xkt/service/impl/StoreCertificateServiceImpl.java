package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.model.ESystemRole;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.sms.SmsClientWrapper;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.xkt.domain.Store;
import com.ruoyi.xkt.domain.StoreCertificate;
import com.ruoyi.xkt.domain.SysFile;
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
    final IAssetService assetService;
    final StoreSaleDetailMapper saleDetailMapper;
    final StoreProductMapper storeProdMapper;
    final ISysUserService userService;
    final SmsClientWrapper smsClient;


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
        storeCert = this.handleStoreCertFileList(certDTO, storeCert);
        return this.storeCertMapper.insert(storeCert);
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
        storeCert = this.handleStoreCertFileList(certDTO, storeCert);
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

    @Transactional(readOnly = true)
    @Override
    public void sendSmsVerificationCode(String phoneNumber) {
        boolean success = smsClient.sendVerificationCode(CacheConstants.SMS_STORE_AUTH_CAPTCHA_CODE_CD_PHONE_NUM_KEY,
                CacheConstants.SMS_STORE_AUTH_CAPTCHA_CODE_KEY, phoneNumber, RandomUtil.randomNumbers(6));
        if (!success) {
            throw new ServiceException("短信发送失败");
        }
    }

    @Transactional(readOnly = true)
    @Override
    public void validateSmsVerificationCode(String phoneNumber, String code) {
        boolean match = smsClient.matchVerificationCode(CacheConstants.SMS_STORE_AUTH_CAPTCHA_CODE_KEY, phoneNumber, code);
        if (!match) {
            throw new ServiceException("验证码错误或已过期");
        }
    }


    /**
     * 新增档口认证文件列表
     *
     * @param certDTO   档口认证文件入参
     * @param storeCert 档口认证对象
     */
    private StoreCertificate handleStoreCertFileList(StoreCertDTO certDTO, StoreCertificate storeCert) {
        // 新增身份证人脸
        StoreCertDTO.SCStoreFileDTO idCardFace = certDTO.getStoreCert().getFileList().stream().filter(x -> Objects.equals(x.getFileType(), FileType.ID_CARD_FACE.getValue()))
                .findFirst().orElseThrow(() -> new ServiceException("身份证人脸文件不存在!", HttpStatus.ERROR));
        StoreCertDTO.SCStoreFileDTO idCardEmblem = certDTO.getStoreCert().getFileList().stream().filter(x -> Objects.equals(x.getFileType(), FileType.ID_CARD_EMBLEM.getValue()))
                .findFirst().orElseThrow(() -> new ServiceException("身份证国徽文件不存在!", HttpStatus.ERROR));
        StoreCertDTO.SCStoreFileDTO license = certDTO.getStoreCert().getFileList().stream().filter(x -> Objects.equals(x.getFileType(), FileType.BUSINESS_LICENSE.getValue()))
                .findFirst().orElseThrow(() -> new ServiceException("营业执照文件不存在!", HttpStatus.ERROR));
        SysFile idCardFaceFile = BeanUtil.toBean(idCardFace, SysFile.class);
        SysFile idCardEmblemFile = BeanUtil.toBean(idCardEmblem, SysFile.class);
        SysFile licenseFile = BeanUtil.toBean(license, SysFile.class);
        this.fileMapper.insert(Arrays.asList(idCardFaceFile, idCardEmblemFile, licenseFile));
        return storeCert.setIdCardFaceFileId(idCardFaceFile.getId()).setIdCardEmblemFileId(idCardEmblemFile.getId()).setLicenseFileId(licenseFile.getId());
    }

    /**
     * 新增档口
     *
     * @param certDTO
     */
    private Store createStore(StoreCertDTO certDTO) {
        // 判断档口名称是否已存在
        List<Store> storeList = this.storeMapper.selectList(new LambdaQueryWrapper<Store>()
                .eq(Store::getDelFlag, Constants.UNDELETED).eq(Store::getStoreName, certDTO.getStoreBasic().getStoreName().trim()));
        if (ObjectUtils.isNotEmpty(storeList)) {
            throw new ServiceException("该档口名称已存在，请修改后重新提交!", HttpStatus.ERROR);
        }
        Store store = BeanUtil.toBean(certDTO.getStoreBasic(), Store.class);
        // 初始化注册时只需绑定用户ID即可
        store.setUserId(SecurityUtils.getUserId());
        // 默认档口状态为：待审核
        store.setStoreStatus(StoreStatus.UN_AUDITED.getValue());
        // 设置已使用空间为0
        store.setStorageUsage(BigDecimal.ZERO);
        // 默认使用第一个模板
        store.setTemplateNum(1);
        // 试用期往后推3个月
        Date threeMonthAfter = Date.from(LocalDate.now().plusMonths(3).atStartOfDay(ZoneId.systemDefault()).toInstant());
        store.setServiceEndTime(threeMonthAfter);
        // 设置档口默认权重 0
        store.setStoreWeight(Constants.WEIGHT_DEFAULT_ZERO);
        // 设置默认的库存系统为 步橘 ，后续可在条码迁移之处修改
        store.setStockSys(StockSysType.BU_JU.getValue());
        // 档口浏览次数设置为0
        store.setViewCount(0L);
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


}
