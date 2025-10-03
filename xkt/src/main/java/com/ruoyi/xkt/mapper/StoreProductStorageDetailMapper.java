package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.StoreProductStorageDetail;
import com.ruoyi.xkt.dto.storeProdStorage.StoreStorageDetailDownloadDTO;
import com.ruoyi.xkt.dto.storeProdStorage.StoreStorageExportDTO;

import java.util.List;

/**
 * 档口商品入库明细Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface StoreProductStorageDetailMapper extends BaseMapper<StoreProductStorageDetail> {

    /**
     * 导出选定的入库记录
     *
     * @param exportDTO 导出入参
     * @return 入库记录
     */
    List<StoreStorageDetailDownloadDTO> selectExportList(StoreStorageExportDTO exportDTO);

    /**
     * 导出指定时间段内的入库记录
     *
     * @param exportDTO 导出入参
     * @return 入库记录
     */
    List<StoreStorageDetailDownloadDTO> selectExportListVoucherDateBetween(StoreStorageExportDTO exportDTO);

}
