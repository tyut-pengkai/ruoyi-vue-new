package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.enums.AdType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.xkt.domain.Advert;
import com.ruoyi.xkt.domain.SysFile;
import com.ruoyi.xkt.dto.advert.*;
import com.ruoyi.xkt.enums.AdDisplayType;
import com.ruoyi.xkt.enums.AdOnlineStatus;
import com.ruoyi.xkt.enums.AdPlatformType;
import com.ruoyi.xkt.enums.AdTab;
import com.ruoyi.xkt.mapper.AdvertMapper;
import com.ruoyi.xkt.mapper.SysFileMapper;
import com.ruoyi.xkt.service.IAdvertService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 推广营销Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
@RequiredArgsConstructor
public class AdvertServiceImpl implements IAdvertService {

    final AdvertMapper advertMapper;
    final SysFileMapper fileMapper;

    /**
     * 新增推广营销
     *
     * @param createDTO 新增推广营销入参
     * @return Integer
     */
    @Override
    @Transactional
    public Integer create(AdvertCreateDTO createDTO) {
        Advert advert = BeanUtil.toBean(createDTO, Advert.class);
        advert.setBasicSymbol(random10Str());
        advert.setOnlineStatus(AdOnlineStatus.ONLINE.getValue());
        // 推广类型为 推广图 或者 图及商品 则新增系统文件
        if ((Objects.equals(createDTO.getDisplayType(), AdDisplayType.PICTURE.getValue()) || Objects.equals(createDTO.getDisplayType(), AdDisplayType.PIC_AND_PROD.getValue()))
                && ObjectUtils.isNotEmpty(createDTO.getExample())) {
            // 将文件插入到SysFile表中
            SysFile file = BeanUtil.toBean(createDTO.getExample(), SysFile.class);
            this.fileMapper.insert(file);
            advert.setExamplePicId(file.getId());
        }
        return this.advertMapper.insert(advert);
    }


    /**
     * 获取推广营销详情
     *
     * @param advertId 推广营销ID
     * @return AdvertResDTO
     */
    @Override
    @Transactional(readOnly = true)
    public AdvertResDTO getInfo(Long advertId) {
        Advert advert = Optional.ofNullable(this.advertMapper.selectOne(new LambdaQueryWrapper<Advert>()
                        .eq(Advert::getId, advertId).eq(Advert::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("推广营销不存在!", HttpStatus.ERROR));
        AdvertResDTO resDTO = BeanUtil.toBean(advert, AdvertResDTO.class);
        // 找到范例图片
        if (ObjectUtils.isNotEmpty(advert.getExamplePicId())) {
            SysFile file = Optional.ofNullable(this.fileMapper.selectById(advert.getExamplePicId()))
                    .orElseThrow(() -> new ServiceException("推广营销范例图片不存在!", HttpStatus.ERROR));
            resDTO.setExample(BeanUtil.toBean(file, AdvertResDTO.AdvertFileDTO.class).setTypeId(advert.getTypeId()));
        }
        return resDTO;
    }

    /**
     * 推广营销分页
     *
     * @param pageDTO 分页查询入参
     * @return Page<AdvertResDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AdvertResDTO> page(AdvertPageDTO pageDTO) {
        LambdaQueryWrapper<Advert> queryWrapper = new LambdaQueryWrapper<Advert>()
                .eq(Advert::getDelFlag, Constants.UNDELETED).orderByDesc(Advert::getCreateTime);
        if (ObjectUtils.isNotEmpty(pageDTO.getOnlineStatus())) {
            queryWrapper.eq(Advert::getOnlineStatus, pageDTO.getOnlineStatus());
        }
        if (ObjectUtils.isNotEmpty(pageDTO.getPlatformId())) {
            queryWrapper.eq(Advert::getPlatformId, pageDTO.getPlatformId());
        }
        if (ObjectUtils.isNotEmpty(pageDTO.getTypeId())) {
            queryWrapper.eq(Advert::getTypeId, pageDTO.getTypeId());
        }
        PageHelper.startPage(pageDTO.getPageNum(), pageDTO.getPageSize());
        List<Advert> advertList = this.advertMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(advertList)) {
            return Page.empty(pageDTO.getPageSize(), pageDTO.getPageNum());
        }
        List<Long> fileIdList = advertList.stream().map(Advert::getExamplePicId).filter(ObjectUtils::isNotEmpty).collect(Collectors.toList());
        Map<Long, SysFile> fileMap = CollectionUtils.isEmpty(fileIdList) ? new HashMap<>()
                : this.fileMapper.selectByIds(fileIdList).stream().collect(Collectors.toMap(SysFile::getId, Function.identity()));
        List<AdvertResDTO> advertResDTOList = advertList.stream().map(x -> {
            AdvertResDTO advertResDTO = BeanUtil.toBean(x, AdvertResDTO.class);
            if (ObjectUtils.isNotEmpty(x.getExamplePicId())) {
                advertResDTO.setExample(BeanUtil.toBean(fileMap.get(x.getExamplePicId()), AdvertResDTO.AdvertFileDTO.class));
            }
            return advertResDTO;
        }).collect(Collectors.toList());
        return Page.convert(new PageInfo<>(advertList), advertResDTOList);
    }

    /**
     * 更新推广营销
     *
     * @param updateDTO 更新推广营销入参
     * @return Integer
     */
    @Override
    @Transactional
    public Integer updateAdvert(AdvertUpdateDTO updateDTO) {
        Advert advert = Optional.ofNullable(this.advertMapper.selectOne(new LambdaQueryWrapper<Advert>()
                        .eq(Advert::getId, updateDTO.getAdvertId()).eq(Advert::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("推广营销不存在!", HttpStatus.ERROR));
        BeanUtil.copyProperties(updateDTO, advert);
        if (ObjectUtils.isNotEmpty(updateDTO.getExample())) {
            // 将文件插入到SysFile表中
            SysFile file = BeanUtil.toBean(updateDTO.getExample(), SysFile.class);
            this.fileMapper.insert(file);
            advert.setExamplePicId(file.getId());
        }
        return this.advertMapper.updateById(advert);
    }

    /**
     * 下线推广营销
     *
     * @param changeStatusDTO 推广营销入参
     * @return Integer
     */
    @Override
    @Transactional
    public Integer changeAdvertStatus(AdvertChangeStatusDTO changeStatusDTO) {
        // 判断状态是否合法
        AdOnlineStatus.of(changeStatusDTO.getStatus());
        Advert advert = Optional.ofNullable(this.advertMapper.selectOne(new LambdaQueryWrapper<Advert>()
                        .eq(Advert::getId, changeStatusDTO.getAdvertId()).eq(Advert::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("推广营销不存在!", HttpStatus.ERROR));
        advert.setOnlineStatus(changeStatusDTO.getStatus());
        return this.advertMapper.updateById(advert);
    }

    /**
     * 获取初始化平台列表
     *
     * @return List<AdvertGroupResDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public List<AdvertPlatformResDTO> getPlatformList() {
        List<AdvertPlatTabDTO> list = this.advertMapper.selectPlatTabList();
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        // 所有的推广平台
        List<AdvertPlatformResDTO> platformList = new ArrayList<>();
        list.stream().collect(Collectors
                        .groupingBy(AdvertPlatTabDTO::getPlatformId, Collectors
                                .groupingBy(AdvertPlatTabDTO::getTabId)))
                .forEach((platformId, tabMap) -> {
                    // 平台下所有的tab
                    List<AdvertPlatformResDTO.APTabDTO> tabList = new ArrayList<>();
                    tabMap.forEach((tabId, typeList) -> tabList.add(new AdvertPlatformResDTO.APTabDTO()
                            .setTabId(tabId).setTabName(AdTab.of(tabId).getLabel())
                            // tab下所有的推广类型
                            .setTypeList(typeList.stream().map(type -> new AdvertPlatformResDTO.APTypeDTO()
                                                    .setAdvertId(type.getAdvertId()).setTypeId(type.getTypeId()).setTypeName(AdType.of(type.getTypeId()).getLabel())
//                                    .setDemoUrl(AdType.of(type.getTypeId()).getDemoUrl())
                                    )
                                    .collect(Collectors.toList()))));
                    platformList.add(new AdvertPlatformResDTO().setPlatformId(platformId)
                            .setPlatformName(AdPlatformType.of(platformId).getLabel())
                            .setTabList(tabList));
                });
        return platformList;
    }

    /**
     * 查看当前推广类型 示例图
     *
     * @param advertType 推广类型
     * @return String
     */
    @Override
    @Transactional(readOnly = true)
    public String getDemoPic(Integer advertType) {
        Advert advert = Optional.ofNullable(this.advertMapper.selectOne(new LambdaQueryWrapper<Advert>()
                        .eq(Advert::getTypeId, advertType).eq(Advert::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("推广类型不存在!", HttpStatus.ERROR));
        SysFile file = Optional.ofNullable(this.fileMapper.selectById(advert.getExamplePicId()))
                .orElseThrow(() -> new ServiceException("示例图不存在!", HttpStatus.ERROR));
        return file.getFileUrl();
    }

    /**
     * 随机生成10位，包含大小写字母、数字的字符串
     *
     * @return
     */
    public static String random10Str() {
        final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "abcdefghijklmnopqrstuvwxyz"
                + "0123456789";
        final int STRING_LENGTH = 10;
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(STRING_LENGTH);
        for (int i = 0; i < STRING_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }


}
