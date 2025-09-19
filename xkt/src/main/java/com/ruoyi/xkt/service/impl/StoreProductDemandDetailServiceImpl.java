package com.ruoyi.xkt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.xkt.domain.StoreProductDemandDetail;
import com.ruoyi.xkt.dto.storeProductDemandDetail.StoreProdDemandDetailUpdateDTO;
import com.ruoyi.xkt.mapper.StoreProductDemandDetailMapper;
import com.ruoyi.xkt.service.IStoreProductDemandDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 档口商品需求单明细Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
@RequiredArgsConstructor
public class StoreProductDemandDetailServiceImpl implements IStoreProductDemandDetailService {

    final StoreProductDemandDetailMapper demandDetailMapper;

    /**
     * 更新商品需求单明细数量
     *
     * @param updateDTO 更新入参
     * @return 更新数量
     */
    @Override
    @Transactional
    public Integer updateDetailQuantity(StoreProdDemandDetailUpdateDTO updateDTO) {
        StoreProductDemandDetail demandDetail = Optional.ofNullable(this.demandDetailMapper.selectOne(new LambdaQueryWrapper<StoreProductDemandDetail>()
                        .eq(StoreProductDemandDetail::getId, updateDTO.getStoreProdDemandDetailId()).eq(StoreProductDemandDetail::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("商品需求单明细不存在!", HttpStatus.ERROR));
        // 如果数量为0，则设置为null
        demandDetail.setSize30(this.convertZeroToNull(updateDTO.getSize30()));
        demandDetail.setSize31(this.convertZeroToNull(updateDTO.getSize31()));
        demandDetail.setSize32(this.convertZeroToNull(updateDTO.getSize32()));
        demandDetail.setSize33(this.convertZeroToNull(updateDTO.getSize33()));
        demandDetail.setSize34(this.convertZeroToNull(updateDTO.getSize34()));
        demandDetail.setSize35(this.convertZeroToNull(updateDTO.getSize35()));
        demandDetail.setSize36(this.convertZeroToNull(updateDTO.getSize36()));
        demandDetail.setSize37(this.convertZeroToNull(updateDTO.getSize37()));
        demandDetail.setSize38(this.convertZeroToNull(updateDTO.getSize38()));
        demandDetail.setSize39(this.convertZeroToNull(updateDTO.getSize39()));
        demandDetail.setSize40(this.convertZeroToNull(updateDTO.getSize40()));
        demandDetail.setSize41(this.convertZeroToNull(updateDTO.getSize41()));
        demandDetail.setSize42(this.convertZeroToNull(updateDTO.getSize42()));
        demandDetail.setSize43(this.convertZeroToNull(updateDTO.getSize43()));
        return this.demandDetailMapper.updateById(demandDetail);
    }

    /**
     * 将0值转换为null
     */
    private Integer convertZeroToNull(Integer value) {
        return value == null || value == 0 ? null : value;
    }

}
