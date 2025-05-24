package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.DailySaleCustomer;
import com.ruoyi.xkt.dto.dailySale.DailySaleCusDTO;
import com.ruoyi.xkt.dto.store.StoreIndexCusSaleTop10ResDTO;
import com.ruoyi.xkt.dto.store.StoreSaleCustomerTop10DTO;

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
     * @param saleCusTop10DTO 查询入参
     * @return List<StoreIndexCustomerSaleTop10ResDTO>
     */
    List<StoreIndexCusSaleTop10ResDTO> selectTop10SaleCustomerList(StoreSaleCustomerTop10DTO saleCusTop10DTO);

}
