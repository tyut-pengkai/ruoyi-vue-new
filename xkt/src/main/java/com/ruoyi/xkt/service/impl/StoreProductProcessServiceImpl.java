package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.xkt.domain.StoreProductProcess;
import com.ruoyi.xkt.dto.storeProdProcess.StoreProdProcessDTO;
import com.ruoyi.xkt.dto.storeProdProcess.StoreProdProcessUpdateDTO;
import com.ruoyi.xkt.mapper.StoreProductProcessMapper;
import com.ruoyi.xkt.service.IStoreProductProcessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 档口商品工艺信息Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StoreProductProcessServiceImpl implements IStoreProductProcessService {

    final StoreProductProcessMapper prodProcessMapper;

    /**
     * 获取档口商品工艺信息
     *
     * @param storeProdId 档口商品ID
     * @return 档口商品工艺信息
     */
    @Override
    @Transactional(readOnly = true)
    public StoreProdProcessDTO getProcess(Long storeProdId) {
        StoreProductProcess process = Optional.ofNullable(this.prodProcessMapper.selectOne(new LambdaQueryWrapper<StoreProductProcess>()
                        .eq(StoreProductProcess::getStoreProdId, storeProdId).eq(StoreProductProcess::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("档口商品工艺不存在!", HttpStatus.ERROR));
        return BeanUtil.toBean(process, StoreProdProcessDTO.class);
    }

    /**
     * 更新档口工艺 信息
     *
     * @param storeProdId 档口商品ID
     * @param updateDTO   更新入参
     * @return Integer
     */
    @Override
    @Transactional
    public Integer update(Long storeProdId, StoreProdProcessUpdateDTO updateDTO) {
        StoreProductProcess process = Optional.ofNullable(this.prodProcessMapper.selectOne(new LambdaQueryWrapper<StoreProductProcess>()
                        .eq(StoreProductProcess::getStoreProdId, storeProdId).eq(StoreProductProcess::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("档口商品工艺不存在!", HttpStatus.ERROR));
        BeanUtil.copyProperties(updateDTO, process);
        return this.prodProcessMapper.updateById(process);
    }


}
