package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.xkt.domain.StoreSale;
import com.ruoyi.xkt.domain.StoreSaleRefundRecord;
import com.ruoyi.xkt.dto.storeSaleRefundRecord.StoreSaleRefundRecordDTO;
import com.ruoyi.xkt.mapper.StoreSaleMapper;
import com.ruoyi.xkt.mapper.StoreSaleRefundRecordMapper;
import com.ruoyi.xkt.service.IStoreSaleRefundRecordService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 档口销售返单Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
@RequiredArgsConstructor
public class StoreSaleRefundRecordServiceImpl implements IStoreSaleRefundRecordService {

    final StoreSaleRefundRecordMapper refundRecordMapper;
    final StoreSaleMapper storeSaleMapper;

    /**
     * 查询档口销售返单列表
     *
     * @param storeId     档口ID
     * @param storeSaleId 档口销售ID
     * @return 档口销售返单集合
     */
    @Override
    @Transactional(readOnly = true)
    public List<StoreSaleRefundRecordDTO> selectList(Long storeId, Long storeSaleId) {
        List<StoreSaleRefundRecord> refundRecordList = this.refundRecordMapper.selectList(new LambdaQueryWrapper<StoreSaleRefundRecord>()
                .eq(StoreSaleRefundRecord::getStoreId, storeId).eq(StoreSaleRefundRecord::getStoreSaleId, storeSaleId).eq(StoreSaleRefundRecord::getDelFlag, Constants.UNDELETED));
        if (CollectionUtils.isEmpty(refundRecordList)) {
            return new ArrayList<>();
        }
        List<StoreSaleRefundRecordDTO> refundRecordDTOList = refundRecordList.stream().map(x -> BeanUtil.toBean(x, StoreSaleRefundRecordDTO.class)
                .setStoreSaleRefundRecordId(x.getId())).collect(Collectors.toList());
        // 获取当前最新数据
        StoreSale storeSale = Optional.ofNullable(storeSaleMapper.selectById(storeSaleId))
                .orElseThrow(() -> new ServiceException("档口销售出库订单不存在!", HttpStatus.ERROR));
        refundRecordDTOList.add(BeanUtil.toBean(storeSale, StoreSaleRefundRecordDTO.class).setStoreSaleRefundRecordId(-1L));
        // 将最新数据放在第一位
        Collections.reverse(refundRecordDTOList);
        return refundRecordDTOList;
    }

}
