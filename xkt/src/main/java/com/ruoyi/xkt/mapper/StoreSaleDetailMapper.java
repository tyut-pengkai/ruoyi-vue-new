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
     * 根据条码查询档口销售明细销售记录
     * @param barcodeDTO 条码信息
     * @return
     */
    StoreSaleSnResDTO selectBySn(StoreSaleSnDTO barcodeDTO);
}
