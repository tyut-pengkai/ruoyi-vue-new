package com.ruoyi.xkt.manager.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.xkt.dto.express.ExpressCancelReqDTO;
import com.ruoyi.xkt.dto.express.ExpressPrintDTO;
import com.ruoyi.xkt.dto.express.ExpressShipReqDTO;
import com.ruoyi.xkt.enums.EExpressChannel;
import com.ruoyi.xkt.manager.ExpressManager;
import com.ruoyi.xkt.thirdpart.yto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author liangyq
 * @date 2025-04-17 16:55
 */
@Slf4j
@Component
public class YtoExpressManagerImpl implements ExpressManager {

    @Value("${yto.appKey:}")
    private String appKey;

    @Value("${yto.appSecret:}")
    private String appSecret;

    @Value("${yto.appKey2:}")
    private String appKey2;

    @Value("${yto.appSecret2:}")
    private String appSecret2;

    @Value("${yto.gatewayUrl:}")
    private String gatewayUrl;

    @Override
    public EExpressChannel channel() {
        return EExpressChannel.YTO;
    }

    @Override
    public String shipStoreOrder(ExpressShipReqDTO shipReqDTO) {
        YtoCreateOrderParam createOrderParam = trans2CreateOrderReq(shipReqDTO);
        try {
            String param = JSONUtil.toJsonStr(createOrderParam);
            String sign = YtoSignUtil.sign("privacy_create_adapter", "v1", param, appSecret);
            YtoPublicRequest request = YtoPublicRequest.builder()
                    .timestamp(System.currentTimeMillis())
                    .param(param)
                    .format(YtoPublicRequest.EFormat.JSON)
                    .sign(sign).build();
            String rtnStr = HttpUtil.post(gatewayUrl + "open/privacy_create_adapter/v1/N364gM/" + appKey,
                    JSONUtil.toJsonStr(request));
            log.info("圆通订单创建返回信息: {}", rtnStr);
            JSONObject rtnJson = JSONUtil.parseObj(rtnStr);
            String waybillNo = rtnJson.getStr("mailNo");
            if (StrUtil.isNotEmpty(waybillNo)) {
                return waybillNo;
            }
        } catch (Exception e) {
            log.error("圆通订单创建异常", e);
        }
        throw new ServiceException("圆通订单创建失败");
    }

    @Override
    public boolean cancelShipOrder(ExpressCancelReqDTO cancelReqDTO) {
        Assert.notNull(cancelReqDTO);
        Assert.notEmpty(cancelReqDTO.getExpressReqNo());
        YtoCancelOrderParam cancelOrderParam = new YtoCancelOrderParam();
        cancelOrderParam.setLogisticsNo(cancelReqDTO.getExpressReqNo());
        cancelOrderParam.setCancelDesc("订单取消");
        try {
            String param = JSONUtil.toJsonStr(cancelOrderParam);
            String sign = YtoSignUtil.sign("korder_cancel_adapter", "v1", param, appSecret);
            YtoPublicRequest request = YtoPublicRequest.builder()
                    .timestamp(System.currentTimeMillis())
                    .param(param)
                    .format(YtoPublicRequest.EFormat.JSON)
                    .sign(sign).build();
            String rtnStr = HttpUtil.post(gatewayUrl + "open/korder_cancel_adapter/v1/N364gM/" + appKey,
                    JSONUtil.toJsonStr(request));
            log.info("圆通订单取消返回信息: {}", rtnStr);
            JSONObject rtnJson = JSONUtil.parseObj(rtnStr);
            String logisticsNo = rtnJson.getStr("logisticsNo");
            if (StrUtil.isNotEmpty(logisticsNo)
                    && logisticsNo.equals(cancelOrderParam.getLogisticsNo())) {
                return true;
            }
        } catch (Exception e) {
            log.error("圆通订单取消异常", e);
        }
        log.warn("圆通订单取消失败: {}", cancelReqDTO);
        return false;
    }

    @Override
    public List<ExpressPrintDTO> printOrder(Collection<String> waybillNos) {
        Assert.notEmpty(waybillNos);
        List<ExpressPrintDTO> list = new ArrayList<>(waybillNos.size());
        for (String waybillNo : waybillNos) {
            YtoPrintOrderParam printOrderParam = YtoPrintOrderParam.builder().waybillNo(waybillNo).build();
            try {
                String param = JSONUtil.toJsonStr(printOrderParam);
                String sign = YtoSignUtil.sign("waybill_print_adapter", "v1", param, appSecret);
                YtoPublicRequest request = YtoPublicRequest.builder()
                        .timestamp(System.currentTimeMillis())
                        .param(param)
                        .format(YtoPublicRequest.EFormat.JSON)
                        .sign(sign).build();
                String rtnStr = HttpUtil.post(gatewayUrl + "open/waybill_print_adapter/v1/N364gM/" + appKey,
                        JSONUtil.toJsonStr(request));
                log.info("圆通打印面单返回信息: {}", rtnStr);
                JSONObject rtnJson = JSONUtil.parseObj(rtnStr);
                String rtn = rtnJson.getJSONObject("data").getStr("pdfBase64");
                if (StrUtil.isNotEmpty(rtn)) {
                    list.add(new ExpressPrintDTO(waybillNo, rtn));
                    continue;
                }
            } catch (Exception e) {
                log.error("圆通打印面单异常" + waybillNo, e);
            }
            throw new ServiceException("圆通打印面单失败");
        }
        return list;
    }


    private YtoCreateOrderParam trans2CreateOrderReq(ExpressShipReqDTO shipReqDTO) {
        YtoCreateOrderParam reqDTO = new YtoCreateOrderParam();
        reqDTO.setLogisticsNo(shipReqDTO.getExpressReqNo());

        reqDTO.setSenderName(shipReqDTO.getOriginContactName());
        reqDTO.setSenderProvinceName(shipReqDTO.getOriginProvinceName());
        reqDTO.setSenderCityName(shipReqDTO.getOriginCityName());
        reqDTO.setSenderCountyName(shipReqDTO.getOriginCountyName());
        reqDTO.setSenderAddress(shipReqDTO.getOriginDetailAddress());
        reqDTO.setSenderMobile(shipReqDTO.getOriginContactPhoneNumber());

        reqDTO.setRecipientName(shipReqDTO.getDestinationContactName());
        reqDTO.setRecipientProvinceName(shipReqDTO.getDestinationProvinceName());
        reqDTO.setRecipientCityName(shipReqDTO.getDestinationCityName());
        reqDTO.setRecipientCountyName(shipReqDTO.getDestinationCountyName());
        reqDTO.setRecipientAddress(shipReqDTO.getDestinationDetailAddress());
        reqDTO.setRecipientMobile(shipReqDTO.getDestinationContactPhoneNumber());

        int goodsSize = shipReqDTO.getOrderItems().size();
        if (shipReqDTO.getOrderItems().size() > 20) {
            throw new ServiceException("圆通每次发货物品在20个以内！");
        }
        reqDTO.setGoods(new ArrayList<>(goodsSize));
        for (ExpressShipReqDTO.OrderItem item : shipReqDTO.getOrderItems()) {
            YtoCreateOrderParam.OrderGoods goods = new YtoCreateOrderParam.OrderGoods();
            reqDTO.getGoods().add(goods);
            goods.setName(item.getName());
            goods.setQuantity(item.getQuantity());
        }
        return reqDTO;
    }

}
