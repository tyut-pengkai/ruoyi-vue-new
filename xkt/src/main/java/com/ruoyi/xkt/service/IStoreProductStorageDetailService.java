package com.ruoyi.xkt.service;

import com.ruoyi.xkt.dto.storeProdStorage.StoreStorageDetailDownloadDTO;
import com.ruoyi.xkt.dto.storeProdStorage.StoreStorageExportDTO;

import java.util.List;

/**
 * 档口商品入库明细Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IStoreProductStorageDetailService {

    /**
     * 导出档口商品入库明细
     *
     * @param exportDTO 导出参数
     * @return 导出数据
     */
    List<StoreStorageDetailDownloadDTO> export(StoreStorageExportDTO exportDTO);

}
