package com.easycode.cloud.service;

import com.easycode.cloud.domain.dto.OutSourceOrderBomDto;

import java.util.List;

/**
 * 委外订单bom获取
 *
 * @author weifu
 * @date 2022-12-12
 */
public interface IOutsourceOrderBomSyncService {

    /**
     * 从mom获取容器信息
     * @param purchaseOrderNo 容器号
     * @return
     */
    List<OutSourceOrderBomDto> getOutsourceBomList(String purchaseOrderNo) throws Exception;
}
