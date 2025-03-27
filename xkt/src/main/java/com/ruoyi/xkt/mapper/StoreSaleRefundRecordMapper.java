package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.StoreSaleRefundRecord;

import java.util.List;

/**
 * 档口销售返单Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface StoreSaleRefundRecordMapper extends BaseMapper<StoreSaleRefundRecord> {
    /**
     * 查询档口销售返单
     *
     * @param storeSaleRefundRecordId 档口销售返单主键
     * @return 档口销售返单
     */
    public StoreSaleRefundRecord selectStoreSaleRefundRecordByStoreSaleRefundRecordId(Long storeSaleRefundRecordId);

    /**
     * 查询档口销售返单列表
     *
     * @param storeSaleRefundRecord 档口销售返单
     * @return 档口销售返单集合
     */
    public List<StoreSaleRefundRecord> selectStoreSaleRefundRecordList(StoreSaleRefundRecord storeSaleRefundRecord);

    /**
     * 新增档口销售返单
     *
     * @param storeSaleRefundRecord 档口销售返单
     * @return 结果
     */
    public int insertStoreSaleRefundRecord(StoreSaleRefundRecord storeSaleRefundRecord);

    /**
     * 修改档口销售返单
     *
     * @param storeSaleRefundRecord 档口销售返单
     * @return 结果
     */
    public int updateStoreSaleRefundRecord(StoreSaleRefundRecord storeSaleRefundRecord);

    /**
     * 删除档口销售返单
     *
     * @param storeSaleRefundRecordId 档口销售返单主键
     * @return 结果
     */
    public int deleteStoreSaleRefundRecordByStoreSaleRefundRecordId(Long storeSaleRefundRecordId);

    /**
     * 批量删除档口销售返单
     *
     * @param storeSaleRefundRecordIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStoreSaleRefundRecordByStoreSaleRefundRecordIds(Long[] storeSaleRefundRecordIds);
}
