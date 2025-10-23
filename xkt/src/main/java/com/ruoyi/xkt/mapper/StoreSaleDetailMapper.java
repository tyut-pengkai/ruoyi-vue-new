package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.StoreSaleDetail;
import com.ruoyi.xkt.dto.storeProdColorSize.StoreSaleSnDTO;
import com.ruoyi.xkt.dto.storeProdColorSize.StoreSaleSnResDTO;

/**
 * 档口销售明细Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface StoreSaleDetailMapper extends BaseMapper<StoreSaleDetail> {

    /**
     * 根据 步橘条码 客户ID 查询最近的一条 普通销售的数据
     *
     * @param barcodeDTO 查询入参
     * @return StoreSaleSnResDTO
     */
    StoreSaleSnResDTO selectRefundByBuJuSnSale(StoreSaleSnDTO barcodeDTO);

    /**
     * 根据 其它系统条码 客户ID 查询最近的一条 普通销售的数据
     *
     * @param barcodeDTO 查询入参
     * @return StoreSaleSnResDTO
     */
    StoreSaleSnResDTO selectRefundByOtherSnSale(StoreSaleSnDTO barcodeDTO);

}
