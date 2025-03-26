package com.ruoyi.xkt.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.xkt.domain.StoreOrderDetail;
import com.ruoyi.xkt.mapper.StoreOrderDetailMapper;
import com.ruoyi.xkt.service.IStoreOrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 档口代发订单明细Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
public class StoreOrderDetailServiceImpl implements IStoreOrderDetailService {
    @Autowired
    private StoreOrderDetailMapper storeOrderDetailMapper;

    /**
     * 查询档口代发订单明细
     *
     * @param storeOrderDetailId 档口代发订单明细主键
     * @return 档口代发订单明细
     */
    @Override
    public StoreOrderDetail selectStoreOrderDetailByStoreOrderDetailId(Long storeOrderDetailId) {
        return storeOrderDetailMapper.selectStoreOrderDetailByStoreOrderDetailId(storeOrderDetailId);
    }

    /**
     * 查询档口代发订单明细列表
     *
     * @param storeOrderDetail 档口代发订单明细
     * @return 档口代发订单明细
     */
    @Override
    public List<StoreOrderDetail> selectStoreOrderDetailList(StoreOrderDetail storeOrderDetail) {
        return storeOrderDetailMapper.selectStoreOrderDetailList(storeOrderDetail);
    }

    /**
     * 新增档口代发订单明细
     *
     * @param storeOrderDetail 档口代发订单明细
     * @return 结果
     */
    @Override
    public int insertStoreOrderDetail(StoreOrderDetail storeOrderDetail) {
        storeOrderDetail.setCreateTime(DateUtils.getNowDate());
        return storeOrderDetailMapper.insertStoreOrderDetail(storeOrderDetail);
    }

    /**
     * 修改档口代发订单明细
     *
     * @param storeOrderDetail 档口代发订单明细
     * @return 结果
     */
    @Override
    public int updateStoreOrderDetail(StoreOrderDetail storeOrderDetail) {
        storeOrderDetail.setUpdateTime(DateUtils.getNowDate());
        return storeOrderDetailMapper.updateStoreOrderDetail(storeOrderDetail);
    }

    /**
     * 批量删除档口代发订单明细
     *
     * @param storeOrderDetailIds 需要删除的档口代发订单明细主键
     * @return 结果
     */
    @Override
    public int deleteStoreOrderDetailByStoreOrderDetailIds(Long[] storeOrderDetailIds) {
        return storeOrderDetailMapper.deleteStoreOrderDetailByStoreOrderDetailIds(storeOrderDetailIds);
    }

    /**
     * 删除档口代发订单明细信息
     *
     * @param storeOrderDetailId 档口代发订单明细主键
     * @return 结果
     */
    @Override
    public int deleteStoreOrderDetailByStoreOrderDetailId(Long storeOrderDetailId) {
        return storeOrderDetailMapper.deleteStoreOrderDetailByStoreOrderDetailId(storeOrderDetailId);
    }
}
