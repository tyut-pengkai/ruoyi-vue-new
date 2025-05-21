package com.ruoyi.xkt.service;

import com.ruoyi.common.core.page.Page;
import com.ruoyi.xkt.dto.dailySale.StoreTodaySaleDTO;
import com.ruoyi.xkt.dto.storeCustomer.StoreCusGeneralSaleDTO;
import com.ruoyi.xkt.dto.storeSale.*;

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
    public StoreSaleDTO selectStoreSaleByStoreSaleId(Long storeSaleId);

    /**
     * 新增档口销售出库
     *
     * @param storeSaleDTO 档口销售出库
     * @return 结果
     */
    public int insertStoreSale(StoreSaleDTO storeSaleDTO);

    /**
     * 修改档口销售出库
     *
     * @param storeSaleDTO 档口销售出库
     * @return 结果
     */
    public int updateStoreSale(StoreSaleDTO storeSaleDTO);

    /**
     * 删除档口销售出库信息
     *
     * @param storeSaleId 档口销售出库主键
     * @return 结果
     */
    public int deleteStoreSaleByStoreSaleId(Long storeSaleId);

    /**
     * 获取当前档口客户的销售业绩
     *
     * @param days       查询时间ditff
     * @param storeId    档口ID
     * @param storeCusId 档口客户ID
     * @return StoreCusGeneralSaleDTO
     */
    StoreCusGeneralSaleDTO getCusGeneralSale(Integer days, Long storeId, Long storeCusId);

    /**
     * 分页查询销售出库列表
     *
     * @param pageDTO 入参
     * @return Page
     */
    Page<StoreSalePageResDTO> page(StoreSalePageDTO pageDTO);

    /**
     * 清除店铺顾客债务信息
     * <p>
     * 该方法旨在根据提供的店铺销售支付状态信息来清除或更新店铺顾客的债务记录
     * 它通常在完成销售交易、债务偿还或其他需要调整顾客债务的情况下调用
     *
     * @param payStatusDTO 包含店铺销售支付状态的DTO对象，用于确定是否需要清除顾客债务
     * @return 返回一个整数，表示受影响的债务记录数量或状态更新结果
     */
    Integer clearStoreCusDebt(StoreSalePayStatusDTO payStatusDTO);

    /**
     * 获取当前档口今日销售数据
     *
     * @param storeId 档口ID
     * @return StoreTodaySaleDTO
     */
    StoreTodaySaleDTO getTodaySale(Long storeId);

    /**
     * 修改备注
     *
     * @param updateMemoDTO 入参
     * @return Integer
     */
    Integer updateMemo(StoreSaleUpdateMemoDTO updateMemoDTO);
}
