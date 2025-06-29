package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.model.ESystemRole;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.xkt.domain.Store;
import com.ruoyi.xkt.domain.StoreCertificate;
import com.ruoyi.xkt.domain.SysFile;
import com.ruoyi.xkt.dto.storeCertificate.StoreCertDTO;
import com.ruoyi.xkt.dto.storeCertificate.StoreCertResDTO;
import com.ruoyi.xkt.enums.FileType;
import com.ruoyi.xkt.enums.StoreStatus;
import com.ruoyi.xkt.mapper.*;
import com.ruoyi.xkt.service.IAssetService;
import com.ruoyi.xkt.service.IStoreCertificateService;
import com.ruoyi.xkt.service.IStoreService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * 新增档口认证
     *
     * @param certDTO 档口认证入参
     * @return int
     */
    @Override
    @Transactional
    public Integer create(StoreCertDTO certDTO) {
        StoreCertificate storeCert = BeanUtil.toBean(certDTO, StoreCertificate.class);
        // 新增档口认证的文件列表
        this.handleStoreCertFileList(certDTO, storeCert);
        // 新增档口
        this.create();
        return this.storeCertMapper.insert(storeCert);
    }

    public int create() {
        Store store = new Store();
        // 初始化注册时只需绑定用户ID即可
        store.setUserId(SecurityUtils.getUserId());
        // 默认档口状态为：待审核
        store.setStoreStatus(StoreStatus.UN_AUDITED.getValue());
        // 当前时间往后推1年为试用期时间
        Date oneYearAfter = Date.from(LocalDate.now().plusYears(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        store.setTrialEndTime(oneYearAfter);
        // 设置档口默认权重 0
        store.setStoreWeight(Constants.STORE_WEIGHT_DEFAULT_ZERO);
        int count = this.storeMapper.insert(store);
        // 创建档口账户
        assetService.createInternalAccountIfNotExists(store.getId());
        // 档口用户绑定
        userService.refreshRelStore(store.getUserId(), ESystemRole.SUPPLIER.getId());
        // 放到redis中
        redisCache.setCacheObject(CacheConstants.STORE_KEY + store.getId(), store.getId());
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
                .setFileType(Objects.equals(x.getId(), storeCert.getIdCardFaceFileId()) ? 4 :
                        (Objects.equals(x.getId(), storeCert.getIdCardEmblemFileId()) ? 5 : 6))).collect(Collectors.toList());
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
        // 档口认证ID不能为空
        Optional.ofNullable(certDTO.getStoreCertId()).orElseThrow(() -> new ServiceException("档口认证ID不能为空!", HttpStatus.ERROR));
        StoreCertificate storeCert = Optional.ofNullable(this.storeCertMapper.selectOne(new LambdaQueryWrapper<StoreCertificate>()
                        .eq(StoreCertificate::getId, certDTO.getStoreCertId()).eq(StoreCertificate::getDelFlag, Constants.UNDELETED)
                        .eq(StoreCertificate::getStoreId, certDTO.getStoreId())))
                .orElseThrow(() -> new ServiceException("档口认证不存在!", HttpStatus.ERROR));
        // 先将旧的档口认证相关文件置为无效
        List<SysFile> oldFileList = Optional.ofNullable(this.fileMapper.selectList(new LambdaQueryWrapper<SysFile>()
                        .eq(SysFile::getDelFlag, Constants.UNDELETED).in(SysFile::getId,
                                Arrays.asList(storeCert.getIdCardFaceFileId(), storeCert.getIdCardEmblemFileId(), storeCert.getLicenseFileId()))))
                .orElseThrow(() -> new ServiceException("档口认证相关文件不存在!", HttpStatus.ERROR));
        this.fileMapper.updateById(oldFileList.stream().peek(x -> x.setDelFlag(Constants.DELETED)).collect(Collectors.toList()));
        // 更新属性
        BeanUtil.copyProperties(certDTO, storeCert);
        // 新增档口认证的文件列表
        this.handleStoreCertFileList(certDTO, storeCert);
        return this.storeCertMapper.updateById(storeCert);
    }

    /**
     * 新增档口认证文件列表
     *
     * @param certDTO   档口认证文件入参
     * @param storeCert 档口认证对象
     */
    private void handleStoreCertFileList(StoreCertDTO certDTO, StoreCertificate storeCert) {
        // 档口认证文件类型与文件名映射
        Map<Integer, String> typeNameTransMap = certDTO.getFileList().stream().collect(Collectors
                .toMap(StoreCertDTO.StoreCertFileDTO::getFileType, StoreCertDTO.StoreCertFileDTO::getFileName));
        // 上传的文件列表
        final List<StoreCertDTO.StoreCertFileDTO> fileDTOList = certDTO.getFileList();
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


}
