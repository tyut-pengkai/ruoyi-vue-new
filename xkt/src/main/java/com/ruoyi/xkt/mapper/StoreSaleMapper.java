package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.StoreSale;
import com.ruoyi.xkt.dto.storeSale.StoreSaleDownloadDTO;
import com.ruoyi.xkt.dto.storeSale.StoreSalePageDTO;
import com.ruoyi.xkt.dto.storeSale.StoreSalePageResDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
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
     * @param pageDTO 入参
     * @return
     */
    List<StoreSalePageResDTO> selectPage(StoreSalePageDTO pageDTO);

    /**
     * 导出指定id列表
     *
     * @param storeSaleIdList 导出入参
     * @return List<StoreSaleDownloadDTO>
     */
    List<StoreSaleDownloadDTO> selectExportList(@Param("storeSaleIdList") List<Long> storeSaleIdList);

    /**
     * 导出指定时间段内的数据
     *
     * @param voucherDateStart 开始时间
     * @param voucherDateEnd   结束时间
     * @return
     */
    List<StoreSaleDownloadDTO> selectExportListVoucherDateBetween(@Param("voucherDateStart") Date voucherDateStart, @Param("voucherDateEnd") Date voucherDateEnd);

}
