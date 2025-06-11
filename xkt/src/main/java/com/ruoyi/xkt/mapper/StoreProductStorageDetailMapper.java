package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.StoreProductStorageDetail;
import com.ruoyi.xkt.dto.storeProdStorage.StoreStorageDetailDownloadDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
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
     * @param storeProdStorageIdList 入库单ID列表
     * @return 入库记录
     */
    List<StoreStorageDetailDownloadDTO> selectExportList(@Param("storeProdStorageIdList") List<Long> storeProdStorageIdList);

    /**
     * 导出指定时间段内的入库记录
     *
     * @param voucherDateStart 开始时间
     * @param voucherDateEnd   结束时间
     * @return 入库记录
     */
    List<StoreStorageDetailDownloadDTO> selectExportListVoucherDateBetween(@Param("voucherDateStart") Date voucherDateStart, @Param("voucherDateEnd") Date voucherDateEnd);

}
