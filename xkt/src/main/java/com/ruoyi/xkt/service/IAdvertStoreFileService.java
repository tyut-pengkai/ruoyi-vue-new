package com.ruoyi.xkt.service;

import com.ruoyi.common.core.page.Page;
import com.ruoyi.xkt.dto.advertStoreFile.AdvertStoreFilePageDTO;
import com.ruoyi.xkt.dto.advertStoreFile.AdvertStoreFileResDTO;

/**
 * 推广营销Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IAdvertStoreFileService {

    /**
     * 查询推广营销图片管理列表
     *
     * @param pageDTO 查询参数
     * @return 推广营销图片管理列表
     */
    Page<AdvertStoreFileResDTO> page(AdvertStoreFilePageDTO pageDTO);

}
