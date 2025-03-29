package com.ruoyi.xkt.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.xkt.domain.StoreOrderExpress;
import com.ruoyi.xkt.mapper.StoreOrderExpressMapper;
import com.ruoyi.xkt.service.IStoreOrderExpressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 档口代发订单快递Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
public class StoreOrderExpressServiceImpl implements IStoreOrderExpressService {
    @Autowired
    private StoreOrderExpressMapper storeOrderExpressMapper;

    /**
     * 查询档口代发订单快递
     *
     * @param storeOrderExprId 档口代发订单快递主键
     * @return 档口代发订单快递
     */
    @Override
    public StoreOrderExpress selectStoreOrderExpressByStoreOrderExprId(Long storeOrderExprId) {
        return storeOrderExpressMapper.selectStoreOrderExpressByStoreOrderExprId(storeOrderExprId);
    }

    /**
     * 查询档口代发订单快递列表
     *
     * @param storeOrderExpress 档口代发订单快递
     * @return 档口代发订单快递
     */
    @Override
    public List<StoreOrderExpress> selectStoreOrderExpressList(StoreOrderExpress storeOrderExpress) {
        return storeOrderExpressMapper.selectStoreOrderExpressList(storeOrderExpress);
    }

    /**
     * 新增档口代发订单快递
     *
     * @param storeOrderExpress 档口代发订单快递
     * @return 结果
     */
    @Override
    @Transactional
    public int insertStoreOrderExpress(StoreOrderExpress storeOrderExpress) {
        storeOrderExpress.setCreateTime(DateUtils.getNowDate());
        return storeOrderExpressMapper.insertStoreOrderExpress(storeOrderExpress);
    }

    /**
     * 修改档口代发订单快递
     *
     * @param storeOrderExpress 档口代发订单快递
     * @return 结果
     */
    @Override
    @Transactional
    public int updateStoreOrderExpress(StoreOrderExpress storeOrderExpress) {
        storeOrderExpress.setUpdateTime(DateUtils.getNowDate());
        return storeOrderExpressMapper.updateStoreOrderExpress(storeOrderExpress);
    }

    /**
     * 批量删除档口代发订单快递
     *
     * @param storeOrderExprIds 需要删除的档口代发订单快递主键
     * @return 结果
     */
    @Override
    public int deleteStoreOrderExpressByStoreOrderExprIds(Long[] storeOrderExprIds) {
        return storeOrderExpressMapper.deleteStoreOrderExpressByStoreOrderExprIds(storeOrderExprIds);
    }

    /**
     * 删除档口代发订单快递信息
     *
     * @param storeOrderExprId 档口代发订单快递主键
     * @return 结果
     */
    @Override
    public int deleteStoreOrderExpressByStoreOrderExprId(Long storeOrderExprId) {
        return storeOrderExpressMapper.deleteStoreOrderExpressByStoreOrderExprId(storeOrderExprId);
    }
}
