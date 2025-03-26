package com.ruoyi.xkt.service;

import com.ruoyi.xkt.domain.StoreSale;

import java.util.List;

/**
 * 档口销售出库Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IStoreSaleService {
    /**
     * 查询档口销售出库
     *
     * @param storeSaleId 档口销售出库主键
     * @return 档口销售出库
     */
    public StoreSale selectStoreSaleByStoreSaleId(Long storeSaleId);

    /**
     * 查询档口销售出库列表
     *
     * @param storeSale 档口销售出库
     * @return 档口销售出库集合
     */
    public List<StoreSale> selectStoreSaleList(StoreSale storeSale);

    /**
     * 新增档口销售出库
     *
     * @param storeSale 档口销售出库
     * @return 结果
     */
    public int insertStoreSale(StoreSale storeSale);

    /**
     * 修改档口销售出库
     *
     * @param storeSale 档口销售出库
     * @return 结果
     */
    public int updateStoreSale(StoreSale storeSale);

    /**
     * 批量删除档口销售出库
     *
     * @param storeSaleIds 需要删除的档口销售出库主键集合
     * @return 结果
     */
    public int deleteStoreSaleByStoreSaleIds(Long[] storeSaleIds);

    /**
     * 删除档口销售出库信息
     *
     * @param storeSaleId 档口销售出库主键
     * @return 结果
     */
    public int deleteStoreSaleByStoreSaleId(Long storeSaleId);
}
