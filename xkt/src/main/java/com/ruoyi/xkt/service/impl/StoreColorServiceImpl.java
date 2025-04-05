package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.xkt.domain.StoreColor;
import com.ruoyi.xkt.dto.storeColor.StoreColorDTO;
import com.ruoyi.xkt.mapper.StoreColorMapper;
import com.ruoyi.xkt.service.IStoreColorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 档口所有颜色Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
@RequiredArgsConstructor
public class StoreColorServiceImpl implements IStoreColorService {

    final StoreColorMapper storeColorMapper;

    /**
     * 查询档口所有颜色列表
     *
     * @param storeId 档口ID
     * @return List<StoreColorDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public List<StoreColorDTO> list(Long storeId) {
        List<StoreColor> storeColorList = Optional.ofNullable(this.storeColorMapper.selectList(new LambdaQueryWrapper<StoreColor>()
                        .eq(StoreColor::getStoreId, storeId).eq(StoreColor::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("当前档口没有颜色!", HttpStatus.ERROR));
        return storeColorList.stream().map(x -> BeanUtil.toBean(x, StoreColorDTO.class).setStoreColorId(x.getId()))
                .sorted(Comparator.comparing(StoreColorDTO::getOrderNum)).collect(Collectors.toList());
    }


}
