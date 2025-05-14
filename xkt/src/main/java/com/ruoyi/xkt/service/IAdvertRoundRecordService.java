package com.ruoyi.xkt.service;

import com.ruoyi.common.core.page.Page;
import com.ruoyi.xkt.dto.advertRoundRecord.AdvertRoundRecordPageDTO;
import com.ruoyi.xkt.dto.advertRoundRecord.AdvertRoundRecordPageResDTO;

/**
 * 推广营销竞价失败Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IAdvertRoundRecordService {

    /**
     * 获取竞价失败列表
     *
     * @param pageDTO 竞价失败查询列表
     * @return Page<AdvertRoundRecordPageResDTO>
     */
    Page<AdvertRoundRecordPageResDTO> page(AdvertRoundRecordPageDTO pageDTO);

}
