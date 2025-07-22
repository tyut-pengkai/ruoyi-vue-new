package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.DailySaleCustomer;
import com.ruoyi.xkt.dto.dailySale.DailySaleCusDTO;
import com.ruoyi.xkt.dto.store.StoreIndexCusSaleTop10ResDTO;
import com.ruoyi.xkt.dto.store.StoreIndexOverviewResDTO;
import com.ruoyi.xkt.dto.store.StoreSaleCustomerTop10DTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 调度任务信息 数据层
 *
 * @author ruoyi
 */
public interface DailySaleCustomerMapper extends BaseMapper<DailySaleCustomer> {

    List<DailySaleCusDTO> selectDailySale(Date voucherDate);

    /**
     * 档口客户销售榜前10
     *
     * @param storeId storeId
     * @param voucherDateStart 开始时间
     * @param voucherDateEnd 结束时间
     * @return List<StoreIndexCustomerSaleTop10ResDTO>
     */
    List<StoreIndexCusSaleTop10ResDTO> selectTop10SaleCustomerList(@Param("storeId") Long storeId,
                                                                   @Param("voucherDateStart") String voucherDateStart,
                                                                   @Param("voucherDateEnd") String voucherDateEnd);

    /**
     * 筛选销售额最大的客户
     *
     * @param storeId          档口ID
     * @param voucherDateStart 开始时间
     * @param voucherDateEnd   结束时间
     * @return StoreIndexOverviewResDTO
     */
    StoreIndexOverviewResDTO getMaxSaleCus(@Param("storeId") Long storeId,
                                           @Param("voucherDateStart") String voucherDateStart,
                                           @Param("voucherDateEnd") String voucherDateEnd);
}
