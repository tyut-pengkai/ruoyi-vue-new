package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.StoreSale;
import com.ruoyi.xkt.dto.storeSale.StoreSaleDownloadDTO;
import com.ruoyi.xkt.dto.storeSale.StoreSaleExportDTO;
import com.ruoyi.xkt.dto.storeSale.StoreSalePageDTO;
import com.ruoyi.xkt.dto.storeSale.StoreSalePageResDTO;

import java.util.List;

/**
 * 档口销售出库Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface StoreSaleMapper extends BaseMapper<StoreSale> {

    /**
     * 分页
     *
     * @param pageDTO 入参
     * @return List<StoreSalePageResDTO>
     */
    List<StoreSalePageResDTO> selectPage(StoreSalePageDTO pageDTO);

    /**
     * 导出指定id列表
     *
     * @param exportDTO 导出入参
     * @return List<StoreSaleDownloadDTO>
     */
    List<StoreSaleDownloadDTO> selectExportList(StoreSaleExportDTO exportDTO);

    /**
     * 导出指定时间段内的数据
     *
     * @param exportDTO 导出入参
     * @return List<StoreSaleDownloadDTO>
     */
    List<StoreSaleDownloadDTO> selectExportListVoucherDateBetween(StoreSaleExportDTO exportDTO);

}
