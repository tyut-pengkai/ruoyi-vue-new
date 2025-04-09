package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.xkt.domain.Store;
import com.ruoyi.xkt.domain.StoreHomepage;
import com.ruoyi.xkt.domain.StoreProduct;
import com.ruoyi.xkt.domain.SysFile;
import com.ruoyi.xkt.dto.storeHomepage.StoreHomeDecorationDTO;
import com.ruoyi.xkt.dto.storeHomepage.StoreHomeDecorationResDTO;
import com.ruoyi.xkt.enums.HomepageJumpType;
import com.ruoyi.xkt.enums.HomepageType;
import com.ruoyi.xkt.mapper.StoreHomepageMapper;
import com.ruoyi.xkt.mapper.StoreMapper;
import com.ruoyi.xkt.mapper.StoreProductMapper;
import com.ruoyi.xkt.mapper.SysFileMapper;
import com.ruoyi.xkt.service.IStoreHomepageService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 档口首页Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@RequiredArgsConstructor
@Service
public class StoreHomepageServiceImpl implements IStoreHomepageService {

    final SysFileMapper fileMapper;
    final StoreHomepageMapper storeHomeMapper;
    final StoreMapper storeMapper;
    final StoreProductMapper storeProdMapper;


    /**
     * 新增档口首页各部分图
     *
     * @param storeId     档口ID
     * @param templateNum 使用的模板No
     * @param homepageDTO 新增档口首页各部分图
     * @return Integer
     */
    @Override
    @Transactional
    public Integer insert(Long storeId, Integer templateNum, StoreHomeDecorationDTO homepageDTO) {
        List<StoreHomepage> homepageList = this.insertToHomepage(storeId, homepageDTO);
        // 当前档口首页各部分总的文件大小
        BigDecimal totalSize = homepageDTO.getBigBannerList().stream().map(x -> ObjectUtils.defaultIfNull(x.getFileSize(), BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add);
        Store store = Optional.ofNullable(this.storeMapper.selectOne(new LambdaQueryWrapper<Store>().eq(Store::getId, storeId).eq(Store::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("档口不存在!", HttpStatus.ERROR));
        store.setTemplateNum(templateNum);
        // 更新档口首页使用的总的文件容量
        store.setStorageUsage(ObjectUtils.defaultIfNull(store.getStorageUsage(), BigDecimal.ZERO).add(totalSize));
        this.storeMapper.updateById(store);
        return homepageList.size();
    }


    /**
     * 获取档口首页各个部分的图信息
     *
     * @param storeId 档口ID
     * @return StoreHomeDTO
     */
    @Override
    @Transactional(readOnly = true)
    public StoreHomeDecorationResDTO selectByStoreId(Long storeId) {
        Store store = Optional.ofNullable(this.storeMapper.selectOne(new LambdaQueryWrapper<Store>()
                        .eq(Store::getId, storeId).eq(Store::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("档口不存在!", HttpStatus.ERROR));
        List<StoreHomepage> homeList = this.storeHomeMapper.selectList(new LambdaQueryWrapper<StoreHomepage>()
                .eq(StoreHomepage::getStoreId, storeId).eq(StoreHomepage::getDelFlag, Constants.UNDELETED));
        List<SysFile> fileList = Optional.ofNullable(this.fileMapper.selectList(new LambdaQueryWrapper<SysFile>()
                        .in(SysFile::getId, homeList.stream().map(StoreHomepage::getFileId).filter(ObjectUtils::isNotEmpty).collect(Collectors.toList()))
                        .eq(SysFile::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("文件不存在", HttpStatus.ERROR));
        Map<Long, SysFile> fileMap = fileList.stream().collect(Collectors.toMap(SysFile::getId, Function.identity()));
        // 所有的档口商品ID
        List<StoreProduct> storeProdList = Optional.ofNullable(this.storeProdMapper.selectList(new LambdaQueryWrapper<StoreProduct>()
                        .eq(StoreProduct::getStoreId, storeId).in(StoreProduct::getId, homeList.stream()
                                .filter(x -> Objects.equals(x.getJumpType(), HomepageJumpType.JUMP_PRODUCT.getValue())).map(StoreHomepage::getBizId).collect(Collectors.toList()))
                        .eq(StoreProduct::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("档口商品不存在", HttpStatus.ERROR));
        Map<Long, StoreProduct> storeProdMap = storeProdList.stream().collect(Collectors.toMap(StoreProduct::getId, Function.identity()));
        // 轮播图
        List<StoreHomeDecorationResDTO.DecorationDTO> bigBannerList = homeList.stream().filter(x -> Objects.equals(x.getFileType(), HomepageType.SLIDING_PICTURE.getValue()))
                .map(x -> {
                    StoreHomeDecorationResDTO.DecorationDTO decorationDTO = BeanUtil.toBean(x, StoreHomeDecorationResDTO.DecorationDTO.class)
                            .setBizName((Objects.equals(x.getJumpType(), HomepageJumpType.JUMP_PRODUCT.getValue()))
                                    ? (storeProdMap.containsKey(x.getBizId()) ? storeProdMap.get(x.getBizId()).getProdName() : null)
                                    : (ObjectUtils.isEmpty(x.getJumpType()) ? null : store.getStoreName()));
                    if (fileMap.containsKey(x.getFileId())) {
                        decorationDTO.setFileType(x.getFileType()).setFileName(fileMap.get(x.getFileId()).getFileName())
                                .setFileUrl(fileMap.get(x.getFileId()).getFileUrl()).setFileSize(fileMap.get(x.getFileId()).getFileSize());
                    }
                    return decorationDTO;
                }).collect(Collectors.toList());
        // 其它图部分
        List<StoreHomeDecorationResDTO.DecorationDTO> decorList = homeList.stream().filter(x -> !Objects.equals(x.getFileType(), HomepageType.SLIDING_PICTURE.getValue()))
                .map(x -> {
                    StoreHomeDecorationResDTO.DecorationDTO decorationDTO = BeanUtil.toBean(x, StoreHomeDecorationResDTO.DecorationDTO.class)
                            .setBizName(storeProdMap.containsKey(x.getBizId()) ? storeProdMap.get(x.getBizId()).getProdName() : null);
                    if (fileMap.containsKey(x.getFileId())) {
                        decorationDTO.setFileType(x.getFileType()).setFileName(fileMap.get(x.getFileId()).getFileName())
                                .setFileUrl(fileMap.get(x.getFileId()).getFileUrl()).setFileSize(fileMap.get(x.getFileId()).getFileSize());
                    }
                    return decorationDTO;
                })
                .collect(Collectors.toList());
        return new StoreHomeDecorationResDTO() {{
            setTemplateNum(store.getTemplateNum());
            setBigBannerList(bigBannerList);
            setDecorList(decorList);
        }};
    }

    /**
     * 更新档口首页各部分图信息
     *
     * @param storeId     档口ID
     * @param templateNum 选择的模板Num
     * @param homepageDTO 更新的dto
     * @return Integer
     */
    @Override
    @Transactional
    public Integer updateStoreHomepage(Long storeId, Integer templateNum, StoreHomeDecorationDTO homepageDTO) {
        // 先将所有的档口模板的文件都删除掉
        List<StoreHomepage> oldHomeList = this.storeHomeMapper.selectList(new LambdaQueryWrapper<StoreHomepage>()
                .eq(StoreHomepage::getStoreId, storeId).eq(StoreHomepage::getDelFlag, Constants.UNDELETED));
        if (CollectionUtils.isNotEmpty(oldHomeList)) {
            oldHomeList.forEach(x -> x.setDelFlag(Constants.DELETED));
            this.storeHomeMapper.updateById(oldHomeList);
        }
        // 新增档口首页各个部分的图信息
        List<StoreHomepage> homepageList = this.insertToHomepage(storeId, homepageDTO);
        Store store = Optional.ofNullable(this.storeMapper.selectOne(new LambdaQueryWrapper<Store>()
                        .eq(Store::getId, storeId).eq(Store::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("档口不存在!", HttpStatus.ERROR));
        store.setTemplateNum(templateNum);
        this.storeMapper.updateById(store);
        return homepageList.size();
    }


    /**
     * 新增档口首页模板展示
     *
     * @param storeId     档口ID
     * @param homepageDTO 新增档口首页入参
     * @return
     */
    private List<StoreHomepage> insertToHomepage(Long storeId, StoreHomeDecorationDTO homepageDTO) {
        // 新增的首页轮播大图部分
        List<SysFile> bigBannerFileList = homepageDTO.getBigBannerList().stream().filter(x -> StringUtils.isNotBlank(x.getFileUrl())
                        && StringUtils.isNotBlank(x.getFileName()) && ObjectUtils.isNotEmpty(x.getFileSize()) && ObjectUtils.isNotEmpty(x.getOrderNum()))
                .map(x -> BeanUtil.toBean(x, SysFile.class)).collect(Collectors.toList());
        List<StoreHomepage> homePageList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(bigBannerFileList)) {
            this.fileMapper.insert(bigBannerFileList);
            Map<String, SysFile> bigBannerMap = bigBannerFileList.stream().collect(Collectors.toMap(SysFile::getFileName, Function.identity()));
            homePageList.addAll(homepageDTO.getBigBannerList().stream().map(x -> BeanUtil.toBean(x, StoreHomepage.class).setStoreId(storeId)
                            .setFileId(bigBannerMap.containsKey(x.getFileName()) ? bigBannerMap.get(x.getFileName()).getId() : null))
                    .collect(Collectors.toList()));
        }
        if (CollectionUtils.isNotEmpty(homepageDTO.getDecorList())) {
            homePageList.addAll(homepageDTO.getDecorList().stream().map(x -> BeanUtil.toBean(x, StoreHomepage.class).setStoreId(storeId))
                    .collect(Collectors.toList()));
        }
        if (CollectionUtils.isNotEmpty(homePageList)) {
            this.storeHomeMapper.insert(homePageList);
        }
        return homePageList;
    }


}
