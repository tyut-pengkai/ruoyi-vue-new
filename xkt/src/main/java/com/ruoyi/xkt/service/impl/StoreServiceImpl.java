package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.xkt.domain.Store;
import com.ruoyi.xkt.dto.store.StoreCreateDTO;
import com.ruoyi.xkt.dto.store.StoreUpdateDTO;
import com.ruoyi.xkt.enums.StoreStatus;
import com.ruoyi.xkt.mapper.StoreMapper;
import com.ruoyi.xkt.service.IStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

/**
 * 档口Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements IStoreService {

    final StoreMapper storeMapper;

    /**
     * 注册时新增档口数据
     *
     * @param createDTO 档口基础数据
     * @return 结果
     */
    @Override
    @Transactional
    public int create(StoreCreateDTO createDTO) {
        Store store = new Store();
        // 初始化注册时只需绑定用户ID即可
        store.setUserId(createDTO.getUserId());
        // 默认档口状态为：已注册
        store.setStoreStatus(StoreStatus.REGISTERED.getValue());
        // 当前时间往后推1年为试用期时间
        Date oneYearAfter = Date.from(LocalDate.now().plusYears(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        store.setTrialEndTime(oneYearAfter);
        return this.storeMapper.insert(store);
    }

    /**
     * 修改档口基本信息
     *
     * @param storeUpdateDTO 档口
     * @return 结果
     */
    @Override
    @Transactional
    public int updateStore(StoreUpdateDTO storeUpdateDTO) {
        Store store = Optional.ofNullable(this.storeMapper.selectOne(new LambdaQueryWrapper<Store>()
                        .eq(Store::getId, storeUpdateDTO.getStoreId()).eq(Store::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("档口不存在!", HttpStatus.ERROR));
        BeanUtil.copyProperties(storeUpdateDTO, store);
        // 如果当前状态为认证营业执照
        if (Objects.equals(store.getStoreStatus(), StoreStatus.AUTH_LICENSE.getValue())) {
            // 将档口状态更改为：认证基本信息
            store.setStoreStatus(StoreStatus.AUTH_BASE_INFO.getValue());
        }
        return storeMapper.updateById(store);
    }

}
