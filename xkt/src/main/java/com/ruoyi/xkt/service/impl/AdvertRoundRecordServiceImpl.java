package com.ruoyi.xkt.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.enums.AdType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.xkt.dto.advertRoundRecord.AdvertRoundRecordPageDTO;
import com.ruoyi.xkt.dto.advertRoundRecord.AdvertRoundRecordPageResDTO;
import com.ruoyi.xkt.enums.*;
import com.ruoyi.xkt.mapper.AdvertRoundRecordMapper;
import com.ruoyi.xkt.service.IAdvertRoundRecordService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 推广营销轮次播放Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
@RequiredArgsConstructor
public class AdvertRoundRecordServiceImpl implements IAdvertRoundRecordService {

    final AdvertRoundRecordMapper advertRoundRecordMapper;

    /**
     * 获取竞价失败列表
     *
     * @param pageDTO 竞价失败查询列表
     * @return Page<AdvertRoundRecordPageResDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AdvertRoundRecordPageResDTO> page(AdvertRoundRecordPageDTO pageDTO) {
        // 用户是否为档口管理者或子账户
        if (!SecurityUtils.isStoreManagerOrSub(pageDTO.getStoreId())) {
            throw new ServiceException("当前用户非档口管理者或子账号，无权限操作!", HttpStatus.ERROR);
        }
        PageHelper.startPage(pageDTO.getPageNum(), pageDTO.getPageSize());
        List<AdvertRoundRecordPageResDTO> list = this.advertRoundRecordMapper.selectRecordPage(pageDTO);
        list.forEach(item -> item.setPlatformName(AdPlatformType.of(item.getPlatformId()).getLabel())
                .setLaunchStatusName(AdLaunchStatus.of(item.getLaunchStatus()).getLabel())
                .setPicAuditStatusName(ObjectUtils.isNotEmpty(item.getPicAuditStatus()) ? AdPicAuditStatus.of(item.getPicAuditStatus()).getLabel() : "")
                .setPicDesignTypeName(ObjectUtils.isNotEmpty(item.getPicDesignType()) ? AdDesignType.of(item.getPicDesignType()).getLabel() : "")
                .setPicAuditStatusName(ObjectUtils.isNotEmpty(item.getPicAuditStatus()) ? AdPicAuditStatus.of(item.getPicAuditStatus()).getLabel() : "")
                .setPicSetTypeName(ObjectUtils.isNotEmpty(item.getPicSetType()) ? AdPicSetType.of(item.getPicSetType()).getLabel() : "")
                .setTypeName(AdType.of(item.getTypeId()).getLabel())
                .setBiddingStatusName(AdBiddingStatus.of(item.getBiddingStatus()).getLabel()));
        return Page.convert(new PageInfo<>(list));
    }


}
