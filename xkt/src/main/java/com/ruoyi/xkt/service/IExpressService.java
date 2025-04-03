package com.ruoyi.xkt.service;

import com.ruoyi.xkt.domain.ExpressFeeConfig;

/**
 * @author liangyq
 * @date 2025-04-03 13:35
 */
public interface IExpressService {

    /**
     * 检查快递是否可用
     *
     * @param expressId
     */
    void checkExpress(Long expressId);

    /**
     * 获取快递费配置
     *
     * @param expressId
     * @param provinceCode
     * @param cityCode
     * @param countyCode
     * @return
     */
    ExpressFeeConfig getExpressFeeConfig(Long expressId, String provinceCode, String cityCode, String countyCode);
}
