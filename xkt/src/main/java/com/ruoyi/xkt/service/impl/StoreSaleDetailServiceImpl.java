package com.ruoyi.xkt.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.xkt.domain.StoreSaleDetail;
import com.ruoyi.xkt.mapper.StoreSaleDetailMapper;
import com.ruoyi.xkt.service.IStoreSaleDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 档口销售明细Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
public class StoreSaleDetailServiceImpl implements IStoreSaleDetailService {
    @Autowired
    private StoreSaleDetailMapper storeSaleDetailMapper;

    /**
     * 查询档口销售明细
     *
     * @param storeSaleDetailId 档口销售明细主键
     * @return 档口销售明细
     */
    @Override
    public StoreSaleDetail selectStoreSaleDetailByStoreSaleDetailId(Long storeSaleDetailId) {
        return storeSaleDetailMapper.selectStoreSaleDetailByStoreSaleDetailId(storeSaleDetailId);
    }

    /**
     * 查询档口销售明细列表
     *
     * @param storeSaleDetail 档口销售明细
     * @return 档口销售明细
     */
    @Override
    public List<StoreSaleDetail> selectStoreSaleDetailList(StoreSaleDetail storeSaleDetail) {
        return storeSaleDetailMapper.selectStoreSaleDetailList(storeSaleDetail);
    }

    /**
     * 新增档口销售明细
     *
     * @param storeSaleDetail 档口销售明细
     * @return 结果
     */
    @Override
    public int insertStoreSaleDetail(StoreSaleDetail storeSaleDetail) {
        storeSaleDetail.setCreateTime(DateUtils.getNowDate());
        return storeSaleDetailMapper.insertStoreSaleDetail(storeSaleDetail);
    }

    /**
     * 修改档口销售明细
     *
     * @param storeSaleDetail 档口销售明细
     * @return 结果
     */
    @Override
    public int updateStoreSaleDetail(StoreSaleDetail storeSaleDetail) {
        storeSaleDetail.setUpdateTime(DateUtils.getNowDate());
        return storeSaleDetailMapper.updateStoreSaleDetail(storeSaleDetail);
    }

    /**
     * 批量删除档口销售明细
     *
     * @param storeSaleDetailIds 需要删除的档口销售明细主键
     * @return 结果
     */
    @Override
    public int deleteStoreSaleDetailByStoreSaleDetailIds(Long[] storeSaleDetailIds) {
        return storeSaleDetailMapper.deleteStoreSaleDetailByStoreSaleDetailIds(storeSaleDetailIds);
    }

    /**
     * 删除档口销售明细信息
     *
     * @param storeSaleDetailId 档口销售明细主键
     * @return 结果
     */
    @Override
    public int deleteStoreSaleDetailByStoreSaleDetailId(Long storeSaleDetailId) {
        return storeSaleDetailMapper.deleteStoreSaleDetailByStoreSaleDetailId(storeSaleDetailId);
    }
}
