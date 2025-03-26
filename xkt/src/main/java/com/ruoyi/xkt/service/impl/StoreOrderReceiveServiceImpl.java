package com.ruoyi.xkt.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.xkt.domain.StoreOrderReceive;
import com.ruoyi.xkt.mapper.StoreOrderReceiveMapper;
import com.ruoyi.xkt.service.IStoreOrderReceiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 档口代发订单收件人Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
public class StoreOrderReceiveServiceImpl implements IStoreOrderReceiveService {
    @Autowired
    private StoreOrderReceiveMapper storeOrderReceiveMapper;

    /**
     * 查询档口代发订单收件人
     *
     * @param storeOrderRcvId 档口代发订单收件人主键
     * @return 档口代发订单收件人
     */
    @Override
    public StoreOrderReceive selectStoreOrderReceiveByStoreOrderRcvId(Long storeOrderRcvId) {
        return storeOrderReceiveMapper.selectStoreOrderReceiveByStoreOrderRcvId(storeOrderRcvId);
    }

    /**
     * 查询档口代发订单收件人列表
     *
     * @param storeOrderReceive 档口代发订单收件人
     * @return 档口代发订单收件人
     */
    @Override
    public List<StoreOrderReceive> selectStoreOrderReceiveList(StoreOrderReceive storeOrderReceive) {
        return storeOrderReceiveMapper.selectStoreOrderReceiveList(storeOrderReceive);
    }

    /**
     * 新增档口代发订单收件人
     *
     * @param storeOrderReceive 档口代发订单收件人
     * @return 结果
     */
    @Override
    public int insertStoreOrderReceive(StoreOrderReceive storeOrderReceive) {
        storeOrderReceive.setCreateTime(DateUtils.getNowDate());
        return storeOrderReceiveMapper.insertStoreOrderReceive(storeOrderReceive);
    }

    /**
     * 修改档口代发订单收件人
     *
     * @param storeOrderReceive 档口代发订单收件人
     * @return 结果
     */
    @Override
    public int updateStoreOrderReceive(StoreOrderReceive storeOrderReceive) {
        storeOrderReceive.setUpdateTime(DateUtils.getNowDate());
        return storeOrderReceiveMapper.updateStoreOrderReceive(storeOrderReceive);
    }

    /**
     * 批量删除档口代发订单收件人
     *
     * @param storeOrderRcvIds 需要删除的档口代发订单收件人主键
     * @return 结果
     */
    @Override
    public int deleteStoreOrderReceiveByStoreOrderRcvIds(Long[] storeOrderRcvIds) {
        return storeOrderReceiveMapper.deleteStoreOrderReceiveByStoreOrderRcvIds(storeOrderRcvIds);
    }

    /**
     * 删除档口代发订单收件人信息
     *
     * @param storeOrderRcvId 档口代发订单收件人主键
     * @return 结果
     */
    @Override
    public int deleteStoreOrderReceiveByStoreOrderRcvId(Long storeOrderRcvId) {
        return storeOrderReceiveMapper.deleteStoreOrderReceiveByStoreOrderRcvId(storeOrderRcvId);
    }
}
