package com.ruoyi.xkt.service;

import com.ruoyi.xkt.domain.StoreSaleDetail;

import java.util.List;

/**
 * 档口销售明细Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IStoreSaleDetailService {
    /**
     * 查询档口销售明细
     *
     * @param storeSaleDetailId 档口销售明细主键
     * @return 档口销售明细
     */
    public StoreSaleDetail selectStoreSaleDetailByStoreSaleDetailId(Long storeSaleDetailId);

    /**
     * 查询档口销售明细列表
     *
     * @param storeSaleDetail 档口销售明细
     * @return 档口销售明细集合
     */
    public List<StoreSaleDetail> selectStoreSaleDetailList(StoreSaleDetail storeSaleDetail);

    /**
     * 新增档口销售明细
     *
     * @param storeSaleDetail 档口销售明细
     * @return 结果
     */
    public int insertStoreSaleDetail(StoreSaleDetail storeSaleDetail);

    /**
     * 修改档口销售明细
     *
     * @param storeSaleDetail 档口销售明细
     * @return 结果
     */
    public int updateStoreSaleDetail(StoreSaleDetail storeSaleDetail);

    /**
     * 批量删除档口销售明细
     *
     * @param storeSaleDetailIds 需要删除的档口销售明细主键集合
     * @return 结果
     */
    public int deleteStoreSaleDetailByStoreSaleDetailIds(Long[] storeSaleDetailIds);

    /**
     * 删除档口销售明细信息
     *
     * @param storeSaleDetailId 档口销售明细主键
     * @return 结果
     */
    public int deleteStoreSaleDetailByStoreSaleDetailId(Long storeSaleDetailId);
}
