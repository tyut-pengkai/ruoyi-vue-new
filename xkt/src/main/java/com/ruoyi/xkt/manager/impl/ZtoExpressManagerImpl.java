package com.ruoyi.xkt.manager.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.xkt.dto.express.ExpressShipReqDTO;
import com.ruoyi.xkt.enums.EExpressChannel;
import com.ruoyi.xkt.manager.ExpressManager;
import com.ruoyi.xkt.thirdpart.zto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author liangyq
 * @date 2025-04-15 15:45
 */
@Slf4j
@Component
public class ZtoExpressManagerImpl implements ExpressManager, InitializingBean {

    private static final String CREATE_ORDER_URI = "zto.open.createOrder";

    private static final String STRUCTURE_ADDRESS_URI = "zto.innovate.structureNamePhoneAddress";

    public static final String ORDER_PRINT_URI = "zto.open.order.print";

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
        return EExpressChannel.YTO;
    }

    @Override
    public String shipStoreOrder(ExpressShipReqDTO shipReqDTO) {
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
            JSONObject bodyJson = JSONUtil.parseObj(bodyStr);
            boolean success = bodyJson.getBool("status");
            if (success) {
                return bodyJson.getJSONObject("result").getStr("billCode");
            }

        } catch (Exception e) {
            log.error("中通订单创建异常", e);
        }
        throw new ServiceException("中通订单创建失败");
    }

    @Override
    public String printOrder(String waybillNo) {
        Assert.notEmpty(waybillNo);
        ZopPublicRequest request = new ZopPublicRequest();
        request.setBody(JSONUtil.toJsonStr(new ZtoPrintOrderParam(1, waybillNo, true)));
        request.setUrl(gatewayUrl + ORDER_PRINT_URI);
        request.setEncryptionType(EncryptionType.MD5);
        try {
            String bodyStr = client.execute(request);
            log.info("中通订单打印返回信息: {}", bodyStr);
            JSONObject bodyJson = JSONUtil.parseObj(bodyStr);
            boolean success = bodyJson.getBool("status");
            if (success) {
                //TODO 测试环境接口不通
                //等待推送
                TimeUnit.SECONDS.sleep(3);
                String rtn = redisCache.getCacheObject("ZTO_" + waybillNo);
                if (StrUtil.isNotEmpty(rtn)) {
                    return rtn;
                }
            }
        } catch (Exception e) {
            log.error("中通订单打印异常", e);
        }
        throw new ServiceException("中通订单打印失败");
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
            boolean success = bodyJson.getBool("status");
            if (success) {
                return bodyJson.getJSONObject("result");
            }
        } catch (Exception e) {
            log.error("中通智能解析异常", e);
        }
        throw new ServiceException("中通智能解析失败");
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
