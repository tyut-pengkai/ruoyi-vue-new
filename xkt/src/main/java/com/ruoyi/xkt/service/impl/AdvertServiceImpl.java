package com.ruoyi.xkt.service.impl;

import java.security.SecureRandom;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.xkt.domain.Advert;
import com.ruoyi.xkt.domain.SysFile;
import com.ruoyi.xkt.dto.advert.*;
import com.ruoyi.xkt.enums.AdOnlineStatus;
import com.ruoyi.xkt.enums.AdPlatformType;
import com.ruoyi.xkt.enums.AdTab;
import com.ruoyi.common.enums.AdType;
import com.ruoyi.xkt.mapper.AdvertMapper;
import com.ruoyi.xkt.mapper.SysFileMapper;
import com.ruoyi.xkt.service.IAdvertService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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

        // TODO 判断当前是否超级管理员在操作
        // TODO 判断当前是否超级管理员在操作
        // TODO 判断当前是否超级管理员在操作
        this.isSuperAdmin();

        // 将文件插入到SysFile表中
        SysFile file = BeanUtil.toBean(createDTO.getExample(), SysFile.class);
        this.fileMapper.insert(file);
        Advert advert = BeanUtil.toBean(createDTO, Advert.class);
        advert.setBasicSymbol(random10Str());
        advert.setOnlineStatus(AdOnlineStatus.ONLINE.getValue());
        advert.setExamplePicId(file.getId());
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

        // TODO 判断当前是否超级管理员在操作
        // TODO 判断当前是否超级管理员在操作
        // TODO 判断当前是否超级管理员在操作
        this.isSuperAdmin();

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
        LambdaQueryWrapper<Advert> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Advert::getDelFlag, Constants.UNDELETED);
        queryWrapper.eq(Advert::getOnlineStatus, pageDTO.getOnlineStatus());
        if (ObjectUtils.isNotEmpty(pageDTO.getPlatformId())) {
            queryWrapper.eq(Advert::getPlatformId, pageDTO.getPlatformId());
        }
        if (ObjectUtils.isNotEmpty(pageDTO.getTypeId())) {
            queryWrapper.eq(Advert::getTypeId, pageDTO.getTypeId());
        }
        PageHelper.startPage(pageDTO.getPageNum(), pageDTO.getPageSize());
        List<Advert> advertList = this.advertMapper.selectList(queryWrapper);
       return CollectionUtils.isEmpty(advertList) ? Page.empty(pageDTO.getPageSize(), pageDTO.getPageNum())
               : Page.convert(new PageInfo<>(advertList), BeanUtil.copyToList(advertList, AdvertResDTO.class));
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

        // TODO 判断当前是否超级管理员在操作
        // TODO 判断当前是否超级管理员在操作
        // TODO 判断当前是否超级管理员在操作
        this.isSuperAdmin();

        // 将文件插入到SysFile表中
        SysFile file = BeanUtil.toBean(updateDTO.getExample(), SysFile.class);
        this.fileMapper.insert(file);
        Advert advert = Optional.ofNullable(this.advertMapper.selectOne(new LambdaQueryWrapper<Advert>()
                        .eq(Advert::getId, updateDTO.getAdvertId()).eq(Advert::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("推广营销不存在!", HttpStatus.ERROR));
        BeanUtil.copyProperties(updateDTO, advert);
        advert.setExamplePicId(file.getId());
        return this.advertMapper.updateById(advert);
    }

    /**
     * 下线推广营销
     *
     * @param advertId 推广营销ID
     * @return Integer
     */
    @Override
    @Transactional
    public Integer offline(Long advertId) {


        // TODO 判断当前是否超级管理员在操作
        // TODO 判断当前是否超级管理员在操作
        // TODO 判断当前是否超级管理员在操作
        this.isSuperAdmin();

        Advert advert = Optional.ofNullable(this.advertMapper.selectOne(new LambdaQueryWrapper<Advert>()
                .eq(Advert::getId, advertId).eq(Advert::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("推广营销不存在!", HttpStatus.ERROR));
        if (Objects.equals(advert.getOnlineStatus(), AdOnlineStatus.OFFLINE.getValue())) {
            throw new ServiceException("推广营销已下线，不可再次操作!", HttpStatus.ERROR);
        }
        advert.setOnlineStatus(AdOnlineStatus.OFFLINE.getValue());
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
                                    .setDemoUrl(AdType.of(type.getTypeId()).getDemoUrl())).collect(Collectors.toList()))));
                    platformList.add(new AdvertPlatformResDTO().setPlatformId(platformId)
                            .setPlatformName(AdPlatformType.of(platformId).getLabel())
                            .setTabList(tabList));
                });
        return platformList;
    }

    /**
     * 校验当前是否是超级管理员操作
     */
    private void isSuperAdmin() {
        // 获取当前登录用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (ObjectUtils.isEmpty(loginUser)) {
            throw new ServiceException("当前用户不存在!", HttpStatus.ERROR);
        }
        if (!SecurityUtils.isAdmin(loginUser.getUserId())) {
            throw new ServiceException("当前用户不是超级管理员，不可操作!", HttpStatus.ERROR);
        }
    }


    /**
     * 随机生成10位，包含大小写字母、数字的字符串
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
