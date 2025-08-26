package com.ruoyi.xkt.manager.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.xkt.dto.express.*;
import com.ruoyi.xkt.enums.EExpressChannel;
import com.ruoyi.xkt.manager.ExpressManager;
import com.ruoyi.xkt.thirdpart.yto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
    public ExpressShippingLabelDTO shipStoreOrder(ExpressShipReqDTO shipReqDTO) {
        YtoCreateOrderParam createOrderParam = trans2CreateOrderReq(shipReqDTO);
        try {
            String param = JSONUtil.toJsonStr(createOrderParam);
            String sign = YtoSignUtil.sign("privacy_create_adapter", "v1", param, appSecret);
            YtoPublicRequest request = YtoPublicRequest.builder()
                    .timestamp(System.currentTimeMillis())
                    .param(param)
                    .format(YtoPublicRequest.EFormat.JSON)
                    .sign(sign).build();
            String rtnStr = HttpUtil.post(gatewayUrl + "open/privacy_create_adapter/v1/av7bfQ/" + appKey,
                    JSONUtil.toJsonStr(request));
            log.info("圆通订单创建返回信息: {}", rtnStr);
            com.alibaba.fastjson2.JSONObject rtnJson = JSON.parseObject(rtnStr);
            String waybillNo = rtnJson.getString("mailNo");
            if (StrUtil.isNotEmpty(waybillNo)) {
                ExpressShippingLabelDTO rtn = new ExpressShippingLabelDTO(shipReqDTO.getOriginContactName(),
                        shipReqDTO.getOriginContactPhoneNumber(), shipReqDTO.getOriginProvinceName(),
                        shipReqDTO.getOriginCityName(), shipReqDTO.getOriginCountyName(),
                        shipReqDTO.getOriginDetailAddress(), shipReqDTO.getDestinationContactName(),
                        shipReqDTO.getDestinationContactPhoneNumber(), shipReqDTO.getDestinationProvinceName(),
                        shipReqDTO.getDestinationCityName(), shipReqDTO.getDestinationCountyName(),
                        shipReqDTO.getDestinationDetailAddress());
                rtn.setExpressWaybillNo(waybillNo);
                rtn.setExpressId(channel().getExpressId());
                rtn.setVasType("");
                String mark = rtnJson.getString("shortAddress");
                rtn.setMark(mark);
                if (mark != null) {
                    String[] ms = mark.split("-");
                    if (ms.length > 1) {
                        rtn.setShortMark(ms[1]);
                    }
                }
                rtn.setBagAddr("");
                rtn.setLastPrintTime(new Date());
                rtn.setPrintCount(1);
                rtn.setGoodsInfo(shipReqDTO.getGoodsSummary());
                rtn.setRemark(shipReqDTO.getRemark());
                return rtn;
            }
        } catch (Exception e) {
            log.error("圆通订单创建异常", e);
        }
        throw new ServiceException("圆通订单创建失败");
    }

    @Override
    public boolean cancelShip(ExpressCancelReqDTO cancelReqDTO) {
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
            String rtnStr = HttpUtil.post(gatewayUrl + "open/korder_cancel_adapter/v1/av7bfQ/" + appKey,
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
    public boolean interceptShip(ExpressInterceptReqDTO interceptReqDTO) {
        Assert.notNull(interceptReqDTO);
        Assert.notEmpty(interceptReqDTO.getExpressWaybillNo());
        YtoInterceptReturnParam interceptReturnParam = new YtoInterceptReturnParam();
        interceptReturnParam.setWaybillNo(interceptReqDTO.getExpressWaybillNo());
        interceptReturnParam.setWantedDesc("拦截退回");
        try {
            String param = JSONUtil.toJsonStr(interceptReturnParam);
            String sign = YtoSignUtil.sign("wanted_report_adapter", "v1", param, appSecret);
            YtoPublicRequest request = YtoPublicRequest.builder()
                    .timestamp(System.currentTimeMillis())
                    .param(param)
                    .format(YtoPublicRequest.EFormat.JSON)
                    .sign(sign).build();
            String rtnStr = HttpUtil.post(gatewayUrl + "open/wanted_report_adapter/v1/av7bfQ/" + appKey,
                    JSONUtil.toJsonStr(request));
            log.info("圆通订单拦截返回信息: {}", rtnStr);
            JSONObject rtnJson = JSONUtil.parseObj(rtnStr);
            Integer statusCode = rtnJson.getInt("statusCode");
            if (statusCode != null && statusCode == 0) {
                return true;
            }
        } catch (Exception e) {
            log.error("圆通订单拦截异常", e);
        }
        log.warn("圆通订单拦截失败: {}", interceptReqDTO);
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
                String rtnStr = HttpUtil.post(gatewayUrl + "open/waybill_print_adapter/v1/av7bfQ/" + appKey,
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

    @Override
    public boolean subscribeTrack(ExpressTrackSubReqDTO trackSubReq) {
        Assert.notNull(trackSubReq.getExpressWaybillNo());
        YtoSubTrackParam.LogisticsInterface logisticsInterface = new YtoSubTrackParam.LogisticsInterface();
        logisticsInterface.setClientId(appKey2);
        logisticsInterface.setWaybillNo(trackSubReq.getExpressWaybillNo());
        YtoSubTrackParam ytoSubTrackParam = new YtoSubTrackParam();
        ytoSubTrackParam.setClient_id(appKey2);
        ytoSubTrackParam.setMsg_type("online");
        ytoSubTrackParam.setLogistics_interface(JSONUtil.toJsonStr(logisticsInterface));
        try {
            String param = JSONUtil.toJsonStr(ytoSubTrackParam);
            String sign = YtoSignUtil.sign("subscribe_adapter", "v1", param, appSecret2);
            YtoPublicRequest request = YtoPublicRequest.builder()
                    .timestamp(System.currentTimeMillis())
                    .param(param)
                    .format(YtoPublicRequest.EFormat.JSON)
                    .sign(sign).build();
            String rtnStr = HttpUtil.post(gatewayUrl + "open/subscribe_adapter/v1/av7bfQ/" + appKey2,
                    JSONUtil.toJsonStr(request));
            log.info("圆通轨迹订阅返回信息: {}", rtnStr);
            JSONObject rtnJson = JSONUtil.parseObj(rtnStr);
            Boolean success = rtnJson.getBool("success");
            if (BooleanUtil.isTrue(success)) {
                return true;
            }
        } catch (Exception e) {
            log.error("圆通轨迹订阅异常", e);
        }
        log.warn("圆通轨迹订阅失败: {}", trackSubReq);
        return false;
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
