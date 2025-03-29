package com.ruoyi.xkt.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.xkt.domain.StoreSale;
import com.ruoyi.xkt.mapper.StoreSaleMapper;
import com.ruoyi.xkt.service.IStoreSaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 档口销售出库Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
public class StoreSaleServiceImpl implements IStoreSaleService {
    @Autowired
    private StoreSaleMapper storeSaleMapper;

    /**
     * 查询档口销售出库
     *
     * @param storeSaleId 档口销售出库主键
     * @return 档口销售出库
     */
    @Override
    public StoreSale selectStoreSaleByStoreSaleId(Long storeSaleId) {
        return storeSaleMapper.selectStoreSaleByStoreSaleId(storeSaleId);
    }

    /**
     * 查询档口销售出库列表
     *
     * @param storeSale 档口销售出库
     * @return 档口销售出库
     */
    @Override
    public List<StoreSale> selectStoreSaleList(StoreSale storeSale) {
        return storeSaleMapper.selectStoreSaleList(storeSale);
    }

    /**
     * 新增档口销售出库
     *
     * @param storeSale 档口销售出库
     * @return 结果
     */
    @Override
    @Transactional
    public int insertStoreSale(StoreSale storeSale) {
        storeSale.setCreateTime(DateUtils.getNowDate());
        return storeSaleMapper.insertStoreSale(storeSale);
    }

    /**
     * 修改档口销售出库
     *
     * @param storeSale 档口销售出库
     * @return 结果
     */
    @Override
    @Transactional
    public int updateStoreSale(StoreSale storeSale) {
        storeSale.setUpdateTime(DateUtils.getNowDate());
        return storeSaleMapper.updateStoreSale(storeSale);
    }

    /**
     * 批量删除档口销售出库
     *
     * @param storeSaleIds 需要删除的档口销售出库主键
     * @return 结果
     */
    @Override
    public int deleteStoreSaleByStoreSaleIds(Long[] storeSaleIds) {
        return storeSaleMapper.deleteStoreSaleByStoreSaleIds(storeSaleIds);
    }

    /**
     * 删除档口销售出库信息
     *
     * @param storeSaleId 档口销售出库主键
     * @return 结果
     */
    @Override
    public int deleteStoreSaleByStoreSaleId(Long storeSaleId) {
        return storeSaleMapper.deleteStoreSaleByStoreSaleId(storeSaleId);
    }
}
