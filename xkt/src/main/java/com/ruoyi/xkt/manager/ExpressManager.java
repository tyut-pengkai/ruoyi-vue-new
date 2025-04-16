package com.ruoyi.xkt.manager;

import com.ruoyi.xkt.dto.express.ExpressShipReqDTO;
import com.ruoyi.xkt.enums.EExpressChannel;

/**
 * @author liangyq
 * @date 2025-04-15 15:43
 */
public interface ExpressManager {
    /**
     * 物流渠道
     *
     * @return
     */
    EExpressChannel channel();

    /**
     * 订单发货
     *
     * @param shipReqDTO
     * @return 运单号
     */
    String shipStoreOrder(ExpressShipReqDTO shipReqDTO);

    /**
     * 打印面单
     *
     * @param waybillNo
     * @return 文件流Base64编码
     */
    String printOrder(String waybillNo);

}
