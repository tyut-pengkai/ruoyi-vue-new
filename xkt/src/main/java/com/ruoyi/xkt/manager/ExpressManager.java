package com.ruoyi.xkt.manager;

import com.ruoyi.xkt.dto.express.*;
import com.ruoyi.xkt.enums.EExpressChannel;

import java.util.Collection;
import java.util.List;

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
     * 取消发货
     *
     * @param cancelReqDTO
     * @return
     */
    boolean cancelShip(ExpressCancelReqDTO cancelReqDTO);

    /**
     * 拦截
     *
     * @return
     */
    boolean interceptShip(ExpressInterceptReqDTO interceptReqDTO);

    /**
     * 打印面单
     *
     * @param waybillNos
     * @return
     */
    List<ExpressPrintDTO> printOrder(Collection<String> waybillNos);

    /**
     * 订阅轨迹
     *
     * @param trackSubReq
     * @return
     */
    boolean subscribeTrack(ExpressTrackSubReqDTO trackSubReq);
}
