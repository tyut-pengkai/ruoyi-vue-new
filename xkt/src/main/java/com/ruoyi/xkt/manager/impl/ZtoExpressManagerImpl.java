package com.ruoyi.xkt.manager.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.xkt.dto.express.*;
import com.ruoyi.xkt.enums.EExpressChannel;
import com.ruoyi.xkt.manager.ExpressManager;
import com.ruoyi.xkt.thirdpart.zto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author liangyq
 * @date 2025-04-15 15:45
 */
@Slf4j
@Component
public class ZtoExpressManagerImpl implements ExpressManager, InitializingBean {

    private static final String CREATE_ORDER_URI = "zto.open.createOrder";

    private static final String CANCEL_ORDER_URI = "zto.open.cancelPreOrder";

    private static final String INTERCEPT_URI = "thirdcenter.createIntercept";

    private static final String STRUCTURE_ADDRESS_URI = "zto.innovate.structureNamePhoneAddress";

    private static final String ORDER_PRINT_URI = "zto.open.order.print";

    private static final String TRACK_SUB_URI = "zto.merchant.waybill.track.subsrcibe";

    private static final String QUERY_ALL_REGION_URI = "zto.sp.findAllForbServiceGateway";

    @Value("${zto.appKey:}")
    private String appKey;

    @Value("${zto.appSecret:}")
    private String appSecret;

    @Value("${zto.gatewayUrl:}")
    private String gatewayUrl;

    @Value("${zto.accountId:}")
    private String accountId;

    @Value("${zto.accountPassword:}")
    private String accountPassword;

    @Autowired
    private RedisCache redisCache;

    private ZopClient client;

    @Override
    public EExpressChannel channel() {
        return EExpressChannel.ZTO;
    }

    @Override
    public ExpressShippingLabelDTO shipStoreOrder(ExpressShipReqDTO shipReqDTO) {
        Assert.notNull(shipReqDTO);
        Assert.notEmpty(shipReqDTO.getExpressReqNo());
        ZtoCreateOrderParam createOrderParam = trans2CreateOrderReq(shipReqDTO);
        ZopPublicRequest request = new ZopPublicRequest();
        request.setBody(JSONUtil.toJsonStr(createOrderParam));
        request.setUrl(gatewayUrl + CREATE_ORDER_URI);
        request.setEncryptionType(EncryptionType.MD5);
        try {
            String bodyStr = client.execute(request);
            log.info("中通订单创建返回信息: {}", bodyStr);
            com.alibaba.fastjson2.JSONObject bodyJson = JSON.parseObject(bodyStr);
            boolean success = BooleanUtil.isTrue(bodyJson.getBoolean("status"));
            if (success) {
                com.alibaba.fastjson2.JSONObject resultJson = bodyJson.getJSONObject("result");
                String billCode = resultJson.getString("billCode");
                if (billCode != null) {
                    ExpressShippingLabelDTO rtn = new ExpressShippingLabelDTO(shipReqDTO.getOriginContactName(),
                            shipReqDTO.getOriginContactPhoneNumber(), shipReqDTO.getOriginProvinceName(),
                            shipReqDTO.getOriginCityName(), shipReqDTO.getOriginCountyName(),
                            shipReqDTO.getOriginDetailAddress(), shipReqDTO.getDestinationContactName(),
                            shipReqDTO.getDestinationContactPhoneNumber(), shipReqDTO.getDestinationProvinceName(),
                            shipReqDTO.getDestinationCityName(), shipReqDTO.getDestinationCountyName(),
                            shipReqDTO.getDestinationDetailAddress());
                    rtn.setExpressWaybillNo(billCode);
                    rtn.setExpressId(channel().getExpressId());
                    rtn.setVasType("快递包裹");
                    com.alibaba.fastjson2.JSONObject bigMarkInfo = resultJson.getJSONObject("bigMarkInfo");
                    rtn.setMark(bigMarkInfo.getString("mark"));
                    rtn.setShortMark("");
                    rtn.setBagAddr(bigMarkInfo.getString("bagAddr"));
                    rtn.setLastPrintTime(new Date());
                    rtn.setPrintCount(1);
                    rtn.setGoodsInfo(shipReqDTO.getGoodsSummary());
                    rtn.setRemark(shipReqDTO.getRemark());
                    return rtn;
                }
            }

        } catch (Exception e) {
            log.error("中通订单创建异常", e);
        }
        throw new ServiceException("中通订单创建失败");
    }

    @Override
    public boolean cancelShip(ExpressCancelReqDTO cancelReqDTO) {
        Assert.notNull(cancelReqDTO);
        Assert.notEmpty(cancelReqDTO.getExpressWaybillNo());
        ZtoCancelOrderParam cancelOrderParam = new ZtoCancelOrderParam();
        cancelOrderParam.setBillCode(cancelReqDTO.getExpressWaybillNo());
        cancelOrderParam.setCancelType("1");
        ZopPublicRequest request = new ZopPublicRequest();
        request.setBody(JSONUtil.toJsonStr(cancelOrderParam));
        request.setUrl(gatewayUrl + CANCEL_ORDER_URI);
        request.setEncryptionType(EncryptionType.MD5);
        try {
            String bodyStr = client.execute(request);
            log.info("中通订单取消返回信息: {}", bodyStr);
            JSONObject bodyJson = JSONUtil.parseObj(bodyStr);
            boolean success = BooleanUtil.isTrue(bodyJson.getBool("status"));
            if (success) {
                return true;
            }
        } catch (Exception e) {
            log.error("中通订单取消异常", e);
        }
        log.warn("中通订单取消失败: {}", cancelReqDTO);
        return false;
    }

    @Override
    public boolean interceptShip(ExpressInterceptReqDTO interceptReqDTO) {
        Assert.notNull(interceptReqDTO);
        Assert.notEmpty(interceptReqDTO.getExpressWaybillNo());
        Assert.notEmpty(interceptReqDTO.getExpressReqNo());
        ZtoInterceptReturnParam interceptReturnParam = new ZtoInterceptReturnParam();
        interceptReturnParam.setBillCode(interceptReqDTO.getExpressWaybillNo());
        interceptReturnParam.setRequestId(IdUtil.simpleUUID());
        interceptReturnParam.setDestinationType(1);
        interceptReturnParam.setThirdBizNo(interceptReqDTO.getExpressReqNo());
        ZopPublicRequest request = new ZopPublicRequest();
        request.setBody(JSONUtil.toJsonStr(interceptReturnParam));
        request.setUrl(gatewayUrl + INTERCEPT_URI);
        request.setEncryptionType(EncryptionType.MD5);
        try {
            String bodyStr = client.execute(request);
            log.info("中通订单拦截返回信息: {}", bodyStr);
            /**
             * E23101	快件已签收	更换未签收运单发起
             * E23102	订单信息不存在	联系管理员处理
             * E23108	无效的拦截、退改方式	检查入参destinationType
             * E23110	无揽收与面单使用网点	联系管理员处理
             * E23111	时效件，无法拦截	更换运单
             * E23112	已第三方进站不允许拦截	更换运单
             * E23113	有转单信息不允许拦截	更换运单
             * E23114	派件网点不支持到付业务	更换运单
             * E23115	派件网点不支持代收业务	更换运单
             * E23116	渠道不一致，不允许重复发起拦截/超过最大拦截次数	更换运单
             * E23118	订购渠道不存在/该渠道不允许拦截	检查入参platform，如确认无误，联系管理员处理
             * E23119	无任何轨迹不允许发起拦截	揽收后再重试
             * E23120	有派件扫描不允许拦截	更换运单
             * E23121	存在相同渠道的拦截件,不允许再次发起	更换运单
             * E23122	目的地停发不允许拦截	更换运单
             * E23123	存在退改件信息不允许拦截	更换运单
             * E23124	轨迹异常不允许拦截	更换运单
             * E23127	已经有相同诉求,不允许再次发起	更换运单或更改入参destinationType或改地址的收件地址
             * E23130	非拦截中拦截件,不能取消	此运单已无法取消拦截，如仍需取消，请再次拦截改回原址
             * E23131	取消网点与订购网点或原单揽收网点不一致,不能取消	联系管理员处理
             * E23135	同拦截发起方不一致,无权取消	联系管理员处理
             * E23136	已发往港澳台，暂不允许拦截	更换运单
             * E23137	有派件网点到件不允许拦截	更换运单
             * E23138	国际件不允许发起拦截	更换运单
             * E23139	不满足拦截发起业务规则	更换运单
             * E23140	包裹已确认遗失	更换运单
             * E23141	包裹已确认弃件	更换运单
             * E23144	CRM无权限	联系合作网点在客户管理系统开通拦截权限
             * E23146	跨境客户，不允许发起改地址/退回指定地址	更换运单
             * E23148	联系方式格式错误，请检查后提交或不填写	检查入参receivePhone
             * E23149	联系方式是虚拟号，请补全分机号或不填写	检查入参receivePhone
             * E23150	联系方式超过16位，请删减	检查入参receivePhone
             * E23151	已经有相同诉求,不允许再次发起	更换运单或更改入参destinationType或改地址的收件地址
             * E23153	发起人不属于发起网点	联系管理员处理
             * E23154	单号不属于当前网点，不允许发起	联系管理员处理
             * E23157	该渠道不支持重复发起拦截	更换运单
             * E23160	已存在外部平台发起的拦截，不允许发起	更换运单
             */
            JSONObject bodyJson = JSONUtil.parseObj(bodyStr);
            boolean success = BooleanUtil.isTrue(bodyJson.getBool("status"));
            if (success) {
                return true;
            }
        } catch (Exception e) {
            log.error("中通订单拦截异常", e);
        }
        log.warn("中通订单拦截失败: {}", interceptReqDTO);
        return false;
    }

    @Override
    public List<ExpressPrintDTO> printOrder(Collection<String> waybillNos) {
        Assert.notEmpty(waybillNos);
        List<ExpressPrintDTO> list = new ArrayList<>(waybillNos.size());
        for (String waybillNo : waybillNos) {
            ZopPublicRequest request = new ZopPublicRequest();
            request.setBody(JSONUtil.toJsonStr(new ZtoPrintOrderParam(1, waybillNo, true)));
            request.setUrl(gatewayUrl + ORDER_PRINT_URI);
            request.setEncryptionType(EncryptionType.MD5);
            try {
                String bodyStr = client.execute(request);
                log.info("中通订单打印返回信息: {}", bodyStr);
                JSONObject bodyJson = JSONUtil.parseObj(bodyStr);
                boolean success = BooleanUtil.isTrue(bodyJson.getBool("status"));
                if (success) {
                    continue;
                }
            } catch (Exception e) {
                log.error("中通订单打印异常", e);
            }
            throw new ServiceException("中通订单打印失败");
        }
        //等待推送
        try {
            //TODO 等待时间？
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            throw new ServiceException("系统异常");
        }
        for (String waybillNo : waybillNos) {
            //从缓存中获取
            String rtn = redisCache.getCacheObject("ZTO-" + waybillNo);
            if (StrUtil.isEmpty(rtn)) {
                throw new ServiceException("中通订单打印失败，未能获取面单");
            }
            list.add(new ExpressPrintDTO(waybillNo, rtn));
        }
        return list;
    }

    @Override
    public boolean subscribeTrack(ExpressTrackSubReqDTO trackSubReq) {
        Assert.notNull(trackSubReq);
        Assert.notEmpty(trackSubReq.getExpressWaybillNo());
        Assert.notEmpty(trackSubReq.getDestinationContactPhoneNumber());
        ZtoSubTrackParam ztoSubTrackParam = new ZtoSubTrackParam();
        ztoSubTrackParam.setBillCode(trackSubReq.getExpressWaybillNo());
        ztoSubTrackParam.setMobilePhone(StrUtil.subSufByLength(trackSubReq.getDestinationContactPhoneNumber(), 4));
        ZopPublicRequest request = new ZopPublicRequest();
        request.setBody(JSONUtil.toJsonStr(ztoSubTrackParam));
        request.setUrl(gatewayUrl + TRACK_SUB_URI);
        request.setEncryptionType(EncryptionType.MD5);
        try {
            String bodyStr = client.execute(request);
            log.info("中通轨迹订阅返回信息: {}", bodyStr);
            JSONObject bodyJson = JSONUtil.parseObj(bodyStr);
            boolean success = BooleanUtil.isTrue(bodyJson.getBool("status"));
            if (success) {
                return true;
            }
        } catch (Exception e) {
            log.error("中通轨迹订阅异常", e);
        }
        log.warn("中通轨迹订阅失败: {}", trackSubReq);
        return false;
    }

    /**
     * 智能解析
     *
     * @param str
     * @return
     */
    public JSONObject structureNamePhoneAddress(String str) {
        ZopPublicRequest request = new ZopPublicRequest();
        JSONObject body = new JSONObject();
        body.set("address", str);
        request.setBody(body.toString());
        request.setUrl(gatewayUrl + STRUCTURE_ADDRESS_URI);
        request.setEncryptionType(EncryptionType.MD5);
        try {
            String bodyStr = client.execute(request);
            log.info("中通智能解析返回信息: {}", bodyStr);
            JSONObject bodyJson = JSONUtil.parseObj(bodyStr);
            boolean success = BooleanUtil.isTrue(bodyJson.getBool("status"));
            if (success) {
                return bodyJson.getJSONObject("result");
            }
        } catch (Exception e) {
            log.error("中通智能解析异常", e);
        }
        throw new ServiceException("中通智能解析失败");
    }

    /**
     * 查询所有行政区划
     *
     * @return
     */
    public List<ZtoRegion> getAllRegion() {
        ZopPublicRequest request = new ZopPublicRequest();
        request.addParam("a", "a");
        request.setUrl(gatewayUrl + QUERY_ALL_REGION_URI);
        request.setEncryptionType(EncryptionType.MD5);
        request.setReqTimeout(300000);
        try {
            String bodyStr = client.execute(request);
            JSONObject bodyJson = JSONUtil.parseObj(bodyStr);
            boolean success = BooleanUtil.isTrue(bodyJson.getBool("status"));
            if (success) {
                return bodyJson.getJSONArray("result").toList(ZtoRegion.class);
            }
        } catch (Exception e) {
            log.error("中通获取行政区划异常", e);
        }
        throw new ServiceException("中通获取行政区划失败");
    }

    private ZtoCreateOrderParam trans2CreateOrderReq(ExpressShipReqDTO expressShipReqDTO) {
        ZtoCreateOrderParam reqDTO = new ZtoCreateOrderParam();
        //合作模式 ，1：集团客户；2：非集团客户
        reqDTO.setPartnerType("2");
        //partnerType为1时，orderType：1：全网件 2：预约件。 partnerType为2时，orderType：1：全网件 2：预约件（返回运单号） 3：预约件（不返回运单号） 4：星联全网件
        reqDTO.setOrderType("1");
        //合作商订单号
        reqDTO.setPartnerOrderCode(expressShipReqDTO.getExpressReqNo());

        //账号信息
        ZtoCreateOrderParam.AccountInfo accountInfo = new ZtoCreateOrderParam.AccountInfo();
        reqDTO.setAccountInfo(accountInfo);
        //电子面单账号（partnerType为2，orderType传1,2,4时必传）
        accountInfo.setAccountId(accountId);
        //电子面单密码（测试环境传ZTO123）
        accountInfo.setAccountPassword(accountPassword);
        //单号类型:1.普通电子面单；74.星联电子面单；默认是1
        accountInfo.setType(1);
        //集团客户编码（partnerType传1时必传）
        accountInfo.setCustomerId(null);

        //发件人信息
        ZtoCreateOrderParam.SenderInfo senderInfo = new ZtoCreateOrderParam.SenderInfo();
        reqDTO.setSenderInfo(senderInfo);
        senderInfo.setSenderName(expressShipReqDTO.getOriginCountyName());
        senderInfo.setSenderPhone(expressShipReqDTO.getOriginContactPhoneNumber());
        senderInfo.setSenderProvince(expressShipReqDTO.getOriginProvinceName());
        senderInfo.setSenderCity(expressShipReqDTO.getOriginCityName());
        senderInfo.setSenderDistrict(expressShipReqDTO.getOriginCountyName());
        senderInfo.setSenderAddress(expressShipReqDTO.getOriginDetailAddress());

        //收件人信息
        ZtoCreateOrderParam.ReceiveInfo receiveInfo = new ZtoCreateOrderParam.ReceiveInfo();
        reqDTO.setReceiveInfo(receiveInfo);
        receiveInfo.setReceiverName(expressShipReqDTO.getDestinationContactName());
        receiveInfo.setReceiverPhone(expressShipReqDTO.getDestinationContactPhoneNumber());
        receiveInfo.setReceiverProvince(expressShipReqDTO.getDestinationProvinceName());
        receiveInfo.setReceiverCity(expressShipReqDTO.getDestinationCityName());
        receiveInfo.setReceiverDistrict(expressShipReqDTO.getDestinationCountyName());
        receiveInfo.setReceiverAddress(expressShipReqDTO.getDestinationDetailAddress());

        //货物信息
        reqDTO.setOrderItems(BeanUtil.copyToList(expressShipReqDTO.getOrderItems(),
                ZtoCreateOrderParam.OrderItem.class));

        return reqDTO;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        this.client = new ZopClient(appKey, appSecret);
    }
}
