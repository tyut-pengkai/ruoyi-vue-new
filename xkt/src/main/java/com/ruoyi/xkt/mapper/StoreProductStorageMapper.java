package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.StoreProductStorage;
import com.ruoyi.xkt.dto.storeProdStorage.StoreProdStoragePageDTO;
import com.ruoyi.xkt.dto.storeProdStorage.StoreProdStoragePageResDTO;

import java.util.List;

/**
 * 档口商品入库Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface StoreProductStorageMapper extends BaseMapper<StoreProductStorage> {

    /**
     * 查询出库单分页
     *
     * @param storagePageDTO 查询入参
     * @return List<StoreProdStoragePageResDTO>
     */
    List<StoreProdStoragePageResDTO> selectStoragePage(StoreProdStoragePageDTO storagePageDTO);

}
